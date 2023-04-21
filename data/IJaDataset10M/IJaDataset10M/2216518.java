package babylon;

import java.util.*;
import java.io.*;
import java.net.*;

public class babylonStringManager {

    public String language = "";

    protected Hashtable strings = null;

    public babylonStringManager(URL baseURL, String lang) {
        strings = new Hashtable();
        language = lang;
        try {
            readLanguage(baseURL, "babylon/babylonLanguage_" + language);
        } catch (FileNotFoundException e) {
            try {
                readLanguage(baseURL, "babylon/babylonLanguage_en");
            } catch (FileNotFoundException f) {
                System.out.println("Error retrieving language file.");
                f.printStackTrace();
                return;
            }
            System.out.println("No language file for language \"" + language + "\".  Using English as the default.");
        }
    }

    private void readLanguage(URL baseURL, String languageFileName) throws FileNotFoundException {
        ResourceBundle bundle = null;
        try {
            bundle = ResourceBundle.getBundle(languageFileName);
            readFromResourceBundle(bundle);
        } catch (MissingResourceException mre) {
            System.out.println("Reading language as a ResourceBundle " + "failed.  Attempting to read the file " + "manually.");
            try {
                readFromFile(new URL(baseURL.getProtocol(), baseURL.getHost(), baseURL.getFile() + languageFileName + ".properties"));
            } catch (Exception e) {
                throw (new FileNotFoundException());
            }
        }
    }

    private void readFromResourceBundle(ResourceBundle bundle) throws MissingResourceException {
        Enumeration keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String value = bundle.getString(key);
            if (strings.containsKey(key)) System.out.println("Duplicate language key: " + key + " oldvalue: " + strings.get(key) + " newvalue: " + value); else strings.put(key, value);
        }
    }

    private void readFromFile(URL fileURL) throws FileNotFoundException, IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(fileURL.openStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            StringTokenizer tokens = new StringTokenizer(inputLine);
            if (tokens.countTokens() < 2) continue;
            String key = tokens.nextToken();
            if (key.startsWith("#")) continue;
            if (!tokens.nextToken().equals("=")) continue;
            String value = "";
            while (tokens.hasMoreTokens()) value += tokens.nextToken(" ") + " ";
            if (strings.containsKey(key)) System.out.println("Duplicate language key: " + key + " oldvalue: " + strings.get(key) + " newvalue: " + value); else strings.put(key, value);
        }
        in.close();
    }

    public String getTranslator() {
        return ((String) strings.get("translator"));
    }

    public String get(String key) {
        String fullKey = "global." + key;
        String returnString = (String) strings.get(fullKey);
        if (returnString == null) {
            System.out.println("Language key " + fullKey + " not found");
            return ("");
        }
        return (returnString);
    }

    public String get(Class callerClass, String key) {
        String fullKey = callerClass.getName() + "." + key;
        String returnString = (String) strings.get(fullKey);
        if (returnString == null) {
            System.out.println("Language key " + fullKey + " not found");
            return ("");
        }
        return (returnString);
    }
}
