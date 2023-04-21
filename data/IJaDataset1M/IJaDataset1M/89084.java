package colonsdelutbm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ThreadAccept extends Thread {

    private ServerSocket serveur;

    private ThreadByClient[] clients;

    private static int nbClients;

    public static int getNbClients() {
        return nbClients;
    }

    public void setCommande(int i, int c) {
        clients[i].setCommande(c);
    }

    public String getClientName(int i) {
        return clients[i - 1].getNameJoueur();
    }

    public ThreadAccept(ServerSocket serv) {
        serveur = serv;
        clients = new ThreadByClient[3];
        start();
    }

    public void run() {
        nbClients = 0;
        Socket client;
        while (Jeu.ingame == false && nbClients < 3) {
            try {
                client = serveur.accept();
                if (Jeu.ingame == false) {
                    clients[nbClients] = new ThreadByClient(client);
                    ++nbClients;
                }
            } catch (SocketTimeoutException id) {
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            sleep(1000, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
