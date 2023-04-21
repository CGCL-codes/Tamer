package net.sourceforge.iwii.project.stat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author Gregor736
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Main main = new Main();
        main.run();
    }

    private void run() throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream("conf.properties"));
        String extList = prop.getProperty("project.conf.extentions");
        String bannedDirs = prop.getProperty("project.conf.bannedDirs");
        List<String> extNames = new LinkedList<String>();
        List<String> bannedDirsNames = new LinkedList<String>();
        StringTokenizer tokenizer = new StringTokenizer(extList, ";");
        while (tokenizer.hasMoreTokens()) {
            extNames.add(tokenizer.nextToken());
        }
        tokenizer = new StringTokenizer(bannedDirs, ";");
        while (tokenizer.hasMoreTokens()) {
            bannedDirsNames.add(tokenizer.nextToken());
        }
        long start = System.currentTimeMillis();
        List<StatisticEntry> result = this.processDirectory(new File("."), extNames, bannedDirsNames);
        long end = System.currentTimeMillis();
        System.out.println("====================================================================");
        System.out.println("Date: " + new Date());
        System.out.println("Completed in: " + String.valueOf(end - start) + " [ms]");
        System.out.println("Found: ");
        StatisticEntry global = new StatisticEntry("global");
        System.out.print("+===============+");
        for (StatisticEntry entry : result) {
            System.out.print("============+");
        }
        System.out.println();
        System.out.print("|Extentions     |");
        for (StatisticEntry entry : result) {
            StringBuilder extention = new StringBuilder(" " + entry.getExtention());
            while (extention.length() < 12) {
                extention.append(" ");
            }
            System.out.print(extention.toString() + "|");
        }
        System.out.println();
        System.out.print("+---------------+");
        for (StatisticEntry entry : result) {
            System.out.print("------------+");
        }
        System.out.println();
        System.out.print("|Lines          |");
        for (StatisticEntry entry : result) {
            global.addLines(entry.getLines());
            StringBuilder value = new StringBuilder(" " + entry.getLines());
            while (value.length() < 12) {
                value.append(" ");
            }
            System.out.print(value.toString() + "|");
        }
        System.out.println();
        System.out.print("|Not empty lines|");
        for (StatisticEntry entry : result) {
            global.addNotEmptyLines(entry.getNotEmptyLines());
            StringBuilder value = new StringBuilder(" " + entry.getNotEmptyLines());
            while (value.length() < 12) {
                value.append(" ");
            }
            System.out.print(value.toString() + "|");
        }
        System.out.println();
        System.out.print("|Words          |");
        for (StatisticEntry entry : result) {
            global.addWords(entry.getWords());
            StringBuilder value = new StringBuilder(" " + entry.getWords());
            while (value.length() < 12) {
                value.append(" ");
            }
            System.out.print(value.toString() + "|");
        }
        System.out.println();
        System.out.print("|Chars          |");
        for (StatisticEntry entry : result) {
            global.addChars(entry.getChars());
            StringBuilder value = new StringBuilder(" " + entry.getChars());
            while (value.length() < 12) {
                value.append(" ");
            }
            System.out.print(value.toString() + "|");
        }
        System.out.println();
        System.out.print("|Files          |");
        for (StatisticEntry entry : result) {
            global.addInstances(entry.getInstances());
            StringBuilder value = new StringBuilder(" " + entry.getInstances());
            while (value.length() < 12) {
                value.append(" ");
            }
            System.out.print(value.toString() + "|");
        }
        System.out.println();
        System.out.print("+===============+");
        for (StatisticEntry entry : result) {
            System.out.print("============+");
        }
        System.out.println();
        System.out.println();
        System.out.println("Total lines:             " + global.getLines());
        System.out.println("Total not empty lines:   " + global.getNotEmptyLines());
        System.out.println("Total words:             " + global.getWords());
        System.out.println("Total chars:             " + global.getChars());
        System.out.println("Total files:             " + global.getInstances());
        System.out.println("====================================================================");
    }

    public List<StatisticEntry> processDirectory(File directory, List<String> extNames, List<String> bannedDirs) throws FileNotFoundException {
        if (!directory.exists() || !directory.isDirectory()) {
            return new LinkedList<StatisticEntry>();
        }
        System.out.println("Processing directory: " + directory.getAbsolutePath() + "...");
        File[] elements = directory.listFiles();
        List<File> files = new LinkedList<File>();
        List<File> directories = new LinkedList<File>();
        for (File element : elements) {
            if (element.isDirectory()) {
                boolean isCorrect = true;
                for (String banned : bannedDirs) {
                    if (element.getAbsolutePath().contains(banned)) {
                        isCorrect = false;
                    }
                }
                if (isCorrect) {
                    directories.add(element);
                }
            }
            if (element.isFile()) {
                for (String ext : extNames) {
                    if (element.getAbsolutePath().endsWith(ext)) {
                        files.add(element);
                    }
                }
            }
        }
        List<StatisticEntry> result = new LinkedList<StatisticEntry>();
        for (File file : files) {
            StatisticEntry fileResult = this.processFile(file, extNames);
            if (fileResult != null) this.addEntryToList(fileResult, result);
        }
        for (File dir : directories) {
            List<StatisticEntry> dirResult = this.processDirectory(dir, extNames, bannedDirs);
            for (StatisticEntry entry : dirResult) {
                this.addEntryToList(entry, result);
            }
        }
        return result;
    }

    private void addEntryToList(StatisticEntry entry, List<StatisticEntry> entryList) {
        boolean isNew = true;
        for (StatisticEntry currentEntry : entryList) {
            if (entry.getExtention().equals(currentEntry.getExtention())) {
                currentEntry.addChars(entry.getChars());
                currentEntry.addLines(entry.getLines());
                currentEntry.addNotEmptyLines(entry.getNotEmptyLines());
                currentEntry.addWords(entry.getWords());
                currentEntry.addInstances(entry.getInstances());
                isNew = false;
                break;
            }
        }
        if (isNew) {
            entryList.add(entry);
        }
    }

    private StatisticEntry processFile(File file, List<String> extNames) throws FileNotFoundException {
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        System.out.println("Processing file: " + file.getAbsolutePath() + "...");
        String extention = null;
        for (String ext : extNames) {
            if (file.getAbsolutePath().endsWith(ext)) {
                extention = ext;
            }
        }
        Scanner scanner = new Scanner(file);
        long lines = 0;
        long notEmptyLines = 0;
        long words = 0;
        long chars = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            StringTokenizer tokenizer = new StringTokenizer(line, " ");
            lines++;
            words += tokenizer.countTokens();
            chars += line.length();
            if (line.trim().length() > 0) notEmptyLines++;
        }
        scanner.close();
        return new StatisticEntry(extention, lines, notEmptyLines, chars, words, 1);
    }
}
