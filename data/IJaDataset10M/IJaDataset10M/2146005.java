package ssgen.sql.common.element.clause;

import ssgen.core.element.SsGenElement;

/**
 * Created by IntelliJ IDEA.
 * User: tadaya
 * Date: Mar 29, 2007
 * Time: 10:32:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class UpdateClause implements SsGenElement {

    private SsGenElement tableElement;

    public Class<? extends SsGenElement> getElementClass() {
        return UpdateClause.class;
    }

    public boolean hasTableElement() {
        return tableElement != null;
    }

    public SsGenElement getTableElement() {
        return tableElement;
    }

    public void setTableElement(SsGenElement tableElement) {
        this.tableElement = tableElement;
    }
}
