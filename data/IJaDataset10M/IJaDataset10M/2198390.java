package org.softsmithy.lib.swing;

import java.awt.*;
import javax.swing.*;
import org.softsmithy.lib.swing.icon.*;

/**
 * JXIconCustomizer allows visual scaling of icons.
 * @author  puce
 */
public class JXIconCustomizer extends JCustomizer {

    /** Creates a new instance of this class */
    public JXIconCustomizer() {
        this(new JXIconLabel());
        getXIconLabel().setText(null);
        getXIconLabel().setHorizontalAlignment(SwingConstants.CENTER);
        initForDefaultLabel();
    }

    /** Creates a new instance of this class */
    public JXIconCustomizer(JXIconLabel label) {
        super(label);
    }

    /** Creates a new instance of this class */
    public JXIconCustomizer(XIcon icon) {
        this(new JXIconLabel(icon));
        initForDefaultLabel();
    }

    private void initForDefaultLabel() {
        setBackground(Color.WHITE);
        getXIconLabel().setZoomingStrategy(FullZooming.NON_RESPECTING_ASPECT_RATIO_INSTANCE);
    }

    /** Getter for property imageSrc.
   * @return Value of property imageSrc.
   *
   */
    public XIcon getXIcon() {
        return getXIconLabel().getXIcon();
    }

    public void setXIcon(XIcon icon) {
        getXIconLabel().setXIcon(icon);
    }

    /** Setter for property fComponent.
   * @param fComponent New value of property fComponent.
   *
   */
    @Override
    public void setComponent(JComponent component) {
        if (!(component instanceof JXIconLabel)) {
            throw new IllegalArgumentException("comp must be a JXIconLabel");
        }
        JXIconLabel label = (JXIconLabel) component;
        super.setComponent(label);
    }

    public void setXIconLabel(JXIconLabel label) {
        setComponent(label);
    }

    public JXIconLabel getXIconLabel() {
        return (JXIconLabel) getComponent();
    }
}
