package org.sergioveloso.spott.model.entity;

import java.util.Set;
import java.util.HashSet;

public class TestSuiteItemCategory implements Comparable<TestSuiteItemCategory> {

    protected Long id;

    protected String categoryName;

    protected String categoryDescription;

    protected Set<TestSuiteItem> items = new HashSet<TestSuiteItem>();

    protected TestSuiteItemCategory() {
    }

    public TestSuiteItemCategory(String categoryName, String categoryDescription) {
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int compareTo(TestSuiteItemCategory cat) {
        return getCategoryName().compareTo(cat.getCategoryName());
    }

    public Set<TestSuiteItem> getItems() {
        return items;
    }

    public void setItems(Set<TestSuiteItem> items) {
        this.items = items;
    }

    public int getNumberOfItems() {
        return getItems().size();
    }
}
