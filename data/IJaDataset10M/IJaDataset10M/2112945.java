package app.pos.dao;

import app.pos.iface.crud;
import app.pos.model.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author JollkY
 */
public class ProductDAO implements crud {

    EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("POSPU");

    EntityManager em = emf.createEntityManager();

    public ProductDAO() {
    }

    public List getList() {
        List<Product> products = new ArrayList<Product>();
        try {
            em.getTransaction().begin();
            products = em.createQuery("SELECT p FROM Product p").getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return products;
    }

    public Object load(long id) {
        Product product = new Product();
        try {
            em.getTransaction().begin();
            product = em.find(Product.class, id);
            em.getTransaction().commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return product;
    }

    public String persist(Object o) {
        String statusProses = "";
        try {
            em.getTransaction().begin();
            em.persist(o);
            em.getTransaction().commit();
            statusProses = "[SUKSES]";
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            em.getTransaction().rollback();
            statusProses = "[GAGAL]";
        } finally {
            em.close();
        }
        return statusProses;
    }

    public String update(Object o) {
        String statusProses = "";
        try {
            em.getTransaction().begin();
            Product product = new Product();
            Product p = (Product) o;
            product = em.find(Product.class, p.getId());
            product.setProductCost(p.getProductCost());
            product.setProductStock(p.getProductStock());
            em.getTransaction().commit();
            statusProses = "[SUKSES]";
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            em.getTransaction().rollback();
            statusProses = "[GAGAL]";
        } finally {
            em.close();
        }
        return statusProses;
    }

    public String delete(long id) {
        String statusProses = "";
        try {
            em.getTransaction().begin();
            Product product = em.find(Product.class, id);
            em.remove(product);
            em.getTransaction().commit();
            statusProses = "[SUKSES]";
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            em.getTransaction().rollback();
            statusProses = "[GAGAL]";
        } finally {
            em.close();
        }
        return statusProses;
    }

    public List search(String word) {
        List<Product> products = new ArrayList<Product>();
        try {
            em.getTransaction().begin();
            products = (List<Product>) em.createQuery("SELECT p FROM Product p " + "WHERE p.productName LIKE '%" + word + "%' " + "OR p.category.categoryName LIKE '%" + word + "%'").getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return products;
    }
}
