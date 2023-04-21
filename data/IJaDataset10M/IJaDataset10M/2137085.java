package it.webscience.uima.annotators.solr;

import java.util.HashMap;

/**
 * @author filieri
 */
public class OcSolrProperties {

    /**
     * contiene la rappresentazione dei dati, implementata come una lista di
     * associazioni nome-valore, da inviare a SOLR per la creazione degli indici
     * corrispondenti.
     */
    private static HashMap<String, Object> properties;

    /**
     * Metodo costruttore che inizializza la hashmap
     * contente le proprieta e i valori corrispondenti, dai
     * quali saranno valorizzati gli indici di solr.
     */
    public OcSolrProperties() {
        properties = new HashMap<String, Object>();
    }

    /**
     * @param key - chiave della propriet� da settare.
     * @param value - valore della propriet� da settare.
     */
    public void setProperty(final String key, final Object value) {
        properties.put(key, value);
    }

    /**
     * @return properties - restituisce la lista di associazioni chiave-valore
     * necessarie a popolare gli indici di solr.
     */
    public static HashMap<String, Object> getProperties() {
        return properties;
    }

    /**
     * @param properties - assegna all'istanza della classe
     * le properties che sono state ricavate.
     */
    public static void setProperties(final HashMap<String, Object> properties) {
        OcSolrProperties.properties = properties;
    }
}
