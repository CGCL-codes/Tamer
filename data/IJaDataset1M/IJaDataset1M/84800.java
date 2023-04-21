package net.assimilator.qos.measurable.cpu;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import net.assimilator.resources.resource.PoolableThread;
import net.assimilator.resources.resource.ResourceUnavailableException;

/**
 * The MpstatOutputParser parses the output of the mpstat command on Solaris
 */
public class MpstatOutputParser extends CPUExecHandler {

    InputStream in;

    double utilization;

    public void parse(InputStream in) {
        this.in = in;
        try {
            PoolableThread thread = (PoolableThread) tPool.get();
            thread.execute(new Runner());
        } catch (ResourceUnavailableException e) {
            logger.log(Level.WARNING, "Parsing output", e);
        }
    }

    public double getUtilization() {
        return (utilization);
    }

    public String getCommand() {
        return ("mpstat");
    }

    class Runner implements Runnable {

        public void run() {
            BufferedReader br = null;
            try {
                InputStreamReader isr = new InputStreamReader(in);
                br = new BufferedReader(isr);
                String line = null;
                int i = 0;
                double currentUtilization = 0.0;
                java.util.List elements = new java.util.ArrayList();
                while ((line = br.readLine()) != null) {
                    if (logger.isLoggable(Level.FINEST)) logger.finest("Parsing outpt from mpstat\n" + "[" + line + "]");
                    if (i > 0) {
                        elements.clear();
                        StringTokenizer st = new StringTokenizer(line, " ");
                        while (st.hasMoreTokens()) elements.add(st.nextToken());
                        double cpuIdle = new Double((String) elements.get(15)).doubleValue();
                        if (logger.isLoggable(Level.FINEST)) logger.finest("CPU idle value=" + cpuIdle);
                        double utilPercent = 1.0 - (cpuIdle / 100);
                        if (utilPercent < 0) {
                            if (logger.isLoggable(Level.FINEST)) logger.finest("CPU utilization=" + utilPercent + ", adjust to 0");
                            utilPercent = 0;
                        }
                        if (logger.isLoggable(Level.FINEST)) logger.finest("CPU utilization percent=" + utilPercent);
                        currentUtilization = (currentUtilization + utilPercent) / i;
                    }
                    i++;
                }
                utilization = currentUtilization;
            } catch (IOException e) {
                logger.log(Level.WARNING, "Parsing stream from mpstat", e);
            } finally {
                try {
                    if (br != null) br.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Closing BufferedReader", e);
                }
            }
        }
    }
}
