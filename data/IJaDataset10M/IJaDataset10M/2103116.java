package org.highcon.pddpd.plugins.producers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import org.highcon.pddpd.lib.ConfigurationException;
import org.highcon.pddpd.lib.Producer;

/**
 * @author paul
 *
 */
public class UptimeProducer extends Producer {

    private boolean alive = true;

    private int timer = 100;

    public void run() {
        Runtime rtime = Runtime.getRuntime();
        BufferedReader read;
        Process prc;
        String status;
        String temp;
        while (alive) {
            try {
                prc = rtime.exec("uptime");
            } catch (IOException e) {
                messageQ.add(new String("Exception while running uptime: " + e.getLocalizedMessage()));
                continue;
            }
            read = new BufferedReader(new InputStreamReader(prc.getInputStream()));
            status = "";
            try {
                while ((temp = read.readLine()) != null) {
                    status += temp + "\n";
                }
                outQueue.push(status);
            } catch (IOException e1) {
                messageQ.add(new String("Exception while reading uptime output: " + e1.getLocalizedMessage()));
                continue;
            }
            try {
                Thread.sleep(timer);
            } catch (InterruptedException e2) {
                continue;
            }
        }
    }

    public Hashtable getConfig() {
        Hashtable config = new Hashtable();
        config.put("timer", "" + this.timer);
        return config;
    }

    public Hashtable getSchema() {
        Hashtable schema = new Hashtable();
        schema.put("timer", "Milleseconds to wait between polls (int)");
        return schema;
    }

    public Object getValue(String name) {
        if (name.equals("timer")) return new Integer(this.timer);
        return null;
    }

    public void setConfig(Hashtable config) throws ConfigurationException {
        String timerS = (String) config.get("timer");
        if (timerS != null) {
            try {
                int timer = Integer.parseInt(timerS);
                this.timer = timer;
            } catch (NumberFormatException nfe) {
                messageQ.add(timerS + " could not be parsed as an integer");
            }
        }
    }

    public void kill(boolean shutdown) {
        this.alive = false;
        this.myLock.lock();
    }

    public String getConfigFilename() {
        return "UptimeProducer.conf";
    }
}
