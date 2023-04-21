package org.xmlcml.cml.tools;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.xmlcml.cml.base.AbstractTool;
import org.xmlcml.cml.element.CMLAtom;
import org.xmlcml.cml.element.CMLAtomSet;
import org.xmlcml.cml.element.CMLBond;
import org.xmlcml.cml.element.CMLBondSet;
import org.xmlcml.euclid.Util;

/**
 * tool to support a ring. not fully developed
 * 
 * @author pmr
 * 
 */
public class Junction extends AbstractTool {

    private static final Logger LOG = Logger.getLogger(Junction.class.getName());

    private List<CMLAtomSet> ringAtomSetList;

    private List<CMLBondSet> ringBondSetList;

    private List<Ring> ringList;

    private CMLAtomSet commonAtomSet;

    private CMLBondSet commonBondSet;

    private List<CMLBond> commonBondList = null;

    private List<CMLAtom> commonAtomList = null;

    private int direction;

    private List<BridgeAtom> bridgeAtomList;

    /** get junction between two ringList.
	 * 
	 * @param ring0
	 * @param ring1
	 * @param commonAtomSet
	 */
    Junction(Ring ring0, Ring ring1, CMLAtomSet commonAtomSet) {
        if (ring0 == null || ring1 == null) {
            throw new RuntimeException("null ring in junction");
        }
        if (ring0.equals(ring1)) {
            throw new RuntimeException("cannot form junction with self");
        }
        this.ringAtomSetList = new ArrayList<CMLAtomSet>();
        ringAtomSetList.add(ring0.getAtomSet());
        ringAtomSetList.add(ring1.getAtomSet());
        this.ringBondSetList = new ArrayList<CMLBondSet>();
        ringBondSetList.add(ring0.getBondSet());
        ringBondSetList.add(ring1.getBondSet());
        this.commonAtomSet = commonAtomSet;
        this.ringList = new ArrayList<Ring>();
        this.ringList.add(ring0);
        this.ringList.add(ring1);
        commonBondSet = ringBondSetList.get(0).intersection(ringBondSetList.get(1));
        makeJunction();
        ring0.add(ring1, this);
        ring1.add(ring0, this);
    }

    /** factory for Junction
	 * 
	 * @param ring0
	 * @param ring1
	 * @return null if ringList have no atoms in common
	 */
    public static Junction createJunction(Ring ring0, Ring ring1) {
        Junction junction = null;
        CMLAtomSet commonAtomSet = ring0.getAtomSet().intersection(ring1.getAtomSet());
        if (commonAtomSet.size() > 0) {
            junction = new Junction(ring0, ring1, commonAtomSet);
        }
        return junction;
    }

    /** 
	 */
    private void makeJunction() {
        if (commonAtomSet.size() != commonBondSet.size() + 1) {
            throw new RuntimeException("commonAtomSet (" + commonAtomSet.size() + ") should be 1 larger than commonBondSet (" + commonBondSet.size() + ")");
        }
        if (ringList.get(0).size() == ringList.get(1).size()) {
            CMLBondSet bs = ringBondSetList.get(0).complement(ringBondSetList.get(1));
            if (bs.size() == 0) {
                throw new RuntimeException("identical ringList");
            }
        }
        commonBondList = new ArrayList<CMLBond>();
        commonAtomList = new ArrayList<CMLAtom>();
        if (commonBondSet.size() > 0) {
            sortAtomsAndBonds();
        } else {
            commonAtomList.add(commonAtomSet.getAtom(0));
        }
    }

    private void sortAtomsAndBonds() {
        CyclicList<CMLAtom> cyclicAtomList0 = this.ringList.get(0).getCyclicAtomList();
        CyclicList<CMLBond> cyclicBondList0 = this.ringList.get(0).getCyclicBondList();
        CyclicList<CMLBond> cyclicBondList1 = this.ringList.get(1).getCyclicBondList();
        int atom2BondOffset = ringList.get(0).getCanonicalBondToAtomOffset();
        CMLBond bond = cyclicBondList0.get(0);
        cyclicBondList0.getIndexOfAndCache(bond);
        CMLBond startBond = bond;
        while (ringBondSetList.get(1).contains(bond)) {
            bond = cyclicBondList0.getNext();
            if (bond.equals(startBond)) {
                LOG.debug(cyclicBondList0);
                LOG.debug(cyclicBondList1);
                throw new RuntimeException("all bonds are contained in one ring");
            }
        }
        startBond = bond;
        while (!ringBondSetList.get(1).contains(bond)) {
            bond = cyclicBondList0.getNext();
            if (bond.equals(startBond)) {
                LOG.debug(cyclicBondList0);
                LOG.debug(cyclicBondList1);
                throw new RuntimeException("NO bonds are contained in one ring");
            }
        }
        startBond = bond;
        int startBondIdx = cyclicBondList0.getIndexOfAndCache(bond);
        int startAtomIdx = (startBondIdx + atom2BondOffset) % cyclicBondList0.size();
        CMLAtom atom = cyclicAtomList0.get(startAtomIdx);
        while (ringBondSetList.get(1).contains(bond)) {
            commonBondList.add(bond);
            commonAtomList.add(atom);
            bond = cyclicBondList0.getNext();
            atom = cyclicAtomList0.getNext();
            if (bond.equals(startBond)) {
                LOG.debug(cyclicBondList0);
                LOG.debug(cyclicBondList1);
                throw new RuntimeException("All/NO bonds are contained in one ring");
            }
        }
        commonAtomList.add(atom);
        CyclicList<CMLAtom> cyclicAtomList1 = this.ringList.get(1).getCyclicAtomList();
        direction = getDirection(commonAtomList, cyclicAtomList1);
        if (direction == 0) {
            throw new RuntimeException("bad direction");
        }
    }

