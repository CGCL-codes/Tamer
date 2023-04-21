package playground.tnicolai.matsim4opus.utils.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import playground.tnicolai.matsim4opus.constants.Constants;

/**
 * @author thomas
 *
 */
public class LoadFile {

    private static final Logger log = Logger.getLogger(LoadFile.class);

    private String source = null;

    private String destinationPath = null;

    private String fileName = null;

    @SuppressWarnings(value = "all")
    private LoadFile() {
    }

    /**
	 * constructor
	 * @param source
	 * @param destination
	 */
    public LoadFile(String source, String destination, String fileName) {
        this.source = source;
        this.destinationPath = Paths.checkPathEnding(destination);
        this.fileName = fileName;
    }

    /**
	 * linke loadMATSim4UrbanSimXSD() but returns the canonical path
	 * of the loaded xsd file
	 * @return path of loaded xsd file
	 */
    public String loadMATSim4UrbanSimXSDString() {
        try {
            File f = loadMATSim4UrbanSimXSD();
            return f.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * creates a local copy of the MATSim4UrbanSim xsd file from matsim.org
	 * and returns the reference to the created file
	 * @return reference to the created xsd file
	 */
    public File loadMATSim4UrbanSimXSD() {
        InputStream is = null;
        BufferedOutputStream bos = null;
        boolean isInternetStream = Boolean.TRUE;
        File dir = new File(this.destinationPath);
        if (!dir.exists()) dir.mkdirs();
        File output = new File(destinationPath + fileName);
        log.info("Trying to load " + this.source + ". In some cases (e.g. network interface up but no connection), this may take a bit.");
        log.info("The xsd file will be saved in " + this.destinationPath + ".");
        try {
            is = new URL(this.source).openStream();
            bos = new BufferedOutputStream(new FileOutputStream(output));
            for (int c; (c = is.read()) != -1; ) {
                bos.write(c);
            }
            log.info("Loading successfully.");
            if (bos != null) {
                bos.flush();
                bos.close();
            }
            if (is != null) is.close();
            return output;
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            log.error(e1.toString() + ". May not be fatal.");
            isInternetStream = Boolean.FALSE;
            output = null;
        }
        if (!isInternetStream) {
            log.info("Trying to access local dtd folder at standard location " + Constants.CURRENT_MATSIM_4_URBANSIM_XSD_LOCALJAR + " ...");
            File dtdFile = new File(Constants.CURRENT_MATSIM_4_URBANSIM_XSD_LOCALJAR);
            if (dtdFile.exists() && dtdFile.isFile() && dtdFile.canRead()) {
                log.info("Using the local DTD " + dtdFile.getAbsolutePath());
                output = new File(dtdFile.getAbsolutePath());
                return output;
            }
            String currentDir = System.getProperty("user.dir");
            int index = (currentDir.indexOf("playground") > 0) ? currentDir.indexOf("playground") : currentDir.indexOf("contrib");
            String root = currentDir.substring(0, index);
            dtdFile = new File(root + "/matsim" + Constants.CURRENT_MATSIM_4_URBANSIM_XSD_LOCALJAR);
            log.info("Trying to access local dtd folder at standard location " + dtdFile.getAbsolutePath() + " ...");
            if (dtdFile.exists() && dtdFile.isFile() && dtdFile.canRead()) {
                log.info("Using the local DTD " + dtdFile.getAbsolutePath());
                output = new File(dtdFile.getAbsolutePath());
                return output;
            }
        }
        log.warn("Could neither get the XSD from the web nor a local one.");
        return null;
    }
}
