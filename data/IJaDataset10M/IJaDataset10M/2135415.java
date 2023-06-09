package pcgen.rules.context;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import pcgen.base.util.DoubleKeyMapToList;
import pcgen.base.util.MapToList;
import pcgen.base.util.TreeMapToList;
import pcgen.cdom.base.AssociatedPrereqObject;
import pcgen.cdom.base.CDOMList;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.CDOMObjectUtilities;
import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.MasterListInterface;
import pcgen.cdom.base.PrereqObject;
import pcgen.cdom.base.SimpleAssociatedObject;
import pcgen.cdom.enumeration.AssociationKey;

public class ConsolidatedListCommitStrategy implements ListCommitStrategy, MasterListInterface {

    private URI sourceURI;

    private URI extractURI;

    public URI getExtractURI() {
        return extractURI;
    }

    public void setExtractURI(URI extractURI) {
        this.extractURI = extractURI;
    }

    public URI getSourceURI() {
        return sourceURI;
    }

    public void setSourceURI(URI sourceURI) {
        this.sourceURI = sourceURI;
    }

    private final DoubleKeyMapToList<CDOMReference, CDOMObject, AssociatedPrereqObject> masterList = new DoubleKeyMapToList<CDOMReference, CDOMObject, AssociatedPrereqObject>();

    public <T extends CDOMObject> AssociatedPrereqObject addToMasterList(String tokenName, CDOMObject owner, CDOMReference<? extends CDOMList<T>> list, T allowed) {
        SimpleAssociatedObject a = new SimpleAssociatedObject();
        a.setAssociation(AssociationKey.OWNER, owner);
        a.setAssociation(AssociationKey.TOKEN, tokenName);
        masterList.addToListFor(list, allowed, a);
        return a;
    }

    public <T extends CDOMObject> void removeFromMasterList(String tokenName, CDOMObject owner, CDOMReference<? extends CDOMList<T>> list, T allowed) {
        masterList.removeListFor(list, allowed);
    }

    public Changes<CDOMReference> getMasterListChanges(String tokenName, CDOMObject owner, Class<? extends CDOMList<?>> cl) {
        ArrayList<CDOMReference> list = new ArrayList<CDOMReference>();
        LIST: for (CDOMReference<? extends CDOMList<?>> ref : masterList.getKeySet()) {
            if (!cl.equals(ref.getReferenceClass())) {
                continue;
            }
            for (CDOMObject allowed : masterList.getSecondaryKeySet(ref)) {
                for (AssociatedPrereqObject assoc : masterList.getListFor(ref, allowed)) {
                    if (owner.equals(assoc.getAssociation(AssociationKey.OWNER)) && tokenName.equals(assoc.getAssociation(AssociationKey.TOKEN))) {
                        list.add(ref);
                        continue LIST;
                    }
                }
            }
        }
        return new CollectionChanges<CDOMReference>(list, null, false);
    }

    public void clearAllMasterLists(String tokenName, CDOMObject owner) {
        for (CDOMReference<? extends CDOMList<?>> ref : masterList.getKeySet()) {
            for (CDOMObject allowed : masterList.getSecondaryKeySet(ref)) {
                for (AssociatedPrereqObject assoc : masterList.getListFor(ref, allowed)) {
                    if (owner.equals(assoc.getAssociation(AssociationKey.OWNER)) && tokenName.equals(assoc.getAssociation(AssociationKey.TOKEN))) {
                        masterList.removeFromListFor(ref, allowed, assoc);
                    }
                }
            }
        }
    }

    public <T extends CDOMObject> AssociatedChanges<T> getChangesInMasterList(String tokenName, CDOMObject owner, CDOMReference<? extends CDOMList<T>> swl) {
        Set<CDOMObject> added = masterList.getSecondaryKeySet(swl);
        MapToList<T, AssociatedPrereqObject> owned = new TreeMapToList<T, AssociatedPrereqObject>(CDOMObjectUtilities.CDOM_SORTER);
        for (CDOMObject lw : added) {
            List<AssociatedPrereqObject> list = masterList.getListFor(swl, lw);
            for (AssociatedPrereqObject assoc : list) {
                if (owner.equals(assoc.getAssociation(AssociationKey.OWNER))) {
                    owned.addToListFor((T) lw, assoc);
                    break;
                }
            }
        }
        return new AssociatedCollectionChanges<T>(owned, null, false);
    }

    public boolean hasMasterLists() {
        return !masterList.isEmpty();
    }

    public <T extends PrereqObject> AssociatedPrereqObject addToList(String tokenName, CDOMObject owner, CDOMReference<? extends CDOMList<? super T>> list, CDOMReference<T> allowed) {
        SimpleAssociatedObject a = new SimpleAssociatedObject();
        a.setAssociation(AssociationKey.TOKEN, tokenName);
        owner.putToList(list, allowed, a);
        return a;
    }

    public <T extends PrereqObject> AssociatedPrereqObject removeFromList(String tokenName, CDOMObject owner, CDOMReference<? extends CDOMList<? super T>> swl, CDOMReference<T> ref) {
        owner.removeFromList(swl, ref);
        return new SimpleAssociatedObject();
    }

    public void removeAllFromList(String tokenName, CDOMObject owner, CDOMReference<? extends CDOMList<?>> swl) {
        owner.removeAllFromList(swl);
    }

    public Collection<CDOMReference<? extends CDOMList<? extends PrereqObject>>> getChangedLists(CDOMObject owner, Class<? extends CDOMList<?>> cl) {
        ArrayList<CDOMReference<? extends CDOMList<? extends PrereqObject>>> list = new ArrayList<CDOMReference<? extends CDOMList<? extends PrereqObject>>>();
        for (CDOMReference<? extends CDOMList<? extends PrereqObject>> ref : owner.getModifiedLists()) {
            if (cl.equals(ref.getReferenceClass())) {
                list.add(ref);
            }
        }
        return list;
    }

    public <T extends PrereqObject> AssociatedChanges<CDOMReference<T>> getChangesInList(String tokenName, CDOMObject owner, CDOMReference<? extends CDOMList<T>> swl) {
        return new ListChanges<T>(tokenName, owner, null, swl, false);
    }

    public Set<CDOMReference> getActiveLists() {
        return masterList.getKeySet();
    }

    public <T extends CDOMObject> Collection<AssociatedPrereqObject> getAssociations(CDOMReference<? extends CDOMList<T>> key1, T key2) {
        return masterList.getListFor(key1, key2);
    }

    public <T extends CDOMObject> Collection<AssociatedPrereqObject> getAssociations(CDOMList<T> key1, T key2) {
        List<AssociatedPrereqObject> list = new ArrayList<AssociatedPrereqObject>();
        for (CDOMReference ref : masterList.getKeySet()) {
            if (ref.contains(key1)) {
                List<AssociatedPrereqObject> tempList = masterList.getListFor(ref, key2);
                if (tempList != null) {
                    list.addAll(tempList);
                }
            }
        }
        return list;
    }

    public boolean equalsTracking(ListCommitStrategy commit) {
        return false;
    }

    public <T extends CDOMObject> Collection<T> getObjects(CDOMReference<CDOMList<T>> ref) {
        return (Collection<T>) masterList.getSecondaryKeySet(ref);
    }
}
