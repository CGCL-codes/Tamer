package gnu.java.beans.decoder;

/** Creates a Long instance from the character data in a &lt;long&gt; tag.
 *
 * @author Robert Schuster
 */
class LongHandler extends SimpleHandler {

    /**
   * @param PersistenceParser
   */
    LongHandler(ElementHandler parent) {
        super(parent);
    }

    protected Object parse(String number) throws NumberFormatException {
        return Long.valueOf(number);
    }
}
