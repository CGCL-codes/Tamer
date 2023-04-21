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
public class ProductRelation extends AbstractComplexEObject {

    /** Creates a new instance of ProductRelation */
    public ProductRelation() {
    }

    public Title title() {
        StringBuffer text = new StringBuffer();
        text.append("[ProductRelation]");
        return new Title(text.toString());
    }

    private final RelationalList sourceProducts = new RelationalList(Product.class);

    public static final Class sourceProductsType = Product.class;

    public static String sourceProductsInverseFieldName = "sourceProductRelations";

    public static final int sourceProductsRelationType = PersistenceMechanism.MANY_TO_MANY;

    public RelationalList getSourceProducts() {
        return sourceProducts;
    }

    private final RelationalList targetProducts = new RelationalList(Product.class);

    public static final Class targetProductsType = Product.class;

    public static String targetProductsInverseFieldName = "targetProductRelations";

    public static final int targetProductsRelationType = PersistenceMechanism.MANY_TO_MANY;

    public static final boolean targetProductsRelationIsInverse = true;

    public RelationalList getTargetProducts() {
        return targetProducts;
    }

    public static String[] fieldOrder = { "sourceProducts", "targetProducts" };

    public static java.awt.Color colorCode = new java.awt.Color(0x33ffff);
}
