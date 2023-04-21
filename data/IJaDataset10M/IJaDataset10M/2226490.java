package fd2.diagram.edit.policies;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.ICompositeCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.SemanticEditPolicy;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.GetEditContextRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.gmf.runtime.notation.View;
import fd2.BaseFeatureNode;
import fd2.Diagram;
import fd2.Fd2Package;
import fd2.OneManyRelation;
import fd2.diagram.edit.helpers.Fd2BaseEditHelper;
import fd2.diagram.expressions.Fd2AbstractExpression;
import fd2.diagram.expressions.Fd2OCLFactory;
import fd2.diagram.part.Fd2DiagramEditorPlugin;
import fd2.diagram.part.Fd2VisualIDRegistry;
import fd2.diagram.providers.Fd2ElementTypes;

/**
 * @generated
 */
public class Fd2BaseItemSemanticEditPolicy extends SemanticEditPolicy {

    /**
	 * Extended request data key to hold editpart visual id.
	 * @generated
	 */
    public static final String VISUAL_ID_KEY = "visual_id";

    /**
	 * @generated
	 */
    private final IElementType myElementType;

    /**
	 * @generated
	 */
    protected Fd2BaseItemSemanticEditPolicy(IElementType elementType) {
        myElementType = elementType;
    }

    /**
	 * Extended request data key to hold editpart visual id.
	 * Add visual id of edited editpart to extended data of the request
	 * so command switch can decide what kind of diagram element is being edited.
	 * It is done in those cases when it's not possible to deduce diagram
	 * element kind from domain element.
	 * 
	 * @generated
	 */
    public Command getCommand(Request request) {
        if (request instanceof ReconnectRequest) {
            Object view = ((ReconnectRequest) request).getConnectionEditPart().getModel();
            if (view instanceof View) {
                Integer id = new Integer(Fd2VisualIDRegistry.getVisualID((View) view));
                request.getExtendedData().put(VISUAL_ID_KEY, id);
            }
        }
        return super.getCommand(request);
    }

    /**
	 * Returns visual id from request parameters.
	 * @generated
	 */
    protected int getVisualID(IEditCommandRequest request) {
        Object id = request.getParameter(VISUAL_ID_KEY);
        return id instanceof Integer ? ((Integer) id).intValue() : -1;
    }

    /**
	 * @generated
	 */
    protected Command getSemanticCommand(IEditCommandRequest request) {
        IEditCommandRequest completedRequest = completeRequest(request);
        Command semanticCommand = getSemanticCommandSwitch(completedRequest);
        semanticCommand = getEditHelperCommand(completedRequest, semanticCommand);
        if (completedRequest instanceof DestroyRequest) {
            DestroyRequest destroyRequest = (DestroyRequest) completedRequest;
            return shouldProceed(destroyRequest) ? addDeleteViewCommand(semanticCommand, destroyRequest) : null;
        }
        return semanticCommand;
    }

    /**
	 * @generated
	 */
    protected Command addDeleteViewCommand(Command mainCommand, DestroyRequest completedRequest) {
        Command deleteViewCommand = getGEFWrapper(new DeleteCommand(getEditingDomain(), (View) getHost().getModel()));
        return mainCommand == null ? deleteViewCommand : mainCommand.chain(deleteViewCommand);
    }

    /**
	 * @generated
	 */
    private Command getEditHelperCommand(IEditCommandRequest request, Command editPolicyCommand) {
        if (editPolicyCommand != null) {
            ICommand command = editPolicyCommand instanceof ICommandProxy ? ((ICommandProxy) editPolicyCommand).getICommand() : new CommandProxy(editPolicyCommand);
            request.setParameter(Fd2BaseEditHelper.EDIT_POLICY_COMMAND, command);
        }
        IElementType requestContextElementType = getContextElementType(request);
        request.setParameter(Fd2BaseEditHelper.CONTEXT_ELEMENT_TYPE, requestContextElementType);
        ICommand command = requestContextElementType.getEditCommand(request);
        request.setParameter(Fd2BaseEditHelper.EDIT_POLICY_COMMAND, null);
        request.setParameter(Fd2BaseEditHelper.CONTEXT_ELEMENT_TYPE, null);
        if (command != null) {
            if (!(command instanceof CompositeTransactionalCommand)) {
                command = new CompositeTransactionalCommand(getEditingDomain(), command.getLabel()).compose(command);
            }
            return new ICommandProxy(command);
        }
        return editPolicyCommand;
    }

