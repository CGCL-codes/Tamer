package com.xmultra.processor.db.cms.helper;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import oracle.jdbc.driver.OracleCallableStatement;
import oracle.sql.CLOB;
import com.xmultra.processor.db.DataWriter;
import com.xmultra.processor.db.cms.content.BasicArticle;

/**
 * <p>Title: ArticleCSHelper </p>
 * <p>Description: A helper class to map BasicArticle attributes to the right
 * parameters in s_insert_article stored procedure</p>
 *
 * @author Shannon Brown <sbrown@knightridder.com>
 * @version 1.0*/
public class ArticleCSHelper {

    private static String INSERT_ARTICLE_SQL = "{? = call S_INSERTARTICLE(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

    public static String URL_RETRIEVAL_FUNCTION = "pkg_article_feeds.get_article_urls";

    private static String GET_ARTICLE_URLS_SQL = "{? = call " + URL_RETRIEVAL_FUNCTION + "(?)}";

    /**
   * @return database Id of article
   */
    public static int storeArticle(BasicArticle ba, Connection con) throws SQLException {
        OracleCallableStatement ocs = null;
        ResultSet rs = null;
        int id = 0;
        try {
            ocs = (OracleCallableStatement) con.prepareCall(INSERT_ARTICLE_SQL);
            if (ocs != null && ba != null) {
                setArticleId(ba.getArticleId(), ocs);
                setPubId(ba.getPubId(), ocs);
                setCategoryId(ba.getCategoryId(), ocs);
                setStartTime(ba.getStartTime(), ocs);
                setEndTime(ba.getEndTime(), ocs);
                setMemo("", ocs);
                setCorrection("", ocs);
                setHeadline(ba.getHeadline(), ocs);
                setLead(ba.getLead(), ocs);
                setDateline(ba.getDateline(), ocs);
                setKeywords("", ocs);
                setPubData("", ocs);
                setVersionNum(ba.getVersionNum(), ocs);
                setDocType(0, ocs);
                setShortHeadline(ba.getShortHeadline(), ocs);
                setByline(ba.getByline(), ocs);
                setData("", ocs);
                setBody(ba.getBody(), ocs, con);
                setRank(ba.getRank(), ocs);
                setHeadline2(ba.getHeadline2(), ocs);
                setByCredit(ba.getBycredit(), ocs);
                setWebLead(ba.getWebLead(), ocs);
                setUseTopixLinks(0, ocs);
                setFilename(ba.getFileName(), ocs);
                setDisableIndex(ba.getDisableIndex(), ocs);
                setColumnistId(ba.getColumnistId(), ocs);
                setWebHeadline(ba.getWebHeadline(), ocs);
                setCustomURL(ba.getCustomURL(), ocs);
                setLeadin("", ocs);
                setStatus(ba.getStatus(), ocs);
                setCreateTime(null, ocs);
                setUpdateTime(null, ocs);
                setCreateBy(ba.getCreateBy(), ocs);
                setUpdateBy(ba.getUpdateBy(), ocs);
                setCategoryName(ba.getCatName(), ocs);
                setPubName(ba.getPubName(), ocs);
                setColumnistName(ba.getColumnistName(), ocs);
                setAltName("", ocs);
                setAltDesc("", ocs);
                setDocSourceType(ba.getDocSourceType(), ocs);
                setDocSource(ba.getDocSource(), ocs);
                setSig(ba.getSig(), ocs);
                setKicker(ba.getKicker(), ocs);
                if (ba.isHasTable()) {
                    setHasTable(1, ocs);
                } else {
                    setHasTable(0, ocs);
                }
                setNpSection(ba.getNpSection(), ocs);
                setNpPage(ba.getNpPage(), ocs);
                setNpEdition(ba.getNpEdition(), ocs);
                if (ba.isMapToTaxonomy()) {
                    setMapToTaxonomy(1, ocs);
                } else {
                    setMapToTaxonomy(0, ocs);
                }
                setPubDate(ba.getPubDate(), ocs);
                setOverride(1, ocs);
                ocs.registerOutParameter(1, oracle.jdbc.driver.OracleTypes.CURSOR);
                ocs.executeUpdate();
                rs = ocs.getCursor(1);
                while (rs.next()) {
                    id = rs.getInt("article_id");
                    ba.setArticleId(id);
                }
                con.commit();
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException sqle) {
                rs = null;
            }
            try {
                if (ocs != null) {
                    ocs.close();
                }
            } catch (SQLException s) {
                ocs = null;
            }
        }
        return ba.getArticleId();
    }

