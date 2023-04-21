package org.matsim.core.config;

import java.io.File;
import java.io.InputStream;
import java.util.Stack;
import org.apache.log4j.Logger;
import org.matsim.core.api.internal.MatsimSomeReader;
import org.matsim.core.utils.io.MatsimXmlParser;
import org.matsim.core.utils.io.UncheckedIOException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;

/**
 * A reader for config-files of MATSim. This reader recognizes the format of the config-file and uses
 * the correct reader for the specific config-version, without manual setting.
 *
 * @author mrieser
 */
public class MatsimConfigReader extends MatsimXmlParser implements MatsimSomeReader {

    private static final Logger log = Logger.getLogger(MatsimConfigReader.class);

    private static final String CONFIG_V1 = "config_v1.dtd";

    private final Config config;

    private MatsimXmlParser delegate = null;

    private String localDtd;

    /**
	 * Creates a new reader for MATSim configuration files.
	 *
	 * @param config The Config-object to store the configuration settings in.
	 */
    public MatsimConfigReader(final Config config) {
        this.config = config;
    }

    @Override
    public void startTag(final String name, final Attributes atts, final Stack<String> context) {
        this.delegate.startTag(name, atts, context);
    }

    @Override
    public void endTag(final String name, final String content, final Stack<String> context) {
        this.delegate.endTag(name, content, context);
    }

    /**
	 * Parses the specified config file. This method calls {@link #parse(String)}.
	 *
	 * @param filename The name of the file to parse.
	 * @throws UncheckedIOException e.g. if the file cannot be found
	 */
    public void readFile(final String filename) throws UncheckedIOException {
        parse(filename);
    }

    /**
	 * Parses the specified config file, and uses the given dtd file as a local copy to use as dtd
	 * if the one specified in the config file cannot be found.
	 *
	 * @param filename The name of the file to parse.
	 * @param dtdFilename The name of a (local) dtd-file to be used for validating the config file.
	 * @throws UncheckedIOException e.g. if the file cannot be found
	 */
    public void readFile(final String filename, final String dtdFilename) throws UncheckedIOException {
        log.info("trying to read config from " + filename);
        this.localDtd = dtdFilename;
        parse(filename);
        this.localDtd = null;
    }

    @Override
    protected void setDoctype(final String doctype) {
        super.setDoctype(doctype);
        if (CONFIG_V1.equals(doctype)) {
            this.delegate = new ConfigReaderMatsimV1(this.config);
            log.info("using config_v1-reader.");
        } else {
            throw new IllegalArgumentException("Doctype \"" + doctype + "\" not known.");
        }
    }

    @Override
    public InputSource resolveEntity(final String publicId, final String systemId) {
        InputSource is = super.resolveEntity(publicId, systemId);
        if (is != null) {
            return is;
        }
        if (this.localDtd != null) {
            File dtdFile = new File(this.localDtd);
            if (dtdFile.exists() && dtdFile.isFile() && dtdFile.canRead()) {
                log.info("Using the local DTD " + this.localDtd);
                return new InputSource(this.localDtd);
            }
        }
        int index = systemId.replace('\\', '/').lastIndexOf("/");
        String shortSystemId = systemId.substring(index + 1);
        InputStream stream = this.getClass().getResourceAsStream("/dtd/" + shortSystemId);
        if (stream != null) {
            log.info("Using local DTD from jar-file " + shortSystemId);
            return new InputSource(stream);
        }
        return null;
    }
}
