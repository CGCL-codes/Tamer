package co.edu.unal.ungrid.image.dicom.validate;

import javax.xml.transform.TransformerFactory;

/**
 * <p>
 * make a translet from any XSL-T source file.
 * </p>
 * 
 * 
 */
public class CompileXSLTIntoTranslet {

    /**
	 * <p>
	 * Read the XSLT-C file specified on the command line and make a translate
	 * with the same name but a .class extension.
	 * </p>
	 * 
	 * @param arg
	 *            the name of the file containing the XSLT-C source
	 */
    public static void main(String arg[]) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setAttribute("generate-translet", Boolean.TRUE);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
