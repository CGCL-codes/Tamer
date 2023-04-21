package com.myjavatools.lib;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.MessageFormat;

public class FormattedWriter {

    MessageFormat head = null;

    MessageFormat body = null;

    MessageFormat tail = null;

    Writer writer = null;

    /**
   * Creates a writer that formats its output according to the format specified
   *
   * @see #FormattedWriter(Writer,String,String,String)
   *
   * @param writer the writer that outputs the formatted contents
   * @param format head format string
   *
   * <p><b>Example</b><br>
   * <code>
   * String format = "Copyright (c) {0} {1}\r\n\r\n" +<br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"{2}\r\n";<br>
   *<br>
   * FileFormat ff = new FileFormat(new FileWriter("readme.txt"), format);<br>
   * ff.write("George Rasputin", "" + new Date().getYear(), "This is a stub for future readme text");<br>
   * </code>
   * <p>This code will produce something like this:<br><code>
   * Copyright (c) George Rasputin 2004<br>
   * <br>
   * This is a stub for future readme text<br>
   * <br></code>
   */
    public FormattedWriter(final Writer writer, final String format) {
        this.writer = writer;
        head = format == null ? null : new MessageFormat(format);
    }

    /**
   * Creates a writer that formats its output according to head, body and tail formats specified
   *
   * @param writer the writer that outputs the formatted contents
   * @param head format string for the file head
   * @param body format string for (repeating) entries of the file body
   * @param tail format strings for the file tail
   *
   * <p><b>Example</b><br>
   * <code>
   * String head = "/*\r\n Resource Data for package {0} in project {1}\r\n&#47;*\r\n" +<br>
   * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"class ResourceData {\r\n";<br>
   * String body = "  public String {0} = \"{1}\";\r\n";<br>
   * String tail = "}\r\n";<br>
   *<br>
   * FileFormat ff = new FileFormat(new FileWriter("resource.java", head, body, tail);<br>
   * ff.open(packageName, "My Cool Project");<br>
   * Properties p = ...;<br>
   * for (Enumeration i = p.keys(); i.hasMoreElements();) {<br>
   * &nbsp;&nbsp;String key = i.nextElement().toString();<br>
   * &nbsp;&nbsp;ff.write(new String[] {key, p.getProperty(key, "undef"});<br>
   * }<br>
   * ff.close();</code>
   * <p>This code will produce something like this:<br><code>
   * /*<br>
   *  Resource Data for package com.my.package in project My Cool Project<br>
   * *&#47;<br>
   * class ResourceData {<br>
   * &nbsp;&nbsp;String dialogTitle = "Romeo vs Juliet";<br>
   * &nbsp;&nbsp;String label1      = "One of unmentionable major copyrighted labels";<br>
   * &nbsp;&nbsp;String size        = "100x200";<br>
   * }<br></code>
   */
    public FormattedWriter(final Writer writer, final String head, final String body, final String tail) {
        this(writer, head);
        this.body = body == null ? null : new MessageFormat(body);
        this.tail = tail == null ? null : new MessageFormat(tail);
    }

    /**
 * Creates a writer that formats its output according to the format specified
 *
 * @see #FormattedWriter(Writer,String)
 *
 * @param os output stream that outputs the formatted contents
 * @param format head format string
 */
    public FormattedWriter(final OutputStream os, final String format) {
        this(new OutputStreamWriter(os), format);
    }

    /**
   * Creates a writer that formats its output according to head, body and tail formats specified
   *
   * @see #FormattedWriter(Writer,String,String,String)
   *
   * @param os output stream that outputs the formatted contents
   * @param head format string for the file head
   * @param body format string for (repeating) entries of the file body
   * @param tail format strings for the file tail
   */
    public FormattedWriter(final OutputStream os, final String head, final String body, final String tail) {
        this(new OutputStreamWriter(os), head, body, tail);
    }

    /**
   * Gets head format string
   * @return head format string
   */
    public MessageFormat getHead() {
        return head;
    }

    /**
   * Gets body format string
   * @return body format string
   */
    public MessageFormat getBody() {
        return body;
    }

    /**
   * gets tail format string
   * @return tail format string
   */
    public MessageFormat getTail() {
        return tail;
    }

    /**
   * Opens formatted output
   * @param args arguments for the head format
   * @throws IOException if write operation failes
   */
    public void open(final Object[] args) throws IOException {
        writer.write(head.format(args));
    }

    /**
   * Opens formatted output
   * @param arg the only argiment for the head format
   * @throws IOException if write operation fails
   */
    public void open(final Object arg) throws IOException {
        writer.write(head.format(new Object[] { arg }));
    }

    /**
   * Opens formatted output, no arguments for head format
   * @throws IOException
   */
    public void open() throws IOException {
        writer.write(head.format(null));
    }

    /**
   * Writes a body string to formatted output
   *
   * @param args arguments for body format
   * @throws IOException if write operation fails
   *
   * <b>Note.</b> If body format is missing (then there is only head format),
   * then head format is applied to the arguments, the result of formatting is
   * written to the output, and the writer is closed.
   */
    public void write(final Object[] args) throws IOException {
        if (body != null) {
            writer.write(body.format(args));
        } else {
            open(args);
            close();
        }
    }

    /**
   * Writes an arbitrary string to (generally speaking) formatted output
   *
   * @param s the string to output
   * @throws IOException if write operation fails
   *
   * This skips any formatting; the string is just sent to output "as is".
   */
    public void write(final String s) throws IOException {
        writer.write(s);
    }

    /**
   * Writes the tail to formatted output and closes it
   *
   * @param args arguments for tail format
   * @throws IOException if write operation fails
   *
   * <b>Note.</b> if there is no tail format, the output just closes.
   */
    public void close(final Object[] args) throws IOException {
        if (tail != null) {
            writer.write(tail.format(args));
        }
        writer.close();
    }

    /**
   * Writes the tail to formatted output and closes it
   *
   * @throws IOException if write operation fails
   */
    public void close() throws IOException {
        close(null);
    }
}
