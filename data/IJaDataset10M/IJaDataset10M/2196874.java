package de.fraunhofer.isst.axbench.eastadlinterface.wizards;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ISetSelectionTarget;
import de.fraunhofer.isst.axbench.Session;
import de.fraunhofer.isst.axbench.api.AXLMessage.MessageType;
import de.fraunhofer.isst.axbench.axlang.elements.Model;
import de.fraunhofer.isst.axbench.eastadlinterface.Env;
import de.fraunhofer.isst.axbench.operations.AXLReader;
import de.fraunhofer.isst.axbench.operations.ValidityChecker;
import de.fraunhofer.isst.eastadl.autosar.presentation.EastEditorPlugin;
import de.fraunhofer.isst.eastadl.elements.EAXML;
import de.fraunhofer.isst.eastadl.elements.ElementsFactory;
import de.fraunhofer.isst.eastadl.elements.presentation.ElementsEditor;

public class AxlToEastSyncWizard extends Wizard implements IExportWizard {

    protected IWorkbench workbench;

    protected IStructuredSelection selection;

    protected Model axlModel;

    public static final List<String> FILE_EXTENSIONS = Collections.unmodifiableList(Arrays.asList(EastEditorPlugin.INSTANCE.getString("_UI_ElementsEditorFilenameExtensions").split("\\s*,\\s*")));

    public static final String FORMATTED_FILE_EXTENSIONS = EastEditorPlugin.INSTANCE.getString("_UI_ElementsEditorFilenameExtensions").replaceAll("\\s*,\\s*", ", ");

