package pcgen.util;

import org.apache.fop.apps.*;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.awt.AWTRenderer;
import org.apache.fop.viewer.SecureResourceBundle;
import org.xml.sax.XMLReader;
import pcgen.core.Constants;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Title:        FOPHandler.java
 * Description:  Interface to the Apache FOP API;
 *               this class handles all the interaction
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author Thomas Behr
 * @version $Revision: 1.40 $
 */
public final class FOPHandler implements Runnable {

    /** PDF_MODE = 0 */
    public static final int PDF_MODE = 0;

    /** AWT_MODE = 1 */
    public static final int AWT_MODE = 1;

    private Driver driver;

    private File outFile;

    private FileOutputStream fos;

    private InputHandler inputHandler;

    private Renderer renderer;

    private StringBuffer errBuffer;

    private int mode;

    /**
	 *
	 */
    public FOPHandler() {
        driver = new Driver();
        inputHandler = null;
        outFile = null;
        mode = PDF_MODE;
        errBuffer = new StringBuffer();
        renderer = null;
    }

    /**
	 * @return error message
	 *
	 */
    public String getErrorMessage() {
        return errBuffer.toString();
    }

    /**
	 * Dekker500
	 * Feb 1, 2003
	 * Immediately convert file into appropriatee InputHandler type
	 * @param in
	 */
    public void setInputFile(File in) {
        try {
            if (in == null) {
                throw new NullPointerException("XML file must be specified for the tranform mode");
            }
            if (!in.exists()) {
                throw new FileNotFoundException("xml file " + in.getAbsolutePath() + " not found ");
            }
            inputHandler = new FOInputHandler(in);
        } catch (FileNotFoundException e) {
            errBuffer.append(e.getMessage()).append(Constants.s_LINE_SEP);
            Logging.errorPrint("Exception in FOPHandler:setInputFile", e);
        }
    }

    /**
	 * Dekker500
	 * Feb 1, 2003
	 * Immediately convert file into appropriatee InputHandler type
	 * @param xmlFile
	 * @param xsltFile
	 */
    public void setInputFile(File xmlFile, File xsltFile) {
        try {
            if (xmlFile == null) {
                throw new NullPointerException("XML file must be specified for the tranform mode");
            }
            if (xsltFile == null) {
                throw new NullPointerException("XSLT file must be specified for the tranform mode");
            }
            if (!xmlFile.exists()) {
                throw new FileNotFoundException("xml file " + xmlFile.getAbsolutePath() + " not found ");
            }
            if (!xsltFile.exists()) {
                throw new FileNotFoundException("xsl file " + xsltFile.getAbsolutePath() + " not found ");
            }
            inputHandler = new XSLTInputHandler(xmlFile, xsltFile);
        } catch (FileNotFoundException e) {
            errBuffer.append(e.getMessage()).append(Constants.s_LINE_SEP);
            Logging.errorPrint("Exception in FOPHandler:setInputFile", e);
        } catch (NullPointerException e) {
            errBuffer.append(e.getMessage()).append(Constants.s_LINE_SEP);
            Logging.errorPrint("Exception in FOPHandler:setInputFile", e);
        } catch (FOPException e) {
            errBuffer.append(e.getMessage()).append(Constants.s_LINE_SEP);
            Logging.errorPrint("Exception in FOPHandler:setInputFile", e);
        }
    }

    /**
	 * @param m
	 *
	 */
    public void setMode(int m) {
        mode = m;
    }

    /**
	 * @param out
	 *
	 */
    public void setOutputFile(File out) {
        outFile = out;
    }

    /**
	 * @return Renderer
	 *
	 */
    public Renderer getRenderer() {
        return renderer;
    }

    /**
	 *
	 */
    public void run() {
        errBuffer.delete(0, errBuffer.length());
        driver.reset();
        if (mode == PDF_MODE) {
            fos = null;
            renderer = null;
            driver.setRenderer(Driver.RENDER_PDF);
            try {
                XMLReader parser = inputHandler.getParser();
                Map rendererOptions = new HashMap();
                rendererOptions.put("fineDetail", Boolean.valueOf(false));
                driver.getRenderer().setOptions(rendererOptions);
                driver.getRenderer().setProducer("PC Gen Character Generator");
                driver.setOutputStream(fos = new FileOutputStream(outFile));
                driver.render(parser, inputHandler.getInputSource());
            } catch (FOPException fopex) {
                errBuffer.append(fopex.getMessage()).append(Constants.s_LINE_SEP);
                Logging.errorPrint("Exception in FOPHandler:run", fopex);
            } catch (FileNotFoundException fnfex) {
                errBuffer.append(fnfex.getMessage()).append(Constants.s_LINE_SEP);
                Logging.errorPrint("Exception in FOPHandler:run, cannot find file: " + fnfex.getMessage());
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException ioex) {
                        errBuffer.append(ioex.getMessage()).append(Constants.s_LINE_SEP);
                        Logging.errorPrint("Exception in FOPHandler:run", ioex);
                    }
                }
            }
        } else if (mode == AWT_MODE) {
            renderer = createAWTRenderer();
            driver.setRenderer(renderer);
            try {
                XMLReader parser;
                parser = inputHandler.getParser();
                driver.render(parser, inputHandler.getInputSource());
            } catch (FOPException fopex) {
                errBuffer.append(fopex.getMessage()).append(Constants.s_LINE_SEP);
                Logging.errorPrint("Exception in FOPHandler:run", fopex);
            }
        } else {
            Logging.errorPrint("Unsupported mode for file export.");
        }
    }

    /**
	 * author: Thomas Behr 25-02-02
	 * @return AWTRenderer
	 */
    private static AWTRenderer createAWTRenderer() {
        final byte[] bytes = new byte[0];
        return new AWTRenderer(new SecureResourceBundle(new ByteArrayInputStream(bytes)));
    }
}
