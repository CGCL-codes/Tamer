package it.unina.gaeframework.geneticalgorithm.util;

import it.unina.gaeframework.geneticalgorithm.AbstractChromosomeFactory;
import it.unina.gaeframework.geneticalgorithm.ChromosomeFactory;
import it.unina.gaeframework.geneticalgorithm.statistics.GeneticStatistics;
import it.unina.tools.datastore.DatastoreLoadAndSave;
import java.util.List;
import com.google.appengine.api.datastore.Key;

/**
 * classe che fornisce le chiavi necessarie all'algoritmo genetico
 * 
 * @author barren
 * 
 */
public final class GAKeyFactory {

    private static GAKeyFactory gaKeyFactory = null;

    private List<Key> chromosomeKeyList = null;

    private Key genetisStatisticsKey = null;

    private GAKeyFactory() {
        super();
        GeneticAlgorithmXMLConfigReader geneticConfigXML = GeneticAlgorithmXMLConfigReader.getInstance();
        Integer numKeys = geneticConfigXML.getPopulationSize() + geneticConfigXML.getNewOffspringForIteration();
        ChromosomeFactory chromosomeFactory = AbstractChromosomeFactory.getConcreteChromosomeFactory();
        chromosomeKeyList = DatastoreLoadAndSave.generateKeys(1L, numKeys, chromosomeFactory.getConcreteChromosomeClass());
        genetisStatisticsKey = DatastoreLoadAndSave.generateKey(GeneticStatistics.class, 1L);
    }

    /**
	 * metodo che fornisce un'istanza di GAKeyFactory
	 * 
	 * @return un'istanza di GAKeyFactory
	 */
    public static GAKeyFactory getGAKeyFactoryInstance() {
        if (gaKeyFactory == null) gaKeyFactory = new GAKeyFactory();
        return gaKeyFactory;
    }

    public List<Key> getChromosomeKeyList() {
        return chromosomeKeyList;
    }

    public Key getGenetisStatisticsKey() {
        return genetisStatisticsKey;
    }
}