    protected ElementsModelWizardNewFileCreationPage newFileCreationPage = null;

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbench = workbench;
        this.selection = selection;
        this.setWindowTitle("aXBench-model => EAST-ADL model");
    }

    @Override
    public boolean canFinish() {
        return axlModel != null;
    }

    @Override
    public void addPages() {
        axlModel = (Model) Session.getCurrentElement();
        if (axlModel == null) {
            System.err.println("aXBench Editor not open/active.");
            return;
        }
        super.addPages();
        newFileCreationPage = new ElementsModelWizardNewFileCreationPage("Whatever", selection);
        newFileCreationPage.setTitle(EastEditorPlugin.INSTANCE.getString("_UI_ElementsModelWizard_label"));
        newFileCreationPage.setDescription(EastEditorPlugin.INSTANCE.getString("_UI_ElementsModelWizard_description"));
        newFileCreationPage.setFileName(EastEditorPlugin.INSTANCE.getString("_UI_ElementsEditorFilenameDefaultBase") + "." + FILE_EXTENSIONS.get(0));
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
                    String defaultModelBaseFilename = EastEditorPlugin.INSTANCE.getString("_UI_ElementsEditorFilenameDefaultBase");
                    String defaultModelFilenameExtension = FILE_EXTENSIONS.get(0);
                    String modelFilename = defaultModelBaseFilename + "." + defaultModelFilenameExtension;
                    for (int i = 1; ((IContainer) selectedResource).findMember(modelFilename) != null; ++i) {
                        modelFilename = defaultModelBaseFilename + i + "." + defaultModelFilenameExtension;
                    }
                    newFileCreationPage.setFileName(modelFilename);
                }
            }
        }
    }

    /**
	 * @brief reads in an aXBench model from the file and check it.
	 * @param searchedfile
	 * @return aXBench model - without errors or null
	 */
    public Model readInAXBenchModelFromFile(IFile searchedfile) {
        String filepath1 = searchedfile.getProject().getLocation().removeLastSegments(1).toPortableString();
        String filepath2 = searchedfile.getFullPath().toPortableString();
        String filename = filepath1 + filepath2;
        AXLReader theReader = new AXLReader();
        Model axlEditorElement = (Model) theReader.readFile(filename);
        if (axlEditorElement == null) {
            System.err.println("AXLReader.readFile(" + filename + ") returned null");
            return null;
        }
        ValidityChecker validityChecker = new ValidityChecker();
        Map<String, Object> mapCheckInputParameters = new HashMap<String, Object>();
        mapCheckInputParameters.put(ValidityChecker.ID_IN_ELEMENT, axlEditorElement);
        validityChecker.executeOperation(mapCheckInputParameters);
        if (validityChecker.getAXLMessages(MessageType.ERROR).size() > 0) {
            System.err.println("ValidityChecker reported the errors above.");
            return null;
        }
        return axlEditorElement;
    }

    private EObject createInitialModel() {
        EAXML eaXML = ElementsFactory.eINSTANCE.createEAXML();
        return eaXML;
    }

    /**
	 * find a EAXML object in a resource set.<br>
	 * - handling all sorts of unexpected cases.
	 * @param resourceSet the ResourceSet to scan
	 * @return the (one and only allowed) EAXML
	 */
    private EAXML findEAXML(ResourceSet resourceSet) {
        if (resourceSet.getResources().size() > 1) {
            System.err.println("AxlToEastWizard: Synchronization of multiple Resources is not supported yet.");
            return null;
        }
        for (Resource r : resourceSet.getResources()) {
            if (r.getContents().size() > 1) {
                System.err.println("AxlToEastWizard: Synchronization does not allow multiple Objects in one Resource yet.");
                return null;
            }
            for (EObject eObject : r.getContents()) {
                if (eObject instanceof EAXML) {
                    return (EAXML) eObject;
                } else {
                    System.err.println("AxlToEastWizard: Synchronization does not allow other Objects than EAXML in a Resource yet.");
                    return null;
                }
            }
        }
        System.err.println("AxlToEastWizard: Could not find an EAXML Object in the ResourceSet.");
        return null;
    }

    /**
	 * generated method from de.fraunhofer.isst.eastadl.editor.elements.presentation.ElementsModelWizard<br>
	 *          from Project de.fraunhofer.isst.eastadl.editor<br>
	 *          of EMF Model de.fraunhofer.isst.eastadl/model/east.genmodel<br>
	 *          from Project de.fraunhofer.isst.eastadl
	 */
    @Override
    public boolean performFinish() {
        try {
            final IFile eastModelFile = getModelFile();
            try {
                ResourceSet resourceSet = new ResourceSetImpl();
                URI fileURI = URI.createPlatformResourceURI(eastModelFile.getFullPath().toString(), true);
                Resource resource = resourceSet.createResource(fileURI);
                resource.save(null);
            } catch (Exception exception) {
                EastEditorPlugin.INSTANCE.log(exception);
            }
            IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
            IWorkbenchPage page = workbenchWindow.getActivePage();
            final IWorkbenchPart activePart = page.getActivePart();
            if (activePart instanceof ISetSelectionTarget) {
                final ISelection targetSelection = new StructuredSelection(eastModelFile);
                getShell().getDisplay().asyncExec(new Runnable() {

                    public void run() {
                        ((ISetSelectionTarget) activePart).selectReveal(targetSelection);
                    }
                });
            }
            try {
                String eastModelPath = eastModelFile.getFullPath().toString();
                IEditorDescriptor editor = workbench.getEditorRegistry().getDefaultEditor(eastModelPath);
                IEditorPart iEditorPart = page.openEditor(new FileEditorInput(eastModelFile), editor.getId());
                ElementsEditor elementsEditor = (ElementsEditor) iEditorPart;
                ResourceSet resourceSet = elementsEditor.getEditingDomain().getResourceSet();
                Env env = new Env();
                env.synchronizedExport(axlModel, resourceSet);
            } catch (PartInitException exception) {
                MessageDialog.openError(workbenchWindow.getShell(), EastEditorPlugin.INSTANCE.getString("_UI_OpenEditorError_label"), exception.getMessage());
                return false;
            }
            System.err.println("AxlToEastWizard: done.");
            return true;
        } catch (Exception exception) {
            EastEditorPlugin.INSTANCE.log(exception);
            return false;
        }
    }

    /**
	 * This is the one page of the wizard.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public class ElementsModelWizardNewFileCreationPage extends WizardNewFileCreationPage {

        /**
		 * Pass in the selection.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        public ElementsModelWizardNewFileCreationPage(String pageId, IStructuredSelection selection) {
            super(pageId, selection);
        }

        /**
		 * The framework calls this to see if the file is correct.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        @Override
        protected boolean validatePage() {
            if (super.validatePage()) {
                String extension = new Path(getFileName()).getFileExtension();
                if (extension == null || !FILE_EXTENSIONS.contains(extension)) {
                    String key = FILE_EXTENSIONS.size() > 1 ? "_WARN_FilenameExtensions" : "_WARN_FilenameExtension";
                    setErrorMessage(EastEditorPlugin.INSTANCE.getString(key, new Object[] { FORMATTED_FILE_EXTENSIONS }));
                    return false;
                }
                return true;
            }
            return false;
        }

        /**
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
        public IFile getModelFile() {
            return ResourcesPlugin.getWorkspace().getRoot().getFile(getContainerFullPath().append(getFileName()));
        }
    }

    /**
	 * Get the file from the page.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IFile getModelFile() {
        return newFileCreationPage.getModelFile();
    }
}
