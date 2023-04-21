package modele;

import java.util.Vector;

public class SystemeDeSimplexe2D {

    private Vector<Equation2D> listeEquations;

    private FonctionAOptimiser2D fonctionOpt;

    public SystemeDeSimplexe2D() {
        listeEquations = new Vector<Equation2D>();
    }

    public Vector<Equation2D> getListeEquations() {
        return listeEquations;
    }

    public FonctionAOptimiser2D getFonctionAOptimiser2D() {
        return fonctionOpt;
    }

    public void setListeEquations(Vector<Equation2D> listeEquations) {
        this.listeEquations = listeEquations;
    }

    public void setFonctionAOptimiser2D(FonctionAOptimiser2D fct) {
        this.fonctionOpt = fct;
    }

    public void ajouterLibelleContrainte(int index, String libelle) {
        listeEquations.get(index).setLibelle(libelle);
    }

    public void ajouterEquation(Equation2D eq) {
        listeEquations.addElement(eq);
    }

    public void ajouterEquationAt(Equation2D eq, int index) {
        listeEquations.insertElementAt(eq, index);
    }

    public void modifierEquation(Equation2D neweq, int index) {
        Equation2D eq = listeEquations.get(index);
        eq.setX1(neweq.getX1());
        eq.setX2(neweq.getX2());
        eq.setPartieDroite(neweq.getPartieDroite());
        eq.setOperateur(neweq.getOperateur());
        eq.setCouleur(neweq.getCouleur());
    }

    public void supprimerEquation(int index) {
        listeEquations.removeElementAt(index);
    }
}
