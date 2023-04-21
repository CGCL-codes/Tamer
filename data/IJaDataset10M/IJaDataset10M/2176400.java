package org.qtitools.qti.xxx.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.qtitools.qti.ext.LifecycleEventType;
import org.qtitools.qti.ext.LifecycleListener;
import org.qtitools.qti.node.expression.Expression;
import org.qtitools.qti.node.item.AssessmentItem;
import org.qtitools.qti.node.outcome.declaration.OutcomeDeclaration;
import org.qtitools.qti.node.outcome.processing.OutcomeProcessing;
import org.qtitools.qti.node.result.AssessmentResult;
import org.qtitools.qti.node.result.OutcomeVariable;
import org.qtitools.qti.node.result.TestResult;
import org.qtitools.qti.node.shared.VariableDeclaration;
import org.qtitools.qti.node.shared.declaration.DefaultValue;
import org.qtitools.qti.node.test.AbstractPart;
import org.qtitools.qti.node.test.AssessmentItemRef;
import org.qtitools.qti.node.test.AssessmentSection;
import org.qtitools.qti.node.test.AssessmentTest;
import org.qtitools.qti.node.test.ControlObject;
import org.qtitools.qti.node.test.TestPart;
import org.qtitools.qti.node.test.TimeLimit;
import org.qtitools.qti.value.DurationValue;
import org.qtitools.qti.value.FloatValue;
import org.qtitools.qti.value.NullValue;
import org.qtitools.qti.value.Value;
import org.qtitools.qti.xxx.state.AssessmentItemRefState;
import org.qtitools.qti.xxx.state.AssessmentItemState;
import org.qtitools.qti.xxx.state.AssessmentTestState;
import org.qtitools.qti.xxx.state.ControlObjectState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FIXME: Document this!
 */
public final class AssessmentTestLogic implements RuntimeContext {

    private static final long serialVersionUID = 4123213286542020535L;

    protected static Logger logger = LoggerFactory.getLogger(AssessmentTestLogic.class);

    private final AssessmentTest test;

    private final AssessmentTestState testState;

    private final List<LifecycleListener> lifecycleListeners;

    private final Map<String, Value> expressionValues;

    public AssessmentTestLogic(AssessmentTest assessmentTest, AssessmentTestState assessmentTestState) {
        this.test = assessmentTest;
        this.testState = assessmentTestState;
        this.lifecycleListeners = new ArrayList<LifecycleListener>();
        this.expressionValues = new TreeMap<String, Value>();
    }

    public AssessmentTest getAssessmentTest() {
        return test;
    }

    public AssessmentTestState getAssessmentTestState() {
        return testState;
    }

    public void addLifecycleListener(LifecycleListener listener) {
        lifecycleListeners.add(listener);
    }

    public void removeLifecycleListener(LifecycleListener listener) {
        lifecycleListeners.remove(listener);
    }

    public void fireLifecycleEvent(LifecycleEventType eventType) {
        for (LifecycleListener l : lifecycleListeners) {
            l.lifecycleEvent(eventType);
        }
    }

    public List<LifecycleListener> getLifecycleListeners() {
        return lifecycleListeners;
    }

    public Value getExpressionValue(Expression expression) {
        return expressionValues.get(expression.getXPath());
    }

    public void setExpressionValue(Expression expression, Value value) {
        expressionValues.put(expression.getXPath(), value);
    }

    public AssessmentItemRefLogic getItemRefLogic(AssessmentItemRef assessmentItemRef) {
        return new AssessmentItemRefLogic(this, assessmentItemRef, testState.getAssessmentItemRefState(assessmentItemRef));
    }

    /**
     * Returns an unmodifiable map of outcome identifiers to outcome values.
     * 
     * @return Unmodifiable map of outcome values. 
     */
    public Map<String, Value> getOutcomeValues() {
        List<OutcomeDeclaration> declarations = test.getOutcomeDeclarations();
        Map<String, Value> values = new HashMap<String, Value>();
        for (OutcomeDeclaration declaration : declarations) {
            values.put(declaration.getIdentifier(), testState.getOutcomeValue(declaration));
        }
        return Collections.unmodifiableMap(values);
    }

    public VariableDeclaration lookupDeclaration(String identifier) {
        return test.getOutcomeDeclaration(identifier);
    }

    public Value getEffectiveDefaultValue(String identifier) {
        VariableDeclaration declaration = lookupDeclaration(identifier);
        DefaultValue defaultValue = declaration.getDefaultValue();
        Value result = null;
        if (defaultValue != null) {
            result = defaultValue.evaluate();
        }
        return result;
    }

    public Value computeInitialValue(String identifier) {
        Value defaultValue = getEffectiveDefaultValue(identifier);
        if (defaultValue != null) {
            return defaultValue;
        }
        return new NullValue();
    }

    public Value computeInitialValue(OutcomeDeclaration declaration) {
        return computeInitialValue(declaration.getIdentifier());
    }

    public void resetOutcomeValue(OutcomeDeclaration declaration) {
        testState.setOutcomeValue(declaration, computeInitialValue(declaration));
    }

