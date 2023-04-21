package org.xmlcml.cmlimpl.normalise;

import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import org.xmlcml.cml.CMLAtom;
import org.xmlcml.cml.CMLBond;
import org.xmlcml.cml.CMLDocument;
import org.xmlcml.cml.CMLException;
import org.xmlcml.cml.subset.AtomSet;
import org.xmlcml.cml.subset.BondSet;
import org.xmlcml.cml.normalise.NormalAtom;
import org.xmlcml.cml.normalise.NormalMolecule;
import org.xmlcml.cml.normalise.Regularizer;
import org.xmlcml.cmlimpl.subset.AtomSetImpl;
import org.xmlcml.cmlimpl.subset.BondSetImpl;
import org.xmlcml.cmlimpl.CMLDocumentImpl;
import org.xmlcml.cmlimpl.CMLDocumentImpl;
import org.xmlcml.molutil.ChemicalElement;
import uk.co.demon.ursus.dom.PMRDelegate;
import uk.co.demon.ursus.dom.PMRNode;
import jumbo.xml.util.Util;

public class RegularizerImpl implements Regularizer, Cloneable {

    protected NormalMolecule molecule;

    protected boolean regularize = false;

    protected boolean zm2cart = true;

    protected boolean fract2cart = true;

    protected boolean cart2bond = true;

    protected boolean length2order = true;

    protected boolean calculateHydrogenCount = false;

    protected boolean totalChargeKnown = false;

    protected boolean atomChargesKnown = false;

    protected boolean hydrogenCountKnown = false;

    protected boolean useExistingHydrogens = true;

    protected boolean use3dForLayout = false;

    protected boolean calculate2dLayout = true;

    protected int hydrogenOption = NormalAtom.HMODE_SUBSUME;

    public RegularizerImpl() {
        super();
        init();
    }

    public RegularizerImpl(NormalMolecule molecule) {
        this.setMolecule(molecule);
        init();
    }

    public RegularizerImpl(RegularizerImpl regularizer) {
        this();
        this.setMolecule(regularizer.molecule);
        this.regularize = regularizer.regularize;
        this.zm2cart = regularizer.zm2cart;
        this.fract2cart = regularizer.fract2cart;
        this.cart2bond = regularizer.cart2bond;
        this.length2order = regularizer.length2order;
        this.useExistingHydrogens = regularizer.useExistingHydrogens;
        this.totalChargeKnown = regularizer.totalChargeKnown;
        this.atomChargesKnown = regularizer.atomChargesKnown;
        this.hydrogenCountKnown = regularizer.hydrogenCountKnown;
        this.calculateHydrogenCount = regularizer.calculateHydrogenCount;
        this.calculateHydrogenCount = regularizer.calculateHydrogenCount;
        this.use3dForLayout = regularizer.use3dForLayout;
        this.calculate2dLayout = regularizer.calculate2dLayout;
        this.hydrogenOption = regularizer.hydrogenOption;
    }

    void init() {
        regularize = false;
        zm2cart = true;
        fract2cart = true;
        cart2bond = true;
        length2order = true;
        calculateHydrogenCount = false;
        hydrogenCountKnown = false;
        totalChargeKnown = false;
        atomChargesKnown = false;
        useExistingHydrogens = true;
        use3dForLayout = true;
        calculate2dLayout = true;
        hydrogenOption = NormalAtom.HMODE_SUBSUME;
    }

    public void setMolecule(NormalMolecule molecule) {
        if (molecule != null) molecule.setRegularizer(this);
        this.molecule = molecule;
    }

    public NormalMolecule getMolecule() {
        return this.molecule;
    }

    /** submit molecule to the regularization processes in this class*/
    public void setRegularize(boolean regularize) {
        this.regularize = regularize;
    }

    public boolean getRegularize() {
        return this.regularize;
    }

    /** calculate missing Cartesians from internals	*/
    public void setCalculateCartesiansFromZMatrix(boolean zm2cart) {
        this.zm2cart = zm2cart;
    }

    public boolean getCalculateCartesiansFromZMatrix() {
        return this.zm2cart;
    }

    /** calculate missing Cartesians from fractionals (crystallography)*/
    public void setCalculateCartesiansFromFractionals(boolean fract2cart) {
        this.fract2cart = fract2cart;
    }

