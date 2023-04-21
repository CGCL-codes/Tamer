package net.tourbook.device.csv.tours;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import net.tourbook.application.TourbookPlugin;
import net.tourbook.data.TourData;
import net.tourbook.data.TourTag;
import net.tourbook.data.TourType;
import net.tourbook.database.TourDatabase;
import net.tourbook.importdata.DeviceData;
import net.tourbook.importdata.SerialParameters;
import net.tourbook.importdata.TourbookDevice;
import net.tourbook.preferences.ITourbookPreferences;
import net.tourbook.preferences.TourTypeColorDefinition;
import net.tourbook.tour.TourEventId;
import net.tourbook.tour.TourManager;
import org.eclipse.swt.widgets.Display;

public class CSVTourDataReader extends TourbookDevice {

    private static final String TOUR_CSV_ID = "Date (yyyy-mm-dd); Time (hh-mm); Duration (sec); Paused Time (sec), Distance (m); Title; Comment; Tour Type; Tags;";

    /**
	 * This header is a modified header for {@link #TOUR_CSV_ID} with a semikolon instead of a komma
	 * after
	 * <p>
	 * <i>Paused Time (sec);</i>
	 */
    private static final String TOUR_CSV_ID_2 = "Date (yyyy-mm-dd); Time (hh-mm); Duration (sec); Paused Time (sec); Distance (m); Title; Comment; Tour Type; Tags;";

    private static final String CSV_TOKEN_SEPARATOR = ";";

    private static final String CSV_TAG_SEPARATOR = ",";

    public CSVTourDataReader() {
    }

    @Override
    public String buildFileNameFromRawData(final String rawDataFileName) {
        return null;
    }

    @Override
    public boolean checkStartSequence(final int byteIndex, final int newByte) {
        return false;
    }

    public String getDeviceModeName(final int profileId) {
        return null;
    }

    @Override
    public SerialParameters getPortParameters(final String portName) {
        return null;
    }

    @Override
    public int getStartSequenceSize() {
        return -1;
    }

    public int getTransferDataSize() {
        return -1;
    }

    private boolean isFileValid(final String fileHeader) {
        if (fileHeader.startsWith(TOUR_CSV_ID) || fileHeader.startsWith(TOUR_CSV_ID_2)) {
            return true;
        }
        return false;
    }

    private void parseDate(final TourData tourData, final String nextToken) {
        final int tourYear = Integer.parseInt(nextToken.substring(0, 4));
        final int tourMonth = Integer.parseInt(nextToken.substring(5, 7));
        final int tourDay = Integer.parseInt(nextToken.substring(8, 10));
        tourData.setStartDay((short) tourDay);
        tourData.setStartMonth((short) tourMonth);
        tourData.setStartYear((short) tourYear);
    }

    /**
	 * @param tourData
	 * @param tagToken
	 * @return <code>true</code> when a new tag is created
	 */
    private boolean parseTags(final TourData tourData, final String tagToken) {
        boolean isNewTag = false;
        final StringTokenizer tokenizer = new StringTokenizer(tagToken, CSV_TAG_SEPARATOR);
        final Set<TourTag> tourTags = new HashSet<TourTag>();
        HashMap<Long, TourTag> tourTagMap = TourDatabase.getAllTourTags();
        TourTag[] allTourTags = tourTagMap.values().toArray(new TourTag[tourTagMap.size()]);
        try {
            String tagLabel;
            while ((tagLabel = tokenizer.nextToken()) != null) {
                tagLabel = tagLabel.trim();
                boolean isTagAvailable = false;
                for (final TourTag tourTag : allTourTags) {
                    if (tourTag.getTagName().equals(tagLabel)) {
                        isTagAvailable = true;
                        tourTags.add(tourTag);
                        break;
                    }
                }
                if (isTagAvailable == false) {
                    final TourTag tourTag = new TourTag(tagLabel);
                    tourTag.setRoot(true);
                    final TourTag savedTag = TourDatabase.saveEntity(tourTag, TourDatabase.ENTITY_IS_NOT_SAVED, TourTag.class);
                    if (savedTag != null) {
                        tourTags.add(savedTag);
                        TourDatabase.clearTourTags();
                        tourTagMap = TourDatabase.getAllTourTags();
                        allTourTags = tourTagMap.values().toArray(new TourTag[tourTagMap.size()]);
                        isNewTag = true;
                    }
                }
            }
        } catch (final NoSuchElementException e) {
        } finally {
            tourData.setTourTags(tourTags);
        }
        return isNewTag;
    }

    private void parseTime(final TourData tourData, final String nextToken) {
        final int tourHour = Integer.parseInt(nextToken.substring(0, 2));
        final int tourMin = Integer.parseInt(nextToken.substring(3, 5));
        tourData.setStartMinute((short) tourMin);
        tourData.setStartHour((short) tourHour);
    }

