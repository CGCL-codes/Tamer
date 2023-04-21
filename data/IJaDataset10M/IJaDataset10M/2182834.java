package com.intel.gpe.clients.gpe4gtk.virtualworkspace;

import com.intel.gpe.clients.api.virtualworkspace.State;

/**
 * @author Sai Srinivas Dharanikota
 * @version $Id$
 */
public class StateImpl implements State {

    String type;

    public StateImpl(String type) {
        this.type = type;
    }

    public String getState() {
        return type;
    }
}
