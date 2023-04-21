package outils.regles;

/** décrit une règle dans nat */
public abstract class Regle {

    /** Description de la règle ou catégorie de règle
	 * <p>Exemples:
	 * 	<ul>
	 * 		<li>"Locution",</li>
	 * 		<li> "Le préfixe CONTRE ne s'abrège que lorsqu'il est suivi d'un trait d'union")</li>
	 * 	</ul>
	 * </p>
	 **/
    protected String description = "";

    /**
	 * Référence de la règle (pour l'instant, issue du manuel d'abrégé de l'AVH).
	 * <p>Conventions de nommage:
	 * 	<ul>
	 * 		<li>Chapitre: <strong>chiffres romains</strong>: la règle est définie dans un chapître (par exemple, locution, mots abrégés par un seul signe, etc)</li>
	 * 		<li>Règle d'abréviation: <strong>chiffre arabes</strong>: n° de la règle dans le manuel</li>
	 * 		<li>Règle non présente dans le manuel: <strong>Source de la règle + référence dans la source</strong>: par exemple "Méthode Le Rest/Perdoux, édition 2008, page x, §y</li>
	 * 	</ul>
	 * <p>
	 * <p>Il est recommandé de donner une référence complète en rappelant le n° du chapitre: par exemple, préférer "II-19" plutôt que "19"</p>
	 */
    protected String reference = "";

    /**
	 * Vrai si la règle est active dans la configuration
	 */
    protected boolean actif = true;

    /**
	 * @param d la description de la règle
	 * @param ref la référence de la règle 
	 * @param a true si règle active
	 */
    public Regle(String d, String ref, boolean a) {
        description = d;
        reference = ref;
        actif = a;
    }

    /**
	 * @param d la description de la règle
	 * @param ref la référence de la règle 
     */
    public Regle(String d, String ref) {
        description = d;
        reference = ref;
        actif = true;
    }

    /**
	 * Pour obliger la redéfinition de toString() de la Classe Object
	 * @return une chaine représentant la règle
	 */
    @Override
    public abstract String toString();

    /**
	 * Vérifie si deux règles sont identiques
	 * @param o instance d'Object à comparer
	 * @return vrai si les règles sont identiques
	 */
    @Override
    public abstract boolean equals(Object o);

    /**
	 * Renvoie un noeud xml sous forme de chaine représentant la règle
	 * @return une chaine xml représentant la règle
	 */
    public abstract String getXML();

    /**
     * @param a valeur pour #actif
     */
    public void setActif(boolean a) {
        actif = a;
    }

    /**
     * @return valeur de #actif
     */
    public boolean isActif() {
        return actif;
    }
}
