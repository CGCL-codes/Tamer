package org.blueoxygen.cimande.category.actions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.blueoxygen.cimande.category.Category;
import org.blueoxygen.cimande.descriptors.Descriptor;
import org.blueoxygen.cimande.persistence.hibernate.HibernateSessionFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author dwi miyanto [ mee_andto@yahoo.com ]
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SearchCategory extends CategoryForm {

    @Autowired
    private HibernateSessionFactory hsf;

    private Session sess;

    private List categories = new ArrayList();

    private int maxPage, currPage, nextPage, prevPage = 0, page = 0;

    private int maxRowPerPage = 10;

    private String orderBy = "code";

    private int resultRows;

    protected Descriptor categoryDescriptor = new Descriptor();

    private String descriptor_id = "";

    private String iparentcode = "";

    protected Category parentCategory;

    public String execute() {
        try {
            sess = hsf.createSession();
            Criteria crit = sess.createCriteria(Category.class);
            categoryDescriptor = (Descriptor) manager.getById(Descriptor.class, getDescriptor_id());
            parentCategory = (Category) manager.getById(Category.class, getIparentcode());
            if (!getId().equalsIgnoreCase("")) {
                crit.add(Expression.like("id", "%" + getId() + "%"));
            }
            if (!getCode().equalsIgnoreCase("")) {
                crit.add(Expression.like("code", "%" + getCode() + "%"));
            }
            if (!getDescription().equalsIgnoreCase("")) {
                crit.add(Expression.like("description", "%" + getDescription() + "%"));
            }
            if (!getUrl_category_image().equalsIgnoreCase("")) {
                crit.add(Expression.like("url_category_image", "%" + getUrl_category_image() + "%"));
            }
            if (!getDescriptor_id().equalsIgnoreCase("")) {
                crit.add(Expression.like("categoryDescriptor.id", "%" + getDescriptor_id() + "%"));
            }
            if (!getIparentcode().equalsIgnoreCase("")) {
                crit.add(Expression.like("parentCategory.id", "%" + getIparentcode() + "%"));
            }
            if (getActiveFlag() != -1) {
                crit.add(Expression.eq("logInformation.activeFlag", new Integer(getActiveFlag())));
            }
            resultRows = crit.list().size();
            maxPage = resultRows / maxRowPerPage;
            if (resultRows % maxRowPerPage == 0) maxPage = maxPage - 1;
            categories = crit.addOrder(Order.asc(orderBy)).setFirstResult(currPage * maxRowPerPage).setMaxResults(maxRowPerPage).list();
            prevPage = currPage - 1;
            nextPage = currPage + 1;
            page = currPage + 1;
            hsf.endSession(sess);
            return SUCCESS;
        } catch (HibernateException e) {
            return ERROR;
        } catch (SQLException e) {
            return ERROR;
        } finally {
            try {
                hsf.closeSession(sess);
            } catch (HibernateException e1) {
                return ERROR;
            } catch (SQLException e1) {
                return ERROR;
            }
        }
    }

    public List getCategories() {
        return categories;
    }

    public void setCategories(List categories) {
        this.categories = categories;
    }

    public Descriptor getCategoryDescriptor() {
        return categoryDescriptor;
    }

    public void setCategoryDescriptor(Descriptor categoryDescriptor) {
        this.categoryDescriptor = categoryDescriptor;
    }

    public String getDescriptor_id() {
        return descriptor_id;
    }

    public void setDescriptor_id(String descriptor_id) {
        this.descriptor_id = descriptor_id;
    }

    public String getIparentcode() {
        return iparentcode;
    }

    public void setIparentcode(String iparentcode) {
        this.iparentcode = iparentcode;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getMaxRowPerPage() {
        return maxRowPerPage;
    }

    public void setMaxRowPerPage(int maxRowPerPage) {
        this.maxRowPerPage = maxRowPerPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public int getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }

    public int getResultRows() {
        return resultRows;
    }

    public void setResultRows(int resultRows) {
        this.resultRows = resultRows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
