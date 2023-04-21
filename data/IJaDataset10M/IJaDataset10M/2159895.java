package lazyj.mail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/**
 * Mail-related helper functions
 * 
 * @author costing
 * @since Sep 15, 2009
 * @since 1.0.6
 */
public class MailUtils {

    /**
	 * Wrapper around a MX DNS entry
	 * 
	 * @author costing
	 * @since Sep 15, 2009
	 * @see MailUtils#getMXServers(String)
	 */
    public static class MXRecord implements Comparable<MXRecord> {

        /**
		 * Server priority
		 */
        private int prio;

        /**
		 * Server address
		 */
        private String server;

        /**
		 * An attribute returned by the query
		 * 
		 * @param queryResponse
		 */
        MXRecord(final String queryResponse) {
            final StringTokenizer st = new StringTokenizer(queryResponse);
            this.prio = Integer.parseInt(st.nextToken());
            this.server = st.nextToken();
            if (this.server.endsWith(".")) this.server = this.server.substring(0, this.server.length() - 1);
        }

        @Override
        public int compareTo(final MXRecord other) {
            if (this.prio != other.prio) return this.prio - other.prio;
            return this.server.compareTo(other.server);
        }

        @Override
        public boolean equals(final Object o) {
            if (o == null || !(o instanceof MXRecord)) return false;
            return compareTo((MXRecord) o) == 0;
        }

        @Override
        public int hashCode() {
            return this.server.hashCode() * 31 + this.prio;
        }

        @Override
        public String toString() {
            return this.server;
        }

        /**
		 * Get the MX priority
		 * 
		 * @return priority
		 */
        public int getPriority() {
            return this.prio;
        }

        /**
		 * Get server name
		 * 
		 * @return server name
		 */
        public String getServer() {
            return this.server;
        }
    }

    /**
	 * Get the mail servers for the given domain name
	 * 
	 * @param domain
	 * @return list of mail servers, ordered by the DNS priority
	 */
    public static List<MXRecord> getMXServers(final String domain) {
        final Hashtable<String, String> env = new Hashtable<String, String>();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        try {
            final DirContext ictx = new InitialDirContext(env);
            final Attributes attrs = ictx.getAttributes(domain, new String[] { "MX" });
            final Attribute attr = attrs.get("MX");
            if (attr == null || attr.size() == 0) {
                return null;
            }
            final List<MXRecord> ret = new ArrayList<MXRecord>(attr.size());
            for (int i = 0; i < attr.size(); i++) {
                try {
                    final MXRecord record = new MXRecord(attr.get(i).toString());
                    ret.add(record);
                } catch (Throwable t) {
                }
            }
            Collections.sort(ret);
            return ret;
        } catch (Throwable e) {
        }
        return null;
    }

    /**
	 * Remove servers that show up more than once in a MX list of a domain.
	 * 
	 * @param original
	 * @return the list of servers where each server appears only once, and only the "best" one is kept.
	 * @see #getMXServers(String) 
	 */
    public static List<MXRecord> removeDuplicates(final List<MXRecord> original) {
        if (original == null) return null;
        final List<MXRecord> ret = new LinkedList<MXRecord>(original);
        if (ret.size() <= 1) return ret;
        for (int i = 0; i < ret.size() - 1; i++) {
            final MXRecord ref = ret.get(i);
            for (int j = i + 1; j < ret.size(); j++) {
                final MXRecord temp = ret.get(j);
                if (ref.getServer().equalsIgnoreCase(temp.getServer())) {
                    ret.remove(j);
                    j--;
                }
            }
        }
        return ret;
    }
}
