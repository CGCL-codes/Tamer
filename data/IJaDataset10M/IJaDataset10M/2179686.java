package com.yerihyo.yeritools.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.yerihyo.yeritools.collections.CountMap;

public class EnglishTFIDF {

    public static CountMap<String> getTermCountMap(Reader reader) throws IOException {
        CountMap<String> tfMap = new CountMap<String>();
        StreamTokenizer st = new StreamTokenizer(new BufferedReader(reader));
        st.resetSyntax();
        st.whitespaceChars(0x00, 0x20);
        st.wordChars(0x21, 0xFF);
        while (st.nextToken() != StreamTokenizer.TT_EOF) {
            tfMap.increCountAll(PorterStemmer.porterStem(new StringReader(st.sval.toLowerCase())));
        }
        return tfMap;
    }

    private static class TermDocumentCount {

        public TermDocumentCount(List<Map<String, Integer>> termCountMapList, Map<String, Integer> documentCountMap) {
            this.termCountMapList = termCountMapList;
            this.documentCountMap = documentCountMap;
        }

        private List<Map<String, Integer>> termCountMapList;

        private Map<String, Integer> documentCountMap;

        public List<Map<String, Integer>> getTermCountMapList() {
            return termCountMapList;
        }

        public Map<String, Integer> getDocumentCountMap() {
            return documentCountMap;
        }

        private List<Map<String, Double>> tfMapList = null;

        public List<Map<String, Double>> getTFMapList() {
            if (tfMapList == null) {
                tfMapList = new ArrayList<Map<String, Double>>();
                for (Map<String, Integer> termCountMap : termCountMapList) {
                    Map<String, Double> tfMap = CountMap.getProportionMap(termCountMap);
                    tfMapList.add(tfMap);
                }
            }
            return tfMapList;
        }

        private Map<String, Double> idfMap = null;

        public Map<String, Double> getIDFMap() {
            if (idfMap == null) {
                idfMap = EnglishTFIDF.getIDFMap(getDocumentCountMap(), termCountMapList.size());
            }
            return idfMap;
        }
    }

    public static TermDocumentCount getTermDocumentCount(String[] documentArray) {
        List<Reader> readerList = new ArrayList<Reader>();
        for (String document : documentArray) {
            readerList.add(new StringReader(document));
        }
        try {
            return getTermDocumentCount(readerList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static TermDocumentCount getTermDocumentCount(Collection<? extends Reader> readerCollection) throws IOException {
        List<Map<String, Integer>> tfMapList = new ArrayList<Map<String, Integer>>();
        CountMap<String> idfMap = new CountMap<String>();
        for (Reader reader : readerCollection) {
            CountMap<String> tfMap = getTermCountMap(reader);
            tfMapList.add(tfMap);
            for (String wordType : tfMap.keySet()) {
                idfMap.increCount(wordType);
            }
        }
        return new TermDocumentCount(tfMapList, idfMap);
    }

    public static double[] getTFIDFScore(String[] segments) {
        int documentCount = segments.length;
        double[] score = new double[documentCount];
        TermDocumentCount termDocumentCount = getTermDocumentCount(segments);
        List<Map<String, Double>> tfMapList = termDocumentCount.getTFMapList();
        Map<String, Double> idfMap = termDocumentCount.getIDFMap();
        for (int j = 0; j < documentCount; j++) {
            Map<String, Double> tfMap = tfMapList.get(j);
            double thisScore = 0;
            for (String wordType : tfMap.keySet()) {
                thisScore += tfMap.get(wordType) * idfMap.get(wordType);
            }
            score[j] = thisScore;
        }
        return score;
    }

    private static Map<String, Double> getIDFMap(Map<String, Integer> documentCountMap, int documentCount) {
        Map<String, Double> idfMap = new HashMap<String, Double>();
        for (String wordType : documentCountMap.keySet()) {
            double idfValue = Math.log10(((double) documentCount) / documentCountMap.get(wordType));
            idfMap.put(wordType, idfValue);
        }
        return idfMap;
    }

    public static void main(String[] args) throws IOException {
        String s = "123 213 jei a;;; \" ... 	\n \ra hello";
        StreamTokenizer st = new StreamTokenizer(new StringReader(s));
        st.resetSyntax();
        st.whitespaceChars(0x00, 0x20);
        st.wordChars(0x21, 0xFF);
        while (st.nextToken() != StreamTokenizer.TT_EOF) {
            System.out.println(st.sval + " | " + st.nval);
        }
    }
}
