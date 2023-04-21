package org.googlecode.horseshoe.formatters;

import org.googlecode.horseshoe.lang.OutputStreamWriterProxy;
import org.googlecode.horseshoe.lang.TextFormatter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class HtmlResponseFormatter implements IHorseshoeResponseFormatter {

    private String uri;

    private OutputStreamWriterProxy _writer;

    /**
   * @param uri
   * @param stream
   */
    public HtmlResponseFormatter(String uri, OutputStream stream) {
        this.uri = uri;
        _writer = new OutputStreamWriterProxy(new OutputStreamWriter(stream));
    }

    /**
   * @see org.googlecode.horseshoe.formatters.IHorseshoeResponseFormatter#getContentType()
   */
    public String getContentType() {
        return "text/HTML";
    }

    /**
   * @see org.googlecode.horseshoe.formatters.IHorseshoeResponseFormatter#startDocument()
   */
    public void startDocument() {
    }

    /**
   * @see org.googlecode.horseshoe.formatters.IHorseshoeResponseFormatter#endDocument()
   */
    public void endDocument() {
    }

    /**
   * @see org.googlecode.horseshoe.formatters.IHorseshoeResponseFormatter#writeEntityList(java.lang.String,
   *      java.lang.Class<?>[])
   */
    public void writeEntityList(String setName, Class<?>[] types) {
        _writer.write(String.format("<table><tr><th>%1$s</th></tr>", setName));
        for (Class<?> type : types) {
            _writer.write(String.format("<tr><td><a href='%1$s/%2$s'>%2$s</a></td></tr>", getBaseUri(), TextFormatter.pluralize(type.getName())));
        }
        _writer.write("</table>");
    }

    /**
   * @see org.googlecode.horseshoe.formatters.IHorseshoeResponseFormatter#startList(java.lang.String)
   */
    public void startList(String listName) {
        _writer.write(String.format("<table><tr><th>%1$s</th></tr>", listName));
    }

    /**
   * @see org.googlecode.horseshoe.formatters.IHorseshoeResponseFormatter#endList()
   */
    public void endList() {
        _writer.write("</table>");
    }

    /**
   * @see org.googlecode.horseshoe.formatters.IHorseshoeResponseFormatter#writeList(java.lang.String, java.lang.Class, java.lang.Object, java.lang.String)
   */
    public void writeList(String listName, Class<?> type, Object id, String idProperty) {
        _writer.write(String.format("<table><tr><td><a href='%1$s/%2$s[%3$s]/%4$s'>%4$s</a></td></tr></table>", getBaseUri(), TextFormatter.pluralize(type.getName()), id, listName));
    }

    /**
   * @see org.googlecode.horseshoe.formatters.IHorseshoeResponseFormatter#startElement(java.lang.String, java.lang.Class, java.lang.Object, java.lang.String)
   */
    public void startElement(String elementName, Class<?> type, Object id, String idProperty) {
        _writer.write("<tr><td>{3} = <a href='%1$s/%2$s[%3$s]'>%3$s</a>", getBaseUri(), TextFormatter.pluralize(type.getName()), id, elementName);
    }

    /**
   * @see org.googlecode.horseshoe.formatters.IHorseshoeResponseFormatter#startElement(java.lang.String)
   */
    public void startElement(String elementName) {
        _writer.write("<tr><td>{0}", elementName);
    }

    /**
   * @see org.googlecode.horseshoe.formatters.IHorseshoeResponseFormatter#endElement()
   */
    public void endElement() {
        _writer.write("</td></tr>");
    }

    /**
   * @see org.googlecode.horseshoe.formatters.IHorseshoeResponseFormatter#writeElement(java.lang.String, java.lang.Class, java.lang.Object, java.lang.String)
   */
    public void writeElement(String elementName, Class<?> type, Object id, String idProperty) {
        _writer.write("<tr><td>{3} = <a href='%1$s/%2$s[%3$s]'>%3$s</a></td></tr>", getBaseUri(), TextFormatter.pluralize(type.getName()), id, elementName);
    }

    /**
   * @see org.googlecode.horseshoe.formatters.IHorseshoeResponseFormatter#writeField(java.lang.String, java.lang.String)
   */
    public void writeField(String name, String value) {
        _writer.write("<tr><td>%1$s = %2$s</td></tr>", name, value);
    }

    /**
   * @see org.googlecode.horseshoe.formatters.IHorseshoeResponseFormatter#flush()
   */
    public void flush() {
        _writer.flush();
    }

    /**
   * @see org.googlecode.horseshoe.formatters.IHorseshoeResponseFormatter#close()
   */
    public void close() {
        _writer.close();
    }

    /**
   * @return
   */
    private String getBaseUri() {
        return uri.substring(0, uri.indexOf(".horseshoe") + 7);
    }
}
