package org.xaware.shared.util.logging;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xaware.shared.util.SearchUtil;

/**
 * This class provides static methods to fetch lines from a XAware log file that match given criteria. I is assumed that
 * the code behind the three public methods will be replaced with something more robust.
 * 
 * @author jtarnowski
 */
public class EventLogUtil {

    private static final int DEFAULT_MAX_LINES = 500;

    private static HashMap levels = null;

    static {
        levels = new HashMap();
        levels.put("CONFIG", new Integer(8));
        levels.put("SEVERE", new Integer(7));
        levels.put("WARNING", new Integer(6));
        levels.put("INFO", new Integer(5));
        levels.put("FINE", new Integer(4));
        levels.put("FINER", new Integer(3));
        levels.put("FINEST", new Integer(2));
        levels.put("DEBUG", new Integer(1));
        levels.put("ALL", new Integer(0));
    }

    public static final String SEARCH_TYPE_OFF = "OFF";

    public static final String SEARCH_TYPE_STANDARD = "STANDARD";

    public static final String SEARCH_TYPE_COMPONENT = "COMPONENT";

    public static final String SEARCH_TYPE_PARAMETER = "PARAMETER";

    private static final String timeLeaderPattern = "^20[0-9]{2}-[0-1][0-9]-[0-3][0-9]" + " [0-2][0-9]:[0-5][0-9]:[0-5][0-9]\\.[0-9]{3}.*";

    private static final Pattern logTimeLeader = Pattern.compile(timeLeaderPattern);

    public static boolean isLogLineStart(final String line) {
        if (line == null) {
            return false;
        }
        final String segment[] = line.split("\n");
        final Matcher m = logTimeLeader.matcher(segment[0]);
        return m.matches();
    }

    /**
     * return the first 23 characters of a line if the log line is a start line otherwise return null
     * 
     * @param line
     * @return
     */
    public static String getStartTime(final String line) {
        if (isLogLineStart(line)) {
            final int timeLen = 23;
            return line.substring(0, timeLen);
        }
        return null;
    }

    /**
     * if the log line is a start log line then return a 4 character thread id otherwise return null
     * 
     * @param line
     * @return
     */
    public static String getThreadID(final String line) {
        if (isLogLineStart(line)) {
            final int threadStart = 23 + 1;
            return line.substring(threadStart, threadStart + 4);
        }
        return null;
    }

    /**
     * Get the lines for given criteria. Also return the start and end offsets in an int array.
     * 
     * @param startTime -
     *            String
     * @param endTime -
     *            String
     * @param threadId -
     *            String
     * @param filename -
     *            String
     * @param logLevel -
     *            String
     * @param maxLines -
     *            String
     * @param startEnd -
     *            int[2] - out
     * @param searchStr
     * @param searchType
     * @return List of matching lines.
     */
    public static List getLogLines(String startTime, String endTime, String threadId, final String filename, final String logLevel, final String maxLines, final int[] startEnd, final boolean[] enablePrevNext, final String searchStr, final String searchType) {
        enablePrevNext[0] = false;
        enablePrevNext[1] = true;
        boolean logLineStart = false;
        boolean includeLogLine = false;
        int startLength = 0;
        int endLength = 0;
        int threadLength = 0;
        final int totalLines = getNumber(maxLines);
        final List lines = new ArrayList(totalLines);
        if (startTime != null && startTime.trim().length() > 0) {
            startTime = startTime.trim();
            startLength = startTime.length();
        }
        if (endTime != null && endTime.trim().length() > 0) {
            endTime = endTime.trim();
            endLength = endTime.length();
        }
        if (threadId != null && threadId.trim().length() > 0) {
            threadId = threadId.trim();
            threadLength = threadId.length();
        }
        int linesRead = 0;
        BufferedReader in = null;
        try {
            final FileReader logReader = new FileReader(filename);
            in = new BufferedReader(logReader);
            int count = 0;
            String line;
            final boolean[] tooLate = new boolean[1];
            while (in != null && count < totalLines) {
                line = in.readLine();
                if (line == null) {
                    enablePrevNext[1] = false;
                    break;
                }
                final Matcher m = logTimeLeader.matcher(line);
                logLineStart = m.matches();
                if (logLineStart) {
                    includeLogLine = isMatch(line, startTime, startLength, endTime, endLength, threadId, threadLength, logLevel, tooLate, searchStr, searchType);
                    if (includeLogLine) {
                        lines.add(line);
                        count++;
                    }
                } else if (includeLogLine) {
                    lines.add(line);
                    count++;
                }
                if (tooLate[0]) {
                    linesRead++;
                    enablePrevNext[1] = false;
                    break;
                }
                linesRead++;
            }
            if (count > 0) {
                startEnd[0] = 0;
                startEnd[1] = linesRead;
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
            }
        }
        return lines;
    }

