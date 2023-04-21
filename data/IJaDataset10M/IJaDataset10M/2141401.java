package org.xmlcml.cml.parsetree.amount;

import nu.xom.Element;
import nu.xom.Nodes;

/**
 *
 * @author pm286
 */
public class Vol extends AbstractAmount {

    public static final String TAG = "VOL";

    public static final String NNTAG = "nn-vol";

    public String getTag() {
        return TAG;
    }

    public String getNNTag() {
        return NNTAG;
    }

    public Vol(Element element) {
        super(element);
    }

    public static void normalize(Element element) {
        Nodes nodes = element.query(".//*[local-name()='" + TAG + "']");
        for (int i = 0; i < nodes.size(); i++) {
            new Vol((Element) nodes.get(i)).normalize();
        }
    }

    public void normalize() {
        if (normalizeSimple()) {
        }
    }
}
