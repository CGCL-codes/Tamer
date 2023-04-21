package javajabberc;

import java.io.*;
import java.util.Vector;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Stack;

public class XMLStorageTag {

    public String name = null;

    public Vector tags = null;

    public HashMap attribs = null;

    public String data = null;

    private static boolean debug = false;

    public String tab = "    ";

    /**
	 * Creates an empty XMLStorageTag.
	 */
    public XMLStorageTag() {
    }

    /**
	 * Creates an XMLStorageTag with the given name.
	 */
    public XMLStorageTag(String name) {
        this.name = name;
    }

    /**
	 * Creates an XMLStorageTag with the given name, which stores the given data.
	 */
    public XMLStorageTag(String name, String data) {
        this.name = name;
        this.data = data;
    }

    public XMLStorageTag(XMLStorage xmlStorage) {
        StringBuffer nameBuffer = new StringBuffer(257);
        StringBuffer ident = new StringBuffer(257);
        StringBuffer value = new StringBuffer(257);
        StringBuffer buffer = xmlStorage.buffer;
        int state = 0;
        int start = xmlStorage.pos;
        int tmpPos;
        boolean error = false;
        boolean done = false;
        char c;
        XMLStorageTag child = null;
        boolean emptyTag = false;
        boolean headerTag = false;
        if (debug) {
            System.out.println("Creating a new XMLStorageTag from stream.");
            System.out.println(xmlStorage.buffer.substring(xmlStorage.pos));
        }
        while (!error && !done) {
            c = buffer.charAt(xmlStorage.pos);
            switch(state) {
                case 0:
                    if (debug) System.out.println("State: 0:'" + c + '\'');
                    switch(c) {
                        case '<':
                            state = 1;
                            if (nameBuffer.length() > 0) nameBuffer.delete(0, nameBuffer.length());
                            break;
                        case '\r':
                        case '\n':
                        case ' ':
                        case '\t':
                            break;
                        default:
                            System.out.println("ERROR!!");
                            error = true;
                    }
                    break;
                case 1:
                    if (debug) System.out.println("State: 1");
                    switch(c) {
                        case '<':
                        case '"':
                        case '\'':
                        case '=':
                            error = true;
                            break;
                        case '!':
                            state = 10;
                            break;
                        case '>':
                            state = 7;
                            this.name = nameBuffer.toString();
                            if (nameBuffer.length() > 0) nameBuffer.delete(0, nameBuffer.length());
                            break;
                        case '/':
                            state = 2;
                            break;
                        case '?':
                            if (!headerTag) {
                                headerTag = true;
                                emptyTag = true;
                                nameBuffer.append('?');
                            } else {
                                this.name = nameBuffer.toString();
                                done = true;
                                xmlStorage.pos++;
                            }
                            break;
                        case ' ':
                            state = 3;
                            if (ident.length() > 0) ident.delete(0, ident.length());
                            break;
                        default:
                            nameBuffer.append(c);
                    }
                    break;
                case 2:
                    if (debug) System.out.println("State: 2");
                    switch(c) {
                        case '>':
                            name = nameBuffer.toString();
                            if (nameBuffer.length() > 0) nameBuffer.delete(0, nameBuffer.length());
                            emptyTag = true;
                            done = true;
                            break;
                        default:
                            error = true;
                    }
                    break;
                case 3:
                    if (debug) System.out.println("State: 3");
                    switch(c) {
                        case '<':
                        case '>':
                        case '"':
                        case '\'':
                            error = true;
                            break;
                        case '/':
                            state = 2;
                            break;
                        case '?':
                            state = 2;
                            break;
                        case '=':
                            state = 4;
                            break;
                        case ' ':
                            state = 3;
                            break;
                        default:
                            ident.append(c);
                    }
                    break;
                case 4:
                    if (debug) System.out.println("State: 4");
                    switch(c) {
                        case '"':
                        case '\'':
                            if (value.length() > 0) value.delete(0, value.length());
                            if (c == '"') state = 5; else state = 6;
                            break;
                        case ' ':
                            state = 4;
                            break;
                        default:
                            error = true;
                    }
                    break;
                case 5:
                    if (debug) System.out.println("State: 5");
                    switch(c) {
                        case '"':
                            addAttrib(ident.toString(), value.toString());
                            if (ident.length() > 0) ident.delete(0, ident.length());
                            if (value.length() > 0) value.delete(0, value.length());
                            state = 1;
                            break;
                        default:
                            value.append(c);
                    }
                    break;
                case 6:
                    if (debug) System.out.println("State: 6");
                    switch(c) {
                        case '\'':
                            addAttrib(ident.toString(), value.toString());
                            if (ident.length() > 0) ident.delete(0, ident.length());
                            if (value.length() > 0) value.delete(0, value.length());
                            state = 1;
                            break;
                        default:
                            value.append(c);
                    }
                    break;
                case 7:
                    if (debug) System.out.println("State: 7:" + c);
                    switch(c) {
                        case '<':
                            state = 8;
                            break;
                        case '\r':
                        case '\n':
                            break;
                        default:
                            value.append(c);
                    }
                    break;
                case 8:
                    if (debug) System.out.println("State: 8");
                    switch(c) {
                        case '/':
                            this.data = value.toString();
                            if (this.data.trim().length() < 1) {
                                this.data = null;
                                emptyTag = true;
                            }
                            if (nameBuffer.length() > 0) nameBuffer.delete(0, nameBuffer.length());
                            state = 9;
                            break;
                        case '>':
                        case '"':
                        case '\'':
                        case ' ':
                            error = true;
                            break;
                        default:
                            xmlStorage.pos--;
                            xmlStorage.buffer.delete(start, xmlStorage.pos);
                            xmlStorage.pos = start;
                            child = new XMLStorageTag(xmlStorage);
                            if (child == null) error = true; else addTag(child);
                            xmlStorage.pos--;
                            state = 7;
                    }
                    break;
                case 9:
                    if (debug) System.out.println("State: 9");
                    switch(c) {
                        case '=':
                        case '\'':
                        case '"':
                        case ' ':
                            error = true;
                            break;
                        case '>':
                            done = true;
                            break;
                        default:
                            nameBuffer.append(c);
                    }
                    break;
                case 10:
                    if (debug) System.out.println("State: 10");
                    switch(c) {
                        case '-':
                            state = 11;
                            break;
                        default:
                            error = true;
                    }
                    break;
                case 11:
                    if (debug) System.out.println("State: 11");
                    switch(c) {
                        case '-':
                            state = 12;
                            break;
                        default:
                            error = true;
                    }
                    break;
                case 12:
                    if (debug) System.out.println("State: 12");
                    switch(c) {
                        case '-':
                            state = 13;
                    }
                    break;
                case 13:
                    if (debug) System.out.println("State: 13");
                    switch(c) {
                        case '-':
                            state = 14;
                            break;
                        default:
                            state = 12;
                    }
                    break;
                case 14:
                    if (debug) System.out.println("State: 14");
                    switch(c) {
                        case '>':
                            xmlStorage.pos++;
                            xmlStorage.buffer.delete(start, xmlStorage.pos);
                            xmlStorage.pos = start;
                            while (xmlStorage.pos <= xmlStorage.buffer.length()) {
                                c = xmlStorage.buffer.charAt(xmlStorage.pos);
                                if ((c == '\r') || (c == '\n') || (c == ' ') || (c == '\t')) xmlStorage.buffer.deleteCharAt(xmlStorage.pos); else break;
                                if (xmlStorage.isBufferEmpty()) break;
                            }
                            xmlStorage.pos--;
                            state = 0;
                            break;
                        case '-':
                            break;
                        default:
                            state = 12;
                    }
                    break;
            }
            xmlStorage.pos++;
            if (xmlStorage.pos >= (xmlStorage.buffer.length() - 1)) {
                if (!xmlStorage.stringMode && !xmlStorage.readData()) {
                    xmlStorage.lastError = "Out of data.";
                    error = true;
                }
            }
        }
        if (done) {
            if (debug) System.out.println("Parsed tag.");
            if (!emptyTag) {
                if (debug) {
                    System.out.println("Not an empty tag.");
                    System.out.println("name:       " + name);
                    System.out.println("nameBuffer: " + nameBuffer.toString());
                }
                if (name.compareTo(nameBuffer.toString()) != 0) error = true; else done = true;
            } else {
                done = true;
            }
            if (xmlStorage.stringMode && !error && done) return;
            xmlStorage.buffer.delete(start, xmlStorage.pos);
            xmlStorage.pos = start;
            while (xmlStorage.pos <= xmlStorage.buffer.length()) {
                c = xmlStorage.buffer.charAt(xmlStorage.pos);
                if ((c == '\r') || (c == '\n') || (c == ' ') || (c == '\t')) xmlStorage.buffer.deleteCharAt(xmlStorage.pos); else break;
                if (xmlStorage.isBufferEmpty()) break;
            }
        } else if (error) {
            name = null;
            xmlStorage.lastError = "XML parser error: " + xmlStorage.buffer.substring(start);
        }
    }

