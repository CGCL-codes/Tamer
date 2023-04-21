package com.thoughtworks.fireworks.adapters;

import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.thoughtworks.fireworks.adapters.document.AllEditorsOpenedAdapter;
import com.thoughtworks.fireworks.adapters.document.DocumentChangedAction;
import com.thoughtworks.fireworks.adapters.document.DocumentEventAdapter;
import com.thoughtworks.fireworks.controllers.timer.ConfiguredTimer;
import com.thoughtworks.fireworks.core.ApplicationAdaptee;
import java.awt.*;
import java.awt.event.AWTEventListener;

public class DocumentListenerAdapter implements DocumentListener, AWTEventListener {

    private final ApplicationAdaptee application;

    private final ProjectAdapter project;

    private final ConfiguredTimer timer;

    private final AllEditorsOpenedAdapter editors;

    private Runnable action;

    public DocumentListenerAdapter(ApplicationAdaptee application, ProjectAdapter project, ConfiguredTimer timer, AllEditorsOpenedAdapter editors) {
        this.application = application;
        this.project = project;
        this.timer = timer;
        this.editors = editors;
    }

    public void eventDispatched(AWTEvent event) {
        timer.reschedule();
    }

    public void documentChanged(final DocumentEvent event) {
        if (action != null) {
            application.invokeLater(action);
        }
    }

    public void beforeDocumentChange(final DocumentEvent event) {
        if (documentEvent(event).documentInSourceOrTestContent() && editors.hasNonViewerEditorAndWritable()) {
            action = new DocumentChangedAction(project, event.getDocument(), timer, editors);
        } else {
            action = null;
        }
    }

    private DocumentEventAdapter documentEvent(DocumentEvent event) {
        return new DocumentEventAdapter(event, project);
    }
}
