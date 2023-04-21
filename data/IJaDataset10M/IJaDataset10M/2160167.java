package org.opencms.workplace.tools.history;

import org.opencms.file.CmsObject;
import org.opencms.main.CmsException;
import org.opencms.report.A_CmsReportThread;
import org.opencms.report.I_CmsReport;

/**
 * Clears the file history of the OpenCms database.<p>
 * 
 * @author  Andreas Zahner 
 * 
 * @version $Revision: 1.4 $ 
 * 
 * @since 6.0.0 
 */
public class CmsHistoryClearThread extends A_CmsReportThread {

    private CmsHistoryClear m_historyClear;

    /**
     * Creates the history clear Thread.<p>
     * 
     * @param cms the current OpenCms context object
     * @param historyClear the settings to clear the history
     */
    public CmsHistoryClearThread(CmsObject cms, CmsHistoryClear historyClear) {
        super(cms, Messages.get().getBundle().key(Messages.GUI_HISTORY_CLEAR_THREAD_NAME_1, cms.getRequestContext().currentProject().getName()));
        m_historyClear = historyClear;
        initHtmlReport(cms.getRequestContext().getLocale());
    }

    /**
     * @see org.opencms.report.A_CmsReportThread#getReportUpdate()
     */
    public String getReportUpdate() {
        return getReport().getReportUpdate();
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public void run() {
        getReport().println(Messages.get().container(Messages.RPT_DELETE_HISTORY_BEGIN_0), I_CmsReport.FORMAT_HEADLINE);
        int versions = m_historyClear.getKeepVersions();
        int versionsDeleted = m_historyClear.getKeepVersions();
        long timeDeleted = m_historyClear.getClearOlderThan();
        String folderName = "/";
        if (timeDeleted == 0) {
            timeDeleted = -1;
        }
        if (!m_historyClear.isClearDeleted()) {
            versionsDeleted = -1;
        }
        try {
            getCms().deleteHistoricalVersions(folderName, versions, versionsDeleted, timeDeleted, getReport());
        } catch (CmsException e) {
            getReport().println(e);
        }
        getReport().println(Messages.get().container(Messages.RPT_DELETE_HISTORY_END_0), I_CmsReport.FORMAT_HEADLINE);
    }
}
