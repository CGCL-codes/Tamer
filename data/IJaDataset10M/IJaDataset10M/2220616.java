package ch.ethz.dcg.spamato.peerato.peer;

/**
 * Exception when a chunk is asked that we haven't yet received.
 * 
 * @author Michelle Ackermann
 */
public class ChunkNotAvailableException extends Exception {

    public ChunkNotAvailableException(String message) {
        super(message);
    }
}
