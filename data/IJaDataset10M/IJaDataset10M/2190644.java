package org.iupac.goldbook.goldify.bases;

import java.util.*;
import java.util.regex.*;

public class TextMarker {

    private Map<String, String> wordToLinkMap;

    private Set<String> wordIndex;

    private Set<String> excludedWords;

    public Set<String> getExcludedWords() {
        return excludedWords;
    }

    public void setExcludedWords(Set<String> excludedWords) {
        this.excludedWords = excludedWords;
    }

    public TextMarker(Map<String, String> wordToLinkMap) {
        this(wordToLinkMap, new HashSet<String>());
    }

    public TextMarker(Map<String, String> wordToLinkMap, Set<String> excludedWords) {
        this.wordToLinkMap = wordToLinkMap;
        this.excludedWords = excludedWords;
        wordIndex = buildIndex();
    }

    public List<TextChunk> parseNext(String chunk) {
        List<TextChunk> chunkList = new ArrayList<TextChunk>();
        String[] parts = splitWords(chunk);
        int pos = 0;
        String current = "";
        String normCurrent = "";
        while (pos < parts.length) {
            current += parts[pos];
            if (pos % 2 == 1) {
                if (current.equals(parts[pos])) {
                    chunkList.add(new TextChunk(current, null));
                    current = "";
                }
                pos++;
                continue;
            }
            normCurrent = normalizeSpace(current);
            if (wordIndex.contains(normCurrent)) {
                if (pos < parts.length - 2) {
                    String normNext = normalizeSpace(current + parts[pos + 1] + parts[pos + 2]);
                    if ((wordIndex.contains(normNext)) || (wordToLinkMap.containsKey(normNext)) && !(excludedWords.contains(normNext))) {
                        pos++;
                        continue;
                    }
                }
            }
            TextChunk tch;
            if ((wordToLinkMap.containsKey(normCurrent) && !(excludedWords.contains(normCurrent)))) tch = new TextChunk(current, wordToLinkMap.get(normCurrent)); else tch = new TextChunk(current, null);
            chunkList.add(tch);
            current = "";
            pos++;
        }
        return chunkList;
    }

    private static String normalizeSpace(String current) {
        return current.replaceAll("\\s+", " ");
    }

    private static String[] splitWords(String text) {
        String[] parts = Pattern.compile("\\b").split(text);
        if (parts.length > 0 && !parts[0].matches("\\w+")) {
            ArrayList<String> pl = new ArrayList<String>(Arrays.asList(parts));
            if (pl.get(0).equals("")) pl.remove(0); else pl.add(0, "");
            pl.trimToSize();
            int i = 0;
            while (i < pl.size()) {
                if (pl.get(i).equals("-") && i > 0 && i < pl.size() - 1) {
                    String new_val = pl.get(i - 1) + pl.get(i) + pl.get(i + 1);
                    pl.set(i - 1, new_val);
                    pl.remove(i + 1);
                    pl.remove(i);
                    i -= 1;
                }
                i++;
            }
            parts = pl.toArray(new String[0]);
        }
        return parts;
    }

    private Set<String> buildIndex() {
        Set<String> index = new HashSet<String>();
        for (String key : wordToLinkMap.keySet()) {
            String part = "";
            for (String word : key.split("\\s+")) {
                part += word;
                index.add(part);
                part += " ";
            }
        }
        return index;
    }
}
