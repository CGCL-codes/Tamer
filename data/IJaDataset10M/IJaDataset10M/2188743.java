package siscweb.contcentric;

import javax.servlet.ServletContext;
import siscweb.util.Environment;

public class ContinuationStoreLocator {

    public static synchronized ContinuationStore lookup(ServletContext servletContext) {
        ContinuationStore continuationStore = (ContinuationStore) servletContext.getAttribute("siscweb.continuation-store");
        if (continuationStore == null) {
            final String className = Environment.getContinuationStoreClassName();
            continuationStore = ContinuationStore.Factory.create(className);
            servletContext.setAttribute("siscweb.continuation-store", continuationStore);
        }
        return continuationStore;
    }
}
