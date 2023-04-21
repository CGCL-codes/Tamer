package simsim;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 *
 * Description:
 * <p>
 * This class represents simulation server that communicates between nodes.
 *
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public class SimulatorServer {

    private static final ArrayList<Socket> clientSocketVector = new ArrayList<Socket>(500);

    private static final int BROAD_CAST_ADDRESS = 65535;

    /**
     * Key is the node Id to where packets are to be send and
     * the corresponding values is set of data packets.
     */
    private static final HashMap<Short, ArrayList<byte[]>> receivedPackets = new HashMap<Short, ArrayList<byte[]>>();

    private static short currentClientId = 1;

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("\nUsage: java SimulatorServer <name-of-topology-file>\n\n");
            System.err.println("\npress any key to use default topology");
            System.in.read();
            new TopologyManager().readTopologyFromFile("topo.txt");
        } else {
            new TopologyManager().readTopologyFromFile(args[0].trim());
        }
        new SimulatorServer().start();
    }

    private void start() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(Node.SERVER_PORT_NUMBER);
        } catch (IOException e) {
            System.err.println("Could not listen on port :" + Node.SERVER_PORT_NUMBER);
            System.exit(1);
        }
        new CommunicateWithClients().start();
        while (true) {
            try {
                clientSocketVector.add(serverSocket.accept());
                Debug.println("Accepted ..... ", clientSocketVector.size(), false);
                Debug.println("data available for nodes ", receivedPackets.keySet());
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        }
    }

    private void savePacketForDestinations(byte[] data, int destination) {
        System.out.println("\n**** inside savePacketForDestinations ******** ");
        ArrayList<NeighborAndProb> neighbors = TopologyManager.getDestProb(currentClientId);
        Random random = new Random(System.currentTimeMillis());
        for (int loop = 0; loop < neighbors.size(); loop++) {
            NeighborAndProb neighbor = neighbors.get(loop);
            short neighborID = neighbor.nodeId;
            double probabilityInput = neighbor.probability;
            System.out.println("neigbhour size " + neighbors.size() + " loop =" + loop);
            ArrayList<byte[]> dataPackets = receivedPackets.get(neighborID);
            if (dataPackets == null) {
                dataPackets = new ArrayList<byte[]>();
                receivedPackets.put(neighborID, dataPackets);
            }
            double probToCompare = random.nextDouble();
            Debug.println(probToCompare, " Vs " + probabilityInput, true);
            if (probToCompare <= probabilityInput && (destination == BROAD_CAST_ADDRESS || destination == neighborID)) {
                Debug.println("saving data to send ", true);
                dataPackets.add(data);
            }
        }
    }

    private class CommunicateWithClients extends Thread {

        private boolean input(Socket clientSocket) {
            boolean shouldSendDataPacket = false;
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine = in.readLine();
                Debug.println("******** 1st packet ", inputLine);
                if (inputLine != null) {
                    currentClientId = Short.parseShort(inputLine);
                }
                Debug.println("***** serving node = ", currentClientId, true);
                inputLine = in.readLine();
                Debug.println("************* 2 packet (destination) =", inputLine);
                int destination = Integer.parseInt(inputLine);
                Debug.println("going to read line.");
                inputLine = in.readLine();
                Debug.println("************* 3 packet =", inputLine);
                int byteArraylenght = Integer.parseInt(inputLine);
                if (byteArraylenght > 0) {
                    Debug.println("node has a data packets to send to dest ", currentClientId + ", with data=");
                    System.out.println("\n");
                }
                Debug.println("array of length =", byteArraylenght);
                byte[] toAdd = new byte[byteArraylenght];
                for (int loop = 0; loop < byteArraylenght; loop++) {
                    String data = in.readLine();
                    if (data != null && data.getBytes().length != 0) {
                        System.out.print(" " + data.getBytes()[0]);
                        toAdd[loop] = data.getBytes()[0];
                    }
                    Thread.sleep(1);
                }
                if (byteArraylenght != 0) {
                    savePacketForDestinations(toAdd, destination);
                }
                Debug.println("Before packet 3 ********* ");
                inputLine = in.readLine();
                Debug.println("************ 3 packet =", inputLine);
                int intInput = 0;
                if (inputLine.trim().length() != 0) {
                    intInput = Integer.parseInt(inputLine);
                }
                if (intInput == 1) {
                    Debug.println("node wants to receive data packets");
                    shouldSendDataPacket = true;
                } else {
                    shouldSendDataPacket = false;
                }
                Debug.println("end of input");
            } catch (Exception d) {
                d.printStackTrace();
                System.exit(1);
            }
            return shouldSendDataPacket;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(5);
                    if (clientSocketVector.size() > 0) {
                        Debug.println("******************* =", 1);
                        Socket clientSocket = clientSocketVector.remove(0);
                        Debug.println("******************* =", 2);
                        boolean sendDataPackets = input(clientSocket);
                        output(clientSocket, sendDataPackets);
                        Debug.println("******************* =", 3);
                        Debug.println("\n\n\n\n\n");
                    }
                }
            } catch (Exception d) {
                d.printStackTrace();
                System.exit(1);
            }
        }

        private void output(Socket clientSocket, boolean sendDataPackets) throws IOException {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), false);
            Debug.println(sendDataPackets, ", " + currentClientId);
            if (!sendDataPackets || receivedPackets.get(currentClientId) == null) {
                Debug.println("no data packet to send ", false);
                out.println(0);
            } else {
                Debug.println("sending data to node ....", false);
                ArrayList<byte[]> packetsList = receivedPackets.remove(currentClientId);
                out.println(packetsList.size());
                Iterator<byte[]> it = packetsList.iterator();
                while (it.hasNext()) {
                    byte[] packet = it.next();
                    out.println(packet.length);
                    Debug.println("sending data packet of length " + packet.length, true);
                    for (int loop = 0; loop < packet.length; loop++) {
                        out.println(packet[loop]);
                    }
                }
            }
            out.flush();
        }
    }
}
