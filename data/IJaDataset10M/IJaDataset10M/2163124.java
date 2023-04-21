package ProcLecture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import Predicat.Objet;

public class Lect_PB {

    public Lect_PB(String _fichier, String _emplacement) {
    }

    private static File fiche;

    private static int compteur = 0;

    private static boolean com = false;

    private static Vector<Objet> v_obj = new Vector<Objet>();

    private static int obj_indices = -1;

    private static int indDebObj = 0;

    private static boolean b_obj = false;

    private static boolean b_obj1 = false;

    private static boolean b_obj2 = false;

    private static void decoder(String ligne) {
        StringTokenizer st = new StringTokenizer(ligne);
        com = false;
        while (st.hasMoreTokens() && com != true) {
            String mot = st.nextToken();
            if (mot.indexOf(";") != -1) {
                com = true;
            } else if (mot.equals(")")) {
                b_obj = false;
                b_obj1 = false;
            } else if (b_obj == true) {
                if (mot.equals("-")) {
                    b_obj1 = false;
                    b_obj2 = true;
                } else if (b_obj1 == true) {
                    obj_indices++;
                    if (indDebObj == 0) indDebObj = obj_indices;
                    Objet t = new Objet("");
                    v_obj.add(t);
                    String var = convertir_string(mot);
                    v_obj.elementAt(obj_indices).setIdent(var);
                } else if (b_obj2 == true) {
                    b_obj2 = false;
                    String nom = convertir_string(mot);
                    for (int i = indDebObj; i <= obj_indices; i++) v_obj.elementAt(i).setType(nom);
                    b_obj1 = true;
                    indDebObj = 0;
                }
            } else if (mot.indexOf(":objects") != -1) {
                b_obj = true;
                b_obj1 = true;
            }
            compteur++;
        }
    }

    private static String convertir_string(String ligne) {
        int max = ligne.length() - compteur(ligne);
        char[] mot = new char[max];
        String m = null;
        int i = 0;
        if (max != ligne.length()) {
            for (int j = 0; j < ligne.length(); j++) {
                if (ligne.charAt(j) != '(' && ligne.charAt(j) != ')' && ligne.charAt(j) != '[' && ligne.charAt(j) != ']') {
                    mot[i] = ligne.charAt(j);
                    i++;
                }
                m = new String(mot);
            }
        } else {
            m = ligne;
        }
        return m;
    }

    private static int compteur(String ligne) {
        int nb = 0;
        for (int j = 0; j < ligne.length(); j++) {
            if (ligne.charAt(j) == '(' || ligne.charAt(j) == ')' || ligne.charAt(j) == '[' || ligne.charAt(j) == ']') nb++;
        }
        return nb;
    }

    private static int convertir_nombre(String ligne) {
        int max = ligne.length() - compteur(ligne);
        char[] nombre = new char[max];
        int i = 0;
        for (int j = 0; j <= max; j++) {
            if (ligne.charAt(j) != '(' && ligne.charAt(j) != ')' && ligne.charAt(j) != '[' && ligne.charAt(j) != ']') {
                nombre[i] = ligne.charAt(j);
                i++;
            }
        }
        String nbre = new String(nombre);
        return Integer.parseInt(nbre);
    }

    public static Vector<Objet> lirefichier(File fichier) {
        fiche = fichier;
        BufferedReader lecteurAvecBuffer = null;
        String ligne;
        try {
            lecteurAvecBuffer = new BufferedReader(new FileReader(fiche));
        } catch (FileNotFoundException exc) {
            System.out.println("Erreur d'ouverture");
        }
        try {
            while ((ligne = lecteurAvecBuffer.readLine()) != null) decoder(ligne);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            lecteurAvecBuffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return v_obj;
    }
}
