package net.lunglet.hdf;

public final class H5FileException extends H5Exception {

    private static final long serialVersionUID = 1L;

    public H5FileException(final String message) {
        super(message);
    }

    public H5FileException(final String message, final boolean includeError) {
        super(message, includeError);
    }
}
