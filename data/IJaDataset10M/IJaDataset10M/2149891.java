package com.codename1.ui;

import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.geom.*;
import com.codename1.ui.plaf.DefaultLookAndFeel;
import com.codename1.ui.plaf.LookAndFeel;

/**
 * Checkbox is a button that can be selected or deselected, and which displays
 * its state to the user.
 * 
 * @author Chen Fishbein
 */
public class CheckBox extends Button {

    private boolean selected = false;

    private boolean oppositeSide;

    /**
     * Constructs a checkbox with the given text
     * 
     * @param text to display next to the checkbox
     */
    public CheckBox(String text) {
        this(text, null);
    }

    /**
     * Constructs a checkbox with no text
     */
    public CheckBox() {
        this("");
    }

    /**
     * Constructs a checkbox with the given icon
     * 
     * @param icon icon to display next to the checkbox
     */
    public CheckBox(Image icon) {
        this("", icon);
    }

    /**
     * Constructs a checkbox with the given text and icon
     * 
     * @param text to display next to the checkbox
     * @param icon icon to display next to the text
     */
    public CheckBox(String text, Image icon) {
        super(text, icon);
        setUIID("CheckBox");
        updateSide();
    }

    /**
     * Return true if the checkbox is selected
     * 
     * @return true if the checkbox is selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Selects the current checkbox
     * 
     * @param selected value for selection
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    /**
     * @inheritDoc
     */
    public void released(int x, int y) {
        selected = !isSelected();
        super.released(x, y);
    }

    /**
     * @inheritDoc
     */
    public void paint(Graphics g) {
        if (isToggle()) {
            getUIManager().getLookAndFeel().drawButton(g, this);
        } else {
            getUIManager().getLookAndFeel().drawCheckBox(g, this);
        }
    }

    /**
     * @inheritDoc
     */
    protected Dimension calcPreferredSize() {
        return getUIManager().getLookAndFeel().getCheckBoxPreferredSize(this);
    }

    /**
     * @inheritDoc
     */
    protected String paramString() {
        return super.paramString() + ", selected = " + selected;
    }

    void initComponentImpl() {
        super.initComponentImpl();
    }

    private void updateSide() {
        Boolean v = getUIManager().isThemeConstant("checkBoxOppositeSideBool");
        if (v != null) {
            oppositeSide = v.booleanValue();
        }
    }

    /**
     * @inheritDoc
     */
    public void refreshTheme(boolean merge) {
        super.refreshTheme(merge);
        updateSide();
    }

    int getAvaliableSpaceForText() {
        if (isToggle()) {
            return super.getAvaliableSpaceForText();
        }
        LookAndFeel l = getUIManager().getLookAndFeel();
        if (l instanceof DefaultLookAndFeel) {
            Image[] rButtonImages = ((DefaultLookAndFeel) l).getCheckBoxImages();
            if (rButtonImages != null) {
                int index = isSelected() ? 1 : 0;
                return super.getAvaliableSpaceForText() - rButtonImages[index].getWidth();
            }
        }
        return super.getAvaliableSpaceForText() - (getHeight() + getGap());
    }

    /**
     * Places the check box on the opposite side at the far end
     *
     * @return the oppositeSide
     */
    public boolean isOppositeSide() {
        return oppositeSide;
    }

    /**
     * Places the check box on the opposite side at the far end
     *
     * @param oppositeSide the oppositeSide to set
     */
    public void setOppositeSide(boolean oppositeSide) {
        this.oppositeSide = oppositeSide;
    }
}