    public void initialize() {
        initialize(test);
    }

    /** (This is pulled from the the initialize() methods that used to be in ControlObject hierarchy */
    private void initialize(ControlObject controlObject) {
        for (AbstractPart child : controlObject.getChildren()) {
            initialize(child);
        }
        if (controlObject instanceof AssessmentTest) {
            initializeTest((AssessmentTest) controlObject);
        } else if (controlObject instanceof AssessmentSection) {
            initialiseSection((AssessmentSection) controlObject);
        } else if (controlObject instanceof TestPart) {
            initialiseTestPart((TestPart) controlObject);
        } else if (controlObject instanceof AssessmentItemRef) {
            initializeItemRef((AssessmentItemRef) controlObject);
        }
        for (AbstractPart child : controlObject.getChildren()) {
            initialize(child);
        }
    }

    private void initializeTest(AssessmentTest test) {
        testState.reset();
        for (OutcomeDeclaration outcomeDeclaration : test.getOutcomeDeclarations()) {
            resetOutcomeValue(outcomeDeclaration);
        }
    }

    private void initialiseSection(AssessmentSection section) {
        if (section.getSelection() != null) {
            section.getSelection().process();
        }
        if (section.getOrdering() != null) {
            section.getOrdering().process();
        }
        testState.putControlObjectState(new ControlObjectState(testState, section.getIdentifier()));
    }

    private void initialiseTestPart(TestPart testPart) {
        testState.putControlObjectState(new ControlObjectState(testState, testPart.getIdentifier()));
    }

    private void initializeItemRef(AssessmentItemRef itemRef) {
        if (itemRef.getItem() == null) {
            File sourceFile = new File(test.getSourceFile().getParentFile(), itemRef.getHref().getPath());
            AssessmentItem item = new AssessmentItem();
            item.load(sourceFile);
            itemRef.setItem(item);
            AssessmentItemState itemState = new AssessmentItemState();
            AssessmentItemRefState itemRefState = new AssessmentItemRefState(testState, itemState, itemRef.getIdentifier());
            testState.putControlObjectState(itemRefState);
            itemState.setTimeRecord(itemRefState.getTimeRecord());
        }
    }

    /**
	 * Evaluates outcome processing.
	 * <ol>
	 * <li>Resets outcomes to default values.</li>
	 * <li>Evaluates outcome processing.</li>
	 * </ol>
	 */
    public void processOutcome() {
        logger.info("Test outcome processing starting");
        for (OutcomeDeclaration outcomeDeclaration : test.getOutcomeDeclarations()) {
            resetOutcomeValue(outcomeDeclaration);
        }
        OutcomeProcessing outcomeProcessing = test.getOutcomeProcessing();
        if (outcomeProcessing != null) {
            outcomeProcessing.evaluate(this);
        }
        logger.info("Test outcome processing finished");
    }

    /**
	 * Returns current result of this test (only test itself, no items).
	 *
	 * @param parent parent of created result
	 * @return current result of this test (only test itself, no items)
	 */
    public TestResult getTestResult(AssessmentResult parent) {
        TestResult result = new TestResult(parent);
        result.setIdentifier(test.getIdentifier());
        result.setDateStamp(new Date());
        for (OutcomeDeclaration declaration : test.getOutcomeDeclarations()) {
            Value declarationValue = testState.getOutcomeValue(declaration);
            OutcomeVariable variable = new OutcomeVariable(result, declaration, null, declarationValue);
            result.getItemVariables().add(variable);
        }
        result.getItemVariables().add(new OutcomeVariable(result, AssessmentItemRef.VARIABLE_DURATION_NAME, new DurationValue(getDuration(test) / 1000.0)));
        for (TestPart testPart : test.getTestParts()) {
            processDuration(result, testPart);
        }
        return result;
    }

    private void processDuration(TestResult result, AbstractPart parent) {
        if (!(parent instanceof AssessmentItemRef)) {
            String identifier = parent.getIdentifier() + "." + AssessmentItemRef.VARIABLE_DURATION_NAME;
            DurationValue duration = new DurationValue(getDuration(parent) / 1000.0);
            result.getItemVariables().add(new OutcomeVariable(result, identifier, duration));
        }
        for (AbstractPart child : parent.getChildren()) {
            processDuration(result, child);
        }
    }

    /**
	 * Returns current result of whole assessment (test and all its items).
	 *
	 * @return current result of whole assessment (test and all its items)
	 */
    public AssessmentResult getAssessmentResult() {
        AssessmentResult result = new AssessmentResult();
        result.setTestResult(getTestResult(result));
        List<AssessmentItemRef> itemRefs = test.lookupItemRefs(null);
        int sequenceIndex = 1;
        for (AssessmentItemRef itemRef : itemRefs) {
            result.getItemResults().addAll(getItemRefLogic(itemRef).getItemResult(result, sequenceIndex++, null));
        }
        return result;
    }

