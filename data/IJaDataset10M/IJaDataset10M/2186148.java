package ecom.beans;

import java.util.List;
import javax.ejb.Remote;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

@Remote
public interface EcomAdminRemote {

    public void begin() throws NotSupportedException, SystemException;

    public void commit() throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException, SystemException;

    public void rollback() throws IllegalStateException, SecurityException, SystemException;

    public AccountBean createAccount(double balance);

    public void updateAccount(AccountBean account);

    public void removeAccount(int accountId);

    public List listAllAccounts();

    public AccountBean findAccountById(int id);

    public ProductBean createProduct(int modelId, int productStoreId);

    public void updateProduct(ProductBean product);

    public void removeProduct(long productId);

    public List listAllProducts();

    public ProductBean findProductById(Long productId);

    public List findProductsByName(String name);

    public ProductStoreBean createProductStore(String name, String address, String zipCode, String city, String country, double balance);

    public void updateProductStore(ProductStoreBean productStore);

    public void removeProductStore(int productStoreId);

    public List listAllProductsStore();

    public ProductStoreBean findProductStoreById(int productStoreId);

    public List findProductStoresByName(String name);

    public ClientBean createClient(String firstname, String lastname, String loginEmail, String password, String address, String zipCode, String city, String country, double balance);

    void updateClient(ClientBean client);

    void removeClient(int clientId);

    List listAllClients();

    ClientBean findClientById(int clientId);

    BrandBean findBrandById(int brandId);

    BrandBean createBrand(String name, String imagePath);

    List listAllBrands();

    void removeBrand(int brandId);

    void updateBrand(BrandBean client);

    ModelBean createModel(String name, double price, String description, String imagePath, int brandId);

    List listAllModels();

    void removeModel(int modelId);

    void updateModel(ModelBean model);

    ModelBean findModelById(int modelId);
}
