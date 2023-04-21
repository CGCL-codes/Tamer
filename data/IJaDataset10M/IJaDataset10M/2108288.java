package org.sbugs.model.defect;

import java.util.*;

public class Defect extends DefectHeader implements DefectConstants {

    private String description;

    private ArrayList notes = new ArrayList();

    private String emailAddresses;

    private boolean notesSorted = false;

    public Defect(String headline) {
        super(headline);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descr) {
        description = descr;
    }

    public void addNote(DefectNote note) {
        if (!equals(note.getParent())) {
            throw new RuntimeException("Note cannot be added to a defect other than its parent");
        }
        notes.add(note);
        notesSorted = false;
    }

    public List getNotes() {
        if (!notesSorted) {
            Collections.sort(notes);
            notesSorted = true;
        }
        return notes;
    }

    /**
       Should be a semi-colon seprated list of valid email addresses
     **/
    public String getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(String emailAddressesParam) {
        emailAddresses = emailAddressesParam;
    }

    public boolean equals(Object other) {
        if (other instanceof Defect) {
            return super.equals(other);
        }
        return false;
    }
}
