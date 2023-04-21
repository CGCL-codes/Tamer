package ch.xwr.dispo.excel.rep1;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;

public class TempFileFilter implements FilenameFilter {

    @Override
    public boolean accept(File fi, String name) {
        if (name.endsWith(".xls") && name.startsWith("xwrdispo")) {
            Date df = new Date(fi.lastModified());
            Date now = new Date();
            long dif = now.getTime() - df.getTime();
            if ((dif / 1000) > 24 * 60 * 60 * 3) {
                return true;
            }
        }
        return false;
    }
}
