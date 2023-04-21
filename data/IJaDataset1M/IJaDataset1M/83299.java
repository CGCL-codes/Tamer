package com.germinus.portlet.scribmage.model;

import java.util.List;
import com.germinus.util.ListPage;
import com.germinus.util.PaginationData;

public class ScribmageResults {

    public static final String FLICKR_GALLERY = "flickrGallery";

    public static final String FILE_DIRECTORY_GALLERY = "fileDirectoryGallery";

    private List<? extends ScribmagePhoto> photos;

    private PaginationData paginationData;

    private String currentGallery;

    private GalleryClass galleryClass = GalleryClass.fd;

    private String viewMode;

    public enum GalleryClass {

        fd, flickr;

        public static GalleryClass galleryClassFromGalleryType(String galleryType) {
            return FILE_DIRECTORY_GALLERY.equals(galleryType) ? fd : flickr;
        }
    }

    public ScribmageResults() {
    }

    public ScribmageResults(ListPage<? extends ScribmagePhoto> scribmagePhotoPage, String viewMode, String galleryType) {
        this.paginationData = scribmagePhotoPage.getPaginationData();
        this.photos = scribmagePhotoPage.getPageElements();
        this.currentGallery = galleryType;
        this.galleryClass = GalleryClass.galleryClassFromGalleryType(galleryType);
        this.viewMode = viewMode;
    }

    public boolean isEmpty() {
        return photos == null;
    }

    public PaginationData getPaginationData() {
        return paginationData;
    }

    public List<? extends ScribmagePhoto> getPhotos() {
        return photos;
    }

    public String getCurrentGallery() {
        return currentGallery;
    }

    public String getGalleryClass() {
        return galleryClass.toString();
    }

    public String getViewMode() {
        return viewMode;
    }
}
