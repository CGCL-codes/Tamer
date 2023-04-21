package playground.scnadine.gpsPreprocess;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeSet;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.scenario.ScenarioLoaderImpl;
import playground.scnadine.gpsProcessing.GPSCoordFactoryMGEFromFile;
import playground.scnadine.gpsProcessing.GPSFileFilterBin;
import playground.scnadine.gpsProcessing.coordAlgorithms.GPSCoordWriteAllCoordsForGIS;
import playground.scnadine.gpsProcessingV2.coordAlgorithms.GPSCoordFilteringAndSmoothing;
import playground.scnadine.gpsProcessingV2.coordAlgorithms.GPSCoordWriteCoordsForAnalysis;

public class ParseMGEData {

    static File[] allFiles;

    static GPSMGEFileComparator compFiles = new GPSMGEFileComparator();

    static TreeSet<GPSMGEDataset> sortedFiles = new TreeSet<GPSMGEDataset>(compFiles);

    static ArrayList<GPSParsingPerson> allPersons = new ArrayList<GPSParsingPerson>();

    public static void main(String[] args) throws ParseException {
        Gbl.startMeasurement();
        System.out.println("Start parsing MGE binaries...");
        System.out.println();
        final Scenario scenario = ScenarioLoaderImpl.createScenarioLoaderImplAndResetRandomSeed(args[0]).getScenario();
        final Config config = scenario.getConfig();
        final String CONFIG_MODULE = "GPS";
        System.out.println(config.getModule(CONFIG_MODULE));
        System.out.println();
        File sourcedir = new File(config.findParam(CONFIG_MODULE, "sourcedir"));
        File processedDir = new File(config.findParam(CONFIG_MODULE, "processedDir"));
        System.out.println();
        GPSFileFilterBin filter = new GPSFileFilterBin();
        allFiles = sourcedir.listFiles(filter);
        System.out.println("Number of files: " + allFiles.length);
        for (int i = 0; i < allFiles.length; i++) {
            try {
                int deviceId = Integer.parseInt(allFiles[i].getName().substring(0, 4));
                DataInputStream in = new DataInputStream(new FileInputStream(allFiles[i]));
                byte[] bytes = new byte[48];
                in.read(bytes, 0, 48);
                while (bytes[0] != 0x00) {
                    in.read(bytes, 0, 48);
                }
                in.close();
                int millis = (unsig(bytes[4]) * 0x1000000) + (unsig(bytes[3]) * 0x10000) + (unsig(bytes[2]) * 0x100) + unsig(bytes[1]);
                int tenths = millis % 1000;
                millis = millis - tenths;
                int dateoffset = (unsig(bytes[31]) << 8) + unsig(bytes[30]);
                double gpsTimeInDays = 3657 + dateoffset * 7 + millis / (double) 86400000;
                GregorianCalendar startDate = new GregorianCalendar();
                startDate.setTimeInMillis((long) (gpsTimeInDays * 86400000));
                System.out.println("file = " + allFiles[i].getName() + ", deviceId = " + deviceId + ", personId = unknown, startDate = " + startDate.get(Calendar.DAY_OF_MONTH) + "." + (startDate.get(Calendar.MONTH) + 1) + "." + startDate.get(Calendar.YEAR));
                GPSMGEDataset dataset = new GPSMGEDataset(deviceId, startDate);
                sortedFiles.add(dataset);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Number of sorted files: " + sortedFiles.size());
        for (GPSMGEDataset dataset : sortedFiles) {
            System.out.println("deviceId = " + dataset.getDeviceId() + ", startDate = " + dataset.getStartDate().get(Calendar.DAY_OF_MONTH) + "." + (dataset.getStartDate().get(Calendar.MONTH) + 1) + "." + dataset.getStartDate().get(Calendar.YEAR) + ", " + dataset.getStartDate().get(Calendar.HOUR) + ":" + dataset.getStartDate().get(Calendar.MINUTE) + ":" + dataset.getStartDate().get(Calendar.SECOND));
        }
        int lastDeviceId = 0;
        int lastGPSFactorySize = 0;
        GPSCoordFactoryMGEFromFile gpsFactory = new GPSCoordFactoryMGEFromFile();
        ArrayList<File> successfullyProcessedFiles = new ArrayList<File>();
        boolean append = false;
        for (GPSMGEDataset dataset : sortedFiles) {
            try {
                for (int i = 0; i < allFiles.length; i++) {
                    int deviceId = Integer.parseInt(allFiles[i].getName().substring(0, 4));
                    DataInputStream in = new DataInputStream(new FileInputStream(allFiles[i]));
                    byte[] bytes = new byte[48];
                    in.read(bytes, 0, 48);
                    while (bytes[0] != 0x00) {
                        in.read(bytes, 0, 48);
                    }
                    int millis = (unsig(bytes[4]) * 0x1000000) + (unsig(bytes[3]) * 0x10000) + (unsig(bytes[2]) * 0x100) + unsig(bytes[1]);
                    int tenths = millis % 1000;
                    millis = millis - tenths;
                    int dateoffset = (unsig(bytes[31]) << 8) + unsig(bytes[30]);
                    double gpsTimeInDays = 3657 + dateoffset * 7 + millis / (double) 86400000;
                    GregorianCalendar fileStartDate = new GregorianCalendar();
                    fileStartDate.setTimeInMillis((long) (gpsTimeInDays * 86400000));
                    in.close();
                    if (deviceId == dataset.getDeviceId() && fileStartDate.getTimeInMillis() == dataset.getStartDate().getTimeInMillis()) {
                        System.out.println("Process: file = " + allFiles[i].getName() + ", deviceId = " + deviceId + ", personId = unknown, +, startDate = " + dataset.getStartDate().get(Calendar.DAY_OF_MONTH) + "." + (dataset.getStartDate().get(Calendar.MONTH) + 1) + "." + dataset.getStartDate().get(Calendar.YEAR) + ", " + dataset.getStartDate().get(Calendar.HOUR) + ":" + dataset.getStartDate().get(Calendar.MINUTE) + ":" + dataset.getStartDate().get(Calendar.SECOND));
                        if (deviceId != lastDeviceId && gpsFactory.getCoords().size() > 0) {
                            System.out.println("New device, write coords and initialise factory....");
                            GPSCoordFilteringAndSmoothing filteringAndSmoothing = new GPSCoordFilteringAndSmoothing(config, CONFIG_MODULE);
                            filteringAndSmoothing.run(gpsFactory);
                            GPSCoordWriteAllCoordsForGIS writeCoordsForGIS = new GPSCoordWriteAllCoordsForGIS(config, CONFIG_MODULE);
                            writeCoordsForGIS.run(gpsFactory);
                            GPSCoordWriteCoordsForAnalysis writeCoordsForAnalysis = new GPSCoordWriteCoordsForAnalysis(config, CONFIG_MODULE);
                            writeCoordsForAnalysis.run(gpsFactory);
                            append = true;
                            gpsFactory.initialiseFactory();
                        }
                        System.out.println("coordIdCounter = " + gpsFactory.getCoordIdCounter() + ", nofCoords = " + gpsFactory.getCoords().size());
                        gpsFactory.createGPSCoords(allFiles[i]);
                        if ((gpsFactory.getCoords().size() - lastGPSFactorySize) == 0) {
                            throw new DataException("There was something going wrong with " + ", deviceId = " + deviceId + ", personId = unknown, +, startDate = " + dataset.getStartDate().get(Calendar.DAY_OF_MONTH) + "." + (dataset.getStartDate().get(Calendar.MONTH) + 1) + "." + dataset.getStartDate().get(Calendar.YEAR) + ", " + dataset.getStartDate().get(Calendar.HOUR) + ":" + dataset.getStartDate().get(Calendar.MINUTE) + ":" + dataset.getStartDate().get(Calendar.SECOND));
                        } else {
                            lastDeviceId = deviceId;
                            lastGPSFactorySize = gpsFactory.getCoords().size();
                            System.out.println("Successfully processed: File = " + allFiles[i].getName());
                            successfullyProcessedFiles.add(allFiles[i]);
                        }
                        break;
                    }
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            } catch (DataException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Write final set of coords....");
        GPSCoordFilteringAndSmoothing filteringAndSmoothing = new GPSCoordFilteringAndSmoothing(config, CONFIG_MODULE);
        filteringAndSmoothing.run(gpsFactory);
        GPSCoordWriteAllCoordsForGIS writeCoordsForGIS = new GPSCoordWriteAllCoordsForGIS(config, CONFIG_MODULE);
        writeCoordsForGIS.run(gpsFactory);
        GPSCoordWriteCoordsForAnalysis writeCoordsForAnalysis = new GPSCoordWriteCoordsForAnalysis(config, CONFIG_MODULE);
        writeCoordsForAnalysis.run(gpsFactory);
        System.out.println("Move successfully processed files... Number of files: " + successfullyProcessedFiles.size());
        for (File file : successfullyProcessedFiles) {
            file.renameTo(new File(processedDir.getPath(), file.getName()));
        }
        System.out.println("Done");
        Gbl.printElapsedTime();
        System.out.println("==================================================================");
    }

    private static int unsig(byte b) {
        if (b < 0) {
            return b + 256;
        }
        return b;
    }

    private static class DataException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public DataException(final String message) {
            super(message);
        }
    }
}
