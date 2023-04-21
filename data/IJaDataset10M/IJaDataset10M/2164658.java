package org.formaria.svg;

import java.awt.Component;
import javax.swing.JComponent;

/**
 *
 *
 * <p> Copyright (c) Formaria Ltd., 2008, This software is licensed under
 * the GNU Public License (GPL), please see license.txt for more details. If
 * you make commercial use of this software you must purchase a commercial
 * license from Formaria.</p>
 * <p> $Revision: 1.2 $</p>
 */
public class OverlayComponent extends JComponent {

    /** Creates a new instance of ComponentLayer */
    public OverlayComponent() {
        setOpaque(false);
    }

    /**
   * Used to add a component to the component layer.
   * @param comp the <CODE>Component</CODE> to be added.
   * @return the added <CODE>Component</CODE>.
   */
    public Component add(Component comp) {
        add(comp);
        return comp;
    }
}
