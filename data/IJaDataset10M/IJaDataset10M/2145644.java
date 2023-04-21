package com.sri.emo.wizard.completion.management;

import java.util.Iterator;
import java.util.Set;
import com.jcorporate.expresso.core.controller.ErrorCollection;
import com.sri.emo.wizard.completion.EmoCompletionWizard;
import com.sri.emo.wizard.completion.model.CompletionBean;
import com.sri.emo.wizard.completion.model.CompletionPartsBean;
import com.sri.emo.wizard.completion.model.FieldCompletion;

/**
 * A test fixture for creating new completion wizards.
 * @author Michael Rimov
 */
public class NewCompletionFixtureTemplate extends BaseCompletionFixtureTemplate {

    private final int targetNodeId;

    public NewCompletionFixtureTemplate(String stateName, int targetNodeId) {
        super(stateName);
        this.targetNodeId = targetNodeId;
    }

    protected void setUp() throws Exception {
        super.setUp();
        CompletionBean beanToCompareTo = new CompletionBean();
        beanToCompareTo.setTargetId(targetNodeId);
        ErrorCollection ec = new ErrorCollection();
        beanToCompareTo.initializeFromNodeId(ec);
        if (ec.getErrorCount() > 0) {
            throw new Exception("Error initializing test fixture");
        }
        Set allParts = beanToCompareTo.getCompletionParts();
        if (allParts.size() < 4) {
            throw new Exception("Error initializing test fixture. Completion Parts are: " + allParts.size());
        }
        beanToCompareTo.setWizardTitle("Test Wizard");
        beanToCompareTo.setWizardClass(EmoCompletionWizard.class);
        beanToCompareTo.setSummary("This is a test wizard");
        Iterator partIterator = allParts.iterator();
        CompletionPartsBean onePart = (CompletionPartsBean) partIterator.next();
        onePart.setFieldCompletion(FieldCompletion.WIZARD);
        onePart = (CompletionPartsBean) partIterator.next();
        onePart.setFieldCompletion(FieldCompletion.FIXED);
        onePart = (CompletionPartsBean) partIterator.next();
        onePart.setFieldCompletion(FieldCompletion.WIZARD);
        onePart = (CompletionPartsBean) partIterator.next();
        onePart.setFieldCompletion(FieldCompletion.WIZARD);
        setCompletionBean(beanToCompareTo);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
