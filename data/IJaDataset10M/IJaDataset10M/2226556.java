package org.xmlcml.noncml;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import org.xmlcml.cml.AbstractBase;
import org.xmlcml.cml.CMLAtom;
import org.xmlcml.cml.CMLBond;
import org.xmlcml.cml.CMLCoordinate3;
import org.xmlcml.cml.CMLCrystal;
import org.xmlcml.cml.CMLException;
import org.xmlcml.cml.CMLFloatArray;
import org.xmlcml.cml.CMLFloatVal;
import org.xmlcml.cml.CMLIntegerVal;
import org.xmlcml.cml.CMLIntegerArray;
import org.xmlcml.cml.CMLList;
import org.xmlcml.cml.CMLMolecule;
import org.xmlcml.cml.CMLStringVal;
import org.xmlcml.cml.metadata.CMLDictionary;
import org.xmlcml.cml.metadata.CMLDictionaryEntry;
import org.xmlcml.molutil.ChemicalElement;
import org.xmlcml.cml.subset.SpanningTree;
import org.xmlcml.cml.normalise.NormalMolecule;
import org.xmlcml.cmlimpl.AtomImpl;
import org.xmlcml.cmlimpl.BondImpl;
import org.xmlcml.cmlimpl.Coord3;
import org.xmlcml.cmlimpl.CrystalImpl;
import org.xmlcml.cmlimpl.FloatArrayImpl;
import org.xmlcml.cmlimpl.FloatValImpl;
import org.xmlcml.cmlimpl.IntegerArrayImpl;
import org.xmlcml.cmlimpl.IntegerValImpl;
import org.xmlcml.cmlimpl.ListImpl;
import org.xmlcml.cmlimpl.MoleculeImpl;
import org.xmlcml.cmlimpl.StringValImpl;
import org.xmlcml.cmlimpl.metadata.DictionaryImpl;
import org.xmlcml.cmlimpl.subset.SpanningTreeImpl;
import jumbo.euclid.IntArray;
import jumbo.euclid.Point3;
import jumbo.euclid.RealArray;
import jumbo.euclid.RealSquareMatrix;
import uk.co.demon.ursus.dom.PMRDelegate;
import uk.co.demon.ursus.dom.PMRNode;
import jumbo.xml.util.Util;
import uk.co.demon.ursus.util.Selector;
import uk.co.demon.ursus.util.SelectorImpl;

/** class to read (? and write?) CASTEP files
<P>
NOT COMPLETE
@author (C) P. Murray-Rust, 1996, 2001
*/
public class CASTEPImpl extends NonCMLDocumentImpl implements CASTEP {

    public static final Hashtable globalKeywordTable = new Hashtable();

    int iterCount;

    String line;

    /** form a CASTEP object from a local file
@exception BadFileException file was not a standard CASTEP file
*/
    public CASTEPImpl() {
        super();
        DummySelector();
    }

    private void DummySelector() {
        selector = new SelectorImpl();
        boolean skippable = false;
        skippable = true;
    }

    /** form an CASTEP object from a local file
@exception Exception file was not a standard CASTEP file
*/
    public CASTEPImpl(BufferedReader bReader, String id) throws Exception {
        this();
        createCMLElement(CASTEP, id);
        parse(bReader);
    }

    /** form a CASTEP object from a CML file
*/
    public CASTEPImpl(CMLMolecule outputCMLMolecule) {
        this();
        setOutputCMLMolecule(outputCMLMolecule);
    }

    public String getDictionaryName() {
        return CASTEP_DICTIONARY_NAME;
    }

    public String getDictionaryPrefix() {
        return CASTEP_DICTIONARY_PREFIX;
    }

    public String getDictionaryNamespaceURI() {
        return CASTEP_DICTIONARY_NAMESPACE_URI;
    }

    public String getDictionaryFileName() {
        return CASTEP_DICTIONARY_FILENAME;
    }

