package edu.psu.citeseerx.dao2;

import edu.psu.citeseerx.domain.Hub;
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

/**
 * CitationDAO Spring-JDBC Implementation using MySQL as a persistent storage
 * @author Isaac Councill
 * @version $Rev$ $Date$
 */
public class HubDAOImpl extends JdbcDaoSupport implements HubDAO {

    private GetHubs getHubs;

    private GetHubsForUrl getHubsForUrl;

    private GetHub getHub;

    private InsertHub insertHub;

    private UpdateHub updateHub;

    private InsertHubMapping insertHubMapping;

    private GetHubID getHubID;

    private GetUrlID getUrlID;

    private InsertUrl insertUrl;

    private GetUrls getUrls;

    private GetPaperIDSForHub getPaperIDSForHub;

    protected void initDao() throws ApplicationContextException {
        initMappingSqlQueries();
    }

    protected void initMappingSqlQueries() throws ApplicationContextException {
        getHubs = new GetHubs(getDataSource());
        getHubsForUrl = new GetHubsForUrl(getDataSource());
        getHub = new GetHub(getDataSource());
        insertHub = new InsertHub(getDataSource());
        updateHub = new UpdateHub(getDataSource());
        insertHubMapping = new InsertHubMapping(getDataSource());
        getHubID = new GetHubID(getDataSource());
        getUrlID = new GetUrlID(getDataSource());
        insertUrl = new InsertUrl(getDataSource());
        getUrls = new GetUrls(getDataSource());
        getPaperIDSForHub = new GetPaperIDSForHub(getDataSource());
    }

    public List<Hub> getHubs(String doi) throws DataAccessException {
        return getHubs.run(doi);
    }

    public List<Hub> getHubsForUrl(String url) throws DataAccessException {
        return getHubsForUrl.run(url);
    }

    public Hub getHub(String url) throws DataAccessException {
        return getHub.run(url);
    }

    public long insertHub(Hub hub) throws DataAccessException {
        return insertHub.run(hub);
    }

    public void insertHubMapping(long urlID, long hubID) throws DataAccessException {
        insertHubMapping.run(urlID, hubID);
    }

    public void updateHub(Hub hub) throws DataAccessException {
        updateHub.run(hub);
    }

    public void addHubMapping(Hub hub, String url, String doi) throws DataAccessException {
        long hid = getHubID.run(hub.getUrl());
        if (hid <= 0) {
            hid = insertHub.run(hub);
        }
        long uid = getUrlID.run(url);
        if (uid <= 0) {
            uid = insertUrl.run(doi, url);
        }
        insertHubMapping.run(uid, hid);
    }

    public List<String> getUrls(String doi) throws DataAccessException {
        return getUrls.run(doi);
    }

    public long insertUrl(String doi, String url) throws DataAccessException {
        return insertUrl.run(doi, url);
    }

    public List<String> getPaperIdsFromHubUrl(String hubUrl) throws DataAccessException {
        List<String> paperIDs = null;
        long hid = getHubID.run(hubUrl);
        if (hid < 0) {
            paperIDs = null;
        } else {
            paperIDs = getPaperIDSForHub.run(hid);
            if (paperIDs.isEmpty()) {
                paperIDs = null;
            }
        }
        return paperIDs;
    }

    private static final String DEF_GET_HUBS_QUERY = "select hubUrls.url, lastCrawl, hubUrls.repositoryID from hubUrls, " + "hubMap, urls, papers where papers.id=? and urls.paperid=papers.id " + "and urls.id=hubMap.urlid and hubUrls.id=hubMap.hubid";

    private class GetHubs extends MappingSqlQuery {

        public GetHubs(DataSource dataSource) {
            setDataSource(dataSource);
            setSql(DEF_GET_HUBS_QUERY);
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }

        public Hub mapRow(ResultSet rs, int rowNum) throws SQLException {
            Hub hub = new Hub();
            hub.setUrl(rs.getString(1));
            hub.setLastCrawled(new java.util.Date(rs.getTimestamp(2).getTime()));
            hub.setRepID(rs.getString(3));
            return hub;
        }

        public List<Hub> run(String doi) {
            return execute(doi);
        }
    }

    private static final String DEF_GET_HUBS_FOR_URL_QUERY = "select hubUrls.url, lastCrawl, repositoryID from hubUrls, hubMap, " + "urls where urls.url=? and urls.id=hubMap.urlid and " + "hubUrls.id=hubMap.hubid";

    private class GetHubsForUrl extends MappingSqlQuery {

        public GetHubsForUrl(DataSource dataSource) {
            setDataSource(dataSource);
            setSql(DEF_GET_HUBS_FOR_URL_QUERY);
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }

        public Hub mapRow(ResultSet rs, int rowNum) throws SQLException {
            Hub hub = new Hub();
            hub.setUrl(rs.getString(1));
            hub.setLastCrawled(new java.util.Date(rs.getTimestamp(2).getTime()));
            hub.setRepID(rs.getString(3));
            return hub;
        }

        public List<Hub> run(String url) {
            return execute(url);
        }
    }

    private static final String DEF_GET_HUB_QUERY = "select lastCrawl, repositoryID from hubUrls where url=?";

    private class GetHub extends MappingSqlQuery {

        public GetHub(DataSource dataSource) {
            setDataSource(dataSource);
            setSql(DEF_GET_HUB_QUERY);
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }

