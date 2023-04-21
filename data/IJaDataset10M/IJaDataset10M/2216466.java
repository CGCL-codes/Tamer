package org.opencms.workplace.tools.database;

import org.opencms.file.CmsObject;
import org.opencms.main.CmsEvent;
import org.opencms.main.I_CmsEventListener;
import org.opencms.main.OpenCms;
import org.opencms.report.A_CmsReportThread;
import org.opencms.report.I_CmsReport;
import java.util.HashMap;
import java.util.Map;

/**
 * Does a full static export of all system resources in the current site.<p>
 * 
 * @author  Michael Emmerich 
 * 
 * @version $Revision: 1.12 $ 
 * 
 * @since 6.0.0 
 */
public class CmsStaticExportThread extends A_CmsReportThread {

    /**
     * Creates a static export Thread.<p>
     * 
     * @param cms the current cms context
     */
    public CmsStaticExportThread(CmsObject cms) {
        super(cms, Messages.get().getBundle().key(Messages.GUI_STATEXP_THREAD_NAME_0));
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
        getReport().println(org.opencms.staticexport.Messages.get().container(org.opencms.staticexport.Messages.RPT_STATICEXPORT_BEGIN_0), I_CmsReport.FORMAT_HEADLINE);
        try {
            OpenCms.getStaticExportManager().exportFullStaticRender(true, getReport());
            Map eventData = new HashMap();
            eventData.put("purge", Boolean.TRUE);
            eventData.put(I_CmsEventListener.KEY_REPORT, getReport());
            OpenCms.fireCmsEvent(new CmsEvent(I_CmsEventListener.EVENT_FULLSTATIC_EXPORT, eventData));
        } catch (Exception e) {
            getReport().println(e);
        }
        getReport().print(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_STAT_0));
        getReport().println(org.opencms.report.Messages.get().container(org.opencms.report.Messages.RPT_STAT_DURATION_1, getReport().formatRuntime()));
        getReport().println(org.opencms.staticexport.Messages.get().container(org.opencms.staticexport.Messages.RPT_STATICEXPORT_END_0), I_CmsReport.FORMAT_HEADLINE);
    }
}
