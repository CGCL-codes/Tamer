package com.visitrend.ndvis.actions;

import com.visitrend.ndvis.gui.Zoomable;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.actions.Presenter;

/**
 * The only difference between this class and ZoomOut is the zoomFactor, name,
 * icon, and cursor. I've kept them separate for clarity and in keeping with the
 * general PlugInINF design where the PlugInINF contains everything it needs for the
 * PlugInAction to wrap around it.
 * 
 * @author John T. Langton - jlangton at visitrend dot com
 * 
 * 
 */
@ActionID(id = "com.visitrend.ndvis.actions.ZoomInAction", category = "NDVisActions")
@ActionRegistration(displayName = "Zoom In")
@ActionReference(path = "Toolbars/Zoom", position = 60)
public class ZoomInAction extends AbstractAction implements Presenter.Toolbar {

    @Override
    public void actionPerformed(ActionEvent e) {
        assert false;
    }

    @Override
    public Component getToolbarPresenter() {
        return new ZoomableModeButton(Zoomable.Mode.IN, "Zoom In");
    }
}
