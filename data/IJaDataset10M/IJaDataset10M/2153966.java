package com.benrhughes.absxml;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class SheetFactory {

    public static String getSheet(String uri, int sheetNo) throws IOException, BiffException {
        sheetNo--;
        String key = uri + sheetNo;
        StringBuilder data = Cache.get(key);
        if (data == null) {
            URL url = new URL(uri);
            URLConnection uc = url.openConnection();
            uc.setConnectTimeout(10000);
            Workbook w = Workbook.getWorkbook(uc.getInputStream());
            int n = w.getNumberOfSheets();
            for (int i = 0; i < n; i++) {
                Cache.add(uri + i, XMLConverter.convert(w, uri, i));
            }
            data = Cache.get(key);
        }
        return data.toString();
    }
}
