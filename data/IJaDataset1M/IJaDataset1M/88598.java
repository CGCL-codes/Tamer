package org.openscience.cdk.isomorphism;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import java.util.*;

/**
 * Facilitates sort before isomorphic comparison
 * @author      markr
 * @cdk.keyword isomorphism
 * @cdk.module standard
 *
 */
public class IsomorphismSort {

    static class AtomCountComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            Map.Entry e1 = (Map.Entry) o1;
            Map.Entry e2 = (Map.Entry) o2;
            return ((Integer) e1.getValue()).compareTo((Integer) e2.getValue());
        }
    }

    /**
     * Returns a sorted array of Atoms for a given input IAtomContainer,
     * with the sort based on the frequency of atom symbols.<BR>
     * For example input C47H65N11O6 will produce an atom array with the "O"
     * atoms first, then "N", then "C" and lastly "H".<BR>
     * This type of sort can benefit algorithms like VF2, sorting the query
     * container before a subgraph match.<BR>
     *
     * @param iac input IAtomContainer
     * @return a sorted array of atoms for that IAtomContainer
     */
    public static IAtom[] atomsByFrequency(IAtomContainer iac) {
        Map map = new TreeMap();
        Iterator atomIterator = iac.atoms().iterator();
        while (atomIterator.hasNext()) {
            IAtom a = (IAtom) atomIterator.next();
            Integer count = (Integer) (map.get(a.getSymbol()));
            if (count == null) {
                map.put(a.getSymbol(), new Integer(1));
            } else map.put(a.getSymbol(), ++count);
        }
        Iterator it = map.entrySet().iterator();
        List<Map.Entry> atomCountList = new ArrayList<Map.Entry>();
        while (it.hasNext()) {
            atomCountList.add((Map.Entry) it.next());
        }
        Collections.sort(atomCountList, new AtomCountComparator());
        IAtom[] iAtomArray = new IAtom[iac.getAtomCount()];
        int iacSortedIdx = 0;
        for (Map.Entry e : atomCountList) for (int i = 0; i < iac.getAtomCount(); i++) {
            if (iac.getAtom(i).getSymbol().equals(e.getKey())) iAtomArray[iacSortedIdx++] = iac.getAtom(i);
        }
        return iAtomArray;
    }
}
