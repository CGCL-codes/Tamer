package org.speech.asr.recognition.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * //@todo class description
 * <p/>
 * Creation date: Aug 31, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class ContextMfccMapper extends BaseInputMapper {

    /**
   * slf4j Logger.
   */
    private static final Logger log = LoggerFactory.getLogger(ContextMfccMapper.class.getName());

    private static final int MFCC_SIZE = 39;

    public ContextMfccMapper(int n) {
        int t = 0;
        addMapping(0, n - 1, "lE");
        addMapping(n, 13 * n - 1, "lCEP");
        addMapping(13 * n, 14 * n - 1, "ldE");
        addMapping(14 * n, 26 * n - 1, "ldCEP");
        addMapping(26 * n, 27 * n - 1, "lddE");
        addMapping(27 * n, 39 * n - 1, "lddCEP");
        t = 39 * n;
        addMapping(t + 0, t + 0, "E");
        addMapping(t + 1, t + 12, "CEP");
        addMapping(t + 13, t + 13, "dE");
        addMapping(t + 14, t + 25, "dCEP");
        addMapping(t + 26, t + 26, "ddE");
        addMapping(t + 27, t + 38, "ddCEP");
        t = 39 * (n + 1);
        addMapping(t + 0, t + n - 1, "rE");
        addMapping(t + n, t + 13 * n - 1, "rCEP");
        addMapping(t + 13 * n, t + 14 * n - 1, "rdE");
        addMapping(t + 14 * n, t + 26 * n - 1, "rdCEP");
        addMapping(t + 26 * n, t + 27 * n - 1, "rddE");
        addMapping(t + 27 * n, t + 39 * n - 1, "rddCEP");
    }
}
