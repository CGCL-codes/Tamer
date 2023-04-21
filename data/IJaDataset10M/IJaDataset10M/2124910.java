package com.aptana.ide.scripting;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import com.aptana.ide.core.IdeLog;
import com.aptana.ide.core.StringUtils;
import com.aptana.ide.core.Trace;

/**
 * @author Ingo Muschenetz
 */
public final class FileUtilities {

    /**
	 * FileUtilities
	 *
	 * make this non-instantiable
	 */
    private FileUtilities() {
    }

    /**
	 * Creates the directory at the named location
	 * 
	 * @param parentDirectory
	 *            The location of the parent directory
	 * @param directoryName
	 *            The name of the directory to create.
	 * @return A file if the directory was created, null if not.
	 */
    public static File createDirectory(String parentDirectory, String directoryName) {
        File newDirectory = new File(parentDirectory + File.separator + directoryName);
        if (!newDirectory.exists()) {
            newDirectory.mkdir();
        }
        return newDirectory;
    }

    /**
	 * Appeends the two paths together, with the proper separator string
	 * 
	 * @param parentDirectory
	 *            The location of the parent directory
	 * @param directoryName
	 *            The name of the directory to create.
	 * @return A string representing the concatenated paths
	 */
    public static String appendPaths(String parentDirectory, String directoryName) {
        if (parentDirectory.endsWith(File.separator)) {
            return parentDirectory + directoryName;
        } else {
            return parentDirectory + File.separator + directoryName;
        }
    }

    /**
	 * Copy bytes from one stream to another.
	 * 
	 * @param in
	 *            The input stream
	 * @param out
	 *            The output stream
	 * @throws IOException
	 *             Thrown if there is an error in copying.
	 */
    public static void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.close();
    }

    /**
	 * Determines if the directory at the named location exists
	 * 
	 * @param parentDirectory
	 *            The location of the parent directory
	 * @param directoryName
	 *            The name of the directory to create.
	 * @return Returns true if the specified directory exists
	 */
    public static boolean directoryExists(String parentDirectory, String directoryName) {
        return fileExists(parentDirectory, directoryName);
    }

    /**
	 * Determines if the file at the named location exists
	 * 
	 * @param parentDirectory
	 *            The location of the parent directory
	 * @param filename
	 *            The name of the directory to create.
	 * @return Returns true if the specified file exists
	 */
    public static boolean fileExists(String parentDirectory, String filename) {
        return new File(parentDirectory + File.separator + filename).exists();
    }

    /**
	 * Determines if the specified file exists
	 * 
	 * @param filename
	 *            The relative or full path to the file name to test for existance
	 * @return Returns true if the specified file exists
	 */
    public static boolean fileExists(String filename) {
        return new File(filename).exists();
    }

    /**
	 * catch (Exception e) { e.printStackTrace(); } Grab the text for our bootstrap script
	 * 
	 * @param name
	 *            The name of the resource to read
	 * @return Returns the string content of the specified resource
	 */
    public static String getResourceText(String name) {
        InputStream stream = ScriptingEngine.class.getResourceAsStream(name);
        return getStreamText(stream);
    }

    /**
	 * Extrac files into the named directory
	 * 
	 * @param zipFilePath
	 *            The path to the zip file
	 * @param destinationDirectory
	 *            The destination directory
	 * @param prefix
	 *            The prefix path of any files we wish to extract. Null if we want all files.
	 */
    public static void extractFiles(String zipFilePath, String destinationDirectory, String prefix) {
        if (zipFilePath == null || zipFilePath.length() == 0) {
            throw new IllegalArgumentException(Messages.FileUtilities_Zip_File_Path_Undefined);
        }
        if (destinationDirectory == null || destinationDirectory.length() == 0) {
            throw new IllegalArgumentException(Messages.FileUtilities_Destination_Directory_Undefined);
        }
        if (prefix == null || prefix.length() == 0) {
            throw new IllegalArgumentException(Messages.FileUtilities_Prefix_Undefined);
        }
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            Enumeration entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String entryName = entry.getName();
                if (entryName.startsWith(prefix)) {
                    entryName = entryName.substring(prefix.length());
                    String endPath = appendPaths(destinationDirectory, entryName);
                    if (entry.isDirectory()) {
                        File newDir = new File(endPath);
                        newDir.mkdir();
                    } else {
                        FileUtilities.copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(endPath)));
                        String message = StringUtils.format(Messages.FileUtilities_Extracted_File, new File(endPath).getName());
                        Trace.info(message);
                    }
                }
            }
            zipFile.close();
        } catch (Exception ex) {
            IdeLog.logError(ScriptingPlugin.getDefault(), Messages.FileUtilities_Error, ex);
        }
    }

    /**
	 * getCRC
	 * 
	 * @param file
	 * @return String
	 */
    public static String getCRC(File file) {
        DataInputStream input = null;
        String result = null;
        try {
            input = new DataInputStream(new FileInputStream(file));
            CRC32 crc = new CRC32();
            byte[] data = new byte[(int) file.length()];
            crc.update(input.read(data));
            result = String.valueOf(crc.getValue());
        } catch (FileNotFoundException e) {
            IdeLog.logError(ScriptingPlugin.getDefault(), Messages.FileUtilities_Error, e);
        } catch (IOException e) {
            IdeLog.logError(ScriptingPlugin.getDefault(), Messages.FileUtilities_Error, e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    IdeLog.logError(ScriptingPlugin.getDefault(), Messages.FileUtilities_Error, e);
                }
            }
        }
        return result;
    }

    /**
	 * getStreamText
	 * 
	 * @param stream
	 * @return String
	 */
    public static String getStreamText(InputStream stream) {
        try {
            StringWriter sw = new StringWriter();
            try {
                InputStreamReader isr = new InputStreamReader(stream);
                BufferedReader reader = new BufferedReader(isr);
                char[] buf = new char[1024];
                int numRead = reader.read(buf);
                while (numRead != -1) {
                    sw.write(buf, 0, numRead);
                    numRead = reader.read(buf);
                }
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
            return sw.toString();
        } catch (Exception e) {
            if (e instanceof InterruptedException == false) {
                IdeLog.logError(ScriptingPlugin.getDefault(), Messages.FileUtilities_Error, e);
            }
            return null;
        }
    }
}
