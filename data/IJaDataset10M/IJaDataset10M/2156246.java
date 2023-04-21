package com.topq.monitor;

import com.aqua.sysobj.conn.CliCommand;
import com.aqua.sysobj.conn.CmdConnection;

public class Wmic extends CmdConnection {

    public static enum WmicAliasses {

        process
    }

    public static enum WmicOperations {

        get, call
    }

    @Override
    public void init() throws Exception {
        super.init();
        setCloneOnEveryOperation(true);
    }

    public boolean isProcessRunning(String process) throws Exception {
        return !handleGetProcessCommand(process, "Description").contains("No Instance(s) Available.");
    }

    public void killProcess(String process) throws Exception {
        handleCallProcessCommand(process, "terminate");
    }

    public String handleGetProcessCommand(String process, String fields) throws Exception {
        return handleProcessCommand(process, WmicOperations.get, fields);
    }

    public String handleCallProcessCommand(String process, String function) throws Exception {
        return handleProcessCommand(process, WmicOperations.call, function);
    }

    public String handleProcessCommand(String process, WmicOperations operation, String fields) throws Exception {
        return handleWmiCommand(WmicAliasses.process, process, operation, fields);
    }

    public String handleWmiCommand(WmicAliasses alias, String object, WmicOperations operation, String fields) throws Exception {
        String node = "";
        String user = "";
        String password = "";
        if (!getHost().equals("localhost") && !getHost().equals("127.0.0.1")) {
            node = "/node:\'" + getHost() + "\'";
            user = "/user:\'" + getUser() + "\'";
            password = "/password:\'" + getPassword() + "\'";
        }
        String whereName = "where name=\'" + object + "\'";
        CliCommand cmd = new CliCommand("wmic " + node + " " + user + " " + password + " " + alias.name() + " " + whereName + " " + operation + " " + fields + " /format:csv");
        cmd.setSilent(true);
        cmd.addErrors("ERROR:");
        cmd.addErrors("Invalid");
        handleCliCommand("", cmd);
        return cmd.getResult();
    }
}
