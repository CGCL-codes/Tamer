package corujao.logic;

import java.sql.Connection;

/**
 *
 * @author Eduardo_Rangel
 */
public class BaseLogic {

    protected Connection conn;

    /** Creates a new instance of BaseLogic */
    public BaseLogic(Connection conn) {
        this.conn = conn;
    }
}
