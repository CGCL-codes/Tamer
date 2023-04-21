package org.remus.infomngmnt.link.preferences;

import java.util.Set;
import org.remus.infomngmnt.link.LinkActivator;
import org.remus.infomngmnt.link.messsage.Messages;
import org.remus.infomngmnt.link.webshot.WebshotUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.remus.BinaryReference;
import org.eclipse.remus.InformationUnit;
import org.eclipse.remus.InformationUnitListItem;
import org.eclipse.remus.common.core.util.ResourceUtil;
import org.eclipse.remus.core.commands.CommandFactory;
import org.eclipse.remus.core.commands.DeleteBinaryReferenceCommand;
import org.eclipse.remus.core.edit.DisposableEditingDomain;
import org.eclipse.remus.core.services.IEditingHandler;
import org.eclipse.remus.util.InformationUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class LinkEditorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private Group createGroup;

    private ComboFieldEditor comboEditor;

    /**
	 * Create the preference page
	 */
    public LinkEditorPreferencePage() {
        super();
    }

    @Override
    protected IPreferenceStore doGetPreferenceStore() {
        return LinkActivator.getDefault().getPreferenceStore();
    }

    /**
	 * Initialize the preference page
	 */
    public void init(final IWorkbench workbench) {
    }

    @Override
    protected Control createContents(final Composite parent) {
        Composite fieldEditorParent = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        fieldEditorParent.setLayout(layout);
        fieldEditorParent.setFont(parent.getFont());
        createGroup = new Group(fieldEditorParent, SWT.NONE);
        createGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        createGroup.setText(Messages.LinkEditorPreferencePage_Creating);
        createFieldEditors();
        Link link = new Link(createGroup, SWT.NONE);
        link.setText(Messages.LinkEditorPreferencePage_DownloadRenderer);
        link.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                int selectionIndex = getCombo().getSelectionIndex();
                if (selectionIndex >= 1) {
                    String url = getPreferenceStore().getString(LinkPreferenceInitializer.LIST_RENDERER_URL).split(",")[selectionIndex];
                    Program.launch(url);
                } else {
                    MessageDialog.openInformation(getShell(), Messages.LinkEditorPreferencePage_SelectRenderer, Messages.LinkEditorPreferencePage_PleaseSelectRenderrer);
                }
            }
        });
        Button button = new Button(createGroup, SWT.PUSH);
        button.setText(Messages.LinkEditorPreferencePage_RefreshWebshots);
        button.setLayoutData(new GridData(SWT.END, SWT.TOP, true, false, 2, 1));
        button.addListener(SWT.Selection, new Listener() {

            public void handleEvent(final Event event) {
                Job job = new Job(Messages.LinkEditorPreferencePage_RefreshingWebshots) {

                    @Override
                    protected IStatus run(final IProgressMonitor monitor) {
                        IEditingHandler service = LinkActivator.getDefault().getServiceTracker().getService(IEditingHandler.class);
                        DisposableEditingDomain editingDomain = service.createNewEditingDomain();
                        Set<? extends EObject> allItemsByType = InformationUtil.getAllItemsByType(LinkActivator.LINK_INFO_ID);
                        monitor.beginTask(Messages.LinkEditorPreferencePage_RefreshingWebshots, allItemsByType.size());
                        for (EObject eObject : allItemsByType) {
                            if (eObject instanceof InformationUnitListItem) {
                                InformationUnit adapter = (InformationUnit) ((InformationUnitListItem) eObject).getAdapter(InformationUnit.class);
                                monitor.subTask(NLS.bind(Messages.LinkEditorPreferencePage_RefreshingElement, adapter.getLabel()));
                                if (adapter != null) {
                                    IFile tmpFile = ResourceUtil.createTempFile("png");
                                    WebshotUtil.performWebShot(adapter.getStringValue(), tmpFile.getLocation().toOSString());
                                    BinaryReference binaryReferences = adapter.getBinaryReferences();
                                    CompoundCommand cc = new CompoundCommand();
                                    if (binaryReferences != null) {
                                        cc.append(new DeleteBinaryReferenceCommand(binaryReferences, editingDomain));
                                    }
                                    cc.append(CommandFactory.addFileToInfoUnit(tmpFile, adapter, editingDomain));
                                    editingDomain.getCommandStack().execute(cc);
                                    service.saveObjectToResource(adapter);
                                }
                                monitor.worked(1);
                            }
                        }
                        editingDomain.getCommandStack().flush();
                        editingDomain.dispose();
                        LinkActivator.getDefault().getServiceTracker().ungetService(service);
                        return Status.OK_STATUS;
                    }
                };
                job.setUser(true);
                job.schedule();
            }
        });
        initialize();
        checkState();
        GridLayout gridLayout = new GridLayout(2, false);
        createGroup.setLayout(gridLayout);
        return fieldEditorParent;
    }

    @Override
    protected void createFieldEditors() {
        BooleanFieldEditor fieldEditor = new BooleanFieldEditor(LinkPreferenceInitializer.INDEX_DOCUMENT, Messages.LinkEditorPreferencePage_IndexByDefault, createGroup);
        fieldEditor.fillIntoGrid(createGroup, 2);
        addField(fieldEditor);
        BooleanFieldEditor fieldEditor2 = new BooleanFieldEditor(LinkPreferenceInitializer.MAKE_SCREENSHOT, Messages.LinkEditorPreferencePage_MakeWebshotByDefault, createGroup);
        fieldEditor2.fillIntoGrid(createGroup, 2);
        addField(fieldEditor2);
        String[] split = getPreferenceStore().getString(LinkPreferenceInitializer.LIST_RENDERER).split(",");
        String[][] entries = new String[split.length][2];
        for (int i = 0; i < split.length; i++) {
            entries[i][0] = split[i];
            entries[i][1] = String.valueOf(i);
        }
        comboEditor = new ComboFieldEditor(LinkPreferenceInitializer.RENDERER_SELECTED, Messages.LinkEditorPreferencePage_SelectRenderer, entries, createGroup);
        addField(comboEditor);
        addField(new StringFieldEditor(LinkPreferenceInitializer.SCREENSHOT_CMD, Messages.LinkEditorPreferencePage_PathToWebshot, createGroup));
    }

    private Combo getCombo() {
        Control[] children = createGroup.getChildren();
        for (Control control : children) {
            if (control instanceof Combo) {
                return (Combo) control;
            }
        }
        return null;
    }
}
