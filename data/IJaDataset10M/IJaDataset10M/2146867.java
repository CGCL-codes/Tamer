package pcgen.cdom.facet;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.enumeration.CharID;

/**
 * This is a transition class, designed to allow things to be taken out of
 * PlayerCharacter while a transition is made to a sytem where abilities are
 * added in a forward manner, rather than a loop.
 */
public class EquipmentConsolidationFacet implements DataFacetChangeListener<CDOMObject> {

    private final CDOMObjectBridge bridge = CDOMObjectBridge.getInstance();

    public void add(CharID id, CDOMObject obj, Object source) {
        bridge.add(id, obj, source);
    }

    public void remove(CharID id, CDOMObject obj, Object source) {
        bridge.remove(id, obj, source);
    }

    public void addDataFacetChangeListener(DataFacetChangeListener<? super CDOMObject> listener) {
        bridge.addDataFacetChangeListener(listener);
    }

    public void dataAdded(DataFacetChangeEvent<CDOMObject> dfce) {
        CDOMObject cdo = dfce.getCDOMObject();
        add(dfce.getCharID(), cdo, dfce.getSource());
    }

    public void dataRemoved(DataFacetChangeEvent<CDOMObject> dfce) {
        CDOMObject cdo = dfce.getCDOMObject();
        remove(dfce.getCharID(), cdo, dfce.getSource());
    }
}
