package com.etymgiko.lyricscollectionseam.session;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import com.etymgiko.lyricscollectionseam.entity.Artist;

/**
 * @author Ivan Holub
 */
@Name("artistList")
public class ArtistList extends EntityQuery<Artist> {

    private static final String EJBQL = "select artist from Artist artist";

    private Artist artist = new Artist();

    public ArtistList() {
        setEjbql(EJBQL);
    }

    public Artist getArtist() {
        return artist;
    }
}
