package org.modelversioning.operations.detection.impl;

import java.net.BindException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeLeftTarget;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.metamodel.Side;
import org.eclipse.emf.ecore.EObject;
import org.modelversioning.core.conditions.Template;
import org.modelversioning.core.conditions.engines.ITemplateBinding;
import org.modelversioning.core.conditions.engines.ITemplateBindings;
import org.modelversioning.core.conditions.engines.impl.TemplateBindingImpl;
import org.modelversioning.core.conditions.util.ConditionsUtil;
import org.modelversioning.core.diff.DiffElementTypeComparator;
import org.modelversioning.core.diff.DiffSignature;
import org.modelversioning.core.diff.util.DiffUtil;
import org.modelversioning.core.match.util.MatchUtil;
import org.modelversioning.operations.OperationSignature;
import org.modelversioning.operations.OperationSpecification;
import org.modelversioning.operations.TemplateAffection;
import org.modelversioning.operations.TemplateAffection.AffectionKind;
import org.modelversioning.operations.detection.operationoccurrence.OperationOccurrence;
import org.modelversioning.operations.util.OperationsUtil;

/**
 * A map for binding templates and changes of an {@link OperationSpecification}
 * (through the {@link DiffSignature}) to input changes. This class also
 * calculates these dependencies.
 * 
 * @author <a href="mailto:langer@big.tuwien.ac.at">Philip Langer</a>
 * 
 */
public class OperationChangeMap {

    /**
	 * The diff element comparator.
	 */
    private DiffElementTypeComparator comparator = DiffElementTypeComparator.getInstance();

    /**
	 * The operation signature of this operation change map (elements to map
	 * from).
	 */
    private OperationSignature operationSignature;

    /**
	 * The input signature (elements to map to).
	 */
    private DiffSignature inputSignature;

    /**
	 * The actual change map from operation diff elements to a set of
	 * corresponding input diff elements.
	 */
    private Map<DiffElement, Set<DiffElement>> changeMap = new HashMap<DiffElement, Set<DiffElement>>();

    /**
	 * Match model of the input diff.
	 */
    private MatchModel matchModel;

    /**
	 * Creates and initializes this operation change map with the specified
	 * input and operation signatures.
	 * 
	 * @param operationSignature
	 *            signature of an {@link OperationSpecification}.
	 * @param inputSignature
	 *            input change signature.
	 * @param matchModel
	 *            to use.
	 */
    public OperationChangeMap(OperationSignature operationSignature, DiffSignature inputSignature, MatchModel matchModel) {
        this.operationSignature = operationSignature;
        this.inputSignature = inputSignature;
        this.matchModel = matchModel;
        initializeChangeMap();
    }

    /**
	 * Initializes the change map.
	 */
    private void initializeChangeMap() {
        Assert.isNotNull(this.operationSignature);
        Assert.isNotNull(this.inputSignature);
        List<DiffElement> inputDiffList = inputSignature.getSignatureElements();
        DiffElement previousDiffElement = null;
        int lastCheckedIndex = 0;
        for (DiffElement operationDiffElement : operationSignature.getSignatureElements()) {
            if (previousDiffElement != null && comparator.compare(operationDiffElement, previousDiffElement) == 0) {
                for (DiffElement inputDiffElement : changeMap.get(previousDiffElement)) {
                    addChangeToChangeMap(operationDiffElement, inputDiffElement);
                }
                previousDiffElement = operationDiffElement;
                continue;
            }
            for (; lastCheckedIndex < inputDiffList.size() && comparator.compare(inputDiffList.get(lastCheckedIndex), operationDiffElement) != 0; lastCheckedIndex++) {
            }
            while (lastCheckedIndex < inputDiffList.size() && comparator.compare(inputDiffList.get(lastCheckedIndex), operationDiffElement) == 0) {
                addChangeToChangeMap(operationDiffElement, inputDiffList.get(lastCheckedIndex));
                lastCheckedIndex++;
            }
            previousDiffElement = operationDiffElement;
        }
    }

    /**
	 * Returns the map that maps an operation change to one or more input
	 * changes.
	 * 
	 * @return change map.
	 */
    public Map<DiffElement, Set<DiffElement>> getChangeMap() {
        return changeMap;
    }

