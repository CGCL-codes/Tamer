package com.wangyu001.logicimpl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.sysolar.sun.mvc.support.Pager;
import com.wangyu001.constant.Dao;
import com.wangyu001.dao.HotWebDao;
import com.wangyu001.dao.UserWebDao;
import com.wangyu001.entity.CoreUrl;
import com.wangyu001.entity.HotWeb;
import com.wangyu001.entity.HotWebCat;
import com.wangyu001.logic.HotWebLogic;

public class HotWebLogicImpl implements HotWebLogic {

    HotWebDao hotWebDao = Dao.hotWebDao;

    UserWebDao userWebDao = Dao.userWebDao;

    public void create(HotWeb hotWeb) throws Exception {
        if (hotWebDao.fetchByCoreUrlId(hotWeb.getCoreUrlId()) != null) {
            hotWebDao.updateByCoreUrlId(hotWeb);
        } else {
            hotWebDao.create(hotWeb, false);
        }
    }

    public HotWeb fetch(Long webId) throws Exception {
        return null;
    }

    public boolean remove(Long webId) throws Exception {
        return false;
    }

    public List<CoreUrl> fetchHotWebList(Integer pageNum, Long userId, Long catId, Long zuiWhat, Long mediaType, Long agoTimeByHour) throws Exception {
        Pager<CoreUrl> pager = new Pager<CoreUrl>((null == pageNum) ? 1 : pageNum, 10, 500);
        Long timeInMillis = agoTimeByHour * 60 * 60 * 1000;
        Long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        Date timeAgo = new Date(currentTimeInMillis - timeInMillis);
        List<CoreUrl> coreUrlList = hotWebDao.fetchHotWebList(pager, userId, catId, zuiWhat, mediaType, timeAgo);
        return coreUrlList;
    }

    public Integer fetchHotWebCount(Long catId, Long mediaType, Long agoTimeByHour) throws Exception {
        Long timeInMillis = agoTimeByHour * 60 * 60 * 1000;
        Long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        Date timeAgo = new Date(currentTimeInMillis - timeInMillis);
        return hotWebDao.fetchHotWebCount(catId, mediaType, timeAgo);
    }

    /**
     * 点击热门分享->管理->数据审核时调用改方法,列出未被热门的用户分享,以供管理员审核成热门
     * @param pager
     * @param userId
     * @param catId
     * @param zuiWhat
     * @param mediaType
     * @param timeAgo
     * @return
     * @throws Exception
     */
    public List<CoreUrl> fetchUnHotWebList(Integer pageNum, Long zuiWhat, Long agoTimeByHour) throws Exception {
        Pager<CoreUrl> pager = new Pager<CoreUrl>((null == pageNum) ? 1 : pageNum, 10, 500);
        Long timeInMillis = agoTimeByHour * 60 * 60 * 1000;
        Long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        Date timeAgo = new Date(currentTimeInMillis - timeInMillis);
        List<CoreUrl> unHotWebList = hotWebDao.fetchUnHotWebList(pager, zuiWhat, timeAgo);
        return unHotWebList;
    }

    /**
     * 
     * @param catId
     * @param mediaType
     * @param timeAgo
     * @return
     * @throws Exception
     */
    public Integer fetchUnHotWebCount(Long agoTimeByHour) throws Exception {
        Long timeInMillis = agoTimeByHour * 60 * 60 * 1000;
        Long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        Date timeAgo = new Date(currentTimeInMillis - timeInMillis);
        return hotWebDao.fetchUnHotWebCount(timeAgo);
    }

    public void updateTopCatIdByCatId(HotWebCat hotWebCat) throws Exception {
        hotWebDao.updateTopCatIdByCatId(hotWebCat);
    }
}
