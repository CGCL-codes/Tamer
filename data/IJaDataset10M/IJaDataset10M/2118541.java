package com.loribel.commons.module.states;

import com.loribel.commons.abstraction.GB_StateMap;
import com.loribel.commons.abstraction.GB_StateOwner;
import com.loribel.commons.util.GB_MapTools;
import com.loribel.commons.util.STools;
import com.loribel.commons.util.collection.GB_MapWithIntegerKey;

/**
 * Permet de construire une string indiquand le nombre d'item par state.
 *
 * @author Gregory Borelli
 */
public class GB_StateMapInfo {

    private int count = 0;

    private String labelItem = "Item(s)";

    /**
     * <ul>
     *   <li>key [Integer]: state</li>
     *   <li>value [Integer]: count of item</li>
     * </ul>
     */
    private GB_MapWithIntegerKey stateCountMap = new GB_MapWithIntegerKey();

    private GB_StateMap stateMap;

    public GB_StateMapInfo(GB_StateMap a_stateMap) {
        stateMap = a_stateMap;
    }

    public void addItem(GB_StateOwner a_owner) {
        if (a_owner == null) {
            return;
        }
        int l_state = a_owner.getState();
        addState(l_state);
    }

    public void addState(int a_state) {
        count++;
        Integer l_count = (Integer) stateCountMap.get(a_state);
        if (l_count == null) {
            stateCountMap.put(a_state, new Integer(1));
        } else {
            stateCountMap.put(a_state, new Integer(l_count.intValue() + 1));
        }
    }

    public int getCount(int a_state) {
        Integer l_count = (Integer) stateCountMap.get(a_state);
        if (l_count == null) {
            return 0;
        }
        return l_count.intValue();
    }

    public String getInfo() {
        String retour = count + " " + labelItem;
        int[] l_states = GB_MapTools.getIntKeys(stateCountMap);
        int len = l_states.length;
        for (int i = 0; i < len; i++) {
            int l_state = l_states[i];
            int l_count = getCount(l_state);
            String l_label = GB_StateTools.getLabel(stateMap, l_state);
            if (l_count > 0 && !STools.isNull(l_label)) {
                retour += " - " + l_label + " (" + l_count + ")";
            }
        }
        return retour;
    }

    public void removeItem(GB_StateOwner a_owner) {
        if (a_owner == null) {
            return;
        }
        int l_state = a_owner.getState();
        removeState(l_state);
    }

    public void removeState(int a_state) {
        count--;
        Integer l_count = (Integer) stateCountMap.get(a_state);
        if (l_count == null) {
            stateCountMap.put(a_state, new Integer(-1));
        } else {
            stateCountMap.put(a_state, new Integer(l_count.intValue() - 1));
        }
    }
}
