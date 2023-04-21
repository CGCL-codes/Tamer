package org.nightlabs.jfire.update.admin.ui.editor;

import javax.jdo.FetchPlan;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.nightlabs.base.ui.entity.editor.EntityEditorPageControllerModifyEvent;
import org.nightlabs.jfire.update.AbstractClientElement;
import org.nightlabs.jfire.update.AbstractClientElementI18NText;

/**
 * @author Marius Heinzmann -- Marius[at]NightLabs[dot]de
 *
 */
public class ClientElementLicenseCopyrightPage extends AbstractClientElementPage<AbstractClientElement> implements IInputSetable<AbstractClientElement> {

    /**
	 * The fetch groups needed for this page.
	 */
    public static final String[] FETCH_GROUPS_CLIENTELEMENT = new String[] { FetchPlan.DEFAULT, AbstractClientElement.FETCH_GROUP_LICENSE_COPYRIGHT, AbstractClientElementI18NText.FETCH_GROUP_TEXTVALUES };

    public static final String PAGE_ID = ClientElementLicenseCopyrightPage.class.toString();

    public ClientElementLicenseCopyrightPage(FormEditor editor) {
        super(editor, PAGE_ID, "License and Copright");
    }

    private ClientElementLicenseSection licenseSection;

    private ClientElementCopyrightSection copyrightSection;

    @Override
    protected void addSections(Composite parent) {
        licenseSection = new ClientElementLicenseSection(this, parent, ExpandableComposite.TWISTIE | ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED, "License");
        copyrightSection = new ClientElementCopyrightSection(this, parent, ExpandableComposite.TWISTIE | ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED, "Copyright");
        getManagedForm().addPart(licenseSection);
        getManagedForm().addPart(copyrightSection);
    }

    @Override
    protected void handleControllerObjectModified(final EntityEditorPageControllerModifyEvent modifyEvent) {
        super.handleControllerObjectModified(modifyEvent);
        switchToContent();
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                if (licenseSection.getSection() == null || licenseSection.getSection().isDisposed()) return;
                setInput((AbstractClientElement) modifyEvent.getNewObject(), modifyEvent.isResetDirtyState());
            }
        });
    }

    @Override
    protected String getPageFormTitle() {
        return "License and Copyright";
    }

    @Override
    public void setInput(AbstractClientElement model, boolean resetDirtyState) {
        assert model != null;
        licenseSection.setClientElement(model, resetDirtyState);
        copyrightSection.setClientElement(model, resetDirtyState);
    }
}
