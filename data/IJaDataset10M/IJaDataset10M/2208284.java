package jp.go.aist.six.test.util.orm.inheritance;

import jp.go.aist.six.util.castor.CastorDao;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author  Akihito Nakamura, AIST
 * @version $Id: ContainerDao.java 222 2010-12-08 04:09:11Z nakamura5akihito $
 */
public class ContainerDao extends CastorDao<String, ContainerX> {

    /**
     * Constructor.
     */
    public ContainerDao() {
        super(ContainerX.class);
    }

    @Override
    public String create(final ContainerX container) {
        if (container.getPersistentID() == null) {
            String uuid = UUID.randomUUID().toString();
            container.setPersistentID(uuid);
        }
        List<AbstractA> elements = new ArrayList<AbstractA>();
        for (AbstractA element : container) {
            element.setMasterObject(container);
            elements.add(element);
        }
        List<AbstractA> p_elements = getForwardingDao(AbstractA.class).syncAll(elements);
        container.setElement(p_elements);
        return super.create(container);
    }
}
