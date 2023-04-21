package net.sf.picasto.uploader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.picasto.Publisher;

/**
 * @author bauerb
 *
 */
public class FileUploader extends DummyUploader {

    public FileUploader(Publisher publisher) {
        super(publisher);
    }

    public String uploadStream(InputStream stream, String directory, String name, boolean overwrite) throws IOException {
        String outputDirName = settings.getProperty("OutputDirectory", System.getProperty("user.dir"));
        File outputDir = new File(new File(outputDirName), directory);
        if (!overwrite && outputDir.exists()) {
            if (!outputDir.isDirectory()) {
                throw new IOException(outputDir + " occupied by file");
            }
        } else {
            if (!outputDir.mkdirs()) {
                throw new IOException("Couldn't create path " + outputDir);
            }
        }
        int i = 0;
        String extension = null;
        Matcher matcher = Pattern.compile("^(.*)\\.([^.]*)$").matcher(name);
        if (matcher.matches()) {
            name = matcher.group(1);
            extension = matcher.group(2);
        }
        String tryName = name + ((extension == null) ? "" : "." + extension);
        if (!overwrite) {
            while (new File(outputDir, tryName).exists()) {
                i++;
                tryName = name + "_" + i + ((extension == null) ? "" : "." + extension);
            }
        }
        File targetFile = new File(outputDir, tryName);
        String publishedURI = publishedURI(directory, targetFile.getName());
        mapping().put(publishedURI, targetFile);
        copyStream(stream, new FileOutputStream(targetFile));
        return publishedURI;
    }

    public void removeUploadedFile(String uri) throws IOException {
        File uploadedFile = (File) mapping().remove(uri);
        if (uploadedFile != null) {
            uploadedFile.delete();
        }
    }
}
