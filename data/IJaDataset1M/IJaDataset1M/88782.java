package net.sf.amemailchecker.gui.component.accordion;

import javax.swing.*;

public class AccordionHeader extends JComponent {

    private static final String uiClassID = "AccordionHeaderUI";

    private String text;

    private boolean active;

    AccordionHeader() {
    }

    AccordionHeader(String text) {
        this.text = text;
    }

    @Override
    public boolean isShowing() {
        return true;
    }

    public void updateUI() {
        if (UIManager.get(getUIClassID()) != null) {
            setUI((AccordionHeaderUI) UIManager.getUI(this));
        } else {
            setUI(new BasicAccordionHeaderUI());
        }
    }

    public void setUI(AccordionHeaderUI ui) {
        super.setUI(ui);
    }

    public AccordionHeaderUI getUI() {
        return (AccordionHeaderUI) ui;
    }

    public String getUIClassID() {
        return uiClassID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
