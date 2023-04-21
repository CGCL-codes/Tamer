package flickr.service.iface;

/**
 * @author leon
 */
public interface FavoritesInterface {

    @FlickrMethod(method = "flickr.favorites.add")
    void addToFavorites(@FlickrMethodParam(name = "api_key") String apiKey, @FlickrMethodParam(name = "photo_id") String photoId, @FlickrMethodParam(name = "auth_token") String authToken);
}