    private int getDirection(List<CMLAtom> atomList, CyclicList<CMLAtom> cyclicAtomList) {
        int direction = 0;
        CMLAtom startAtom = atomList.get(0);
        int idx = cyclicAtomList.getIndexOfAndCache(startAtom);
        if (idx == -1) {
            throw new RuntimeException("atom lists do not intersect");
        }
        direction = 1;
        if (!testDirection(direction, atomList, cyclicAtomList)) {
            direction = -1;
            if (!testDirection(direction, atomList, cyclicAtomList)) {
                direction = 0;
            }
        }
        return direction;
    }

    private boolean testDirection(int direction, List<CMLAtom> atomList, CyclicList<CMLAtom> cyclicAtomList) {
        CMLAtom startAtom = atomList.get(0);
        CMLAtom cyclicAtom = startAtom;
        cyclicAtomList.getIndexOfAndCache(startAtom);
        boolean ok = true;
        for (int i = 0; i < atomList.size(); i++) {
            CMLAtom atom = atomList.get(i);
            if (!atom.equals(cyclicAtom)) {
                ok = false;
                break;
            }
            cyclicAtom = (direction == 1) ? cyclicAtomList.getNext() : cyclicAtomList.getPrevious();
        }
        return ok;
    }

    /** get bridge atoms between two ringList.
	 * 
	 * @return null if nothing in common
	 */
    public List<BridgeAtom> getBridgeAtomList() {
        if (bridgeAtomList == null) {
            bridgeAtomList = new ArrayList<BridgeAtom>();
            bridgeAtomList.add(new BridgeAtom(this, commonAtomList.get(0)));
            bridgeAtomList.add(new BridgeAtom(this, commonAtomList.get(commonAtomList.size() - 1)));
        }
        return bridgeAtomList;
    }

    /** has 1 atom in common.
	 * 
	 * @return true if 1 atom
	 */
    public boolean isSpiro() {
        return (commonAtomSet.size() == 1 && commonBondSet.size() == 0);
    }

    /** has 1 bond in common.
	 * 
	 * @return true if 1 bond
	 */
    public boolean isFusion() {
        return (commonAtomSet.size() == 2 && commonBondSet.size() == 1);
    }

    /** has > 1 bond in common.
	 * 
	 * @return true if > 1 bond
	 */
    public boolean isBridge() {
        return (commonBondSet.size() > 1);
    }

    /**
	 * @return the commonAtomSet
	 */
    public CMLAtomSet getCommonAtomSet() {
        return commonAtomSet;
    }

    /**
	 * @return the commonBondList
	 */
    public List<CMLBond> getCommonBondList() {
        return commonBondList;
    }

    /**
	 * @return the commonBondSet
	 */
    public CMLBondSet getCommonBondSet() {
        return commonBondSet;
    }

    /**
	 * @return the direction
	 */
    public int getDirection() {
        return direction;
    }

    /**
	 * @return the ringAtomSetList
	 */
    public List<CMLAtomSet> getRingAtomSetList() {
        return ringAtomSetList;
    }

    /**
	 * @return the ringBondSetList
	 */
    public List<CMLBondSet> getRingBondSetList() {
        return ringBondSetList;
    }

    /** get ordered atom list
	 * @return list
	 */
    public List<CMLAtom> getCommonAtomList() {
        return commonAtomList;
    }

    /** get ring list
	 * @return list
	 */
    public List<Ring> getRingList() {
        return ringList;
    }

    /** debug.
	 */
    public void debug() {
        ringList.get(0).debug();
        if (commonBondList.size() > 0) {
            Util.print("..bond");
            for (CMLBond bond : commonBondList) {
                Util.print(" ... " + bond.getId());
            }
            Util.println();
            Util.println("..atom");
            for (CMLAtom atom : commonAtomList) {
                Util.print(" ... " + atom.getId());
            }
        } else {
            LOG.info("..atom " + commonAtomSet.getAtoms().get(0).getId());
        }
    }
}
