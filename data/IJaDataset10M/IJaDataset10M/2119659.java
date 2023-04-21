package org.jsmtpd.plugins.dnsServices;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.dnsService.IDNSResolver;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.DClass;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.ReverseMap;
import org.xbill.DNS.Section;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;

/**
 * A mx resolver plugin, this one uses dnsjava, http://www.xbill.org/dnsjava/
 * @author Jean-Francois POUX
 */
public class DNSJavaResolver implements IDNSResolver {

    private String[] dnsServers;

    private ExtendedResolver dnsResolver = null;

    private Log log = LogFactory.getLog(DNSJavaResolver.class);

    public List<InetAddress> doMXLookup(String name) {
        if ((name == null) || (name.indexOf(".") <= 0)) return new LinkedList<InetAddress>();
        List<InetAddress> singleResults;
        singleResults = doSingleMXLookup(name);
        return singleResults;
    }

    private List<InetAddress> doSingleMXLookup(String name) {
        List<InetAddress> results = new LinkedList<InetAddress>();
        List<SortableMXRecord> mxRecords = new LinkedList<SortableMXRecord>();
        try {
            Lookup look = new Lookup(name, Type.MX);
            look.setResolver(dnsResolver);
            Record[] records = look.run();
            if (records == null) return results;
            for (int i = 0; i < records.length; i++) {
                if (records[i] instanceof MXRecord) {
                    MXRecord mx = (MXRecord) records[i];
                    mxRecords.add(new SortableMXRecord(mx));
                }
            }
            Collections.sort(mxRecords);
            for (int i = 0; i < mxRecords.size(); i++) {
                SortableMXRecord smx = (SortableMXRecord) mxRecords.get(i);
                MXRecord mx = smx.getMXRecord();
                Lookup tmp = new Lookup(mx.getTarget(), Type.A);
                tmp.setResolver(dnsResolver);
                Record[] partial = tmp.run();
                if (partial != null) {
                    for (int j = 0; j < partial.length; j++) {
                        if (partial[j] instanceof ARecord) {
                            ARecord re = (ARecord) partial[j];
                            results.add(re.getAddress());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("resolver error: ", e);
        }
        return results;
    }

    public String getPluginName() {
        return "DNSJava resolver plugin for jsmptd";
    }

    public void initPlugin() throws PluginInitException {
        SimpleResolver[] srez = new SimpleResolver[dnsServers.length];
        try {
            for (int i = 0; i < dnsServers.length; i++) {
                srez[i] = new SimpleResolver(dnsServers[i]);
            }
            dnsResolver = new ExtendedResolver(srez);
        } catch (UnknownHostException e) {
            throw new PluginInitException(e);
        }
    }

    public void setDnsServers(String cfg) {
        if (cfg.contains(",")) dnsServers = cfg.split(","); else {
            dnsServers = new String[1];
            dnsServers[0] = cfg;
        }
    }

    public void shutdownPlugin() {
    }

    public boolean exists(String name) {
        try {
            Lookup look = new Lookup(name, Type.ANY);
            look.setResolver(dnsResolver);
            Record[] records = look.run();
            if ((records == null) || (records.length == 0)) return false; else return true;
        } catch (Exception e) {
            log.error("error: ", e);
        }
        return false;
    }

    public InetAddress doLookupToIp(String name) {
        return null;
    }

    public String doReverseLookup(InetAddress in) {
        Name name = ReverseMap.fromAddress(in);
        int type = Type.PTR;
        int dclass = DClass.IN;
        Record rec = Record.newRecord(name, type, dclass);
        Message query = Message.newQuery(rec);
        Message response;
        try {
            response = dnsResolver.send(query);
        } catch (IOException e) {
            return "";
        }
        Record[] answers = response.getSectionArray(Section.ANSWER);
        if (answers.length == 0) return ""; else return answers[0].rdataToString();
    }
}
