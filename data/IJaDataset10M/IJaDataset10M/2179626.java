package at.rc.tacos.client.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;
import at.rc.tacos.client.controller.EditorCloseAction;
import at.rc.tacos.client.controller.EditorSaveAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.ServiceType;

public class ServiceTypeEditor extends EditorPart implements PropertyChangeListener {

    public static final String ID = "at.rc.tacos.client.editors.serviceTypeEditor";

    boolean isDirty;

    private FormToolkit toolkit;

    private ScrolledForm form;

    private CLabel infoLabel;

    private ImageHyperlink saveHyperlink, removeHyperlink;

    private Text id, name;

    private ServiceType serviceType;

    private boolean isNew;

    /**
	 * Default class constructor
	 */
    public ServiceTypeEditor() {
        ModelFactory.getInstance().getServiceManager().addPropertyChangeListener(this);
    }

    /**
	 * Cleanup
	 */
    @Override
    public void dispose() {
        ModelFactory.getInstance().getServiceManager().removePropertyChangeListener(this);
    }

    /**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
    @Override
    public void createPartControl(final Composite parent) {
        serviceType = ((ServiceTypeEditorInput) getEditorInput()).getServiceType();
        isNew = ((ServiceTypeEditorInput) getEditorInput()).isNew();
        isDirty = false;
        toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
        form = toolkit.createScrolledForm(parent);
        toolkit.decorateFormHeading(form.getForm());
        form.getBody().setLayout(new GridLayout());
        form.getBody().setLayoutData(new GridData(GridData.FILL_BOTH));
        createManageSection(form.getBody());
        createDetailSection(form.getBody());
        loadData();
        if (serviceType.getId() <= 5 && serviceType.getId() > 0) {
            form.setText("Vom System vorgegebene Dienstverh�ltnisse k�nnen nicht bearbeitet werden.");
            form.setEnabled(false);
        }
        form.pack(true);
    }

    /**
	 * Creates the section to manage the changes
	 */
    private void createManageSection(Composite parent) {
        Composite client = createSection(parent, "Dienstverh�ltnis verwalten");
        infoLabel = new CLabel(client, SWT.NONE);
        infoLabel.setText("Hier k�nnen sie das aktuelle Dienstverh�ltnis verwalten und die �nderungen speichern.");
        infoLabel.setImage(ImageFactory.getInstance().getRegisteredImage("admin.info"));
        saveHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
        saveHyperlink.setText("�nderungen speichern");
        saveHyperlink.setEnabled(false);
        saveHyperlink.setForeground(CustomColors.GREY_COLOR);
        saveHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.saveDisabled"));
        saveHyperlink.addHyperlinkListener(new HyperlinkAdapter() {

            @Override
            public void linkActivated(HyperlinkEvent e) {
                EditorSaveAction saveAction = new EditorSaveAction();
                saveAction.run();
            }
        });
        removeHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
        removeHyperlink.setText("Dienstverh�ltnis l�schen");
        removeHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.serviceTypeRemove"));
        removeHyperlink.addHyperlinkListener(new HyperlinkAdapter() {

            @Override
            public void linkActivated(HyperlinkEvent e) {
                boolean result = MessageDialog.openConfirm(getSite().getShell(), "L�schen des Dienstverh�ltnisses best�tigen", "M�chten sie das Dienstverh�ltnis " + serviceType.getServiceName() + " wirklich l�schen?");
                if (!result) return;
                isDirty = false;
                NetWrapper.getDefault().sendRemoveMessage(ServiceType.ID, serviceType);
            }
        });
        GridData data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 2;
        data.widthHint = 600;
        infoLabel.setLayoutData(data);
    }

