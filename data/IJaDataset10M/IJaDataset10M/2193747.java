package net.sf.mogbox;

import java.net.URL;
import net.sf.mogbox.renderer.engine.scene.Node;
import org.eclipse.swt.widgets.Composite;

public interface Perspective {

    public Node getNode();

    public String getName();

    public String getDisplayName();

    public URL getIconLocation();

    public void createPanel(Composite parent);
}
