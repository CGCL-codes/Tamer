package org.makagiga.commons;

import static org.makagiga.commons.UI._;
import java.awt.Font;
import org.makagiga.commons.beans.AbstractBeanInfo;

/**
 * @since 2.0
 */
public class MFontButton extends MButton {

    /**
	 * @since 3.0
	 */
    public static final String SELECTED_FONT_PROPERTY = "selectedFont";

    private Font selectedFont;

    private String textLabel;

    public MFontButton() {
        this(null, null);
        AbstractBeanInfo.init();
    }

    public MFontButton(final Font defaultFont) {
        this(null, defaultFont);
    }

    /**
	 * @since 2.4
	 */
    public MFontButton(final String textLabel, final Font defaultFont) {
        this.textLabel = textLabel;
        setIconName("ui/font");
        setSelectedFont(defaultFont);
        setToolTipText(_("Select a Font"));
    }

    /**
	 * @since 2.4
	 */
    public void addValueListener(final ValueListener<Font> l) {
        listenerList.add(ValueListener.class, l);
    }

    /**
	 * @since 2.4
	 */
    @SuppressWarnings("unchecked")
    public ValueListener<Font>[] getValueListeners() {
        return listenerList.getListeners(ValueListener.class);
    }

    /**
	 * @since 2.4
	 */
    public void removeValueListener(final ValueListener<Font> l) {
        listenerList.remove(ValueListener.class, l);
    }

    /**
	 * @since 3.0
	 */
    public String getTextLabel() {
        return textLabel;
    }

    /**
	 * @since 3.0
	 */
    public void setTextLabel(final String value) {
        if (TK.isChange(textLabel, value)) {
            textLabel = value;
            updateFontText();
        }
    }

    public Font getSelectedFont() {
        return selectedFont;
    }

    public void setSelectedFont(final Font value) {
        Font newValue = (value == null) ? getFont() : value;
        if (TK.isChange(selectedFont, newValue)) {
            Font oldValue = selectedFont;
            selectedFont = newValue;
            firePropertyChange(SELECTED_FONT_PROPERTY, oldValue, newValue);
            updateFontText();
        }
    }

    /**
	 * @since 2.4
	 */
    protected void fireValueChanged(final Font oldFont, final Font newFont) {
        TK.fireValueChanged(this, getValueListeners(), oldFont, newFont);
    }

    @Override
    protected final void onClick() {
        Font oldFont = getSelectedFont();
        Font newFont = MFontChooser.getFont(getWindowAncestor(), oldFont);
        if (newFont != null) {
            setSelectedFont(newFont);
            fireValueChanged(oldFont, getSelectedFont());
        }
    }

    private void updateFontText() {
        String text = (textLabel == null) ? _("Select a Font...") : textLabel;
        setText(text + " [" + selectedFont.getName() + " " + selectedFont.getSize() + "]");
        setFont(getFont().deriveFont(selectedFont.getStyle()));
    }
}
