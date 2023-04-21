package javax.agent.service.naming;

import javax.agent.AgentName;
import javax.agent.service.Service;

/**
 * The AgentNamingService defines the operations that can be made on a
 * generic naming service.  A Naming Service produces Globally Unique
 * IDentifiers (GUIDs) (i.e. AgentNames).
 *
 * @see AgentName
 *
 * @author A. Spydell
 */
public interface AgentNamingService extends Service {

    /** The well-known service type. */
    String SERVICE_TYPE = "agent-naming-service";

    /**
    * Factory method to create globally unique AgentNames.
    *
    * @return AgentName a new globally unique identifier.
    * @exception NamingException thrown if anything goes awry in the
    *               creation of the AgentName.
    * @exception NamingFailure thrown due to a problem with the naming
    *               service infrastructure.
    */
    AgentName createAgentName() throws NamingException, NamingFailure;
}
