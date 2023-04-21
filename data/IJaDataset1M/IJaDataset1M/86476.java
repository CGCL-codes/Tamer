package edu.mit.osidimpl.agent.mulberry;

import edu.mit.osidimpl.manager.*;
import edu.mit.osidimpl.agent.shared.*;

/**
 *  <p>
 *  Implements the AgentManager for the mulberry agent service.
 *  </p><p>
 *  Implementation Properties:
 *  <table border=0>
 *  </table>
 *  </p><p>
 *  CVS $Id: AgentManager.java,v 1.1 2005/08/31 17:21:28 tom Exp $
 *  </p>
 *  
 *  @author  Tom Coppeto
 *  @version $OSID: 2.0$ $Revision: 1.1 $
 *  @see     org.osid.authentication.AuthenticationManager
 */
public class AgentManager implements org.osid.agent.AgentManager {

    OsidLogger logger;

    AgentServer server;

    protected int page_size = 100;

    private String server_location = null;

    /**
     *  Constructs a new <code>AgentManager</code>.
     */
    public AgentManager() {
        super();
    }

    protected void initialize() throws org.osid.OsidException {
        String property;
        logger = getLogger();
        logger.logDebug("initializing AgentManager");
        property = getConfiguration("page_size");
        if (property != null) {
            try {
                this.page_size = Integer.parseInt(property);
            } catch (NumberFormatException nfe) {
                logger.logError("page_size property not a number: " + property);
            }
            logger.logTrace("page_size=" + this.page_size);
        }
        property = getConfiguration("server_location");
        if (property != null) {
            this.server_location = property;
            logger.logTrace("server_location=" + this.server_location);
        }
        try {
            this.server = new AgentServer(this.server_location, this.page_size);
        } catch (org.osid.agent.AgentException se) {
            logger.logCritical("unable to start agent server");
            throw ae;
        }
        return;
    }

    /** 
     *  Creates a new Agent
     *
     *  @param displayName 
     *  @param agentType
     *  @param properties
     *  @return Agent
     *  @throws org.osid.agent.AgentException An exception with one of the
     *          following messages defined in org.osid.agent.AgentException may
     *          be thrown:  {@link
     *          org.osid.agent.AgentException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.agent.AgentException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.agent.AgentException#CONFIGURATION_ERROR
     *          CONFIGURATION_ERROR}, {@link
     *          org.osid.agent.AgentException#UNIMPLEMENTED UNIMPLEMENTED},
     *          {@link org.osid.agent.AgentException#NULL_ARGUMENT
     *          NULL_ARGUMENT}, {@link
     *          org.osid.agent.AgentException#UNKNOWN_TYPE UNKNOWN_TYPE}
     */
    public Agent createAgent(String displayName, org.osid.shared.Type agentType, org.osid.shared.Properties properties) throws org.osid.agent.AgentException {
        throw new AgentException(AgentException.UNIMPLEMENTED);
    }

    /**
     *  Delete the Agent with the specified unique Id.
     *
     *  @param id
     *  @throws org.osid.agent.AgentException An exception with one of the
     *          following messages defined in org.osid.agent.AgentException may
     *          be thrown:  {@link
     *          org.osid.agent.AgentException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.agent.AgentException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.agent.AgentException#CONFIGURATION_ERROR
     *          CONFIGURATION_ERROR}, {@link
     *          org.osid.agent.AgentException#UNIMPLEMENTED UNIMPLEMENTED},
     *          {@link org.osid.agent.AgentException#NULL_ARGUMENT
     *          NULL_ARGUMENT}, {@link org.osid.agent.AgentException#UNKNOWN_ID
     *          UNKNOWN_ID}
     */
    public void deleteAgent(org.osid.shared.Id id) throws org.osid.agent.AgentException {
        throw new AgentException(AgentException.UNIMPLEMENTED);
    }

