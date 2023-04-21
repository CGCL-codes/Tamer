package redora.test.rdo.service;

import org.jetbrains.annotations.NotNull;
import redora.test.rdo.service.base.JUnitPigsEarServiceBase;
import redora.service.ServiceBase;
import redora.exceptions.ConnectException;

/**
* Service layer for all database access activities around the JUnitPigsEar object.
* Redora actually implements everything in JUnitPigsEarServiceBase and JUnitPigsEarService
* extends it. You can add extra services yourself in this class. Usage:
* <code>
* JUnitPigsEarService service ServiceFactory.jUnitPigsEarService();
* service.doStuff();
* ServiceFactory.close(service);
* </code>
* @author Redora (www.redora.net)
*/
public class JUnitPigsEarService extends JUnitPigsEarServiceBase {

    protected JUnitPigsEarService() throws ConnectException {
        super();
    }

    /**
    * Instantiate by passing another service object. This JUnitPigsEarService
    * will then used passed service object for database connection and transaction management.
    * Typically you use this when a transaction is spawned over several objects.
    * @param chain (Mandatory) Active service.
    */
    public JUnitPigsEarService(@NotNull ServiceBase chain) {
        super(chain);
    }
}
