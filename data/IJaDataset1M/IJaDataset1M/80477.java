package services.agrega;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.exception.*;
import services.agrega.AgregaProductoAction;
import utilidades.HibernateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.*;
import excepciones.productoexiste;
import modelo.Product;

public class AgregaProductoAction {

    private Product newproduct = new Product();

    private static final Logger logger = LogManager.getLogger(AgregaProductoAction.class);

    public AgregaProductoAction() {
        init();
    }

    /**
      * Initializeing  
      * objects to be used by the application.
      */
    private synchronized void init() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.getTransaction().commit();
    }

    /**
      * Obtiene el Producto de hafner recien creado.
      * @return Producto de hafner recien creado.
      */
    public Product getNewProduct() {
        return this.newproduct;
    }

    /**
      * Obtiene el producto de hafner que se esta actualizando.
      * @return Producto de hafner que se esta actualizando.
      */
    public void setNewProduct(Product newProduct) {
        this.newproduct = newProduct;
    }

    /**
      * Listener para add product button click action.
      * 
      */
    public boolean addProduct() throws productoexiste {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Product p = this.getNewProduct();
        try {
            session.save(p);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
        p.clear();
        init();
        return true;
    }
}
