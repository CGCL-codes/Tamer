package com.technoetic.xplanner.importer.spreadsheet;

import java.io.IOException;
import java.io.InputStream;
import com.technoetic.xplanner.importer.BaseTestCase;
import com.technoetic.xplanner.importer.SpreadsheetStoryFactory;
import com.technoetic.xplanner.importer.util.IOStreamFactory;

public class TestSpreadsheet extends BaseTestCase {

    Spreadsheet spreadsheet;

    public void testOpen() throws Exception {
        String path = "jkhjhk:\\No-such path";
        spreadsheet = new Spreadsheet(new IOStreamFactory() {

            public InputStream newInputStream(String path) throws IOException {
                throw new IOException();
            }
        }, new SpreadsheetStoryFactory());
        spreadsheet.open(path);
    }
}
