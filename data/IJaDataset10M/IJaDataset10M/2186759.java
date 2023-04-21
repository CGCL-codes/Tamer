package de.uka.aifb.owl.odm.module.seperator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.semanticweb.kaon2.api.KAON2Exception;
import org.semanticweb.kaon2.api.owl.elements.DataProperty;
import org.semanticweb.kaon2.api.owl.elements.Individual;
import org.semanticweb.kaon2.api.owl.elements.OWLClass;
import org.semanticweb.kaon2.api.owl.elements.OWLEntity;
import org.semanticweb.kaon2.api.owl.elements.ObjectProperty;
import com.ontoprise.ontostudio.datamodel.exception.ControlException;
import com.ontoprise.ontostudio.gui.commands.concept.ProjectOntologyChangeCommand;
import com.ontoprise.ontostudio.gui.commands.ontology.CreateOntology;
import com.ontoprise.ontostudio.gui.control.Control;
import com.ontoprise.ontostudio.owl.gui.navigator.AbstractOwlEntityTreeElement;
import com.ontoprise.ontostudio.owl.model.OWLModel;
import com.ontoprise.ontostudio.owl.model.OWLModelFactory;
import de.uka.aifb.owl.odm.module.seperator.commands.SeparateCommand;

public abstract class ModuleSeparatorImpl implements ModuleSeparator {

    protected IStructuredSelection selection;

    protected List<ProjectOntologyChangeCommand> commands = new ArrayList<ProjectOntologyChangeCommand>();

    private String projectName;

    private String ontologyId;

    protected ModuleSeparatorImpl(IStructuredSelection selection, String moduleUri, String projectName) throws ControlException {
        this.ontologyId = moduleUri;
        this.projectName = projectName;
        this.selection = selection;
    }

    ;

    public static ModuleSeparator getInstance(IStructuredSelection selection, String moduleUri, String projectName) throws ControlException {
        if (selection.getFirstElement() instanceof AbstractOwlEntityTreeElement) return new ModuleSeparatorTreeElement(selection, moduleUri, projectName);
        if (selection.getFirstElement() instanceof ShapeNodeEditPart) return new ModuleSeparatorDiagramElement(selection, moduleUri, projectName);
        if (selection.getFirstElement() instanceof EObject) return new ModuleSeparatorEObject(selection, moduleUri, projectName);
        return null;
    }

    public void createModule(ModuleSeparationConfig config, IProgressMonitor monitor) {
        Date currentTime = new Date();
        if (monitor == null) monitor = new NullProgressMonitor();
        monitor.beginTask("Extract module", IProgressMonitor.UNKNOWN);
        try {
            if (OWLModelFactory.getOntology(ontologyId, projectName) == null) {
                monitor.subTask("Create new module");
                commands.add(createNewModulCommand());
            }
            monitor.subTask("Extract axioms");
            addCommandsForSelection(config);
            executeCommands(config, monitor);
        } catch (ControlException e) {
            e.printStackTrace();
        } catch (KAON2Exception e) {
            e.printStackTrace();
        }
        String result = "\n\nModule created in " + (new Date().getTime() - currentTime.getTime()) + " ms \n" + "with " + selection.size() + " start element";
        try {
            OWLModel toModel = OWLModelFactory.getOWLModel(ontologyId, projectName);
            result += "\n\nCreated Module" + "\nuri: " + ontologyId + "\nclasses: " + toModel.getAllClasses().size() + "\nindividuals: " + countIndividuals(toModel) + "\nObjectProperties: " + toModel.getAllObjectProperties().size() + "\nDataProperties: " + toModel.getAllDataProperties().size() + "\noverall axiome: " + toModel.getOntology().createAxiomRequest().getAll().size();
            OWLModel fromModel = OWLModelFactory.getOWLModel(getOldOnotlogy(selection.getFirstElement()), getProjectName());
            result += "\n\nSource Ontology" + "\nuri: " + getOldOnotlogy(selection.getFirstElement()) + "\nclasses: " + fromModel.getAllClasses().size() + "\nindividuals: " + countIndividuals(fromModel) + "\nObjectProperties: " + fromModel.getAllObjectProperties().size() + "\nDataProperties: " + fromModel.getAllDataProperties().size() + "\noverall axiome: " + fromModel.getOntology().createAxiomRequest().getAll().size();
            System.out.println(result);
        } catch (KAON2Exception e) {
            e.printStackTrace();
        }
    }

    private int countIndividuals(OWLModel model) throws KAON2Exception {
        int result = 0;
        for (OWLClass owlClass : model.getAllClasses()) {
            result += model.getAllIndividuals(owlClass.getURI()).size();
        }
        return result;
    }

    private void addCommandsForSelection(ModuleSeparationConfig config) throws KAON2Exception {
        List<OWLEntity> selectedProperties = new ArrayList<OWLEntity>();
        Map<OWLEntity, String> startItems = new HashMap<OWLEntity, String>();
        for (Iterator iter = selection.iterator(); iter.hasNext(); ) {
            Object item = iter.next();
            OWLEntity entity = getEntityFromOldOntology(item);
            if (entity instanceof ObjectProperty || entity instanceof DataProperty) {
                selectedProperties.add(entity);
            } else if (entity instanceof OWLClass || entity instanceof Individual) {
                startItems.put(entity, getOldOnotlogy(item));
            }
        }
        if (config.isIncludeAllObjectProperties() && !startItems.isEmpty()) {
            selectedProperties.clear();
            OWLModel model = OWLModelFactory.getOWLModel(startItems.values().iterator().next(), projectName);
            selectedProperties.addAll(model.getAllObjectProperties());
        }
        for (OWLEntity startItem : startItems.keySet()) {
            ProjectOntologyChangeCommand addCmd = createAddEntityToModuleCommand(startItem, startItems.get(startItem), selectedProperties, config);
            if (addCmd != null) commands.add(addCmd);
        }
    }

    protected void executeCommands(ModuleSeparationConfig config, IProgressMonitor monitor) throws ControlException {
        int worked = 0;
        for (ProjectOntologyChangeCommand command : commands) {
            command.run();
            config.resetTraversalCount();
            monitor.setTaskName((++worked) + " start elements extracted");
        }
    }

    protected ProjectOntologyChangeCommand createNewModulCommand() throws ControlException {
        String moduleId = Control.getValidModuleIdentifier(ontologyId).toString();
        return new CreateOntology(projectName, moduleId, ontologyId.substring(0, ontologyId.indexOf("#") + 1), "");
    }

    protected abstract OWLEntity getEntityFromOldOntology(Object selectionItem) throws KAON2Exception;

    protected abstract String getOldOnotlogy(Object selectionItem);

    protected ProjectOntologyChangeCommand createAddEntityToModuleCommand(OWLEntity entity, String ontology, List<OWLEntity> selectedProperties, ModuleSeparationConfig config) throws KAON2Exception {
        return SeparateCommand.getInstance(getProjectName(), getOntologyId(), entity, ontology, selectedProperties, config);
    }

    public String getOntologyId() {
        return ontologyId;
    }

    public String getProjectName() {
        return projectName;
    }
}
