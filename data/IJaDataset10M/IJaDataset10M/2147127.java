package relex.feature;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class FeatureNameFilter {

    Set<String> ignoreSet;

    private ArrayList<String> featureOrder;

    void transferMultiNames(ArrayList<String> output, Set<String> featureNames, String namePrefix) {
        transferName(output, featureNames, namePrefix);
        Iterator<String> i = featureNames.iterator();
        TreeSet<String> ts = new TreeSet<String>();
        while (i.hasNext()) {
            String s = i.next();
            if (s.indexOf(namePrefix) == 0) ts.add(s);
        }
        i = ts.iterator();
        while (i.hasNext()) transferName(output, featureNames, i.next());
    }

    private void transferName(ArrayList<String> output, Set<String> featureNames, String name) {
        if (featureNames.contains(name)) {
            featureNames.remove(name);
            output.add(name);
        }
    }

    private void copyRest(ArrayList<String> output, Set<String> featureNames) {
        TreeSet<String> t = new TreeSet<String>(featureNames);
        Iterator<String> i = t.iterator();
        while (i.hasNext()) {
            output.add(i.next());
        }
    }

    protected void transfer(ArrayList<String> output, Set<String> featureNames) {
        Iterator<String> i = featureOrder.iterator();
        boolean foundEmpty = false;
        while (i.hasNext() && !foundEmpty) {
            String s = i.next();
            if (s.equals("")) foundEmpty = true; else transferMultiNames(output, featureNames, s);
        }
        ArrayList<String> afterOthers = new ArrayList<String>();
        while (i.hasNext()) transferMultiNames(afterOthers, featureNames, i.next());
        copyRest(output, featureNames);
        i = afterOthers.iterator();
        while (i.hasNext()) output.add(i.next());
    }

    public FeatureNameFilter() {
        ignoreSet = new HashSet<String>();
        featureOrder = new ArrayList<String>();
    }

    public FeatureNameFilter(Set<String> ignores, ArrayList<String> order) {
        this();
        ignoreSet.addAll(ignores);
        if (order != null) featureOrder.addAll(order); else {
            featureOrder.add("");
        }
    }
}
