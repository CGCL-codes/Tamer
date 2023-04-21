package org.fudaa.ctulu;

import com.memoire.fu.FuLog;

/**
 * Classe gerant les messages : mode deboguage et mode info.
 *
 * @author Fred Deniger
 * @version $Id: CtuluLibMessage.java,v 1.10 2006-07-13 13:34:36 deniger Exp $
 */
public final class CtuluLibMessage {

    /**
   * Variable de d�boguage. Activ�e initialement avec la variable globale DEBUG "java -DDEBUG=TRUE"
   */
    public static final boolean DEBUG = "TRUE".equals(System.getProperty("DEBUG"));

    /**
   * True si les infos utilisateurs doivent etre affichees.
   */
    public static final boolean INFO = DEBUG || "TRUE".equals(System.getProperty("INFO"));

    private CtuluLibMessage() {
        super();
    }

    /**
   * Ecrit sur la sortie standart DEBUG+_s.
   *
   * @param _mess la chaine a ecrire
   */
    public static void debug(final String _mess) {
        FuLog.debug(_mess);
    }

    /**
   * Ecrit sur la sortie d'erreur ERROR+_s.
   *
   * @param _mess la chaine a ecrire
   */
    public static void error(final String _mess) {
        FuLog.error(_mess);
    }

    /**
   * @param _mess la chaine d'info a ecrire en sortie standard
   */
    public static void info(final String _mess) {
        FuLog.all(" --> " + _mess);
    }

    /**
   * Affiche une chaine suivi d'un retour chariot sur la sortie standard si la propri�t� DEBUG est vrai.
   *
   * @param _message La chaine � afficher.
   */
    public static void println(final String _message) {
        if (DEBUG) {
            System.out.println(_message);
        }
    }

    /**
   * Affiche une chaine sur la sortie standard si la propri�t� DEBUG est vrai.
   *
   * @param _message La chaine � afficher.
   */
    public static void print(final String _message) {
        if (DEBUG) {
            System.out.print(_message);
        }
    }

    /**
   * Affiche un tableau d'objets sur la sortie standard si la propri�t� DEBUG est vrai.
   *
   * @param _nomTab Le nom du tableau.
   * @param _tab Le tableau.
   */
    public static void println(final String _nomTab, final Object[] _tab) {
        if (DEBUG) {
            System.out.print(_nomTab + CtuluLibString.arrayToString(_tab));
        }
    }

    /**
   * Affiche un tableau d'entiers sur la sortie standard si la propri�t� DEBUG est vrai.
   *
   * @param _nomTab Le nom du tableau.
   * @param _tab Le tableau.
   */
    public static void println(final String _nomTab, final int[] _tab) {
        if (DEBUG) {
            System.out.println(_nomTab + CtuluLibString.arrayToString(_tab));
        }
    }

    /**
   * Affiche un tableau de doubles sur la sortie standard si la propri�t� DEBUG est vrai.
   *
   * @param _nomTab Le nom du tableau.
   * @param _tab Le tableau.
   */
    public static void println(final String _nomTab, final double[] _tab) {
        if (DEBUG) {
            System.out.print(_nomTab + CtuluLibString.arrayToString(_tab));
        }
    }

    /**
   * Affiche un tableau de floats sur la sortie standard si la propri�t� DEBUG est vrai.
   *
   * @param _nomTab Le nom du tableau.
   * @param _tab Le tableau.
   */
    public static void println(final String _nomTab, final float[] _tab) {
        if (DEBUG) {
            System.out.print(_nomTab + CtuluLibString.arrayToString(_tab));
        }
    }

    /**
   * Affiche un tableau 2D de float sur la sortie standard si la propri�t� DEBUG est vrai.
   *
   * @param _nomTab Le nom du tableau.
   * @param _tab Le tableau 2D.
   */
    public static void println(final String _nomTab, final float[][] _tab) {
        if (DEBUG) {
            if (_tab.length > 0) {
                for (int i = 0; i < _tab.length; i++) {
                    println(_nomTab + "[" + i + "]", _tab[i]);
                }
            }
        }
    }

    /**
   * Affiche un tableau 2D de double sur la sortie standard si la propri�t� DEBUG est vrai.
   *
   * @param _nomTab Le nom du tableau.
   * @param _tab Le tableau 2D.
   */
    public static void println(final String _nomTab, final double[][] _tab) {
        if (DEBUG) {
            if (_tab.length > 0) {
                for (int i = 0; i < _tab.length; i++) {
                    println(_nomTab + "[" + i + "]", _tab[i]);
                }
            }
        }
    }

    /**
   * Affiche un tableau 3D de double sur la sortie standard si la propri�t� DEBUG est vrai.
   *
   * @param _nomTab Le nom du tableau.
   * @param _tab Le tableau 3D.
   */
    public static void println(final String _nomTab, final double[][][] _tab) {
        if (DEBUG) {
            if (_tab.length > 0) {
                for (int i = 0; i < _tab.length; i++) {
                    println(_nomTab + "[" + i + "]", _tab[i]);
                }
            }
        }
    }
}
