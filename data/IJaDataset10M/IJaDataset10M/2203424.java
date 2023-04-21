package net.sf.excompcel.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author detlev struebig
 * @version v0.7
 *
 */
public class FileExtensionsHelper {

    /** Logger. */
    private static Logger log = Logger.getLogger(FileExtensionsHelper.class);

    /**
	 * Check, if Filename represents a Excel Filename.
	 * @param filename The filename.
	 * @return true, if filename end with .xls
	 */
    public static boolean isExcelFile(String filename) {
        if (StringUtils.isNotEmpty(filename)) {
            Set<String> setOfExtensions = getAllowedFileExtensionsKeysSpreadSheet();
            for (String extension : setOfExtensions) {
                if (filename.toLowerCase().endsWith("." + extension)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * Check, if Filename represents a Doc Filename.
	 * @param filename The filename.
	 * @return true, if filename end with .doc
	 */
    public static boolean isDocFile(String filename) {
        if (StringUtils.isNotEmpty(filename)) {
            Set<String> setOfExtensions = getAllowedFileExtensionsKeysTextFile();
            for (String extension : setOfExtensions) {
                if (filename.toLowerCase().endsWith("." + extension)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * Check, if the File extensions of the two filenames are equal.
	 * @param filenameOne Filename
	 * @param filenameTwo Filename
	 * @return true, if file extension is equal.
	 */
    public static boolean isSameFileExtention(String filenameOne, String filenameTwo) {
        if (StringUtils.isEmpty(filenameOne) || StringUtils.isEmpty(filenameTwo)) {
            return false;
        }
        String extOne = null;
        int idxOne = filenameOne.lastIndexOf(".");
        if (idxOne > 0) {
            extOne = filenameOne.substring(idxOne + 1);
        }
        String extTwo = null;
        int idxTwo = filenameTwo.lastIndexOf(".");
        if (idxTwo > 0) {
            extTwo = filenameTwo.substring(idxTwo + 1);
        }
        log.debug("Extension One=" + extOne + " Extension Two=" + extTwo);
        if (!StringUtils.isEmpty(extOne) && !StringUtils.isEmpty(extTwo)) {
            if (extOne.equals(extTwo)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Extract the File Extension from Filename.
	 * Sample: abc.xls return xls.
	 * @param filename Filename
	 * @return the File extension.
	 */
    public static String getFileExtension(String filename) {
        if (!StringUtils.isEmpty(filename)) {
            int dotPos = filename.lastIndexOf(".");
            if (dotPos != -1) {
                return filename.substring(dotPos + 1);
            }
        }
        return "";
    }

    /**
	 * 
	 * @return Map<String, String>
	 */
    private static Map<String, String> initFileExtensionMap() {
        Map<String, String> mapFileTypes = initFileExtensionMapSpreadSheet();
        mapFileTypes.putAll(initFileExtensionMapTextFile());
        return mapFileTypes;
    }

    /**
	 * 
	 * @return Map<String, String>
	 */
    private static Map<String, String> initFileExtensionMapTextFile() {
        Map<String, String> mapFileTypes = new HashMap<String, String>();
        mapFileTypes.put("doc", "Doc Files (*.doc)");
        return mapFileTypes;
    }

    /**
	 * 
	 * @return Map<String, String>
	 */
    private static Map<String, String> initFileExtensionMapSpreadSheet() {
        Map<String, String> mapFileTypes = new HashMap<String, String>();
        mapFileTypes.put("xls", "MS Excel 2003 (*.xls)");
        return mapFileTypes;
    }

    /**
	 * Get Allowed File Extensions
	 * @return Map<String, String>
	 */
    public static Map<String, String> getAllowedFileExtensions() {
        return initFileExtensionMap();
    }

    /**
	 * Get Allowed File Extensions
	 * @return Map<String, String>
	 */
    public static Map<String, String> getAllowedFileExtensionsSpreadsheMap() {
        return initFileExtensionMapSpreadSheet();
    }

    /**
	 * Get Allowed File Extensions
	 * @return Map<String, String>
	 */
    public static Map<String, String> getAllowedFileExtensionsTextFile() {
        return initFileExtensionMapTextFile();
    }

    /**
	 * Get Keys of Allowed FileExtensions.
	 * eg. xls, xlsx, odt, doc
	 * @return Set<String>
	 */
    public static Set<String> getAllowedFileExtensionsKeys() {
        Map<String, String> mapFileTypes = initFileExtensionMap();
        return mapFileTypes.keySet();
    }

    /**
	 * Get Keys of Allowed FileExtensions.
	 * eg. xls, xlsx, odt, doc
	 * @return Set<String>
	 */
    public static Set<String> getAllowedFileExtensionsKeysSpreadSheet() {
        Map<String, String> mapFileTypes = initFileExtensionMapSpreadSheet();
        return mapFileTypes.keySet();
    }

    /**
	 * Get Keys of Allowed FileExtensions.
	 * eg. xls, xlsx, odt, doc
	 * @return Set<String>
	 */
    public static Set<String> getAllowedFileExtensionsKeysTextFile() {
        Map<String, String> mapFileTypes = initFileExtensionMapTextFile();
        return mapFileTypes.keySet();
    }
}
