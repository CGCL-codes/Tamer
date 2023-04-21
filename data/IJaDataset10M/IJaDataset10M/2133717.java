package de.fhg.igd.semoa.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.AccessController;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import de.fhg.igd.logging.LogLevel;
import de.fhg.igd.logging.Logger;
import de.fhg.igd.logging.LoggerFactory;
import de.fhg.igd.semoa.net.ship.ShipException;
import de.fhg.igd.semoa.net.ship.ShipService;
import de.fhg.igd.semoa.service.AbstractFilter;
import de.fhg.igd.util.Resource;
import de.fhg.igd.util.URL;
import de.fhg.igd.util.WhatIs;

/**
 * This service provides the gateway out of the system (for agents).
 * Agents forwarded via this service are subsequently sent to the
 * destination defined in the Ticket. If the Ticket is not valid,
 * appropriate action is taken.
 *
 * @author Volker Roth
 * @author Ulrich Pinsdorf
 * @version $Id: OutGate.java 1913 2007-08-08 02:41:53Z jpeters $
 */
public class OutGate extends AbstractFilter {

    /**
     * The <code>Logger</code> instance for this class 
     */
    private static Logger log_ = LoggerFactory.getLogger("semoa/agent_state");

    /**
     * The {@link WhatIs} entry key for this service.
     */
    public static final String WHATIS = "OUTGATE";

    /**
     * The dependencies to other objects in the global <code>
     * Environment</code>.
     */
    private static final String[] DEPEND_ = { "WHATIS:SHIP" };

    /**
     * A private lock object.
     */
    private final Object lock_ = new Object();

    /**
     * The last time the list of security filters was fetched.
     */
    private long filterUpdate_ = -1;

    /**
     * The last time the list of gateways was fetched.
     */
    private long gatewayUpdate_ = -1;

    /**
     * The <code>SortedMap</code> of security filters an agent has
     * to pass.
     */
    private SortedMap filters_;

    /**
     * The <code>Map</code> of currently known gateways out of
     * the server. This <code>Map</code> is updated automatically
     * whenever a change in the set of gateways is detected and
     * an agent is sent.
     */
    private SortedMap gateways_;

    /**
     * Creates an instance of this service. This constructor invokes
     * the <code>SecurityManager</code> and checks if the calling
     * thread has permission to look up and publish the outgate at
     * its designated position in the global environment (under
     * {@link WhatIs WhatIs} key <code>OUTGATE</code>).
     */
    public OutGate() {
        SecurityManager sm;
        Environment env;
        String key;
        key = WhatIs.stringValue(OutGate.WHATIS);
        env = getEnvironment();
        if ((sm = System.getSecurityManager()) != null) {
            sm.checkPermission(new EnvironmentPermission(key, "lookup,publish"));
        } else {
            AccessController.checkPermission(new EnvironmentPermission(key, "lookup,publish"));
        }
        env.addWatch(key);
        env.addWatch(WhatIs.stringValue(AgentFilter.Out.WHATIS));
    }

    public String author() {
        return People.VROTH;
    }

    public String info() {
        return "The main gateway for agents out of this server.";
    }

    public String revision() {
        return "$Revision: 1913 $/$Date: 2007-08-07 22:41:53 -0400 (Tue, 07 Aug 2007) $";
    }

    public String[] dependencies() {
        return (String[]) DEPEND_.clone();
    }

    /**
     * @return The <code>SortedMap</code> of agent filters.
     */
    protected SortedMap getSecurityFilters() {
        Iterator i;
        String key;
        long millis;
        synchronized (lock_) {
            key = WhatIs.stringValue(AgentFilter.Out.WHATIS);
            millis = getEnvironment().lastChange(key);
            if (millis != filterUpdate_) {
                filterUpdate_ = millis;
                filters_ = getEnvironment().lookupAll(key + "/-");
                for (i = filters_.values().iterator(); i.hasNext(); ) {
                    if (!(i.next() instanceof AgentFilter.Out)) {
                        i.remove();
                    }
                }
            }
            return filters_;
        }
    }

    /**
     * @return The <code>SortedMap</code> of agent gateways.
     */
    protected SortedMap getGateways() {
        Iterator i;
        String key;
        long millis;
        synchronized (lock_) {
            key = WhatIs.stringValue(OutGate.WHATIS);
            millis = getEnvironment().lastChange(key);
            if (millis != gatewayUpdate_) {
                gatewayUpdate_ = millis;
                gateways_ = getEnvironment().lookupAll(key + "/-");
                for (i = gateways_.values().iterator(); i.hasNext(); ) {
                    if (!(i.next() instanceof AgentGateway.Out)) {
                        i.remove();
                    }
                }
            }
            return gateways_;
        }
    }

