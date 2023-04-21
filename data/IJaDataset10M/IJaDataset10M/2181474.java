package edu.rpi.usf.condor;

import java.io.*;
import java.util.*;
import edu.rpi.usf.utils.Config;
import edu.rpi.usf.utils.Parameters;
import edu.rpi.usf.condor.CondorMachine;
import edu.rpi.usf.workers.CondorWorker;
import edu.rpi.usf.exceptions.CondorMachineException;

public class CondorMaster extends Properties {

    public static final boolean singleWorker = false;

    private static CondorMaster instance = null;

    private final String condorStatus = "condor_status -l";

    private Vector workerList = new Vector();

    private Hashtable machines = new Hashtable();

    private final String hostname;

    private int nIdle;

    private CondorMaster(String h) {
        hostname = h;
        instance = this;
        updateStatus();
        try {
            System.err.println("CLASSAD File: " + Config.getNetworkProperty("CLASSAD", "condor.submit"));
            load(new FileInputStream(new File(Config.getNetworkProperty("CLASSAD"))));
            String key, val;
            for (Enumeration e = propertyNames(); e.hasMoreElements(); ) {
                key = (String) e.nextElement();
                val = getProperty(key);
                remove(key);
                setProperty(key.toLowerCase(), val);
            }
            if (null == getProperty("output")) setProperty("output", "standard_out");
            if (null == getProperty("error")) setProperty("error", "standard_err");
        } catch (IOException ioe) {
            System.err.println("Unable to load ClassAd: " + Config.getNetworkProperty("CLASSAD"));
            System.exit(-1);
        }
    }

    public static CondorMaster getInstance() {
        if (instance == null) instance = new CondorMaster(Config.getNetworkProperty("MASTER", "drone1.mitre.org"));
        return instance;
    }

    public CondorMachine getMachine(String id) {
        return (CondorMachine) machines.get(id);
    }

    /**
	 * Call condor_status and configure class member items appropriately
	 * @returns: the number of Condor client CPUs found
	 */
    public int updateStatus() {
        Runtime os = Runtime.getRuntime();
        CondorMachine m;
        int cnt = 0;
        try {
            Process proc = os.exec(condorStatus);
            BufferedReader rdr = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            while (true) {
                String props = "";
                String line;
                if (singleWorker && workerList.size() > 0) break;
                while (null != (line = rdr.readLine())) {
                    if (line.equals("")) break;
                    props += line + "\n";
                }
                if (props.equals("")) break;
                m = new CondorMachine(props);
                m.worker = new CondorWorker(m.name(), cnt++, m);
                if (null != machines.get(m.worker.getUniqueId())) {
                    cnt--;
                    continue;
                }
                workerList.add(m.worker);
                machines.put(m.worker.getUniqueId(), m);
            }
            System.err.println("Worker Count: " + workerList.size());
            rdr.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to retrieve Condor status!");
            System.exit(-1);
        }
        return workerList.size();
    }

    public String getHostname() {
        return hostname;
    }

    public Vector getWorkerList() {
        return workerList;
    }

    public int getWorkerCount() {
        return workerList.size();
    }
}
