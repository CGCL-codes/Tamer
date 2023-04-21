package org.openscience.cdk.smsd.algorithm.matchers;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.annotations.TestClass;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.isomorphism.matchers.IQueryBond;
import org.openscience.cdk.smsd.algorithm.vflib.builder.TargetProperties;

/**
 * Checks if a bond is matching between query and target molecules.
 * @cdk.module smsd
 * @cdk.githash
 * @author Syed Asad Rahman <asad@ebi.ac.uk>
 */
@TestClass("org.openscience.cdk.smsd.algorithm.vflib.VFLibTest")
public class DefaultVFBondMatcher implements VFBondMatcher {

    static final long serialVersionUID = -7861469841127328812L;

    private IBond queryBond = null;

    private int unsaturation = 0;

    private boolean shouldMatchBonds;

    private IQueryBond smartQueryBond = null;

    /**
     * Constructor
     */
    public DefaultVFBondMatcher() {
        this.queryBond = null;
        this.unsaturation = -1;
        shouldMatchBonds = false;
    }

    /**
     * Constructor
     * @param queryMol query Molecule
     * @param queryBond query Molecule
     * @param shouldMatchBonds bond match flag
     */
    public DefaultVFBondMatcher(IAtomContainer queryMol, IBond queryBond, boolean shouldMatchBonds) {
        super();
        this.queryBond = queryBond;
        this.unsaturation = getUnsaturation(queryMol, this.queryBond);
        setBondMatchFlag(shouldMatchBonds);
    }

    /**
     * Constructor
     * @param queryBond query Molecule
     */
    public DefaultVFBondMatcher(IQueryBond queryBond) {
        super();
        this.smartQueryBond = queryBond;
    }

    /** {@inheritDoc}
     *
     * @param targetConatiner target container
     * @param targetBond target bond
     * @return true if bonds match
     */
    public boolean matches(TargetProperties targetConatiner, IBond targetBond) {
        if (this.smartQueryBond != null) {
            return smartQueryBond.matches(targetBond);
        } else {
            if (!isBondMatchFlag()) {
                return true;
            }
            if (isBondMatchFlag() && isBondTypeMatch(targetBond)) {
                return true;
            }
            if (isBondMatchFlag() && this.unsaturation == getUnsaturation(targetConatiner, targetBond)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true if a bond is matched between query and target
     * @param targetBond
     * @return
     */
    private boolean isBondTypeMatch(IBond targetBond) {
        int reactantBondType = queryBond.getOrder().ordinal();
        int productBondType = targetBond.getOrder().ordinal();
        if ((queryBond.getFlag(CDKConstants.ISAROMATIC) == targetBond.getFlag(CDKConstants.ISAROMATIC)) && (reactantBondType == productBondType)) {
            return true;
        } else if (queryBond.getFlag(CDKConstants.ISAROMATIC) && targetBond.getFlag(CDKConstants.ISAROMATIC)) {
            return true;
        }
        return false;
    }

    private int getUnsaturation(TargetProperties container, IBond bond) {
        return getUnsaturation(container, bond.getAtom(0)) + getUnsaturation(container, bond.getAtom(1));
    }

    private int getUnsaturation(TargetProperties container, IAtom atom) {
        return getValency(atom) - container.countNeighbors(atom);
    }

    private int getValency(IAtom atom) {
        return (atom.getValency() == null) ? 0 : atom.getValency().intValue();
    }

    private int getUnsaturation(IAtomContainer container, IBond bond) {
        return getUnsaturation(container, bond.getAtom(0)) + getUnsaturation(container, bond.getAtom(1));
    }

    private int getUnsaturation(IAtomContainer container, IAtom atom) {
        return getValency(atom) - (countNeighbors(container, atom) + countImplicitHydrogens(atom));
    }

    private int countNeighbors(IAtomContainer container, IAtom atom) {
        return container.getConnectedAtomsCount(atom);
    }

    private int countImplicitHydrogens(IAtom atom) {
        return (atom.getImplicitHydrogenCount() == null) ? 0 : atom.getImplicitHydrogenCount();
    }

    /**
     * @return the shouldMatchBonds
     */
    public boolean isBondMatchFlag() {
        return shouldMatchBonds;
    }

    /**
     * @param shouldMatchBonds the shouldMatchBonds to set
     */
    public final void setBondMatchFlag(boolean shouldMatchBonds) {
        this.shouldMatchBonds = shouldMatchBonds;
    }
}
