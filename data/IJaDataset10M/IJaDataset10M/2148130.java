package org.eclipse.osgi.framework.adaptor;

import java.io.File;

/** 
 * A utility class for manipulating file system paths.
 * <p>
 * This class is not intended to be subclassed by clients but
 * may be instantiated.
 * </p>
 * 
 * @since 3.1
 */
public class FilePath {

    private static final boolean WINDOWS = java.io.File.separatorChar == '\\';

    private static final String CURRENT_DIR = ".";

    private static final char DEVICE_SEPARATOR = ':';

    private static final byte HAS_LEADING = 1;

    private static final byte HAS_TRAILING = 4;

    private static final String[] NO_SEGMENTS = new String[0];

    private static final String PARENT_DIR = "..";

    private static final char SEPARATOR = '/';

    private static final String UNC_SLASHES = "//";

    private String device;

    private byte flags;

    private String[] segments;

    /**
	 * Constructs a new file path from the given File object.
	 * 
	 * @param location
	 */
    public FilePath(File location) {
        initialize(location.getPath());
        if (location.isDirectory()) flags |= HAS_TRAILING; else flags &= ~HAS_TRAILING;
    }

    /**
	 * Constructs a new file path from the given string path.
	 * 
	 * @param original
	 */
    public FilePath(String original) {
        initialize(original);
    }

    private int computeSegmentCount(String path) {
        int len = path.length();
        if (len == 0 || (len == 1 && path.charAt(0) == SEPARATOR)) return 0;
        int count = 1;
        int prev = -1;
        int i;
        while ((i = path.indexOf(SEPARATOR, prev + 1)) != -1) {
            if (i != prev + 1 && i != len) ++count;
            prev = i;
        }
        if (path.charAt(len - 1) == SEPARATOR) --count;
        return count;
    }

    private String[] computeSegments(String path) {
        int maxSegmentCount = computeSegmentCount(path);
        if (maxSegmentCount == 0) return NO_SEGMENTS;
        String[] newSegments = new String[maxSegmentCount];
        int len = path.length();
        int firstPosition = isAbsolute() ? 1 : 0;
        int lastPosition = hasTrailingSlash() ? len - 2 : len - 1;
        int next = firstPosition;
        int actualSegmentCount = 0;
        for (int i = 0; i < maxSegmentCount; i++) {
            int start = next;
            int end = path.indexOf(SEPARATOR, next);
            next = end + 1;
            String segment = path.substring(start, end == -1 ? lastPosition + 1 : end);
            if (CURRENT_DIR.equals(segment)) continue;
            if (PARENT_DIR.equals(segment)) {
                if (actualSegmentCount > 0) actualSegmentCount--;
                continue;
            }
            newSegments[actualSegmentCount++] = segment;
        }
        if (actualSegmentCount == newSegments.length) return newSegments;
        if (actualSegmentCount == 0) return NO_SEGMENTS;
        String[] actualSegments = new String[actualSegmentCount];
        System.arraycopy(newSegments, 0, actualSegments, 0, actualSegments.length);
        return actualSegments;
    }

    /**
	 * Returns the device for this file system path, or <code>null</code> if 
	 * none exists. The device string ends with a colon.
	 * 
	 * @return the device string or null 
	 */
    public String getDevice() {
        return device;
    }

    /**
	 * Returns the segments in this path. If this path has no segments, returns an empty array.
	 * 
	 * @return an array containing all segments for this path 
	 */
    public String[] getSegments() {
        return (String[]) segments.clone();
    }

    /**
	 * Returns whether this path ends with a slash.
	 * 
	 * @return <code>true</code> if the path ends with a slash, false otherwise
	 */
    public boolean hasTrailingSlash() {
        return (flags & HAS_TRAILING) != 0;
    }

    private void initialize(String original) {
        original = original.indexOf('\\') == -1 ? original : original.replace('\\', SEPARATOR);
        if (WINDOWS) {
            int deviceSeparatorPos = original.indexOf(DEVICE_SEPARATOR);
            if (deviceSeparatorPos >= 0) {
                int start = original.charAt(0) == SEPARATOR ? 1 : 0;
                device = original.substring(start, deviceSeparatorPos + 1);
                original = original.substring(deviceSeparatorPos + 1, original.length());
            } else if (original.startsWith(UNC_SLASHES)) {
                int uncPrefixEnd = original.indexOf(SEPARATOR, 2);
                if (uncPrefixEnd >= 0) uncPrefixEnd = original.indexOf(SEPARATOR, uncPrefixEnd + 1);
                if (uncPrefixEnd >= 0) {
                    device = original.substring(0, uncPrefixEnd);
                    original = original.substring(uncPrefixEnd, original.length());
                } else throw new IllegalArgumentException("Not a valid UNC: " + original);
            }
        }
        if (original.charAt(0) == SEPARATOR) flags |= HAS_LEADING;
        if (original.charAt(original.length() - 1) == SEPARATOR) flags |= HAS_TRAILING;
        segments = computeSegments(original);
    }

    /**
	 * Returns whether this path is absolute (begins with a slash).
	 * 
	 * @return <code>true</code> if this path is absolute, <code>false</code> otherwise
	 */
    public boolean isAbsolute() {
        return (flags & HAS_LEADING) != 0;
    }

    /**
	 * Returns a string representing this path as a relative to the given base path.
	 * <p>
	 * If this path and the given path do not use the same device letter, this path's
	 * string representation is returned as is. 
	 * </p>
	 * 
	 * @param base the path this path should be made relative to
	 * @return a string representation for this path as relative to the given base path 
	 */
    public String makeRelative(FilePath base) {
        if (base.device != null && !base.device.equalsIgnoreCase(this.device)) return base.toString();
        int baseCount = this.segments.length;
        int count = this.matchingFirstSegments(base);
        if (baseCount == count && count == base.segments.length) return base.hasTrailingSlash() ? ("." + SEPARATOR) : ".";
        StringBuffer relative = new StringBuffer();
        for (int j = 0; j < baseCount - count; j++) relative.append(PARENT_DIR + SEPARATOR);
        for (int i = 0; i < base.segments.length - count; i++) {
            relative.append(base.segments[count + i]);
            relative.append(SEPARATOR);
        }
        if (!base.hasTrailingSlash()) relative.deleteCharAt(relative.length() - 1);
        return relative.toString();
    }

    private int matchingFirstSegments(FilePath anotherPath) {
        int anotherPathLen = anotherPath.segments.length;
        int max = Math.min(segments.length, anotherPathLen);
        int count = 0;
        for (int i = 0; i < max; i++) {
            if (!segments[i].equals(anotherPath.segments[i])) return count;
            count++;
        }
        return count;
    }

    /**
	 * Returns a string representation of this path.
	 * 
	 * @return  a string representation of this path
	 */
    public String toString() {
        StringBuffer result = new StringBuffer();
        if (device != null) result.append(device);
        if (isAbsolute()) result.append(SEPARATOR);
        for (int i = 0; i < segments.length; i++) {
            result.append(segments[i]);
            result.append(SEPARATOR);
        }
        if (segments.length > 0 && !hasTrailingSlash()) result.deleteCharAt(result.length() - 1);
        return result.toString();
    }
}
