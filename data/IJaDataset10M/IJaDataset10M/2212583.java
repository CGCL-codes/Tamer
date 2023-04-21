package sg.edu.nus.iss.se07.bc.category;

import java.util.ArrayList;

/**
 * Category Collection Class
 *
 * @author Isak Rabin
 * @version 0.1
 * @since v0.1 (21/03/2009)
 *<p>
 * <b>ChangeLog</b><br>
 * v 0.1
 * <ul>
 *      <li></li>
 * </ul>
 *</p>
 */
public class CategorySet {

    private ArrayList<Category> set = null;

    public CategorySet() {
        set = new ArrayList<Category>();
    }

    public void add(Category dataObject) {
        set.add(dataObject);
    }

    public Category get(int index) {
        if (index < length()) {
            Category dataObject = set.get(index);
            return dataObject;
        } else {
            return null;
        }
    }

    public int length() {
        return set.size();
    }
}
