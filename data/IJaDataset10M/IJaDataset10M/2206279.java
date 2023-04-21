package org.gamenet.application.mm8leveleditor.handler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.gamenet.application.mm8leveleditor.lod.LodResource;
import org.gamenet.util.TaskObserver;
import org.gamenet.util.UnimplementedMethodException;

public class ErrorHandler implements LodResourceHandler {

    private String error = null;

    public ErrorHandler(String anError) {
        super();
        error = anError;
    }

    public Component getComponentFor(LodResource lodResource, TaskObserver taskObserver) {
        JTextArea errorTextArea = new JTextArea(error);
        errorTextArea.setForeground(Color.RED);
        Component component = errorTextArea;
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea description = new JTextArea(lodResource.getTextDescription());
        panel.add(description, BorderLayout.PAGE_START);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    public LodResource getUpdatedLodResource() {
        throw new UnimplementedMethodException("getUpdatedLodResource() Unimplemented.");
    }
}
