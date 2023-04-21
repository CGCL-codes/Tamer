package org.wsmostudio.ui.views.navigator.actions;

import java.util.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.factory.WsmoFactory;
import org.wsmostudio.runtime.LogManager;
import org.wsmostudio.runtime.WSMORuntime;
import org.wsmostudio.ui.IdentifierInputDialog;
import org.wsmostudio.ui.WsmoUIPlugin;
import org.wsmostudio.ui.editors.model.ObservableModel;
import org.wsmostudio.ui.views.navigator.WSMONavigator;

public class RenameEntityAction extends AbstractAction {

    public static final String RENAME_NO_WARNING_PROP = "RenameActionWarn";

    WsmoFactory factory = WSMORuntime.getRuntime().getWsmoFactory();

    public void run() {
        if (false == WsmoUIPlugin.getDefault().getPreferenceStore().getBoolean(RENAME_NO_WARNING_PROP)) {
            MessageDialogWithToggle dialog = MessageDialogWithToggle.openOkCancelConfirm(navigator.getSite().getShell(), "Rename/Replace Entity", "Warning! This operation replaces the selected entity with" + " a duplicated one having the new identifier. " + "This might introduce graph reference inconsistencies." + "\n\nDo you want to proceed with the operation ?", " Do not show this warning in future", false, WsmoUIPlugin.getDefault().getPreferenceStore(), RENAME_NO_WARNING_PROP);
            WsmoUIPlugin.getDefault().getPreferenceStore().setValue(RENAME_NO_WARNING_PROP, dialog.getToggleState());
            if (dialog.getReturnCode() != Window.OK) {
                return;
            }
        }
        Entity selected = (Entity) ((IStructuredSelection) navigator.getTree().getSelection()).getFirstElement();
        TopEntity nsHolder = findNSHolder(selected);
        IdentifierInputDialog iDialog = new IdentifierInputDialog(navigator.getSite().getShell(), "New Identifier", "Identifier: ", selected.getIdentifier().toString(), nsHolder, factory, selected instanceof Axiom || selected instanceof Instance || selected instanceof RelationInstance);
        if (iDialog.open() != Window.OK) {
            return;
        }
        Identifier id = iDialog.getIdentifier();
        if (id == null || selected.getIdentifier().equals(id)) {
            return;
        }
        boolean expState = navigator.getTree().getExpandedState(selected);
        Entity result = null;
        Set<Entity> affectedEntities = new HashSet<Entity>();
        if (selected instanceof Concept) {
            result = renameconcept((Concept) selected, id, affectedEntities);
        } else if (selected instanceof Axiom) {
            result = renameAxiom((Axiom) selected, id);
        } else if (selected instanceof Relation) {
            result = renameRelation((Relation) selected, id, affectedEntities);
        } else if (selected instanceof Instance) {
            result = renameInstance((Instance) selected, id, affectedEntities);
        } else if (selected instanceof RelationInstance) {
            result = renameRelationInstance((RelationInstance) selected, id, affectedEntities);
        } else {
            MessageDialog.openError(navigator.getSite().getShell(), "Unsupported Operation", "The rename operation is not supported for this type of entities!");
            return;
        }
        if (navigator.getWsmoInput().getAdapter(ObservableModel.class) != null) {
            ((ObservableModel) navigator.getWsmoInput().getAdapter(ObservableModel.class)).setChanged();
        }
        navigator.ensureEditorForEntityIsClosed(selected, false);
        for (Entity affected : affectedEntities) {
            navigator.fireEntityChanged(affected);
            navigator.getTree().refresh(affected, true);
        }
        navigator.getTree().setExpandedState(result, expState);
        navigator.getTree().setSelection(new StructuredSelection(result));
        WSMONavigator.propsViewRefresh();
    }

    private Concept renameconcept(Concept oldConcept, Identifier newID, Set<Entity> affectedEntities) {
        Concept result = factory.createConcept(newID);
        copyNFPs(oldConcept, result);
        try {
            result.setOntology(oldConcept.getOntology());
            oldConcept.setOntology(null);
            Set<Concept> superConcepts = new HashSet<Concept>(oldConcept.listSuperConcepts());
            for (Concept superConcept : superConcepts) {
                oldConcept.removeSuperConcept(superConcept);
                result.addSuperConcept(superConcept);
                affectedEntities.add(superConcept);
            }
            Set<Concept> subConcepts = new HashSet<Concept>(oldConcept.listSubConcepts());
            for (Concept subConcept : subConcepts) {
                oldConcept.removeSubConcept(subConcept);
                result.addSubConcept(subConcept);
                affectedEntities.add(subConcept);
            }
            Set<Attribute> attrs = new HashSet<Attribute>(oldConcept.listAttributes());
            for (Attribute attr : attrs) {
                oldConcept.removeAttribute(attr);
                cloneAttribute(attr, result);
                affectedEntities.add(attr);
            }
            Set<Instance> instances = new HashSet<Instance>(oldConcept.listInstances());
            for (Instance inst : instances) {
                oldConcept.removeInstance(inst);
                result.addInstance(inst);
                affectedEntities.add(inst);
            }
        } catch (Throwable ime) {
            LogManager.logError(ime);
        }
        return result;
    }

