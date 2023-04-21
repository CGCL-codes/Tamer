package org.elogistics.dataaccess.location;

import org.elogistics.command.datacommand.IDataCommand;
import org.elogistics.dataaccess.IGenericDAO;
import org.elogistics.domain.entities.location.City;

/**
 * Interface for city entities.
 * 
 * @author Jurkschat, Oliver
 *
 */
public interface ICityDAO extends IGenericDAO<City, Integer, IDataCommand> {
}
