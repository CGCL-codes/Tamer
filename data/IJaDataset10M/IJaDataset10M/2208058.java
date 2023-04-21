package com.hk.svr;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.hk.bean.CompanyRefLaba;
import com.hk.bean.FavLaba;
import com.hk.bean.IndexLaba;
import com.hk.bean.IpCityLaba;
import com.hk.bean.IpCityRangeLaba;
import com.hk.bean.Laba;
import com.hk.bean.LabaCmt;
import com.hk.bean.LabaDel;
import com.hk.bean.LabaDelInfo;
import com.hk.bean.PinkLaba;
import com.hk.bean.RefLaba;
import com.hk.bean.RespLaba;
import com.hk.bean.Tag;
import com.hk.bean.TagLaba;
import com.hk.bean.UserLabaReply;
import com.hk.bean.UserRecentLaba;
import com.hk.svr.laba.parser.LabaInfo;
import com.hk.svr.pub.HonorConfig;
import com.hk.svr.pub.ScoreConfig;

public interface LabaService {

    /**
	 * 发表小喇叭,积分和荣誉参见配置文件 {@link ScoreConfig} {@link HonorConfig}
	 * 
	 * @param labaInfo
	 */
    long createLaba(LabaInfo labaInfo);

    void updateLaba(long labaId, String sContent, String lContent);

    /**
	 * 获取喇叭
	 * 
	 * @param begin
	 * @param size
	 * @return
	 */
    List<Laba> getLabaList(int begin, int size);

    /**
	 * 获取个人喇叭
	 * 
	 * @param userId
	 * @param begin
	 * @param size
	 * @return
	 */
    List<Laba> getLabaListByUserId(long userId, int begin, int size);

    /**
	 * 获得用户最近10条喇叭
	 * 
	 * @param userId
	 * @param begin
	 * @param size
	 * @return
	 */
    UserRecentLaba getUserRecentLaba(long userId);

    /**
	 * 删除喇叭，把喇叭从当前表中删除，备份到其他表
	 * 
	 * @param userId
	 *            删除喇叭人的userid 不一定是喇叭发送人
	 * @param labaId
	 * @return
	 */
    LabaDelInfo removeLaba(long userId, long labaId, boolean forBomb);

    /**
	 * 恢复被删除的喇叭
	 * 
	 * @param labaDelInfo
	 * @return
	 */
    Laba reRemoveLaba(LabaDelInfo labaDelInfo);

    /**
	 * 获得喇叭对象
	 * 
	 * @param labaId
	 * @return
	 */
    Laba getLaba(long labaId);

    public Laba getLastUserLaba(long userId);

    List<Laba> getLabaListFromIpCity(int ipCityId, int begin, int size);

    List<Laba> getLabaListByCompanyIdFromCompanyRefLaba(long companyId, int begin, int size);

    List<Laba> getInformationLabaList(long userId, long tagId, int begin, int size);

    List<Laba> getInformationUserLabaList(long userId, long tagId, int begin, int size);

    void indexLaba(List<IndexLaba> list);

    List<Laba> getLabaListForFollowByUserId(long userId, int begin, int size);

    List<UserLabaReply> getUserLabaReplyList(long userId, int begin, int size);

    List<TagLaba> getTagLabaList(long tagId, int begin, int size);

    List<TagLaba> getTagLabaListByUserId(long tagId, long userId, int begin, int size);

    List<FavLaba> getFavLabaListByUserId(long userId, int begin, int size);

    /**
	 * 收藏喇叭
	 * 
	 * @param userId
	 * @param labaId
	 */
    void collectLaba(long userId, long labaId);

    /**
	 * 删除收藏的喇叭
	 * 
	 * @param userId
	 * @param labaId
	 */
    void delCollectLaba(long userId, long labaId);

    /**
	 * 获得频道集合
	 * 
	 * @param labaId
	 * @param accessional
	 *            条件为属于喇叭内容中自带的或者是外观的
	 * @return
	 */
    List<Tag> getTagList(long labaId, byte accessional);

    List<IpCityLaba> getIpCityLabaList(int ipCityId, int begin, int size);

    List<Laba> getIpLabaList(String ip, int begin, int size);

    List<IpCityRangeLaba> getIpCityRangeLabaList(int rangeId, int begin, int size);

    boolean isCollected(long userId, long labaId);

    int count();

    List<Laba> getLabaListForSearch(String key, int begin, int size);

    String getLabaIdxDir();

    List<UserLabaReply> getUserLabaReplyListByUserIdAndTime(long userId, Date beginTime, Date endTime, int begin, int size);

