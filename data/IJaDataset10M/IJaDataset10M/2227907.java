package fr.crim.m2im.a2009.PierreMarchal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Outils {

    /**
	ChargeTexte:charge le contenu d'un fichier dans un objet String
	@param file nom du fichier à charger
	@return String le contenu du fichier 
	*/
    public static String ChargeTexte(String file) {
        String txt = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            int x = fis.available();
            byte b[] = new byte[x];
            fis.read(b);
            txt = new String(b, "UTF-8");
            fis.close();
        } catch (FileNotFoundException e) {
            Error(e, "Fichier " + file + " introuvable");
        } catch (IOException e) {
            Error(e, "Problème de lecture du fichier " + file);
        }
        return txt;
    }

    /**
	Error:gestion des messages d'erreurs
	@param e exception à l'origine de l'erreur
	@param message message d'erreur
	*/
    public static void Error(Exception e, String message) {
        if (message != null) System.out.println(message);
        System.out.println(e);
    }
}