    /**
     *  Get the Agent with the specified unique Id. Getting an Agent by name is
     *  not supported since names are not guaranteed to be unique.
     *
     *  @param id
     *  @return Agent
     *  @throws org.osid.agent.AgentException An exception with one of the
     *          following messages defined in org.osid.agent.AgentException may
     *          be thrown:  {@link
     *          org.osid.agent.AgentException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.agent.AgentException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.agent.AgentException#CONFIGURATION_ERROR
     *          CONFIGURATION_ERROR}, {@link
     *          org.osid.agent.AgentException#UNIMPLEMENTED UNIMPLEMENTED},
     *          {@link org.osid.agent.AgentException#NULL_ARGUMENT
     *          NULL_ARGUMENT}, {@link org.osid.agent.AgentException#UNKNOWN_ID
     *          UNKNOWN_ID}
     */
    public Agent getAgent(org.osid.shared.Id id) throws org.osid.agent.AgentException {
        return (server.getAgent(id));
    }

    /**
     *  Get all the Agents.  
     *
     *  @return AgentIterator
     *  @throws org.osid.agent.AgentException An exception with one of the
     *          following messages defined in org.osid.agent.AgentException may
     *          be thrown:  {@link
     *          org.osid.agent.AgentException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.agent.AgentException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.agent.AgentException#CONFIGURATION_ERROR
     *          CONFIGURATION_ERROR}, {@link
     *          org.osid.agent.AgentException#UNIMPLEMENTED UNIMPLEMENTED}
     */
    public AgentIterator getAgents() throws org.osid.agent.AgentException {
        return (server.getAgents());
    }

    /**
     *  Get all the agent Types. 
     *
     *  @return org.osid.shared.TypeIterator
     *  @throws org.osid.agent.AgentException An exception with one of the
     *          following messages defined in org.osid.agent.AgentException may
     *          be thrown:  {@link
     *          org.osid.agent.AgentException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.agent.AgentException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.agent.AgentException#CONFIGURATION_ERROR
     *          CONFIGURATION_ERROR}, {@link
     *          org.osid.agent.AgentException#UNIMPLEMENTED UNIMPLEMENTED}
     */
    public org.osid.shared.TypeIterator getAgentTypes() throws org.osid.agent.AgentException {
        return (AgentType.getAgentTypes());
    }

    /**
     *  Get all the property Types.
     *
     *  @return org.osid.shared.TypeIterator
     *  @throws org.osid.agent.AgentException An exception with one of the
     *          following messages defined in org.osid.agent.AgentException may
     *          be thrown:  {@link
     *          org.osid.agent.AgentException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.agent.AgentException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.agent.AgentException#CONFIGURATION_ERROR
     *          CONFIGURATION_ERROR}, {@link
     *          org.osid.agent.AgentException#UNIMPLEMENTED UNIMPLEMENTED}
     */
    public org.osid.shared.TypeIterator getPropertyTypes() throws org.osid.agent.AgentException {
        return (AgentType.getPropertyTypes());
    }

    /**
     *  Create a Group with the display name, Type, description, and Properties
     *  specified.  All but description are immutable.
     *
     *  @param displayName
     *  @param groupType
     *  @param description
     *  @param properties
     *  @return Group
     *  @throws org.osid.agent.AgentException An exception with one of the
     *          following messages defined in org.osid.agent.AgentException may
     *          be thrown:  {@link
     *          org.osid.agent.AgentException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.agent.AgentException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.agent.AgentException#CONFIGURATION_ERROR
     *          CONFIGURATION_ERROR}, {@link
     *          org.osid.agent.AgentException#UNIMPLEMENTED UNIMPLEMENTED},
     *          {@link org.osid.agent.AgentException#NULL_ARGUMENT
     *          NULL_ARGUMENT}, {@link
     *          org.osid.agent.AgentException#UNKNOWN_TYPE UNKNOWN_TYPE}
     */
    public Group createGroup(String displayName, org.osid.shared.Type groupType, String description, org.osid.shared.Properties properties) throws org.osid.agent.AgentException {
        throw new AgentException(AgentException.UNIMPLEMENTED);
    }

    /**
     * Delete the Group with the specified unique Id.
     *
     *  @param id
     *  @throws org.osid.agent.AgentException An exception with one of the
     *          following messages defined in org.osid.agent.AgentException may
     *          be thrown:  {@link
     *          org.osid.agent.AgentException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.agent.AgentException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.agent.AgentException#CONFIGURATION_ERROR
     *          CONFIGURATION_ERROR}, {@link
     *          org.osid.agent.AgentException#UNIMPLEMENTED UNIMPLEMENTED},
     *          {@link org.osid.agent.AgentException#NULL_ARGUMENT
     *          NULL_ARGUMENT}, {@link org.osid.agent.AgentException#UNKNOWN_ID
     *          UNKNOWN_ID}
     */
    public void deleteGroup(org.osid.shared.Id id) throws org.osid.agent.AgentException {
        throw new AgentException(AgentException.UNIMPLEMENTED);
    }

