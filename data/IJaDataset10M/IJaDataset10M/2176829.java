package com.elibera.ccs.buttons.action.style;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import com.elibera.ccs.buttons.ButtonRoot;
import com.elibera.ccs.img.HelperBinary;
import com.elibera.ccs.img.ImageReader;
import com.elibera.ccs.parser.InterfaceDocContainer;
import com.elibera.ccs.res.Msg;

/**
 * @author meisi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ButtonPRight extends ButtonRoot {

    public ButtonPRight(InterfaceDocContainer conf, JPanel panel) {
        super(conf, panel);
    }

    protected Action getActionForThisButton() {
        return new ButtonPCenter.ActionParagraph(container, 'r');
    }

    protected ImageIcon getButtonImageIcon(InterfaceDocContainer conf) {
        return new ImageIcon(ImageReader.openImageJar(conf, HelperBinary.EDITOR_IMG_RIGHT));
    }

    protected String getButtonTitel() {
        return null;
    }

    protected String[][] getStylesKeysAndValues() {
        return null;
    }

    protected void checkForYourSelfForActiveState(AttributeSet s) {
        if (StyleConstants.getAlignment(s) == StyleConstants.ALIGN_RIGHT) {
            setActiveColor(true, s);
            this.setSelected(true);
        } else {
            setActiveColor(false, s);
            this.setSelected(false);
        }
    }

    protected String getHelpText() {
        return Msg.getString("ButtonPRight.TOOLTIP");
    }
}