    /**
   * After storing an article in the database, call
   * pkg_article_feeds.get_article_urls to retrieve the URLs
   * so they may be added to the NITF file for syndication.
   *
   * @author Bob Hucker
   * @param article the article that was inserted into the database earlier.
   * @param originalFileName name of the content package XML file being processed
   * @param con database connection
   * @param dataWriter DataWriter with locations information for writing
   *        the URLs
   */
    public static void writeUrlsToFile(BasicArticle article, String originalFileName, Connection con, DataWriter dataWriter) throws SQLException {
        OracleCallableStatement ocs = null;
        ResultSet rs = null;
        try {
            ocs = (OracleCallableStatement) con.prepareCall(GET_ARTICLE_URLS_SQL);
            if (ocs != null && article != null) {
                setArticleId(article.getArticleId(), ocs);
                ocs.registerOutParameter(1, oracle.jdbc.driver.OracleTypes.CURSOR);
                ocs.executeUpdate();
                rs = ocs.getCursor(1);
                while (rs.next()) {
                    dataWriter.append(rs);
                }
            }
        } finally {
            try {
                dataWriter.write(originalFileName, URL_RETRIEVAL_FUNCTION);
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException sqle) {
                rs = null;
            }
            try {
                if (ocs != null) {
                    ocs.close();
                }
            } catch (SQLException s) {
                ocs = null;
            }
        }
    }

    private static void setArticleId(int id, OracleCallableStatement ocs) throws SQLException {
        ocs.setInt(2, id);
    }

    private static void setPubId(int id, OracleCallableStatement ocs) throws SQLException {
        ocs.setInt(3, id);
    }

    private static void setCategoryId(int id, OracleCallableStatement ocs) throws SQLException {
        ocs.setInt(4, id);
    }

    private static void setStartTime(Date sTime, OracleCallableStatement ocs) throws SQLException {
        if (sTime == null) {
            ocs.setNull(5, java.sql.Types.DATE);
        } else {
            ocs.setTimestamp(5, new Timestamp(sTime.getTime()));
        }
    }

    private static void setEndTime(Date eTime, OracleCallableStatement ocs) throws SQLException {
        if (eTime == null) {
            ocs.setNull(6, java.sql.Types.DATE);
        } else {
            ocs.setTimestamp(6, new Timestamp(eTime.getTime()));
        }
    }

    private static void setMemo(String memo, OracleCallableStatement ocs) throws SQLException {
        if (memo == null || memo.equals("")) {
            ocs.setString(7, "");
        } else {
            ocs.setString(7, memo);
        }
    }

    private static void setCorrection(String corr, OracleCallableStatement ocs) throws SQLException {
        if (corr == null || corr.equals("")) {
            ocs.setString(8, "");
        } else {
            ocs.setString(8, corr);
        }
    }

    private static void setHeadline(String headline, OracleCallableStatement ocs) throws SQLException {
        if (headline == null || headline.equals("")) {
            ocs.setString(9, "");
        } else {
            ocs.setString(9, headline);
        }
    }

    private static void setLead(String lead, OracleCallableStatement ocs) throws SQLException {
        if (lead == null || lead.equals("")) {
            ocs.setString(10, "");
        } else {
            ocs.setString(10, lead);
        }
    }

    private static void setDateline(String dateline, OracleCallableStatement ocs) throws SQLException {
        if (dateline == null || dateline.equals("")) {
            ocs.setString(11, "");
        } else {
            ocs.setString(11, dateline);
        }
    }

    private static void setKeywords(String keywords, OracleCallableStatement ocs) throws SQLException {
        if (keywords == null || keywords.equals("")) {
            ocs.setString(12, "");
        } else {
            ocs.setString(12, keywords);
        }
    }

    private static void setPubData(String pubData, OracleCallableStatement ocs) throws SQLException {
        if (pubData == null || pubData.equals("")) {
            ocs.setString(13, "");
        } else {
            ocs.setString(13, pubData);
        }
    }

    private static void setVersionNum(int versionNum, OracleCallableStatement ocs) throws SQLException {
        ocs.setInt(14, versionNum);
    }

    private static void setDocType(int docType, OracleCallableStatement ocs) throws SQLException {
        ocs.setInt(15, docType);
    }

    private static void setShortHeadline(String shortHeadline, OracleCallableStatement ocs) throws SQLException {
        if (shortHeadline == null || shortHeadline.equals("")) {
            ocs.setString(16, "");
        } else {
            ocs.setString(16, shortHeadline);
        }
    }

    private static void setByline(String byline, OracleCallableStatement ocs) throws SQLException {
        if (byline == null || byline.equals("")) {
            ocs.setString(17, "");
        } else {
            ocs.setString(17, byline);
        }
    }

    private static void setData(String data, OracleCallableStatement ocs) throws SQLException {
        if (data == null || data.equals("")) {
            ocs.setString(18, "");
        } else {
            ocs.setString(18, data);
        }
    }

