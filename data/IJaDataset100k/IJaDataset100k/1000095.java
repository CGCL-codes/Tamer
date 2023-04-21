package org.magnification;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.magnification.options.api.MagnificationOptions;
import org.magnify.MagnifierFactory;
import org.magnify.MagnifierInterface;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Accessibility", id = "org.magnification.ToggleMagnification")
@ActionRegistration(displayName = "#CTL_ToggleMagnification")
@ActionReferences({ @ActionReference(path = "Menu/Tools/Magnification", position = -100), @ActionReference(path = "Shortcuts", name = "DS-BACK_QUOTE") })
@Messages("CTL_ToggleMagnification=Toggle Magnification")
public final class ToggleMagnification implements ActionListener {

    private MagnifierInterface magnifier = MagnifierFactory.getDefaultMagnifier();

    public void actionPerformed(ActionEvent e) {
        if (magnifier.isStarted()) {
            magnifier.stop();
            MagnificationOptions.setMagnificationEnabled(false);
        } else {
            magnifier.start();
            magnifier.setFullScreen(MagnificationOptions.isFullscreenEnabled());
            if (!MagnificationOptions.isFullscreenEnabled()) magnifier.setSize(MagnificationOptions.getMagnifierHeight(), MagnificationOptions.getMagnifierWidth());
            MagnificationOptions.setMagnificationEnabled(true);
        }
    }
}
