package org.riverock.webmill.portal.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.riverock.common.tools.RsetTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.generic.schema.db.types.PrimaryKeyTypeTypeType;
import org.riverock.interfaces.portal.bean.Css;
import org.riverock.sql.cache.SqlStatement;
import org.riverock.sql.cache.SqlStatementRegisterException;
import org.riverock.webmill.core.GetWmPortalCssDataWithIdSiteContentCssList;
import org.riverock.webmill.core.GetWmPortalCssWithIdSiteList;
import org.riverock.webmill.core.InsertWmPortalCssItem;
import org.riverock.webmill.core.UpdateWmPortalCssItem;
import org.riverock.webmill.core.GetWmPortalCssItem;
import org.riverock.webmill.exception.PortalPersistenceException;
import org.riverock.webmill.main.ContentCSS;
import org.riverock.webmill.main.CssBean;
import org.riverock.webmill.schema.core.WmPortalCssDataItemType;
import org.riverock.webmill.schema.core.WmPortalCssDataListType;
import org.riverock.webmill.schema.core.WmPortalCssItemType;
import org.riverock.webmill.schema.core.WmPortalCssListType;

/**
 * @author Sergei Maslyukov
 *         Date: 18.05.2006
 *         Time: 13:34:41
 */
@SuppressWarnings({ "UnusedAssignment" })
public class InternalCssDaoImpl implements InternalCssDao {

    private static final Logger log = Logger.getLogger(InternalCssDaoImpl.class);

    static String cssCurrentSql_ = "select a.date_post, b.css_data " + "from   WM_PORTAL_CSS a, WM_PORTAL_CSS_DATA b " + "where  a.ID_SITE=? and a.is_current=1 and " + "       a.id_site_content_css=b.id_site_content_css " + "order by ID_SITE_CONTENT_CSS_DATA asc";

    static {
        try {
            SqlStatement.registerSql(cssCurrentSql_, ContentCSS.class);
        } catch (Throwable exception) {
            final String es = "Exception in SqlStatement.registerSql()";
            log.error(es, exception);
            throw new SqlStatementRegisterException(es, exception);
        }
    }

