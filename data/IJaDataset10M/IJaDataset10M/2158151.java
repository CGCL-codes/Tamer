package net.taylor.uml2.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.StringTokenizer;
import net.taylor.uml2.uml.edit.UMLEditPlugin;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * This is a simple wizard for creating a new model file. <!-- begin-user-doc
 * --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class UML2ModelWizard extends Wizard implements INewWizard {

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public static final String copyright = "";

    /**
	 * This caches an instance of the model package. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected UMLPackage uml2Package = UMLPackage.eINSTANCE;

    /**
	 * This caches an instance of the model factory. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected UMLFactory uml2Factory = uml2Package.getUMLFactory();

    /**
	 * This is the file creation page. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
    protected UML2ModelWizardNewFileCreationPage newFileCreationPage;

    /**
	 * This is the initial object creation page. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
    protected UML2ModelWizardInitialObjectCreationPage initialObjectCreationPage;

    /**
	 * Remember the selection during initialization for populating the default
	 * container. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected IStructuredSelection selection;

    /**
	 * Remember the workbench during initialization. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected IWorkbench workbench;

    /**
	 * Caches the names of the types that can be created as the root object.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected List initialObjectNames;

    /**
	 * This just records the information. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbench = workbench;
        this.selection = selection;
        setWindowTitle(UML2EditorPlugin.INSTANCE.getString("_UI_Wizard_label"));
        setDefaultPageImageDescriptor(ExtendedImageRegistry.INSTANCE.getImageDescriptor(UML2EditorPlugin.INSTANCE.getImage("full/wizban/NewUML2")));
    }

    /**
	 * Returns the names of the types that can be created as the root object.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    protected Collection getInitialObjectNames() {
        if (initialObjectNames == null) {
            initialObjectNames = new ArrayList();
            initialObjectNames.add("Model");
        }
        return initialObjectNames;
    }

    /**
	 * Create a new model. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 */
    protected EObject createInitialModel() {
        EClass eClass = (EClass) uml2Package.getEClassifier(initialObjectCreationPage.getInitialObjectName());
        EObject rootObject = uml2Factory.create(eClass);
        List<String> profiles = getProfiles();
        for (String string : profiles) {
            try {
                importProfile((Package) rootObject, string);
            } catch (Exception e) {
                throw new RuntimeException("Loading Profile: " + string, e);
            }
        }
        return rootObject;
    }

    protected List<String> getProfiles() {
        List<String> profiles = new ArrayList<String>();
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint point = registry.getExtensionPoint("net.taylor.mda.profiles.profiles");
        if (point == null) throw new RuntimeException("No profiles defined");
        IExtension[] extensions = point.getExtensions();
        for (int i = 0; i < extensions.length; i++) {
            IConfigurationElement[] ce = extensions[i].getConfigurationElements();
            for (int j = 0; j < ce.length; j++) {
                profiles.add(ce[j].getAttribute("id"));
            }
        }
        return profiles;
    }

    protected void importProfile(Package package_, String path) {
        Profile profile = (Profile) load(URI.createURI(path));
        package_.applyProfile(profile);
    }

    protected Package load(URI uri) {
        ResourceSet resourceSet = new ResourceSetImpl();
        Package package_ = null;
        try {
            Resource resource = resourceSet.getResource(uri, true);
            package_ = (Package) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.eINSTANCE.getPackage());
        } catch (WrappedException we) {
            throw new RuntimeException(we);
        }
        return package_;
    }

    /**
	 * Do the work after everything is specified. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
    public boolean performFinish() {
        try {
            final IFile modelFile = getModelFile();
            WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {

                protected void execute(IProgressMonitor progressMonitor) {
                    try {
                        ResourceSet resourceSet = new ResourceSetImpl();
                        URI fileURI = URI.createPlatformResourceURI(modelFile.getFullPath().toString());
                        Resource resource = resourceSet.createResource(fileURI);
                        EObject rootObject = createInitialModel();
                        if (rootObject != null) {
                            resource.getContents().add(rootObject);
                        }
                        Map options = new HashMap();
                        options.put(XMLResource.OPTION_ENCODING, initialObjectCreationPage.getEncoding());
                        resource.save(options);
                    } catch (Exception exception) {
                        UML2EditorPlugin.INSTANCE.log(exception);
                    } finally {
                        progressMonitor.done();
                    }
                }
            };
            getContainer().run(false, false, operation);
            return true;
        } catch (Exception exception) {
            UML2EditorPlugin.INSTANCE.log(exception);
            return false;
        }
    }

    /**
	 * This is the one page of the wizard. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
    public class UML2ModelWizardNewFileCreationPage extends WizardNewFileCreationPage {

        /**
		 * Pass in the selection. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        public UML2ModelWizardNewFileCreationPage(String pageId, IStructuredSelection selection) {
            super(pageId, selection);
        }

        /**
		 * The framework calls this to see if the file is correct. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        protected boolean validatePage() {
            if (super.validatePage()) {
                String requiredExt = UML2EditorPlugin.INSTANCE.getString("_UI_UML2EditorFilenameExtension");
                String enteredExt = new Path(getFileName()).getFileExtension();
                if (enteredExt == null || !enteredExt.equals(requiredExt)) {
                    setErrorMessage(UML2EditorPlugin.INSTANCE.getString("_WARN_FilenameExtension", new Object[] { requiredExt }));
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }

        /**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        public IFile getModelFile() {
            return ResourcesPlugin.getWorkspace().getRoot().getFile(getContainerFullPath().append(getFileName()));
        }
    }

    /**
	 * This is the page where the type of object to create is selected. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public class UML2ModelWizardInitialObjectCreationPage extends WizardPage {

        /**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        protected Combo initialObjectField;

        /**
		 * @generated <!-- begin-user-doc --> <!-- end-user-doc -->
		 */
        protected List encodings;

        /**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        protected Combo encodingField;

        /**
		 * Pass in the selection. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        public UML2ModelWizardInitialObjectCreationPage(String pageId) {
            super(pageId);
        }

        /**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        public void createControl(Composite parent) {
            Composite composite = new Composite(parent, SWT.NONE);
            {
                GridLayout layout = new GridLayout();
                layout.numColumns = 1;
                layout.verticalSpacing = 12;
                composite.setLayout(layout);
                GridData data = new GridData();
                data.verticalAlignment = GridData.FILL;
                data.grabExcessVerticalSpace = true;
                data.horizontalAlignment = GridData.FILL;
                composite.setLayoutData(data);
            }
            Label containerLabel = new Label(composite, SWT.LEFT);
            {
                containerLabel.setText(UML2EditorPlugin.INSTANCE.getString("_UI_ModelObject"));
                GridData data = new GridData();
                data.horizontalAlignment = GridData.FILL;
                containerLabel.setLayoutData(data);
            }
            initialObjectField = new Combo(composite, SWT.BORDER);
            {
                GridData data = new GridData();
                data.horizontalAlignment = GridData.FILL;
                data.grabExcessHorizontalSpace = true;
                initialObjectField.setLayoutData(data);
            }
            for (Iterator i = getInitialObjectNames().iterator(); i.hasNext(); ) {
                initialObjectField.add(getLabel((String) i.next()));
            }
            if (initialObjectField.getItemCount() == 1) {
                initialObjectField.select(0);
            }
            initialObjectField.addModifyListener(validator);
            Label encodingLabel = new Label(composite, SWT.LEFT);
            {
                encodingLabel.setText(UML2EditorPlugin.INSTANCE.getString("_UI_XMLEncoding"));
                GridData data = new GridData();
                data.horizontalAlignment = GridData.FILL;
                encodingLabel.setLayoutData(data);
            }
            encodingField = new Combo(composite, SWT.BORDER);
            {
                GridData data = new GridData();
                data.horizontalAlignment = GridData.FILL;
                data.grabExcessHorizontalSpace = true;
                encodingField.setLayoutData(data);
            }
            for (Iterator i = getEncodings().iterator(); i.hasNext(); ) {
                encodingField.add((String) i.next());
            }
            encodingField.select(0);
            encodingField.addModifyListener(validator);
            setPageComplete(validatePage());
            setControl(composite);
        }

        /**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        protected ModifyListener validator = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                setPageComplete(validatePage());
            }
        };

        /**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        protected boolean validatePage() {
            return getInitialObjectName() != null && getEncodings().contains(encodingField.getText());
        }

        /**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        public void setVisible(boolean visible) {
            super.setVisible(visible);
            if (visible) {
                if (initialObjectField.getItemCount() == 1) {
                    initialObjectField.clearSelection();
                    encodingField.setFocus();
                } else {
                    encodingField.clearSelection();
                    initialObjectField.setFocus();
                }
            }
        }

        /**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        public String getInitialObjectName() {
            String label = initialObjectField.getText();
            for (Iterator i = getInitialObjectNames().iterator(); i.hasNext(); ) {
                String name = (String) i.next();
                if (getLabel(name).equals(label)) {
                    return name;
                }
            }
            return null;
        }

        /**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        public String getEncoding() {
            return encodingField.getText();
        }

        /**
		 * Returns the label for the specified type name. <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        protected String getLabel(String typeName) {
            try {
                return UMLEditPlugin.INSTANCE.getString("_UI_" + typeName + "_type");
            } catch (MissingResourceException mre) {
                UML2EditorPlugin.INSTANCE.log(mre);
            }
            return typeName;
        }

        /**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
        protected Collection getEncodings() {
            if (encodings == null) {
                encodings = new ArrayList();
                for (StringTokenizer stringTokenizer = new StringTokenizer(UML2EditorPlugin.INSTANCE.getString("_UI_XMLEncodingChoices")); stringTokenizer.hasMoreTokens(); ) {
                    encodings.add(stringTokenizer.nextToken());
                }
            }
            return encodings;
        }
    }

    /**
	 * The framework calls this to create the contents of the wizard. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public void addPages() {
        newFileCreationPage = new UML2ModelWizardNewFileCreationPage("Whatever", selection);
        newFileCreationPage.setTitle(UML2EditorPlugin.INSTANCE.getString("_UI_UML2ModelWizard_label"));
        newFileCreationPage.setDescription(UML2EditorPlugin.INSTANCE.getString("_UI_UML2ModelWizard_description"));
        newFileCreationPage.setFileName(UML2EditorPlugin.INSTANCE.getString("_UI_UML2EditorFilenameDefaultBase") + "." + UML2EditorPlugin.INSTANCE.getString("_UI_UML2EditorFilenameExtension"));
        addPage(newFileCreationPage);
        if (selection != null && !selection.isEmpty()) {
            Object selectedElement = selection.iterator().next();
            if (selectedElement instanceof IResource) {
                IResource selectedResource = (IResource) selectedElement;
                if (selectedResource.getType() == IResource.FILE) {
                    selectedResource = selectedResource.getParent();
                }
                if (selectedResource instanceof IFolder || selectedResource instanceof IProject) {
                    newFileCreationPage.setContainerFullPath(selectedResource.getFullPath());
                    String defaultModelBaseFilename = UML2EditorPlugin.INSTANCE.getString("_UI_UML2EditorFilenameDefaultBase");
                    String defaultModelFilenameExtension = UML2EditorPlugin.INSTANCE.getString("_UI_UML2EditorFilenameExtension");
                    String modelFilename = defaultModelBaseFilename + "." + defaultModelFilenameExtension;
                    for (int i = 1; ((IContainer) selectedResource).findMember(modelFilename) != null; ++i) {
                        modelFilename = defaultModelBaseFilename + i + "." + defaultModelFilenameExtension;
                    }
                    newFileCreationPage.setFileName(modelFilename);
                }
            }
        }
        initialObjectCreationPage = new UML2ModelWizardInitialObjectCreationPage("Whatever2");
        initialObjectCreationPage.setTitle(UML2EditorPlugin.INSTANCE.getString("_UI_UML2ModelWizard_label"));
        initialObjectCreationPage.setDescription(UML2EditorPlugin.INSTANCE.getString("_UI_Wizard_initial_object_description"));
        addPage(initialObjectCreationPage);
    }

    /**
	 * Get the file from the page. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
    public IFile getModelFile() {
        return newFileCreationPage.getModelFile();
    }
}