    /**
	 * @generated
	 */
    private IElementType getContextElementType(IEditCommandRequest request) {
        IElementType requestContextElementType = Fd2ElementTypes.getElementType(getVisualID(request));
        return requestContextElementType != null ? requestContextElementType : myElementType;
    }

    /**
	 * @generated
	 */
    protected Command getSemanticCommandSwitch(IEditCommandRequest req) {
        if (req instanceof CreateRelationshipRequest) {
            return getCreateRelationshipCommand((CreateRelationshipRequest) req);
        } else if (req instanceof CreateElementRequest) {
            return getCreateCommand((CreateElementRequest) req);
        } else if (req instanceof ConfigureRequest) {
            return getConfigureCommand((ConfigureRequest) req);
        } else if (req instanceof DestroyElementRequest) {
            return getDestroyElementCommand((DestroyElementRequest) req);
        } else if (req instanceof DestroyReferenceRequest) {
            return getDestroyReferenceCommand((DestroyReferenceRequest) req);
        } else if (req instanceof DuplicateElementsRequest) {
            return getDuplicateCommand((DuplicateElementsRequest) req);
        } else if (req instanceof GetEditContextRequest) {
            return getEditContextCommand((GetEditContextRequest) req);
        } else if (req instanceof MoveRequest) {
            return getMoveCommand((MoveRequest) req);
        } else if (req instanceof ReorientReferenceRelationshipRequest) {
            return getReorientReferenceRelationshipCommand((ReorientReferenceRelationshipRequest) req);
        } else if (req instanceof ReorientRelationshipRequest) {
            return getReorientRelationshipCommand((ReorientRelationshipRequest) req);
        } else if (req instanceof SetRequest) {
            return getSetCommand((SetRequest) req);
        }
        return null;
    }

    /**
	 * @generated
	 */
    protected Command getConfigureCommand(ConfigureRequest req) {
        return null;
    }

    /**
	 * @generated
	 */
    protected Command getCreateRelationshipCommand(CreateRelationshipRequest req) {
        return null;
    }

    /**
	 * @generated
	 */
    protected Command getCreateCommand(CreateElementRequest req) {
        return null;
    }

    /**
	 * @generated
	 */
    protected Command getSetCommand(SetRequest req) {
        return null;
    }

    /**
	 * @generated
	 */
    protected Command getEditContextCommand(GetEditContextRequest req) {
        return null;
    }

    /**
	 * @generated
	 */
    protected Command getDestroyElementCommand(DestroyElementRequest req) {
        return null;
    }

    /**
	 * @generated
	 */
    protected Command getDestroyReferenceCommand(DestroyReferenceRequest req) {
        return null;
    }

    /**
	 * @generated
	 */
    protected Command getDuplicateCommand(DuplicateElementsRequest req) {
        return null;
    }

    /**
	 * @generated
	 */
    protected Command getMoveCommand(MoveRequest req) {
        return null;
    }

    /**
	 * @generated
	 */
    protected Command getReorientReferenceRelationshipCommand(ReorientReferenceRelationshipRequest req) {
        return UnexecutableCommand.INSTANCE;
    }

    /**
	 * @generated
	 */
    protected Command getReorientRelationshipCommand(ReorientRelationshipRequest req) {
        return UnexecutableCommand.INSTANCE;
    }

    /**
	 * @generated
	 */
    protected final Command getGEFWrapper(ICommand cmd) {
        return new ICommandProxy(cmd);
    }

