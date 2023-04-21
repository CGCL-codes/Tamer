package net.sf.ahtutils.test.model.ejb.status.cli;

import java.util.Random;
import net.sf.ahtutils.model.ejb.status.AhtUtilsStatus;
import net.sf.ahtutils.test.model.ejb.status.TestStatus;
import net.sf.exlp.util.io.LoggerInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TstStatus {

    static final Logger logger = LoggerFactory.getLogger(TstStatus.class);

    private Random rnd;

    public TstStatus() {
        rnd = new Random();
    }

    public void create() {
        AhtUtilsStatus status = TestStatus.create(rnd, "code");
        logger.debug(status.toString());
    }

    public static void main(String[] args) {
        LoggerInit loggerInit = new LoggerInit("log4j.xml");
        loggerInit.addAltPath("config");
        loggerInit.init();
        TstStatus test = new TstStatus();
        test.create();
    }
}
