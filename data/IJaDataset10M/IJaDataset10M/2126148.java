package test.it.hotel.model.gallery;

import it.hotel.model.gallery.Gallery;
import it.hotel.model.photo.Photo;
import java.util.Collection;
import test.it.hotel.model.abstrakt.BaseTestCase;

public class TestGallery extends BaseTestCase {

    private Gallery gallery;

    public void setUpGallery() {
        gallery = new Gallery();
    }

    public void testGallery() throws Exception {
        setUpGallery();
        int id = gallery.getId();
        Collection<Photo> photos = gallery.getPhotos();
        assertTrue(photos.isEmpty());
        assertTrue(id == 0);
    }
}
