package net.cygeek.tech.client.service;

import java.util.ArrayList;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import net.cygeek.tech.client.logic.EmpreportLogic;
import net.cygeek.tech.client.HsHrEmpreport;
import net.cygeek.tech.client.data.Empreport;
import java.util.Date;

/**
 * Author: Thilina Hasantha
 */
public class EmpreportServiceImpl extends RemoteServiceServlet implements EmpreportService {

    EmpreportLogic logic = EmpreportLogic.getInstance();

    public ArrayList getEmpreports() {
        ArrayList a = new ArrayList();
        ArrayList<HsHrEmpreport> k = logic.getEmpreports();
        for (HsHrEmpreport h : k) {
            a.add(Empreport.getProxy(h));
        }
        return a;
    }

    public Boolean addEmpreport(Empreport mEmpreport, boolean isNew) {
        HsHrEmpreport bean = Empreport.getClass(mEmpreport);
        bean.setNew(isNew);
        return logic.addEmpreport(bean);
    }

    public Boolean deleteEmpreport(String repCode) {
        return logic.delEmpreport(repCode);
    }

    public Empreport getEmpreport(String repCode) {
        HsHrEmpreport k = logic.getEmpreport(repCode);
        return Empreport.getProxy(k);
    }
}
