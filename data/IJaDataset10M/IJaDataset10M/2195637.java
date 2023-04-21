package com.csam.browser.liveconnect.dom;

import com.csam.browser.JApplet2;
import netscape.javascript.JSObject;
import org.w3c.dom.DocumentFragment;

/**
 *
 * @author Nathan Crause <ncrause at clarkesolomou.com>
 */
public class LiveConnectDocumentFragment extends LiveConnectNode implements DocumentFragment {

    public LiveConnectDocumentFragment(JSObject js, JApplet2 applet) {
        super(js, applet);
    }
}
