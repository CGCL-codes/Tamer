package com.loribel.commons.business.abstraction;

import com.loribel.commons.abstraction.GB_LabelIcon;

/**
 * Interface pour d�finir des r�gles pour import/export de valeurs d'un BO.
 * <br/> 
 * Cette abstraction permet de d�finir :
 * <ul>
 *   <li>le mapping pour construire une table � partir d'une liste de BO</li>
 *   <li>le mapping pour l'import/export de valeurs d'un BO avec un format CSV.</li>
 * </ul>
 * 
 * @author Gregory Borelli
 */
public interface GB_BOPropertyLinkMap {

    /**
     * Retourne les keys des colonnes � exporter (format libre)
     */
    String[] getKeys();

    /**
     * Retourne le labelIcon associ� � une key, peut �tre null si non utile.
     * Utile dans le cas d'une repr�sentation en table.
     */
    GB_LabelIcon getLabelIcon(String a_key);

    /**
     * Retourne le lien � une valeur. Ca peut �tre simplement un nom de propri�t�,
     * ou pour des propri�t� plus complexes des noms de propri�t�s s�par�s par des '.', ...
     * Voir GB_BOPropertyLinkTools pour plus de d�tails.
     */
    String getPropertyLink(String a_key);

    /**
     * Retourne si la key est editable.
     */
    boolean isEditableKey(String a_key);
}
