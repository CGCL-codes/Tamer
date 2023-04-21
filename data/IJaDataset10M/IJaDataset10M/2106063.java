package org.compiere.wf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import org.compiere.model.DocWorkflowMgr;
import org.compiere.model.PO;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Evaluator;

/**
 *	Document Workflow Manager
 *	
 *  @author Jorg Janke
 *  @version $Id: DocWorkflowManager.java,v 1.2 2006/07/30 00:51:05 jjanke Exp $
 */
public class DocWorkflowManager implements DocWorkflowMgr {

    /**
	 * 	Get Document Workflow Manager
	 *	@return mgr
	 */
    public static DocWorkflowManager get() {
        if (s_mgr == null) s_mgr = new DocWorkflowManager();
        return s_mgr;
    }

    static {
        PO.setDocWorkflowMgr(get());
    }

    /**	Document Workflow Manager		*/
    private static DocWorkflowManager s_mgr = null;

    /**	Logger			*/
    private static CLogger log = CLogger.getCLogger(DocWorkflowManager.class);

    /**
	 * 	Doc Workflow Manager
	 */
    private DocWorkflowManager() {
        super();
        if (s_mgr == null) s_mgr = this;
    }

    private int m_noCalled = 0;

    private int m_noStarted = 0;

    /**
	 * 	Process Document Value Workflow
	 *	@param document document
	 *	@param AD_Table_ID table
	 *	@return true if WF started
	 */
    public boolean process(PO document, int AD_Table_ID) {
        m_noCalled++;
        MWorkflow[] wfs = MWorkflow.getDocValue(document.getCtx(), document.getAD_Client_ID(), AD_Table_ID, document.get_TrxName());
        if (wfs == null || wfs.length == 0) return false;
        boolean started = false;
        for (int i = 0; i < wfs.length; i++) {
            MWorkflow wf = wfs[i];
            String logic = wf.getDocValueLogic();
            if (logic == null || logic.length() == 0) {
                log.severe("Workflow has no Logic - " + wf.getName());
                continue;
            }
            if (wf.getAD_Client_ID() != document.getAD_Client_ID()) continue;
            boolean sql = logic.startsWith("SQL=");
            if (sql && !testStart(wf, document)) {
                log.fine("SQL Logic evaluated to false (" + logic + ")");
                continue;
            }
            if (!sql && !Evaluator.evaluateLogic(document, logic)) {
                log.fine("Logic evaluated to false (" + logic + ")");
                continue;
            }
            log.fine(logic);
            int AD_Process_ID = 305;
            ProcessInfo pi = new ProcessInfo(wf.getName(), AD_Process_ID, AD_Table_ID, document.get_ID());
            pi.setAD_User_ID(Env.getAD_User_ID(document.getCtx()));
            pi.setAD_Client_ID(document.getAD_Client_ID());
            if (wf.start(pi, document.get_TrxName()) != null) {
                log.config(wf.getName());
                m_noStarted++;
                started = true;
            }
        }
        return started;
    }

    /**
	 * 	Test Start condition
	 *	@param wf workflow
	 *	@param document document
	 *	@return true if WF should be started
	 */
    private boolean testStart(MWorkflow wf, PO document) {
        boolean retValue = false;
        String logic = wf.getDocValueLogic();
        logic = logic.substring(4);
        String tableName = document.get_TableName();
        String[] keyColumns = document.get_KeyColumns();
        if (keyColumns.length != 1) {
            log.severe("Tables with more then one key column not supported - " + tableName + " = " + keyColumns.length);
            return false;
        }
        String keyColumn = keyColumns[0];
        StringBuffer sql = new StringBuffer("SELECT ").append(keyColumn).append(" FROM ").append(tableName).append(" WHERE AD_Client_ID=? AND ").append(keyColumn).append("=? AND ").append(logic).append(" AND NOT EXISTS (SELECT * FROM AD_WF_Process wfp ").append("WHERE wfp.AD_Table_ID=? AND wfp.Record_ID=").append(tableName).append(".").append(keyColumn).append(" AND wfp.AD_Workflow_ID=?").append(" AND SUBSTR(wfp.WFState,1,1)='O')");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = DB.prepareStatement(sql.toString(), document.get_TrxName());
            pstmt.setInt(1, wf.getAD_Client_ID());
            pstmt.setInt(2, document.get_ID());
            pstmt.setInt(3, document.get_Table_ID());
            pstmt.setInt(4, wf.getAD_Workflow_ID());
            rs = pstmt.executeQuery();
            if (rs.next()) retValue = true;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Logic=" + logic + " - SQL=" + sql.toString(), e);
        } finally {
            DB.close(rs, pstmt);
            rs = null;
            pstmt = null;
        }
        return retValue;
    }

    /**
	* 	String Representation
	*	@return info
	*/
    public String toString() {
        StringBuffer sb = new StringBuffer("DocWorkflowManager[");
        sb.append("Called=").append(m_noCalled).append(",Stated=").append(m_noStarted).append("]");
        return sb.toString();
    }
}
