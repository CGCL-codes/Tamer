package org.vue.panneau.listener;

import java.awt.event.ActionEvent;
import org.vue.fenetre.Fenetre;
import org.vue.panneau.LoginPan;

public class OubliLoginPanAnnulerListener extends PanneauBoutonListener {

    public OubliLoginPanAnnulerListener(Fenetre f) {
        super(f);
    }

    public void actionPerformed(ActionEvent e) {
        fenetre.setPanneau(new LoginPan(fenetre));
    }
}
