package com.hk.svr.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.bean.CheckInResult;
import com.hk.bean.CmpCheckInUser;
import com.hk.bean.CmpCheckInUserLog;
import com.hk.bean.CmpRefUser;
import com.hk.bean.Company;
import com.hk.bean.CompanyUserStatus;
import com.hk.bean.Feed;
import com.hk.bean.FeedInfo;
import com.hk.bean.HandleCheckInUser;
import com.hk.bean.IpCityRange;
import com.hk.bean.Laba;
import com.hk.bean.Notice;
import com.hk.bean.User;
import com.hk.bean.UserDateCheckInCmp;
import com.hk.bean.UserLastCheckIn;
import com.hk.frame.util.DataUtil;
import com.hk.svr.CmpCheckInService;
import com.hk.svr.CmpRefUserService;
import com.hk.svr.CompanyService;
import com.hk.svr.FeedService;
import com.hk.svr.FollowService;
import com.hk.svr.HandleService;
import com.hk.svr.IpCityService;
import com.hk.svr.LabaService;
import com.hk.svr.NoticeService;
import com.hk.svr.UserService;
import com.hk.svr.equipment.EquipmentMsg;
import com.hk.svr.equipment.EquipmentStatus;
import com.hk.svr.equipment.HandleEquipmentProcessor;
import com.hk.svr.laba.parser.LabaInPutParser;
import com.hk.svr.laba.parser.LabaInfo;
import com.hk.svr.pub.CheckInPointConfig;

public class CmpCheckInProcessor {

    @Autowired
    private UserService userService;

    @Autowired
    private CmpCheckInService cmpCheckInService;

    @Autowired
    private HandleEquipmentProcessor handleEquipmentProcessor;

    @Autowired
    private HandleService handleService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private LabaService labaService;

    @Autowired
    private IpCityService ipCityService;

    @Autowired
    private FeedService feedService;

    @Autowired
    private FollowService followService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private CmpRefUserService cmpRefUserService;

    private boolean checkInWithCreateLaba;

    protected static Comparator<Long> logIdCmptor = new Comparator<Long>() {

        public int compare(Long o1, Long o2) {
            if (o1.longValue() > o2.longValue()) {
                return -1;
            }
            return 1;
        }
    };

    public void setCheckInWithCreateLaba(boolean checkInWithCreateLaba) {
        this.checkInWithCreateLaba = checkInWithCreateLaba;
    }

    private final Log log = LogFactory.getLog(CmpCheckInProcessor.class);

    /**
	 * 获得地区的报到记录
	 * 
	 * @param pcityId 城市id
	 * @param buildUser 是否组装数据中的用户对象
	 * @param buildCompany 是否组装数据中的足迹对象
	 * @param begin
	 * @param size
	 * @return
	 *         2010-4-23
	 */
    public List<CmpCheckInUserLog> getCmpCheckInUserLogListByPcityId(int pcityId, boolean buildUser, boolean buildCompany, int begin, int size) {
        List<CmpCheckInUserLog> list = this.cmpCheckInService.getEffectCmpCheckInUserLogListByPcityId(pcityId, begin, size);
        if (buildUser) {
            List<Long> idList = new ArrayList<Long>();
            for (CmpCheckInUserLog o : list) {
                idList.add(o.getUserId());
            }
            Map<Long, User> map = this.userService.getUserMapInId(idList);
            for (CmpCheckInUserLog o : list) {
                o.setUser(map.get(o.getUserId()));
            }
        }
        if (buildCompany) {
            List<Long> idList = new ArrayList<Long>();
            for (CmpCheckInUserLog o : list) {
                idList.add(o.getCompanyId());
            }
            Map<Long, Company> map = this.companyService.getCompanyMapInId(idList);
            for (CmpCheckInUserLog o : list) {
                o.setCompany(map.get(o.getCompanyId()));
            }
        }
        return list;
    }

    /**
	 * 获得用户报到记录
	 * 
	 * @param userId
	 * @param buildUser 是否组装数据中的用户对象
	 * @param buildCompany 是否组装数据中的足迹对象
	 * @return
	 *         2010-4-23
	 */
    public List<CmpCheckInUser> getCmpCheckInUserListByUserId(long userId, boolean buildUser, boolean buildCompany, int begin, int size) {
        List<CmpCheckInUser> list = this.cmpCheckInService.getCmpCheckInUserListByUserId(userId, begin, size);
        if (buildUser) {
            List<Long> idList = new ArrayList<Long>();
            for (CmpCheckInUser o : list) {
                idList.add(o.getUserId());
            }
            Map<Long, User> map = this.userService.getUserMapInId(idList);
            for (CmpCheckInUser o : list) {
                o.setUser(map.get(o.getUserId()));
            }
        }
        if (buildCompany) {
            List<Long> idList = new ArrayList<Long>();
            for (CmpCheckInUser o : list) {
                idList.add(o.getCompanyId());
            }
            Map<Long, Company> map = this.companyService.getCompanyMapInId(idList);
            for (CmpCheckInUser o : list) {
                o.setCompany(map.get(o.getCompanyId()));
            }
        }
        return list;
    }

