package de.mogwai.common.dao;

import de.mogwai.common.business.entity.Entity;

/**
 * Data Access Object Interface.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-06-17 15:06:38 $
 */
public interface Dao {

    /**
     * Liest eine Gesch�ftsobjekt per ID.
     * 
     * @param entityClass
     *                Typ des Gesch�ftsobjektes.
     * @param id
     *                ID des zu lesenden Gesch�ftsobjektes.
     * @return Gesch�ftsobjekt oder null, falls kein Gesch�ftsobjekt mit dieser
     *         ID vorhanden ist.
     */
    Object getById(Class entityClass, Long id);

    /**
     * Speichert ein Gesch�ftsobjekt.
     * 
     * @param entity
     *                zu speicherndes Gesch�ftsobjekt.
     */
    void save(Object entity);

    /**
     * L�scht ein Gesch�ftsobjekt.
     * 
     * @param entity
     *                zu l�schendes Gesch�ftsobjekt.
     */
    void delete(Object entity);

    /**
     * Neuladen einer Entity Instanz von der Datenbank.
     * 
     * @param entity
     *                Neuzuladendes Entity.
     * @return neugeladenes Entity.
     */
    Entity refresh(Entity entity);
}
