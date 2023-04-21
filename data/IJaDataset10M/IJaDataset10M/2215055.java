package org.opencms.staticexport;

import org.opencms.util.CmsFileUtil;
import java.io.File;
import java.io.FileFilter;
import java.util.List;

/**
 * Concrete OnDemandExportHandler.<p>
 * 
 * The published files and folders are purged, as also all the html files in each subtree.<p>
 * 
 * @author Michael Moossen  
 * 
 * @version $Revision: 1.12 $ 
 * 
 * @since 6.0.0 
 * 
 * @see I_CmsStaticExportHandler
 */
public class CmsOnDemandHtmlSubTreeHandler extends A_CmsOnDemandStaticExportHandler {

    /**
     * @see org.opencms.staticexport.A_CmsOnDemandStaticExportHandler#getRelatedFilesToPurge(java.lang.String, java.lang.String)
     */
    protected List getRelatedFilesToPurge(String exportFileName, String vfsName) {
        FileFilter htmlFilter = new FileFilter() {

            public boolean accept(File file) {
                return file.isFile() && (file.getName().endsWith(".html") || file.getName().endsWith(".htm"));
            }
        };
        return CmsFileUtil.getFiles(exportFileName, htmlFilter, true);
    }
}
