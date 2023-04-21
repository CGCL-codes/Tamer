package uk.ac.roslin.ensembl.dao.database;

import com.ibatis.sqlmap.client.event.RowHandler;
import java.util.List;

/**
 *
 * @author tpaterso
 */
public interface DBRowHandler extends RowHandler {

    public List<? extends Object> getListResult();

    public Object getObjectResult();
}
