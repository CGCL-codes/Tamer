package org.t2framework.commons.util;

import java.io.File;
import java.net.URL;
import java.util.zip.ZipFile;

/**
 * <#if locale="en">
 * <p>
 * {@link ZipFileUtil} is an utility class for using {@link ZipFile}.
 * 
 * </p>
 * <#else>
 * <p>
 * 
 * </p>
 * </#if>
 * 
 * @author shot
 */
public class ZipFileUtil {

    /**
	 * <#if locale="en">
	 * <p>
	 * no instantiation.
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 */
    private ZipFileUtil() {
    }

    /**
	 * <#if locale="en">
	 * <p>
	 * Convert to {@link URL} to zip file path.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param zipUrl
	 * @return
	 */
    public static String toZipFilePath(final URL zipUrl) {
        Assertion.notNull(zipUrl, "zipUrl");
        final String urlString = zipUrl.getPath();
        final int pos = urlString.lastIndexOf('!');
        final String zipFilePath = urlString.substring(0, pos);
        final File zipFile = new File(URLUtil.decode(zipFilePath, "UTF8"));
        return FileUtil.getCanonicalPath(zipFile);
    }
}
