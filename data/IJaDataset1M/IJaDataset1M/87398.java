package org.rubypeople.rdt.refactoring.tests.core.inlinelocal.conditionchecks;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.rubypeople.rdt.refactoring.core.inlinelocal.InlineLocalConditionChecker;
import org.rubypeople.rdt.refactoring.core.inlinelocal.InlineLocalConfig;
import org.rubypeople.rdt.refactoring.core.inlinelocal.LocalVariableInliner;
import org.rubypeople.rdt.refactoring.tests.FileTestData;
import org.rubypeople.rdt.refactoring.tests.RefactoringConditionTestCase;

public class InlineLocalConditionTester extends RefactoringConditionTestCase {

    private InlineLocalConfig config;

    private FileTestData testData;

    public InlineLocalConditionTester(String fileName) {
        super(fileName);
    }

    @Override
    public void runTest() throws FileNotFoundException, IOException {
        testData = new FileTestData(getName(), ".test_source", ".test_source");
        config = new InlineLocalConfig(testData, testData.getIntProperty("cursorPosition"));
        InlineLocalConditionChecker checker = new InlineLocalConditionChecker(config);
        checkConditions(checker, testData);
    }

    @Override
    protected void createEditProviderAndSetUserInput() {
        new LocalVariableInliner(config);
        if (testData.hasProperty("newName")) {
            config.setReplaceTempWithQuery(true);
            config.setNewMethodName(testData.getProperty("newName"));
        }
    }
}
