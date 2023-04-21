package sg.edu.nus.comp.simTL.engine.interpreter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import sg.edu.nus.comp.simTL.engine.IInterpreter;
import sg.edu.nus.comp.simTL.engine.IModel;
import sg.edu.nus.comp.simTL.engine.ITemplate;
import sg.edu.nus.comp.simTL.engine.InterpretationResult;
import sg.edu.nus.comp.simTL.engine.Util;
import sg.edu.nus.comp.simTL.engine.IModel.ModelType;
import sg.edu.nus.comp.simTL.engine.exceptions.InjectionException;
import sg.edu.nus.comp.simTL.engine.exceptions.InterpreterException;
import sg.edu.nus.comp.simTL.engine.exceptions.SimTLException;
import sg.edu.nus.comp.simTL.engine.exceptions.ValidationException;
import sg.edu.nus.comp.simTL.engine.interpreter.evaluators.TElement;
import sg.edu.nus.comp.simTL.engine.interpreter.evaluators.TForLoop;
import sg.edu.nus.comp.simTL.engine.interpreter.evaluators.TIfStatement;
import sg.edu.nus.comp.simTL.engine.interpreter.evaluators.TPlaceholder;
import sg.edu.nus.comp.simTL.engine.synchronizer.EObjectCopier;
import sg.edu.nus.comp.simTL.engine.tracing.DynamicContext;
import sg.edu.nus.comp.simTL.engine.tracing.ModelCorrespondence;
import sg.edu.nus.comp.simTL.engine.tracing.Reference2Attribute;
import sg.edu.nus.comp.simTL.engine.tracing.Reference2Element;
import sg.edu.nus.comp.simTL.engine.tracing.ReferenceFromParent;
import sg.edu.nus.comp.simTL.engine.tracing.StaticContext;
import sg.edu.nus.comp.simTL.engine.tracing.TraceLink;
import sg.edu.nus.comp.simTL.engine.tracing.TraceLinkNode;

/**
 * 
 * @author Marcel B�hme
 * Comment created on: 25-Feb-2010
 */
public class SimTLInterpreter implements IInterpreter {

    private static Logger log = Logger.getLogger(SimTLInterpreter.class);

    private static final Logger interpreterLog = Logger.getLogger("Interpreter");

    private ITemplate template;

    private URI outURI;

    private SimTLModel instance;

    private DynamicContext currentContext;

    private ModelCorrespondence modelCorrespondence;

    private Map<EObject, EObject> interpretedX2IMap;

    @Override
    public InterpretationResult interprete(ITemplate template, URI outURI) throws SimTLException {
        this.template = template;
        this.outURI = outURI;
        currentContext = new DynamicContext();
        interpretedX2IMap = new HashMap<EObject, EObject>();
        modelCorrespondence = new ModelCorrespondence();
        try {
            load();
            instance.save();
        } catch (ValidationException e) {
            interpreterLog.error(e.getMessage());
            throw e;
        } catch (IOException e) {
            throw new SimTLException("Couldn't save instance", e);
        }
        return new InterpretationResult(instance, modelCorrespondence);
    }

    private void load() throws SimTLException {
        EPackage instancePackage = template.getObjectLanguagePackage();
        Resource.Factory resourceFactory = template.getObjectLanguageTextResourceFactory();
        ResourceSet resourceSet = template.getChild().eResource().getResourceSet();
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(template.getObjectLanguageExtension(), resourceFactory);
        Resource instanceResource = resourceSet.createResource(outURI);
        if (instanceResource == null) {
            throw new SimTLException("Instance - resource couldn't be created");
        }
        instance = new SimTLModel(ModelType.TEMPLATE_INSTANCE_MODEL, instanceResource);
        evaluate(template.getChild(), new InstanceElementProxy(instance.getModelResource().getContents()), instancePackage);
    }

    private void evaluateTElement(EObject tObject, InstanceElementProxy iObjectProxy, EPackage iRootPackage) throws SimTLException {
        if (TPlaceholder.isTPlaceholder(tObject)) {
            return;
        } else if (TForLoop.isTForLoop(tObject)) {
            log.debug("Evaluating loop");
            TForLoop loop = new TForLoop(tObject, currentContext, template);
            ReferenceFromParent pReferenceFromParent = loop.getSetToBeIterated().getReferenceFromParent();
            if (pReferenceFromParent instanceof Reference2Attribute) {
                throw new ValidationException("For-loop references an attribute and no list of elements! " + Util.getFullName(loop.getTElement()));
            } else if (!((Reference2Element) pReferenceFromParent).getReferenceToChildren().isMany()) {
                throw new ValidationException("For-loop doesn't reference a list of elements! " + Util.getFullName(loop.getTElement()));
            }
            List<?> parameterList = (List<?>) pReferenceFromParent.getReferencedValue();
            for (Object o : parameterList) {
                currentContext.pushVariable(loop.getName(), (EObject) o);
                for (EObject tObjectChildren : template.getChildrenFrom(loop.getTElement(), loop.getOLReference(), currentContext)) {
                    evaluate(tObjectChildren, iObjectProxy, iRootPackage);
                }
                currentContext.pullVariable(loop.getName());
            }
        } else if (TIfStatement.isTIfStatement(tObject)) {
            log.debug("Evaluating if");
            TIfStatement ifStatement = new TIfStatement(tObject, currentContext, template);
            Boolean condition = (Boolean) ifStatement.getCondition().getReferenceFromParent().getReferencedValue();
            if (condition) {
                for (EObject tObjectChildren : template.getChildrenFrom(ifStatement.getTElement(), ifStatement.getOLReference(), currentContext)) {
                    evaluate(tObjectChildren, iObjectProxy, iRootPackage);
                }
            }
        } else {
            log.warn("Didn't recognize XFrameElement: " + tObject);
        }
    }

