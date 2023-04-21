package es.alvsanand.webpage.services.admin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import org.apache.commons.lang.StringUtils;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.media.MediaByteArraySource;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.PhotoFeed;
import com.google.gdata.data.photos.UserFeed;
import com.google.gdata.util.AuthenticationException;
import es.alvsanand.webpage.common.AlvsanandProperties;
import es.alvsanand.webpage.common.Globals;
import es.alvsanand.webpage.common.Logger;
import es.alvsanand.webpage.common.photo.Album;
import es.alvsanand.webpage.common.photo.Photo;
import es.alvsanand.webpage.services.ServiceException;

/**
 * This class implements the service Tag
 * 
 * @author alvaro.santos
 * @date 02/12/2010
 * 
 */
public class ImageAdminServiceImpl implements ImageAdminService {

    private static final Logger logger = new Logger(ImageAdminServiceImpl.class);

    private static final String PICASA_FEED_URL = "https://picasaweb.google.com/data/feed/api/user/{0}";

    private static final String PICASA_ENTRY_URL = "https://picasaweb.google.com/data/entry/api/user/{0}";

    private static final String PICASA_GET_ALBUMS_FEED_URL = PICASA_FEED_URL + "?kind=album";

    private static final String PICASA_ALBUM_FEED_URL = PICASA_FEED_URL + "/albumid/{1}";

    private static final String PICASA_PHOTO_FEED_URL = PICASA_ALBUM_FEED_URL + "/photoid/{2}";

    private static final String PICASA_ALBUM_ENTRY_URL = PICASA_ENTRY_URL + "/albumid/{1}";

    private static final String PICASA_PHOTO_ENTRY_URL = PICASA_ENTRY_URL + "/photoid/{2}";

    private PicasawebService picasawebService;

    private ImagesService imagesService = ImagesServiceFactory.getImagesService();

    private boolean logged = false;

    private void login() throws ServiceException {
        logger.debug("Logging to picasa");
        try {
            getPicasawebService().setUserCredentials(AlvsanandProperties.getProperty(Globals.PICASA_USER_CONFIG_KEY), AlvsanandProperties.getProperty(Globals.PICASA_PASS_CONFIG_KEY));
            logged = true;
        } catch (AuthenticationException authenticationException) {
            logger.error("Error loggin to picasa");
            throw new ServiceException("Error removing erasing cache data", authenticationException);
        }
    }

    private URL getUserFeedUrl() throws com.google.gdata.util.ServiceException, MalformedURLException {
        return new URL(MessageFormat.format(PICASA_GET_ALBUMS_FEED_URL, AlvsanandProperties.getProperty(Globals.PICASA_USER_CONFIG_KEY)));
    }

    private UserFeed getUserFeed() throws IOException, com.google.gdata.util.ServiceException {
        URL userFeedUrl = getUserFeedUrl();
        return getPicasawebService().getFeed(userFeedUrl, UserFeed.class);
    }

    private URL getAlbumFeedUrl(String albumId) throws com.google.gdata.util.ServiceException, MalformedURLException {
        return new URL(MessageFormat.format(PICASA_ALBUM_FEED_URL, AlvsanandProperties.getProperty(Globals.PICASA_USER_CONFIG_KEY), albumId));
    }

    private URL getAlbumEntryUrl(String albumId) throws com.google.gdata.util.ServiceException, MalformedURLException {
        return new URL(MessageFormat.format(PICASA_ALBUM_ENTRY_URL, AlvsanandProperties.getProperty(Globals.PICASA_USER_CONFIG_KEY), albumId));
    }

    private AlbumFeed getAlbumFeed(String albumId) throws com.google.gdata.util.ServiceException, IOException {
        URL albumFeedUrl = getAlbumFeedUrl(albumId);
        return getPicasawebService().getFeed(albumFeedUrl, AlbumFeed.class);
    }

    private URL getPhotoFeedUrl(String albumId, String photoId) throws com.google.gdata.util.ServiceException, MalformedURLException {
        return new URL(MessageFormat.format(PICASA_PHOTO_FEED_URL, AlvsanandProperties.getProperty(Globals.PICASA_USER_CONFIG_KEY), albumId, photoId));
    }

    private URL getPhotoEntryUrl(String albumId, String photoId) throws com.google.gdata.util.ServiceException, MalformedURLException {
        return new URL(MessageFormat.format(PICASA_PHOTO_ENTRY_URL, AlvsanandProperties.getProperty(Globals.PICASA_USER_CONFIG_KEY), albumId, photoId));
    }

    private PhotoFeed getPhotoFeed(String albumId, String photoId) throws com.google.gdata.util.ServiceException, IOException {
        URL photoFeedUrl = getPhotoFeedUrl(albumId, photoId);
        return getPicasawebService().getFeed(photoFeedUrl, PhotoFeed.class);
    }

