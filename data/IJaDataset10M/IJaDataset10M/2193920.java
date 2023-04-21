package org.imogene.studio.contrib.ui.navigator;

import org.eclipse.core.resources.IProject;
import org.imogene.studio.contrib.ImogeneStudioPlugin;

public class SynchroShadow extends AbstractShadow {

    public static final String TYPE = "sync";

    public static final String NATURE = "org.imogene.nature.gen.sync";

    public SynchroShadow(IProject parent) {
        super(parent, TYPE);
        setLabel(Messages.SynchroShadow_2);
        setIcon(ImogeneStudioPlugin.getImageDescriptor("icons/serverSynchro.png").createImage());
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