    public void parse(BufferedReader bReader) throws IOException, CMLException {
        this.bReader = bReader;
        boolean ignore = false;
        createAndAddCMLElement(CASTEP, "");
        int count = 0;
        while (true) {
            line = getCurrentLine();
            count++;
            if (line.startsWith("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")) break;
            if (line == null || count > 10) {
                throw new CMLException("CASTEP must start with 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx'");
            }
        }
        line = getCurrentLine();
        while (true) {
            line = getCurrentLine();
            if (line.startsWith(" CASTEP calculation from Cerius2")) {
                line = getCurrentLine();
                if (!line.startsWith(" ******************")) throw new CMLException("CASTEP expecting:' *************************************************'");
                break;
            }
        }
        while (true) {
            line = getCurrentLine();
            if (line.trim().startsWith("--------------------------")) break;
        }
        try {
            while (true) {
                line = getCurrentLine();
                if (line == null) break;
                if (line.trim().equals("")) {
                } else if (line.trim().startsWith("********************************") || line.trim().startsWith("---------------------------") || line.trim().startsWith("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx") || line.trim().startsWith("+++++++++++++++++++++++++++++++")) {
                } else if (line.trim().startsWith("GLOBAL    SETUP    DEFINITIONS")) {
                    readGlobalSetup(ignore);
                } else if (line.trim().startsWith("SETUP VARIABLES FOR CASTEP RUN")) {
                    readSetupVariables(ignore);
                } else if (line.trim().startsWith("CELL  GEOMETRY  FOR  CASTEP  RUN")) {
                    readCellGeometry(ignore);
                } else if (line.trim().startsWith("*                    Pseudopotential Information")) {
                    readPseudopotential(ignore);
                } else if (line.trim().startsWith("x  Element    Atom     Fractional coordinates of atoms    Fixed or  x")) {
                    readFractionals(ignore);
                } else if (line.trim().startsWith("+             Special k-points for Brillouin zone sampling")) {
                    readBrillouin(ignore);
                } else if (line.trim().startsWith("Start initialization of wavefunctions")) {
                    readWavefunctions(ignore);
                } else if (line.trim().startsWith(">>>>>>>>>>>>>>>>>>>> ITERATION =      1")) {
                    readFirstIteration(ignore);
                } else if (line.trim().startsWith(">>>>>>>>>>>>>>>>>>>> ITERATION =")) {
                    readIteration(ignore);
                } else if (line.trim().startsWith(">>>>> Final electronic minimization for iteration =")) {
                    readFinalIteration(ignore);
                } else if (line.trim().startsWith("Total execution time:")) {
                    readFinal(ignore);
                } else {
                    System.out.println("ignored: " + line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CMLException("Error in line: (" + e + ")" + nLine + ":" + currentLine);
        }
    }

    ListImpl globalSetupList;

    public void readGlobalSetup(boolean ignore) throws IOException, CMLException {
        line = getCurrentLine();
        line = getCurrentLine();
        line = getCurrentLine();
        globalSetupList = new ListImpl(this);
        globalSetupList.setTitle("globalSetup");
        globalSetupList.setAttribute("display", "nvlist");
        this.inputCML.appendChild(globalSetupList);
        while (true) {
            line = getCurrentLine();
            if (line.trim().equals("")) break;
            if (ignore) continue;
            if (line.charAt(40) != ':') {
                System.out.println("Cannot parse line: " + line);
                continue;
            }
            String name = line.substring(0, 40).trim();
            String value = line.substring(41).trim();
            CMLDictionaryEntry entry = localDictionary.getEntryByName(name);
            if (entry == null) {
                System.out.println("Unrecognised keyword/quantity: " + line);
                continue;
            }
            AbstractBase ab = null;
            int idx = value.indexOf("(");
            if (idx != -1) value = value.substring(0, idx).trim();
            if (entry.getId().equals("f001")) {
                StringTokenizer st = new StringTokenizer(value, "x");
                if (st.countTokens() != 3) throw new CMLException("Grid requires 3 coords");
                int xyz[] = new int[3];
                xyz[0] = Integer.parseInt(st.nextToken().trim());
                xyz[1] = Integer.parseInt(st.nextToken().trim());
                xyz[2] = Integer.parseInt(st.nextToken().trim());
                ab = new IntegerArrayImpl(this, xyz, null);
                ab.setTitle(entry.getTitle());
            } else {
                ab = makeDataItem(entry, value);
            }
            if (ab != null) {
                globalSetupList.appendChild(ab);
            }
        }
        System.out.print("<<Global setup");
    }

    ListImpl setupList;

    public void readSetupVariables(boolean ignore) throws IOException, CMLException {
        setupList = new ListImpl(this);
        setupList.setTitle("setup");
        setupList.setAttribute("display", "nvlist");
        this.inputCML.appendChild(setupList);
        line = getCurrentLine();
        line = getCurrentLine();
        while (true) {
            line = getCurrentLine();
            if (line == null) return;
            if (line.trim().startsWith("Wavefunctions are stored in memory")) continue;
            if (line.trim().startsWith("The current FFT grid is sufficient for this cutoff")) continue;
            if (line.trim().startsWith("Density mixing scheme with the conjugate gradient solver")) continue;
            if (line.trim().startsWith(" Starting charge density is generated from atomic orbitals")) continue;
            if (line.trim().equals("")) continue;
            if (line.trim().startsWith("Minimum recommended grid is:")) {
                break;
            }
            if (line.charAt(40) != ':') {
                System.out.println("Cannot parse line: " + line);
                continue;
            }
            String name = line.substring(0, 40).trim();
            String value = line.substring(41).trim();
            CMLDictionaryEntry entry = localDictionary.getEntryByName(name);
            if (entry == null) {
                System.out.println("Unrecognised keyword/quantity: " + line);
                continue;
            }
            AbstractBase ab = null;
            int idx = value.indexOf("(");
            if (idx != -1) value = value.substring(0, idx).trim();
            ab = makeDataItem(entry, value);
            if (ab != null) {
                setupList.appendChild(ab);
            }
        }
        System.out.print("<<Read setup");
    }

    ListImpl cellList;

    RealSquareMatrix orthmat = null;

    double cellParam[][] = new double[3][2];

    CMLCrystal crystal;

    public void readCellGeometry(boolean ignore) throws IOException, CMLException {
        cellList = new ListImpl(this);
        cellList.setTitle("cell");
        setupList.appendChild(cellList);
        line = getCurrentLine();
        line = getCurrentLine();
        line = getCurrentLine();
        AbstractBase ab = null;
        CMLDictionaryEntry entry = null;
        for (int i = 0; i < 3; i++) {
            line = getCurrentLine();
            for (int j = 0; j < 2; j++) {
                int offset = j * 36;
                String name = line.substring(offset, offset + 6).trim();
                String value = line.substring(offset + 10, offset + 22).trim();
                cellParam[i][j] = new Double(value).doubleValue();
                entry = localDictionary.getEntryByName(name);
                if (entry == null) {
                    System.out.println("Unrecognised keyword/quantity: " + line + "/" + name);
                    continue;
                }
            }
        }
        crystal = new CrystalImpl(this);
        crystal.setCellLengths(cellParam[0][0], cellParam[1][0], cellParam[2][0]);
        crystal.setCellAngles(cellParam[0][1], cellParam[1][1], cellParam[2][1]);
        orthmat = crystal.getOrthogonalisationMatrix();
        cellList.appendChild(crystal);
        line = getCurrentLine();
        line = getCurrentLine();
        String name = "Cell volume";
        String value = line.substring(22, 36).trim();
        entry = localDictionary.getEntryByName(name);
        if (entry == null) {
            System.out.println("Unrecognised keyword/quantity: " + line + "/" + name);
        } else {
            try {
                ab = makeDataItem(entry, value);
                if (ab != null) {
                    cellList.appendChild(ab);
                }
            } catch (Exception e) {
                Util.bug(e);
            }
        }
        System.out.print("<<Read cell");
    }

    CMLList pspotList;

    public void readPseudopotential(boolean ignore) throws IOException {
        line = getCurrentLine();
        line = getCurrentLine();
        line = getCurrentLine();
        line = getCurrentLine();
        pspotList = new ListImpl(this);
        setupList.appendChild(pspotList);
        while (true) {
            line = getCurrentLine();
            if (line == null) return;
            if (line.trim().startsWith("*************************************************")) {
                break;
            }
            String elem = line.substring(1, 8).trim();
            String type = line.substring(10, 32).trim();
            String projs = line.substring(33, 70).trim();
            CMLList pspot = new ListImpl(this);
            pspot.setTitle("pseudopotential");
            CMLStringVal sv = new StringValImpl(this, elem, null);
            sv.setTitle("element");
            pspot.appendChild(sv);
            sv = new StringValImpl(this, type, null);
            sv.setTitle("type");
            pspot.appendChild(sv);
            StringTokenizer st = new StringTokenizer(projs.trim());
            while (st.hasMoreTokens()) {
                sv = new StringValImpl(this, st.nextToken(), null);
                sv.setTitle("nonlocalProjector");
                pspot.appendChild(sv);
            }
            pspotList.appendChild(pspot);
        }
        System.out.print("<<Read Pseudopotential");
    }

    public void readFractionals(boolean ignore) throws IOException, CMLException {
        line = getCurrentLine();
        line = getCurrentLine();
        CMLMolecule molecule = MOLECULE_FACTORY.createMolecule(this);
        setupList.appendChild(molecule);
        while (true) {
            line = getCurrentLine();
            if (line == null) return;
            if (line.trim().startsWith("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")) {
                break;
            }
            StringTokenizer st = new StringTokenizer(line);
            st.nextToken();
            String elem = st.nextToken();
            String id = elem + st.nextToken();
            Coord3 coord3 = new Coord3(new Double(st.nextToken()).doubleValue(), new Double(st.nextToken()).doubleValue(), new Double(st.nextToken()).doubleValue());
            try {
                CMLAtom atom = ATOM_FACTORY.createAtom(molecule, id);
                atom.setElementType(elem);
                atom.setXYZFract(coord3);
            } catch (Exception e) {
                Util.bug(e);
            }
        }
        molecule.convertFractionalToCartesian(crystal);
        ((org.xmlcml.cmlimpl.jumbo3.JUMBOMoleculeImpl) molecule).calculateBondsFromXYZ3(0.3);
        System.out.print("<<Read Fractionals");
    }

    public void readBrillouin(boolean ignore) throws IOException {
        while (true) {
            line = getCurrentLine();
            if (line == null) return;
            if (line.trim().startsWith("+++++++++++++++++++++++++++++++")) {
                break;
            }
        }
        System.out.print("<<Read Brillouin");
    }

    public void readWavefunctions(boolean ignore) throws IOException {
        while (true) {
            line = getCurrentLine();
            if (line == null) return;
            if (line.trim().startsWith("Finished initialization of wavefunctions")) {
                break;
            }
        }
        System.out.print("<<Read Wavefunctions");
    }

    CMLList iterList;

    private CMLList addIterList(String s) throws CMLException {
        int iter = Integer.parseInt(s.trim());
        if (iter != ++iterCount) throw new CMLException("Bad iteration count: " + line);
        s = "iteration " + iter;
        iterList = new ListImpl(this);
        iterList.setTitle(s);
        this.inputCML.appendChild(iterList);
        return iterList;
    }

    public void readIteration(boolean ignore) throws IOException, CMLException {
        CMLList iterList = addIterList(line.substring(34, 40).trim());
        while (true) {
            line = getCurrentLine();
            if (line == null) return;
            if (line.trim().startsWith("SCF loop        Energy (eV)          Energy gain      Timer")) {
                readSCFLoop();
                iterList.appendChild(scfLoop);
            }
            if (line.trim().startsWith("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")) {
                break;
            }
        }
        System.out.print("<<Read iteration");
    }

    CMLList scfLoop;

    public void readSCFLoop() throws IOException, CMLException {
        scfLoop = new ListImpl(this);
        scfLoop.setTitle("SCF Loop");
        scfLoop.setAttribute("display", "nvtable");
        line = getCurrentLine();
        line = getCurrentLine();
        int count = 0;
        RealArray eInitArray = new RealArray();
        RealArray eFinalArray = new RealArray();
        RealArray eAtomArray = new RealArray();
        RealArray timerArray = new RealArray();
        while (true) {
            line = getCurrentLine();
            if (line == null) return;
            if (line.trim().equals("")) {
                line = getCurrentLine();
                if (!line.trim().startsWith("TOTAL ENERGY IS")) throw new CMLException("Bad line: " + line);
                double totalE = new Double(line.substring(17, 45)).doubleValue();
                CMLFloatVal fv = new FloatValImpl(this, totalE, null);
                fv.setTitle("total Energy");
                fv.setDictRef("totalE");
                break;
            }
            String line0 = line;
            int idx = line.indexOf("<-- SCF");
            if (idx != -1) line = line.substring(0, idx);
            StringTokenizer st = new StringTokenizer(line);
            int c = Integer.parseInt(st.nextToken());
            if (c != ++count) throw new CMLException("Bad line: " + line0);
            double eInit = new Double(st.nextToken()).doubleValue();
            double eFinal = new Double(st.nextToken()).doubleValue();
            double eAtom = new Double(st.nextToken()).doubleValue();
            double timer = new Double(st.nextToken()).doubleValue();
            eInitArray.addElement(eInit);
            eFinalArray.addElement(eFinal);
            eAtomArray.addElement(eAtom);
            timerArray.addElement(timer);
        }
        addDataItem(scfLoop, "e003", eInitArray);
        addDataItem(scfLoop, "e004", eFinalArray);
        addDataItem(scfLoop, "p003", eAtomArray);
        addDataItem(scfLoop, "t008", timerArray);
        System.out.print("  <<Read SCF");
    }

    public void readFirstIteration(boolean ignore) throws IOException, CMLException {
        iterList = addIterList(line.substring(34, 40).trim());
        line = getCurrentLine();
        while (true) {
            line = getCurrentLine();
            if (line == null) return;
            if (line.trim().startsWith("----------------------------------")) {
            } else if (line.trim().startsWith("SCF loop        Energy (eV)          Energy gain      Timer")) {
                readSCFLoop();
                iterList.appendChild(scfLoop);
            } else if (line.trim().equals("")) {
                break;
            } else {
                System.out.println("ignored: " + line);
            }
        }
        System.out.print("<<Read first iteration");
    }

    CMLList finalIterList;

    public void readFinalIteration(boolean ignore) throws IOException, CMLException {
        finalIterList = new ListImpl(this);
        finalIterList.setTitle("final electronic minimization");
        iterList.appendChild(finalIterList);
        while (true) {
            line = peekLine();
            if (line == null) return;
            if (line.trim().startsWith("Total execution time:")) {
                break;
            }
            line = getCurrentLine();
            if (line.trim().equals("")) continue;
            if (line.trim().startsWith("SCF loop        Energy (eV)          Energy gain      Timer")) {
                readSCFLoop();
                finalIterList.appendChild(scfLoop);
            }
            if (line.trim().startsWith("+                       Electronic energies")) {
                CMLList finalElectronic = readFinalElectronic();
                finalIterList.appendChild(finalElectronic);
            }
            if (line.trim().startsWith("Element  Atom     Fractional coordinates       Forces")) {
                CMLMolecule finalMol = readFinalMolecule();
                finalIterList.appendChild(finalMol);
            }
        }
        System.out.print("<<Read final iteration");
    }

    public CMLMolecule readFinalMolecule() throws IOException, CMLException {
        line = getCurrentLine();
        line = getCurrentLine();
        line = getCurrentLine();
        CMLMolecule molecule = MOLECULE_FACTORY.createMolecule(this);
        setupList.appendChild(molecule);
        while (true) {
            line = getCurrentLine();
            if (line == null) return null;
            if (line.trim().startsWith("----------------------------")) {
                break;
            }
            StringTokenizer st = new StringTokenizer(line);
            String elem = st.nextToken();
            String id = elem + st.nextToken();
            Coord3 coord3 = new Coord3(new Double(st.nextToken()).doubleValue(), new Double(st.nextToken()).doubleValue(), new Double(st.nextToken()).doubleValue());
            try {
                CMLAtom atom = ATOM_FACTORY.createAtom(molecule, id);
                atom.setElementType(elem);
                atom.setXYZFract(coord3);
            } catch (Exception e) {
                Util.bug(e);
            }
        }
        molecule.convertFractionalToCartesian(crystal);
        ((org.xmlcml.cmlimpl.jumbo3.JUMBOMoleculeImpl) molecule).calculateBondsFromXYZ3(0.3);
        System.out.print("<<Read Final Molecule");
        return molecule;
    }

    public CMLList readFinalElectronic() throws IOException, CMLException {
        CMLList finalElecList = new ListImpl(this);
        finalElecList.setTitle("final electronic energies");
        line = getCurrentLine();
        line = getCurrentLine();
        line = getCurrentLine();
        line = getCurrentLine();
        line = getCurrentLine();
        line = getCurrentLine();
        line = getCurrentLine();
        CMLStringVal sv = new StringValImpl(this, line.trim(), null);
        sv.setTitle("k-point");
        finalElecList.appendChild(sv);
        line = getCurrentLine();
        line = getCurrentLine();
        IntArray bArray = new IntArray();
        RealArray eArray = new RealArray();
        RealArray oArray = new RealArray();
        while (true) {
            line = getCurrentLine();
            if (line == null) return null;
            if (line.trim().startsWith("+                    ")) {
                line = getCurrentLine();
                break;
            }
            StringTokenizer st = new StringTokenizer(line);
            st.nextToken();
            try {
                bArray.addElement(Integer.parseInt(st.nextToken()));
                eArray.addElement(new Double(st.nextToken()).doubleValue());
                oArray.addElement(new Double(st.nextToken()).doubleValue());
            } catch (NumberFormatException nfe) {
                System.out.println("Bad number in line: " + line);
            }
        }
        addDataItem(finalElecList, "b003", bArray);
        addDataItem(finalElecList, "e005", eArray);
        addDataItem(finalElecList, "o002", oArray);
        System.out.print("<<Read final electronic");
        return finalElecList;
    }

    public void readFinal(boolean ignore) throws IOException {
        while (true) {
            line = getCurrentLine();
            if (line == null) return;
            if (line.trim().startsWith("CASTEP job finished")) {
                break;
            }
        }
        System.out.print("<<Read final");
    }

    /** outputs CMLMolecule as CASTEP if possible. CASTEP.
@param Writer writer to output it to
*/
    public String output(Writer writer) throws CMLException, IOException {
        getAndCheckVectors();
        writeHeader(writer);
        return writer.toString();
    }

    public void writeHeader(Writer w) throws IOException {
        w.write("CASTEP output\n");
    }

    public static void main(String[] args) {
        String omit = "";
        if (args.length > 1) {
            omit = args[1];
        }
        CASTEP castep = null;
        try {
        } catch (Exception e) {
            System.out.println("CASTEP failed: " + e);
            e.printStackTrace();
            System.exit(0);
        }
    }
}
