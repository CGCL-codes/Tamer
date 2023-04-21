package netgest.bo.xwc.framework.localization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateLocalizationFiles {

    static PrintWriter outCsvWriter;

    static File rootDir = new File("C:\\projects_eclipse\\xeo\\xeo_v3_xwc\\src\\");

    public static void main(String[] args) throws Exception {
        loadCsv(new File("c:\\lixo\\local.csv"), "en");
    }

    public static void createCsv() throws Exception {
        List<File> scanFiles = new ArrayList<File>();
        File outCsv = new File("c:\\lixo\\local.txt");
        FileWriter outCsvW = new FileWriter(outCsv);
        outCsvWriter = new PrintWriter(outCsvW);
        scanFiles(rootDir, scanFiles);
        createCsv(scanFiles);
        outCsvWriter.close();
        outCsvW.close();
    }

    public static void loadCsv(File csvFile, String lang) throws Exception {
        HashMap<String, Boolean> filesMap = new HashMap<String, Boolean>();
        FileReader fr = new FileReader(csvFile);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            String[] lineSplit = line.split(";");
            if (lineSplit.length == 3) {
                String bundle = lineSplit[0].replaceAll("\"", "");
                String key = lineSplit[1].replaceAll("\"", "");
                ;
                String value = lineSplit[2].replaceAll("\"", "");
                ;
                String fileName = bundle.substring(0, bundle.lastIndexOf('.'));
                fileName += "_" + lang + ".properties";
                String absolutePath = rootDir.getAbsolutePath() + fileName;
                FileWriter fw = new FileWriter(absolutePath, filesMap.containsKey(absolutePath));
                PrintWriter pw = new PrintWriter(fw);
                pw.println(key + " = " + value);
                pw.close();
                fw.close();
                filesMap.put(absolutePath, Boolean.TRUE);
            }
        }
        br.close();
        fr.close();
    }

    public static void createCsv(List<File> files) throws Exception {
        for (File file : files) {
            createCsvFromFil(file);
        }
    }

    public static void createCsvFromFil(File file) throws Exception {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String bundle = file.getAbsolutePath().substring(rootDir.getAbsolutePath().length());
        String line;
        while ((line = br.readLine()) != null) {
            String[] lineSplit = line.split("=");
            if (lineSplit.length == 2) {
                outCsvWriter.println(bundle + "|" + lineSplit[0] + "|" + lineSplit[1]);
            }
        }
        br.close();
        fr.close();
    }

    public static void scanFiles(File rootDir, List<File> foundFiles) {
        File[] files = rootDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                scanFiles(file, foundFiles);
            } else if (file.getName().endsWith(".properties")) {
                foundFiles.add(file);
            }
        }
    }
}
