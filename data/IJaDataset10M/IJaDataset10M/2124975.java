package org.elogistics.dataaccess.transportplan;

import org.elogistics.command.datacommand.IDataCommand;
import org.elogistics.dataaccess.IGenericDAO;
import org.elogistics.domain.entities.transportplan.cluster.IntercityCluster;

/**
 * Interface for Intercity-Cluster entities.
 * 
 * @author Jurkschat, Oliver
 *
 */
public interface IIntercityClusterDAO extends IGenericDAO<IntercityCluster, Integer, IDataCommand> {
}
