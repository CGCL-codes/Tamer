package joelib2.feature.types.atomlabel;

import joelib2.data.IdentifierExpertSystem;
import joelib2.feature.AbstractDynamicAtomProperty;
import joelib2.feature.BasicFeatureInfo;
import joelib2.feature.FeatureException;
import joelib2.feature.FeatureHelper;
import joelib2.feature.result.DynamicArrayResult;
import joelib2.feature.types.bondlabel.BondInRing;
import joelib2.molecule.Atom;
import joelib2.molecule.Molecule;
import joelib2.molecule.types.AtomPropertyHelper;
import org.apache.log4j.Category;

/**
 * Is this atom negatively charged atom.
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.13 $, $Date: 2005/02/17 16:48:31 $
 */
public class AtomIsAromaticNOxide extends AbstractDynamicAtomProperty {

    private static final String VENDOR = "http://joelib.sf.net";

    private static final String RELEASE_VERSION = "$Revision: 1.13 $";

    private static final String RELEASE_DATE = "$Date: 2005/02/17 16:48:31 $";

    private static Category logger = Category.getInstance(AtomIsAromaticNOxide.class.getName());

    private static final Class[] DEPENDENCIES = new Class[] { AtomIsNitrogen.class, AtomInAromaticSystem.class, AtomIsOxygen.class, BondInRing.class };

    /**
     *  Constructor for the KierShape1 object
     */
    public AtomIsAromaticNOxide() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initialize " + this.getClass().getName());
        }
        descInfo = FeatureHelper.generateFeatureInfo(this.getClass(), BasicFeatureInfo.TYPE_NO_COORDINATES, null, joelib2.feature.result.AtomDynamicResult.class.getName());
    }

    public static Class[] getDependencies() {
        return DEPENDENCIES;
    }

    public static String getName() {
        return AtomIsAromaticNOxide.class.getName();
    }

    public static String getReleaseDate() {
        return VENDOR;
    }

    public static String getReleaseVersion() {
        return IdentifierExpertSystem.transformCVStag(RELEASE_VERSION);
    }

    public static String getVendor() {
        return IdentifierExpertSystem.transformCVStag(RELEASE_DATE);
    }

    /**
    *  Returns <tt>true</tt> if this is an aromatic N-Oxide atom.
    *
    * @return    <tt>true</tt> if this is an aromatic N-Oxide atom
    */
    public static boolean isValue(Atom atom) {
        boolean isTrue = false;
        try {
            isTrue = AtomPropertyHelper.getBooleanAtomProperty(atom, getName());
        } catch (FeatureException e1) {
            logger.error(e1.getMessage());
        }
        return isTrue;
    }

    public Object getAtomPropertiesArray(Molecule mol) {
        int atomsSize = mol.getAtomsSize();
        boolean[] anoxide = (boolean[]) DynamicArrayResult.getNewArray(DynamicArrayResult.BOOLEAN, atomsSize);
        Atom atom;
        for (int atomIdx = 1; atomIdx <= atomsSize; atomIdx++) {
            atom = mol.getAtom(atomIdx);
            anoxide[atomIdx - 1] = AbstractIsAromaticNOxide.calculate(atom);
        }
        return anoxide;
    }

    public int hashedDependencyTreeVersion() {
        return IdentifierExpertSystem.getDependencyTreeHash(getName());
    }
}
