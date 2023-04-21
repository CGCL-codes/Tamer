package com.ibm.tuningfork.infra.units;

import com.ibm.tuningfork.infra.sharing.ISharingConvertibleCallback;

/**
 * GC#
 * 
 */
public class GCIndex extends Count {

    public GCIndex() {
        super(0);
    }

    @Override
    public void collectReconstructionArguments(ISharingConvertibleCallback cb) throws Exception {
    }

    public String name() {
        return "Collector Invocation";
    }

    public String nominalUnit() {
        return "Garbage Collection";
    }

    public String nominalUnitAbbrv() {
        return "GC#";
    }
}
