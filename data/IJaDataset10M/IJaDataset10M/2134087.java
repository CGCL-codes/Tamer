package net.sf.elbe.ui.wizards;

import net.sf.elbe.ui.ELBEUIConstants;
import net.sf.elbe.ui.ELBEUIPlugin;
import net.sf.elbe.ui.widgets.search.SearchPageWrapper;

public class ExportExcelFromWizardPage extends ExportBaseFromWizardPage {

    public ExportExcelFromWizardPage(String pageName, ExportBaseWizard wizard) {
        super(pageName, wizard, new SearchPageWrapper(SearchPageWrapper.NAME_INVISIBLE | SearchPageWrapper.DN_VISIBLE | SearchPageWrapper.DN_CHECKED | SearchPageWrapper.ALLATTRIBUTES_VISIBLE | SearchPageWrapper.OPERATIONALATTRIBUTES_VISIBLE | ((wizard.getSearch().getReturningAttributes() == null || wizard.getSearch().getReturningAttributes().length == 0) ? SearchPageWrapper.ALLATTRIBUTES_CHECKED : SearchPageWrapper.NONE)));
        super.setImageDescriptor(ELBEUIPlugin.getDefault().getImageDescriptor(ELBEUIConstants.IMG_EXPORT_XLS_WIZARD));
    }

    public boolean isExportDn() {
        return spw.isExportDn();
    }
}
