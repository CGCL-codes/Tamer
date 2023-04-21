package world.unit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public final class UnitLoader2 {

    private HashMap<String, Byte> unitTypeID = new HashMap<String, Byte>();

    public Unit createUnit(String name, boolean isGhost, byte unitType, short id) {
        Unit u = null;
        if (unitType == UnitTypeConstants.avatar) {
            u = new Avatar(isGhost, id);
        } else {
            u = new Unit(isGhost, id, (short) 10);
        }
        return u;
    }

    public UnitLoader(String unitStats) {
        unitTypeID.put(Unit.class.getName(), Byte.MIN_VALUE);
        unitTypeID.put(Avatar.class.getName(), (byte) (Byte.MIN_VALUE + 1));
        System.out.println("loading unit file...");
        File f = new File(unitStats);
        ArrayList<String> lines = new ArrayList<String>();
        try {
            Scanner scanner = new Scanner(f);
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (IOException e) {
        }
        parseFile(lines);
    }

    public static void main(String[] args) {
        new UnitLoader("example unit stats.txt");
    }

    private void parseFile(ArrayList<String> lines) {
        int index = 0;
        byte type = Byte.MIN_VALUE;
        boolean skip = false;
        String strip = "";
        while (index < lines.size()) {
            String line = lines.get(index);
            line = removeSpaces(line);
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (c == '=') {
                    skip = true;
                } else if (c == '-') {
                    skip = false;
                    parseStrip(strip);
                    strip = "";
                } else if (!skip) {
                    strip += c;
                }
            }
            index++;
        }
        parseStrip(strip);
    }

    /**
	 * parses a strip of characters from the file
	 * @param strip
	 */
    private void parseStrip(String strip) {
        if (strip.length() > 0) {
            String[] s = strip.split(",");
            for (String temp : s) {
                System.out.println(temp);
            }
        }
    }

    private String removeSpaces(String line) {
        String temp = "";
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c != ' ') {
                temp += c;
            }
        }
        return temp;
    }
}