        public Hub mapRow(ResultSet rs, int rowNum) throws SQLException {
            Hub hub = new Hub();
            hub.setLastCrawled(new java.util.Date(rs.getTimestamp(1).getTime()));
            hub.setRepID(rs.getString(2));
            return hub;
        }

        public Hub run(String url) {
            List<Hub> list = execute(url);
            if (list.isEmpty()) {
                return null;
            } else {
                Hub hub = (Hub) list.get(0);
                hub.setUrl(url);
                return hub;
            }
        }
    }

    private static final String DEF_INSERT_HUB_STMT = "insert into hubUrls values (NULL, ?, ?, ?)";

    private class InsertHub extends SqlUpdate {

        public InsertHub(DataSource dataSource) {
            setDataSource(dataSource);
            setSql(DEF_INSERT_HUB_STMT);
            declareParameter(new SqlParameter(Types.VARCHAR));
            declareParameter(new SqlParameter(Types.TIMESTAMP));
            declareParameter(new SqlParameter(Types.VARCHAR));
            setReturnGeneratedKeys(true);
            compile();
        }

        public long run(Hub hub) {
            Object[] params = new Object[] { hub.getUrl(), new Timestamp(hub.getLastCrawled().getTime()), hub.getRepID() };
            KeyHolder holder = new GeneratedKeyHolder();
            update(params, holder);
            return holder.getKey().longValue();
        }
    }

    private static final String DEF_UPDATE_HUB_STMT = "update hubUrls set lastCrawl=?, repositoryID=? where url=?";

    private class UpdateHub extends SqlUpdate {

        public UpdateHub(DataSource dataSource) {
            setDataSource(dataSource);
            setSql(DEF_UPDATE_HUB_STMT);
            declareParameter(new SqlParameter(Types.TIMESTAMP));
            declareParameter(new SqlParameter(Types.VARCHAR));
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }

        public int run(Hub hub) {
            Object[] params = new Object[] { new Timestamp(hub.getLastCrawled().getTime()), hub.getRepID(), hub.getUrl() };
            return update(params);
        }
    }

    private static final String DEF_INSERT_HUBMAP_STMT = "insert into hubMap values (NULL, ?, ?)";

    private class InsertHubMapping extends SqlUpdate {

        public InsertHubMapping(DataSource dataSource) {
            setDataSource(dataSource);
            setSql(DEF_INSERT_HUBMAP_STMT);
            declareParameter(new SqlParameter(Types.BIGINT));
            declareParameter(new SqlParameter(Types.BIGINT));
            compile();
        }

        public int run(long urlID, long hubID) {
            Object[] params = new Object[] { new Long(urlID), new Long(hubID) };
            return update(params);
        }
    }

    private static final String DEF_GET_HUBID_QUERY = "select id from hubUrls where url=?";

    private class GetHubID extends MappingSqlQuery {

        public GetHubID(DataSource dataSource) {
            setDataSource(dataSource);
            setSql(DEF_GET_HUBID_QUERY);
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }

        public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getLong(1);
        }

        public long run(String url) {
            List<Long> list = execute(url);
            if (list.isEmpty()) {
                return 0;
            } else {
                return (Long) list.get(0);
            }
        }
    }

    private static final String DEF_GET_URLID_QUERY = "select id from urls where url=?";

    private class GetUrlID extends MappingSqlQuery {

        public GetUrlID(DataSource dataSource) {
            setDataSource(dataSource);
            setSql(DEF_GET_URLID_QUERY);
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }

        public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getLong(1);
        }

        public long run(String url) {
            List<Long> list = execute(url);
            if (list.isEmpty()) {
                return 0;
            } else {
                return (Long) list.get(0);
            }
        }
    }

    private static final String DEF_INS_URL_STMT = "insert into urls values (NULL, ?, ?)";

    private class InsertUrl extends SqlUpdate {

        public InsertUrl(DataSource dataSource) {
            setDataSource(dataSource);
            setSql(DEF_INS_URL_STMT);
            declareParameter(new SqlParameter(Types.VARCHAR));
            declareParameter(new SqlParameter(Types.VARCHAR));
            setReturnGeneratedKeys(true);
            compile();
        }

        public long run(String doi, String url) {
            Object[] params = new Object[] { url, doi };
            KeyHolder holder = new GeneratedKeyHolder();
            update(params, holder);
            return holder.getKey().longValue();
        }
    }

    private static final String DEF_GET_URLS_STMT = "select url from urls where paperid=?";

    private class GetUrls extends MappingSqlQuery {

        public GetUrls(DataSource dataSource) {
            setDataSource(dataSource);
            setSql(DEF_GET_URLS_STMT);
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }

        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString("url");
        }

        public List<String> run(String doi) {
            return execute(doi);
        }
    }

    private static String DEF_GET_PAPER_IDS_FOR_HUB_STMT = "select " + "urls.paperid from hubUrls, hubMap, urls where " + "hubUrls.id = hubMap.hubid and hubMap.urlid = urls.id " + "and hubUrls.id = ? order by urls.paperid";

    private class GetPaperIDSForHub extends MappingSqlQuery {

        public GetPaperIDSForHub(DataSource dataSource) {
            setDataSource(dataSource);
            setSql(DEF_GET_PAPER_IDS_FOR_HUB_STMT);
            declareParameter(new SqlParameter(Types.BIGINT));
            compile();
        }

        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString("paperid");
        }

        public List<String> run(long hubUrlId) {
            return execute(hubUrlId);
        }
    }
}
