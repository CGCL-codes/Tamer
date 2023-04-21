package midnightmarsbrowser.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import midnightmarsbrowser.application.MMBWorkspace;
import midnightmarsbrowser.metadata.ImageMetadata;
import midnightmarsbrowser.metadata.ImageMetadataEntry;
import midnightmarsbrowser.metadata.ObservationMetadata;
import midnightmarsbrowser.metadata.ObservationMetadataEntry;

public class TimeIntervalList {

    private static final String[] solarFilters = new String[] { "L8", "R8" };

    private static String[] sundialCmdSeqNbrs = new String[] { "P209", "P210", "P211", "P212", "P280", "P281", "P282", "P283", "P284", "P285" };

    private static double matchTolerance = 0.1;

    String roverCode;

    TimeInterval[] entries = null;

    private TimeIntervalList(String roverCode) {
        this.roverCode = roverCode;
    }

    public String getRoverCode() {
        return roverCode;
    }

    public TimeInterval[] getEntries() {
        return entries;
    }

    public TimeInterval getEntry(int index) {
        if ((entries != null) && (index >= 0)) {
            return entries[index];
        }
        return null;
    }

    public int getLength() {
        if (entries != null) {
            return entries.length;
        }
        return 0;
    }

    public int indexOf(TimeInterval timeInterval) {
        if (entries == null) return -1;
        for (int n = 0; n < entries.length; n++) {
            if (entries[n] == timeInterval) return n;
        }
        return -1;
    }

    public TimeInterval getEntry(LocationCounter location) {
        TimeInterval entry = null;
        for (int n = 0; n < entries.length; n++) {
            if (entries[n].startLocation.equals(location)) {
                return entries[n];
            }
        }
        return null;
    }

    public TimeInterval findEntryClosestToLocation(LocationCounter location) {
        for (int n = 0; n < entries.length; n++) {
            TimeInterval entry = entries[n];
            if (entry.startLocation.compareTo(location) >= 0) {
                return entry;
            }
        }
        return null;
    }

    public TimeInterval findTimeFrameClosestTo(ImageEntry imageEntry) {
        if (entries.length == 0) return null;
        if (entries.length == 1) return entries[0];
        for (int n = 0; n < entries.length - 1; n++) {
            TimeInterval tfEntry = entries[n];
            if (imageEntry.roverClock <= tfEntry.endTime) {
                return tfEntry;
            }
            TimeInterval tfEntry2 = entries[n + 1];
            if (imageEntry.roverClock < tfEntry2.startTime) {
                if (imageEntry.roverClock - tfEntry.endTime > tfEntry2.startTime - imageEntry.roverClock) {
                    return tfEntry;
                } else {
                    return tfEntry2;
                }
            }
        }
        return entries[entries.length - 1];
    }

    public ImageEntry findImageEntryClosestTo(ImageEntry imageEntry) {
        TimeInterval timeFrame = findTimeFrameClosestTo(imageEntry);
        if (timeFrame != null) {
            return timeFrame.findImageEntryClosestTo(imageEntry);
        } else {
            return null;
        }
    }

