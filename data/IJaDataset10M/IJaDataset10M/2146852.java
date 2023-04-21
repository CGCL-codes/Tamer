package playground.kai.ids;

/**
 * @author nagel
 *
 */
public class NetworkIdFactory {

    public static LinkId createLinkId(String str) {
        return new MyIdImpl(str);
    }
}
