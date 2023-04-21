package net.sf.otrcutmp4.test;

import java.io.FileNotFoundException;
import net.sf.exlp.util.xml.JaxbUtil;
import net.sf.otrcutmp4.AviToMp4.Audio;
import net.sf.otrcutmp4.AviToMp4.Profile;
import net.sf.otrcutmp4.AviToMp4.Quality;
import net.sf.otrcutmp4.controller.batch.CutGenerator;
import net.sf.otrcutmp4.controller.batch.RenameGenerator;
import net.sf.otrcutmp4.controller.exception.OtrInternalErrorException;
import net.sf.otrcutmp4.model.xml.cut.VideoFiles;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TstBatchProcessor {

    static final Logger logger = LoggerFactory.getLogger(TstBatchProcessor.class);

    private Configuration config;

    public TstBatchProcessor() {
    }

    public void cutGenerator() throws FileNotFoundException, OtrInternalErrorException {
        String xmlIn = config.getString("xml.test.cut.3");
        logger.debug("Loading from file: " + xmlIn);
        VideoFiles vFiles = (VideoFiles) JaxbUtil.loadJAXB(xmlIn, VideoFiles.class);
        CutGenerator test = new CutGenerator(null);
        test.create(vFiles, Quality.HQ, Audio.Mp3, Profile.P0);
    }

    public void renameGenerator() throws FileNotFoundException {
        String xmlIn = config.getString("xml.test.rename.3");
        logger.debug("Loading from file: " + xmlIn);
        VideoFiles vFiles = (VideoFiles) JaxbUtil.loadJAXB(xmlIn, VideoFiles.class);
        RenameGenerator test = new RenameGenerator(null);
        test.create(vFiles);
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public static void main(String args[]) throws Exception {
        Configuration config = OtrClientTstBootstrap.init();
        TstBatchProcessor test = new TstBatchProcessor();
        test.setConfig(config);
        test.renameGenerator();
    }
}
