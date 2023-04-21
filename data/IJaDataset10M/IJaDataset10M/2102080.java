package com.gestioni.adoc.aps.system.services.scanner;

import java.util.Map;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.gestioni.adoc.aps.system.services.scanner.model.ScannerConfig;

/**
 * @version 1.0
 * @author Gabriele Milia
 * 
 */
public interface IScannerManager {

    /**
	 * Execute the command scanimage -d... for the specific file and put the result on a tempdirectory  
	 * @param the scanning name
	 * @return exit code ( 0 -> no problem , 46->busy)
	 * @throws ApsSystemException 
	 */
    public Integer execScan(String scanFile, Integer idScanner) throws ApsSystemException;

    /**
	 * Extract the config to 
	 * @return scanner configuration
	 */
    public ScannerConfig getConfig();

    public Integer searchForScanner() throws ApsSystemException;

    public String getConfScanPath();

    public String getScanTempDiskFolder();

    public Map<String, String> getScannerSettings();

    public Map<Integer, String> getScannersFound();

    public String generatePreviewImage(String image) throws ApsSystemException;

    public String deviceFound(String idDevSelected) throws ApsSystemException;

    public int convert(String file, String fileFormat, String anotherFile, String anotherFileFormat) throws ApsSystemException;

    public static final String PAR_QUALITY = "quality";

    public static final String PAR_ORNT = "ornt";

    public static final String PAR_MODE = "mode";

    public static final String PAR_BRIGHT = "quality";

    public static final String PAR_ROTATE = "rotate";

    public static final String PAR_SCALE = "scale";

    public static final String PAR_FILE_TYPE = "fileType";

    public static final String PAR_FILE_TYPE_PREVIEW = "fileTypePreview";

    public static final String PAR_WIDTH = "width";

    public static final String PAR_HEIGHT = "height";

    public static final String PREVIEW_IMAGE = "previewImage";

    public static final String SCANNERS_CONF = "scanners.conf";
}
