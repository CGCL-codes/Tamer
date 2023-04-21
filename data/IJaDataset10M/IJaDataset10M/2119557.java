package rat.document.impl.zip;

import java.util.zip.ZipEntry;

final class ZipUtils {

    public static String getStem(final ZipEntry entry) {
        final String name = entry.getName();
        final int lastIndexOfForwardSlash = name.lastIndexOf('/');
        final int lastIndexOfBackSlash = name.lastIndexOf('\\');
        final int index = Math.max(lastIndexOfBackSlash, lastIndexOfForwardSlash);
        String result = "";
        if (index >= 0) {
            result = name.substring(0, index);
        }
        return result;
    }

    public static String getName(final ZipEntry entry) {
        String name = entry.getName();
        if (name.endsWith("/") || name.endsWith("\\")) {
            name = name.substring(0, name.length() - 1);
        }
        final int lastIndexOfForwardSlash = name.lastIndexOf('/');
        final int lastIndexOfBackSlash = name.lastIndexOf('\\');
        final int index = Math.max(lastIndexOfBackSlash, lastIndexOfForwardSlash);
        String result = name;
        if (index >= 0) {
            final int length = name.length();
            result = name.substring(index + 1, length);
        }
        return result;
    }

    public static String getUrl(final ZipEntry entry) {
        return "zip:" + entry.getName();
    }

    public static boolean isTopLevel(final ZipEntry entry) {
        final String name = entry.getName();
        final int lastPosition = name.length() - 1;
        final int indexOfForwardSlash = name.indexOf('/');
        final int indexOfBackSlash = name.indexOf('\\');
        final boolean result = (indexOfForwardSlash < 0 || indexOfForwardSlash == lastPosition) && (indexOfBackSlash < 0 || indexOfBackSlash == lastPosition);
        return result;
    }
}
