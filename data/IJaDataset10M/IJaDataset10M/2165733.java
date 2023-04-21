package $packageName$;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.fixtures.AbstractFixture;
import $applicationProjectName$.domain.Customer;
import $applicationProjectName$.domain.Product;
import $applicationProjectName$.services.CustomerRepository;
import $applicationProjectName$.services.ProductRepository;

public class CustomerOrdersFixture extends AbstractFixture {

    private static final Logger LOGGER = Logger.getLogger(CustomerOrdersFixture.class);

    public Logger getLOGGER() {
        return LOGGER;
    }

    public void install() {
        Customer richard = getCustomerRepository().findByName("Pawson");
        Product foldingTable = getProductRepository().findByCode("820-72721");
        Product foldingChair = getProductRepository().findByCode("820-72725");
        Product waspCatcher = getProductRepository().findByCode("850-18003");
        Product coolbox = getProductRepository().findByCode("845-01020");
        setDate(2007, 4, 11);
        setTime(10, 15);
        richard.placeOrder(foldingTable, 1);
        setDate(2007, 4, 12);
        setTime(9, 35);
        richard.placeOrder(foldingChair, 6);
        setDate(2007, 4, 13);
        setTime(14, 20);
        richard.placeOrder(waspCatcher, 1);
        setDate(2007, 4, 14);
        setTime(11, 10);
        richard.placeOrder(coolbox, 1);
    }

    private CustomerRepository customerRepository;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected CustomerRepository getCustomerRepository() {
        return this.customerRepository;
    }

    /**
     * Injected by the application container.
     */
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    private ProductRepository productRepository;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected ProductRepository getProductRepository() {
        return this.productRepository;
    }

    /**
     * Injected by the application container.
     */
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