    /**
     * Pipes the given context through the list of known
     * security filters.
     *
     * @param ctx The <code>AgentContext</code> that is piped
     *   through the list of security filters. All security
     *   filters must implement interface <code>AgentFilter.Out
     *   </code>, and they must be published with the parent
     *   path defined by the {@link WhatIs WhatIs} key
     *   &quot;SECURITY_FILTERS_OUT&quot;.
     * @exception IllegalAgentException if the given agent is
     *   rejected by one of the filters.
     */
    protected void filterAgent(AgentContext ctx) throws IllegalAgentException {
        AgentFilter.Out filter;
        SortedMap filters;
        ErrorCode error;
        AgentCard card;
        Map.Entry entry;
        Iterator i;
        long time;
        filters = getSecurityFilters();
        time = System.currentTimeMillis();
        card = (AgentCard) ctx.get(FieldType.CARD);
        for (i = filters.entrySet().iterator(); i.hasNext(); ) {
            entry = (Map.Entry) i.next();
            filter = (AgentFilter.Out) entry.getValue();
            log_.debug("Filtering agent: implicit_name=[" + card + "], filter=" + entry.getKey());
            try {
                error = filter.filter(ctx);
                if (error != ErrorCode.OK) {
                    log_.error("Agent rejected by filter: implicit_name=[" + card + "], filter=[" + entry.getKey() + "], return_code=[" + error + "]");
                    throw new IllegalAgentException("Agent rejected by filter \"" + entry.getKey().toString() + "\", return code (" + error + ")");
                }
            } catch (IllegalStateException e) {
                System.err.println("[OutGate] I just lost a security filter!");
                continue;
            }
        }
        updateStatistics(System.currentTimeMillis() - time);
    }

    /**
     * Takes the agent and tries to dispatch it to the given
     * destination. The caller needs to have the full rights
     * required to deliver the agent.<p>
     *
     * If no ticket is set in the given context the this
     * method simply returns.
     *
     * @param struct The agent's structure as a <code>Resource
     *   </code>.
     * @param ticket The ticket specifying the agent's destination.
     * @exception TicketNotSupportedException if either the
     *   given ticket is <code>null</code> or no transport service
     *   supports the given ticket.
     * @exception IOException if an error occurs while transporting
     *   the agent.
     * @exception IllegalAgentException if the given context is
     *   rejected by one of the outgoing security filters, or
     *   if the agent context is incomplete or erroneous.
     */
    public void dispatchAgent(AgentContext ctx) throws TicketNotSupportedException, IOException, IllegalAgentException {
        AgentGateway.Out gate;
        AgentState state;
        AgentCard card;
        Resource resource;
        String[] protos;
        Iterator i;
        Ticket ticket;
        String key;
        int j;
        if (ctx == null) {
            throw new NullPointerException("Need an AgentContext!");
        }
        state = (AgentState) ctx.get(FieldType.STATE);
        if (state == null) {
            throw new NullPointerException("agent state");
        }
        if (state == AgentState.RUNNING) {
            throw new IllegalArgumentException("Agent is still running!");
        }
        try {
            ticket = (Ticket) ctx.get(FieldType.TICKET);
            resource = (Resource) ctx.get(FieldType.RESOURCE);
        } catch (ClassCastException e) {
            throw new IllegalAgentException("Ticket or resource is of a bad class!");
        }
        if (ticket == null || resource == null) {
            throw new NullPointerException("No ticket or resource!");
        }
        ticket = shipLookup(ticket);
        filterAgent(ctx);
        protos = ticket.getProtocols();
        key = WhatIs.stringValue(OutGate.WHATIS);
        card = (AgentCard) ctx.get(FieldType.CARD);
        for (j = 0; j < protos.length; j++) {
            gate = (AgentGateway.Out) getEnvironment().lookup(key + "/" + protos[j]);
            if (gate != null) {
                try {
                    log_.info("Agent ID mapping: implicit_name=[" + card + "] => resourc_hash=[" + resource.hashCode() + "]");
                    onTransport(ctx);
                    gate.dispatchAgent(resource, ticket);
                    return;
                } catch (TicketNotSupportedException e) {
                    log_.caught(LogLevel.WARNING, "Agent transport error: implicit_name=[" + card + "]", e);
                    System.err.println("[OutGate] Transport error: \"" + e.getMessage() + "\"");
                } catch (IOException e) {
                    System.err.println("[OutGate] I/O error: \"" + e.getMessage() + "\"");
                }
            }
        }
        log_.error("No transport service supports the ticket: implicit_name=[" + card + "]");
        throw new TicketNotSupportedException("No transport service supports the ticket!");
    }

