package com.volantis.mcs.eclipse.ab.editors.layout.impl;

import com.volantis.mcs.eclipse.ab.editors.layout.LayoutEditorContextFactory;
import com.volantis.mcs.eclipse.ab.editors.layout.LayoutEditorContext;
import com.volantis.mcs.eclipse.ab.editors.layout.LayoutEditor;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessException;
import org.eclipse.core.resources.IFile;

/**
 * Layout editor context factory for use in standard file-based projects.
 */
public class DefaultLayoutEditorContextFactory extends LayoutEditorContextFactory {

    public LayoutEditorContext createLayoutEditorContext(LayoutEditor editor, IFile file) throws PolicyFileAccessException {
        return new LayoutEditorContext(editor, file);
    }
}
