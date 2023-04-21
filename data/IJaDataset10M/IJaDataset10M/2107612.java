package test.it.hotel.model.photo.manager;

import it.hotel.model.gallery.Gallery;
import it.hotel.model.gallery.manager.IGalleryManager;
import it.hotel.model.hotel.manager.IHotelManager;
import it.hotel.model.photo.Photo;
import it.hotel.model.photo.manager.IPhotoManager;
import java.util.Collection;
import test.it.hotel.model.abstrakt.BaseTestCase;

public class TestPhotoManager extends BaseTestCase {

    protected IPhotoManager photoRawManager;

    protected IGalleryManager galleryRawManager;

    protected IHotelManager hotelRawManager;

    public Photo setUpPhotos() {
        Photo photo = new Photo();
        Gallery gallery = new Gallery();
        galleryRawManager.add(gallery);
        photo.setDescription("description");
        photo.setGallery(gallery);
        return photo;
    }

    public void testDeletePhoto() {
        Photo photo = setUpPhotos();
        Gallery gallery = new Gallery();
        galleryRawManager.add(gallery);
        photo.setGallery(gallery);
        gallery.addNewPhoto(photo);
        photoRawManager.add(photo);
        Collection<Photo> photos = photoRawManager.getAll();
        assertNotNull(photos);
        gallery.deletePhoto(photo);
        photoRawManager.remove(photo.getId());
        photos = photoRawManager.getAll();
        assertTrue(photos.isEmpty());
    }

    public void testGetAllPhotos() {
        Photo photo = setUpPhotos();
        Gallery gallery = new Gallery();
        galleryRawManager.add(gallery);
        photo.setGallery(gallery);
        gallery.addNewPhoto(photo);
        photoRawManager.add(photo);
        Collection<Photo> photosByGallery = gallery.getPhotos();
        Collection<Photo> photos = photoRawManager.getAll();
        assertTrue(photos.contains(photo));
        assertTrue(photos.size() == 1);
        assertTrue(gallery.getPhotos().size() == 1);
        assertTrue(gallery.getPhotos().contains(photo));
        assertEquals(photos.size(), photosByGallery.size());
        assertTrue(gallery.getPhotos().contains(photo));
    }

    public void testAddPhoto() {
        Photo photo = setUpPhotos();
        photoRawManager.add(photo);
        Photo photo2 = (Photo) photoRawManager.get(photo.getId());
        assertEquals(photo, photo2);
    }
}
