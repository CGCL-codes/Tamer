package net.sf.rcer.rfcgen.ui.wizard;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunctionTemplate;

/**
 * A page to let the user select function modules for mapping.
 * @author vwegert
 *
 */
public class FunctionModuleSelectionPage extends WizardPage {

    private static final String PLUGIN_ID = "net.sf.rcer.rfcgen.ui";

    private JCoDestination destination;

    private Map<String, JCoFunctionTemplate> selectedFunctionModules = new TreeMap<String, JCoFunctionTemplate>();

    private Text functionModuleText;

    private List functionModuleList;

    /**
	 * Default constructor.
	 * @param destination the destination to use
	 */
    public FunctionModuleSelectionPage(JCoDestination destination) {
        super("Select Function Modules");
        setTitle("Select Function Modules");
        setDescription("Select the RFC function modules you want to include in the generated mapping file. You can use * as a wildcard to search for function modules.");
        setPageComplete(false);
        this.destination = destination;
    }

    public void createControl(Composite parent) {
        Composite top = new Composite(parent, SWT.NONE);
        GridLayoutFactory.swtDefaults().numColumns(2).applyTo(top);
        functionModuleText = new Text(top, SWT.BORDER | SWT.SINGLE);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(functionModuleText);
        Button addButton = new Button(top, SWT.PUSH);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).applyTo(addButton);
        addButton.setText("Add");
        addButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                addFunctionModule(functionModuleText.getText().toUpperCase());
            }
        });
        functionModuleList = new List(top, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(functionModuleList);
        Button removeButton = new Button(top, SWT.PUSH);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.BEGINNING).applyTo(removeButton);
        removeButton.setText("Remove");
        removeButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                removeSelectedFunctionModules();
            }
        });
        setControl(top);
        Dialog.applyDialogFont(top);
    }

    /**
	 * Checks a function module name for existence and adds the function module to the list. 
	 * If the name contains a *, a wildcard search is run and a list of matching function modules is displayed.
	 * @param name
	 */
    protected void addFunctionModule(String name) {
        if (!selectedFunctionModules.containsKey(name)) {
            try {
                if (name.contains("*")) {
                    final FunctionModuleSearchCall searchCall = new FunctionModuleSearchCall();
                    searchCall.setFunctionName(name);
                    searchCall.setLanguage(destination.getLanguage());
                    getContainer().run(true, false, new IRunnableWithProgress() {

                        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                            monitor.beginTask("Searching for function modules...", IProgressMonitor.UNKNOWN);
                            try {
                                searchCall.execute(destination);
                            } catch (JCoException e) {
                                throw new InvocationTargetException(e);
                            } finally {
                                monitor.done();
                            }
                        }
                    });
                    if (searchCall.getFunctions().isEmpty()) {
                        final String message = MessageFormat.format("No function module mathching the search pattern {0} was found.", name);
                        ErrorDialog.openError(getShell(), "RFC Mapping Wizard", message, new Status(IStatus.ERROR, PLUGIN_ID, message));
                    } else {
                        ILabelProvider labelProvider = new LabelProvider() {

                            @Override
                            public String getText(Object element) {
                                if (element instanceof FunctionModuleDescription) {
                                    FunctionModuleDescription desc = (FunctionModuleDescription) element;
                                    return MessageFormat.format("{0} ({1})", desc.getFunctionName(), desc.getText());
                                }
                                return super.getText(element);
                            }
                        };
                        ListSelectionDialog dlg = new ListSelectionDialog(getShell(), searchCall.getFunctions(), new ArrayContentProvider(), labelProvider, "Select the function modules to add.");
                        dlg.setTitle("Add Function Modules");
                        if (dlg.open() == ListSelectionDialog.OK) {
                            final Object[] res = dlg.getResult();
                            getContainer().run(true, true, new IRunnableWithProgress() {

                                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                                    monitor.beginTask("Reading interface of function module:", res.length);
                                    for (Object obj : res) {
                                        if (monitor.isCanceled()) {
                                            throw new InterruptedException();
                                        }
                                        if (obj instanceof FunctionModuleDescription) {
                                            final FunctionModuleDescription desc = (FunctionModuleDescription) obj;
                                            monitor.subTask(desc.getFunctionName());
                                            JCoFunctionTemplate functionModule = null;
                                            try {
                                                functionModule = destination.getRepository().getFunctionTemplate(desc.getFunctionName());
                                            } catch (final JCoException e) {
                                                getShell().getDisplay().syncExec(new Runnable() {

                                                    public void run() {
                                                        final String message = MessageFormat.format("Unable to read the interface structure of function module {0}.", desc.getFunctionName());
                                                        ErrorDialog.openError(getShell(), "RFC Mapping Wizard", message, new Status(IStatus.ERROR, PLUGIN_ID, e.getMessage(), e));
                                                    }
                                                });
                                            }
                                            if (functionModule != null) {
                                                selectedFunctionModules.put(desc.getFunctionName(), functionModule);
                                                monitor.worked(1);
                                            }
                                        }
                                    }
                                    monitor.done();
                                }
                            });
                            updateList();
                        }
                    }
                } else {
                    JCoFunctionTemplate functionModule = destination.getRepository().getFunctionTemplate(name);
                    if (functionModule == null) {
                        final String message = MessageFormat.format("The function module {0} does not exist.", name);
                        ErrorDialog.openError(getShell(), "RFC Mapping Wizard", message, new Status(IStatus.ERROR, PLUGIN_ID, message));
                    } else {
                        selectedFunctionModules.put(name, functionModule);
                        updateList();
                    }
                }
            } catch (JCoException e) {
                ErrorDialog.openError(getShell(), "RFC Mapping Wizard", e.getMessage(), new Status(IStatus.ERROR, PLUGIN_ID, e.getMessage(), e));
            } catch (InvocationTargetException e) {
                ErrorDialog.openError(getShell(), "RFC Mapping Wizard", e.getCause().getMessage(), new Status(IStatus.ERROR, PLUGIN_ID, e.getCause().getMessage(), e.getCause()));
            } catch (InterruptedException e) {
                ErrorDialog.openError(getShell(), "RFC Mapping Wizard", e.getMessage(), new Status(IStatus.ERROR, PLUGIN_ID, e.getMessage(), e));
            }
        }
    }

    /**
	 * Removes the selected function modules from the list. 
	 */
    protected void removeSelectedFunctionModules() {
        for (final String functionModule : functionModuleList.getSelection()) {
            selectedFunctionModules.remove(functionModule);
        }
        updateList();
    }

    /**
	 * Updates the list display and determines whether the user may continue.
	 */
    private void updateList() {
        final Set<String> functionModules = selectedFunctionModules.keySet();
        functionModuleList.setItems(functionModules.toArray(new String[functionModules.size()]));
        setPageComplete(functionModules.size() > 0);
    }

    /**
	 * @return the list of selected function modules
	 * @see java.util.Map#values()
	 */
    public Collection<JCoFunctionTemplate> getSelectedFunctionModules() {
        return selectedFunctionModules.values();
    }
}