    /**
     *  Gets the Group with the specified unique Id. Getting a Group by name is
     *  not supported since names are not guaranteed to be unique.
     *
     *  @param id
     *  @return Group
     *  @throws org.osid.agent.AgentException An exception with one of the
     *          following messages defined in org.osid.agent.AgentException may
     *          be thrown:  {@link
     *          org.osid.agent.AgentException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.agent.AgentException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.agent.AgentException#CONFIGURATION_ERROR
     *          CONFIGURATION_ERROR}, {@link
     *          org.osid.agent.AgentException#UNIMPLEMENTED UNIMPLEMENTED},
     *          {@link org.osid.agent.AgentException#NULL_ARGUMENT
     *          NULL_ARGUMENT}, {@link org.osid.agent.AgentException#UNKNOWN_ID
     *          UNKNOWN_ID}
     */
    public Group getGroup(org.osid.shared.Id id) throws org.osid.agent.AgentException {
        return (server.getGroup(id));
    }

    /**
     *  Get all the Groups.
     *
     *  @return AgentIterator
     *  @throws org.osid.agent.AgentException An exception with one of the
     *          following messages defined in org.osid.agent.AgentException may
     *          be thrown:  {@link
     *          org.osid.agent.AgentException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.agent.AgentException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.agent.AgentException#CONFIGURATION_ERROR
     *          CONFIGURATION_ERROR}, {@link
     *          org.osid.agent.AgentException#UNIMPLEMENTED UNIMPLEMENTED}
     */
    public AgentIterator getGroups() throws org.osid.agent.AgentException {
        return (server.getGroups());
    }

    /**
     *  Get all the group Types.
     *
     *  @return org.osid.shared.TypeIterator
     *  @throws org.osid.agent.AgentException An exception with one of the
     *          following messages defined in org.osid.agent.AgentException may
     *          be thrown:  {@link
     *          org.osid.agent.AgentException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.agent.AgentException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.agent.AgentException#CONFIGURATION_ERROR
     *          CONFIGURATION_ERROR}, {@link
     *          org.osid.agent.AgentException#UNIMPLEMENTED UNIMPLEMENTED}
     */
    public org.osid.shared.TypeIterator getGroupTypes() throws org.osid.agent.AgentException {
        return (AgentType.getGroupTypes());
    }

    /**
     *  Get all the Agents of the specified Type.
     *
     *  @return AgentIterator
     *  @throws org.osid.agent.AgentException An exception with one of the
     *          following messages defined in org.osid.agent.AgentException may
     *          be thrown:  {@link
     *          org.osid.agent.AgentException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.agent.AgentException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.agent.AgentException#CONFIGURATION_ERROR
     *          CONFIGURATION_ERROR}, {@link
     *          org.osid.agent.AgentException#UNIMPLEMENTED UNIMPLEMENTED},
     *          {@link org.osid.agent.AgentException#NULL_ARGUMENT
     *          NULL_ARGUMENT}, {@link
     *          org.osid.agent.AgentException#UNKNOWN_TYPE UNKNOWN_TYPE}
     */
    public AgentIterator getAgentsByType(org.osid.shared.Type agentType) throws org.osid.agent.AgentException {
        return (getAgents());
    }

    /**
     *  Get all the Groups of the specified Type.
     *
     *  @return AgentIterator
     *  @throws org.osid.agent.AgentException An exception with one of the
     *          following messages defined in org.osid.agent.AgentException may
     *          be thrown:  {@link
     *          org.osid.agent.AgentException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.agent.AgentException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.agent.AgentException#CONFIGURATION_ERROR
     *          CONFIGURATION_ERROR}, {@link
     *          org.osid.agent.AgentException#UNIMPLEMENTED UNIMPLEMENTED},
     *          {@link org.osid.agent.AgentException#NULL_ARGUMENT
     *          NULL_ARGUMENT}, {@link
     *          org.osid.agent.AgentException#UNKNOWN_TYPE UNKNOWN_TYPE}
     */
    public AgentIterator getGroupsByType(org.osid.shared.Type groupType) throws org.osid.agent.AgentException {
        return (getGroups());
    }

