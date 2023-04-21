package com.ctb.diagram.edit.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import com.ctb.CtbPackage;
import com.ctb.diagram.edit.commands.ApplicationCreateCommand;
import com.ctb.diagram.edit.commands.ConnectorCreateCommand;
import com.ctb.diagram.edit.commands.FunctionalComponentCreateCommand;
import com.ctb.diagram.edit.commands.TechnicalComponentCreateCommand;
import com.ctb.diagram.providers.CtbElementTypes;

/**
 * @generated
 */
public class ExecutionEnvironmentExecutionEnvironmentCompItemSemanticEditPolicy extends CtbBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    protected Command getCreateCommand(CreateElementRequest req) {
        if (CtbElementTypes.TechnicalComponent_2002 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(CtbPackage.eINSTANCE.getExecutionEnvironment_RuntimeComponents());
            }
            return getGEFWrapper(new TechnicalComponentCreateCommand(req));
        }
        if (CtbElementTypes.Connector_2005 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(CtbPackage.eINSTANCE.getExecutionEnvironment_RuntimeConnectors());
            }
            return getGEFWrapper(new ConnectorCreateCommand(req));
        }
        if (CtbElementTypes.Application_2006 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(CtbPackage.eINSTANCE.getExecutionEnvironment_Applications());
            }
            return getGEFWrapper(new ApplicationCreateCommand(req));
        }
        if (CtbElementTypes.FunctionalComponent_2009 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(CtbPackage.eINSTANCE.getExecutionEnvironment_RuntimeComponents());
            }
            return getGEFWrapper(new FunctionalComponentCreateCommand(req));
        }
        return super.getCreateCommand(req);
    }
}
