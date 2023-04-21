package com.um2.simplexe.serveur.ui.util.controleUI.actionsGeneriques;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JOptionPane;
import com.um2.simplexe.serveur.ui.entryPoint.FenetreApplicationServeur;
import com.um2.simplexe.serveur.ui.util.controleUI.InformationsSurApplicationServeur;
import com.um2.simplexe.serveur.ui.util.controleUI.chargeur.ChargeurLibrairie;
import com.um2.simplexe.serveur.ui.util.controleUI.chargeur.menu.ControleurDeMenu;

/**
 * 
 * Dans le but d'avoir une interface unifiee,
 * il faut se mettre d'accord sur la taille de police,
 * les messages d'alerte,
 * les menus,...
 * 
 * Ici on a un acces centralise sur les fonction UI.
 * 
 * De plus cela evite les fautes d'orthographe,
 * car on n'a cas corriger chaque faute une seule fois .
 * 
 * @author jean-marie codol
 *
 */
public class Evenements {

    static int compteurDeFichiersTemporaires = 0;

    /**
	 * constructeur par defaut
	 *
	 */
    public Evenements() {
    }

    public static void quitterLAppli() {
        if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(FenetreApplicationServeur.getInstance(), "Quitter ?", "Quitter Simplexe", JOptionPane.YES_NO_OPTION)) System.exit(0);
    }

    public static void warning(String message) {
        JOptionPane.showMessageDialog(null, message, "alerte", JOptionPane.WARNING_MESSAGE);
    }

    public static void verifierSiDerniereVersionDesPluginsMenus(ControleurDeMenu i) {
        if (i.getURLFichierInfoDerniereVersion() == null || i.getURLFichierInfoDerniereVersion() == "") {
            System.err.println("Evenements.java:verifierSiDerniereVersionDesPluginsMenus impossible:\n" + "pour le plugin chargeur de menu :" + i.getNomPlugin());
        }
        if (i.getVersionPlugin() == 0) {
            System.err.println("version non renseignee pour :" + i.getNomPlugin() + " on continue sur le plugin suivant");
            return;
        }
        URL url;
        try {
            url = new URL(i.getURLFichierInfoDerniereVersion());
        } catch (MalformedURLException e1) {
            System.err.println("impossible d'ouvrir l'URL (url mal formee)" + i.getURLFichierInfoDerniereVersion() + "\n lors de la recuperation des informations de version sur " + i.getNomPlugin());
            return;
        }
        InputStream is;
        try {
            is = url.openStream();
        } catch (IOException e1) {
            System.err.println("impossible d'ouvrir l'URL (destination inaccessible)" + i.getURLFichierInfoDerniereVersion() + "\n lors de la recuperation des informations de version sur " + i.getNomPlugin());
            return;
        }
        File destination;
        try {
            destination = File.createTempFile("SimplexeReseau" + compteurDeFichiersTemporaires, ".buf");
        } catch (IOException e1) {
            System.err.println("impossible de creer le fichier temporaire\n lors de la recuperation des informations de version sur " + i.getNomPlugin());
            return;
        }
        compteurDeFichiersTemporaires++;
        destination.deleteOnExit();
        java.io.InputStream sourceFile = null;
        java.io.FileOutputStream destinationFile = null;
        try {
            destination.createNewFile();
        } catch (IOException e) {
            System.err.println("impossible de creer un fichier temporaire\n lors de la recuperation des informations de version sur " + i.getNomPlugin());
            return;
        }
        sourceFile = is;
        try {
            destinationFile = new FileOutputStream(destination);
        } catch (FileNotFoundException e) {
            System.err.println("impossible d'ouvrir le flux reseau\n lors de la recuperation des informations de version sur " + i.getNomPlugin());
            return;
        }
        byte buffer[] = new byte[512 * 1024];
        int nbLecture;
        try {
            while ((nbLecture = sourceFile.read(buffer)) != -1) {
                destinationFile.write(buffer, 0, nbLecture);
            }
        } catch (IOException e) {
            System.err.println("impossible d'ecrire dans le fichier temporaire\n lors de la recuperation des informations de version sur " + i.getNomPlugin());
            return;
        }
        try {
            sourceFile.close();
            destinationFile.close();
        } catch (IOException e) {
            System.err.println("impossible de fermer le fichier temporaire ou le flux reseau\n lors de la recuperation des informations de version sur " + i.getNomPlugin());
            return;
        }
        BufferedReader lecteurAvecBuffer = null;
        String ligne;
        try {
            lecteurAvecBuffer = new BufferedReader(new FileReader(destination));
        } catch (FileNotFoundException e) {
            System.err.println("impossible d'ouvrir le fichier temporaire apres sa creation (contacter un developpeur)\n lors de la recuperation des informations de version sur " + i.getNomPlugin());
            return;
        }
        try {
            boolean estLaDerniereVersion = true;
            String URLRecupererDerniereVersion = null;
            while ((ligne = lecteurAvecBuffer.readLine()) != null) {
                if (ligne.startsWith("version:")) {
                    if (ligne.equals("version:" + i.getVersionPlugin())) {
                    } else {
                        System.err.println("la version pour " + i.getNomPlugin() + " est depassee (" + i.getVersionPlugin() + " alors que la " + ligne + "est disponible)");
                        estLaDerniereVersion = false;
                    }
                }
                if (ligne.startsWith("url:")) {
                    URLRecupererDerniereVersion = ligne.substring(4, ligne.length());
                }
            }
            if (!estLaDerniereVersion && URLRecupererDerniereVersion != null) {
                TelechargerPluginEtCharger(i, URLRecupererDerniereVersion);
            } else {
                System.out.println("on est a la derniere version du plugin " + i.getNomPlugin());
            }
        } catch (IOException e) {
            System.err.println("impossible de lire le fichier temporaire apres sa creation\n lors de la recuperation des informations de version sur " + i.getNomPlugin());
            return;
        }
        try {
            lecteurAvecBuffer.close();
        } catch (IOException e) {
            return;
        }
    }

    private static void TelechargerPluginEtCharger(ControleurDeMenu i, String adresse) {
        URL url;
        try {
            url = new URL(adresse);
        } catch (MalformedURLException e1) {
            System.err.println("impossible d'ouvrir l'URL (url mal formee)" + adresse);
            return;
        }
        InputStream is;
        try {
            is = url.openStream();
        } catch (IOException e1) {
            System.err.println("impossible d'ouvrir l'URL (destination inaccessible)" + adresse);
            return;
        }
        File f = new File(InformationsSurApplicationServeur.cheminVersPlugins + java.io.File.separatorChar + i.getNomPlugin() + ".jar.new");
        if (f.exists()) {
            if (!f.delete()) {
                System.err.println("la supression du plugin de " + i.getNomPlugin() + " (extention .jar.new) obselete a echoue " + f.getAbsolutePath());
                return;
            }
        }
        try {
            if (f.createNewFile()) {
            } else {
                System.err.println("impossible de creer le fichier .jar (plugins) pour le plugin " + i.getNomPlugin());
            }
        } catch (IOException e) {
            System.err.println("impossible de creer le fichier .jar (plugins) pour le plugin " + i.getNomPlugin() + "\n :" + e.getMessage());
            return;
        }
        java.io.FileOutputStream destinationFile = null;
        try {
            destinationFile = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            System.err.println("impossible d'ouvrir le flux en ecriture pour ecrire le plugin (.jar) " + i.getNomPlugin());
            return;
        }
        byte buffer[] = new byte[512 * 1024];
        int nbLecture;
        try {
            while ((nbLecture = is.read(buffer)) != -1) {
                destinationFile.write(buffer, 0, nbLecture);
            }
        } catch (IOException e) {
            System.err.println("impossible d'ecrire dans le fichier jar plugins de " + i.getNomPlugin());
            return;
        }
        try {
            is.close();
            destinationFile.close();
        } catch (IOException e) {
            System.err.println("impossible de fermer le fichier plugin " + i.getNomPlugin());
            return;
        }
        chargerUnSimpleJar(f.getAbsolutePath());
    }

    private static void chargerUnSimpleJar(String absolutePath) {
        if (!absolutePath.endsWith(".jar")) {
            System.err.println("on veut charger un jar dont l'extention n'est pas .jar :" + absolutePath);
            return;
        }
        ChargeurLibrairie chargeurDeLib = new ChargeurLibrairie();
        chargeurDeLib.setFiles(new String[] { absolutePath });
        try {
            chargeurDeLib.chargerToutLesPlugins();
        } catch (Exception e) {
            Evenements.warning("erreur dans le chargement de la librairie " + absolutePath + ": " + e.getMessage());
        }
        ;
    }
}
