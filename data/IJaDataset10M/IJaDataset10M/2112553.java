package fr.crim.m2im.a2009.wakako;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Différentes méthodes d'usage général
 * 
 * @author Frédéric Glorieux
 */
public class Util {

    /** Encodage par défaut pour différentes opérations */
    static String encoding = "UTF-8";

    /** Expression réguière pour spliter les mots d'un texte. (apostrophe ?) */
    static String split = "[ ,.;:?!\"«»§{}()\\[\\]\\n’'…—]+";

    /** */
    static Locale locale = Locale.FRANCE;

    public static String[] arrayIntersectPM(String[] a, String[] b) {
        int i, j;
        String tmp = "";
        for (i = 0; i < a.length; i++) for (j = 0; j < b.length; j++) if (a[i].equals(b[j])) tmp += a[i] + ",";
        return (tmp.split(","));
    }

    public static String[] arrayIntersect(String[] a, String[] b) {
        String MotCommun[] = new String[a.length];
        int k = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b.length; j++) {
                if (a[i].equals(b[j])) MotCommun[k] = a[i];
            }
        }
        return MotCommun;
    }

    /**
	 * Trier un map sur la valeur
	 * 
	 * @param map
	 *            Une table <clé,valeur>
	 * @return une liste de Fréquence
	 */
    public static List sortByValue(Map map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {

            public int compare(Object o1, Object o2) {
                int ret = -((Comparable) ((Map.Entry) o1).getValue()).compareTo(((Map.Entry) o2).getValue());
                if (ret == 0) {
                    ret = (((Comparable) ((Map.Entry) o1).getKey())).compareTo(((Map.Entry) o2).getKey());
                }
                return ret;
            }
        });
        return list;
    }

    /**
	 * Liste de fréquence à partir d'un tableau de mots. Utilise un objet de
	 * type "dictionnaire" (Map, clé:valeur). Ressemble à l'effet des commandes
	 * shell {tableau de mots à 1 par ligne} | sort | uniq -c
	 * 
	 * @param texte
	 *            Un tableau de mots avec doublons
	 * @return une liste de Fréquence, mots unique avec effectif
	 */
    public static HashMap<String, String> effectifs(String[] texte) {
        HashMap<String, String> liste = new HashMap<String, String>();
        for (String cle : texte) {
            String valeur = liste.get(cle);
            if (valeur == null) liste.put(cle, "1"); else liste.put(cle, "" + (new Integer(valeur) + 1));
        }
        return liste;
    }

    /**
	 * Intersection de 2 tableaux de chaînes
	 * 
	 * @param a
	 *            tableau 1
	 * @param b
	 *            tableau 2
	 * @return intersection
	 */
    public static String[] arrayIntersectFG1(String[] a, String[] b) {
        String[] inter = new String[((a.length > b.length) ? a.length : b.length)];
        int count = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b.length; j++) {
                if (a[i].equals(b[j])) {
                    inter[count] = a[i];
                    count++;
                    break;
                }
            }
        }
        String[] ret = new String[count];
        System.arraycopy(inter, 0, ret, 0, count);
        return ret;
    }

    /**
	 * Autre algorithme de comparaison de tableau, utilisant la fonction
	 * "binarySearch" de l'objet Arrays. En théorie, cette méthode pourrait être
	 * plus performante qu'une double boucle (ixj test), puisqu'elle ne demande
	 * qu'une boucle. En pratique, après tests, elle est plus performante dans
	 * le cas où les tableaux a et b sont de tailles comparables.
	 * 
	 * @param a
	 *            tableau 1
	 * @param b
	 *            tableau 2
	 * @return intersection
	 */
    public static String[] arrayIntersectFG2(String[] a, String[] b) {
        StringBuilder sb = new StringBuilder();
        String sep = "¤§¤";
        sb.append(sep);
        Arrays.sort(b);
        String[] texte;
        String[] voc;
        if (a.length > b.length) {
            texte = a;
            voc = b;
        } else {
            texte = b;
            voc = a;
        }
        Arrays.sort(texte);
        String key;
        for (int i = 0; i < voc.length; i++) {
            key = voc[i];
            if (sb.indexOf(sep + key + sep) > -1) continue;
            if (Arrays.binarySearch(texte, key) >= 0) sb.append(key + sep);
        }
        sb.delete(0, sep.length());
        return sb.toString().split(sep);
    }

    /**
	 * Chargement d'un fichier texte dans une chaîne, avec gestion des
	 * exceptions
	 * 
	 * @param file
	 *            Chemin fichier en objet fichier
	 * @param enc
	 *            Encodage du fichier
	 * @return Le contenu du fichier dans une chaîne
	 */
    public static String charger(File file, String enc) {
        if (enc == null) enc = "UTF-8";
        String texte = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            int x = fis.available();
            byte b[] = new byte[x];
            fis.read(b);
            texte = new String(b, enc);
            fis.close();
        } catch (FileNotFoundException e) {
            error(e, "Fichier " + file + " introuvable");
        } catch (IOException e) {
            error(e, "Problème de lecture du fichier " + file);
        }
        return texte;
    }

    /**
	 * Centralisation d'une méthode pour les messages d'erreurs, permettra de
	 * modifier les comportements, quelle que soit l'erreur.
	 * 
	 * @param e
	 *            Exception à l'origine de l'erreur
	 * @param message
	 *            Message de l'utilisateur
	 */
    public static void error(Exception e, String message) {
        if (message != null) System.out.println(message);
        System.out.println(e);
    }

    /**
	 * Translate, voir XSLT
	 * 
	 * @param s
	 *            chaîne à "traduire"
	 * @param from
	 *            Liste de caractère en entrée
	 * @param to
	 *            Liste de caractères en sortie
	 * @return La chaîne "traduite"
	 */
    public static String tr(String s, String from, String to) {
        StringBuilder sb = null;
        int sLen = s.length();
        int toLen = to.length();
        for (int i = 0; i < sLen; i++) {
            char c = s.charAt(i);
            int j = from.indexOf(c);
            if (j < toLen) {
                sb.append((j < 0 ? c : to.charAt(j)));
            }
        }
        return sb.toString();
    }

    /**
	 * TriComptage : ce programme trie par ordre de longeur les chaines de
	 * caractères passées en argument. l'algorithme est très largement inspiré
	 * de celui du tri par comptage.
	 * 
	 * @param a :
	 *            un ensemble de chaines de caractères
	 */
    public static void sortPaniers(String[] a) {
        if (a == null || a.length < 1) return;
        final int x = a.length;
        int min = 0;
        int max = 0;
        String paniers[][];
        for (int i = 0; i < x; i++) {
            if (a[i].length() > max) max = a[i].length();
            if (a[i].length() < min) min = a[i].length();
        }
        final int y = max - min + 1;
        paniers = new String[x][y];
        for (int i = 0; i < x; i++) {
            if (a[i].length() > max) max = a[i].length();
            if (a[i].length() < min) min = a[i].length();
        }
        int x1, y1;
        for (int i = 0; i < x; i++) {
            x1 = 0;
            y1 = a[i].length() - min;
            while (paniers[x1][y1] != null) x1++;
            paniers[x1][y1] = a[i];
        }
        for (y1 = 0; y1 < y; y1++) {
            x1 = 0;
            while (paniers[x1][y1] != null) {
                System.out.println(paniers[x1][y1]);
                x1++;
            }
        }
    }

    /**
	 * Proposition de tri utilisant un algorithme existant de tri de tableau sur
	 * l'ordre lexicographique des chaînes. Le truc consiste à préfixer une
	 * chaîne avec le nombre de sa taille en notation positionnelle sur 5
	 * chiffres.
	 * 
	 * Cet algorithme est encore couteux, car le comparateur de chaîne est plus
	 * long qu'une comparaison de int, mais cet exemple permet d'illustrer
	 * certaines pratiques de traitement de chaînes avec substring.
	 * 
	 * http://java.sun.com/j2se/1.5.0/docs/api/java/util/Arrays.html#sort(java.lang.Object[])
	 * 
	 * @param a :
	 *            un tableau de chaînes
	 * @return tableau de chaines rangé en ordre de taille
	 */
    public static String[] sortSize(String[] a) {
        final String format = "00000";
        String nb;
        for (int i = 0; i < a.length; i++) {
            nb = "" + a[i].length();
            a[i] = format.substring(0, format.length() - nb.length()) + nb + "_" + a[i];
        }
        java.util.Arrays.sort(a);
        for (int i = 0; i < a.length; i++) {
            a[i] = a[i].substring(a[i].indexOf("_") + 1);
        }
        return a;
    }

    /**
	 * Comparateur pour liste de fréquences
	 * 
	 * @author Frédéric Glorieux
	 * 
	 */
    class MapValueComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            int ret = ((Comparable) ((Map.Entry) o1).getValue()).compareTo(((Map.Entry) o2).getValue());
            if (ret == 0) {
                ret = (((Comparable) ((Map.Entry) o1).getKey())).compareTo(((Map.Entry) o2).getKey());
            }
            return ret;
        }
    }
}
