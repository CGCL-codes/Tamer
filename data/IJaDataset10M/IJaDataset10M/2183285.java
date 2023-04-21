package sls.crystalstore.db;

import java.util.Collection;
import sls.crystalstore.util.exception.AssertionException;
import sls.crystalstore.util.exception.CreateException;
import sls.crystalstore.util.exception.DatabaseException;
import sls.crystalstore.util.exception.DuplicateKeyException;
import sls.crystalstore.util.exception.ObjectNotFoundException;

public interface ProductDAO {

    public static final String TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "products";

    public static final String INDEX_TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "products_to_categories";

    public static final String DESC_TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "products_description";

    public static final String SPECIAL_TABLE_NAME = DatabaseConfig.TABLE_PREFIX + "specials";

    public void findByPrimaryKey(int productID) throws ObjectNotFoundException, DatabaseException;

    public void deleteByPrimaryKey(int productID) throws DatabaseException;

    public void create(ProductBean product) throws CreateException, DatabaseException, DuplicateKeyException;

    public void update(int productID, ProductBean product) throws ObjectNotFoundException, DatabaseException;

    /************************************************
	 * Customized methods come below
	 ************************************************/
    public int getNumberOfProductsInCategory(int categoryID) throws AssertionException, DatabaseException;

    public Collection getProductsInCategory(int categoryID) throws DatabaseException;

    public ProductBean getProduct(int productID) throws ObjectNotFoundException, DatabaseException;

    public ProductBean getProductFullInfo(int productID, int languageID) throws ObjectNotFoundException, DatabaseException;

    public Collection getProductsInCategoryWithFullInfo(int categoryID, int languageID) throws DatabaseException;

    public ProductBean getProductWithDesc(int productID, int languageID) throws ObjectNotFoundException, DatabaseException;

    public int getCategoryID(int productID) throws ObjectNotFoundException, DatabaseException;

    public ProductBean getRandomNewWithDesc(int languageID) throws DatabaseException;

    public ProductBean getRandSpecialProduct(int languageID) throws ObjectNotFoundException, DatabaseException;

    public Collection getAllSpeicalProducts(int languageID) throws DatabaseException;

    public Collection getNewProducts(int languageID) throws DatabaseException;

    public Collection getProductsByNameSearch(String namelike, int languageID) throws DatabaseException;
}
