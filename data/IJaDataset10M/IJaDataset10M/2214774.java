package net.sourceforge.swinguiloc.util;

import java.awt.Component;
import javax.swing.Action;
import javax.swing.Icon;
import net.sourceforge.swinguiloc.beans.LButton;
import net.sourceforge.swinguiloc.trans.LanguageSwitchedEvent;
import net.sourceforge.swinguiloc.trans.Translatable;
import net.sourceforge.swinguiloc.trans.Translator;

public class LResizableButton extends LButton {

    private static final long serialVersionUID = 1L;

    public LResizableButton() {
        super();
    }

    public LResizableButton(Action arg0) {
        super(arg0);
    }

    public LResizableButton(Icon arg0) {
        super(arg0);
    }

    public LResizableButton(String arg0, Icon arg1) {
        super(arg0, arg1);
    }

    public LResizableButton(String arg0) {
        super(arg0);
    }

    /**
     * @see net.sourceforge.swinguiloc.trans.Translatable#handleLanguageSwitched(net.sourceforge.swinguiloc.trans.LanguageSwitchedEvent)
     */
    public void handleLanguageSwitched(LanguageSwitchedEvent e) {
        Translator translator = e.getTranslator();
        Object source = e.getSource();
        if (translator.getLocale() != null) this.setLocale(translator.getLocale());
        if (source != null) {
            if (source instanceof Translatable) {
                String superCaptionTag = ((Translatable) source).getCaptionTag();
                String caption = translator.getCaptionFor(superCaptionTag + "." + getCaptionTag());
                if (caption == null) caption = translator.getCaptionFor(getCaptionTag());
                if (caption != null) {
                    if (caption.length() > 10) {
                        setSize(100 + caption.length(), getHeight());
                    } else {
                        setSize(100, 25);
                    }
                    if (getParent().getComponentCount() > 10) {
                        Component neighbour = getParent().getComponent(9);
                        neighbour.setSize(185 - caption.length(), neighbour.getHeight());
                        setLocation(getParent().getWidth() - 10 - getWidth(), getLocation().y);
                        getParent().validate();
                    }
                }
                setText(caption);
            }
        } else {
            String caption = translator.getCaptionFor(getCaptionTag());
            setText(caption);
        }
        String toolTipText = null;
        if (source != null) {
            String superCaptionTag = ((Translatable) source).getCaptionTag();
            toolTipText = translator.getCaptionFor(superCaptionTag + "." + getCaptionTag() + ".toolTipText");
        }
        if (toolTipText == null) {
            toolTipText = translator.getCaptionFor(getCaptionTag() + ".toolTipText");
        }
        if (toolTipText != null) this.setToolTipText(toolTipText);
    }
}
