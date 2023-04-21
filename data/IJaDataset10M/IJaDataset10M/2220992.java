package com.aaspring.util.file;

import java.util.Locale;

/**
 * @author Balazs
 * 
 */
public class FileAccessorUtil {

    public static String getFilePath(final String path, final String extension, final Locale locale) {
        int deepness = 0;
        if (!"".equals(locale.getLanguage())) deepness = 1;
        if (!"".equals(locale.getCountry())) deepness = 2;
        if (!"".equals(locale.getVariant())) deepness = 3;
        StringBuilder sb = new StringBuilder(path);
        if (deepness > 0) {
            sb.append("_").append(locale.getLanguage());
            if (deepness > 1) {
                sb.append("_").append(locale.getCountry());
                if (deepness > 2) sb.append("_").append(locale.getVariant());
            }
        }
        if (!extension.equals("")) sb.append(".").append(extension);
        return sb.toString();
    }
}
