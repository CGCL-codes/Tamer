package plasmid.fileWorker;

import java.io.File;

public class Walker {

    private Visitor visitor;

    static int filesVisited;

    static int dirsVisited;

    public Walker(Visitor v) {
        visitor = v;
    }

    /**
     * Walk the specified subtree, invoking visitor on each node walked.
     * Returns true if completes. Returns false if interrupted before completion.
     **/
    public boolean pathWalk(String path) {
        File f = new File(path);
        if (f == null) {
            return false;
        }
        try {
            fileWalk(f);
        } catch (InterruptedException ie) {
            return false;
        }
        return true;
    }

    public boolean fileWalk(File f) throws InterruptedException {
        visitor.preVisit(f);
        if (f.isDirectory()) {
            dirsVisited++;
            String[] entries = f.list();
            for (int i = 0; i < entries.length; i++) {
                File subf = new File(f, entries[i]);
                if (subf == null) continue;
                fileWalk(subf);
            }
            entries = null;
        } else {
            filesVisited++;
        }
        visitor.postVisit(f);
        return true;
    }
}
