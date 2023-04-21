package org.zkoss.zk.au;

/**
 * A response to ask the client to scroll the desktop (aka., the browser window)
 * to specified location (in pixel).
 *
 * <p>data[0]: x<br/>
 * data[1]: y
 * 
 * @author tomyeh
 */
public class AuScrollTo extends AuResponse {

    public AuScrollTo(int x, int y) {
        super("scrollTo", new String[] { Integer.toString(x), Integer.toString(y) });
    }
}