    public List<Album> getAlbums() throws ServiceException {
        try {
            logger.debug("Getting picasa albums");
            if (!logged) {
                login();
            }
            UserFeed userFeed = getUserFeed();
            List<AlbumEntry> albumEntries = userFeed.getAlbumEntries();
            List<Album> albums = new ArrayList<Album>();
            if (albumEntries != null) {
                for (AlbumEntry albumEntry : albumEntries) {
                    albums.add(new Album(albumEntry));
                }
            }
            return albums;
        } catch (MalformedURLException malformedURLException) {
            logger.error("Error getting picasa albums", malformedURLException);
            throw new ServiceException("Error getting picasa albums", malformedURLException);
        } catch (IOException ioException) {
            logger.error("Error getting picasa albums", ioException);
            throw new ServiceException("Error getting picasa albums", ioException);
        } catch (com.google.gdata.util.ServiceException serviceException) {
            logger.error("Error getting picasa albums", serviceException);
            throw new ServiceException("Error getting picasa albums", serviceException);
        }
    }

    public Album getAlbum(Album album) throws ServiceException {
        if (album == null || StringUtils.isEmpty(album.getId())) {
            throw new IllegalArgumentException();
        }
        return getAlbum(album.getId());
    }

    public Album getAlbum(String albumId) throws ServiceException {
        try {
            if (StringUtils.isEmpty(albumId)) {
                throw new IllegalArgumentException();
            }
            logger.debug("Getting picasa album[" + albumId + "]");
            return new Album(getAlbumEntry(albumId));
        } catch (MalformedURLException malformedURLException) {
            logger.error("Error getting picasa album[" + albumId + "]", malformedURLException);
            throw new ServiceException("Error getting picasa album[" + albumId + "]", malformedURLException);
        } catch (IOException ioException) {
            logger.error("Error getting picasa album[" + albumId + "]", ioException);
            throw new ServiceException("Error getting picasa album[" + albumId + "]", ioException);
        } catch (com.google.gdata.util.ServiceException serviceException) {
            logger.error("Error getting picasa album[" + albumId + "]", serviceException);
            throw new ServiceException("Error getting picasa album[" + albumId + "]", serviceException);
        }
    }

    private AlbumEntry getAlbumEntry(String albumId) throws ServiceException, MalformedURLException, IOException, com.google.gdata.util.ServiceException {
        if (StringUtils.isEmpty(albumId)) {
            throw new IllegalArgumentException();
        }
        if (!logged) {
            login();
        }
        return getPicasawebService().getEntry(getAlbumEntryUrl(albumId), AlbumEntry.class);
    }

    public List<Photo> getPhotos(Album album) throws ServiceException {
        try {
            if (album == null || StringUtils.isEmpty(album.getId())) {
                throw new IllegalArgumentException();
            }
            logger.debug("Getting picasa photos");
            if (!logged) {
                login();
            }
            AlbumFeed albumFeed = getAlbumFeed(album.getId());
            List<PhotoEntry> photoEntries = albumFeed.getPhotoEntries();
            List<Photo> photos = new ArrayList<Photo>();
            if (photoEntries != null) {
                for (PhotoEntry photoEntry : photoEntries) {
                    photos.add(new Photo(photoEntry));
                }
            }
            return photos;
        } catch (MalformedURLException malformedURLException) {
            logger.error("Error getting picasa photos", malformedURLException);
            throw new ServiceException("Error getting picasa photos", malformedURLException);
        } catch (IOException ioException) {
            logger.error("Error getting picasa photos", ioException);
            throw new ServiceException("Error getting picasa photos", ioException);
        } catch (com.google.gdata.util.ServiceException serviceException) {
            logger.error("Error getting picasa photos", serviceException);
            throw new ServiceException("Error getting picasa photos", serviceException);
        }
    }

    public Album addAlbum(Album album) throws ServiceException {
        try {
            if (album == null) {
                throw new IllegalArgumentException();
            }
            logger.debug("Adding picasa album[" + album + "]");
            if (!logged) {
                login();
            }
            removeGalleryAlbumsFromCache();
            return new Album(getPicasawebService().insert(getUserFeedUrl(), album.toAlbumEntry()));
        } catch (MalformedURLException malformedURLException) {
            logger.error("Error adding picasa album[" + album + "]", malformedURLException);
            throw new ServiceException("Error adding picasa album[" + album + "]", malformedURLException);
        } catch (IOException ioException) {
            logger.error("Error adding picasa album[" + album + "]", ioException);
            throw new ServiceException("Error adding picasa album[" + album + "]", ioException);
        } catch (com.google.gdata.util.ServiceException serviceException) {
            logger.error("Error adding picasa album[" + album + "]", serviceException);
            throw new ServiceException("Error adding picasa album[" + album + "]", serviceException);
        }
    }

