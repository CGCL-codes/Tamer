package com.google.gwt.dev.resource.impl;

import org.apache.tools.ant.types.ZipScanner;
import java.util.regex.Pattern;

/**
 * A singleton class that provides blazingly fast implementation of the default
 * excludes of Ant's {@link org.apache.tools.ant.DirectoryScanner}, assuming
 * case-sensitiveness.
 * 
 * TODO: this class needs to be revisited, when Gwt's Ant is upgraded.
 * 
 * Currently, we do not go to ant if (a) the filterList is empty, or (b) the
 * filterList has "common" patterns. Exception: When path ends in '/', we defer
 * to ant.
 * 
 * TODO: This code could be made more general and cleaner by removing the
 * dependency on Ant completely. All ant patterns could be compiled into
 * reg-exps. That could also make the code faster. Plus, at several places,
 * Ant's documentation seems to be incomplete. Instead, perhaps, we should
 * specify our own rules for writing patterns.
 */
public class DefaultFilters {

    private static final boolean IS_EXCLUDES = false;

    private static final boolean IS_INCLUDES = true;

    private static final boolean NOT_JAVA = false;

    private static final boolean YES_JAVA = true;

    static ZipScanner getScanner(String[] includeList, String[] excludeList, boolean defaultExcludes, boolean caseSensitive) {
        ZipScanner scanner = new ZipScanner();
        if (includeList.length > 0) {
            scanner.setIncludes(includeList);
        }
        if (excludeList.length > 0) {
            scanner.setExcludes(excludeList);
        }
        if (defaultExcludes) {
            scanner.addDefaultExcludes();
        }
        scanner.setCaseSensitive(caseSensitive);
        scanner.init();
        return scanner;
    }

    final ResourceFilter defaultResourceFilter = new ResourceFilter() {

        public boolean allows(String path) {
            return defaultAntIncludes.allows(path) && !defaultExcludesPattern.matcher(path).matches();
        }
    };

    final ResourceFilter defaultJavaFilter = new ResourceFilter() {

        public boolean allows(String path) {
            return justJavaFilter.allows(path) && !defaultJavaExcludesPattern.matcher(path).matches();
        }
    };

    final ResourceFilter justResourceFilter = new ResourceFilter() {

        public boolean allows(String path) {
            return defaultAntIncludes.allows(path);
        }
    };

    final ResourceFilter justJavaFilter = new ResourceFilter() {

        public boolean allows(String path) {
            return defaultAntIncludes.allows(path) && isJavaFile(path);
        }
    };

    private final Pattern defaultExcludesPattern;

    private final Pattern defaultJavaExcludesPattern;

    private final Pattern antPattern = Pattern.compile("^[\\w\\.\\$/\\-\\*~#%]*$");

    private final ResourceFilter defaultAntIncludes = new ResourceFilter() {

        public boolean allows(String path) {
            return path.charAt(0) != '/';
        }
    };

    private final ResourceFilter rejectAll = new ResourceFilter() {

        public boolean allows(String path) {
            return false;
        }
    };

    public DefaultFilters() {
        String defaultExcludes[] = new String[] { "**/*~", "**/#*#", "**/.#*", "**/%*%", "**/._*", "**/CVS", "**/CVS/**", "**/.cvsignore", "**/SCCS", "**/SCCS/**", "**/vssver.scc", "**/.svn", "**/.svn/**", "**/.DS_Store" };
        defaultExcludesPattern = getPatternFromAntStrings(defaultExcludes);
        String defaultExcludesJava[] = new String[] { "**/.#*", "**/._*", "**/CVS/**", "**/SCCS/**", "**/.svn/**" };
        defaultJavaExcludesPattern = getPatternFromAntStrings(defaultExcludesJava);
    }

    public ResourceFilter customJavaFilter(String includeList[], String excludeList[], boolean defaultExcludes, boolean caseSensitive) {
        return getCustomFilter(includeList, excludeList, defaultExcludes, caseSensitive, YES_JAVA);
    }

    public ResourceFilter customResourceFilter(String includeList[], String excludeList[], boolean defaultExcludes, boolean caseSensitive) {
        return getCustomFilter(includeList, excludeList, defaultExcludes, caseSensitive, NOT_JAVA);
    }

    /**
   * return a customResourceFiter that handles all the argument. If unable to
   * create a customResourceFilter that handles the arguments, catchAll is used
   * as the final ResourceFilter.
   */
    ResourceFilter customFilterWithCatchAll(final String includeList[], final String excludeList[], final boolean defaultExcludes, final ResourceFilter catchAll, final boolean isJava) {
        assert includeList.length > 0 || excludeList.length > 0;
        final ResourceFilter includeFilter = getFilterPart(includeList, IS_INCLUDES);
        final ResourceFilter excludeFilter = getFilterPart(excludeList, IS_EXCLUDES);
        if (includeFilter == null || excludeFilter == null) {
            return catchAll;
        }
        ResourceFilter filter = new ResourceFilter() {

            public boolean allows(String path) {
                if (path.endsWith("/")) {
                    return catchAll.allows(path);
                }
                return isPathAllowedByDefaults(path, defaultExcludes, isJava) && includeFilter.allows(path) && !excludeFilter.allows(path);
            }

            private boolean isPathAllowedByDefaults(String path, boolean defaultExcludes, boolean isJava) {
                if (defaultExcludes) {
                    return isJava ? !defaultJavaExcludesPattern.matcher(path).matches() && isJavaFile(path) : !defaultExcludesPattern.matcher(path).matches();
                }
                return isJava ? isJavaFile(path) : true;
            }
        };
        return filter;
    }

