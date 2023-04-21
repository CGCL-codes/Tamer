package jena.tests;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.FileManager;
import java.io.*;

/** Tutorial 5 - read RDF XML from a file and write it to standard out
 *
 * @author  bwm - updated by kers/Daniel
 * @version Release='$Name:  $' Revision='$Revision: 1.4 $' Date='$Date: 2006/04/27 09:30:07 $'
 */
public class Tutorial05 extends Object {

    /**
        NOTE that the file is loaded from the class-path and so requires that
        the data-directory, as well as the directory containing the compiled
        class, must be added to the class-path when running this and
        subsequent examples.
    */
    static final String inputFileName = "file:/paco/cardea/jena-2.5.6/Jena-2.5.6/doc/tutorial/RDF_API/data/vc-db-1.rdf";

    public void test() {
        Model model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(inputFileName);
        if (in == null) {
            throw new IllegalArgumentException("File: " + inputFileName + " not found");
        }
        model.read(in, "");
        model.write(System.out);
    }
}
