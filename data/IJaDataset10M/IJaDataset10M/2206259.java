package serveur;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stephane
 */
public class MonThread extends Thread implements IMonThread {

    private String nameClient;

    private Socket sock;

    private boolean admin = false;

    private boolean kickClient = false;

    private String mdp;

    private InterfaceDebriefing interfaceDebriefing = null;

    MonThread() {
    }

    MonThread(InterfaceDebriefing interfaceDebriefing) {
        this.interfaceDebriefing = interfaceDebriefing;
    }

    MonThread(Socket sock, InterfaceDebriefing interfaceDebriefing) {
        this.interfaceDebriefing = interfaceDebriefing;
        this.sock = sock;
    }

    public void setKickClient(boolean bool) {
        kickClient = bool;
    }

    /**
     * @see serveur.IMonThread#getSock()
     */
    public Socket getSock() {
        return sock;
    }

    /**
     * @see serveur.IMonThread#setSock(Socket)
     */
    public void setSock(Socket sock) {
        this.sock = sock;
    }

    /**
     * @see serveur.IMonThread#getNameClient()
     */
    public String getNameClient() {
        return nameClient;
    }

    /**
     * @see serveur.IMonThread#setNameClient(java.lang.String)
     */
    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    /**
     * @see serveur.IMonThread#checkName(String)
     */
    public boolean checkName(String name) {
        Iterator myIter = listClient.iterator();
        while (myIter.hasNext()) {
            IMonThread myClient = (IMonThread) myIter.next();
            if (myClient.getNameClient().equals(name)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void run() {
        byte[] data = new byte[1000];
        int size = 0;
        try {
            size = sock.getInputStream().read(data);
            setNameClient(new String(data, 0, size));
        } catch (IOException ex) {
            Logger.getLogger(MonThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (checkName(this.nameClient)) {
            interfaceDebriefing.maServeurFrame.setTextePane(this.nameClient + " est entré sur le serveur !!");
            try {
                envoieMessage(this.sock, "/serveur Vous ete correctement connecter sur le Serveur");
                try {
                    sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MonThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                envoieConnection();
                listClient.add(this);
                refreshUserList();
                while (true) {
                    size = this.sock.getInputStream().read(data);
                    messageTraitement(new String(data, 0, size));
                }
            } catch (IOException ex) {
                interfaceDebriefing.maServeurFrame.setTextePane(this.nameClient + " viens de se deco !!");
                Logger.getLogger(MonThread.class.getName()).log(Level.SEVERE, this.nameClient + " viens de se deco !!", ex);
            } finally {
                try {
                    deconnectionClient(this.sock);
                    removeClient();
                    if (!kickClient) {
                        envoieDeconection();
                    }
                    refreshUserList();
                    finalized();
                } catch (Throwable ex) {
                    Logger.getLogger(MonThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            interfaceDebriefing.maServeurFrame.setTextePane(this.nameClient + " est deja un nom d'utilisateur enregistré !!");
            try {
                envoieMessage(this.sock, "/serveur Name already use !");
                deconnectionClient(this.sock);
                finalized();
            } catch (IOException ex) {
                Logger.getLogger(MonThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Throwable ex) {
                Logger.getLogger(MonThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void finalized() throws Throwable {
        this.finalize();
    }

    /**
     * @see serveur.IMonThread#addClient(this)
     */
    public void addClient(IMonThread client) {
        listClient.add(client);
    }

    /**
     * @see serveur.IMonThread#removeClient()
     */
    public void removeClient() {
        Iterator myIter = listClient.iterator();
        while (myIter.hasNext()) {
            IMonThread myClient = (IMonThread) myIter.next();
            if (myClient.getSock().isClosed()) {
                myIter.remove();
            }
        }
    }

    /**
     * @see serveur.IMonThread#envoieDeconection(sock)
     */
    public void envoieDeconection() throws IOException {
        Iterator myIter = listClient.iterator();
        while (myIter.hasNext()) {
            IMonThread myClient = (IMonThread) myIter.next();
            myClient.getSock().getOutputStream().write(("/serveur " + new String(nameClient + " quit the server")).getBytes());
            myClient.getSock().getOutputStream().flush();
        }
    }

    /**
     * @see serveur.IMonThread#envoieMessage(java.net.Socket, java.lang.String)
     */
    public void envoieMessage(Socket sock, String msg) throws IOException {
        sock.getOutputStream().write(new String(msg).getBytes());
        sock.getOutputStream().flush();
    }

    /**
     * @see serveur.IMonThread#envoieConnection(java.net.Socket)
     */
    public void envoieConnection() throws IOException {
        Iterator myIter = listClient.iterator();
        while (myIter.hasNext()) {
            IMonThread myClient = (IMonThread) myIter.next();
            myClient.getSock().getOutputStream().write(("/serveur " + this.nameClient + " enter on the server").getBytes());
            myClient.getSock().getOutputStream().flush();
        }
    }

    /**
     * @see serveur.IMonThread#deconnectionClient()
     */
    public void deconnectionClient(Socket sock) throws IOException {
        sock.close();
    }

    /**
     * @see serveur.IMonThread#refreshUserList() 
     */
    public void refreshUserList() throws IOException {
        Iterator myIter = listClient.iterator();
        while (myIter.hasNext()) {
            IMonThread myClient = (IMonThread) myIter.next();
            envoieListClient(myClient.getSock());
        }
    }

    /**
     * @see serveur.IMonThread#envoieListClient(java.net.Socket)
     */
    public void envoieListClient(Socket sock) throws IOException {
        String clientNames = "";
        Iterator myIter = listClient.iterator();
        while (myIter.hasNext()) {
            IMonThread myClient = (IMonThread) myIter.next();
            clientNames += " " + myClient.getNameClient();
        }
        envoieMessage(sock, "/userlist" + clientNames);
    }

    /**
     * @see serveur.IMonThread#kickClient(String)
     */
    public void kickClient(String name, String raison) throws IOException, Throwable {
        Iterator myIter = listClient.iterator();
        while (myIter.hasNext()) {
            IMonThread myClient = (IMonThread) myIter.next();
            if (myClient.getNameClient().equals(name)) {
                envoieMessage(myClient.getSock(), "/serveur Vous avez été kick par " + this.nameClient);
                envoieMessage(myClient.getSock(), "/serveur cause: " + raison);
                myClient.setKickClient(true);
                deconnectionClient(myClient.getSock());
            } else {
                envoieMessage(myClient.getSock(), "/serveur " + name + " a été kick par " + this.nameClient);
            }
        }
    }

    /**
     * @see serveur.IMonThread#messageTraitement(String)
     */
    public void messageTraitement(String msg) {
        if (msg.startsWith("/admin ")) {
            if (msg.startsWith("/admin denisTheBest")) {
                admin = true;
                setNameClient("@" + this.nameClient);
                try {
                    refreshUserList();
                    envoieMessage(this.sock, "/serveur Vous ete maintenant Administrateur !");
                } catch (IOException ex) {
                    Logger.getLogger(MonThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (msg.startsWith("/kick ")) {
            if (admin) {
                String[] monKick = msg.split(" ");
                try {
                    kickClient(monKick[1], msg.substring("/kick ".length() + monKick[1].length()));
                } catch (IOException ex) {
                    Logger.getLogger(MonThread.class.getName()).log(Level.SEVERE, "Le client n'a pu etre kick", ex);
                } catch (Throwable ex) {
                    Logger.getLogger(MonThread.class.getName()).log(Level.SEVERE, "Le client n'a pu etre detruit", ex);
                }
            }
        } else if (msg.startsWith("/me ")) {
            Iterator myIter = listClient.iterator();
            while (myIter.hasNext()) {
                IMonThread myClient = (IMonThread) myIter.next();
                try {
                    envoieMessage(myClient.getSock(), new String("*" + this.nameClient + " " + msg.substring(4) + "*"));
                } catch (IOException ex) {
                    Logger.getLogger(MonThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (msg.startsWith("/send ") || msg.startsWith("/w ")) {
            Socket sockSend = null;
            IMonThread myClient = null;
            String name = msg.split(" ", 3)[1];
            Iterator myIter = listClient.iterator();
            while (myIter.hasNext() && sockSend == null) {
                myClient = (IMonThread) myIter.next();
                if (myClient.getNameClient().equals(name)) {
                    sockSend = myClient.getSock();
                }
            }
            if (sockSend != null) {
                try {
                    envoieMessage(sockSend, "/send <" + this.nameClient + " Send>" + msg.substring(msg.indexOf(" ") + name.length() + 2));
                    envoieMessage(this.sock, "/send <" + this.nameClient + " Send>" + msg.substring(msg.indexOf(" ") + name.length() + 2));
                } catch (IOException ex) {
                    Logger.getLogger(MonThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    envoieMessage(this.sock, "/serveur " + name + " est deconnecté ou n'existe pas !!");
                } catch (IOException ex) {
                    Logger.getLogger(MonThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            Iterator myIter = listClient.iterator();
            while (myIter.hasNext()) {
                IMonThread myClient = (IMonThread) myIter.next();
                try {
                    envoieMessage(myClient.getSock(), new String("<" + this.nameClient + ">" + msg));
                } catch (IOException ex) {
                    Logger.getLogger(MonThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
