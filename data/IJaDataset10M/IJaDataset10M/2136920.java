package org.databene.document.csv;

/**
 * Callback interface for CSV parsing.<br/>
 * <br/>
 * Created: 26.10.2007 12:53:01
 */
public interface CSVLineHandler {

    void handle(String[] cells);
}
