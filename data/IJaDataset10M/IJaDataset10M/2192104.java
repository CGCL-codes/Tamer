package gnu.rhuelga.cirl.htmllib;

import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.*;

class Tag {

    static final String HTML_COMMENT_TAG = "!--";

    static final int OPEN_TAG_CHAR_LEFT = '<';

    static final int OPEN_TAG_CHAR_RIGHT = '>';

    static final int CLOSE_TAG_CHAR = '/';

    static final String COMMENT_ATTRIBUTE_KEY = "COMMENT";

    private String name;

    private boolean open;

    Map attributes;

    Tag() {
        attributes = new HashMap();
        open = true;
    }

    Tag(String tag) {
        this();
        name = tag;
    }

    public String getName() {
        return name;
    }

    public boolean isOpen() {
        return open;
    }

    static final boolean isSpace(int c) {
        return Character.isWhitespace((char) c);
    }

    public boolean equals(Tag t) {
        return name.equals(t.name);
    }

    public boolean equals(String str) {
        return name.equals(str);
    }

    void print() {
        Object ob;
        Iterator iter;
        Set set;
        System.out.println("Tag name: " + name + " open - " + open);
        set = attributes.keySet();
        iter = set.iterator();
        while (iter.hasNext()) {
            ob = iter.next();
            System.out.println("\t" + ob + "=" + attributes.get(ob));
        }
    }

    static Object getTag(HTMLReader in) throws PrematureEndHTMLException, IOException {
        if (isTag(in)) {
            Tag t = new Tag();
            t.readTag(in);
            if (t.isComment()) {
                return "<" + t.name + " " + t.attributes.get(COMMENT_ATTRIBUTE_KEY) + ">";
            }
            return t;
        } else {
            return readString(in);
        }
    }

    static boolean isTag(HTMLReader in) throws PrematureEndHTMLException, IOException {
        if (in.getLastChar() == OPEN_TAG_CHAR_LEFT) {
            return true;
        }
        while (isSpace(in.getChar())) {
        }
        if (in.getLastChar() == OPEN_TAG_CHAR_LEFT) {
            return true;
        } else {
            return false;
        }
    }

    void readTag(HTMLReader i) throws PrematureEndHTMLException, IOException {
        StringBuffer strbuf = new StringBuffer();
        ;
        attributes = new HashMap();
        if (i.getChar() == CLOSE_TAG_CHAR) {
            open = false;
            while (isSpace(i.getChar())) ;
        } else {
            open = true;
        }
        do {
            strbuf.append((char) (i.getLastChar()));
            i.getChar();
        } while ((i.getLastChar() != OPEN_TAG_CHAR_RIGHT) && (!(isSpace(i.getLastChar()))));
        name = strbuf.toString().toUpperCase();
        if (isComment()) {
            attributes.put(COMMENT_ATTRIBUTE_KEY, comment(i));
            return;
        }
        if (i.getLastChar() != OPEN_TAG_CHAR_RIGHT) {
            readAttributes(i);
        }
    }

    String getToken(HTMLReader i, String delim) throws PrematureEndHTMLException, IOException {
        StringBuffer strbuf = new StringBuffer();
        while (isSpace(i.getLastChar())) {
            i.getChar();
        }
        while (delim.indexOf(i.getLastChar()) == -1) {
            strbuf.append((char) (i.getLastChar()));
            i.getChar();
        }
        return strbuf.toString();
    }

    void readAttributes(HTMLReader i) throws PrematureEndHTMLException, IOException {
        String key;
        String content;
        while (true) {
            key = getToken(i, ">= \t\n").toUpperCase();
            while (isSpace(i.getLastChar())) {
                i.getChar();
            }
            if (i.getLastChar() == OPEN_TAG_CHAR_RIGHT) {
                if (key.length() != 0) {
                    attributes.put(key, "");
                }
                return;
            }
            if ((char) (i.getLastChar()) == '=') {
                while (isSpace(i.getChar())) ;
                if (i.getLastChar() == OPEN_TAG_CHAR_RIGHT) {
                    return;
                }
                if ((char) (i.getLastChar()) == '\"') {
                    i.getChar();
                    content = getToken(i, "\"");
                    i.getChar();
                } else {
                    content = getToken(i, "> \t\n");
                }
                attributes.put(key, content);
            }
        }
    }

    static String readString(HTMLReader i) throws PrematureEndHTMLException, IOException {
        StringBuffer strbuf = new StringBuffer();
        do {
            strbuf.append((char) (i.getLastChar()));
        } while (i.getChar() != OPEN_TAG_CHAR_LEFT);
        return strbuf.toString();
    }

    boolean isComment(String str) {
        return str.startsWith(HTML_COMMENT_TAG);
    }

    public boolean isComment() {
        return isComment(name);
    }

    String comment(HTMLReader in) throws PrematureEndHTMLException, IOException {
        StringBuffer strbuf = new StringBuffer();
        while (true) {
            strbuf.append((char) (in.getChar()));
            if ((char) (in.getLastChar()) == '-') {
                strbuf.append((char) (in.getChar()));
                if ((char) (in.getLastChar()) == '-') {
                    if ((char) (in.getChar()) == '>') {
                        return strbuf.toString();
                    } else {
                        strbuf.append((char) (in.getLastChar()));
                    }
                }
            }
        }
    }

    void write(Writer out) throws IOException {
        out.write("<" + name);
        Object ob;
        Iterator iter;
        Set set;
        set = attributes.keySet();
        iter = set.iterator();
        while (iter.hasNext()) {
            ob = iter.next();
            out.write(" " + ob + "=\"" + attributes.get(ob) + "\"");
        }
        out.write(">");
    }

    void writeClose(Writer out) throws IOException {
        out.write("</" + name + ">");
    }
}
