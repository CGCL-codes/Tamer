package org.fm.addressbook.converter;

import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.fm.addressbook.bean.CategoryBean;
import org.fm.addressbook.businessobject.Category;
import org.fm.addressbook.util.FacesUtil;

/**
 * JContact- online Address Book Management System<a
 * href="http://jcontact.sourceforge.net/">http://jcontact.sourceforge.net/</a>
 * <p>
 * Licensed under the terms of any of the following licenses at your choice: -
 * <br/> GNU General Public License Version 2 or later (the "GPL") <a
 * href="http://www.gnu.org/licenses/gpl.html">http://www.gnu.org/licenses/gpl.html</a> -
 * <br/>GNU Lesser General Public License Version 2.1 or later (the "LGPL") <a
 * href="http://www.gnu.org/licenses/lgpl.html">http://www.gnu.org/licenses/lgpl.html</a>
 * <p>
 * JSF converter for Category
 * <p>
 * 
 * @author <a href="mailto:tennyson.varghese@yahoo.co.in">Tennyson Varghese</a>
 *         <br/> <a href="mailto:aneeshadoor2003@yahoo.co.in">Aneesh S</a>
 */
public class CategoryConverter implements Converter {

    CategoryBean categoryBean = null;

    @Override
    public Object getAsObject(FacesContext fc, UIComponent component, String value) {
        Category category = null;
        if (value == null || value.equals("")) {
            return category;
        }
        try {
            category = new Category();
            category.setId(Long.parseLong(value));
            categoryBean = (CategoryBean) FacesUtil.getManagedBean("categoryBean");
            List<Category> lstCategory = categoryBean.getCategoryList();
            int pos = 0;
            if (lstCategory != null && (pos = lstCategory.indexOf(category)) != -1) {
                category = lstCategory.get(pos);
            }
        } catch (Exception e) {
        }
        return category;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent component, Object obj) {
        if (obj == null) return "All"; else return ((Category) obj).getId() + "";
    }
}
