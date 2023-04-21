package sg.edu.nus.comp.simTL.engine.interpreter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import sg.edu.nus.comp.simTL.engine.IModel;
import sg.edu.nus.comp.simTL.engine.ITemplate;
import sg.edu.nus.comp.simTL.engine.Util;
import sg.edu.nus.comp.simTL.engine.exceptions.InterpreterException;
import sg.edu.nus.comp.simTL.engine.exceptions.SimTLException;
import sg.edu.nus.comp.simTL.engine.interpreter.evaluators.TTemplate;
import sg.edu.nus.comp.simTL.engine.tracing.IContext;
import sg.edu.nus.comp.simTL.engine.tracing.Reference2Element;

/**
 * @author Marcel B�hme
 * Comment created on: 29-Jun-2010
 */
public class Template extends SimTLModel implements ITemplate {

    private static Logger log = Logger.getLogger(Template.class);

    private TTemplate templateElement;

    /**
	 * @param modelType
	 * @param model
	 * @throws SimTLException
	 */
    public Template(Resource model) throws SimTLException {
        super(ModelType.TEMPLATE_MODEL, null);
        if (model == null) {
            throw new InterpreterException("Template resource is null");
        } else if (model.getContents().size() == 0) {
            throw new InterpreterException("Template resource is empty");
        }
        templateElement = new TTemplate(model.getContents().get(0));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<EObject> getChildrenFrom(EObject parent, EReference reference, IContext context) throws SimTLException {
        if (parent == null) return null; else if (reference == null) throw new SimTLException("Reference is null");
        List<EObject> list = new ArrayList<EObject>();
        if (reference.isMany()) {
            List<EObject> children = (List<EObject>) parent.eGet(reference);
            if (children.size() > 0) {
                list.addAll(children);
            }
        } else {
            EObject child = (EObject) parent.eGet(reference);
            if (child != null) {
                list.add(child);
            }
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public Reference2Element getParentFrom(EObject child, IContext context) throws SimTLException {
        if (child == null) return null;
        EObject parent = child.eContainer();
        EReference reference = child.eContainmentFeature();
        return new Reference2Element(parent, reference, context, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean removeChildFrom(EObject tParent, EReference tParent2tChildRef, EObject tChild, IContext context) throws SimTLException {
        if (tParent == null) throw new SimTLException("tParent is null"); else if (tParent2tChildRef == null) throw new SimTLException("tParent2tChildRef is null"); else if (tChild == null) return false;
        log.info("Remove " + Util.getFullName(tChild) + " from " + Util.getFullName(tParent));
        if (tParent2tChildRef.isMany()) {
            List<EObject> tParentChildren = (List<EObject>) tParent.eGet(tParent2tChildRef);
            if (tParentChildren.contains(tChild)) {
                tParentChildren.remove(tChild);
                return true;
            }
        } else {
            tParent.eSet(tParent2tChildRef, null);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addChildTo(EObject tParent, EReference tParent2tChildRef, EObject tChild, Integer position, IContext context) throws SimTLException {
        if (tParent == null) throw new SimTLException("tParent is null"); else if (tParent2tChildRef == null) throw new SimTLException("tParent2tChildRef is null"); else if (tChild == null) return false;
        log.info("Add " + Util.getFullName(tChild) + " to " + Util.getFullName(tParent) + " at reference " + tParent2tChildRef.getName() + " and position " + position);
        if (tParent2tChildRef.isMany()) {
            List<EObject> listOfChildren = ((List<EObject>) tParent.eGet(tParent2tChildRef));
            if (!listOfChildren.contains(tChild)) {
                if (position == null || position == Util.NO_POSITION) {
                    listOfChildren.add(tChild);
                } else {
                    listOfChildren.add(position, tChild);
                }
                return true;
            }
        } else {
            if (position != null || position != Util.NO_POSITION) {
                throw new SimTLException("Cannot set element. Position given (" + position + ")");
            } else {
                tParent.eSet(tParent2tChildRef, tChild);
            }
            return true;
        }
        return false;
    }

    @Override
    public EObject getChild() throws SimTLException {
        return templateElement.getChild();
    }

    @Override
    public IModel getInputModel(String inputModelName) {
        return templateElement.getInputModel(inputModelName);
    }

    @Override
    public List<IModel> getInputModels() {
        return templateElement.getInputModels();
    }

    @Override
    public String getObjectLanguageExtension() {
        return templateElement.getObjectLanguageExtension();
    }

    @Override
    public EPackage getObjectLanguagePackage() {
        return templateElement.getObjectLanguagePackage();
    }

    @Override
    public Factory getObjectLanguageTextResourceFactory() {
        return templateElement.getObjectLanguageTextResourceFactory();
    }
}
