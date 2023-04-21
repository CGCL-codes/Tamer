package physique;

import java.io.EOFException;
import java.net.Socket;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.lang.Runnable;
import java.net.SocketException;

/**
 * 
 * @author Quentin, Vincent, Charlie
 *
 */
public class Connexion implements Runnable {

    private Socket lui;

    private Communicateur moi;

    private ObjectOutputStream output;

    private ObjectInputStream input;

    /**
	 * Constructeur
	 * Lien entre les flux entrants / sortants
	 * @param lui
	 * @param moi
	 * @throws IOException 
	 */
    public Connexion(Socket lui, Communicateur moi) throws IOException {
        this.lui = lui;
        this.moi = moi;
        output = new ObjectOutputStream(lui.getOutputStream());
        input = new ObjectInputStream(lui.getInputStream());
        Thread t = new Thread(this);
        t.start();
    }

    /**
	 * Lancement du thread
	 */
    public void run() {
        try {
            while (true) {
                Object recu = input.readObject();
                moi.traiteMessage(recu);
            }
        } catch (EOFException eo) {
            System.out.println("Deconnection du client : done !");
        } catch (Exception e) {
            System.out.println("Socket closed ");
        }
    }

    /**
	 * Fermeture de la socket
	 */
    public void close() {
        try {
            lui.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Envoie d'un objet via la socket
	 * @param O
	 */
    public void Envoie(Object O) {
        System.out.println("ENVOI: " + O.toString());
        try {
            output.writeUnshared(O);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur d'écriture : " + e);
        }
    }
}
