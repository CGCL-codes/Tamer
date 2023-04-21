package controleur.updater;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author marion
 */
public class InfosMiseAJour extends Thread {

    private String version = "";

    private String url = "";

    private boolean erreur = false;

    private String texteErreur;

    private int numErreur;

    private String urlInfo;

    /**
     * écouteurs
     */
    private ArrayList<ActionListener> listeners;

    private String informations;

    public InfosMiseAJour(String urlInfo) {
        this.urlInfo = urlInfo;
        listeners = new ArrayList<ActionListener>();
    }

    public void addActionListener(ActionListener al) {
        listeners.add(al);
    }

    public boolean comparerVersions(String versionActuelle) {
        String table1[] = versionActuelle.split("\\.");
        int taille1 = table1.length;
        String table2[] = version.split("\\.");
        int taille2 = table2.length;
        int i = 0;
        try {
            while (i < taille1 && i < taille2) {
                if (Integer.parseInt(table2[i]) > Integer.parseInt(table1[i])) {
                    erreur = false;
                    return true;
                }
                i++;
            }
        } catch (NumberFormatException e) {
            erreur = true;
            return false;
        }
        return false;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL(urlInfo).openStream()));
            String ligneEnCours;
            int i = 0;
            informations = "";
            while ((ligneEnCours = in.readLine()) != null) {
                switch(i) {
                    case 0:
                        version = ligneEnCours;
                        break;
                    case 1:
                        url = ligneEnCours;
                        break;
                    default:
                        informations += ligneEnCours + '\n';
                        break;
                }
                i++;
            }
            in.close();
            erreur = false;
        } catch (IOException e) {
            erreur = true;
            texteErreur = e.getMessage();
            if (texteErreur.equals("Network is unreachable")) {
                texteErreur = "Pas de réseau";
                numErreur = 1;
            }
            if (e instanceof FileNotFoundException) {
                texteErreur = "Problème paramétrage";
                numErreur = 2;
            }
            e.printStackTrace();
        } finally {
            for (ActionListener al : listeners) {
                al.actionPerformed(null);
            }
        }
    }

    public String getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }

    public boolean getErreur() {
        return (erreur);
    }

    public String getTexteErreur() {
        return (texteErreur);
    }

    public int getNumErreur() {
        return (numErreur);
    }

    public String getInformations() {
        return (informations);
    }
}
