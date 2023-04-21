package jacg.config;

import jacg.Configuration;
import jacg.validation.ErrorReporter;
import org.w3c.dom.Node;

public class ValidatorCP extends AbstractConfigurationProcessor {

    public ValidatorCP() {
        NAME = "validator";
    }

    void processInternal(Node node) {
        Node failNode = node.getAttributes().getNamedItem("failOnError");
        Node errorNode = node.getAttributes().getNamedItem("errorReporter");
        if (failNode != null) {
            String failOnError = failNode.getNodeValue();
            Configuration.getValidatorConfig().setFailOnError("true".equalsIgnoreCase(failOnError));
        }
        if (errorNode != null) {
            String errorReporterClass = errorNode.getNodeValue();
            ErrorReporter r = initReporter(errorReporterClass);
            if (r != null) {
                Configuration.getValidatorConfig().setErrorReporter(r);
            }
        }
    }

    private ErrorReporter initReporter(String errorReporterClass) {
        try {
            Class errorRClass = Class.forName(errorReporterClass);
            ErrorReporter newInstance = (ErrorReporter) errorRClass.newInstance();
            return newInstance;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }
}
