package name.ddns.green.mobile.FATJava.exception;

/** Light class to identify data access exceptions, simply passes to Exception for handling
 * 
 * @author paulg
 *
 */
public class DataAccessLayerException extends Exception {

    public DataAccessLayerException(String msg) {
        super(msg);
    }
}
