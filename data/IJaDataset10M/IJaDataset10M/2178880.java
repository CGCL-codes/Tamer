package com.netexplode.jtunes.model.media;

/**
 * <code>Tag</code> represents .....
 * 
 * @author ykim
 * @version $Revision: 1.2 $
 * @since 0.1
 */
public interface Tag {

    String getAlbum();

    String getArtist();

    String getComment();

    String getGenre();

    String getTitle();

    int getTotalTracks();

    int getYear();

    int getTrackNumber();
}
