package org.apache.xmlrpc;

import java.util.*;
import java.io.IOException;

public class Benchmark implements Runnable {

    XmlRpcClient client;

    static String url;

    static int clients = 16;

    static int loops = 100;

    long start;

    int gCalls = 0, gErrors = 0;

    Date date;

    public Benchmark() throws Exception {
        client = new XmlRpcClientLite(url);
        Vector args = new Vector();
        args.addElement(new Integer(123));
        client.execute("math.abs", args);
        date = new Date();
        date = new Date((date.getTime() / 1000) * 1000);
        start = System.currentTimeMillis();
        int nclients = clients;
        for (int i = 0; i < nclients; i++) new Thread(this).start();
    }

    public void run() {
        int errors = 0;
        int calls = 0;
        try {
            int val = (int) (-100 * Math.random());
            Vector args = new Vector();
            args.addElement(new Integer(val));
            for (int i = 0; i < loops; i++) {
                Integer ret = (Integer) client.execute("math.abs", args);
                if (ret.intValue() != Math.abs(val)) {
                    errors += 1;
                }
                calls += 1;
            }
        } catch (IOException x) {
            System.err.println("Exception in client: " + x);
            x.printStackTrace();
        } catch (XmlRpcException x) {
            System.err.println("Server reported error: " + x);
        } catch (Exception other) {
            System.err.println("Exception in Benchmark client: " + other);
        }
        int millis = (int) (System.currentTimeMillis() - start);
        checkout(calls, errors, millis);
    }

    private synchronized void checkout(int calls, int errors, int millis) {
        clients--;
        gCalls += calls;
        gErrors += errors;
        System.err.println("Benchmark thread finished: " + calls + " calls, " + errors + " errors in " + millis + " milliseconds.");
        if (clients == 0) {
            System.err.println("");
            System.err.println("Benchmark result: " + (1000 * gCalls / millis) + " calls per second.");
        }
    }

    public static void main(String args[]) throws Exception {
        if (args.length > 0 && args.length < 3) {
            url = args[0];
            XmlRpc.setKeepAlive(true);
            if (args.length == 2) XmlRpc.setDriver(args[1]);
            new Benchmark();
        } else {
            System.err.println("Usage: java org.apache.xmlrpc.Benchmark URL [SAXDriver]");
        }
    }
}
