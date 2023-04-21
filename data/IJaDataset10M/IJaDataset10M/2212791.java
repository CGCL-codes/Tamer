package dr.reseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import sun.management.Agent;
import dr.joueurs.Humain;
import dr.joueurs.Joueur;
import dr.moteur.AgentAbstrait;
import dr.moteur.BaseAbstraite;
import dr.moteur.GameEngine;
import dr.moteur.GameEngineServer;
import dr.moteur.ProjectileAbstrait;
import dr.moteur.TourAbstraite;
import dr.util.GameSetup;
import dr.util.GrapheChemin;

public class ServeurReseau {

    public class Sender extends Thread {

        private Socket client;

        private int numClient;

        public boolean changementBase;

        public boolean changementTour;

        public boolean changementAgent;

        public boolean changementProjectile;

        public boolean isAGoSend;

        public Sender(Socket client, int numClient) {
            this.client = client;
            this.numClient = numClient;
            changementBase = false;
            changementTour = false;
            changementAgent = false;
            changementProjectile = false;
            isAGoSend = false;
        }

        public void run() {
            ObjectOutputStream OUTOBJ = null;
            System.out.println("Thread d'envoi des donnees du serveur au client " + numClient + " lance ---DEBUT");
            try {
                OUTOBJ = new ObjectOutputStream(client.getOutputStream());
                OUTOBJ.writeObject(this.numClient);
                while (true) {
                    if ((isAGo == true) && (isAGoSend == false)) {
                        OUTOBJ.writeObject("full");
                        for (Joueur g : gamers) {
                            OUTOBJ.writeObject(g);
                        }
                        isAGoSend = true;
                        System.out.println("Je suis plein, je lance la partie !");
                    }
                    if (changementBase == true) {
                        for (BaseAbstraite b : GE.getListeBases()) OUTOBJ.writeObject(b);
                        OUTOBJ.writeObject("end.");
                    }
                    if (changementTour == true) {
                        for (TourAbstraite t : GE.getListeTours()) OUTOBJ.writeObject(t);
                        OUTOBJ.writeObject("end.");
                    }
                    if (changementProjectile == true) {
                        for (ProjectileAbstrait p : GE.getListeProjectiles()) OUTOBJ.writeObject(p);
                        OUTOBJ.writeObject("end.");
                    }
                    if (changementAgent == true) {
                        for (AgentAbstrait a : GE.getListeAgents()) OUTOBJ.writeObject(a);
                        OUTOBJ.writeObject("end.");
                    }
                    changementBase = false;
                    changementTour = false;
                    changementAgent = false;
                    changementProjectile = false;
                    Thread.sleep(200);
                }
            } catch (Exception ex) {
                System.err.println("L'erreur suivante s'est produite : " + ex.getMessage());
            }
            try {
                OUTOBJ.close();
                client.close();
            } catch (Exception ex) {
            }
            System.out.println("Thread d'envoi des donnees du serveur au client " + numClient + " ferme ---FIN");
        }
    }

    public class Receiver extends Thread {

        private Socket client;

        private int numClient;

        public Receiver(Socket client, int numClient) {
            this.client = client;
            this.numClient = numClient;
        }

