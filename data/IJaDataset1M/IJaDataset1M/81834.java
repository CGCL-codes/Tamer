package com.zhiyun.admin.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.zhiyun.admin.common.EntityDaoNotFindIdException;
import com.zhiyun.admin.common.Page;
import com.zhiyun.admin.vo.EbShop;

public class ShopDao extends BaseDao {

    private static Log log = LogFactory.getLog(ShopDao.class);

    @SuppressWarnings("unchecked")
    public List<EbShop> getAllShops() {
        try {
            return (List<EbShop>) getHibernateTemplate().find("from EbShop where status=0");
        } catch (Exception e) {
            log.info("======>[ShopDao.getAllShops()] Exception");
            e.printStackTrace();
            throw new EntityDaoNotFindIdException("find all shops failed!");
        }
    }

    @SuppressWarnings("unchecked")
    public List<EbShop> findById(String id) {
        String hql = "from EbShop where id=?";
        try {
            return (List<EbShop>) getHibernateTemplate().find(hql, id);
        } catch (Exception e) {
            log.info("======>[ShopDao.findById()] Exception");
            e.printStackTrace();
            throw new EntityDaoNotFindIdException("find by id failed!");
        }
    }

    @SuppressWarnings("unchecked")
    public List<EbShop> findByName(String shopName) {
        String hql = "from EbShop where status=0 and shopName=?";
        try {
            return (List<EbShop>) getHibernateTemplate().find(hql, shopName);
        } catch (Exception e) {
            log.info("======>[ShopDao.findByName()] Exception");
            e.printStackTrace();
            throw new EntityDaoNotFindIdException("find by name failed!");
        }
    }

    @SuppressWarnings("unchecked")
    public List<EbShop> checkNameRepeated(String shopName, String id) {
        String hql = "from EbShop where status=0 and id<>? and shopName=?";
        List<Object> params = new ArrayList<Object>();
        params.add(id);
        params.add(shopName);
        try {
            return (List<EbShop>) getHibernateTemplate().find(hql, params.toArray());
        } catch (Exception e) {
            log.info("======>[ShopDao.checkNameRepeated()] Exception");
            e.printStackTrace();
            throw new EntityDaoNotFindIdException("check name repeated failed!");
        }
    }

    public Page searchShop(Map<String, Object> paramMap, Page page) {
        List<Object> params = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer();
        hql.append("from EbShop where 1=1 ");
        try {
            for (String s : paramMap.keySet()) {
                if (s.equals("id")) {
                    hql.append("and " + s + " like ? ");
                    params.add(paramMap.get(s));
                }
                if (s.equals("city.id")) {
                    hql.append("and " + s + "=? ");
                    params.add(paramMap.get(s));
                }
                if (s.equals("shopName")) {
                    hql.append("and " + s + " like ? ");
                    params.add(paramMap.get(s));
                }
            }
            hql.append("and status=0");
            return this.listPage(page, hql.toString(), params);
        } catch (Exception e) {
            log.info("======>[ShopDao.searchShop()] Exception");
            e.printStackTrace();
            throw new EntityDaoNotFindIdException("Search shop failed!");
        }
    }
}
