package net.sf.mgp.unclasses;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Utils {

    private Utils() {
    }

    public static final String FILE_SUFFIX = ".class";

    public static String getClassName(String classFile) {
        String className = classFile.replace(File.separatorChar, '.');
        className = className.substring(0, className.length() - FILE_SUFFIX.length());
        return className;
    }

    private static final Pattern REF_NAME_REGEXP = Pattern.compile("\\[*L(.*);");

    public static String getClassNameFromDescriptor(String descriptor) {
        Matcher m = REF_NAME_REGEXP.matcher(descriptor);
        if (!m.matches()) {
            throw new IllegalArgumentException("Invalid class descriptor " + descriptor);
        }
        return m.group(1).replace('/', '.');
    }

    public static boolean isClassDescriptor(String descriptor) {
        return REF_NAME_REGEXP.matcher(descriptor).matches();
    }
}
