package de.grogra.imp.edit;

import de.grogra.graph.Path;

public class ViewSelectionChanged extends de.grogra.pf.ui.event.EditEvent {

    public ViewSelectionChanged(de.grogra.imp.View view, Path path) {
        super();
        set(view, path);
    }

    public Path getPath() {
        return (Path) getSource();
    }
}
