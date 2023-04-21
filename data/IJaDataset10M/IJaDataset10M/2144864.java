package org.jmol.adapter.readers.more;

import org.jmol.adapter.smarter.*;
import java.io.BufferedReader;
import java.util.Hashtable;

public class GamessUSReader extends GamessReader {

    public AtomSetCollection readAtomSetCollection(BufferedReader reader) {
        this.reader = reader;
        atomSetCollection = new AtomSetCollection("gamess");
        try {
            readLine();
            boolean iHaveAtoms = false;
            while (line != null) {
                if (line.indexOf("COORDINATES (BOHR)") >= 0 || line.indexOf("COORDINATES OF ALL ATOMS ARE (ANGS)") >= 0) {
                    if (++modelNumber != desiredModelNumber && desiredModelNumber > 0) {
                        if (iHaveAtoms) break;
                        readLine();
                        continue;
                    }
                    if (line.indexOf("COORDINATES (BOHR)") >= 0) readAtomsInBohrCoordinates(); else readAtomsInAngstromCoordinates();
                    iHaveAtoms = true;
                } else if (iHaveAtoms && line.indexOf("FREQUENCIES IN CM") >= 0) {
                    readFrequencies();
                } else if (iHaveAtoms && line.indexOf("ATOMIC BASIS SET") >= 0) {
                    readGaussianBasis("SHELL TYPE", "TOTAL");
                    continue;
                } else if (iHaveAtoms && (line.indexOf("  EIGENVECTORS") >= 0 || line.indexOf("  MOLECULAR ORBITALS") >= 0)) {
                    readMolecularOrbitals();
                    continue;
                }
                readLine();
            }
        } catch (Exception e) {
            return setError(e);
        }
        return atomSetCollection;
    }

    protected void readAtomsInBohrCoordinates() throws Exception {
        readLine();
        String atomName;
        atomSetCollection.newAtomSet();
        int n = 0;
        while (readLine() != null && (atomName = parseToken(line, 1, 6)) != null) {
            float x = parseFloat(line, 17, 37);
            float y = parseFloat(line, 37, 57);
            float z = parseFloat(line, 57, 77);
            if (Float.isNaN(x) || Float.isNaN(y) || Float.isNaN(z)) break;
            Atom atom = atomSetCollection.addNewAtom();
            atom.atomName = atomName + (++n);
            atom.set(x, y, z);
            atom.scale(ANGSTROMS_PER_BOHR);
            atomNames.addElement(atomName);
        }
    }

    private void readAtomsInAngstromCoordinates() throws Exception {
        readLine();
        readLine();
        String atomName;
        atomSetCollection.newAtomSet();
        int n = 0;
        while (readLine() != null && (atomName = parseToken(line, 1, 6)) != null) {
            float x = parseFloat(line, 16, 31);
            float y = parseFloat(line, 31, 46);
            float z = parseFloat(line, 46, 61);
            if (Float.isNaN(x) || Float.isNaN(y) || Float.isNaN(z)) break;
            Atom atom = atomSetCollection.addNewAtom();
            atom.atomName = atomName + (++n);
            atom.set(x, y, z);
            atomNames.addElement(atomName);
        }
    }

    protected String fixShellTag(String tag) {
        return tag;
    }

    protected void getMOHeader(String[] tokens, Hashtable[] mos, int nThisLine) throws Exception {
        tokens = getTokens(readLine());
        for (int i = 0; i < nThisLine; i++) mos[i].put("energy", new Float(tokens[i]));
        tokens = getTokens(readLine());
        for (int i = 0; i < nThisLine; i++) mos[i].put("symmetry", tokens[i]);
    }
}
