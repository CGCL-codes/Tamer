package net.jwde.database;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import net.jwde.database.DAOFactory;
import net.jwde.database.DAOFactoryException;
import net.jwde.database.DatabaseException;
import net.jwde.database.GenericDAOFactory;
import net.jwde.database.NoProductTypeFoundException;
import net.jwde.object.Category;
import net.jwde.object.Product;
import net.jwde.object.Supplier;
import net.jwde.object.SupplierCurrency;
import net.jwde.object.SupplierProduct;
import net.jwde.object.SupplierShippingFee;
import net.jwde.NoSupplierException;

public class DatabaseManager {

    private DAOFactory daoFact;

    private Logger log = Logger.getLogger(this.getClass().getName());

    public DatabaseManager() {
    }

    public DatabaseManager(String propertyURLName) {
        daoFact = GenericDAOFactory.getInstance(propertyURLName);
    }

    public void setDAOFactory(String propertyURLName) {
        daoFact = GenericDAOFactory.getInstance(propertyURLName);
    }

    @SuppressWarnings("unchecked")
    public Object insertProduct(Product product) throws NoProductTypeFoundException, DatabaseException {
        try {
            return daoFact.getProductDAO().insertProduct(product);
        } catch (NoProductTypeFoundException ex) {
            throw new NoProductTypeFoundException();
        }
    }

    @SuppressWarnings("unchecked")
    public boolean insertOSCommerceProducts(Collection<Product> product) throws NoProductTypeFoundException, DatabaseException {
        return daoFact.getProductDAO().insertProducts(product);
    }

    public Product getProduct(int productID) throws NoProductTypeFoundException, DatabaseException {
        return daoFact.getProductDAO().getProduct(productID);
    }

    @SuppressWarnings("unchecked")
    public List<Product> findAllProducts() throws NoProductTypeFoundException, DatabaseException {
        return daoFact.getProductDAO().findAll();
    }

    @SuppressWarnings("unchecked")
    public boolean updateProduct(Product product) throws NoProductTypeFoundException, DatabaseException {
        return daoFact.getProductDAO().updateProduct(product);
    }

    public boolean deleteProduct(int productID) throws NoProductTypeFoundException, DatabaseException {
        return daoFact.getProductDAO().deleteProduct(productID);
    }

    @SuppressWarnings("unchecked")
    public boolean deleteProducts(Collection<Product> product) throws NoProductTypeFoundException, DatabaseException {
        return daoFact.getProductDAO().deleteProducts(product);
    }

    public int productSize() throws NoProductTypeFoundException, DatabaseException {
        return daoFact.getProductDAO().size();
    }

    public Object insertSupplierCurrency(SupplierCurrency supCur) throws DatabaseException {
        return daoFact.getSupplierCurrencyDAO().insertSupplierCurrency(supCur);
    }

    public boolean insertSupplierCurrencies(Collection<SupplierCurrency> supCurs) throws DatabaseException {
        return daoFact.getSupplierCurrencyDAO().insertSupplierCurrencies(supCurs);
    }

    public SupplierCurrency getSupplierCurrency(long id) throws DatabaseException {
        return daoFact.getSupplierCurrencyDAO().getSupplierCurrency(id);
    }

    public SupplierCurrency findSupplierCurrencyByName(String name) throws DatabaseException {
        return daoFact.getSupplierCurrencyDAO().findByName(name);
    }

    public List<SupplierCurrency> findAllSupplierCurrencies() throws DatabaseException {
        return daoFact.getSupplierCurrencyDAO().findAll();
    }

    public boolean updateSupplierCurrency(SupplierCurrency supCur) throws DatabaseException {
        return daoFact.getSupplierCurrencyDAO().updateSupplierCurrency(supCur);
    }

    public boolean deleteSupplierCurrency(long id) throws DatabaseException {
        return daoFact.getSupplierCurrencyDAO().deleteSupplierCurrency(id);
    }

    public boolean deleteSupplierCurrencies(Collection<SupplierCurrency> supCurs) throws DatabaseException {
        return daoFact.getSupplierCurrencyDAO().deleteSupplierCurrencies(supCurs);
    }

    public int supplierCurrencySize() throws DatabaseException {
        return daoFact.getSupplierCurrencyDAO().size();
    }

    public Object insertSupplier(Supplier supplier) throws DatabaseException {
        return daoFact.getSupplierDAO().insertSupplier(supplier);
    }

    public boolean insertSuppliers(Collection<Supplier> suppliers) throws DatabaseException {
        return daoFact.getSupplierDAO().insertSuppliers(suppliers);
    }

    public Supplier getSupplier(long supplierID) throws DatabaseException {
        return daoFact.getSupplierDAO().getSupplier(supplierID);
    }

    public Supplier findSupplierByName(String name) throws DatabaseException {
        return daoFact.getSupplierDAO().findByName(name);
    }

    public Supplier findSupplierByCode(String code) throws DatabaseException {
        try {
            return daoFact.getSupplierDAO().findByCode(code);
        } catch (NoSupplierException noSupEx) {
            log.debug("No supplier found with code");
            return null;
        }
    }

    public List<Supplier> findAllSuppliers() throws DatabaseException {
        return daoFact.getSupplierDAO().findAll();
    }

