package com.c4j.composition;

import com.c4j.linker.IReference;
import com.c4j.workspace.IPort;

public interface IPortReference {

    IPort getPort();

    IReference<? extends IPort> getPortReference();
}
