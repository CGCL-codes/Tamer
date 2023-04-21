package de.dirkdittmar.flickr.group.comment.service;

import java.util.List;
import de.dirkdittmar.flickr.group.comment.domain.Group;
import de.dirkdittmar.flickr.group.comment.domain.Photo;
import de.dirkdittmar.flickr.group.comment.exception.FlickrException;
import de.dirkdittmar.flickr.group.comment.exception.NetworkException;
import de.dirkdittmar.flickr.group.comment.exception.RemoteException;

public interface GroupService {

    List<Group> getYourGroups(String token) throws FlickrException, RemoteException, NetworkException;

    void addPhotoToGroup(String token, String photoId, String groupId) throws FlickrException, RemoteException, NetworkException;

    List<Photo> getRecentGroupPoolPhotos(String groupId, int numPhotos) throws FlickrException, RemoteException, NetworkException;
}
