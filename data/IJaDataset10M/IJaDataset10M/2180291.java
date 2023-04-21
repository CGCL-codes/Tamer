package logop.backing;

import javax.faces.event.ActionEvent;
import logop.PersonHome;
import logop.controller.PersonController;
import logop.model.Person;

public class ManagePersons {

    private PersonController controller;

    private PersonHome personHome;

    private Person person;

    private boolean editMode;

    public void actionCreatePerson(ActionEvent event) {
        person = new Person();
        editMode = true;
    }

    public void actionSavePerson(ActionEvent event) {
        personHome.persist(person);
        person = null;
        editMode = false;
        controller.loadPersons();
    }

    public void actionToggleEditMode(ActionEvent event) {
        editMode = !editMode;
    }

    public PersonController getController() {
        return controller;
    }

    public void setController(PersonController controller) {
        this.controller = controller;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public PersonHome getPersonHome() {
        return personHome;
    }

    public void setPersonHome(PersonHome personHome) {
        this.personHome = personHome;
    }
}