    /**
	 * 统计在时段内回复某人的喇叭数量
	 * 
	 * @param userId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
    int counttUserLabaReplyListByUserIdAndTime(long userId, Date beginTime, Date endTime);

    int countUserLabaReplyByUserId(long userId);

    /**
	 * 查看labaid中哪些已经被用户收藏
	 * 
	 * @param userId
	 * @param idList
	 * @return
	 */
    List<Long> getCollectedLabaIdList(long userId, List<Long> idList);

    /**
	 * 获得用户的最新一个喇叭
	 * 
	 * @param userIdList
	 * @return 以用户id作为key
	 */
    Map<Long, UserRecentLaba> getUserRecentLabaMapInUser(List<Long> userIdList);

    /**
	 * @param labaId
	 * @param userId
	 * @param tagId
	 * @param accessional
	 *            是否是附加频道
	 * @return
	 */
    boolean addTagForLaba(long labaId, long userId, long tagId, byte accessional);

    /**
	 * 删除喇叭与某个频道的关系
	 * 
	 * @param labaId
	 * @param tagId
	 */
    void deleteTagForLaba(long labaId, long tagId);

    /**
	 * 删除喇叭与某个频道的关系，并且还是某人添加的
	 * 
	 * @param labaId
	 * @param tagId
	 * @param userId
	 */
    void deleteTagForLaba(long labaId, long tagId, long userId);

    /**
	 * 统计某人在时间段内的喇叭数量
	 * 
	 * @param userId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
    int countLaba(long userId, Date beginTime, Date endTime);

    /**
	 * 回应某人,并且是某频道的喇叭id集合，个人喇叭回应表与频道表的关联，获得对于某个特定频道某人的回应
	 * 
	 * @param userId
	 * @param tagId
	 * @return labaid的集合
	 */
    List<Long> getInformationLabaIdList(long userId, long tagId, int begin, int size);

    /**
	 * 获得个人信息台的喇叭集合,由于信息台依靠频道进行聚合，所以需要关联标签表
	 * 
	 * @param userId
	 * @param tagId
	 * @param begin
	 * @param size
	 * @return
	 */
    List<Long> getInformationLabaIdListForMe(long userId, long tagId, int begin, int size);

    /**
	 * 获得引用当前喇叭的喇叭集合
	 * 
	 * @param labaId
	 * @param begin
	 * @param size
	 * @return
	 */
    List<RefLaba> getRefLabaList(long labaId, int begin, int size);

    /**
	 * 根据labaid获得喇叭集合
	 * 
	 * @param idList
	 * @return
	 */
    List<Laba> getLabaListInId(List<Long> idList);

    List<Laba> getLabaListInId(List<Long> idList, int begin, int size);

    Map<Long, Laba> getLabaMapInId(List<Long> idList);

    Map<Long, LabaDel> getLabaDelMapInId(List<Long> idList);

    /**
	 * 获得企业相关喇叭表集合,不包括喇叭内容
	 * 
	 * @param companyId
	 * @param begin
	 * @param size
	 * @return
	 */
    List<CompanyRefLaba> getCompanyRefLabaList(long companyId, int begin, int size);

    RefLaba getRefLaba(long labaId, long userId);

    List<Laba> getLabaListByRefLabaIdAndUserId(long reflabaId, long userId);

    /**
	 * 获得精华表的喇叭数据,如果不是精华返回null
	 * 
	 * @param labaId
	 * @return
	 */
    PinkLaba getPinkLaba(long labaId);

    List<RespLaba> getRespLabaList(Date beginTime, Date endTime);

    void updateHotLaba(RespLaba respLaba);

    List<RespLaba> getRespLabaList(int begin, int size);

    int countByUserId(long userId);

    /**
	 * 获取用户引用的labaid集合中的数据，放到map中
	 * 
	 * @param idList
	 * @return
	 */
    Map<Long, RefLaba> getRefLabaMapInLabaIdByRefUserId(long refUserId, List<Long> idList);

    void createLabaCmt(LabaCmt labaCmt, LabaInfo labaInfo);

    void deleteLabaCmt(long cmtId);

    List<LabaCmt> getLabaCmtListByLabaId(long labaId, int begin, int size);

    List<LabaCmt> getLabaCmtListInId(List<Long> idList);

    Map<Long, LabaCmt> getLabaCmtMapInId(List<Long> idList);

    List<LabaCmt> getUserLabaCmtListByLabaId(long labaId, long userId, int begin, int size);

    LabaCmt getLabaCmt(long cmtId);

    List<Laba> getLabaList();

    /**
	 * 处理热榜喇叭数据(当评论喇叭或者回复喇叭时)
	 * 
	 * @param labaInfo
	 */
    void processRespLaba(LabaInfo labaInfo);
}
