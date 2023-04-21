package com.faunos.skwish;

/**
 * An exception that is specific to a <code>Segment</code>.
 *
 * @author Babak Farhang
 */
@SuppressWarnings("serial")
public class SegmentException extends SkwishException {

    public SegmentException() {
    }

    public SegmentException(String message) {
        super(message);
    }

    public SegmentException(Throwable cause) {
        super(cause);
    }

    public SegmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