    public boolean updateSupplier(Supplier supplier) throws DatabaseException {
        return daoFact.getSupplierDAO().updateSupplier(supplier);
    }

    public boolean deleteSupplier(long supplierID) throws DatabaseException {
        return daoFact.getSupplierDAO().deleteSupplier(supplierID);
    }

    public boolean deleteSuppliers(Collection<Supplier> suppliers) throws DatabaseException {
        return daoFact.getSupplierDAO().deleteSuppliers(suppliers);
    }

    public int supplierSize() throws DatabaseException {
        return daoFact.getSupplierDAO().size();
    }

    public Object insertSupplierProduct(SupplierProduct supplierProduct) throws DatabaseException {
        return daoFact.getSupplierProductDAO().insertSupplierProduct(supplierProduct);
    }

    public boolean insertSupplierProducts(Collection<SupplierProduct> supPros) throws DatabaseException {
        return daoFact.getSupplierProductDAO().insertSupplierProducts(supPros);
    }

    public SupplierProduct getSupplierProduct(long supplierProductID) throws DatabaseException {
        return daoFact.getSupplierProductDAO().getSupplierProduct(supplierProductID);
    }

    public SupplierProduct findSupplierProductByCode(String code) throws DatabaseException {
        return daoFact.getSupplierProductDAO().getSupplierProductByCode(code);
    }

    public List<SupplierProduct> findAllSupplierProducts() throws DatabaseException {
        return daoFact.getSupplierProductDAO().findAll();
    }

    public boolean updateSupplierProduct(SupplierProduct supplierProduct) throws DatabaseException {
        return daoFact.getSupplierProductDAO().updateSupplierProduct(supplierProduct);
    }

    public boolean updateSupplierProducts(Collection<SupplierProduct> supplierProducts) throws DatabaseException {
        return daoFact.getSupplierProductDAO().updateSupplierProducts(supplierProducts);
    }

    public boolean deleteSupplierProduct(long supProID) throws DatabaseException {
        return daoFact.getSupplierProductDAO().deleteSupplierProduct(supProID);
    }

    public boolean deleteSupplierProducts(Collection<SupplierProduct> supPros) throws DatabaseException {
        return daoFact.getSupplierProductDAO().deleteSupplierProducts(supPros);
    }

    public int supplierProductSize() throws DatabaseException {
        return daoFact.getSupplierProductDAO().size();
    }

    public Object insertSupplierShippingFee(SupplierShippingFee supShipFee) throws DatabaseException {
        return daoFact.getSupplierShippingFeeDAO().insertSupplierShippingFee(supShipFee);
    }

    public boolean insertSupplierShippingFees(Collection<SupplierShippingFee> supShipFees) throws DatabaseException {
        return daoFact.getSupplierShippingFeeDAO().insertSupplierShippingFees(supShipFees);
    }

    public SupplierShippingFee getSupplierShippingFee(long supplierID) throws DatabaseException {
        return daoFact.getSupplierShippingFeeDAO().getSupplierShippingFee(supplierID);
    }

    public SupplierShippingFee findSupplierShippingFeeByName(String name) throws DatabaseException {
        return daoFact.getSupplierShippingFeeDAO().findSupplierShippingFeeByName(name);
    }

    public List<SupplierShippingFee> findAllSupplierShippingFees() throws DatabaseException {
        return daoFact.getSupplierShippingFeeDAO().findAll();
    }

    public boolean updateSupplierShippingFee(SupplierShippingFee supShipFee) throws DatabaseException {
        return daoFact.getSupplierShippingFeeDAO().updateSupplierShippingFee(supShipFee);
    }

    public boolean deleteSupplierShippingFee(long supplierID) throws DatabaseException {
        return daoFact.getSupplierShippingFeeDAO().deleteSupplierShippingFee(supplierID);
    }

    public boolean deleteSupplierShippingFees(Collection<SupplierShippingFee> supShipFees) throws DatabaseException {
        return daoFact.getSupplierShippingFeeDAO().deleteSupplierShippingFees(supShipFees);
    }

    public int supplierShippingFeeSize() throws DatabaseException {
        return daoFact.getSupplierShippingFeeDAO().size();
    }

    public Object insertCategory(Category category) throws DatabaseException {
        return daoFact.getCategoryDAO().insertCategory(category);
    }

    public boolean insertCategories(Collection<Category> categories) throws DatabaseException {
        return daoFact.getCategoryDAO().insertCategories(categories);
    }

    public Category getCategory(long CategoryID) throws DatabaseException {
        return daoFact.getCategoryDAO().getCategory(CategoryID);
    }

    public Category findCategoryByName(String name) throws DatabaseException {
        return daoFact.getCategoryDAO().findByName(name);
    }

    public List<Category> findAllCategories() throws DatabaseException {
        return daoFact.getCategoryDAO().findAll();
    }

    public boolean updateCategory(Category category) throws DatabaseException {
        return daoFact.getCategoryDAO().updateCategory(category);
    }

    public boolean deleteCategory(long CategoryID) throws DatabaseException {
        return daoFact.getCategoryDAO().deleteCategory(CategoryID);
    }

    public boolean deleteCategories(Collection<Category> categories) throws DatabaseException {
        return daoFact.getCategoryDAO().deleteCategories(categories);
    }

    public int categorySize() throws DatabaseException {
        return daoFact.getCategoryDAO().size();
    }
}
