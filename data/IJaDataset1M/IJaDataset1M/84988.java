package org.jmol.adapter.readers.xtal;

import org.jmol.adapter.smarter.*;
import org.jmol.util.Eigen;
import org.jmol.util.Escape;
import org.jmol.util.Logger;
import org.jmol.util.Quaternion;
import org.jmol.util.TextFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import javax.vecmath.Matrix3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * 
 * A reader of OUT and OUTP files for CRYSTAL
 * 
 * http://www.crystal.unito.it/
 * 
 * @author Pieremanuele Canepa, Room 104, FM Group School of Physical Sciences,
 *         Ingram Building, University of Kent, Canterbury, Kent, CT2 7NH United
 *         Kingdom, pc229@kent.ac.uk
 * 
 * @version 1.4
 * 
 * 
 *          for a specific model in the set, use
 * 
 *          load "xxx.out" n
 * 
 *          as for all readers, where n is an integer > 0
 * 
 *          for final optimized geometry use
 * 
 *          load "xxx.out" 0
 * 
 *          (that is, "read the last model") as for all readers
 * 
 *          for conventional unit cell -- input coordinates only, use
 * 
 *          load "xxx.out" filter "conventional"
 * 
 *          to NOT load vibrations, use
 * 
 *          load "xxx.out" FILTER "novibrations"
 * 
 *          to load just the input deck exactly as indicated, use
 * 
 *          load "xxx.out" FILTER "input"
 * 
 *          now allows reading of frequencies and atomic values with
 *          conventional as long as this is not an optimization.
 * 
 * 
 * 
 * 
 */
public class CrystalReader extends AtomSetCollectionReader {

    private boolean isVersion3;

    private boolean isPrimitive;

    private boolean isPolymer;

    private boolean isSlab;

    private boolean isMolecular;

    private boolean haveCharges;

    private boolean isFreqCalc;

    private boolean inputOnly;

    private boolean isLongMode;

    private boolean getLastConventional;

    private boolean havePrimitiveMapping;

    private boolean isProperties;

    private int atomCount;

    private int atomIndexLast;

    private int[] atomFrag;

    private int[] primitiveToIndex;

    private float[] nuclearCharges;

    private List<String> vInputCoords;

    private Double energy;

    private Point3f ptOriginShift = new Point3f();

    private Matrix3f primitiveToCryst;

    private Vector3f[] directLatticeVectors;

    private String spaceGroupName;

    @Override
    protected void initializeReader() throws Exception {
        doProcessLines = false;
        inputOnly = checkFilter("INPUT");
        isPrimitive = !inputOnly && !checkFilter("CONV");
        addVibrations &= !inputOnly && isPrimitive;
        getLastConventional = (!isPrimitive && desiredModelNumber == 0);
        setFractionalCoordinates(readHeader());
    }