    private static void setBody(String body, OracleCallableStatement ocs, Connection con) throws SQLException {
        if (body == null || body.equals("")) {
            ocs.setNull(19, java.sql.Types.CLOB);
        } else {
            oracle.sql.CLOB myClob = null;
            try {
                myClob = getClobFromString(body, con);
            } catch (IOException io) {
                io.printStackTrace();
                throw new SQLException("Error creating CLOB");
            }
            ocs.setCLOB(19, myClob);
        }
    }

    private static void setRank(int rank, OracleCallableStatement ocs) throws SQLException {
        ocs.setInt(20, rank);
    }

    private static void setHeadline2(String h2, OracleCallableStatement ocs) throws SQLException {
        if (h2 == null || h2.equals("")) {
            ocs.setString(21, "");
        } else {
            ocs.setString(21, h2);
        }
    }

    private static void setByCredit(String bc, OracleCallableStatement ocs) throws SQLException {
        if (bc == null || bc.equals("")) {
            ocs.setString(22, "");
        } else {
            ocs.setString(22, bc);
        }
    }

    private static void setWebLead(String wl, OracleCallableStatement ocs) throws SQLException {
        if (wl == null || wl.equals("")) {
            ocs.setString(23, "");
        } else {
            ocs.setString(23, wl);
        }
    }

    private static void setUseTopixLinks(int useTopixLinks, OracleCallableStatement ocs) throws SQLException {
        ocs.setInt(24, useTopixLinks);
    }

    private static void setFilename(String fn, OracleCallableStatement ocs) throws SQLException {
        if (fn == null || fn.equals("")) {
            ocs.setString(25, "");
        } else {
            ocs.setString(25, fn);
        }
    }

    private static void setDisableIndex(int di, OracleCallableStatement ocs) throws SQLException {
        ocs.setInt(26, di);
    }

    private static void setColumnistId(int cid, OracleCallableStatement ocs) throws SQLException {
        ocs.setInt(27, cid);
    }

    private static void setWebHeadline(String wh, OracleCallableStatement ocs) throws SQLException {
        if (wh == null || wh.equals("")) {
            ocs.setString(28, "");
        } else {
            ocs.setString(28, wh);
        }
    }

    private static void setCustomURL(String cUrl, OracleCallableStatement ocs) throws SQLException {
        if (cUrl == null || cUrl.equals("")) {
            ocs.setString(29, "");
        } else {
            ocs.setString(29, cUrl);
        }
    }

    private static void setLeadin(String li, OracleCallableStatement ocs) throws SQLException {
        if (li == null || li.equals("")) {
            ocs.setString(30, "");
        } else {
            ocs.setString(30, li);
        }
    }

    private static void setStatus(int status, OracleCallableStatement ocs) throws SQLException {
        ocs.setInt(31, status);
    }

    private static void setCreateTime(Date cTime, OracleCallableStatement ocs) throws SQLException {
        if (cTime == null) {
            ocs.setNull(32, java.sql.Types.DATE);
        } else {
            ocs.setTimestamp(32, new Timestamp(cTime.getTime()));
        }
    }

    private static void setUpdateTime(Date uTime, OracleCallableStatement ocs) throws SQLException {
        if (uTime == null) {
            ocs.setNull(33, java.sql.Types.DATE);
        } else {
            ocs.setTimestamp(33, new Timestamp(uTime.getTime()));
        }
    }

    private static void setCreateBy(String createBy, OracleCallableStatement ocs) throws SQLException {
        if (createBy == null || createBy.equals("")) {
            ocs.setString(34, "");
        } else {
            ocs.setString(34, createBy);
        }
    }

    private static void setUpdateBy(String updateBy, OracleCallableStatement ocs) throws SQLException {
        if (updateBy == null || updateBy.equals("")) {
            ocs.setString(35, "");
        } else {
            ocs.setString(35, updateBy);
        }
    }

    private static void setCategoryName(String categoryName, OracleCallableStatement ocs) throws SQLException {
        if (categoryName == null || categoryName.equals("")) {
            ocs.setString(36, "");
        } else {
            ocs.setString(36, categoryName);
        }
    }

    private static void setPubName(String pubName, OracleCallableStatement ocs) throws SQLException {
        if (pubName == null || pubName.equals("")) {
            ocs.setString(37, "");
        } else {
            ocs.setString(37, pubName);
        }
    }

    private static void setColumnistName(String columnistName, OracleCallableStatement ocs) throws SQLException {
        if (columnistName == null || columnistName.equals("")) {
            ocs.setString(38, "");
        } else {
            ocs.setString(38, columnistName);
        }
    }

