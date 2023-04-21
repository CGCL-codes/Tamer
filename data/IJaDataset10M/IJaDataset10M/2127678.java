package org.dbe.kb.p2p;

import java.security.MessageDigest;
import java.net.InetAddress;
import org.dbe.kb.service.XDBhandler;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.Collection;
import org.dbe.servent.ServiceContext;
import java.io.ByteArrayInputStream;
import org.dbe.kb.util.Filesystem;
import org.dbe.kb.service.CoreService;

public class PeerManager extends Thread {

    public static final String _PEER_IDENTIFIER = "dbe://peer-id";

    public static String _PEER_INIT = "dbe://peer-init/";

    public static String _PEER_ALIVE = "dbe://peer-info/";

    public static final long _INFO_PERIOD = 300000;

    private XDBhandler _xdbHandler;

    private String _peerId;

    private float _peerStabilityFactor;

    private long _initTime;

    private long _startTime;

    private long _workingTime;

    private Logger _logger;

    private boolean _stop;

    private String _serviceName;

    private ServiceContext _sc;

    public void setServiceContext(ServiceContext sc) {
        _sc = sc;
    }

    public PeerManager(XDBhandler xdbHandler, String serviceName) {
        _peerId = null;
        _PEER_INIT += serviceName;
        _PEER_ALIVE += serviceName;
        _serviceName = serviceName;
        _workingTime = 0;
        _xdbHandler = xdbHandler;
        _startTime = System.currentTimeMillis();
        _initTime = _startTime;
        _logger = CoreService._logger;
        _logger.debug("PM[" + _serviceName + "]:Start Peer manager");
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String peerIDdoc = _xdbHandler.retrieve(_PEER_IDENTIFIER, _xdbHandler._VAR_CONTAINER);
            if (peerIDdoc != null) {
                Document doc = builder.parse(new java.io.ByteArrayInputStream(peerIDdoc.getBytes()));
                _peerId = doc.getFirstChild().getFirstChild().getNodeValue();
            }
            String startTimeDoc = _xdbHandler.retrieve(_PEER_INIT, _xdbHandler._VAR_CONTAINER);
            if (startTimeDoc != null) {
                Document doc = builder.parse(new java.io.ByteArrayInputStream(startTimeDoc.getBytes()));
                _initTime = Long.parseLong(doc.getFirstChild().getFirstChild().getNodeValue());
            }
            String workTimeDoc = _xdbHandler.retrieve(_PEER_ALIVE, _xdbHandler._VAR_CONTAINER);
            if (workTimeDoc != null) {
                Document doc = builder.parse(new java.io.ByteArrayInputStream(workTimeDoc.getBytes()));
                _workingTime = Long.parseLong(doc.getFirstChild().getFirstChild().getNodeValue());
            }
        } catch (Exception ex) {
            _logger.debug("Error", ex);
        }
    }

    private static byte[] getshaID(String source) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] bytes = md.digest(source.getBytes());
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String hexEncode(byte[] aInput) {
        StringBuffer result = new StringBuffer();
        char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        for (int idx = 0; idx < aInput.length; ++idx) {
            byte b = aInput[idx];
            result.append(digits[(b & 0xf0) >> 4]);
            result.append(digits[b & 0x0f]);
        }
        return result.toString();
    }

    public float getPeerStabilityFactor() {
        String csf = _sc.getParameter("calculateSF");
        if (csf != null && csf.equals("off")) return 1f; else return _peerStabilityFactor;
    }

    public String generatePeerID() {
        try {
            if (_peerId != null) return _peerId;
            InetAddress thisIp = InetAddress.getLocalHost();
            return hexEncode(getshaID(thisIp.getHostAddress() + System.currentTimeMillis()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void configurePeerService(String dir) {
        _peerId = generatePeerID();
        String filename = dir + "/" + "deployment.props";
        System.out.println(filename);
        String ff = Filesystem.readFile(filename);
        Filesystem.writeFile(filename, ff.replaceFirst("DBEID", _serviceName + "-" + _peerId));
        ff = Filesystem.readFile(filename);
        Filesystem.writeFile(filename, ff.replaceFirst("DBEID", _serviceName + "-" + _peerId));
        String peerIDdoc = "<DBE-PEER-ID>" + _peerId + "</DBE-PEER-ID>";
        _xdbHandler.store(_PEER_IDENTIFIER, false, peerIDdoc, "DBE Peer Identifier", _xdbHandler._VAR_CONTAINER, false);
    }

    public void stopP2Pmanager() {
        _stop = true;
    }

    public Collection processRequest(String query, String skey) {
        ArrayList ret = ret = new ArrayList();
        ;
        try {
            DocumentBuilder dbuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dbuilder.parse(new ByteArrayInputStream(query.getBytes()));
            float sf = Float.parseFloat(doc.getFirstChild().getFirstChild().getFirstChild().getNodeValue());
            long size = Long.parseLong(doc.getFirstChild().getLastChild().getFirstChild().getNodeValue());
            _logger.debug("PM[" + _serviceName + "]: Process Request for SF=" + sf + " and size =" + size);
            if (sf <= _peerStabilityFactor) {
                _logger.debug("PM[" + _serviceName + "]:Accept request to host replicas");
                String[] sres = new String[2];
                sres[0] = skey;
                sres[1] = String.valueOf(getPeerStabilityFactor());
                ret.add(sres);
            }
        } catch (Exception ex) {
            _logger.debug("Error", ex);
        }
        return ret;
    }

    public synchronized void calculateStabilityFactor() {
        long curtime = System.currentTimeMillis();
        if (_initTime != _startTime) _peerStabilityFactor = ((float) (_workingTime + curtime - _startTime)) / ((float) (curtime - _initTime)); else _peerStabilityFactor = 1L;
        _logger.debug("PM[" + _serviceName + "]:Independent Peer Stability Factor = " + _peerStabilityFactor);
    }

    public void run() {
        String workTimeDoc = null;
        if (_workingTime == 0) {
            String startTimeDoc = "<PEER-INIT>" + _initTime + "</PEER-INIT>";
            _xdbHandler.store(_PEER_INIT, false, startTimeDoc, "Local Peer Init", _xdbHandler._VAR_CONTAINER, false);
        }
        while (!_stop) {
            calculateStabilityFactor();
            workTimeDoc = "<PEER-ALIVE>" + (_workingTime + System.currentTimeMillis() - _startTime) + "</PEER-ALIVE>";
            _xdbHandler.store(_PEER_ALIVE, false, workTimeDoc, "Local Peer Information", _xdbHandler._VAR_CONTAINER, false);
            try {
                sleep(_INFO_PERIOD);
            } catch (Exception ex) {
                _logger.debug("Error", ex);
            }
        }
    }
}