    /**
	 * Creates the section containing the job details
	 * 
	 * @param parent
	 *            the parent composite
	 */
    private void createDetailSection(Composite parent) {
        Composite client = createSection(parent, "Dienstverh�ltnis Details");
        final Label labelId = toolkit.createLabel(client, "Dienstverh�ltnis ID");
        id = toolkit.createText(client, "");
        id.setEditable(false);
        id.setBackground(CustomColors.GREY_COLOR);
        id.setToolTipText("Die ID wird automatisch generiert");
        final Label labelCompName = toolkit.createLabel(client, "Dienstverh�ltnis");
        name = toolkit.createText(client, "");
        name.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent me) {
                inputChanged();
            }
        });
        GridData data = new GridData();
        data.widthHint = 150;
        labelId.setLayoutData(data);
        data = new GridData();
        data.widthHint = 150;
        labelCompName.setLayoutData(data);
        GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
        id.setLayoutData(data2);
        data2 = new GridData(GridData.FILL_HORIZONTAL);
        name.setLayoutData(data2);
    }

    /**
	 * Loads the data and shows them in the view
	 */
    private void loadData() {
        if (isNew) {
            form.setText("Details des Dienstverh�ltnisses " + serviceType.getServiceName());
            removeHyperlink.setVisible(false);
            return;
        }
        removeHyperlink.setVisible(true);
        form.setText("Neues Dienstverh�ltnis anlegen");
        id.setText(String.valueOf(serviceType.getId()));
        name.setText(serviceType.getServiceName());
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        form.setMessage(null, IMessageProvider.NONE);
        if (name.getText().length() > 30 || name.getText().trim().isEmpty()) {
            form.getDisplay().beep();
            form.setMessage("Bitte geben sie eine g�ltige Bezeichnung f�r das Dienstverh�ltnis an(max. 30 Zeichen)", IMessageProvider.ERROR);
            return;
        }
        serviceType.setServiceName(name.getText());
        if (isNew) NetWrapper.getDefault().sendAddMessage(ServiceType.ID, serviceType); else NetWrapper.getDefault().sendUpdateMessage(ServiceType.ID, serviceType);
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
        setPartName(input.getName());
    }

    @Override
    public void setFocus() {
        form.setFocus();
    }

    @Override
    public boolean isDirty() {
        return isDirty;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("SERVICETYPE_UPDATE".equals(evt.getPropertyName()) || "SERVICETYPE_ADD".equalsIgnoreCase(evt.getPropertyName())) {
            ServiceType updateService = null;
            if (evt.getNewValue() instanceof ServiceType) updateService = (ServiceType) evt.getNewValue();
            if (updateService == null) return;
            if (serviceType.equals(updateService) || serviceType.getServiceName().equals(updateService.getServiceName())) {
                setInput(new ServiceTypeEditorInput(updateService, false));
                setPartName(updateService.getServiceName());
                serviceType = updateService;
                isNew = false;
                loadData();
                isDirty = false;
                infoLabel.setText("�nderungen gespeichert");
                infoLabel.setImage(ImageFactory.getInstance().getRegisteredImage("info.ok"));
                Display.getCurrent().beep();
            }
        }
        if ("SERVICETYPE_REMOVE".equalsIgnoreCase(evt.getPropertyName())) {
            ServiceType removedService = (ServiceType) evt.getOldValue();
            if (serviceType.equals(removedService)) {
                MessageDialog.openInformation(getSite().getShell(), "Dienstverh�ltnis wurde gel�scht", "Das Dienstverh�ltnis, welches Sie gerade editieren, wurde gel�scht");
                EditorCloseAction closeAction = new EditorCloseAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
                closeAction.run();
            }
        }
    }

    /**
	 * Creates and returns a section and a composite with two colums
	 * 
	 * @param parent
	 *            the parent composite
	 * @param sectionName
	 *            the title of the section
	 * @return the created composite to hold the other widgets
	 */
    private Composite createSection(Composite parent, String sectionName) {
        Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE);
        toolkit.createCompositeSeparator(section);
        section.setText(sectionName);
        section.setLayout(new GridLayout());
        section.setLayoutData(new GridData(GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));
        section.setExpanded(true);
        Composite client = new Composite(section, SWT.NONE);
        section.setClient(client);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;
        client.setLayout(layout);
        GridData clientDataLayout = new GridData(GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH);
        client.setLayoutData(clientDataLayout);
        return client;
    }

    /**
	 * This is called when the input of a text box or a combo box was changes
	 */
    private void inputChanged() {
        isDirty = false;
        ServiceTypeEditorInput serviceInput = (ServiceTypeEditorInput) getEditorInput();
        ServiceType persistantService = serviceInput.getServiceType();
        if (!name.getText().equalsIgnoreCase(persistantService.getServiceName())) {
            isDirty = true;
            infoLabel.setText("Bitte speichern Sie ihre lokalen �nderungen.");
            infoLabel.setImage(ImageFactory.getInstance().getRegisteredImage("info.warning"));
            saveHyperlink.setEnabled(true);
            saveHyperlink.setForeground(CustomColors.COLOR_LINK);
            saveHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.save"));
        } else {
            infoLabel.setText("Hier k�nnen sie das aktuelle Dienstverh�ltnis verwalten und die �nderungen speichern.");
            infoLabel.setImage(ImageFactory.getInstance().getRegisteredImage("admin.info"));
            saveHyperlink.setEnabled(false);
            saveHyperlink.setForeground(CustomColors.GREY_COLOR);
            saveHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.saveDisabled"));
        }
        firePropertyChange(IWorkbenchPartConstants.PROP_DIRTY);
    }
}
