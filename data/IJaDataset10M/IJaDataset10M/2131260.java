package edu.rpi.usf.condor;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import edu.rpi.usf.exceptions.CondorMachineException;
import edu.rpi.usf.workers.CondorWorker;

/**
 * A CondorMachine is defined by the result of running condor_status -long.
 */
public class CondorMachine extends Properties {

    private final String condorSubmit = "condor_submit";

    public CondorWorker worker;

    public CondorMachine(String props) {
        try {
            load(new ByteArrayInputStream(props.getBytes()));
            for (Enumeration e = propertyNames(); e.hasMoreElements(); ) {
                String key = (String) e.nextElement();
                String val = getProperty(key);
                Pattern p = Pattern.compile("[\"\']");
                Matcher m = p.matcher(getProperty(key));
                val = m.replaceAll("");
                setProperty(key, val);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error processing machine");
            System.exit(-1);
        }
    }

    public int ncpu() {
        return Integer.parseInt(getProperty("Cpus", "0"));
    }

    public int mem() {
        return Integer.parseInt(getProperty("Memory", "0"));
    }

    public String name() {
        return getProperty("Name", "");
    }

    public String dns() {
        return getProperty("Machine", "");
    }

    public boolean isIdle() {
        if (getProperty("Activity", "").equalsIgnoreCase("idle")) return true;
        return false;
    }

    public boolean hasJava() {
        if (getProperty("HasJava", "").equalsIgnoreCase("true")) return true;
        return false;
    }

    public String arch() {
        return getProperty("Arch", "");
    }

    public String os() {
        return getProperty("OpSys", "");
    }
}
