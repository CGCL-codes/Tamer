package com.reserveamerica.elastica.test;

import java.rmi.RemoteException;
import java.util.Map;
import com.reserveamerica.commons.ServiceLocatorException;
import com.reserveamerica.commons.ejb.NamingContextProperties;
import com.reserveamerica.elastica.cluster.ClientContext;
import com.reserveamerica.elastica.server.ServerState;
import com.reserveamerica.elastica.test.ejb.TestSessionRemote;
import com.reserveamerica.elastica.test.ejb.TestSessionRemoteCreatorMap;

public class TestCommandLine {

    private void setState(String nodeId, String strState) throws Exception {
        ServerState state = ServerState.valueOf(strState);
        ClientContext.getInstance().setOverrideNodeId(nodeId);
        try {
            ServerState previousState = getRemote().setServerState(state);
            System.out.println("Changed state of node [" + nodeId + "] from [" + previousState + "] to [" + state + "]");
        } finally {
            ClientContext.getInstance().resetOverrideNodeId();
        }
    }

    private void setDelay(String nodeId, String method, String strDelay) throws Exception {
        int maxDelayMs = getInt(strDelay);
        ClientContext.getInstance().setOverrideNodeId(nodeId);
        try {
            int previousDelayMs = getRemote().setDelay(method, maxDelayMs);
            System.out.println("Changed delay of node [" + nodeId + "] for method " + method + " from " + previousDelayMs + " ms to " + maxDelayMs + " ms.");
        } finally {
            ClientContext.getInstance().resetOverrideNodeId();
        }
    }

    private void getDelay(String nodeId, String method) throws Exception {
        ClientContext.getInstance().setOverrideNodeId(nodeId);
        try {
            int maxDelayMs = getRemote().getDelay(method);
            System.out.println("Delay of node [" + nodeId + "] for method " + method + " is " + maxDelayMs + " ms");
        } finally {
            ClientContext.getInstance().resetOverrideNodeId();
        }
    }

    private void getDelays(String nodeId) throws Exception {
        ClientContext.getInstance().setOverrideNodeId(nodeId);
        try {
            System.out.println("Delays for node [" + nodeId + "]:");
            for (Map.Entry<String, Integer> entry : getRemote().getDelays().entrySet()) {
                System.out.println(" - " + entry.getKey() + " = " + entry.getValue() + " ms");
            }
        } finally {
            ClientContext.getInstance().resetOverrideNodeId();
        }
    }

    private void setDefaultDelay(String nodeId, String strDelay) throws Exception {
        int maxDelayMs = getInt(strDelay);
        ClientContext.getInstance().setOverrideNodeId(nodeId);
        try {
            int previousDelayMs = getRemote().setDefaultDelay(maxDelayMs);
            System.out.println("Changed default delay of node [" + nodeId + "] from " + previousDelayMs + " ms to " + maxDelayMs + " ms.");
        } finally {
            ClientContext.getInstance().resetOverrideNodeId();
        }
    }

    private void getDefaultDelay(String nodeId) throws Exception {
        ClientContext.getInstance().setOverrideNodeId(nodeId);
        try {
            int maxDelayMs = getRemote().getDefaultDelay();
            System.out.println("Default delay of node [" + nodeId + "] is " + maxDelayMs + " ms");
        } finally {
            ClientContext.getInstance().resetOverrideNodeId();
        }
    }

    private void clearDelays(String nodeId) throws Exception {
        ClientContext.getInstance().setOverrideNodeId(nodeId);
        try {
            getRemote().clearDelays();
            System.out.println("Cleared method delays for node [" + nodeId + "].");
        } finally {
            ClientContext.getInstance().resetOverrideNodeId();
        }
    }

    private void setSpinCount(String nodeId, String strSpinCount) throws Exception {
        int spinCount = getInt(strSpinCount);
        ClientContext.getInstance().setOverrideNodeId(nodeId);
        try {
            long previousSpinCount = getRemote().setSpinCount(spinCount);
            System.out.println("Changed spin count of node [" + nodeId + "] from " + previousSpinCount + " to " + spinCount + ".");
        } finally {
            ClientContext.getInstance().resetOverrideNodeId();
        }
    }

    private void getSpinCount(String nodeId) throws Exception {
        ClientContext.getInstance().setOverrideNodeId(nodeId);
        try {
            long spinCount = getRemote().getSpinCount();
            System.out.println("Spin count of node [" + nodeId + "] is " + spinCount + ".");
        } finally {
            ClientContext.getInstance().resetOverrideNodeId();
        }
    }

    private TestSessionRemote getRemote() throws RemoteException, ServiceLocatorException {
        return TestSessionRemoteCreatorMap.getInstance().get(NamingContextProperties.getDefaultCluster(), "ejb/AdministrationRemote");
    }

    private int getInt(String strInt) throws CommandLineException {
        try {
            return Integer.parseInt(strInt);
        } catch (NumberFormatException ex) {
            throw new CommandLineException("Invalid numeric value [" + strInt + "]");
        }
    }

    /**
   * @param args
   */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Commands:");
            System.out.println("    state");
            System.out.println("    delay");
            System.out.println("    mdelay");
            return;
        }
        TestCommandLine instance = new TestCommandLine();
        try {
            String command = args[0];
            if (command.equals("state")) {
                if (args.length != 3) {
                    System.out.println("Args: <node-id> <offline | standby | online>");
                } else {
                    instance.setState(args[1], args[2]);
                }
            } else if (command.equals("clear")) {
                if (args.length == 2) {
                    instance.clearDelays(args[1]);
                } else {
                    System.out.println("Args: <node-id>");
                }
            } else if (command.equals("delay")) {
                if (args.length == 2) {
                    instance.getDefaultDelay(args[1]);
                } else if (args.length == 3) {
                    instance.setDefaultDelay(args[1], args[2]);
                } else {
                    System.out.println("Args: <node-id> <delay-ms>");
                }
            } else if (command.equals("mdelay")) {
                if (args.length == 2) {
                    instance.getDelays(args[1]);
                } else if (args.length == 3) {
                    instance.getDelay(args[1], args[2]);
                } else if (args.length == 4) {
                    instance.setDelay(args[1], args[2], args[3]);
                } else {
                    System.out.println("Args: <node-id> <method> <delay-ms>");
                }
            } else if (command.equals("spin")) {
                if (args.length == 2) {
                    instance.getSpinCount(args[1]);
                } else if (args.length == 3) {
                    instance.setSpinCount(args[1], args[2]);
                } else {
                    System.out.println("Args: <node-id> <delay-ms>");
                }
            } else {
                System.out.println("Commands:");
                System.out.println("    state");
                System.out.println("    delay");
                System.out.println("    spin");
            }
        } catch (CommandLineException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
