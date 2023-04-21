package net.assimilator.substrates.sca.utils;

import java.io.*;
import java.util.logging.Logger;

/**
 * Performs search and replace on text within files. Changes
 * Windows filenames containing spaces to the equivalent 8.3 format name.
 *
 * @author Kevin Hartig
 * @version $Id$
 */
public final class FileEditor implements Serializable {

    static final long serialVersionUID = 5198322919070339667L;

    private boolean windowsOS = false;

    private final Logger logger = Logger.getLogger("net.assimilator.examples.sca.web.tomcat");

    /**
     * Creates a new instance of FileEditor
     */
    public FileEditor() {
        String osName = System.getProperty("os.name").toLowerCase();
        if ((osName.indexOf("windows") > -1) || (osName.indexOf("nt") > -1)) {
            windowsOS = true;
        }
    }

    /**
     * Find the specified line in the file and delete the line in the position
     * defined by the offset position.
     *
     * @param filename         the name of the file to edit.
     * @param search           the search string.
     * @param position         offset position of line to delete from the search string
     *                         found.
     * @param numLinesToDelete the number of lines to delete.
     * @param blankline        boolean indicating to write blank lines.
     * @return true if the delete operation was successful, false otherwise.
     */
    public boolean deleteLines(String filename, String search, int position, int numLinesToDelete, boolean blankline) {
        File in = null;
        File out = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        String fn = filename;
        int lineNumber;
        String newline = "\n";
        if (windowsOS) {
            fn = checkForSpaces(fn);
        }
        try {
            in = new File(fn);
            out = File.createTempFile("temp", null, new File("."));
            reader = new BufferedReader(new FileReader(in));
            writer = new BufferedWriter(new FileWriter(out));
            reader.mark(64000);
            String currentLine;
            boolean found = false;
            lineNumber = 0;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.toLowerCase().indexOf(search.toLowerCase()) > -1) {
                    found = true;
                    lineNumber++;
                    break;
                } else {
                    lineNumber++;
                }
            }
            if (!found) {
                logger.warning("Error inserting new strings. " + search + " not found.");
                out.delete();
                return false;
            } else {
                reader.reset();
                int deleteLineNumber = lineNumber + position;
                lineNumber = 0;
                while ((currentLine = reader.readLine()) != null) {
                    lineNumber++;
                    if ((lineNumber < deleteLineNumber) || (lineNumber > deleteLineNumber + numLinesToDelete - 1)) {
                        writer.write(currentLine + newline);
                    } else {
                        if (blankline) writer.write(newline);
                    }
                }
                return true;
            }
        } catch (IOException e) {
            logger.severe("CRITICAL ERROR! Unable to read file " + filename);
            return false;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (in != null) in.delete();
            out.renameTo(in);
        }
    }

    /**
     * Search for substring in file and insert a new line after it.
     *
     * @param filename The file to edit.
     * @param search   The substring in the file to search for.
     * @param strings  A whole new line to insert after the line containing the
     *                 search substring.
     * @return true if the operation was successfull, false otherwise.
     */
    public boolean insertString(String filename, String search, String[] strings) {
        File in = null;
        File out = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        String fn = filename;
        if (windowsOS) {
            fn = checkForSpaces(fn);
        }
        try {
            in = new File(fn);
            out = File.createTempFile("temp", null, new File("."));
            reader = new BufferedReader(new FileReader(in));
            writer = new BufferedWriter(new FileWriter(out));
            String currentLine;
            boolean found = false;
            String newline = "\n";
            while ((currentLine = reader.readLine()) != null) {
                logger.fine(currentLine);
                if (currentLine.toLowerCase().indexOf(search.toLowerCase()) > -1) {
                    found = true;
                    writer.write(currentLine + newline);
                    for (String string : strings) {
                        writer.write(string + newline);
                    }
                } else {
                    writer.write(currentLine + newline);
                }
            }
            if (!found) {
                logger.warning("Error inserting new strings. " + search + " not found in file " + fn + ".");
                out.delete();
                return false;
            }
        } catch (IOException e) {
            logger.warning("CRITICAL ERROR! Unable to read file " + filename);
            return false;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (in != null) in.delete();
            out.renameTo(in);
        }
        return true;
    }

    public String findString(String filename, String search) {
        File in;
        BufferedReader reader = null;
        String currentLine = null;
        boolean found = false;
        String fn = filename;
        if (windowsOS) {
            fn = checkForSpaces(fn);
        }
        try {
            in = new File(fn);
            reader = new BufferedReader(new FileReader(in));
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.toLowerCase().indexOf(search.toLowerCase()) > -1) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                currentLine = null;
            }
        } catch (IOException e) {
            logger.severe("CRITICAL ERROR! Unable to read file " + filename);
            currentLine = null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return currentLine;
    }

    /**
     * Replaces all occurances of the given substring with the replacement
     * string.
     *
     * @param filename    The file to edit
     * @param original    The substring in the file that needs to be replaced
     * @param replacement A whole new line to replace the line containing the
     *                    substring
     * @return true if the operation was successful, false otherwise.
     */
    public boolean replaceAllStrings(String filename, String original, String replacement) {
        return replaceString(filename, original, replacement, true, 0);
    }

    /**
     * Replace the first occurance of the substring with the repalcement string.
     *
     * @param filename    The file to edit.
     * @param original    The substring in the file that needs to be replaced.
     * @param replacement A whole new line to replace the line containing the
     *                    substring.
     * @param occurrence  The occurence of the string to replace. This is used if
     *                    there is more than one occurance of the designated string to replace. The integer
     *                    value defines which occurance to replace.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean replaceOccurrenceOfString(String filename, String original, String replacement, int occurrence) {
        return replaceString(filename, original, replacement, false, occurrence);
    }

    /**
     * Search for substring in file and replace with a whole new line.
     *
     * @param filename    The file to edit.
     * @param original    The substring in the file that needs to be replaced.
     * @param replacement A whole new line to replace the line containing the
     *                    substring.
     * @param all         boolean indicating if all or firat occurance of string
     *                    should be replaced.
     * @param occurrence  The occurence of the string to replace. This is used if
     *                    there is more than one occurance of the designated string to replace. The integer
     *                    value defines which occurance to replace.
     * @return true if the operation is successful, false otherwise.
     */
    public boolean replaceString(String filename, String original, String replacement, boolean all, int occurrence) {
        File in = null;
        File out = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        String fn = filename;
        if (windowsOS) {
            fn = checkForSpaces(fn);
        }
        try {
            in = new File(fn);
            out = File.createTempFile("temp", null, new File("."));
            reader = new BufferedReader(new FileReader(in));
            writer = new BufferedWriter(new FileWriter(out));
            String currentLine;
            int found = 0;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.toLowerCase().indexOf(original.toLowerCase()) > -1) {
                    found++;
                    if (all || (!all && (found == occurrence))) {
                        writer.write(replacement + "\n");
                        logger.fine("all = " + all + ". found = " + found);
                        logger.fine("Replacing " + occurrence + "th of " + currentLine + " with " + replacement);
                    } else {
                        writer.write(currentLine + "\n");
                    }
                } else {
                    writer.write(currentLine + "\n");
                }
            }
            if (found == 0) {
                logger.warning("Error replacing string. " + replacement + " not found.");
                out.delete();
                return false;
            }
        } catch (IOException e) {
            logger.warning("CRITICAL ERROR! Unable to read file " + filename);
            return false;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (in != null) in.delete();
            out.renameTo(in);
        }
        return true;
    }

    public String checkForSpaces(String filename) {
        String cmd;
        String dir;
        int endSlashIndex;
        int spaceIndex;
        WindowsFilenameFormatter formatter;
        Process process;
        boolean atEnd = false;
        String newFilename = filename;
        String backslash = "\\";
        while (newFilename.indexOf(' ') > -1) {
            logger.fine("Converting filename" + newFilename);
            spaceIndex = newFilename.indexOf(' ');
            endSlashIndex = newFilename.indexOf(backslash, spaceIndex);
            if (endSlashIndex == -1) {
                endSlashIndex = newFilename.length();
                atEnd = true;
            }
            dir = newFilename.substring(0, endSlashIndex);
            formatter = new WindowsFilenameFormatter();
            formatter.setFilename(dir);
            try {
                cmd = formatter.getCommandLine("dir /X " + dir.substring(0, dir.lastIndexOf(backslash) + 1));
                logger.fine("Process command [" + cmd + "] ...");
                process = Runtime.getRuntime().exec(cmd);
                formatter.setInputStream(process.getInputStream());
                formatter.start();
                process.waitFor();
                if (!atEnd) {
                    newFilename = formatter.getFilename() + backslash + newFilename.substring(endSlashIndex + 1);
                } else {
                    newFilename = formatter.getFilename();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        logger.fine("Converted filename = " + newFilename);
        return newFilename;
    }

    public static void main(String[] args) {
        String filename = "C:\\Documents and Settings\\Kevin Hartig\\.assimilator\\external\\Tomcat-LB\\jakarta-tomcat-5.0.27\\webapps\\balancer\\WEB-INF\\config\\test-rules.xml";
        FileEditor editor = new FileEditor();
        editor.deleteLines(filename, "192.168.0.4", -3, 7, false);
        for (int i = 1; i < 2 + 1; i++) {
            editor.replaceOccurrenceOfString(filename, "serverInstance=", "    serverInstance=\"" + i + "\"", i);
        }
        editor.replaceAllStrings(filename, "maxServerInstances", "    maxServerInstances=\"" + 2 + "\"");
    }

    class WindowsFilenameFormatter extends Thread {

        InputStream in;

        String filename;

        String directory;

        String file;

        String osName;

        public void setInputStream(InputStream inputStream) {
            this.in = inputStream;
        }

        private String getCommandLine(String arg) {
            osName = System.getProperty("os.name").toLowerCase();
            logger.fine("In WindowsFilnameFormatter, osName = " + osName);
            String cmd;
            if ((osName.indexOf("nt") > -1) || (osName.indexOf("windows 2000") > -1) || (osName.indexOf("windows 2003") > -1) || (osName.indexOf("windows xp") > -1) || (osName.indexOf("windows vista") > -1)) {
                cmd = "cmd.exe /C " + arg;
            } else if (osName.indexOf("windows 9") > -1) {
                cmd = "command.com /C " + arg;
            } else {
                cmd = arg;
            }
            return (cmd);
        }

        public void setFilename(String filename) {
            this.filename = filename;
            directory = filename.substring(0, filename.lastIndexOf("\\"));
            file = filename.substring(filename.lastIndexOf("\\") + 1, filename.length());
            logger.fine("directory = " + directory);
            logger.fine("file = " + file);
        }

        public String getFilename() {
            return directory + "\\" + file;
        }

        public void run() {
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String line;
            String[] dirInfo;
            try {
                while ((line = br.readLine()) != null) {
                    if (line.indexOf(file) != -1) {
                        dirInfo = line.split("\\s+");
                        if ((osName.indexOf("windows xp") > -1) || (osName.indexOf("windows 2003") > -1) || (osName.indexOf("windows vista") > -1)) {
                            file = dirInfo[4];
                        } else {
                            file = dirInfo[3];
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
