package fca.gui.util.constant;

import javax.swing.ImageIcon;
import fca.gui.util.constant.LMColors.LatticeColor;
import fca.gui.util.constant.LMColors.SelectionColor;

/**
 * Contient toutes les constantes relatives aux images du programme Lattice Minner
 * @author Ludovic Thomas
 * @version 1.0
 */
public final class LMImages {

    /** Chemin des images */
    private static final String PATH_IMAGES = "/" + "images" + "/";

    /** Chemin des images de selection */
    private static final String PATH_SELECTION = "Selection" + "/";

    /** Extension des images */
    private static final String EXTENSION_IMAGES = ".png";

    /** Nom du noeud */
    private static final String NODE = "Node_";

    /** Noeud inactif */
    private static final String INACTIVE_NODE = "GrayNode";

    /** Intensit� par d�faut */
    private static final int DEFAULT_INTENSITY = 3;

    /** Le singleton repr�sentant {@link LMImages} */
    private static LMImages SINGLETON = null;

    private static final String FUZZY_CLOSED = "FuzzyClosed";

    private static final String FUZZY_OPENED = "FuzzyOpened";

    private static final String CLEAR_CLOSED = "ClearClosed";

    private static final String CLEAR_OPENED = "FuzzyOpened";

    /** Pour une selection classique */
    private static final String SELECTION = "Selection";

    /** Pour une selection classique ouverte */
    private static final String OPEN_SELECTION = "OpenSelection";

    /**
	 * Constructeur priv� de l'instance {@link LMImages}
	 */
    private LMImages() {
        SINGLETON = this;
    }

    /**
	 * Permet de recup�rer, ou de cr�er s'il n'existe pas, une unique instance de {@link LMImages}
	 * @return l'instance unique de {@link LMImages}
	 */
    public static LMImages getLMImages() {
        if (SINGLETON == null) {
            SINGLETON = new LMImages();
        }
        return SINGLETON;
    }

    /**
	 * Permet d'obtenir l'image claire et ouverte des noeuds de ce treillis
	 * @param color la couleur du noeud
	 * @param changeIntensity le bool�en indiquant si l'intensi� doit �tre prise en compte
	 * @param intensity l'intensit� du noeud
	 * @return L'ImageIcon associ�e au noeuds du treillis
	 */
    public static ImageIcon getClearOpenedIcon(LatticeColor color, boolean changeIntensity, int intensity) {
        return getImagePathExtension(color, CLEAR_OPENED + color + NODE + DEFAULT_INTENSITY);
    }

    /**
	 * Permet d'obtenir l'image claire et fermee des noeuds de ce treillis
	 * @param color la couleur du noeud
	 * @param changeIntensity le bool�en indiquant si l'intensi� doit �tre prise en compte
	 * @param intensity l'intensit� du noeud
	 * @return L'ImageIcon associ�e au noeuds du treillis
	 */
    public static ImageIcon getClearClosedIcon(LatticeColor color, boolean changeIntensity, int intensity) {
        if (changeIntensity) return getImagePathExtension(color, CLEAR_CLOSED + color + NODE + intensity); else return getImagePathExtension(color, CLEAR_CLOSED + color + NODE + DEFAULT_INTENSITY);
    }

    /**
	 * Permet d'obtenir l'image floue et fermee des noeuds de ce treillis
	 * @param color la couleur du noeud
	 * @return L'ImageIcon associ�e au noeuds du treillis
	 */
    public static ImageIcon getFuzzyOpenedIcon(LatticeColor color) {
        return getImagePathExtension(color, FUZZY_OPENED + color + NODE + DEFAULT_INTENSITY);
    }

    /**
	 * Permet d'obtenir l'image floue et fermee des noeuds de ce treillis
	 * @param color la couleur du noeud
	 * @return L'ImageIcon associ�e au noeuds du treillis
	 */
    public static ImageIcon getFuzzyClosedIcon(LatticeColor color) {
        return getImagePathExtension(color, FUZZY_CLOSED + color + NODE + DEFAULT_INTENSITY);
    }

    /**
	 * Permet d'obtenir l'image floue et inactive des noeuds de ce treillis
	 * @return L'ImageIcon associ�e au noeuds du treillis
	 */
    public static ImageIcon getFuzzyInactiveIcon() {
        return getImagePathExtension(FUZZY_CLOSED + INACTIVE_NODE);
    }

    /**
	 * Permet d'obtenir l'image claire et inactive des noeuds de ce treillis
	 * @return L'ImageIcon associ�e au noeuds du treillis
	 */
    public static ImageIcon getClearInactiveIcon() {
        return getImagePathExtension(CLEAR_CLOSED + INACTIVE_NODE);
    }

    /**
	 * Permet d'obtenir l'image de surbrillance pour un concept ferm�
	 * @param color la couleur du noeud
	 * @return L'ImageIcon associ�e � la surbrillance
	 */
    public static ImageIcon getClosedHighlightIcon(SelectionColor color) {
        return getImagePathExtension(PATH_SELECTION + color + SELECTION);
    }

    /**
	 * Permet d'obtenir l'image de selection pour un concept ferm�
	 * @param color la couleur du noeud
	 * @return L'ImageIcon associ�e � la s�lection
	 */
    public static ImageIcon getOpenedHighlightIcon(SelectionColor color) {
        return getImagePathExtension(PATH_SELECTION + color + OPEN_SELECTION);
    }

    /**
	 * Permet d'obtenir l'image de s�lection pour un concept ouvert
	 * @param color la couleur du noeud
	 * @return L'ImageIcon associ�e � la s�lection
	 */
    public static ImageIcon getOpenedSelectionIcon(SelectionColor color) {
        return getImagePathExtension(PATH_SELECTION + color + OPEN_SELECTION);
    }

    /**
	 * Permet d'obtenir l'image de s�lection pour un concept ferm�
	 * @param color la couleur du noeud
	 * @return L'ImageIcon associ�e � la s�lection
	 */
    public static ImageIcon getClosedSelectionIcon(SelectionColor color) {
        return getImagePathExtension(PATH_SELECTION + color + SELECTION);
    }

    /**
	 * Permet d'obtenir l'image avec le path complet et l'extension usuelle
	 * @param content la chaine de caractere representant un path d'une image sans couleur
	 * @return L'ImageIcon associ�e � l'image
	 */
    private static ImageIcon getImagePathExtension(String content) {
        String path = PATH_IMAGES + content + EXTENSION_IMAGES;
        return new ImageIcon(SINGLETON.getClass().getResource(path));
    }

    /**
	 * Permet d'obtenir l'image avec le path complet et l'extension usuelle
	 * @param la couleur du noeud a prendre en compte comme dossier
	 * @param content la chaine de caractere representant un path d'une image sans couleur
	 * @return L'ImageIcon associ�e � l'image
	 */
    private static ImageIcon getImagePathExtension(LatticeColor color, String content) {
        String path = PATH_IMAGES + color + "/" + content + EXTENSION_IMAGES;
        return new ImageIcon(SINGLETON.getClass().getResource(path));
    }
}
