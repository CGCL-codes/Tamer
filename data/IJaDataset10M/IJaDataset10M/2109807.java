package com.csol.chem.io;

import java.io.*;
import java.io.File;
import java.io.IOException;
import com.csol.chem.core.Molecule;

/**
 * Versioned Data Stream writer. 
 * @author jiro
 *
 */
public class VDSWriter {

    /**
     * Not supposed to be instantiated. Throws UnsupportedOperationException.
     */
    private VDSWriter() {
        throw new UnsupportedOperationException("" + "This class is not meant to be instantiated.");
    }

    public static void write(Molecule molecule, File file) throws IOException {
        DataOutputStream data = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        data.write(molecule.toBinary());
        data.close();
    }
}
