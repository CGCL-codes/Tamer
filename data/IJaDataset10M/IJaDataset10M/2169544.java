package org.zkoss.hil.au.out;

import org.zkoss.zk.au.AuResponse;

/**
 * A response to request ZK Mobile to go to its home page.
 *  
 * @author robbiecheng
 */
public class AuGoHome extends AuResponse {

    public AuGoHome(String url) {
        super("home", url);
    }
}
