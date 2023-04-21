package org.apache.myfaces.context;

import org.apache.myfaces.shared_impl.renderkit.html.HtmlResponseWriterImpl;
import javax.faces.component.UIComponent;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

/**
 * <p/>
 * Double buffering partial response writer
 * to take care if embedded CDATA blocks in update delete etc...
 * <p/>
 * According to the spec 13.4.4.1 Writing The Partial Response
 * implementations have to take care to handle nested cdata blocks properly
 * <p/>
 * This means we cannot allow nested CDATA
 * according to the xml spec http://www.w3.org/TR/REC-xml/#sec-cdata-sect
 * everything within a CDATA block is unparsed except for ]]>
 * <p/>
 * Now we have following problem, that CDATA inserts can happen everywhere
 * not only within the CDATA instructions.
 * <p/>
 * What we have to do now is to double buffer CDATA blocks until their end
 * and also!!! parse their content for CDATA embedding and replace it with an escaped end sequence.
 * <p/>
 * Now parsing CDATA embedding is a little bit problematic in case of PPR because
 * it can happen that someone simply adds a CDATA in a javascript string or somewhere else.
 * Because he/she is not aware that we wrap the entire content into CDATA.
 * Simply encoding and decoding of the CDATA is similarly problematic
 * because the browser then chokes on embedded //<![CDATA[ //]]> sections
 * <p/>
 * What we do for now is to simply remove //<![CDATA[ and //]]>
 * and replace all other pending cdatas with their cdata escapes
 * ]]&gt; becomes &lt;![CDATA[]]]]&gt;&lt;![CDATA[&gt;
 * <p/>
 * If this causes problems in corner cases we also can add a second encoding step in
 * case of the cdata Javascript comment removal is not enough to cover all corner cases.
 * <p/>
 * For now I will only implement this in the impl, due to the spec stating
 * that implementations are responsible of the correct CDATA handling!
 *
 * @author Werner Punz (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class PartialResponseWriterImpl extends PartialResponseWriter {

    class StackEntry {

        HtmlResponseWriterImpl writer;

        StringWriter _doubleBuffer;

        StackEntry(HtmlResponseWriterImpl writer, StringWriter doubleBuffer) {
            this.writer = writer;
            _doubleBuffer = doubleBuffer;
        }

        public HtmlResponseWriterImpl getWriter() {
            return writer;
        }

        public void setWriter(HtmlResponseWriterImpl writer) {
            this.writer = writer;
        }

        public StringWriter getDoubleBuffer() {
            return _doubleBuffer;
        }

        public void setDoubleBuffer(StringWriter doubleBuffer) {
            _doubleBuffer = doubleBuffer;
        }
    }

    HtmlResponseWriterImpl _cdataDoubleBufferWriter = null;

    StringWriter _doubleBuffer = null;

    List<StackEntry> _nestingStack = new LinkedList<StackEntry>();

    public PartialResponseWriterImpl(ResponseWriter writer) {
        super(writer);
    }

    @Override
    public void startCDATA() throws IOException {
        if (!isDoubleBufferEnabled()) {
            super.startCDATA();
        } else {
            _cdataDoubleBufferWriter.write("<![CDATA[");
        }
        openDoubleBuffer();
    }

    private void openDoubleBuffer() {
        _doubleBuffer = new StringWriter();
        _cdataDoubleBufferWriter = new HtmlResponseWriterImpl(_doubleBuffer, super.getContentType(), super.getCharacterEncoding());
        StackEntry entry = new StackEntry(_cdataDoubleBufferWriter, _doubleBuffer);
        _nestingStack.add(0, entry);
    }

    @Override
    public void endCDATA() throws IOException {
        closeDoubleBuffer(false);
        if (isDoubleBufferEnabled()) {
            _cdataDoubleBufferWriter.write("]]>");
        } else {
            super.endCDATA();
        }
    }

    /**
     * Close double buffer condition
     * This does either a normal close or a force
     * close in case of a force close
     * the entire buffer  is pushed with the post processing
     * operations into the originating writer
     *
     * @param force if set to true the close is a forced close which in any condition
     *              immediately pushes the buffer content into our writer with a pre operation
     *              done upfront, in case of a false, the buffer is only swept out if our
     *              internal CDATA nesting counter is at the nesting depth 1
     * @throws IOException
     */
    private void closeDoubleBuffer(boolean force) throws IOException {
        if (!isDoubleBufferEnabled()) return;
        if (force) {
            while (!_nestingStack.isEmpty()) {
                popAndEncodeCurrentStackEntry();
            }
        } else {
            popAndEncodeCurrentStackEntry();
        }
    }

    private void popAndEncodeCurrentStackEntry() throws IOException {
        StackEntry elem = _nestingStack.remove(0);
        StackEntry parent = (_nestingStack.isEmpty()) ? null : _nestingStack.get(0);
        String result = postProcess(elem);
        if (parent != null) {
            _cdataDoubleBufferWriter = parent.getWriter();
            _doubleBuffer = parent.getDoubleBuffer();
            _cdataDoubleBufferWriter.write(result);
        } else {
            _cdataDoubleBufferWriter = null;
            _doubleBuffer = null;
            super.write(result);
        }
        elem.getDoubleBuffer().close();
        elem.getWriter().close();
    }

    /**
     * string post processing
     *
     * @param currentElement the current writer element
     * @return the post processed string
     * @throws IOException in case of an error
     */
    private String postProcess(StackEntry currentElement) throws IOException {
        currentElement.getWriter().flush();
        StringBuffer buffer = currentElement.getDoubleBuffer().getBuffer();
        String resultString = buffer.toString();
        if (resultString.contains("]]>")) {
            resultString = resultString.replaceAll("//\\s*((\\<\\!\\[CDATA\\[)|(\\]\\]\\>))", "");
            resultString = resultString.replaceAll("\\]\\]\\>", "]]><![CDATA[]]]]><![CDATA[>");
        }
        return resultString;
    }

    @Override
    public void endInsert() throws IOException {
        closeDoubleBuffer(true);
        super.endInsert();
    }

    @Override
    public void endUpdate() throws IOException {
        closeDoubleBuffer(true);
        super.endUpdate();
    }

    @Override
    public void endExtension() throws IOException {
        closeDoubleBuffer(true);
        super.endExtension();
    }

    @Override
    public void endEval() throws IOException {
        closeDoubleBuffer(true);
        super.endEval();
    }

    @Override
    public void endError() throws IOException {
        closeDoubleBuffer(true);
        super.endError();
    }

    @Override
    public void endElement(String name) throws IOException {
        if (isDoubleBufferEnabled()) {
            _cdataDoubleBufferWriter.endElement(name);
        } else {
            super.endElement(name);
        }
    }

    @Override
    public void writeComment(Object comment) throws IOException {
        if (isDoubleBufferEnabled()) {
            _cdataDoubleBufferWriter.writeComment(comment);
        } else {
            super.writeComment(comment);
        }
    }

    private boolean isDoubleBufferEnabled() {
        return !_nestingStack.isEmpty();
    }

    @Override
    public void startElement(String name, UIComponent component) throws IOException {
        if (isDoubleBufferEnabled()) {
            _cdataDoubleBufferWriter.startElement(name, component);
        } else {
            super.startElement(name, component);
        }
    }

    @Override
    public void writeText(Object text, String property) throws IOException {
        if (isDoubleBufferEnabled()) {
            _cdataDoubleBufferWriter.writeText(text, property);
        } else {
            super.writeText(text, property);
        }
    }

    @Override
    public void writeText(char[] text, int off, int len) throws IOException {
        if (isDoubleBufferEnabled()) {
            _cdataDoubleBufferWriter.writeText(text, off, len);
        } else {
            super.writeText(text, off, len);
        }
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        if (isDoubleBufferEnabled()) {
            _cdataDoubleBufferWriter.writeText(cbuf, off, len);
        } else {
            super.write(cbuf, off, len);
        }
    }

    @Override
    public ResponseWriter cloneWithWriter(Writer writer) {
        return super.cloneWithWriter(writer);
    }

    @Override
    public void writeURIAttribute(String name, Object value, String property) throws IOException {
        if (isDoubleBufferEnabled()) {
            _cdataDoubleBufferWriter.writeURIAttribute(name, value, property);
        } else {
            super.writeURIAttribute(name, value, property);
        }
    }

    @Override
    public void close() throws IOException {
        if (isDoubleBufferEnabled()) {
            closeDoubleBuffer(true);
            super.endCDATA();
        }
        super.close();
    }

    @Override
    public void flush() throws IOException {
        if (isDoubleBufferEnabled()) {
            _cdataDoubleBufferWriter.flush();
        }
        super.flush();
    }

    @Override
    public void writeAttribute(String name, Object value, String property) throws IOException {
        if (isDoubleBufferEnabled()) {
            _cdataDoubleBufferWriter.writeAttribute(name, value, property);
        } else {
            super.writeAttribute(name, value, property);
        }
    }

    @Override
    public void writeText(Object object, UIComponent component, String string) throws IOException {
        if (isDoubleBufferEnabled()) {
            _cdataDoubleBufferWriter.writeText(object, component, string);
        } else {
            super.writeText(object, component, string);
        }
    }

    @Override
    public Writer append(char c) throws IOException {
        if (isDoubleBufferEnabled()) {
            _cdataDoubleBufferWriter.append(c);
            return this;
        } else {
            return super.append(c);
        }
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        if (isDoubleBufferEnabled()) {
            _cdataDoubleBufferWriter.append(csq, start, end);
            return this;
        } else {
            return super.append(csq, start, end);
        }
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {
        if (isDoubleBufferEnabled()) {
            _cdataDoubleBufferWriter.append(csq);
            return this;
        } else {
            return super.append(csq);
        }
    }

    @Override
    public void write(char[] cbuf) throws IOException {
        if (isDoubleBufferEnabled()) {
            _cdataDoubleBufferWriter.write(cbuf);
        } else {
            super.write(cbuf);
        }
    }

    @Override
    public void write(int c) throws IOException {
        if (isDoubleBufferEnabled()) {
            _cdataDoubleBufferWriter.write(c);
        } else {
            super.write(c);
        }
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        if (isDoubleBufferEnabled()) {
            _cdataDoubleBufferWriter.write(str, off, len);
        } else {
            super.write(str, off, len);
        }
    }

    @Override
    public void write(String str) throws IOException {
        if (isDoubleBufferEnabled()) {
            _cdataDoubleBufferWriter.write(str);
        } else {
            super.write(str);
        }
    }
}
