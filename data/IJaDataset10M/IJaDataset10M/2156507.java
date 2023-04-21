package org.omegat.core;

import org.omegat.gui.editor.IEditor;

/**
 * Core initializer for unit tests.
 * 
 * @author Alex Buloichik (alex73mail@gmail.com)
 */
public class TestCoreInitializer {

    public static void initEditor(IEditor editor) {
        Core.editor = editor;
    }
}