    private static void setAltName(String altName, OracleCallableStatement ocs) throws SQLException {
        if (altName == null || altName.equals("")) {
            ocs.setString(39, "");
        } else {
            ocs.setString(39, altName);
        }
    }

    private static void setAltDesc(String altDesc, OracleCallableStatement ocs) throws SQLException {
        if (altDesc == null || altDesc.equals("")) {
            ocs.setString(40, "");
        } else {
            ocs.setString(40, altDesc);
        }
    }

    private static void setDocSourceType(String docSourceType, OracleCallableStatement ocs) throws SQLException {
        if (docSourceType == null || docSourceType.equals("")) {
            ocs.setString(41, "");
        } else {
            ocs.setString(41, docSourceType);
        }
    }

    private static void setDocSource(String docSource, OracleCallableStatement ocs) throws SQLException {
        if (docSource == null || docSource.equals("")) {
            ocs.setString(42, "");
        } else {
            ocs.setString(42, docSource);
        }
    }

    private static void setSig(String sig, OracleCallableStatement ocs) throws SQLException {
        if (sig == null || sig.equals("")) {
            ocs.setString(43, "");
        } else {
            ocs.setString(43, sig);
        }
    }

    private static void setKicker(String kicker, OracleCallableStatement ocs) throws SQLException {
        if (kicker == null || kicker.equals("")) {
            ocs.setString(44, "");
        } else {
            ocs.setString(44, kicker);
        }
    }

    private static void setHasTable(int hasTable, OracleCallableStatement ocs) throws SQLException {
        ocs.setInt(45, hasTable);
    }

    private static void setNpSection(String npSection, OracleCallableStatement ocs) throws SQLException {
        if (npSection == null || npSection.equals("")) {
            ocs.setString(46, "");
        } else {
            ocs.setString(46, npSection);
        }
    }

    private static void setNpPage(String npPage, OracleCallableStatement ocs) throws SQLException {
        if (npPage == null || npPage.equals("")) {
            ocs.setString(47, "");
        } else {
            ocs.setString(47, npPage);
        }
    }

    private static void setNpEdition(String npEdition, OracleCallableStatement ocs) throws SQLException {
        if (npEdition == null || npEdition.equals("")) {
            ocs.setString(48, "");
        } else {
            ocs.setString(48, npEdition);
        }
    }

    private static void setMapToTaxonomy(int mapToTaxonomy, OracleCallableStatement ocs) throws SQLException {
        ocs.setInt(49, mapToTaxonomy);
    }

    private static void setPubDate(String pubDate, OracleCallableStatement ocs) throws SQLException {
        if (pubDate == null || pubDate.equals("")) {
            ocs.setString(50, "");
        } else {
            ocs.setString(50, pubDate);
        }
    }

    private static void setOverride(int override, OracleCallableStatement ocs) throws SQLException {
        ocs.setInt(51, override);
    }

    /**
   * Convert the string passed in to a character large object.
   *
   * @param text The string text to be inserted into the CLOB object.
   *
   * @return oracle.sql.CLOB*/
    private static final oracle.sql.CLOB getClobFromString(String text, Connection con) throws IOException, SQLException {
        oracle.sql.CLOB clob = null;
        clob = createClob(con);
        clob = writeToClob(clob, text);
        return clob;
    }

    /**
   * Obtain a character large object from Oracle by creating an anonymous
   * Oracle function that has only an output parameter and then executing it.
   *
   * This technique requires the existence of a get_clob function in the
   * database and a synonym giving the application user access to it.
   *
   * This technique has an advantage over selecting get_clob from a table,
   * because no table or row locking by the database apparently is required.
   *
   * @return empty Oracle CLOB*/
    private static oracle.sql.CLOB createClob(Connection con) throws SQLException {
        CallableStatement cs = null;
        oracle.sql.CLOB clob = null;
        try {
            String anonymousFunction = "BEGIN DBMS_LOB.createtemporary(?, TRUE, DBMS_LOB.SESSION); END;";
            cs = con.prepareCall(anonymousFunction);
            if (cs != null) {
                cs.registerOutParameter(1, Types.CLOB);
                cs.execute();
                clob = (CLOB) (cs.getClob(1));
            }
        } finally {
            if (cs != null) {
                try {
                    cs.close();
                } catch (SQLException se2) {
                    cs = null;
                }
            }
        }
        return clob;
    }

    /**
   * Put text into character large object.
   *
   * @param clob Character large object previously created
   * @param text String to place in object*/
    private static oracle.sql.CLOB writeToClob(oracle.sql.CLOB clob, String text) throws IOException, SQLException {
        java.io.Writer writer = null;
        try {
            writer = clob.getCharacterOutputStream();
            writer.write(text);
        } finally {
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException ioe) {
                    writer = null;
                }
            }
        }
        return clob;
    }
}