    public CssBean getCssCurrent(Long siteId) {
        if (siteId == null) {
            return null;
        }
        PreparedStatement ps = null;
        ResultSet rset = null;
        boolean isFirstRecord = true;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement(cssCurrentSql_);
            RsetTools.setLong(ps, 1, siteId);
            rset = ps.executeQuery();
            CssBean cssBean = new CssBean();
            StringBuilder sb = new StringBuilder();
            while (rset.next()) {
                if (isFirstRecord) {
                    cssBean.setDate(RsetTools.getTimestamp(rset, "DATE_POST"));
                    isFirstRecord = false;
                }
                sb.append(RsetTools.getString(rset, "CSS_DATA", ""));
            }
            cssBean.setCss(sb.toString());
            return cssBean;
        } catch (Exception e) {
            final String es = "Error get css ";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(adapter, rset, ps);
            adapter = null;
            rset = null;
            ps = null;
        }
    }

    public Long createCss(Css css) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            clearCurrentFlag(css, adapter);
            CustomSequenceType seq = new CustomSequenceType();
            seq.setSequenceName("seq_WM_PORTAL_CSS");
            seq.setTableName("WM_PORTAL_CSS");
            seq.setColumnName("ID_SITE_CONTENT_CSS");
            Long id = adapter.getSequenceNextValue(seq);
            WmPortalCssItemType item = new WmPortalCssItemType();
            item.setIdSiteContentCss(id);
            item.setIdSite(css.getSiteId());
            item.setIsCurrent(css.isCurrent());
            item.setTextComment(css.getCssComment());
            item.setDatePost(new Date(System.currentTimeMillis()));
            InsertWmPortalCssItem.process(adapter, item);
            DatabaseManager.insertBigText(adapter, id, "ID_SITE_CONTENT_CSS", PrimaryKeyTypeTypeType.NUMBER, "WM_PORTAL_CSS_DATA", "ID_SITE_CONTENT_CSS_DATA", "CSS_DATA", css.getCss(), false);
            adapter.commit();
            return id;
        } catch (Throwable e) {
            try {
                if (adapter != null) adapter.rollback();
            } catch (Throwable th) {
            }
            String es = "Error create css";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public void updateCss(Css css) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            clearCurrentFlag(css, adapter);
            WmPortalCssItemType item = new WmPortalCssItemType();
            item.setDatePost(css.getDate());
            item.setIdSite(css.getSiteId());
            item.setIdSiteContentCss(css.getCssId());
            item.setIsCurrent(css.isCurrent());
            item.setTextComment(css.getCssComment());
            UpdateWmPortalCssItem.process(adapter, item);
            DatabaseManager.insertBigText(adapter, css.getCssId(), "ID_SITE_CONTENT_CSS", PrimaryKeyTypeTypeType.NUMBER, "WM_PORTAL_CSS_DATA", "ID_SITE_CONTENT_CSS_DATA", "CSS_DATA", css.getCss(), true);
            adapter.commit();
        } catch (Throwable e) {
            try {
                if (adapter != null) adapter.rollback();
            } catch (Throwable th) {
            }
            String es = "Error update css";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public List<Css> getCssList(Long siteId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            List<Css> list = new ArrayList<Css>();
            WmPortalCssListType cssList = GetWmPortalCssWithIdSiteList.getInstance(adapter, siteId).item;
            for (Object o : cssList.getWmPortalCssAsReference()) {
                WmPortalCssItemType css = (WmPortalCssItemType) o;
                CssBean cssBean = new CssBean();
                cssBean.setCss(getCssData(adapter, css.getIdSiteContentCss()).toString());
                cssBean.setCssComment(css.getTextComment());
                cssBean.setCssId(css.getIdSiteContentCss());
                cssBean.setCurrent(css.getIsCurrent());
                cssBean.setDate(css.getDatePost());
                cssBean.setSiteId(css.getIdSite());
                list.add(cssBean);
            }
            return list;
        } catch (Throwable e) {
            String es = "Error get css";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    static String cssSql_ = "select a.ID_SITE_CONTENT_CSS, a.IS_CURRENT, a.DATE_POST, a.TEXT_COMMENT, a.ID_SITE, b.css_data " + "from   WM_PORTAL_CSS a, WM_PORTAL_CSS_DATA b " + "where  a.ID_SITE_CONTENT_CSS=? and a.ID_SITE_CONTENT_CSS=b.ID_SITE_CONTENT_CSS " + "order by ID_SITE_CONTENT_CSS_DATA asc";

    static {
        try {
            SqlStatement.registerSql(cssCurrentSql_, ContentCSS.class);
        } catch (Throwable exception) {
            final String es = "Exception in SqlStatement.registerSql()";
            log.error(es, exception);
            throw new SqlStatementRegisterException(es, exception);
        }
    }

    public Css getCss(Long cssId) {
        if (cssId == null) return new CssBean();
        PreparedStatement ps = null;
        ResultSet rset = null;
        boolean isFirstRecord = true;
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            ps = adapter.prepareStatement(cssSql_);
            RsetTools.setLong(ps, 1, cssId);
            rset = ps.executeQuery();
            CssBean cssBean = new CssBean();
            StringBuilder sb = new StringBuilder();
            while (rset.next()) {
                if (isFirstRecord) {
                    cssBean.setCssId(RsetTools.getLong(rset, "ID_SITE_CONTENT_CSS"));
                    cssBean.setCurrent(RsetTools.getInt(rset, "IS_CURRENT", 0) == 1);
                    cssBean.setDate(RsetTools.getTimestamp(rset, "DATE_POST"));
                    cssBean.setCssComment(RsetTools.getString(rset, "TEXT_COMMENT"));
                    cssBean.setSiteId(RsetTools.getLong(rset, "ID_SITE"));
                    isFirstRecord = false;
                }
                sb.append(RsetTools.getString(rset, "CSS_DATA", ""));
            }
            cssBean.setCss(sb.toString());
            return cssBean;
        } catch (Exception e) {
            final String es = "Error get css ";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(adapter, rset, ps);
            adapter = null;
            rset = null;
            ps = null;
        }
    }

    public void deleteCss(Long cssId) {
        DatabaseAdapter adapter = null;
        try {
            adapter = DatabaseAdapter.getInstance();
            WmPortalCssItemType currentCss = GetWmPortalCssItem.getInstance(adapter, cssId).item;
            if (currentCss != null && currentCss.getIsCurrent()) {
                return;
            }
            DatabaseManager.runSQL(adapter, "delete from WM_PORTAL_CSS_DATA " + "where ID_SITE_CONTENT_CSS=?", new Object[] { cssId }, new int[] { Types.DECIMAL });
            DatabaseManager.runSQL(adapter, "delete from WM_PORTAL_CSS where ID_SITE_CONTENT_CSS=?", new Object[] { cssId }, new int[] { Types.DECIMAL });
            adapter.commit();
        } catch (Throwable e) {
            try {
                if (adapter != null) adapter.rollback();
            } catch (Throwable th) {
            }
            String es = "Error delete css";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        } finally {
            DatabaseManager.close(adapter);
            adapter = null;
        }
    }

    public void deleteCssForSite(DatabaseAdapter adapter, Long siteId) {
        try {
            DatabaseManager.runSQL(adapter, "delete from WM_PORTAL_CSS_DATA " + "where ID_SITE_CONTENT_CSS in " + "(select ID_SITE_CONTENT_CSS from WM_PORTAL_CSS where ID_SITE=?)", new Object[] { siteId }, new int[] { Types.DECIMAL });
            DatabaseManager.runSQL(adapter, "delete from WM_PORTAL_CSS where ID_SITE=?", new Object[] { siteId }, new int[] { Types.DECIMAL });
        } catch (SQLException e) {
            String es = "Error delete css";
            log.error(es, e);
            throw new IllegalStateException(es, e);
        }
    }

    private StringBuilder getCssData(DatabaseAdapter adapter, Long cssId) throws PortalPersistenceException {
        StringBuilder sb = new StringBuilder();
        WmPortalCssDataListType cssList = GetWmPortalCssDataWithIdSiteContentCssList.getInstance(adapter, cssId).item;
        for (Object o : cssList.getWmPortalCssDataAsReference()) {
            WmPortalCssDataItemType item = (WmPortalCssDataItemType) o;
            sb.append(item.getCssData());
        }
        return sb;
    }

    private void clearCurrentFlag(Css css, DatabaseAdapter adapter) throws SQLException {
        if (css.isCurrent()) {
            DatabaseManager.runSQL(adapter, "update WM_PORTAL_CSS set IS_CURRENT=0 where ID_SITE=? and IS_CURRENT!=0", new Object[] { css.getSiteId() }, new int[] { Types.NUMERIC });
        }
    }
}
