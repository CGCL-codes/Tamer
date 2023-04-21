package org.rubypeople.rdt.refactoring.tests.core.inlinemethod;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.eclipse.jface.text.BadLocationException;
import org.rubypeople.rdt.refactoring.core.inlinemethod.InlineAndRemoveEditProvider;
import org.rubypeople.rdt.refactoring.core.inlinemethod.InlineMethodConditionChecker;
import org.rubypeople.rdt.refactoring.core.inlinemethod.InlineMethodConfig;
import org.rubypeople.rdt.refactoring.core.inlinemethod.TargetClassFinder;
import org.rubypeople.rdt.refactoring.tests.FileTestCase;
import org.rubypeople.rdt.refactoring.tests.FileTestData;

public class InlineMethodTester extends FileTestCase {

    public InlineMethodTester(String fileName) {
        super(fileName);
    }

    @Override
    public void runTest() throws FileNotFoundException, IOException, BadLocationException {
        FileTestData testData = new FileTestData(getName(), ".source.rb", ".result.rb");
        int caretPosition = testData.getIntProperty("pos");
        InlineMethodConfig inlineMethodConfig = new InlineMethodConfig(testData, caretPosition, new TargetClassFinder());
        InlineMethodConditionChecker checker = new InlineMethodConditionChecker(inlineMethodConfig);
        if (!checker.shouldPerform()) {
            fail();
        }
        InlineAndRemoveEditProvider editProvider = new InlineAndRemoveEditProvider(inlineMethodConfig);
        if (testData.hasProperty("remove")) {
            editProvider.setRemove(testData.getBoolProperty("remove"));
        }
        createEditAndCompareResult(testData.getSource(), testData.getExpectedResult(), editProvider);
    }
}
