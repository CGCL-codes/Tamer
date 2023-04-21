package com.elibera.ccs.mlos;

import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import com.elibera.ccs.buttons.editorbuttons.ButtonEditorTagLink;
import com.elibera.ccs.mlos.XMLTagMenu.RemoveBrTag;
import com.elibera.ccs.parser.HelperXMLParserSimple;
import com.elibera.ccs.parser.InterfaceDocContainer;
import com.elibera.ccs.tagdata.DataLink;
import com.elibera.ccs.tagdata.DataMenu;

/**
 * @author Jeremy
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XMLTagLink extends MLORoot {

    public static String TAG = "link";

    public static String TAG_SHORT = "li";

    public static String ATTR_OBJECT_ID = TAG + "-id";

    public static char TEXT_CHAR = XMLTagMenu.TEXT_CHAR;

    public XMLTagLink(InterfaceDocContainer ed) {
        super(ed);
    }

    public String getTAGNameShort() {
        return TAG_SHORT;
    }

    public String getTAGName() {
        return TAG;
    }

    protected void doCheckEndTag(String[][] attr) {
    }

    protected void doCheckStartTag(String[][] attr) {
        XMLTagMenu.doCheckStartTagMenuButton(container, attr);
    }

    protected void registerStylesToDocument() {
    }

    protected String doBuildXMLWithStyleAttributes(String[][] attrs, AttributeSet as) {
        Object obh = StyleConstants.getComponent(as);
        if (obh != null) {
            if (obh.getClass().getName().compareTo(ButtonEditorTagLink.class.getName()) == 0) {
                ButtonEditorTagLink button = (ButtonEditorTagLink) obh;
                DataMenu data = (DataMenu) button.data;
                HelperMLOsSimple.registerMLOParsingCaptureListener(new RemoveBrTag());
                return XMLTagMenu.buildXML(data);
            }
        }
        return "";
    }

    public void doResetForNewXMLBuild() {
    }
}
