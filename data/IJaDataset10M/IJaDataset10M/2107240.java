package artem.finance.server.dao;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import artem.finance.server.persist.SubContract;

public class SubContractDAO extends GenericDaoImpl implements SubContractDAOI {

    public static final Logger LOG = Logger.getLogger(SubContractDAO.class);

    public SubContractDAO() {
        super(SubContract.class);
    }

    /**
     * @param sessionFactory the sessionFactory to set
     */
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
        LOG.debug("!!! Session Factory was created in subcontract dao!!!");
    }
}
