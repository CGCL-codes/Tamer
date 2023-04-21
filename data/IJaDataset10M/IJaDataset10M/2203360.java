package com.cateshop.def;

import java.util.List;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import com.cateshop.IPagenation;
import com.cateshop.IntService;
import com.cateshop.IntServiceImpl;

/**
 * @author notXX
 */
public class CategoryService extends IntServiceImpl<Category> implements IntService<Category> {

    /**
     * 
     */
    public CategoryService() {
        super(Category.class);
    }

    /**
     * �б����и�����(�����಻���ڵ�����).
     * 
     * @return ��������������б�.
     */
    public List<? extends Category> listRoots() {
        return listRoots(null);
    }

    /**
     * �б����и�����(�����಻���ڵ�����).
     * 
     * @param pagenation
     *            ��ҳ.
     * @return ��������������б�.
     */
    public List<? extends Category> listRoots(IPagenation pagenation) {
        return list(Restrictions.isNull("parent"), pagenation, getDefaultOrders());
    }

    /**
     * �б����и������ŵ��ڸ�ֵ������.
     * 
     * @param parentId
     *            ��ĸ�������.
     * @param pagenation
     *            ��ҳ.
     * @return ��������������б�.
     */
    public List<? extends Category> list(int parentId, IPagenation pagenation) {
        return list(Restrictions.eq("parent.id", Integer.valueOf(parentId)), pagenation, getDefaultOrders());
    }

    /**
     * �б�������ư��ֵ������.
     * 
     * @param name
     *            ������.
     * @param pagenation
     *            ��ҳ.
     * @return ��������������б�.
     */
    public List<? extends Category> list(String name, IPagenation pagenation) {
        if (name == null) return list(pagenation); else return list(Restrictions.like("name", name, MatchMode.ANYWHERE), pagenation, getDefaultOrders());
    }

    /**
     * ��ȡ���ŵ�����.
     * 
     * @param ids
     *            ���.
     * @return �����б�.
     */
    public List<? extends Category> list(Integer[] ids) {
        return list(Restrictions.in("id", ids), null, getDefaultOrders());
    }
}
