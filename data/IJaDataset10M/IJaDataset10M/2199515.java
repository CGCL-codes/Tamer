package controller;

import test.*;
import java.net.*;

/**
 *
 * @author dave
 */
public class Reciever implements Runnable {

    public DatagramSocket dsocket;

    private BehaviourController controller;

    private Decoder decoder;

    public Reciever(BehaviourController controller) {
        this.controller = controller;
        new Thread(this, "Reciever").start();
    }

    public Decoder getDecoder() {
        return decoder;
    }

    public DatagramSocket getSocket() {
        return this.dsocket;
    }

    public void run() {
        try {
            decoder = new Decoder();
            String host = "localhost";
            byte[] buffer = new byte[20480];
            byte[] message = new byte[0];
            if (controller.getProperty("INIT_STRING").equals("left")) {
                message = "(init Left(version 13))".getBytes();
            } else if (controller.getProperty("INIT_STRING").equals("left_goalie")) {
                message = "(init Left(version 13)(goalie))".getBytes();
            } else if (controller.getProperty("INIT_STRING").equals("right")) {
                message = "(init Right(version 13))".getBytes();
            } else if (controller.getProperty("INIT_STRING").equals("right_goalie")) {
                message = "(init Right(version 13)(goalie))".getBytes();
            }
            InetAddress address = InetAddress.getByName(host);
            DatagramPacket packet = new DatagramPacket(message, message.length, address, Integer.parseInt(controller.getProperty("port")));
            dsocket = new DatagramSocket();
            dsocket.send(packet);
            packet = new DatagramPacket(buffer, buffer.length);
            int i = 0;
            while (true) {
                dsocket.receive(packet);
                String msg = new String(buffer, 0, packet.getLength());
                if (i == 0) {
                    controller.setProperty("port", "" + packet.getPort());
                }
                decoder.decode(msg, controller);
                if (msg.startsWith("(see") || msg.startsWith("(hear")) {
                }
                System.out.println(packet.getAddress().getHostName() + ": " + " " + packet.getPort() + ";" + msg);
                packet.setLength(buffer.length);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
