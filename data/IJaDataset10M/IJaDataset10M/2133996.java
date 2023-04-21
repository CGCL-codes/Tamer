package edu.uncw.jxpl.reliable.impl.exception;

import edu.uncw.jxpl.reliable.impl.*;

/**
*    Carries out actions if a failure occurs while processing a request.
*    Current implementation will involve repeating the particular piece of work up to the limit specified by
*    the request and setting job as failed (consequently initializing a notification to the client).
*    @author Eric Harris
*/
public class FailureHandler {

    private JxplSegment proc;

    /**
    *    @param proc Process for the job being run.
    */
    public FailureHandler(JxplSegment proc) {
        this.proc = proc;
    }

    public void handle(Exception failure) throws commonj.work.WorkException {
        RJxplRequest req = proc.getRJxplRequest();
        System.out.println(proc.getAttempts() + ", " + req.getCount());
        if (proc.getAttempts() < req.getCount()) {
            proc.updateStatus(ReliableJxplConstants.FAILED);
            proc.schedule();
        } else {
            proc.updateStatus(ReliableJxplConstants.FAILED);
            System.out.println("Ultimate Fail...notify client of failure on request " + proc.getRJxplRequest().getID());
            proc.getRJxplRequest().setFailure(failure.getMessage());
            System.out.println("Scheduling Next Request after failure");
            req = proc.getRJxplRequest().getResourceManager().getNextRequest();
            try {
                req.getNextSegment().schedule();
            } catch (Exception e) {
            }
        }
    }
}
