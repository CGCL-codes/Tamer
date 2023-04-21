package org.dbe.kb.server.srproxyimpl;

import java.io.*;
import org.dbe.kb.server.common.KBconnector;
import org.dbe.kb.server.SRI;
import org.dbe.kb.server.proxyimpl.OQLproxy;
import org.dbe.kb.proxy.QMI;
import org.dbe.kb.server.common.SRinfo;

public class SRproxy extends KBconnector implements SRI, java.io.Serializable {

    private String _serveraddr;

    boolean _newmodel = true;

    boolean _init = false;

    private static OQLproxy _oqlp = null;

    public SRproxy(String serverAdress) {
        _serveraddr = serverAdress;
    }

    /**
     * getSM
     *
     * @param SMID String
     * @return InputStream
     * @throws Exception
     * @todo Implement this org.dbe.kb.srproxy.SRI method
     */
    public InputStream getSM(String SMID) throws Exception {
        _saddr = _serveraddr;
        InputStream in = getConnection(SRinfo.Get_Service_Manifest, SMID);
        return in;
    }

    /**
     * publishSM
     *
     * @param ins InputStream
     * @throws Exception
     * @todo Implement this org.dbe.kb.srproxy.SRI method
     */
    public String publishSM(InputStream ins) throws Exception {
        _saddr = _serveraddr;
        System.out.println("ADDR:" + _saddr);
        System.out.flush();
        try {
            OutputStream out = postConnection(SRinfo.Publish_Service_Manifest);
            write(ins, out);
            InputStream in = getResponse();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            write(in, bout);
            String res = bout.toString();
            System.out.println(res);
            System.out.flush();
            String smid = res.substring(6, res.indexOf("</SMID>"));
            System.out.println("SMID=" + res.substring(6, res.indexOf("</SMID>")) + "|");
            System.out.flush();
            return smid;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IOException("SR: Constraint Violation Error. SM not published");
        }
    }

    public void eraseSM(String SMID) throws Exception {
        _saddr = _serveraddr;
    }

    public QMI getQueryProxy() {
        if (_oqlp != null) return _oqlp;
        _oqlp = new OQLproxy(_init, _serveraddr, SRinfo.SR_Get_OQL_Metamodel, SRinfo.SRProxy_OQL_Models);
        _oqlp.init(false);
        _init = true;
        return _oqlp;
    }
}
