package hubsniffer.sghdc.file.util;

import hubsniffer.sghdc.file.io.FileBufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Iterator;
import java.io.File;

public class FileIterator implements Iterator<libjdc.dc.filelist.File> {

    private FileBufferedReader br = null;

    private libjdc.dc.filelist.File nextLine = null;

    public FileIterator(File file) throws IOException, ParseException {
        br = new FileBufferedReader(new InputStreamReader(new FileInputStream(file)));
        nextLine = br.readFile();
    }

    public FileIterator(String filename) throws IOException, ParseException {
        this(new File(filename));
    }

    public boolean hasNext() {
        try {
            return br.ready() || nextLine != null;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("asdsad");
        return nextLine != null;
    }

    public libjdc.dc.filelist.File next() {
        libjdc.dc.filelist.File answer = nextLine;
        try {
            if (!br.ready()) {
                nextLine = null;
            } else {
                nextLine = br.readFile();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return answer;
    }

    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported");
    }

    public static void main(String argv[]) throws IOException, ParseException {
        FileIterator fi = new FileIterator("/home/dbotelho/Filelist.txt");
        long i = 0;
        while (fi.hasNext()) {
            i++;
            System.out.println(i);
            fi.next();
        }
        System.out.println("num docs=" + i);
    }
}
