package com.web.music.impl;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.web.music.BaseTest;
import com.web.music.model.Album;
import com.web.music.model.Artist;
import com.web.music.model.Person;
import com.web.music.model.Singer;
import com.web.music.model.Track;

public class PersistenceHelperImplTest1 extends BaseTest {

    PersistenceHelperImpl helper;

    Person testPerson = new Person(20L, "Paul", "McCartney");

    Artist testSinger = new Singer(33L, "Pseudonym", "attribute", testPerson, null);

    Track testTrack1 = new Track("testTrack#1", 1, "path to mp3 #1");

    Track testTrack2 = new Track("testTrack#2", 2, "path to mp3 #2");

    List<Track> testTracks = Arrays.asList(new Track[] { testTrack1, testTrack2 });

    Album testAlbum = new Album(18L, "The White Album", "", testSinger, null, null);

    @BeforeClass
    public static void setUp() throws Exception {
        System.out.println("Before the Test.");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        persistenceHelper.deleteEverything();
        System.out.println("After the Test.");
    }

    @Test
    public void testSave() {
        persistenceHelper.save(testPerson);
        persistenceHelper.save(testSinger);
        persistenceHelper.save(testTrack1);
        persistenceHelper.saveAll(testTracks);
        persistenceHelper.save(testAlbum);
    }

    @Test
    public void testGetById() {
        Album getTestAlbum = (Album) persistenceHelper.getById(Album.class, 18L);
        Person getTestPerson = (Person) persistenceHelper.getById(Person.class, 20L);
        Artist getTestArtist = (Artist) persistenceHelper.getById(Artist.class, 33L);
        assertEquals(testAlbum, getTestAlbum);
        assertEquals(testPerson, getTestPerson);
        assertEquals(testSinger, getTestArtist);
    }
}
