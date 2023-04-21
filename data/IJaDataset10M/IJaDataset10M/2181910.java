package net.future118.smallbusiness.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import net.future118.smallbusiness.dao.EntityDAO;
import net.future118.smallbusiness.model.company.Country;
import net.future118.smallbusiness.model.product.PointOfSale;
import net.future118.smallbusiness.model.product.Product;
import net.future118.smallbusiness.model.product.ProductForSale;
import net.future118.smallbusiness.model.product.ProductGroup;
import net.future118.smallbusiness.model.product.Unit;

public class ProductServiceImpl extends HibernateDaoSupport implements ProductService {

    protected EntityDAO dao;

    public void setDao(EntityDAO dao) {
        this.dao = dao;
    }

    @SuppressWarnings("unchecked")
    public List<Product> loadProductFromGroup(ProductGroup productGroup) {
        List<Product> list = dao.loadAll(Product.class);
        List<Product> list2 = new ArrayList<Product>();
        for (Product product : list) {
            if (product.getProductGroup().equals(productGroup)) {
                list2.add(product);
            }
        }
        return list2;
    }

    public Product loadProductById(Long id) {
        Product product = (Product) dao.loadById(Product.class, id);
        return product;
    }

    @SuppressWarnings("unchecked")
    public List<Product> loadAllProducts() {
        List<Product> list = dao.loadAll(Product.class);
        return list;
    }

    public void saveProduct(Product product) {
        dao.save(product);
    }

    public void updateProduct(Product product) {
        dao.update(product);
    }

    @SuppressWarnings("unchecked")
    public List<Country> loadAllCountries() {
        List<Country> list = dao.loadAll(Country.class);
        return list;
    }

    public void saveNewProductGroup(ProductGroup productGroup) {
        dao.save(productGroup);
    }

    public void saveNewCountry(Country country) {
        dao.save(country);
    }

    @SuppressWarnings("unchecked")
    public List<ProductGroup> loadProductGroups() {
        List<ProductGroup> list = dao.loadAll(ProductGroup.class);
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<Unit> loadUnits() {
        List<Unit> list = dao.loadAll(Unit.class);
        return list;
    }

    public void deleteProduct(Product product) {
        dao.delete(product);
    }

    public void updateProductGroup(ProductGroup productGroup) {
        System.out.println("updateuje pg: " + productGroup);
        dao.update(productGroup);
    }

    public ProductGroup loadProductGroupById(Long id) {
        ProductGroup group = (ProductGroup) dao.loadById(ProductGroup.class, id);
        return group;
    }

    public void deleteProductGroup(ProductGroup group) {
        if (group.getProducts() == null || group.getProducts().size() == 0) {
            dao.delete(group);
        }
    }

    public List<ProductForSale> loadPfsFromProductGroup(ProductGroup productGroup, PointOfSale pointOfSale) {
        List<Product> list = loadProductFromGroup(productGroup);
        List<ProductForSale> list2 = new ArrayList<ProductForSale>();
        for (Product product : list) {
            if (product.getProductForSale(pointOfSale) != null) {
                list2.add(product.getProductForSale(pointOfSale));
            }
        }
        return list2;
    }
}
