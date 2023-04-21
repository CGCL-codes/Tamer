package org.sinaxe;

import java.util.List;
import org.sinaxe.runtime.RuntimeComponentBase;
import org.sinaxe.context.SinaxeVariableContext;
import org.dom4j.Node;

public interface SinaxeQuery {

    public SinaxeSubscription newSubscription();

    public String getString(Object context);

    public Object getNode(Object context);

    public List getNodes(Object context);

    public Number getNumber(Object context);

    public boolean getBoolean(Object context);

    public RuntimeComponentBase getComponentContext();

    public SinaxeVariableContext getVariableContext();
}
