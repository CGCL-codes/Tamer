package de.objectcode.time4u.server.web.ws;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.objectcode.time4u.server.api.IPersonService;
import de.objectcode.time4u.server.api.data.ClientIdList;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Person;
import de.objectcode.time4u.server.api.data.PersonSummary;
import de.objectcode.time4u.server.api.filter.PersonFilter;

@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws", endpointInterface = "de.objectcode.time4u.server.api.IPersonService")
@SOAPBinding(style = Style.RPC)
public class PersonServiceWS implements IPersonService {

    private static final Log LOG = LogFactory.getLog(ProjectServiceWS.class);

    private final IPersonService m_personService;

    public PersonServiceWS() throws Exception {
        final InitialContext ctx = new InitialContext();
        m_personService = (IPersonService) ctx.lookup("time4u-server/PersonService/remote");
    }

    public Person getSelf() {
        LOG.info("getSelf");
        return m_personService.getSelf();
    }

    public boolean registerClient(final long clientId) {
        if (LOG.isInfoEnabled()) {
            LOG.info("registerClient: " + clientId);
        }
        return m_personService.registerClient(clientId);
    }

    public ClientIdList getRegisteredClients() {
        if (LOG.isInfoEnabled()) {
            LOG.info("getRegisteredClients");
        }
        return m_personService.getRegisteredClients();
    }

    public FilterResult<Person> getPersons(final PersonFilter filter) {
        if (LOG.isInfoEnabled()) {
            LOG.info("getPersons: " + filter);
        }
        return m_personService.getPersons(filter);
    }

    public FilterResult<PersonSummary> getPersonSummaries(final PersonFilter filter) {
        if (LOG.isInfoEnabled()) {
            LOG.info("getPersonSummaries: " + filter);
        }
        return m_personService.getPersonSummaries(filter);
    }

    public Person getPerson(final String personId) {
        if (LOG.isInfoEnabled()) {
            LOG.info("getPerson: " + personId);
        }
        return m_personService.getPerson(personId);
    }
}
