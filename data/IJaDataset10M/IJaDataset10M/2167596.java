package rpg.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * rothens.tarhely.biz
 * @author k-zed
 */
public class LineReader implements Iterator<String> {

    private BufferedReader br = null;

    private String nextLine = null;

    private String path = null;

    private String loadLine() throws IOException {
        nextLine = null;
        for (; ; ) {
            String l = br.readLine();
            if (l == null) {
                br.close();
                br = null;
                return l;
            }
            l = l.trim();
            if (l.length() == 0) {
                continue;
            }
            if (l.charAt(0) == '#') {
                continue;
            }
            nextLine = l;
            return l;
        }
    }

    public LineReader(File path) {
        try {
            this.path = path.getAbsolutePath();
            br = new BufferedReader(new FileReader(path.getPath()));
        } catch (IOException ex) {
            System.err.println("[LineReader] Exception while reading file " + path.getPath());
            br = null;
        }
    }

    public boolean hasNext() {
        if (br == null) {
            return false;
        }
        try {
            if (nextLine == null) {
                loadLine();
            }
        } catch (IOException ex) {
            System.err.println("[LineReader] Exception");
            br = null;
            nextLine = null;
            return false;
        }
        return nextLine != null;
    }

    public String next() {
        if (br == null) {
            throw new NoSuchElementException();
        }
        try {
            if (nextLine == null) {
                loadLine();
            }
        } catch (IOException ex) {
            System.err.println("[LineReader] Exception while reading file " + path);
            br = null;
            nextLine = null;
            throw new NoSuchElementException();
        }
        if (nextLine == null) {
            throw new NoSuchElementException();
        }
        String line = nextLine;
        nextLine = null;
        return line;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
