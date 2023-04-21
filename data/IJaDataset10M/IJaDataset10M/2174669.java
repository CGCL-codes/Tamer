package esra.conformation.stacking;

import java.util.*;
import esra.io.g96.in.*;

public abstract class StackingLibrary {

    protected static Hashtable<String, Vector<String>> data;

    public static void read(String file) {
        data = new Hashtable<String, Vector<String>>();
        try {
            InBlock reader = new InBlock(file);
            ArrayList<String> block;
            while ((block = reader.getNextBlock()) != null) {
                if (block.size() == 0) break;
                if (block.get(0).equals("STACKINGLIB")) {
                    for (int i = 1; i < block.size() - 1; i++) {
                        String line = block.get(i);
                        StringTokenizer tok = new StringTokenizer(line);
                        String residue = tok.nextToken();
                        Vector<String> atoms = new Vector<String>();
                        while (tok.hasMoreTokens()) {
                            String token = tok.nextToken();
                            atoms.addElement(token);
                        }
                        data.put(residue, atoms);
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Vector<String> getDefiningAtoms(StackingResidue res) {
        String name = res.getName();
        return data.get(name);
    }
}
