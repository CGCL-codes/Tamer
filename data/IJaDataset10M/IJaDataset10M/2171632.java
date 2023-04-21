package com.celiosilva.swingDK.buttons;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author celio@celiosilva.com
 */
public class BtnAlterar extends SwingDKButton {

    /** Creates a new instance of BtnAlterar */
    public BtnAlterar() {
        Icon icone = new ImageIcon(this.getClass().getResource("/com/celiosilva/swingDK/images/refresh24.png"));
        this.setIcon(icone);
        this.setMessage("Alterar Registro");
    }
}
