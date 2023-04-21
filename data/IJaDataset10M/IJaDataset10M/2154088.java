package com.safi.workshop.importwiz;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.List;
import com.safi.workshop.part.AsteriskDiagramEditorPlugin;
import com.swtdesigner.ResourceManager;

public class SelectSafletsPage extends WizardPage {

    class ContentProvider implements IStructuredContentProvider {

        public Object[] getElements(Object inputElement) {
            if (inputElement instanceof Set) return ((Set<File>) inputElement).toArray();
            return null;
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    }

    private Button browseFilesButton;

    private List list;

    private ListViewer listViewer;

    private java.util.Set<File> saflets;

    /**
   * Create the wizard
   */
    public SelectSafletsPage() {
        super("wizardPage");
        setTitle("Import Saflets Wizard");
        setDescription("Select saflets for import");
        setImageDescriptor(ResourceManager.getPluginImageDescriptor(AsteriskDiagramEditorPlugin.getDefault(), "icons/wizban/ImportSafletWizard.gif"));
        saflets = new HashSet<File>();
    }

    /**
   * Create contents of the wizard
   * 
   * @param parent
   */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginRight = 20;
        gridLayout.marginBottom = 30;
        gridLayout.marginTop = 20;
        gridLayout.marginLeft = 20;
        gridLayout.numColumns = 2;
        container.setLayout(gridLayout);
        setControl(container);
        listViewer = new ListViewer(container, SWT.BORDER);
        listViewer.setContentProvider(new ContentProvider());
        list = listViewer.getList();
        list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        listViewer.setInput(saflets);
        browseFilesButton = new Button(container, SWT.NONE);
        browseFilesButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                FileDialog dlg = new FileDialog(getShell(), SWT.OPEN | SWT.MULTI);
                dlg.setFilterExtensions(new String[] { "*.saflet" });
                dlg.setText("Select saflets for import");
                String file = dlg.open();
                if (file != null) {
                    String[] files = dlg.getFileNames();
                    for (String fname : files) {
                        String pathname = dlg.getFilterPath() + File.separatorChar + fname;
                        saflets.add(new File(pathname));
                    }
                    if (!saflets.isEmpty()) setPageComplete(true); else setPageComplete(false);
                    listViewer.refresh();
                }
            }
        });
        browseFilesButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
        browseFilesButton.setText("Browse Files");
    }

    public java.util.Set<File> getSaflets() {
        return saflets;
    }
}
