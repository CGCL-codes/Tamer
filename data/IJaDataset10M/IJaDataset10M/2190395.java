package org.openscience.cdk.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import org.openscience.cdk.config.AtomTypeFactory;
import org.openscience.cdk.config.IsotopeFactory;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IElement;
import org.openscience.cdk.interfaces.IIsotope;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

/**
 * Analyses a molecular formula given in String format and builds
 * an AtomContainer with the Atoms in the molecular formula.
 * 
 * About implict H handling: By default the methods to calculate formula, natural and canonical mass
 * use the explicit Hs and only the explicit Hs if there is at least one in the molecule, implicit Hs are 
 * ignored. If there is no explicit H and only then the implicit Hs are used. If you use the constructor 
 * MFAnalyser(IAtomContainer ac, boolean useboth) and set useboth to true, all explicit Hs and all implicit Hs are used, 
 * the implicit ones also on atoms with explicit Hs.
 *
 * @author         seb
 * @cdk.created    13. April 2005
 * @cdk.module     standard
 * @cdk.keyword    molecule, molecular mass
 * @cdk.keyword    molecule, molecular formula
 * @cdk.keyword    molecule, double bond equivalents
 * @cdk.bug        1647096
 */
public class MFAnalyser {

    private static final String H_ELEMENT_SYMBOL = "H";

    private String MF;

    private IAtomContainer atomContainer;

    private int HCount = 0;

    static HashMap massMap = new HashMap();

    private boolean useboth = false;

    private LoggingTool logger = new LoggingTool(MFAnalyser.class);

    static {
        massMap.put("1", "1.00794");
        massMap.put("2", "4.002602");
        massMap.put("3", "6.941");
        massMap.put("4", "9.012182");
        massMap.put("5", "10.811");
        massMap.put("6", "12.0107");
        massMap.put("7", "14.0067");
        massMap.put("8", "15.9994");
        massMap.put("9", "18.9984032");
        massMap.put("10", "20.1797");
        massMap.put("11", "22.989770");
        massMap.put("12", "24.3050");
        massMap.put("13", "26.981538");
        massMap.put("14", "28.0855");
        massMap.put("15", "30.973761");
        massMap.put("16", "32.065");
        massMap.put("17", "35.453");
        massMap.put("18", "39.948");
        massMap.put("19", "39.0983");
        massMap.put("20", "40.078");
        massMap.put("21", "44.955910");
        massMap.put("22", "47.867");
        massMap.put("23", "50.9415");
        massMap.put("24", "51.9961");
        massMap.put("25", "54.938049");
        massMap.put("26", "55.845");
        massMap.put("27", "58.933200");
        massMap.put("28", "58.6934");
        massMap.put("29", "	63.546");
        massMap.put("30", "65.409");
        massMap.put("31", "69.723");
        massMap.put("32", "72.64");
        massMap.put("33", "74.92160");
        massMap.put("34", "78.96");
        massMap.put("35", "79.904");
        massMap.put("36", "83.798");
        massMap.put("37", "85.4678");
        massMap.put("38", "87.62");
        massMap.put("39", "88.90585");
        massMap.put("40", "91.224");
        massMap.put("41", "92.90638");
        massMap.put("42", "95.94");
        massMap.put("43", "98");
        massMap.put("44", "101.07");
        massMap.put("45", "102.90550");
        massMap.put("46", "106.42");
        massMap.put("47", "107.8682");
        massMap.put("48", "112.411");
        massMap.put("49", "114.818");
        massMap.put("50", "118.710");
        massMap.put("51", "121.760");
        massMap.put("52", "127.60");
        massMap.put("53", "126.90447");
        massMap.put("54", "131.293");
        massMap.put("55", "132.90545");
        massMap.put("56", "137.327");
        massMap.put("57", "138.9055");
        massMap.put("58", "140.116");
        massMap.put("59", "140.90765");
        massMap.put("60", "144.24");
        massMap.put("61", "145");
        massMap.put("62", "150.36");
        massMap.put("63", "151.964");
        massMap.put("64", "157.25");
        massMap.put("65", "158.92534");
        massMap.put("66", "162.500");
        massMap.put("67", "164.93032");
        massMap.put("68", "167.259");
        massMap.put("69", "168.93421");
        massMap.put("70", "173.04");
        massMap.put("71", "174.967");
        massMap.put("72", "178.49");
        massMap.put("73", "180.9479");
        massMap.put("74", "183.84");
        massMap.put("75", "186.207");
        massMap.put("76", "190.23");
        massMap.put("77", "192.217");
        massMap.put("78", "195.078");
        massMap.put("79", "196.96655");
        massMap.put("80", "200.59");
        massMap.put("81", "204.3833");
        massMap.put("82", "207.2");
        massMap.put("83", "208.98038");
        massMap.put("84", "209");
        massMap.put("85", "210");
        massMap.put("86", "222");
        massMap.put("87", "223");
        massMap.put("88", "226");
        massMap.put("89", "227");
        massMap.put("90", "232.0381");
        massMap.put("91", "231.03588");
        massMap.put("92", "238.02891");
        massMap.put("93", "237");
        massMap.put("94", "244");
        massMap.put("95", "243");
        massMap.put("96", "247");
        massMap.put("97", "247");
        massMap.put("98", "251");
        massMap.put("99", "252");
        massMap.put("100", "257");
        massMap.put("101", "258");
        massMap.put("102", "259");
        massMap.put("103", "262");
        massMap.put("104", "261");
        massMap.put("105", "262");
        massMap.put("106", "266");
        massMap.put("107", "264");
        massMap.put("108", "277");
        massMap.put("109", "268");
        massMap.put("110", "281");
        massMap.put("111", "272");
        massMap.put("112", "285");
        massMap.put("113", "284");
        massMap.put("114", "289");
        massMap.put("115", "288");
        massMap.put("116", "292");
    }

