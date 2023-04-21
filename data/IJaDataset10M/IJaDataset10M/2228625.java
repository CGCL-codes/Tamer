package eu.more.measurementservicegui.logic;

import java.util.GregorianCalendar;
import eu.more.measurementservice.generated.SensorInvoker;

/**
 * @author Peter Daum
 *
 */
public interface MeasurementController {

    /**
   * @param from
   * @param till
   * @param directoryName
   * @param fileName
   * @param appendToFile
   * @param invokerID
   */
    public void invokeDataRequest(GregorianCalendar from, GregorianCalendar till, String directoryName, String fileName, boolean appendToFile, String invokerID);

    /**
   * @param directoryName
   * @param fileName
   * @param appendToFile
   * @param invokerID
   */
    public void invokeLatestDataRequest(String directoryName, String fileName, boolean appendToFile, String invokerID);

    /**
   *
   */
    public void invokeServiceDiscovery();

    /**
   * @param currentDirectory
   * @param currentFileName
   * @param appendToFile
   */
    public void saveCurrentState(String currentDirectory, String currentFileName, boolean appendToFile);

    /**
   * @param sensorInvoker
   */
    public void setCurrentMeasurementService(SensorInvoker sensorInvoker);
}
