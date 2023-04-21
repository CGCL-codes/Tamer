package enzimaweb.sample.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import enzimaweb.sample.service.ProductService;

public class Configuration {

    private static Configuration instance;

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    protected static void setInstance(Configuration newInstance) {
        instance = newInstance;
    }

    private AnnotationConfiguration cfg;

    private SessionFactory sessionFactory;

    private ProductService productService;

    protected Configuration() {
        cfg = new AnnotationConfiguration();
        cfg.configure();
        sessionFactory = null;
    }

    public AnnotationConfiguration getHibernateConfiguration() {
        return cfg;
    }

    public SessionFactory getHibernateSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = cfg.buildSessionFactory();
        }
        return sessionFactory;
    }

    public ProductService getProductService() {
        if (productService == null) {
            productService = new ProductService();
        }
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
