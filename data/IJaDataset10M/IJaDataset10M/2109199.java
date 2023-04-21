package org.sf.scounter.saverimpl;

import java.io.*;
import java.util.*;
import org.sf.scounter.*;
import org.sf.scounter.utilty.FileHelper;
import org.sf.scounter.utilty.RCHelper;

;

/**
 * @author <a href="mailto:jonahliu@263.net">jonah liu</a>
 *
 */
public final class HtmlSaver implements ResultSaver {

    /**
	 * save calculate results to a file in html format. when use this saver file
	 * name and path must be special by property name file.name
	 *
	 * @note the property named file.name must be special
	 * @see org.sf.scounter.ResultSaver#saveResult(java.util.Collection,
	 *      java.util.Properties)
	 */
    public void saveResult(Collection<CounterResult> collection, File root, Properties prop) throws Exception {
        Map<String, Collection<CounterResult>> dirMap = RCHelper.getPackages(collection, root);
        String fileName = prop.getProperty("file.name");
        String style = prop.getProperty("style");
        File file = new File(fileName);
        FileOutputStream outSream = new FileOutputStream(file);
        PrintWriter pw = new PrintWriter(outSream);
        StringBuilder sb = new StringBuilder();
        sb.append(HtmlEntity.htmlStart);
        sb.append(HtmlEntity.summary);
        sb.append(HtmlEntity.tableStart);
        sb.append(HtmlEntity.makeTR(FileHelper.getTableHeader(), false));
        int i = 0;
        for (String dir : RCHelper.order(dirMap.keySet())) {
            StaticsResult statics = RCHelper.getStatics(dirMap.get(dir));
            statics.setDir(dir);
            sb.append(HtmlEntity.makeTR(RCHelper.getStaticRow(statics), (i % 2 == 0)));
            i++;
        }
        sb.append(HtmlEntity.tableEnd);
        sb.append(HtmlEntity.br);
        sb.append(HtmlEntity.details);
        sb.append(HtmlEntity.tableStart);
        sb.append(HtmlEntity.makeTR(FileHelper.getDetailTableHead(style), false));
        i = 0;
        for (CounterResult result : collection) {
            sb.append(HtmlEntity.makeTR(RCHelper.getDetailRow(result, style), (i % 2 == 0)));
            i++;
        }
        sb.append(HtmlEntity.tableEnd);
        sb.append(HtmlEntity.br);
        sb.append(HtmlEntity.support);
        sb.append(HtmlEntity.htmlEnd);
        pw.println(sb.toString());
        pw.close();
    }
}
