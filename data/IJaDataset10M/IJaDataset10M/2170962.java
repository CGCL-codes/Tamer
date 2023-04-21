package gcr.mmm2.model;

import java.util.List;

/**
 * This is an interface for a share object. This is used by an owner to share objects (i.e. photos and albums)
 * @author Simon King
 *
 */
public interface IShare {

    public int getID();

    public IUser getOwner();

    public String getMessage();

    public void setMessage(String message, IUser owner) throws PermissionException;

    public void addRecipient(IContactList contactList, IUser owner) throws PermissionException;

    public void addRecipient(IPerson recipient, IUser owner) throws PermissionException;

    public List listRecipients();

    public boolean containsRecipient(IPerson person);

    public void addAlbum(IAlbum album, IUser owner) throws PermissionException;

    public void addPhoto(IPhoto photo, IUser owner) throws PermissionException;

    public boolean containsPhoto(IPhoto photo);

    public boolean containsAlbum(IAlbum album);

    public List listPhotos();

    public List listAlbums();

    public void notifyRecipients(boolean mobileNotification);
}
