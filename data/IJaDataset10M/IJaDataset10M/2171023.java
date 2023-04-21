package edu.whitman.halfway.jigs;

/** Like a filefilter, decides wether or not to accept the given
    picture for inclusion of an album.  

    accept and hasOpinion should return false on all non-mediaItems.

*/
public interface MediaItemFilter extends AlbumObjectFilter {
}
