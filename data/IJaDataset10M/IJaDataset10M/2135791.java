package org.fb4j.notes.impl;

import org.fb4j.impl.BooleanMethodCallBase;
import org.fb4j.notes.Note;

/**
 * @author Mino Togna
 * 
 */
public class EditNoteMethodCall extends BooleanMethodCallBase {

    /**
	 * @param method
	 */
    public EditNoteMethodCall(Note note) {
        super("notes.edit");
        setParameter("uid", String.valueOf(note.getUserId()));
        setParameter("note_id", String.valueOf(note.getId()));
        setParameter("title", note.getTitle());
        setParameter("content", note.getContent());
    }
}
