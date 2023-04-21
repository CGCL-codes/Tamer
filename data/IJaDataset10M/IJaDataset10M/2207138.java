package jfpsm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

final class CloseSelectedEntitiesAction implements ActionListener {

    private final ProjectManager projectManager;

    CloseSelectedEntitiesAction(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    @Override
    public final void actionPerformed(ActionEvent e) {
        this.projectManager.closeSelectedEntities();
    }
}
