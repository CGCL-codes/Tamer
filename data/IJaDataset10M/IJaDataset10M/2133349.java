package name.huliqing.qblog.daocache;

import java.util.List;
import name.huliqing.qblog.dao.PhotoDa;
import name.huliqing.qblog.entity.PhotoEn;

/**
 *
 * @author huliqing
 */
public class PhotoCache extends PhotoDa {

    private static final PhotoCache ins = new PhotoCache();

    private PhotoCache() {
    }

    public static final PhotoCache getInstance() {
        return ins;
    }

    @Override
    public boolean save(PhotoEn t) {
        return super.save(t);
    }

    @Override
    public boolean update(PhotoEn t) {
        return super.update(t);
    }

    @Override
    public boolean delete(Long photoId) {
        return super.delete(photoId);
    }

    @Override
    public boolean deleteByFolderId(Long folderId) {
        return super.deleteByFolderId(folderId);
    }

    @Override
    public PhotoEn find(Long id) {
        return super.find(id);
    }

    /**
     * 通过photoId查找photo,该方法不会取出photo的bytes字段
     * @param photoId
     * @return
     */
    public PhotoEn findSimple(Long photoId) {
        PhotoEn search = new PhotoEn();
        search.setPhotoId(photoId);
        List<PhotoEn> result = findByObject(search, null, null, 0, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    /**
     * 分页查询相册文件夹下的所有图片
     * @param folderId 文件夹id, 如果folderId为null，则返回null
     * @param sortField 排序字段
     * @param asc 是否正序
     * @param start 起始记录
     * @param size 获取的最高数量
     * @return
     */
    public List<PhotoEn> findByFolder(Long folderId, String sortField, Boolean asc, Integer start, Integer size) {
        if (folderId == null) throw new NullPointerException("folderId couldn't be null.");
        PhotoEn search = new PhotoEn();
        search.setFolder(folderId);
        return findByObject(search, sortField, asc, start, size);
    }

    /**
     * 查询某文件夹下所有图片的数量
     * @param folderId
     * @return
     */
    public Integer countByFolder(Long folderId) {
        if (folderId == null) throw new NullPointerException("folderId couldn't be null.");
        PhotoEn search = new PhotoEn();
        search.setFolder(folderId);
        return countByObject(search);
    }
}
