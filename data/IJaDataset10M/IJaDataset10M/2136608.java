package org.openscience.cdk.structgen.deterministic;

import java.util.ArrayList;
import java.util.List;

public class FragmentList {

    List<String> smiles = new ArrayList<String>();

    public boolean contains(Fragment fragment) {
        if (smiles.contains(fragment.getSmiles())) return true; else return false;
    }

    public void add(Fragment fragment) {
        smiles.add(fragment.getSmiles());
    }

    public int size() {
        return smiles.size();
    }

    public String get(int i) {
        return smiles.get(i);
    }
}
