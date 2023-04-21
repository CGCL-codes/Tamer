package com.nokia.ats4.appmodel.perspective.modeldesign.controller;

import com.nokia.ats4.appmodel.event.EventQueue;
import com.nokia.ats4.appmodel.event.KendoEvent;
import com.nokia.ats4.appmodel.event.KendoEventListener;
import com.nokia.ats4.appmodel.grapheditor.event.ImageCellReSizeEvent;
import com.nokia.ats4.appmodel.grapheditor.event.RemoveImageEvent;
import com.nokia.ats4.appmodel.model.domain.SystemState;
import com.nokia.ats4.appmodel.model.domain.event.ModelObjectPropertyChangedEvent;
import com.nokia.ats4.appmodel.util.Settings;
import com.nokia.ats4.appmodel.util.image.ImageData;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;

/**
 * RemoveImageCommand handles the removing of a picture from a graph cell.
 *
 * @author Kimmo Tuukkanen
 * @version $Revision: 2 $
 */
public class RemoveImageCommand implements KendoEventListener {

    /**
     * Creates a new instance of RemoveImageCommand
     */
    public RemoveImageCommand() {
    }

    @Override
    public void processEvent(KendoEvent event) {
        RemoveImageEvent evt = (RemoveImageEvent) event;
        DefaultGraphCell cell = (DefaultGraphCell) evt.getCell();
        if (cell != null) {
            SystemState state = (SystemState) cell.getUserObject();
            String variant = Settings.getProperty("language.variant");
            ImageData id = state.getImageData();
            id.removeImage(variant);
            ((JGraph) event.getSource()).repaint();
            EventQueue.dispatchEvent(new ModelObjectPropertyChangedEvent(state.getContainingModel(), state, "image"));
            boolean fit = Settings.getBooleanProperty("graph.systemState.fitImageToCell");
            EventQueue.dispatchEvent(new ImageCellReSizeEvent(event.getSource(), fit));
        }
    }
}
