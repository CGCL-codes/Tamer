package org.dicom4j.samples.io.media;

import org.apache.log4j.PropertyConfigurator;
import org.dicom4j.core.Dicom4j;
import org.dicom4j.data.directory.RootDicomDirectory;
import org.dicom4j.data.directory.record.DicomDirectoryRecord;
import org.dicom4j.io.media.DicomDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * sample class to Dump a DicomDir into a log file
 * 
 * @since 0.0.4
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public class DicomDirDump {

    /**
	 * the Logger
	 */
    private static final Logger flogger = LoggerFactory.getLogger(DicomDirDump.class);

    /**
	 * dump One Item
	 * 
	 * @param aItem
	 * @throws Exception
	 */
    public static void DumpItem(DicomDirectoryRecord aItem) throws Exception {
        for (int i = 0; i < aItem.getChildCount(); i++) {
            DicomDirectoryRecord lChild = (DicomDirectoryRecord) aItem.getChildAt(i);
            flogger.info(lChild.toString());
            DumpItem(lChild);
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        PropertyConfigurator.configure(".//samples-log4j.properties");
        flogger.info("DicomDirDump.start");
        try {
            Dicom4j.configure();
            DicomDir lDir = new DicomDir(".//DICOMDIR");
            lDir.open();
            RootDicomDirectory lDicomdir = lDir.getDicomDirectory();
            DicomDirectoryRecord lItem = lDicomdir.getRoot();
            DumpItem(lItem);
            flogger.info("DicomDirDump.stop");
        } catch (Exception e) {
            flogger.error(e.getMessage());
        }
    }
}
