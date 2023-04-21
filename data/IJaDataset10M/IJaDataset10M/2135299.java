package exercicio51;

import java.net.*;

public class Sockets {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            InetAddress servidor = InetAddress.getByName("localhost");
            DatagramSocket socket;
            for (int i = 0; i < 100; i++) {
                socket = new DatagramSocket();
                DatagramPacket pacote = new DatagramPacket("teste".getBytes(), 0, servidor, 500);
                socket.send(pacote);
                System.out.println(socket.getLocalPort());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
