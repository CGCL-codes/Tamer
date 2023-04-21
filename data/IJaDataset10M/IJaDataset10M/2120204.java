package it.univaq.di.chameleonserver.abstractanalyzer.utility;

import java.io.IOException;
import java.util.Enumeration;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.varia.NullAppender;

public class LogServices {

    public enum logType {

        TailorJ, ACA, ARU, ARC, Customizer
    }

    ;

    public static Logger getLogger(logType type, String file) throws IOException {
        Logger logger = null;
        if (type == logType.TailorJ) {
            logger = Logger.getLogger("TailorJ");
        } else {
            String nomeLog = type.toString();
            nomeLog = "TailorJ.Module." + nomeLog;
            logger = Logger.getLogger(nomeLog);
            Layout layout = Logger.getLogger("NULL").getAppender("moduleAppender").getLayout();
            FileAppender logFileAppender = new FileAppender(layout, file, false);
            logFileAppender.setName(nomeLog);
            logger.addAppender(logFileAppender);
            logFileAppender.activateOptions();
        }
        return logger;
    }

    public static Logger getLogger(logType type) {
        Logger logger = null;
        if (type == logType.TailorJ) {
            logger = Logger.getLogger("TailorJ");
        } else {
            String nomeLog = type.toString();
            nomeLog = "TailorJ.Module." + nomeLog;
            logger = Logger.getLogger(nomeLog);
            if (logger.getAppender(nomeLog) == null) {
                getLogger(logType.TailorJ).error("Logger " + nomeLog + " non inizializzato");
            }
        }
        return logger;
    }
}