    public void addAttrib(String name, String value) {
        if (attribs == null) attribs = new HashMap();
        attribs.put(name, value);
    }

    public void addTag(XMLStorageTag tag) {
        if (tags == null) tags = new Vector();
        tags.add(tag);
    }

    public boolean addTag(String rawXML) {
        XMLStorage xmlSS = new XMLStorage(rawXML);
        XMLStorageTag newTag = new XMLStorageTag(xmlSS);
        if (newTag == null) return (false);
        addTag(newTag);
        return (true);
    }

    public boolean addUniqueTag(String rawXML) {
        XMLStorage xmlSS = new XMLStorage(rawXML);
        XMLStorageTag newTag = new XMLStorageTag(xmlSS);
        if (newTag == null) return (false);
        addUniqueTag(newTag);
        return (true);
    }

    public void addUniqueTag(XMLStorageTag tag) {
        int i;
        XMLStorageTag current = null;
        if (tags != null) {
            i = 0;
            while (i < tags.size()) {
                current = (XMLStorageTag) tags.get(i);
                if (current.name.equals(tag.name)) tags.remove(i); else i++;
            }
        }
        addTag(tag);
    }

    public XMLStorageTag getTag(String name) {
        if (name == null) return (null);
        Iterator it = tagsIterator(name);
        if (it.hasNext()) return ((XMLStorageTag) it.next()); else return (null);
    }

