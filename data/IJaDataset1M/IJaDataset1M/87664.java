package com.csol.chem.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;
import com.csol.chem.core.Atom;
import com.csol.chem.core.Element;
import com.csol.chem.core.Molecule;

/**
 * Tinker import filter stub. Works for some structures.
 * @author jiro
 *
 */
public class TINKERReader {

    /**
     * 
     */
    public TINKERReader() {
        super();
    }

    /**
     * Read a MOL file.
     * @param file
     * @return a molecule representing the data in the file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static Molecule read(File file) throws FileNotFoundException, IOException {
        Molecule mol = new Molecule();
        HashMap<Integer, LinkedList<Integer>> bondMap = new HashMap<Integer, LinkedList<Integer>>();
        HashMap<Integer, Atom> atomMap = new HashMap<Integer, Atom>();
        BufferedReader in = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = in.readLine()) != null) {
            StringTokenizer t = new StringTokenizer(line, " ");
            if (t.countTokens() >= 5) {
                int nr = Integer.parseInt(t.nextToken());
                Element e = Element.getBySymbol(t.nextToken());
                double x = Double.parseDouble(t.nextToken());
                double y = Double.parseDouble(t.nextToken());
                double z = Double.parseDouble(t.nextToken());
                int type = Integer.parseInt(t.nextToken());
                Atom atom = new Atom(x, y, z, e);
                atomMap.put(nr, atom);
                LinkedList<Integer> bonds = new LinkedList<Integer>();
                while (t.hasMoreTokens()) {
                    bonds.add(Integer.parseInt(t.nextToken()));
                }
                bondMap.put(nr, bonds);
                mol.addAtom(atom);
            }
        }
        for (int rootNr : atomMap.keySet()) {
            Atom root = atomMap.get(rootNr);
            for (int boundNr : bondMap.get(rootNr)) {
                Atom bound = atomMap.get(boundNr);
                root.bindAtom(bound);
            }
        }
        return mol;
    }
}
