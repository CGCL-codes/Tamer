package integrationtier.mock;

import integrationtier.AbstractDAOFactory;
import integrationtier.ITitulDAO;

/**
 *
 * @author lucky
 */
public class MockDAOFactory extends AbstractDAOFactory {

    @Override
    public ITitulDAO getTitulDAO() {
        return new TitulDAO();
    }
}
