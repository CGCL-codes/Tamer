package com.celiosilva.swingDK.buttons;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author celio@celiosilva.com
 */
public class BtnProximo extends SwingDKButton {

    /** Creates a new instance of BtnProximo */
    public BtnProximo() {
        Icon icone = new ImageIcon(this.getClass().getResource("/com/celiosilva/swingDK/images/navigate_right24.png"));
        this.setIcon(icone);
        this.setMessage("Avan�ar para o Pr�ximo Registro");
    }
}