    /**
     * Verifies if an agent can be sent to the destination specified
     * in the ticket. Each transport service registered at the time
     * of the call is queried whether it accepts at least one of the
     * alternative URLs specified in the ticket.
     *
     * @param ticket The ticket specifying the destination to
     *   which some agent shall be sent.
     * @return <code>true</code> if some transport service at the
     *   time of the call exists, that in principle offers
     *   transportation to at least one of the URLs specified
     *   in the ticket.
     */
    public boolean acceptsTicket(Ticket ticket) {
        AgentGateway.Out gate;
        Iterator i;
        if (ticket != null) {
            for (i = getGateways().values().iterator(); i.hasNext(); ) {
                gate = (AgentGateway.Out) i.next();
                if (gate.acceptsTicket(ticket)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a set of all the protocols supported by the outgoing
     * gateways. The outgoing gateways must be published using the
     * path defined by {@link WhatIs WhatIs} key &quot;OUTGATE&quot;
     * with the protocol name as the last path element.
     *
     * @return The set of protocol names being supported by the
     *   outgoing gateways in the system at the time of the call.
     */
    public Set protocolSet() {
        Iterator i;
        Set protos;
        protos = new HashSet();
        for (i = getGateways().values().iterator(); i.hasNext(); ) {
            protos.add(((AgentGateway.Out) i.next()).protocol());
        }
        return protos;
    }

    /**
     * Callback that can be used to e.g. play a sound whenever
     * an agent is transported. This callback is invoked right
     * after a successful transport of each agent. The default
     * implementation does nothing.
     *
     * @param ctx The <code>AgentContext</code> that was just
     *   transported.
     */
    protected void onTransport(AgentContext ctx) {
        return;
    }

    /**
     * Returns the correct transport URL after a lookup at the remote
     * SHIP service. We interpret the fully qualified URLs enclosed in
     * the ticket as URL to a SHIP port. This is because actually
     * vicinity post its port within the LAN. Hence, we have to do a
     * SHIP lookup in order to get the URL of the desired transport
     * protocol.<p>
     *
     * EXAMPLE: Given that we have a ticket containing the URL
     * <tt>raw://somehost:47470</tt> and the port of RawInGate at
     * 'somehost' is 50000. Now, the port 47470 is understand to be
     * the port of the remote SHIP service. This method invokes a SHIP
     * request and - hopefully - transforms the ticket URL into
     * <tt>raw://somehost:50000</tt>.<p>
     *
     * This method does this resolving for all URLs contained in the
     * given ticket. If any error occurs during the ship request(s)
     * the URL will remain unchanged. This is the same if the local
     * ship service is not running. So occasionally the returned
     * ticket may be the given <code>ticket</code>.<p>
     *
     * Note: this URL replacement is a fine mechanism to do load
     * balancing on a cluster of agent servers. Simply configure the
     * cluster ingate server in a way that it tells different
     * transport URLs on every request.
     *
     * @param ticket The agent ticket to be transformed.
     * @return A ticket with the transformed URLs.
     * @exception NullPointerException iff <code>ticket</code> is
     *     <code>null</code>.
     */
    protected Ticket shipLookup(Ticket ticket) {
        ShipService ship;
        Environment env;
        String proto;
        String path;
        String key;
        String val;
        URL[] urls;
        URL url;
        if (ticket == null) {
            throw new NullPointerException("Parameter ticket may not be null");
        }
        env = Environment.getEnvironment();
        path = WhatIs.stringValue(ShipService.WHATIS);
        try {
            ship = (ShipService) env.lookup(path);
            if (ship == null) {
                System.err.println("[OutGate] No ship service found at '" + path + "'.");
                return ticket;
            }
        } catch (ClassCastException ex) {
            System.err.println("[OutGate] Service at '" + path + "' is not a ship service.");
            return ticket;
        }
        urls = ticket.getTargets();
        for (int i = 0; i < urls.length; i++) {
            try {
                proto = urls[i].getProtocol();
                key = "transport.in." + proto + ".url";
                val = ship.getValue(key, urls[i]);
                if (val == null || val.equals("")) {
                    continue;
                }
                url = new URL(val);
                if (!proto.equals(url.getProtocol())) {
                    System.err.println("[OutGate] Warning: SHIP retrieved " + "an URL with a different protocol than " + "the URL in the original ticket " + "specifies. Conversion form  " + url + " to " + urls[i] + " denied.");
                    continue;
                }
                urls[i] = url;
            } catch (ShipException ex) {
                continue;
            } catch (MalformedURLException ex) {
                continue;
            }
        }
        return new Ticket(urls);
    }
}
