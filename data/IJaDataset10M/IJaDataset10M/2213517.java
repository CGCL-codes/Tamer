package net.spamcomplaint.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.logging.Level;
import net.spamcomplaint.App;
import net.spamcomplaint.util.NetworkUtil;
import net.spamcomplaint.whois.Whois;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.DClass;
import org.xbill.DNS.ExtendedResolver;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.ReverseMap;
import org.xbill.DNS.Section;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TXTRecord;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

public class ReverseDnsTest {

    private static final String[] TEST_IPS = { "122.211.244.67", "123.224.102.90", "125.164.172.11", "125.191.86.45", "125.24.192.31", "130.13.166.227", "148.244.180.109", "151.21.179.134", "189.156.15.45", "189.25.82.224", "189.5.207.184", "190.45.104.23", "190.47.215.138", "190.65.67.3", "193.19.223.113", "200.101.49.135", "200.88.186.113", "201.141.206.74", "201.160.200.6", "201.219.82.15", "201.22.255.162", "201.240.216.246", "201.250.97.9", "201.255.174.14", "201.36.216.195", "201.6.254.245", "201.9.138.226", "206.169.89.44", "207.112.82.225", "212.195.104.174", "213.237.52.130", "217.132.149.120", "217.228.111.149", "217.230.2.178", "219.133.212.117", "219.68.215.121", "219.95.163.172", "220.165.107.181", "221.7.107.13", "222.130.251.195", "222.132.177.62", "222.138.141.76", "222.99.108.112", "24.235.214.4", "58.142.100.35", "58.61.118.20", "59.48.127.140", "59.96.153.82", "60.213.206.204", "60.50.120.219", "60.6.162.56", "61.169.169.65", "61.217.63.96", "77.101.63.34", "77.198.184.26", "78.113.106.207", "78.3.113.150", "79.2.32.148", "80.56.107.116", "81.214.19.122", "81.215.165.34", "81.227.17.92", "81.29.247.22", "81.29.247.38", "82.207.201.93", "82.238.244.162", "82.52.151.120", "83.110.143.205", "83.181.89.108", "83.182.207.190", "83.19.111.210", "83.193.195.173", "83.233.26.129", "83.29.50.78", "84.142.219.32", "84.153.122.75", "84.168.223.11", "84.177.210.113", "84.220.242.229", "84.61.87.141", "85.136.38.173", "85.164.215.237", "85.181.194.194", "85.192.164.242", "85.2.14.30", "85.232.212.155", "85.241.127.214", "85.97.103.11", "85.97.149.146", "86.130.107.32", "86.61.109.228", "86.97.214.75", "87.10.36.226", "87.110.57.191", "87.181.225.177", "87.185.44.111", "88.111.13.249", "88.218.72.130", "88.226.151.31", "88.227.12.2", "88.232.52.218", "88.239.57.94", "88.254.236.217", "88.254.95.190", "89.235.149.179", "89.241.7.156", "89.53.208.35", "91.96.142.19", "97.84.147.177" };

    /**
     * 
     * @param hostIp
     * @return host name resolved from IP address or IP if host could resolve
     * @throws IOException
     */
    public static String[] reverseDns(String hostIp) throws IOException {
        Resolver res = new ExtendedResolver();
        Name name = ReverseMap.fromAddress(hostIp);
        int type = Type.PTR;
        int dclass = DClass.IN;
        Record rec = Record.newRecord(name, type, dclass);
        Message query = Message.newQuery(rec);
        Message response = res.send(query);
        System.out.println("#Java DNS response follows\n" + response);
        Record[] answers = response.getSectionArray(Section.ANSWER);
        if (answers.length == 0) return new String[] { hostIp }; else {
            String[] hosts = new String[answers.length];
            for (int i = 0; i < answers.length; i++) {
                String host = answers[i].rdataToString();
                System.out.println("JavaDNS = " + answers[i]);
                System.out.println("   host = " + host);
                System.out.println("   type = " + answers[i].getType());
                System.out.println(" dclass = " + answers[i].getDClass());
                if (host.endsWith(".")) hosts[i] = host.substring(0, host.length() - 1);
            }
            return hosts;
        }
    }

