package arbre;

import java.util.HashMap;

/**
 *
 * @author ksz
 */
public class NoeudFeuille extends Noeud {

    private Element contenu;

    public NoeudFeuille() {
    }

    public NoeudFeuille(Element contenu) {
        this.contenu = contenu;
    }

    public Integer getPrioritee() {
        return getContenu().getPrioritee();
    }

    public void afficherCodeBinaireCharacter(boolean first, String code) {
    }

    public Element getContenu() {
        return contenu;
    }

    @Override
    public boolean estFeuille() {
        return true;
    }

    @Override
    public void creerBitsCode(StringBuffer code, HashMap bitsCodeDictionnaire) {
        bitsCodeDictionnaire.put(contenu.getContenu(), code.toString());
    }
}
