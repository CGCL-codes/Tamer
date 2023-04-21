package edu.tufts.vue.metadata.ui;

import java.util.*;
import javax.swing.*;
import edu.tufts.vue.metadata.*;
import edu.tufts.vue.ontology.*;
import edu.tufts.vue.metadata.gui.*;

public class CategoryComboBoxModel extends DefaultComboBoxModel {

    public static final String ERROR_LABEL = "Not Found";

    Object selectedItem;

    CategoryModel vueCategoryModel;

    EditCategoryItem editCategory = new EditCategoryItem();

    /** Creates a new instance of CategoryComboBoxModel */
    public CategoryComboBoxModel() {
        vueCategoryModel = tufts.vue.VUE.getCategoryModel();
        setSelectedItem(getElementAt(0));
    }

    public Object getSelectedItem() {
        return selectedItem;
    }

    public List<Ontology> getOntologuies() {
        return vueCategoryModel;
    }

    public void setSelectedItem(Object newValue) {
        selectedItem = newValue;
    }

    public int getSize() {
        int count = 0;
        for (Ontology ont : vueCategoryModel) {
            count += ont.getOntTypes().size();
            count++;
        }
        count++;
        return count;
    }

    public Object getElementAt(int i) {
        if (i == (getSize() - 1)) {
            return editCategory;
        }
        for (Ontology ont : vueCategoryModel) {
            if (i < ont.getOntTypes().size()) {
                return ont.getOntTypes().get(i);
            } else if (i == ont.getOntTypes().size()) {
                return new ComboBoxSeparator();
            } else {
                i -= ont.getOntTypes().size();
                i--;
            }
        }
        return ERROR_LABEL;
    }

    public void refresh() {
        fireContentsChanged(this, 0, getSize());
    }
}
