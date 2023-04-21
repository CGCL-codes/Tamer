package org.unicore.outcome;

import org.unicore.ajo.FileCheck;

/**
 * The result of executing a {@link org.unicore.ajo.FileCheck} which
 * adds the result of the tests and the actual values of the tested file.
 *
 * @author D. Snelling (Fujitsu Laboratories of Europe)
 * @author S. van den Berghe (Fujitsu Laboratories of Europe)
 * 
 * @since AJO 3
 *
 * @version $Id: FileCheck_Outcome.java,v 1.3 2004/06/06 18:37:24 svenvdb Exp $
 * 
 **/
public class FileCheck_Outcome extends AbstractTask_Outcome implements Decision {

    static final long serialVersionUID = 400;

    public static String SATISFIED = "satisfied";

    public static String NOT_SATISFIED = "not satisfied";

    public FileCheck_Outcome() {
        this(null, null);
    }

    /**
     * Create a new FileCheck_Outcome.
     * <p>
     * This will hold the standard information from {@link Outcome}.
     *
     * @param source The FileCheck for which this Outcome will hold the results.
     * @param initial_status The status of the FileCheck at the time that the Outcome is created.
     *
     **/
    public FileCheck_Outcome(FileCheck source, AbstractActionStatus initial_status) {
        super(source, initial_status);
    }

    private XFile file;

    public void setFile(XFile file) {
        this.file = file;
    }

    /**
     *
     * @return The actual values for the file
     *
     * @since AJO 4.0
     *
     **/
    public XFile getFile() {
        return file;
    }

    private String decision = null;

    /**
     * Return the decision produced by the FileCheck's execution.
     *
     * @return FileCheck_Outcome.SATISFIED or FileCheck_Outcome.NOT_SATISFIED or
     *         null (never executed, failure in incarnation)
     *
     * @since AJO 4.0
     *
     **/
    public String getDecision() {
        return decision;
    }

    /**
     * The conditions set on the file at the time of the FileCheck's 
     * execution were satisfied.
     *
     * @since AJO 4.0
     *
     **/
    public void satisfied() {
        decision = SATISFIED;
    }

    /**
     * The conditions set on the file at the time of the FileCheck's 
     * execution were not satisfied.
     *
     * @since AJO 4.0
     *
     **/
    public void notsatisfied() {
        decision = NOT_SATISFIED;
    }
}
