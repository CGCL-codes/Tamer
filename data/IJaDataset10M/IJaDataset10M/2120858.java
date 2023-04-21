package com.city.itis.dao.impl;

import java.sql.SQLException;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import com.city.itis.dao.SiteDao;
import com.city.itis.domain.Member;
import com.city.itis.domain.Site;
import com.city.itis.domain.SiteCategory;

/**
 * Site Dao 实现类
 * @author WY
 *
 */
@Component("siteDao")
public class SiteDaoImpl implements SiteDao {

    private HibernateTemplate hibernateTemplate;

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    @Resource
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Override
    public int save(final Site site) {
        try {
            SiteCategory siteCategory = (SiteCategory) this.hibernateTemplate.get(SiteCategory.class, site.getCategory().getCategoryNo());
            if (siteCategory != null) {
                if (site.getMember().getId() != null) {
                    Member m = (Member) this.hibernateTemplate.get(Member.class, site.getMember().getId());
                    site.setMember(m);
                } else {
                    site.getMember().setId(0);
                }
                site.setCategory(siteCategory);
                this.hibernateTemplate.execute(new HibernateCallback() {

                    public Object doInHibernate(Session session) throws HibernateException, SQLException {
                        String sql = "insert into Site(siteName,siteDesc,categoryNo,address,city,contactName,tel,fax,mobile,webSite,email,photoName,lat,lng,showStatus,memberId) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        Query query = session.createSQLQuery(sql);
                        query.setString(0, site.getSiteName());
                        query.setString(1, site.getSiteDesc());
                        query.setInteger(2, site.getCategory().getCategoryNo());
                        query.setString(3, site.getAddress());
                        query.setString(4, site.getCity());
                        query.setString(5, site.getContactName());
                        query.setString(6, site.getTel());
                        query.setString(7, site.getFax());
                        query.setString(8, site.getMobile());
                        query.setString(9, site.getWebSite());
                        query.setString(10, site.getEmail());
                        query.setString(11, site.getPhotoName());
                        query.setDouble(12, site.getLat());
                        query.setDouble(13, site.getLng());
                        query.setString(14, site.getShowStatus());
                        query.setInteger(15, site.getMember().getId());
                        query.executeUpdate();
                        return 1;
                    }
                });
            }
            return 1;
        } catch (Exception e) {
        }
        return 1;
    }

