package org.psw.manager.common;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import android.content.Context;

/**
 * Utility class for file operations.
 * @author Roberts Vartins
 */
public class FileUtils {

    public static boolean isFileCreated(Context context) {
        String[] filenames = context.fileList();
        for (String name : filenames) {
            if (name.equals(Constants.DATA_FILE)) {
                return true;
            }
        }
        return false;
    }

    public static void write(String contentToWrite, Context context) {
        OutputStream outputStream;
        Writer writer;
        try {
            outputStream = context.openFileOutput(Constants.DATA_FILE, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(outputStream, Constants.ENCODING);
            writer.write(contentToWrite);
            writer.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String read(Context context) throws RuntimeException {
        FileInputStream inputStream;
        try {
            inputStream = context.openFileInput(Constants.DATA_FILE);
            FileChannel fc = inputStream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            String fileContent = Charset.forName(Constants.ENCODING).decode(bb).toString();
            inputStream.close();
            return fileContent;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
