package net.admin4j.dao.xml;

import java.beans.DefaultPersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Set;
import net.admin4j.config.Admin4JConfiguration;
import net.admin4j.dao.TaskTimerDAO;
import net.admin4j.timer.BasicTaskTimer;
import net.admin4j.timer.SummaryDataMeasure;
import net.admin4j.timer.TaskTimer;
import net.admin4j.util.Admin4jRuntimeException;

/**
 * DAO implementation for reading/writing performance measurement information.
 * @author D. Ashmore
 * @since 1.0
 */
public class TaskTimerDAOXml extends BaseDAOXml implements TaskTimerDAO {

    private static final Object SAVE_LOCK = new Object();

    @SuppressWarnings("unchecked")
    public Set<TaskTimer> findAll() {
        Set<TaskTimer> result = null;
        XMLDecoder decoder = null;
        String xmlFileName = Admin4JConfiguration.getPerformanceInformationXmlFileName();
        ClassLoader currentContextLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(TaskTimer.class.getClassLoader());
            decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(xmlFileName)));
            decoder.setExceptionListener(new DefaultExceptionListener(xmlFileName));
            result = (Set<TaskTimer>) decoder.readObject();
        } catch (Throwable t) {
            throw new Admin4jRuntimeException("Error reading XML Performance Information.", t).addContextValue("xmlFileName", xmlFileName);
        } finally {
            if (decoder != null) decoder.close();
            Thread.currentThread().setContextClassLoader(currentContextLoader);
        }
        return result;
    }

    public void saveAll(Set<TaskTimer> exceptionList) {
        XMLEncoder encoder = null;
        String xmlFileName = Admin4JConfiguration.getPerformanceInformationXmlFileName();
        String tempFileName = xmlFileName + ".temp";
        String previousFileName = derivePreviousFileName(".previous");
        boolean encoderClosed = false;
        synchronized (SAVE_LOCK) {
            ClassLoader currentContextLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(TaskTimer.class.getClassLoader());
                encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(tempFileName)));
                encoder.setExceptionListener(new DefaultExceptionListener(xmlFileName));
                encoder.setPersistenceDelegate(BasicTaskTimer.class, new DefaultPersistenceDelegate(new String[] { "label", "dataMeasures" }));
                encoder.setPersistenceDelegate(SummaryDataMeasure.class, new DefaultPersistenceDelegate(new String[] { "firstObservationTime" }));
                encoder.writeObject(exceptionList);
                encoder.close();
                encoderClosed = true;
                versionOutputFile(xmlFileName, tempFileName, previousFileName);
            } catch (Throwable t) {
                if (encoder != null && !encoderClosed) encoder.close();
            } finally {
                Thread.currentThread().setContextClassLoader(currentContextLoader);
            }
        }
    }
}
