package net.sourceforge.webflowtemplate.reporting.unindexedfk.dao;

import javax.servlet.jsp.jstl.sql.Result;
import net.sourceforge.webflowtemplate.db.dao.DAO;
import org.springframework.dao.DataAccessException;

public interface UnindexedForeignKeyDAO extends DAO {

    public Result getReport() throws DataAccessException;
}
