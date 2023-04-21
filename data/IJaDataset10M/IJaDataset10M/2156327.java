package org.nakedobjects.doclet.xml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Stack;
import com.sun.javadoc.SeeTag;
import com.sun.javadoc.Tag;

public class CommentProcessor {

    private static class Element {

        private boolean preserveWhiteSpace = false;

        private String docbookElement;

        private String docbookStartingTag;

        private String docbookEndingTag;

        private boolean canBeOpenBlock;

        Element(String element, boolean canBeOpenBlock) {
            docbookElement = element;
            this.canBeOpenBlock = canBeOpenBlock;
            docbookStartingTag = null;
            docbookEndingTag = null;
        }

        Element(String startingTag, String endingTag, boolean canBeOpenBlock) {
            this.canBeOpenBlock = canBeOpenBlock;
            docbookElement = null;
            docbookStartingTag = startingTag;
            docbookEndingTag = endingTag;
        }

        String startingTag() {
            if (docbookElement == null) {
                return docbookStartingTag;
            } else {
                return "<" + docbookElement + ">";
            }
        }

        String endingTag() {
            if (docbookElement == null) {
                return docbookEndingTag;
            } else {
                return "</" + docbookElement + ">";
            }
        }

        public Element preserveWhiteSpace(boolean flag) {
            preserveWhiteSpace = flag;
            return this;
        }
    }

    private static HashMap<String, Element> elementMapping = new HashMap<String, Element>();

    static {
        elementMapping.put("p", new Element("para", true));
        elementMapping.put("ol", new Element("orderedlist", true));
        elementMapping.put("pre", new Element("programlisting", true).preserveWhiteSpace(true));
        elementMapping.put("ul", new Element("<itemizedlist mark=\"opencircle\">", "</itemizedlist>", true));
        elementMapping.put("li", new Element("<listitem><para>", "</para></listitem>", true));
        elementMapping.put("i", new Element("emphasis", false));
        elementMapping.put("tt", new Element("literal", false));
    }

    private final BufferedWriter writer;

    private final Stack<String> stack = new Stack<String>();

    private boolean preserveWhiteSpace;

    public CommentProcessor(BufferedWriter writer) {
        this.writer = writer;
    }

    void processComment(Tag[] tags) throws IOException {
        preserveWhiteSpace = false;
        for (Tag tag : tags) {
            if (tag instanceof SeeTag) {
                String className = ((SeeTag) tag).referencedClassName();
                if (className != null) {
                    writer.append("<classname>");
                    writer.append(className);
                    writer.append("</classname>");
                }
                String methodName = ((SeeTag) tag).referencedMemberName();
                if (methodName != null) {
                    if (className != null) {
                        writer.append(".");
                    }
                    writer.append("<methodname>");
                    writer.append(methodName);
                    writer.append("</methodname>");
                    writer.append("(");
                    writer.append(")");
                }
                preserveWhiteSpace = true;
            } else if (tag.kind().equals("Text")) {
                processComment(tag.text());
            }
        }
        while (stack.size() > 0) {
            endTag(stack.peek());
        }
    }

    private void processComment(String commentText) throws IOException {
        StringReader reader = new StringReader(commentText);
        boolean hasSpace = true;
        int c = reader.read();
        while (c != -1) {
            if (c == '<') {
                tag(reader);
                c = reader.read();
            } else if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                if (preserveWhiteSpace || !hasSpace) {
                    writer.write(c);
                    hasSpace = true;
                }
                c = reader.read();
            } else if (c == '&') {
                hasSpace = false;
                writer.write("&amp;");
                c = reader.read();
            } else {
                if (stack.size() == 0) {
                    stack.push("p");
                    writer.write("<para>");
                }
                hasSpace = false;
                writer.append((char) c);
                c = reader.read();
            }
        }
    }

    private void tag(StringReader reader) throws IOException {
        String tag = readTag(reader);
        if (tag.startsWith("/")) {
            if (tag.equals("/" + stack.peek())) {
                String endTag = tag.substring(1);
                endTag(endTag);
            } else {
                writer.append("&lt;" + tag + "&gt;");
            }
        } else {
            startTag(tag);
        }
    }

    private void startTag(String tag) throws IOException {
        Element element = elementMapping.get(tag);
        if (element != null) {
            if (element.canBeOpenBlock && stack.size() > 0 && stack.peek().equals("p")) {
                stack.pop();
                writer.append("</para>");
            }
            writer.append(element.startingTag());
            stack.push(tag);
            preserveWhiteSpace = element.preserveWhiteSpace;
        } else {
            stack.push(tag);
            writer.append("&lt;" + tag + "&gt;");
        }
    }

    private void endTag(String tag) throws IOException {
        Element element = elementMapping.get(tag);
        if (element != null) {
            writer.append(element.endingTag());
            stack.pop();
            if (element.preserveWhiteSpace) {
                preserveWhiteSpace = false;
            }
        } else {
            writer.append("&lt;/" + tag + "&gt;");
            stack.pop();
        }
    }

    private String readTag(StringReader reader) throws IOException {
        return readTo(reader, '>').trim().toLowerCase();
    }

    private String readTo(StringReader reader, int endChar) throws IOException {
        return readTo(reader, endChar, endChar);
    }

    private String readTo(StringReader reader, int endChar1, int endChar2) throws IOException {
        int c = reader.read();
        String block = "";
        while (c != -1 && c != endChar1 && c != endChar2) {
            block += (char) c;
            c = reader.read();
        }
        return block;
    }
}
