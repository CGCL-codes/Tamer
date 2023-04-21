package org.example.dsl.editor;

import org.openarchitectureware.xtext.AbstractXtextEditorPlugin;
import org.openarchitectureware.xtext.editor.AbstractXtextEditor;
import org.example.dsl.MydslEditorPlugin;

public class MydslEditor extends AbstractXtextEditor {

    public AbstractXtextEditorPlugin getPlugin() {
        return MydslEditorPlugin.getDefault();
    }
}
