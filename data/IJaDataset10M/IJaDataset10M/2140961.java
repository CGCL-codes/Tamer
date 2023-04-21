package jaxlib.jdbc.tds;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: ConnectedTdsObject.java 2731 2009-04-24 21:44:41Z joerg_wassmer $
 */
abstract class ConnectedTdsObject extends TdsObject implements TdsConnectionObject {

    ConnectedTdsObject() {
        super();
    }

    @Override
    public abstract TdsConnection tdsConnection();
}
