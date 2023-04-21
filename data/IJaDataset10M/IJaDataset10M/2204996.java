package sg.edu.nus.iss.se07.bc.product;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sg.edu.nus.iss.se07.common.NameValue;
import sg.edu.nus.iss.se07.common.Tuple8;
import sg.edu.nus.iss.se07.common.error.ErrorList;
import sg.edu.nus.iss.se07.common.exceptions.AppException;
import sg.edu.nus.iss.se07.da.ProductDA;

/**
 * A handler class for every process related to the product.
 *
 * @author Isak Rabin
 * @version 0.1
 * @since v0.1 (21/03/2009)
 *<p>
 * <b>ChangeLog</b><br>
 * v 0.1
 * <ul>
 *      <li>Add method create, list</li>
 * </ul>
 *</p>
 */
public class ProductFcd {

    public static final String MEMBER_DUPLICATE_ENTRY = "product.code.duplicate";

    private ProductDA productDA = null;

    public ProductFcd() {
    }

    /**
         *
         * @param product
         * @return
         * @throws AppException
         */
    public ErrorList create(Product product) throws AppException {
        ErrorList errorList = new ErrorList();
        ProductSet productSet = null;
        try {
            productSet = list(product.getProductID());
        } catch (AppException ex) {
            errorList.addError("[ProductManager::create]", ex.getMessage());
        }
        if (productSet != null) {
            if (productSet.length() != 0) {
                errorList.addError(MEMBER_DUPLICATE_ENTRY);
                return errorList;
            }
        }
        try {
            productDA = new ProductDA();
            productDA.writeData(product, true);
            productDA = null;
        } catch (AppException ex) {
            Logger.getLogger(ProductManager.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return errorList;
    }

    public ErrorList update(Product newCategory, Product oldCategory) {
        ErrorList errorList = new ErrorList();
        return errorList;
    }

    public ErrorList delete(Product product) throws AppException {
        ErrorList errorList = new ErrorList();
        return errorList;
    }

    public ErrorList delete(String productID) throws AppException {
        ErrorList errorList = new ErrorList();
        return errorList;
    }

    /**
         *
         * @return
         * @throws sg.edu.nus.iss.se07.common.exceptions.AppException
         */
    public ProductSet list() throws AppException {
        ProductSet objectSet = new ProductSet();
        try {
            productDA = new ProductDA();
            objectSet = productDA.readDataSet();
            productDA = null;
        } catch (AppException ex) {
            Logger.getLogger(ProductManager.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return objectSet;
    }

    /**
         *
         * @param productID
         * @return
         * @throws sg.edu.nus.iss.se07.common.exceptions.AppException
         */
    public ProductSet list(String productID) throws AppException {
        ProductSet objectSet = new ProductSet();
        try {
            productDA = new ProductDA();
            objectSet = productDA.readDataSet(productID);
            productDA = null;
        } catch (AppException ex) {
            Logger.getLogger(ProductManager.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return objectSet;
    }

    /**
         *
         * @return
         * @throws sg.edu.nus.iss.se07.common.exceptions.AppException
         */
    public ProductSet select() throws AppException {
        ProductSet objectSet = new ProductSet();
        ArrayList<Tuple8<NameValue<String>, NameValue<String>, NameValue<String>, NameValue<Integer>, NameValue<Float>, NameValue<String>, NameValue<Integer>, NameValue<Integer>>> records = null;
        try {
            productDA = new ProductDA();
            records = productDA.readData();
            if (records != null) {
                for (int i = 0; i < records.size(); i++) {
                    Tuple8<NameValue<String>, NameValue<String>, NameValue<String>, NameValue<Integer>, NameValue<Float>, NameValue<String>, NameValue<Integer>, NameValue<Integer>> record = records.get(i);
                    Product product = new Product();
                    product.setProductID(record.getItem1().getValue());
                    product.setProductName(record.getItem2().getValue());
                    product.setProductDescription(record.getItem3().getValue());
                    product.setQuantityAvailable(record.getItem4().getValue());
                    product.setProductPrice(record.getItem5().getValue());
                    product.setBarcodeNumber(record.getItem6().getValue());
                    product.setReorderQuantity(record.getItem7().getValue());
                    product.setOrderQuantity(record.getItem8().getValue());
                    objectSet.add(product);
                }
            }
            productDA = null;
        } catch (AppException ex) {
            Logger.getLogger(ProductManager.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return objectSet;
    }

    /**
         *
         * @param productID
         * @return
         * @throws sg.edu.nus.iss.se07.common.exceptions.AppException
         */
    public ProductSet select(String productID) throws AppException {
        ProductSet objectSet = new ProductSet();
        ArrayList<Tuple8<NameValue<String>, NameValue<String>, NameValue<String>, NameValue<Integer>, NameValue<Float>, NameValue<String>, NameValue<Integer>, NameValue<Integer>>> records = null;
        try {
            productDA = new ProductDA();
            records = productDA.readData(productID);
            if (records != null) {
                for (int i = 0; i < records.size(); i++) {
                    Tuple8<NameValue<String>, NameValue<String>, NameValue<String>, NameValue<Integer>, NameValue<Float>, NameValue<String>, NameValue<Integer>, NameValue<Integer>> record = records.get(i);
                    Product product = new Product();
                    product.setProductID(record.getItem1().getValue());
                    product.setProductName(record.getItem2().getValue());
                    product.setProductDescription(record.getItem3().getValue());
                    product.setQuantityAvailable(Integer.valueOf(record.getItem4().getValue()));
                    product.setProductPrice(Float.valueOf(record.getItem5().getValue()));
                    product.setBarcodeNumber(record.getItem6().getValue());
                    product.setReorderQuantity(Integer.valueOf(record.getItem7().getValue()));
                    product.setOrderQuantity(Integer.valueOf(record.getItem8().getValue()));
                    objectSet.add(product);
                }
            }
            productDA = null;
        } catch (AppException ex) {
            Logger.getLogger(ProductManager.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return objectSet;
    }

    /**
         *
         * @return
         * @throws sg.edu.nus.iss.se07.common.exceptions.AppException
         */
    public ProductSet query() throws AppException {
        return select();
    }

    /**
         *
         * @param productID
         * @return
         * @throws sg.edu.nus.iss.se07.common.exceptions.AppException
         */
    public ProductSet query(String productID) throws AppException {
        ProductSet objectSet = new ProductSet();
        ArrayList<Tuple8<NameValue<String>, NameValue<String>, NameValue<String>, NameValue<Integer>, NameValue<Float>, NameValue<String>, NameValue<Integer>, NameValue<Integer>>> records = null;
        try {
            productDA = new ProductDA();
            records = productDA.readData();
            if (records != null) {
                for (int i = 0; i < records.size(); i++) {
                    Tuple8<NameValue<String>, NameValue<String>, NameValue<String>, NameValue<Integer>, NameValue<Float>, NameValue<String>, NameValue<Integer>, NameValue<Integer>> record = records.get(i);
                    if (productID.equalsIgnoreCase(record.getItem1().getValue())) {
                        Product product = new Product();
                        product.setProductID(record.getItem1().getValue());
                        product.setProductName(record.getItem2().getValue());
                        product.setProductDescription(record.getItem3().getValue());
                        product.setQuantityAvailable(Integer.valueOf(record.getItem4().getValue()));
                        product.setProductPrice(Float.valueOf(record.getItem5().getValue()));
                        product.setBarcodeNumber(record.getItem6().getValue());
                        product.setReorderQuantity(Integer.valueOf(record.getItem7().getValue()));
                        product.setOrderQuantity(Integer.valueOf(record.getItem8().getValue()));
                        objectSet.add(product);
                        i = records.size();
                    }
                }
            }
            productDA = null;
        } catch (AppException ex) {
            Logger.getLogger(ProductManager.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        return objectSet;
    }

    @Override
    protected void finalize() throws Throwable {
        productDA = null;
    }
}
