package org.openscience.cdk.applications.taverna.io;

import java.io.StringWriter;
import org.openscience.cdk.applications.taverna.CMLChemFile;
import org.openscience.cdk.io.MDLWriter;

public class CDKIOWriter {

    /**
	 * Converts the CML string into a MDL mol file.
	 * 
	 * @taverna.consume
	 */
    public static String convertToMDLMolfile(CMLChemFile file) throws Exception {
        StringWriter writer = new StringWriter();
        MDLWriter molWriter = new MDLWriter(writer);
        molWriter.write(file);
        return writer.toString();
    }

    /**
	 * Converts the ChemFile into a CML string.
	 * 
	 * @taverna.consume
	 */
    public static String convertChemFileToCMLString(CMLChemFile chemFile) throws Exception {
        return chemFile.toCML();
    }

    /**
	 * Converts the CML string into a ChemFile.
	 * 
	 * @taverna.consume
	 */
    public static CMLChemFile convertCMLStringToChemFile(String cmlString) throws Exception {
        return new CMLChemFile(cmlString);
    }
}
