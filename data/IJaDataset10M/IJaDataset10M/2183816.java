package simple.template.layout;

import simple.template.Document;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.util.Set;

/**
 * The <code>DocumentAdapter</code> provides an implementation
 * of the <code>Document</code> interface. This provides a
 * proxy implementation that delegates to an internal instance
 * of the <code>Viewer</code>, which performs the rendering of
 * the template, and also stores the objects used. Converting
 * this to a string can be done using the <code>toString</code>
 * method. Each time a string conversion is produced a new
 * rendering of the template is performed so it is up to date.
 *
 * @author Niall Gallagher
 */
final class DocumentAdapter implements Document {

    /**
    * This is used to capture the output of the template.
    */
    private ViewerBuffer data;

    /**
    * The template instance used to generate the document.
    */
    private Viewer viewer;

    /**
    * This is the path used to reference this document object.
    */
    private String path;

    /**
    * Constructor for the <code>DocumentAdapter</code> object.
    * The document uses the <code>Viewer</code> object internaly
    * to perform all actions on the template, however this also
    * contains information regarding the path reference.
    * 
    * @param viewer template object used to generate output
    * @param path this is the path used to reference this
    */
    public DocumentAdapter(Viewer viewer, String path) {
        this.data = new ViewerBuffer(viewer);
        this.viewer = viewer;
        this.path = path;
    }

    /**
    * The <code>put</code> method is used to insert a mapping in
    * the database that pairs the issued name with the issued
    * value. The value can be referenced in future by its name.
    *
    * @param name this is the name of the value being inserted
    * @param value this is the named value that is inserted
    */
    public void put(String name, Object value) {
        viewer.put(name, value);
    }

    /**
    * The <code>get</code> method is used to retrieve the value
    * mapped to the specified name. If a value does not exist
    * matching the given name, then this returns null.
    * 
    * @param name this is the name of the value to be retrieved
    *  
    * @return returns the value if it exists or null otherwise
    */
    public Object get(String name) {
        return viewer.get(name);
    }

    /**
    * The <code>remove</code> method is used to remove the 
    * named value from the database. This method either removes
    * the value or returns silently if the name does not exits.
    *
    * @param name this is the name of the value to be removed
    */
    public void remove(String name) {
        viewer.remove(name);
    }

    /**
    * To ascertain what mappings exist, the names of all values
    * previously put into thhis database can be retrieved with 
    * this method. This will return a <code>Set</code> that 
    * contains the names of all the mappings added to this.
    *
    * @return this returns all the keys for existing mappings
    */
    public Set keySet() {
        return viewer.keySet();
    }

    /**
    * The <code>contains</code> method is used to determine if
    * a mapping exists for the given name. This returns true if
    * the mapping exists or false otherwise.
    *
    * @param name this is the name of the mapping to determine
    *
    * @return returns true if a mapping exists, false otherwise
    */
    public boolean contains(String name) {
        return viewer.contains(name);
    }

    /**
    * Displays the contents of the generated template output to
    * the <code>OutputStream</code>. This encapsulates the means
    * of rendering the template to a single method. Internally
    * the properties that are set within the document will be
    * used to configure the template, enabling dynamic output.
    * <p>
    * If there are any problems parsing the template or emitting
    * its contents an exception is thrown. However, if it is
    * successfully processed it will be written to the issued
    * output, which will remain unflushed for performance. The
    * output is written using the UTF-8 charset.
    *
    * @param out the output to write the template rendering to
    *
    * @throws Exception thrown if there is a problem parsing or 
    * emitting the template 
    */
    public void write(OutputStream out) throws Exception {
        write(out, viewer.getCharset());
    }

    /**
    * Displays the contents of the generated template output to
    * the <code>OutputStream</code>. This encapsulates the means
    * of rendering the template to a single method. Internally
    * the properties that are set within the document will be
    * used to configure the template, enabling dynamic output.
    * <p>
    * If there are any problems parsing the template or emitting
    * its contents an exception is thrown. However, if it is
    * successfully processed it will be written to the issued
    * output, which will remain unflushed for performance. The
    * output is written using the specified charset.
    *
    * @param out the output to write the template rendering to
    * @param charset the charset used to write the template 
    *
    * @throws Exception thrown if there is a problem parsing or 
    * emitting the template 
    */
    private void write(OutputStream out, String charset) throws Exception {
        write(new ProxyOutputStream(out), charset);
    }

    /**
    * Displays the contents of the generated template output to
    * the <code>OutputStream</code>. This encapsulates the means
    * of rendering the template to a single method. Internally
    * the properties that are set within the document will be
    * used to configure the template, enabling dynamic output.
    * <p>
    * If there are any problems parsing the template or emitting
    * its contents an exception is thrown. However, if it is
    * successfully processed it will be written to the issued
    * output, which will remain unflushed for performance. The
    * output is written using the specified charset.
    *
    * @param out the output to write the template rendering to
    * @param charset the charset used to write the template 
    *
    * @throws Exception thrown if there is a problem parsing or 
    * emitting the template 
    */
    private void write(ProxyOutputStream out, String charset) throws Exception {
        write(new OutputStreamWriter(out, charset));
    }

    /**
    * Displays the contents of the generated template output to
    * the issued <code>Writer</code>. This encapsulates the means
    * of rendering the template to a single method. Internally
    * the properties that are set within the document will be
    * used to configure the template, enabling dynamic output.
    * <p>
    * If there are any problems parsing the template or emitting
    * its contents an exception is thrown. However, if it is
    * successfully processed it will be written to the issued
    * output, which is then flushed.
    *
    * @param out the output to write the template rendering to
    *
    * @throws Exception thrown if there is a problem parsing or 
    * emitting the template 
    */
    private void write(OutputStreamWriter out) throws Exception {
        viewer.write(out);
        out.flush();
    }

    /**
    * The content that is dynamically generated by the object
    * is written as a specific MIME type, including charset
    * information which determines the content encoding. For
    * example if the output was HTML written using UTF-8 
    * format then this would return "text/html; charset=utf-8".
    *
    * @return returns the MIME type of the generated content
    */
    public String getContentType() {
        return viewer.getContentType();
    }

    /**
    * Produces the generated template output as a string. This
    * is useful for embedding documents within each other
    * creating a layout effect. For example, if a templated
    * HTML page required content from different sources, then
    * a document could be added, as a property, for display.
    * <p>
    * This is very useful if the output is to be used as an
    * SQL query, or an email message. The rendering is stored
    * conveniently for use as a string and does not require
    * an <code>OutputStream</code> to capture the output.   
    *
    * @return document output if successful, null otherwise
    */
    public String toString() {
        return data.toString();
    }
}
