package org.plazmaforge.studio.reportdesigner.storage;

import org.plazmaforge.studio.reportdesigner.storage.jasperreports.JRReportManager;

/** 
 * @author Oleh Hapon
 * $Id: ReportManagerFactory.java,v 1.2 2010/04/28 06:43:12 ohapon Exp $
 */
public class ReportManagerFactory {

    public static ReportManager getReportManager() {
        return new JRReportManager();
    }
}
