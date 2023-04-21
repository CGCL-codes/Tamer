package com.redoddity.faml.tests;

import com.redoddity.faml.model.Album;
import com.redoddity.faml.model.Tracks;
import com.redoddity.faml.model.daos.AlbumDAO;
import junit.framework.TestCase;

public class AlbumDAOTest extends TestCase {

    public void testAddAlbum() {
        Album album = new Album();
        Tracks[] tracks = new Tracks[2];
        album.setID(1);
        album.setTitle("Back in Black");
        album.setID(1980);
        album.setTracks(tracks);
        AlbumDAO albumDAO = new AlbumDAO();
        Album a = albumDAO.addAlbum(album);
        assertEquals(a.equals(album), true);
    }
}
