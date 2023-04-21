package com.orientechnologies.odbms.client;

import javax.jdo.JDOFatalDataStoreException;
import com.orientechnologies.jdo.types.oBinary;
import com.orientechnologies.jdo.utils.oFastEncryption;
import com.orientechnologies.jdo.utils.oFormatOutput;
import com.orientechnologies.odbms.enterprise.comm.ConnectionManager;
import com.orientechnologies.odbms.enterprise.comm.LogicConnection;
import com.orientechnologies.odbms.enterprise.comm.bus.Bus;
import com.orientechnologies.odbms.enterprise.comm.bus.BusFactory;
import com.orientechnologies.odbms.enterprise.comm.bus.BusManager;
import com.orientechnologies.odbms.enterprise.interfaces.misc.IBuffer;
import com.orientechnologies.odbms.enterprise.interfaces.request.IServerAdminExecuteA;
import com.orientechnologies.odbms.enterprise.message.RequestManager;

public class ClientAdmin {

    public ClientAdmin(String iHost, String iPort, String iUserName, String iUserPasswd) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
        host = iHost;
        port = iPort;
        oFastEncryption encryption = new oFastEncryption();
        userName = oFormatOutput.toAscii(encryption.crypt(iUserName, TMP));
        userPasswd = oFormatOutput.toAscii(encryption.crypt(iUserPasswd, TMP));
        BusFactory factory = (BusFactory) BusManager.getInstance().getFactory("TCP").newInstance();
        Bus bus = factory.createBus("host:" + host + ",port:" + port);
        if (bus == null) throw new JDOFatalDataStoreException("Orient Server not ready, host: " + host + " port:" + port);
        lconn = ConnectionManager.getInstance().createActiveConnection(bus, bus.getLocalPort(), ConnectionManager.KIND_APP);
    }

    public oBinary execute(String iNode, String iValue) {
        IServerAdminExecuteA objA = new IServerAdminExecuteA(userName, userPasswd, iNode, iValue);
        IBuffer objR = (IBuffer) RequestManager.getInstance().sendSynchRequest(null, "", -1, lconn.getId(), objA, IBuffer.class);
        return objR.getBuffer();
    }

    private String host;

    private String port;

    private String userName;

    private String userPasswd;

    private LogicConnection lconn;

    private static final String TMP = "fshj79dcjkduedsk1e27deiucb";
}
