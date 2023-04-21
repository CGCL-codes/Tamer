package ar.com.oddie.persistence.cache;

@SuppressWarnings("serial")
public class DirtyObjectRemovedException extends RuntimeException {

    private CacheElement<?, ?> dirtyElement;

    public DirtyObjectRemovedException(CacheElement<?, ?> element) {
        this.dirtyElement = element;
    }

    public DirtyObjectRemovedException(String message) {
        super(message);
    }

    public DirtyObjectRemovedException(Throwable cause) {
        super(cause);
    }

    public DirtyObjectRemovedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheElement<?, ?> getDirtyElement() {
        return dirtyElement;
    }
}
