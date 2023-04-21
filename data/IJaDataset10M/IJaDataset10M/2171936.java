package com.dotmarketing.portlets.contentlet.factories;

import java.io.Serializable;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import com.dotmarketing.util.LuceneUtils;
import com.dotmarketing.util.UtilMethods;

/** @author Hibernate CodeGenerator */
public class ReindexationProcessStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private static boolean inFullReindexation = false;

    private static int contentCountToIndex = -1;

    private static int contentCountIndexed = -1;

    private static Date lastIndexationStartTime = null;

    private static Date lastIndexationEndTime = null;

    public static synchronized boolean inFullReindexation() {
        return inFullReindexation;
    }

    public static synchronized void setInFullReindexation(boolean newValue) {
        inFullReindexation = newValue;
        if (!newValue) {
            contentCountToIndex = -1;
            lastIndexationEndTime = new Date();
        } else {
            lastIndexationStartTime = new Date();
        }
    }

    public static synchronized int getContentCountToIndex() {
        return contentCountToIndex;
    }

    public static synchronized void setContentCountToIndex(int count) {
        contentCountToIndex = count;
        contentCountIndexed = 0;
        setInFullReindexation(true);
    }

    public static synchronized int getLastIndexationProgress() {
        return contentCountIndexed;
    }

    public static synchronized void updateIndexationProgress(int increment) {
        contentCountIndexed += increment;
    }

    public static synchronized String currentIndexPath() {
        return LuceneUtils.getCurrentLuceneDirPath();
    }

    public static synchronized String getNewIndexPath() {
        return LuceneUtils.getNewLuceneDirPath();
    }

    public static synchronized Map getProcessIndexationMap() {
        Map<String, Object> theMap = new Hashtable<String, Object>();
        theMap.put("inFullReindexation", inFullReindexation());
        theMap.put("contentCountToIndex", getContentCountToIndex());
        theMap.put("lastIndexationProgress", getLastIndexationProgress());
        theMap.put("currentIndexPath", currentIndexPath());
        theMap.put("newIndexPath", getNewIndexPath());
        theMap.put("lastIndexationStartTime", UtilMethods.dateToHTMLDate(lastIndexationStartTime) + " " + UtilMethods.dateToHTMLTime(lastIndexationStartTime));
        theMap.put("lastIndexationEndTime", UtilMethods.dateToHTMLDate(lastIndexationEndTime) + " " + UtilMethods.dateToHTMLTime(lastIndexationEndTime));
        return theMap;
    }
}
