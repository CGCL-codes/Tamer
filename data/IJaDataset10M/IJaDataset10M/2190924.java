package com.cameocontrol.cameo.output;

import java.util.TreeSet;

public class CameoPatch extends BasicPatch {

    public String getIDValue() {
        return "cameo";
    }

    /**
	 * Default all channels all dimmers patched 1 to 1
	 *
	 */
    public CameoPatch() {
        for (int x = 0; x < _totalChannels; x++) {
            TreeSet<Integer> dims = new TreeSet<Integer>();
            dims.add(new Integer(x));
            _patch.add(dims);
        }
    }
}
