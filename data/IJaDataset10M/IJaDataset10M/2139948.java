package mwt.xml.xdbforms.cache;

import java.util.Collection;
import java.util.Map;
import mwt.xml.xdbforms.cache.exception.CacheServiceException;

/**
 *
 * @author Gianfranco Murador
 * @author Matteo Ferri
 * @author Cristian Castiglia
 * Copyright (C) MCG08 2009
 * Uso della libreria EhCache, aggiunta altra dipendenza.
 *
 * Definisce un'iterfaccia comune per un servizio di caching
 */
public interface CacheService {

    /**
     * Ripulisce la cache programmaticamente
     * @throws mwt.xml.xdbforms.cache.exception.CacheServiceException
     */
    public void flush() throws CacheServiceException;

    /**
     * Verifica se un'oggetto è presente nella cache,
     * fornendo la sua chiave in ingresso
     * @param keys
     * @return true se presente, false altrimenti
     * @throws mwt.xml.xdbforms.cache.exception.CacheServiceException
     */
    public boolean contains(Object key) throws CacheServiceException;

    /**
     * Prende l'ultimo oggetto nella cache con la chiave specificata
     * @param key
     * @return
     * @throws mwt.xml.xdbforms.cache.exception.CacheServiceException
     */
    public Object getLastObject(Object key) throws CacheServiceException;

    /**
      * Carica un nuovo oggetto nella cache
      * @param obj
      * @param key
      * @return Il reference dell'oggetto appena creato
      * @throws mwt.xml.xdbforms.cache.exception.CacheServiceException
      */
    public Object load(Object obj, Object key) throws CacheServiceException;

    /**
     *
     * Carica un'isieme di oggetti nella chache. Gli oggetti
     * e devono essere inserite a seconda degli abbinamenti giusti
     * @param objs Collezione di oggetti
     * @param keys Collezioni di chiavi
     * @return Una hashmap con la l'insieme appena creato
     * @throws mwt.xml.xdbforms.cache.exception.CacheServiceException
     */
    public Map loadAll(Collection objs, Collection keys) throws CacheServiceException;
}
