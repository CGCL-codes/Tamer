package com.wangyu001.logic;

import java.util.List;
import com.wangyu001.entity.CoreUrl;
import com.wangyu001.entity.HotUrl;
import com.wangyu001.entity.HotUrlCat;

public interface HotUrlLogic {

    /**
     * create
     * 
     * @param web
     * @return 
     * 
     * @author JN
     */
    public void create(HotUrl hotUrl) throws Exception;

    /**
     * delete
     * 
     * @param webId
     * @return 
     * 
     * @author JN
     */
    public boolean remove(Long webId) throws Exception;

    /**
     * fetch
     * 
     * @param webId
     * @return 
     * 
     * @author JN
     */
    public HotUrl fetch(Long webId) throws Exception;

    /**
     * 
     * @param pager
     * @return
     */
    public List<CoreUrl> fetchHotUrlList(Integer pageNum, Long userId, Long catId, Long zuiXX, Long mediaType, Long agoTimeByHour) throws Exception;

    /**
     * 
     * @param catId
     * @param mediaType
     * @param agoTimeByHour
     * @return
     * @throws Exception
     */
    public Integer fetchHotUrlCount(Long catId, Long mediaType, Long agoTimeByHour) throws Exception;

    /**
     * 点击热门分享->管理->数据审核时调用改方法,列出未被热门的用户分享,以供管理员审核成热门
     * @param pager
     * @param userId
     * @param catId
     * @param zuiWhat
     * @param mediaType
     * @param timeAgo
     * @return
     */
    public List<CoreUrl> fetchUnHotUrlList(Integer pageNum, Long zuiWhat, Long agoTimeByHour) throws Exception;

    /**
     * 
     * @param catId
     * @param mediaType
     * @param timeAgo
     * @return
     * @throws Exception
     */
    public Integer fetchUnHotUrlCount(Long agoTimeByHour) throws Exception;

    /**
     * 根据catId更新hotUrl的topCatId字段
     * @param hotUrlCat
     * @throws Exception
     */
    public void updateTopCatIdByCatId(HotUrlCat hotUrlCat) throws Exception;
}
