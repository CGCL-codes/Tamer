package org.mcisb.ontology.ws.client;

import java.io.*;
import java.net.*;

/**
 * Client for the OntologyUtils web service.
 * 
 * Note that this class will fail to compile until the ant build script is run.
 * 
 * Running the ant build script calls wsgen and wsimport, and creates the java classes
 * required for successful compilation of this class.
 * 
 * @author Neil Swainston
 */
public class OntologyUtilsClient {

    /**
	 *
	 * @param wsdlLocation
	 */
    public OntologyUtilsClient(final URL wsdlLocation) {
    }

    /**
	 * 
	 * @param uri
	 * @return String
	 * @throws java.lang.Exception
	 */
    public String getSynonyms(final String uri) throws java.lang.Exception {
        return null;
    }

    /**
	 *
	 * @param sbmlIn
	 * @param sbmlOut
	 * @throws java.lang.Exception
	 */
    public void getSynonyms(final File sbmlIn, final File sbmlOut) throws java.lang.Exception {
        InputStream is = null;
        try {
            is = new FileInputStream(sbmlIn);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
	 * 
	 * @param sbmlInFile
	 * @return String
	 * @throws java.lang.Exception
	 */
    public String annotateSbml(final File sbmlInFile) throws java.lang.Exception {
        return null;
    }

    /**
	 *
	 * @param args
	 * @throws java.lang.Exception
	 */
    public static void main(String[] args) throws java.lang.Exception {
        System.out.println(new OntologyUtilsClient(new URL(args[0])).annotateSbml(new File(args[1])));
    }
}