    public boolean isPresented(ControlObject controlObject) {
        if (controlObject instanceof AssessmentItemRef) {
            return getAssessmentTestState().getAssessmentItemRefState((AssessmentItemRef) controlObject).isPresented();
        } else {
            for (ControlObject child : controlObject.getChildren()) {
                if (isPresented(child)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
	 * Gets total number of item references of this control object.
	 *
	 * @return total number of item references of this control object
	 */
    public int getTotalCount(ControlObject controlObject) {
        int result;
        if (controlObject instanceof AssessmentItemRef) {
            result = 1;
        } else {
            result = 0;
            for (ControlObject child : controlObject.getChildren()) {
                result += getTotalCount(child);
            }
        }
        return result;
    }

    public int getPresentedCount(ControlObject controlObject) {
        int result;
        if (controlObject instanceof AssessmentItemRef) {
            AssessmentItemRefState itemRefState = testState.getAssessmentItemRefState(((AssessmentItemRef) controlObject));
            result = itemRefState.isPresented() ? 1 : 0;
        } else {
            result = 0;
            for (ControlObject child : controlObject.getChildren()) {
                result += getPresentedCount(child);
            }
        }
        return result;
    }

    public int getFinishedCount(ControlObject controlObject) {
        int result;
        if (controlObject instanceof AssessmentItemRef) {
            result = 1;
        } else {
            result = 0;
            for (ControlObject child : controlObject.getChildren()) {
                result += getPresentedCount(child);
            }
        }
        return result;
    }

    public long getTotalTime(ControlObject controlObject) {
        long result;
        if (controlObject instanceof AssessmentItemRef) {
            AssessmentItemRefState itemRefState = testState.getAssessmentItemRefState(((AssessmentItemRef) controlObject));
            return itemRefState.getTimeRecord().getActualTotal();
        } else {
            result = 0;
            for (ControlObject child : controlObject.getChildren()) {
                result += getTotalTime(child);
            }
        }
        return result;
    }

    public long getResponseTime(ControlObject controlObject) {
        long result;
        if (controlObject instanceof AssessmentItemRef) {
            AssessmentItemRefState itemRefState = testState.getAssessmentItemRefState(((AssessmentItemRef) controlObject));
            return itemRefState.getTimeRecord().getActualDuration();
        } else {
            result = 0;
            for (ControlObject child : controlObject.getChildren()) {
                result += getResponseTime(child);
            }
        }
        return result;
    }

    public long getDuration(ControlObject controlObject) {
        return getResponseTime(controlObject);
    }

    /**
	 * DM-NOTE: This used to be ControlObject#lookupValue(), which was overridden in {@link AssessmentItemRef} 
	 * @param controlObject
	 * @param identifier
	 * @return
	 */
    public Value lookupValue(ControlObject controlObject, String identifier) {
        Value result = null;
        if (identifier != null) {
            if (controlObject instanceof AssessmentItemRef) {
                AssessmentItemRefLogic itemRefLogic = getItemRefLogic((AssessmentItemRef) controlObject);
                result = itemRefLogic.lookupValue(identifier);
            } else {
                if (identifier.equals(ControlObject.VARIABLE_DURATION_NAME)) {
                    result = new FloatValue(getDuration(controlObject) / 1000.0);
                }
            }
        }
        return result;
    }

    /**
	 * Returns true if time used by this control object is higher or equal than minimum time limit; false otherwise.
	 * <p>
	 * Time used by this control object is calculated as:
	 * <ul>
	 * <li>If this control object is {@code AssessmentItemRef} method {@code getDuration} is used.</li>
	 * <li>If this control object if not {@code AssessmentItemRef} method {@code getTotalTime} is used.</li>
	 * </ul>
	 * <p>
	 * This method is not implemented and returns always true.
	 *
	 * @return true if time used by this control object is higher or equal than minimum time limit; false otherwise
	 */
    public boolean passMinimumTimeLimit(ControlObject controlObject) {
        return true;
    }

    /**
	 * Returns true if time used by this control object is lower or equal than maximum time limit; false otherwise.
	 * This method checks first this control object and then recursively all its parents
	 * and returns true only if every tested object passed check.
	 * <p>
	 * Time used by this control object is calculated as:
	 * <ul>
	 * <li>If this control object is {@code AssessmentItemRef} method {@code getDuration} is used.</li>
	 * <li>If this control object if not {@code AssessmentItemRef} method {@code getTotalTime} is used.</li>
	 * </ul>
	 * <p>
	 * This method is used for check if item can be shown to the user.
	 *
	 * @return true if time used by this control object is lower or equal than maximum time limit; false otherwise
	 */
    public boolean passMaximumTimeLimit(ControlObject controlObject) {
        TimeLimit timeLimit = controlObject.getTimeLimit();
        if (timeLimit != null && timeLimit.getMaximumMillis() != null) {
            if (getDuration(controlObject) >= timeLimit.getMaximumMillis()) {
                return false;
            }
        }
        return (controlObject.getParent() != null) ? passMaximumTimeLimit(controlObject.getParent()) : true;
    }
}
