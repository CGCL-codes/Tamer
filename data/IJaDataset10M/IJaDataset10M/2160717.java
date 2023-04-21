package yaw.core.swt.wizards.migrator;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import yaw.core.controller.MigrationMgr;
import yaw.core.swt.views.MessagePage;
import yaw.core.swt.views.SummaryPage;
import yaw.core.swt.views.SummaryPage.ISummaryGetter;
import yaw.core.swt.wizards.migrator.pages.ExportPage;
import yaw.core.swt.wizards.migrator.pages.ImportPage;
import yaw.core.wizard.IProjectLocator;

public class MigrationWizard extends Wizard {

    private MigrationMgr mgr;

    ImportPage importPage;

    ExportPage exportPage;

    MessagePage messagePage;

    SummaryPage summaryPage;

    private ISummaryGetter summaryGetter = new ISummaryGetter() {

        public String getSummary() {
            return "<h3>Export successful!</h3>";
        }
    };

    public MigrationWizard(MigrationMgr mgr) {
        this.mgr = mgr;
        mgr.load();
        importPage = new ImportPage(this.mgr);
        messagePage = new MessagePage(this.mgr, ExportPage.PAGENAME);
        exportPage = new ExportPage(this.mgr);
        summaryPage = new SummaryPage(summaryGetter);
        addPage(importPage);
        addPage(messagePage);
        addPage(exportPage);
        addPage(summaryPage);
        setNeedsProgressMonitor(true);
        setWindowTitle("YAW/Migration - Tool to Tool Transfer");
    }

    @Override
    public boolean canFinish() {
        return mgr.isFinished();
    }

    @Override
    public boolean performFinish() {
        return true;
    }

    @Override
    public boolean performCancel() {
        return true;
    }

    public static void openWizard(Shell parent, IProjectLocator locator) {
        MigrationMgr mgr = new MigrationMgr(locator);
        WizardDialog dlg = new WizardDialog(parent, new MigrationWizard(mgr));
        dlg.open();
    }
}
