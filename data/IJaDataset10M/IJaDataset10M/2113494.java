package com.u2d.crpmodel;

import com.u2d.model.*;
import com.u2d.app.PersistenceMechanism;
import com.u2d.type.*;
import com.u2d.type.atom.*;
import com.u2d.type.composite.*;
import com.u2d.list.RelationalList;
import com.u2d.reflection.Fld;
import com.u2d.persist.Persist;

@Persist
public class ProductAttribute extends AbstractComplexEObject {

    /** Creates a new instance of ProductAttribute */
    public ProductAttribute() {
    }

    public void initialize() {
        attributeCategory.setValue(attributeCategory.list().get(0));
    }

    public Title title() {
        StringBuffer text = new StringBuffer();
        text.append(attributeCategory.title() + ": " + attributeValue.title());
        return new Title(text.toString());
    }

    private final ProductAttributeCategory attributeCategory = new ProductAttributeCategory();

    @Fld(label = "Attribute Category")
    public ProductAttributeCategory getCategory() {
        return attributeCategory;
    }

    private final StringEO attributeValue = new StringEO();

    public StringEO getAttributeValue() {
        return attributeValue;
    }

    private final RelationalList productGroups = new RelationalList(ProductGroup.class);

    public static final Class productGroupsType = ProductGroup.class;

    public static String productGroupsInverseFieldName = "productAttributes";

    public static final int productGroupsRelationType = PersistenceMechanism.MANY_TO_MANY;

    public static final boolean productGroupsRelationIsInverse = true;

    public RelationalList getProductGroups() {
        return productGroups;
    }

    private final RelationalList products = new RelationalList(Product.class);

    public static final Class productsType = Product.class;

    public static String productsInverseFieldName = "productAttributes";

    public static final int productsRelationType = PersistenceMechanism.MANY_TO_MANY;

    public static final boolean productsRelationIsInverse = true;

    public RelationalList getProducts() {
        return products;
    }

    public static String[] fieldOrder = { "attributeCategory", "attributeValue", "productGroups", "products" };

    public static String[] tabViews = { "productGroups", "products" };

    public static java.awt.Color colorCode = new java.awt.Color(0x6666ff);
}
