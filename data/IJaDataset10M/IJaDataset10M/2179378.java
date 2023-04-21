package hu.sztaki.lpds.pgportal.services.is.mds2;

import hu.sztaki.lpds.pgportal.services.is.*;
import hu.sztaki.lpds.pgportal.services.is.mds2.ldap.QueryMDSResourceList;
import hu.sztaki.lpds.pgportal.services.pgrade.GridConfiguration;
import javax.naming.*;
import javax.naming.directory.*;
import java.util.Hashtable;

/**
  *
  * @author  boci
  */
public class MDSInformationSystemTester {

    private String ldapConnectionTimeout;

    private String ldapGIISHost;

    private String ldapGIISPort;

    private String ldapBaseDn;

    /** Creates a new instance of MDSInformationSystemTester */
    public MDSInformationSystemTester() {
        this.ldapConnectionTimeout = "20000";
        this.ldapGIISHost = "n0.hpcc.sztaki.hu";
        this.ldapGIISPort = "2135";
        this.ldapBaseDn = "MDS-Vo-name=SzuperGRID,o=grid";
        this.test09();
    }

    /**
      * @param args the command line arguments
      */
    public static void main(String[] args) {
        MDSInformationSystemTester mdsIST = new MDSInformationSystemTester();
    }

    private void test04() {
        this.getMDSAttributes();
    }

    private void test05() {
        MDSResource res = new MDSResource();
        res.setHost("n0.hpcc.sztaki.hu");
        MDSJobManager[] mdsJMs = new MDSJobManager[] { new MDSJobManager("fork"), new MDSJobManager("sge"), new MDSJobManager("condor") };
        res.addJobManagers(mdsJMs);
        MDSJobManager[] mdsJMs2 = res.getJobmanagers();
        String contactString = res.getHost();
        System.out.println(" mdsJM.length:" + mdsJMs2.length);
        for (int j = 0; j < mdsJMs2.length; j++) {
            System.out.println(contactString + ":/" + mdsJMs2[j].getName());
        }
    }

    private void test06() {
        boolean b = Boolean.valueOf("false").booleanValue();
        System.out.println("b:" + b);
    }

    private void test07() {
    }

    private void test08() {
        Hashtable ht = new Hashtable();
        ht.put("aa", new Integer(1));
        System.out.println("ekieki:" + ht.get(""));
    }

    private void test09() {
        GridConfiguration gc = new GridConfiguration("ekieki", null);
    }

    private void test10() {
    }

