package nl.gridshore.samples.raffle.business.exceptions;

/**
 * Created by IntelliJ IDEA.
 * User: jettro
 * Date: Jan 10, 2008
 * Time: 9:32:23 PM
 * Parent exception for all exceptions thrown in the business layer
 */
public class RaffleBusinessException extends RuntimeException {

    public RaffleBusinessException(String s) {
        super(s);
    }

    public RaffleBusinessException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
