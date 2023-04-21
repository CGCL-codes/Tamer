package fr.kb.frais.control.menu.interne;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import fr.kb.frais.modele.date.Mois;
import fr.kb.frais.modele.object.Personne;
import fr.kb.frais.vue.FraisDepVue;
import fr.kb.frais.vue.tarif.InternalFichePersonne;
import fr.kb.frais.vue.tarif.InternalFichePersonneFactory;

public class OuvrirFichePersonneAdapter implements ActionListener {

    private Personne personne;

    private Mois mois;

    public OuvrirFichePersonneAdapter(Personne personne, Mois mois) {
        this.personne = personne;
        this.mois = mois;
    }

    public void actionPerformed(ActionEvent arg0) {
        InternalFichePersonne fiche = InternalFichePersonneFactory.getInstance(personne, mois);
        if (fiche == null) {
            fiche = new InternalFichePersonne(personne, mois);
            InternalFichePersonneFactory.put(personne, fiche);
        }
        if (FraisDepVue.getDesktop().getIndexOf(fiche) == -1) {
            try {
                FraisDepVue.getDesktop().add(fiche);
                fiche.setVisible();
            } catch (Exception e) {
            }
        }
    }
}
