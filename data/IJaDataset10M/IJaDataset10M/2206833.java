package org.shapelogic.filter;

/** Similar to AllPredicate every filter has to be true for this to be true 
 * 
 * @author Sami Badawi
 *
 */
public class AllFilter<BaseClass, Element> extends BaseFilter<BaseClass, Element> {

    IFilter<BaseClass, Element>[] _filters;

    public AllFilter() {
    }

    public AllFilter(IFilter<BaseClass, Element>[] filters) {
        _filters = filters;
    }

    @Override
    public boolean evaluate(Object arg0) {
        if (_filters == null) return true;
        for (IFilter<BaseClass, Element> filter : _filters) {
            if (!filter.evaluate(arg0)) return false;
        }
        return true;
    }
}
