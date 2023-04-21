package at.rc.tacos.client.view.admin;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;
import at.rc.tacos.client.Activator;
import at.rc.tacos.client.controller.EditorNewMobilePhoneAction;
import at.rc.tacos.client.controller.RefreshViewAction;
import at.rc.tacos.client.editors.MobilePhoneEditor;
import at.rc.tacos.client.editors.MobilePhoneEditorInput;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.providers.MobilePhoneContentProvider;
import at.rc.tacos.client.providers.MobilePhoneLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.model.MobilePhoneDetail;

public class PhoneAdminView extends ViewPart implements PropertyChangeListener {

    public static final String ID = "at.rc.tacos.client.view.admin.phoneAdminView";

    private TableViewer viewer;

    private FormToolkit toolkit;

    private ScrolledForm form;

    /**
	 * Default class constructor
	 */
    public PhoneAdminView() {
        ModelFactory.getInstance().getPhoneManager().addPropertyChangeListener(this);
    }

    /**
	 * Cleanup the view
	 */
    @Override
    public void dispose() {
        ModelFactory.getInstance().getPhoneManager().removePropertyChangeListener(this);
    }

    /**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
    @Override
    public void createPartControl(final Composite parent) {
        String authorization = SessionManager.getInstance().getLoginInformation().getAuthorization();
        toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
        form = toolkit.createScrolledForm(parent);
        form.setText("Liste der Mobiltelefone");
        if (!authorization.equalsIgnoreCase("Administrator")) form.setEnabled(false);
        toolkit.decorateFormHeading(form.getForm());
        GridLayout layout = new GridLayout();
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        form.getBody().setLayout(layout);
        form.getBody().setLayoutData(new GridData(GridData.FILL_BOTH));
        viewer = new TableViewer(form.getBody(), SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        viewer.getTable().setLayout(new GridLayout());
        viewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
        viewer.addDoubleClickListener(new IDoubleClickListener() {

            @Override
            public void doubleClick(DoubleClickEvent dce) {
                ISelection selection = viewer.getSelection();
                Object obj = ((IStructuredSelection) selection).getFirstElement();
                MobilePhoneDetail phone = (MobilePhoneDetail) obj;
                MobilePhoneEditorInput input = new MobilePhoneEditorInput(phone, false);
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                try {
                    page.openEditor(input, MobilePhoneEditor.ID);
                } catch (PartInitException e) {
                    Activator.getDefault().log("Failed to open the editor for the mobile phone " + phone, IStatus.ERROR);
                }
            }
        });
        viewer.setContentProvider(new MobilePhoneContentProvider());
        viewer.setLabelProvider(new MobilePhoneLabelProvider());
        viewer.setInput(ModelFactory.getInstance().getPhoneManager().toArray());
        getViewSite().setSelectionProvider(viewer);
        createToolBarActions();
        getViewSite().setSelectionProvider(viewer);
    }

    /**
	 * Passes the focus to the view
	 */
    @Override
    public void setFocus() {
        form.setFocus();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String event = evt.getPropertyName();
        if ("PHONE_ADD".equalsIgnoreCase(event) || "PHONE_REMOVE".equalsIgnoreCase(event) || "PHONE_UPDATE".equalsIgnoreCase(event) || "PHONE_CLEARED".equalsIgnoreCase(event)) {
            viewer.refresh();
        }
    }

    /**
	 * Creates and adds the actions for the toolbar
	 */
    private void createToolBarActions() {
        EditorNewMobilePhoneAction addAction = new EditorNewMobilePhoneAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
        RefreshViewAction refreshView = new RefreshViewAction(MobilePhoneDetail.ID);
        form.getToolBarManager().add(addAction);
        form.getToolBarManager().add(refreshView);
        form.getToolBarManager().update(true);
    }
}
