package Utilitaire;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class LecteurIni {

    @SuppressWarnings("unchecked")
    private HashMap _sections;

    /** 
   * Creates a new ProfileReader 
   */
    @SuppressWarnings("unchecked")
    public LecteurIni() {
        _sections = new HashMap();
    }

    /** 
   * Load the current objet with the data found in the given stream 
   * @param aStream the stream that represent the INI file. 
   * @throws Exception in case of problems. 
   */
    @SuppressWarnings("unchecked")
    public void load(InputStream aStream) throws Exception {
        if (null == aStream) {
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(aStream));
        String line = null;
        String sectionName = null;
        HashMap section = null;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (null == sectionName) {
                if (((!line.startsWith("[")) || (!line.endsWith("]"))) && (!line.startsWith(";"))) {
                    throw new Exception("Invalid format: data found outside section");
                }
                sectionName = line.substring(1, line.length() - 1).trim();
                addSection(sectionName);
                section = getSection(sectionName);
            } else {
                if (line.startsWith("[")) {
                    if (!line.endsWith("]")) {
                        throw new Exception("Invalid format: no ending ] for section name");
                    }
                    sectionName = line.substring(1, line.length() - 1).trim();
                    addSection(sectionName);
                    section = getSection(sectionName);
                } else {
                    addLineToSection(line, section);
                }
            }
        }
        reader.close();
    }

    /** 
   * Return the value of the given key in the given section 
   * @param aSectionName the name of the section 
   * @param aKey the key 
   * @return the value if found or null. 
   */
    @SuppressWarnings("unchecked")
    public String getProperty(String aSectionName, String aKey) {
        HashMap section = getSection(aSectionName);
        if (null == section) {
            return null;
        }
        return (String) section.get(aKey);
    }

    @SuppressWarnings("unchecked")
    private void addLineToSection(String aLine, HashMap aSection) throws Exception {
        if (null == aLine) {
            return;
        }
        if (null == aSection) {
            throw new Exception("No section found to add data");
        }
        aLine = aLine.trim();
        if (aLine.startsWith(";")) {
            return;
        }
        if (aLine.length() == 0) {
            return;
        }
        StringTokenizer st = new StringTokenizer(aLine, "=");
        if (st.countTokens() != 2) {
            throw new Exception("Invalid format of data: " + aLine);
        }
        String key = st.nextToken().trim();
        for (int index = 0; index < key.length(); index++) {
            if (Character.isWhitespace(key.charAt(index))) {
                throw new Exception("Invalid format of data: " + aLine);
            }
        }
        String value = st.nextToken().trim();
        aSection.put(key, value);
    }

    @SuppressWarnings("unchecked")
    private void addSection(String aSectionName) {
        if (null == aSectionName) {
            return;
        }
        HashMap section = getSection(aSectionName);
        if (null == section) {
            section = new HashMap();
            _sections.put(aSectionName, section);
        }
    }

    @SuppressWarnings("unchecked")
    private HashMap getSection(String aSectionName) {
        return (HashMap) _sections.get(aSectionName);
    }
}
