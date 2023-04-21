package jppl.core.store.criteria;

import jppl.core.basics.Basic;
import jppl.core.basics.JpplId;
import jppl.core.store.SearchCriterium;

/**
 *
 * @author rolf
 */
public class ScById implements SearchCriterium {

    private JpplId id;

    public ScById(JpplId id) {
        this.id = id;
    }

    public boolean isSelected(Basic basic) {
        return basic.getId().equals(id);
    }
}
