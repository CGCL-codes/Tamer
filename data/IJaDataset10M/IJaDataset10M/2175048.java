package org.openremote.controller.protocol.russound;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;
import org.openremote.controller.event.Event;
import org.openremote.controller.spring.SpringContext;

/**
 * 
 * @author Marcus Redeker
 * 
 */
public class RussoundEvent extends Event {

    /**
     * The russound command. Such zone1.on or zone1.source1 The commands are
     * configured in russound.properties
     */
    private String command;

    /**
     * {@inheritDoc}
     */
    @Override
    public void exec() {
        Properties props = (Properties) SpringContext.getInstance().getBean("russoundConfig");
        if (props.getProperty("connection.type").equals("UDP")) {
            Socket socket = null;
            try {
                InetAddress addr = InetAddress.getByName(props.getProperty("udp.ip"));
                DatagramSocket clientSocket = new DatagramSocket();
                byte[] dataBytes = hexStringToByteArray(props.getProperty(command).replaceAll(" ", "").toLowerCase());
                DatagramPacket sendPacket = new DatagramPacket(dataBytes, dataBytes.length, addr, 4008);
                clientSocket.send(sendPacket);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            try {
                CommPortIdentifier id = CommPortIdentifier.getPortIdentifier(props.getProperty("com.port"));
                SerialPort serialPort = (SerialPort) id.open("ORBController", 2000);
                serialPort.setSerialPortParams(19200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                OutputStream outputStream = serialPort.getOutputStream();
                byte[] dataBytes = hexStringToByteArray(props.getProperty(command).replaceAll(" ", "").toLowerCase());
                outputStream.write(dataBytes);
                outputStream.close();
                serialPort.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
