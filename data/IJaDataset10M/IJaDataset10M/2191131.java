package org.openscience.cdk.structgen.deterministic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GenerationList {

    Set<Integer> smiles = new HashSet<Integer>(100000);

    public boolean contains(List<Fragment> generation) {
        if (smiles.contains(makeSmiles(generation))) return true; else return false;
    }

    private int makeSmiles(List<Fragment> generation) {
        Iterator<Fragment> it = generation.iterator();
        List<String> fragments = new ArrayList<String>();
        while (it.hasNext()) {
            Fragment fragment = it.next();
            fragments.add(new StringBuffer(fragment.getSmiles()).append(":").append(fragment.getCount()).append(";").toString());
        }
        Collections.sort(fragments);
        StringBuffer smiles = new StringBuffer();
        for (int i = 0; i < fragments.size(); i++) {
            smiles.append(fragments.get(i));
        }
        return smiles.toString().hashCode();
    }

    public void add(List<Fragment> generation) {
        smiles.add(makeSmiles(generation));
    }
}
