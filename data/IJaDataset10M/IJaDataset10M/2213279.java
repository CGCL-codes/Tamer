package com.appspot.trent.denis;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;

public class Denis {

    /**
	 * @param args
	 */
    public static void server() {
        try {
            DatagramSocket socket = new DatagramSocket(5454);
            byte buffer[] = new byte[1024];
            DatagramPacket p = new DatagramPacket(buffer, buffer.length);
            System.out.println("going to receive");
            socket.receive(p);
            DnsRequest request = new DnsRequest(buffer, p.getLength());
            System.out.println("got " + p.getLength() + " bytes from " + p.getAddress());
            System.out.println("Request: " + request);
            HostRecord hostRecord = new HostRecord(request.getQuestions().get(0).getDomainName());
            hostRecord.addIpAddress(new IPAddress(new byte[] { 1, 2, 3, 4 }));
            hostRecord.addIpAddress(new IPAddress(new byte[] { 5, 6, 7, 8 }));
            byte reply[] = DnsResponse.constructPacket(hostRecord, request.getTxnId());
            DatagramPacket replyPkt = new DatagramPacket(reply, reply.length);
            replyPkt.setPort(p.getPort());
            replyPkt.setAddress(p.getAddress());
            socket.send(replyPkt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void client() {
        try {
            DatagramSocket socket = new DatagramSocket();
            byte buffer[] = DnsRequest.constructPacket(1234, 0x100, "baidu.com");
            DatagramPacket p = new DatagramPacket(buffer, buffer.length);
            p.setAddress(Inet4Address.getByAddress(new byte[] { (byte) 192, (byte) 168, (byte) 200, 10 }));
            p.setPort(53);
            socket.send(p);
            byte recvBuffer[] = new byte[1024];
            p = new DatagramPacket(recvBuffer, recvBuffer.length);
            System.out.println("Going to receive");
            socket.receive(p);
            DnsResponse response = new DnsResponse(recvBuffer, p.getLength());
            System.out.println("Response " + response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void testHttp() throws Exception {
        HttpResolver r = new HttpResolver();
        HostRecord record = r.addressForHost("baidu.com");
        System.out.println(record);
    }

    public static void main(String args[]) {
        try {
            DnsServer s = new DnsServer(53);
            s.start();
            s.runShell();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main_x(String args[]) {
        try {
            testHttp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