    private void evaluate(EObject tObject, InstanceElementProxy iObjectProxy, EPackage iRootPackage) throws SimTLException {
        if (tObject == null) return;
        if (TElement.isTElement(tObject)) {
            evaluateTElement(tObject, iObjectProxy, iRootPackage);
            return;
        }
        EObject iObject = EObjectCopier.instantiate(tObject.eClass(), iRootPackage);
        if (iObject == null) {
            throw new InterpreterException("Didn't find respective template instance class for template class " + tObject.eClass().getName());
        }
        iObjectProxy.set(iObject);
        interpretedX2IMap.put(tObject, iObject);
        for (EAttribute tAttributeClass : tObject.eClass().getEAllAttributes()) {
            evaluateTAttribute(tAttributeClass, tObject, iObject);
        }
        for (EReference tReference : tObject.eClass().getEAllReferences()) {
            if (tReference != null) {
                if (TElement.isTElement(tReference)) {
                    continue;
                }
            }
            InstanceElementProxy iReferenceProxy = new InstanceElementProxy(iObject, (EReference) iObject.eClass().getEStructuralFeature(tReference.getName()));
            for (EObject tObjectChild : template.getChildrenFrom(tObject, tReference, currentContext)) {
                evaluate(tObjectChild, iReferenceProxy, iRootPackage);
            }
        }
        modelCorrespondence.add(new TraceLink(currentContext.saveToStaticContext(), new TraceLinkNode(template, tObject), new TraceLinkNode(instance, iObject)));
    }

    /**
	 * Evaluates attributes. If there is a placeholder, fill 
	 * attribute in <code>tiObject</code> with respective value, else
	 * just copy value of attribute in <code>tObject</code> to attribute in <code>tiObject</code> 
	 * @param tAttributeClass The attribute (has name and all) in template
	 * @param tObject The templateObject which contains this attribute in template
	 * @param tiObject The respective eObject in template instance corresponding to the <code>tObject</code>
	 */
    private void evaluateTAttribute(EAttribute tAttributeClass, EObject tObject, EObject iObject) throws SimTLException {
        EAttribute iAttributeClass = (EAttribute) iObject.eClass().getEStructuralFeature(tAttributeClass.getName());
        if (iAttributeClass == null) {
            throw new InjectionException("No tiAttributeClass found for tAttributeClass " + tObject.eClass().getName() + "." + tAttributeClass.getName());
        }
        if (checkActualAttribute(tAttributeClass, iAttributeClass, tObject, iObject)) {
            modelCorrespondence.add(new TraceLink(currentContext.saveToStaticContext(), new TraceLinkNode(template, new Reference2Attribute(tObject, tAttributeClass)), new TraceLinkNode(instance, new Reference2Attribute(iObject, iAttributeClass))));
        } else {
            ReferenceFromParent refFromParent = checkPlaceholder(tAttributeClass, iAttributeClass, tObject, iObject);
            if (refFromParent != null) {
                StaticContext staticContext = currentContext.saveToStaticContext();
                modelCorrespondence.add(new TraceLink(staticContext, new TraceLinkNode(getImportedModel(refFromParent.getParent().eResource()), (Reference2Attribute) refFromParent), new TraceLinkNode(instance, new Reference2Attribute(iObject, iAttributeClass))));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private boolean checkActualAttribute(EAttribute tAttributeClass, EAttribute iAttributeClass, EObject tObject, EObject iObject) throws SimTLException {
        if (!iAttributeClass.isMany()) {
            Object tAttribute = tObject.eGet(tAttributeClass);
            if (tAttribute != null) {
                iObject.eSet(iAttributeClass, tAttribute);
                return true;
            }
        } else {
            for (Object tAttribute : (List<?>) tObject.eGet(tAttributeClass)) {
                ((EList<Object>) iObject.eGet(iAttributeClass)).add(tAttribute);
            }
            return !((List<?>) tObject.eGet(tAttributeClass)).isEmpty();
        }
        return false;
    }

    private ReferenceFromParent checkPlaceholder(EAttribute tAttributeClass, EAttribute iAttributeClass, EObject tObject, EObject iObject) throws SimTLException {
        String possibleName = TPlaceholder.attributeToPlaceHolder(tAttributeClass.getName());
        EReference tReferenceClass = (EReference) tObject.eClass().getEStructuralFeature(possibleName);
        if (tReferenceClass == null) return null;
        if (tAttributeClass.isMany()) {
            log.warn("Placeholder for a many-attributes is not supported yet: " + tAttributeClass.getName());
            return null;
        } else {
            List<EObject> phList = (List<EObject>) template.getChildrenFrom(tObject, tReferenceClass, currentContext);
            if (phList.isEmpty()) {
                return null;
            }
            EObject placeHolder = phList.iterator().next();
            TPlaceholder phe = new TPlaceholder(placeHolder, currentContext, template);
            ReferenceFromParent pReferenceFromParent = phe.getTMethodStatement().getReferenceFromParent();
            Object pValue = pReferenceFromParent.getReferencedValue();
            if (pValue != null) {
                iObject.eSet(iAttributeClass, pValue);
                return pReferenceFromParent;
            }
        }
        return null;
    }

    private IModel getImportedModel(Resource r) {
        for (IModel m : template.getInputModels()) {
            if (m.getModelResource() == r) {
                return m;
            }
        }
        return null;
    }

    public SimTLModel getInstance() {
        return instance;
    }
}
