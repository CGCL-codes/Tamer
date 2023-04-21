package edu.whitman.halfway.jigs.extras.brendan;

import edu.whitman.halfway.jigs.*;

/**
   Default filter for albums that excludes  hidden files, dot
   files (like .pics on a unix system), and empty albums.
*/
public class MyFilt extends MultiFilter {

    public MyFilt() {
        super(new MyAlbFilt(), new MyPicFilt());
    }
}
