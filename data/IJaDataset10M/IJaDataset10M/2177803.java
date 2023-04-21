package opennlp.grok.lexicon;

import opennlp.hylo.*;
import opennlp.common.synsem.*;
import org.jdom.*;

/**
 * Data structure for storing information about a lexical entry.  Specifically
 * used by LMR grammars.
 *
 * @author      Jason Baldridge
 * @version $Revision: 1.1 $, $Date: 2002/08/23 11:17:13 $
 */
public class DataItem {

    private String stem = "";

    private String pred = "";

    private LF _semantics;

    public DataItem() {
    }

    public DataItem(String s, String p) {
        stem = s;
        pred = p;
    }

    public DataItem(Element datael) {
        stem = datael.getAttributeValue("stem");
        pred = datael.getAttributeValue("pred");
        if (null == pred) {
            pred = stem;
        }
        Element lf = datael.getChild("lf");
        if (lf != null) {
            _semantics = HyloHelper.getLF(lf);
        }
    }

    public void setStem(String s) {
        stem = s;
    }

    public void setPred(String s) {
        pred = s;
    }

    public String getStem() {
        return stem;
    }

    public String getPred() {
        return pred;
    }

    public LF getLF() {
        return _semantics;
    }
}
