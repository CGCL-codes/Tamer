package pt.utl.ist.lucene.treceval.geotime.utiltasks;

import org.apache.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author Jorge Machado
 * @date 31/Dez/2009
 * @time 13:25:50
 * @email machadofisher@gmail.com
 */
public class MultiDocumentTaggerXinhuaDigmap2 implements Runnable {

    private static final Logger logger = Logger.getLogger(MultiDocumentTaggerXinhuaDigmap2.class);

    String path;

    String mode;

    String url = null;

    String startDocument = null;

    public MultiDocumentTaggerXinhuaDigmap2(String path, String mode, String url, String startDocument) {
        this.path = path;
        this.mode = mode;
        this.url = url;
        this.startDocument = startDocument;
    }

    public void run() {
        try {
            String[] args;
            if (url != null && startDocument == null) args = new String[] { path, mode, url }; else if (url == null && startDocument == null) args = new String[] { path, mode }; else if (url != null) args = new String[] { path, mode, url, startDocument }; else args = new String[] { path, mode, startDocument };
            DocumentTagger.main(args);
        } catch (IOException e) {
            logger.error(e, e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String path = "D:\\Servidores\\DATA\\ntcir\\NOVAS\\mainichidaly\\M2";
        String url = "http://digmap2.ist.utl.pt/jmachado/TIMEXTAG/index.php";
        args = new String[] { path, url, "mai1999.txt.gz", "EN-9905272A12DM313" };
        String startFile = null;
        String startDocument = null;
        if (args != null) {
            path = args[0];
            url = args[1];
            if (path.startsWith("\"")) path = path.substring(1, path.length() - 1);
            if (url.startsWith("\"")) url = url.substring(1, url.length() - 1);
        } else {
            logger.fatal("No arguments, use: path url startfile");
            return;
        }
        if (args.length > 2) {
            startFile = args[2];
            if (startFile.startsWith("\"")) startFile = startFile.substring(1, startFile.length() - 1);
        }
        if (args.length > 3) {
            startDocument = args[3];
            if (startDocument.startsWith("\"")) startDocument = startDocument.substring(1, startDocument.length() - 1);
        }
        File d = new File(path);
        File[] files = d.listFiles();
        Arrays.sort(files, new Comparator() {

            public int compare(final Object o1, final Object o2) {
                return ((File) o1).getName().compareTo(((File) o2).getName());
            }
        });
        for (File f : files) {
            if (f.isFile() && f.getName().endsWith(".gz")) {
                if (startFile == null || f.getName().compareTo(startFile) >= 0) {
                    System.out.println("STARTING WITH FILE: " + f.getName());
                    startFile = null;
                    MultiDocumentTaggerXinhuaDigmap2 multiDocumentIterator = new MultiDocumentTaggerXinhuaDigmap2(f.getAbsolutePath(), "timextag", url, startDocument);
                    startDocument = null;
                    multiDocumentIterator.run();
                } else System.out.println("SKIPING FILE: " + f.getName());
            }
        }
    }

    public static List<String> failIds = null;
}
