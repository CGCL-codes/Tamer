package org.imogene.studio.contrib.ui.navigator;

import org.eclipse.core.resources.IProject;
import org.imogene.studio.contrib.ImogeneStudioPlugin;

public class RcpShadow extends AbstractShadow {

    public static final String TYPE = "rcp";

    public static final String NATURE = "org.imogene.nature.gen.rcp";

    public RcpShadow(IProject parent) {
        super(parent, TYPE);
        setLabel("Desktop(RCP)");
        setIcon(ImogeneStudioPlugin.getImageDescriptor("icons/eclipse.png").createImage());
    }

    @Override
    public Object[] getChildren() {
        return getProjects(NATURE).toArray();
    }

    @Override
    public boolean hasChildren() {
        return true;
    }
}
