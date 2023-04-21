package org.jmol.smiles;

import java.util.BitSet;
import java.util.List;
import org.jmol.api.SmilesMatcherInterface;
import org.jmol.util.JmolNode;
import org.jmol.util.TextFormat;

/**
 * Originating author: Nicholas Vervelle
 * 
 * A class to handle a variety of SMILES/SMARTS-related functions, including:
 *  -- determining if two SMILES strings are equivalent
 *  -- determining the molecular formula of a SMILES or SMARTS string
 *  -- searching for specific runs of atoms in a 3D model
 *  -- searching for specific runs of atoms in a SMILES description
 *  -- generating valid (though not canonical) SMILES and bioSMILES strings
 *  -- getting atom-atom correlation maps to be used with biomolecular alignment methods
 *  
 * <p>
 * The original SMILES description can been found at the
 * <a href="http://www.daylight.com/smiles/">SMILES Home Page</a>.
 * 
 * Specification for this implementation can be found in package.html.
 * 
 * <p>
 * <pre><code>
 * public methods:
 * 
 * int areEqual  -- checks a SMILES string against a reference (-1 for error; 0 for no finds; >0 for number of finds)
 * 
 * BitSet[] find  -- finds one or more occurances of a SMILES or SMARTS string within a SMILES string
 * 
 * int[][] getCorrelationMaps  -- returns correlated arrays of atoms
 * 
 * String getLastError  -- returns any error that was last encountered.
 * 
 * String getMolecularFormula   -- returns the MF of a SMILES or SMARTS string
 * 
 * String getRelationship -- returns isomeric relationship
 * 
 * String getSmiles  -- returns a standard SMILES string or a
 *                  Jmol BIOSMILES string with comment header.
 * 
 * BitSet getSubstructureSet  -- returns a single BitSet with all found atoms included
 *   
 *   
 *   in Jmol script:
 *   
 *   string2.find("SMILES", string1)
 *   string2.find("SMARTS", string1)
 *   
 *   e.g.
 *   
 *     print "CCCC".find("SMILES", "C[C]")
 *
 *   select search("smartsString")
 *   
 *   All bioSMARTS strings begin with ~ (tilde).
 *   
 * </code></pre>
 * 
 * @author Bob Hanson
 * 
 */
public class SmilesMatcher implements SmilesMatcherInterface {

    private static final int MODE_BITSET = 1;

    private static final int MODE_ARRAY = 2;

    private static final int MODE_MAP = 3;

    public String getLastException() {
        return InvalidSmilesException.getLastError();
    }

    public String getMolecularFormula(String pattern, boolean isSmarts) {
        InvalidSmilesException.setLastError(null);
        try {
            SmilesSearch search = SmilesParser.getMolecule(pattern, isSmarts);
            search.createTopoMap(null);
            search.nodes = search.jmolAtoms;
            return search.getMolecularFormula(!isSmarts);
        } catch (InvalidSmilesException e) {
            if (InvalidSmilesException.getLastError() == null) InvalidSmilesException.setLastError(e.getMessage());
            return null;
        }
    }

    public String getSmiles(JmolNode[] atoms, int atomCount, BitSet bsSelected, boolean asBioSmiles, boolean allowUnmatchedRings, boolean addCrossLinks, String comment) {
        InvalidSmilesException.setLastError(null);
        try {
            if (asBioSmiles) return (new SmilesGenerator()).getBioSmiles(atoms, atomCount, bsSelected, allowUnmatchedRings, addCrossLinks, comment);
            return (new SmilesGenerator()).getSmiles(atoms, atomCount, bsSelected);
        } catch (InvalidSmilesException e) {
            if (InvalidSmilesException.getLastError() == null) InvalidSmilesException.setLastError(e.getMessage());
            return null;
        }
    }

    public int areEqual(String smiles1, String smiles2) {
        BitSet[] result = find(smiles1, smiles2, false, false);
        return (result == null ? -1 : result.length);
    }

    /**
   * for JUnit test, mainly
   * 
   * @param smiles
   * @param molecule
   * @return        true only if the SMILES strings match and there are no errors
   */
    public boolean areEqual(String smiles, SmilesSearch molecule) {
        BitSet[] ret = find(smiles, molecule, false, true, true);
        return (ret != null && ret.length == 1);
    }

