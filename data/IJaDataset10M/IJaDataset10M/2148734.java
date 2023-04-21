package tiniweb.module.phone.asterisk;

/**
 *
 * @author Yannick Poirier 
 */
public class NotConnectedException extends Exception {

    public NotConnectedException() {
        super("Not Connected");
    }
}