    /**
	 * Adds the <code>inputChange</code> and bind it to
	 * <code>operationChange</code>.
	 * 
	 * @param operationChange
	 *            to which the <code>inputChange</code> to add shall be bound.
	 * @param inputChange
	 *            to add.
	 */
    private void addChangeToChangeMap(DiffElement operationChange, DiffElement inputChange) {
        Set<DiffElement> inputDiffSet = null;
        if (!changeMap.containsKey(operationChange)) {
            inputDiffSet = new HashSet<DiffElement>();
            changeMap.put(operationChange, inputDiffSet);
        } else {
            inputDiffSet = changeMap.get(operationChange);
        }
        inputDiffSet.add(inputChange);
    }

    /**
	 * Removes the <code>inputChange</code> from the change map.
	 * 
	 * @param operationChange
	 *            change from operation signature to which
	 *            <code>inputSignature</code> is bound.
	 * @param inputChange
	 *            to remove.
	 */
    @SuppressWarnings("unused")
    private void removeChangeFromChangeMap(DiffElement operationChange, DiffElement inputChange) {
        Set<DiffElement> set = changeMap.get(operationChange);
        set.remove(inputChange);
        if (set.size() < 1) {
            changeMap.remove(operationChange);
        }
    }

    /**
	 * @return the operationSignature
	 */
    public OperationSignature getOperationSignature() {
        return operationSignature;
    }

    /**
	 * @return the inputSignature
	 */
    public DiffSignature getInputSignature() {
        return inputSignature;
    }

    /**
	 * Removes the input changes from this change map that are contained in the
	 * specified <code>operationOccurrence</code>.
	 * 
	 * @param operationOccurrence
	 *            to remove changes from input changes in this map.
	 */
    public void removeChanges(OperationOccurrence operationOccurrence) {
        for (Set<DiffElement> elements : changeMap.values()) {
            elements.removeAll(operationOccurrence.getHiddenChanges());
        }
        Set<DiffElement> operationDiffs = changeMap.keySet();
        Set<DiffElement> operationDiffsToRemove = new HashSet<DiffElement>();
        for (DiffElement operationDiff : operationDiffs) {
            if (changeMap.get(operationDiff).size() < 1) {
                operationDiffsToRemove.add(operationDiff);
            }
        }
        for (DiffElement toRemove : operationDiffsToRemove) {
            changeMap.remove(toRemove);
        }
    }

    /**
	 * Specifies whether the operation in {@link #getOperationSignature()} is
	 * still possible for {@link #getInputSignature()}.
	 * 
	 * @return <code>true</code> if possible, <code>false</code> if not.
	 */
    public boolean isOperationPossible() {
        for (DiffElement diffElement : operationSignature.getSignatureElements()) {
            if (changeMap.get(diffElement) == null || changeMap.get(diffElement).size() < 1) {
                return false;
            }
        }
        return true;
    }

