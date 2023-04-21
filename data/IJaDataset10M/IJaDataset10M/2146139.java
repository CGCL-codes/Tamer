package test.dao.stub;

import java.util.LinkedList;
import java.util.List;
import jmemento.dao.db.api.IPoolDao;
import jmemento.domain.album.api.IPhotoCollection;
import jmemento.domain.album.impl.Pool;
import jmemento.domain.photo.api.IPhoto;
import jmemento.domain.photo.impl.Photo;

/**
 * @author rusty
 */
public final class PoolStubDao implements IPoolDao {

    public void deletePoolById(final String id) {
        if ((id == null) || (id.compareTo("1") != 0)) throw (new IllegalArgumentException("only id 1 allowed"));
    }

    public IPhotoCollection getPoolById(final String id) {
        if ((id == null) || (id.compareTo("1") != 0)) throw (new IllegalArgumentException("only id 1 allowed"));
        return (createPoolOfOne());
    }

    public List<IPhotoCollection> getPoolsByUserId(final String id) {
        if ((id == null) || (id.compareTo("1") != 0)) throw (new IllegalArgumentException("only id 1 allowed"));
        final List<IPhotoCollection> pools = new LinkedList<IPhotoCollection>();
        pools.add(createPoolOfOne());
        return (pools);
    }

    private IPhotoCollection createPoolOfOne() {
        final List<IPhoto> photos = new LinkedList<IPhoto>();
        photos.add(makePhoto());
        final IPhotoCollection pool = makePool(photos);
        return (pool);
    }

    /**
     * @return
     */
    private IPhotoCollection makePool(final List<IPhoto> photos) {
        final IPhotoCollection pool = new Pool();
        pool.setDisplayName("stub pool");
        pool.addPhotos(photos);
        return pool;
    }

    /**
     * @return
     */
    private IPhoto makePhoto() {
        final IPhoto photo = new Photo();
        photo.setDisplayName("stub photo in pool");
        return photo;
    }
}
