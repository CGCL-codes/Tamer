package tuxiazi.dao;

import halo.dao.query.IDao;
import java.util.Date;
import java.util.List;
import tuxiazi.bean.PhotoLikeLog;

public interface PhotoLikeLogDao extends IDao<PhotoLikeLog> {

    List<PhotoLikeReport> buildPhotoLikeReportListByTime(Date begin, Date end);
}
