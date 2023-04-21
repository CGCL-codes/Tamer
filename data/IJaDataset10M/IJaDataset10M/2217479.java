package net.sf.ideoreport.reportgenerator.config;

import java.io.Serializable;

/**
 * Repr�sente la configuration d'un gestionnaire de donn�es.
 * Un gestionnaire de donn�es est compos� de:
 * <ol>
 *      <li>Le type de gestionnaire de donn�es (SQL...)
 *      <li>La classe qui impl�mente le gestionnaire de donn�es
 * </ol>
 *
 * @author roset
 */
public interface IDataManagerConfig extends Serializable {

    /**
     * @return la valeur de l'�l�ment
     */
    String getType();

    /**
     * @return la valeur de l'�l�ment
     */
    String getClassName();

    /**
     * @param pType la nouvelle valeur de l'�l�ment
     */
    void setType(String pType);

    /**
     * @param pImplementationClass la nouvelle valeur de l'�l�ment
     */
    void setClassName(String pImplementationClass);
}
