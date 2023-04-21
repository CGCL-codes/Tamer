package com.rapidminer.operator;

import java.util.List;
import com.rapidminer.operator.ports.PortOwner;
import com.rapidminer.operator.ports.quickfix.QuickFix;

/**
 *  An error in the process setup that can be registered with the operator in which it appears
 *  using {@link Operator#addError(ProcessSetupError)}. Beyond specifying its message, an error
 *  may also provide a collections of quick fixes that solve this problem.
 * 
 * @author Simon Fischer
 *
 */
public interface ProcessSetupError {

    /** Severity levels of ProcessSetupErrors. */
    public enum Severity {

        /** This indicates that the corresponding message is just for information
		 */
        INFORMATION, /** This is an indicator of wrong experiment setup, but
		 *  the process may run nevertheless. */
        WARNING, /** Process will definitely (well, say, most certainly) not run. */
        ERROR
    }

    /** Returns the human readable, formatted message. */
    public String getMessage();

    /** Returns the owner of the port that should be displayed by the GUI to 
	 *  fix the error. */
    public PortOwner getOwner();

    /** If possible, return a list of fixes for the error. */
    public List<? extends QuickFix> getQuickFixes();

    /** Returns the severity of the error. */
    public Severity getSeverity();
}
