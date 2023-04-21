package monitor.layer3logic;

import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import monitor.edu.berkeley.guir.prefuse.graph.DefaultNode;
import monitor.mib.DiscoveryType;
import monitor.mib.NamedOID;
import monitor.snmp.QueryHandler;
import monitor.utils.AbstractIpAddress;
import monitor.utils.AddrCommunityPair;
import monitor.snmp.L3SnmpResponseColl;
import java.io.*;
import net.AddressException;

public class L3NodeDiscover extends Thread {

    private static final String blacklistfile = "blacklist.txt";

    private static final String defaultcommunity = "sikcededBo";

    private Vector<AddrCommunityPair> startIPs;

    private QueryHandler qh;

    public PropetryHandler prh;

    private boolean finished = false;

    private Set<AbstractIpAddress> vizsgalt;

    private BufferedReader bin;

    private BufferedWriter bout;

    public L3NodeDiscover(Vector<AddrCommunityPair> startRouters) throws IOException {
        this.startIPs = startRouters;
        qh = new QueryHandler();
        prh = new PropetryHandler();
        vizsgalt = new TreeSet<AbstractIpAddress>();
        File file = new File(blacklistfile);
        if (!file.exists()) file.createNewFile();
        bin = new BufferedReader(new FileReader(file));
        bout = new BufferedWriter(new FileWriter(file, true));
    }

    /**
	 * Felveszi annak az eszk�znek az IP c�met a tilt�list�ra,
	 * amelyik nem v�laszolt az SNMP k�r�sre, vagy nem tudtunk kapcsol�dni
	 * */
    protected void ResponseCheck(Vector<AddrCommunityPair> query) throws IOException {
        for (AddrCommunityPair addrcomm : query) {
            boolean response = false;
            for (L3SnmpResponseColl node : this.prh.getProcessedNodes()) if (node.managementIP.equals(addrcomm.addr)) {
                response = true;
                break;
            }
            if (!response) {
                bout.append(addrcomm.addr.toString());
                bout.append("\n");
            }
        }
    }

    public void discoverAndCreateTopology() throws Exception {
        System.out.println("Starting MPLS Discovery...");
        finished = false;
        Vector<AddrCommunityPair> query = new Vector<AddrCommunityPair>();
        query.addAll(startIPs);
        Vector<NamedOID> oids = this.prh.getOIDs(DiscoveryType.Layer3);
        while (!query.isEmpty()) {
            qh.collectData(query, oids, DiscoveryType.Layer3);
            for (AddrCommunityPair pair : query) {
                vizsgalt.add(pair.addr);
            }
            while (!qh.isFinished()) {
                try {
                    sleep(1000 * startIPs.size());
                } catch (Exception e) {
                }
            }
            prh.processQueryColls(qh.getNodes());
            ResponseCheck(query);
            query.clear();
            for (DefaultNode node : qh.getNodes()) for (String ipcim : ((L3SnmpResponseColl) node).getNeighbours()) {
                AbstractIpAddress ip = ((L3SnmpResponseColl) node).CreateIpFromLDPid(ipcim);
                if (!vizsgalt.contains(ip)) query.add(new AddrCommunityPair(ip, defaultcommunity));
            }
        }
        long begin, end;
        System.out.print("Fizikai felder�t�s...");
        begin = System.currentTimeMillis();
        prh.physicalDiscovery();
        end = System.currentTimeMillis();
        System.out.println("k�sz. Eltelt id�:" + ((end - begin) / 1000) + " sec");
        System.out.println(" *** MPLS 'fizikai' topol�gia ***");
        prh.printPhys();
        finished = true;
    }

    public Vector<AddrCommunityPair> getStartRouters() {
        return startIPs;
    }

    public void setStartRouters(Vector<AddrCommunityPair> snmpStart) {
        this.startIPs = snmpStart;
    }

    public boolean isFinished() {
        return finished;
    }

    /**
	 * Betölti fájlból az IP címeket, amelyeket nem kell lekérdezni a felderítés során
	 * */
    public void LoadBlacklist() throws IOException, AddressException {
        String buffer;
        while ((buffer = bin.readLine()) != null) {
            vizsgalt.add(AbstractIpAddress.createAddress(buffer));
        }
    }

    /**
	 * V�ltoz�sok ment�se a tilt�lista f�jlban
	 * */
    public void SaveBlacklist() throws IOException {
        bin.close();
        bout.close();
    }

    public static void main(String[] args) throws IOException, AddressException {
        Vector<AddrCommunityPair> ips = new Vector<AddrCommunityPair>();
        ips.add(new AddrCommunityPair(AbstractIpAddress.createAddress("195.111.97.232"), "sikcededBo"));
        L3NodeDiscover nd = new L3NodeDiscover(ips);
        try {
            long start_time = System.currentTimeMillis();
            nd.LoadBlacklist();
            nd.discoverAndCreateTopology();
            nd.SaveBlacklist();
            long finish_time = System.currentTimeMillis();
            System.out.println("A felderités összesen " + (finish_time - start_time) / 1000 + " másodpercet vett igénybe!");
            String xml = nd.prh.printPhysToXML();
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter("discovery.xml"));
                out.write(xml);
                out.close();
            } catch (IOException e) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
