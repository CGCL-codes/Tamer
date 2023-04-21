package vi.crawl.uniteCrawler.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import net.htmlparser.jericho.HTMLElementName;
import vi.Repository;

/**
 * Repository which download given documents and save them on disk.<br>
 * This repository saves web sites to repository,<br>
 * others, downloadable objects to same repository by it's URL and category.
 * 
 * @author Ondrej Buch
 * @author adam.mihalik
 * 
 */
public class FileRepository extends Repository {

    private String targetDirectory;

    private static final String DOWNLOAD_DIRECTORY = "C:\\download";

    private static final String OBJ_REPOSITORY = "Assigned Objects";

    private static final String HTML_FILE_EXTENSION = ".htm";

    public FileRepository() {
        this(null);
    }

    public FileRepository(String targetDirectory) {
        if (targetDirectory == null) {
            this.targetDirectory = DOWNLOAD_DIRECTORY + File.separator;
        } else {
            this.targetDirectory = targetDirectory;
        }
    }

    public String saveFile(URL url) {
        String newUrlToReturn = url.toString();
        try {
            String directory = Util.appendDirPath(targetDirectory, OBJ_REPOSITORY);
            String category = url.openConnection().getContentType();
            category = category.substring(0, category.indexOf("/"));
            String fileUrl = Util.transformUrlToPath(url.toString());
            directory = Util.appendDirPath(directory, category);
            directory = Util.appendDirPath(directory, fileUrl);
            String objectFileName = url.toString().substring(url.toString().lastIndexOf('/') + 1);
            BufferedInputStream in = new java.io.BufferedInputStream(url.openStream());
            File dir = new File(directory);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(Util.appendDirPath(dir.getPath(), objectFileName));
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                bout.write(data, 0, count);
            }
            bout.close();
            fos.close();
            in.close();
            newUrlToReturn = Util.getRelativePath(file.getAbsolutePath(), targetDirectory);
        } catch (IOException e) {
            return newUrlToReturn;
        }
        return newUrlToReturn;
    }

    public String savePage(String docCode, String docLink) {
        String newUrlToReturn = docLink;
        File dir = new File(targetDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String title = Util.getHTMLPart(docCode, HTMLElementName.TITLE);
        if (title == null || title.length() == 0) {
            title = docLink;
        }
        title = Util.normalizeUrlForFileName(title) + HTML_FILE_EXTENSION;
        File page = new File(Util.appendDirPath(targetDirectory, title));
        try {
            Writer out = new OutputStreamWriter(new FileOutputStream(page), Util.getHTMLencoding(docCode));
            out.write(docCode);
            out.close();
            newUrlToReturn = Util.getRelativePath(page.getAbsolutePath(), targetDirectory);
        } catch (IOException e) {
            return newUrlToReturn;
        }
        return newUrlToReturn;
    }

    public String getFileName(URL url) {
        String file = url.getFile();
        int lastSlash = file.lastIndexOf("/");
        int lastDot = file.lastIndexOf(".");
        if (lastSlash < lastDot & lastSlash != -1 & lastDot != -1) {
            return file.substring(lastSlash + 1, file.length());
        } else {
            return null;
        }
    }

    public String preparePageFile(String docCode, String docLink) {
        String title = Util.getHTMLPart(docCode, HTMLElementName.TITLE);
        if (title == null || title.length() == 0) {
            title = docLink;
        }
        title = Util.normalizeUrlForFileName(title) + HTML_FILE_EXTENSION;
        String absolutePath = Util.appendDirPath(targetDirectory, title);
        return Util.getRelativePath(absolutePath, targetDirectory);
    }

    public String getExistedPagePath(String title) {
        String absolutePath = Util.appendDirPath(targetDirectory, Util.normalizeUrlForFileName(title) + HTML_FILE_EXTENSION);
        File checkedFile = new File(absolutePath);
        if (checkedFile.exists()) {
            return Util.getRelativePath(absolutePath, targetDirectory);
        }
        return null;
    }
}