    @Override
    protected boolean checkLine() throws Exception {
        if (line.startsWith(" LATTICE PARAMETER")) {
            boolean isConvLattice = (line.indexOf("- CONVENTIONAL") >= 0);
            if (isConvLattice) {
                if (isPrimitive) return true;
                readCellParams(true);
            } else if (!isPrimitive && !havePrimitiveMapping && !getLastConventional) {
                if (readPrimitiveMapping()) return true;
            }
            readCellParams(true);
            if (!isPrimitive) {
                discardLinesUntilContains(" TRANSFORMATION");
                readTransformationMatrix();
                discardLinesUntilContains(" CRYSTALLOGRAPHIC");
                readCellParams(false);
                discardLinesUntilContains(" CRYSTALLOGRAPHIC");
                readCrystallographicCoords();
                if (modelNumber == 1) {
                } else if (!isFreqCalc) {
                }
                if (!getLastConventional) {
                    if (!doGetModel(++modelNumber)) {
                        vInputCoords = null;
                        checkLastModel();
                    }
                    processInputCoords();
                }
            }
            return true;
        }
        if (isPrimitive) {
            if (line.indexOf("VOLUME=") >= 0 && line.indexOf("- DENSITY") >= 0) return readVolumePrimCell();
        } else {
            if (line.startsWith(" SHIFT OF THE ORIGIN")) return readShift();
            if (line.startsWith(" INPUT COORDINATES")) {
                readCrystallographicCoords();
                if (inputOnly) continuing = false;
                return true;
            }
        }
        if (line.startsWith(" DIRECT LATTICE VECTOR")) return setDirect();
        if (line.indexOf("DIMENSIONALITY OF THE SYSTEM") >= 0) {
            if (line.indexOf("2") >= 0) isSlab = true;
            if (line.indexOf("1") >= 0) isPolymer = true;
            return true;
        }
        if (line.indexOf("FRQFRQ") >= 0) {
            isFreqCalc = true;
            return true;
        }
        if (line.startsWith(" FREQUENCIES COMPUTED ON A FRAGMENT")) return readFragments();
        if (line.indexOf("CONSTRUCTION OF A NANOTUBE FROM A SLAB") >= 0) {
            isPolymer = true;
            isSlab = false;
            return true;
        }
        if (line.indexOf("* CLUSTER CALCULATION") >= 0) {
            isMolecular = true;
            isSlab = false;
            isPolymer = false;
            return true;
        }
        if ((isPrimitive && line.startsWith(" ATOMS IN THE ASYMMETRIC UNIT")) || isProperties && line.startsWith("   ATOM N.AT.")) {
            if (!doGetModel(++modelNumber)) return checkLastModel();
            return readAtoms();
        }
        if (!doProcessLines) return true;
        if (line.startsWith(" TOTAL ENERGY")) {
            readEnergy();
            readLine();
            if (line.startsWith(" ********")) discardLinesUntilContains("SYMMETRY ALLOWED"); else if (line.startsWith(" TTTTTTTT")) discardLinesUntilContains("PREDICTED ENERGY CHANGE", "HHHHHHH");
            return true;
        }
        if (line.startsWith(" TYPE OF CALCULATION")) {
            calculationType = line.substring(line.indexOf(":") + 1).trim();
            return true;
        }
        if (line.startsWith(" MULLIKEN POPULATION ANALYSIS")) return readPartialCharges();
        if (line.startsWith(" TOTAL ATOMIC CHARGES")) return readTotalAtomicCharges();
        if (addVibrations && line.contains(isVersion3 ? "EIGENVALUES (EV) OF THE MASS" : "EIGENVALUES (EIGV) OF THE MASS") || line.indexOf("LONGITUDINAL OPTICAL (LO)") >= 0) {
            if (vInputCoords != null) processInputCoords();
            isLongMode = (line.indexOf("LONGITUDINAL OPTICAL (LO)") >= 0);
            return readFrequencies();
        }
        if (line.startsWith(" MAX GRADIENT")) return readGradient();
        if (line.startsWith(" ATOMIC SPINS SET")) return readSpins();
        if (line.startsWith(" TOTAL ATOMIC SPINS  :")) return readMagneticMoments();
        if (!isProperties) return true;
        if (line.startsWith(" DEFINITION OF TRACELESS")) return getPropertyTensors();
        if (line.startsWith(" MULTIPOLE ANALYSIS BY ATOMS")) {
            appendLoadNote("Multipole Analysis");
            return true;
        }
        return true;
    }

    @Override
    protected void finalizeReader() throws Exception {
        if (vInputCoords != null) processInputCoords();
        if (energy != null) setEnergy();
        super.finalizeReader();
    }

