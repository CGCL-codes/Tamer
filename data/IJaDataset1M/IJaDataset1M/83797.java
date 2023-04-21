package net.sourceforge.freejava.l10n.en;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import net.sourceforge.freejava.l10n.en.IrregularPlural.Handler;

public class English {

    private static Map<String, String> pastTense;

    private static Map<String, String> perfectTense;

    static {
        pastTense = new HashMap<String, String>();
        perfectTense = new HashMap<String, String>();
        IrregularPlural.process(Locale.ENGLISH, new Handler() {

            @Override
            public void handle(String name, String value) {
                int eq = value.indexOf('=');
                String past = value;
                String perfect;
                if (eq == -1) perfect = past; else {
                    perfect = value.substring(eq + 1);
                    past = value.substring(0, eq);
                }
                pastTense.put(name, past);
                perfectTense.put(name, perfect);
            }
        });
    }

    private static final Pattern P_Ce;

    private static final Pattern P_VVC;

    private static final Pattern P_CVC;

    private static final Pattern P_Cy;

    private static final Pattern P_aiouxs;

    static {
        P_Ce = Pattern.compile("[^aeiou]e$", Pattern.CASE_INSENSITIVE);
        P_VVC = Pattern.compile("[aeiou][aeiou][^aeiou]$", Pattern.CASE_INSENSITIVE);
        P_CVC = Pattern.compile("[^aeiou][aeiou][^aeiou]$", Pattern.CASE_INSENSITIVE);
        P_Cy = Pattern.compile("[^aeiou]y$", Pattern.CASE_INSENSITIVE);
        P_aiouxs = Pattern.compile("[xsaiou]$", Pattern.CASE_INSENSITIVE);
    }

    private static boolean matches(String word, Pattern pattern) {
        return pattern.matcher(word).find();
    }

    /**
     * <pre>
     * [C]y      -&gt; *ies     fly *ies
     * [xsaiou]  -&gt; &tilde;es      fax -es, go -es
     * [else]    -&gt; &tilde;s       dog -s
     * </pre>
     */
    public static String pluralOf(String word) {
        int len = word.length();
        if (matches(word, P_Cy)) return word.substring(0, len - 1) + "ies";
        if (matches(word, P_aiouxs)) return word + "es";
        return word + "s";
    }

    /**
     * <pre>
     * ie    -&gt; &tilde;ying    d(ie) -ying
     * [C]e  -&gt; &tilde;ing     lov(e) -ing
     * [VVC] -&gt; &tilde;ing     eat -ing
     * [CVC] -&gt; &tilde;$ing    program -ming
     * (else)-&gt; &tilde;ing     act -ing
     * </pre>
     */
    public static String presentOf(String word) {
        int len = word.length();
        if (word.endsWith("ie")) return word.substring(0, len - 2) + "ying";
        if (matches(word, P_Ce)) return word.substring(0, len - 1) + "ing";
        if (matches(word, P_VVC)) return word + "ing";
        if (matches(word, P_CVC)) return word + word.charAt(len - 1) + "ing";
        return word + "ing";
    }

    /**
     * <pre>
     * [V]e  -&gt; &tilde;d       die -d
     * [C]e  -&gt; &tilde;d       love -d
     * [VVC] -&gt; &tilde;ed      eat -ed
     * [CVC] -&gt; &tilde;$ed     program -med
     * [else]-&gt; &tilde;ed      act -ed
     * </pre>
     */
    private static String _pastOf(String word) {
        int len = word.length();
        if (word.endsWith("e")) return word + "d";
        if (matches(word, P_VVC)) return word + "ed";
        if (matches(word, P_CVC)) return word + word.charAt(len - 1) + "ed";
        return word + "ed";
    }

    public static String pastOf(String word) {
        String past = pastTense.get(word);
        return past == null ? _pastOf(word) : past;
    }

    public static String perfectOf(String word) {
        String perfect = perfectTense.get(word);
        return perfect == null ? _pastOf(word) : perfect;
    }
}
