package com.javaeye.lonlysky.lforum.service;

import java.util.List;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;
import com.javaeye.lonlysky.lforum.cache.LForumCache;
import com.javaeye.lonlysky.lforum.entity.forum.Announcements;

/**
 * 论坛公告操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class AnnouncementManager {

    private static final Logger logger = LoggerFactory.getLogger(AnnouncementManager.class);

    private SimpleHibernateTemplate<Announcements, Integer> announcementDAO;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        announcementDAO = new SimpleHibernateTemplate<Announcements, Integer>(sessionFactory, Announcements.class);
    }

    /**
	 * 获得全部指定时间段内的公告列表
	 * @param starttime 开始时间
	 * @param endtime 结束时间
	 * @return 公告列表
	 */
    @SuppressWarnings("unchecked")
    public List<Announcements> getAnnouncementList(String starttime, String endtime) {
        List<Announcements> announcemenList = LForumCache.getInstance().getListCache("AnnouncementList", Announcements.class);
        if (announcemenList == null) {
            announcemenList = announcementDAO.find("from Announcements where starttime<=? and endtime>=? order by displayorder,id desc", starttime, endtime);
            if (logger.isDebugEnabled()) {
                logger.debug("获得时间段" + starttime + " - " + endtime + "的公告列表");
            }
            LForumCache.getInstance().addCache("AnnouncementList", announcemenList);
        }
        return announcemenList;
    }

    /**
	 * 获得全部指定时间段内的前n条公告列表
	 * @param starttime 开始时间
	 * @param endtime 结束时间
	 * @param maxcount 最大记录数,小于0返回全部
	 * @return 公告列表
	 */
    @SuppressWarnings("unchecked")
    public List<Announcements> getSimplifiedAnnouncementList(String starttime, String endtime, int maxcount) {
        List<Announcements> announcemenList = LForumCache.getInstance().getListCache("SimplifiedAnnouncementList", Announcements.class);
        if (announcemenList == null) {
            announcemenList = announcementDAO.createQuery("from Announcements where starttime<=? and endtime>=? order by displayorder,id desc", starttime, endtime).setMaxResults(maxcount).list();
            LForumCache.getInstance().addCache("SimplifiedAnnouncementList", announcemenList);
        }
        return announcemenList;
    }
}
