package org.plazmaforge.studio.reportdesigner.wizards;

import org.eclipse.jface.viewers.ISelection;
import org.plazmaforge.studio.core.wizards.NewFileWizardPage;
import org.plazmaforge.studio.reportdesigner.ReportDesignerPlugin;
import org.plazmaforge.studio.reportdesigner.storage.jasperreports.JRReportManager;

/** 
 * @author Oleh Hapon
 * $Id: NewReportWizardPage.java,v 1.6 2010/04/28 06:43:11 ohapon Exp $
 */
public class NewReportWizardPage extends NewFileWizardPage {

    public NewReportWizardPage(ISelection selection) {
        super(selection);
    }

    protected String getWizardTitle() {
        return "Report";
    }

    protected String getWizardDescription() {
        return "This wizard creates a new file with *." + getWizardFileExt() + " extension that can be opened with the Report Editor.";
    }

    protected String getWizardNewFileName() {
        return "Report" + ReportDesignerPlugin.getDefault().getNextReportEditorNumber() + "." + getWizardFileExt();
    }

    protected String getWizardFileExt() {
        return JRReportManager.JASPERREPORTS_SOURCE_FILE_EXT;
    }
}
