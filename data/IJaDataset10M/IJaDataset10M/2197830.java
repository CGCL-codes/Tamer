package shu.util.source;

import java.io.*;
import Model.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: </p>
 * �p��{���X�����
 *
 * <p>Copyright: Copyright (c) 2001</p>
 *
 * <p>Company: </p>
 *
 * @author cms.shu.edu.tw
 * @version 1.0
 */
public class SourceLinesCounter {

    protected boolean countByloc = true;

    /**
   *
   * @param countByloc boolean �O�_�n�ư��ťո���
   */
    public SourceLinesCounter(boolean countByloc) {
        this.countByloc = countByloc;
    }

    public SourceLinesCounter() {
        this(true);
    }

    public static void main(String[] args) {
        String[] countDirs = new String[] { "D:/WorkSpace/CMSJava2/" };
        SourceLinesCounter counter = new SourceLinesCounter();
        for (String dir : countDirs) {
            counter.parse(new File(dir));
        }
        System.out.println();
        System.out.println(counter.files + " files");
        System.out.println(counter.lines + " lines");
        System.exit(0);
    }

    private static String[] extFilenameList = { ".java", ".frm", "bas" };

    private boolean endWithSpecificExtFilename(String filename) {
        for (String specific : extFilenameList) {
            if (filename.endsWith(specific)) {
                return true;
            }
        }
        return false;
    }

    public void parse(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int x = 0; x < files.length; x++) {
                parse(files[x]);
            }
        } else if (endWithSpecificExtFilename(file.getName()) && file.getParent().indexOf("sandbox") == -1) {
            files++;
            if (countByloc) {
                countLinesByloc(file, false);
            } else {
                countLines(file);
            }
            System.out.print(".");
        }
    }

    public int files;

    public int lines;

    protected LOCcounter counter = new LOCcounter();

    public void countLines(File file) {
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(file));
            while (reader.readLine() != null) {
            }
            lines += reader.getLineNumber();
            reader.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void countLinesByloc(File file, boolean countComments) {
        counter.setCommentState(countComments);
        lines += counter.getLinesInFile(file.getAbsolutePath());
    }
}
