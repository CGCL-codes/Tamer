package com.genia.toolbox.projects.csv.bean;

import java.util.Date;

/**
 * represents the version of a Cvs import.
 */
public interface CsvVersion {

    /**
   * getter for the versionDate property.
   * 
   * @return the versionDate
   */
    public abstract Date getVersionDate();

    /**
   * getter for the versionNumber property.
   * 
   * @return the versionNumber
   */
    public abstract Integer getVersionNumber();
}
