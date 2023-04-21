package tuxiazi.dao;

import halo.dao.query.IDao;
import java.util.List;
import tuxiazi.bean.PhotoUserLike;

public interface PhotoUserLikeDao extends IDao<PhotoUserLike> {

    public int deleteByPhotoid(long photoid);

    public int deleteByUseridAndPhotoid(long userid, long photoid);

    public PhotoUserLike getByUseridAndPhotoid(long userid, long photoid);

    public List<PhotoUserLike> getListByPhotoid(long photoid, int begin, int size);

    public List<PhotoUserLike> getListByUserid(long userid, int begin, int size);
}
