package org.env3d.nbproject;

import java.awt.Image;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectIconAnnotator;
import org.openide.util.ChangeSupport;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author jmadar
 */
@ServiceProvider(service = ProjectIconAnnotator.class)
public class Env3DProjectIconAnnotator implements ProjectIconAnnotator {

    private final ChangeSupport pcs = new ChangeSupport(this);

    private boolean enabled;

    @Override
    public Image annotateIcon(Project p, Image original, boolean openedNode) {
        if (p.getProjectDirectory().getFileObject("env3dkey") != null) {
            String iconPath = "org/env3d/nbproject/env3dicon.png";
            System.out.println("Icon path: " + iconPath);
            return ImageUtilities.loadImage(iconPath);
        } else {
            return original;
        }
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        pcs.addChangeListener(listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        pcs.removeChangeListener(listener);
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
        pcs.fireChange();
    }
}
