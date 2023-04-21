package org.eun.lre.toolkit.identifier.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * 
 * @author tien-dungle
 *
 */
public class Id2IloxConverter {

    public static String ID_XLS_FILENAME = "/cmrId2ilox.xsl";

    protected String xslFilename;

    protected Transformer transformer;

    protected String xslSystranStr;

    /**
	 * 
	 * @param xslFilename	E.g, "/cmrId2ilox.xsl" - 
	 * 						lom2ilox.xsl should be in the root of the jar file
	 * @throws Exception
	 */
    public Id2IloxConverter() throws Exception {
        xslFilename = ID_XLS_FILENAME;
        prepare(xslFilename);
    }

    /**
	 * 
	 * @param xslFilename	E.g, "/lom2ilox.xsl" - 
	 * 						lom2ilox.xsl should be in the root of the jar file
	 * @throws Exception
	 */
    protected void prepare(String xslFilename) throws Exception {
        this.xslFilename = xslFilename;
        xslSystranStr = getXslStr(xslFilename);
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Source xslSource = new StreamSource(new StringReader(xslSystranStr));
        transformer = tFactory.newTransformer(xslSource);
    }

    /**
	 * Should be Override if needed
	 * 
	 * @param src
	 * @param cmrId
	 * @return
	 * @throws Exception
	 */
    public String id2ilox(String src, String cmrId, String handle) throws Exception {
        try {
            Source xmlSource = new StreamSource(new StringReader(src));
            Writer writer = new StringWriter();
            transformer.clearParameters();
            transformer.setParameter("cmrId", cmrId);
            if (handle != null) {
                transformer.setParameter("handle", handle);
            }
            transformer.transform(xmlSource, new StreamResult(writer));
            return writer.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String getXslStr(String filename) throws Exception {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(filename)));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("CANNOT_LOAD_XSL: " + ex.getMessage());
        }
    }

    /**
	 * Get the id from filename
	 * @param filename		should be id.xml
	 * @return
	 * @throws Exception
	 */
    public String getId(File f) throws Exception {
        try {
            String shortFilename = f.getName();
            String[] tokens = shortFilename.split("[.]");
            return tokens[0];
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("CANNOT_GET_ID: " + ex.getMessage());
        }
    }

    public String getXslFilename() {
        return xslFilename;
    }

    public void setXslFilename(String xslFilename) {
        this.xslFilename = xslFilename;
    }

    public Transformer getTransformer() {
        return transformer;
    }

    public void setTransformer(Transformer transformer) {
        this.transformer = transformer;
    }

    public String getXslSystranStr() {
        return xslSystranStr;
    }

    public void setXslSystranStr(String xslSystranStr) {
        this.xslSystranStr = xslSystranStr;
    }
}
