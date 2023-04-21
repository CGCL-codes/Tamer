package com.hba.web.logger.server.serverimpl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;
import com.hba.web.lib.server.utils.AbstractServiceImpl;
import com.hba.web.logger.server.business.Appender;
import com.hba.web.logger.server.business.ApplicationLog;
import com.hba.web.logger.server.business.Layout;
import com.hba.web.logger.server.business.Level;
import com.hba.web.logger.server.business.Param;
import com.hba.web.logger.server.business.Priority;
import com.hba.web.logger.server.jaxb.AppenderRef;
import com.hba.web.logger.server.manager.LoggerService;

public class LoggerServiceImpl extends AbstractServiceImpl<ApplicationLog> implements LoggerService {

    public static final String CONSOLE_APPENDER = "ConsoleAppender";

    public static final String DAILY_ROLLING_FILE_APPENDER = "DailyRollingFileAppender";

    public static final String ROLLING_FILE_APPENDER = "RollingFileAppender";

    public static final String SMTP_APPENDER = "SMTPAppender";

    public static final String JDBC_APPENDER = "JDBCAppender";

    public static final String LOJ4J_PATH_NAME = "log4j.xml";

    public static final String LOGGER_DEFAULT = "org.developers";

    public static Logger getApplicationLogger() {
        return Logger.getLogger(LOGGER_DEFAULT);
    }

    /**
	 * Parse the XML and return an instance of the Log4j class
	 * 
	 * @param xmlFile
	 *            - Example: /WEB-INF/yourlog4j.xml
	 * @return Log4j object
	 */
    public com.hba.web.logger.server.jaxb.Log4j parsingLog4jXML(String xmlFile) {
        com.hba.web.logger.server.jaxb.Log4j log4j;
        try {
            JAXBContext js = JAXBContext.newInstance(com.hba.web.logger.server.jaxb.Log4j.class);
            Unmarshaller u = js.createUnmarshaller();
            JAXBElement<com.hba.web.logger.server.jaxb.Log4j> xmlConf = u.unmarshal(new StreamSource(new File(LOJ4J_PATH_NAME)), com.hba.web.logger.server.jaxb.Log4j.class);
            log4j = xmlConf.getValue();
            return log4j;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HashMap<String, Appender> parsingAppender(com.hba.web.logger.server.jaxb.Log4j log4j) {
        List<com.hba.web.logger.server.jaxb.Appender> appenders = log4j.getAppenders();
        HashMap<String, Appender> appensB = new HashMap<String, Appender>();
        for (com.hba.web.logger.server.jaxb.Appender appender : appenders) {
            Appender appenderB = new Appender();
            appenderB.setName(appender.name);
            appenderB.setClazz(appender.clazz);
            List<com.hba.web.logger.server.jaxb.Param> params = appender.getParams();
            HashMap<String, Param> paramsB = new HashMap<String, Param>();
            if (params != null) {
                for (com.hba.web.logger.server.jaxb.Param param : params) {
                    Param paramB = new Param();
                    paramB.setName(param.name);
                    paramB.setValue(param.value);
                    paramsB.put(paramB.getName(), paramB);
                }
            }
            appenderB.setParams(paramsB);
            com.hba.web.logger.server.jaxb.Layout layout = appender.getLayout();
            Layout layoutB = new Layout();
            if (layout != null) {
                layoutB.setClazz(layout.clazz);
            }
            appenderB.setLayout(layoutB);
            com.hba.web.logger.server.jaxb.Param layoutParam = layout.getParam();
            Param layoutParamB = new Param();
            if (layoutParam != null) {
                layoutParamB.setName(layoutParam.name);
                layoutParamB.setValue(layoutParam.value);
            }
            layoutB.setParam(layoutParamB);
            appensB.put(appenderB.getName(), appenderB);
        }
        return appensB;
    }

    @Override
    public ApplicationLog[] load() throws IllegalArgumentException {
        com.hba.web.logger.server.jaxb.Log4j log4j = parsingLog4jXML(LOJ4J_PATH_NAME);
        Vector<ApplicationLog> appLogVec = new Vector<ApplicationLog>();
        HashMap<String, Appender> appendersB = parsingAppender(log4j);
        for (com.hba.web.logger.server.jaxb.Logger logger : log4j.getLoggers()) {
            ApplicationLog applicationLog = new ApplicationLog();
            applicationLog.setName(logger.name);
            Level level = new Level();
            level.setValue(logger.level.value);
            applicationLog.setLevel(level);
            HashMap<String, Appender> aB = new HashMap<String, Appender>();
            if (logger.appenderRef != null) {
                if (appendersB.containsKey(logger.appenderRef.ref)) {
                    aB.put(logger.appenderRef.ref, appendersB.get(logger.appenderRef.ref));
                }
            }
            applicationLog.setAppenders(aB);
            appLogVec.add(applicationLog);
        }
        ApplicationLog rootLog = new ApplicationLog();
        Priority priority = new Priority();
        priority.setValue(log4j.root.priority.value);
        rootLog.setPriority(priority);
        rootLog.setIsRoot(true);
        List<AppenderRef> appenderRefs = log4j.root.getAppenderRefs();
        HashMap<String, Appender> aB = new HashMap<String, Appender>();
        for (AppenderRef appenderRef : appenderRefs) {
            if (appendersB.containsKey(appenderRef.ref)) {
                aB.put(appenderRef.ref, appendersB.get(appenderRef.ref));
            }
        }
        rootLog.setAppenders(aB);
        appLogVec.add(rootLog);
        ApplicationLog[] applicationLogs = new ApplicationLog[appLogVec.size()];
        for (int i = 0; i < appLogVec.size(); i++) {
            applicationLogs[i] = appLogVec.get(i);
        }
        return applicationLogs;
    }
}