    /**
	 * Specifies whether the operation in {@link #getOperationSignature()} is
	 * still possible for {@link #getInputSignature()} when excluding the
	 * {@link DiffElement}s hidden by the specified occurrences of
	 * <code>operations</code>.
	 * 
	 * @param operations
	 *            operation occurrences to exclude.
	 * 
	 * @return <code>true</code> if possible, <code>false</code> if not.
	 */
    public boolean isOperationPossibleExlcudingOperations(Set<OperationOccurrence> operations) {
        Set<DiffElement> excludingDiffElements = new HashSet<DiffElement>();
        for (OperationOccurrence occurrence : operations) {
            excludingDiffElements.addAll(occurrence.getHiddenChanges());
        }
        for (DiffElement diffElement : operationSignature.getSignatureElements()) {
            if (changeMap.get(diffElement) == null || changeMap.get(diffElement).size() < 1) {
                return false;
            } else {
                Set<DiffElement> diffElements = new HashSet<DiffElement>(changeMap.get(diffElement));
                diffElements.removeAll(excludingDiffElements);
                if (diffElements.size() < 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
	 * Instantiates an empty {@link ITemplateBinding}.
	 * 
	 * @return empty {@link ITemplateBinding}.
	 */
    private ITemplateBinding createEmptyTemplateBinding() {
        return new TemplateBindingImpl();
    }

    /**
	 * Derives a candidate binding for the precondition templates in the set
	 * {@link DiffSignature} ( {@link #setOperationSignature(DiffSignature)})
	 * with the set input signature ({@link #setInputSignature(DiffSignature)}).
	 * 
	 * For each precondition {@link Template} in the
	 * {@link OperationSpecification} in the specified
	 * {@link OperationSignature}, {@link EObject}s on the right side of the
	 * {@link #inputSignature} are searched that are modified by
	 * {@link DiffElement}s in the {@link #inputSignature} according to the
	 * DiffElements in {@link OperationSignature#getAffectingChanges(Template)}.
	 * 
	 * @return candidate binding (all possible template bindings).
	 */
    public ITemplateBinding derivePreConditionCandidateBinding() {
        ITemplateBinding templateBinding = createEmptyTemplateBinding();
        for (Template template : operationSignature.getAffectedPreconditionTemplates()) {
            Map<EObject, Set<TemplateAffection>> inputObjectToChangesMap = new HashMap<EObject, Set<TemplateAffection>>();
            Set<TemplateAffection> affections = operationSignature.getAffectingChanges(template);
            for (TemplateAffection affection : affections) {
                for (DiffElement inputDiffElement : changeMap.get(affection.getDiffElement())) {
                    EObject inputObject = getAffectedObject(affection, inputDiffElement, true);
                    if (inputObject != null) {
                        Set<TemplateAffection> mappedAffections = null;
                        if (!inputObjectToChangesMap.containsKey(inputObject)) {
                            mappedAffections = new HashSet<TemplateAffection>();
                            inputObjectToChangesMap.put(inputObject, mappedAffections);
                        } else {
                            mappedAffections = inputObjectToChangesMap.get(inputObject);
                        }
                        mappedAffections.add(affection);
                    }
                }
            }
            for (Entry<EObject, Set<TemplateAffection>> mapEntry : inputObjectToChangesMap.entrySet()) {
                if (mapEntry.getValue().containsAll(affections)) {
                    templateBinding.add(template, mapEntry.getKey());
                    bindParents(templateBinding, template, mapEntry.getKey());
                }
            }
        }
        return templateBinding;
    }

    /**
	 * Returns the object affected by the specified <code>diffElement</code>
	 * given the specified <code>affection</code>.
	 * 
	 * @param affection
	 *            to use as basis for object retrieval.
	 * @param diffElement
	 *            diff element to get affected object from.
	 * @param rightSide
	 *            get from right side (<code>false</code>) or from left side (
	 *            <code>false</code>).
	 * @return the affected object from the specified side.
	 */
    private EObject getAffectedObject(TemplateAffection affection, DiffElement diffElement, boolean rightSide) {
        EObject inputObject = null;
        switch(affection.getAffectionKind()) {
            case ELEMENT:
                inputObject = rightSide ? DiffUtil.getRightElement(diffElement) : DiffUtil.getLeftElement(diffElement);
                break;
            case PARENT:
                inputObject = rightSide ? DiffUtil.getRightParent(diffElement) : DiffUtil.getLeftParent(diffElement);
                break;
            case TARGET:
                inputObject = rightSide ? DiffUtil.getRightTarget(diffElement) : DiffUtil.getLeftTarget(diffElement);
                break;
            case OPPOSITE_TARGET:
                inputObject = rightSide ? DiffUtil.getLeftTarget(diffElement) : DiffUtil.getRightTarget(diffElement);
                break;
        }
        if (inputObject != null) {
            if (rightSide && Side.RIGHT.equals(MatchUtil.getSide(inputObject, matchModel))) {
                return inputObject;
            } else if (!rightSide && Side.LEFT.equals(MatchUtil.getSide(inputObject, matchModel))) {
                return inputObject;
            } else {
                return MatchUtil.getMatchingObject(inputObject, matchModel);
            }
        }
        return inputObject;
    }

    /**
	 * Extracts a map mapping operation diff elements to truly essential changes
	 * in input diff elements based on the specified
	 * <code>postConditionBinding</code> and adds them to the specified
	 * <code>preConditionChangeMap</code>.
	 * 
	 * TODO we might increase performance by only iterating over templates that
	 * have been added during the operation and not through all (cf. for
	 * (Template template : this.operationSignature
	 * .getAffectedPostconditionTemplates()).
	 * 
	 * @param preConditionBinding
	 *            to derive truly essential changes from.
	 * @param preConditionChangeMap
	 *            change map to extract from.
	 * 
	 * @return truly essential change map.
	 */
    private Map<DiffElement, Set<DiffElement>> addEssentialChangesFromPostconditions(ITemplateBindings postConditionBinding, Map<DiffElement, Set<DiffElement>> preConditionChangeMap) {
        Map<DiffElement, Set<DiffElement>> essentialChangeMap = preConditionChangeMap;
        for (Template template : this.operationSignature.getAffectedPostconditionTemplates()) {
            Set<EObject> boundObjects = postConditionBinding.getBoundObjects(template);
            if (boundObjects == null || boundObjects.size() < 1) {
                throw new IllegalArgumentException("Specified postcondition binding is incomplete");
            }
            for (DiffElement operationDiffElement : changeMap.keySet()) {
                if (operationDiffElement instanceof ModelElementChangeLeftTarget) {
                    for (DiffElement inputAddition : changeMap.get(operationDiffElement)) {
                        if (inputAddition instanceof ModelElementChangeLeftTarget) {
                            EObject inputObject = ((ModelElementChangeLeftTarget) inputAddition).getLeftElement();
                            if (inputObject != null && boundObjects.contains(inputObject)) {
                                Set<DiffElement> set = null;
                                if (!essentialChangeMap.containsKey(operationDiffElement)) {
                                    set = new HashSet<DiffElement>();
                                    essentialChangeMap.put(operationDiffElement, set);
                                } else {
                                    set = essentialChangeMap.get(operationDiffElement);
                                }
                                set.add(inputAddition);
                            }
                        }
                    }
                }
            }
        }
        return essentialChangeMap;
    }

    /**
	 * Returns a map mapping operation diff elements to truly essential changes
	 * in input diff elements based on the specified
	 * <code>preConditionBinding</code>.
	 * 
	 * @param preConditionBinding
	 *            to derive truly essential changes from.
	 * @param regardOnlyElement
	 *            specifies whether only changes should be included, in which
	 *            the bound element acts as <em>ELEMENT</em> and not for
	 *            instance as <em>TARGET</em> and so on.
	 * @return truly essential change map.
	 */
    private Map<DiffElement, Set<DiffElement>> getEssentialChangesFromPreconditions(ITemplateBindings preConditionBinding, boolean regardOnlyElement) {
        Map<DiffElement, Set<DiffElement>> essentialChangeMap = new HashMap<DiffElement, Set<DiffElement>>();
        for (Template template : this.operationSignature.getAffectedPreconditionTemplates()) {
            Set<EObject> boundObjects = preConditionBinding.getBoundObjects(template);
            if (boundObjects == null || boundObjects.size() < 1) {
                throw new IllegalArgumentException("Specified precondition binding is incomplete");
            }
            for (TemplateAffection affection : this.operationSignature.getAffectingChanges(template)) {
                for (DiffElement inputDiffElement : changeMap.get(affection.getDiffElement())) {
                    if (!regardOnlyElement || AffectionKind.ELEMENT.equals(affection.getAffectionKind())) {
                        EObject inputObject = getAffectedObject(affection, inputDiffElement, true);
                        if (inputObject != null && boundObjects.contains(inputObject)) {
                            Set<DiffElement> set = null;
                            if (!essentialChangeMap.containsKey(affection.getDiffElement())) {
                                set = new HashSet<DiffElement>();
                                essentialChangeMap.put(affection.getDiffElement(), set);
                            } else {
                                set = essentialChangeMap.get(affection.getDiffElement());
                            }
                            set.add(inputDiffElement);
                        }
                    }
                }
            }
        }
        return essentialChangeMap;
    }

    /**
	 * Returns a map mapping operation diff elements to truly essential changes
	 * in input diff elements based on the specified
	 * <code>preConditionBinding</code> and <code>postConditionBinding</code>.
	 * 
	 * @param preConditionBinding
	 *            to derive truly essential changes from.
	 * @param preConditionBinding
	 *            to derive truly essential changes from.
	 * @return truly essential change map.
	 */
    private Map<DiffElement, Set<DiffElement>> getEssentialChanges(ITemplateBindings preConditionBinding, ITemplateBindings postConditionBinding) {
        Map<DiffElement, Set<DiffElement>> changesFromPreconditions = getEssentialChangesFromPreconditions(preConditionBinding, true);
        return addEssentialChangesFromPostconditions(postConditionBinding, changesFromPreconditions);
    }

    /**
	 * Returns the essential changes of the specified pre and postcondition
	 * bindings.
	 * 
	 * @param preConditionBinding
	 *            pre condition binding of which changes are requested.
	 * @param postConditionBinding
	 *            post condition binding of which changes are requested.
	 * @return the truly essential changes contained by this change map.
	 */
    public Collection<DiffElement> getChanges(ITemplateBindings preConditionBinding, ITemplateBindings postConditionBinding) {
        Set<DiffElement> changes = new HashSet<DiffElement>();
        Map<DiffElement, Set<DiffElement>> essentialChanges = this.getEssentialChanges(preConditionBinding, postConditionBinding);
        for (DiffElement operationDiffElement : essentialChanges.keySet()) {
            changes.addAll(essentialChanges.get(operationDiffElement));
        }
        return Collections.unmodifiableCollection(changes);
    }

    /**
	 * Derives a post condition template binding on the base of the specified
	 * <code>preConditionBinding</code> and the <code>MatchModel</code>.
	 * 
	 * Basically this method infers a binding using the specified
	 * <code>matchModel</code>. For each unmatched element in this
	 * {@link MatchModel} (i.e., added elements), it tries to infer a binding
	 * using the changes in the map.
	 * 
	 * @param preConditionBinding
	 *            to infer post condition binding from.
	 * @param matchModel
	 *            to infer post condition binding from.
	 * @return inferred post condition template binding.
	 * @throws BindException
	 *             if the binding could not be created for the specified
	 *             preConditionBinding.
	 */
    public ITemplateBinding derivePostConditionBinding(ITemplateBindings preConditionBinding, MatchModel matchModel) throws BindException {
        ITemplateBinding postConditionCandidateBinding = OperationsUtil.derivePostConditionTemplateBinding(preConditionBinding, matchModel, operationSignature.getOperationSpecification());
        Set<Template> unboundTemplates = ConditionsUtil.getUnboundTemplates(postConditionCandidateBinding, operationSignature.getOperationSpecification().getPostconditions());
        if (unboundTemplates.size() == 0) {
            return postConditionCandidateBinding;
        }
        Map<DiffElement, Set<DiffElement>> changesFromPreconditions = getEssentialChangesFromPreconditions(preConditionBinding, false);
        for (Template template : unboundTemplates) {
            Map<EObject, List<TemplateAffection>> inputObjectToAffectionsMap = new HashMap<EObject, List<TemplateAffection>>();
            Set<TemplateAffection> affections = operationSignature.getAffectingChanges(template);
            for (TemplateAffection affection : affections) {
                if (changesFromPreconditions.get(affection.getDiffElement()) == null) {
                    throw new BindException("Post condition binding could not be completed " + "for the specified precondition binding");
                }
                for (DiffElement inputDiffElement : changesFromPreconditions.get(affection.getDiffElement())) {
                    EObject inputObject = getAffectedObject(affection, inputDiffElement, false);
                    if (inputObject != null) {
                        List<TemplateAffection> mappedAffections = null;
                        if (!inputObjectToAffectionsMap.containsKey(inputObject)) {
                            mappedAffections = new ArrayList<TemplateAffection>();
                            inputObjectToAffectionsMap.put(inputObject, mappedAffections);
                        } else {
                            mappedAffections = inputObjectToAffectionsMap.get(inputObject);
                        }
                        mappedAffections.add(affection);
                    }
                }
            }
            for (Entry<EObject, List<TemplateAffection>> mapEntry : inputObjectToAffectionsMap.entrySet()) {
                if (mapEntry.getValue().containsAll(affections)) {
                    postConditionCandidateBinding.add(template, mapEntry.getKey());
                    bindParents(postConditionCandidateBinding, template, mapEntry.getKey());
                }
            }
        }
        return postConditionCandidateBinding;
    }

    /**
	 * Binds the parent objects of <code>eObject</code> to the parents of
	 * <code>template</code> if they are not bound already.
	 * 
	 * @param templateBinding
	 *            to add binding to.
	 * @param eObject
	 *            to bind parents.
	 * @param template
	 *            to bind parents.
	 */
    private void bindParents(ITemplateBinding templateBinding, Template template, EObject eObject) {
        while ((template = template.getParentTemplate()) != null && !templateBinding.getBoundObjects(template).contains(eObject) && (eObject = eObject.eContainer()) != null) {
            templateBinding.add(template, eObject);
        }
    }
}