    protected MDSResource[] getMDSResources() {
        MDSResource[] resources;
        try {
            resources = processMDSResourceListResult(getMDSResourceList(this.ldapGIISHost, this.ldapGIISPort, this.ldapBaseDn, "(objectclass=MdsHost)"));
            return resources;
        } catch (javax.naming.CommunicationException e) {
            System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.getResources()-CommunicationException was occured." + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (javax.naming.NamingException e) {
            System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.getResources()-NamingException was occured: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private NamingEnumeration getMDSResourceList(String host, String port, String baseDN, String filter) throws NamingException {
        java.util.Hashtable env = new java.util.Hashtable();
        env.put(Context.PROVIDER_URL, "ldap://" + host + ":" + port);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put("com.sun.jndi.ldap.connect.timeout", this.ldapConnectionTimeout);
        System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.getMDSResourceList()-InitialDirContext() calling.");
        DirContext ctx = new InitialDirContext(env);
        String[] retAttr = new String[] { "Mds-Host-hn" };
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(retAttr);
        System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.getMDSResourceList()-search() starting.[" + System.currentTimeMillis() + "]");
        Attributes matchAttrs = new BasicAttributes(true);
        matchAttrs.put(new BasicAttribute("Mds-Host-hn"));
        NamingEnumeration results = ctx.search(baseDN, matchAttrs, retAttr);
        System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.getMDSResourceList()-search() finished.[" + System.currentTimeMillis() + "]");
        ctx.close();
        System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.getMDSResourceList()-Connection was closed.");
        return results;
    }

    private MDSResource[] processMDSResourceListResult(NamingEnumeration results) throws javax.naming.NamingException {
        java.util.Vector resources = new java.util.Vector();
        MDSResource r;
        SearchResult sr;
        String host;
        System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.processMDSResourceListResult()-Start.[" + System.currentTimeMillis() + "]");
        while (results.hasMoreElements()) {
            System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.processMDSResourceListResult()-results.next() start");
            sr = (SearchResult) results.next();
            r = new MDSResource();
            System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.processMDSResourceListResult()-host start");
            host = sr.getAttributes().get("Mds-Host-hn").get().toString();
            Attribute a = sr.getAttributes().get("Mds-Software-deployment");
            if (a != null) System.out.println("Mds-Software-deployment" + a.get().toString());
            r.setHost(host);
            System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.processMDSResourceListResult()-host=" + host);
            resources.add(r);
        }
        System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.processMDSResourceListResult()-finished.[" + System.currentTimeMillis() + "]");
        MDSResource[] mdsR = new MDSResource[resources.size()];
        for (int i = 0; i < resources.size(); i++) {
            mdsR[i] = (MDSResource) resources.get(i);
        }
        return mdsR;
    }

    protected MDSResourceDetails getMDSResourceDetails(String host) {
        MDSResourceDetails mdsRD;
        try {
            String baseDN = new String("Mds-Host-hn=" + host + "," + this.ldapBaseDn);
            mdsRD = processMDSResourceDetails(getMDSResourceDetails(this.ldapGIISHost, this.ldapGIISPort, baseDN, ""));
            mdsRD.setHost(host);
            return mdsRD;
        } catch (javax.naming.CommunicationException e) {
            System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.getMDSResourceDetails()-CommunicationException was occured.");
            e.printStackTrace();
            return null;
        } catch (javax.naming.NamingException e) {
            System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.getMDSResourceDetails()-NamingException was occured: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private Attributes getMDSResourceDetails(String host, String port, String baseDN, String filter) throws NamingException {
        java.util.Hashtable env = new java.util.Hashtable();
        env.put(Context.PROVIDER_URL, "ldap://" + host + ":" + port);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put("com.sun.jndi.ldap.connect.timeout", this.ldapConnectionTimeout);
        String[] attrIDs = { "Mds-Os-name", "Mds-Os-release", "Mds-Cpu-Total-count", "Mds-Cpu-model", "Mds-Cpu-speedMHz", "Mds-Cpu-Total-Free-1minX100", "Mds-Cpu-Total-Free-5minX100", "Mds-Cpu-Total-Free-15minX100", "Mds-Memory-Ram-Total-sizeMB", "Mds-Memory-Vm-Total-sizeMB", "Mds-Fs-Total-sizeMB", "Mds-Memory-Ram-freeMB", "Mds-Memory-Vm-freeMB", "Mds-Fs-Total-freeMB", "Mds-Host-hn", "Mds-Software-deployment" };
        System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.getMDSResourceDetails()-InitialDirContext() calling.");
        DirContext ctx = new InitialDirContext(env);
        System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.getMDSResourceDetails()-search() starting.[" + System.currentTimeMillis() + "]");
        Attributes answer = ctx.getAttributes(baseDN, attrIDs);
        System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.getMDSResourceDetails()-search() finished.[" + System.currentTimeMillis() + "]");
        ctx.close();
        System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.getMDSResourceDetails()-Connection was closed.");
        return answer;
    }

    private MDSResourceDetails processMDSResourceDetails(Attributes answer) throws javax.naming.NamingException {
        MDSResourceDetails mdsRD = new MDSResourceDetails();
        SearchResult sr;
        String host;
        System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.processMDSResourceDetails()-printing attributes:[" + System.currentTimeMillis() + "]");
        Attribute attr;
        NamingEnumeration ae;
        NamingEnumeration e;
        String attrValue = "";
        String attrName = "";
        for (ae = answer.getAll(); ae.hasMore(); ) {
            attr = (Attribute) ae.next();
            attrName = attr.getID();
            System.out.print(">>>>>attribute: " + attr.getID() + " - ");
            for (e = attr.getAll(); e.hasMore(); attrValue = (String) e.next()) {
            }
            System.out.println("value: " + attrValue);
            mdsRD.put(attrName, attrValue);
        }
        System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.processMDSResourceDetails()-finished.[" + System.currentTimeMillis() + "]");
        return mdsRD;
    }

    protected void getMDSAttributes() {
        try {
            processMDSAttribute(getMDSAttribute());
        } catch (javax.naming.CommunicationException e) {
            System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.getResources()-CommunicationException was occured." + e.getMessage());
            e.printStackTrace();
        } catch (javax.naming.NamingException e) {
            System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.getResources()-NamingException was occured: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Attributes getMDSAttribute() throws NamingException {
        java.util.Hashtable env = new java.util.Hashtable();
        env.put(Context.PROVIDER_URL, "ldap://" + this.ldapGIISHost + ":" + this.ldapGIISPort);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put("com.sun.jndi.ldap.connect.timeout", this.ldapConnectionTimeout);
        System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.getMDSResourceList()-InitialDirContext() calling.");
        DirContext ctx = new InitialDirContext(env);
        String host = "n0.ikpc.iit.bme.hu";
        String condorBaseDN = new String("Mds-Software-deployment=jobmanager-condor,Mds-Host-hn=" + host + "," + this.ldapBaseDn);
        String forkBaseDN = new String("Mds-Software-deployment=jobmanager-fork,Mds-Host-hn=" + host + "," + this.ldapBaseDn);
        String sgeBaseDN = new String("Mds-Software-deployment=jobmanager-sge,Mds-Host-hn=" + host + "," + this.ldapBaseDn);
        String[] attrIDs = new String[] { "Mds-Software-deployment" };
        System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.getMDSResourceList()-search() starting.[" + System.currentTimeMillis() + "]");
        Attributes answer = ctx.getAttributes(condorBaseDN, attrIDs);
        System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.getMDSResourceList()-search() finished.[" + System.currentTimeMillis() + "]");
        ctx.close();
        System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.getMDSResourceList()-Connection was closed.");
        return answer;
    }

    private void processMDSAttribute(Attributes answer) throws javax.naming.NamingException {
        MDSResourceDetails mdsRD = new MDSResourceDetails();
        SearchResult sr;
        String host;
        System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.processMDSResourceDetails()-printing attributes:[" + System.currentTimeMillis() + "]");
        Attribute attr;
        NamingEnumeration ae;
        NamingEnumeration e;
        String attrValue = "";
        String attrName = "";
        for (ae = answer.getAll(); ae.hasMore(); ) {
            attr = (Attribute) ae.next();
            attrName = attr.getID();
            System.out.print(">>>>>attribute: " + attr.getID() + " - ");
            for (e = attr.getAll(); e.hasMore(); attrValue = (String) e.next()) {
            }
            System.out.println("value: " + attrValue);
        }
        System.out.println("hu.sztaki.lpds.pgportal.services.is.mds2.processMDSResourceDetails()-finished.[" + System.currentTimeMillis() + "]");
    }
}
