package hub.sam.mof.simulator.behaviour.diagram.edit.policies;

import hub.sam.mof.simulator.behaviour.diagram.edit.commands.MAssignAction2CreateCommand;
import hub.sam.mof.simulator.behaviour.diagram.edit.commands.MAtomicGroup2CreateCommand;
import hub.sam.mof.simulator.behaviour.diagram.edit.commands.MCreateAction2CreateCommand;
import hub.sam.mof.simulator.behaviour.diagram.edit.commands.MDecisionMergeNode2CreateCommand;
import hub.sam.mof.simulator.behaviour.diagram.edit.commands.MFinalNode2CreateCommand;
import hub.sam.mof.simulator.behaviour.diagram.edit.commands.MForkJoinNode2CreateCommand;
import hub.sam.mof.simulator.behaviour.diagram.edit.commands.MInitialNode2CreateCommand;
import hub.sam.mof.simulator.behaviour.diagram.edit.commands.MInvocationAction2CreateCommand;
import hub.sam.mof.simulator.behaviour.diagram.edit.commands.MIterateAction2CreateCommand;
import hub.sam.mof.simulator.behaviour.diagram.edit.commands.MObjectNode2CreateCommand;
import hub.sam.mof.simulator.behaviour.diagram.edit.commands.MQueryAction2CreateCommand;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;

/**
 * @generated
 */
public class MIterateActionMIterateActionContentPaneCompartment2ItemSemanticEditPolicy extends M3ActionsBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    protected Command getCreateCommand(CreateElementRequest req) {
        if (hub.sam.mof.simulator.behaviour.diagram.providers.M3ActionsElementTypes.MAtomicGroup_3012 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(M3Actions.M3ActionsPackage.eINSTANCE.getMActionGroup_MemberNodes());
            }
            return getGEFWrapper(new MAtomicGroup2CreateCommand(req));
        }
        if (hub.sam.mof.simulator.behaviour.diagram.providers.M3ActionsElementTypes.MInitialNode_3013 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(M3Actions.M3ActionsPackage.eINSTANCE.getMActionGroup_MemberNodes());
            }
            return getGEFWrapper(new MInitialNode2CreateCommand(req));
        }
        if (hub.sam.mof.simulator.behaviour.diagram.providers.M3ActionsElementTypes.MFinalNode_3014 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(M3Actions.M3ActionsPackage.eINSTANCE.getMActionGroup_MemberNodes());
            }
            return getGEFWrapper(new MFinalNode2CreateCommand(req));
        }
        if (hub.sam.mof.simulator.behaviour.diagram.providers.M3ActionsElementTypes.MDecisionMergeNode_3015 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(M3Actions.M3ActionsPackage.eINSTANCE.getMActionGroup_MemberNodes());
            }
            return getGEFWrapper(new MDecisionMergeNode2CreateCommand(req));
        }
        if (hub.sam.mof.simulator.behaviour.diagram.providers.M3ActionsElementTypes.MForkJoinNode_3016 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(M3Actions.M3ActionsPackage.eINSTANCE.getMActionGroup_MemberNodes());
            }
            return getGEFWrapper(new MForkJoinNode2CreateCommand(req));
        }
        if (hub.sam.mof.simulator.behaviour.diagram.providers.M3ActionsElementTypes.MObjectNode_3017 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(M3Actions.M3ActionsPackage.eINSTANCE.getMActionGroup_MemberNodes());
            }
            return getGEFWrapper(new MObjectNode2CreateCommand(req));
        }
        if (hub.sam.mof.simulator.behaviour.diagram.providers.M3ActionsElementTypes.MAssignAction_3007 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(M3Actions.M3ActionsPackage.eINSTANCE.getMActionGroup_MemberNodes());
            }
            return getGEFWrapper(new MAssignAction2CreateCommand(req));
        }
        if (hub.sam.mof.simulator.behaviour.diagram.providers.M3ActionsElementTypes.MCreateAction_3008 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(M3Actions.M3ActionsPackage.eINSTANCE.getMActionGroup_MemberNodes());
            }
            return getGEFWrapper(new MCreateAction2CreateCommand(req));
        }
        if (hub.sam.mof.simulator.behaviour.diagram.providers.M3ActionsElementTypes.MInvocationAction_3009 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(M3Actions.M3ActionsPackage.eINSTANCE.getMActionGroup_MemberNodes());
            }
            return getGEFWrapper(new MInvocationAction2CreateCommand(req));
        }
        if (hub.sam.mof.simulator.behaviour.diagram.providers.M3ActionsElementTypes.MIterateAction_3021 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(M3Actions.M3ActionsPackage.eINSTANCE.getMActionGroup_MemberNodes());
            }
            return getGEFWrapper(new MIterateAction2CreateCommand(req));
        }
        if (hub.sam.mof.simulator.behaviour.diagram.providers.M3ActionsElementTypes.MQueryAction_3022 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(M3Actions.M3ActionsPackage.eINSTANCE.getMActionGroup_MemberNodes());
            }
            return getGEFWrapper(new MQueryAction2CreateCommand(req));
        }
        return super.getCreateCommand(req);
    }
}
