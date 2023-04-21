package org.modelversioning.core.conditions.engines.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.eclipse.emf.ecore.EObject;
import org.modelversioning.core.conditions.EvaluationResult;
import org.modelversioning.core.conditions.Template;
import org.modelversioning.core.conditions.engines.ITemplateBinding;
import org.modelversioning.core.conditions.engines.ITemplateBindings;

/**
 * Implements the {@link ITemplateBindings} interface.
 * 
 * @author <a href="mailto:langer@big.tuwien.ac.at">Philip Langer</a>
 * 
 */
public class TemplateBindingsImpl implements ITemplateBindings {

    /**
	 * The root template
	 */
    private Template rootTemplate = null;

    /**
	 * Set of all possible bindings.
	 */
    private Set<ITemplateBinding> bindings = new HashSet<ITemplateBinding>();

    /**
	 * The evaluation result holding information about the generation and
	 * validity of this binding.
	 */
    private EvaluationResult evaluationResult = null;

    /**
	 * Saves the removed bindings.
	 */
    private Map<EObject, Set<ITemplateBinding>> removedBindings = new HashMap<EObject, Set<ITemplateBinding>>();

    /**
	 * Constructs a new template binding for the specified
	 * <code>rootTemplate</code>.
	 * 
	 * @param rootTemplate
	 *            root template to set.
	 */
    public TemplateBindingsImpl(Template rootTemplate) {
        this.rootTemplate = rootTemplate;
    }

    /**
	 * Sets the root template.
	 * 
	 * @param rootTemplate
	 *            the rootTemplate to set
	 */
    protected void setRootTemplate(Template rootTemplate) {
        this.rootTemplate = rootTemplate;
    }

    /**
	 * Sets the evaluation result holding information about the generation and
	 * validity of this binding.
	 * 
	 * @param evaluationResult
	 *            the evaluationResult to set
	 */
    protected void setEvaluationResult(EvaluationResult evaluationResult) {
        this.evaluationResult = evaluationResult;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Set<ITemplateBinding> getAllPossibleBindings() {
        return bindings;
    }

    /**
	 * {@inheritDoc}
	 */
    public ITemplateBindings extractSubBindings(Template template, EObject boundObject, boolean removeExtracted) {
        Set<ITemplateBinding> extractedBindings = new HashSet<ITemplateBinding>();
        for (ITemplateBinding binding : bindings) {
            if (binding.getBoundObjects(template).contains(boundObject)) {
                extractedBindings.add(binding);
            }
        }
        TemplateBindingsImpl extractedTemplateBindings = new TemplateBindingsImpl(this.rootTemplate);
        extractedTemplateBindings.setPossibleBindings(extractedBindings);
        extractedTemplateBindings.setEvaluationResult(this.evaluationResult);
        if (removeExtracted) {
            this.bindings.removeAll(extractedBindings);
        }
        return extractedTemplateBindings;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Set<EObject> getBoundObjects(Template template) {
        Set<EObject> boundObjects = new HashSet<EObject>();
        for (ITemplateBinding binding : bindings) {
            if (binding.getBoundObjects(template) != null) {
                boundObjects.addAll(binding.getBoundObjects(template));
            }
        }
        return boundObjects;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Set<Template> getBoundTemplates(EObject eObject) {
        Set<Template> set = new HashSet<Template>();
        for (ITemplateBinding binding : bindings) {
            Template boundTemplate = binding.getBoundTemplate(eObject);
            if (boundTemplate != null) {
                set.add(boundTemplate);
            }
        }
        return set;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Template getRootTemplate() {
        return rootTemplate;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean isRemovable(EObject object, Template template) {
        if (bindings.size() <= 1) {
            return false;
        }
        boolean isRemovable = false;
        for (ITemplateBinding binding : bindings) {
            if (binding.getBoundObjects(template) == null || !binding.getBoundObjects(template).contains(object)) {
                isRemovable = true;
                break;
            }
        }
        return isRemovable;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean remove(EObject object, Template template) {
        if (!isRemovable(object, template)) {
            return false;
        } else {
            boolean didRemove = false;
            Set<ITemplateBinding> bindingsToRemove = new HashSet<ITemplateBinding>();
            for (ITemplateBinding binding : bindings) {
                if (binding.getBoundObjects(template) != null && binding.getBoundObjects(template).contains(object)) {
                    bindingsToRemove.add(binding);
                }
            }
            for (ITemplateBinding binding : bindingsToRemove) {
                if (removedBindings.get(object) == null) {
                    removedBindings.put(object, new HashSet<ITemplateBinding>());
                }
                removedBindings.get(object).add(binding);
                bindings.remove(binding);
                didRemove = true;
            }
            return didRemove;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean undoRemove(EObject object) {
        boolean didUndo = removedBindings.containsKey(object);
        if (didUndo) {
            bindings.addAll(removedBindings.get(object));
            removedBindings.remove(object);
        }
        return didUndo;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public EvaluationResult validate() {
        return this.evaluationResult;
    }

    /**
	 * Sets the possible bindings.
	 * 
	 * @param possibleBindings
	 *            the found bindings to set.
	 */
    protected void setPossibleBindings(Collection<ITemplateBinding> foundBindings) {
        for (ITemplateBinding iTemplateBinding : foundBindings) {
            this.bindings.add(iTemplateBinding);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Set<Template> getTemplates() {
        Set<Template> set = new HashSet<Template>();
        for (ITemplateBinding binding : bindings) {
            set.addAll(binding.getTemplates());
        }
        return set;
    }
}
