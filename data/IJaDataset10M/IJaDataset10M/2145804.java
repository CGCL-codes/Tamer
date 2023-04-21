package org.adempierelbr.process;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import org.compiere.model.MLocation;
import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.model.X_C_City;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

/**
 *	ProcUpdateLocation
 *
 *	Process to define the correct C_City_ID for C_Location
 *
 *	@author Mario Grigioni
 *	@version $Id: ProcUpdateLocation.java, 13/11/2009 14:24:00 mgrigioni
 */
public class ProcUpdateLocation extends SvrProcess {

    /**
	 *  Prepare - e.g., get Parameters.
	 */
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null) ; else log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
        }
    }

    /**
	 *  Perform process.
	 *  @return Message
	 *  @throws Exception if not successful
	 */
    protected String doIt() throws Exception {
        log.info("Update C_City_ID - C_Location");
        int C_City_ID = 0;
        int count = 0;
        MLocation[] locations = getLocations();
        for (MLocation location : locations) {
            C_City_ID = getC_City_ID(location.getCity(), location.getC_Region_ID());
            if (C_City_ID > 0) {
                if (C_City_ID != location.getC_City_ID()) {
                    location.setC_City_ID(C_City_ID);
                    if (location.save(get_TrxName())) count++;
                }
            }
        }
        addLog(0, null, new BigDecimal(count), "Registros Atualizados");
        return "Process Completed";
    }

    private int getC_City_ID(String cityName, int C_Region_ID) {
        int C_City_ID = 0;
        String whereClause = "Name=? " + "AND IsActive='Y' " + "AND (AD_Client_ID=0 OR AD_Client_ID=?) " + "AND C_Region_ID=?";
        MTable table = MTable.get(Env.getCtx(), X_C_City.Table_Name);
        Query query = new Query(Env.getCtx(), table, whereClause, null);
        query.setParameters(new Object[] { cityName, Env.getAD_Client_ID(getCtx()), C_Region_ID });
        query.setOrderBy("C_City_ID DESC");
        List<X_C_City> listCity = query.list();
        if (listCity.size() > 0) {
            X_C_City city = listCity.get(0);
            C_City_ID = city.get_ID();
        }
        return C_City_ID;
    }

    private MLocation[] getLocations() {
        String whereClause = "AD_Client_ID=?";
        MTable table = MTable.get(Env.getCtx(), MLocation.Table_Name);
        Query query = new Query(Env.getCtx(), table, whereClause, null);
        query.setParameters(new Object[] { Env.getAD_Client_ID(getCtx()) });
        List<MLocation> list = query.list();
        return list.toArray(new MLocation[list.size()]);
    }
}
