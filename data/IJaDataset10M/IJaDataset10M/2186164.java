package com.whitebearsolutions.imagine.wbsagnitio.directory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import com.whitebearsolutions.directory.Entry;
import com.whitebearsolutions.directory.EntryBase;
import com.whitebearsolutions.directory.Query;
import com.whitebearsolutions.directory.ldap.LDAPEntry;
import com.whitebearsolutions.imagine.wbsagnitio.NetworkManager;
import com.whitebearsolutions.imagine.wbsagnitio.configuration.DNSConfiguration;
import com.whitebearsolutions.util.Configuration;

public class DomainNameManager {

    public static final int A_RECORD = 1;

    public static final int CNAME_RECORD = 2;

    private String domain;

    private Configuration _c;

    private EntryBase _eb;

    private NetworkManager _nm;

    public DomainNameManager(Configuration conf) throws Exception {
        this._c = conf;
        this._eb = new EntryBase(this._c);
        this._nm = new NetworkManager(this._c);
        this.domain = this._nm.getDomain();
    }

    public String getDefaultDomain() {
        return this.domain;
    }

    public Entry getDomainNameEntry(String name, String zone) throws Exception {
        Query _q = new Query();
        _q.addCondition("relativeDomainName", name, Query.EXACT);
        if (zone != null && !zone.isEmpty()) {
            _q.addCondition("zoneName", zone, Query.EXACT);
        } else {
            _q.addCondition("zoneName", this._nm.getDomain(), Query.EXACT);
        }
        List<Entry> _results = this._eb.search(_q);
        if (_results.size() <= 0) {
            throw new Exception("domain name [" + name + "] does not exist");
        }
        return _results.get(0);
    }

    public List<Entry> getForwardZones() throws Exception {
        return searchForwardZones(null);
    }

    public Entry getRecord(String name, String zone) throws Exception {
        if (name == null) {
            name = "@";
        }
        if (zone == null) {
            zone = this.domain;
        }
        if (zone.startsWith(".")) {
            zone = zone.substring(1);
        }
        if (zone.endsWith(".")) {
            zone = zone.substring(0, zone.length() - 1);
        }
        Entry _e = getDomainNameEntry(name, zone);
        if (_e == null) {
            if (name.equals("@")) {
                throw new Exception("zone name [" + zone + "] does not exist");
            } else {
                throw new Exception("domain name [" + name + "." + zone + "] does not exist");
            }
        }
        return _e;
    }

    public List<Entry> getRecords(String zone) throws Exception {
        if (zone == null) {
            zone = this.domain;
        }
        if (zone.startsWith(".")) {
            zone = zone.substring(1);
        }
        if (zone.endsWith(".")) {
            zone = zone.substring(0, zone.length() - 1);
        }
        return searchDomainNames(null, zone);
    }

    public Entry getDomainZoneEntry(String zone) throws Exception {
        Query _q = new Query();
        _q.addCondition("relativeDomainName", "@", Query.EXACT);
        if (zone != null && !zone.isEmpty()) {
            _q.addCondition("zoneName", zone);
        } else {
            _q.addCondition("zoneName", new NetworkManager(this._c).getDomain());
        }
        List<Entry> _results = this._eb.search(_q);
        if (_results.size() <= 0) {
            throw new Exception("domain zone [" + zone + "] does not exist");
        }
        return _results.get(0);
    }

    /**
	 * Verifica si es la zona o dominio principal del directorio.
	 * 
	 * @param domain
	 * @return
	 */
    public boolean isMainZone(String domain) {
        if (this.domain.equals(domain)) {
            return true;
        }
        return false;
    }

    /**
	 * verifica si es un dominio del directorio.
	 * 
	 * @param zone
	 * @return
	 */
    public boolean isLocalDomain(String zone) {
        try {
            List<Entry> results = searchForwardZones(zone);
            if (results != null && !results.isEmpty()) {
                return true;
            }
        } catch (Exception _ex) {
        }
        return false;
    }

