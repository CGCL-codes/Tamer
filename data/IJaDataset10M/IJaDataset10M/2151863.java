package com.io_software.utils.web;

import com.abb.util.RequestManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.InetAddress;
import java.util.Hashtable;
import java.util.Enumeration;

/** Creates a number of randomly sampled IP addresses and probes them
    for the presence of a server replying to an HTTP request on port 80.<p>
    
    The requests are {@link URLProbeRequest} objects that get pooled and
    executed by an {@link com.abb.util.RequestManager}.
    
    @author Axel Uhl
    @version $Id: Port80Prober.java,v 1.2 2001/02/03 15:24:11 aul Exp $
  */
public class Port80Prober {

    /** Parameter <tt>args[0]</tt> is supposed to hold the number of random
	IP addresses to generate and probe.
      */
    public static void main(String[] args) {
        if (args.length != 1) usage(); else {
            int numberOfThreads = defaultNumberOfThreads;
            Hashtable probingResults = new Hashtable();
            IPRandomGenerator rg = new IPRandomGenerator();
            if (args.length > 1) numberOfThreads = new Integer(args[1]).intValue();
            int numberOfSamples = new Integer(args[0]).intValue();
            RequestManager rm = new RequestManager(numberOfThreads);
            for (int i = 0; i < numberOfSamples; i++) {
                InetAddress ia = rg.nextIP();
                try {
                    URL url = new URL("http://" + ia.getHostAddress());
                    URLProbeRequest request = new URLProbeRequest(0, url, probingResults);
                    rm.addRequest(request);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            rm.waitForLastRequestToFinish();
            printResults(probingResults);
        }
    }

    /** prints the probing results to the passed table to stdout
    
	@param results The keys are InetAddress objects, the values are
		{@link URLProbeRequest.ProbeResult} objects.
      */
    private static void printResults(Hashtable probingResults) {
        for (Enumeration e = probingResults.elements(); e.hasMoreElements(); ) {
            URLProbeRequest.ProbeResult pr = (URLProbeRequest.ProbeResult) e.nextElement();
            System.out.println(pr);
        }
    }

    /** displays a usage message to stderr */
    private static void usage() {
        System.err.println("Usage: java Port80Prober <number-of-samples> [<number-of-threads>]");
        System.err.println("<number-of-threads> defaults to " + defaultNumberOfThreads);
    }

    /** default number of request runner threads */
    private static int defaultNumberOfThreads = 500;
}
