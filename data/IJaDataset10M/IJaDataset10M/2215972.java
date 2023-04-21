package Xml;

import java.util.ArrayList;
import java.util.Iterator;
import org.jdom.Element;
import Exception.InvalideScriptSyntaxException;

public class ScriptXML extends LecteurXML {

    private ArrayList<Element> listeEtape = new ArrayList<Element>();

    private int indice = 0;

    /**
	 * Initialise le script xml en v�rifiant qu'il comporte au minimum
	 * la balise <script> et une <etape> au minimum.
	 * 
	 * @param url chemin du script � ouvrir.
	 * @throws InvalideScriptSyntaxException est lanc� si le fichier
	 * ne comporte pas les �l�ments minimum pour �tre consid�r� comme
	 * valide.
	 */
    @SuppressWarnings("unchecked")
    public ScriptXML(String url) throws InvalideScriptSyntaxException {
        super(url);
        if (racine.getName() != "script") throw new InvalideScriptSyntaxException("<script> introuvable");
        if (!racine.getChildren("etape").isEmpty()) {
            super.lstElement = racine.getChildren("etape");
            super.iter_lstElement = this.lstElement.iterator();
            while (iter_lstElement.hasNext()) {
                RemplirEtape(iter_lstElement.next());
            }
            super.courant = listeEtape.get(indice);
        } else {
            throw new InvalideScriptSyntaxException("<etape> introuvables");
        }
    }

    @SuppressWarnings("unchecked")
    private void RemplirEtape(Element courant) {
        listeEtape.add(courant);
        Iterator<Element> liste_etp = courant.getChildren("etape").iterator();
        while (liste_etp.hasNext()) {
            RemplirEtape(liste_etp.next());
        }
    }

    /**
	 * R�initialise le script au d�but.
	 * 
	 * @deprecated
	 */
    @SuppressWarnings("unchecked")
    public void reinitialiser() {
        super.reinitialiser();
        super.lstElement = racine.getChildren("etape");
        super.iter_lstElement = this.lstElement.iterator();
    }

    /**
	 * Cette fonction avance le pointeur sur l'�tape suivante ou pr�c�dente
	 * et la renvoi.
	 * 
	 * @param avance Si "true", s�lectionne la suivante. Sinon renvoi la pr�c�dante.
	 * @return l'�tape suivante ou pr�c�dente.
	 * @deprecated utiliser getEtapeNext() ou getEtapePrevious()
	 */
    public Element getEtape(Boolean avance) {
        if (avance) {
            return getEtapeNext();
        } else {
            return getEtapePrevious();
        }
    }

    /**
	 * Sert � obtenir directement l'�tape qui poss�de l'indice de notre
	 * choix. Elle modifie le pointeur indice. 
	 * 
	 * @param position est la position de l'�tape qu'on souhaite obtenir
	 * @return l'�tape si la position est valide
	 */
    public Element getEtape(int position) {
        if ((position < listeEtape.size()) && (position >= 0)) {
            this.indice = position;
            super.courant = listeEtape.get(position);
            return super.courant;
        }
        return null;
    }

    /**
	 * Fait pointer l'attribut courant sur l'�tape suivante.
	 * Augmente l'indice.
	 * 
	 * @return l'�tape suivant si elle existe, sinon null.
	 */
    public Element getEtapeNext() {
        if (indice < (listeEtape.size() - 1)) {
            indice++;
            super.courant = listeEtape.get(indice);
            return super.courant;
        }
        return null;
    }

    /**
	 * Fait pointer l'attribut courant sur l'�tape pr�c�dante.
	 * Diminue l'indice.
	 * 
	 * @return l'�tape pr�c�dante si elle existe, sinon null.
	 */
    public Element getEtapePrevious() {
        if (indice > 0) {
            indice--;
            super.courant = listeEtape.get(indice);
            return super.courant;
        }
        return null;
    }

    /**
	 * Revoie le nombre d'�l�ments contenu dans la liste
	 * 
	 * @return le nombre d'�l�ments
	 */
    public int getLongueurListe() {
        return listeEtape.size();
    }

    /**
	 * Revoie l'indice de la derni�re �tape demand�.
	 * 
	 * @return l'indice
	 */
    public int getIndiceEtape() {
        return this.indice;
    }
}
