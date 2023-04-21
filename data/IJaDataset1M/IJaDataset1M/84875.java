package com.aimluck.eip.system;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.jar.Attributes;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.cayenne.om.account.EipMCompany;
import com.aimluck.eip.common.ALAbstractSelectData;
import com.aimluck.eip.orm.query.ResultList;
import com.aimluck.eip.system.util.SystemUtils;
import com.aimluck.eip.util.ALServletUtils;

/**
 *
 */
public class SystemNetworkSelectData extends ALAbstractSelectData<EipMCompany, EipMCompany> {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(SystemNetworkSelectData.class.getName());

    /**
   * 
   * @param rundata
   * @param context
   * @return
   */
    @Override
    protected ResultList<EipMCompany> selectList(RunData rundata, Context context) {
        return null;
    }

    /**
   * 
   * @param rundata
   * @param context
   * @return
   */
    @Override
    protected EipMCompany selectDetail(RunData rundata, Context context) {
        return SystemUtils.getEipMCompany(rundata, context);
    }

    /**
   * 
   * @param record
   * @return
   */
    @Override
    protected Object getResultData(EipMCompany record) {
        return null;
    }

    /**
   * 
   * @param record
   * @return
   */
    @Override
    protected Object getResultDataDetail(EipMCompany record) {
        SystemNetworkResultData rd = new SystemNetworkResultData();
        rd.initField();
        try {
            String ipaddress = record.getIpaddressInternal();
            if (null == ipaddress || "".equals(ipaddress)) {
                Enumeration<NetworkInterface> enuIfs = NetworkInterface.getNetworkInterfaces();
                if (null != enuIfs) {
                    while (enuIfs.hasMoreElements()) {
                        NetworkInterface ni = enuIfs.nextElement();
                        Enumeration<InetAddress> enuAddrs = ni.getInetAddresses();
                        while (enuAddrs.hasMoreElements()) {
                            InetAddress in4 = enuAddrs.nextElement();
                            if (!in4.isLoopbackAddress()) {
                                ipaddress = in4.getHostAddress();
                            }
                        }
                    }
                }
            }
            Integer port_internal = record.getPortInternal();
            if (null == port_internal) {
                port_internal = 80;
            }
            String localurl = ALServletUtils.getAccessUrl(ipaddress, port_internal, false);
            String globalurl = ALServletUtils.getAccessUrl(record.getIpaddress(), record.getPort().intValue(), true);
            rd.setLocalUrl(localurl);
            rd.setGlobalUrl(globalurl);
        } catch (SocketException e) {
            logger.error("[SystemNetworkSelectData]", e);
        }
        return rd;
    }

    /**
   * 
   * @return
   */
    @Override
    protected Attributes getColumnMap() {
        return null;
    }
}