    public boolean isEditable(Entry e) throws Exception {
        if (e == null) {
            throw new Exception("invalid domain name entry");
        }
        if (!(new ArrayList<Object>(Arrays.asList(e.getAttribute("objectclass")))).contains("dNSZone")) {
            throw new Exception("invalid domain name entry");
        }
        if (e.hasAttribute("sOARecord")) {
            if (!String.valueOf(e.getAttribute("zoneName")[0]).contains(".in-addr.arpa")) {
                return true;
            }
        } else if (e.hasAttribute("pTRRecord") && !String.valueOf(e.getAttribute("pTRRecord")[0]).startsWith("wbsagnitio")) {
            return true;
        } else if (e.hasAttribute("aRecord") || e.hasAttribute("cNAMERecord")) {
            return true;
        }
        return false;
    }

    public boolean isRemovable(Entry e) throws Exception {
        if (e == null) {
            throw new Exception("invalid domain name entry");
        }
        if (!(new ArrayList<Object>(Arrays.asList(e.getAttribute("objectclass")))).contains("dNSZone")) {
            throw new Exception("invalid domain name entry");
        }
        if (e.hasAttribute("sOARecord")) {
            if (!this.domain.equals(e.getAttribute("zoneName")[0])) {
                StringBuilder _sb = new StringBuilder();
                String[] network = this._nm.getNetwork(this._nm.getInterface(0));
                _sb.append(network[2]);
                _sb.append(".");
                _sb.append(network[1]);
                _sb.append(".");
                _sb.append(network[0]);
                _sb.append(".in-addr.arpa");
                if (!_sb.toString().equals(e.getAttribute("zoneName")[0])) {
                    return true;
                }
            }
        } else if (e.hasAttribute("pTRRecord")) {
            if (!String.valueOf(e.getAttribute("pTRRecord")[0]).startsWith("wbsagnitio")) {
                return true;
            }
        } else if (e.hasAttribute("aRecord")) {
            if (!"wbsagnitio".equals(e.getAttribute("relativeDomainName")[0])) {
                return true;
            }
        } else if (e.hasAttribute("cNAMERecord")) {
            return true;
        }
        return false;
    }

    public String makeForwardDomainComponent(String zoneName) throws Exception {
        String domainComponentString = "";
        ArrayList<String> domainComponents = new ArrayList<String>();
        StringTokenizer _st = new StringTokenizer((String) zoneName, ".");
        while (_st.hasMoreTokens()) {
            domainComponents.add(_st.nextToken());
        }
        while (!domainComponents.isEmpty()) {
            String dc = domainComponents.remove(domainComponents.size() - 1);
            domainComponentString = "dc=" + dc + "," + domainComponentString;
            if (!this._eb.checkEntry(domainComponentString + "ou=forward,ou=dns," + this._c.getProperty("ldap.basedn"))) {
                LDAPEntry _e = new LDAPEntry(domainComponentString + "ou=forward,ou=dns," + this._c.getProperty("ldap.basedn"));
                _e.setAttribute("objectclass", "domain");
                _e.setAttribute("dc", dc);
                this._eb.addEntry(_e);
            }
        }
        return domainComponentString;
    }

    public String makeReverseDomainComponent(String zoneName) throws Exception {
        if (zoneName == null || zoneName.isEmpty()) {
            throw new Exception("invalid reverse zone name");
        }
        StringBuilder _domainComponent = new StringBuilder();
        String[] default_address = NetworkManager.inverseAddress(this._nm.getNetwork(this._nm.getInterface(0)));
        if (zoneName.contains(".in-addr.arpa")) {
            zoneName = zoneName.substring(0, zoneName.indexOf(".in-addr.arpa"));
        }
        String[] address = zoneName.split("\\.");
        System.arraycopy(default_address, 1, default_address, 0, 3);
        default_address = Arrays.copyOf(default_address, 3);
        if (Arrays.equals(default_address, address)) {
            return "";
        }
        List<String> domainComponents = new ArrayList<String>(Arrays.asList(address));
        while (!domainComponents.isEmpty()) {
            String _dc = domainComponents.remove(domainComponents.size() - 1);
            if (_dc == null || _dc.isEmpty()) {
                return "";
            }
            _domainComponent.insert(0, ",");
            _domainComponent.insert(0, _dc);
            _domainComponent.insert(0, "dc=");
            if (!this._eb.checkEntry(_domainComponent.toString() + "ou=reverse,ou=dns," + this._c.getProperty("ldap.basedn"))) {
                LDAPEntry _e = new LDAPEntry(_domainComponent.toString() + "ou=reverse,ou=dns," + this._c.getProperty("ldap.basedn"));
                _e.setAttribute("objectclass", "domain");
                _e.setAttribute("dc", _dc);
                this._eb.addEntry(_e);
            }
        }
        return _domainComponent.toString();
    }

