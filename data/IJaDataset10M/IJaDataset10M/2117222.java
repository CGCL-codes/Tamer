package ch.eivd.sas.teamedit.document;

import ch.eivd.sas.teamedit.event.DocumentListener;
import ch.eivd.sas.teamedit.exception.IllegalIdException;
import javax.swing.text.StyledDocument;

/**
 * Cette interface d�finit un document de TeamEdit
 *
 * @author Daniel Lifschitz
 */
public interface Document extends DocumentAttribute {

    /**
     * Ajoute le texte pass� en param�tre en l'attribuant � l'utilisateur
     * sp�cifi�.
     * @param userId - l'utilisateur ayant effectu� la modification
     * @param text - le text ajout�
     * @throws IllegalIdException - est lev�e si l'utilisateur
     * sp�cifi� ne travail pas sur le document
     */
    public void addText(int userId, int pos, String text) throws IllegalIdException;

    /**
     * Supprime offset caract�res � partir de la position associ�e
     * � l'utilisateur sp�cifi�. Un offset n�gatif indique que l'effacement
     * se situe � gauche du curseur alors qu'un offset positif indique
     * une suppression � droite.
     * @param userId - l'utlisateur ayant effectu� la modification.
     * @param offset - le nombre de caract�res � effacer.
     * @throws IllegalIdException - est lev�e si l'utilisateur
     * sp�cifi� ne travail pas sur le document.
     */
    public void removeText(int userId, int pos, int offset) throws IllegalIdException, IllegalArgumentException;

    /**
     * S�lectionne une portion du texte.
     * @param userId - l'utilisateur ayant s�lectionn� du texte.
     * @param offset - le nombre de caract�re s�lectionn�. Un nombre n�gatif
     * indique une s�lection � gauche du curseur et un nombre positif � droite.
     * @throws IllegalIdException - est lev�e si l'utilisateur
     * sp�cifi� ne travail pas sur le document.
     */
    public void selectText(int userId, int offset) throws IllegalIdException;

    /**
     * D�place le curseur de l'utilisateur a la position sp�cifi�e.
     * @param userId - l'utilisateur ayant d�plac� son curseur.
     * @param newPos - la nouvelle position du curseur.
     * @throws IllegalIdException - est lev�e si l'utilisateur
     * sp�cifi� ne travail pas sur le document.
     */
    public void moveCursor(int userId, int newPos) throws IllegalIdException;

    /**
     * Retourne la liste des utilisateurs travaillant sur le document.
     * @return - la liste des utilisateurs travaillant sur le document.
     */
    public List getWorkerList();

    /**
     * Ajoute un listener permettant de recevoir les modifications apport�es
     * au document
     * @param l - le listener invoqu� lors d'une modification.
     */
    public void addDocumentListener(DocumentListener l);

    /**
     * Supprime un listener permettant de recevoir les modifications apport�es
     * au document.
     * @param l - le listener invoqu� lors d'une modification.
     */
    public void removeDocumentListenr(DocumentListener l);

    /**
     * Retourne le contenu du document.
     * @return - le contenu du document,
     */
    public StyledDocument getStyledDocument();

    /**
     * Recherche la premi�re occurence du texte pass� en param�tre
     * @param text - le texte � rechercher
     * @return - vrai si le texte a �t� trouv�
     */
    public boolean find(String text);

    /**
     * Remplace toute les occurence oldtext par newText
     * @param oldText - le texte � rechercher
     * @param newText - le texte � remplacer
     */
    public void replace(String oldText, String newText);

    /**
     * Enregistre le document dans le fichier courant
     */
    public void save();

    /**
     * Enregistre le document sous un nouveau nom
     */
    public void saveAs();

    /**
     * Imprime le document
     */
    public void print();
}
