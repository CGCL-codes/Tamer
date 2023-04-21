package org.opennms.mavenize;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.codehaus.plexus.util.FileUtils;

public class FilterType extends SourceType {

    public FilterType(String typeName) {
        super(typeName);
    }

    public void afterSaveFileSets(File baseDir, String target, PomBuilder builder) throws Exception {
        super.afterSaveFileSets(baseDir, target, builder);
        List fileNames = FileUtils.getFileNames(baseDir, target + "/**/*.properties", null, false);
        for (Iterator it = fileNames.iterator(); it.hasNext(); ) {
            String fileName = (String) it.next();
            builder.addFilterFile(fileName);
        }
    }
}
