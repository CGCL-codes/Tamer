package tuxiazi.dao;

import halo.dao.query.IDao;
import java.util.List;
import tuxiazi.bean.Notice;
import tuxiazi.bean.benum.NoticeEnum;
import tuxiazi.bean.benum.NoticeReadEnum;

public interface NoticeDao extends IDao<Notice> {

    public Notice getByUseridAndSenderidAndRefoidAndNotice_flg(long userid, long senderid, long refoid, NoticeReadEnum noticeReadEnum);

    public Notice getLastByUseridAndSenderidAndRefoid(long userid, long senderid, long refoid);

    public List<Notice> getListByUserid(long userid, boolean buildSender, int begin, int size);

    /**
	 * 根据读取状态获取通知数据集合
	 * 
	 * @param userid
	 * @param noticeReadEnum
	 * @param begin
	 * @param size
	 * @return
	 */
    List<Notice> getListByUseridAndReadflg(long userid, NoticeReadEnum noticeReadEnum, boolean buildSender, int begin, int size);

    /**
	 * 根据不同类型获得未读通知
	 * 
	 * @param userid
	 * @param noticeEnum
	 * @return
	 */
    int countByUseridAndNotice_flgForUnread(long userid, NoticeEnum noticeEnum);

    /**
	 * 统计所有未读通知
	 * 
	 * @param userid
	 * @return
	 */
    int countByUseridForUnread(long userid);

    int updateReaded(long noticeid, int read_flg);
}
