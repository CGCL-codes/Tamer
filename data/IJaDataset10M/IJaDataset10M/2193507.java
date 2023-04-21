package fitlibrary.exception.table;

import fitlibrary.exception.FitLibraryException;

public class MissingTableException extends FitLibraryException {

    private static final long serialVersionUID = 1L;

    public MissingTableException() {
        super("Missing table");
    }
}
