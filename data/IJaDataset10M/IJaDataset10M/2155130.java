package org.fao.fenix.domain.loader.filetypes;

import java.util.List;

public class FenixZipFile {

    String location;

    private List<CsvFilePoor> csvFileList;

    public List<CsvFilePoor> getCsvFileList() {
        return csvFileList;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCsvFileList(List<CsvFilePoor> csvFileList) {
        this.csvFileList = csvFileList;
    }
}
