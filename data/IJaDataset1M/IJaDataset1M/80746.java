package com.uspto.pati.Redbook;

public class RedBookFactory {

    public static void main(String[] args) {
        try {
            UnzipRedbookFile.unzipFiles();
            XmlCutter.oldDTDSplit();
            XmlCutter.newDTDSplit();
            RedBookRenameFiles.filterRedBookFiles();
            XMLTransformation.transformRedBookFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