        public void run() {
            ObjectInputStream INOBJ = null;
            Requete req = null;
            Object objRecu = null;
            System.out.println("Thread de lecture des donnees du client " + numClient + "au serveur lance ---DEBUT");
            try {
                INOBJ = new ObjectInputStream(client.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    objRecu = INOBJ.readObject();
                } catch (IOException e) {
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (objRecu == null) break;
                if (objRecu instanceof String) {
                    config = (String) objRecu;
                    configure = true;
                } else {
                    req = (Requete) objRecu;
                    System.out.println("<-- " + req);
                    if (req.tour != null) {
                        GE.getListeTours().addLast(req.tour);
                        GE.getListeTours().getLast().setGameEngine(GE);
                        setChange(false, false, true, false);
                    } else {
                        if (req.tourCourante != -1) {
                            if (req.amelioration == 0) {
                                GE.getListeTours().remove(req.tourCourante);
                            } else if (req.amelioration == 1) {
                                GE.getListeTours().get(req.tourCourante).setNiveau(GE.getListeTours().get(req.tourCourante).getNiveau() + 1);
                                GE.getListeTours().get(req.tourCourante).renommer();
                                GE.getListeTours().get(req.tourCourante).setPorteeMem((float) GE.getListeTours().get(req.tourCourante).getPorteeMem() * 1.10f);
                                GE.getListeTours().get(req.tourCourante).setPuissanceMem((int) (GE.getListeTours().get(req.tourCourante).getPuissanceMem() * 1.10f));
                                GE.getListeTours().get(req.tourCourante).setCadenceDeTirMem(GE.getListeTours().get(req.tourCourante).getCadenceDeTirMem() * 0.90f);
                            } else if (req.amelioration == 2) {
                                GE.getListeTours().get(req.tourCourante).setNiveau(GE.getListeTours().get(req.tourCourante).getNiveau() + 1);
                                GE.getListeTours().get(req.tourCourante).renommer();
                                GE.getListeTours().get(req.tourCourante).setPorteeMem((float) GE.getListeTours().get(req.tourCourante).getPorteeMem() * 1.20f);
                                GE.getListeTours().get(req.tourCourante).setPuissanceMem((int) (GE.getListeTours().get(req.tourCourante).getPuissanceMem() * 1.05f));
                                GE.getListeTours().get(req.tourCourante).setCadenceDeTirMem(GE.getListeTours().get(req.tourCourante).getCadenceDeTirMem() * 0.95f);
                            } else if (req.amelioration == 3) {
                                GE.getListeTours().get(req.tourCourante).setNiveau(GE.getListeTours().get(req.tourCourante).getNiveau() + 1);
                                GE.getListeTours().get(req.tourCourante).renommer();
                                GE.getListeTours().get(req.tourCourante).setPorteeMem((float) GE.getListeTours().get(req.tourCourante).getPorteeMem() * 1.05f);
                                GE.getListeTours().get(req.tourCourante).setPuissanceMem((int) (GE.getListeTours().get(req.tourCourante).getPuissanceMem() * 1.20f));
                                GE.getListeTours().get(req.tourCourante).setCadenceDeTirMem(GE.getListeTours().get(req.tourCourante).getCadenceDeTirMem() * 0.95f);
                            } else if (req.amelioration == 4) {
                                GE.getListeTours().get(req.tourCourante).setNiveau(GE.getListeTours().get(req.tourCourante).getNiveau() + 1);
                                GE.getListeTours().get(req.tourCourante).renommer();
                                GE.getListeTours().get(req.tourCourante).setPorteeMem((float) GE.getListeTours().get(req.tourCourante).getPorteeMem() * 1.05f);
                                GE.getListeTours().get(req.tourCourante).setPuissanceMem((int) (GE.getListeTours().get(req.tourCourante).getPuissanceMem() * 1.05f));
                                GE.getListeTours().get(req.tourCourante).setCadenceDeTirMem(GE.getListeTours().get(req.tourCourante).getCadenceDeTirMem() * 0.80f);
                            }
                            setChange(false, false, true, false);
                        } else {
                            if (req.baseDestination != -1) {
                                GE.getListeBases().get(req.baseCourante).setCible(GE.getListeBases().get(req.baseDestination));
                                LinkedList<Integer> chemin = graph.courtCheminLinked(req.baseCourante, req.baseDestination);
                                GE.getListeBases().get(req.baseCourante).setItineraire(chemin);
                            } else {
                                GE.getListeBases().get(req.baseCourante).setAgentCree(req.production);
                            }
                            setChange(false, true, false, false);
                        }
                    }
                }
                try {
                    Thread.sleep(200);
                } catch (Exception ex) {
                    System.err.println("L'erreur suivante s'est produite : " + ex.getMessage());
                }
            }
            try {
                INOBJ.close();
                client.close();
            } catch (Exception ex) {
            }
            System.out.println("Thread de lecture des donnees du client " + numClient + "au serveur ferme ---FIN");
        }
    }

    public static boolean debug_setup_claire = false;

    public static boolean debug_no_map = false;

    public static boolean debug_no_textures = false;

    public static boolean debug_display = false;

    public static boolean debug_zones = false;

    public static GrapheChemin graph;

    private static LinkedList<Sender> clients;

    public LinkedList<Joueur> gamers;

    public static GameEngine GE;

    public GameSetup GS;

    public String config;

    public int numClient;

    public boolean configure;

    public boolean isAGo;

    public ServeurReseau() {
        gamers = new LinkedList<Joueur>();
        clients = new LinkedList<Sender>();
        config = new String();
        configure = false;
        isAGo = false;
        numClient = -1;
    }

    @Override
    public String toString() {
        System.out.println(clients.toString());
        return clients.toString();
    }

