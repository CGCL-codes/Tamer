package flickr.service;

import flickr.response.Group;
import flickr.response.Photo;
import flickr.response.Photos;

/**
 * @author leon
 */
public interface PoolsService {

    void loadPhotos(Group group, FlickrResponseHandler<Photos> handler);

    void loadPhotos(int page, int pageSize, Group group, FlickrResponseHandler<Photos> handler);

    void addPhoto(Group group, Photo photo, FlickrResponseHandler<?> handler);
}
