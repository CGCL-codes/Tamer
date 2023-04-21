package org.pojosoft.lms.support;

import org.pojosoft.core.configuration.ConfigurationService;
import java.io.File;

/**
 * Lms Config Utils
 * @author POJO Software
 * @version 1.0
 * @since 1.0
 */
public class ConfigUtils {

    public static String getTestResourceDir() {
        return ConfigurationService.getInstance().getSystemConfig().getProperty("test.resource.location");
    }

    public static String getMetaXmlDir() {
        return ConfigurationService.getInstance().getSystemConfig().getProperty("metadata.xmls.location");
    }

    /**
   * Return content location, ending with System.getProperty("file.separator")
   * @return
   */
    public static String getContentContext() {
        return (ConfigurationService.getInstance().getSystemConfig().getProperty("content.context"));
    }

    /**
   * Return the Web root real path, ending with System.getProperty("file.separator")
   * @return
   */
    public static String getWebRootRealpath() {
        String s = ConfigurationService.getInstance().getSystemConfig().getProperty("webroot.realpath");
        if (s != null && !s.endsWith(System.getProperty("file.separator"))) s = s + System.getProperty("file.separator");
        return (s);
    }

    /**
   * Return the file upload location, ending with System.getProperty("file.separator")
   * The file uplaod dir can be configed with key "fileupload.location"; If not configed, the System.getProperty("java.io.tmpdir") will be used
   * to get the default temp dir in the system. 
   * @return
   */
    public static String getFileUploadDir() {
        String s = ConfigurationService.getInstance().getSystemConfig().getProperty("fileupload.location");
        if (s == null) {
            s = System.getProperty("java.io.tmpdir");
        }
        if (s != null && !s.endsWith(System.getProperty("file.separator"))) s = s + System.getProperty("file.separator");
        return (s);
    }

    public static boolean isSCORMdebugEnabled() {
        return ConfigurationService.getInstance().getSystemConfig().getBooleanProperty("scorm.debug");
    }
}