    /**
	 * Find the time frame list, which contains image lists for each time frame, based on the settings.
	 * @param workspace
	 * @param settings
	 * @param limitImagePathnames
	 * @return
	 */
    public static TimeIntervalList findTimeFrameList(MMBWorkspace workspace, ViewerSettings settings, HashSet limitImagePathnames) {
        ArrayList entriesList = new ArrayList();
        ImageIndex index = workspace.getImageIndex();
        ImageMetadata imageMetadata = workspace.getImageMetadata();
        ObservationMetadata observationMetadata = workspace.getObservationMetadata();
        for (int n = settings.fromSol; n <= settings.toSol; n++) {
            findImageSetSol(entriesList, index, settings, new Integer(n), limitImagePathnames, imageMetadata, observationMetadata);
        }
        ImageEntry[] entries = new ImageEntry[entriesList.size()];
        entries = (ImageEntry[]) entriesList.toArray(entries);
        if (entries.length > 0) {
            Arrays.sort(entries);
        }
        TimeIntervalList newTimeFrameList = new TimeIntervalList(settings.roverCode);
        ArrayList timeIntervalList = new ArrayList();
        TimeInterval currentTimeInterval = null;
        ArrayList currentImageList = null;
        for (int n = 0; n < entries.length; n++) {
            ImageEntry imageEntry = entries[n];
            if (currentTimeInterval == null || (settings.splitByLocation && (!currentTimeInterval.endLocation.equals(imageEntry.location))) || (settings.splitBySol && (currentTimeInterval.endSol != imageEntry.sol)) || (settings.splitByElapsedTime && (imageEntry.roverClock - currentTimeInterval.endTime > settings.splitElapsedTimeMinutes * 60))) {
                if (currentTimeInterval != null) {
                    currentTimeInterval.finishSetup(currentImageList, workspace);
                }
                currentTimeInterval = new TimeInterval(newTimeFrameList);
                currentTimeInterval.startTime = imageEntry.roverClock;
                currentTimeInterval.startSol = imageEntry.sol;
                currentTimeInterval.startLocation = imageEntry.location;
                timeIntervalList.add(currentTimeInterval);
                currentImageList = new ArrayList();
            }
            currentTimeInterval.endTime = imageEntry.roverClock;
            currentTimeInterval.endSol = imageEntry.sol;
            currentTimeInterval.endLocation = imageEntry.location;
            imageEntry.parent = currentTimeInterval;
            currentImageList.add(imageEntry);
        }
        if (currentTimeInterval != null) {
            currentTimeInterval.finishSetup(currentImageList, workspace);
        }
        if (currentImageList != null) {
            currentTimeInterval.imageList = new ImageEntry[currentImageList.size()];
            currentTimeInterval.imageList = (ImageEntry[]) currentImageList.toArray(currentTimeInterval.imageList);
        }
        TimeInterval[] timeIntervalArray = new TimeInterval[timeIntervalList.size()];
        timeIntervalArray = (TimeInterval[]) timeIntervalList.toArray(timeIntervalArray);
        newTimeFrameList.entries = timeIntervalArray;
        if (settings.panorama) {
            for (int n = 0; n < timeIntervalArray.length; n++) {
                TimeInterval interval = timeIntervalArray[n];
                ImageEntry[] images = interval.imageList;
                for (int i = 0; i < images.length; i++) {
                    ImageEntry entry = images[i];
                    ImageEntry matchingEntry = null;
                    ImageEntry testEntry;
                    for (int j = 0; j < i; j++) {
                        testEntry = images[j];
                        if (testEntry.enabled) {
                            double dAz = testEntry.getImageMetadataEntry().inst_az_rover - entry.getImageMetadataEntry().inst_az_rover;
                            double dEl = testEntry.getImageMetadataEntry().inst_el_rover - entry.getImageMetadataEntry().inst_el_rover;
                            if ((Math.abs(dAz) < matchTolerance) && (Math.abs(dEl) < matchTolerance)) {
                                matchingEntry = testEntry;
                                break;
                            }
                        }
                    }
                    if (matchingEntry != null) {
                        if (entry.imageClass > matchingEntry.imageClass) {
                            matchingEntry.enabled = false;
                            entry.enabled = true;
                        } else {
                            entry.enabled = false;
                        }
                    }
                }
            }
        }
        for (int n = 0; n < timeIntervalArray.length; n++) {
            timeIntervalArray[n].countEnabledImages();
        }
        return newTimeFrameList;
    }

    private static void findImageSetSol(ArrayList entries, ImageIndex index, ViewerSettings settings, Integer sol, HashSet limitImagePathnames, ImageMetadata imageMetadata, ObservationMetadata observationMetadata) {
        if (settings.left || settings.right) {
            if (settings.n) {
                addToEntries(entries, index, settings, sol, limitImagePathnames, "n", imageMetadata, observationMetadata);
            }
            if (settings.p) {
                addToEntries(entries, index, settings, sol, limitImagePathnames, "p", imageMetadata, observationMetadata);
            }
            if (!settings.panorama) {
                if (settings.f) {
                    addToEntries(entries, index, settings, sol, limitImagePathnames, "f", imageMetadata, observationMetadata);
                }
                if (settings.r) {
                    addToEntries(entries, index, settings, sol, limitImagePathnames, "r", imageMetadata, observationMetadata);
                }
                if (settings.m) {
                    addToEntries(entries, index, settings, sol, limitImagePathnames, "m", imageMetadata, observationMetadata);
                }
            }
        }
        if ((!settings.panorama) || (!settings.left && !settings.right)) {
            if (settings.n && settings.anaglyph) {
                addToEntries(entries, index, settings, sol, limitImagePathnames, "na", imageMetadata, observationMetadata);
            }
            if (settings.p && settings.anaglyph) {
                addToEntries(entries, index, settings, sol, limitImagePathnames, "pa", imageMetadata, observationMetadata);
            }
        }
        if (settings.p && settings.pancamMMBFalseColor) {
            addToEntries(entries, index, settings, sol, limitImagePathnames, "pc", imageMetadata, observationMetadata);
        }
        if (settings.p && settings.pancamDCCalibratedColor) {
            addToEntries(entries, index, settings, sol, limitImagePathnames, "ps", imageMetadata, observationMetadata);
        }
        if (!settings.panorama) {
            if (settings.f && settings.anaglyph) {
                addToEntries(entries, index, settings, sol, limitImagePathnames, "fa", imageMetadata, observationMetadata);
            }
            if (settings.r && settings.anaglyph) {
                addToEntries(entries, index, settings, sol, limitImagePathnames, "ra", imageMetadata, observationMetadata);
            }
        }
    }

