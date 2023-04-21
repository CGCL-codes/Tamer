package org.shapelogic.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** 
 * 
 * @author Sami Badawi
 *
 */
public abstract class BaseFilter<BaseClass, Element> implements IFilter<BaseClass, Element> {

    protected Collection<Element> _collection;

    protected BaseClass _parent;

    protected Object _constraint;

    @Override
    public Collection<Element> getCollection() {
        return _collection;
    }

    @Override
    public BaseClass getParent() {
        return _parent;
    }

    @Override
    public void setCollection(Collection<Element> collection) {
        _collection = collection;
    }

    @Override
    public void setParent(BaseClass parent) {
        _parent = parent;
    }

    public abstract boolean evaluate(Object arg0);

    @Override
    public Collection<Element> filter() {
        List<Element> result = new ArrayList<Element>();
        try {
            setup();
            for (Element shape : getCollection()) {
                if (evaluate(shape)) result.add(shape);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    @Deprecated
    public Object getConstraint() {
        return _constraint;
    }

    @Override
    public void setConstraint(Object constraint) {
        _constraint = constraint;
    }

    @Override
    public void setup() throws Exception {
    }
}
