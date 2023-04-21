package org.xmlvm.iphone;

import java.util.ArrayList;
import org.xmlvm.XMLVMSkeletonOnly;

@XMLVMSkeletonOnly
public class SKProductsResponse extends NSObject {

    private ArrayList<String> invalidProductIdentifiers;

    private ArrayList<SKProduct> products;

    public ArrayList<String> getInvalidProductIdentifiers() {
        return invalidProductIdentifiers;
    }

    public ArrayList<SKProduct> getProducts() {
        return products;
    }
}
