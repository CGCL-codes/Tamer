package softwareengineering;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * <p><code>FileIO</code> takes a filepath, converts it to a File, reads the File 
 * and then streams out the code</p>
 *
 * @version 1.00, 08 June 2009
 */
public class FileIO {

    /**
     * This is essentially the head of the FileIO class The Method
     * that takes in a filePath and passes it to openFile. It then
     * takes the file and passes it to readFile and returns the contents
     *
     * @param filePath  The path a nf filename of the file to read
     * @return The contents of the file
     * @throws IOException if the file can not be read
     */
    @Deprecated
    public static String getFileContents(String filePath, URL context) throws IOException {
        URL url = new URL(context, (isURL(filePath) ? "" : "file:") + filePath);
        String contents = readUrl(url);
        return contents;
    }

    /**
     * Method that returns the contents of the file at the given path.
     * Depreciated in favour of the new URL based file I/O.
     *
     * @param filePath The path to the file to load
     * @return  The contents of the file as a string.
     * @throws java.io.IOException  when a file read error occurs.
     * @deprecated  Use readUrl instead.
     */
    @Deprecated
    public static String getFileContents(String filePath) throws IOException {
        return getFileContents(filePath, null);
    }

    /**
     * This method takes a filePath from getFileContents and if it
     * has a correct address it creates a file and passes it back to 
     * getFileContents if it has a local address then it creates a
     * URI and then adds that to front of the file before returning it
     *
     * @param filePath  The path and filename of the file to open
     * @return a new filehandler object
     * @throws java.io.IOException if file cannot be read
     */
    @Deprecated
    protected static File openFile(String filePath) throws IOException {
        File file;
        file = new File(filePath);
        return file;
    }

    /**
     * This method takes a file, reads it using a BufferedReader. It
     * creates a printWriter and a stringWriter, it wraps the
     * printWriter around the StringWriter and prints once the
     * writing has finished it closes the streams and
     * returns the contents to getFileContents
     *
     * @param file  The file to read the contents of
     * @return the contents of the file
     * @throws java.io.IOException when the file can't be read
     */
    @Deprecated
    protected static String readFile(File file) throws IOException {
        BufferedReader inputStream = null;
        PrintWriter outputStream = null;
        StringWriter outputString = null;
        try {
            inputStream = new BufferedReader(new FileReader(file));
            outputString = new StringWriter();
            outputStream = new PrintWriter(outputString);
            final char[] buffer = new char[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) outputStream.write(buffer, 0, len);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
        return outputString.toString();
    }

    /**
     * Overloaded getURL that set the second parameter (context) to null.
     * Returns a url object for the given string.
     *
     * @param str   The string representation of a url
     * @return  The url object from the parameter
     * @throws java.net.MalformedURLException when the string is invalid
     */
    public static URL getURL(String str) throws MalformedURLException {
        return getURL(str, null);
    }

    /**
     * Returns a new URL object representing the location of a file. Uses the
     * str parameter for the location, relative to the context URL.
     *
     * @param str   The string representation of the url
     * @param context Path the the string should be relative to
     * @return  A new url object
     * @throws java.net.MalformedURLException when the string is invalid
     */
    public static URL getURL(String str, URL context) throws MalformedURLException {
        if (!isURL(str)) {
            return new URL(context, "file:" + str);
        } else {
            return new URL(context, str);
        }
    }

    /**
     * Method that checks if the provided string parameter is a vliad url or
     * not. This used so that local paths can be converted to urls if nessesary.
     *
     * @param str A file path of url string
     * @return True if the string is a url, false otherwise.
     */
    protected static boolean isURL(String str) {
        return str.matches("\\w{2,}:/.*");
    }

    /**
     * Return the content of the provided url as a string.
     *
     * @param url The file to read
     * @return  the string contents of the file
     * @throws java.io.IOException when a read error occurs
     */
    protected static String readUrl(URL url) throws IOException {
        BufferedReader in = null;
        StringBuffer buf = new StringBuffer();
        try {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            final char[] charBuf = new char[1024];
            int len = 0;
            while ((len = in.read(charBuf)) != -1) buf.append(charBuf, 0, len);
        } finally {
            if (in != null) in.close();
        }
        return buf.toString();
    }
}
