package org.xmlcml.cml.parsetree.amount;

import java.util.List;
import nu.xom.Attribute;
import org.xmlcml.cml.parsetree.*;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import org.xmlcml.cml.element.CMLScalar;
import org.xmlcml.cml.parsetree.Sentence.POS;

/**
 *
 * @author pm286
 */
public class Percent extends AbstractAmount {

    public static final String TAG = "PERCENT";

    public static final String NNTAG = "nn-percent";

    public String getTag() {
        return TAG;
    }

    public String getNNTag() {
        return NNTAG;
    }

    public Percent(Element element) {
        super(element);
    }

    public static void normalize(Element element) {
        Nodes nodes = element.query(".//*[local-name()='" + TAG + "']");
        for (int i = 0; i < nodes.size(); i++) {
            new Percent((Element) nodes.get(i)).normalize();
        }
    }

    public void normalize() {
        if (normalizeSimple()) {
        }
    }
}
