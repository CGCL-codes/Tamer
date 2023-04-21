package com.spring.rssReader;

import com.spring.rssReader.util.IParentChild;
import com.spring.rssReader.util.IParentChildDao;
import com.spring.rssReader.util.ParentChildImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * User: Ronald
 * Date: Apr 22, 2004
 * Time: 5:22:13 PM
 */
public class Category extends ParentChildImpl implements ICategory {

    private Set channels;

    private Set items;

    public Category() {
    }

    public Category(String name) {
        setName(name);
    }

    public void moveTo(ICategory category) {
        if (category != null) {
            if (this.getParent() != null) {
                this.getParent().getChildren().remove(this);
            }
            category.addChild(this);
        }
    }

    public ICategory find(String categoryName) {
        if (this.getName().equals(categoryName)) {
            return this;
        }
        for (int i = 0; i < getChildren().size(); i++) {
            ICategory child = (ICategory) getChildren().get(i);
            ICategory found = child.find(child.getName());
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
	 * This method returns a comma seperated list with the id of this category and all of its children (and their
	 * children and their children and etc)
	 *
	 * @return
	 */
    public String getCategoryAndChildrenIds() {
        return getCategoryAndChildrenIds(this).toString();
    }

    private StringBuffer getCategoryAndChildrenIds(ICategory category) {
        StringBuffer sb = new StringBuffer();
        sb.append(category.getId());
        for (int i = 0; i < category.getChildren().size(); i++) {
            Category childCategory = (Category) category.getChildren().get(i);
            sb.append(", ").append(getCategoryAndChildrenIds(childCategory));
        }
        return sb;
    }

    public int delete(IParentChildDao dao) {
        return 0;
    }

    public Set getChannels() {
        return channels;
    }

    public void setChannels(Set channels) {
        this.channels = channels;
    }

    public Set getItems() {
        return items;
    }

    public void setItems(Set items) {
        this.items = items;
    }
}
