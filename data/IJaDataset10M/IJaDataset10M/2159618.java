package gr.konstant.transonto.kb.prolog;

import java.io.File;
import java.net.URI;
import gr.konstant.transonto.exception.*;

public class Prolog extends KBFilePrologImpl {

    public Prolog() {
    }

    public Prolog(URI base) {
        super(base);
    }

    public Prolog(File f) {
        super(f);
    }

    public Prolog(URI base, File f) {
        super(base, f);
    }

    public Prolog(File inFile, File outFile) {
        super(inFile, outFile);
    }

    public Prolog(URI base, File inFile, File outFile) {
        super(base, inFile, outFile);
    }
}
