package net.sourceforge.musicsvg.render;

import net.sourceforge.musicsvg.model.Note;

/**
 *
 * @author Dav
 */
public interface NoteTranslater<T> {

    T translater(Note note);
}
