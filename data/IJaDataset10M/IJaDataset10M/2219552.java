package alice.tuprolog.event;

import alice.tuprolog.Prolog;

/**
 * 
 * This class represents events concerning library management.
 * 
 * @since 1.3
 * 
 */
public class LibraryEvent extends PrologEvent {

    private String libName;

    public LibraryEvent(Prolog source, String libName) {
        super(source);
        this.libName = libName;
    }

    /**
     * Gets the library name (loaded or unloaded).
     * 
     * @return library name
     */
    public String getLibraryName() {
        return libName;
    }
}