    private Axiom renameAxiom(Axiom oldAxiom, Identifier newID) {
        Axiom result = factory.createAxiom(newID);
        copyNFPs(oldAxiom, result);
        try {
            result.setOntology(oldAxiom.getOntology());
            oldAxiom.setOntology(null);
            Set<LogicalExpression> LEs = new HashSet<LogicalExpression>(oldAxiom.listDefinitions());
            for (LogicalExpression le : LEs) {
                oldAxiom.removeDefinition(le);
                result.addDefinition(le);
            }
        } catch (Throwable ime) {
            LogManager.logError(ime);
        }
        return result;
    }

    private Relation renameRelation(Relation oldRelation, Identifier newID, Set<Entity> affectedEntities) {
        Relation result = factory.createRelation(newID);
        copyNFPs(oldRelation, result);
        try {
            result.setOntology(oldRelation.getOntology());
            oldRelation.setOntology(null);
            Set<Relation> superRels = new HashSet<Relation>(oldRelation.listSuperRelations());
            for (Relation superRel : superRels) {
                oldRelation.removeSuperRelation(superRel);
                result.addSuperRelation(superRel);
                affectedEntities.add(superRel);
            }
            Set<Relation> subRels = new HashSet<Relation>(oldRelation.listSubRelations());
            for (Relation subRelation : subRels) {
                oldRelation.removeSubRelation(subRelation);
                result.addSubRelation(subRelation);
                affectedEntities.add(subRelation);
            }
            List<Parameter> params = new LinkedList<Parameter>(oldRelation.listParameters());
            byte pos = 0;
            for (Parameter param : params) {
                Parameter newParam = result.createParameter(pos++);
                cloneParam(param, newParam);
            }
            Set<RelationInstance> instances = new HashSet<RelationInstance>(oldRelation.listRelationInstances());
            for (RelationInstance inst : instances) {
                oldRelation.removeRelationInstance(inst);
                result.addRelationInstance(inst);
                affectedEntities.add(inst);
            }
        } catch (Throwable ime) {
            LogManager.logError(ime);
        }
        return result;
    }

    private Instance renameInstance(Instance oldInstance, Identifier newID, Set<Entity> affectedEntities) {
        Instance result = factory.createInstance(newID);
        copyNFPs(oldInstance, result);
        try {
            result.setOntology(oldInstance.getOntology());
            oldInstance.setOntology(null);
            Set<Concept> superConcepts = new HashSet<Concept>(oldInstance.listConcepts());
            for (Concept superConcept : superConcepts) {
                oldInstance.removeConcept(superConcept);
                result.addConcept(superConcept);
                affectedEntities.add(superConcept);
            }
            Map<Identifier, Set<Value>> attrVals = new HashMap<Identifier, Set<Value>>(oldInstance.listAttributeValues());
            for (Identifier attrID : attrVals.keySet()) {
                Set<Value> values = new HashSet<Value>(oldInstance.listAttributeValues(attrID));
                for (Value value : values) {
                    oldInstance.removeAttributeValue(attrID, value);
                    result.addAttributeValue(attrID, value);
                }
            }
        } catch (Throwable ime) {
            LogManager.logError(ime);
        }
        return result;
    }

    private RelationInstance renameRelationInstance(RelationInstance oldInstance, Identifier newID, Set<Entity> affectedEntities) {
        RelationInstance result = null;
        try {
            result = factory.createRelationInstance(newID, oldInstance.getRelation());
        } catch (InvalidModelException ime) {
            LogManager.logError(ime);
            return null;
        }
        copyNFPs(oldInstance, result);
        affectedEntities.add(oldInstance.getRelation());
        try {
            result.setOntology(oldInstance.getOntology());
            oldInstance.setOntology(null);
            byte pos = 0;
            for (Value value : oldInstance.listParameterValues()) {
                result.setParameterValue(pos++, value);
            }
            oldInstance.getRelation().removeRelationInstance(oldInstance);
        } catch (Throwable ime) {
            LogManager.logError(ime);
        }
        return result;
    }

    private TopEntity findNSHolder(Entity entity) {
        if (entity instanceof OntologyElement) {
            return ((OntologyElement) entity).getOntology();
        }
        return null;
    }

    private void copyNFPs(Entity source, Entity target) {
        Map<IRI, Set<Object>> nfps = source.listNFPValues();
        for (Iterator<?> it = nfps.keySet().iterator(); it.hasNext(); ) {
            IRI key = (IRI) it.next();
            for (Object val : nfps.get(key)) {
                try {
                    if (val instanceof Identifier) {
                        target.addNFPValue(key, (Identifier) val);
                    } else {
                        target.addNFPValue(key, (Value) val);
                    }
                } catch (InvalidModelException ime) {
                    LogManager.logError(ime);
                }
            }
        }
    }

    private Attribute cloneAttribute(Attribute ref, Concept concept) throws InvalidModelException {
        Attribute attr = concept.createAttribute(ref.getIdentifier());
        copyNFPs(ref, attr);
        for (Type type : ref.listTypes()) {
            attr.addType(type);
        }
        attr.setConstraining(ref.isConstraining());
        attr.setInverseOf(ref.getInverseOf());
        attr.setMaxCardinality(ref.getMaxCardinality());
        attr.setMinCardinality(ref.getMinCardinality());
        attr.setReflexive(ref.isReflexive());
        attr.setSymmetric(ref.isSymmetric());
        attr.setTransitive(ref.isTransitive());
        return attr;
    }

    private void cloneParam(Parameter param, Parameter newParam) throws InvalidModelException {
        newParam.setConstraining(param.isConstraining());
        for (Type type : param.listTypes()) {
            newParam.addType(type);
        }
    }
}
