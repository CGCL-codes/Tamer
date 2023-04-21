package com.aragost.html;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author  Jan Sorensen
 * @version 
 */
public class Link extends HtmlElement {

    /** Holds value of property path. */
    private String path;

    /** Holds value of property parameters. */
    private List parameters = new ArrayList();

    private String target;

    /** Creates new Link */
    public Link(String text, String path) {
        this(new Text(text), path);
    }

    /** Creates new Link */
    public Link(Element body, String path) {
        super(new Font(body));
        ((Font) getBody()).setColor("blue");
        this.path = path;
    }

    /** Getter for property path.
   * @return Value of property path.
   */
    public String getPath() {
        return this.path;
    }

    /** Setter for property path.
   * @param path New value of property path.
   */
    public void setPath(String path) {
        this.path = path;
    }

    /** Getter for property target.
   * @return Value of property target.
   */
    public String getTarget() {
        return this.target;
    }

    /** Setter for property target.
   * @param path New value of property target.
   */
    public void setTarget(String target) {
        this.target = target;
    }

    public void addParameter(String key, String value) {
        parameters.add(encode(key) + "=" + encode(value));
    }

    private static String encode(String s) {
        return java.net.URLEncoder.encode(s);
    }

    public String getTag() {
        return "a";
    }

    public void writeAttributes(HtmlWriter writer) {
        boolean first = true;
        writer.print(" href=");
        writer.print(getUrl());
        if (getTarget() != null) {
            writer.print(" target=\"");
            writer.print(getTarget());
            writer.print("\"");
        }
        super.writeAttributes(writer);
    }

    /**
   * Return the URL of this Link element.
   * @return Stirng   */
    public String getUrl() {
        StringBuffer buffer = new StringBuffer();
        boolean first = true;
        buffer.append(getPath());
        for (Iterator i = getParameters().iterator(); i.hasNext(); ) {
            buffer.append(first ? '?' : '&');
            first = false;
            buffer.append((String) i.next());
        }
        return buffer.toString();
    }

    /** Getter for property parameters.
   * @return Value of property parameters.
   */
    public List getParameters() {
        return parameters;
    }

    public boolean equals(Object obj) {
        if (obj.getClass() != Link.class) return false;
        Link lnk = (Link) obj;
        if (!getPath().equals(lnk.getPath())) return false;
        if (!getBody().equals(lnk.getBody())) return false;
        List args = getParameters();
        int size = args.size();
        if (size != lnk.getParameters().size()) return false;
        return args.containsAll(lnk.getParameters());
    }

    public int hashCode() {
        return getPath().hashCode() + getBody().hashCode();
    }
}
