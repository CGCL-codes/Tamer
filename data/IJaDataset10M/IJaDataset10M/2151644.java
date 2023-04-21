package com.elibera.ccs.mlos;

import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import com.elibera.ccs.app.MLEConfig;
import com.elibera.ccs.buttons.editorbuttons.ButtonEditorTagOrderbox;
import com.elibera.ccs.tagdata.DataOrderBox;
import com.elibera.ccs.util.HelperStd;

/**
 * @author meisi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XMLTagOrderbox extends MLORoot {

    private static String TAG = "orderbox";

    private static String TAG_SHORT = "ord";

    public static String ATTR_OBJECT_ID = TAG + "-instanceId";

    public static char TEXT_CHAR = 5;

    private MLEConfig conf;

    public XMLTagOrderbox(MLEConfig conf) {
        super(conf);
        this.conf = conf;
    }

    public String getTAGNameShort() {
        return TAG_SHORT;
    }

    public String getTAGName() {
        return TAG;
    }

    protected void doCheckEndTag(String[][] attr) {
        conf.parser.curPageOpenElement = null;
    }

    protected void doCheckStartTag(String[][] attr) {
        String id = null;
        String group = null;
        int startValue = 0;
        for (int i = 0; i < attr.length; i++) {
            if (attr[i][1].length() <= 0) continue;
            char c = attr[i][0].charAt(0);
            if (c == 'i') id = attr[i][1]; else if (c == 'g') group = attr[i][1]; else if (c == 's') startValue = HelperStd.parseInt(attr[i][1], startValue);
        }
        DataOrderBox data = new DataOrderBox(conf, "t" + container.getNewObjectID(), id, group, startValue);
        conf.parser.curPageOpenElement = data;
        StyleContext.NamedStyle s = data.getNewAttributeSetForThisTagObject();
        if (conf.parser.curPageQuestion != null) conf.parser.curPageQuestion.allowRandomize = true;
        container.getDocParser().appendToDocument(TEXT_CHAR + "", s);
    }

    public void doResetForNewXMLBuild() {
    }

    protected void registerStylesToDocument() {
    }

    protected String doBuildXMLWithStyleAttributes(String[][] attrs, AttributeSet as) {
        Object obh = StyleConstants.getComponent(as);
        if (obh != null) {
            if (obh.getClass().getCanonicalName().compareTo(ButtonEditorTagOrderbox.class.getCanonicalName()) == 0) {
                ButtonEditorTagOrderbox button = (ButtonEditorTagOrderbox) obh;
                DataOrderBox data = (DataOrderBox) button.data;
                if (data.id == null) data.id = "o" + data.container.getNewObjectID() + "_" + HelperStd.getNextUniqueSeed();
                if (data.group == null) data.group = "o";
                String xml = "<" + TAG + "";
                if (data.id != null) xml += " id=\"" + data.id + "\"";
                if (data.group != null) xml += " group=\"" + data.group + "\"";
                if (data.startValue > 0) xml += " startValue=\"" + data.startValue + "\"";
                xml += ">";
                xml += XMLTagAnswer.doBuildXMLWithStyleAttributes(data, container.getDocParser());
                xml += XMLTagHint.doBuildXMLWithStyleAttributes(data, container.getDocParser());
                return xml + "</" + TAG + ">";
            }
        }
        return "";
    }
}
