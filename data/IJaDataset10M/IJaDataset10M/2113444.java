package com.fray.evo.action.build;

import java.io.Serializable;
import com.fray.evo.util.ZergUnitLibrary;

public final class EcActionBuildMutalisk extends EcActionBuildUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    public EcActionBuildMutalisk() {
        super(ZergUnitLibrary.Mutalisk);
    }
}
