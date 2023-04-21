package com.patientis.ejb.common;

import java.util.List;
import java.util.ArrayList;
import org.hibernate.Session;
import com.patientis.model.common.IBaseModel;
import com.patientis.model.common.ServiceCall;
import com.patientis.data.common.BaseData;
import com.patientis.data.common.ISParameter;
import com.patientis.framework.logging.Log;

/**
 * DeleteCommand permanently deletes models
 *
 * <br/>
 */
public class HqlDeleteCommand implements ICommand {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * HQL to delete
	 */
    private String deleteHql;

    /**
	 * HQL to delete
	 */
    private List<ISParameter> deleteParameters = new ArrayList<ISParameter>();

    /**
	 * Service call context
	 */
    private ServiceCall call;

    /**
	 * Last command return value
	 */
    protected Object lastCommandReturn = null;

    /**
	 * delete the model
	 * 
	 * @param model to be deleted
	 * @param call service transaction 
	 */
    public HqlDeleteCommand(String deleteHql, List<ISParameter> parameters, ServiceCall call) {
        this.call = call;
        this.deleteHql = deleteHql;
        this.deleteParameters = parameters;
    }

    /**
	 * Get the last command
	 * 
	 * @return the lastCommandReturn
	 */
    public Object getLastCommandReturn() {
        return lastCommandReturn;
    }

    /**
	 * Set the last command return value
	 * 
	 * @param lastCommandReturn the lastCommandReturn to set
	 */
    public void setLastCommandReturn(Object lastCommandReturn) {
        this.lastCommandReturn = lastCommandReturn;
    }

    /**
	 * Execute the delete command
	 */
    public Long execute(Session session) throws Exception {
        try {
            int rowsDeleted = BaseData.deleteHql(session, deleteHql, deleteParameters, call);
            return new Long(rowsDeleted);
        } catch (Exception ex) {
            Log.exception(ex);
            if (deleteHql != null) {
                Log.error(deleteHql);
            }
            throw ex;
        }
    }

    /**
	 * @return the model
	 */
    public IBaseModel getModel() {
        return null;
    }
}