    /**
	 * Returns editing domain from the host edit part.
	 * @generated
	 */
    protected TransactionalEditingDomain getEditingDomain() {
        return ((IGraphicalEditPart) getHost()).getEditingDomain();
    }

    /**
	 * Clean all shortcuts to the host element from the same diagram
	 * @generated
	 */
    protected void addDestroyShortcutsCommand(ICompositeCommand cmd, View view) {
        assert view.getEAnnotation("Shortcut") == null;
        for (Iterator it = view.getDiagram().getChildren().iterator(); it.hasNext(); ) {
            View nextView = (View) it.next();
            if (nextView.getEAnnotation("Shortcut") == null || !nextView.isSetElement() || nextView.getElement() != view.getElement()) {
                continue;
            }
            cmd.add(new DeleteCommand(getEditingDomain(), nextView));
        }
    }

    /**
	 * @generated
	 */
    public static class LinkConstraints {

        /**
		 * @generated
		 */
        private static final String OPPOSITE_END_VAR = "oppositeEnd";

        /**
		 * @generated
		 */
        private static Fd2AbstractExpression MandatoryRelation_4008_TargetExpression;

        /**
		 * @generated
		 */
        private static Fd2AbstractExpression OptionalRelation_4009_TargetExpression;

        /**
		 * @generated
		 */
        private static Fd2AbstractExpression RequireRelation_4006_TargetExpression;

        /**
		 * @generated
		 */
        private static Fd2AbstractExpression ExcludesRelation_4010_TargetExpression;

        /**
		 * @generated
		 */
        private static Fd2AbstractExpression OneManyRelationTargetFeatureNodes_4004_TargetExpression;

        /**
		 * @generated
		 */
        public static boolean canCreateMandatoryRelation_4008(BaseFeatureNode source, BaseFeatureNode target) {
            return canExistMandatoryRelation_4008(source, target);
        }

        /**
		 * @generated
		 */
        public static boolean canCreateOptionalRelation_4009(BaseFeatureNode source, BaseFeatureNode target) {
            return canExistOptionalRelation_4009(source, target);
        }

        /**
		 * @generated
		 */
        public static boolean canCreateRequireRelation_4006(BaseFeatureNode source, BaseFeatureNode target) {
            return canExistRequireRelation_4006(source, target);
        }

        /**
		 * @generated
		 */
        public static boolean canCreateExcludesRelation_4010(Diagram container, BaseFeatureNode source, BaseFeatureNode target) {
            return canExistExcludesRelation_4010(container, source, target);
        }

        /**
		 * @generated
		 */
        public static boolean canCreateOneManyRelationTargetFeatureNodes_4004(OneManyRelation source, BaseFeatureNode target) {
            if (source != null) {
                if (source.getTargetFeatureNodes().contains(target)) {
                    return false;
                }
            }
            return canExistOneManyRelationTargetFeatureNodes_4004(source, target);
        }

        /**
		 * @generated
		 */
        public static boolean canExistMandatoryRelation_4008(BaseFeatureNode source, BaseFeatureNode target) {
            try {
                if (target == null) {
                    return true;
                }
                if (MandatoryRelation_4008_TargetExpression == null) {
                    Map env = Collections.singletonMap(OPPOSITE_END_VAR, Fd2Package.eINSTANCE.getBaseFeatureNode());
                    MandatoryRelation_4008_TargetExpression = Fd2OCLFactory.getExpression("self <> oppositeEnd", Fd2Package.eINSTANCE.getBaseFeatureNode(), env);
                }
                Object targetVal = MandatoryRelation_4008_TargetExpression.evaluate(target, Collections.singletonMap(OPPOSITE_END_VAR, source));
                if (false == targetVal instanceof Boolean || !((Boolean) targetVal).booleanValue()) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                Fd2DiagramEditorPlugin.getInstance().logError("Link constraint evaluation error", e);
                return false;
            }
        }

