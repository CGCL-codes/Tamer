package bg.price.comparator.dao.jpa;

import static bg.price.comparator.dao.jpa.JpaDaoUtils.compileLikeClause;
import static bg.price.comparator.dao.jpa.JpaDaoUtils.exists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bg.price.comparator.dao.ComparatorDaoException;
import bg.price.comparator.dao.ProductSortOrder;
import bg.price.comparator.dao.ProductsDao;
import bg.price.comparator.domain.Category;
import bg.price.comparator.domain.EShop;
import bg.price.comparator.domain.Offer;
import bg.price.comparator.domain.Picture;
import bg.price.comparator.domain.Product;

@Service
public class JpaSpringProductsDao implements ProductsDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Log log = LogFactory.getLog(JpaSpringProductsDao.class);

    public JpaSpringProductsDao() {
    }

    @Override
    @Transactional
    public Product addProduct(String name, String description, Category cat, Collection<Picture> pictures, Collection<Offer> offers) throws ComparatorDaoException {
        log.trace("Creating product with name " + name + ", description " + description + ", category " + cat + " and offers " + offers == null ? "-" : offers);
        if (cat == null) {
            throw new ComparatorDaoException("The category where this product belongs to cannot be null");
        } else if (!exists(entityManager, cat.getClass(), cat.getId())) {
            throw new ComparatorDaoException("The category where this product belongs to should exist");
        }
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setCategory(cat);
        product.setPictures(pictures);
        product.setOffers(offers);
        if (!product.isValid()) {
            throw new ComparatorDaoException("The product to be created is " + "not valid. Some of the mandatory parameters are null.");
        }
        log.debug("Creating product " + name);
        entityManager.persist(product);
        log.debug("Product " + name + " was created successfully.");
        return product;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Product> findProductsByName(String name) throws ComparatorDaoException {
        if (name == null) {
            throw new ComparatorDaoException("The name of the product to " + "find cannot be null");
        }
        log.trace("Looking for product with name like " + name);
        Query query = entityManager.createNamedQuery("findProductsByName");
        query.setParameter("name", compileLikeClause(name));
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Product> findAllProducts() throws ComparatorDaoException {
        log.trace("Extracting all the products from the database.");
        Query query = entityManager.createNamedQuery("findAllProducts");
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Product> findProductsByCategory(Category cat) throws ComparatorDaoException {
        if (cat == null) {
            throw new ComparatorDaoException("The category of the product to " + "find cannot be null");
        }
        log.trace("Looking for all the products belonging to category " + cat);
        Query query = entityManager.createNamedQuery("findProductsByCategory");
        query.setParameter("category", cat);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Product> findProductsByEShop(EShop eshop, ProductSortOrder order) throws ComparatorDaoException {
        if (eshop == null) {
            throw new ComparatorDaoException("The name of the eshop, which products are " + "saught cannot be null");
        }
        log.trace("Looking for all the products offerred at " + eshop);
        Query query = entityManager.createNamedQuery("findProductsByEShopOrderByName");
        if (order == ProductSortOrder.BY_PRICE) {
            query = entityManager.createNamedQuery("findProductsByEShopOrderByPrice");
        }
        query.setParameter("eshop", eshop);
        return query.getResultList();
    }

    @Override
    public List<Product> findProductsByKeyWords(String... keyWords) throws ComparatorDaoException {
        if (keyWords == null) {
            throw new ComparatorDaoException("The key words for the product to find cannot be null");
        }
        List<Product> result = new ArrayList<Product>();
        Set<Product> unrepeatableList = new HashSet<Product>();
        log.trace("Looking for all products with key words " + Arrays.toString(keyWords));
        for (int i = 0; i < keyWords.length; i++) {
            unrepeatableList.addAll(findProductsBySingleKeyWord(keyWords[i]));
        }
        result.addAll(unrepeatableList);
        return result;
    }

    @SuppressWarnings("unchecked")
    private List<Product> findProductsBySingleKeyWord(String keyWord) {
        Query query = entityManager.createNamedQuery("findProductsByKeyWord");
        query.setParameter("description", compileLikeClause(keyWord));
        return query.getResultList();
    }

    @Override
    @Transactional
    public Product updateProduct(Product product) throws ComparatorDaoException {
        if (product == null || !product.isValid()) {
            log.error("Product " + product + " is not valid.");
            throw new ComparatorDaoException("The product to be updated is " + "not valid.");
        }
        log.debug("Updating product " + product);
        Product productx = entityManager.find(Product.class, product.getId());
        if (productx != null) {
            productx.setName(product.getName());
            productx.setDescription(product.getDescription());
            productx.setCategory(product.getCategory());
            productx.setOffers(product.getOffers());
            productx.setPictures(product.getPictures());
            entityManager.flush();
        } else {
            log.debug("Product " + product + " does not exist. It will be " + "created.");
            productx = product;
            entityManager.persist(productx);
            entityManager.flush();
        }
        log.debug("Product " + product + " was persisted successfully.");
        return productx;
    }

    @Override
    @Transactional
    public void deleteProduct(Product product) throws ComparatorDaoException {
        if (product == null) {
            log.warn("The product to delete is null.");
            return;
        }
        Product productx = entityManager.find(Product.class, product.getId());
        if (productx != null) {
            entityManager.remove(productx);
            entityManager.flush();
            log.debug("Product " + product + " was deleted successfully.");
        } else {
            log.debug("Product " + product + " does not exist.");
        }
    }
}
