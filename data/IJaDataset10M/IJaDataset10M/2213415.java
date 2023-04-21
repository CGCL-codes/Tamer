package gov.lanl.arc.dkImpl;

import java.io.*;
import java.util.*;

/**
 * Performs a binary search through .cdx files for a given prefix string.
 * Currently only handles a single .cdx file, though most of the infrastructure
 * for multiple files is there. findMatchingLines needs fixing.
 */
public class BinSearch {

    /** doSearch method api method to call from registry program */
    public String[] doSearch(String find, String cdxfile) {
        String[] lines;
        try {
            RandomAccessFile in = new RandomAccessFile(new File(cdxfile), "r");
            try {
                long matchingline = binSearch(in, find);
                if (matchingline == -1) {
                    System.err.println("Failed to find " + find + " in " + cdxfile);
                    return null;
                } else {
                    long firstmatching = findFirstLine(in, find, matchingline);
                    in.seek(firstmatching);
                }
                lines = new String[] { in.readLine() };
            } finally {
                in.close();
            }
        } catch (IOException e) {
            System.err.println("Error while finding lines");
            e.printStackTrace();
            return null;
        }
        return lines;
    }

    public static void main(String[] argv) {
        System.err.println("BinSearch build $Date: 2005-05-25 16:11:44 -0600 (Wed, 25 May 2005) $");
        boolean only_one = false;
        if (argv[0].equals("-s")) {
            only_one = true;
            String[] newargv = new String[argv.length - 1];
            System.arraycopy(argv, 1, newargv, 0, argv.length - 1);
            argv = newargv;
        }
        if (argv.length < 2) {
            System.err.println("Usage: java BinSearch [-s] <string> <cdxfiles...>");
            System.exit(1);
        }
        if (argv.length > 2) {
            System.err.println("Cannot handle multiple CDX files yet.");
            System.exit(1);
        }
        String find = argv[0];
        String[] files = new String[argv.length - 1];
        System.arraycopy(argv, 1, files, 0, files.length);
        String[] lines;
        try {
            if (only_one) {
                RandomAccessFile in = new RandomAccessFile(new File(files[0]), "r");
                try {
                    long matchingline = binSearch(in, find);
                    if (matchingline == -1) {
                        System.err.println("Failed to find " + find + " in " + files[0]);
                        System.exit(1);
                        return;
                    } else {
                        long firstmatching = findFirstLine(in, find, matchingline);
                        in.seek(firstmatching);
                    }
                    lines = new String[] { in.readLine() };
                } finally {
                    in.close();
                }
            } else {
                lines = findLinesInFile(files[0], find);
            }
        } catch (IOException e) {
            System.err.println("Error while finding lines");
            e.printStackTrace();
            System.exit(1);
            return;
        }
        printResult(lines);
        System.exit(lines.length > 0 ? 0 : 1);
    }

    /**
     * Print an array of String, or an error message if it is empty.
     * 
     * @param lines
     *            Lines to print.
     */
    private static void printResult(String[] lines) {
        if (lines != null) {
            for (int i = 0; i < lines.length; i++) System.out.println(lines[i]);
        } else {
            System.err.println("No matching lines found.");
        }
    }

    /**
     * Our own comparision function. Right now just does prefix match.
     * 
     * @param line
     *            A line to find the prefix of
     * @param pattern
     *            The prefix to find.
     * @return A result equivalent to String.compareTo, but only for a prefix.
     */
    protected static int compare(String line, String pattern) {
        String start = line.substring(0, Math.min(pattern.length(), line.length()));
        int cmp = start.compareTo(pattern);
        return cmp;
    }

    /**
     * Find in a list of sorted files the first one that may contain a line
     * beginning with a certain text.
     * 
     * @param find
     *            The prefix searched for.
     * @param files
     *            The files that if concatenated together will make up a sorted
     *            list of lines through which we search.
     * @return The index of the first file that may contain the line. To be
     *         exact, the index of file before the first one whose first line
     *         doesn't compare 'before' the string searched for.
     */
    public static int findFirstFile(String find, String[] files) throws IOException {
        int oldfile = 0;
        for (int i = 1; i < files.length; i++) {
            RandomAccessFile in = new RandomAccessFile(new File(files[i]), "r");
            String l = in.readLine();
            in.close();
            if (l == null) {
                continue;
            }
            if (compare(l, find) >= 0) {
                break;
            }
            oldfile = i;
        }
        return oldfile;
    }