    private static void addToEntries(ArrayList entries, ImageIndex index, ViewerSettings settings, Integer sol, HashSet limitImagePathnames, String imageCategory, ImageMetadata imageMetadata, ObservationMetadata observationMetadata) {
        String solString = "" + sol;
        HashMap files = index.getFiles(settings.roverCode, imageCategory, sol);
        if (files != null) {
            Iterator iter = files.keySet().iterator();
            while (iter.hasNext()) {
                String filename = ((String) iter.next());
                File file = (File) files.get(filename);
                try {
                    String testProductType = MerUtils.productTypeFromFilename(filename);
                    if (imageCategory.length() == 1) {
                        if (filename.length() != 31) continue;
                        if (filename.endsWith(".JPG")) {
                            if (!settings.pancamRaw) continue;
                        } else if (filename.endsWith(".IMG")) {
                            if (testProductType.equals("EFF") || testProductType.equals("EDN") || testProductType.equals("ESF")) {
                                if (!settings.pdsEDR) continue;
                            } else if (testProductType.equals("MRD")) {
                                if (!settings.pdsMRD) continue;
                            } else if (testProductType.equals("RAD")) {
                                if (!settings.pdsRAD) continue;
                            } else {
                                continue;
                            }
                        } else {
                            continue;
                        }
                    } else {
                        if (!filename.endsWith(".JPG") || filename.endsWith("_TH.JPG") || filename.length() < 31) continue;
                    }
                    if (imageCategory.equals("f") || imageCategory.equals("r")) {
                        if ((MerUtils.cameraEyeFromFilename(filename) == 'L') && (!settings.left)) continue;
                        if ((MerUtils.cameraEyeFromFilename(filename) == 'R') && (!settings.right)) continue;
                    } else if (imageCategory.equals("n")) {
                        if ((MerUtils.cameraEyeFromFilename(filename) == 'L') && (!settings.left)) continue;
                        if (settings.panorama) {
                            if ((MerUtils.cameraEyeFromFilename(filename) == 'R') && (!settings.right || settings.left)) continue;
                        } else {
                            if ((MerUtils.cameraEyeFromFilename(filename) == 'R') && (!settings.right)) continue;
                        }
                    } else if (imageCategory.equals("m") && !(settings.left || settings.right)) {
                        continue;
                    } else if (imageCategory.equals("p")) {
                        if ((MerUtils.cameraEyeFromFilename(filename) == 'L') && (!settings.left)) continue;
                        if (settings.panorama) {
                            if ((MerUtils.cameraEyeFromFilename(filename) == 'R') && (!(settings.right && !settings.left))) continue;
                        } else {
                            if ((MerUtils.cameraEyeFromFilename(filename) == 'R') && (!settings.right)) continue;
                        }
                    } else if (imageCategory.equals("pc")) {
                        if ((MerUtils.cameraEyeFromFilename(filename) == 'L') && (!(settings.left))) continue;
                        if (settings.panorama) {
                            if ((MerUtils.cameraEyeFromFilename(filename) == 'R') && (!(settings.right))) continue;
                        } else {
                            if ((MerUtils.cameraEyeFromFilename(filename) == 'R') && (!(settings.right && !settings.left))) continue;
                        }
                    } else if (imageCategory.equals("ps")) {
                        if (!settings.left) continue;
                    }
                    String cmdSeqNbr = null;
                    String siteDriveNum = null;
                    try {
                        cmdSeqNbr = filename.substring(18, 23);
                        siteDriveNum = filename.substring(14, 18);
                    } catch (IndexOutOfBoundsException e) {
                        continue;
                    }
                    if (arrayHasAnEntry(settings.includeCmdSeqs)) {
                        if (!arrayMatchesStringStart(settings.includeCmdSeqs, cmdSeqNbr)) {
                            continue;
                        }
                    }
                    if (arrayHasAnEntry(settings.excludeCmdSeqs)) {
                        if (arrayMatchesStringStart(settings.excludeCmdSeqs, cmdSeqNbr)) {
                            continue;
                        }
                    }
                    if (arrayHasAnEntry(settings.includeSiteDriveNumber)) {
                        if (!arrayMatchesStringStart(settings.includeSiteDriveNumber, siteDriveNum)) {
                            continue;
                        }
                    }
                    if (settings.excludeDriveImages) {
                        if ((cmdSeqNbr.startsWith("F")) || (cmdSeqNbr.startsWith("f"))) {
                            continue;
                        }
                    }
                    if (settings.excludeSundial) {
                        if (arrayMatchesStringStart(sundialCmdSeqNbrs, cmdSeqNbr)) {
                            continue;
                        }
                    }
                    if (imageCategory.equals("p")) {
                        String pancamFilter = filename.substring(23, 25);
                        if (arrayHasAnEntry(settings.includePancamFilter)) {
                            if (!arrayMatchesStringStart(settings.includePancamFilter, pancamFilter)) {
                                continue;
                            }
                        }
                        if (arrayHasAnEntry(settings.excludePancamFilter)) {
                            if (arrayMatchesStringStart(settings.excludePancamFilter, pancamFilter)) {
                                continue;
                            }
                        }
                        if (settings.excludeSolarFilter) {
                            if (arrayMatchesStringStart(solarFilters, pancamFilter)) {
                                continue;
                            }
                        }
                    }
                    LocationCounter location = new LocationCounter(siteDriveNum);
                    String spacecraftId = MerUtils.roverCodeFromFilename(filename);
                    Integer roverClock = MerUtils.roverClockIntegerFromFilename(filename);
                    ImageMetadataEntry imageMetadataEntry = imageMetadata.getEntry(spacecraftId, roverClock);
                    if (imageMetadataEntry != null && imageMetadataEntry.rmc_site >= 0) {
                        location.site = imageMetadataEntry.rmc_site;
                        location.drive = imageMetadataEntry.rmc_drive;
                    }
                    if (settings.panorama && imageMetadataEntry == null) {
                        continue;
                    }
                    int framingType = 0;
                    if (testProductType.equals("EFF")) {
                        framingType = 1;
                    } else if (testProductType.equals("EDN")) {
                        framingType = 2;
                    } else if (testProductType.equals("ESF")) {
                        framingType = 3;
                    } else if (imageMetadataEntry != null) {
                        if (imageMetadataEntry.pixel_averaging_height > 1 || imageMetadataEntry.pixel_averaging_width > 1) {
                            framingType = 2;
                        } else if (imageMetadataEntry.first_line != 1 || imageMetadataEntry.first_line_sample != 1 || imageMetadataEntry.n_lines != 1024 || imageMetadataEntry.n_line_samples != 1024) {
                            framingType = 3;
                        } else {
                            framingType = 1;
                        }
                    }
                    if (framingType == 1 && !settings.fullFrame) {
                        continue;
                    } else if (framingType == 2 && !settings.downsampled) {
                        continue;
                    } else if (framingType == 3 && !settings.subFrame) {
                        continue;
                    }
                    String observationDescription = null;
                    if (imageMetadataEntry != null && imageMetadataEntry.obsID != null) {
                        ObservationMetadataEntry obsEntry = observationMetadata.getEntry(imageMetadataEntry.obsID);
                        if (obsEntry != null) {
                            observationDescription = obsEntry.description;
                        }
                    }
                    if (settings.doLimitKeywords && settings.limitKeywords != null && settings.limitKeywords.length > 0) {
                        if (observationDescription == null) continue;
                        boolean found = false;
                        for (int n = 0; n < settings.limitKeywords.length; n++) {
                            if (observationDescription.toUpperCase().indexOf(settings.limitKeywords[n].toUpperCase()) >= 0) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) continue;
                    }
                    if ((limitImagePathnames == null) || (!settings.limitToSet) || (limitImagePathnames.contains(file.getName()))) {
                        ImageEntry newEntry = new ImageEntry(filename, file.getAbsolutePath(), imageCategory);
                        newEntry.sol = sol.intValue();
                        newEntry.solString = solString;
                        newEntry.roverClock = roverClock.intValue();
                        newEntry.location = location;
                        newEntry.imageMetadataEntry = imageMetadataEntry;
                        newEntry.observationDescription = observationDescription;
                        entries.add(newEntry);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean arrayHasAnEntry(String[] array) {
        if (array != null) {
            for (int n = 0; n < array.length; n++) {
                String test = array[n];
                if (test.length() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean arrayMatchesStringStart(String[] array, String str) {
        if (array != null) {
            for (int n = 0; n < array.length; n++) {
                String test = array[n];
                if (test.length() > 0) {
                    if (str.startsWith(test)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
