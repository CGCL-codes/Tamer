package it.unisannio.rcost.callgraphanalyzer.diagram.edit.policies;

import it.unisannio.rcost.callgraphanalyzer.CallGraphPackage;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.commands.Advice2CreateCommand;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.commands.Class2CreateCommand;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.commands.Field2CreateCommand;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.commands.Interface2CreateCommand;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.commands.Method2CreateCommand;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.commands.Method3CreateCommand;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.commands.Pointcut2CreateCommand;
import it.unisannio.rcost.callgraphanalyzer.diagram.providers.CallGraphElementTypes;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;

/**
 * @generated
 */
public class AspectBodyCompartment3ItemSemanticEditPolicy extends CallGraphBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    protected Command getCreateCommand(CreateElementRequest req) {
        if (CallGraphElementTypes.Pointcut_2002 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(CallGraphPackage.eINSTANCE.getAspect_Pointcuts());
            }
            return getGEFWrapper(new Pointcut2CreateCommand(req));
        }
        if (CallGraphElementTypes.Advice_2003 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(CallGraphPackage.eINSTANCE.getAspect_Advices());
            }
            return getGEFWrapper(new Advice2CreateCommand(req));
        }
        if (CallGraphElementTypes.Method_2004 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(CallGraphPackage.eINSTANCE.getInterface_Methods());
            }
            return getGEFWrapper(new Method2CreateCommand(req));
        }
        if (CallGraphElementTypes.Field_2005 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(CallGraphPackage.eINSTANCE.getInterface_Fields());
            }
            return getGEFWrapper(new Field2CreateCommand(req));
        }
        if (CallGraphElementTypes.Interface_2006 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(CallGraphPackage.eINSTANCE.getInterface_InnerModules());
            }
            return getGEFWrapper(new Class2CreateCommand(req));
        }
        if (CallGraphElementTypes.Class_2007 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(CallGraphPackage.eINSTANCE.getInterface_InnerModules());
            }
            return getGEFWrapper(new Interface2CreateCommand(req));
        }
        if (CallGraphElementTypes.Aspect_2008 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(CallGraphPackage.eINSTANCE.getInterface_InnerModules());
            }
            return getGEFWrapper(new Method3CreateCommand(req));
        }
        return super.getCreateCommand(req);
    }
}
