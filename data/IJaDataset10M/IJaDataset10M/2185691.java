package pf.alaudes.mastermahjong.ihm.util.local;

import pf.alaudes.mastermahjong.metier.impl.Combinaison;
import pf.alaudes.mastermahjong.metier.stats.impl.CombinaisonGagnante;

public class LocalizedCombinaisonGagnante extends AbstractLocalizedObject {

    private CombinaisonGagnante combinaisonGagnante;

    @Override
    protected void setObject(Object object) {
        this.combinaisonGagnante = (CombinaisonGagnante) object;
    }

    public void ajouter() {
        combinaisonGagnante.ajouter();
    }

    public void ajouter(int nombre) {
        combinaisonGagnante.ajouter(nombre);
    }

    public boolean equals(Object obj) {
        return combinaisonGagnante.equals(obj);
    }

    public Combinaison getCombinaison() {
        return combinaisonGagnante.getCombinaison();
    }

    public Long getIdentifiantObjet() {
        return combinaisonGagnante.getIdentifiantObjet();
    }

    public String getLibelleCombinaison() {
        return getLibelleCombinaisonLocale(combinaisonGagnante.getLibelleCombinaison());
    }

    public int getNombre() {
        return combinaisonGagnante.getNombre();
    }

    public Long getVersion() {
        return combinaisonGagnante.getVersion();
    }

    public int hashCode() {
        return combinaisonGagnante.hashCode();
    }

    public void setCombinaison(Combinaison combinaison) {
        combinaisonGagnante.setCombinaison(combinaison);
    }

    public void setIdentifiantObjet(Long identifiantObjet) {
        combinaisonGagnante.setIdentifiantObjet(identifiantObjet);
    }

    public void setLibelleCombinaison(String libelleCombinaison) {
        combinaisonGagnante.setLibelleCombinaison(libelleCombinaison);
    }

    public void setNombre(int nombre) {
        combinaisonGagnante.setNombre(nombre);
    }

    public void setVersion(Long version) {
        combinaisonGagnante.setVersion(version);
    }

    public String toString() {
        return combinaisonGagnante.toString();
    }
}
