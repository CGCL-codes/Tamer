package blue.soundObject;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.lang.text.StrBuilder;
import blue.utility.ObjectUtilities;

/**
 * Title: blue Description: an object composition environment for csound
 * Copyright: Copyright (c) 2001 Company: steven yi music
 * 
 * @author steven yi
 * @version 1.0
 */
public class NoteList extends ArrayList implements Cloneable {

    public NoteList() {
        super();
    }

    public void addNote(Note note) {
        if (note != null) {
            super.add(note);
        }
    }

    public Note getNote(int i) {
        return (Note) super.get(i);
    }

    public final void sort() {
        Collections.sort(this);
    }

    public final void merge(NoteList notes) {
        if (notes == null) {
            return;
        }
        this.addAll(notes);
    }

    public final String toString() {
        if (this.size() == 0) {
            return "";
        }
        StrBuilder tempScore = new StrBuilder();
        String firstNote = this.getNote(0).toString();
        if (this.size() == 1) {
            return firstNote + "\n";
        }
        String[] str = new String[this.size()];
        str[0] = firstNote;
        int strSize = str[0].length();
        for (int i = 1; i < this.size(); i++) {
            str[i] = this.getNote(i).toString();
            strSize += str[i].length() + 1;
        }
        tempScore.ensureCapacity(strSize);
        tempScore.appendWithSeparators(str, "\n");
        return tempScore.toString();
    }

    public Object clone() {
        return ObjectUtilities.clone(this);
    }
}