    public boolean getCalculateCartesiansFromFractionals() {
        return this.fract2cart;
    }

    /** calculate connectivity (bond orders set to UNKNOWN) from Cartesians*/
    public void setCalculateBondsFromCartesians(boolean cart2bond) {
        this.cart2bond = cart2bond;
    }

    public boolean getCalculateBondsFromCartesians() {
        return this.cart2bond;
    }

    /** calculate bond orders from Cartesians, using Pauling relation */
    public void setCalculateBondOrdersFromLength(boolean length2order) {
        this.length2order = length2order;
    }

    public boolean getCalculateBondOrdersFromLength() {
        return this.length2order;
    }

    /** use hydrogens to calculate bond order, etc. (presupposes hydrogen count known*/
    public void setUseExistingHydrogens(boolean useExistingHydrogens) {
        this.useExistingHydrogens = useExistingHydrogens;
    }

    public boolean getUseExistingHydrogens() {
        return useExistingHydrogens;
    }

    /** assume charge on molecule is known (default zero) */
    public void setTotalChargeKnown(boolean totalChargeKnown) {
        this.totalChargeKnown = totalChargeKnown;
    }

    public boolean getTotalChargeKnown() {
        return this.totalChargeKnown;
    }

    /** assume charge on atoms are known (default zero) */
    public void setAtomChargesKnown(boolean atomChargesKnown) {
        this.atomChargesKnown = atomChargesKnown;
    }

    public boolean getAtomChargesKnown() {
        return this.atomChargesKnown;
    }

    /** assume hydrogen counts on all atoms are known */
    public void setHydrogenCountKnown(boolean hydrogenCountKnown) {
        this.hydrogenCountKnown = hydrogenCountKnown;
    }

    public boolean getHydrogenCountKnown() {
        return hydrogenCountKnown;
    }

    /** if hydrogenCountKnown==false, calculate H counts where possible */
    public void setCalculateHydrogenCount(boolean calculateHydrogenCount) {
        this.calculateHydrogenCount = calculateHydrogenCount;
    }

    public boolean getCalculateHydrogenCount() {
        return this.calculateHydrogenCount;
    }

    /** use 3D coordinates for layout */
    public void setUse3dForLayout(boolean use3dForLayout) {
        this.use3dForLayout = use3dForLayout;
    }

    public boolean getUse3dForLayout() {
        return use3dForLayout;
    }

    /** calculate 2D coordinates for layout */
    public void setCalculate2dLayout(boolean calculate2dLayout) {
        this.calculate2dLayout = calculate2dLayout;
    }

    public boolean getCalculate2dLayout() {
        return calculate2dLayout;
    }

    /** hydrogen processing options (see NormalAtom) */
    public void setHydrogenOption(int hydrogenOption) {
        this.hydrogenOption = hydrogenOption;
    }

    public int getHydrogenOption() {
        return this.hydrogenOption;
    }

    public String toString() {
        String s = "";
        s += " getRegularize:                         " + this.regularize + "\n";
        s += " getCalculateCartesiansFromZMatrix:     " + this.zm2cart + "\n";
        s += " getCalculateCartesiansFromFractionals: " + this.fract2cart + "\n";
        s += " getCalculateBondsFromCartesians:       " + this.cart2bond + "\n";
        s += " getCalculateBondOrdersFromLength:      " + this.length2order + "\n";
        s += " getUseExistingHydrogens:               " + useExistingHydrogens + "\n";
        s += " getTotalChargeKnown:                   " + this.totalChargeKnown + "\n";
        s += " getAtomChargesKnown:                   " + this.atomChargesKnown + "\n";
        s += " getHydrogenCountKnown:                 " + hydrogenCountKnown + "\n";
        s += " getCalculateHydrogenCount:             " + this.calculateHydrogenCount + "\n";
        s += " setUse3dForLayout                      " + this.use3dForLayout + "\n";
        s += " setCalculate2dLayout                   " + this.calculate2dLayout + "\n";
        s += " hydrogenOption:                        " + NormalAtom.HMODE_TYPES[this.hydrogenOption] + "\n";
        return s;
    }
}
