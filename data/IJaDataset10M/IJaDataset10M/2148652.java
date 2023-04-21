package gnu.java.beans.decoder;

/** Creates a Integer instance from the character data in a &lt;int&gt; tag.
 *
 * @author Robert Schuster
 */
class IntHandler extends SimpleHandler {

    /**
   * @param PersistenceParser
   */
    IntHandler(ElementHandler parent) {
        super(parent);
    }

    protected Object parse(String number) throws NumberFormatException {
        return Integer.valueOf(number);
    }
}
