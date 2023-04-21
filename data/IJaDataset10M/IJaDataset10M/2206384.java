package ssgen.sql.common.element.clause;

import ssgen.core.element.SsGenElement;

/**
 * Created by IntelliJ IDEA.
 * User: tadaya
 * Date: Mar 30, 2007
 * Time: 1:57:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroupByClause implements SsGenElement {

    private SsGenElement element;

    public Class<? extends SsGenElement> getElementClass() {
        return GroupByClause.class;
    }

    public boolean hasElement() {
        return element != null;
    }

    public SsGenElement getElement() {
        return element;
    }

    public void setElement(SsGenElement element) {
        this.element = element;
    }
}
