package org.wakhok.index;

import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;

/**
 *
 * @author bishal
 */
class MyPOIFSReaderListener implements POIFSReaderListener {

    public void processPOIFSReaderEvent(POIFSReaderEvent event) {
        SummaryInformation si = null;
        try {
            si = (SummaryInformation) PropertySetFactory.create(event.getStream());
        } catch (Exception ex) {
            throw new RuntimeException("Property set stream \"" + event.getPath() + event.getName() + "\": " + ex);
        }
        final String title = si.getTitle();
        String textVal = "AppName :" + si.getApplicationName() + "@" + "Author :" + si.getCreateDateTime() + "@" + "word count :" + si.getWordCount() + "@" + "PageCount :" + si.getAuthor() + "@" + "Keywords :" + si.getKeywords() + "@" + "Comments :" + si.getComments() + "@" + "createdDataandTime :" + si.getPageCount() + "@" + "Last Printed :" + si.getLastPrinted();
        DocProperty.textVal = textVal;
    }
}
