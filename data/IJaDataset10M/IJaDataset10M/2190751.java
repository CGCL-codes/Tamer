package com.mlib.mail;

import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class MxFinder {

    private static String dnsServer = "8.8.8.8";

    @SuppressWarnings("unchecked")
    public static String[] getMxServers(String domain) throws NamingException {
        Hashtable env = new Hashtable();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        env.put("java.naming.provider.url", String.valueOf(String.valueOf((new StringBuffer("dns://")).append(dnsServer).append("/"))));
        DirContext dirCtx = new InitialDirContext(env);
        Attributes attr = dirCtx.getAttributes(domain, new String[] { "MX" });
        int attriNum = attr.size();
        if (attriNum == 0) return null;
        NamingEnumeration atributos = attr.getAll();
        int idx = 0;
        String servers;
        for (servers = null; atributos.hasMore(); servers = atributos.next().toString()) ;
        StringTokenizer st = new StringTokenizer(servers, ",");
        String ret[] = new String[st.countTokens()];
        while (st.hasMoreTokens()) {
            String tmp = st.nextToken();
            int pos = tmp.indexOf(" ");
            pos = tmp.indexOf(" ", pos + 1);
            ret[idx++] = tmp.substring(pos + 1);
        }
        return ret;
    }

    public static String getFirstMX(String domain) {
        String servers[] = null;
        try {
            servers = getMxServers(domain);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        if (servers == null) {
            return null;
        } else {
            return servers[0];
        }
    }

    public static void main(String args[]) {
        String servers[] = null;
        try {
            servers = getMxServers("163.com");
        } catch (NamingException e) {
            System.out.println(e);
        }
        if (servers == null) {
            System.out.println("no mx record");
        } else {
            for (int i = 0; i < servers.length; i++) System.out.println(servers[i]);
        }
    }
}
