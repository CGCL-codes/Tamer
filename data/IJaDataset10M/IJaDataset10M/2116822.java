package org.nexopenframework.ide.eclipse.wizards;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.corext.refactoring.StubTypeContext;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.internal.ui.IJavaHelpContextIds;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.dialogs.TableTextCellEditor;
import org.eclipse.jdt.internal.ui.dialogs.TextFieldNavigationHandler;
import org.eclipse.jdt.internal.ui.dialogs.TypeSelectionDialog2;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.CompletionContextRequestor;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.ControlContentAssistHelper;
import org.eclipse.jdt.internal.ui.refactoring.contentassist.JavaTypeCompletionProcessor;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.nexopenframework.ide.eclipse.ui.util.ServiceComponentUtil;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>This wizzard creates a new configurable Service Class</p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class NewServiceWizardPageTwo extends NewServiceComponentWizardPage {

    private ListDialogField fDependsDialogField;

    /**
	 * <p>Constructor for NewServiceWizardPage.</p>
	 * 
	 */
    public NewServiceWizardPageTwo() {
        super(true, "wizardPage2", null);
        setTitle("Service Class");
        setDescription("This wizard creates a new configurable Service");
        String[] addButtons = new String[] { "Add...", null, "Remove" };
        IListAdapter adapter = new IListAdapter() {

            public void customButtonPressed(ListDialogField field, int index) {
                if (field == fDependsDialogField) {
                    chooseDependencies();
                    List interceptors = fDependsDialogField.getElements();
                    if (!interceptors.isEmpty()) {
                        Object element = interceptors.get(interceptors.size() - 1);
                        fDependsDialogField.editElement(element);
                    }
                }
            }

            public void doubleClicked(ListDialogField field) {
            }

            public void selectionChanged(ListDialogField field) {
                List interceptors = fDependsDialogField.getElements();
                Iterator it_interceptors = interceptors.iterator();
                StatusInfo si = null;
                while (it_interceptors.hasNext()) {
                    try {
                        ServiceWrapper wrapper = (ServiceWrapper) it_interceptors.next();
                        if (!ServiceComponentUtil.isServiceImplementor(wrapper.type)) {
                            si = new StatusInfo();
                            si.setError("Not a service implementation class");
                            break;
                        }
                    } catch (JavaModelException e) {
                        JavaPlugin.log(e);
                    }
                }
                updateStatus(si);
            }
        };
        fDependsDialogField = new ListDialogField(adapter, addButtons, new DependenciesListLabelProvider());
        fDependsDialogField.setTableColumns(new ListDialogField.ColumnsDescription(1, false));
        fDependsDialogField.setLabelText("Dependencies");
        fDependsDialogField.setRemoveButtonIndex(2);
    }

    public boolean hasDependencies() {
        List elements = fDependsDialogField.getElements();
        return !elements.isEmpty();
    }

    public String[] getDependencies() {
        List elements = fDependsDialogField.getElements();
        String[] interceptors = new String[elements.size()];
        int k = 0;
        for (Iterator iter = elements.iterator(); iter.hasNext(); ) {
            ServiceWrapper wrapper = (ServiceWrapper) iter.next();
            interceptors[k++] = wrapper.serviceName;
        }
        return interceptors;
    }

    /**
	 * @see IDialogPage#createControl(Composite)
	 */
    public void createControl(Composite parent) {
        initializeDialogUnits(parent);
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setFont(parent.getFont());
        int nColumns = 4;
        GridLayout layout = new GridLayout();
        layout.numColumns = 4;
        composite.setLayout(layout);
        super.createInterceptorsControls(composite, nColumns);
        this.createDependenciesControls(composite, nColumns);
        setControl(composite);
        Dialog.applyDialogFont(composite);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(composite, IJavaHelpContextIds.NEW_CLASS_WIZARD_PAGE);
    }

    protected void initTypePage(IJavaElement elem) {
        super.initTypePage(elem);
        if (elem == null) {
            return;
        }
        final List<Object> dependencies = new ArrayList<Object>(5);
        try {
            IType type = null;
            if (elem.getElementType() == IJavaElement.TYPE) {
                type = (IType) elem;
                if (type.exists() && type.isClass()) {
                    dependencies.add(type);
                }
            }
        } catch (JavaModelException e) {
            JavaPlugin.log(e);
        }
        for (Iterator iter = dependencies.iterator(); iter.hasNext(); ) {
            final IType type = (IType) iter.next();
            dependencies.add(new ServiceWrapper(type));
        }
        fDependsDialogField.setElements(dependencies);
        fDependsDialogField.setEnabled(true);
    }

    /**
	 * Creates the controls for the superclass name field. Expects a <code>GridLayout</code> with 
	 * at least 3 columns.
	 * 
	 * @param composite the parent composite
	 * @param nColumns number of columns to span
	 */
    @SuppressWarnings("deprecation")
    protected void createDependenciesControls(Composite composite, int nColumns) {
        final String SERVICE = "service";
        fDependsDialogField.doFillIntoGrid(composite, nColumns);
        final TableViewer tableViewer = fDependsDialogField.getTableViewer();
        tableViewer.setColumnProperties(new String[] { SERVICE });
        TableTextCellEditor cellEditor = new TableTextCellEditor(tableViewer, 0) {

            protected void doSetFocus() {
                if (text != null) {
                    text.setFocus();
                    text.setSelection(text.getText().length());
                    checkSelection();
                    checkDeleteable();
                    checkSelectable();
                }
            }
        };
        JavaTypeCompletionProcessor interceptorsCompletionProcessor = new JavaTypeCompletionProcessor(false, false);
        interceptorsCompletionProcessor.setCompletionContextRequestor(new CompletionContextRequestor() {

            public StubTypeContext getStubTypeContext() {
                return null;
            }
        });
        org.eclipse.jface.contentassist.SubjectControlContentAssistant contentAssistant = ControlContentAssistHelper.createJavaContentAssistant(interceptorsCompletionProcessor);
        Text cellEditorText = cellEditor.getText();
        org.eclipse.ui.contentassist.ContentAssistHandler.createHandlerForText(cellEditorText, contentAssistant);
        TextFieldNavigationHandler.install(cellEditorText);
        cellEditor.setContentAssistant(contentAssistant);
        tableViewer.setCellEditors(new CellEditor[] { cellEditor });
        tableViewer.setCellModifier(new ICellModifier() {

            public void modify(Object element, String property, Object value) {
                if (element instanceof Item) {
                    element = ((Item) element).getData();
                }
                ((ServiceWrapper) element).serviceName = (String) value;
                fDependsDialogField.elementChanged(element);
            }

            public Object getValue(Object element, String property) {
                return ((ServiceWrapper) element).serviceName;
            }

            public boolean canModify(Object element, String property) {
                return true;
            }
        });
        tableViewer.getTable().addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent event) {
                if (event.keyCode == SWT.F2 && event.stateMask == 0) {
                    ISelection selection = tableViewer.getSelection();
                    if (!(selection instanceof IStructuredSelection)) return;
                    IStructuredSelection structuredSelection = (IStructuredSelection) selection;
                    tableViewer.editElement(structuredSelection.getFirstElement(), 0);
                }
            }
        });
        GridData gd = (GridData) fDependsDialogField.getListControl(null).getLayoutData();
        gd.grabExcessVerticalSpace = false;
        gd.widthHint = getMaxFieldWidth();
    }

    protected void chooseDependencies() {
        IPackageFragmentRoot root = getPackageFragmentRoot();
        if (root == null) {
            return;
        }
        IJavaElement[] elements = new IJavaElement[] { root.getJavaProject() };
        IJavaSearchScope scope = SearchEngine.createJavaSearchScope(elements);
        TypeSelectionDialog2 dialog = new TypeSelectionDialog2(getShell(), false, getWizard().getContainer(), scope, IJavaSearchConstants.CLASS);
        dialog.setTitle("Dependencies Selection");
        dialog.setMessage("Choose a service");
        dialog.setFilter(getSuperClass());
        if (dialog.open() == Window.OK) {
            IType type = (IType) dialog.getFirstResult();
            if (type != null) {
                try {
                    StatusInfo si = null;
                    if (!ServiceComponentUtil.isServiceImplementor(type)) {
                        si = new StatusInfo();
                        si.setError("Not a service implementation class");
                    }
                    fDependsDialogField.addElement(new ServiceWrapper(type));
                    updateStatus(si);
                } catch (JavaModelException e) {
                    JavaPlugin.log(e);
                }
            }
        }
    }

    /**
	 * @param ast
	 * @param type
	 */
    @SuppressWarnings("unchecked")
    protected void handleDependencies(AST ast, TypeDeclaration type) {
        NormalAnnotation interceptorAnnotation = ast.newNormalAnnotation();
        interceptorAnnotation.setTypeName(ast.newSimpleName("Depends"));
        MemberValuePair mvpv = ast.newMemberValuePair();
        mvpv.setName(ast.newSimpleName("value"));
        ArrayInitializer ai = ast.newArrayInitializer();
        String[] dependencies = this.getDependencies();
        for (int k = 0; k < dependencies.length; k++) {
            String interceptor = dependencies[k];
            String[] elements = interceptor.split("\\.");
            String simpleName = elements[elements.length - 1];
            SimpleType stype = ast.newSimpleType(ast.newSimpleName(simpleName));
            TypeLiteral tl = ast.newTypeLiteral();
            tl.setType(stype);
            ai.expressions().add(tl);
        }
        mvpv.setValue(ai);
        interceptorAnnotation.values().add(mvpv);
        type.modifiers().add(0, interceptorAnnotation);
    }

    protected static class DependenciesListLabelProvider extends LabelProvider {

        private Image fServiceImage;

        public DependenciesListLabelProvider() {
            fServiceImage = JavaPluginImages.get(JavaPluginImages.IMG_OBJS_CLASS);
        }

        public String getText(Object element) {
            return ((ServiceWrapper) element).serviceName;
        }

        public Image getImage(Object element) {
            return fServiceImage;
        }
    }

    protected static class ServiceWrapper {

        public String serviceName;

        public IType type;

        public ServiceWrapper(IType type) {
            this.type = type;
            String serviceName = JavaModelUtil.getFullyQualifiedName(type);
            this.serviceName = serviceName;
        }

        public int hashCode() {
            return serviceName.hashCode();
        }

        public boolean equals(Object obj) {
            return obj != null && getClass().equals(obj.getClass()) && ((ServiceWrapper) obj).serviceName.equals(serviceName);
        }
    }
}
