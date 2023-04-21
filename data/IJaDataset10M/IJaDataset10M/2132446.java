package clavicom.core.keygroup.keyboard.key;

import java.awt.Color;
import org.jdom.Element;
import clavicom.CFilePaths;
import clavicom.gui.language.UIString;
import clavicom.tools.TPoint;
import clavicom.tools.TXMLNames;

public class CKeyString extends CKeyDynamicString {

    String baseString;

    public CKeyString(Color myColorNormal, Color myColorClicked, Color myColorEntered, boolean holdable, TPoint myPointMin, TPoint myPointMax) {
        this(myColorNormal, myColorClicked, myColorEntered, holdable, myPointMin, myPointMax, "");
    }

    public CKeyString(Color myColorNormal, Color myColorClicked, Color myColorEntered, boolean holdable, TPoint myPointMin, TPoint myPointMax, String myCaption) {
        super(myColorNormal, myColorClicked, myColorEntered, holdable, myPointMin, myPointMax, myCaption);
    }

    public CKeyString(Element eltKeyString) throws Exception {
        super(eltKeyString);
        try {
            baseString = eltKeyString.getChildText(TXMLNames.KY_ELEMENT_STRING_BASESTRING);
        } catch (Exception ex) {
            throw new Exception(UIString.getUIString("EX_KEYSTRING_MISSING_BASESTRING_1") + TXMLNames.KY_ELEMENT_STRING_BASESTRING + UIString.getUIString("EX_KEYSTRING_MISSING_BASESTRING_2"));
        }
    }

    public void completeNodeSpecific3(Element eltKeyNode) throws Exception {
        Element eltBaseString = new Element(TXMLNames.KY_ELEMENT_STRING_BASESTRING);
        eltBaseString.setText(baseString);
        eltKeyNode.addContent(eltBaseString);
    }

    public String getElementName() {
        return TXMLNames.KY_ELEMENT_STRING;
    }

    public String getBaseString() {
        return baseString;
    }

    public void setBaseString(String baseString) {
        this.baseString = baseString;
    }

    @Override
    protected Boolean toBeSave() {
        return true;
    }

    @Override
    public String GetStringCommand() {
        return baseString;
    }

    @Override
    public String getCaption() {
        if (isCaptionImage()) return CFilePaths.getUserPicturesFolder() + caption; else return caption;
    }

    @Override
    public String toString() {
        return (UIString.getUIString("ST_KEY_TOSTRING_STRING") + " [" + baseString + "]");
    }
}
