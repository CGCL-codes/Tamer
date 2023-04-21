package com.liferay.portal.kernel.util;

import java.io.InputStream;

/**
 * <a href="DocumentConversionUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Bruno Farache
 *
 */
public class DocumentConversionUtil {

    public static InputStream convert(String id, InputStream is, String sourceExtension, String targetExtension) throws Exception {
        Object returnObj = PortalClassInvoker.invoke(_CLASS, "convert", new Object[] { id, is, sourceExtension, targetExtension }, false);
        if (returnObj != null) {
            return (InputStream) returnObj;
        } else {
            return null;
        }
    }

    public static String[] getConversions(String extension) throws Exception {
        Object returnObj = PortalClassInvoker.invoke(_CLASS, "getConversions", new Object[] { extension }, false);
        if (returnObj != null) {
            return (String[]) returnObj;
        } else {
            return null;
        }
    }

    private static final String _CLASS = "com.liferay.portlet.documentlibrary.util.DocumentConversionUtil";
}
