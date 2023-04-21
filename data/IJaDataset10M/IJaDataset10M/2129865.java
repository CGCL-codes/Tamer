package com.liusoft.dlog4j.dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import com.liusoft.dlog4j.base._BeanBase;
import com.liusoft.dlog4j.beans.SiteBean;
import com.liusoft.dlog4j.beans.TagBean;

/**
 * ���ʱ�ǩ����ݿ�ӿ�
 * @author Winter Lau
 */
public class TagDAO extends DAO {

    /**
	 * ��ȡ��ǩ��
	 * @param site
	 * @return
	 */
    public static int getTagCount(SiteBean site) {
        String hql = "SELECT COUNT(*) FROM TagBean t";
        if (site != null) {
            hql += " WHERE t.site.id = ?";
            return executeStatAsInt(hql, site.getId());
        }
        return executeStatAsInt(hql, null);
    }

    /**
	 * �������ų̶��г���ǩ
	 * @param site
	 * @param fromIdx
	 * @param count
	 * @return
	 */
    public static List listTags(SiteBean site, int fromIdx, int count) {
        StringBuffer hql = new StringBuffer("SELECT t.name,COUNT(*) FROM TagBean t");
        if (site != null) hql.append(" WHERE t.site.id = ?");
        hql.append(" GROUP BY t.name ORDER BY 2 DESC");
        Query query = getSession().createQuery(hql.toString());
        if (site != null) query.setInteger(0, site.getId());
        if (fromIdx > 0) query.setFirstResult(fromIdx);
        if (count > 0) query.setMaxResults(count);
        List tags = new ArrayList();
        List results = query.list();
        for (int i = 0; results != null && i < results.size(); i++) {
            tags.add(((Object[]) results.get(i))[0]);
        }
        return tags;
    }

    /**
	 * ɾ��ĳ������ı�ǩ�б�
	 * @param ref_id
	 * @param ref_type
	 * @see com.liusoft.dlog4j.dao.DiaryDAO#update(DiaryBean, boolean)
	 * @see com.liusoft.dlog4j.dao.PhotoDAO#update(int, PhotoBean, String)
	 * @see com.liusoft.dlog4j.dao.BBSTopicDAO#update(TopicBean)
	 */
    public static void deleteTagByRefId(int ref_id, int ref_type) {
        executeNamedUpdate("DELETE_TAG", ref_id, ref_type);
    }

    /**
	 * �г����ŵı�ǩ
	 * @param site ���ò���Ϊnull���г����еı�ǩ
	 * @return
	 */
    public static List listHotTags(SiteBean site, int count) {
        StringBuffer hql = new StringBuffer("SELECT t.name,COUNT(*) FROM TagBean t");
        if (site != null) hql.append(" WHERE t.site.id = ?");
        hql.append(" GROUP BY t.name ORDER BY 2 DESC");
        Query query = getSession().createQuery(hql.toString());
        if (site != null) query.setInteger(0, site.getId());
        query.setMaxResults(count);
        List tags = new ArrayList();
        List results = query.list();
        for (int i = 0; results != null && i < results.size(); i++) {
            tags.add(((Object[]) results.get(i))[0]);
        }
        return tags;
    }

    /**
	 * ��ȡָ��ĳ����ǩ���ռ���
	 * @param site
	 * @param tag
	 * @return
	 */
    public static int getDiaryCountForTag(SiteBean site, String tag) {
        if (site != null) return gettObjectCountForTag(site, TagBean.TYPE_DIARY, tag);
        return getObjectCountForTag(TagBean.TYPE_DIARY, tag);
    }

    /**
	 * ��ȡָ��ĳ����ǩ����Ƭ��
	 * @param site
	 * @param tag
	 * @return
	 */
    public static int getPhotoCountForTag(SiteBean site, String tag) {
        if (site != null) return gettObjectCountForTag(site, TagBean.TYPE_PHOTO, tag);
        return getObjectCountForTag(TagBean.TYPE_PHOTO, tag);
    }

    /**
	 * ��ȡָ��ĳ����ǩ��������
	 * @param site
	 * @param tag
	 * @return
	 */
    public static int getTopicCountForTag(SiteBean site, String tag) {
        if (site != null) return gettObjectCountForTag(site, TagBean.TYPE_BBS, tag);
        return getObjectCountForTag(TagBean.TYPE_BBS, tag);
    }

