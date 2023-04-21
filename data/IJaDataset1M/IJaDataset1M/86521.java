package edu.psu.citeseerx.dao2.logic;

import java.util.List;
import java.util.Iterator;
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
import edu.psu.citeseerx.domain.ThinDoc;
import edu.psu.citeseerx.domain.AuthorStatContainer;

public class CitationStatisticsDAOImpl extends JdbcDaoSupport implements CitationStatisticsDAO {

    private GetClusters getClusters;

    private GetInCol getInCol;

    private GetClustersByYear getClustersByYear;

    private GetInColByYear getInColByYear;

    private GetAuthorStats getAuthorStats;

    protected void initDao() throws ApplicationContextException {
        initMappingSqlQueries();
    }

    protected void initMappingSqlQueries() throws ApplicationContextException {
        getClusters = new GetClusters(getDataSource());
        getInCol = new GetInCol(getDataSource());
        getClustersByYear = new GetClustersByYear(getDataSource());
        getInColByYear = new GetInColByYear(getDataSource());
        getAuthorStats = new GetAuthorStats(getDataSource());
    }

    public List getMostCitedArticles(int amount, boolean includeCitations) throws DataAccessException {
        if (includeCitations) {
            return getClusters.run(amount);
        } else {
            return getInCol.run(amount);
        }
    }

    public List getMostCitedArticlesByYear(int amount, int year, boolean includeCitations) throws DataAccessException {
        if (includeCitations) {
            return getClustersByYear.run(year, amount);
        } else {
            return getInColByYear.run(year, amount);
        }
    }

    public List getAuthorStats(long startingID, int amount) throws DataAccessException {
        return getAuthorStats.run(startingID, amount);
    }

    private static final String DEF_GET_CLUSTERS_QUERY = "select id, size, incollection, cauth, ctitle, cvenue, cyear " + "from clusters order by size desc limit ?";

    private class GetClusters extends MappingSqlQuery {

        public GetClusters(DataSource dataSource) {
            setDataSource(dataSource);
            setSql(DEF_GET_CLUSTERS_QUERY);
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }

        public ThinDoc mapRow(ResultSet rs, int rowNum) throws SQLException {
            return mapThinDoc(rs);
        }

        public List run(int amount) {
            return execute(amount);
        }
    }

    private static final String DEF_GET_INCOL_QUERY = "select id, size, incollection, cauth, ctitle, cvenue, cyear " + "from clusters where incollection=1 order by size desc limit ?";

    private class GetInCol extends MappingSqlQuery {

        public GetInCol(DataSource dataSource) {
            setDataSource(dataSource);
            setSql(DEF_GET_INCOL_QUERY);
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }

        public ThinDoc mapRow(ResultSet rs, int rowNum) throws SQLException {
            return mapThinDoc(rs);
        }

        public List run(int amount) {
            return execute(amount);
        }
    }

    private static final String DEF_GET_CLUSTERS_BY_YEAR_QUERY = "select id, size, incollection, cauth, ctitle, cvenue, cyear " + "from clusters where cyear=? order by size desc limit ?";

    private class GetClustersByYear extends MappingSqlQuery {

        public GetClustersByYear(DataSource dataSource) {
            setDataSource(dataSource);
            setSql(DEF_GET_CLUSTERS_BY_YEAR_QUERY);
            declareParameter(new SqlParameter(Types.INTEGER));
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }

        public ThinDoc mapRow(ResultSet rs, int rowNum) throws SQLException {
            return mapThinDoc(rs);
        }

        public List run(int year, int amount) {
            Object[] params = new Object[] { new Integer(year), new Integer(amount) };
            return execute(params);
        }
    }

    private static final String DEF_GET_INCOL_BY_YEAR_QUERY = "select id, size, incollection, cauth, ctitle, cvenue, cyear " + "from clusters where cyear=? and incollection=1 " + "order by size desc limit ?";

    private class GetInColByYear extends MappingSqlQuery {

        public GetInColByYear(DataSource dataSource) {
            setDataSource(dataSource);
            setSql(DEF_GET_INCOL_BY_YEAR_QUERY);
            declareParameter(new SqlParameter(Types.INTEGER));
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }

        public ThinDoc mapRow(ResultSet rs, int rowNum) throws SQLException {
            return mapThinDoc(rs);
        }

        public List run(int year, int amount) {
            Object[] params = new Object[] { new Integer(year), new Integer(amount) };
            return execute(params);
        }
    }

    private static ThinDoc mapThinDoc(ResultSet rs) throws SQLException {
        ThinDoc doc = new ThinDoc();
        doc.setCluster(rs.getLong(1));
        doc.setNcites(rs.getInt(2));
        doc.setInCollection(rs.getBoolean(3));
        doc.setAuthors(rs.getString(4));
        doc.setTitle(rs.getString(5));
        doc.setVenue(rs.getString(6));
        doc.setYear(rs.getInt(7));
        return doc;
    }

    private static final String DEF_GET_AUTHORS_QUERY = "select id, cauth, size, selfCites from clusters where id>=? " + "order by id asc limit ?";

    private class GetAuthorStats extends MappingSqlQuery {

        public GetAuthorStats(DataSource dataSource) {
            setDataSource(dataSource);
            setSql(DEF_GET_AUTHORS_QUERY);
            declareParameter(new SqlParameter(Types.BIGINT));
            declareParameter(new SqlParameter(Types.INTEGER));
            compile();
        }

        public AuthorStatContainer mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong(1);
            String authors = rs.getString(2);
            int size = rs.getInt(3);
            int self = rs.getInt(4);
            AuthorStatContainer ac = new AuthorStatContainer(authors, size - self);
            ac.setCluster(id);
            return ac;
        }

        public List run(long startingID, int amount) {
            Object[] params = new Object[] { new Long(startingID), new Integer(amount) };
            return execute(params);
        }
    }
}