    /**
	 * 获得在这里报道过的人的数据集合
	 * 
	 * @param companyId
	 * @param buildUser
	 * @param begin
	 * @param size
	 * @return
	 *         2010-5-8
	 */
    public List<CmpCheckInUser> getCmpCheckInUserListByCompanyId(long companyId, boolean buildUser, int begin, int size) {
        List<CmpCheckInUser> list = this.cmpCheckInService.getCmpCheckInUserListByCompanyId(companyId, begin, size);
        if (buildUser) {
            List<Long> idList = new ArrayList<Long>();
            for (CmpCheckInUser o : list) {
                idList.add(o.getUserId());
            }
            Map<Long, User> map = this.userService.getUserMapInId(idList);
            for (CmpCheckInUser o : list) {
                o.setUser(map.get(o.getUserId()));
            }
        }
        return list;
    }

    /**
	 * 获得用户关注的人的报到情况
	 * 
	 * @param userId
	 * @param buildUser 是否组装数据中的用户对象
	 * @param buildCompany 是否组装数据中的足迹对象
	 * @return
	 *         2010-4-23
	 */
    public List<CmpCheckInUserLog> getFriendCmpCheckInUserLog(long userId, boolean buildUser, boolean buildCompany, int size) {
        List<Long> idList = this.followService.getFollowFriendIdListByUserId(userId);
        List<UserLastCheckIn> olist = this.cmpCheckInService.getUserLastCheckInListInIdList(idList);
        List<Long> list = new ArrayList<Long>();
        for (UserLastCheckIn o : olist) {
            list.addAll(o.getLogIdList());
        }
        Collections.sort(list, logIdCmptor);
        list = DataUtil.subList(list, 0, size);
        List<CmpCheckInUserLog> loglist = this.cmpCheckInService.getCmpCheckInUserLogListInId(list);
        if (buildCompany) {
            list.clear();
            for (CmpCheckInUserLog o : loglist) {
                list.add(o.getCompanyId());
            }
            Map<Long, Company> map = this.companyService.getCompanyMapInId(list);
            for (CmpCheckInUserLog o : loglist) {
                o.setCompany(map.get(o.getCompanyId()));
            }
        }
        if (buildUser) {
            list.clear();
            for (CmpCheckInUserLog o : loglist) {
                list.add(o.getUserId());
            }
            Map<Long, User> usermap = this.userService.getUserMapInId(list);
            for (CmpCheckInUserLog o : loglist) {
                o.setUser(usermap.get(o.getUserId()));
            }
        }
        return loglist;
    }

    public CheckInResult checkIn(CmpCheckInUserLog cmpCheckInUserLog, boolean forceInvalid, Company company, String ip) {
        boolean _forceInvalid = forceInvalid;
        UserDateCheckInCmp userDateCheckInCmp = this.cmpCheckInService.getUserDateCheckInCmp(cmpCheckInUserLog.getUserId());
        if (userDateCheckInCmp == null) {
            userDateCheckInCmp = new UserDateCheckInCmp();
            userDateCheckInCmp.setUserId(cmpCheckInUserLog.getUserId());
            userDateCheckInCmp.setUptime(new Date());
        }
        if (!_forceInvalid) {
            if (userDateCheckInCmp.isInOneDate()) {
                if (userDateCheckInCmp.getCompanyIdListSize() >= 10) {
                    _forceInvalid = true;
                }
            } else {
                userDateCheckInCmp.clearCompanyIdList();
            }
        }
        userDateCheckInCmp.addCompanyId(cmpCheckInUserLog.getCompanyId());
        userDateCheckInCmp.toData(10);
        userDateCheckInCmp.setUptime(new Date());
        this.cmpCheckInService.saveUserDateCheckInCmp(userDateCheckInCmp);
        CheckInResult checkInResult = this.cmpCheckInService.checkIn(cmpCheckInUserLog, _forceInvalid, company);
        if (this.checkInWithCreateLaba) {
            this.createLaba(cmpCheckInUserLog);
        }
        this.processBadgeProcess(cmpCheckInUserLog);
        this.processUserLastCheckIn(checkInResult, cmpCheckInUserLog);
        this.updateCompanyCheckInCount(checkInResult, cmpCheckInUserLog);
        this.processCompanyUserStatus(cmpCheckInUserLog);
        this.processAfterCheckIn(checkInResult, cmpCheckInUserLog, company, ip);
        CmpRefUser cmpRefUser = new CmpRefUser();
        cmpRefUser.setCompanyId(cmpCheckInUserLog.getCompanyId());
        cmpRefUser.setUserId(cmpCheckInUserLog.getUserId());
        cmpRefUser.setJoinflg(CmpRefUser.JOINFLG_CHECKIN);
        this.cmpRefUserService.createCmpRefUser(cmpRefUser);
        return checkInResult;
    }

