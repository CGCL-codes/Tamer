package org.s3b.search.indexing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.s3b.search.config.ConfigKeeper;
import org.jeromedl.beans.ContextKeeper;
import org.jeromedl.stringdict.Statics;

/**
 * FileEngine
 * 
 * @author Mariusz Cygan, Sebastian Ryszard Kruk
 */
public class FileEngine {

    private FileEngine() {
    }

    /**
	 * Gets a file
	 *
	 * @param uri
	 * @return File
	 */
    public static File getFile(String uri) {
        File file = null;
        if (uri.startsWith(Statics.FILE_1)) {
            file = new File(ConfigKeeper.getStoragePath() + uri.substring(Statics.FILE_1.length()));
        } else {
            file = new File(ContextKeeper.getInstallPath() + uri);
        }
        return file;
    }

    /**
	 * Loads resource with given resource uri
	 *
	 * @param uri
	 * @return String
	 */
    public static String getDocument(String uri) {
        File file = getFile(uri);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (reader != null) {
            try {
                while (reader.ready()) {
                    stringBuilder.append(reader.readLine());
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
}