    /**
	 * Construct an instance of MFAnalyser, initialized with a molecular
	 * formula string. The string is immediatly analysed and a set of Nodes
	 * is built based on this analysis
	 *
	 * @param  MF  Description of the Parameter
	 * @param target TODO
	 */
    public MFAnalyser(String MF, IAtomContainer target) {
        this.MF = MF;
        this.atomContainer = analyseMF(MF, target);
    }

    /**
	 * Construct an instance of MFAnalyser, initialized with a set of Nodes
	 * The set is analysed and a molecular formular is constructed
	 *  based on this analysis
	 *
	 * @param  ac  Description of the Parameter
	 */
    public MFAnalyser(IAtomContainer ac) {
        this(ac, false);
    }

    /**
	 * Construct an instance of MFAnalyser, initialized with a set of Nodes
	 * The set is analysed and a molecular formular is constructed
	 *  based on this analysis
	 *
	 * @param  ac  Description of the Parameter
	 * @param  useboth true=implicit and explicit hs will be used, false=explicit used, implicit only if no explicit
	 */
    public MFAnalyser(IAtomContainer ac, boolean useboth) {
        this.useboth = useboth;
        this.atomContainer = ac;
        this.MF = analyseAtomContainer(ac);
    }

    /**
	 * returns the complete set of Nodes, as implied by the molecular
	 * formula, including all the hydrogens.
	 *
	 * @return    The atomContainer value
	 */
    public IAtomContainer getAtomContainer() {
        return atomContainer;
    }

    /**
	 * Returns the complete set of Nodes, as implied by the molecular
	 * formula, including all the hydrogens.
	 *
	 * @return    The molecularFormula value
	 * @see       #getHTMLMolecularFormula()
	 */
    public String getMolecularFormula() {
        return MF;
    }

