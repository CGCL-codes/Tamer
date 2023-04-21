package org.cishell.remoting.service.framework;

import java.util.Hashtable;
import java.util.Vector;
import org.cishell.framework.algorithm.AlgorithmFactory;

public interface AlgorithmFactoryRegistry {

    public static String SERVICE_NAME = "AlgorithmFactoryRegistry";

    public String createParameters(String servicePID, Vector dataModelIDs);

    public String createAlgorithm(String sessionID, String servicePID, Vector dataModelIDs, Hashtable dictionary);

    public Hashtable getProperties(String servicePID);

    public AlgorithmFactory getAlgorithmFactory(String servicePID);
}
