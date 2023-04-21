package net.tourbook.device.tur;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import net.tourbook.chart.ChartLabel;
import net.tourbook.data.TimeData;
import net.tourbook.data.TourData;
import net.tourbook.data.TourMarker;
import net.tourbook.data.TourType;
import net.tourbook.importdata.DeviceData;
import net.tourbook.importdata.SerialParameters;
import net.tourbook.importdata.TourbookDevice;
import net.tourbook.ui.UI;

/**
 * @author stm
 */
public class TurDeviceReader extends TourbookDevice {

    private final int MAX_INT = 0x10000;

    /**
	 * Plugin constructor
	 */
    public TurDeviceReader() {
    }

    @Override
    public String buildFileNameFromRawData(final String rawDataFileName) {
        return null;
    }

    @Override
    public boolean checkStartSequence(final int byteIndex, final int newByte) {
        if (byteIndex == 0 & newByte == 'H') {
            return true;
        }
        if (byteIndex == 1 & newByte == 'A') {
            return true;
        }
        if (byteIndex == 2 & newByte == 'C') {
            return true;
        }
        if (byteIndex == 3 & newByte == 't') {
            return true;
        }
        if (byteIndex == 4 & newByte == 'r') {
            return true;
        }
        if (byteIndex == 5 & newByte == 'o') {
            return true;
        }
        if (byteIndex == 6 & newByte == 'n') {
            return true;
        }
        if (byteIndex == 7 & newByte == 'i') {
            return true;
        }
        if (byteIndex == 8 & newByte == 'c') {
            return true;
        }
        if (byteIndex == 9 & newByte == ' ') {
            return true;
        }
        if (byteIndex == 10 & newByte == '-') {
            return true;
        }
        if (byteIndex == 11 & newByte == ' ') {
            return true;
        }
        if (byteIndex == 12 & newByte == 'T') {
            return true;
        }
        if (byteIndex == 13 & newByte == 'o') {
            return true;
        }
        if (byteIndex == 14 & newByte == 'u') {
            return true;
        }
        if (byteIndex == 15 & newByte == 'r') {
            return true;
        }
        return false;
    }

    public String getDeviceModeName(final int modeId) {
        return UI.EMPTY_STRING;
    }

    @Override
    public SerialParameters getPortParameters(final String portName) {
        return null;
    }

    @Override
    public int getStartSequenceSize() {
        return 16;
    }

    private TourType getTourType() {
        return null;
    }

    public int getTransferDataSize() {
        return 0;
    }