    public static String findFirstMatchingLine(String find, String[] files) {
        RandomAccessFile in = null;
        try {
            int first = findFirstFile(find, files);
            in = new RandomAccessFile(new File(files[first]), "r");
            try {
                long matchingline = binSearch(in, find);
                if (matchingline == -1) {
                    if (first != files.length - 1) {
                        in = new RandomAccessFile(new File(files[first + 1]), "r");
                    } else return null;
                } else {
                    long firstmatching = findFirstLine(in, find, matchingline);
                    in.seek(firstmatching);
                }
                return in.readLine();
            } finally {
                in.close();
            }
        } catch (IOException e) {
            System.err.println("Error finding first line");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find lines that begin with a certain text, across several sorted files.
     * 
     * @param find
     *            The prefix searched for.
     * @param files
     *            The files that make up a sorted list of lines through which we
     *            search.
     * @return The lines that begin with the prefix. If none were found, the
     *         array is empty.
     */
    public static String[] findMatchingLines(String find, String[] files) {
        int i = 0;
        try {
            i = findFirstFile(find, files);
            String[] lines = findLinesInFile(files[i], find);
            System.out.println("Found " + lines.length + " first " + lines[0] + " in " + files[i]);
            String l = null;
            while (compare(l, find) == 0) {
                String[] morelines = getFollowingLines(files[i], find, 0);
                if (morelines.length == 0) break;
                l = morelines[morelines.length - 1];
                if (lines.length > 0) {
                    String[] alllines = new String[lines.length + morelines.length];
                    System.arraycopy(lines, 0, alllines, 0, lines.length);
                    System.arraycopy(morelines, 0, alllines, lines.length, morelines.length);
                    lines = morelines;
                } else lines = morelines;
                i++;
            }
            return lines;
        } catch (IOException e) {
            System.err.println("Error reading " + files[i]);
            e.printStackTrace();
        }
        return new String[0];
    }

    /**
     * Given the first file that contains lines with the prefix 'find', return
     * all the matching lines in the file. If the file does not contain any
     * matching lines, an empty array is returned.
     * 
     * @param file
     *            The name of a .cdx file, i.e., a file of lexicographically
     *            sorted lines.
     * @param find
     *            A prefix to search for, e.g. http://www.foo.com/bar/
     * @return The lines in the file beginning with the prefix.
     * @throws IOException
     *             If an underlying IO error occurs.
     */
    private static String[] findLinesInFile(String file, String find) throws IOException {
        RandomAccessFile in = new RandomAccessFile(new File(file), "r");
        try {
            long matchingline = binSearch(in, find);
            if (matchingline == -1) {
                return new String[0];
            }
            long firstmatching = findFirstLine(in, find, matchingline);
            return getFollowingLines(in, find, firstmatching);
        } finally {
            in.close();
        }
    }

    /**
     * Return the index of the first line in the file to match 'find'. Reads
     * O(sqrt(n)) lines, where n is the distance from matchingline to the first
     * line. Works best on files of approximately equal length lines.
     * 
     * @param in
     *            The file to search in
     * @param find
     *            The string to match against the first line
     * @param matchingline
     *            The index to start searching from. This line should match
     *            'find'
     * @return The offset into the file of the first line matching 'find'.
     *         Guaranteed to be <= matchingline.
     */
    private static long findFirstLine(RandomAccessFile in, String find, long matchingline) throws IOException {
        in.seek(matchingline);
        String line = in.readLine();
        if (line == null || compare(line, find) != 0) {
            System.err.println("Internal: Called findFirstLine without " + "a matching line: " + in + " byte " + matchingline);
        }
        int linelength = line.length();
        long offset = linelength;
        for (int i = 1; matchingline - offset > 0; i++, offset = i * i * linelength) {
            skipToLine(in, matchingline - offset);
            line = in.readLine();
            if (line == null || compare(line, find) != 0) break;
        }
        long pos;
        if (matchingline - offset <= 0) {
            pos = 0;
            in.seek(0);
        } else pos = in.getFilePointer();
        while ((line = in.readLine()) != null) {
            if (compare(line, find) == 0) return pos;
            pos = in.getFilePointer();
        }
        return -1;
    }

    /**
     * Skip to the next line after the given position by reading a line. Note
     * that if the position is at the start of a line, it will go to the next
     * line.
     * 
     * @param in
     *            A file to read from
     * @param pos
     *            The position to start at.
     * @return A new position in the file. The file's pointer (as given by
     *         getFilePointer()) is updated to match.
     * @throws IOException
     */
    private static long skipToLine(RandomAccessFile in, long pos) throws IOException {
        in.seek(pos);
        in.readLine();
        return in.getFilePointer();
    }

    /**
     * Return all lines that match 'find', starting at 'pos'
     * 
     * @param in
     *            A file of sorted lines.
     * @param find
     *            A prefix.
     * @param pos
     *            A position in the file.
     * @return The lines found following pos that begins with find.
     * @throws IOException
     *             If an IO error happens.
     */
    private static String[] getFollowingLines(RandomAccessFile in, String find, long pos) throws IOException {
        in.seek(pos);
        List l = new ArrayList();
        do {
            String line = in.readLine();
            if (line == null) break;
            if (compare(line, find) != 0) break;
            l.add(line);
        } while (true);
        return (String[]) l.toArray(new String[0]);
    }

    /**
     * Return all lines from pos onwards that begin with 'find.
     * 
     * @param file
     *            A file of sorted lines.
     * @param find
     *            A prefix.
     * @param pos
     *            A position in the file.
     * @return The lines found following pos that begins with find.
     * @throws IOException
     *             If an IO error happens.
     */
    private static String[] getFollowingLines(String file, String find, long pos) throws IOException {
        RandomAccessFile in = new RandomAccessFile(new File(file), "r");
        try {
            return getFollowingLines(in, find, pos);
        } finally {
            in.close();
        }
    }

    /**
     * Perform a binary search for a string in a file. Returns the position of a
     * line that begins with 'find'. Note that this may not be the first line,
     * if there be duplicates.
     * 
     * @return The index of a line matching find, or -1 if none found.
     */
    public static long binSearch(RandomAccessFile in, String find) throws IOException {
        long startpos = 0;
        in.seek(startpos);
        String line = in.readLine();
        if (line == null) return -1;
        if (compare(line, find) == 0) return startpos;
        long endpos = in.length();
        findMiddleLine(in, startpos, endpos);
        long prevpos = in.getFilePointer();
        do {
            line = in.readLine();
            if (line == null) {
                System.err.println("Internal: Ran past end of file in " + in + " at " + endpos);
                return -1;
            }
            int cmp = compare(line, find);
            if (cmp > 0) {
                endpos = prevpos;
            } else if (cmp < 0) {
                startpos = prevpos;
            } else {
                return prevpos;
            }
            if (startpos == endpos) {
                return -1;
            }
            prevpos = findMiddleLine(in, startpos, endpos);
            if (prevpos == startpos || prevpos == -1) {
                return -1;
            }
        } while (true);
    }

    /**
     * Returns the position of a line between startpos and endpos. If no line
     * other than the one starting at startpos can be found, returns -1. Also
     * sets the file pointer to the start of the line.
     * 
     * @param in
     *            The file to read from
     * @param startpos
     *            The lower bound for the position. Must be the start of a line.
     * @param endpos
     *            The upper bound for the position. Must be the start of a line
     *            or EOF.
     * @return The position of a line s.t. startpos < returnval < endpos, or -1
     *         if no such line can be found.
     * @throws IOException
     */
    private static long findMiddleLine(RandomAccessFile in, long startpos, long endpos) throws IOException {
        long newmidpos = endpos;
        int div = 1;
        while (newmidpos == endpos) {
            div *= 2;
            newmidpos = startpos + (endpos - startpos) / div;
            if (newmidpos == startpos) return -1;
            newmidpos = skipToLine(in, newmidpos);
        }
        if (newmidpos == startpos || newmidpos == endpos) {
            System.out.println("Invariant dead: " + startpos + " < " + newmidpos + " < " + endpos);
        }
        return newmidpos;
    }
}
