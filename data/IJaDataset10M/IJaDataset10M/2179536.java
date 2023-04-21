package edu.cmu.vlis.wassup.coordinator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class encapsulates Node (physical machine).
 * Contains info like ip-address and stuff.
 */
public class Node {

    private String ipAdd;

    private List<Process> runningProcesses;

    private int port;

    private Socket socket;

    private PrintWriter writer;

    private String pingMsg = "ALIVE?";

    private BufferedReader reader;

    /**
     * @param ipAdd
     */
    public Node(String ipAdd, int port) {
        super();
        this.ipAdd = ipAdd;
        this.runningProcesses = new ArrayList<Process>(4);
        this.port = port;
    }

    /**
     * @return the ipAdd
     */
    public String getIpAdd() {
        return ipAdd;
    }

    /**
     * @return the runningProcesses
     */
    public List<Process> getRunningProcesses() {
        return runningProcesses;
    }

    /**
     * @param runningProcesses the runningProcesses to set
     */
    public void addRunningProcess(Process process) {
        this.addRunningProcess(process);
    }

    public boolean startSocket() {
        try {
            this.socket = new Socket(this.getIpAdd(), port);
            this.writer = new PrintWriter(this.socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            return true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Coordinator cant connect to: " + this.getIpAdd() + ":" + port);
        }
        return false;
    }

    public boolean isAlive() {
        if (this.socket == null || !socket.isBound() || socket.isClosed() || !socket.isConnected() || socket.isInputShutdown() || socket.isOutputShutdown()) return false;
        this.writer.println(pingMsg);
        this.writer.flush();
        try {
            Thread.sleep(1000);
            String response = this.reader.readLine();
            if (response == null) return false;
            return true;
        } catch (IOException e) {
            System.err.println("Connection lost: " + this.ipAdd);
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Exception occured while pinging remote process");
    }

    public boolean startRemoteProcess(String name, int port) {
        this.writer.println(name + ":" + port);
        this.writer.flush();
        try {
            Thread.sleep(5000);
            String response = this.reader.readLine();
            System.out.println(response);
            if (response.equalsIgnoreCase("Yes")) return true;
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Exception occured while pinging remote process");
    }
}
