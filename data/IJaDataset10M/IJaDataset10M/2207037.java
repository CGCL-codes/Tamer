package com.mockturtlesolutions.snifflib.invprobs;

import java.io.*;
import java.beans.*;
import com.mockturtlesolutions.snifflib.guitools.components.IconServer;
import com.mockturtlesolutions.snifflib.graphics.AbstractIconEntityPersistenceDelegate;

public class GraphicalStatisticalModelPersistenceDelegate extends AbstractIconEntityPersistenceDelegate {

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        super.initialize(type, oldInstance, newInstance, out);
        GraphicalStatisticalModel m = (GraphicalStatisticalModel) oldInstance;
    }

    /**
	Instantiates an appropriate new instance which can be initialized using public methods.
	*/
    protected Expression instantiate(Object oldInstance, Encoder out) {
        GraphicalStatisticalModel m = (GraphicalStatisticalModel) oldInstance;
        return new Expression(oldInstance, oldInstance.getClass(), "new", new Object[] {});
    }
}