    private boolean setDirect() throws Exception {
        boolean isBohr = (line.indexOf("(BOHR") >= 0);
        directLatticeVectors = read3Vectors(isBohr);
        if (Logger.debugging) {
            addJmolScript("draw va vector {0 0 0} " + Escape.escape(directLatticeVectors[0]) + " color red");
            if (!isPolymer) {
                addJmolScript("draw vb vector {0 0 0} " + Escape.escape(directLatticeVectors[1]) + " color green");
                if (!isSlab) addJmolScript("draw vc vector {0 0 0} " + Escape.escape(directLatticeVectors[2]) + " color blue");
            }
        }
        Vector3f a = new Vector3f();
        Vector3f b = new Vector3f();
        if (isPrimitive) {
            a = directLatticeVectors[0];
            b = directLatticeVectors[1];
        } else {
            if (primitiveToCryst == null) return true;
            Matrix3f mp = new Matrix3f();
            mp.setColumn(0, directLatticeVectors[0]);
            mp.setColumn(1, directLatticeVectors[1]);
            mp.setColumn(2, directLatticeVectors[2]);
            mp.mul(primitiveToCryst);
            a = new Vector3f();
            b = new Vector3f();
            mp.getColumn(0, a);
            mp.getColumn(1, b);
        }
        matUnitCellOrientation = Quaternion.getQuaternionFrame(new Point3f(), a, b).getMatrix();
        Logger.info("oriented unit cell is in model " + atomSetCollection.getAtomSetCount());
        return !isProperties;
    }

    private void readTransformationMatrix() throws Exception {
        primitiveToCryst = new Matrix3f(fillFloatArray(null, 0, new float[9]));
    }

    private boolean readShift() {
        String[] tokens = getTokens();
        int pt = tokens.length - 3;
        ptOriginShift.set(fraction(tokens[pt++]), fraction(tokens[pt++]), fraction(tokens[pt]));
        return true;
    }

    private float fraction(String f) {
        String[] ab = TextFormat.split(f, '/');
        return (ab.length == 2 ? parseFloat(ab[0]) / parseFloat(ab[1]) : 0);
    }

    private boolean readGradient() throws Exception {
        String key = null;
        while (line != null) {
            String[] tokens = getTokens();
            if (line.indexOf("MAX GRAD") >= 0) key = "maxGradient"; else if (line.indexOf("RMS GRAD") >= 0) key = "rmsGradient"; else if (line.indexOf("MAX DISP") >= 0) key = "maxDisplacement"; else if (line.indexOf("RMS DISP") >= 0) key = "rmsDisplacement"; else break;
            if (atomSetCollection.getAtomCount() > 0) atomSetCollection.setAtomSetModelProperty(key, tokens[2]);
            readLine();
        }
        return true;
    }

    private boolean readVolumePrimCell() {
        String[] tokens = getTokens(line);
        String volumePrim = tokens[7];
        if (tokens[9].length() > 7) {
            line = TextFormat.simpleReplace(line, "DENSITY", "DENSITY ");
        }
        String densityPrim = tokens[10];
        atomSetCollection.setAtomSetModelProperty("volumePrimitive", TextFormat.formatDecimal(parseFloat(volumePrim), 3));
        atomSetCollection.setAtomSetModelProperty("densityPrimitive", TextFormat.formatDecimal(parseFloat(densityPrim), 3));
        return true;
    }

    private boolean readSpins() throws Exception {
        String data = "";
        while (readLine() != null && line.indexOf("ALPHA") < 0) data += line;
        data = TextFormat.simpleReplace(data, "-", " -");
        setData("spin", data, 2, 3);
        return true;
    }

    private boolean readMagneticMoments() throws Exception {
        String data = "";
        while (readLine() != null && line.indexOf("TTTTTT") < 0) data += line;
        setData("magneticMoment", data, 0, 1);
        return true;
    }

    private void setData(String name, String data, int pt, int dp) throws Exception {
        if (vInputCoords != null) processInputCoords();
        String[] s = new String[atomCount];
        for (int i = 0; i < atomCount; i++) s[i] = "0";
        String[] tokens = getTokens(data);
        for (int i = 0; i < atomCount; i++, pt += dp) {
            int iConv = getAtomIndexFromPrimitiveIndex(i);
            if (iConv >= 0) s[iConv] = tokens[pt];
        }
        data = TextFormat.join(s, '\n', 0);
        atomSetCollection.setAtomSetAtomProperty(name, data, -1);
    }

