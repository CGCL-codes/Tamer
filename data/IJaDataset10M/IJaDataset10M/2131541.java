package odm.diagram.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import odm.diagram.edit.parts.ConstantEditPart;
import odm.diagram.edit.parts.DataPropertyEditPart;
import odm.diagram.edit.parts.DataRangeEditPart;
import odm.diagram.edit.parts.IndividualEditPart;
import odm.diagram.edit.parts.MainOntologyEditPart;
import odm.diagram.edit.parts.OWLClassEditPart;
import odm.diagram.edit.parts.OWLEditPart;
import odm.diagram.edit.parts.ObjectPropertyEditPart;
import odm.diagram.edit.parts.OtherOntologyEditPart;
import odm.diagram.part.OdmDiagramEditorPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantProvider;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * @generated
 */
public class OdmModelingAssistantProvider extends ModelingAssistantProvider {

    /**
	 * @generated
	 */
    public List getTypesForPopupBar(IAdaptable host) {
        IGraphicalEditPart editPart = (IGraphicalEditPart) host.getAdapter(IGraphicalEditPart.class);
        if (editPart instanceof OWLClassEditPart) {
            List types = new ArrayList();
            types.add(OdmElementTypes.ClassAttribute_3001);
            return types;
        }
        if (editPart instanceof IndividualEditPart) {
            List types = new ArrayList();
            types.add(OdmElementTypes.IndividualAttribute_3002);
            return types;
        }
        if (editPart instanceof OWLEditPart) {
            List types = new ArrayList();
            types.add(OdmElementTypes.OWLClass_2001);
            types.add(OdmElementTypes.DataProperty_2002);
            types.add(OdmElementTypes.ObjectProperty_2003);
            types.add(OdmElementTypes.MainOntology_2004);
            types.add(OdmElementTypes.OtherOntology_2005);
            types.add(OdmElementTypes.DataRange_2006);
            types.add(OdmElementTypes.Annotation_2007);
            types.add(OdmElementTypes.Individual_2008);
            types.add(OdmElementTypes.Constant_2009);
            return types;
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getRelTypesOnSource(IAdaptable source) {
        IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
        if (sourceEditPart instanceof MainOntologyEditPart) {
            List types = new ArrayList();
            types.add(OdmElementTypes.Imports_4001);
            return types;
        }
        if (sourceEditPart instanceof DataRangeEditPart) {
            List types = new ArrayList();
            types.add(OdmElementTypes.DataComplementOf_4006);
            types.add(OdmElementTypes.DataOneOf_4030);
            types.add(OdmElementTypes.DataMinCardinality_4042);
            types.add(OdmElementTypes.DataMaxCardinality_4043);
            types.add(OdmElementTypes.DataExactCardinality_4044);
            return types;
        }
        if (sourceEditPart instanceof IndividualEditPart) {
            List types = new ArrayList();
            types.add(OdmElementTypes.SameIndividual_4010);
            types.add(OdmElementTypes.DifferentIndividuals_4011);
            types.add(OdmElementTypes.ClassAssertion_4039);
            types.add(OdmElementTypes.PropertyAssertion_4045);
            return types;
        }
        if (sourceEditPart instanceof DataPropertyEditPart) {
            List types = new ArrayList();
            types.add(OdmElementTypes.DataAllValuesFrom_4012);
            types.add(OdmElementTypes.DataSomeValuesFrom_4013);
            types.add(OdmElementTypes.FunctionalDataProperty_4022);
            types.add(OdmElementTypes.SubDataPropertyOf_4023);
            types.add(OdmElementTypes.DataHasValue_4029);
            types.add(OdmElementTypes.EquivalentDataProperty_4034);
            types.add(OdmElementTypes.DisjointDataProperty_4036);
            return types;
        }
        if (sourceEditPart instanceof ObjectPropertyEditPart) {
            List types = new ArrayList();
            types.add(OdmElementTypes.InverseObjectProperty_4018);
            types.add(OdmElementTypes.FunctionalObjectProperty_4019);
            types.add(OdmElementTypes.InverseFunctionalObjectProperty_4020);
            types.add(OdmElementTypes.SubObjectPropertyOf_4021);
            types.add(OdmElementTypes.SymmetricObjectProperty_4024);
            types.add(OdmElementTypes.ReflexiveObjectProperty_4025);
            types.add(OdmElementTypes.ObjectHasValue_4028);
            types.add(OdmElementTypes.EquivalentObjectProperty_4033);
            types.add(OdmElementTypes.DisjointObjectProperty_4035);
            types.add(OdmElementTypes.AntisymmetricObjectProperty_4037);
            types.add(OdmElementTypes.TransitiveObjectProperty_4038);
            return types;
        }
        if (sourceEditPart instanceof OWLClassEditPart) {
            List types = new ArrayList();
            types.add(OdmElementTypes.ObjectPropertyLink_4046);
            types.add(OdmElementTypes.DataPropertyLink_4047);
            return types;
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getRelTypesOnTarget(IAdaptable target) {
        IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
        if (targetEditPart instanceof OtherOntologyEditPart) {
            List types = new ArrayList();
            types.add(OdmElementTypes.Imports_4001);
            return types;
        }
        if (targetEditPart instanceof DataRangeEditPart) {
            List types = new ArrayList();
            types.add(OdmElementTypes.DataComplementOf_4006);
            types.add(OdmElementTypes.DataAllValuesFrom_4012);
            types.add(OdmElementTypes.DataSomeValuesFrom_4013);
            types.add(OdmElementTypes.DataPropertyAssoziation_4015);
            types.add(OdmElementTypes.DataPropertyLink_4047);
            return types;
        }
        if (targetEditPart instanceof IndividualEditPart) {
            List types = new ArrayList();
            types.add(OdmElementTypes.ObjectOneOf_4009);
            types.add(OdmElementTypes.SameIndividual_4010);
            types.add(OdmElementTypes.DifferentIndividuals_4011);
            types.add(OdmElementTypes.ObjectHasValue_4028);
            return types;
        }
        if (targetEditPart instanceof ObjectPropertyEditPart) {
            List types = new ArrayList();
            types.add(OdmElementTypes.InverseObjectProperty_4018);
            types.add(OdmElementTypes.FunctionalObjectProperty_4019);
            types.add(OdmElementTypes.InverseFunctionalObjectProperty_4020);
            types.add(OdmElementTypes.SubObjectPropertyOf_4021);
            types.add(OdmElementTypes.SymmetricObjectProperty_4024);
            types.add(OdmElementTypes.ReflexiveObjectProperty_4025);
            types.add(OdmElementTypes.ObjectExistsSelf_4031);
            types.add(OdmElementTypes.EquivalentObjectProperty_4033);
            types.add(OdmElementTypes.DisjointObjectProperty_4035);
            types.add(OdmElementTypes.AntisymmetricObjectProperty_4037);
            types.add(OdmElementTypes.TransitiveObjectProperty_4038);
            return types;
        }
        if (targetEditPart instanceof DataPropertyEditPart) {
            List types = new ArrayList();
            types.add(OdmElementTypes.FunctionalDataProperty_4022);
            types.add(OdmElementTypes.SubDataPropertyOf_4023);
            types.add(OdmElementTypes.EquivalentDataProperty_4034);
            types.add(OdmElementTypes.DisjointDataProperty_4036);
            return types;
        }
        if (targetEditPart instanceof ConstantEditPart) {
            List types = new ArrayList();
            types.add(OdmElementTypes.DataHasValue_4029);
            types.add(OdmElementTypes.DataOneOf_4030);
            return types;
        }
        if (targetEditPart instanceof OWLClassEditPart) {
            List types = new ArrayList();
            types.add(OdmElementTypes.ClassAssertion_4039);
            types.add(OdmElementTypes.ObjectPropertyLink_4046);
            return types;
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getRelTypesOnSourceAndTarget(IAdaptable source, IAdaptable target) {
        IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
        IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
        if (sourceEditPart instanceof MainOntologyEditPart) {
            List types = new ArrayList();
            if (targetEditPart instanceof OtherOntologyEditPart) {
                types.add(OdmElementTypes.Imports_4001);
            }
            return types;
        }
        if (sourceEditPart instanceof DataRangeEditPart) {
            List types = new ArrayList();
            if (targetEditPart instanceof DataRangeEditPart) {
                types.add(OdmElementTypes.DataComplementOf_4006);
            }
            if (targetEditPart instanceof ConstantEditPart) {
                types.add(OdmElementTypes.DataOneOf_4030);
            }
            return types;
        }
        if (sourceEditPart instanceof IndividualEditPart) {
            List types = new ArrayList();
            if (targetEditPart instanceof IndividualEditPart) {
                types.add(OdmElementTypes.SameIndividual_4010);
            }
            if (targetEditPart instanceof IndividualEditPart) {
                types.add(OdmElementTypes.DifferentIndividuals_4011);
            }
            if (targetEditPart instanceof OWLClassEditPart) {
                types.add(OdmElementTypes.ClassAssertion_4039);
            }
            return types;
        }
        if (sourceEditPart instanceof DataPropertyEditPart) {
            List types = new ArrayList();
            if (targetEditPart instanceof DataRangeEditPart) {
                types.add(OdmElementTypes.DataAllValuesFrom_4012);
            }
            if (targetEditPart instanceof DataRangeEditPart) {
                types.add(OdmElementTypes.DataSomeValuesFrom_4013);
            }
            if (targetEditPart instanceof DataPropertyEditPart) {
                types.add(OdmElementTypes.FunctionalDataProperty_4022);
            }
            if (targetEditPart instanceof DataPropertyEditPart) {
                types.add(OdmElementTypes.SubDataPropertyOf_4023);
            }
            if (targetEditPart instanceof ConstantEditPart) {
                types.add(OdmElementTypes.DataHasValue_4029);
            }
            if (targetEditPart instanceof DataPropertyEditPart) {
                types.add(OdmElementTypes.EquivalentDataProperty_4034);
            }
            if (targetEditPart instanceof DataPropertyEditPart) {
                types.add(OdmElementTypes.DisjointDataProperty_4036);
            }
            return types;
        }
        if (sourceEditPart instanceof ObjectPropertyEditPart) {
            List types = new ArrayList();
            if (targetEditPart instanceof ObjectPropertyEditPart) {
                types.add(OdmElementTypes.InverseObjectProperty_4018);
            }
            if (targetEditPart instanceof ObjectPropertyEditPart) {
                types.add(OdmElementTypes.FunctionalObjectProperty_4019);
            }
            if (targetEditPart instanceof ObjectPropertyEditPart) {
                types.add(OdmElementTypes.InverseFunctionalObjectProperty_4020);
            }
            if (targetEditPart instanceof ObjectPropertyEditPart) {
                types.add(OdmElementTypes.SubObjectPropertyOf_4021);
            }
            if (targetEditPart instanceof ObjectPropertyEditPart) {
                types.add(OdmElementTypes.SymmetricObjectProperty_4024);
            }
            if (targetEditPart instanceof ObjectPropertyEditPart) {
                types.add(OdmElementTypes.ReflexiveObjectProperty_4025);
            }
            if (targetEditPart instanceof IndividualEditPart) {
                types.add(OdmElementTypes.ObjectHasValue_4028);
            }
            if (targetEditPart instanceof ObjectPropertyEditPart) {
                types.add(OdmElementTypes.EquivalentObjectProperty_4033);
            }
            if (targetEditPart instanceof ObjectPropertyEditPart) {
                types.add(OdmElementTypes.DisjointObjectProperty_4035);
            }
            if (targetEditPart instanceof ObjectPropertyEditPart) {
                types.add(OdmElementTypes.AntisymmetricObjectProperty_4037);
            }
            if (targetEditPart instanceof ObjectPropertyEditPart) {
                types.add(OdmElementTypes.TransitiveObjectProperty_4038);
            }
            return types;
        }
        if (sourceEditPart instanceof OWLClassEditPart) {
            List types = new ArrayList();
            if (targetEditPart instanceof OWLClassEditPart) {
                types.add(OdmElementTypes.ObjectPropertyLink_4046);
            }
            if (targetEditPart instanceof DataRangeEditPart) {
                types.add(OdmElementTypes.DataPropertyLink_4047);
            }
            return types;
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getTypesForSource(IAdaptable target, IElementType relationshipType) {
        IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
        if (targetEditPart instanceof OtherOntologyEditPart) {
            List types = new ArrayList();
            if (relationshipType == OdmElementTypes.Imports_4001) {
                types.add(OdmElementTypes.MainOntology_2004);
            }
            return types;
        }
        if (targetEditPart instanceof DataRangeEditPart) {
            List types = new ArrayList();
            if (relationshipType == OdmElementTypes.DataComplementOf_4006) {
                types.add(OdmElementTypes.DataRange_2006);
            }
            if (relationshipType == OdmElementTypes.DataAllValuesFrom_4012) {
                types.add(OdmElementTypes.DataProperty_2002);
            }
            if (relationshipType == OdmElementTypes.DataSomeValuesFrom_4013) {
                types.add(OdmElementTypes.DataProperty_2002);
            }
            if (relationshipType == OdmElementTypes.DataPropertyLink_4047) {
                types.add(OdmElementTypes.OWLClass_2001);
            }
            return types;
        }
        if (targetEditPart instanceof IndividualEditPart) {
            List types = new ArrayList();
            if (relationshipType == OdmElementTypes.SameIndividual_4010) {
                types.add(OdmElementTypes.Individual_2008);
            }
            if (relationshipType == OdmElementTypes.DifferentIndividuals_4011) {
                types.add(OdmElementTypes.Individual_2008);
            }
            if (relationshipType == OdmElementTypes.ObjectHasValue_4028) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            return types;
        }
        if (targetEditPart instanceof ObjectPropertyEditPart) {
            List types = new ArrayList();
            if (relationshipType == OdmElementTypes.InverseObjectProperty_4018) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            if (relationshipType == OdmElementTypes.FunctionalObjectProperty_4019) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            if (relationshipType == OdmElementTypes.InverseFunctionalObjectProperty_4020) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            if (relationshipType == OdmElementTypes.SubObjectPropertyOf_4021) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            if (relationshipType == OdmElementTypes.SymmetricObjectProperty_4024) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            if (relationshipType == OdmElementTypes.ReflexiveObjectProperty_4025) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            if (relationshipType == OdmElementTypes.EquivalentObjectProperty_4033) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            if (relationshipType == OdmElementTypes.DisjointObjectProperty_4035) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            if (relationshipType == OdmElementTypes.AntisymmetricObjectProperty_4037) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            if (relationshipType == OdmElementTypes.TransitiveObjectProperty_4038) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            return types;
        }
        if (targetEditPart instanceof DataPropertyEditPart) {
            List types = new ArrayList();
            if (relationshipType == OdmElementTypes.FunctionalDataProperty_4022) {
                types.add(OdmElementTypes.DataProperty_2002);
            }
            if (relationshipType == OdmElementTypes.SubDataPropertyOf_4023) {
                types.add(OdmElementTypes.DataProperty_2002);
            }
            if (relationshipType == OdmElementTypes.EquivalentDataProperty_4034) {
                types.add(OdmElementTypes.DataProperty_2002);
            }
            if (relationshipType == OdmElementTypes.DisjointDataProperty_4036) {
                types.add(OdmElementTypes.DataProperty_2002);
            }
            return types;
        }
        if (targetEditPart instanceof ConstantEditPart) {
            List types = new ArrayList();
            if (relationshipType == OdmElementTypes.DataHasValue_4029) {
                types.add(OdmElementTypes.DataProperty_2002);
            }
            if (relationshipType == OdmElementTypes.DataOneOf_4030) {
                types.add(OdmElementTypes.DataRange_2006);
            }
            return types;
        }
        if (targetEditPart instanceof OWLClassEditPart) {
            List types = new ArrayList();
            if (relationshipType == OdmElementTypes.ClassAssertion_4039) {
                types.add(OdmElementTypes.Individual_2008);
            }
            if (relationshipType == OdmElementTypes.ObjectPropertyLink_4046) {
                types.add(OdmElementTypes.OWLClass_2001);
            }
            return types;
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getTypesForTarget(IAdaptable source, IElementType relationshipType) {
        IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
        if (sourceEditPart instanceof MainOntologyEditPart) {
            List types = new ArrayList();
            if (relationshipType == OdmElementTypes.Imports_4001) {
                types.add(OdmElementTypes.OtherOntology_2005);
            }
            return types;
        }
        if (sourceEditPart instanceof DataRangeEditPart) {
            List types = new ArrayList();
            if (relationshipType == OdmElementTypes.DataComplementOf_4006) {
                types.add(OdmElementTypes.DataRange_2006);
            }
            if (relationshipType == OdmElementTypes.DataOneOf_4030) {
                types.add(OdmElementTypes.Constant_2009);
            }
            return types;
        }
        if (sourceEditPart instanceof IndividualEditPart) {
            List types = new ArrayList();
            if (relationshipType == OdmElementTypes.SameIndividual_4010) {
                types.add(OdmElementTypes.Individual_2008);
            }
            if (relationshipType == OdmElementTypes.DifferentIndividuals_4011) {
                types.add(OdmElementTypes.Individual_2008);
            }
            if (relationshipType == OdmElementTypes.ClassAssertion_4039) {
                types.add(OdmElementTypes.OWLClass_2001);
            }
            return types;
        }
        if (sourceEditPart instanceof DataPropertyEditPart) {
            List types = new ArrayList();
            if (relationshipType == OdmElementTypes.DataAllValuesFrom_4012) {
                types.add(OdmElementTypes.DataRange_2006);
            }
            if (relationshipType == OdmElementTypes.DataSomeValuesFrom_4013) {
                types.add(OdmElementTypes.DataRange_2006);
            }
            if (relationshipType == OdmElementTypes.FunctionalDataProperty_4022) {
                types.add(OdmElementTypes.DataProperty_2002);
            }
            if (relationshipType == OdmElementTypes.SubDataPropertyOf_4023) {
                types.add(OdmElementTypes.DataProperty_2002);
            }
            if (relationshipType == OdmElementTypes.DataHasValue_4029) {
                types.add(OdmElementTypes.Constant_2009);
            }
            if (relationshipType == OdmElementTypes.EquivalentDataProperty_4034) {
                types.add(OdmElementTypes.DataProperty_2002);
            }
            if (relationshipType == OdmElementTypes.DisjointDataProperty_4036) {
                types.add(OdmElementTypes.DataProperty_2002);
            }
            return types;
        }
        if (sourceEditPart instanceof ObjectPropertyEditPart) {
            List types = new ArrayList();
            if (relationshipType == OdmElementTypes.InverseObjectProperty_4018) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            if (relationshipType == OdmElementTypes.FunctionalObjectProperty_4019) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            if (relationshipType == OdmElementTypes.InverseFunctionalObjectProperty_4020) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            if (relationshipType == OdmElementTypes.SubObjectPropertyOf_4021) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            if (relationshipType == OdmElementTypes.SymmetricObjectProperty_4024) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            if (relationshipType == OdmElementTypes.ReflexiveObjectProperty_4025) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            if (relationshipType == OdmElementTypes.ObjectHasValue_4028) {
                types.add(OdmElementTypes.Individual_2008);
            }
            if (relationshipType == OdmElementTypes.EquivalentObjectProperty_4033) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            if (relationshipType == OdmElementTypes.DisjointObjectProperty_4035) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            if (relationshipType == OdmElementTypes.AntisymmetricObjectProperty_4037) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            if (relationshipType == OdmElementTypes.TransitiveObjectProperty_4038) {
                types.add(OdmElementTypes.ObjectProperty_2003);
            }
            return types;
        }
        if (sourceEditPart instanceof OWLClassEditPart) {
            List types = new ArrayList();
            if (relationshipType == OdmElementTypes.ObjectPropertyLink_4046) {
                types.add(OdmElementTypes.OWLClass_2001);
            }
            if (relationshipType == OdmElementTypes.DataPropertyLink_4047) {
                types.add(OdmElementTypes.DataRange_2006);
            }
            return types;
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public EObject selectExistingElementForSource(IAdaptable target, IElementType relationshipType) {
        return selectExistingElement(target, getTypesForSource(target, relationshipType));
    }

    /**
	 * @generated
	 */
    public EObject selectExistingElementForTarget(IAdaptable source, IElementType relationshipType) {
        return selectExistingElement(source, getTypesForTarget(source, relationshipType));
    }

    /**
	 * @generated
	 */
    protected EObject selectExistingElement(IAdaptable host, Collection types) {
        if (types.isEmpty()) {
            return null;
        }
        IGraphicalEditPart editPart = (IGraphicalEditPart) host.getAdapter(IGraphicalEditPart.class);
        if (editPart == null) {
            return null;
        }
        Diagram diagram = (Diagram) editPart.getRoot().getContents().getModel();
        Collection elements = new HashSet();
        for (Iterator it = diagram.getElement().eAllContents(); it.hasNext(); ) {
            EObject element = (EObject) it.next();
            if (isApplicableElement(element, types)) {
                elements.add(element);
            }
        }
        if (elements.isEmpty()) {
            return null;
        }
        return selectElement((EObject[]) elements.toArray(new EObject[elements.size()]));
    }

    /**
	 * @generated
	 */
    protected boolean isApplicableElement(EObject element, Collection types) {
        IElementType type = ElementTypeRegistry.getInstance().getElementType(element);
        return types.contains(type);
    }

    /**
	 * @generated
	 */
    protected EObject selectElement(EObject[] elements) {
        Shell shell = Display.getCurrent().getActiveShell();
        ILabelProvider labelProvider = new AdapterFactoryLabelProvider(OdmDiagramEditorPlugin.getInstance().getItemProvidersAdapterFactory());
        ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, labelProvider);
        dialog.setMessage("Available domain model elements:");
        dialog.setTitle("Select domain model element");
        dialog.setMultipleSelection(false);
        dialog.setElements(elements);
        EObject selected = null;
        if (dialog.open() == Window.OK) {
            selected = (EObject) dialog.getFirstResult();
        }
        return selected;
    }
}
