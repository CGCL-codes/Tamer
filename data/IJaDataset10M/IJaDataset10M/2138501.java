package edu.vt.middleware.gator.util;

import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayDeque;
import java.util.Deque;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Simple {@link InvocationHandler} to be used with a proxy around a
 * {@link XMLStreamWriter} to provide indented XML output.
 *
 * @author Middleware
 * @version $Revision: 2092 $
 *
 */
public class StaxIndentationHandler implements InvocationHandler {

    /** Describes content types that can output by an XML writer. */
    private enum OutputState {

        NONE, ELEMENT, DATA
    }

    /** Name of method to write a processing instruction. */
    private static final String PROC_INSTR = "writeProcessingInstruction";

    /** Name of method to write an element. */
    private static final String EMPTY_ELEMENT = "writeEmptyElement";

    /** Name of method to write a start element. */
    private static final String START_ELEMENT = "writeStartElement";

    /** Name of method to write an element. */
    private static final String END_ELEMENT = "writeEndElement";

    /** Logger instance. */
    private final Log logger = LogFactory.getLog(getClass());

    /** Underlying StAX writer. */
    private final XMLStreamWriter writer;

    /** Line termination character. */
    private final String newline = "\n";

    /** Tab character or character string. */
    private final String tab = "  ";

    /** Current output state. */
    private OutputState state = OutputState.NONE;

    /** State stack. */
    private Deque<OutputState> stack = new ArrayDeque<OutputState>();

    /**
   * Creates a new instance around the given StAX writer.
   *
   * @param  w  StAX writer to delegate to.
   */
    public StaxIndentationHandler(final XMLStreamWriter w) {
        this.writer = w;
    }

    /**
   * Convenience method for creating a {@link XMLStreamWriter} that emits
   * indented output using an instance of this class.
   * 
   * @param  out  Output stream that receives written XML.
   *
   * @return  Proxy to a {@link XMLStreamWriter} that has an instance of this
   * class as an invocation handler to provided indented output.
   * 
   * @throws  FactoryConfigurationError
   * On serious errors creating an XMLOutputFactory
   * @throws  XMLStreamException  On errors creating a an XMLStreamWriter from
   * the default factory.
   */
    public static XMLStreamWriter createIndentingStreamWriter(final OutputStream out) throws XMLStreamException, FactoryConfigurationError {
        final XMLOutputFactory factory = XMLOutputFactory.newInstance();
        factory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, true);
        final XMLStreamWriter writer = factory.createXMLStreamWriter(out, "UTF-8");
        return (XMLStreamWriter) Proxy.newProxyInstance(XMLStreamWriter.class.getClassLoader(), new Class[] { XMLStreamWriter.class }, new StaxIndentationHandler(writer));
    }

    /** {@inheritDoc} */
    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final String name = method.getName();
        try {
            if (START_ELEMENT.equals(name)) {
                indent();
                method.invoke(writer, args);
                stack.push(state);
                state = OutputState.ELEMENT;
            } else if (END_ELEMENT.equals(name)) {
                state = stack.pop();
                indent();
                method.invoke(writer, args);
                if (state == OutputState.NONE) {
                    writer.writeCharacters(newline);
                }
            } else if (EMPTY_ELEMENT.equals(name)) {
                indent();
                method.invoke(writer, args);
            } else if (PROC_INSTR.equals(name)) {
                writer.writeCharacters(newline);
                method.invoke(writer, args);
            } else {
                return method.invoke(writer, args);
            }
        } catch (Exception e) {
            final Throwable cause = e.getCause() != null ? e.getCause() : e;
            logger.error("StAX write error", cause);
            throw cause;
        }
        return null;
    }

    private void indent() throws XMLStreamException {
        writer.writeCharacters(newline);
        for (int i = 0; i < stack.size(); i++) {
            writer.writeCharacters(tab);
        }
    }
}
