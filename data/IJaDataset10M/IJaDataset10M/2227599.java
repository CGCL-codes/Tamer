package org.dengues.reports.wizards;

import org.dengues.commons.IDenguesSharedImage;
import org.dengues.commons.utils.ImageUtil;
import org.dengues.core.resource.WarehouseResourceFactory;
import org.dengues.core.warehouse.ENodeCategoryName;
import org.dengues.model.DenguesModelManager;
import org.dengues.model.reports.Reports;
import org.dengues.reports.i18n.Messages;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.wizard.Wizard;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf 2008-1-7 qiang.zhang $
 * 
 */
public class NewReportWizard extends Wizard {

    private NewReportWizardPage page;

    private IPath path;

    private Reports reports;

    private boolean isActived;

    /**
     * Qiang.Zhang.Adolf@gmail.com NewReportWizard constructor comment.
     * 
     * @param path
     */
    public NewReportWizard(IPath path) {
        this();
        this.path = path;
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com NewReportWizard constructor comment.
     */
    private NewReportWizard() {
        super();
        setDefaultPageImageDescriptor(ImageUtil.getDescriptor(IDenguesSharedImage.REPORTS_WIZARD));
        setWindowTitle(Messages.getString("NewReportWizard.createTitle"));
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com NewReportWizard constructor comment.
     * 
     * @param process
     * @param path
     * @param isActived
     */
    public NewReportWizard(Reports process, IPath path, boolean isActived) {
        this();
        this.reports = process;
        this.path = path;
        this.isActived = isActived;
        setWindowTitle(Messages.getString("NewReportWizard.editTitle"));
    }

    @Override
    public void addPages() {
        if (reports != null) {
            page = new NewReportWizardPage(reports, isActived);
        } else {
            page = new NewReportWizardPage(path);
        }
        addPage(page);
    }

    @Override
    public boolean performFinish() {
        boolean create = false;
        boolean rename = false;
        String processName = page.getModelName();
        if (reports == null) {
            reports = DenguesModelManager.getReportsFactory().createReports();
            reports.setUuid(WarehouseResourceFactory.getEMFId());
            reports.setCategoryName(ENodeCategoryName.SCRIPTS.getName());
            create = true;
        } else if (path != null) {
            if (!reports.getName().equals(processName)) {
                rename = true;
            }
        }
        reports.setComment(page.getModelComment());
        reports.setName(processName);
        reports.setStatus(page.getModelStatus());
        if (create) {
            WarehouseResourceFactory.createStorage(reports, path);
        } else {
            WarehouseResourceFactory.saveStorage(reports);
        }
        return true;
    }

    /**
     * Getter for reports.
     * 
     * @return the reports
     */
    public Reports getReports() {
        return this.reports;
    }
}
