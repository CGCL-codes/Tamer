package statechum.analysis.learning;

import java.io.*;
import java.util.*;
import statechum.Configuration;
import statechum.analysis.learning.Visualiser.VIZ_ENV_PROPERTIES;

public class QSMTool {

    private static boolean includeLTL = false;

    private static int k = -1;

    private static boolean textoutput = false;

    private static boolean dotoutput = false;

    public static void main(String[] args) {
        Set<List<String>> sPlus = new HashSet<List<String>>();
        Set<List<String>> sMinus = new HashSet<List<String>>();
        Set<String> ltl = new HashSet<String>();
        boolean active = true;
        try {
            BufferedReader in = new BufferedReader(new FileReader(args[0]));
            String fileString;
            String activePassive = in.readLine();
            if (activePassive.trim().equalsIgnoreCase("passive")) active = false;
            while ((fileString = in.readLine()) != null) {
                process(fileString, sPlus, sMinus, ltl);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Configuration config = Configuration.getDefaultConfiguration();
        String AutoName = System.getProperty(VIZ_ENV_PROPERTIES.VIZ_AUTOFILENAME.toString());
        if (AutoName != null) config.setAutoAnswerFileName(AutoName);
        if (textoutput) config.setGenerateTextOutput(true);
        if (dotoutput) config.setGenerateDotOutput(true);
        PickNegativesVisualiser.setSimpleConfiguration(config, active, k);
        PickNegativesVisualiser pnv = new PickNegativesVisualiser();
        if (!includeLTL) pnv.construct(sPlus, sMinus, null, config); else pnv.construct(sPlus, sMinus, ltl, config);
        pnv.startLearner(null);
    }

    private static void process(String fileString, Set<List<String>> sPlus, Set<List<String>> sMinus, Set<String> ltl) {
        if (fileString.trim().equalsIgnoreCase("")) return;
        StringTokenizer tokenizer = new StringTokenizer(fileString.substring(1));
        ArrayList<String> sequence = new ArrayList<String>();
        while (tokenizer.hasMoreTokens()) sequence.add(tokenizer.nextToken());
        if (fileString.startsWith("+")) sPlus.add(sequence); else if (fileString.startsWith("-")) sMinus.add(sequence); else if (fileString.startsWith("ltl")) {
            includeLTL = true;
            if (fileString.substring(4).trim().length() > 0) ltl.add(getLtlString(sequence));
        } else if (fileString.startsWith("k")) {
            String value = fileString.substring(1).trim();
            k = Integer.valueOf(value);
        } else if (fileString.startsWith("textoutput")) textoutput = true; else if (fileString.startsWith("dotoutput")) dotoutput = true;
    }

    private static String getLtlString(List<String> sequence) {
        String expression = new String();
        for (int i = 1; i < sequence.size(); i++) {
            expression = expression.concat(sequence.get(i));
        }
        return expression;
    }
}
