package com.um2.simplexe.serveur.ui.util.controleUI;

import java.util.Vector;
import com.um2.simplexe.serveur.ui.util.controleUI.chargeur.menu.ControleurDeMenu;
import com.um2.simplexe.serveur.ui.util.controleUI.chargeur.menu.ControleurDePanneaux;

/**
 * Cette classe equivaut a un fichier .ini
 * elle regroupe les informations sur l'application
 * 
 * on gardera donc par r�f�rence a travers cette classe:
 * - les contr�leurs,
 * - une r�f�rence sur la fenetre,
 * - l'adresse ip par defaut,...
 * 
 * on peut imaginer par le suite faire
 * une m�thode qui r�cup�rerai ces donn�es depuis
 * un fichier .ini ou .properties .
 * 
 * 03-02-2007
 * 
 * @author jean-marie codol
 *
 */
public abstract class InformationsSurApplication {

    /**
	 * Pouvoir acc�der a travers des m�thodes statiques,
	 * a contenu de cette classe,
	 * en cr�ant une instance unique a chaque instant.
	 */
    private static InformationsSurApplication instance;

    /**
	 * le repertoire ou aller chercher les plugins (gr�ffons)
	 */
    public static String cheminVersPlugins;

    /**
	 * le repertoire ou aller chercher les librairies partag�es
	 */
    public static String cheminVersLibrairies;

    /**
	 * le nom de l'interface,
	 * que la classe principale du plugin doit implementer.
	 * pour controler les menus.
	 */
    public static String INTERFACE_CONTROLE_MENU;

    /**
	 * le nom de l'interface,
	 * que la classe principale du plugin doit implementer.
	 * pour controler les panneaux.
	 */
    public static String INTERFACE_CONTROLE_PANNEAU;

    /**
	 * a chaque instant permet d'avoir la liste des librairies partagées chargées.
	 */
    public static Vector<String> listePluginsChargeurDeLibrairiesPartagees = new Vector<String>();

    /**
	 * a chaque instant permet d'avoir la liste des plugins de menus chargés.
	 */
    public static Vector<ControleurDeMenu> listePluginsChargeurDeMenu = new Vector<ControleurDeMenu>();

    /**
	 * a chaque instant permet d'avoir la liste des des plugins de panneaux chargés.
	 */
    public static Vector<ControleurDePanneaux> listePluginsChargeurDePanneaux = new Vector<ControleurDePanneaux>();

    /**
	 * recupere l'instance
	 * @return
	 */
    public static InformationsSurApplication getInstance() {
        return instance;
    }
}
