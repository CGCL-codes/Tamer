package net.sourceforge.eclipsetrader.trading.wizards;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class CommonDialogPage extends PreferencePage {

    private CommonPreferencePage page;

    public CommonDialogPage(CommonPreferencePage page) {
        super(page.getTitle());
        this.page = page;
        noDefaultAndApplyButton();
    }

    protected Control createContents(Composite parent) {
        page.createControl(parent);
        return page.getControl();
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        page.setVisible(visible);
    }

    public boolean performOk() {
        if (page.isPageComplete() && page.getControl() != null) page.performFinish();
        return page.isPageComplete();
    }
}
