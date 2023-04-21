package com.emental.mindraider.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;

/**
 * XSL helper.
 * 
 * @author Martin.Dvorak
 * @author Francesco Tinti
 * @version $Revision: 1.3 $ ($Author: mindraider $)
 */
public class Xsl {

    /**
	 * Logger for this class.
	 */
    private static final Logger cat = Logger.getLogger(Xsl.class);

    /**
	 * Apply stylesheet.
	 * 
	 * @param inputFile
	 * @param outputFile
	 * @param xslFile
	 */
    public static void xsl(String inputFile, String outputFile, String xslFile) {
        cat.debug("=-> stylesheeting: " + inputFile + " -> " + xslFile + " -> " + outputFile);
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Templates templates = factory.newTemplates(new StreamSource(new FileInputStream(xslFile)));
            Transformer transformer = templates.newTransformer();
            FileInputStream fileInputStream = new FileInputStream(inputFile);
            Source source = new StreamSource(fileInputStream);
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            Result result = new StreamResult(fileOutputStream);
            transformer.transform(source, result);
            cat.debug("Transformed!");
            fileOutputStream.flush();
            fileOutputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            cat.error("Unable to stylesheet!", e);
        } finally {
        }
    }
}
