package jragonsoft.javautil.cmdtool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.FileUtils;

/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: WebGet.java 4 2006-03-16 15:27:19Z zemian $
 */
public class WebGet {

    /** Description of the Method */
    public static void printExitHelp() {
        String help = "USAGE:" + "\n  WebGet [options] Url [Url ...]" + "\n  A simple download utitlity to get files from web. It downloads file" + "\n  given by Url's and save it in current directory." + "\n" + "\n  [options]" + "\n    -h      Help and version." + "\n    -fFILE  Change destination file location and name." + "\n" + "\nEXAMPLES:" + "\n  $ # Assume using wrapper scripts and PATH is set" + "\n  $ webget http://jakarta.apache.org/index.html" + "\n  $ webget -f/tmp/jakarta_index.html http://jakarta.apache.org/index.html" + "\n  $ webget http://apache.seekmeup.com/jakarta/commons/cli/binaries/cli-1.0.zip" + "\n  $ webget ftp://www.ibiblio.org/pub/mirrors/apache/jakarta/commons/cli/binaries/cli-1.0.zip" + "\n" + "\nCREDITS:" + "\n  ZMan Java Utility. <zemiandeng@gmail.com>" + "\n  $Id: WebGet.java 4 2006-03-16 15:27:19Z zemian $";
        System.out.println(help);
        System.exit(0);
    }

    /**
	 * The main program for the WebGet class
	 * 
	 * @param args
	 *            The command line arguments
	 */
    public static void main(String[] args) {
        GetOpt opt = new GetOpt(args);
        if (opt.isOpt("h") || opt.getArgsCount() < 1) {
            printExitHelp();
        }
        String filename = opt.getOpt("f", null);
        for (int i = 0; i < opt.getArgsCount(); i++) {
            try {
                if (filename != null && !new File(filename).isDirectory()) webget(opt.getArg(i), FileUtils.appendBasename(filename, "_" + i)); else if (filename != null && new File(filename).isDirectory()) webget(opt.getArg(i), filename); else webget(opt.getArg(i), null);
            } catch (Exception e) {
                System.err.println("[ERROR] " + "Failed to get file.");
                e.printStackTrace(System.err);
            }
        }
    }

    public static File webget(String urlString, String filename) throws MalformedURLException, IOException, FileNotFoundException {
        InputStream in = null;
        FileOutputStream out = null;
        URL url = new URL(urlString);
        File destFile = null;
        if (filename == null) {
            String inputStr = url.getFile();
            destFile = new File(inputStr.replaceAll(".*[\\/](.*)$", "$1"));
        } else if (new File(filename).isDirectory()) {
            String inputStr = url.getFile();
            destFile = new File(filename + "/" + inputStr.replaceAll(".*[\\/](.*)$", "$1"));
        } else {
            destFile = new File(filename);
        }
        URLConnection conn = url.openConnection();
        long len = conn.getContentLength();
        String contentLen;
        if (len < 0) {
            contentLen = "Unknown Length";
        } else {
            contentLen = len + " byte" + ((len > 1) ? "s" : "");
        }
        System.out.println("Downloading " + destFile.getName() + " " + contentLen);
        in = url.openStream();
        out = new FileOutputStream(destFile);
        copyStream(in, out, len);
        System.out.println("Download completed.");
        return destFile;
    }

    /** copy while display a progress bar */
    public static void copyStream(InputStream inputstream, OutputStream outstream, long maxLen) {
        long downloadLen = 0;
        try {
            BufferedInputStream in = new BufferedInputStream(inputstream);
            BufferedOutputStream out = new BufferedOutputStream(outstream);
            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = in.read(buf, 0, 1024)) != -1) {
                out.write(buf, 0, len);
                downloadLen += len;
                if (maxLen < 0) {
                    System.out.println("Downloading " + downloadLen + " bytes");
                } else {
                    System.out.print("\r");
                    System.out.print("Downloading " + downloadLen + "/" + maxLen + " bytes ");
                    System.out.print("[" + getProgress(downloadLen, maxLen, 20) + "]");
                }
            }
            out.flush();
            System.out.println();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProgress(long current, long max, int numStep) {
        StringBuffer sb = new StringBuffer();
        int progress = (int) ((current / (double) max) * numStep);
        for (int i = 0; i < progress; i++) {
            sb.append(".");
        }
        for (int i = progress; i < numStep; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
