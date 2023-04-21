package net.sf.tacos.demo.partial;

import java.util.Set;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;
import org.apache.tapestry.valid.IValidationDelegate;

public abstract class PartialForms extends BasePage {

    public void storeNote(IRequestCycle cycle) {
        IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
        if (delegate.getHasErrors()) {
            return;
        }
        storeNote(getCurrNote());
        setEditing(getCurrNote().getId(), false);
    }

    public void editNote(IRequestCycle cycle) {
        Long id = (Long) cycle.getServiceParameters()[0];
        setEditing(id, true);
    }

    public void removeNote(IRequestCycle cycle) {
        Long id = (Long) cycle.getServiceParameters()[0];
        setEditing(id, false);
        NoteStore store = getStore();
        store.removeNote(id);
        setStore(store);
    }

    public void addNote(IRequestCycle cycle) {
        Note note = getStore().createNote();
        storeNote(note);
        setEditing(note.getId(), true);
    }

    private void storeNote(Note note) {
        NoteStore store = getStore();
        store.storeNote(note);
        setStore(store);
    }

    private void setEditing(Long noteId, boolean edit) {
        Set editIds = getEditorIds();
        if (edit) {
            editIds.add(noteId);
        } else {
            editIds.remove(noteId);
        }
        setEditorIds(editIds);
    }

    public abstract NoteStore getStore();

    public abstract void setStore(NoteStore store);

    public abstract Set getEditorIds();

    public abstract void setEditorIds(Set edit);

    public abstract Note getCurrNote();
}
