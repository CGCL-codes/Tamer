package de.iritgo.aktera.tools;

import de.iritgo.aktera.comm.BinaryWrapper;
import de.iritgo.aktera.model.ModelRequest;
import de.iritgo.simplelife.string.StringTools;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Useful file methods.
 */
public final class FileTools {

    /**
	 * Store an uploaded audio file. The file will be converted to wav format.
	 *
	 * @param req The model request.
	 * @param paramName Request paramter name.
	 * @param dir Directory where to store the uploaded file.
	 * @param extensions Allowed file extension
	 */
    public static File storeUploadedAudioFile(ModelRequest req, String paramName, String dir, String extensions) {
        return storeUploadedAudioFile(req, paramName, dir, extensions, true, true);
    }

    /**
	 * Store an uploaded audio file. The file will be converted to wav format.
	 *
	 * @param req The model request.
	 * @param paramName Request paramter name.
	 * @param dir Directory where to store the uploaded file.
	 * @param extensions Allowed file extension
	 * @param useDataFileName If true, the file name specified in the binary data is used as the
	 *   actual file name. If false, dir must specify the complete file path.
	 * @param boolean If true we convert the file.
	 */
    public static File storeUploadedAudioFile(ModelRequest req, String paramName, String dir, String extensions, boolean useDataFileName) {
        return storeUploadedAudioFile(req, paramName, dir, extensions, useDataFileName, true);
    }

    /**
	 * Store an uploaded audio file. The file will be converted to wav format.
	 *
	 * @param req The model request.
	 * @param paramName Request paramter name.
	 * @param dir Directory where to store the uploaded file.
	 * @param extensions Allowed file extension
	 * @param useDataFileName If true, the file name specified in the binary data is used as the
	 *   actual file name. If false, dir must specify the complete file path.
	 * @param convert If true we convert the file.
	 * @return The created file
	 */
    public static File storeUploadedAudioFile(ModelRequest req, String paramName, String dir, String extensions, boolean useDataFileName, boolean convert) {
        BinaryWrapper data = (BinaryWrapper) req.getParameter(paramName);
        if (data == null) {
            return null;
        }
        String fileName = FilenameUtils.getBaseName(data.getName()).replaceAll("\\s", "-");
        String fileExt = FilenameUtils.getExtension(data.getName()).toLowerCase();
        boolean ok = false;
        String[] exts = extensions.split("\\|");
        for (int i = 0; i < exts.length; ++i) {
            if (fileExt.equals(exts[i])) {
                ok = true;
                break;
            }
        }
        if (ok) {
            File outFile = FileTools.newAkteraFile(dir);
            outFile.mkdirs();
            if (useDataFileName) {
                outFile = FileTools.newAkteraFile(dir, fileName + "." + fileExt);
            }
            try {
                outFile.delete();
                outFile.createNewFile();
                data.write(outFile);
            } catch (IOException x) {
                System.out.println("[FileTools.storeUploadedAudioFile] Error: " + x);
            }
            try {
                String filePathNoExt = outFile.getCanonicalPath();
                filePathNoExt = filePathNoExt.substring(0, filePathNoExt.lastIndexOf('.'));
                if (convert) {
                    if (fileExt.equals("mp3")) {
                        SystemTools.startAndWaitAkteraProcess("/usr/bin/id3convert", "-s " + outFile.getCanonicalPath());
                    } else if (fileExt.equals("gsm")) {
                        SystemTools.startAndWaitAkteraProcess("/usr/bin/sox", filePathNoExt + ".gsm" + " -r 8000 -c 1 -s -w " + filePathNoExt + ".wav");
                        SystemTools.startAndWaitAkteraProcess("/usr/bin/rm", filePathNoExt + ".gsm");
                    } else if (fileExt.equals("wav")) {
                        SystemTools.startAndWaitAkteraProcess("/usr/bin/sox", filePathNoExt + ".wav" + " -r 8000 -c 1 -s -w " + filePathNoExt + "-tmp.wav");
                        SystemTools.startAndWaitAkteraProcess("/usr/bin/mv", filePathNoExt + "-tmp.wav " + filePathNoExt + ".wav");
                    }
                }
            } catch (IOException x) {
                System.out.println("[FileTools.storeUploadedAudioFile] Error: " + x);
            }
            return outFile;
        }
        return null;
    }

    /**
	 * Get Filename
	 *
	 * @param req The model request.
	 * @param paramName Request paramter name.
	 */
    public static String getFilename(ModelRequest req, String paramName) {
        BinaryWrapper data = (BinaryWrapper) req.getParameter(paramName);
        if (data == null) {
            return "";
        }
        return data.getName().replaceAll("\\s", "-");
    }

    /**
	 * Create a File object that is located relative to the path stored in the
	 * AKTERA_FS_ROOT environment variable.
	 *
	 * @param path The file path
	 * @return The File object
	 */
    public static File newAkteraFile(String path) {
        return new File(new File(StringTools.trim(System.getenv("AKTERA_FS_ROOT"))), path);
    }

    /**
	 * Create a File object that is located relative to the path stored in the
	 * AKTERA_FS_ROOT environment variable.
	 *
	 * @param parent The parent directory path
	 * @param child The file path
	 * @return The File object
	 */
    public static File newAkteraFile(String parent, String child) {
        return new File(new File(StringTools.trim(System.getenv("AKTERA_FS_ROOT")), parent), child);
    }

    /**
	 * Return the file owner (The system env. variable "AKTERA_FS_OWNER" if set,
	 * "root:tomcat" else).
	 *
	 * @return The unix owner format e.g. "root:tomcat"
	 */
    public static String getAkteraFileOwner() {
        String owner = System.getenv("AKTERA_FS_OWNER");
        return !StringTools.isTrimEmpty(owner) ? owner : "root:tomcat";
    }

    /**
	 * Copy a file.
	 *
	 * @param src The source file
	 * @param dst The destination file
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
    public static void copyAkteraFile(String src, String dst) throws FileNotFoundException, IOException {
        IOUtils.copy(new FileInputStream(newAkteraFile(src)), new FileOutputStream(newAkteraFile(dst)));
    }
}
