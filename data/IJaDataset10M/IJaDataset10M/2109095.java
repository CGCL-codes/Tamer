package klient.controller.networkctrl;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.JOptionPane;
import klient.controller.ViewsController;
import klient.model.IllegalOperation;
import klient.model.Model;
import klient.model.fields.Player;

public class TCPSocketThread extends Thread {

    private Socket socket;

    private Model model;

    private ViewsController viewsctrl;

    private DatagramSocket datagsocket;

    public TCPSocketThread(Socket socket, Model m, ViewsController v, DatagramSocket udpsocket) {
        this.socket = socket;
        this.model = m;
        this.viewsctrl = v;
        this.datagsocket = udpsocket;
    }

    public void run() {
        if ((model.isGameOn() == false) && (model.isGameOff() == false)) {
            String send = "hello:" + model.getLocalPlayerNick() + ":" + datagsocket.getLocalPort() + "\n";
            try {
                OutputWriter.sendStringAsPacket(send, socket.getOutputStream());
                System.out.print("<<" + send);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        while (!isInterrupted()) {
            try {
                if ((model.isGameOn() == false) && (model.isGameOff() == false)) {
                    String[] tab = InputReader.getStringsFromPacket(socket.getInputStream());
                    if (tab == null) {
                        System.out.println("Watek TCP zakonczony");
                        viewsctrl.closeApplication();
                    }
                    for (String t : tab) {
                        String[] tokens = t.split(":");
                        if (tokens[0].equals("allow")) {
                            System.out.println(">>" + t);
                            int ID = Integer.parseInt(tokens[1]);
                            model.setLocalPlayerId(ID);
                        } else if (tokens[0].equals("deny")) {
                            System.out.println(">>" + t);
                            model.endGame();
                            JOptionPane.showMessageDialog(null, tokens[1]);
                            viewsctrl.closeApplication();
                            this.interrupt();
                        } else if (tokens[0].equals("players")) {
                            System.out.println(">>" + t);
                            for (int i = 1; i < tokens.length; i++) {
                                String nick = tokens[i].split(",")[0];
                                int id = Integer.parseInt(tokens[i].split(",")[1]);
                                Player p = model.addPlayer(nick, id);
                                viewsctrl.addPlayerToInfoView(p);
                            }
                        } else if (tokens[0].equals("board")) {
                            model.getLm().loadFromMessage(tokens[1]);
                            System.out.println(">>" + tokens[0] + ":" + tokens[1] + ":" + tokens[2]);
                            for (int i = 2; i < tokens.length; i++) {
                                int id = Integer.parseInt(tokens[i].split(",")[0]);
                                int x = Integer.parseInt(tokens[i].split(",")[1]);
                                int y = Integer.parseInt(tokens[i].split(",")[2]);
                                model.setActualPlayerPosition(id, x, y, true);
                            }
                        } else if (tokens[0].equals("countingdown")) {
                            int number = Integer.parseInt(tokens[1]);
                            System.out.println(">>" + number);
                            if (number == 1) {
                                model.startGame();
                                viewsctrl.startPlayViews();
                            }
                        }
                    }
                } else if (model.isGameOn() && (model.isGameOff() == false)) {
                    String[] tab = InputReader.getStringsFromPacket(socket.getInputStream());
                    if (tab == null) {
                        System.out.println("Watek TCP zakonczony");
                        viewsctrl.closeApplication();
                    }
                    for (String t : tab) {
                        String[] tokens = t.split(":");
                        if (tokens[0].equals("end")) {
                            System.out.println(">>" + t);
                            model.getLm().loadFromMessage(tokens[1]);
                            for (int i = 2; i < tokens.length; i++) {
                                int id = Integer.parseInt(tokens[i].split(",")[0]);
                                int x = Integer.parseInt(tokens[i].split(",")[1]);
                                int y = Integer.parseInt(tokens[i].split(",")[2]);
                                int points = Integer.parseInt(tokens[i].split(",")[3]);
                                model.setActualPlayerPosition(id, x, y, false);
                                model.setPoints(id, points);
                                model.endGame();
                            }
                            sendQuit();
                        }
                    }
                } else if ((model.isGameOn() == false) && model.isGameOff()) {
                    System.out.println("bla");
                    sendQuit();
                }
            } catch (SocketException e) {
                System.out.println("Watek TCP zakonczony - socket exception");
                viewsctrl.closeApplication();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalOperation e) {
                e.printStackTrace();
            }
        }
        System.out.println("Watek TCP zakonczony");
        viewsctrl.closeApplication();
    }

    /**
	 * @throws IOException
	 */
    public void sendQuit() throws IOException {
        String send = String.valueOf(model.getLocalPlayerId());
        send = send.concat(":quit\n");
        OutputWriter.sendStringAsPacket(send, socket.getOutputStream());
        System.out.print("<<" + send);
        System.out.println("Watek TCP zakonczony - koniec gry");
        viewsctrl.closeApplication();
    }
}