    /**
   * 
   * Searches for all matches of a pattern within a SMILES string.
   * If SMILES (not isSmarts), requires that all atoms be part of the match.
   * 
   * 
   * @param pattern
   *          SMILES or SMARTS pattern.
   * @param smiles
   * @param isSmarts TRUE for SMARTS strings, FALSE for SMILES strings
   * @param firstMatchOnly 
   * @return number of occurances of pattern within smiles
   */
    public BitSet[] find(String pattern, String smiles, boolean isSmarts, boolean firstMatchOnly) {
        InvalidSmilesException.setLastError(null);
        try {
            SmilesSearch search = SmilesParser.getMolecule(smiles, false);
            return find(pattern, search, isSmarts, !isSmarts, firstMatchOnly);
        } catch (Exception e) {
            if (InvalidSmilesException.getLastError() == null) InvalidSmilesException.setLastError(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String getRelationship(String smiles1, String smiles2) {
        if (smiles1 == null || smiles2 == null || smiles1.length() == 0 || smiles2.length() == 0) return "";
        String mf1 = getMolecularFormula(smiles1, false);
        String mf2 = getMolecularFormula(smiles2, false);
        if (!mf1.equals(mf2)) return "none";
        boolean check;
        int n1 = countStereo(smiles1);
        int n2 = countStereo(smiles2);
        check = (n1 == n2 && areEqual(smiles2, smiles1) > 0);
        if (!check) {
            String s = smiles1 + smiles2;
            if (s.indexOf("/") >= 0 || s.indexOf("\\") >= 0 || s.indexOf("@") >= 0) {
                if (n1 == n2 && n1 > 0) {
                    smiles1 = reverseChirality(smiles1);
                    check = (areEqual(smiles1, smiles2) > 0);
                    if (check) return "enantiomers";
                }
                check = (areEqual("/nostereo/" + smiles2, smiles1) > 0);
                if (check) return (n1 == n2 ? "diastereomers" : "ambiguous stereochemistry!");
            }
            return "constitutional isomers";
        }
        return "identical";
    }

    public String reverseChirality(String smiles) {
        smiles = TextFormat.simpleReplace(smiles, "@@", "!@");
        smiles = TextFormat.simpleReplace(smiles, "@", "@@");
        smiles = TextFormat.simpleReplace(smiles, "!@@", "@");
        smiles = TextFormat.simpleReplace(smiles, "@@SP", "@SP");
        smiles = TextFormat.simpleReplace(smiles, "@@OH", "@OH");
        smiles = TextFormat.simpleReplace(smiles, "@@TB", "@TB");
        return smiles;
    }

    /**
   * Returns a bitset matching the pattern within atoms.
   * 
   * @param pattern
   *          SMILES or SMARTS pattern.
   * @param atoms
   * @param atomCount
   * @param bsSelected
   * @param isSmarts
   * @param firstMatchOnly
   * @return BitSet indicating which atoms match the pattern.
   */
    public BitSet getSubstructureSet(String pattern, JmolNode[] atoms, int atomCount, BitSet bsSelected, boolean isSmarts, boolean firstMatchOnly) {
        return (BitSet) match(pattern, atoms, atomCount, bsSelected, null, isSmarts, false, firstMatchOnly, MODE_BITSET);
    }

    /**
   * Returns a vector of bitsets indicating which atoms match the pattern.
   * 
   * @param pattern
   *          SMILES or SMARTS pattern.
   * @param atoms 
   * @param atomCount 
   * @param bsSelected 
   * @param bsAromatic 
   * @param isSmarts 
   * @param firstMatchOnly
   * @return BitSet Array indicating which atoms match the pattern.
   */
    public BitSet[] getSubstructureSetArray(String pattern, JmolNode[] atoms, int atomCount, BitSet bsSelected, BitSet bsAromatic, boolean isSmarts, boolean firstMatchOnly) {
        return (BitSet[]) match(pattern, atoms, atomCount, bsSelected, bsAromatic, isSmarts, false, firstMatchOnly, MODE_ARRAY);
    }

    /**
   * Rather than returning bitsets, this method returns the
   * sets of matching atoms in array form so that a direct 
   * atom-atom correlation can be made.
   * 
   * @param pattern 
   *          SMILES or SMARTS pattern.
   * @param atoms 
   * @param atomCount 
   * @param bsSelected 
   * @param isSmarts 
   * @param firstMatchOnly
   * @return      a set of atom correlations
   * 
   */
    public int[][] getCorrelationMaps(String pattern, JmolNode[] atoms, int atomCount, BitSet bsSelected, boolean isSmarts, boolean firstMatchOnly) {
        return (int[][]) match(pattern, atoms, atomCount, bsSelected, null, isSmarts, false, firstMatchOnly, MODE_MAP);
    }

    private BitSet[] find(String pattern, SmilesSearch search, boolean isSmarts, boolean matchAllAtoms, boolean firstMatchOnly) {
        BitSet bsAromatic = new BitSet();
        search.createTopoMap(bsAromatic);
        return (BitSet[]) match(pattern, search.jmolAtoms, -search.jmolAtoms.length, null, bsAromatic, isSmarts, matchAllAtoms, firstMatchOnly, MODE_ARRAY);
    }

    @SuppressWarnings({ "unchecked" })
    private Object match(String pattern, JmolNode[] atoms, int atomCount, BitSet bsSelected, BitSet bsAromatic, boolean isSmarts, boolean matchAllAtoms, boolean firstMatchOnly, int mode) {
        InvalidSmilesException.setLastError(null);
        try {
            SmilesSearch search = SmilesParser.getMolecule(pattern, isSmarts);
            search.jmolAtoms = atoms;
            search.jmolAtomCount = Math.abs(atomCount);
            if (atomCount < 0) search.isSmilesFind = true;
            search.setSelected(bsSelected);
            search.bsRequired = null;
            search.setRingData(bsAromatic);
            search.firstMatchOnly = firstMatchOnly;
            search.matchAllAtoms = matchAllAtoms;
            switch(mode) {
                case MODE_BITSET:
                    search.asVector = false;
                    return search.search(false);
                case MODE_ARRAY:
                    search.asVector = true;
                    List<BitSet> vb = (List<BitSet>) search.search(false);
                    return vb.toArray(new BitSet[vb.size()]);
                case MODE_MAP:
                    search.getMaps = true;
                    List<int[]> vl = (List<int[]>) search.search(false);
                    return vl.toArray(new int[vl.size()][]);
            }
        } catch (Exception e) {
            if (InvalidSmilesException.getLastError() == null) InvalidSmilesException.setLastError(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private int countStereo(String s) {
        s = TextFormat.simpleReplace(s, "@@", "@");
        int i = s.lastIndexOf('@') + 1;
        int n = 0;
        for (; --i >= 0; ) if (s.charAt(i) == '@') n++;
        return n;
    }
}