    public void newARecord(String name, String zone, String[] address) throws Exception {
        Entry _e = newRecord(name, zone);
        int[] ipAddress = new int[4];
        try {
            ipAddress[0] = Integer.parseInt(address[0]);
            ipAddress[1] = Integer.parseInt(address[1]);
            ipAddress[2] = Integer.parseInt(address[2]);
            ipAddress[3] = Integer.parseInt(address[3]);
        } catch (NumberFormatException _ex) {
            throw new Exception("invalid address format");
        }
        if (ipAddress[0] < 0 || ipAddress[0] > 254 || ipAddress[1] < 0 || ipAddress[1] > 254 || ipAddress[2] < 0 || ipAddress[2] > 254 || ipAddress[3] < 0 || ipAddress[3] > 254) {
            throw new Exception("invalid address format");
        }
        _e.setAttribute("aRecord", ipAddress[0] + "." + ipAddress[1] + "." + ipAddress[2] + "." + ipAddress[3]);
        this._eb.addEntry(_e);
        if (_e.hasAttribute("aRecord")) {
            if (searchReverseDomainNames(address).isEmpty()) {
                newPTRRecord(address, _e.getAttribute("relativeDomainName")[0] + "." + _e.getAttribute("zoneName")[0]);
            }
        }
    }

    public void newCNAMERecord(String name, String zone, String cname) throws Exception {
        if (!checkHost(cname)) {
            throw new Exception("host [" + cname + "] does not match with any A record");
        }
        Entry _e = newRecord(name, zone);
        if (cname.startsWith(".")) {
            cname = cname.substring(1);
        }
        if (!cname.endsWith(".")) {
            cname += ".";
        }
        char[] url = cname.toCharArray();
        cname = "";
        for (int i = 0; i < url.length; i++) {
            if (url[i] != ' ' && url[i] != '-' && url[i] != '+' && url[i] != '=' && url[i] != '*' && url[i] != '&' && url[i] != '%' && url[i] != '$' && url[i] != '@' && url[i] != '#' && url[i] != ',' && url[i] != '/' && url[i] != '(' && url[i] != ')' && url[i] != ';' && url[i] != ':' && url[i] != '>' && url[i] != '<') {
                cname += url[i];
            }
        }
        _e.setAttribute("cNAMERecord", cname);
        this._eb.addEntry(_e);
    }

    public void newPTRRecord(String[] address, String host) throws Exception {
        String zone = address[2] + "." + address[1] + "." + address[0] + ".in-addr.arpa";
        String domainNameComponent = makeReverseDomainComponent(zone);
        if (!host.endsWith(".")) {
            host += ".";
        }
        try {
            searchDomainNames(host.substring(0, host.indexOf(".")), host.substring(host.indexOf(".") + 1));
        } catch (Exception _ex) {
            throw new Exception("an A record [" + host + "] must be exist in directory");
        }
        Entry _re = new LDAPEntry("relativeDomainName=@," + domainNameComponent + "ou=reverse,ou=dns," + this._c.getProperty("ldap.basedn"));
        if (!this._eb.checkEntry(_re.getID())) {
            _re.setAttribute("objectclass", "dNSZone");
            _re.setAttribute("dNSClass", "IN");
            _re.setAttribute("nSRecord", "wbsagnitio." + this.domain + ".");
            _re.setAttribute("relativeDomainName", "@");
            _re.setAttribute("sOARecord", "wbsagnitio." + this.domain + ". soporte.whitebearsolutions.com. 200812101 3600 1800 604800 86400");
            _re.setAttribute("zoneName", zone);
            this._eb.addEntry(_re);
        }
        _re = new LDAPEntry("relativeDomainName=" + address[3] + "," + domainNameComponent + "ou=reverse,ou=dns," + this._c.getProperty("ldap.basedn"));
        if (this._eb.checkEntry(_re.getID())) {
            throw new Exception("PTR record already exists");
        }
        _re.setAttribute("objectclass", "dNSZone");
        _re.setAttribute("dNSClass", "IN");
        _re.setAttribute("pTRRecord", host);
        _re.setAttribute("relativeDomainName", address[3]);
        _re.setAttribute("zoneName", zone);
        this._eb.addEntry(_re);
    }

