package egovframework.com.uss.ion.rss.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import egovframework.com.cmm.ComDefaultCodeVO;
import egovframework.com.uss.ion.rss.service.RssManage;
import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

/**
 * RSS태그관리를 처리하는 Dao Class 구현
 * @author 공통콤포넌트 장동한
 * @since 2010.06.16
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.07.03  장동한          최초 생성
 *   2011.10.18  서준식          Altibase DB 처리를 위한 코드 추가
 * </pre>
 */
@Repository("rssManageDao")
public class RssTagManageDao extends EgovComAbstractDAO {

    /**
     * JDBC 테이블 목록을조회한다.
     * @return List -조회한목록이담긴List
     * @throws Exception
     */
    public List selectRssTagManageTableList() throws Exception {
        String TABLE_NAME = "TABLE_NAME";
        String TABLE_SCHEMA = "TABLE_SCHEM";
        String[] TABLE_AND_VIEW_TYPES = { "TABLE", "VIEW" };
        ArrayList arrListResult = new ArrayList();
        Connection conn = null;
        DatabaseMetaData dbmd = null;
        ResultSet tables = null;
        try {
            conn = getSqlMapClientTemplate().getDataSource().getConnection();
            dbmd = conn.getMetaData();
            tables = dbmd.getTables(null, null, null, TABLE_AND_VIEW_TYPES);
            while (tables.next()) {
                ComDefaultCodeVO codeVO = new ComDefaultCodeVO();
                codeVO.setCode(tables.getString(TABLE_NAME));
                codeVO.setCodeNm(tables.getString(TABLE_SCHEMA));
                arrListResult.add((ComDefaultCodeVO) codeVO);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            if (tables != null) {
                try {
                    tables.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            if (dbmd != null) {
                try {
                    dbmd = null;
                } catch (Exception e) {
                    System.out.println("IGNORE: " + e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    System.out.println("IGNORE: " + e);
                }
            }
        }
        return arrListResult;
    }

    /**
     * JDBC 테이블 컬럼 목록을 조회한다.
     * @param map - 컬럼조회정보
     * @return List -조회한목록이담긴List
     * @throws Exception
     */
    public List selectRssTagManageTableColumnList(Map map) throws Exception {
        String sTableName = (String) map.get("tableName");
        String sDbType = (String) map.get("dbType");
        String sSQL = "";
        ArrayList arrListResult = new ArrayList();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = getSqlMapClientTemplate().getDataSource().getConnection();
            if (!sDbType.equals("altibase")) {
                st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            }
            st = conn.createStatement();
            if (sDbType.equals("mysql")) {
                sSQL = "SELECT * FROM " + sTableName + " LIMIT 1 ";
            } else {
                sSQL = "SELECT * FROM " + sTableName + " WHERE ROWNUM <= 1 ";
            }
            rs = st.executeQuery(sSQL);
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int numberOfColumns = rsMetaData.getColumnCount();
            for (int i = 1; i < numberOfColumns + 1; i++) {
                Map hmResult = new HashMap();
                hmResult.put("code", (String) rsMetaData.getTableName(i));
                hmResult.put("codeNm", (String) rsMetaData.getColumnName(i));
                arrListResult.add((Map) hmResult);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println("IGNORE: " + e);
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (Exception e) {
                    System.out.println("IGNORE: " + e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    System.out.println("IGNORE: " + e);
                }
            }
        }
        return arrListResult;
    }

    /**
     * RSS태그관리를(을) 목록을 한다.
     * @param rssManage -조회할 정보가 담긴 객체
     * @return -조회한목록이담긴List
     * @throws Exception
     */
    public List selectRssTagManageList(RssManage rssManage) throws Exception {
        return (List) list("RssTagManage.selectRssTagManage", rssManage);
    }

    /**
     * RSS태그관리를(을) 목록 전체 건수를(을) 조회한다.
     * @param rssManage -조회할 정보가 담긴 객체
     * @return -조회한건수가담긴Integer
     * @throws Exception
     */
    public int selectRssTagManageListCnt(RssManage rssManage) throws Exception {
        return (Integer) getSqlMapClientTemplate().queryForObject("RssTagManage.selectRssTagManageCnt", rssManage);
    }

    /**
     * RSS태그관리를(을) 상세조회 한다.
     * @param rssManage -RSS태그관리 정보가 담김 객체
     * @return RssManage -RSS태그관리 정보가 담김 객체
     * @throws Exception
     */
    public RssManage selectRssTagManageDetail(RssManage rssManage) throws Exception {
        return (RssManage) getSqlMapClientTemplate().queryForObject("RssTagManage.selectRssTagManageDetail", rssManage);
    }

    /**
     * RSS태그관리를(을) 등록한다.
     * @param rssManage -RSS태그관리 정보가 담김 객체
     * @throws Exception
     */
    public void insertRssTagManage(RssManage rssManage) throws Exception {
        insert("RssTagManage.insertRssTagManage", rssManage);
    }

    /**
     * RSS태그관리를(을) 수정한다.
     * @param rssManage -RSS태그관리 정보가 담김 객체
     * @throws Exception
     */
    public void updateRssTagManage(RssManage rssManage) throws Exception {
        update("RssTagManage.updateRssTagManage", rssManage);
    }

    /**
     * RSS태그관리를(을) 삭제한다.
     * @param rssManage -RSS태그관리 정보가 담김 객체
     * @throws Exception
     */
    public void deleteRssTagManage(RssManage rssManage) throws Exception {
        delete("RssTagManage.deleteRssTagManage", rssManage);
    }
}