    public int checkMayor(long userId, Company company, String ip, boolean forceMayor) {
        long old_mayorId = company.getMayorUserId();
        int result = this.cmpCheckInService.checkMayor(userId, company, ip, forceMayor);
        this.onCheckMayored(result, userId, old_mayorId, company, ip);
        return result;
    }

    /**
	 * 当成为新地主时，记录动态
	 */
    private void onCheckMayored(int result, long userId, long old_mayorId, Company company, String ip) {
        if (result == 1) {
            Feed feed = new Feed();
            feed.setCityId(company.getPcityId());
            feed.setFeedType(Feed.FEEDTYPE_BECOME_MAYOR);
            feed.setUserId(userId);
            feed.setCreateTime(new Date());
            if (ip != null) {
                IpCityRange ipCityRange = this.ipCityService.getIpCityRange(ip);
                if (ipCityRange != null) {
                    feed.setRangeId(ipCityRange.getRangeId());
                    feed.setIpNumber(DataUtil.parseIpNumber(ip));
                }
            }
            Map<String, String> map = new HashMap<String, String>();
            User user = this.userService.getUser(feed.getUserId());
            map.put("nickname", user.getNickName());
            map.put("headpath", user.getHeadPath());
            map.put("companyid", String.valueOf(company.getCompanyId()));
            map.put("cmpname", company.getName());
            feed.setData(DataUtil.toJson(map));
            FeedInfo feedInfo = feed.createFeedInfo();
            feedInfo.setObjId(company.getCompanyId());
            this.feedService.createFeed(feed, feedInfo);
            Notice notice = new Notice();
            notice.setUserId(old_mayorId);
            notice.setNoticeType(Notice.NOTICETYPE_CHANGEMAYOR);
            notice.setCreateTime(new Date());
            notice.setReadflg(Notice.READFLG_N);
            notice.setData(DataUtil.toJson(map));
            this.noticeService.createNotice(notice);
        }
    }

    private void processBadgeProcess(CmpCheckInUserLog cmpCheckInUserLog) {
        if (cmpCheckInUserLog.isEffective()) {
            HandleCheckInUser handleCheckInUser = new HandleCheckInUser();
            handleCheckInUser.setUserId(cmpCheckInUserLog.getUserId());
            handleCheckInUser.setCompanyId(cmpCheckInUserLog.getCompanyId());
            try {
                this.handleService.createHandleCheckInUser(handleCheckInUser);
            } catch (Exception e) {
                log.error("============ badgeprocess ===========");
                log.error(e.getMessage());
                log.error("========== badgeprocess end =========");
            }
        }
    }

    private void processCompanyUserStatus(CmpCheckInUserLog cmpCheckInUserLog) {
        CompanyUserStatus companyUserStatus = this.companyService.getCompanyUserStatus(cmpCheckInUserLog.getCompanyId(), cmpCheckInUserLog.getUserId());
        if (companyUserStatus != null && companyUserStatus.isDone()) {
            return;
        }
        this.companyService.createCompanyUserStatus(cmpCheckInUserLog.getCompanyId(), cmpCheckInUserLog.getUserId(), CompanyUserStatus.OK_FLG);
    }

    private void updateCompanyCheckInCount(CheckInResult checkInResult, CmpCheckInUserLog cmpCheckInUserLog) {
        if (checkInResult.isCheckInSuccess()) {
            this.companyService.addCheckInCount(cmpCheckInUserLog.getCompanyId(), 1);
        }
    }

    private void processUserLastCheckIn(CheckInResult checkInResult, CmpCheckInUserLog cmpCheckInUserLog) {
        if (checkInResult.isCheckInSuccess()) {
            UserLastCheckIn userLastCheckIn = this.cmpCheckInService.getUserLastCheckIn(cmpCheckInUserLog.getUserId());
            if (userLastCheckIn == null) {
                userLastCheckIn = new UserLastCheckIn();
                userLastCheckIn.setUserId(cmpCheckInUserLog.getUserId());
            }
            userLastCheckIn.addLogId(cmpCheckInUserLog.getLogId());
            userLastCheckIn.toData(20);
            this.cmpCheckInService.updateUserLastCheckIn(userLastCheckIn);
        }
    }

