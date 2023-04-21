package org.jabusuite.webclient.controls.accordionpane;

import nextapp.echo2.app.Alignment;
import org.jabusuite.webclient.main.Styles;
import nextapp.echo2.app.Button;
import nextapp.echo2.app.Color;
import nextapp.echo2.app.ResourceImageReference;
import org.jabusuite.logging.Logger;
import org.jabusuite.webclient.controls.JbsExtent;

public class JbsAccPaneButton extends Button {

    private static final long serialVersionUID = 6726730216871072268L;

    private Logger logger = Logger.getLogger(JbsAccPaneButton.class);

    private boolean selected;

    private Color stdColor;

    /**
     * Cretaes a button for the <code>JbsAccordionPane</code>
     * @param imgFileName The filename of the icon - relative to <code>Styles.getIconPath()</code>
     * @param title The title of the button
     */
    public JbsAccPaneButton(String imgFileName, String title) {
        super();
        try {
            ResourceImageReference ref = new ResourceImageReference(Styles.getIconPath() + imgFileName);
            this.setIcon(ref);
        } catch (Exception e) {
            logger.debug(Styles.getIconPath() + imgFileName + " not found.");
        }
        this.setStyleName("Default");
        this.setText(title);
        this.setWidth(new JbsExtent(100, JbsExtent.PERCENT));
        this.setStdColor(this.getBackground());
        this.setAlignment(new Alignment(Alignment.LEFT, Alignment.CENTER));
    }

    public void setSelected(boolean value) {
        this.selected = value;
        if (this.isSelected()) this.setBackground(Color.LIGHTGRAY); else this.setBackground(this.getStdColor());
    }

    public boolean isSelected() {
        return this.selected;
    }

    public Color getStdColor() {
        return stdColor;
    }

    public void setStdColor(Color stdColor) {
        this.stdColor = stdColor;
    }
}
