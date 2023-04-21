package org.uasd.jalgor.business;

/**
 *
 * @author Edwin Bratini <edwin.bratini@gmail.com>
 */
public class InvalidOutFileNameException extends InvalidFileNameException {

    public InvalidOutFileNameException() {
    }

    public InvalidOutFileNameException(String message) {
        super(message);
    }
}
