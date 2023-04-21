package com.hibernate.daoimp.product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.common.Constant;
import com.common.Page;
import com.hibernate.dao.product.ProductDao;
import com.hibernate.pojo.Articles;
import com.hibernate.pojo.Product;
import com.struts.form.ProductForm;

public class ProductDaoImp extends HibernateDaoSupport implements ProductDao {

    private boolean bool;

    private List list;

    public int record_count = 0;

    public Map findProducts(int page, final int productleibieID) {
        list = null;
        Map hashmap = new HashMap();
        record_count = page * Constant.PAGE_VIEW_COUNT;
        list = this.getHibernateTemplate().find("select count(*) from Product where categry_id=" + productleibieID);
        Integer counts = (Integer) list.get(0);
        Page pageobject = new Page();
        Map map = pageobject.pageCount(counts);
        map.put("pagecur", page);
        map.put("productleibieID", productleibieID);
        hashmap.put("page_content", map);
        list = getHibernateTemplate().executeFind(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String hql_news;
                Query query;
                hql_news = " from Product where categry_id=? order by ID desc";
                query = session.createQuery(hql_news);
                query.setInteger(0, productleibieID);
                query.setFirstResult(record_count);
                query.setMaxResults(Constant.PAGE_VIEW_COUNT);
                List list = query.list();
                return list;
            }
        });
        hashmap.put("product_list", list);
        return hashmap;
    }

    public Long saveProduct(Product product) {
        Long id = 0L;
        bool = false;
        try {
            product.setCreate_time(new Date());
            product.setEdit_time(new Date());
            this.getHibernateTemplate().save(product);
            bool = true;
        } catch (Exception e) {
            bool = false;
            e.printStackTrace();
        }
        if (bool) {
            String temp = product.getProduct_name();
            list = findProduct("from Product order by id desc limit 0,0");
            Product product_temp = null;
            product_temp = (Product) list.get(0);
            id = product_temp.getId();
            System.out.println(id);
        }
        return id;
    }

    public boolean updateProductAudit(ProductForm productform) {
        bool = false;
        try {
            Product product = (Product) this.getHibernateTemplate().load(Product.class, productform.getId());
            product.setProduct_audit(productform.getProduct_audit());
            this.getHibernateTemplate().update(product);
            bool = true;
        } catch (Exception e) {
            bool = false;
            e.printStackTrace();
        } finally {
            return bool;
        }
    }

    public List findProduct(String queryString) {
        return this.getHibernateTemplate().find(queryString);
    }

    public boolean deleteProduct(final Long id) {
        bool = false;
        try {
            getHibernateTemplate().execute(new HibernateCallback() {

                public Object doInHibernate(Session session) throws HibernateException, SQLException {
                    String hql_news;
                    Query query;
                    hql_news = "delete from ProductAndProductAtrribute where product_id=?";
                    query = session.createQuery(hql_news);
                    query.setLong(0, id);
                    int n = query.executeUpdate();
                    return n;
                }
            });
            Product product = (Product) this.getHibernateTemplate().load(Product.class, id);
            this.getHibernateTemplate().delete(product);
            bool = true;
        } catch (Exception e) {
            bool = false;
            e.printStackTrace();
        }
        return bool;
    }

    public List updateProductView(Long id) {
        list = new ArrayList();
        Product product;
        product = (Product) this.getHibernateTemplate().load(Product.class, id);
        list.add(product);
        list.add(product.getProduct_content());
        list.add(product.getCategry_id());
        return list;
    }

    public List updateProduct(ProductForm productform, String newfilename) {
        Product product = (Product) this.getHibernateTemplate().load(Product.class, productform.getId());
        product.setProduct_name(productform.getProduct_name());
        product.setKeyword(productform.getKeyword());
        product.setProduct_depict(productform.getProduct_depict());
        product.setProduct_content(productform.getProduct_content());
        product.setProduct_author(productform.getProduct_author());
        product.setProduct_price(productform.getProduct_price());
        product.setProduct_count(productform.getProduct_count());
        product.setProduct_recommend(productform.getProduct_recommend());
        if (!newfilename.equals("")) {
            product.setProduct_pic(newfilename);
        }
        List list = this.getHibernateTemplate().find("select new Map(p.id as productId,pa.atrribute_name as atrribute_name,ppa.id as ppaId,ppa.atrribute_value as atrribute_value) from Product p,ProductAtrribute pa,ProductAndProductAtrribute ppa where p.id=ppa.product_id and pa.id=ppa.atrribute_id and p.id=" + product.getId());
        this.getHibernateTemplate().save(product);
        return list;
    }
}
