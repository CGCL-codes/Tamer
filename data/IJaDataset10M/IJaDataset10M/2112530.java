package org.spockframework.runtime.extension.builtin;

import java.io.IOException;
import org.spockframework.runtime.AbstractRunListener;
import org.spockframework.runtime.SpecRunHistory;
import org.spockframework.runtime.extension.IGlobalExtension;
import org.spockframework.runtime.model.*;
import spock.config.RunnerConfiguration;

/**
 * Inspired from JUnit's MaxCore.
 */
public class OptimizeRunOrderExtension implements IGlobalExtension {

    private RunnerConfiguration configuration;

    public void visitSpec(SpecInfo spec) {
        if (!configuration.optimizeRunOrder) return;
        final SpecRunHistory history = new SpecRunHistory(spec.getReflection().getName());
        safeLoadFromDisk(history);
        history.sortFeatures(spec);
        spec.addListener(new AbstractRunListener() {

            long specStarted;

            long featureStarted;

            boolean errorOccurred;

            @Override
            public void beforeSpec(SpecInfo spec) {
                specStarted = System.nanoTime();
            }

            @Override
            public void beforeFeature(FeatureInfo feature) {
                featureStarted = System.nanoTime();
                errorOccurred = false;
            }

            @Override
            public void afterFeature(FeatureInfo feature) {
                history.collectFeatureData(feature, System.nanoTime() - featureStarted, errorOccurred);
            }

            @Override
            public void error(ErrorInfo error) {
                errorOccurred = true;
            }

            @Override
            public void afterSpec(SpecInfo spec) {
                history.collectSpecData(spec, System.nanoTime() - specStarted);
                safeSaveToDisk(history);
            }
        });
    }

    private void safeLoadFromDisk(SpecRunHistory history) {
        try {
            history.loadFromDisk();
        } catch (IOException ignored) {
        }
    }

    private void safeSaveToDisk(SpecRunHistory history) {
        try {
            history.saveToDisk();
        } catch (IOException ignored) {
        }
    }
}
