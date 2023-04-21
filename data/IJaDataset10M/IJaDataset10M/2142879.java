package game;

import java.io.*;
import java.util.*;

/**
*Functional successor to Node which uses correct XML terminology; element names are case senstive and may not contains spaces.  <BR/>
*This class does NOT represent a fully spec compliant DOM XML parser. <BR/>
*<BR/>
*XmlElement a = new XmlElement("species");<BR/>
*a.addChild("name","Bulbasaur");<BR/>
*a.addChild("type","grass");<BR/>
*XmlElement b = new XmlElement("species");<BR/>
*b.addChild("name","Mew");<BR/>
*b.addChild("type","pyschic")<BR/>
*OutputStream out = new FileOutputStream("example.xml");<BR/>
*a.writeOn(out);<BR/>
*b.writeOn(out):<BR/>
*<BR/>
*produces:<BR/>
*&lt;species&gt;&lt;name&gt;Bulbasaur&lt;/name&gt;&lt;type&gt;grass&lt;/type&gt;&lt;/species&gt;&lt;species&gt;&lt;name&gt;Mew&lt;/name&gt;&lt;type&gt;pyschic&lt;/type&gt;&lt;/species&gt;<BR/>
*<BR/>
*@author rmacnak
*/
public class XmlElement {

    public XmlElement() {
        this("", "");
    }

    public XmlElement(String name) {
        this(name, "");
    }

    public XmlElement(String name, String content) {
        if (name != null) this.name = name;
        if (content != null) this.content = content;
    }

    /**
	*Returns the name of this node
	 */
    public String name() {
        return name;
    }

    /**
	*Sets the name of this node
	 */
    public void name(String x) {
        name = x;
    }

    /**
	*Returns the content of this node
	 */
    public String content() {
        return content;
    }

    /**
	*Sets the content of this node
	 */
    public void content(String x) {
        content = x;
    }

    /**
	*Returns an enumeration of all children of this node
	 */
    public List<XmlElement> children() {
        return children;
    }

    /**
	*Adds a child with the given name and content.
	 */
    public XmlElement addChild(String name, String contents) {
        return addChild(new XmlElement(name, contents));
    }

    /**
	*Adds a child
	 */
    public XmlElement addChild(XmlElement n) {
        children.add(n);
        n.parent = this;
        return n;
    }

    /**
	*Returns the first found child with the given name
	 */
    public XmlElement child(String childName) {
        for (XmlElement n : children) {
            if (n.name.equals(childName)) return n;
        }
        return null;
    }

    /**
	*Returns an enumeration of all children with the given name
	 */
    public List<XmlElement> children(String childName) {
        List<XmlElement> matches = new ArrayList<XmlElement>();
        for (XmlElement n : children) {
            if (n.name.equals(childName)) matches.add(n);
        }
        return matches;
    }

    /**
	*Returns the content of the first child with the given name
	 */
    public String contentOf(String childName) {
        XmlElement n = child(childName);
        if (n != null) return n.content();
        return "";
    }

    public int icontentOf(String childName) {
        return new Integer(contentOf(childName));
    }

    public double dcontentOf(String childName) {
        return new Double(contentOf(childName));
    }

    private String encoded() {
        String x = "";
        x += "<" + encode(name) + ">" + encode(content);
        for (XmlElement n : children) x += n.encoded();
        x += "</" + encode(name) + ">";
        return x;
    }

    /**
	*Writes this stanza to the given stream
	 */
    public void writeOn(OutputStream os) throws IOException {
        os.write(encoded().getBytes());
        os.flush();
    }

    /**
	*Writes this stanza to the given stream, ignoring any errors
	 */
    public void tryWriteOn(OutputStream os) {
        try {
            writeOn(os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	*Returns an element containing all stanzas as children
	 */
    public static XmlElement documentRootFrom(InputStream is) throws IOException {
        XmlElement root = new XmlElement("root");
        for (XmlElement n : parseAllFrom(is)) root.addChild(n);
        return root;
    }

    /**
	*Parses and returns all stanzas from the stream, an empty collection if failure before any parsed
	 */
    public static List<XmlElement> parseAllFrom(InputStream is) {
        ArrayList<XmlElement> elements = new ArrayList<XmlElement>();
        try {
            while (true) elements.add(parseFrom(is));
        } catch (IOException e) {
        }
        return elements;
    }

    /**
	*Parse and return the next stanza, useful for streaming apps
	 */
    public static XmlElement parseFrom(InputStream is) throws IOException {
        XmlElement top = new XmlElement();
        upTo(is, '<');
        top.name(decode(upTo(is, '>')).split(" ")[0]);
        while (true) {
            String con = decode(upTo(is, '<'));
            top.appendContent(con);
            String name = decode(upTo(is, '>'));
            name = name.split(" ")[0];
            if (name.equals("")) {
                throw new IOException("Format error: empty tag");
            } else if (name.startsWith("/")) {
                name = name.substring(1);
                if (name.equals(top.name())) {
                    XmlElement parent = top.parent;
                    if (parent == null) return top; else top = parent;
                } else {
                    throw new IOException("Format error: Premature close of " + name);
                }
            } else if (name.endsWith("/")) {
                name = name.substring(0, name.length() - 1);
                top.addChild(name, "");
            } else {
                top = top.addChild(name, "");
            }
        }
    }

    private String name = "";

    private String content = "";

    private List<XmlElement> children = new ArrayList<XmlElement>();

    private XmlElement parent = null;

    private XmlElement(XmlElement parent) {
        this.parent = parent;
    }

    private void appendContent(String c) {
        content += c;
    }

    private XmlElement parent() {
        return parent;
    }

    public String toString() {
        String x = "";
        x += name + "(c=" + content + ")(p=" + (parent == null ? "null" : parent.name) + ")\n";
        for (XmlElement n : children) x += "\t" + n.toString() + "";
        return x;
    }

    private static String upTo(InputStream is, char end) throws IOException {
        byte[] bytes = new byte[1024];
        int pos = 0;
        int b;
        while ((b = is.read()) != -1) {
            if (b == (int) end) {
                return new String(bytes, 0, pos);
            }
            bytes[pos] = (byte) b;
            pos++;
        }
        return new String(bytes, 0, pos);
    }

    private static String encode(String x) {
        x = x.replace("&", "&amp;");
        x = x.replace("<", "&lt;");
        x = x.replace(">", "&gt;");
        return x;
    }

    private static String decode(String x) {
        x = x.replace("&lt;", "<");
        x = x.replace("&gt;", ">");
        x = x.replace("&amp;", "&");
        return x;
    }
}