    public void updateAlbum(Album album) throws ServiceException {
        try {
            if (album == null || StringUtils.isEmpty(album.getId())) {
                throw new IllegalArgumentException();
            }
            logger.debug("Updating picasa album[" + album + "]");
            if (!logged) {
                login();
            }
            removeGalleryAlbumsFromCache();
            AlbumEntry albumEntry = getAlbumEntry(album.getId());
            album.updateAlbumEntry(albumEntry);
            albumEntry.update();
        } catch (MalformedURLException malformedURLException) {
            logger.error("Error updating picasa album[" + album + "]", malformedURLException);
            throw new ServiceException("Error updating picasa album[" + album + "]", malformedURLException);
        } catch (IOException ioException) {
            logger.error("Error updating picasa album[" + album + "]", ioException);
            throw new ServiceException("Error adding picasa album[" + album + "]", ioException);
        } catch (com.google.gdata.util.ServiceException serviceException) {
            logger.error("Error updating picasa album[" + album + "]", serviceException);
            throw new ServiceException("Error updating picasa album[" + album + "]", serviceException);
        }
    }

    public void deleteAlbum(Album album) throws ServiceException {
        try {
            if (album == null || StringUtils.isEmpty(album.getId())) {
                throw new IllegalArgumentException();
            }
            logger.debug("Deleting picasa album[" + album + "]");
            if (!logged) {
                login();
            }
            removeGalleryAlbumsFromCache();
            getAlbumEntry(album.getId()).delete();
        } catch (MalformedURLException malformedURLException) {
            logger.error("Error deleting picasa album[" + album + "]", malformedURLException);
            throw new ServiceException("Error deleting picasa album[" + album + "]", malformedURLException);
        } catch (IOException ioException) {
            logger.error("Error deleting picasa album[" + album + "]", ioException);
            throw new ServiceException("Error adding picasa album[" + album + "]", ioException);
        } catch (com.google.gdata.util.ServiceException serviceException) {
            logger.error("Error deleting picasa album[" + album + "]", serviceException);
            throw new ServiceException("Error deleting picasa album[" + album + "]", serviceException);
        }
    }

    public Photo getPhoto(Photo photo) throws ServiceException {
        if (photo == null) {
            throw new IllegalArgumentException();
        }
        return getPhoto(photo.getAlbumId(), photo.getId());
    }

    public Photo getPhoto(String albumId, String photoId) throws ServiceException {
        try {
            if (StringUtils.isEmpty(albumId) || StringUtils.isEmpty(photoId)) {
                throw new IllegalArgumentException();
            }
            logger.debug("Getting picasa photo[" + photoId + "]");
            return new Photo(getPhotoEntry(albumId, photoId));
        } catch (MalformedURLException malformedURLException) {
            logger.error("Error getting picasa photo[" + photoId + "]", malformedURLException);
            throw new ServiceException("Error getting picasa photo[" + photoId + "]", malformedURLException);
        } catch (IOException ioException) {
            logger.error("Error getting picasa photo[" + photoId + "]", ioException);
            throw new ServiceException("Error getting picasa photo[" + photoId + "]", ioException);
        } catch (com.google.gdata.util.ServiceException serviceException) {
            logger.error("Error getting picasa photo[" + photoId + "]", serviceException);
            throw new ServiceException("Error getting picasa photo[" + photoId + "]", serviceException);
        }
    }

    private PhotoEntry getPhotoEntry(String albumId, String photoId) throws ServiceException, MalformedURLException, IOException, com.google.gdata.util.ServiceException {
        if (StringUtils.isEmpty(albumId) || StringUtils.isEmpty(photoId)) {
            throw new IllegalArgumentException();
        }
        if (!logged) {
            login();
        }
        return getPicasawebService().getEntry(getPhotoEntryUrl(albumId, photoId), PhotoEntry.class);
    }

    public Photo addPhoto(Photo photo, byte[] data, String mediaType) throws ServiceException {
        try {
            if (photo == null || data == null || StringUtils.isEmpty(mediaType) || StringUtils.isEmpty(photo.getAlbumId())) {
                throw new IllegalArgumentException();
            }
            logger.debug("Adding picasa photo[" + photo + "]");
            if (!logged) {
                login();
            }
            PhotoEntry photoEntry = photo.toPhotoEntry();
            MediaByteArraySource mediaByteArraySource = new MediaByteArraySource(data, mediaType);
            photoEntry.setMediaSource(mediaByteArraySource);
            removeGalleryAlbumsFromCache();
            return new Photo(getPicasawebService().insert(getAlbumFeedUrl(photo.getAlbumId()), photoEntry));
        } catch (MalformedURLException malformedURLException) {
            logger.error("Error adding picasa photo[" + photo + "]", malformedURLException);
            throw new ServiceException("Error adding picasa photo[" + photo + "]", malformedURLException);
        } catch (IOException ioException) {
            logger.error("Error adding picasa photo[" + photo + "]", ioException);
            throw new ServiceException("Error adding picasa photo[" + photo + "]", ioException);
        } catch (com.google.gdata.util.ServiceException serviceException) {
            logger.error("Error adding picasa photo[" + photo + "]", serviceException);
            throw new ServiceException("Error adding picasa photo[" + photo + "]", serviceException);
        }
    }

