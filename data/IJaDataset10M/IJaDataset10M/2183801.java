package apollo.datamodel;

import java.util.*;

public class EvidenceFinder implements java.io.Serializable {

    private FeatureSetI fset;

    private Hashtable nameHash;

    public EvidenceFinder(FeatureSetI fset) {
        setFeatureSet(fset);
    }

    public void setFeatureSet(FeatureSetI fset) {
        this.fset = fset;
        nameHash = new Hashtable();
        _hashSet(this.fset);
    }

    private void _hashSet(FeatureSetI subSet) {
        if (subSet != null) {
            for (int i = 0; i < subSet.size(); i++) {
                SeqFeatureI sf = subSet.getFeatureAt(i);
                _putName(sf);
                if (sf.canHaveChildren()) {
                    _hashSet((FeatureSetI) sf);
                }
            }
        }
    }

    private void _putName(SeqFeatureI sf) {
        if (sf.getId() != null) {
            nameHash.put(sf.getId(), sf);
        }
    }

    public SeqFeatureI findEvidence(String id) {
        if (id != null) {
            if (nameHash.containsKey(id)) {
                return (SeqFeatureI) nameHash.get(id);
            } else {
                return null;
            }
        } else {
            System.out.println("Null id in findEvidence.");
            return null;
        }
    }
}
