package com.loribel.commons.util;

import com.loribel.commons.util.impl.GB_HtmlTransformRandomImpl;

/**
 * Tools for HTML (format text).
 * 
 * Remplacement dans le texte de mots (ex: "VAL") par des valeurs random en fonction d'une liste 
 *   format : [|VAL|val1|val2|val3|]
 * 
 * Remplacement de ${rndI:xx} par une valeur Rnd de type int (valeur entre 1 et xx/max)
 * 
 * Ordonne une liste de mani�re random
 *   format : (|XX|val1|val2|val3|) XX = s�parateur si besoin
 *   format : (|ET|val1|val2|val3|) s�parateur sp�cial ',' et 'et'
 *   format : (|AND|val1|val2|val3|) s�parateur sp�cial ',' et 'and'
 *   format : (|OU|val1|val2|val3|) s�parateur sp�cial ',' et 'ou'
 *   format : (|OR|val1|val2|val3|) s�parateur sp�cial ',' et 'or'
 * 
 * See : GB_HtmlTransformRandomImpl
 * See Test : GB_HtmlTextRndToolsTest
 * 
 * @author Gregory Borelli
 */
public final class GB_HtmlTextRndTools {

    /**
     * Reordonne des blocs avec (|...|).
     * Remplace les valeurs random [|...|]
     *  
     * @param s � traiter
     */
    public static String doAllRandom(String s, int[] a_rnd) {
        String retour = s;
        retour = orderAllRandom(retour, a_rnd);
        retour = replaceAllRandom(retour);
        retour = replaceAllRandomI(retour);
        return retour;
    }

    /**
     * Reordonne des blocs.
     *  
     * @param s � traiter
     */
    public static String orderAllRandom(String s) {
        return orderAllRandom(s, null);
    }

    /**
     * Reordonne des blocs.
     *  
     * @param s � traiter
     */
    public static String orderAllRandom(String s, int[] a_rnd) {
        String l_start = "(|";
        String l_end = "|)";
        String[] l_blocs = GB_StringExtractTools.extractBlocs(s, l_start, l_end, true, true);
        int len = CTools.getSize(l_blocs);
        for (int i = 0; i < len; i++) {
            String l_bloc = l_blocs[i];
            s = orderBlocRandom(s, l_bloc, a_rnd);
        }
        return s;
    }

    /**
     * Reordonne des items d'un bloc
     * Le 1er item du bloc est le s�parateur si necessaire
     *  
     * @param s � traiter
     * @paran a_bloc format: (|, |val1|val2|val3|val4|)
     *                    ou (| |val1|val2|val3|val4|)
     */
    private static String orderBlocRandom(String s, String a_bloc, int[] a_rnd) {
        String l_bloc = a_bloc;
        l_bloc = STools.removeStart(l_bloc, "(|");
        l_bloc = STools.removeEnd(l_bloc, "|)");
        String[] l_blocs = GB_StringSplitTools.splitTwoBlocs(l_bloc, "|");
        String l_separator = l_blocs[0];
        l_bloc = l_blocs[1];
        String l_lastSeparator = null;
        if ((l_separator.equals("AND")) || (l_separator.equals("ET")) || (l_separator.equals("OU")) || (l_separator.equals("OR"))) {
            l_lastSeparator = " " + l_separator.toLowerCase() + " ";
            l_separator = "--AND--";
        }
        String[] l_items = GB_StringSplitTools.split(l_bloc, "|", true);
        CTools.sortByIndex(l_items, a_rnd, true);
        String l_replace = GB_StringTools.toString(l_items, l_separator);
        if (l_lastSeparator != null) {
            l_replace = STools.replaceLast(l_replace, l_separator, l_lastSeparator);
            l_replace = STools.replace(l_replace, l_separator, ", ");
        }
        String retour = STools.replaceFirst(s, a_bloc, l_replace);
        return retour;
    }

    /**
     * Remplace les valeurs avec les blocs de remplacement � m�me le texte
     */
    public static String replaceAllRandom(String s) {
        return replaceAllRandom(s, s);
    }

    /**
     * Remplace les valeurs avec les blocs de remplacement � m�me le texte
     *  
     * @param s � traiter
     * @param a_blocs Contenu de type (multiple) : [|val1|val2|val3|]
     */
    public static String replaceAllRandom(String s, String a_blocs) {
        String l_start = "[|";
        String l_end = "|]";
        String[] l_blocs = GB_StringExtractTools.extractBlocs(a_blocs, l_start, l_end, true, true);
        s = GB_StringExtractTools.removeBlocs(s, l_start, l_end);
        int len = CTools.getSize(l_blocs);
        for (int i = 0; i < len; i++) {
            String l_bloc = l_blocs[i];
            s = replaceAllRandomBloc(s, l_bloc);
        }
        return s;
    }

    private static String replaceAllRandomBloc(String s, String a_bloc) {
        String l_bloc = a_bloc;
        l_bloc = STools.remove(l_bloc, "[|");
        l_bloc = STools.remove(l_bloc, "|]");
        String[] l_words = GB_StringSplitTools.splitNotEmpty(l_bloc, "|");
        GB_HtmlTransformRandomImpl l_sa = new GB_HtmlTransformRandomImpl(l_words);
        s = l_sa.doActionStr(s);
        return s;
    }

    /**
     * Remplace les valeurs ${rndI:xx} par une valeur rnd.
     */
    public static String replaceAllRandomI(String s) {
        String retour = s;
        String l_start = "${rndI:";
        String l_end = "}";
        String[] l_blocs = GB_StringExtractTools.extractBlocs(s, l_start, l_end, false, false);
        int len = CTools.getSize(l_blocs);
        for (int i = 0; i < len; i++) {
            String l_blocI = l_blocs[i];
            int l_max = Integer.parseInt(l_blocI) - 1;
            int l_val = GB_RandomTools.randomInt(l_max);
            String l_bloc = l_start + l_blocI + l_end;
            String l_replace = "" + (l_val + 1);
            retour = STools.replaceFirst(retour, l_bloc, l_replace);
        }
        return retour;
    }

    private GB_HtmlTextRndTools() {
        super();
    }
}
