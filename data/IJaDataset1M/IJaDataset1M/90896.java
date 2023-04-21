package relex.frame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class ConceptVarManager {

    private static final String CONCEPT_VARS = System.getProperty("user.dir") + "/data/frame/concept_vars.txt";

    static HashMap<String, ArrayList<String>> conceptVarMap = new HashMap<String, ArrayList<String>>();

    private static final String COMMENT_DELIM = ";;";

    public static ConceptVarManager conV = new ConceptVarManager();

    public ConceptVarManager GetInstance() {
        return (conV);
    }

    private ConceptVarManager() {
        System.out.println(loadConceptVars());
    }

    public static ArrayList<String> GetConceptVarsOfType(String concept) {
        if (conceptVarMap.containsKey(concept.toLowerCase())) {
            return conceptVarMap.get(concept.toLowerCase());
        }
        return (new ArrayList<String>());
    }

    /**
	 * Determine the frame file that will be used.
	 * Concept format: atLocation ($ has removed)
	 *
	 * First try to load the the file in the directory defined by the system property frame.datapath.</li>
	 * Then try to load the file as a resource in the jar file.</li>
	 * Finally, tries the default location (equivalent to -Dframe.datapath=./data/frame)</li>
	 *
	 * @return
	 */
    private static String loadConceptVars() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(CONCEPT_VARS)));
            String msg = "";
            StringBuilder fileStr = new StringBuilder();
            try {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    int cmnt = line.indexOf(COMMENT_DELIM);
                    if (-1 < cmnt) {
                        line = line.substring(cmnt);
                    }
                    fileStr.append(line + "\n");
                }
            } catch (IOException e) {
                msg = "Error reading in Frame concept vars file - file not loaded";
                System.err.println(msg);
                System.err.println(e);
                return "\n" + msg + "\n" + e;
            } finally {
                bufferedReader.close();
            }
            HashMap<String, ArrayList<String>> newConceptVarMap = new HashMap<String, ArrayList<String>>();
            String[] varEntries = fileStr.toString().split("\\$");
            for (int i = 1; i < varEntries.length; i++) {
                String varEntry = varEntries[i];
                String[] lines = varEntry.split("\\n");
                if (lines.length == 0) {
                    continue;
                }
                String varName = lines[0].trim().toLowerCase();
                int valuesCount = lines.length - 1;
                newConceptVarMap.put(varName, new ArrayList<String>());
                for (int v = 1; v <= valuesCount; v++) {
                    String value = lines[v].trim().toLowerCase();
                    if (!value.equals("")) {
                        newConceptVarMap.get(varName).add(lines[v].trim().toLowerCase());
                    }
                }
            }
            conceptVarMap = newConceptVarMap;
            return msg + "Frame concept variables have been loaded.\n";
        } catch (Exception e) {
            String msg = "Error processing Frame concept vars file - file not loaded";
            System.err.println(msg);
            System.err.println(e);
            e.printStackTrace();
            return "\n" + msg + "\n" + e;
        }
    }
}