    /**
     *  Get all the agent search Types supported by this implementation.
     *
     *  @return org.osid.shared.TypeIterator
     *  @throws org.osid.agent.AgentException An exception with one of the
     *          following messages defined in org.osid.agent.AgentException may
     *          be thrown:  {@link
     *          org.osid.agent.AgentException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.agent.AgentException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.agent.AgentException#CONFIGURATION_ERROR
     *          CONFIGURATION_ERROR}, {@link
     *          org.osid.agent.AgentException#UNIMPLEMENTED UNIMPLEMENTED},
     *          {@link org.osid.agent.AgentException#NULL_ARGUMENT
     *          NULL_ARGUMENT}, {@link
     *          org.osid.agent.AgentException#UNKNOWN_TYPE UNKNOWN_TYPE}
     */
    public org.osid.shared.TypeIterator getAgentSearchTypes() throws org.osid.agent.AgentException {
        return (AgentType.getAgentSearchTypes());
    }

    /**
     *  Get all the Agents with the specified search criteria and search Type.
     *
     *  @return AgentIterator
     *  @throws org.osid.agent.AgentException An exception with one of the
     *          following messages defined in org.osid.agent.AgentException may
     *          be thrown:  {@link
     *          org.osid.agent.AgentException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.agent.AgentException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.agent.AgentException#CONFIGURATION_ERROR
     *          CONFIGURATION_ERROR}, {@link
     *          org.osid.agent.AgentException#UNIMPLEMENTED UNIMPLEMENTED},
     *          {@link org.osid.agent.AgentException#NULL_ARGUMENT
     *          NULL_ARGUMENT}, {@link
     *          org.osid.agent.AgentException#UNKNOWN_TYPE UNKNOWN_TYPE}
     */
    public AgentIterator getAgentsBySearch(java.io.Serializable searchCriteria, org.osid.shared.Type agentSearchType) throws org.osid.agent.AgentException {
        return (server.getAgentsBySearch(searchCriteria, agentSearchType));
    }

    /**
     *  Get all the group search types supported by this implementation.
     *
     *  @return org.osid.shared.TypeIterator
     *  @throws org.osid.agent.AgentException An exception with one of the
     *          following messages defined in org.osid.agent.AgentException may
     *          be thrown:  {@link
     *          org.osid.agent.AgentException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.agent.AgentException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.agent.AgentException#CONFIGURATION_ERROR
     *          CONFIGURATION_ERROR}, {@link
     *          org.osid.agent.AgentException#UNIMPLEMENTED UNIMPLEMENTED},
     *          {@link org.osid.agent.AgentException#NULL_ARGUMENT
     *          NULL_ARGUMENT}, {@link
     *          org.osid.agent.AgentException#UNKNOWN_TYPE UNKNOWN_TYPE}
     */
    public org.osid.shared.TypeIterator getGroupSearchTypes() throws org.osid.agent.AgentException {
        return (AgentType.getGroupSearchTypes());
    }

    /**
     *  Get all the groups with the specified search criteria and search Type.
     *
     *  @return AgentIterator
     *  @throws org.osid.agent.AgentException An exception with one of the
     *          following messages defined in org.osid.agent.AgentException may
     *          be thrown:  {@link
     *          org.osid.agent.AgentException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.agent.AgentException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.agent.AgentException#CONFIGURATION_ERROR
     *          CONFIGURATION_ERROR}, {@link
     *          org.osid.agent.AgentException#UNIMPLEMENTED UNIMPLEMENTED},
     *          {@link org.osid.agent.AgentException#NULL_ARGUMENT
     *          NULL_ARGUMENT}, {@link
     *          org.osid.agent.AgentException#UNKNOWN_TYPE UNKNOWN_TYPE}
     */
    public AgentIterator getGroupsBySearch(java.io.Serializable searchCriteria, org.osid.shared.Type groupSearchType) throws org.osid.agent.AgentException {
        return (server.getGroupsBySearch(searchCriteria, groupSearchType));
    }
}
