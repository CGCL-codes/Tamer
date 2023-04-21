package nuts.exts.struts2.actions;

import nuts.core.orm.dao.DataAccessClient;
import nuts.core.orm.dao.DataAccessClientAware;

/**
 */
@SuppressWarnings("serial")
public class CommonDataAccessClientAction extends CommonAction implements DataAccessClientAware {

    private DataAccessClient dataAccessClient;

    /**
	 * @return the dataAccessClient
	 */
    public DataAccessClient getDataAccessClient() {
        return dataAccessClient;
    }

    /**
	 * @param dataAccessClient the dataAccessClient to set
	 */
    public void setDataAccessClient(DataAccessClient dataAccessClient) {
        this.dataAccessClient = dataAccessClient;
    }
}