    @Override
    public int delete(Site site) {
        SiteCategory siteCategory = (SiteCategory) this.hibernateTemplate.get(SiteCategory.class, site.getCategory().getCategoryNo());
        if (siteCategory != null) {
            if (site.getMember().getId() != null) {
                Member m = (Member) this.hibernateTemplate.get(Member.class, site.getMember().getId());
                site.setMember(m);
            } else {
                site.setMember(null);
            }
            site.setCategory(siteCategory);
            try {
                this.hibernateTemplate.delete(site);
                return 1;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    @Override
    public int deleteById(Integer id) {
        Site site = (Site) this.hibernateTemplate.get(Site.class, id);
        if (site != null) {
            return delete(site);
        }
        return 0;
    }

    @Override
    public int deleteByMemberId(Integer id) {
        final Member m = (Member) this.getHibernateTemplate().get(Member.class, id);
        if (m != null) {
            this.hibernateTemplate.execute(new HibernateCallback() {

                public Object doInHibernate(Session session) throws HibernateException, SQLException {
                    String queryString = "delete FROM Site s where s.member='" + m.getId() + "'";
                    Query query = session.createQuery(queryString);
                    query.executeUpdate();
                    return 1;
                }
            });
        }
        return 0;
    }

    @Override
    public int update(final Site site) {
        SiteCategory siteCategory = (SiteCategory) this.hibernateTemplate.get(SiteCategory.class, site.getCategory().getCategoryNo());
        if (siteCategory != null) {
            site.setCategory(siteCategory);
            if (site.getMember().getId() != null) {
                Member m = (Member) this.hibernateTemplate.get(Member.class, site.getMember().getId());
                site.setMember(m);
            } else {
                site.getMember().setId(0);
            }
            try {
                this.hibernateTemplate.execute(new HibernateCallback() {

                    public Object doInHibernate(Session session) throws HibernateException, SQLException {
                        String sql = "update  Site set siteName=?,siteDesc=?,categoryNo=?,address=?,city=?,contactName=?,tel=?,fax=?,mobile=?,webSite=?,email=?,photoName=?,lat=?,lng=?,showStatus=?,memberId=? where siteNo=?";
                        Query query = session.createSQLQuery(sql);
                        query.setString(0, site.getSiteName());
                        query.setString(1, site.getSiteDesc());
                        query.setInteger(2, site.getCategory().getCategoryNo());
                        query.setString(3, site.getAddress());
                        query.setString(4, site.getCity());
                        query.setString(5, site.getContactName());
                        query.setString(6, site.getTel());
                        query.setString(7, site.getFax());
                        query.setString(8, site.getMobile());
                        query.setString(9, site.getWebSite());
                        query.setString(10, site.getEmail());
                        query.setString(11, site.getPhotoName());
                        query.setDouble(12, site.getLat());
                        query.setDouble(13, site.getLng());
                        query.setString(14, site.getShowStatus());
                        query.setInteger(15, site.getMember().getId());
                        query.setInteger(16, site.getSiteNo());
                        query.executeUpdate();
                        return 1;
                    }
                });
            } catch (Exception e) {
            }
        }
        return 1;
    }

    @Override
    public Site getSiteById(Integer id) {
        Site site = (Site) this.hibernateTemplate.get(Site.class, id);
        if (site != null) {
            return site;
        }
        return null;
    }

    @Override
    public Site getSiteBySiteName(String siteName) {
        String queryString = "FROM Site s where s.siteName=?";
        @SuppressWarnings("unchecked") List<Site> siteList = (List<Site>) this.getHibernateTemplate().find(queryString, siteName);
        if (siteList != null) {
            for (Site s : siteList) {
                return s;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Site> findAll() {
        String queryString = "FROM Site s order by s.siteNo desc";
        List<Site> siteList = this.hibernateTemplate.find(queryString);
        if (siteList != null) {
            return siteList;
        }
        return null;
    }

    @Override
    public List<Site> findAllByCategoryNo(Integer categoryNo) {
        SiteCategory category = (SiteCategory) this.hibernateTemplate.get(SiteCategory.class, categoryNo);
        if (category != null) {
            String queryString = "FROM Site s where s.category=? order by s.siteNo desc";
            @SuppressWarnings("unchecked") List<Site> siteList = this.hibernateTemplate.find(queryString, category);
            if (siteList != null) {
                return siteList;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Site> getListByCategoryAndPosition(final int categoryNo, final double lat, final double lng, final double area) {
        List<Site> siteList = null;
        String queryString = "FROM Site  where CategoryNo=? and 2*asin(sqrt(pow(sin((lng-?)/2),2) +cos(lat)*cos(?)*sin(pow((lat-?)/2,2) )))*6378.137<=?";
        Object values[] = { categoryNo, lng, lat, lat, area };
        siteList = this.getHibernateTemplate().find(queryString, values);
        if (siteList != null) {
            return siteList;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Site> getListByNameAndPosition(String name, double lat, double lng, double area) {
        List<Site> siteList = null;
        String queryString = "FROM Site  where siteName=? and 2*asin(sqrt(pow(sin((lng-?)/2),2) +cos(lat)*cos(?)*sin(pow((lat-?)/2,2) )))*6378.137<=?";
        Object values[] = { name, lng, lat, lat, area };
        siteList = this.getHibernateTemplate().find(queryString, values);
        if (siteList != null) {
            return siteList;
        } else {
            return null;
        }
    }

    @Override
    public List<Site> listByMemberId(Integer memberId) {
        Member member = (Member) this.hibernateTemplate.get(Member.class, memberId);
        if (member != null) {
            String queryString = "FROM Site s where s.member=? order by s.siteNo desc";
            @SuppressWarnings("unchecked") List<Site> siteList = this.hibernateTemplate.find(queryString, member);
            if (siteList != null) {
                return siteList;
            }
        }
        return null;
    }
}