    public void setChange(boolean agent, boolean base, boolean tours, boolean projectiles) {
        for (int i = 0; i < clients.size(); i++) {
            if (agent) clients.get(i).changementAgent = agent;
            if (base) clients.get(i).changementBase = base;
            if (projectiles) clients.get(i).changementProjectile = projectiles;
            if (tours) clients.get(i).changementTour = tours;
        }
    }

    public static void erreur(String msg, int exitCode) {
        System.err.println(msg);
        System.exit(exitCode);
    }

    public static void main(String[] args) {
        final String syntaxe = "Syntaxe : java ServeurReseau 'port' \n";
        if (args.length != 1) erreur(syntaxe, 1);
        int port = 0;
        boolean erreurPort = false;
        Exception E = null;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            E = e;
            erreurPort = true;
        }
        erreurPort = erreurPort || port <= 0;
        if (erreurPort) erreur(syntaxe + "\n" + "Port incorrect (" + E + ")", 2);
        ServerSocket ecoute = null;
        ServeurReseau serveur = new ServeurReseau();
        serveur.numClient = 0;
        try {
            ecoute = new ServerSocket(port);
        } catch (Exception ex) {
            erreur("L'erreur suivante s'est produite : " + ex.getMessage(), 3);
        }
        System.out.println("Serveur g�n�rique lanc� sur le port " + port + "\n");
        Socket client = null;
        Sender sClient;
        boolean continuer = true;
        long tempsStocke = 0;
        long temps = 0;
        int njMax = 2;
        while (serveur.numClient < njMax) {
            try {
                client = ecoute.accept();
            } catch (Exception ex) {
                erreur("L'erreur suivante s'est produite : " + ex.getMessage(), 3);
            }
            sClient = serveur.new Sender(client, serveur.numClient);
            sClient.start();
            serveur.new Receiver(client, serveur.numClient).start();
            clients.add(sClient);
            if (serveur.numClient == 0) {
                while (!serveur.configure) {
                    try {
                        Thread.sleep(200);
                    } catch (Exception ex) {
                        erreur("L'erreur suivante s'est produite : " + ex.getMessage(), 3);
                    }
                }
                ServeurReseau.GE = new GameEngineServer();
                serveur.GS = new GameSetup(serveur.config, ServeurReseau.GE);
                ServeurReseau.graph = serveur.GS.get_graphe();
                ServeurReseau.graph.finalisation();
            }
            Joueur j = new Humain(serveur.numClient, 1000, "bob");
            ServeurReseau.GE.getListeJoueurs().addLast(j);
            serveur.gamers.add(j);
            serveur.numClient++;
            System.out.println(serveur.numClient);
        }
        int ij = 0, cpt_b = 0;
        for (BaseAbstraite b : GE.getListeBases()) {
            if (b.getNom().equalsIgnoreCase("QG") && ij < njMax) {
                GE.getListeBases().get(cpt_b).setIdJoueur(ij);
                ij++;
            }
            cpt_b++;
        }
        try {
            Thread.sleep(200);
        } catch (Exception ex) {
            erreur("L'erreur suivante s'est produite : " + ex.getMessage(), 3);
        }
        serveur.isAGo = true;
        serveur.setChange(false, true, false, false);
        System.out.println("Cesticilestedf " + ServeurReseau.GE.getListeBases().get(0).getIdJoueur());
        try {
            Thread.sleep(2000);
        } catch (Exception ex) {
            erreur("L'erreur suivante s'est produite : " + ex.getMessage(), 3);
        }
        while (continuer) {
            temps = System.currentTimeMillis();
            for (int i = 0; i < ServeurReseau.GE.getListeTours().size(); i++) {
                ServeurReseau.GE.getListeTours().get(i).sauverEffet();
                ServeurReseau.GE.getListeTours().get(i).appliquerEffets();
            }
            for (int i = 0; i < ServeurReseau.GE.getListeAgents().size(); i++) {
                ServeurReseau.GE.getListeAgents().get(i).sauverEffet();
                ServeurReseau.GE.getListeAgents().get(i).appliquerEffets();
            }
            ServeurReseau.GE.update();
            if (temps - tempsStocke > 16.0) {
                tempsStocke = temps;
            }
            for (int i = 0; i < ServeurReseau.GE.getListeTours().size(); i++) {
                ServeurReseau.GE.getListeTours().get(i).retirerEffet();
            }
            for (int i = 0; i < ServeurReseau.GE.getListeAgents().size(); i++) {
                ServeurReseau.GE.getListeAgents().get(i).retirerEffet();
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