    /**
     * Get the next matching lines before the offset provided in the lastLine param
     * 
     * @param lastLine -
     *            String
     * @param startTime -
     *            String
     * @param threadId -
     *            String
     * @param filename -
     *            String
     * @param logLevel -
     *            String
     * @param maxLines -
     *            String
     * @param startEnd -
     *            int[2] - out
     * @param searchStr
     * @param searchType
     * @return List of matching lines.
     */
    public static List getPreviousLines(final String lastLine, String startTime, String threadId, final String filename, final String logLevel, final String maxLines, final int[] startEnd, final boolean[] enablePrevNext, final String searchStr, final String searchType) {
        enablePrevNext[0] = false;
        enablePrevNext[1] = true;
        boolean logLineStart = false;
        boolean includeLogLine = false;
        int startLength = 0;
        int threadLength = 0;
        final int index = getNumber(lastLine) - 1;
        final int totalLines = getNumber(maxLines);
        final List lines = new ArrayList(totalLines);
        final List queue = new ArrayList(totalLines);
        final List lineNums = new ArrayList(totalLines);
        if (lastLine == null && lastLine.trim().length() == 0) {
            return getLogLines(startTime, "", threadId, filename, logLevel, maxLines, startEnd, enablePrevNext, searchStr, searchType);
        }
        BufferedReader in = null;
        try {
            if (startTime != null && startTime.trim().length() > 0) {
                startTime = startTime.trim();
                startLength = startTime.length();
            }
            if (threadId != null && threadId.trim().length() > 0) {
                threadId = threadId.trim();
                threadLength = threadId.length();
            }
            final FileReader logReader = new FileReader(filename);
            in = new BufferedReader(logReader);
            String line = " ";
            int count = 0;
            int currentLineNum = 0;
            final boolean[] tooLate = new boolean[1];
            for (int i = 0; i < index && in != null && line != null; i++) {
                line = in.readLine();
                if (line == null) {
                    break;
                } else {
                    final Matcher m = logTimeLeader.matcher(line);
                    logLineStart = m.matches();
                    currentLineNum++;
                    if (logLineStart) {
                        includeLogLine = isMatch(line, startTime, startLength, "", 0, threadId, threadLength, logLevel, tooLate, searchStr, searchType);
                        if (includeLogLine) {
                            enablePrevNext[0] = queueUpLogline(totalLines, queue, lineNums, line, currentLineNum);
                            count++;
                        }
                    } else if (includeLogLine) {
                        enablePrevNext[0] = queueUpLogline(totalLines, queue, lineNums, line, currentLineNum);
                        count++;
                    }
                }
            }
            lines.addAll(queue);
            final int size = lineNums.size();
            if (size == 0) {
                startEnd[0] = 0;
                startEnd[1] = 0;
            } else {
                startEnd[0] = ((Integer) lineNums.get(0)).intValue();
                startEnd[1] = ((Integer) lineNums.get(size - 1)).intValue();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
            }
        }
        return lines;
    }

