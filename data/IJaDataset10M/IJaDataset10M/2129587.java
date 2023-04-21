package org.geogurus.gas.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.geogurus.data.Datasource;
import org.geogurus.data.Factory;
import org.geogurus.data.database.OracleDatasource;
import org.geogurus.gas.objects.HostDescriptorBean;

/**
 * Strategy for processing oracle connection parameters into Datasources
 * 
 * @author jeichar
 */
public final class OracleFactoryStrategy implements Factory<List<Datasource>, HostDescriptorBean> {

    protected final Logger logger = Logger.getLogger(getClass().getName());

    public boolean canCreateFrom(HostDescriptorBean host) {
        return "ORA".equalsIgnoreCase(host.getType()) || "Oracle".equalsIgnoreCase(host.getType());
    }

    public List<Datasource> create(HostDescriptorBean host) {
        List<Datasource> res = new ArrayList<Datasource>();
        OracleDatasource ds = new OracleDatasource(host.getInstance(), host.getPort(), host.getName(), host.getUname(), host.getUpwd());
        if (ds.load()) {
            res.add(ds);
            logger.fine("datasource added, contains: " + ds.getDataList().size());
        } else {
            logger.warning("getDataInformation failed on datasource: " + host.getName() + " " + host.getInstance() + "\n\tmessage is: " + ds.errorMessage);
        }
        return res;
    }
}
