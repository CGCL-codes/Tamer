package com.ojt.dao;

import com.ojt.dao.ffjdadat.FFJDADatCompetitorsDao;
import com.ojt.dao.xls.XlsCompetitorsDao;
import java.io.File;

public class CompetitorsDaoFactory {

    public static CompetitorsDao createCompetitorsDao(final File inputFile, final boolean onlyWithWeight) {
        if (inputFile != null) {
            if (inputFile.getAbsolutePath().endsWith(".xls")) {
                return new XlsCompetitorsDao(inputFile, onlyWithWeight);
            }
            if (inputFile.getAbsolutePath().endsWith(".dat")) {
                return new FFJDADatCompetitorsDao(inputFile, onlyWithWeight);
            }
        }
        throw new IllegalArgumentException("File format not supported : " + inputFile);
    }
}