    public void updatePhoto(Photo photo) throws ServiceException {
        try {
            if (photo == null || StringUtils.isEmpty(photo.getId()) || StringUtils.isEmpty(photo.getAlbumId())) {
                throw new IllegalArgumentException();
            }
            logger.debug("Updating picasa photo[" + photo + "]");
            if (!logged) {
                login();
            }
            removeGalleryAlbumsFromCache();
            PhotoEntry photoEntry = getPhotoEntry(photo.getAlbumId(), photo.getId());
            photo.updatePhotoEntry(photoEntry);
            photoEntry.update();
        } catch (MalformedURLException malformedURLException) {
            logger.error("Error updating picasa photo[" + photo + "]", malformedURLException);
            throw new ServiceException("Error updating picasa photo[" + photo + "]", malformedURLException);
        } catch (IOException ioException) {
            logger.error("Error updating picasa photo[" + photo + "]", ioException);
            throw new ServiceException("Error adding picasa photo[" + photo + "]", ioException);
        } catch (com.google.gdata.util.ServiceException serviceException) {
            logger.error("Error updating picasa photo[" + photo + "]", serviceException);
            throw new ServiceException("Error updating picasa photo[" + photo + "]", serviceException);
        }
    }

    public void deletePhoto(Photo photo) throws ServiceException {
        try {
            if (photo == null || StringUtils.isEmpty(photo.getId()) || StringUtils.isEmpty(photo.getAlbumId())) {
                throw new IllegalArgumentException();
            }
            logger.debug("Deleting picasa photo[" + photo + "]");
            if (!logged) {
                login();
            }
            removeGalleryAlbumsFromCache();
            getPhotoEntry(photo.getAlbumId(), photo.getId()).delete();
        } catch (MalformedURLException malformedURLException) {
            logger.error("Error deleting picasa photo[" + photo + "]", malformedURLException);
            throw new ServiceException("Error deleting picasa photo[" + photo + "]", malformedURLException);
        } catch (IOException ioException) {
            logger.error("Error deleting picasa photo[" + photo + "]", ioException);
            throw new ServiceException("Error adding picasa photo[" + photo + "]", ioException);
        } catch (com.google.gdata.util.ServiceException serviceException) {
            logger.error("Error deleting picasa photo[" + photo + "]", serviceException);
            throw new ServiceException("Error deleting picasa photo[" + photo + "]", serviceException);
        }
    }

    protected PicasawebService getPicasawebService() {
        if (picasawebService == null) {
            picasawebService = new PicasawebService(AlvsanandProperties.getProperty(Globals.PICASA_APPLICATION_CONFIG_KEY));
        }
        return picasawebService;
    }

    private void removeGalleryAlbumsFromCache() {
        Cache cache;
        try {
            logger.debug("Removing gallery albums from cache.");
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
            cache.remove(Globals.GALLERY_ALBUMS_CACHE_NAME);
        } catch (CacheException cacheException) {
            logger.error("Error removing gallery albums from cache.", cacheException);
        }
    }

    public byte[] createAvatarImage(byte[] photoData) throws ServiceException {
        try {
            logger.debug("Getting avatar image");
            if (photoData == null) {
                throw new IllegalArgumentException();
            }
            int defaultHeight = Integer.parseInt(AlvsanandProperties.getProperty(Globals.AVATAR_IMAGE_HEIGHT_CONFIG_KEY));
            int defaultWidth = Integer.parseInt(AlvsanandProperties.getProperty(Globals.AVATAR_IMAGE_WIDTH_CONFIG_KEY));
            Image oldImage = ImagesServiceFactory.makeImage(photoData);
            Transform resize = ImagesServiceFactory.makeResize(defaultWidth, defaultHeight);
            Image newImage = imagesService.applyTransform(resize, oldImage);
            byte[] newImageData = newImage.getImageData();
            return newImageData;
        } catch (Exception serviceException) {
            logger.error("Error getting avatar image", serviceException);
            throw new ServiceException("Error getting avatar image", serviceException);
        }
    }
}
