package com.freshbooks.model;

import java.util.ArrayList;

public class Categories extends PagedResponseContent<Category> {

    public ArrayList<Category> getCategories() {
        return getContents();
    }
}
