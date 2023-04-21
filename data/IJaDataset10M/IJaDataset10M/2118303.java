package edu.whitman.halfway.jigs;

/** An interface implemented by a class that can do some operation on
    a Picture.  Used as a callback by AlbumUtil.recursivePictureWalk() 
    
    That is, if you want to do something that operates on every Picture
    in an Album hierarchy, then implement this interface and pass
    yourself into recursivePictureWalk as the AlbumFunction.

*/
public interface MediaItemFunction {

    /** This will be called on every album in the hierarchy from
        recursiveAlbumWalk */
    public void process(MediaItem pic);
}
