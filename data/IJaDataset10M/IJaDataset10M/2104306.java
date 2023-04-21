package org.baatar.opt.pga.intf;

import org.baatar.opt.genetic.AlgorithmParameters;
import org.baatar.opt.genetic.chromosome.MigrantChromosome;
import org.baatar.remoting.service.RemotingServiceInfo;

/**
 *
 * @author Ba
 */
public interface IPGACoordinator<T, M> {

    public Integer registerCluster(RemotingServiceInfo clusterNetInfo, Integer numOfWorkers) throws Exception;

    public AlgorithmParameters<T, M> getAlgorithmParameters() throws Exception;

    public void reportProgress(Integer clusterIndex, Integer optimizerIndex, Integer iteration, String progress) throws Exception;

    public void migrate(Integer clusterIndex, Integer optimizerIndex, Integer iteration, MigrantChromosome<M> migrants[]) throws Exception;
}
