package sales;

import org.modelibra.config.DomainConfig;
import org.modelibra.config.ModelConfig;

/**
 * Domain tests. In general, there may be more than one domain model.
 * 
 * @author Dzenan Ridjanovic
 * @version 2009-01-05
 */
public class SalesTest {

    private static SalesTest salesTest;

    private Sales sales;

    private PersistentSales persistentSales;

    private SalesTest() {
        super();
        open();
    }

    public static SalesTest getSingleton() {
        if (salesTest == null) {
            salesTest = new SalesTest();
        }
        return salesTest;
    }

    private void open() {
        SalesConfig salesConfig = new SalesConfig();
        DomainConfig domainConfig = salesConfig.getDomainConfig();
        for (ModelConfig modelConfig : domainConfig.getModelsConfig()) {
            modelConfig.setPersistenceRelativePath(modelConfig.getPersistenceRelativePath() + SalesConfig.SEPARATOR + salesConfig.getModelibraProperties().getTestDirectoryName());
        }
        sales = new Sales(domainConfig);
        persistentSales = new PersistentSales(sales);
    }

    public Sales getSales() {
        return sales;
    }

    public void close() {
        if (persistentSales != null) {
            persistentSales.close();
        }
    }
}
