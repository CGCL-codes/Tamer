package org.tigr.htc.server;

import junit.framework.*;
import java.io.*;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.tigr.htc.cmd.*;

public class MWServerTest extends TestCase {

    static Logger log = Logger.getLogger(MWServerTest.class);

    Command lsCom = new Command();

    MWRunner hr = null;

    public MWServerTest(String p_name) {
        super(p_name);
    }

    public void setUp() {
        PropertyConfigurator.configure("conf/log4j.configuration");
        File test = new File("working");
        log.debug("Create command ");
        lsCom.setType("mw");
        lsCom.setUserName("sgetest");
        lsCom.setCommand("/home/sgetest/tmp/test.sh");
        lsCom.setInitialDir("/home/sgetest/tmp");
        lsCom.setOutput("test.$(Index).out");
        lsCom.setError("test.err");
        lsCom.setInput("test.input");
        lsCom.addParam("-- -l -a");
        lsCom.addParam("wow");
        lsCom.addParam("a", "apple$(Index)");
        lsCom.addParam("c", "cat", ParamType.FILE);
        lsCom.getConfig().setPriority(1);
        log.debug("Persist command ");
        lsCom.createCommand();
        hr = new MWRunner(lsCom, test);
    }

    public void testGetCommand() {
        log.debug("getcommand call");
        MWServer mwserver = MWServer.getInstance();
        String i = Long.toString(lsCom.getID());
        Hashtable hash = mwserver.getCommandHash(i);
        String output = (String) hash.get("output");
        String input = (String) hash.get("input");
        String command = (String) hash.get("command");
        Vector p = (Vector) hash.get("params");
        log.debug("run command " + command + " " + output + " " + input + "\n");
        log.debug(" print parameters ");
        Iterator params = p.iterator();
        int count = 0;
        for (; params.hasNext(); ) {
            String param = (String) params.next();
            log.debug(" param " + count + " " + param);
            count++;
        }
        int ind;
        ind = mwserver.getNextIndex(i, 0, "10.0.0.0");
        log.debug(" index was " + ind);
        mwserver.setTaskFinished(i, ind, 0, "/bin/test.sh blah blah");
        ind = mwserver.getNextIndex(i, 0, "10.0.0.0");
        log.debug(" index was " + ind);
        ind = mwserver.getNextIndex(i, 0, "10.0.0.0");
        log.debug(" index was " + ind);
    }

    public static Test suite() {
        return new TestSuite(MWServerTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