    ResourceFilter getCustomFilter(final String includeList[], final String excludeList[], final boolean defaultExcludes, final boolean caseSensitive, final boolean isJava) {
        if (includeList.length == 0 && excludeList.length == 0 && caseSensitive) {
            return getMatchingDefaultFilter(defaultExcludes, isJava);
        }
        ResourceFilter catchAll = new ResourceFilter() {

            ZipScanner scanner = getScanner(includeList, excludeList, defaultExcludes, caseSensitive);

            public boolean allows(String path) {
                if (isJava) {
                    return isJavaFile(path) && scanner.match(path);
                }
                return scanner.match(path);
            }
        };
        if (!caseSensitive) {
            return catchAll;
        }
        return customFilterWithCatchAll(includeList, excludeList, defaultExcludes, catchAll, isJava);
    }

    ResourceFilter getFilterPart(final String list[], final boolean defaultValue) {
        if (list.length == 0) {
            return defaultValue ? defaultAntIncludes : rejectAll;
        }
        String patternStrings[] = new String[list.length];
        int count = 0;
        for (String antPatternString : list) {
            String patternString = getPatternFromAntPattern(antPatternString);
            if (patternString == null) {
                return null;
            }
            patternStrings[count++] = patternString;
        }
        final Pattern pattern = getPatternFromStrings(patternStrings);
        return new ResourceFilter() {

            public boolean allows(String path) {
                return pattern.matcher(path).matches();
            }
        };
    }

    /**
   * Returns a pattern string that can be passed in Java Pattern.compile(..).
   * For spec, see <a href="http://www.jajakarta.org/ant/ant-1.6.1/docs/ja/manual/api/org/apache/tools/ant/DirectoryScanner.html"
   * >DirectoryScanner</a> From the spec: There is a special case regarding the
   * use of File.separators at the beginning of the pattern and the string to
   * match: When a pattern starts with a File.separator, the string to match
   * must also start with a File.separator. When a pattern does not start with a
   * File.separator, the string to match may not start with a File.separator.
   * 
   * </p>
   * 
   * TODO: This method could accept all ant patterns, but then all characters
   * that have a special meaning in Java's regular expression would need to be
   * escaped.
   * 
   * @param antPatternString the ant pattern String.
   * @return a pattern string that can be passed in Java's Pattern.compile(..),
   *         null if cannot process the pattern.
   */
    String getPatternFromAntPattern(String antPatternString) {
        if (!antPattern.matcher(antPatternString).matches()) {
            return null;
        }
        if (antPatternString.indexOf("***") != -1) {
            return null;
        }
        if (antPatternString.endsWith("/")) {
            antPatternString = antPatternString + "**";
        }
        StringBuffer sb = new StringBuffer();
        int length = antPatternString.length();
        for (int i = 0; i < length; i++) {
            char c = antPatternString.charAt(i);
            switch(c) {
                case '.':
                    sb.append("\\.");
                    break;
                case '$':
                    sb.append("\\$");
                    break;
                case '/':
                    if (i != 0 && i + 2 < length && antPatternString.charAt(i + 1) == '*' && antPatternString.charAt(i + 2) == '*') {
                        sb.append("(/[^/]*)*");
                        i += 2;
                    } else {
                        sb.append(c);
                    }
                    break;
                case '*':
                    if (i + 1 < length && antPatternString.charAt(i + 1) == '*') {
                        if (i + 2 < length && antPatternString.charAt(i + 2) == '/') {
                            if (i == 0) {
                                sb.append("([^/]+/)*");
                            } else {
                                sb.append("([^/]*/)*");
                            }
                            i += 2;
                        } else {
                            if (i == 0) {
                                sb.append("([^/].*)*");
                            } else {
                                sb.append(".*");
                            }
                            i++;
                        }
                    } else {
                        sb.append("[^/]*");
                    }
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /**
   * Obtain the appropriate resourceFilter based on defaultExcludes and isJava
   * values. Assumptions: caseSensitive = true,and the includesList and
   * excludesList are empty
   */
    private ResourceFilter getMatchingDefaultFilter(boolean defaultExcludes, boolean isJava) {
        if (defaultExcludes) {
            return isJava ? defaultJavaFilter : defaultResourceFilter;
        }
        return isJava ? justJavaFilter : justResourceFilter;
    }

    private Pattern getPatternFromAntStrings(String... antPatterns) {
        String patternStrings[] = new String[antPatterns.length];
        int count = 0;
        for (String antPatternString : antPatterns) {
            String patternString = getPatternFromAntPattern(antPatternString);
            if (patternString == null) {
                throw new RuntimeException("Unable to convert " + antPatternString + " to java code");
            }
            patternStrings[count++] = patternString;
        }
        return getPatternFromStrings(patternStrings);
    }

    private Pattern getPatternFromStrings(String... patterns) {
        StringBuffer entirePattern = new StringBuffer("^");
        int length = patterns.length;
        int count = 0;
        for (String pattern : patterns) {
            entirePattern.append("(" + pattern + ")");
            if (count < length - 1) {
                entirePattern.append("|");
            }
            count++;
        }
        entirePattern.append("$");
        return Pattern.compile(entirePattern.toString());
    }

    private boolean isJavaFile(String path) {
        return path.endsWith(".java");
    }
}