        /**
		 * @generated
		 */
        public static boolean canExistOptionalRelation_4009(BaseFeatureNode source, BaseFeatureNode target) {
            try {
                if (target == null) {
                    return true;
                }
                if (OptionalRelation_4009_TargetExpression == null) {
                    Map env = Collections.singletonMap(OPPOSITE_END_VAR, Fd2Package.eINSTANCE.getBaseFeatureNode());
                    OptionalRelation_4009_TargetExpression = Fd2OCLFactory.getExpression("self <> oppositeEnd", Fd2Package.eINSTANCE.getBaseFeatureNode(), env);
                }
                Object targetVal = OptionalRelation_4009_TargetExpression.evaluate(target, Collections.singletonMap(OPPOSITE_END_VAR, source));
                if (false == targetVal instanceof Boolean || !((Boolean) targetVal).booleanValue()) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                Fd2DiagramEditorPlugin.getInstance().logError("Link constraint evaluation error", e);
                return false;
            }
        }

        /**
		 * @generated
		 */
        public static boolean canExistRequireRelation_4006(BaseFeatureNode source, BaseFeatureNode target) {
            try {
                if (target == null) {
                    return true;
                }
                if (RequireRelation_4006_TargetExpression == null) {
                    Map env = Collections.singletonMap(OPPOSITE_END_VAR, Fd2Package.eINSTANCE.getBaseFeatureNode());
                    RequireRelation_4006_TargetExpression = Fd2OCLFactory.getExpression("self <> oppositeEnd", Fd2Package.eINSTANCE.getBaseFeatureNode(), env);
                }
                Object targetVal = RequireRelation_4006_TargetExpression.evaluate(target, Collections.singletonMap(OPPOSITE_END_VAR, source));
                if (false == targetVal instanceof Boolean || !((Boolean) targetVal).booleanValue()) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                Fd2DiagramEditorPlugin.getInstance().logError("Link constraint evaluation error", e);
                return false;
            }
        }

        /**
		 * @generated
		 */
        public static boolean canExistExcludesRelation_4010(Diagram container, BaseFeatureNode source, BaseFeatureNode target) {
            try {
                if (target == null) {
                    return true;
                }
                if (ExcludesRelation_4010_TargetExpression == null) {
                    Map env = Collections.singletonMap(OPPOSITE_END_VAR, Fd2Package.eINSTANCE.getBaseFeatureNode());
                    ExcludesRelation_4010_TargetExpression = Fd2OCLFactory.getExpression("self <> oppositeEnd", Fd2Package.eINSTANCE.getBaseFeatureNode(), env);
                }
                Object targetVal = ExcludesRelation_4010_TargetExpression.evaluate(target, Collections.singletonMap(OPPOSITE_END_VAR, source));
                if (false == targetVal instanceof Boolean || !((Boolean) targetVal).booleanValue()) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                Fd2DiagramEditorPlugin.getInstance().logError("Link constraint evaluation error", e);
                return false;
            }
        }

        /**
		 * @generated
		 */
        public static boolean canExistOneManyRelationTargetFeatureNodes_4004(OneManyRelation source, BaseFeatureNode target) {
            try {
                if (target == null) {
                    return true;
                }
                if (OneManyRelationTargetFeatureNodes_4004_TargetExpression == null) {
                    Map env = Collections.singletonMap(OPPOSITE_END_VAR, Fd2Package.eINSTANCE.getOneManyRelation());
                    OneManyRelationTargetFeatureNodes_4004_TargetExpression = Fd2OCLFactory.getExpression("self <> oppositeEnd", Fd2Package.eINSTANCE.getBaseFeatureNode(), env);
                }
                Object targetVal = OneManyRelationTargetFeatureNodes_4004_TargetExpression.evaluate(target, Collections.singletonMap(OPPOSITE_END_VAR, source));
                if (false == targetVal instanceof Boolean || !((Boolean) targetVal).booleanValue()) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                Fd2DiagramEditorPlugin.getInstance().logError("Link constraint evaluation error", e);
                return false;
            }
        }
    }
}
