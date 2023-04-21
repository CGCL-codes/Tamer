package org.jxul.framework;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import org.w3c.dom.*;
import org.jxul.util.*;

/**
 * A XUL Titled Box.
 */
public class XulHBox extends XulBox {

    public static final boolean debug;

    static {
        boolean temp = false;
        try {
            String sdebug = System.getProperty("debug");
            if (sdebug != null && sdebug.equalsIgnoreCase("true")) temp = true; else temp = false;
        } catch (Exception e) {
        }
        debug = temp;
    }

    private Element hBoxElement;

    public XulHBox(Element hBoxElement, Hashtable peerMap) throws XulFormatException {
        super(hBoxElement, peerMap);
        this.hBoxElement = hBoxElement;
        align = XulSyntax.BOX_ATTR_ALIGN_HORIZONTAL;
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    }

    /** for unit testing. */
    public static void main(String args[]) {
        try {
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
