package com.metanology.mde.core.metaModel.statemachines;

import com.metanology.mde.utils.*;

public abstract class StateDiagramSerializer {

    public abstract void addToObjectRepository(XmlWriteObjectRepository objRep);

    public abstract StateDiagram createStateDiagramFromObjectRepository(XmlReadObjectRepository objRep, String val);
}