    /**
	 * Returns the string representation of the molecule formula with
	 * numbers wrapped in &lt;sub&gt;&lt;/sub&gt; tags. Useful for displaying
	 * formulae in Swing components or on the web.
	 *
	 * @return    A HTML representation of the molecular formula.
	 */
    public String getHTMLMolecularFormula() {
        boolean lastCharacterWasDigit = false;
        boolean currentCharacterIsDigit;
        StringBuffer htmlString = new StringBuffer(MF);
        for (int characterCounter = 0; characterCounter <= htmlString.length(); characterCounter++) {
            try {
                currentCharacterIsDigit = Character.isDigit(htmlString.charAt(characterCounter));
            } catch (StringIndexOutOfBoundsException oobe) {
                currentCharacterIsDigit = false;
            }
            if (currentCharacterIsDigit && !lastCharacterWasDigit) {
                htmlString.insert(characterCounter, "<sub>");
                characterCounter += 5;
            } else if (lastCharacterWasDigit && !currentCharacterIsDigit) {
                htmlString.insert(characterCounter, "</sub>");
                characterCounter += 6;
            }
            lastCharacterWasDigit = currentCharacterIsDigit;
        }
        return htmlString.toString();
    }

    /**
	 * Returns the number of double bond equivalents in this molecule.
	 *
	 * @return      The number of DBEs
	 * @cdk.keyword DBE
	 * @cdk.keyword double bond equivalent
	 */
    public float getDBE() throws IOException, ClassNotFoundException, CDKException {
        int valencies[] = new int[5];
        AtomTypeFactory factory = AtomTypeFactory.getInstance("org/openscience/cdk/config/data/structgen_atomtypes.xml", getAtomContainer().getBuilder());
        IAtomContainer ac = getAtomContainer();
        for (int f = 0; f < ac.getAtomCount(); f++) {
            IAtomType[] types = factory.getAtomTypes(ac.getAtom(f).getSymbol());
            if (types.length == 0) throw new CDKException("Calculation of double bond equivalents not possible due to problems with element " + ac.getAtom(f).getSymbol());
            valencies[(int) types[0].getBondOrderSum() + ac.getAtom(f).getFormalCharge()]++;
        }
        return 1 + (valencies[4]) + (valencies[3] / 2) - (valencies[1] / 2);
    }

    /**
	 * returns the exact mass for a given molecular formula, using major isotope for each element.
	 *
	 * @return    The mass value
	 */
    public float getMass() {
        float mass = 0;
        IIsotope i;
        IsotopeFactory si = null;
        try {
            si = IsotopeFactory.getInstance(getAtomContainer().getBuilder());
        } catch (Exception exception) {
            System.err.println("Could not instantiate the IsotopeFactory: " + exception.getMessage());
        }
        IAtomContainer ac = getAtomContainer();
        IIsotope h = si.getMajorIsotope(H_ELEMENT_SYMBOL);
        for (int f = 0; f < ac.getAtomCount(); f++) {
            i = si.getMajorIsotope(ac.getAtom(f).getSymbol());
            if (i != null) {
                mass += i.getExactMass();
            } else {
                return 0;
            }
            mass += ac.getAtom(f).getHydrogenCount() * h.getExactMass();
        }
        return mass;
    }

    /**
	 *  Gets the natural mass of this element, defined as average of masses of isotopes, weighted by abundance.
	 *
	 * @param  element                     Description of the Parameter
	 * @return                             The natural mass value
	 * @exception  java.io.IOException     Description of the Exception
	 * @exception  ClassNotFoundException  Description of the Exception
	 */
    public static double getNaturalMass(IElement element) throws java.io.IOException, ClassNotFoundException {
        IIsotope[] isotopes = IsotopeFactory.getInstance(element.getBuilder()).getIsotopes(element.getSymbol());
        double summedAbundances = 0;
        double summedWeightedAbundances = 0;
        double getNaturalMass = 0;
        for (int i = 0; i < isotopes.length; i++) {
            summedAbundances += isotopes[i].getNaturalAbundance();
            summedWeightedAbundances += isotopes[i].getNaturalAbundance() * isotopes[i].getExactMass();
            getNaturalMass = summedWeightedAbundances / summedAbundances;
        }
        return getNaturalMass;
    }

