package il.co.gadiworks.jumper;

import il.co.gadiworks.games.framework.FileIO;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Settings {

    public static boolean soundEnabled = true;

    public static final int[] HIGH_SCORES = new int[] { 100, 80, 50, 30, 10 };

    public static final String FILE = ".superjumper";

    public static void load(FileIO files) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(files.readFile(FILE)));
            soundEnabled = Boolean.parseBoolean(in.readLine());
            for (int i = 0; i < 5; i++) {
                HIGH_SCORES[i] = Integer.parseInt(in.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void save(FileIO files) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(files.writeFile(FILE)));
            out.write(Boolean.toString(soundEnabled));
            out.write("\n");
            for (int i = 0; i < 5; i++) {
                out.write(Integer.toString(HIGH_SCORES[i]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addScore(int score) {
        for (int i = 0; i < 5; i++) {
            if (HIGH_SCORES[i] < score) {
                for (int j = 4; j > i; j--) {
                    HIGH_SCORES[j] = HIGH_SCORES[j - 1];
                }
                HIGH_SCORES[i] = score;
                break;
            }
        }
    }
}
