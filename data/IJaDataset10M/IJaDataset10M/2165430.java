package de.ah7.imp.render;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * @author Andreas Huber <dev@ah7.de>
 */
public abstract class ParamListRenderSet extends BasicRenderSet {

    private List<Object> params = new Vector<Object>();

    public ParamListRenderSet(String renderType) {
        super(renderType);
    }

    @Override
    public Object addParameter(Object param) {
        this.params.add(param);
        return new Integer(this.params.size() - 1);
    }

    public Iterator getParamIterator() {
        return this.params.iterator();
    }

    public List getParams() {
        return this.params;
    }
}
