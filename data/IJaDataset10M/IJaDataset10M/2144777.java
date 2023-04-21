package com.duroty.lucene.parser.utils;

import org.apache.poi.hpsf.MarkUnsupportedException;
import org.apache.poi.hpsf.NoPropertySetStreamException;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hpsf.UnexpectedPropertySetTypeException;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import java.io.IOException;

/**
 * DOCUMENT ME!
 *
 * @author jordi marques TODO To change the template for this generated type
 *         comment go to Window - Preferences - Java - Code Style - Code
 *         Templates
 */
public class POIFSListener implements POIFSReaderListener {

    /** DOCUMENT ME! */
    private String author;

    /** DOCUMENT ME! */
    private String title;

    /** DOCUMENT ME! */
    private String keywords;

    /** DOCUMENT ME! */
    private String subject;

    /**
     *
     */
    public POIFSListener() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     */
    public void processPOIFSReaderEvent(POIFSReaderEvent readerEvent) {
        org.apache.poi.hpsf.PropertySet propertySet;
        try {
            propertySet = PropertySetFactory.create(readerEvent.getStream());
            SummaryInformation info = (SummaryInformation) propertySet;
            this.author = info.getAuthor();
            this.title = info.getTitle();
            this.keywords = info.getKeywords();
            this.subject = info.getSubject();
        } catch (NoPropertySetStreamException e) {
            e.printStackTrace();
        } catch (MarkUnsupportedException e) {
            e.printStackTrace();
        } catch (UnexpectedPropertySetTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAuthor() {
        return author;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSubject() {
        return subject;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTitle() {
        return title;
    }
}
