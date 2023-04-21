package cz.cvut.felk.cig.jcool.experiment.wizard;

import cz.cvut.felk.cig.configuration.ConfigurationManager;
import cz.cvut.felk.cig.jcool.core.FunctionDetail;
import cz.cvut.felk.cig.jcool.core.OptimizationMethodDetail;
import java.util.Map;
import org.apache.commons.configuration.BaseConfiguration;
import org.netbeans.spi.wizard.WizardException;
import org.netbeans.spi.wizard.WizardPage.WizardResultProducer;

/**
 *
 * @author ytoh
 */
public class ConfigurationResultProducer implements WizardResultProducer {

    public Object finish(Map map) throws WizardException {
        BaseConfiguration configuration = new BaseConfiguration();
        FunctionDetail functionDetail = null;
        OptimizationMethodDetail methodDetail = null;
        for (Object o : map.keySet()) {
            if ("selectedFunction".equals(String.valueOf(o))) {
                functionDetail = (FunctionDetail) map.get(o);
            } else if ("selectedOptimizationMethod".equals(String.valueOf(o))) {
                methodDetail = (OptimizationMethodDetail) map.get(o);
            } else {
                configuration.addProperty(String.valueOf(o), map.get(o));
            }
        }
        return new Product(functionDetail, methodDetail, new WizardConfigurationManager(configuration));
    }

    public boolean cancel(Map map) {
        return true;
    }

    public class Product {

        private FunctionDetail functionDetail;

        private OptimizationMethodDetail methodDetail;

        private ConfigurationManager manager;

        public Product(FunctionDetail functionDetail, OptimizationMethodDetail methodDetail, ConfigurationManager manager) {
            this.functionDetail = functionDetail;
            this.methodDetail = methodDetail;
            this.manager = manager;
        }

        public FunctionDetail getFunctionDetail() {
            return functionDetail;
        }

        public ConfigurationManager getManager() {
            return manager;
        }

        public OptimizationMethodDetail getMethodDetail() {
            return methodDetail;
        }
    }
}
