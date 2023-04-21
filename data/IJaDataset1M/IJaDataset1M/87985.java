package query.parsetree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import storage.Store;

public class UnionNode extends ParsedNode {

    @Override
    protected String stringVal() {
        return TYPE.UNION.toString();
    }

    @Override
    public Set<ArrayList<String>> eval(Store store) {
        ArrayList<Integer> binding = null;
        Set<ArrayList<String>> result = null;
        for (ParsedNode child : getChildren()) {
            ArrayList<Integer> childbinding = child.getBindings();
            result = union(binding, result, childbinding, child.eval(store));
            binding = mergeBindings(binding, childbinding);
        }
        return result;
    }

    public ArrayList<Integer> getBindings() {
        ArrayList<Integer> temp = getChildren().get(0).getBindings();
        for (ParsedNode child : getChildren()) {
            mergeBindings(temp, child.getBindings());
        }
        return temp;
    }

    @Override
    public Map<Integer, ArrayList<String>> evalNew(Store store) {
        Map<Integer, ArrayList<String>> result = null;
        for (ParsedNode child : getChildren()) {
            result = unionNew(result, child.evalNew(store));
        }
        return result;
    }

    private Map<Integer, ArrayList<String>> unionNew(Map<Integer, ArrayList<String>> result1, Map<Integer, ArrayList<String>> result2) {
        if (result1 == null) return result2;
        int rs1size = (result1.size() != 0) ? result1.get(result1.keySet().iterator().next()).size() : 0;
        int rs1size2 = (result2.size() != 0) ? result2.get(result2.keySet().iterator().next()).size() : 0;
        for (Integer binding : result2.keySet()) {
            if (!result1.containsKey(binding)) {
                ArrayList<String> temp = new ArrayList<String>();
                for (int i = 0; i < rs1size; i++) temp.add(null);
                result1.put(binding, temp);
            }
        }
        for (Integer binding : result1.keySet()) {
            ArrayList<String> temp = result2.get(binding);
            if (temp == null) {
                temp = new ArrayList<String>();
                for (int i = 0; i < rs1size2; i++) temp.add(null);
            }
            result1.get(binding).addAll(temp);
        }
        return result1;
    }

    private Set<ArrayList<String>> union(ArrayList<Integer> binding1, Set<ArrayList<String>> result1, ArrayList<Integer> binding2, Set<ArrayList<String>> result2) {
        if (binding1 == null) return result2;
        Map<Integer, Integer> bindingmap1 = new Hashtable<Integer, Integer>();
        Map<Integer, Integer> bindingmap2 = new Hashtable<Integer, Integer>();
        for (int i = 0; i < binding1.size(); i++) bindingmap1.put(binding1.get(i), i);
        for (int i = 0; i < binding2.size(); i++) bindingmap2.put(binding2.get(i), i);
        Set<ArrayList<String>> result = new HashSet<ArrayList<String>>();
        ArrayList<Integer> bindings = getBindings();
        for (Iterator<ArrayList<String>> it = result1.iterator(); it.hasNext(); ) {
            ArrayList<String> i = it.next();
            ArrayList<String> temp = new ArrayList<String>();
            for (int binding : bindings) {
                if (bindingmap1.containsKey(binding)) temp.add(i.get(bindingmap1.get(binding))); else temp.add(null);
            }
            result.add(temp);
            it.remove();
        }
        for (Iterator<ArrayList<String>> it = result2.iterator(); it.hasNext(); ) {
            ArrayList<String> i = it.next();
            ArrayList<String> temp = new ArrayList<String>();
            for (int binding : bindings) {
                if (bindingmap2.containsKey(binding)) temp.add(i.get(bindingmap2.get(binding))); else temp.add(null);
            }
            result.add(temp);
            it.remove();
        }
        return result;
    }

    @Override
    public TYPE getType() {
        return TYPE.UNION;
    }
}
