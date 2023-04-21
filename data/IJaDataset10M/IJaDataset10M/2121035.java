package org.jmol.adapter.readers.more;

import java.util.ArrayList;
import org.jmol.adapter.smarter.*;
import org.jmol.api.JmolAdapter;
import org.jmol.util.Logger;

/**
 * A reader for Amber Molecular Dynamics topology files --
 * requires subsequent COORD "xxxx.mdcrd" file 
 * 
 *<p>
 * <a href=''>
 *  
 * </a>
 * 
 * PDB note:
 * 
 * Note that topology format does not include chain designations,
 * chain terminator, chain designator, or element symbol.
 * 
 * Chains based on numbering reset just labeled A B C D .... Z a b c d .... z
 * Element symbols based on reasoned guess and properties of hetero groups
 * 
 * In principal we could use average atomic mass.
 * 
 * 
 *<p>
 */
public class MdTopReader extends ForceFieldReader {

    private int nAtoms = 0;

    private int atomCount = 0;

    @Override
    protected void initializeReader() throws Exception {
        setUserAtomTypes();
    }

    @Override
    protected boolean checkLine() throws Exception {
        if (line.indexOf("%FLAG ") != 0) return true;
        line = line.substring(6).trim();
        if (line.equals("POINTERS")) getPointers(); else if (line.equals("ATOM_NAME")) getAtomNames(); else if (line.equals("CHARGE")) getCharges(); else if (line.equals("RESIDUE_LABEL")) getResidueLabels(); else if (line.equals("RESIDUE_POINTER")) getResiduePointers(); else if (line.equals("AMBER_ATOM_TYPE")) getAtomTypes(); else if (line.equals("MASS")) getMasses();
        return false;
    }

    @Override
    protected void finalizeReader() throws Exception {
        super.finalizeReader();
        Atom[] atoms = atomSetCollection.getAtoms();
        Atom atom;
        for (int i = 0; i < atomCount; i++) {
            atom = atoms[i];
            atom.isHetero = JmolAdapter.isHetero(atom.group3);
            String atomType = atomTypes[i];
            if (!getElementSymbol(atom, atomType)) atom.elementSymbol = deducePdbElementSymbol(atom.isHetero, atom.atomName, atom.group3);
        }
        Atom[] atoms2 = null;
        if (filter == null) {
            nAtoms = atomCount;
        } else {
            atoms2 = new Atom[atoms.length];
            nAtoms = 0;
            for (int i = 0; i < atomCount; i++) if (filterAtom(atoms[i], i)) atoms2[nAtoms++] = atoms[i];
        }
        for (int i = 0, j = 0, k = 0; i < atomCount; i++) {
            if (filter == null || bsFilter.get(i)) {
                if (k % 100 == 0) j++;
                setAtomCoord(atoms[i], (i % 100) * 2, j * 2, 0);
            }
        }
        if (atoms2 != null) {
            atomSetCollection.discardPreviousAtoms();
            for (int i = 0; i < nAtoms; i++) atomSetCollection.addAtom(atoms2[i]);
        }
        Logger.info("Total number of atoms used=" + nAtoms);
        setIsPDB();
        htParams.put("defaultType", "mdcrd");
    }

    private String[] getDataBlock() throws Exception {
        ArrayList<String> vdata = new ArrayList<String>();
        discardLinesUntilContains("FORMAT");
        int n = getFortranFormatLengths(line.substring(line.indexOf("("))).get(0).intValue();
        int i = 0;
        int len = 0;
        while (true) {
            if (i >= len) {
                if (readLine() == null) break;
                i = 0;
                len = line.length();
                if (len == 0 || line.indexOf("FLAG") >= 0) break;
            }
            vdata.add(line.substring(i, i + n).trim());
            i += n;
        }
        return vdata.toArray(new String[vdata.size()]);
    }

    private void getPointers() throws Exception {
        String[] tokens = getDataBlock();
        atomCount = parseInt(tokens[0]);
        boolean isPeriodic = (tokens[27].charAt(0) != '0');
        if (isPeriodic) {
            Logger.info("Periodic type: " + tokens[27]);
            htParams.put("isPeriodic", Boolean.TRUE);
        }
        Logger.info("Total number of atoms read=" + atomCount);
        htParams.put("templateAtomCount", Integer.valueOf(atomCount));
        for (int i = 0; i < atomCount; i++) atomSetCollection.addAtom(new Atom());
    }

    String[] atomTypes;

    private void getAtomTypes() throws Exception {
        atomTypes = getDataBlock();
    }

    private void getCharges() throws Exception {
        String[] data = getDataBlock();
        if (data.length != atomCount) return;
        Atom[] atoms = atomSetCollection.getAtoms();
        for (int i = atomCount; --i >= 0; ) atoms[i].partialCharge = parseFloat(data[i]);
    }

    private void getResiduePointers() throws Exception {
        String[] resPtrs = getDataBlock();
        Logger.info("Total number of residues=" + resPtrs.length);
        int pt1 = atomCount;
        int pt2;
        Atom[] atoms = atomSetCollection.getAtoms();
        for (int i = resPtrs.length; --i >= 0; ) {
            int ptr = pt2 = parseInt(resPtrs[i]) - 1;
            while (ptr < pt1) {
                if (group3s != null) atoms[ptr].group3 = group3s[i];
                atoms[ptr++].sequenceNumber = i + 1;
            }
            pt1 = pt2;
        }
    }

    String[] group3s;

    private void getResidueLabels() throws Exception {
        group3s = getDataBlock();
    }

    private void getAtomNames() throws Exception {
        String[] names = getDataBlock();
        Atom[] atoms = atomSetCollection.getAtoms();
        for (int i = 0; i < atomCount; i++) atoms[i].atomName = names[i];
    }

    private void getMasses() throws Exception {
    }
}