    private boolean readHeader() throws Exception {
        discardLinesUntilContains("*                                CRYSTAL");
        isVersion3 = (line.indexOf("CRYSTAL03") >= 0);
        discardLinesUntilContains("EEEEEEEEEE");
        String name;
        if (readLine().length() == 0) {
            name = readLines(2).trim();
        } else {
            name = line.trim();
            readLine();
        }
        String type = readLine().trim();
        int pt = type.indexOf("- PROPERTIES");
        if (pt >= 0) {
            isProperties = true;
            type = type.substring(0, pt).trim();
        }
        if (type.indexOf("EXTERNAL FILE") >= 0) {
            type = readLine().trim();
            isPolymer = (type.equals("1D - POLYMER"));
            isSlab = (type.equals("2D - SLAB"));
        } else {
            isPolymer = (type.equals("POLYMER CALCULATION"));
            isSlab = (type.equals("SLAB CALCULATION"));
        }
        atomSetCollection.setCollectionName(name + (!isProperties && desiredModelNumber == 0 ? " (optimized)" : ""));
        atomSetCollection.setAtomSetCollectionAuxiliaryInfo("symmetryType", type);
        if ((isPolymer || isSlab) && !isPrimitive) {
            Logger.error("Cannot use FILTER \"conventional\" with POLYMER or SLAB");
            isPrimitive = true;
        }
        atomSetCollection.setAtomSetCollectionAuxiliaryInfo("unitCellType", (isPrimitive ? "primitive" : "conventional"));
        if (type.indexOf("MOLECULAR") >= 0) {
            isMolecular = doProcessLines = true;
            readLine();
            atomSetCollection.setAtomSetCollectionAuxiliaryInfo("molecularCalculationPointGroup", line.substring(line.indexOf(" OR ") + 4).trim());
            return false;
        }
        spaceGroupName = "P1";
        if (!isPrimitive) {
            readLines(5);
            pt = line.indexOf(":");
            if (pt >= 0) spaceGroupName = line.substring(pt + 1).trim();
        }
        doApplySymmetry = isProperties;
        return !isProperties;
    }

    private void readCellParams(boolean isNewSet) throws Exception {
        float f = (line.indexOf("(BOHR") >= 0 ? ANGSTROMS_PER_BOHR : 1);
        if (isNewSet) newAtomSet();
        if (isPolymer && !isPrimitive) {
            setUnitCell(parseFloat(line.substring(line.indexOf("CELL") + 4)) * f, -1, -1, 90, 90, 90);
        } else {
            discardLinesUntilContains("GAMMA");
            String[] tokens = getTokens(readLine());
            if (isSlab) {
                if (isPrimitive) setUnitCell(parseFloat(tokens[0]) * f, parseFloat(tokens[1]) * f, -1, parseFloat(tokens[3]), parseFloat(tokens[4]), parseFloat(tokens[5])); else setUnitCell(parseFloat(tokens[0]) * f, parseFloat(tokens[1]) * f, -1, 90, 90, parseFloat(tokens[2]));
            } else if (isPolymer) {
                setUnitCell(parseFloat(tokens[0]) * f, -1, -1, parseFloat(tokens[3]), parseFloat(tokens[4]), parseFloat(tokens[5]));
            } else {
                setUnitCell(parseFloat(tokens[0]) * f, parseFloat(tokens[1]) * f, parseFloat(tokens[2]) * f, parseFloat(tokens[3]), parseFloat(tokens[4]), parseFloat(tokens[5]));
            }
        }
    }

