package artem.finance.server.dao;

import java.sql.Date;
import java.util.List;
import artem.finance.server.persist.beans.VipiskaBean;

public interface VipiskaDAOI extends GenericDaoI {

    public List<String> getGroups();

    public List<Object> findByExample(VipiskaBean vipiskaBean, Date dateFrom, Date dateTo, boolean shouldFindAll);
}
