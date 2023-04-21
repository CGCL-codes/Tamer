package com.c2b2.ipoint.processing.jobs;

import com.c2b2.ipoint.model.Property;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
  * This job cleans up old files out of the JFree Chart directory 
  * <p>
  * $Date: 2005/12/26 21:13:24 $
  * 
  * $Id: CleanUploadDirectory.java,v 1.1 2005/12/26 21:13:24 steve Exp $<br/>
  * 
  * Copyright 2005 C2B2 Consulting Limited. All rights reserved.
  * </p>
  * @author $Author: steve $
  * @version $Revision: 1.1 $
  */
public class CleanUploadDirectory extends SchedulableJob {

    /**
   * Default Constructor required by Job Scheduler
   */
    public CleanUploadDirectory() {
    }

    /**
   * Deletes all files over an hour old from the Upload Directory
   * @throws JobException 
   */
    public void executeJob() throws JobException {
        String p = Property.getPropertyValue("UploadLocation");
        if (p != null) {
            File directory = new File(p);
            if (!directory.isDirectory()) {
                throw new JobException(p + " is not a directory");
            }
            File files[] = directory.listFiles();
            Calendar then = new GregorianCalendar();
            then.setTime(new Date());
            then.set(Calendar.HOUR_OF_DAY, -1);
            Date test = then.getTime();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isFile() && file.lastModified() < test.getTime()) {
                    myLogger.info("Deleting File " + file.getName());
                    file.delete();
                }
            }
        } else {
            throw new JobException("Could not find the JFreeChartGraphicDirectory Property ");
        }
    }
}
