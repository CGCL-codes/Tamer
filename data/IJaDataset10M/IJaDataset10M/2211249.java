package org.osmorc.frameworkintegration.impl.knopflerfish;

import org.jetbrains.annotations.NotNull;
import org.osmorc.frameworkintegration.FrameworkRunner;
import org.osmorc.frameworkintegration.impl.AbstractFrameworkIntegrator;
import org.osmorc.frameworkintegration.impl.knopflerfish.ui.KnopflerfishRunPropertiesEditor;
import org.osmorc.run.ui.FrameworkRunPropertiesEditor;

/**
 * Knopflerfish specific implementation of {@link org.osmorc.frameworkintegration.FrameworkIntegrator}.
 *
 * @author Robert F. Beeger (robert@beeger.net)
 */
public class KnopflerfishIntegrator extends AbstractFrameworkIntegrator {

    public KnopflerfishIntegrator(KnopflerfishFrameworkInstanceManager frameworkInstanceManager) {
        super(frameworkInstanceManager);
    }

    @NotNull
    public FrameworkRunner createFrameworkRunner() {
        return new KnopflerfishFrameworkRunner();
    }

    @NotNull
    public String getDisplayName() {
        return FRAMEWORK_NAME;
    }

    @Override
    public FrameworkRunPropertiesEditor createRunPropertiesEditor() {
        return new KnopflerfishRunPropertiesEditor();
    }

    private static final String FRAMEWORK_NAME = "Knopflerfish";
}
