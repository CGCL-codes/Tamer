package hu.sztaki.lpds.information.net.wsaxis13;

import hu.sztaki.lpds.information.com.ServiceType;
import hu.sztaki.lpds.information.data.GuseServiceBean;
import hu.sztaki.lpds.information.data.ServicePropertyBean;
import hu.sztaki.lpds.information.inf.InformationService;
import hu.sztaki.lpds.information.service.alice.Base;
import hu.sztaki.lpds.information.service.alice.DH;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

/**
 * @author krisztian
 */
public class InformationServiceImpl implements InformationService {

    /**
 * @see hu.sztaki.lpds.information.inf.InformationService#getService(java.lang.String, java.lang.String, java.util.Hashtable, java.util.Vector)
 * @throws Exception service not found
 */
    @Override
    public ServiceType getService(String pStype, String pFrom, Hashtable pParam, Vector pFault) throws Exception {
        return Base.getI().getService(pStype, pFrom, pParam, pFault);
    }

    /**
 * @see hu.sztaki.lpds.information.inf.InformationService#getAllService(java.lang.String, java.lang.String, java.util.Hashtable)
 * @throws Exception service service not found
 */
    @Override
    public Vector<ServiceType> getAllService(String pStype, String pFrom, Hashtable pParam) throws Exception {
        return Base.getI().getAllService(pStype, pFrom, pParam);
    }

    /**
 * @see hu.sztaki.lpds.information.inf.InformationService
 */
    @Override
    public HashMap getAllProperties(String pServiceURL) throws Exception {
        HashMap res = new HashMap();
        try {
            List<GuseServiceBean> tmp = DH.getI().getGuseService(pServiceURL);
            for (ServicePropertyBean t : tmp.get(0).getProperties()) res.put(t.getPropkey(), t.getPropvalue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
