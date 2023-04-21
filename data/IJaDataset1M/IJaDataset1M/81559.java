package com.technoetic.xplanner.importer;

/**
 * User: Mateusz Prokopowicz
 * Date: Jun 7, 2005
 * Time: 2:35:48 PM
 */
public class MissingFieldSpreadsheetImporterException extends SpreadsheetImporterException {

    private String field;

    public String getField() {
        return field;
    }

    public MissingFieldSpreadsheetImporterException(String field, String message) {
        super(message);
        this.field = field;
    }
}
