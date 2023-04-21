package com.ixora.rms.agents.apache;

import com.ixora.rms.CounterId;
import com.ixora.rms.CounterType;
import com.ixora.rms.EntityId;
import com.ixora.rms.agents.AgentExecutionContext;
import com.ixora.rms.agents.apache.ApacheAgent.ApacheExecutionContext;
import com.ixora.rms.agents.apache.messages.Msg;
import com.ixora.rms.agents.impl.Counter;
import com.ixora.rms.agents.impl.Entity;
import com.ixora.rms.data.CounterValueDouble;

/**
 * Entity representing the status of the Apache server.
 * Counters:
 * 		Total accesses
 * 		Total traffic(MB)
 * 		Requests/sec
 * 		KiloBytes/sec
 * 		KiloBytes/request
 * 		Total number of servers
 * 		Idle servers
 * 		Servers in "Waiting for connection" state
 * 		Servers in "Starting up" state
 * 		Servers in "Reading request" state
 * 		Servers in "Sending reply" state
 * 		Servers in "Keepalive(Read)" state
 * 		Servers in "DNS lookup" state
 * 		Servers in "Logging" state
 * 		Servers in "Gracefully finishing" state
 * @author Daniel Moraru
 */
public final class ApacheEntityStatus extends Entity implements ApacheConstants {

    private static final long serialVersionUID = 3960207818163346703L;

    /**
	 * @param parent
	 * @param c
	 */
    public ApacheEntityStatus(EntityId parent, AgentExecutionContext c) {
        super(new EntityId(parent, Msg.APACHE_ENTITY_STATUS), Msg.APACHE_ENTITY_STATUS_DESCRIPTION, c);
        fHasChildren = false;
        addCounter(new Counter(Msg.APACHE_ENTITY_STATUS_COUNTER_TOTAL_NUMBER_OF_SERVERS, Msg.APACHE_ENTITY_STATUS_COUNTER_TOTAL_NUMBER_OF_SERVERS + ".description"));
        if (getApacheContext().isExtendedStatusOn()) {
            addCounter(new Counter(Msg.APACHE_ENTITY_STATUS_COUNTER_TOTAL_ACCESSES, Msg.APACHE_ENTITY_STATUS_COUNTER_TOTAL_ACCESSES + ".description"));
            addCounter(new Counter(Msg.APACHE_ENTITY_STATUS_COUNTER_TOTAL_TRAFFIC, Msg.APACHE_ENTITY_STATUS_COUNTER_TOTAL_TRAFFIC + ".description", CounterType.DOUBLE));
            addCounter(new Counter(Msg.APACHE_ENTITY_STATUS_COUNTER_REQUESTS_PER_SECOND, Msg.APACHE_ENTITY_STATUS_COUNTER_REQUESTS_PER_SECOND + ".description", CounterType.DOUBLE));
            addCounter(new Counter(Msg.APACHE_ENTITY_STATUS_COUNTER_KILOBYTES_PER_SECOND, Msg.APACHE_ENTITY_STATUS_COUNTER_KILOBYTES_PER_SECOND + ".description"));
            addCounter(new Counter(Msg.APACHE_ENTITY_STATUS_COUNTER_KILOBYTES_PER_REQUEST, Msg.APACHE_ENTITY_STATUS_COUNTER_KILOBYTES_PER_REQUEST + ".description", CounterType.DOUBLE));
        }
        addCounter(new Counter(Msg.APACHE_ENTITY_STATUS_COUNTER_IDLE_SERVERS, Msg.APACHE_ENTITY_STATUS_COUNTER_IDLE_SERVERS + ".description"));
        addCounter(new Counter(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_WAITING_FOR_CONNECTION, Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_WAITING_FOR_CONNECTION + ".description"));
        addCounter(new Counter(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_DNSLOOKUP, Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_DNSLOOKUP + ".description"));
        addCounter(new Counter(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_GRACEFULLY_FINISHING, Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_GRACEFULLY_FINISHING + ".description"));
        addCounter(new Counter(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_KEEPALIVE, Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_KEEPALIVE + ".description"));
        addCounter(new Counter(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_LOGGING, Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_LOGGING + ".description"));
        addCounter(new Counter(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_READING_REQUEST, Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_READING_REQUEST + ".description"));
        addCounter(new Counter(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_SENDING_REPLY, Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_SENDING_REPLY + ".description"));
        addCounter(new Counter(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_STARTING_UP, Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_STARTING_UP + ".description"));
    }

