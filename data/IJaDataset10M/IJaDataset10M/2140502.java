package info.gryb.xacml.pdp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import org.apache.xalan.lib.ExsltDatetime;
import os.schema.policy._0._2.xacml.tc.names.oasis.PolicySetType;
import os.schema.policy._0._2.xacml.tc.names.oasis.PolicySetDocument;
import os.schema.policy._0._2.xacml.tc.names.oasis.PolicyDocument;
import os.schema.policy._0._2.xacml.tc.names.oasis.PolicyType;

public class PolicyIterator<T> implements Iterator<T> {

    private PolicySetType policySet;

    private int curInd = -1;

    private Vector<T> all = new Vector<T>();

    private Vector<PolicySetType> parents = new Vector<PolicySetType>();

    private boolean bSet;

    private String getId;

    private String getArray;

    private String setArray;

    public PolicyIterator(PolicySetType ps, boolean bSet) {
        this.policySet = ps;
        this.curInd = -1;
        this.bSet = bSet;
        if (bSet) {
            this.getId = "getPolicySetId";
            this.getArray = "getPolicySetArray";
            this.setArray = "setPolicySetArray";
        } else {
            this.getId = "getPolicyId";
            this.getArray = "getPolicyArray";
            this.setArray = "setPolicyArray";
        }
        enumerateNodes(ps, null);
    }

    private void enumerateNodes(PolicySetType pol, PolicySetType parent) {
        if (pol.getPolicySetArray() != null && pol.getPolicySetArray().length > 0) {
            PolicySetType[] pols = pol.getPolicySetArray();
            for (int i = 0; i < pols.length; i++) {
                enumerateNodes(pols[i], pol);
            }
        }
        if (bSet) {
            this.all.add((T) pol);
            this.parents.add(parent);
        } else {
            PolicyType[] policies = pol.getPolicyArray();
            if (policies != null) {
                for (int i = 0; i < policies.length; i++) {
                    this.all.add((T) policies[i]);
                    this.parents.add(pol);
                }
            }
        }
    }

    public boolean hasNext() {
        if (this.curInd + 1 < all.size()) return true;
        return false;
    }

    public T next() {
        if (++this.curInd < all.size()) return all.elementAt(this.curInd);
        return null;
    }

    public void remove() {
        if (this.curInd >= all.size() || parents.elementAt(this.curInd) == null) return;
        T pol = all.elementAt(this.curInd);
        String id = (String) Helper.callMethod(pol, getId, null);
        PolicySetType par = parents.elementAt(this.curInd);
        if (par == null) return;
        T[] pols = (T[]) Helper.callMethod(par, getArray, null);
        for (int i = 0; i < pols.length; i++) {
            String curId = (String) Helper.callMethod(pols[i], getId, null);
            if (id.equals(curId)) {
                T[] newPols = !bSet ? (T[]) new PolicyType[pols.length - 1] : (T[]) new PolicySetType[pols.length - 1];
                int k = 0;
                for (int j = 0; j < newPols.length; j++, k++) {
                    if (k != i) {
                        newPols[j] = pols[k];
                    } else {
                        j--;
                    }
                }
                Helper.callMethod(par, setArray, new Object[] { newPols });
                break;
            }
        }
    }

    public PolicySetType getPolicySet() {
        return policySet;
    }

    public void setPolicySet(PolicySetType policySet) {
        this.policySet = policySet;
    }
}
