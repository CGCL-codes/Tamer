package jaxe;

import org.w3c.dom.Element;

/**
 * Le gestionnaire d'erreurs affiche les erreurs aux utilisateurs.
 * Il peut être remplacé pour une meilleure gestion d'erreurs, comme
 * avec l'affichage de bulles d'aides.
 */
public interface InterfaceGestionErreurs {

    /**
     * L'utilisateur a essayé d'ajouter un élément avant ou après la racine
     * @param refElement Référence vers l'élément que l'utilisateur a essayé d'ajouter
     */
    public void pasSousLaRacine(Element refElement);

    /**
     * L'utilisateur a essayé d'ajouter un élément dans un élément qui n'est pas éditable
     * @param parent L'élément Jaxe édité
     * @param refElement Référence vers l'élément que l'utilisateur a essayé d'ajouter
     */
    public void editionInterdite(JaxeElement parent, Element refElement);

    /**
     * Un enfant n'a pas été inséré parce-qu'il n'est pas autorisé sous le parent
     * @param refParent L'élément Jaxe du parent
     * @param defbalise Référence vers l'élément que l'utilisateur a essayé d'insérer
     */
    public void enfantInterditSousParent(JaxeElement parent, Element refElement);

    /**
     * L'enfant est interdit à cet endroit d'après l'expression régulière de l'élément parent.
     * @param expr Expression régulière de l'élément parent
     * @param parent L'élément Jaxe du parent
     * @param refElement Référence vers l'élément que l'utilisateur a essayé d'insérer
     */
    public void insertionImpossible(String expr, JaxeElement parent, Element refElement);

    /**
     * Le texte n'est pas autorisé sous cet élément
     * @param je L'élément Jaxe sous lequel le texte n'est pas autorisé
     */
    public void texteInterdit(JaxeElement parent);
}