    public static void dnsQuery(String address, String dnsblDomain, int type) throws UnknownHostException, TextParseException {
        String name = address + "." + dnsblDomain;
        Lookup lookup = new Lookup(name, type);
        Resolver resolver = new SimpleResolver();
        lookup.setResolver(resolver);
        lookup.setCache(null);
        Record[] records = lookup.run();
        if (lookup.getResult() == Lookup.SUCCESSFUL) {
            String responseMessage = "";
            String listingType = null;
            for (int i = 0; i < records.length; i++) {
                if (records[i] instanceof TXTRecord) {
                    TXTRecord txt = (TXTRecord) records[i];
                    for (Iterator j = txt.getStrings().iterator(); j.hasNext(); ) {
                        responseMessage += "\n\t" + (String) j.next();
                    }
                } else if (records[i] instanceof ARecord) {
                    listingType = ((ARecord) records[i]).getAddress().getHostAddress();
                    System.out.println("ARecord = " + listingType);
                }
            }
            System.out.println(name + " responsed: " + responseMessage);
        } else if (lookup.getResult() == Lookup.HOST_NOT_FOUND) {
            System.out.println(name + ": Not found.");
        } else {
            System.out.println("Error!");
        }
    }

    public static String[] timeJavaDnsLookup(String ip) throws IOException {
        String[] ret;
        long now = System.currentTimeMillis();
        ret = reverseDns(ip);
        long after = System.currentTimeMillis();
        System.out.println(ip + "  dns took " + (after - now) + " ms");
        return ret;
    }

    public static String timeJavaLookup(String ip) throws IOException {
        String ret;
        long now = System.currentTimeMillis();
        ret = InetAddress.getByName(ip).getHostName();
        long after = System.currentTimeMillis();
        System.out.println(ip + " java took " + (after - now) + " ms");
        return ret;
    }

    private static void check(String ip) throws IOException {
        System.out.println("-----------------");
        String[] javadnsHosts = timeJavaDnsLookup(ip);
        String javaHost = timeJavaLookup(ip);
        if (javadnsHosts.length == 1 && javadnsHosts[0].equals(javaHost)) {
            System.out.println("#java+javadns = " + javaHost);
            if (!ip.equals(javadnsHosts[0])) dnsQuery(javaHost, "contacts.abuse.net", Type.TXT);
        } else {
            for (int i = 0; i < javadnsHosts.length; i++) {
                String host = javadnsHosts[i];
                System.out.println("#javadns lookup: " + host);
                if (!ip.equals(host)) dnsQuery(host, "contacts.abuse.net", Type.TXT);
            }
            System.out.println("#java lookup   : " + javaHost);
            if (!ip.equals(javaHost)) dnsQuery(javaHost, "contacts.abuse.net", Type.TXT);
        }
        System.out.print("#");
        dnsQuery(ip, "sbl-xbl.spamhaus.org", Type.ANY);
        System.out.println("#whois client response follows");
        try {
            String str = whois.lookupByIp(ip);
            System.out.println(str);
        } catch (Exception e) {
            App.log.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static Whois whois = new Whois();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String cmd;
        while (true) {
            System.err.print("\n[q=quit, IP Address, or t=test_all]$ ");
            while ((cmd = br.readLine()) != null) {
                cmd = cmd.trim();
                if (cmd.length() == 0) break;
                if (cmd.equalsIgnoreCase("q")) return;
                if (cmd.equalsIgnoreCase("t")) {
                    for (int i = 0; i < TEST_IPS.length; i++) try {
                        check(TEST_IPS[i]);
                    } catch (IOException e) {
                        App.log.log(Level.SEVERE, e.getMessage(), e);
                    }
                    break;
                }
                if (NetworkUtil.isIp(cmd)) try {
                    check(cmd);
                    break;
                } catch (IOException e) {
                    App.log.log(Level.SEVERE, e.getMessage(), e);
                } else {
                    System.err.println("Unrecoginzied command or IP: " + cmd);
                    break;
                }
            }
        }
    }
}
