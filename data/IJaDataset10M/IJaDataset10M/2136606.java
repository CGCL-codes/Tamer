package com.dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import com.domain.Announcement;
import com.domain.User;

public class AnnouncementDAO extends BaseObjectDAO {

    public Announcement getAnnouncementByKeyId(Long keyId) {
        Announcement announcement = null;
        if ((keyId != null) && (keyId > 0)) {
            Criteria crit = this.getSession().createCriteria(Announcement.class);
            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            crit.add(Expression.eq("keyId", keyId));
            crit.add(Expression.eq("deleted", false));
            List<Announcement> results = crit.list();
            if (results.size() > 0) announcement = results.get(0); else return null;
        }
        return announcement;
    }

    public Announcement getAnnouncementByTitle(String title) {
        Announcement announcement = null;
        if ((title != null) && (title.equals(""))) {
            Criteria crit = this.getSession().createCriteria(Announcement.class);
            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            crit.add(Expression.eq("announcementTitle", title));
            crit.add(Expression.eq("deleted", false));
            List<Announcement> results = crit.list();
            announcement = results.get(0);
        }
        return announcement;
    }

    public Announcement save(Announcement announcement) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        announcement.setLastModDate(now);
        this.getSession().saveOrUpdate(announcement);
        this.getSession().flush();
        if ((announcement.getKeyId() != null)) {
            return announcement;
        } else return null;
    }

    public Announcement saveAnnouncement(Long keyId, User user, Timestamp startDate, Timestamp endDate, String announcementTitle, String announcementDescription, String announcementDetails, boolean publicViewable, boolean approved) {
        Announcement announcement = null;
        if ((keyId != null) && (keyId > 0)) {
            announcement = this.getAnnouncementByKeyId(keyId);
            if (announcement != null) {
                announcement.setStartDate(startDate);
                announcement.setEndDate(endDate);
                announcement.setAnnouncementTitle(announcementTitle);
                announcement.setAnnouncementDescription(announcementDescription);
                announcement.setAnnouncementDetails(announcementDetails);
                announcement.setPublicViewable(publicViewable);
                announcement.setApproved(approved);
                announcement.setLastUpdateUser(user);
                announcement = this.save(announcement);
            }
        } else {
            announcement = new Announcement();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            announcement.setStartDate(startDate);
            announcement.setEndDate(endDate);
            announcement.setAnnouncementTitle(announcementTitle);
            announcement.setAnnouncementDescription(announcementDescription);
            announcement.setAnnouncementDetails(announcementDetails);
            announcement.setPublicViewable(publicViewable);
            announcement.setApproved(approved);
            announcement.setCreationDate(now);
            announcement.setCreatedBy(user);
            announcement.setLastUpdateUser(user);
            announcement = this.save(announcement);
        }
        return announcement;
    }

    public List<Announcement> getAllAnnouncements() {
        Criteria crit = this.getSession().createCriteria(Announcement.class);
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Announcement> results = crit.list();
        if (results.size() > 0) return results; else return null;
    }

    public List<Announcement> getPendingAnnouncements() {
        Criteria crit = this.getSession().createCriteria(Announcement.class);
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        crit.add(Expression.eq("approved", Boolean.FALSE));
        crit.add(Expression.eq("deleted", false));
        List<Announcement> results = crit.list();
        if (results.size() > 0) return results; else return null;
    }

    public List<Announcement> getTopAnnouncements() {
        Criteria crit = this.getSession().createCriteria(Announcement.class);
        long oneDay = 86400000;
        long twoWeeks = 14 * oneDay;
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp then = new Timestamp(System.currentTimeMillis() + twoWeeks);
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        crit.add(Expression.eq("approved", Boolean.TRUE));
        crit.add(Expression.eq("deleted", false));
        crit.add(Expression.between("startDate", now, then));
        List<Announcement> results = crit.list();
        if (results.size() > 0) return results; else return null;
    }

    public List<Announcement> getAllUndeletedAnnouncements() {
        Criteria crit = this.getSession().createCriteria(Announcement.class);
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        crit.add(Expression.eq("deleted", false));
        List<Announcement> results = crit.list();
        if (results.size() > 0) return results; else return null;
    }
}
