package ch.headshot.photomap.client.exception;

public class MapperException extends RuntimeException {

    public MapperException() {
        super();
    }

    public MapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapperException(String message) {
        super(message);
    }

    public MapperException(Throwable cause) {
        super(cause);
    }
}
