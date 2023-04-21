package cell;

import java.io.*;

public class CellFile extends CellRam {

    RandomAccessFile f;

    CellFile(String name) {
        name = name.toUpperCase();
        if (name.length() != 3) throw new IllegalArgumentException("Table names must be precisecly three characters long.");
        for (int i = 0; i < name.length(); i++) if (!Character.isLetter(name.charAt(i))) new IllegalArgumentException("Table names may only contain alpha characters.");
        File path = new File(name + ".csv");
        try {
            if (!path.exists()) path.createNewFile();
            f = new RandomAccessFile(path, "rw");
        } catch (IOException e) {
            System.err.println(path);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
