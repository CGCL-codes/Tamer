package com.patientis.ejb.reports;

/**
 * One line class description
 *
 */
public class ReportNotFoundException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public ReportNotFoundException() {
    }

    /**
	 * 
	 * @param msg
	 */
    public ReportNotFoundException(String msg) {
        super(msg);
    }
}