    /**
	 * �г�ָ����ǩ�������ռ�
	 * @param site
	 * @param tagname
	 * @return
	 */
    public static List listDiaryForTag(SiteBean site, String tagname, int fromIdx, int count) {
        return listObjectsForTags(site, "DiaryOutlineBean", TagBean.TYPE_DIARY, tagname, fromIdx, count);
    }

    /**
	 * �г�ָ����ǩ��������Ƭ
	 * @param site
	 * @param tagname
	 * @param fromIdx
	 * @param count
	 * @return
	 */
    public static List listPhotosForTag(SiteBean site, String tagname, int fromIdx, int count) {
        return listObjectsForTags(site, "PhotoOutlineBean", TagBean.TYPE_PHOTO, tagname, fromIdx, count);
    }

    /**
	 * �г�ָ����ǩ��������̳����
	 * @param site
	 * @param tagname
	 * @param fromIdx
	 * @param count
	 * @return
	 */
    public static List listTopicsForTag(SiteBean site, String tagname, int fromIdx, int count) {
        return listObjectsForTags(site, "TopicOutlineBean", TagBean.TYPE_BBS, tagname, fromIdx, count);
    }

    /**
	 * �г�ָ����ǩ�������ռ�
	 * @param site
	 * @param tagname
	 * @return
	 */
    public static List listDiaryForTag(String tagname, int fromIdx, int count) {
        return listObjectsForTags("DiaryOutlineBean", TagBean.TYPE_DIARY, tagname, fromIdx, count);
    }

    /**
	 * �г�ָ����ǩ��������Ƭ
	 * @param site
	 * @param tagname
	 * @param fromIdx
	 * @param count
	 * @return
	 */
    public static List listPhotosForTag(String tagname, int fromIdx, int count) {
        return listObjectsForTags("PhotoOutlineBean", TagBean.TYPE_PHOTO, tagname, fromIdx, count);
    }

    /**
	 * �г�ָ����ǩ��������̳����
	 * @param site
	 * @param tagname
	 * @param fromIdx
	 * @param count
	 * @return
	 */
    public static List listTopicsForTag(String tagname, int fromIdx, int count) {
        return listObjectsForTags("TopicOutlineBean", TagBean.TYPE_BBS, tagname, fromIdx, count);
    }

    /**
	 * �г���ǩ��ָ��Ķ���
	 * @param site
	 * @param beanName
	 * @param tagname
	 * @param fromIdx
	 * @param count
	 * @return
	 */
    protected static List listObjectsForTags(SiteBean site, String beanName, int objType, String tagname, int fromIdx, int count) {
        StringBuffer hql = new StringBuffer("FROM ");
        hql.append(beanName);
        hql.append(" AS d WHERE d.status=? AND d.site.id=? AND d.id IN (SELECT t.refId FROM TagBean AS t WHERE t.site.id=? AND t.refType=? AND t.name=?) ORDER BY d.id DESC");
        Integer siteId = new Integer(site.getId());
        return executeQuery(hql.toString(), fromIdx, count, new Object[] { _BeanBase.I_STATUS_NORMAL, siteId, siteId, new Integer(objType), tagname });
    }

    /**
	 * �г���ǩ��ָ��Ķ���
	 * @param beanName
	 * @param tagname
	 * @param fromIdx
	 * @param count
	 * @return
	 */
    protected static List listObjectsForTags(String beanName, int objType, String tagname, int fromIdx, int count) {
        StringBuffer hql = new StringBuffer("FROM ");
        hql.append(beanName);
        hql.append(" AS d WHERE d.status=? AND d.id IN (SELECT t.refId FROM TagBean AS t WHERE t.refType=? AND t.name=?) ORDER BY d.id DESC");
        return executeQuery(hql.toString(), fromIdx, count, new Object[] { _BeanBase.I_STATUS_NORMAL, new Integer(objType), tagname });
    }

    /**
	 * �г���ǩ��ָ��Ķ������
	 * @param site
	 * @param beanName
	 * @param tagname
	 * @param fromIdx
	 * @param count
	 * @return
	 */
    protected static int gettObjectCountForTag(SiteBean site, int objType, String tagname) {
        return executeNamedStatAsInt("TAG_COUNT_OF_SITE", new Object[] { new Integer(site.getId()), new Integer(objType), tagname });
    }

    /**
	 * �г���ǩ��ָ��Ķ������
	 * @param beanName
	 * @param tagname
	 * @param fromIdx
	 * @param count
	 * @return
	 */
    protected static int getObjectCountForTag(int objType, String tagname) {
        return executeNamedStatAsInt("TAG_COUNT", new Object[] { new Integer(objType), tagname });
    }
}
