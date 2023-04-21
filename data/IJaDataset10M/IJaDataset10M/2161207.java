package org.csiro.darjeeling.infuser.instructions;

import org.apache.bcel.Constants;
import org.csiro.darjeeling.infuser.structure.LocalId;

@SuppressWarnings("serial")
public class ANEWARRAY extends GlobalIdInstruction {

    public ANEWARRAY(LocalId globalClassId) {
        super(Constants.ANEWARRAY, globalClassId);
    }
}
