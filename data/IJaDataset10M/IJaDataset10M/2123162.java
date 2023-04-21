package com.loribel.commons.module.states;

import java.util.ArrayList;
import java.util.List;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_StateMap;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.GB_ArrayTools;
import com.loribel.commons.util.collection.GB_MapWithIntegerKey;

/**
 * Abstraction of GB_StateMap.
 *
 * @author Gregory Borelli
 */
public class GB_StateMapImpl implements GB_StateMap {

    private GB_MapWithIntegerKey map = new GB_MapWithIntegerKey();

    private List states = new ArrayList();

    public GB_StateMapImpl() {
    }

    public void addState(int a_stateValue, GB_LabelIcon a_state) {
        states.add(new Integer(a_stateValue));
        map.put(a_stateValue, a_state);
    }

    public int getSize() {
        return CTools.getSize(states);
    }

    public GB_LabelIcon getState(int a_state) {
        return (GB_LabelIcon) map.get(a_state);
    }

    public int[] getStates() {
        return GB_ArrayTools.toArrayOfInt(states);
    }

    public void removeState(int a_stateValue, GB_LabelIcon a_state) {
        states.remove(new Integer(a_stateValue));
        map.remove(a_stateValue);
    }
}