    /**
   * create arrays that maps primitive atoms to conventional atoms in a 1:1
   * fashion. Creates:
   * 
   * int[] primitiveToIndex -- points to model-based atomIndex
   * 
   * @return TRUE
   * 
   * @throws Exception
   */
    private boolean readPrimitiveMapping() throws Exception {
        if (vInputCoords == null) return false;
        havePrimitiveMapping = true;
        BitSet bsInputAtomsIgnore = new BitSet();
        int n = vInputCoords.size();
        int[] indexToPrimitive = new int[n];
        primitiveToIndex = new int[n];
        for (int i = 0; i < n; i++) indexToPrimitive[i] = -1;
        readLines(3);
        while (readLine() != null && line.indexOf(" NOT IRREDUCIBLE") >= 0) {
            bsInputAtomsIgnore.set(parseInt(line.substring(21, 25)) - 1);
            readLine();
        }
        readLines(3);
        int iPrim = 0;
        int nPrim = 0;
        while (readLine() != null && line.indexOf("NUMBER") < 0) {
            if (line.length() == 0) continue;
            nPrim++;
            int iAtom = parseInt(line.substring(4, 8)) - 1;
            if (indexToPrimitive[iAtom] < 0) {
                indexToPrimitive[iAtom] = iPrim++;
            }
        }
        if (bsInputAtomsIgnore.nextSetBit(0) >= 0) for (int i = n; --i >= 0; ) if (bsInputAtomsIgnore.get(i)) vInputCoords.remove(i);
        atomCount = vInputCoords.size();
        Logger.info(nPrim + " primitive atoms and " + atomCount + " conventionalAtoms");
        primitiveToIndex = new int[nPrim];
        for (int i = 0; i < nPrim; i++) primitiveToIndex[i] = -1;
        for (int i = atomCount; --i >= 0; ) {
            iPrim = indexToPrimitive[parseInt(vInputCoords.get(i).substring(0, 4)) - 1];
            if (iPrim >= 0) primitiveToIndex[iPrim] = i;
        }
        return true;
    }

    private boolean readAtoms() throws Exception {
        if (isMolecular) newAtomSet();
        while (readLine() != null && line.indexOf("*") < 0) {
            if (line.indexOf("X(ANGSTROM") >= 0) {
                setFractionalCoordinates(false);
                isMolecular = true;
            }
        }
        int i = atomIndexLast;
        boolean doNormalizePrimitive = false;
        atomIndexLast = atomSetCollection.getAtomCount();
        while (readLine() != null && line.length() > 0 && line.indexOf(isPrimitive ? "*" : "=") < 0) {
            Atom atom = atomSetCollection.addNewAtom();
            String[] tokens = getTokens();
            int pt = (isProperties ? 1 : 2);
            atom.elementSymbol = getElementSymbol(getAtomicNumber(tokens[pt++]));
            atom.atomName = getAtomName(tokens[pt++]);
            if (isProperties) pt++;
            float x = parseFloat(tokens[pt++]);
            float y = parseFloat(tokens[pt++]);
            float z = parseFloat(tokens[pt]);
            if (haveCharges) atom.partialCharge = atomSetCollection.getAtom(i++).partialCharge;
            if (iHaveFractionalCoordinates && !isProperties) {
                if (x < 0 && (isPolymer || isSlab || doNormalizePrimitive)) x += 1;
                if (y < 0 && (isSlab || doNormalizePrimitive)) y += 1;
                if (z < 0 && doNormalizePrimitive) z += 1;
            }
            setAtomCoord(atom, x, y, z);
        }
        atomCount = atomSetCollection.getAtomCount() - atomIndexLast;
        return true;
    }

    private String getAtomName(String s) {
        String atomName = s;
        if (atomName.length() > 1 && Character.isLetter(atomName.charAt(1))) atomName = atomName.substring(0, 1) + Character.toLowerCase(atomName.charAt(1)) + atomName.substring(2);
        return atomName;
    }

    private int getAtomicNumber(String token) {
        int atomicNumber = parseInt(token);
        while (atomicNumber >= 100) atomicNumber -= 100;
        return atomicNumber;
    }

    private void readCrystallographicCoords() throws Exception {
        readLine();
        readLine();
        vInputCoords = new ArrayList<String>();
        while (readLine() != null && line.length() > 0) vInputCoords.add(line);
    }

