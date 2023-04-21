package tuxiazi.dao;

import halo.dao.query.IDao;
import java.util.List;
import tuxiazi.bean.HotPhoto;

public interface HotPhotoDao extends IDao<HotPhoto> {

    public int deleteByPhotoid(long photoid);

    public List<HotPhoto> getList(int begin, int size);

    int deleteAll();
}
