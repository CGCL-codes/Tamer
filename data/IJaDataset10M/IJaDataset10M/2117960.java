package org.mobicents.slee.container.management.console.client.usage;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author Povilas Jurna
 * 
 */
public interface RaUsageService extends RemoteService {

    public String[] getParameterSets(String raId, String entityName) throws ManagementConsoleException;

    public void createUsageParameterSet(String entityName, String name) throws ManagementConsoleException;

    public void removeUsageParameterSet(String entityName, String parameterSet) throws ManagementConsoleException;

    public void resetAllUsageParameters(String entityName) throws ManagementConsoleException;

    public UsageParameterInfo[] getRaUsageParameters(String entityName, String parameterSet) throws ManagementConsoleException;

    public void resetAllUsageParameters(String entityName, String parameterSet) throws ManagementConsoleException;

    public void resetUsageParameter(String entityName, String parameterSet, String parameterName, boolean isCounterType) throws ManagementConsoleException;
}