    private void processInputCoords() throws Exception {
        atomCount = vInputCoords.size();
        for (int i = 0; i < atomCount; i++) {
            Atom atom = atomSetCollection.addNewAtom();
            String[] tokens = getTokens(vInputCoords.get(i));
            int atomicNumber, offset;
            if (tokens.length == 7) {
                atomicNumber = getAtomicNumber(tokens[2]);
                offset = 2;
            } else {
                atomicNumber = getAtomicNumber(tokens[1]);
                offset = 0;
            }
            float x = parseFloat(tokens[2 + offset]) + ptOriginShift.x;
            float y = parseFloat(tokens[3 + offset]) + ptOriginShift.y;
            float z = parseFloat(tokens[4 + offset]) + ptOriginShift.z;
            setAtomCoord(atom, x, y, z);
            atom.elementSymbol = getElementSymbol(atomicNumber);
        }
        vInputCoords = null;
    }

    private void newAtomSet() throws Exception {
        if (atomSetCollection.getAtomCount() > 0) {
            applySymmetryAndSetTrajectory();
            atomSetCollection.newAtomSet();
        }
        if (spaceGroupName != null) setSpaceGroupName(spaceGroupName);
    }

    private void readEnergy() {
        line = TextFormat.simpleReplace(line, "( ", "(");
        String[] tokens = getTokens();
        energy = Double.valueOf(Double.parseDouble(tokens[2]));
        setEnergy();
    }

    private void setEnergy() {
        atomSetCollection.setAtomSetEnergy("" + energy, energy.floatValue());
        atomSetCollection.setAtomSetAuxiliaryInfo("Energy", energy);
        atomSetCollection.setAtomSetCollectionAuxiliaryInfo("Energy", energy);
        atomSetCollection.setAtomSetName("Energy = " + energy + " Hartree");
    }

    private boolean readPartialCharges() throws Exception {
        if (haveCharges || atomSetCollection.getAtomCount() == 0) return true;
        haveCharges = true;
        readLines(3);
        Atom[] atoms = atomSetCollection.getAtoms();
        int i0 = atomSetCollection.getLastAtomSetAtomIndex();
        int iPrim = 0;
        while (readLine() != null && line.length() > 3) if (line.charAt(3) != ' ') {
            int iConv = getAtomIndexFromPrimitiveIndex(iPrim);
            if (iConv >= 0) atoms[i0 + iConv].partialCharge = parseFloat(line.substring(9, 11)) - parseFloat(line.substring(12, 18));
            iPrim++;
        }
        return true;
    }

    private boolean readTotalAtomicCharges() throws Exception {
        StringBuffer data = new StringBuffer();
        while (readLine() != null && line.indexOf("T") < 0) data.append(line);
        String[] tokens = getTokens(data.toString());
        float[] charges = new float[tokens.length];
        if (nuclearCharges == null) nuclearCharges = charges;
        if (atomSetCollection.getAtomCount() == 0) return true;
        Atom[] atoms = atomSetCollection.getAtoms();
        int i0 = atomSetCollection.getLastAtomSetAtomIndex();
        for (int i = 0; i < charges.length; i++) {
            int iConv = getAtomIndexFromPrimitiveIndex(i);
            if (iConv >= 0) {
                charges[i] = parseFloat(tokens[i]);
                atoms[i0 + iConv].partialCharge = nuclearCharges[i] - charges[i];
            }
        }
        return true;
    }

    private int getAtomIndexFromPrimitiveIndex(int iPrim) {
        return (primitiveToIndex == null ? iPrim : primitiveToIndex[iPrim]);
    }

    private boolean readFragments() throws Exception {
        int numAtomsFrag = parseInt(line.substring(39, 44));
        if (numAtomsFrag < 0) return true;
        atomFrag = new int[numAtomsFrag];
        String Sfrag = "";
        while (readLine() != null && line.indexOf("(") >= 0) Sfrag += line;
        Sfrag = TextFormat.simpleReplace(Sfrag, "(", " ");
        Sfrag = TextFormat.simpleReplace(Sfrag, ")", " ");
        String[] tokens = getTokens(Sfrag);
        for (int i = 0, pos = 0; i < numAtomsFrag; i++, pos += 3) atomFrag[i] = getAtomIndexFromPrimitiveIndex(parseInt(tokens[pos]) - 1);
        Arrays.sort(atomFrag);
        return true;
    }

