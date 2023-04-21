package org.jrazdacha.model;

import static org.junit.Assert.*;
import org.jrazdacha.dao.creator.ImageCreator;
import org.jrazdacha.dao.creator.TorrentCreator;
import org.junit.Test;

public class TorrentTest {

    private static final String TEST_IMAGE_PATH = "/Test/folder/test.jpg";

    @Test
    public void testAddImage() {
        Torrent torrent = TorrentCreator.createTorrent();
        Image firstImage = ImageCreator.createImage();
        Image secondImage = ImageCreator.createImage();
        secondImage.setPathToFile(TEST_IMAGE_PATH);
        torrent.addImage(firstImage);
        torrent.addImage(secondImage);
        assertEquals(2, torrent.getImages().size());
    }
}
