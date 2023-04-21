package com.android.ide.eclipse.adt.internal.editors.manifest.model;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.AndroidEditor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.AttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.descriptors.TextAttributeDescriptor;
import com.android.ide.eclipse.adt.internal.editors.ui.SectionHelper;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiTextAttributeNode;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.actions.OpenNewPackageWizardAction;
import org.eclipse.jdt.ui.actions.ShowInPackageViewAction;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import java.util.ArrayList;

/**
 * Represents an XML attribute for a package, that can be modified using a simple text field or
 * a dialog to choose an existing package. Also, there's a link to create a new package.
 * <p/>
 * See {@link UiTextAttributeNode} for more information.
 */
public class UiPackageAttributeNode extends UiTextAttributeNode {

    /**
     * Creates a {@link UiPackageAttributeNode} object that will display ui to select or create
     * a package.
     * @param attributeDescriptor the {@link AttributeDescriptor} object linked to the Ui Node.
     */
    public UiPackageAttributeNode(AttributeDescriptor attributeDescriptor, UiElementNode uiParent) {
        super(attributeDescriptor, uiParent);
    }

    @Override
    public void createUiControl(final Composite parent, final IManagedForm managedForm) {
        setManagedForm(managedForm);
        FormToolkit toolkit = managedForm.getToolkit();
        TextAttributeDescriptor desc = (TextAttributeDescriptor) getDescriptor();
        StringBuilder label = new StringBuilder();
        label.append("<form><p><a href='unused'>");
        label.append(desc.getUiName());
        label.append("</a></p></form>");
        FormText formText = SectionHelper.createFormText(parent, toolkit, true, label.toString(), true);
        formText.addHyperlinkListener(new HyperlinkAdapter() {

            @Override
            public void linkActivated(HyperlinkEvent e) {
                super.linkActivated(e);
                doLabelClick();
            }
        });
        formText.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.MIDDLE));
        SectionHelper.addControlTooltip(formText, desc.getTooltip());
        Composite composite = toolkit.createComposite(parent);
        composite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.MIDDLE));
        GridLayout gl = new GridLayout(2, false);
        gl.marginHeight = gl.marginWidth = 0;
        composite.setLayout(gl);
        toolkit.paintBordersFor(composite);
        final Text text = toolkit.createText(composite, getCurrentValue());
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalIndent = 1;
        text.setLayoutData(gd);
        setTextWidget(text);
        Button browseButton = toolkit.createButton(composite, "Browse...", SWT.PUSH);
        browseButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                super.widgetSelected(e);
                doBrowseClick();
            }
        });
    }

    @Override
    protected void onAddValidators(final Text text) {
        ModifyListener listener = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                String package_name = text.getText();
                if (package_name.indexOf('.') < 1) {
                    getManagedForm().getMessageManager().addMessage(text, "Package name should contain at least two identifiers.", null, IMessageProvider.ERROR, text);
                } else {
                    getManagedForm().getMessageManager().removeMessage(text, text);
                }
            }
        };
        text.addModifyListener(listener);
        text.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                getManagedForm().getMessageManager().removeMessage(text, text);
            }
        });
        listener.modifyText(null);
    }

    /**
     * Handles response to the Browse button by creating a Package dialog.
     * */
    private void doBrowseClick() {
        Text text = getTextWidget();
        IProject project = getProject();
        if (project != null) {
            try {
                SelectionDialog dlg = JavaUI.createPackageDialog(text.getShell(), JavaCore.create(project), 0);
                dlg.setTitle("Select Android Package");
                dlg.setMessage("Select the package for the Android project.");
                SelectionDialog.setDefaultImage(AdtPlugin.getAndroidLogo());
                if (dlg.open() == Window.OK) {
                    Object[] results = dlg.getResult();
                    if (results.length == 1) {
                        setPackageTextField((IPackageFragment) results[0]);
                    }
                }
            } catch (JavaModelException e1) {
            }
        }
    }

    /**
     * Handles response to the Label hyper link being activated.
     */
    private void doLabelClick() {
        String package_name = getTextWidget().getText().trim();
        if (package_name.length() == 0) {
            createNewPackage();
        } else {
            IProject project = getProject();
            if (project == null) {
                AdtPlugin.log(IStatus.ERROR, "Failed to get project for UiPackageAttribute");
                return;
            }
            IWorkbenchPartSite site = getUiParent().getEditor().getSite();
            if (site == null) {
                AdtPlugin.log(IStatus.ERROR, "Failed to get editor site for UiPackageAttribute");
                return;
            }
            for (IPackageFragmentRoot root : getPackageFragmentRoots(project)) {
                IPackageFragment fragment = root.getPackageFragment(package_name);
                if (fragment != null && fragment.exists()) {
                    ShowInPackageViewAction action = new ShowInPackageViewAction(site);
                    action.run(fragment);
                    return;
                }
            }
        }
    }

    /**
     * Utility method that returns the project for the current file being edited.
     * 
     * @return The IProject for the current file being edited or null.
     */
    private IProject getProject() {
        UiElementNode uiNode = getUiParent();
        AndroidEditor editor = uiNode.getEditor();
        IEditorInput input = editor.getEditorInput();
        if (input instanceof IFileEditorInput) {
            IFile file = ((IFileEditorInput) input).getFile();
            return file.getProject();
        }
        return null;
    }

    /**
     * Utility method that computes and returns the list of {@link IPackageFragmentRoot}
     * corresponding to the source folder of the specified project.
     * 
     * @param project the project
     * @return an array of IPackageFragmentRoot. Can be empty but not null.
     */
    private IPackageFragmentRoot[] getPackageFragmentRoots(IProject project) {
        ArrayList<IPackageFragmentRoot> result = new ArrayList<IPackageFragmentRoot>();
        try {
            IJavaProject javaProject = JavaCore.create(project);
            IPackageFragmentRoot[] roots = javaProject.getPackageFragmentRoots();
            for (int i = 0; i < roots.length; i++) {
                IClasspathEntry entry = roots[i].getRawClasspathEntry();
                if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                    result.add(roots[i]);
                }
            }
        } catch (JavaModelException e) {
        }
        return result.toArray(new IPackageFragmentRoot[result.size()]);
    }

    /**
     * Utility method that sets the package's text field to the package fragment's name.
     * */
    private void setPackageTextField(IPackageFragment type) {
        Text text = getTextWidget();
        String name = type.getElementName();
        text.setText(name);
    }

    /**
     * Displays and handles a "Create Package Wizard".
     * 
     * This is invoked by doLabelClick() when clicking on the hyperlink label with an
     * empty package text field.  
     */
    private void createNewPackage() {
        OpenNewPackageWizardAction action = new OpenNewPackageWizardAction();
        IProject project = getProject();
        action.setSelection(new StructuredSelection(project));
        action.run();
        IJavaElement element = action.getCreatedElement();
        if (element != null && element.exists() && element.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
            setPackageTextField((IPackageFragment) element);
        }
    }

    @Override
    public String[] getPossibleValues(String prefix) {
        return null;
    }
}
