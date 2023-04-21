package org.vue.panneau.listener;

import java.awt.event.ActionEvent;
import org.modele.entity.Utilisateur;
import org.vue.fenetre.Fenetre;
import org.vue.panneau.PrincipalPan;

public class ProfilPanAnnulerListener extends PanneauBoutonListener {

    Utilisateur user;

    public ProfilPanAnnulerListener(Fenetre f, Utilisateur u) {
        super(f);
        user = u;
    }

    public void actionPerformed(ActionEvent e) {
        fenetre.setPanneau(new PrincipalPan(fenetre, user));
    }
}
