package educate.sis.library;

import java.io.*;

public class DualBook extends Dual {

    protected static synchronized String getId() throws IOException {
        return getId("book");
    }
}