    public String getAttrib(String name) {
        if (attribs == null) return (null);
        if (attribs.containsKey(name)) return ((String) attribs.get(name)); else return (null);
    }

    public boolean getBooleanAttrib(String name, boolean def) {
        String tmp;
        if (attribs == null) return (def);
        if (attribs.containsKey(name)) {
            tmp = ((String) attribs.get(name)).toLowerCase();
            if (tmp.compareTo("true") == 0) return (true); else if (tmp.compareTo("false") == 0) return (false);
            return (def);
        }
        return (def);
    }

    public int getIntAttrib(String name, int def) {
        int result;
        if (attribs == null) return (def);
        if (attribs.containsKey(name)) {
            try {
                result = Integer.parseInt((String) attribs.get(name));
            } catch (Exception e) {
                return (def);
            }
            return (result);
        }
        return (def);
    }

    public long getLongAttrib(String name, long def) {
        long result;
        if (attribs == null) return (def);
        if (attribs.containsKey(name)) {
            try {
                result = Long.parseLong((String) attribs.get(name));
            } catch (Exception e) {
                return (def);
            }
            return (result);
        }
        return (def);
    }

    public float getFloatAttrib(String name, float def) {
        float result;
        if (attribs == null) return (def);
        if (attribs.containsKey(name)) {
            try {
                result = Float.parseFloat((String) attribs.get(name));
            } catch (Exception e) {
                return (def);
            }
            return (result);
        }
        return (def);
    }

    public double getDoubleAttrib(String name, double def) {
        double result;
        if (attribs == null) return (def);
        if (attribs.containsKey(name)) {
            try {
                result = Double.parseDouble((String) attribs.get(name));
            } catch (Exception e) {
                return (def);
            }
            return (result);
        }
        return (def);
    }