    public void newZone(String zoneName) throws Exception {
        if (zoneName.startsWith(".")) {
            zoneName = zoneName.substring(1);
        }
        if (zoneName.endsWith(".")) {
            zoneName = zoneName.substring(0, zoneName.length() - 1);
        }
        char[] req_zoneName = zoneName.toCharArray();
        zoneName = "";
        for (int i = 0; i < req_zoneName.length; i++) {
            if (req_zoneName[i] != ' ' && req_zoneName[i] != '+' && req_zoneName[i] != '=' && req_zoneName[i] != '*' && req_zoneName[i] != '&' && req_zoneName[i] != '%' && req_zoneName[i] != '$' && req_zoneName[i] != '@' && req_zoneName[i] != '#' && req_zoneName[i] != ',' && req_zoneName[i] != '/' && req_zoneName[i] != '(' && req_zoneName[i] != ')' && req_zoneName[i] != ';' && req_zoneName[i] != ':' && req_zoneName[i] != '>' && req_zoneName[i] != '<') {
                zoneName += req_zoneName[i];
            }
        }
        if (zoneName.equals(this.domain)) {
            throw new Exception("zone name already exist");
        }
        Entry _e = new LDAPEntry("relativeDomainName=@," + makeForwardDomainComponent(zoneName) + "ou=forward,ou=dns," + this._c.getProperty("ldap.basedn"));
        _e.setAttribute("objectclass", new String[] { "dNSZone" });
        _e.setAttribute("relativeDomainName", "@");
        _e.setAttribute("zoneName", zoneName);
        _e.setAttribute("nSRecord", "wbsagnitio." + this.domain + ".");
        _e.setAttribute("dNSClass", "IN");
        _e.setAttribute("sOARecord", "wbsagnitio." + this.domain + ". soporte.whitebearsolutions.com. 200812101 3600 1800 604800 86400");
        this._eb.addEntry(_e);
        new DNSConfiguration(this._c).update();
    }

    public void newNSHost(String host, String zone) throws Exception {
        if (!checkHost(host)) {
            throw new Exception("host [" + host + "] does not match with any A record");
        }
        Entry _e = getRecord("@", zone);
        if (_e == null) {
            throw new Exception("zone name [" + zone + "] not found");
        }
        if (!host.endsWith(".")) {
            host += ".";
        }
        ArrayList<Object> _values = new ArrayList<Object>();
        if (_e.hasAttribute("nSRecord")) {
            _values.addAll(Arrays.asList(_e.getAttribute("nSRecord")));
            if (!_values.contains(host)) {
                _values.add(host);
            }
        } else {
            _values.add(host);
        }
        _e.setAttribute("nSRecord", _values.toArray());
        this._eb.updateEntry(_e);
    }

    public void newMXHost(String host, String priority, String zone) throws Exception {
        if (!checkHost(host)) {
            throw new Exception("host [" + host + "] does not match with any A record");
        }
        Entry _e = getRecord("@", zone);
        if (_e == null) {
            throw new Exception("zone name [" + zone + "] not found");
        }
        int ipriority = 10;
        try {
            ipriority = Integer.parseInt(priority);
        } catch (NumberFormatException _ex) {
        }
        if (!host.endsWith(".")) {
            host += ".";
        }
        host = ipriority + " " + host;
        ArrayList<Object> _values = new ArrayList<Object>();
        if (_e.hasAttribute("mXRecord")) {
            _values.addAll(Arrays.asList(_e.getAttribute("mXRecord")));
            if (!_values.contains(host)) {
                _values.add(host);
            }
        } else {
            _values.add(host);
        }
        _e.setAttribute("mXRecord", _values.toArray());
        this._eb.updateEntry(_e);
    }

    public void removeRecord(String name, String zone) throws Exception {
        Entry _e = getRecord(name, zone);
        if (_e == null) {
            throw new Exception("domain name [" + name + "] not found");
        }
        if (_e.hasAttribute("aRecord")) {
            List<Entry> results = searchReverseDomainNames(NetworkManager.toAddress(String.valueOf(_e.getAttribute("aRecord")[0])));
            for (Entry _r : results) {
                this._eb.removeEntry(_r.getID());
            }
        }
        this._eb.removeEntry(_e.getID());
    }

