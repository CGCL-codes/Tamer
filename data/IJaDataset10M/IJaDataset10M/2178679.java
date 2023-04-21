package net.exclaimindustries.fotobilder;

/**
 * Exception thrown if the GalleryTree structure is ever determined to be in
 * an unstable or unuseable state.  This can include things like an unparented
 * circular reference that can't find a root.  It can also mean that something
 * went wrong in a tree traversal.
 *
 * @author Nicholas Killewald
 */
public class BrokenGalleryTreeException extends java.lang.Exception {

    /**
     * Creates a new instance of <code>BrokenGalleryTreeException</code> without detail message.
     */
    public BrokenGalleryTreeException() {
    }

    /**
     * Constructs an instance of <code>BrokenGalleryTreeException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public BrokenGalleryTreeException(String msg) {
        super(msg);
    }
}
