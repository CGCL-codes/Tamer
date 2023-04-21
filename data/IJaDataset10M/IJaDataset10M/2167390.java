package io;

import java.io.*;
import java.util.HashMap;

public class UnitDataLoader {

    HashMap<String, Object[]> unitData = new HashMap<String, Object[]>();

    /**
	 * loads the unit data files in the specified directory
	 * @param udir
	 */
    public UnitDataLoader(File udir) {
        loadUnits(udir);
    }

    private void loadUnits(File f) {
        if (f.isDirectory()) {
            File[] temp = f.listFiles();
            for (int i = 0; i < temp.length; i++) {
                loadUnits(temp[i]);
            }
        } else if (f.getName().endsWith(".udat")) {
            try {
                loadData(f);
            } catch (IOException e) {
            }
        }
    }

    private void loadData(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        int version = dis.readInt();
        if (version == 1) {
            loadDataV1(f);
        }
    }

    private void loadDataV1(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        int length = dis.readInt();
        String name = "";
        for (int i = 0; i < length; i++) {
            name += dis.readChar();
        }
        double width = dis.readDouble();
        double height = dis.readDouble();
        double life = dis.readDouble();
        double movement = dis.readDouble();
        length = dis.readInt();
        String weapon = "";
        for (int i = 0; i < length; i++) {
            weapon += dis.readChar();
        }
        double buildTime = dis.readDouble();
        double cost = dis.readDouble();
        unitData.put(name, new Object[] { name, width, height, life, movement, weapon, buildTime, cost });
    }

    public HashMap<String, Object[]> getUnitData() {
        return unitData;
    }
}