    /**
	 * @param tourData
	 * @param parsedTourTypeLabel
	 * @return <code>true</code> when a new {@link TourType} is created
	 */
    private boolean parseTourType(final TourData tourData, final String parsedTourTypeLabel) {
        final ArrayList<TourType> tourTypeMap = TourDatabase.getAllTourTypes();
        TourType tourType = null;
        for (final TourType mapTourType : tourTypeMap) {
            if (parsedTourTypeLabel.equalsIgnoreCase(mapTourType.getName())) {
                tourType = mapTourType;
                break;
            }
        }
        TourType newSavedTourType = null;
        if (tourType == null) {
            final TourType newTourType = new TourType(parsedTourTypeLabel);
            final TourTypeColorDefinition newColorDefinition = new TourTypeColorDefinition(newTourType, Long.toString(newTourType.getTypeId()), newTourType.getName());
            newTourType.setColorBright(newColorDefinition.getDefaultGradientBright());
            newTourType.setColorDark(newColorDefinition.getDefaultGradientDark());
            newTourType.setColorLine(newColorDefinition.getDefaultLineColor());
            newTourType.setColorText(newColorDefinition.getDefaultTextColor());
            newSavedTourType = TourDatabase.saveEntity(newTourType, newTourType.getTypeId(), TourType.class);
            if (newSavedTourType != null) {
                tourType = newSavedTourType;
            }
        }
        tourData.setTourType(tourType);
        return newSavedTourType != null;
    }

    @Override
    public boolean processDeviceData(final String importFilePath, final DeviceData deviceData, final HashMap<Long, TourData> alreadyImportedTours, final HashMap<Long, TourData> newlyImportedTours) {
        boolean returnValue = false;
        boolean isNewTag = false;
        boolean isNewTourType = false;
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(importFilePath));
            final String fileHeader = fileReader.readLine();
            if (isFileValid(fileHeader) == false) {
                return false;
            }
            String tokenLine;
            while ((tokenLine = fileReader.readLine()) != null) {
                int distance = 0;
                int duration = 0;
                int pausedTime = 0;
                final TourData tourData = new TourData();
                try {
                    final String[] allToken = tokenLine.split(CSV_TOKEN_SEPARATOR);
                    parseDate(tourData, allToken[0]);
                    parseTime(tourData, allToken[1]);
                    duration = Integer.parseInt(allToken[2]);
                    tourData.setTourRecordingTime(duration);
                    pausedTime = Integer.parseInt(allToken[3]);
                    tourData.setTourDrivingTime(Math.max(0, duration - pausedTime));
                    distance = Integer.parseInt(allToken[4]);
                    tourData.setTourDistance(distance);
                    tourData.setTourTitle(allToken[5]);
                    tourData.setTourDescription(allToken[6]);
                    isNewTourType |= parseTourType(tourData, allToken[7]);
                    isNewTag |= parseTags(tourData, allToken[8]);
                } catch (final NoSuchElementException e) {
                } finally {
                    tourData.importRawDataFile = importFilePath;
                    tourData.setTourImportFilePath(importFilePath);
                    final Long tourId = tourData.createTourId(Integer.toString(distance));
                    if (alreadyImportedTours.containsKey(tourId) == false) {
                        newlyImportedTours.put(tourId, tourData);
                        tourData.setDeviceId(deviceId);
                        tourData.setDeviceName(visibleName);
                        tourData.setWeek(tourData.getStartYear(), tourData.getStartMonth(), tourData.getStartDay());
                        returnValue = true;
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
                final Display display = Display.getDefault();
                if (isNewTag) {
                    display.syncExec(new Runnable() {

                        public void run() {
                            TourManager.fireEvent(TourEventId.TAG_STRUCTURE_CHANGED, null);
                        }
                    });
                }
                if (isNewTourType) {
                    TourDatabase.clearTourTypes();
                    TourManager.getInstance().clearTourDataCache();
                    display.syncExec(new Runnable() {

                        public void run() {
                            TourbookPlugin.getDefault().getPreferenceStore().setValue(ITourbookPreferences.TOUR_TYPE_LIST_IS_MODIFIED, Math.random());
                        }
                    });
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return returnValue;
    }

    /**
	 * checks if the data file has a valid .crp data format
	 * 
	 * @return true for a valid .crp data format
	 */
    public boolean validateRawData(final String fileName) {
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(fileName));
            final String fileHeader = fileReader.readLine();
            if (fileHeader == null || isFileValid(fileHeader) == false) {
                return false;
            }
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (final IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return true;
    }
}