    public Iterator tagsIterator() {
        return (new XMLStorageTagIterator(tags));
    }

    public Iterator tagsIterator(String name) {
        return (new XMLStorageTagIterator(tags, name));
    }

    public boolean write(Writer out) {
        char[] buf;
        if (out == null) return (false);
        buf = getXML(false).toCharArray();
        try {
            out.write(buf, 0, buf.length);
        } catch (IOException ioe) {
            return (false);
        }
        return (true);
    }

    /**
	 * Returns a XML representation of the tag.
	 */
    public String getXML(boolean lineSplitTags) {
        StringBuffer buffer = new StringBuffer(1024);
        Iterator keysIt, valuesIt, tagsIt;
        buffer.append('<');
        buffer.append(name);
        if (attribs != null) {
            keysIt = attribs.keySet().iterator();
            valuesIt = attribs.values().iterator();
            while (keysIt.hasNext() && valuesIt.hasNext()) {
                buffer.append(' ');
                buffer.append((String) keysIt.next());
                buffer.append("=\"");
                buffer.append(cleanString((String) valuesIt.next()));
                buffer.append('"');
            }
        }
        if ((data == null) && (tags == null)) {
            buffer.append("/>");
            if (lineSplitTags) buffer.append('\n');
        } else {
            buffer.append(">");
            if (lineSplitTags) buffer.append('\n');
            if (data != null) buffer.append(cleanString(data));
            if (tags != null) {
                tagsIt = tags.iterator();
                while (tagsIt.hasNext()) buffer.append(((XMLStorageTag) tagsIt.next()).getXML(lineSplitTags));
            }
            buffer.append("</");
            buffer.append(name);
            buffer.append('>');
            if (lineSplitTags) buffer.append('\n');
        }
        return (buffer.toString());
    }

    /**
	 * Returns a string that is safe to store in an XML document.
	 */
    public static String cleanString(String str) {
        StringBuffer buffer = new StringBuffer(str.length() * 2);
        int pos = 0;
        char c;
        buffer.insert(0, str);
        while (pos < buffer.length()) {
            c = buffer.charAt(pos);
            switch(c) {
                case '<':
                    buffer.deleteCharAt(pos);
                    buffer.insert(pos, "&lt;");
                    pos += 4;
                    break;
                case '>':
                    buffer.deleteCharAt(pos);
                    buffer.insert(pos, "&gt;");
                    pos += 4;
                    break;
                case '"':
                    buffer.deleteCharAt(pos);
                    buffer.insert(pos, "&quot;");
                    pos += 6;
                    break;
                case '\'':
                    buffer.deleteCharAt(pos);
                    buffer.insert(pos, "&apos;");
                    pos += 6;
                    break;
                case '&':
                    buffer.insert(pos + 1, "amp;");
                    pos += 4;
                    break;
            }
            pos++;
        }
        return (buffer.toString());
    }

    /**
	 * Outputs the contents of the tag to the given PrintStream.
	 */
    public void showContents(PrintStream out) {
        if (out == null) return;
        showContentsIndent(out, 0);
    }

    /**
	 * Does the actual work of outputting the tag data, correctly indented.
	 */
    private void showContentsIndent(PrintStream out, int indentLevel) {
        String indent = "";
        Iterator keysIt, valuesIt, tagsIt;
        for (int i = 0; i < indentLevel; i++) indent += tab;
        if (name != null) out.println(indent + name + '{'); else out.println(indent + "!!null!! {");
        indent += tab;
        if (data != null) out.println(indent + "Data: " + data);
        if (attribs != null) {
            keysIt = attribs.keySet().iterator();
            valuesIt = attribs.values().iterator();
            while (keysIt.hasNext() && valuesIt.hasNext()) out.println(indent + '>' + (String) keysIt.next() + " : " + (String) valuesIt.next());
        }
        if ((tags != null) && (tags.size() > 0)) {
            tagsIt = tags.iterator();
            while (tagsIt.hasNext()) ((XMLStorageTag) tagsIt.next()).showContentsIndent(out, indentLevel + 1);
        }
        out.println(indent + '}');
    }
}
