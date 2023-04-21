package com.gorillalogic.dex.actions;

import org.apache.struts.action.*;
import org.apache.struts.config.*;
import javax.servlet.http.*;
import java.util.*;
import com.gorillalogic.dex.*;
import com.gorillalogic.dal.*;
import com.gorillalogic.dex.*;
import com.gorillalogic.webapp.MessageBean;
import org.apache.log4j.Logger;

/**
 *
 * @author  Stu
 */
public class LinkRowToTable extends DexAction {

    static Logger logger = Logger.getLogger(LinkRowToTable.class);

    /** Creates a new instance of GetRow */
    public LinkRowToTable() {
    }

    protected ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws DexException {
        goshlog.debug("-- linkRowToTable");
        MessageBean msgs = (MessageBean) request.getAttribute(Names.MESSAGES);
        Map map = request.getParameterMap();
        Set set = map.entrySet();
        Iterator i = set.iterator();
        String tableName = null, tableValue = null;
        String rowName = null, rowValue = null;
        boolean tableFound = false, rowFound = false;
        while (i.hasNext()) {
            Util.Parsed parsed = Util.parser.parse((Map.Entry) i.next());
            if (parsed.isTable) {
                tableFound = true;
                tableName = parsed.name;
                tableValue = parsed.value;
            } else if (parsed.isRow) {
                rowFound = true;
                rowName = parsed.name;
                rowValue = parsed.value;
            }
        }
        if (!tableFound || !rowFound) {
            throw new DexException("Expected parameters \"table_tableName\" and \"row_rowName\" not found.");
        }
        RowBean rowbean = new RowBean(rowValue);
        request.setAttribute(rowName, rowbean);
        Table.Row row = rowbean.getRow();
        TableBean tablebean = new TableBean(tableValue);
        request.setAttribute(tableName, tablebean);
        Table table = tablebean.getTable();
        try {
            Txn.mgr.begin();
            table.extend().addRef(row);
            Txn.mgr.commit();
            msgs.add(table.getName() + "(" + new RowBean(row).getDisplayKey() + ") link added.");
        } catch (Exception e) {
            throw new DexException("Error linking row to table", e);
        }
        return mapping.findForward(Names.Forwards.NORMAL);
    }
}
