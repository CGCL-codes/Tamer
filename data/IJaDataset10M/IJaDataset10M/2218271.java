package org.openconcerto.xml.persistence;

import org.openconcerto.utils.CollectionMap;
import java.io.IOException;
import java.util.Set;

/**
 * Permet de lire et ecrire des Persistent déjà serialisés.
 * 
 * @author ILM Informatique 29 juin 2004
 */
public interface PersistenceIO {

    /**
     * Sauve l'élément passé.
     * 
     * @param ser
     * @param pers
     * @param id
     * @throws IOException if an error occurs while saving.
     */
    public void save(Object ser, Persistent pers, String id) throws IOException;

    /**
     * Load a serialized form.
     * 
     * @param clazz the class of the serialized object.
     * @param id the id of the serialized object.
     * @return the serialized form of the object, or <code>null</code> if not found.
     * @throws IOException if pb while loading.
     */
    public Object load(Class clazz, String id) throws IOException;

    public Set<String> getIDs(Class clazz) throws IOException;

    public CollectionMap<Class, String> getIDs() throws IOException;

    public void delete(Class clazz, String id) throws IOException;

    public void delete(Class clazz) throws IOException;

    public void deleteAll() throws IOException;

    public boolean exists(Class clazz, String id);

    public void setAutoCommit(boolean b) throws IOException;

    /**
     * Should free loaded objects from any cache. In general, this means that the next load() will
     * do some io, so this method should only be used when all objects we're interested in have been
     * loaded or when memory run low.
     */
    public void unload();
}
