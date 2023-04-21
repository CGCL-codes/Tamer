package de.haumacher.timecollect;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.lustin.trac.xmlprc.TrackerDynamicProxy;
import de.haumacher.timecollect.Ticket.LCState;
import de.haumacher.timecollect.common.config.PropertiesUtil;
import de.haumacher.timecollect.common.config.ValueFactory;
import de.haumacher.timecollect.plugin.Configurable;

public class Trac implements TicketResolver, Configurable {

    private static final String CONFIG_PREFIX = "trac.";

    private TracConfig config = ValueFactory.newInstance(TracConfig.class);

    private org.lustin.trac.xmlprc.Ticket ticketSystem;

    private final Pattern numberPattern = Pattern.compile("[^\\d]*(\\d+)(:?[^\\d].*)?");

    public void loadConfig(Properties properties) {
        PropertiesUtil.load(properties, CONFIG_PREFIX, this.config);
    }

    public void saveConfig(Properties properties) {
        PropertiesUtil.save(properties, CONFIG_PREFIX, this.config);
    }

    public TracConfig getConfig() {
        return config;
    }

    public void reset() {
        ticketSystem = null;
    }

    private org.lustin.trac.xmlprc.Ticket init() throws TracException {
        if (config.getUrl() == null) {
            throw new TracException("Trac not configured.");
        }
        XmlRpcClientConfigImpl clientConfig = new XmlRpcClientConfigImpl();
        try {
            clientConfig.setServerURL(new URL(config.getUrl()));
        } catch (MalformedURLException e) {
            throw new TracException("Invalid trac URL: " + config.getUrl());
        }
        clientConfig.setBasicUserName(config.getUser());
        clientConfig.setBasicPassword(config.getPasswd());
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(clientConfig);
        TrackerDynamicProxy dynamicProxy = new TrackerDynamicProxy(client);
        return (org.lustin.trac.xmlprc.Ticket) dynamicProxy.newInstance(org.lustin.trac.xmlprc.Ticket.class);
    }

    public Ticket getTicket(int ticketId) throws TicketException {
        try {
            Vector ticketProperties = getTicketSystem().get(ticketId);
            Map ticketEntries = (Map) ticketProperties.get(3);
            String summary = (String) ticketEntries.get("summary");
            String type = (String) ticketEntries.get("type");
            String component = (String) ticketEntries.get("component");
            String state = (String) ticketEntries.get("status");
            Ticket ticket = new Ticket(1, ticketId, "", summary, type, component, state, LCState.SELECTED);
            return ticket;
        } catch (XmlRpcException ex) {
            throw new TicketException("Trac reported error: " + ex.getMessage(), ex);
        } catch (TracException ex) {
            throw new TicketException("Trac access failed: " + ex.getMessage(), ex);
        }
    }

    public List<Integer> queryTickets() throws TracException {
        try {
            return getTicketSystem().query(config.getQuery());
        } catch (XmlRpcException ex) {
            throw new TracException("Trac reported error: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Ticket> queryTickets(String searchString) throws TicketException {
        List<Ticket> result = new ArrayList<Ticket>();
        Matcher matcher = numberPattern.matcher(searchString);
        if (matcher.matches()) {
            int id = Integer.parseInt(matcher.group(1));
            try {
                result.add(getTicket(id));
            } catch (TicketException ex) {
            }
        }
        try {
            Vector<Integer> matchingIds = getTicketSystem().query("summary~=" + searchString);
            for (int id : matchingIds) {
                result.add(getTicket(id));
            }
        } catch (XmlRpcException ex) {
            throw new TicketException("Trac reported error: " + ex.getMessage(), ex);
        } catch (TracException ex) {
            throw new TicketException("Trac access failed: " + ex.getMessage(), ex);
        }
        return result;
    }

    private org.lustin.trac.xmlprc.Ticket getTicketSystem() throws TracException {
        if (this.ticketSystem == null) {
            this.ticketSystem = init();
        }
        return ticketSystem;
    }
}
