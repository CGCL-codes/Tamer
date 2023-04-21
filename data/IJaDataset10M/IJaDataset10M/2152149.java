package t2s.traitement;

import java.util.regex.*;

/** Une impl�mentation d'une liste simplement chain�e pour des instances de <code>Regle</code>.
 * <p><b>Remarque</b> : les r�gles sont class�es par taille de <code>(suffixe + prefixe)</code>.On applique ainsi la r�gle la plus grande possible en premier</p>
 */
public class ListeRegles {

    /** Le tag correpsondant � la situation o� il n'y a pas de r�gles */
    public static final String PAS_DE_REGLE = "vide";

    ListeRegles suivant;

    Regle tete;

    /** Constructeur de liste vide.
     * <p><b>Remarque</b> : Une liste de r�gles vide n'a ni suivant ni t�te (mis � <code>null</code>).</p>
     */
    public ListeRegles() {
        suivant = null;
        tete = null;
    }

    /** Construit une liste de Regle.
     * @param regle la regle a rajouter en t�te dans la liste
     * @param suivant la liste de r�gles qui suivra <code>regle</code>
     */
    private ListeRegles(Regle regle, ListeRegles suivant) {
        this.suivant = suivant;
        this.tete = regle;
    }

    /** Pour r�cuperer l'�l�ment suivant dans la liste (une liste de r�gles, priv� de la t�te de <code>this<code>)
     * @return l'�l�ment suivant.
     */
    public ListeRegles getListeSuivante() {
        return suivant;
    }

    /** Pour recuperer la regle pr�sente en t�te de liste
     * @return l'�l�ment pr�sent en t�te.
     */
    public Regle getRegle() {
        return tete;
    }

    /** Pour ajouter une r�gle � la liste.
     * <p><b>Remarque</b> : On respecte un ordre d�croissant sur la taille du suffixe et du pr�fixe</p>
     * @param t la regle que l'on veut ajouter dans la liste courante
     */
    public void ajouter(Regle t) {
        if (estVide() || t.priorite() > tete.priorite()) {
            suivant = new ListeRegles(tete, suivant);
            tete = t;
        } else suivant.ajouter(t);
    }

    /** Pour savoir si la liste est vide.
     * @return true si c'est le cas, false sinon.
     */
    public boolean estVide() {
        return tete == null;
    }

    /** Pour trouver les phon�mes associ� � un mot.
     * <p><b>Remarque</b> : On applique la premi�re r�gle qui s'unifie � la sous-chaine du <code>mot</code> se terminant sur <code>indice</code></p>
     * @param mot le mot que l'on veut transformer
     * @param indice l'entier repr�sentant l'indice sur lequel on finit l'analyse.
     * @return  le phon�me correspondant � la partie du mot unifi�e
     */
    public String trouverPhoneme(String mot, int indice) {
        if (estVide()) return PAS_DE_REGLE; else {
            String corps = getRegle().getRacine();
            int debutRacine = indice - corps.length();
            String prefixe = getRegle().getPrefix();
            String suffixe = getRegle().getSuffix();
            Pattern p = Pattern.compile(corps + suffixe);
            Matcher m = p.matcher(mot);
            if (m.find(debutRacine) && m.start() == debutRacine) {
                p = Pattern.compile(prefixe + corps);
                m = p.matcher(mot);
                boolean prefixeOK = false;
                while (!prefixeOK && m.find()) {
                    if (m.end() == indice) prefixeOK = true;
                }
                if (prefixeOK) {
                    return getRegle().getPhoneme();
                }
            }
            return getListeSuivante().trouverPhoneme(mot, indice);
        }
    }

    /** M�thode d'affichage standart d'une liste de r�gles.
     * @return la chaine de caract�res qui va bien ^_^.
     */
    public String toString() {
        if (estVide()) return ""; else return getRegle().toString() + getListeSuivante().toString();
    }
}
