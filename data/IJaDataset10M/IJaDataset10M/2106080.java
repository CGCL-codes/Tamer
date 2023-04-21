package ch.nostromo.lib.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;

/**
 * NosTools. - i.E. for String manipulation
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class NosTools {

    /**
   * Returns a random int
   * 
   * @param maxInt
   *          Description of the Parameter
   * @return The randomInt value
   */
    public static int getRandomInt(int maxInt) {
        Random r = new Random();
        if (maxInt < 1) {
            return r.nextInt();
        } else {
            return r.nextInt(maxInt);
        }
    }

    /**
   * Returns a logging Level by name. If name is not "found" Level.ALL is
   * returned.
   * 
   * @param name
   *          of level
   * @return Level
   */
    public static Level getLevelByString(String name) {
        if (name.equalsIgnoreCase("ALL")) {
            return Level.ALL;
        } else if (name.equalsIgnoreCase("FINEST")) {
            return Level.FINEST;
        } else if (name.equalsIgnoreCase("FINER")) {
            return Level.FINER;
        } else if (name.equalsIgnoreCase("FINE")) {
            return Level.FINE;
        } else if (name.equalsIgnoreCase("CONFIG")) {
            return Level.CONFIG;
        } else if (name.equalsIgnoreCase("INFO")) {
            return Level.INFO;
        } else if (name.equalsIgnoreCase("WARNING")) {
            return Level.WARNING;
        } else if (name.equalsIgnoreCase("SEVERE")) {
            return Level.SEVERE;
        } else if (name.equalsIgnoreCase("OFF")) {
            return Level.OFF;
        } else {
            return Level.ALL;
        }
    }

    public static boolean isArgSet(String[] args, String argument) {
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equalsIgnoreCase(argument)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
   * Description of the Field
   */
    public static final String DATE_FORMAT_EU_DATE = "dd.MM.yyyy";

    /**
   * Description of the Field
   */
    public static final String DATE_FORMAT_EU_DATE_SHORTTIME = "dd.MM.yyyy HH:mm";

    /**
   * Description of the Field
   */
    public static final String TIME_FORMAT = "HH:mm:ss";

    /**
   * Description of the Field
   */
    public static final String TIME_FORMAT_SHORT = "HH:mm";

    /**
   * Description of the Method
   * 
   * @param date
   *          Description of the Parameter
   * @param dateFormat
   *          Description of the Parameter
   * @return Return Value
   * @exception Exception
   *              Exception
   */
    public static Date stringToDate(String date, String dateFormat) throws Exception {
        if (date == null) {
            return null;
        }
        if (date.equals("")) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            sdf.setLenient(false);
            return sdf.parse(date);
        } catch (Exception err) {
            throw err;
        }
    }

    /**
   * Description of the Method
   * 
   * @param date
   *          Description of the Parameter
   * @param dateFormat
   *          Description of the Parameter
   * @return Return Value
   * @exception Exception
   *              Exception
   */
    public static String dateToString(Date date, String dateFormat) throws Exception {
        if (date == null) {
            return "";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            sdf.setLenient(false);
            return sdf.format(date);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
   * Description of the Method
   * 
   * @param integer
   *          Description of the Parameter
   * @return Return Value
   * @exception Exception
   *              Exception
   */
    public static int stringToInt(String integer) throws Exception {
        if (integer.equals("")) {
            return 0;
        }
        return Integer.valueOf(integer).intValue();
    }

    /**
   * Description of the Method
   * 
   * @param integer
   *          Description of the Parameter
   * @return Return Value
   * @exception Exception
   *              Exception
   */
    public static String intToString(int integer) throws Exception {
        return String.valueOf(integer);
    }

    /**
   * Description of the Method
   * 
   * @param doubleVal
   *          Description of the Parameter
   * @return Return Value
   * @exception Exception
   *              Exception
   */
    public static String doubleToString(double doubleVal) throws Exception {
        return String.valueOf(doubleVal);
    }

    /**
   * Description of the Method
   * 
   * @param doubleVal
   *          Description of the Parameter
   * @return Return Value
   */
    public static String doubleToStringC(double doubleVal) {
        return (new DecimalFormat("#.00")).format(doubleVal);
    }

    /**
   * Description of the Method
   * 
   * @param doubleval
   *          Description of the Parameter
   * @return Return Value
   * @exception Exception
   *              Exception
   */
    public static double stringToDouble(String doubleval) throws Exception {
        if (doubleval.equals("")) {
            return 0;
        }
        return Double.valueOf(doubleval).doubleValue();
    }

    public static String rPad(String string, int length) {
        return rPad(string, length, " ");
    }

    /**
   * Description of the Method
   * 
   * @param string
   *          Description of the Parameter
   * @param length
   *          Description of the Parameter
   * @param padCharacter
   *          Description of the Parameter
   * @return Return Value
   */
    public static String rPad(String string, int length, String padCharacter) {
        if (string == null) {
            return null;
        }
        if (padCharacter == null) {
            return null;
        }
        if (string.length() >= length) {
            return string.substring(0, length);
        }
        StringBuffer sb = new StringBuffer(string);
        while (sb.length() < length) {
            sb.append(padCharacter);
        }
        return sb.toString();
    }

    public static String lPad(String string, int length) {
        return lPad(string, length, " ");
    }

    /**
   * Description of the Method
   * 
   * @param string
   *          Description of the Parameter
   * @param length
   *          Description of the Parameter
   * @param padCharacter
   *          Description of the Parameter
   * @return Return Value
   */
    public static String lPad(String string, int length, String padCharacter) {
        if (string == null) {
            return null;
        }
        if (padCharacter == null) {
            return null;
        }
        if (string.length() >= length) {
            return string.substring(0, length);
        }
        StringBuffer sb = new StringBuffer(string);
        while (sb.length() < length) {
            sb.insert(0, padCharacter);
        }
        return sb.toString();
    }

    /**
   * Description of the Method
   * 
   * @param str
   *          Description of the Parameter
   * @param pattern
   *          Description of the Parameter
   * @param replace
   *          Description of the Parameter
   * @return Return Value
   */
    public static String replaceAllSubString(String str, String pattern, String replace) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();
        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e + pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }

    /**
   * Returns the Extension of a file. Returns null if file is null or no
   * extension is found.
   * 
   * @param file
   *          File
   * @return The extension value
   */
    public static String getExtension(File file) {
        if (file != null) {
            String filename = file.getName();
            int pos = filename.lastIndexOf('.');
            if (pos > 0 && pos < filename.length() - 1) {
                return filename.substring(pos + 1);
            }
        }
        return null;
    }

    /**
   * Description of the Method
   * 
   * @param file
   *          Description of the Parameter
   * @param extension
   *          Description of the Parameter
   * @return Return Value
   */
    public static File patchFileExtension(File file, String extension) {
        String absolutePath = file.getAbsolutePath();
        if (!absolutePath.toUpperCase().endsWith(extension.toUpperCase())) {
            absolutePath += extension;
            return new File(absolutePath);
        }
        return file;
    }

    /**
   * Description of the Method
   * 
   * @param file
   *          Description of the Parameter
   * @return Return Value
   * @exception Exception
   *              Exception
   */
    public static String readAsciiFile(File file) throws Exception {
        try {
            String result = "";
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String thisLine = "";
            while ((thisLine = reader.readLine()) != null) {
                result += thisLine + "\n";
            }
            reader.close();
            return result;
        } catch (Exception err) {
            throw err;
        }
    }

    public static String readAsciiFile(java.net.URL url) throws Exception {
        return readAsciiFile(new File(url.getFile()));
    }

    /**
   * Description of the Method
   * 
   * @param file
   *          Description of the Parameter
   * @param content
   *          Description of the Parameter
   * @exception Exception
   *              Exception
   */
    public static void writeAsciiFile(File file, String content) throws Exception {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            Writer writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (Exception err) {
            throw err;
        }
    }

    /**
   * Gets the fileNamesInDirByExtension attribute of the Tools class
   * 
   * @param directory
   *          Description of the Parameter
   * @param extension
   *          Description of the Parameter
   * @return The fileNamesInDirByExtension value
   * @exception Exception
   *              Exception
   */
    public static String[] getFileNamesInDirByExtension(String directory, String extension) throws Exception {
        FileExtensionFilter fileExtensionFilter = new FileExtensionFilter(extension);
        return new File(directory).list(fileExtensionFilter);
    }

    /**
   * Gets the filesInDirByExtension attribute of the Tools class
   * 
   * @param directory
   *          Description of the Parameter
   * @param extension
   *          Description of the Parameter
   * @return The filesInDirByExtension value
   * @exception Exception
   *              Exception
   */
    public static File[] getFilesInDirByExtension(String directory, String extension) throws Exception {
        FileExtensionFilter fileExtensionFilter = new FileExtensionFilter(extension);
        return new File(directory).listFiles(fileExtensionFilter);
    }

    /**
   * Gets the fileNamesInDirByPrefix attribute of the Tools class
   * 
   * @param directory
   *          Description of the Parameter
   * @param prefix
   *          Description of the Parameter
   * @return The fileNamesInDirByPrefix value
   * @exception Exception
   *              Exception
   */
    public static String[] getFileNamesInDirByPrefix(String directory, String prefix) throws Exception {
        NosFilePrefixFilter filePrefixFilter = new NosFilePrefixFilter(prefix);
        return new File(directory).list(filePrefixFilter);
    }

    /**
   * Gets the filesInDirByPrefix attribute of the Tools class
   * 
   * @param directory
   *          Description of the Parameter
   * @param prefix
   *          Description of the Parameter
   * @return The filesInDirByPrefix value
   * @exception Exception
   *              Exception
   */
    public static File[] getFilesInDirByPrefix(String directory, String prefix) throws Exception {
        NosFilePrefixFilter filePrefixFilter = new NosFilePrefixFilter(prefix);
        File dir = new File(directory);
        return dir.listFiles(filePrefixFilter);
    }

    /**
   * Description of the Class
   * 
   * @author Bernhard von Gunten <bvg@users.sourceforge.net>
   */
    public static class FileExtensionFilter implements FilenameFilter {

        private String pattern;

        /**
     * Constructor for the FileExtensionFilter object
     * 
     * @param str
     *          Description of the Parameter
     */
        public FileExtensionFilter(String str) {
            pattern = str.toUpperCase();
        }

        /**
     * Description of the Method
     * 
     * @param dir
     *          Description of the Parameter
     * @param name
     *          Description of the Parameter
     * @return Return Value
     */
        public boolean accept(File dir, String name) {
            String upName = name.toUpperCase();
            return upName.endsWith(pattern);
        }
    }

    /**
   * Description of the Class
   * 
   * @author Bernhard von Gunten <bvg@users.sourceforge.net>
   */
    public static class NosFilePrefixFilter implements FilenameFilter {

        private String pattern;

        /**
     * Constructor for the FilePrefixFilter object
     * 
     * @param str
     *          Description of the Parameter
     */
        public NosFilePrefixFilter(String str) {
            pattern = str.toUpperCase();
        }

        /**
     * Description of the Method
     * 
     * @param dir
     *          Description of the Parameter
     * @param name
     *          Description of the Parameter
     * @return Return Value
     */
        public boolean accept(File dir, String name) {
            String upName = name.toUpperCase();
            return upName.startsWith(pattern);
        }
    }

    public static final boolean checkMask(int value, int mask) {
        return 0 != (mask & value);
    }
}
