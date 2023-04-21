package irve.io;

import irve.data.Question;
import irve.data.WordQuestion;
import irve.data.Stat;
import irve.data.Stats;
import irve.exceptions.CantCreateFileException;
import irve.exceptions.WrongFileException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map.Entry;

public class StatsIO {

    public static Stats load(String fileName) throws WrongFileException, CantCreateFileException {
        File statsFile = new File(fileName);
        try {
            if (statsFile.createNewFile() || statsFile.length() == 0) return null;
        } catch (IOException e) {
            throw new CantCreateFileException(e);
        }
        try {
            return load(statsFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stats load(File f) throws WrongFileException, FileNotFoundException {
        return load(new FileInputStream(f));
    }

    public static boolean delete(String fileName) {
        return new File(fileName).delete();
    }

    public static void reset() {
        delete(Config.getStatsFileName());
        try {
            new File(Config.getStatsFileName()).createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save(Stats stats) throws IOException {
        save(stats, Config.getStatsFileName());
    }

    public static void save(Stats stats, String fileName) throws IOException {
        save(stats, new File(fileName));
    }

    public static Stats load(InputStream fr) throws WrongFileException {
        BufferedReader br = new BufferedReader(new InputStreamReader(fr));
        try {
            Stats stats = new Stats();
            String line = null;
            while ((line = br.readLine()) != null) {
                Stat s = new Stat();
                String[] lineArr = line.split(":");
                String[] resultsArr = lineArr[1].split(",");
                s.setCountCorrect(Integer.parseInt(resultsArr[0].trim()));
                s.setCountHalfCorrect(Integer.parseInt(resultsArr[1].trim()));
                s.setCountTotallyIncorrect(Integer.parseInt(resultsArr[2].trim()));
                stats.put(new WordQuestion(lineArr[0]), s);
            }
            stats.setTotalQuestionNumber(stats.size());
            return stats;
        } catch (Exception e) {
            throw new WrongFileException(e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void save(Stats stats, File f) throws IOException {
        FileWriter fw = new FileWriter(f);
        try {
            for (Entry<Question, Stat> entry : stats.entrySet()) {
                fw.write(entry.getKey() + ": " + entry.getValue().getCountCorrect() + "," + entry.getValue().getCountHalfCorrect() + "," + entry.getValue().getCountTotallyIncorrect() + "\n");
            }
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
