package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 *Класс, содержащий статические данные, константы.
 * @author kaligula
 */
public class Helper {

    public static final Point START_LOCATION = new Point(640, 480);

    public static final Dimension CELL_SIZE = new Dimension(20, 20);

    public static final int SIDE_LENGTH = 20;

    public static final Font SMALL_FONT = new Font("Sans", Font.PLAIN, 12);

    public static final Font BIG_FONT = new Font("Sans", Font.BOLD, 14);

    public static final Color DEFAULT_COLOR = new Color(238, 238, 238);

    public static final int PORT = 6666;

    private static final File recFile = new File("records.dat");

    private static final File lastFile = new File("player.last");

    /**
         * Считывает рекорды из файла и извлекает необходимую запись
         * @param playerName имя игрока, чью запись необходимо извлечь из файла.
         * @param f фишка игрока.
         * @return возвращает объект, содержащий информацию об игроке.
         */
    @SuppressWarnings("unchecked")
    public static Player identifyPlayer(String playerName) {
        Player newP = null;
        HashMap<String, Player> records = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(recFile));
            records = (HashMap<String, Player>) in.readObject();
        } catch (IOException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        if (records != null) {
            newP = records.get(playerName);
        }
        if (newP == null) {
            newP = new Player(playerName);
            System.out.println("created new player");
        }
        return newP;
    }

    /**
         * Считывает рекорды из файла, перезаписывает результат текущего игрока и сохраняет рекорды обратно в файл.
         * @param player объект, содержащий информацию об игроке, результат которого надо сохранить.
         * @param opponent объект, содержащий информацию об игроке-оппоненте, результат которого надо сохранить.
         */
    @SuppressWarnings("unchecked")
    public static void saveRecords(Player player, Player opponent) {
        HashMap<String, Player> records = null;
        ObjectOutputStream out;
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(new FileInputStream(recFile));
            records = (HashMap<String, Player>) in.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }
        if (records == null) {
            records = new HashMap<String, Player>();
        }
        records.put(player.getName(), player);
        records.put(opponent.getName(), opponent);
        try {
            out = new ObjectOutputStream(new FileOutputStream(recFile));
            out.writeObject(records);
        } catch (FileNotFoundException exc) {
            System.out.println("File not found: " + exc);
        } catch (IOException exc) {
            System.out.println(exc);
        }
    }

    /**
         * Сохраняет в файл имя последнего игрока.
         * @param playerName
         */
    public static void saveLastPlayerName(String playerName) {
        try {
            FileWriter out = new FileWriter(lastFile);
            out.write(playerName);
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
         * Восстанавливает из файла имя последнего игрока.
         * @return возвращает имя последнего игрока.
         */
    public static String restoreLastPlayerName() {
        String name = null;
        try {
            FileReader f = new FileReader(lastFile);
            BufferedReader in = new BufferedReader(f);
            name = in.readLine();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
        return name != null ? name : "Unnamed";
    }
}