    public void removeRecord(String[] address) throws Exception {
        String zone = address[2] + "." + address[1] + "." + address[0] + ".in-addr.arpa";
        Entry _e = getRecord(address[3], zone);
        if (_e == null) {
            throw new Exception("domain name [" + address[3] + "." + zone + "] not found");
        }
        this._eb.removeEntry(_e.getID());
    }

    public void removeZone(String name) throws Exception {
        Entry _e = getRecord("@", name);
        if (_e == null) {
            throw new Exception("zone name [" + name + "] not found");
        }
        List<Entry> _results = getRecords(name);
        for (Entry _er : _results) {
            this._eb.removeEntry(_er.getID());
        }
        this._eb.removeEntry(_e.getID());
        new DNSConfiguration(_c).update();
    }

    public void removeMXHost(String host, String zone) throws Exception {
        Entry _e = getRecord("@", zone);
        if (_e == null) {
            throw new Exception("zone name [" + zone + "] not found");
        }
        List<Object> _values = new ArrayList<Object>();
        if (_e.hasAttribute("mXRecord")) {
            _values.addAll(Arrays.asList(_e.getAttribute("mXRecord")));
            for (int i = _values.size(); --i >= 0; ) {
                Object o = _values.get(i);
                if (String.valueOf(o).contains(host)) {
                    _values.remove(o);
                }
            }
            if (_values.size() > 0) {
                _e.setAttribute("mXRecord", _values.toArray());
            } else {
                _e.removeAttribute("mXRecord");
            }
            this._eb.updateEntry(_e);
        } else {
            throw new Exception("zone [" + zone + "] does not have MX records");
        }
    }

    public void removeNSHost(String host, String zone) throws Exception {
        Entry _e = getRecord("@", zone);
        if (_e == null) {
            throw new Exception("zone name [" + zone + "] not found");
        }
        List<Object> _values = new ArrayList<Object>();
        if (_e.hasAttribute("nSRecord")) {
            _values.addAll(Arrays.asList(_e.getAttribute("nSRecord")));
            for (int i = _values.size(); --i >= 0; ) {
                Object o = _values.get(i);
                if (String.valueOf(o).contains(host)) {
                    _values.remove(o);
                }
            }
            if (_values.size() > 0) {
                _e.setAttribute("nSRecord", _values.toArray());
            } else {
                _e.removeAttribute("nSRecord");
            }
            _e.setAttribute("nSRecord", _values.toArray());
            this._eb.updateEntry(_e);
        } else {
            throw new Exception("zone [" + zone + "] does not have NS records");
        }
    }

    public List<Entry> searchForwardDomainNames(String name) throws Exception {
        this._eb.setCountLimit(250);
        Query _q = new Query();
        _q.addCondition("objectclass", "dNSZone", Query.EXACT);
        if (name != null) {
            _q.addCondition("relativeDomainName", name, Query.CONTAINS);
        } else {
            _q.addCondition("relativeDomainName", "*", Query.CONTAINS);
        }
        _q.addCondition("zoneName", "in-addr.arpa", Query.NOT_CONTAINS);
        return this._eb.search(_q);
    }

    public List<Entry> searchForwardZones(String zone) throws Exception {
        this._eb.setCountLimit(250);
        Query _q = new Query();
        _q.addCondition("objectclass", "dNSZone", Query.EXACT);
        _q.addCondition("relativeDomainName", "@", Query.EXACT);
        if (zone == null) {
            _q.addCondition("zoneName", "in-addr.arpa", Query.NOT_CONTAINS);
        } else {
            _q.addCondition("zoneName", zone, Query.CONTAINS);
        }
        return this._eb.search(_q);
    }

    public List<Entry> searchReverseZones(String zone) throws Exception {
        this._eb.setCountLimit(250);
        Query _q = new Query();
        _q.addCondition("objectclass", "dNSZone", Query.EXACT);
        _q.addCondition("relativeDomainName", "@", Query.EXACT);
        if (zone == null) {
            _q.addCondition("zoneName", "in-addr.arpa", Query.CONTAINS);
        } else {
            _q.addCondition("zoneName", zone, Query.CONTAINS);
        }
        return this._eb.search(_q);
    }

    public List<Entry> searchDomainNames(String name, String zone) throws Exception {
        this._eb.setCountLimit(250);
        Query _q = new Query();
        _q.addCondition("objectclass", "dNSZone", Query.EXACT);
        if (name != null) {
            _q.addCondition("relativeDomainName", name, Query.CONTAINS);
        } else {
            _q.addCondition("relativeDomainName", "*", Query.CONTAINS);
        }
        _q.addCondition("zoneName", zone, Query.EXACT);
        return this._eb.search(_q);
    }

