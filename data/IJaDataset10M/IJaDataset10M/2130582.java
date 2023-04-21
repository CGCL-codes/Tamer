package com.varaneckas.hawkscope.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.program.Program;
import com.varaneckas.hawkscope.Constants;
import com.varaneckas.hawkscope.cfg.Configuration;
import com.varaneckas.hawkscope.cfg.ConfigurationFactory;

/**
 * Path processing utilities
 *
 * @author Tomas Varaneckas
 * @version $Id: PathUtils.java 503 2009-03-29 05:59:04Z tomas.varaneckas $
 */
public abstract class PathUtils {

    /**
     * Property marker regex
     */
    public static final String INTERPRET_REGEX = "\\$\\{([^\\{]+)\\}";

    /**
     * Logger
     */
    private static final Log log = LogFactory.getLog(PathUtils.class);

    /**
     * Converts path separated with delimiter
     * @param path
     * @param delimiter
     * @return
     */
    public static List<File> pathToDirList(final String path, final String delimiter) {
        if (path == null || path.equals("")) {
            return Collections.emptyList();
        }
        String[] locations = interpret(path).split(delimiter);
        final List<File> files = new ArrayList<File>();
        for (final String location : locations) {
            final File f = new File(unsanitizePath(location));
            if (!f.isDirectory()) {
                log.warn(f.getAbsolutePath() + " is not a directory!");
            } else if (!f.canRead()) {
                log.warn(f.getAbsolutePath() + " can not be read!");
            } else {
                files.add(f);
            }
        }
        locations = null;
        return files;
    }

    /**
     * Interprets the location
     * 
     * It can either be a full path, a java property, 
     * like ${user.home} (default) or environmental variable like ${$JAVA_HOME}.
     * 
     * @param location
     * @return
     */
    public static String interpret(final String location) {
        if (!location.matches(".*" + INTERPRET_REGEX + ".*")) {
            return location;
        } else {
            String newLocation = location;
            try {
                final Pattern grep = Pattern.compile(INTERPRET_REGEX);
                final Matcher matcher = grep.matcher(location);
                while (matcher.find()) {
                    log.debug("Parsing: " + matcher.group(1));
                    String replacement;
                    if (matcher.group(1).startsWith("$")) {
                        replacement = System.getenv(matcher.group(1).substring(1));
                    } else {
                        replacement = System.getProperty(matcher.group(1));
                    }
                    newLocation = newLocation.replaceFirst(quote(matcher.group()), replacement.replaceAll(Constants.REGEX_BACKSLASH, Constants.REGEX_SLASH));
                }
            } catch (final Exception e) {
                log.warn("Failed parsing location: " + location, e);
            }
            return newLocation;
        }
    }

    /**
     * Gets friendly file name
     * 
     * @param file input file
     * @return friendly file name
     */
    public static String getFileName(final File file) {
        final Configuration cfg = ConfigurationFactory.getConfigurationFactory().getConfiguration();
        String name = OSUtils.getSystemDisplayName(file);
        if (name == null || name.equals("")) {
            name = file.getName();
        }
        if (name == null || name.equals("")) {
            name = file.getPath();
        }
        if (name == null || name.equals("")) {
            name = "Untitled";
        } else if (OSUtils.isMacApp(file)) {
            name = name.substring(0, name.length() - 4);
        } else if (cfg.isKnownFileExtHidden()) {
            String ext = name.replaceAll(".*\\.", ".");
            final Program p = Program.findProgram(ext);
            if (ext.equalsIgnoreCase(".gz")) {
                if (name.toLowerCase().endsWith(".tar.gz")) {
                    ext = ".tar.gz";
                }
            }
            if (p != null) {
                name = name.substring(0, name.length() - ext.length());
            }
        }
        return name;
    }

    /**
     * Convenience method for finding out whether file is a floppy drive
     * 
     * @param file suspect
     * @return is floppy
     */
    public static boolean isFloppy(final File file) {
        return OSUtils.isFloppyDrive(file);
    }

    /**
     * Removes the sanitizing from path string
     * 
     * @param path
     * @return
     */
    public static String unsanitizePath(final String path) {
        if (OSUtils.CURRENT_OS.equals(OSUtils.OS.WIN)) {
            return path.replaceAll(Constants.REGEX_SLASH, Constants.REGEX_BACKSLASH);
        }
        return path;
    }

    /**
     * Sanitizes the path string (cleanup Windows backslashes)
     * 
     * @param path
     * @return
     */
    public static String sanitizePath(final String path) {
        if (OSUtils.CURRENT_OS.equals(OSUtils.OS.WIN)) {
            return path.replaceAll(Constants.REGEX_BACKSLASH, Constants.REGEX_SLASH);
        }
        return path;
    }

    /**
     * Cloning Java Pattern.quote() for Linux GIJ
     * 
     * Returns a literal pattern <code>String</code> for the specified
     * <code>String</code>.
     *
     * <p>This method produces a <code>String</code> that can be used to
     * create a <code>Pattern</code> that would match the string
     * <code>s</code> as if it were a literal pattern.</p> Metacharacters
     * or escape sequences in the input sequence will be given no special
     * meaning.
     *
     * @param  s The string to be literalized
     * @return  A literal string replacement
     * @since 1.5
     */
    public static String quote(final String s) {
        int slashEIndex = s.indexOf("\\E");
        if (slashEIndex == -1) {
            return "\\Q" + s + "\\E";
        }
        final StringBuilder sb = new StringBuilder(s.length() * 2);
        sb.append("\\Q");
        slashEIndex = 0;
        int current = 0;
        while ((slashEIndex = s.indexOf("\\E", current)) != -1) {
            sb.append(s.substring(current, slashEIndex));
            current = slashEIndex + 2;
            sb.append("\\E\\\\E\\Q");
        }
        sb.append(s.substring(current, s.length()));
        sb.append("\\E");
        return sb.toString();
    }
}
