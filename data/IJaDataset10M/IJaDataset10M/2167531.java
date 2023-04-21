package gnu.java.beans.decoder;

import java.beans.ExceptionListener;
import org.xml.sax.Attributes;

/** XML element handler that is specialized on tags that contains a simple string in their
 * body which has to be parsed in a specific way.
 * <p>All of these tags have in common that they do not accept attributes. A warning is
 * send to the parser's ExceptionListener when one or more attributes exist.</p>
 *
 * @author Robert Schuster
 */
abstract class SimpleHandler extends AbstractElementHandler {

    private ObjectContext context;

    /**
   * @param PersistenceParser
   */
    SimpleHandler(ElementHandler parent) {
        super(parent, false);
    }

    protected final Context startElement(Attributes attributes, ExceptionListener exceptionListener) throws AssemblyException {
        int size = attributes.getLength();
        for (int i = 0; i < size; i++) {
            String attributeName = attributes.getQName(i);
            Exception e = new IllegalArgumentException("Unneccessary attribute '" + attributeName + "' discarded.");
            exceptionListener.exceptionThrown(e);
        }
        return context = new ObjectContext();
    }

    public void endElement(String characters) throws AssemblyException, AssemblyException {
        try {
            context.setObject(parse(characters));
        } catch (NumberFormatException nfe) {
            throw new AssemblyException(nfe);
        }
    }

    /** Returns an object that is created from the given characters. If the string is
   * converted into a number a NumberFormatException is cathed and reported
   * appropriately.
   *
   * @param characters A string of characters that has to be processed in some way.
   * @return An Object instance generated from the given data.
   * @throws AssemblerException When the string was invalid.
   * @throws NumberFormatException When the string could not be parsed into a number.
   */
    protected abstract Object parse(String characters) throws AssemblyException, NumberFormatException;
}
