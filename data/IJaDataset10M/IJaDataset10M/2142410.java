package org.blueoxygen.lotion.category.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.blueoxygen.lotion.CategoryC;
import org.blueoxygen.lotion.Contact;

public class DeleteCategory extends CategoryForm {

    public String execute() {
        CategoryC temp = new CategoryC();
        Contact temp2 = new Contact();
        category = (CategoryC) manager.getById(CategoryC.class, getId());
        categories = (ArrayList<CategoryC>) manager.getList("FROM " + CategoryC.class.getSimpleName() + " as ca " + "WHERE ca.parent.id='" + category.getId() + "'", null, null);
        Iterator iter = categories.iterator();
        while (iter.hasNext()) {
            temp = (CategoryC) iter.next();
            temp.setParent(null);
            manager.save(temp);
        }
        String query = "FROM " + Contact.class.getSimpleName() + " as co " + "WHERE co.category.id='" + category.getId() + "'";
        List<Contact> contacts = (ArrayList<Contact>) manager.getList(query, null, null);
        iter = contacts.iterator();
        while (iter.hasNext()) {
            temp2 = (Contact) iter.next();
            temp2.setCategory(null);
            manager.save(temp2);
        }
        manager.remove(category);
        return SUCCESS;
    }
}
