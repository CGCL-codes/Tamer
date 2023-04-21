package at.jku.semwiq.mediator.engine.describe;

import com.hp.hpl.jena.sparql.core.describe.DescribeHandler;
import com.hp.hpl.jena.sparql.core.describe.DescribeHandlerFactory;

/**
 * @author dorgon
 *
 */
public class MediatorDescribeHandlerFactory implements DescribeHandlerFactory {

    public DescribeHandler create() {
        return new MediatorDescribeHandler();
    }
}
