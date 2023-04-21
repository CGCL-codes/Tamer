package org.eclipse.uml2.uml.editor.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.celleditor.FeatureEditorDialog;
import org.eclipse.jface.action.IAction;
import org.eclipse.uml2.common.edit.command.ChangeCommand;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.editor.UMLEditorPlugin;
import org.eclipse.uml2.uml.resource.UMLResource;

public class ReferenceMetaclassAction extends UMLCommandAction {

    public ReferenceMetaclassAction() {
        super();
    }

    @Override
    protected Command createActionCommand(EditingDomain editingDomain, Collection<?> collection) {
        if (collection.size() == 1 && collection.iterator().next() instanceof Profile) {
            return IdentityCommand.INSTANCE;
        }
        return UnexecutableCommand.INSTANCE;
    }

    @Override
    public void run(IAction action) {
        if (command != UnexecutableCommand.INSTANCE) {
            final Profile profile = (Profile) collection.iterator().next();
            EList<org.eclipse.uml2.uml.Class> referencedMetaclasses = profile.getReferencedMetaclasses();
            List<org.eclipse.uml2.uml.Class> choiceOfValues = new ArrayList<org.eclipse.uml2.uml.Class>();
            try {
                ResourceSet resourceSet = profile.eResource().getResourceSet();
                try {
                    resourceSet.getResource(URI.createURI(UMLResource.UML_METAMODEL_URI), true);
                } catch (Exception e) {
                }
                for (Resource resource : resourceSet.getResources()) {
                    for (TreeIterator<EObject> contents = resource.getAllContents(); contents.hasNext(); ) {
                        EObject eObject = contents.next();
                        if (eObject instanceof org.eclipse.uml2.uml.Class) {
                            org.eclipse.uml2.uml.Class clazz = (org.eclipse.uml2.uml.Class) eObject;
                            if (clazz.isMetaclass() && !referencedMetaclasses.contains(clazz)) {
                                choiceOfValues.add(clazz);
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
            Collections.<org.eclipse.uml2.uml.Class>sort(choiceOfValues, new TextComparator<org.eclipse.uml2.uml.Class>());
            String label = UMLEditorPlugin.INSTANCE.getString("_UI_ReferenceMetaclassActionCommand_label");
            final FeatureEditorDialog dialog = new FeatureEditorDialog(workbenchPart.getSite().getShell(), getLabelProvider(), profile, UMLPackage.Literals.CLASS, Collections.EMPTY_LIST, label, choiceOfValues);
            dialog.open();
            if (dialog.getReturnCode() == FeatureEditorDialog.OK) {
                editingDomain.getCommandStack().execute(new ChangeCommand(editingDomain, new Runnable() {

                    public void run() {
                        for (Object result : dialog.getResult()) {
                            profile.createMetaclassReference((org.eclipse.uml2.uml.Class) result);
                        }
                    }
                }, label));
            }
        }
    }
}
