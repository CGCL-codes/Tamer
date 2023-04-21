package org.jscripter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DirectoryLister {

    private static final String fileSeparator = "/";

    private static final String wc = "*";

    public static Collection<File> listFiles(String path, boolean includeHidden) throws IOException {
        FilenameWildcardFilter fnFilter = null;
        DirnameWildcardFilter dnFilter = null;
        ArrayList<File> files = new ArrayList<File>();
        PathExpression pathExp = new PathExpression(path);
        if (JScripter.debug) {
            System.out.println("listing expression: " + pathExp);
        }
        File filePath = new File(pathExp.getBasePath());
        if (!filePath.exists()) throw new IOException("Path doesn't exists: " + pathExp.getBasePath());
        if (pathExp.hasFileNameWildcards()) fnFilter = new FilenameWildcardFilter(pathExp.getFileNameWildcard(), includeHidden);
        if (pathExp.hasSubdirnameWildcard()) dnFilter = new DirnameWildcardFilter(pathExp.getSubdirnameWildcard(), includeHidden);
        if (filePath.isDirectory() && !pathExp.hasFilenameWildcard) {
            files.add(filePath);
        }
        files.addAll(listFiles(filePath, dnFilter, fnFilter));
        return files;
    }

    public static Collection<File> listFiles(File path, FileFilter dirFilter, FileFilter fileFilter) {
        ArrayList<File> files = new ArrayList<File>();
        if (fileFilter != null) for (File file : path.listFiles(fileFilter)) files.add(file);
        if (dirFilter != null) for (File dir : path.listFiles(dirFilter)) {
            if (fileFilter == null) files.add(dir);
            files.addAll(listFiles(dir, dirFilter, fileFilter));
        }
        return files;
    }

    public static List<Match> extractAll(String str, String regex, char escape) {
        Matcher matcher = Pattern.compile(regex).matcher(str);
        ArrayList<Match> list = new ArrayList<Match>();
        boolean found;
        int l = 0;
        int f = 0;
        do {
            found = matcher.find(l);
            if (found) {
                f = matcher.start();
                l = matcher.end();
                if (f > 0) {
                    if (str.charAt(f - 1) != escape) {
                        list.add(new Match(matcher.group(), f, l));
                    }
                } else {
                    list.add(new Match(matcher.group(), f, l));
                }
            }
        } while (found);
        return list;
    }

    private static class PathExpression {

        private static String wilcardRegexp = "" + fileSeparator + "?[^/]*\\*+\\p{Print}*";

        private boolean hasSubdirWildcard = true;

        private boolean hasFilenameWildcard = true;

        private String subdirWildCard = "";

        private String fileNameWildCard = "";

        String basePath;

        public PathExpression(String pathExpression) {
            List<Match> matches = extractAll(pathExpression, wilcardRegexp, '\b');
            if (matches.size() == 0) {
                setNormalPath(pathExpression);
                return;
            }
            Match match = matches.get(0);
            String wcExp = match.getText();
            if (wcExp.startsWith(fileSeparator)) wcExp = wcExp.substring(1);
            setWildcards(wcExp);
            if (match.getStart() == 0) {
                setBasePath(System.getProperty("user.dir"));
            } else {
                setBasePath(pathExpression.substring(0, match.getStart()));
            }
        }

        public String getBasePath() {
            return basePath;
        }

        public String getSubdirnameWildcard() {
            return subdirWildCard;
        }

        public boolean hasSubdirnameWildcard() {
            return hasSubdirWildcard;
        }

        public String getFileNameWildcard() {
            return fileNameWildCard;
        }

        public boolean hasFileNameWildcards() {
            return hasFilenameWildcard;
        }

        private void setWildcards(String wcExp) {
            String exp = new String(wcExp);
            exp = exp.replaceAll(fileSeparator + "+", fileSeparator);
            int li = exp.lastIndexOf(fileSeparator);
            if (li == (exp.length() - 1)) {
                exp = exp.substring(0, li);
            }
            String[] ends = exp.split(fileSeparator);
            if (ends.length < 2) {
                if (ends[0].contains(".")) {
                    hasSubdirWildcard = false;
                    fileNameWildCard = ends[0];
                } else {
                    hasFilenameWildcard = false;
                    subdirWildCard = ends[0];
                }
            } else {
                subdirWildCard = ends[0];
                fileNameWildCard = ends[1];
            }
        }

        private void setNormalPath(String pathname) {
            hasSubdirWildcard = false;
            hasFilenameWildcard = false;
            subdirWildCard = "";
            fileNameWildCard = "";
            setBasePath(pathname);
        }

        private void setBasePath(String pathname) {
            basePath = pathname;
        }

        public String toString() {
            return "[" + basePath + "];[" + subdirWildCard + "];[" + fileNameWildCard + "]";
        }
    }

    private static class FilenameWildcardFilter extends HandleHiddenFilter implements FileFilter {

        private FlatnameWildcardFilter nameFilter;

        private FlatnameWildcardFilter extFilter;

        public FilenameWildcardFilter(String wildcardExpression, boolean includeHidden) {
            super(includeHidden);
            String[] nameEnds = wildcardExpression.split("\\.");
            String fileWc;
            if (nameEnds[0].equals(wc)) fileWc = wc + wc; else fileWc = nameEnds[0];
            nameFilter = new FlatnameWildcardFilter(fileWc);
            if (nameEnds[1].equals(wc)) fileWc = wc + wc; else fileWc = nameEnds[1];
            extFilter = new FlatnameWildcardFilter(fileWc);
        }

        public boolean accept(File pathname) {
            if (!pathname.isFile()) return false;
            String[] nameEnds;
            String path = pathname.getName();
            int dotIndex = path.lastIndexOf(".");
            if (dotIndex < 0) {
                nameEnds = new String[] { path };
            } else if (dotIndex == 0) {
                nameEnds = new String[] { "", path.substring(1) };
            } else {
                nameEnds = new String[] { path.substring(0, dotIndex), path.substring(dotIndex + 1) };
            }
            if (nameEnds.length > 1) return super.accept(pathname) && nameFilter.accept(nameEnds[0]) && extFilter.accept(nameEnds[1]); else return super.accept(pathname) && nameFilter.accept(nameEnds[0]) && extFilter.accept("~no~extension~");
        }
    }

    private static class DirnameWildcardFilter extends HandleHiddenFilter implements FileFilter {

        FlatnameWildcardFilter filter;

        public DirnameWildcardFilter(String wildcardExpression, boolean includeHidden) {
            super(includeHidden);
            filter = new FlatnameWildcardFilter(wildcardExpression);
        }

        public boolean accept(File pathname) {
            if (!pathname.isDirectory()) return false;
            return super.accept(pathname) && filter.accept(pathname.getName());
        }
    }

    private static class HandleHiddenFilter implements FileFilter {

        private boolean acceptsHidden;

        public HandleHiddenFilter(boolean acceptsHidden) {
            this.acceptsHidden = acceptsHidden;
        }

        public boolean accept(File pathname) {
            return !pathname.isHidden() || acceptsHidden;
        }
    }

    private static class FlatnameWildcardFilter {

        private WildcardFilter filter;

        public FlatnameWildcardFilter(String wildcardExpression) {
            if (!wildcardExpression.contains(wc)) {
                filter = getEqualsFilter(wildcardExpression);
                return;
            }
            if ((wc + wc).equals(wildcardExpression)) {
                filter = getAcceptAllFilter();
                return;
            }
            int f = wildcardExpression.indexOf(wc);
            int l = wildcardExpression.lastIndexOf(wc);
            if (f == l) {
                if (f == 0) {
                    filter = getEndsWithFilter(wildcardExpression.substring(f + 1));
                } else {
                    filter = getStartsWithFilter(wildcardExpression.substring(0, l));
                }
            } else {
                filter = getContainsFilter(wildcardExpression.substring(f + 1, l));
            }
        }

        public boolean accept(String name) {
            return filter.accept(name);
        }
    }

    private static WildcardFilter getEndsWithFilter(final String text) {
        return new WildcardFilter() {

            public boolean accept(String name) {
                return name.endsWith(text);
            }
        };
    }

    public static WildcardFilter getEqualsFilter(final String text) {
        return new WildcardFilter() {

            public boolean accept(String name) {
                return name.equals(text);
            }
        };
    }

    private static WildcardFilter getStartsWithFilter(final String text) {
        return new WildcardFilter() {

            public boolean accept(String name) {
                return name.startsWith(text);
            }
        };
    }

    private static WildcardFilter getContainsFilter(final String text) {
        return new WildcardFilter() {

            public boolean accept(String name) {
                return name.contains(text);
            }
        };
    }

    private static WildcardFilter getAcceptAllFilter() {
        return new WildcardFilter() {

            public boolean accept(String name) {
                return true;
            }
        };
    }

    private static interface WildcardFilter {

        public boolean accept(String name);
    }

    public static class Match {

        private String text;

        private int start;

        private int end;

        public Match(String text, int start, int end) {
            this.end = end;
            this.start = start;
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        @Override
        public String toString() {
            return text + "[" + start + "," + end + "]";
        }
    }
}
