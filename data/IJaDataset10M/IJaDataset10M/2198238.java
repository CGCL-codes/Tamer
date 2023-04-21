package com.i3sp.tech.rmi;

import org.mortbay.util.Code;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class Client extends java.lang.Thread {

    private Foo foo;

    private int id;

    private String url;

    public Client(String url, int id, int server) {
        this.id = id;
        this.url = url + server;
        Code.debug("Client " + id + " Connecting to ", this.url);
        try {
            foo = (Foo) Naming.lookup(this.url);
        } catch (java.rmi.ConnectException ex) {
            Code.fail("Name server not running?", ex);
        } catch (java.rmi.NotBoundException ex) {
            Code.fail("Server not running?", ex);
        } catch (java.net.MalformedURLException ex) {
            Code.fail("Malformed server address:" + this.url, ex);
        } catch (RemoteException ex) {
            Code.fail("WHEN????", ex);
        }
    }

    public void run() {
        for (int j = 0; j < 100; j++) {
            try {
                Code.debug("Client." + id + " Doing flob(" + j + ", " + id + ")");
                int retv = foo.flob(j, id);
                Code.debug("Client." + id + " Sent " + j + " Got: " + retv);
            } catch (Exception ex) {
                Code.debug("Client." + id, " Caught exception while invoking flob: ", ex);
            }
        }
    }

    static boolean verbose = false;

    public static void usage() {
        Code.fail("Usage: java ...Client" + " [-v] [-url url] [-s numservers] [-c clients]");
    }

    public static void main(String argv[]) throws Exception {
        int servers = 1;
        int clients = 1;
        String globurl = "server";
        for (int i = 0; i < argv.length; i++) {
            if (argv[i].equals("-url")) if (i + 1 < argv.length) {
                globurl = argv[i + 1];
                i++;
                continue;
            } else usage();
            if (argv[i].equals("-s")) if (i + 1 < argv.length) {
                servers = Integer.valueOf(argv[i + 1]).intValue();
                i++;
                continue;
            } else usage();
            if (argv[i].equals("-c")) if (i + 1 < argv.length) {
                clients = Integer.valueOf(argv[i + 1]).intValue();
                i++;
                continue;
            } else usage();
            if (argv[i].equals("-v")) {
                verbose = true;
                continue;
            }
            usage();
        }
        for (int i = 0; i < clients; i++) {
            int j = i % servers;
            Client cl = new Client(globurl, i, j);
            cl.start();
        }
    }
}