    private boolean readFrequencies() throws Exception {
        energy = null;
        discardLinesUntilContains("MODES");
        boolean haveIntensities = (line.indexOf("INTENS") >= 0);
        readLine();
        List<String[]> vData = new ArrayList<String[]>();
        int freqAtomCount = atomCount;
        while (readLine() != null && line.length() > 0) {
            int i0 = parseInt(line.substring(1, 5));
            int i1 = parseInt(line.substring(6, 10));
            String irrep = (isLongMode ? line.substring(48, 51) : line.substring(49, 52)).trim();
            String intens = (!haveIntensities ? "not available" : (isLongMode ? line.substring(53, 61) : line.substring(59, 69).replace(')', ' ')).trim());
            String irActivity = (isLongMode ? "A" : line.substring(55, 58).trim());
            String ramanActivity = (isLongMode ? "I" : line.substring(71, 73).trim());
            String[] data = new String[] { irrep, intens, irActivity, ramanActivity };
            for (int i = i0; i <= i1; i++) vData.add(data);
        }
        discardLinesUntilContains(isLongMode ? "LO MODES FOR IRREP" : isVersion3 ? "THE CORRESPONDING MODES" : "NORMAL MODES NORMALIZED TO CLASSICAL AMPLITUDES");
        readLine();
        int lastAtomCount = -1;
        while (readLine() != null && line.startsWith(" FREQ(CM**-1)")) {
            String[] tokens = getTokens(line.substring(15));
            float[] frequencies = new float[tokens.length];
            int frequencyCount = frequencies.length;
            for (int i = 0; i < frequencyCount; i++) {
                frequencies[i] = parseFloat(tokens[i]);
                if (Logger.debugging) Logger.debug((vibrationNumber + i) + " frequency=" + frequencies[i]);
            }
            boolean[] ignore = new boolean[frequencyCount];
            int iAtom0 = 0;
            int nData = vData.size();
            for (int i = 0; i < frequencyCount; i++) {
                tokens = vData.get(vibrationNumber % nData);
                ignore[i] = (!doGetVibration(++vibrationNumber) || tokens == null);
                if (ignore[i]) continue;
                applySymmetryAndSetTrajectory();
                lastAtomCount = cloneLastAtomSet(atomCount, null);
                if (i == 0) iAtom0 = atomSetCollection.getLastAtomSetAtomIndex();
                setFreqValue(frequencies[i], tokens);
            }
            readLine();
            fillFrequencyData(iAtom0, freqAtomCount, lastAtomCount, ignore, false, 14, 10, atomFrag);
            readLine();
        }
        return true;
    }

    private void setFreqValue(float freq, String[] data) {
        String activity = "IR: " + data[2] + ", Ram.: " + data[3];
        atomSetCollection.setAtomSetFrequency(null, activity, "" + freq, null);
        atomSetCollection.setAtomSetModelProperty("IRintensity", data[1] + " km/Mole");
        atomSetCollection.setAtomSetModelProperty("vibrationalSymmetry", data[0]);
        atomSetCollection.setAtomSetModelProperty("IRactivity", data[2]);
        atomSetCollection.setAtomSetModelProperty("Ramanactivity", data[3]);
        atomSetCollection.setAtomSetName((isLongMode ? "LO " : "") + data[0] + " " + TextFormat.formatDecimal(freq, 2) + " cm-1 (" + TextFormat.formatDecimal(Float.parseFloat(data[1]), 0) + " km/Mole), " + activity);
    }

    private boolean getPropertyTensors() throws Exception {
        readLines(6);
        Atom[] atoms = atomSetCollection.getAtoms();
        while (readLine() != null && line.startsWith(" *** ATOM")) {
            String[] tokens = getTokens();
            int index = parseInt(tokens[3]) - 1;
            tokens = getTokens(readLines(3));
            atoms[index].setEllipsoid(Eigen.getEllipsoid(directLatticeVectors, new float[] { parseFloat(tokens[1]), parseFloat(tokens[3]), parseFloat(tokens[5]) }, false));
            readLine();
        }
        return true;
    }
}
