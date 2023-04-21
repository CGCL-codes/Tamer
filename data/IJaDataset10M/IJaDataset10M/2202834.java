package br.com.infotec.jbee.core.junit.runner;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import br.com.infotec.jbee.core.builder.HistoryFile;
import br.com.infotec.jbee.core.builder.HistoryBuilder;
import br.com.infotec.jbee.core.junit.executor.HistoryExecutor;

/**
 * Class that extends Runner implements and execution of JUnit tests
 * 
 * @author Carlos Alberto (euprogramador@gmail.com)
 * @since 1.0
 */
public class JBeeRunner extends Runner {

    private HistoryFile history;

    private Description mainDescription;

    private Class<?> testClass;

    public JBeeRunner(Class<?> testClass) {
        this.testClass = testClass;
        this.history = new HistoryBuilder().forTestClass(testClass).build();
        this.mainDescription = Description.createSuiteDescription(testClass.getCanonicalName());
        mainDescription.addChild(Description.createTestDescription(testClass, "testStory"));
    }

    public Description getDescription() {
        return this.mainDescription;
    }

    public void run(RunNotifier notifier) {
        for (Description description : mainDescription.getChildren()) {
            if (history == null) {
                notifier.fireTestFailure(new Failure(description, new RuntimeException("Story not found in file")));
                return;
            }
            notifier.fireTestStarted(description);
            new HistoryExecutor().forDescription(description).forNotifier(notifier).forTestClass(testClass).executeHistory(history);
            notifier.fireTestFinished(description);
        }
    }
}
