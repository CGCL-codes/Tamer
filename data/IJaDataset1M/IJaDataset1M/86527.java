package edu.psu.citeseerx.dao2;

import java.util.List;
import java.sql.*;
import javax.sql.DataSource;
import org.springframework.context.ApplicationContextException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import edu.psu.citeseerx.domain.Table;

/**
 * TableDAO Implementation using MySQL as a persistent storage
 * @author Isaac Councill
 * @version $Rev: 852 $ $Date: 2008-12-24 23:04:39 -0500 (Wed, 24 Dec 2008) $
 */
public class TableDAOImpl extends JdbcDaoSupport implements TableDAO {

    protected GetTableById getById;

    protected CountTableMapping countTable;

    protected GetTablesByPaper getTablePaper;

    protected GetTablesByProxy getTableProxy;

    protected InsertTable insertTable;

    protected GetTableByUpdateTime getListUpdatedTables;

    protected GetTableLastIndexDate getTableLastIndex;

    protected UpdateTableLastIndexDate updateTableIndexTime;

    protected void initDao() throws ApplicationContextException {
        initMappingSqlQueries();
    }

    protected void initMappingSqlQueries() throws ApplicationContextException {
        getById = new GetTableById(getDataSource());
        countTable = new CountTableMapping(getDataSource());
        getTableProxy = new GetTablesByProxy(getDataSource());
        getTablePaper = new GetTablesByPaper(getDataSource());
        insertTable = new InsertTable(getDataSource());
        getListUpdatedTables = new GetTableByUpdateTime(getDataSource());
        getTableLastIndex = new GetTableLastIndexDate(getDataSource());
        updateTableIndexTime = new UpdateTableLastIndexDate(getDataSource());
    }

    public Table getTable(String id) throws DataAccessException {
        return getById.run(id);
    }

    public List<Table> getUpdatedTables(java.sql.Date dt) throws DataAccessException {
        return getListUpdatedTables.run(dt);
    }

    public List<Table> getTables(String id, boolean type) {
        if (type) {
            return getTableProxy.run(id);
        } else {
            return getTablePaper.run(id);
        }
    }

    public void insertTable(Table tobj) {
        insertTable.run(tobj);
    }

    public java.sql.Date lastTableIndexTime() {
        return getTableLastIndex.run();
    }

    public void updateTableIndexTime() {
        updateTableIndexTime.run();
    }

    public Integer countTable() throws DataAccessException {
        return countTable.run();
    }

    private static final String DEF_GET_TABLE_QUERY = "SELECT id, paperid, proxyID, inDocID, caption,footNote, " + "refText , pageNum, content FROM eTables WHERE id = ?";

    private class GetTableById extends MappingSqlQuery {

        public GetTableById(DataSource ds) {
            super(ds, DEF_GET_TABLE_QUERY);
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }

        public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
            Table tbl = new Table();
            tbl.setID(rs.getLong(1));
            tbl.setPaperIDForTable(rs.getString(2));
            tbl.setProxyKey(rs.getString(3));
            tbl.setTableID(rs.getString(4));
            tbl.setCaption(rs.getString(5));
            tbl.setFootNote(rs.getString(6));
            tbl.setTableReference(rs.getString(7));
            tbl.setTableOccursInPage(rs.getInt(8));
            tbl.setContent(rs.getString(9));
            return tbl;
        }

