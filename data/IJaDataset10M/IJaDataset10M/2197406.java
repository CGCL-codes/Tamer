package at.ac.univie.zsu.aguataplan.service;

import at.ac.univie.zsu.aguataplan.exception.DatabaseException;
import at.ac.univie.zsu.aguataplan.exception.ExportException;

/**
 * @author gerry
 * 
 */
public interface ExportManager {

    public void exportEvent() throws DatabaseException, ExportException;

    public void exportDetectedEvent() throws DatabaseException, ExportException;

    public void dumpDatabase() throws DatabaseException;
}