    /**
     * Queue up the log line. The queue is the potential returned set of log lines when the user requests a previous
     * page
     * 
     * @param totalLines
     * @param queue
     * @param lineNums
     * @param line
     * @param currentLineNum
     * @return boolean - true if we had to dequeue anything
     */
    private static boolean queueUpLogline(final int totalLines, final List queue, final List lineNums, final String line, final int currentLineNum) {
        boolean rc = false;
        queue.add(line);
        lineNums.add(new Integer(currentLineNum));
        if (queue.size() > totalLines) {
            queue.remove(0);
            lineNums.remove(0);
            rc = true;
        }
        return rc;
    }

    /**
     * Get the next matching lines after the offset provided in the lastLine param
     * 
     * @param lastLine -
     *            String
     * @param endTime -
     *            String
     * @param threadId -
     *            String
     * @param filename -
     *            String
     * @param logLevel -
     *            String
     * @param maxLines -
     *            String
     * @param startEnd
     *            int[2] - out
     * @param searchStr
     * @param searchType
     * @return List of matching lines.
     */
    public static List getNextLines(final String lastLine, String endTime, String threadId, final String filename, final String logLevel, final String maxLines, final int[] startEnd, final boolean[] enablePrevNext, final String searchStr, final String searchType) {
        enablePrevNext[0] = true;
        enablePrevNext[1] = true;
        boolean logLineStart = false;
        boolean includeLogLine = false;
        int endLength = 0;
        int threadLength = 0;
        final int index = getNumber(lastLine) + 1;
        final int totalLines = getNumber(maxLines);
        final List lines = new ArrayList(totalLines);
        if (lastLine != null && lastLine.trim().length() == 0) {
            return getLogLines("", endTime, threadId, filename, logLevel, maxLines, startEnd, enablePrevNext, searchStr, searchType);
        }
        if (endTime != null && endTime.trim().length() > 0) {
            endTime = endTime.trim();
            endLength = endTime.length();
        }
        if (threadId != null && threadId.trim().length() > 0) {
            threadId = threadId.trim();
            threadLength = threadId.length();
        }
        BufferedReader in = null;
        try {
            final FileReader logReader = new FileReader(filename);
            in = new BufferedReader(logReader);
            String line;
            final boolean[] tooLate = new boolean[1];
            tooLate[0] = false;
            int count = 0;
            line = " ";
            for (int i = 0; i < index && in != null && line != null && !tooLate[0]; i++) {
                startEnd[0] = i;
                line = in.readLine();
                if (line == null) {
                    enablePrevNext[1] = false;
                    break;
                }
                final Matcher m = logTimeLeader.matcher(line);
                logLineStart = m.matches();
                if (logLineStart) {
                    includeLogLine = isMatch(line, "", 0, endTime, endLength, threadId, threadLength, logLevel, tooLate, searchStr, searchType);
                }
            }
            int linesRead = startEnd[0];
            startEnd[0] += 1;
            while (in != null && count < totalLines) {
                if (line == null) {
                    enablePrevNext[1] = false;
                    break;
                }
                final Matcher m = logTimeLeader.matcher(line);
                logLineStart = m.matches();
                if (logLineStart) {
                    includeLogLine = isMatch(line, "", 0, endTime, endLength, threadId, threadLength, logLevel, tooLate, searchStr, searchType);
                    if (includeLogLine) {
                        lines.add(line);
                        count++;
                    }
                } else if (includeLogLine) {
                    lines.add(line);
                    count++;
                }
                if (tooLate[0]) {
                    enablePrevNext[1] = false;
                    break;
                }
                line = in.readLine();
                linesRead++;
            }
            startEnd[1] = linesRead;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
            }
        }
        return lines;
    }

    /**
     * Parse the string to get maximum lines to return. If the string is not an integer, use default
     * 
     * @param maxLines -
     *            String
     * @return int
     */
    private static int getNumber(final String maxLines) {
        int totalLines = DEFAULT_MAX_LINES;
        if (maxLines != null && maxLines.length() > 0) {
            try {
                totalLines = Integer.parseInt(maxLines.trim());
            } catch (final Exception e) {
            }
        }
        return totalLines;
    }

    /**
     * Decide if a line should be in the result set.
     * 
     * @param line -
     *            String
     * @param startTime -
     *            String
     * @param startLength -
     *            int - when 0, don't use it
     * @param endTime -
     *            String
     * @param endLength -
     *            int - when 0, don't use it
     * @param threadId -
     *            String
     * @param threadLength -
     *            int - when 0, don't use it
     * @param logLevel -
     *            String
     * @param tooLate -
     *            boolean[1] - out - when true, no need to read additional lines
     * @param searchType
     * @param searchStr
     * @return boolean
     */
    private static boolean isMatch(final String line, final String startTime, final int startLength, final String endTime, final int endLength, final String threadId, final int threadLength, final String logLevel, final boolean[] tooLate, final String searchStr, final String searchType) {
        String logLineLevel = null;
        int thirdSpace = -1;
        int secondSpace = -1;
        int firstSpace = line.indexOf(' ');
        firstSpace++;
        if (line.length() > firstSpace) {
            secondSpace = line.indexOf(' ', firstSpace);
            if (secondSpace == -1) {
                return false;
            }
            secondSpace++;
        }
        if (startLength > 0) {
            if (line.length() > startLength) {
                if (line.substring(0, startLength).compareTo(startTime) < 0) {
                    return false;
                }
            }
        }
        if (endLength > 0) {
            if (line.length() > endLength) {
                if (line.substring(0, endLength).compareTo(endTime) > 0) {
                    tooLate[0] = true;
                    return false;
                }
            }
        }
        if (threadLength > 0) {
            final int thLen = secondSpace + threadLength;
            if (line.length() > thLen) {
                if (threadLength > 0) {
                    if (!threadId.equals(line.substring(secondSpace, thLen))) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        if (line.length() > secondSpace) {
            thirdSpace = line.indexOf(' ', secondSpace);
            if (thirdSpace == -1) {
                return false;
            }
            thirdSpace++;
            int fourthSpace = line.indexOf(' ', thirdSpace);
            if (fourthSpace == -1) {
                return false;
            }
            logLineLevel = line.substring(thirdSpace, fourthSpace--);
        } else {
            return false;
        }
        if (logLevelMatches(logLineLevel, logLevel) == false) {
            return false;
        }
        return matchSearch(line, searchStr, searchType);
    }

    /**
     * test line for wildcard then if needed wrap in wildcards and test for match.
     * 
     * @param line
     * @param searchStr
     * @param searchType
     * @return
     */
    private static boolean matchSearch(final String line, String searchStr, final String searchType) {
        if (SEARCH_TYPE_OFF.equals(searchType) || searchStr == null || searchStr.length() == 0) {
            return true;
        }
        if (searchStr.indexOf("*") < 0) {
            searchStr = "*" + searchStr + "*";
        }
        final boolean isCaseSensitive = true;
        return SearchUtil.stringMatches(line, searchStr, isCaseSensitive);
    }

    /**
     * Check if log level is one that should be returned
     * 
     * @param level -
     *            String
     * @param desiredLevel -
     *            String
     * @return boolean - if desired is INFO, WARNING and SEVERE should also match
     */
    private static boolean logLevelMatches(final String level, final String desiredLevel) {
        int actual = 0;
        int desired = 0;
        final Integer iLevel = (Integer) levels.get(level);
        if (iLevel != null) {
            actual = iLevel.intValue();
        }
        final Integer dLevel = (Integer) levels.get(desiredLevel);
        if (dLevel != null) {
            desired = dLevel.intValue();
        }
        return actual >= desired;
    }
}
