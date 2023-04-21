package herostats;

import libs.Clases.Hero;
import libs.Clases.Game;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.TreeMap;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author PHENOM II
 */
public class Data {

    private TreeMap<String, Hero> heros = new TreeMap<String, Hero>();

    private TreeMap<String, Game> games = new TreeMap<String, Game>();

    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TreeMap<String, Game> getGames() {
        return games;
    }

    public void setGames(TreeMap<String, Game> games) {
        this.games = games;
    }

    public TreeMap<String, Hero> getHeros() {
        return heros;
    }

    public void setHeros(TreeMap<String, Hero> heros) {
        this.heros = heros;
    }

    public Data() {
        date = DataLoader();
    }

    public String DataLoader() {
        File f;
        do {
            f = fileSelect();
        } while (f == null && JOptionPane.showOptionDialog(null, "No file selected, want to try again?", "No file", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null) == JOptionPane.YES_OPTION);
        if (f == null) {
            return null;
        } else {
            return parseTxt(f, heros, games);
        }
    }

    private String parseTxt(File f, TreeMap<String, Hero> heros, TreeMap<String, Game> games) {
        int gameCount = 0;
        BufferedReader entrada;
        String s;
        String lastLine = "";
        String iniDate = "";
        try {
            FileReader fr = new FileReader(f);
            entrada = new BufferedReader(fr);
            s = entrada.readLine();
            if (s != null) {
                iniDate = s.split("\t")[0];
                parseLine(s, heros, games);
                gameCount++;
                lastLine = s;
            }
            while ((s = entrada.readLine()) != null) {
                parseLine(s, heros, games);
                gameCount++;
                lastLine = s;
            }
            entrada.close();
        } catch (java.io.FileNotFoundException fnfex) {
            System.out.println("File not found: " + fnfex);
            heros = null;
            return null;
        } catch (java.io.IOException ioex) {
            System.out.println("IOException: " + ioex.getMessage());
            heros = null;
            return null;
        }
        return "From " + iniDate + " to " + lastLine.split("\t")[0] + "\t Games counted: " + gameCount;
    }

    private static void parseLine(String s, TreeMap<String, Hero> heros, TreeMap<String, Game> games) {
        String[] data = s.split("\t");
        for (int a = 0; a < data.length; a++) {
            data[a] = data[a].trim();
        }
        Game g = new Game();
        games.put(data[0], g);
        Hero h = heros.get(data[4]);
        if (h == null) {
            h = new Hero(data[4]);
            heros.put(h.getName(), h);
        }
        parseData(data, h, g);
    }

    private static void parseData(String[] data, Hero h, Game g) {
        g.setDate(data[0]);
        g.setName(data[1]);
        g.setType(data[2]);
        g.setHero(data[4]);
        g.setKills(Integer.valueOf(data[5]).intValue());
        g.setDeaths(Integer.valueOf(data[6]).intValue());
        g.setAssists(Integer.valueOf(data[7]).intValue());
        g.setKdRatio(Double.valueOf(data[8]).doubleValue());
        g.setCreepK(Integer.valueOf(data[9]).intValue());
        g.setCreepD(Integer.valueOf(data[10]).intValue());
        g.setNeutralK(Integer.valueOf(data[11]).intValue());
        g.setResult(data[12]);
        h.addGameCount();
        h.addKills(g.getKills());
        h.addDeaths(g.getDeaths());
        h.addAssists(g.getAssists());
        h.addCreepKills(g.getCreepK());
        h.addCreepDenies(g.getCreepD());
        h.addNeutralKills(g.getNeutralK());
        if (g.getResult().equalsIgnoreCase("WON")) {
            h.addWins();
        } else if (g.getResult().equalsIgnoreCase("LOST")) {
            h.addLoses();
        } else if (g.getResult().equalsIgnoreCase("LEAVER")) {
            h.addLeft();
        } else if (g.getResult().equalsIgnoreCase("DRAW")) {
            h.addDraws();
        }
    }

    private static File fileSelect() {
        JFileChooser jfc = new JFileChooser();
        jfc.setName("Select the txt File");
        jfc.showOpenDialog(null);
        jfc.setMultiSelectionEnabled(false);
        return jfc.getSelectedFile();
    }
}
