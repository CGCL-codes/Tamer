package trial;

import org.nkumar.logging.EasyLoggerFactory;
import org.nkumar.logging.EasyLogger;

public final class JDKEasyLoggerAdapterTest {

    private JDKEasyLoggerAdapterTest() {
    }

    public static void main(String[] args) {
        final EasyLogger logger = EasyLoggerFactory.getLogger(JDKEasyLoggerAdapterTest.class.getName(), "trial.sample1");
        logger.error("error1");
        logger.error("error2", 1);
        logger.error("error3", new Exception("error3 exp"));
        logger.warn("warn1");
        logger.warn("warn2", 1, "abc");
        logger.warn("warn3", new Exception("warn3 exp"));
    }
}
