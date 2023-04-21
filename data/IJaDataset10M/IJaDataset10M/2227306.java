package at.rc.tacos.client.ui.admin.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import at.rc.tacos.platform.model.SickPerson;

public class SickPersonEditorInput implements IEditorInput {

    private SickPerson person;

    private boolean isNew;

    /**
	 * Default class constructor for a new sick person editor input.
	 * To add a new sick and to open an empty editor the flag is new should be true.
	 * @param sickPerson the sickPerson to manage
	 * @param isNew true if the editor should be empty
	 */
    public SickPersonEditorInput(SickPerson person, boolean isNew) {
        this.person = person;
        this.isNew = isNew;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return null;
    }

    @Override
    public String getName() {
        if (isNew) return "Neuer Patient";
        return person.getLastName();
    }

    @Override
    public IPersistableElement getPersistable() {
        return null;
    }

    @Override
    public String getToolTipText() {
        return "Patient " + person.getLastName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class arg0) {
        return null;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof SickPersonEditorInput) {
            SickPersonEditorInput otherEditor = (SickPersonEditorInput) other;
            return person.equals(otherEditor.getSickPerson());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return person.hashCode();
    }

    /**
	 * Returns the sick person managed by this editor 
	 * @return the sick person
	 */
    public SickPerson getSickPerson() {
        return person;
    }

    /**
	 * Returns whether or not the sick person is new.
	 * @return true if the sick person is created new
	 */
    public boolean isNew() {
        return isNew;
    }
}
