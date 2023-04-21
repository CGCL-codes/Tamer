package com.neo.flow.jmx.test;

import java.util.Arrays;
import org.apache.log4j.Logger;

/**
 * Writes information to a txnal persistent store/DB etc.
 *
 */
public class ResultWriterImpl implements ResultWriter {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(ResultWriterImpl.class);

    public ResultWriterImpl() {
    }

    public void writeResult(Integer[] result) {
        logger.info("*****Job complete, taskCount:" + result.length);
        logger.info(" results:" + Arrays.toString(result));
    }
}
