package org.psepr.jClient;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.FileReader;

/**
 * @author radams1
 *
 */
public class JClientParamFile extends ParamFile {

    String baseDir = "psepr";

    String baseFilename = "jClient.xml";

    String configFiles[] = null;

    String defaultConfigFiles[] = { File.separator + "usr" + File.separator + "local" + File.separator + "psepr" + File.separator + "etc" + File.separator + baseDir + File.separator + baseFilename, System.getProperty("user.home") + File.separator + ".psepr" + File.separator + baseFilename, baseFilename };

    /**
	 * 
	 */
    public JClientParamFile() {
        super();
        this.init();
        readParams();
    }

    /**
	 * Specify an alternate configuration file.  This is used for reading
	 * service specific information so the service name is use for the
	 * subdir.  The file is looked for in:
	 * <ul>
	 * <li>/usr/local/psepr/etc/<i>Service</i>/<i>Filename</i></li>
	 * <li>$HOME/.psepr/<i>Filename</i></li>
	 * <li>./Filename</li>
	 * </ul>
	 * 
	 * @param service the name of the subdir for the service config
	 * @param filename the actual param filename
	 */
    public JClientParamFile(String userConfigFiles[]) {
        super();
        this.init();
        if (userConfigFiles != null) {
            configFiles = userConfigFiles;
        }
        readParams();
    }

    private void init() {
        baseDir = "psepr";
        baseFilename = "jClient.xml";
        configFiles = defaultConfigFiles;
        return;
    }

    /**
	 * Read the parameter file.  The filenamed 'baseParamFile' is looked for in
	 * the current directory, the user's home directory and then "/usr/local/psepr"
	 * in that order, the first one is read.
	 * TODO: consider reading all the files so local overrides system settings
	 */
    private void readParams() {
        String paramResource = "META-INF/" + baseFilename;
        try {
            InputStream in;
            super.myLog(super.log.PARAMDETAIL, "Trying this.getClass().getClassLoader().getResourceAsStream(" + paramResource + ")");
            in = this.getClass().getClassLoader().getResourceAsStream(paramResource);
            if (in == null) {
                super.myLog(super.log.PARAMDETAIL, "Trying getContextClassLoader().getResourceAsStream(" + paramResource + ")");
                in = Thread.currentThread().getContextClassLoader().getResourceAsStream(paramResource);
            }
            if (in == null) {
                super.myLog(super.log.PARAMDETAIL, "Trying getContextClassLoader().getResourceAsStream(" + paramResource + ")");
                in = ClassLoader.getSystemClassLoader().getResourceAsStream(paramResource);
            }
            if (in != null) {
                super.myLog(super.log.PARAMDETAIL, "parsing resource");
                super.parseFile(new InputStreamReader(in));
            }
        } catch (Exception e) {
            throw new PsEPRException("Failed parsing resource file " + paramResource);
        }
        for (int ii = 0; ii < configFiles.length; ii++) {
            File inFile = new File(configFiles[ii]);
            super.myLog(super.log.PARAMDETAIL, "params from file " + inFile.getAbsolutePath());
            if (inFile.canRead()) {
                try {
                    Reader is = new FileReader(inFile);
                    super.parseFile(is);
                } catch (Exception e) {
                    throw new PsEPRException("Failed parsing param file " + inFile.getAbsolutePath());
                }
            }
        }
        return;
    }
}
