package org.hansel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashSet;
import org.junit.internal.runners.InitializationError;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;

public class CoverageRunner extends Suite {

    private ProbeTable probeTable;

    private Class[] coveredClasses;

    private Description desc;

    private Description coverageDesc;

    private boolean initialized;

    /**
     * The <code>CoverClasses</code> annotation specifies the classes to be covered
     * when the test suite is run.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface CoverClasses {

        public Class[] value();
    }

    /**
     * Internal use only.
     */
    public CoverageRunner(Class<?> klass) throws InitializationError {
        super(klass);
        this.probeTable = new ProbeTable(null);
        this.coveredClasses = getCoveredClasses(klass);
        this.initialized = false;
    }

    private static Class[] getCoveredClasses(Class<?> klass) throws InitializationError {
        CoverClasses annotation = klass.getAnnotation(CoverClasses.class);
        if (annotation == null) throw new InitializationError(String.format("class '%s' must have a CoverClasses annotation", klass.getName()));
        return annotation.value();
    }

    private HashSet<String> toStringSet(Class[] classes) {
        HashSet<String> coveredClasses = new HashSet<String>();
        for (Class clazz : classes) {
            coveredClasses.add(clazz.getName());
        }
        return coveredClasses;
    }

    @Override
    public void run(RunNotifier notifier) {
        init();
        CheckFailureRunListener listener = new CheckFailureRunListener();
        notifier.addListener(listener);
        super.run(notifier);
        shutdown(notifier, listener.hasFailures());
    }

    protected void init() {
        if (initialized) {
            return;
        }
        try {
            ProbeTable.setProbeTable(probeTable);
            Startup.init(toStringSet(coveredClasses));
            initialized = true;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    protected void shutdown(RunNotifier result, boolean hasErrors) {
        try {
            loadClasses();
            Startup.tearDown();
            result.fireTestStarted(getCoverageDescription());
            probeTable.run(result, getCoverageDescription(), hasErrors);
        } catch (Exception e) {
            result.fireTestFailure(new Failure(getCoverageDescription(), e));
        } finally {
            result.fireTestFinished(getCoverageDescription());
        }
    }

    private void loadClasses() {
        for (Class clazz : coveredClasses) {
            clazz.getDeclaredFields();
        }
    }

    public Description getCoverageDescription() {
        if (coverageDesc == null) {
            coverageDesc = Description.createSuiteDescription("Coverage");
        }
        return coverageDesc;
    }

    @Override
    public Description getDescription() {
        init();
        if (this.desc == null) {
            desc = super.getDescription();
            desc.addChild(getCoverageDescription());
            try {
                probeTable.addProbeDescriptions(getCoverageDescription());
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }
        return desc;
    }
}
