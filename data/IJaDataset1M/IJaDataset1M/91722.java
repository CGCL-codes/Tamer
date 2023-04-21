package com.rooster.action.hotlist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.rooster.action.common.UserCheck;
import com.rooster.form.hotlist.HotListConsultantListForm;

public class ConsultantDeletedAction extends Action {

    DataSource db;

    Connection myCon = null;

    ResultSet rs;

    Statement state;

    String sModifiedEmails = new String("");

    Logger logger = Logger.getLogger(ConsultantDeletedAction.class);

    public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession(false);
        if ((session == null) || (session.getAttribute("UserId") == null) || (session.getAttribute("UserId").equals(new String("")))) {
            req.setAttribute("APP_ERROR", "Your Session Got Expired. Please Re-login.");
            try {
                res.sendRedirect("/index.jsp");
            } catch (IOException e) {
                logger.debug(e);
            }
            return null;
        }
        String sUserId = String.valueOf(session.getAttribute("UserId"));
        HotListConsultantListForm consulUptFrm1 = (HotListConsultantListForm) frm;
        String linkId[] = consulUptFrm1.getMultiBoxClientId();
        if (linkId == null) {
            linkId = new String[1];
            linkId[0] = String.valueOf(req.getAttribute("multiBoxClientId"));
        }
        String searchType = req.getParameter("searchType");
        try {
            db = getDataSource(req);
            myCon = db.getConnection();
            for (int i = 0; i < linkId.length; i++) {
                CopyAndDeleteCandidate(req, linkId[i]);
                SaveTheCulprit(req, linkId[i], sUserId);
            }
        } catch (Exception e) {
            logger.debug(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (state != null) {
                    state.close();
                }
                if (myCon != null) {
                    myCon.close();
                }
            } catch (SQLException e) {
                logger.debug(e);
            }
        }
        if (searchType != null && !searchType.equals(new String("null"))) {
            session.setAttribute("searchType", searchType);
        }
        req.setAttribute("modified_id", sModifiedEmails);
        return map.findForward("consultantDeleted");
    }

    public void SaveTheCulprit(HttpServletRequest req, String sDeleted, String sDeletedBy) {
        DataSource dsCulprit = null;
        Connection conCulprit = null;
        PreparedStatement psCulprit = null;
        try {
            String sSql = "insert into rooster_deleted_ids (deleted, deleted_by) values (?, ?);";
            dsCulprit = getDataSource(req);
            conCulprit = dsCulprit.getConnection();
            psCulprit = conCulprit.prepareStatement(sSql);
            psCulprit.setString(1, sDeleted);
            psCulprit.setString(2, sDeletedBy);
            psCulprit.executeUpdate();
        } catch (SQLException sqle) {
            logger.debug(sqle);
            System.out.println(sqle);
        } catch (Exception e) {
            logger.debug(e);
            System.out.println(e);
        } finally {
            try {
                if (psCulprit != null) {
                    psCulprit.close();
                }
                if (conCulprit != null) {
                    conCulprit.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public int CopyAndDeleteCandidate(HttpServletRequest req, String sEmail) {
        DataSource ds = null;
        Connection conMove = null;
        PreparedStatement stmtMove = null;
        int iMoved = 0;
        try {
            String sSqlMove = "insert into deleted_rooster_candidate_info select * from rooster_candidate_info where email_id=?";
            ds = getDataSource(req);
            conMove = ds.getConnection();
            stmtMove = conMove.prepareStatement(sSqlMove);
            stmtMove.setString(1, sEmail);
            iMoved = stmtMove.executeUpdate();
            if (iMoved == 1) {
                iMoved = DeleteCandidate(req, sEmail);
                if (iMoved == 1) {
                    sModifiedEmails += "\"" + sEmail + "\", ";
                }
            }
        } catch (SQLException se) {
            logger.debug(se);
            System.out.println(se);
        } finally {
            try {
                if (stmtMove != null) {
                    stmtMove.close();
                }
                if (conMove != null) {
                    conMove.close();
                }
            } catch (SQLException e) {
                logger.debug(e);
                System.out.println(e);
            }
        }
        return iMoved;
    }

    public int DeleteCandidate(HttpServletRequest req, String sEmail) {
        DataSource ds = null;
        Connection conDelete = null;
        PreparedStatement stmtDelete = null;
        int iMoved = 0;
        try {
            String sSqlDelete = "delete from rooster_candidate_info where email_id=?";
            ds = getDataSource(req);
            conDelete = ds.getConnection();
            stmtDelete = conDelete.prepareStatement(sSqlDelete);
            stmtDelete.setString(1, sEmail);
            iMoved = stmtDelete.executeUpdate();
            UserCheck.deleteUser(ds, sEmail);
        } catch (SQLException se) {
            logger.debug(se);
            System.out.println(se);
        } finally {
            try {
                if (stmtDelete != null) {
                    stmtDelete.close();
                }
                if (conDelete != null) {
                    conDelete.close();
                }
            } catch (SQLException e) {
                logger.debug(e);
                System.out.println(e);
            }
        }
        return iMoved;
    }
}
