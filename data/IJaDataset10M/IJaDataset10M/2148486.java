package org.teiid.cdk.core.wizards;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.teiid.cdk.core.CdkCorePlugin;
import org.teiid.cdk.core.sdk.CdkClasspathContainer;

/**
 * Cdk Library addition Wizard Page
 * 
 * @see IClasspathContainerPage
 * @see IClasspathContainerPageExtension
 * 
 * @author Sanjay Chaudhuri <email2sanjayc@gmail.com>
 */
public class CdkContainerWizardPage extends WizardPage implements IClasspathContainerPage, IClasspathContainerPageExtension {

    private IClasspathEntry[] classpathEntries;

    private CdkSdkSelectionPanel cdkSdkSelectionPanel;

    public final void initialize(IJavaProject project, IClasspathEntry currentEntries[]) {
        classpathEntries = currentEntries;
    }

    public CdkContainerWizardPage() {
        super("Cdk Toolkit Library", "Cdk Toolkit Library", CdkCorePlugin.getDefault().getImageDescriptorFromRegistry("JAR_WIZARD"));
    }

    public final void createControl(Composite parent) {
        Composite cdkComposite = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 5;
        gridLayout.marginHeight = 5;
        cdkComposite.setLayout(gridLayout);
        cdkComposite.setFont(parent.getFont());
        cdkComposite.setLayoutData(new GridData(SWT.H_SCROLL | SWT.V_SCROLL));
        cdkSdkSelectionPanel = new CdkSdkSelectionPanel(cdkComposite, getTitle(), parent.getFont());
        cdkSdkSelectionPanel.setContainerPath(getContainerPath());
        setControl(cdkComposite);
        setTitle(getTitle());
        setMessage("Select a SDK to add to the classpath.");
    }

    @Override
    public boolean finish() {
        setErrorMessage(null);
        setPageComplete(true);
        return true;
    }

    @Override
    public IClasspathEntry getSelection() {
        return JavaCore.newContainerEntry(cdkSdkSelectionPanel.getContainerPath());
    }

    @Override
    public void setSelection(IClasspathEntry containerEntry) {
    }

    private IPath getContainerPath() {
        for (IClasspathEntry classpathEntry : classpathEntries) {
            if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
                IPath containerPath = classpathEntry.getPath();
                if (containerPath.segmentCount() > 0 && containerPath.segment(0).equals(CdkClasspathContainer.CONTAINER_ID)) return containerPath;
            }
        }
        return null;
    }
}