    /**
	 * @see com.ixora.rms.agents.impl.Entity#retrieveCounterValues()
	 */
    protected void retrieveCounterValues() throws Throwable {
        ApacheAgentExecutionContext c = (ApacheAgentExecutionContext) this.fContext;
        String text = c.getStatusPageContent();
        if (text == null) {
            return;
        }
        switch(c.getApacheVersion()) {
            case VERSION_ORACLE_1_3_X:
            case VERSION_IBM_1_3_X:
                if (getApacheContext().isExtendedStatusOn()) {
                    parseExtendedStatus_1_3_x(text);
                } else {
                    parseStatus_1_3_x(text);
                }
                break;
            case VERSION_ORACLE_2_0_X:
            case VERSION_IBM_2_0_X:
                if (getApacheContext().isExtendedStatusOn()) {
                    parseExtendedStatus_2_0_x(text);
                } else {
                    parseStatus_1_3_x(text);
                }
                break;
            case VERSION_IBM_6_0_X:
                if (getApacheContext().isExtendedStatusOn()) {
                    parseExtendedStatus_ibm_6_0(text);
                } else {
                    parseStatus_1_3_x(text);
                }
                break;
        }
    }

    /**
	 * Parses the content for Apache 1.3.x
	 * All counters will be extracted even if just
	 * some of them are enabled as it seems to involve
	 * even more work to filter the disabled ones out.
	 */
    private void parseExtendedStatus_1_3_x(String text) {
        String tmp;
        char unit;
        float f;
        char ch;
        Counter c;
        int idx = text.indexOf("Total accesses");
        int idx2 = text.indexOf('-', idx);
        tmp = text.substring(idx + 16, idx2 - 1);
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_TOTAL_ACCESSES));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(Integer.parseInt(tmp)));
        }
        idx = text.indexOf("T", idx2 + 1);
        idx2 = text.indexOf(' ', idx + 15);
        tmp = text.substring(idx + 15, idx2);
        unit = text.substring(idx2 + 1, idx2 + 2).charAt(0);
        f = Float.parseFloat(tmp);
        f = getMegaBytes(f, unit);
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_TOTAL_TRAFFIC));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(f));
        }
        idx = text.indexOf('>', idx2);
        idx2 = text.indexOf('r', idx + 1);
        tmp = text.substring(idx + 2, idx2 - 1);
        ch = tmp.charAt(0);
        if (ch != '.' && !Character.isDigit(ch)) {
            tmp = tmp.substring(1);
        }
        f = Float.parseFloat(tmp);
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_REQUESTS_PER_SECOND));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(f));
        }
        idx = text.indexOf('-', idx2);
        idx2 = text.indexOf('B', idx + 1);
        tmp = text.substring(idx + 2, idx2 - 1);
        unit = text.charAt(idx2 - 1);
        f = Float.parseFloat(tmp);
        f = getKiloBytes(f, unit);
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_KILOBYTES_PER_SECOND));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(f));
        }
        idx = text.indexOf('-', idx2);
        idx2 = text.indexOf('B', idx + 1);
        tmp = text.substring(idx + 2, idx2 - 1);
        unit = text.charAt(idx2 - 1);
        f = Float.parseFloat(tmp);
        f = getKiloBytes(f, unit);
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_KILOBYTES_PER_REQUEST));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(f));
        }
        int active;
        int idle;
        int total;
        idx = idx2;
        while (!Character.isDigit(text.charAt(idx++))) {
            ;
        }
        idx2 = text.indexOf(' ', idx);
        tmp = text.substring(idx - 1, idx2);
        active = Integer.parseInt(tmp);
        idx = text.indexOf(',', idx2);
        idx2 = text.indexOf("i", idx + 1);
        tmp = text.substring(idx + 2, idx2 - 1);
        idle = Integer.parseInt(tmp);
        total = active + idle;
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_TOTAL_NUMBER_OF_SERVERS));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(active + idle));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_IDLE_SERVERS));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(idle));
        }
        idx = text.indexOf('>', idx2);
        tmp = text.substring(idx + 1, idx + total + 1);
        int[] states = getServerStates(tmp);
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_WAITING_FOR_CONNECTION));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[0]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_KEEPALIVE));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[1]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_READING_REQUEST));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[2]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_SENDING_REPLY));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[3]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_STARTING_UP));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[4]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_DNSLOOKUP));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[5]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_LOGGING));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[6]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_GRACEFULLY_FINISHING));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[7]));
        }
    }

    /**
	 * Parses the content vor Apache 2.0.x
	 * All counters will be extracted even if just
	 * some of them are enabled as it seems to involve
	 * even more work to filter the disabled ones out.
	 */
    private void parseExtendedStatus_2_0_x(String text) {
        String tmp;
        char unit;
        float f;
        char ch;
        Counter c;
        int idx = text.indexOf("Total accesses");
        int idx2 = text.indexOf('-', idx);
        tmp = text.substring(idx + 16, idx2 - 1);
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_TOTAL_ACCESSES));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(Integer.parseInt(tmp)));
        }
        idx = text.indexOf("T", idx2 + 1);
        idx2 = text.indexOf(' ', idx + 15);
        tmp = text.substring(idx + 15, idx2);
        unit = text.substring(idx2 + 1, idx2 + 2).charAt(0);
        f = Float.parseFloat(tmp);
        f = getMegaBytes(f, unit);
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_TOTAL_TRAFFIC));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(f));
        }
        idx = text.indexOf("<dt>", idx2);
        idx2 = text.indexOf('r', idx + 1);
        tmp = text.substring(idx + 4, idx2 - 1);
        ch = tmp.charAt(0);
        if (ch != '.' && !Character.isDigit(ch)) {
            tmp = tmp.substring(1);
        }
        f = Float.parseFloat(tmp);
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_REQUESTS_PER_SECOND));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(f));
        }
        idx = text.indexOf('-', idx2);
        idx2 = text.indexOf('B', idx + 1);
        tmp = text.substring(idx + 2, idx2 - 1);
        unit = text.charAt(idx2 - 1);
        f = Float.parseFloat(tmp);
        f = getKiloBytes(f, unit);
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_KILOBYTES_PER_SECOND));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(f));
        }
        idx = text.indexOf('-', idx2);
        idx2 = text.indexOf('B', idx + 1);
        tmp = text.substring(idx + 2, idx2 - 1);
        unit = text.charAt(idx2 - 1);
        f = Float.parseFloat(tmp);
        f = getKiloBytes(f, unit);
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_KILOBYTES_PER_REQUEST));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(f));
        }
        int active;
        int idle;
        int total;
        idx = idx2;
        while (!Character.isDigit(text.charAt(idx++))) {
            ;
        }
        idx2 = text.indexOf(' ', idx);
        tmp = text.substring(idx - 1, idx2);
        active = Integer.parseInt(tmp);
        idx = text.indexOf(',', idx2);
        idx2 = text.indexOf("i", idx + 1);
        tmp = text.substring(idx + 2, idx2 - 1);
        idle = Integer.parseInt(tmp);
        total = active + idle;
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_TOTAL_NUMBER_OF_SERVERS));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(active + idle));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_IDLE_SERVERS));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(idle));
        }
        idx = text.indexOf('>', idx2);
        tmp = text.substring(idx + 1, idx + total + 1);
        int[] states = getServerStates(tmp);
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_WAITING_FOR_CONNECTION));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[0]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_KEEPALIVE));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[1]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_READING_REQUEST));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[2]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_SENDING_REPLY));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[3]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_STARTING_UP));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[4]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_DNSLOOKUP));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[5]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_LOGGING));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[6]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_GRACEFULLY_FINISHING));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[7]));
        }
    }

    /**
	 * Parses the content vor IBM http server 6.0.x
	 * All counters will be extracted even if just
	 * some of them are enabled
	 */
    private void parseExtendedStatus_ibm_6_0(String text) {
        String tmp;
        char unit;
        float f;
        char ch;
        Counter c;
        int idx = text.indexOf("Total accesses");
        int idx2 = text.indexOf('-', idx);
        tmp = text.substring(idx + 16, idx2 - 1);
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_TOTAL_ACCESSES));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(Integer.parseInt(tmp)));
        }
        idx = text.indexOf("T", idx2 + 1);
        idx2 = text.indexOf(' ', idx + 15);
        tmp = text.substring(idx + 15, idx2);
        unit = text.substring(idx2 + 1, idx2 + 2).charAt(0);
        f = Float.parseFloat(tmp);
        f = getMegaBytes(f, unit);
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_TOTAL_TRAFFIC));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(f));
        }
        idx = text.indexOf("<dt>", idx2);
        idx2 = text.indexOf('r', idx + 1);
        tmp = text.substring(idx + 4, idx2 - 1);
        ch = tmp.charAt(0);
        if (ch != '.' && !Character.isDigit(ch)) {
            tmp = tmp.substring(1);
        }
        f = Float.parseFloat(tmp);
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_REQUESTS_PER_SECOND));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(f));
        }
        idx = text.indexOf('-', idx2);
        idx2 = text.indexOf('B', idx + 1);
        tmp = text.substring(idx + 2, idx2 - 1);
        unit = text.charAt(idx2 - 1);
        f = Float.parseFloat(tmp);
        f = getKiloBytes(f, unit);
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_KILOBYTES_PER_SECOND));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(f));
        }
        idx = text.indexOf('-', idx2);
        idx2 = text.indexOf('B', idx + 1);
        tmp = text.substring(idx + 2, idx2 - 1);
        unit = text.charAt(idx2 - 1);
        f = Float.parseFloat(tmp);
        f = getKiloBytes(f, unit);
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_KILOBYTES_PER_REQUEST));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(f));
        }
        int active;
        int idle;
        int total;
        idx = idx2;
        while (!Character.isDigit(text.charAt(idx++))) {
            ;
        }
        idx2 = text.indexOf(' ', idx);
        tmp = text.substring(idx - 1, idx2);
        active = Integer.parseInt(tmp);
        idx = text.indexOf(',', idx2);
        idx2 = text.indexOf("i", idx + 1);
        tmp = text.substring(idx + 2, idx2 - 1);
        idle = Integer.parseInt(tmp);
        total = active + idle;
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_TOTAL_NUMBER_OF_SERVERS));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(active + idle));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_IDLE_SERVERS));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(idle));
        }
        idx = text.indexOf('>', idx2);
        tmp = text.substring(idx + 1, idx + total + 1);
        int[] states = getServerStates(tmp);
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_WAITING_FOR_CONNECTION));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[0]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_KEEPALIVE));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[1]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_READING_REQUEST));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[2]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_SENDING_REPLY));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[3]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_STARTING_UP));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[4]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_DNSLOOKUP));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[5]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_LOGGING));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[6]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_GRACEFULLY_FINISHING));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[7]));
        }
    }

    /**
	 * Applies the unit char to the given value
	 * @param f value in MB
	 * @param unit
	 * @return the new value
	 */
    private float getMegaBytes(float f, char unit) {
        if (unit == 'k' || unit == 'K') {
            return f / 1024;
        }
        if (unit == 'g' || unit == 'G') {
            return f * 1024;
        }
        if (unit == ' ' || unit == 'b' || unit == 'B') {
            return f / 1048576;
        }
        return f;
    }

    /**
	 * Applies the unit char to the given value
	 * @param f value in KiloBytes
	 * @param unit
	 * @return the new value
	 */
    private float getKiloBytes(float f, char unit) {
        if (unit == ' ' || unit == 'b' || unit == 'B') {
            return f / 1024;
        }
        if (unit == 'g' || unit == 'G') {
            return f * 1048576;
        }
        if (unit == 'm' || unit == 'M') {
            return f * 1024;
        }
        return f;
    }

    /**
	 * @param text the string with the server states
	 * @return the number of server in various states
	 * in this order:
	 * Waiting for Connection (_)
	 * Keepalive (read) (K)
	 * Reading Request (R)
	 * Sending Reply (W)
	 * Starting up (S)
	 * DNS Lookup (D)
	 * Logging (L)
	 * Gracefully finishing (G)
	 */
    private int[] getServerStates(String text) {
        int wc = 0, k = 0, r = 0, w = 0, s = 0, d = 0, l = 0, g = 0;
        int len = text.length();
        char ch;
        for (int i = 0; i < len; i++) {
            ch = text.charAt(i);
            switch(ch) {
                case '_':
                    ++wc;
                    break;
                case 'K':
                    ++k;
                    break;
                case 'R':
                    ++r;
                    break;
                case 'W':
                    ++w;
                    break;
                case 'S':
                    ++s;
                    break;
                case 'D':
                    ++d;
                    break;
                case 'L':
                    ++l;
                    break;
                case 'G':
                    ++g;
                    break;
                default:
                    break;
            }
        }
        return new int[] { wc, k, r, w, s, d, l, g };
    }

    /**
	 * Parses the content vor Apache 1.3.x
	 * All counters will be extracted even if just
	 * some of them are enabled.
	 */
    private void parseStatus_1_3_x(String text) {
        Counter c;
        String tmp;
        int idx = text.indexOf("Server uptime:");
        int idx2 = text.indexOf('>', idx);
        int active;
        int idle;
        int total;
        idx = idx2;
        while (!Character.isDigit(text.charAt(idx++))) {
            ;
        }
        idx2 = text.indexOf(' ', idx);
        tmp = text.substring(idx - 1, idx2);
        active = Integer.parseInt(tmp);
        idx = text.indexOf(',', idx2);
        idx2 = text.indexOf("i", idx + 1);
        tmp = text.substring(idx + 2, idx2 - 1);
        idle = Integer.parseInt(tmp);
        total = active + idle;
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_TOTAL_NUMBER_OF_SERVERS));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(active + idle));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_IDLE_SERVERS));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(idle));
        }
        idx = text.indexOf('>', idx2);
        tmp = text.substring(idx + 1, idx + total + 1);
        int[] states = getServerStates(tmp);
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_WAITING_FOR_CONNECTION));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[0]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_KEEPALIVE));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[1]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_READING_REQUEST));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[2]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_SENDING_REPLY));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[3]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_STARTING_UP));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[4]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_DNSLOOKUP));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[5]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_LOGGING));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[6]));
        }
        c = getCounter(new CounterId(Msg.APACHE_ENTITY_STATUS_COUNTER_SERVERS_IN_GRACEFULLY_FINISHING));
        if (c.isEnabled()) {
            c.dataReceived(new CounterValueDouble(states[7]));
        }
    }

    /**
	 * @return
	 */
    private ApacheExecutionContext getApacheContext() {
        return (ApacheExecutionContext) this.fContext;
    }
}
