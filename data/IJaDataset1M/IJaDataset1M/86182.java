package com.oat;

/**
 * Description: 
 *  
 * Date: 03/09/2007<br/>
 * @author Jason Brownlee 
 *
 * <br/>
 * <pre>
 * Change History
 * ----------------------------------------------------------------------------
 * 
 * </pre>
 */
public class SolutionEvaluationException extends RuntimeException {

    public SolutionEvaluationException() {
    }

    public SolutionEvaluationException(String message) {
        super(message);
    }

    public SolutionEvaluationException(Throwable cause) {
        super(cause);
    }

    public SolutionEvaluationException(String message, Throwable cause) {
        super(message, cause);
    }
}
