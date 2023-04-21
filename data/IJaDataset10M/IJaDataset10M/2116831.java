package com.plus.fcentre.jobfilter.web.bean;

/**
 * "Bean" class encapsulating data on a vacancy note.
 * <p>
 * At present this is merely the note text which we could get around by using a
 * simple string value; however introducing this bean class now allows for the
 * possibility of other note information in the future.
 * </p>
 * 
 * @author Steve Jefferson
 */
public class VacancyNoteBean {

    private String text;

    /**
	 * Construct note bean.
	 */
    public VacancyNoteBean() {
    }

    /**
	 * Retrieve note text.
	 * 
	 * @return note text.
	 */
    public String getText() {
        return text;
    }

    /**
	 * Update note text.
	 * 
	 * @param text note text.
	 */
    public void setText(String text) {
        this.text = text;
    }
}
