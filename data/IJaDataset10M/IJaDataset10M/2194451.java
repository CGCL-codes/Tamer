package org.openuss.commands;

import java.util.Date;
import org.openuss.foundation.DomainObject;

/**
 * DomainCommand 
 * @author Ingo Dueppe
 *
 */
public interface DomainCommand {

    /**
	 * Execute the command
	 */
    void execute() throws Exception;

    /**
	 * Retrieve the associated domain object
	 * @return DomainObject instance
	 */
    DomainObject getDomainObject();

    /**
	 * Define the associated domain object.
	 * @param domainObject
	 */
    void setDomainObject(DomainObject domainObject);

    /**
	 * Getting the subtype of the command
	 * @return String commandType
	 */
    String getCommandType();

    /**
	 * Setting the subtype of the command
	 * @param commandType
	 */
    void setCommandType(String commandType);

    /**
	 * Getting the start time
	 * @return Date - earliest point of time to execute the command
	 */
    Date getStartTime();

    /**
	 * Setting the start time. 
	 * @param startTime - earliest point of time to execute the command
	 */
    void setStartTime(Date startTime);
}