    public List<Entry> searchReverseDomainNames(String name) throws Exception {
        this._eb.setCountLimit(250);
        Query _q = new Query();
        _q.addCondition("objectclass", "dNSZone", Query.EXACT);
        if (name != null) {
            _q.addCondition("relativeDomainName", name, Query.CONTAINS);
        } else {
            _q.addCondition("relativeDomainName", "*", Query.CONTAINS);
        }
        _q.addCondition("zoneName", ".in-addr.arpa", Query.CONTAINS);
        return this._eb.search(_q);
    }

    public List<Entry> searchReverseDomainNames(String[] address) throws Exception {
        this._eb.setCountLimit(250);
        Query _q = new Query();
        _q.addCondition("objectclass", "dNSZone", Query.EXACT);
        if (address != null) {
            _q.addCondition("relativeDomainName", address[3], Query.EXACT);
            _q.addCondition("zoneName", address[2] + "." + address[1] + "." + address[0] + ".in-addr.arpa", Query.EXACT);
        } else {
            _q.addCondition("relativeDomainName", "*", Query.CONTAINS);
            _q.addCondition("zoneName", ".in-addr.arpa", Query.CONTAINS);
        }
        return this._eb.search(_q);
    }

    public void updateARecord(String name, String zone, String[] address) throws Exception {
        int[] ipAddress = new int[4];
        if (isMainZone(zone) && "wbsagnitio".equals(name)) {
            throw new Exception("you cannot modify [wbsagnitio] record");
        }
        Entry _e = getRecord(name, zone);
        try {
            ipAddress[0] = Integer.parseInt(address[0]);
            ipAddress[1] = Integer.parseInt(address[1]);
            ipAddress[2] = Integer.parseInt(address[2]);
            ipAddress[3] = Integer.parseInt(address[3]);
        } catch (NumberFormatException _ex) {
            throw new Exception("invalid address format");
        }
        if (ipAddress[0] < 0 || ipAddress[0] > 254 || ipAddress[1] < 0 || ipAddress[1] > 254 || ipAddress[2] < 0 || ipAddress[2] > 254 || ipAddress[3] < 0 || ipAddress[3] > 254) {
            throw new Exception("invalid address format");
        }
        if (_e.hasAttribute("aRecord")) {
            String[] _address = NetworkManager.toAddress((String) _e.getAttribute("aRecord")[0]);
            _e.setAttribute("aRecord", ipAddress[0] + "." + ipAddress[1] + "." + ipAddress[2] + "." + ipAddress[3]);
            this._eb.updateEntry(_e);
            for (int i = _address.length; --i >= 0; ) {
                if (_address[i] != address[i]) {
                    break;
                }
                if (i == 0) {
                    return;
                }
            }
            if (searchReverseDomainNames(_address).size() > 0) {
                removeRecord(_address);
            }
            if (searchReverseDomainNames(address).size() <= 0) {
                newPTRRecord(address, name + "." + zone);
            }
        } else {
            _e.setAttribute("aRecord", ipAddress[0] + "." + ipAddress[1] + "." + ipAddress[2] + "." + ipAddress[3]);
            this._eb.updateEntry(_e);
        }
    }

    public void updateCNAMERecord(String name, String zone, String cname) throws Exception {
        if (!checkHost(cname)) {
            throw new Exception("host [" + cname + "] does not match with any A record");
        }
        Entry _e = getRecord(name, zone);
        if (cname.startsWith(".")) {
            cname = cname.substring(1);
        }
        if (!cname.endsWith(".")) {
            cname += ".";
        }
        char[] url = cname.toCharArray();
        cname = "";
        for (int i = 0; i < url.length; i++) {
            if (url[i] != ' ' && url[i] != '-' && url[i] != '+' && url[i] != '=' && url[i] != '*' && url[i] != '&' && url[i] != '%' && url[i] != '$' && url[i] != '@' && url[i] != '#' && url[i] != ',' && url[i] != '/' && url[i] != '(' && url[i] != ')' && url[i] != ';' && url[i] != ':' && url[i] != '>' && url[i] != '<') {
                cname += url[i];
            }
        }
        _e.setAttribute("cNAMERecord", cname);
        this._eb.updateEntry(_e);
    }