    public static double getCanonicalMass(IElement element) {
        return Double.parseDouble((String) massMap.get(element.getAtomicNumber() + ""));
    }

    /**
	 * returns the exact mass for a given molecular formula, using using IUPAC official masses published in Pure Appl. Chem., (2003) 75, 1107-1122. 
	 *
	 * @return                             The naturalMass value
	 * @exception  java.io.IOException     Description of the Exception
	 * @exception  ClassNotFoundException  Description of the Exception
	 */
    public float getCanonicalMass() throws java.io.IOException, ClassNotFoundException {
        float mass = 0;
        IsotopeFactory si = null;
        try {
            si = IsotopeFactory.getInstance(getAtomContainer().getBuilder());
        } catch (Exception exception) {
            System.err.println("Could not instantiate the IsotopeFactory: " + exception.getMessage());
        }
        IAtomContainer ac = getAtomContainer();
        IIsotope h = si.getMajorIsotope("H");
        Map symbols = this.getSymolMap(ac);
        Iterator it = symbols.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (key.equals("H")) {
                if (useboth) {
                    mass += getCanonicalMass(h) * HCount;
                } else {
                    if (symbols.get(key) != null) {
                        mass += getCanonicalMass(h) * ((Integer) symbols.get(key)).intValue();
                    } else {
                        mass += getCanonicalMass(h) * HCount;
                    }
                }
            } else {
                IElement i = si.getElement(key);
                mass += getCanonicalMass(i) * ((Integer) symbols.get(key)).intValue();
            }
        }
        return mass;
    }

    /**
	 * returns the exact mass for a given molecular formula, using weighted average of isotopes.
	 *
	 * @return                             The naturalMass value
	 * @exception  java.io.IOException     Description of the Exception
	 * @exception  ClassNotFoundException  Description of the Exception
	 */
    public float getNaturalMass() throws java.io.IOException, ClassNotFoundException {
        float mass = 0;
        IsotopeFactory si = null;
        try {
            si = IsotopeFactory.getInstance(getAtomContainer().getBuilder());
        } catch (Exception exception) {
            System.err.println("Could not instantiate the IsotopeFactory: " + exception.getMessage());
        }
        IAtomContainer ac = getAtomContainer();
        IIsotope h = si.getMajorIsotope("H");
        Map symbols = this.getSymolMap(ac);
        Iterator it = symbols.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (key.equals("H")) {
                if (useboth) {
                    mass += this.getNaturalMass(h) * HCount;
                } else {
                    if (symbols.get(key) != null) {
                        mass += getNaturalMass(h) * ((Integer) symbols.get(key)).intValue();
                    } else {
                        mass += getNaturalMass(h) * HCount;
                    }
                }
            } else {
                IElement i = si.getElement(key);
                mass += getNaturalMass(i) * ((Integer) symbols.get(key)).intValue();
            }
        }
        return mass;
    }

    /**
	 * Produces an AtomContainer without explicit Hs but with H count from one with Hs.
	 * Hs bonded to more than one heavy atom are preserved.  The new molecule is a deep copy.
	 *
	 * @return         The mol without Hs.
	 * @cdk.keyword    hydrogen, removal
	 */
    public IAtomContainer removeHydrogensPreserveMultiplyBonded() {
        IAtomContainer ac = getAtomContainer();
        List h = new ArrayList();
        List multi_h = new ArrayList();
        int count = ac.getBondCount();
        for (int i = 0; i < count; i++) {
            java.util.Iterator atoms = ac.getBond(i).atoms();
            while (atoms.hasNext()) {
                final IAtom atom = (IAtom) atoms.next();
                if (atom.getSymbol().equals(H_ELEMENT_SYMBOL)) {
                    (h.contains(atom) ? multi_h : h).add(atom);
                }
            }
        }
        return removeHydrogens(multi_h);
    }

    /**
	 * Produces an AtomContainer without explicit Hs (except those listed) but with H count from one with Hs.
	 * The new molecule is a deep copy.
	 *
	 * @param  preserve  a list of H atoms to preserve.
	 * @return           The mol without Hs.
	 * @cdk.keyword      hydrogen, removal
	 */
    private IAtomContainer removeHydrogens(List preserve) {
        IAtomContainer ac = getAtomContainer();
        Map map = new HashMap();
        List remove = new ArrayList();
        IMolecule mol = ac.getBuilder().newMolecule();
        int count = ac.getAtomCount();
        for (int i = 0; i < count; i++) {
            IAtom atom = ac.getAtom(i);
            if (!atom.getSymbol().equals(H_ELEMENT_SYMBOL) || preserve.contains(atom)) {
                IAtom a = null;
                try {
                    a = (IAtom) atom.clone();
                } catch (CloneNotSupportedException e) {
                    logger.error("Could not clone: ", atom);
                    logger.debug(e);
                }
                a.setHydrogenCount(0);
                mol.addAtom(a);
                map.put(atom, a);
            } else {
                remove.add(atom);
            }
        }
        count = ac.getBondCount();
        for (int i = 0; i < count; i++) {
            final IBond bond = ac.getBond(i);
            IAtom atom0 = bond.getAtom(0);
            IAtom atom1 = bond.getAtom(1);
            java.util.Iterator atoms = bond.atoms();
            boolean remove_bond = false;
            while (atoms.hasNext()) {
                if (remove.contains((IAtom) atoms.next())) {
                    remove_bond = true;
                    break;
                }
            }
            if (!remove_bond) {
                IBond clone = null;
                try {
                    clone = (IBond) ac.getBond(i).clone();
                } catch (CloneNotSupportedException e) {
                    logger.error("Could not clone: ", ac.getBond(i));
                    logger.debug(e);
                }
                clone.setAtoms(new IAtom[] { (IAtom) map.get(atom0), (IAtom) map.get(atom1) });
                mol.addBond(clone);
            }
        }
        for (Iterator i = remove.iterator(); i.hasNext(); ) {
            for (Iterator n = ac.getConnectedAtomsList((IAtom) i.next()).iterator(); n.hasNext(); ) {
                final IAtom neighb = (IAtom) map.get(n.next());
                neighb.setHydrogenCount(neighb.getHydrogenCount() + 1);
            }
        }
        return (mol);
    }

    /**
	 * Returns a set of nodes excluding all the hydrogens
	 *
	 * @return         The heavyAtoms value
	 * @cdk.keyword    hydrogen, removal
	 */
    public List getHeavyAtoms() {
        ArrayList newAc = new ArrayList();
        IAtomContainer ac = getAtomContainer();
        for (int f = 0; f < ac.getAtomCount(); f++) {
            if (!ac.getAtom(f).getSymbol().equals(H_ELEMENT_SYMBOL)) {
                newAc.add(ac.getAtom(f));
            }
        }
        return newAc;
    }

    /**
	 * Method that actually does the work of analysing the molecular formula
	 *
	 * @param  MF  molecular formula to create an AtomContainer from
	 * @param  ac  AtomContainer in which the Atom's and Bond's will be stored 
	 * @return     the filled AtomContainer
	 */
    private IAtomContainer analyseMF(String MF, IAtomContainer ac) {
        char ThisChar;
        String RecentElementSymbol = new String();
        String RecentElementCountString = new String("0");
        int RecentElementCount;
        if (MF.length() == 0) {
            return null;
        }
        for (int f = 0; f < MF.length(); f++) {
            ThisChar = MF.charAt(f);
            if (f < MF.length()) {
                if (ThisChar >= 'A' && ThisChar <= 'Z') {
                    RecentElementSymbol = java.lang.String.valueOf(ThisChar);
                    RecentElementCountString = "0";
                }
                if (ThisChar >= 'a' && ThisChar <= 'z') {
                    RecentElementSymbol += ThisChar;
                }
                if (ThisChar >= '0' && ThisChar <= '9') {
                    RecentElementCountString += ThisChar;
                }
            }
            if (f == MF.length() - 1 || (MF.charAt(f + 1) >= 'A' && MF.charAt(f + 1) <= 'Z')) {
                Integer RecentElementCountInteger = new Integer(RecentElementCountString);
                RecentElementCount = RecentElementCountInteger.intValue();
                if (RecentElementCount == 0) {
                    RecentElementCount = 1;
                }
                for (int g = 0; g < RecentElementCount; g++) {
                    ac.addAtom(ac.getBuilder().newAtom(RecentElementSymbol));
                }
            }
        }
        return ac;
    }

    /**
	 * creates a sorted hash map of elementsymbol-count of this ac
	 * 
	 * @param ac the atomcontainer to calculate with
	 * @return the hashmap
	 */
    private Map getSymolMap(IAtomContainer ac) {
        String symbol;
        SortedMap symbols = new TreeMap();
        IAtom atom = null;
        HCount = 0;
        for (int f = 0; f < ac.getAtomCount(); f++) {
            int hs = 0;
            atom = ac.getAtom(f);
            symbol = atom.getSymbol();
            if (useboth) {
            }
            if (atom.getHydrogenCount() > 0) {
                HCount += atom.getHydrogenCount();
            }
            if (symbols.get(symbol) != null) {
                symbols.put(symbol, new Integer(((Integer) symbols.get(symbol)).intValue() + 1));
            } else {
                symbols.put(symbol, new Integer(1));
            }
        }
        if (useboth && symbols.get(H_ELEMENT_SYMBOL) != null) HCount += ((Integer) symbols.get(H_ELEMENT_SYMBOL)).intValue();
        return symbols;
    }

    /**
	 * Analyses a set of Nodes that has been changed or recently loaded
	 * and  returns a molecular formula
	 *
	 * @param  ac  Description of the Parameter
	 * @return     a string containing the molecular formula.
	 */
    public String analyseAtomContainer(IAtomContainer ac) {
        String symbol;
        String mf = "";
        Map symbols = this.getSymolMap(ac);
        mf = addSymbolToFormula(symbols, "C", mf);
        if (useboth) {
            if (HCount > 0) mf += H_ELEMENT_SYMBOL;
            if (HCount > 1) {
                mf += Integer.toString(HCount);
            }
        } else {
            if (symbols.get(H_ELEMENT_SYMBOL) != null) {
                mf = addSymbolToFormula(symbols, H_ELEMENT_SYMBOL, mf);
            } else {
                if (HCount > 0) {
                    mf += H_ELEMENT_SYMBOL;
                    if (HCount > 1) {
                        mf += Integer.toString(HCount);
                    }
                }
            }
        }
        mf = addSymbolToFormula(symbols, "N", mf);
        mf = addSymbolToFormula(symbols, "O", mf);
        mf = addSymbolToFormula(symbols, "S", mf);
        mf = addSymbolToFormula(symbols, "P", mf);
        Iterator it = symbols.keySet().iterator();
        while (it.hasNext()) {
            Object key = it.next();
            if (!((String) key).equals("C") && !((String) key).equals(H_ELEMENT_SYMBOL) && !((String) key).equals("N") && !((String) key).equals("O") && !((String) key).equals("S") && !((String) key).equals("P")) {
                mf = addSymbolToFormula(symbols, (String) key, mf);
            }
        }
        return mf;
    }

    /**
	 * Adds an element to a chemical formual string
	 *
	 * @param  sm       The map containing the elements
	 * @param  symbol   The symbol to add
	 * @param  formula  The chemical formula
	 * @return          Description of the Return Value
	 */
    private String addSymbolToFormula(Map sm, String symbol, String formula) {
        if (sm.get(symbol) != null) {
            formula += symbol;
            if (!sm.get(symbol).equals(new Integer(1))) {
                formula += sm.get(symbol).toString();
            }
        }
        return (formula);
    }

    /**
	 * Checks a set of Nodes for the occurence of a particular
	 * element.
	 *
	 * @param  thisElement  Description of the Parameter
	 * @return              The number of atoms for the particular element in the formula
	 */
    public int getAtomCount(String thisElement) {
        int atomCount = 0;
        if (thisElement.equals(H_ELEMENT_SYMBOL) && HCount > 0) {
            return HCount;
        }
        for (int f = 0; f < atomContainer.getAtomCount(); f++) {
            if (atomContainer.getAtom(f).getSymbol().equals(thisElement)) {
                atomCount++;
            }
        }
        return atomCount;
    }

    /**
	 * Returns a Vector (of Strings) with asorted element names.
	 * The order is determined by ElementComparator.
	 *
	 * @return    The elements value
	 * @see       ElementComparator
	 */
    public List getElements() {
        TreeSet elements = new TreeSet(new ElementComparator());
        for (int f = 0; f < atomContainer.getAtomCount(); f++) {
            String symbol = atomContainer.getAtom(f).getSymbol();
            if (!elements.contains(symbol)) {
                elements.add(symbol);
            }
        }
        List results = new ArrayList();
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            results.add((String) iter.next());
        }
        return results;
    }

    /**
	 * Returns the number of distinct elements in the formula.
	 *
	 * @return    The elementCount value
	 */
    public int getElementCount() {
        return getElements().size();
    }

    /**
	 *  Gets a Molecule and an array of element symbols. Counts how many of each of these elements
	 *  the molecule contains. Then it returns the elements followed by their number as a string,
	 *  i.e. C15H8N3.
	 *
	 * @param  mol       The Molecule to be searched
	 * @param  elements  Description of the Parameter
	 * @return           The element formula as a string
	 */
    public static String generateElementFormula(IMolecule mol, String[] elements) {
        int num = elements.length;
        StringBuffer formula = new StringBuffer();
        int[] elementCount = new int[num];
        for (int i = 0; i < mol.getAtomCount(); i++) {
            for (int j = 0; j < num; j++) {
                if (elements[j].equals(mol.getAtom(i).getSymbol())) {
                    elementCount[j]++;
                }
            }
        }
        for (int i = 0; i < num; i++) {
            formula.append(elements[i] + elementCount[i]);
        }
        return formula.toString();
    }

    /**
	 *  Builds the elemental formula of a given molecule as a Hashtable.
	 *  Keys are the elemental symbols (Strings) and values are the no. of occurrence (Integer objects).
	 *
	 * @return    a Hashtable, keys are the elemental symbols and values are their no.
	 */
    public Map getFormulaHashtable() {
        Map formula = new HashMap();
        List elements = this.getElements();
        for (int i = 0; i < elements.size(); i++) {
            Integer numOfAtom = new Integer(this.getAtomCount((String) elements.get(i)));
            formula.put(elements.get(i), numOfAtom);
        }
        return formula;
    }

    /**
	 * 
	 * Returns the string representation of the molecule formula with
	 * numbers wrapped in &lt;sub&gt;&lt;/sub&gt; tags and the total
	 * charge of AtomContainer in &lt;sup&gt;&lt;/sup&gt; tags
	 * Useful for displaying formulae in Swing components or on the web.
	 *
	 * @return    The html-string representation of the sum formula with charge 
	 * @see #getHTMLMolecularFormula()
	 */
    public String getHTMLMolecularFormulaWithCharge() {
        String formula = new MFAnalyser(atomContainer, useboth).getHTMLMolecularFormula();
        int charge = AtomContainerManipulator.getTotalFormalCharge(atomContainer);
        if (charge == 0) {
            return formula;
        } else if (charge < 0) {
            return formula + "<sup>" + charge * -1 + "-" + "</sup>";
        } else {
            return formula + "<sup>" + charge + "+" + "</sup>";
        }
    }
}
