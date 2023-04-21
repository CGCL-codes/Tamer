package metier.modele;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import physique.Communicateur;
import physique.Connexion;
import client.Gestionnaire_Client;
import client.JCanvas;
import client.panUsers;

/**
 * @author Quentin, Vincent, Charlie
 */
public class Client implements Communicateur {

    private String IPServeur;

    private int port;

    private boolean online;

    private Socket socket;

    private Connexion cnx;

    private JCanvas jCanvas;

    private panUsers panUsers;

    private JFrame mainFrame;

    private Utilisateur user;

    /**
	 * Constructeur
	 * @param IPServeur
	 * @param port
	 * @param gestionnaireClient pour la MAJ des informations
	 * @param pseudo
	 */
    public Client(String IPServeur, int port, Gestionnaire_Client gestionnaireClient, String pseudo) {
        this.IPServeur = IPServeur;
        this.port = port;
        this.jCanvas = gestionnaireClient.getPanDessin();
        this.panUsers = gestionnaireClient.getPanUsers();
        this.mainFrame = gestionnaireClient.getFen();
        this.user = new Utilisateur(pseudo, -1, Utilisateur.INIT);
    }

    /**
	 * Connexion au serveur.
	 * @return true si connexion réussie, false sinon
	 */
    public boolean creationClient() {
        try {
            System.out.println("Tentative de connexion à " + IPServeur + ":" + port);
            socket = new Socket(IPServeur, port);
            cnx = new Connexion(socket, this);
            return true;
        } catch (Exception e) {
            System.out.println("Erreur lors de la crétion du client : " + e);
            return false;
        }
    }

    /**
	 * Fermeture du client (envoie de l'info au serveur)
	 * @throws InterruptedException 
	 */
    public void fermetureClient() {
        this.user.setStatus(Utilisateur.SUPPRESSION);
        System.out.println("CLIENT: fermeture. ID: " + user.getId() + " - Status: " + user.getStatus());
        getCnx().Envoie(this.user);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        getCnx().close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Méthode traitant les messages reçus via la socket (Figures, Utilisateurs, Integer, ...)
	 */
    public synchronized void traiteMessage(Object O) {
        try {
            if (O instanceof Figure) {
                Figure f = (Figure) O;
                int s = f.getStatus();
                if (s == Figure.AJOUT) {
                    System.out.println("CLIENT : Réception Figure AJOUT");
                    jCanvas.getList().addLast(f);
                } else if (s == Figure.SUPPRESSION) {
                    System.out.println("CLIENT : Réception Figure SUPPRESSION");
                    jCanvas.getList().remove(f.getId());
                } else if (s == Figure.EDIT) {
                    jCanvas.getList().remove(f.getId());
                    f.setStatus(Figure.AJOUT);
                    jCanvas.getList().addLast(f);
                }
                for (int i = 0; i < jCanvas.getList().size(); i++) jCanvas.getList().get(i).setId(i);
                jCanvas.repaint();
            } else if (O instanceof Utilisateur) {
                Utilisateur user = (Utilisateur) O;
                int status = user.getStatus();
                if (status == Utilisateur.AJOUT) {
                    System.out.println("CLIENT: Réception utilisateur AJOUT");
                    panUsers.getList().addLast(user);
                } else if (status == Utilisateur.INIT) {
                    System.out.println("CLIENT: Réception utilisateur INIT");
                    if (status == this.user.getStatus()) {
                        System.out.println("CLIENT: Attribution nouveau user");
                        this.user = user;
                        this.user.setStatus(Utilisateur.AJOUT);
                        panUsers.setUser(this.user);
                        mainFrame.setTitle(Constantes.CLIENT_TITLE + " - Nom: " + this.user.getNom() + " - ID: " + this.user.getId());
                    }
                    user.setStatus(Utilisateur.AJOUT);
                    panUsers.getList().addLast(user);
                } else if (status == Utilisateur.SUPPRESSION) {
                    System.out.println("CLIENT: Réception utilisateur SUPPRESSION: " + user.toString());
                    displayList(panUsers.getList());
                    panUsers.removeUser(user);
                    displayList(panUsers.getList());
                }
                panUsers.repaint();
            } else if (O instanceof Integer) {
                int i = (Integer) O;
                if (i == Constantes.PING) {
                    System.out.println("CLIENT: PING?");
                    cnx.Envoie(Constantes.PONG);
                } else if (i == Constantes.SERVEUR_CLOSE) {
                    socket.close();
                    setOnline(false);
                    mainFrame.remove(panUsers);
                    mainFrame.repaint();
                    JOptionPane.showMessageDialog(null, Constantes.CLIENT_OFFLINE, Constantes.CLIENT_OFFLINE_TITLE, JOptionPane.WARNING_MESSAGE);
                } else if (i == Constantes.TOO_MANY_CONNECTED) {
                    socket.close();
                    setOnline(false);
                    mainFrame.remove(panUsers);
                    mainFrame.repaint();
                    JOptionPane.showMessageDialog(null, Constantes.CLIENT_SERVER_FULL, Constantes.CLIENT_SERVER_FULL_TITLE, JOptionPane.WARNING_MESSAGE);
                } else if (i == Constantes.INIT_DRAW) {
                    jCanvas.getList().clear();
                    jCanvas.repaint();
                } else {
                    int oldid = user.getId();
                    user.setId(i);
                    System.out.println("CLIENT: MAJ ID: " + oldid + " => " + user.getId());
                }
            } else {
                System.out.println("CLIENT Objet reçu non identifié : Valeur: " + O.toString());
            }
        } catch (Exception e) {
            System.out.println("Objet recu non identifié ! C <rien>");
            e.printStackTrace();
        }
    }

    /**
	 * Affiche une liste
	 * @param l liste à afficher
	 */
    public void displayList(LinkedList<?> l) {
        for (Object object : l) {
            System.out.print(object.toString() + " | ");
        }
        System.out.println();
    }

    public Connexion getCnx() {
        return cnx;
    }

    public Utilisateur getUser() {
        return user;
    }

    public void setUser(Utilisateur user) {
        this.user = user;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isOnline() {
        return online;
    }
}
