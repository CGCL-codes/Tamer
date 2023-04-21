package fr.univ_tln.inf9.exaplanning.controleur.facade;

import fr.univ_tln.inf9.exaplanning.api.facade.event.ListAdminListener;
import fr.univ_tln.inf9.exaplanning.api.facade.event.ListBuildingListener;
import fr.univ_tln.inf9.exaplanning.api.facade.event.ListComponentListener;
import fr.univ_tln.inf9.exaplanning.api.facade.event.ListStudentListener;
import fr.univ_tln.inf9.exaplanning.api.facade.event.ListTeacherListener;

/**
 * @author pivi
 *
 */
public abstract class UniversityView implements ListAdminListener, ListBuildingListener, ListComponentListener, ListStudentListener, ListTeacherListener {

    private UniversityController controller = null;

    public UniversityView(UniversityController controller) {
        super();
        this.controller = controller;
    }

    public final UniversityController getController() {
        return controller;
    }

    public abstract void display();

    public abstract void close();
}
