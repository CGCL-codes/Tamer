package ia;

import java.util.Vector;
import graphes.CheminDeFer;
import edu.uci.ics.jung.graph.impl.DirectedSparseVertex;

public class EtatI {

    private int nbCartesWagonsCouleur[] = new int[8];

    private int nbWagons;

    private Vector cheminsAposer = new Vector();

    private Vector cheminsPosables = new Vector();

    private ActionI vientDe;

    private Vector partVers = new Vector();

    private int ptMin = -1, ptMax = -1, points;

    private int profondeur;

    public EtatI(int[] nbc, int nbw, Vector c, ActionI v, int p) {
        nbCartesWagonsCouleur = nbc;
        nbWagons = nbw;
        cheminsAposer = c;
        VerifCheminsPosables();
        vientDe = v;
        profondeur = p;
    }

    public void VerifCheminsPosables() {
        cheminsPosables = new Vector();
        if (this.cheminsAposer != null) {
            for (int i = 0; i < this.cheminsAposer.size(); i++) {
                CheminDeFer cf = (CheminDeFer) cheminsAposer.get(i);
                int couleur = cf.getIntCouleur();
                if (couleur == 8) {
                    couleur = TrouveMaxCartesCouleur();
                }
                if (nbCartesWagonsCouleur[couleur] >= cf.getValuation()) {
                    cheminsPosables.add(cf);
                }
            }
        }
    }

    public Vector CheminsPosables() {
        return cheminsPosables;
    }

    public int TrouveMaxCartesCouleur() {
        int Max = 0;
        int MaxI = 0;
        for (int i = 0; i < 8; i++) {
            if (Max < this.nbCartesWagonsCouleur[i]) {
                MaxI = i;
                Max = nbCartesWagonsCouleur[i];
            }
        }
        return MaxI;
    }

    public String GetNbCouleurs() {
        String s = "[";
        for (int i = 0; i < 8; i++) {
            s += nbCartesWagonsCouleur[i] + ",";
        }
        return s + "]";
    }

    public int[] GetNbCartesWagonsCouleurs() {
        return this.nbCartesWagonsCouleur;
    }

    public Vector GetChemins() {
        return cheminsAposer;
    }

    public int GetNbWagons() {
        return this.nbWagons;
    }

    public void AjouterBranche(ActionI a) {
        partVers.add(a);
    }

    public int CalculMin() {
        if (partVers.size() == 0) {
            CalculMinMax();
            return ptMin;
        } else {
            int min = 999;
            for (int i = 0; i < partVers.size(); i++) {
                ActionI a = (ActionI) partVers.get(i);
                min = Math.min(min, a.getDest().CalculMin());
            }
            ptMin = min;
            return ptMin;
        }
    }

    public int CalculMax() {
        if (partVers.size() == 0) {
            CalculMinMax();
            return ptMax;
        } else {
            int max = 0;
            for (int i = 0; i < partVers.size(); i++) {
                ActionI a = (ActionI) partVers.get(i);
                max = Math.max(max, a.getDest().CalculMax());
            }
            ptMax = max;
            return ptMax;
        }
    }

    public void CalculMinMax() {
        if (ptMax == -1) {
            ptMax = (int) (Math.random() * 100) + 1;
            ptMin = ptMax;
        }
    }

    public int getMin() {
        return ptMin;
    }

    public int getMax() {
        return ptMax;
    }

    public String toString() {
        return ptMin + " " + ptMax;
    }

    public int getProfondeur() {
        return profondeur;
    }

    public Vector getDests() {
        return partVers;
    }
}
