package JUnit.CompareXML;

import java.io.PrintStream;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Node;

/**
 * Classe permettant de comparer les �l�ments d'un fichier XML. Cette classe est
 * g�r�e par {@link XMLComparator}.
 */
class ElementParse {

    /**
	 * Liste des attributs d'un �l�ment XML (voir {@link AttributeParse})
	 */
    List<AttributeParse> attributes = new Vector<AttributeParse>();

    /**
	 * Liste des �l�ments fils (voir {@link ElementParse})
	 */
    List<ElementParse> elements = new Vector<ElementParse>();

    /**
	 * bool�en permettant de savoir si les fils de l'�lement courant doivent
	 * �tre dans un ordre pr�cis ou non. Si sorted est donc � true, les fils de
	 * cet �l�ment ne pourront pas �tre interchang�s.
	 */
    boolean sorted;

    /**
	 * vrai noeud XML {@link Node}
	 */
    Node node;

    /**
	 * Constructeur vide
	 */
    public ElementParse() {
    }

    /**
	 * Constructeur prenant un vrai noeud XML ({@link Node}) en entr�e
	 * 
	 * @param node
	 */
    public ElementParse(Node node) {
        this.node = node;
    }

    /**
	 * Getter de l'attribut name
	 * 
	 * @return {@link String}
	 */
    public String getName() {
        return node.getNodeName();
    }

    /**
	 * Getter de l'attribut value
	 * 
	 * @return {@link String}
	 */
    public String getValue() {
        if (node.getChildNodes().getLength() == 1) {
            return node.getTextContent();
        } else {
            return null;
        }
    }

    /**
	 * Getter de l'attribut node
	 * 
	 * @return {@link Node}
	 */
    public Node getElement() {
        return node;
    }

    /**
	 * Setter de l'attribut node
	 * 
	 * @param element
	 */
    public void setElement(Node element) {
        this.node = element;
    }

    /**
	 * Setter de l'attribut sorted
	 * 
	 * @param sorted
	 */
    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    /**
	 * Getter de l'attribut sorted
	 * 
	 * @return boolean
	 */
    public boolean isSorted() {
        return sorted;
    }

    /**
	 * Permet de r�cup�rer la liste de tous les �lements fils
	 * 
	 * @return List<{@link ElementParse}>
	 */
    public List<ElementParse> getElements() {
        return elements;
    }

