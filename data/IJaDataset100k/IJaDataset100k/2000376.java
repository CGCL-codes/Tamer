package org.donnchadh.gaelbot.analysis;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import com.csvreader.CsvReader;

public class CommonWordAnalyzer {

    public static void main(String[] args) throws IOException {
        Set<String> gaWords = new HashSet<String>();
        Set<String> enWords = new HashSet<String>();
        Set<String> xWords = new HashSet<String>();
        readCommonWords(gaWords, enWords, xWords);
        Set<String> commonWords = new HashSet<String>(gaWords);
        commonWords.retainAll(enWords);
        System.out.println(commonWords);
        Set<String> commonEnXWords = new HashSet<String>(xWords);
        commonEnXWords.retainAll(enWords);
        Set<String> commonGaXWords = new HashSet<String>(xWords);
        commonGaXWords.retainAll(gaWords);
        Set<String> commonXWords = new HashSet<String>(commonEnXWords);
        commonXWords.addAll(commonGaXWords);
        System.out.println(commonXWords);
    }

    protected static void readCommonWords(Set<String> gaWords, Set<String> enWords, Set<String> xWords) throws IOException {
        try {
            CsvReader csvReader = new CsvReader(new FileInputStream("commonWords.csv"), Charset.forName("UTF-8"));
            csvReader.readHeaders();
            String[] header = csvReader.getHeaders();
            while (csvReader.readRecord()) {
                String[] values = csvReader.getValues();
                if (values[1].equalsIgnoreCase("ga")) {
                    if (gaWords.contains(values[1])) {
                        System.out.println(values[1]);
                    } else {
                        gaWords.add(values[0]);
                    }
                } else if (values[1].equalsIgnoreCase("en")) {
                    if (enWords.contains(values[1])) {
                        System.out.println(values[1]);
                    } else {
                        enWords.add(values[0]);
                    }
                } else {
                    if (xWords.contains(values[1])) {
                        System.out.println(values[1]);
                    } else {
                        xWords.add(values[0]);
                    }
                }
            }
        } catch (IOException e) {
            throw e;
        }
    }
}