    @Override
    public boolean processDeviceData(final String importFilePath, final DeviceData deviceData, final HashMap<Long, TourData> alreadyImportedTours, final HashMap<Long, TourData> newlyImportedTours) {
        FileInputStream fileTurData = null;
        final TurDeviceData turDeviceData = new TurDeviceData();
        final TourType defaultTourType = getTourType();
        try {
            fileTurData = new FileInputStream(importFilePath);
            turDeviceData.readFromFile(fileTurData);
            final TourData tourData = new TourData();
            tourData.importRawDataFile = importFilePath;
            tourData.setTourImportFilePath(importFilePath);
            tourData.setDeviceMode(Short.parseShort(turDeviceData.deviceMode));
            tourData.setStartHour(Short.parseShort(turDeviceData.tourStartTime.substring(0, 2)));
            tourData.setStartMinute(Short.parseShort(turDeviceData.tourStartTime.substring(3, 5)));
            tourData.setStartYear(Short.parseShort(turDeviceData.tourStartDate.substring(6)));
            tourData.setStartMonth(Short.parseShort(turDeviceData.tourStartDate.substring(3, 5)));
            tourData.setStartDay(Short.parseShort(turDeviceData.tourStartDate.substring(0, 2)));
            tourData.setStartDistance(Integer.parseInt(turDeviceData.deviceDistance));
            tourData.setTourDescription(turDeviceData.tourDescription);
            tourData.setTourTitle(turDeviceData.tourTitle);
            tourData.setTourStartPlace(turDeviceData.tourStartPlace);
            tourData.setTourEndPlace(turDeviceData.tourEndPlace);
            final int entryCount = Integer.parseInt(TurFileUtil.readText(fileTurData));
            int secStart1 = 0;
            int secStart2 = 0;
            int secStart3 = 0;
            int secStart4 = 0;
            int oldAltitude = 0;
            int oldDistance = 0;
            int sumDistance = 0;
            int sumAltitude = 0;
            int sumCadence = 0;
            int sumPulse = 0;
            int sumTemperature = 0;
            final ArrayList<TimeData> timeDataList = new ArrayList<TimeData>();
            for (int dataIndex = 0; dataIndex < entryCount; dataIndex++) {
                if (Integer.parseInt(turDeviceData.fileVersion.substring(0, 1)) >= 3) {
                    secStart1 = TurFileUtil.readByte(fileTurData);
                    secStart2 = TurFileUtil.readByte(fileTurData);
                    secStart3 = TurFileUtil.readByte(fileTurData);
                    secStart4 = TurFileUtil.readByte(fileTurData);
                }
                final int sec1 = TurFileUtil.readByte(fileTurData);
                final int sec2 = TurFileUtil.readByte(fileTurData);
                final int sec3 = TurFileUtil.readByte(fileTurData);
                final int sec4 = TurFileUtil.readByte(fileTurData);
                final int dst1 = TurFileUtil.readByte(fileTurData);
                final int dst2 = TurFileUtil.readByte(fileTurData);
                final int dst3 = TurFileUtil.readByte(fileTurData);
                final int dst4 = TurFileUtil.readByte(fileTurData);
                final int hm1 = TurFileUtil.readByte(fileTurData);
                final int hm2 = TurFileUtil.readByte(fileTurData);
                final int pulse = TurFileUtil.readByte(fileTurData);
                final int cadence = TurFileUtil.readByte(fileTurData);
                final int temperature = TurFileUtil.readByte(fileTurData);
                TurFileUtil.readByte(fileTurData);
                TurFileUtil.readByte(fileTurData);
                TurFileUtil.readByte(fileTurData);
                final int secStart = secStart1 + (256 * secStart2) + (256 * 256 * secStart3) + (256 * 256 * 256 * secStart4);
                final int seconds = sec1 + (256 * sec2) + (256 * 256 * sec3) + (256 * 256 * 256 * sec4);
                int distance = dst1 + (256 * dst2) + (256 * 256 * dst3) + (256 * 256 * 256 * dst4);
                distance *= 10;
                int altitude = hm1 + (256 * hm2);
                if (altitude > 6000) {
                    altitude = altitude - MAX_INT;
                }
                if (dataIndex == 0) {
                    tourData.setStartAltitude((short) altitude);
                    tourData.setStartPulse((short) pulse);
                }
                if (dataIndex == 1) {
                    tourData.setDeviceTimeInterval((short) seconds);
                }
                final TimeData timeData = new TimeData();
                timeData.altitude = altitude - oldAltitude;
                oldAltitude = altitude;
                timeData.cadence = cadence;
                timeData.pulse = pulse;
                timeData.temperature = temperature;
                if (dataIndex == 0) {
                    timeData.distance = 0;
                } else {
                    timeData.time = tourData.getDeviceTimeInterval();
                    timeData.distance = distance - oldDistance;
                    oldDistance = distance;
                    tourData.setTourAltUp(tourData.getTourAltUp() + ((timeData.altitude > 0) ? timeData.altitude : 0));
                    tourData.setTourAltDown(tourData.getTourAltDown() + ((timeData.altitude < 0) ? -timeData.altitude : 0));
                }
                timeDataList.add(timeData);
                sumDistance += timeData.distance;
                sumAltitude += Math.abs(altitude);
                sumCadence += cadence;
                sumPulse += pulse;
                sumTemperature += Math.abs(temperature);
            }
            final Long tourId = tourData.createTourId(Integer.toString((int) Math.abs(tourData.getStartDistance())));
            if (alreadyImportedTours.containsKey(tourId) == false) {
                newlyImportedTours.put(tourId, tourData);
                final TimeData firstTimeData = timeDataList.get(0);
                if (sumDistance == 0) {
                    firstTimeData.distance = Float.MIN_VALUE;
                }
                if (sumAltitude == 0) {
                    firstTimeData.altitude = Float.MIN_VALUE;
                }
                if (sumCadence == 0) {
                    firstTimeData.cadence = Float.MIN_VALUE;
                }
                if (sumPulse == 0) {
                    firstTimeData.pulse = Float.MIN_VALUE;
                }
                if (sumTemperature == 0) {
                    firstTimeData.temperature = Float.MIN_VALUE;
                }
                tourData.createTimeSeries(timeDataList, true);
                tourData.computeComputedValues();
                TurFileUtil.readByte(fileTurData);
                final int markerCount = Integer.parseInt(TurFileUtil.readText(fileTurData));
                final float[] distanceSerie = tourData.getMetricDistanceSerie();
                for (int i = 0; i < markerCount; i++) {
                    final TourMarker tourMarker = new TourMarker(tourData, ChartLabel.MARKER_TYPE_DEVICE);
                    tourMarker.setTime(Integer.parseInt(TurFileUtil.readText(fileTurData)));
                    String label = TurFileUtil.readText(fileTurData);
                    label = label.substring(0, label.indexOf(';'));
                    final int index = label.indexOf(", Type:");
                    if (index > 0) {
                        label = label.substring(0, index);
                    } else if (index == 0) {
                        label = Messages.TourData_Tour_Marker_unnamed;
                    }
                    tourMarker.setLabel(label);
                    tourMarker.setVisualPosition(ChartLabel.VISUAL_HORIZONTAL_ABOVE_GRAPH_CENTERED);
                    final int[] timeSerie = tourData.timeSerie;
                    if (timeSerie != null && timeSerie.length > 0) {
                        for (int j = 0; j < timeSerie.length; j++) {
                            if (timeSerie[j] > tourMarker.getTime()) {
                                if (distanceSerie != null) {
                                    tourMarker.setDistance(distanceSerie[j - 1]);
                                }
                                tourMarker.setSerieIndex(j - 1);
                                break;
                            }
                        }
                    }
                    tourData.getTourMarkers().add(tourMarker);
                }
                tourData.setTourType(defaultTourType);
                tourData.computeTourDrivingTime();
                tourData.setDeviceId(deviceId);
                tourData.setDeviceName(visibleName);
                tourData.setWeek(tourData.getStartYear(), tourData.getStartMonth(), tourData.getStartDay());
            }
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (final IOException e) {
            e.printStackTrace();
            return false;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fileTurData != null) {
                try {
                    fileTurData.close();
                } catch (final IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return true;
    }

    public boolean validateRawData(final String fileName) {
        boolean isValid = false;
        BufferedInputStream inStream = null;
        try {
            final byte[] buffer = new byte[17];
            final File dataFile = new File(fileName);
            inStream = new BufferedInputStream(new FileInputStream(dataFile));
            inStream.read(buffer);
            if (!"HACtronic - Tour".equalsIgnoreCase(new String(buffer, 0, 16))) {
                return false;
            }
            isValid = true;
        } catch (final NumberFormatException nfe) {
            return false;
        } catch (final FileNotFoundException e) {
            return false;
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (final IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return isValid;
    }
}
