package ssgen.sql.common.writer.clause;

import ssgen.core.element.SsGenElement;
import ssgen.core.writer.AbstractElementWriter;
import ssgen.sql.common.element.clause.SelectClause;

/**
 * Created by IntelliJ IDEA.
 * User: tadaya
 * Date: Mar 29, 2007
 * Time: 9:41:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class SelectClauseWriter extends AbstractElementWriter {

    public String write(SsGenElement element) {
        SelectClause selectElement = (SelectClause) element;
        final StringBuilder sb = new StringBuilder();
        sb.append("select ");
        if (selectElement.isAll()) {
            sb.append("all ");
        } else if (selectElement.isDistinct()) {
            sb.append("distinct ");
        }
        sb.append(writeChildElement(selectElement.getElement()));
        return sb.toString();
    }
}
