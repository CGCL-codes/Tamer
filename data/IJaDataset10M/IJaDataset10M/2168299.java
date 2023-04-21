package com.volantis.mcs.eclipse.ab.editors.layout;

import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessException;
import com.volantis.mcs.eclipse.ab.common.ClassVersionProperties;
import org.eclipse.core.resources.IFile;

/**
 * Factory for creating layout editor contexts.
 */
public abstract class LayoutEditorContextFactory {

    /**
     * The default factory instance to use for all creation of layout editor
     * contexts.
     */
    private static LayoutEditorContextFactory DEFAULT_INSTANCE = (LayoutEditorContextFactory) ClassVersionProperties.getInstance("LayoutEditorContextFactory.class");

    /**
     * Get a default instance of this factory.
     *
     * @return a default instance of this factory
     */
    public static LayoutEditorContextFactory getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Create a layout editor context for this file and editor.
     */
    public abstract LayoutEditorContext createLayoutEditorContext(LayoutEditor editor, IFile file) throws PolicyFileAccessException;
}