    public void updatePTRRecord(String[] address, String host) throws Exception {
        String zone = address[2] + "." + address[1] + "." + address[0] + ".in-addr.arpa";
        String domainNameComponent = makeReverseDomainComponent(zone);
        if (!host.endsWith(".")) {
            host += ".";
        }
        try {
            searchDomainNames(host.substring(0, host.indexOf(".")), host.substring(host.indexOf(".") + 1));
        } catch (Exception _ex) {
            throw new Exception("an A record [" + host + "] must be exist in directory");
        }
        Entry _re = new LDAPEntry("relativeDomainName=" + address[3] + "," + domainNameComponent + "ou=reverse,ou=dns," + this._c.getProperty("ldap.basedn"));
        if (this._eb.getEntry(_re.getID()) == null) {
            throw new Exception("PTR record does not exists");
        }
        _re.setAttribute("objectclass", "dNSZone");
        _re.setAttribute("dNSClass", "IN");
        _re.setAttribute("pTRRecord", host);
        _re.setAttribute("relativeDomainName", address[3]);
        _re.setAttribute("zoneName", zone);
        this._eb.updateEntry(_re);
    }

    public void updateZone(String zoneName, String mail) throws Exception {
        if (zoneName.startsWith(".")) {
            zoneName = zoneName.substring(1);
        }
        if (zoneName.endsWith(".")) {
            zoneName = zoneName.substring(0, zoneName.length() - 1);
        }
        char[] req_zoneName = zoneName.toCharArray();
        zoneName = "";
        for (int i = 0; i < req_zoneName.length; i++) {
            if (req_zoneName[i] != ' ' && req_zoneName[i] != '+' && req_zoneName[i] != '=' && req_zoneName[i] != '*' && req_zoneName[i] != '&' && req_zoneName[i] != '%' && req_zoneName[i] != '$' && req_zoneName[i] != '@' && req_zoneName[i] != '#' && req_zoneName[i] != ',' && req_zoneName[i] != '/' && req_zoneName[i] != '(' && req_zoneName[i] != ')' && req_zoneName[i] != ';' && req_zoneName[i] != ':' && req_zoneName[i] != '>' && req_zoneName[i] != '<') {
                zoneName += req_zoneName[i];
            }
        }
        Entry _e = getDomainZoneEntry(zoneName);
        _e.setAttribute("sOARecord", "wbsagnitio." + this.domain + ". " + mail + ". 200812101 3600 1800 604800 86400");
        this._eb.updateEntry(_e);
    }

    private boolean checkHost(String host) throws Exception {
        if (!isLocalDomain(host.substring(host.indexOf(".") + 1))) {
            return true;
        }
        try {
            Entry _e = getRecord(host.substring(0, host.indexOf(".")), host.substring(host.indexOf(".") + 1));
            if (_e != null && _e.hasAttribute("aRecord")) {
                return true;
            }
        } catch (Exception _ex) {
            throw new Exception("host [" + host + "] not found on directory");
        }
        return false;
    }

    private Entry newRecord(String name, String zone) throws Exception {
        String domain = this._nm.getDomain();
        if (zone == null) {
            zone = domain;
        }
        if (zone.startsWith(".")) {
            zone = zone.substring(1);
        }
        if (zone.endsWith(".")) {
            zone = zone.substring(0, zone.length() - 1);
        }
        Entry _e = null;
        try {
            _e = getDomainNameEntry(name, zone);
            throw new Exception("domain name [" + name + "." + zone + "] already exist");
        } catch (Exception _ex) {
        }
        _e = getDomainZoneEntry(zone);
        if (_e == null) {
            throw new Exception("zone name [" + zone + "] does not exist");
        }
        if (domain.equals(zone)) {
            _e = new LDAPEntry("relativeDomainName=" + name + ",ou=forward,ou=dns," + this._c.getProperty("ldap.basedn"));
        } else {
            _e = new LDAPEntry("relativeDomainName=" + name + _e.getID().substring(_e.getID().indexOf(",")));
        }
        _e.setAttribute("objectclass", new String[] { "dNSZone" });
        _e.setAttribute("relativeDomainName", name);
        _e.setAttribute("zoneName", zone);
        _e.setAttribute("dNSTTL", "1800");
        _e.setAttribute("dNSClass", "IN");
        return _e;
    }
}