    /**
	 * Permet de cr�er tous les noeuds �l�ments. Cette m�thode est r�cursive et
	 * doit �tre utilis�e � l'initialisation. Toute la hi�rachie des �lements et
	 * de leurs fils sera cr��e. Le texte vide n'est pas pris en compte.
	 */
    public void createNodes() {
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            if (!node.getChildNodes().item(i).getNodeName().equals("#text")) {
                ElementParse element = XMLComparator.getInstance().createNode(node.getChildNodes().item(i));
                elements.add(element);
                element.createNodes();
            }
        }
    }

    /**
	 * Affichage des �lements. M�thode r�cursive. Tout l'arbre sera donc
	 * affich�. Sortie pr�cis�e par l'affichage avec un {@link PrintStream}
	 * 
	 * @param ps
	 */
    public void printNodes(PrintStream ps) {
        for (ElementParse element : elements) {
            if (ps == null) {
                Logger.getLogger("XMLComparator").log(Level.INFO, element + "");
            } else {
                ps.println(element);
            }
            element.printNodes(ps);
        }
    }

    @Override
    public String toString() {
        return "Node " + getName() + " " + getValue();
    }

    /**
	 * Surchage de la m�thode equals. Permet de comparer deux elements
	 * ElementParse entre eux. M�thode r�cursive. Utilisation de la classe
	 * {@link Logger} pour logger les �v�nement sur
	 * Logger.getLogger("XMLComparator")
	 * 
	 * @return boolean - true si les deux elements sont identiques. false sinon.
	 */
    @Override
    public boolean equals(Object obj) {
        Logger.getLogger("XMLComparator").log(Level.INFO, this + " -------------------- " + obj);
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }
        ElementParse elementParse = (ElementParse) obj;
        boolean equals = false;
        if (elements.size() == 0 && elementParse.getElements().size() == 0) {
            Logger.getLogger("XMLComparator").log(Level.INFO, this + " ---- Size==0");
            if (getName().equals(elementParse.getName()) && getValue().equals(elementParse.getValue())) {
                Logger.getLogger("XMLComparator").log(Level.INFO, this + " ---- Size==0 -> Identique avec " + elementParse);
                equals = true;
            }
        } else {
            if (elements.size() == elementParse.getElements().size()) {
                Logger.getLogger("XMLComparator").log(Level.INFO, this + " ---- Meme taille");
                if (!sorted) {
                    int winningSize = elements.size();
                    int valCurrent = 0;
                    for (ElementParse element : elements) {
                        Logger.getLogger("XMLComparator").log(Level.INFO, this + " ---- contain ? " + element + " in " + elementParse.getElements());
                        ElementParse res = containElement(element, elementParse.getElements());
                        if (res != null) {
                            valCurrent++;
                        } else {
                            Logger.getLogger("XMLComparator").log(Level.INFO, this + " ---- BREAK");
                            break;
                        }
                        Logger.getLogger("XMLComparator").log(Level.INFO, this + " ---- " + valCurrent);
                    }
                    if (valCurrent == winningSize) {
                        Logger.getLogger("XMLComparator").log(Level.INFO, this + " ---- (winning size) meme element que " + elementParse);
                        equals = true;
                    } else {
                        Logger.getLogger("XMLComparator").log(Level.INFO, this + " ---- (winning size) different de " + elementParse);
                    }
                } else {
                    Logger.getLogger("XMLComparator").log(Level.INFO, "SORTED");
                    int winningSize = elements.size();
                    int valCurrent = 0;
                    for (int i = 0; i < elements.size(); i++) {
                        Logger.getLogger("XMLComparator").log(Level.INFO, "## " + elements.get(i) + " " + elementParse.getElements().get(i));
                        if (elements.get(i).equals(elementParse.getElements().get(i))) {
                            Logger.getLogger("XMLComparator").log(Level.INFO, "true");
                            valCurrent++;
                        } else {
                            Logger.getLogger("XMLComparator").log(Level.INFO, "false");
                            break;
                        }
                    }
                    if (valCurrent == winningSize) {
                        Logger.getLogger("XMLComparator").log(Level.INFO, "valCurrent == winningSize");
                        equals = true;
                    }
                }
            } else {
                Logger.getLogger("XMLComparator").log(Level.INFO, this + " ---- Taille completement differente");
            }
        }
        if (equals) {
            Logger.getLogger("XMLComparator").log(Level.INFO, this + " EQUALS " + obj);
        } else {
            Logger.getLogger("XMLComparator").log(Level.INFO, this + " NOT EQUALS " + obj);
        }
        return equals;
    }

    /**
	 * M�thode permettant de savoir si un �l�ment est contenu dans une liste
	 * d'�l�ments donn�s. Utile si le bool�en sorted est false et que les
	 * �l�ments peuvent donc �tre dans le d�sordre. Utilisation de la classe
	 * {@link Logger} pour logger les �v�nement sur
	 * Logger.getLogger("XMLComparator")
	 * 
	 * @param el
	 * @param elementsParse
	 * @return ElementParse
	 */
    private ElementParse containElement(ElementParse el, List<ElementParse> elementsParse) {
        for (ElementParse element : elementsParse) {
            if (element.equals(el)) {
                Logger.getLogger("XMLComparator").log(Level.INFO, this + " ---- search " + el + " found " + element + " in " + elementsParse);
                return element;
            }
        }
        Logger.getLogger("XMLComparator").log(Level.INFO, this + " ---- search " + this + " not found in " + elementsParse);
        return null;
    }
}
