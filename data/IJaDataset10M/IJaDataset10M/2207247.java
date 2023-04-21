package com.jaeksoft.searchlib.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class SynonymMap {

    private TreeMap<String, List<String>> sourceMap;

    private volatile TreeMap<String, String[]> perfMap;

    public SynonymMap(File file) throws FileNotFoundException, IOException {
        sourceMap = new TreeMap<String, List<String>>();
        perfMap = null;
        loadFromFile(file);
        updatePerfMap();
    }

    private void loadFromFile(File file) throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            String[] keyValue = line.split("[:=]", 2);
            if (keyValue == null) continue;
            if (keyValue.length != 2) continue;
            String key = keyValue[0].trim();
            String value = keyValue[1];
            String[] terms = value.split(",");
            for (String term : terms) addSourceMap(key, term.trim());
        }
        updatePerfMap();
        br.close();
        isr.close();
        fis.close();
    }

    private void updatePerfMap() {
        synchronized (sourceMap) {
            TreeMap<String, String[]> spm = new TreeMap<String, String[]>();
            Set<String> termSet = new TreeSet<String>();
            for (String key : sourceMap.keySet()) {
                List<String> termList = sourceMap.get(key);
                if (termList == null || termList.size() == 0) continue;
                termSet.clear();
                for (String term : termList) {
                    String[] words = term.split("\\s");
                    for (String word : words) termSet.add(word);
                }
                String[] termArray = new String[termSet.size()];
                termSet.toArray(termArray);
                spm.put(key, termArray);
            }
            perfMap = spm;
        }
    }

    private void addSourceMap(String key, String value) {
        if (value == null || value.length() == 0) return;
        synchronized (sourceMap) {
            List<String> termList = sourceMap.get(key);
            if (termList == null) {
                termList = new ArrayList<String>(1);
                sourceMap.put(key, termList);
            }
            termList.add(value);
        }
    }

    public String[] getSynonyms(String term) {
        return perfMap.get(term);
    }

    public int getSize() {
        return perfMap.size();
    }
}
