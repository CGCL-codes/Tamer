package de.kumpe.hadooptimizer.jeneva;

import java.rmi.Remote;
import java.rmi.RemoteException;
import de.kumpe.hadooptimizer.EaOptimizerConfigurationBase;

interface EvolutionService<I> extends Remote {

    EaOptimizerConfigurationBase<I> getConfiguration() throws RemoteException;

    IndividualService<I> getIndividualService() throws RemoteException;
}
