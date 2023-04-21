package com.idedeluxe.bc.core.model.code;

import com.idedeluxe.bc.core.model.struc.LeafStructure;
import com.idedeluxe.bc.core.model.struc.Structure;

public class JSR extends Code {

    public static final LeafStructure BRANCH = new LeafStructure("branch", 2);

    public static final Structure[] STRUCTURE = new Structure[] { MNEMONIC, BRANCH };

    public Structure[] getStructure() {
        return STRUCTURE;
    }
}
