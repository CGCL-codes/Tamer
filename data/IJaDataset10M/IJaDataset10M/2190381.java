package jppl.core.impl.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import jppl.core.basics.Basic;
import jppl.core.basics.JpplId;
import jppl.core.store.BasicStore;
import jppl.core.store.SearchCriterium;
import org.apache.log4j.Logger;

/**
 *
 * @author rolf
 */
public class BasicStoreImpl implements BasicStore {

    private static final Logger log = Logger.getLogger(BasicStoreImpl.class);

    private Map<JpplId, Basic> basicIdMap;

    private List<Basic> basicList;

    public BasicStoreImpl() {
        this.basicList = new ArrayList<Basic>();
        this.basicIdMap = new Hashtable<JpplId, Basic>();
    }

    public void add(Basic basic) {
        if (!(basicIdMap.containsKey(basic.getId()))) {
            basicList.add(basic);
            basicIdMap.put(basic.getId(), basic);
        }
    }

    @Override
    public Collection<Basic> del(Collection<SearchCriterium> criteria) {
        HashSet<Basic> deletedObjects = new HashSet<Basic>();
        Iterator<Basic> listIterator = basicList.iterator();
        while (listIterator.hasNext()) {
            Basic currentBasic = listIterator.next();
            if (getsSelected(currentBasic, criteria)) {
                log.debug("remove object, id:" + currentBasic.getId());
                basicList.remove(currentBasic);
                basicIdMap.remove(currentBasic.getId());
                deletedObjects.add(currentBasic);
            }
        }
        return deletedObjects;
    }

    @Override
    public Collection<Basic> del(SearchCriterium criterium) {
        ArrayList<SearchCriterium> criteria = new ArrayList<SearchCriterium>();
        criteria.add(criterium);
        return del(criteria);
    }

    public Collection<Basic> get(Collection<SearchCriterium> criteria) {
        return getResultSet(criteria);
    }

    public Collection<Basic> get(SearchCriterium criterium) {
        ArrayList<SearchCriterium> criteria = new ArrayList<SearchCriterium>();
        criteria.add(criterium);
        return get(criteria);
    }

    private Collection<Basic> getResultSet(Collection<SearchCriterium> criteria) {
        HashSet<Basic> result = new HashSet<Basic>();
        Iterator<Basic> listIterator = basicList.iterator();
        while (listIterator.hasNext()) {
            Basic currentBasic = listIterator.next();
            if (getsSelected(currentBasic, criteria)) result.add(currentBasic);
        }
        return result;
    }

    private boolean getsSelected(Basic basic, Collection<SearchCriterium> criteria) {
        Iterator<SearchCriterium> criteriumIterator = criteria.iterator();
        while (criteriumIterator.hasNext()) {
            SearchCriterium currentCriterium = criteriumIterator.next();
            if (!(currentCriterium.isSelected(basic))) return false;
        }
        return true;
    }
}