    private void createLaba(CmpCheckInUserLog cmpCheckInUserLog) {
        if (cmpCheckInUserLog.isEffective()) {
            long companyId = cmpCheckInUserLog.getCompanyId();
            long userId = cmpCheckInUserLog.getUserId();
            Company company = this.companyService.getCompany(companyId);
            String content = "我在{[" + companyId + "," + DataUtil.toTextRow(company.getName()) + "}";
            LabaInPutParser parser = new LabaInPutParser(null);
            LabaInfo labaInfo = parser.parse(content);
            labaInfo.setUserId(userId);
            labaInfo.setSendFrom(Laba.SENDFROM_WEB);
            this.labaService.createLaba(labaInfo);
        }
    }

    private void processAfterCheckIn(CheckInResult checkInResult, CmpCheckInUserLog cmpCheckInUserLog, Company company, String ip) {
        Map<String, Object> ctxAttributeMap = new HashMap<String, Object>();
        long userId = cmpCheckInUserLog.getUserId();
        long companyId = company.getCompanyId();
        ctxAttributeMap.put("companyId", companyId);
        ctxAttributeMap.put("company", company);
        ctxAttributeMap.put("checkInResult", checkInResult);
        ctxAttributeMap.put("ip", ip);
        ctxAttributeMap.put("cityId", company.getPcityId());
        this.handleEquipmentProcessor.processEquipment(userId, companyId, ctxAttributeMap);
        EquipmentMsg equipmentMsg = (EquipmentMsg) ctxAttributeMap.get(EquipmentStatus.EQUIPMENTMSG_ATTR);
        if (equipmentMsg != null) {
            checkInResult.setEquipmentMsg(equipmentMsg);
        }
        this.addPoints(checkInResult, cmpCheckInUserLog, ctxAttributeMap);
        int res = this.checkMayor(userId, company, ip, false);
        if (res == 1) {
            company.setMayorUserId(userId);
            this.companyService.updateCompany(company);
        }
    }

    private int getAddPoints(CmpCheckInUserLog cmpCheckInUserLog, Map<String, Object> ctxAttributeMap) {
        int beishu = 1;
        if (this.canDoublePoints(ctxAttributeMap)) {
            beishu = 2;
        }
        long companyId = cmpCheckInUserLog.getCompanyId();
        long userId = cmpCheckInUserLog.getUserId();
        this.userService.updateUserUpdate(userId);
        int userCount = this.cmpCheckInService.countEffectCmpCheckInUserLogByCompanyIdAndUserId(companyId, userId);
        boolean firstCheckIn = false;
        if (userCount == 1) {
            firstCheckIn = true;
        }
        if (firstCheckIn) {
            return CheckInPointConfig.getFirstCheckIn() * beishu;
        }
        int userCountDate = this.cmpCheckInService.countEffectCmpCheckInUserLogByCompanyIdAndUserId(companyId, userId, new Date());
        if (userCountDate == 1) {
            return CheckInPointConfig.getDateFirstCheckIn() * beishu;
        } else if (userCountDate == 2) {
            return CheckInPointConfig.getDateSecondCheckIn() * beishu;
        }
        return 0;
    }

    private void addPoints(CheckInResult checkInResult, CmpCheckInUserLog cmpCheckInUserLog, Map<String, Object> ctxAttributeMap) {
        int add = 0;
        if (checkInResult.isCheckInSuccess() && this.canAddPoints(ctxAttributeMap)) {
            add = this.getAddPoints(cmpCheckInUserLog, ctxAttributeMap);
            if (add > 0) {
                this.userService.addPoints(cmpCheckInUserLog.getUserId(), add);
                this.cmpCheckInService.addUserCmpPoints(cmpCheckInUserLog.getUserId(), cmpCheckInUserLog.getCompanyId(), add);
            }
        }
    }

    private boolean canDoublePoints(Map<String, Object> ctxAttributeMap) {
        Boolean v = (Boolean) ctxAttributeMap.get(EquipmentStatus.CAN_GET_DOUBLE_POINTS);
        if (v != null && v.booleanValue()) {
            return true;
        }
        return false;
    }

    private boolean canAddPoints(Map<String, Object> ctxAttributeMap) {
        Boolean v = (Boolean) ctxAttributeMap.get(EquipmentStatus.CAN_NOT_GET_POINTS);
        if (v != null && v.booleanValue()) {
            return false;
        }
        return true;
    }
}
