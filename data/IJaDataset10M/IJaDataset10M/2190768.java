package org.crown.kings.jsf;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.crown.kings.dao.DBMapper;
import org.crown.kings.dao.InetAddressEntityDAO;
import org.crown.kings.dao.InetAddressEntityDAOImpl;
import org.crown.kings.entities.InetAddressEntity;
import org.crown.kings.entities.InetAddressEntityExample;

/**
 * A bean for handling InetAddresses (e-mail, internet, etc.) in the UI.
 * 
 * @author nagood
 * @version $Id$
 */
public class InetAddressBean extends BaseAddressBean {

    private static Log logger = LogFactory.getLog(PersonBean.class);

    /**
	 * 
	 * @param id
	 * @return
	 */
    public static List<InetAddressBean> getInetAddressesFor(final int id) {
        final List<InetAddressBean> results = new ArrayList<InetAddressBean>();
        final InetAddressEntityDAO dao = new InetAddressEntityDAOImpl(DBMapper.instance().getSqlMapper());
        final InetAddressEntityExample example = new InetAddressEntityExample();
        example.createCriteria().andPersonIdEqualTo(id);
        List<InetAddressEntity> list;
        try {
            list = dao.selectByExample(example);
            if (logger.isDebugEnabled()) {
                logger.debug("Found " + list.size() + " objects");
            }
            for (final InetAddressEntity entity : list) {
                final InetAddressBean bean = new InetAddressBean();
                bean.setAddress(entity.getInetaddress());
                bean.setAddressType(entity.getInetaddressType());
                bean.setPreferred((entity.getIsPreferred() != null) && entity.getIsPreferred().equals("Y"));
                results.add(bean);
            }
        } catch (final SQLException e) {
            logger.error("Error while getting inet addresses", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while getting inet addresses", ""));
        }
        return results;
    }

    /**
	 * Runs the add action that adds the values from the bean into the persistent
	 * store.
	 * 
	 * @return String value
	 */
    public String doAdd() {
        String result = FAILURE;
        final PersonWipBean wip = (PersonWipBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("personWip");
        final InetAddressEntity record = new InetAddressEntity();
        record.setInetaddress(getAddress());
        record.setInetaddressType(getAddressType());
        record.setPersonId(wip.getId());
        record.setCreateUser("ngood");
        record.setCreateDate(new Date());
        record.setUpdateDate(new Date());
        record.setUpdateUser("ngood");
        record.setVersion(1);
        record.setIsPreferred(isPreferred() ? "Y" : "N");
        final InetAddressEntityDAO dao = new InetAddressEntityDAOImpl(DBMapper.instance().getSqlMapper());
        try {
            dao.insert(record);
            result = SUCCESS;
        } catch (final SQLException e) {
            logger.error("Error while adding inet addresses", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while adding inet addresses", ""));
        }
        logger.debug(result);
        return result;
    }

    /**
	 * Gets a list of inet addresses for the PersonWipBean that is stored in session.  If there
	 * is none stored in session, this will add an error to the FacesContext because it should 
	 * not happen.
	 * @return
	 */
    public List<InetAddressBean> getList() {
        final PersonWipBean wip = (PersonWipBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("personWip");
        return InetAddressBean.getInetAddressesFor(wip.getId());
    }

    @Override
    public String toString() {
        return getAddress();
    }
}
