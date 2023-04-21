package sbmlreader2;

import cytoscape.data.ImportHandler;
import cytoscape.data.readers.GraphReader;
import cytoscape.util.CyFileFilter;
import java.io.File;
import java.io.IOException;

/**
 * SBMLReader extends CyFileFilter for integration into the Cytoscape ImportHandler
 * framework.
 *
 */
public class SBMLFilter extends CyFileFilter {

    /**
	 * XGMML Files are Graphs.
	 */
    private static String fileNature = ImportHandler.GRAPH_NATURE;

    /**
	 * File Extensions.
	 */
    private static String[] fileExtensions = { "xml", "sbml" };

    /**
	 * Filter Description.
	 */
    private static String description = "SBML files";

    /**
	 * Constructor.
	 */
    public SBMLFilter() {
        super(fileExtensions, description, fileNature);
    }

    /**
	 * Indicates which files the SBMLFilter accepts.
	 * <p/>
	 * This method will return true only if:
	 * <UL>
	 * <LI>File ends in .xml or .sbml;  and
	 * <LI>File headers includes the www.sbml.org namespace declaration.
	 * </UL>
	 *
	 * @param file File
	 * @return true or false.
	 */
    public boolean accept(File file) {
        String fileName = file.getName();
        boolean firstPass = false;
        for (int i = 0; i < fileExtensions.length; i++) {
            if (fileName.endsWith(fileExtensions[i])) {
                firstPass = true;
            }
        }
        if (firstPass) {
            try {
                String header = getHeader(file);
                if (header.indexOf("www.sbml.org") > 0) {
                    return true;
                }
            } catch (IOException e) {
            }
        }
        return false;
    }

    /**
	 * Gets the appropirate GraphReader object.
	 * If the libsbml is not available use the old sbml library.
	 *
	 * @author Matthias Koenig
	 * @param fileName File Name.
	 * @return GraphReader Object.
	 */
    public GraphReader getReader(String fileName) {
        System.out.println("Getting SBML reader ...");
        try {
            System.loadLibrary("sbmlj");
            Class.forName("org.sbml.libsbml.libsbml");
            System.out.println("... using 'libsbml parser'");
            return new SBMLGraphReader2(fileName);
        } catch (Exception e) {
            return new SBMLGraphReader(fileName);
        }
    }
}
