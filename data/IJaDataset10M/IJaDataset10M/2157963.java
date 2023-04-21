package fi.arcusys.cygnus.view;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import fi.arcusys.acj.util.ListResult;
import fi.arcusys.cygnus.dao.OrderRowDAO;
import fi.arcusys.cygnus.model.OrderRow;

/**
 * @version 1.0 $Rev$
 * @author mikko
 * Copyright (C) 2008 Arcusys Oy
 */
public class AdminTools {

    private static final Log LOG = LogFactory.getLog(AdminTools.class);

    public String fixDatabaseConsistency() {
        LOG.info("Updating Statuses of OrderRow entities");
        OrderRowDAO dao = new OrderRowDAO();
        dao.beginTransaction();
        List<OrderRow> rows = dao.list();
        if (LOG.isInfoEnabled()) {
            LOG.info("Total number of OrderRow entities to update: " + rows.size());
        }
        for (OrderRow row : rows) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Updating row: " + row.getOrderCode());
            }
            row.updateStatus();
        }
        dao.commitTransaction();
        LOG.info("Order row statuses updated");
        return "ok";
    }
}