        public Table run(String id) {
            List<Table> list = execute(id);
            if (list.isEmpty()) {
                return null;
            } else {
                return (Table) list.get(0);
            }
        }
    }

    private static final String DEF_INSERT_TABLE = "INSERT INTO eTables (id, paperid, proxyID, inDocID, caption," + "footNote, refText, pageNum, content, updateTime) VALUES (NULL,?,?,?,?,?,?,?,?,NOW());";

    private class InsertTable extends SqlUpdate {

        public InsertTable(DataSource ds) {
            setDataSource(ds);
            setSql(DEF_INSERT_TABLE);
            declareParameter(new SqlParameter(Types.VARCHAR));
            declareParameter(new SqlParameter(Types.VARCHAR));
            declareParameter(new SqlParameter(Types.INTEGER));
            declareParameter(new SqlParameter(Types.VARCHAR));
            declareParameter(new SqlParameter(Types.VARCHAR));
            declareParameter(new SqlParameter(Types.VARCHAR));
            declareParameter(new SqlParameter(Types.INTEGER));
            declareParameter(new SqlParameter(Types.VARCHAR));
            setReturnGeneratedKeys(true);
            compile();
        }

        public int run(Table tobj) {
            Object[] params = new Object[] { tobj.getPaperIDForTable(), tobj.getProxyKey(), tobj.getTableID(), tobj.getCaption(), tobj.getFootNote(), tobj.getTableReference(), tobj.getTableOccursInPage(), tobj.getLimitedContent() };
            KeyHolder hold = new GeneratedKeyHolder();
            int n = update(params, hold);
            return n;
        }
    }

    private static final String DEF_GET_TABLE_BYPAPER = "SELECT id, paperid, proxyID, inDocID, caption,footNote, " + "refText , pageNum, content FROM eTables WHERE paperid = ?";

    private class GetTablesByPaper extends MappingSqlQuery {

        public GetTablesByPaper(DataSource ds) {
            super(ds, DEF_GET_TABLE_BYPROXY);
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }

        public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
            Table tbl = new Table();
            tbl.setID(rs.getLong(1));
            tbl.setPaperIDForTable(rs.getString(2));
            tbl.setProxyKey(rs.getString(3));
            tbl.setTableID(rs.getString(4));
            tbl.setCaption(rs.getString(5));
            tbl.setFootNote(rs.getString(6));
            tbl.setTableReference(rs.getString(7));
            tbl.setTableOccursInPage(rs.getInt(8));
            tbl.setContent(rs.getString(9));
            return tbl;
        }

        public List<Table> run(String id) {
            List<Table> tbllist = execute(id);
            if (tbllist.isEmpty()) {
                return null;
            } else {
                return tbllist;
            }
        }
    }

    private static final String DEF_GET_TABLE_BYPROXY = "SELECT id, paperid, proxyID, inDocID, caption,footNote, " + "refText , pageNum, content FROM eTables WHERE proxyID = ?";

    private class GetTablesByProxy extends MappingSqlQuery {

        public GetTablesByProxy(DataSource ds) {
            super(ds, DEF_GET_TABLE_BYPROXY);
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }

        public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
            Table tbl = new Table();
            tbl.setID(rs.getLong(1));
            tbl.setPaperIDForTable(rs.getString(2));
            tbl.setProxyKey(rs.getString(3));
            tbl.setTableID(rs.getString(4));
            tbl.setCaption(rs.getString(5));
            tbl.setFootNote(rs.getString(6));
            tbl.setTableReference(rs.getString(7));
            tbl.setTableOccursInPage(rs.getInt(8));
            tbl.setContent(rs.getString(9));
            return tbl;
        }

        public List<Table> run(String id) {
            List<Table> tbllist = execute(id);
            if (tbllist.isEmpty()) {
                return null;
            } else {
                return tbllist;
            }
        }
    }

    private static final String DEF_GET_TABLE_BYUPDATE = "SELECT id, paperid, proxyID, inDocID, caption,footNote, " + "refText , pageNum, content FROM eTables WHERE updateTime > ?";

    private class GetTableByUpdateTime extends MappingSqlQuery {

        public GetTableByUpdateTime(DataSource ds) {
            super(ds, DEF_GET_TABLE_BYUPDATE);
            declareParameter(new SqlParameter(Types.DATE));
            compile();
        }

        public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
            Table tbl = new Table();
            tbl.setID(rs.getLong(1));
            tbl.setPaperIDForTable(rs.getString(2));
            tbl.setProxyKey(rs.getString(3));
            tbl.setTableID(rs.getString(4));
            tbl.setCaption(rs.getString(5));
            tbl.setFootNote(rs.getString(6));
            tbl.setTableReference(rs.getString(7));
            tbl.setTableOccursInPage(rs.getInt(8));
            tbl.setContent(rs.getString(9));
            return tbl;
        }

        public List<Table> run(java.util.Date dt) {
            Object[] params = new Object[] { dt };
            List<Table> tbllist = execute(params);
            if (tbllist.isEmpty()) {
                return null;
            } else {
                return tbllist;
            }
        }
    }

    private static final String DEF_GET_LAST_INDEX_DATE = "select lastupdate from myciteseerx.indexTime where param =\"tableIndex\"";

    public class GetTableLastIndexDate extends MappingSqlQuery {

        public GetTableLastIndexDate(DataSource ds) {
            super(ds, DEF_GET_LAST_INDEX_DATE);
            compile();
        }

        public Date mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getDate(0);
        }

        public java.sql.Date run() {
            List<java.sql.Date> dList = execute();
            if (dList.isEmpty()) {
                return null;
            } else {
                return dList.get(0);
            }
        }
    }

    private static final String DEF_UPDATE_LAST_INDEX_DATE = "update myciteseerx.indexTime set lastupdate = now() where param =\"tableIndex\"";

    public class UpdateTableLastIndexDate extends SqlUpdate {

        public UpdateTableLastIndexDate(DataSource ds) {
            setDataSource(ds);
            setSql(DEF_UPDATE_LAST_INDEX_DATE);
            compile();
        }

        public int run() {
            return update();
        }
    }

    private static final String DEF_COUNT_TABLE_QUERY = "select count(*) as total from eTables";

    private class CountTableMapping extends MappingSqlQuery {

        public CountTableMapping(DataSource ds) {
            super(ds, DEF_COUNT_TABLE_QUERY);
            compile();
        }

        public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getInt(1);
        }

        public Integer run() {
            List<Integer> rlist = execute();
            if (rlist != null) {
                if (rlist.isEmpty()) {
                    return -1;
                } else {
                    return (Integer) rlist.get(0);
                }
            } else {
                return -1;
            }
        }
    }
}
