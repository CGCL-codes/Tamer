package programme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * @author port
 *
 */
public class Solution {

    private HashMap<Variable, Double> valeurs;

    private Double resultat;

    /**
	 * Solution avec toutes les variables à zéro et le résultat à -1 
	 */
    public Solution(ArrayList<Variable> variables) {
        resultat = Double.MAX_VALUE;
        valeurs = new HashMap<Variable, Double>();
        for (Variable v : variables) {
            valeurs.put(v, 0.0);
        }
    }

    /**
	 * Constructeur par copie
	 * @param s
	 */
    public Solution(Solution s) {
        resultat = s.getResultat();
        valeurs = new HashMap<Variable, Double>(s.valeurs);
    }

    public HashMap<Variable, Double> getValeurs() {
        return valeurs;
    }

    public void setValeurs(HashMap<Variable, Double> valeurs) {
        this.valeurs = valeurs;
    }

    public Double getResultat() {
        return resultat;
    }

    public void setResultat(Double resultat) {
        this.resultat = resultat;
    }

    public void raz() {
        for (Variable v : valeurs.keySet()) valeurs.put(v, 0.0);
        resultat = Double.MAX_VALUE;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("objectif = " + resultat + "\n");
        buf.append("var : \n");
        for (Entry<Variable, Double> e : valeurs.entrySet()) {
            buf.append(e.getKey().getPrefixe().getNom());
            buf.append(e.getKey().getNum());
            buf.append(" = ");
            buf.append(e.getValue());
            buf.append("\n");
        }
        return buf.toString();
    }

    public String StringChoix() {
        StringBuffer buf = new StringBuffer();
        buf.append("objectif = " + resultat + "\n");
        buf.append("var : \n");
        for (Entry<Variable, Double> e : valeurs.entrySet()) {
            if (e.getKey().getPrefixe().getNom() == "Choix") {
                buf.append(e.getKey().getPrefixe().getNom());
                buf.append(e.getKey().getNum());
                buf.append(" = ");
                buf.append(e.getValue());
                buf.append("\n");
            }
        }
        return buf.toString();
    }

    public boolean equals(Object o) {
        if (o instanceof Solution) {
            Solution s = (Solution) o;
            if (s.resultat != resultat || s.valeurs.size() != valeurs.size()) {
                return false;
            }
            for (Variable var : valeurs.keySet()) {
                if (!s.valeurs.containsKey(var) || s.valeurs.get(var) != valeurs.get(var)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
