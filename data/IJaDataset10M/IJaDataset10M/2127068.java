package net.sourceforge.turtleweed.syncher;

import net.sourceforge.turtleweed.model.MapDocument;
import net.sourceforge.turtleweed.workspace.IWorkspace;

public interface ISyncher {

    void store(MapDocument md, IWorkspace ws) throws WriteException;
}
