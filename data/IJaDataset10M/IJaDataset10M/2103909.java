package com.sun.jmx.snmp.daemon;

import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.io.InterruptedIOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import com.sun.jmx.snmp.SnmpMessage;
import com.sun.jmx.snmp.SnmpPduFactory;
import com.sun.jmx.snmp.SnmpPduBulk;
import com.sun.jmx.snmp.SnmpPduPacket;
import com.sun.jmx.snmp.SnmpPduRequest;
import com.sun.jmx.snmp.SnmpPduTrap;
import com.sun.jmx.snmp.SnmpValue;
import com.sun.jmx.snmp.SnmpVarBind;
import com.sun.jmx.snmp.SnmpVarBindList;
import com.sun.jmx.snmp.SnmpDefinitions;
import com.sun.jmx.snmp.SnmpStatusException;
import com.sun.jmx.snmp.SnmpTooBigException;
import com.sun.jmx.snmp.SnmpDataTypeEnums;
import com.sun.jmx.trace.Trace;
import com.sun.jmx.snmp.agent.SnmpMibAgent;
import com.sun.jmx.snmp.agent.SnmpUserDataFactory;
import com.sun.jmx.snmp.InetAddressAcl;

class SnmpRequestHandler extends ClientHandler implements SnmpDefinitions {

    private transient DatagramSocket socket = null;

    private transient DatagramPacket packet = null;

    private transient Vector mibs = null;

    /**
     * Contains the list of sub-requests associated to the current request.
     */
    private transient Hashtable subs = null;

    /**
     * Reference on the MIBS
     */
    private transient SnmpMibTree root;

    private transient Object ipacl = null;

    private transient SnmpPduFactory pduFactory = null;

    private transient SnmpUserDataFactory userDataFactory = null;

    private transient SnmpAdaptorServer adaptor = null;

    /**
     * Full constructor
     */
    public SnmpRequestHandler(SnmpAdaptorServer server, int id, DatagramSocket s, DatagramPacket p, SnmpMibTree tree, Vector m, Object a, SnmpPduFactory factory, SnmpUserDataFactory dataFactory, MBeanServer f, ObjectName n) {
        super(server, id, f, n);
        adaptor = server;
        socket = s;
        packet = p;
        root = tree;
        mibs = (Vector) m.clone();
        subs = new Hashtable(mibs.size());
        ipacl = a;
        pduFactory = factory;
        userDataFactory = dataFactory;
    }

    /**
     * Treat the request available in 'packet' and send the result
     * back to the client.
     * Note: we overwrite 'packet' with the response bytes.
     */
    public void doRun() {
        if (isTraceOn()) {
            trace("doRun", "Packet received:\n" + SnmpMessage.dumpHexBuffer(packet.getData(), 0, packet.getLength()));
        }
        DatagramPacket respPacket = makeResponsePacket(packet);
        if (isTraceOn() && (respPacket != null)) {
            trace("doRun", "Packet to be sent:\n" + SnmpMessage.dumpHexBuffer(respPacket.getData(), 0, respPacket.getLength()));
        }
        if (respPacket != null) {
            try {
                socket.send(respPacket);
            } catch (SocketException e) {
                if (isDebugOn()) {
                    if (e.getMessage().equals(InterruptSysCallMsg)) debug("doRun", "interrupted"); else {
                        debug("doRun", "i/o exception");
                        debug("doRun", e);
                    }
                }
            } catch (InterruptedIOException e) {
                if (isDebugOn()) {
                    debug("doRun", "interrupted");
                }
            } catch (Exception e) {
                if (isDebugOn()) {
                    debug("doRun", "failure when sending response");
                    debug("doRun", e);
                }
            }
        }
    }

    /**
     * Here we make a response packet from a request packet.
     * We return null if there no response packet to sent.
     */
    private DatagramPacket makeResponsePacket(DatagramPacket reqPacket) {
        DatagramPacket respPacket = null;
        SnmpMessage reqMsg = new SnmpMessage();
        try {
            reqMsg.decodeMessage(reqPacket.getData(), reqPacket.getLength());
            reqMsg.address = reqPacket.getAddress();
            reqMsg.port = reqPacket.getPort();
        } catch (SnmpStatusException x) {
            if (isDebugOn()) {
                debug("makeResponsePacket", "packet decoding failed");
                debug("makeResponsePacket", x);
            }
            reqMsg = null;
            ((SnmpAdaptorServer) adaptorServer).incSnmpInASNParseErrs(1);
        }
        SnmpMessage respMsg = null;
        if (reqMsg != null) {
            respMsg = makeResponseMessage(reqMsg);
        }
        if (respMsg != null) {
            try {
                reqPacket.setLength(respMsg.encodeMessage(reqPacket.getData()));
                respPacket = reqPacket;
            } catch (SnmpTooBigException x) {
                if (isDebugOn()) {
                    debug("makeResponsePacket", "response message is too big");
                }
                try {
                    respMsg = newTooBigMessage(reqMsg);
                    reqPacket.setLength(respMsg.encodeMessage(reqPacket.getData()));
                    respPacket = reqPacket;
                } catch (SnmpTooBigException xx) {
                    if (isDebugOn()) {
                        debug("makeResponsePacket", "'too big' is 'too big' !!!");
                    }
                    adaptor.incSnmpSilentDrops(1);
                }
            }
        }
        return respPacket;
    }

    /**
     * Here we make a response message from a request message.
     * We return null if there is no message to reply.
     */
    private SnmpMessage makeResponseMessage(SnmpMessage reqMsg) {
        SnmpMessage respMsg = null;
        SnmpPduPacket reqPdu = null;
        Object userData = null;
        try {
            reqPdu = (SnmpPduPacket) pduFactory.decodeSnmpPdu(reqMsg);
            if (reqPdu != null && userDataFactory != null) userData = userDataFactory.allocateUserData(reqPdu);
        } catch (SnmpStatusException x) {
            reqPdu = null;
            SnmpAdaptorServer snmpServer = (SnmpAdaptorServer) adaptorServer;
            snmpServer.incSnmpInASNParseErrs(1);
            if (x.getStatus() == SnmpDefinitions.snmpWrongSnmpVersion) snmpServer.incSnmpInBadVersions(1);
            if (isDebugOn()) {
                debug("makeResponseMessage", "message decoding failed");
                debug("makeResponseMessage", x);
            }
        }
        SnmpPduPacket respPdu = null;
        if (reqPdu != null) {
            respPdu = makeResponsePdu(reqPdu, userData);
            try {
                if (userDataFactory != null) userDataFactory.releaseUserData(userData, respPdu);
            } catch (SnmpStatusException x) {
                respPdu = null;
            }
        }
        if (respPdu != null) {
            try {
                respMsg = (SnmpMessage) pduFactory.encodeSnmpPdu(respPdu, packet.getData().length);
            } catch (SnmpStatusException x) {
                respMsg = null;
                if (isDebugOn()) {
                    debug("makeResponseMessage", "failure when encoding the response message");
                    debug("makeResponseMessage", x);
                }
            } catch (SnmpTooBigException x) {
                if (isDebugOn()) {
                    debug("makeResponseMessage", "response message is too big");
                }
                try {
                    if (packet.getData().length <= 32) throw x;
                    int pos = x.getVarBindCount();
                    if (isDebugOn()) {
                        debug("makeResponseMessage", "fail on element" + pos);
                    }
                    int old = 0;
                    while (true) {
                        try {
                            respPdu = reduceResponsePdu(reqPdu, respPdu, pos);
                            respMsg = (SnmpMessage) pduFactory.encodeSnmpPdu(respPdu, packet.getData().length - 32);
                            break;
                        } catch (SnmpTooBigException xx) {
                            if (isDebugOn()) {
                                debug("makeResponseMessage", "response message is still too big");
                            }
                            old = pos;
                            pos = xx.getVarBindCount();
                            if (isDebugOn()) {
                                debug("makeResponseMessage", "fail on element" + pos);
                            }
                            if (pos == old) {
                                throw xx;
                            }
                        }
                    }
                } catch (SnmpStatusException xx) {
                    respMsg = null;
                    if (isDebugOn()) {
                        debug("makeResponseMessage", "failure when encoding the response message");
                        debug("makeResponseMessage", xx);
                    }
                } catch (SnmpTooBigException xx) {
                    try {
                        respPdu = newTooBigPdu(reqPdu);
                        respMsg = (SnmpMessage) pduFactory.encodeSnmpPdu(respPdu, packet.getData().length);
                    } catch (SnmpTooBigException xxx) {
                        respMsg = null;
                        if (isDebugOn()) {
                            debug("makeResponseMessage", "'too big' is 'too big' !!!");
                        }
                        adaptor.incSnmpSilentDrops(1);
                    } catch (Exception xxx) {
                        debug("makeResponseMessage", xxx);
                        respMsg = null;
                    }
                } catch (Exception xx) {
                    debug("makeResponseMessage", xx);
                    respMsg = null;
                }
            }
        }
        return respMsg;
    }

    /**
     * Here we make a response pdu from a request pdu.
     * We return null if there is no pdu to reply.
     */
    private SnmpPduPacket makeResponsePdu(SnmpPduPacket reqPdu, Object userData) {
        SnmpAdaptorServer snmpServer = (SnmpAdaptorServer) adaptorServer;
        SnmpPduPacket respPdu = null;
        snmpServer.updateRequestCounters(reqPdu.type);
        if (reqPdu.varBindList != null) snmpServer.updateVarCounters(reqPdu.type, reqPdu.varBindList.length);
        if (checkPduType(reqPdu)) {
            respPdu = checkAcl(reqPdu);
            if (respPdu == null) {
                if (mibs.size() < 1) {
                    if (isTraceOn()) {
                        trace("makeResponsePdu", "Request " + reqPdu.requestId + " received but no MIB registered.");
                    }
                    return makeNoMibErrorPdu((SnmpPduRequest) reqPdu, userData);
                }
                switch(reqPdu.type) {
                    case SnmpPduPacket.pduGetRequestPdu:
                    case SnmpPduPacket.pduGetNextRequestPdu:
                    case SnmpPduPacket.pduSetRequestPdu:
                        respPdu = makeGetSetResponsePdu((SnmpPduRequest) reqPdu, userData);
                        break;
                    case SnmpPduPacket.pduGetBulkRequestPdu:
                        respPdu = makeGetBulkResponsePdu((SnmpPduBulk) reqPdu, userData);
                        break;
                }
            } else {
                if (!snmpServer.getAuthRespEnabled()) {
                    respPdu = null;
                }
                if (snmpServer.getAuthTrapEnabled()) {
                    try {
                        snmpServer.snmpV1Trap(SnmpPduTrap.trapAuthenticationFailure, 0, new SnmpVarBindList());
                    } catch (Exception x) {
                        if (isDebugOn()) {
                            debug("makeResponsePdu", "failure when sending authentication trap");
                            debug("makeResponsePdu", x);
                        }
                    }
                }
            }
        }
        return respPdu;
    }

    SnmpPduPacket makeErrorVarbindPdu(SnmpPduPacket req, int statusTag) {
        final SnmpVarBind[] vblist = req.varBindList;
        final int length = vblist.length;
        switch(statusTag) {
            case SnmpDataTypeEnums.errEndOfMibViewTag:
                for (int i = 0; i < length; i++) vblist[i].value = SnmpVarBind.endOfMibView;
                break;
            case SnmpDataTypeEnums.errNoSuchObjectTag:
                for (int i = 0; i < length; i++) vblist[i].value = SnmpVarBind.noSuchObject;
                break;
            case SnmpDataTypeEnums.errNoSuchInstanceTag:
                for (int i = 0; i < length; i++) vblist[i].value = SnmpVarBind.noSuchInstance;
                break;
            default:
                return newErrorResponsePdu(req, snmpRspGenErr, 1);
        }
        return newValidResponsePdu(req, vblist);
    }

    SnmpPduPacket makeNoMibErrorPdu(SnmpPduRequest req, Object userData) {
        if (req.version == SnmpDefinitions.snmpVersionOne) {
            return newErrorResponsePdu(req, snmpRspNoSuchName, 1);
        } else if (req.version == SnmpDefinitions.snmpVersionTwo) {
            switch(req.type) {
                case pduSetRequestPdu:
                case pduWalkRequest:
                    return newErrorResponsePdu(req, snmpRspNoAccess, 1);
                case pduGetRequestPdu:
                    return makeErrorVarbindPdu(req, SnmpDataTypeEnums.errNoSuchObjectTag);
                case pduGetNextRequestPdu:
                case pduGetBulkRequestPdu:
                    return makeErrorVarbindPdu(req, SnmpDataTypeEnums.errEndOfMibViewTag);
                default:
            }
        }
        return newErrorResponsePdu(req, snmpRspGenErr, 1);
    }

    /**
     * Here we make the response pdu from a get/set request pdu.
     * At this level, the result is never null.
     */
    private SnmpPduPacket makeGetSetResponsePdu(SnmpPduRequest req, Object userData) {
        if (req.varBindList == null) {
            return newValidResponsePdu(req, null);
        }
        splitRequest(req);
        int nbSubRequest = subs.size();
        if (nbSubRequest == 1) return turboProcessingGetSet(req, userData);
        SnmpPduPacket result = executeSubRequest(req, userData);
        if (result != null) return result;
        if (isTraceOn()) {
            trace("makeGetSetResponsePdu", "Build the unified response for request " + req.requestId);
        }
        return mergeResponses(req);
    }

    /**
     * The method runs all the sub-requests associated to the current 
     * instance of SnmpRequestHandler.
     */
    private SnmpPduPacket executeSubRequest(SnmpPduPacket req, Object userData) {
        int errorStatus = SnmpDefinitions.snmpRspNoError;
        int nbSubRequest = subs.size();
        int i = 0;
        if (req.type == pduSetRequestPdu) {
            i = 0;
            for (Enumeration e = subs.elements(); e.hasMoreElements(); i++) {
                SnmpSubRequestHandler sub = (SnmpSubRequestHandler) e.nextElement();
                sub.setUserData(userData);
                sub.type = pduWalkRequest;
                sub.run();
                sub.type = pduSetRequestPdu;
                if (sub.getErrorStatus() != SnmpDefinitions.snmpRspNoError) {
                    if (isDebugOn()) {
                        debug("executeSubRequest", "an error occurs");
                    }
                    return newErrorResponsePdu(req, errorStatus, sub.getErrorIndex() + 1);
                }
            }
        }
        i = 0;
        for (Enumeration e = subs.elements(); e.hasMoreElements(); i++) {
            SnmpSubRequestHandler sub = (SnmpSubRequestHandler) e.nextElement();
            sub.setUserData(userData);
            sub.run();
            if (sub.getErrorStatus() != SnmpDefinitions.snmpRspNoError) {
                if (isDebugOn()) {
                    debug("executeSubRequest", "an error occurs");
                }
                return newErrorResponsePdu(req, errorStatus, sub.getErrorIndex() + 1);
            }
        }
        return null;
    }

    /**
     * Optimize when there is only one sub request
     */
    private SnmpPduPacket turboProcessingGetSet(SnmpPduRequest req, Object userData) {
        int errorStatus = SnmpDefinitions.snmpRspNoError;
        SnmpSubRequestHandler sub = (SnmpSubRequestHandler) subs.elements().nextElement();
        sub.setUserData(userData);
        if (req.type == SnmpDefinitions.pduSetRequestPdu) {
            sub.type = pduWalkRequest;
            sub.run();
            sub.type = pduSetRequestPdu;
            errorStatus = sub.getErrorStatus();
            if (errorStatus != SnmpDefinitions.snmpRspNoError) {
                return newErrorResponsePdu(req, errorStatus, sub.getErrorIndex() + 1);
            }
        }
        sub.run();
        errorStatus = sub.getErrorStatus();
        if (errorStatus != SnmpDefinitions.snmpRspNoError) {
            if (isDebugOn()) {
                debug("turboProcessingGetSet", "an error occurs");
            }
            int realIndex = sub.getErrorIndex() + 1;
            return newErrorResponsePdu(req, errorStatus, realIndex);
        }
        if (isTraceOn()) {
            trace("turboProcessingGetSet", "build the unified response for request " + req.requestId);
        }
        return mergeResponses(req);
    }

    /**
     * Here we make the response pdu for a bulk request.
     * At this level, the result is never null.
     */
    private SnmpPduPacket makeGetBulkResponsePdu(SnmpPduBulk req, Object userData) {
        SnmpVarBind[] respVarBindList = null;
        int L = req.varBindList.length;
        int N = Math.max(Math.min(req.nonRepeaters, L), 0);
        int M = Math.max(req.maxRepetitions, 0);
        int R = L - N;
        if (req.varBindList == null) {
            return newValidResponsePdu(req, null);
        }
        splitBulkRequest(req, N, M, R);
        SnmpPduPacket result = executeSubRequest(req, userData);
        if (result != null) return result;
        respVarBindList = mergeBulkResponses(N + (M * R));
        int m2;
        int t = respVarBindList.length;
        while ((t > N) && (respVarBindList[t - 1].value.equals(SnmpVarBind.endOfMibView))) {
            t--;
        }
        if (t == N) m2 = N + R; else m2 = N + ((t - 1 - N) / R + 2) * R;
        if (m2 < respVarBindList.length) {
            SnmpVarBind[] truncatedList = new SnmpVarBind[m2];
            for (int i = 0; i < m2; i++) {
                truncatedList[i] = respVarBindList[i];
            }
            respVarBindList = truncatedList;
        }
        return newValidResponsePdu(req, respVarBindList);
    }

    /**
     * Check the type of the pdu: only the get/set/bulk request
     * are accepted.
     */
    private boolean checkPduType(SnmpPduPacket pdu) {
        boolean result = true;
        switch(pdu.type) {
            case SnmpDefinitions.pduGetRequestPdu:
            case SnmpDefinitions.pduGetNextRequestPdu:
            case SnmpDefinitions.pduSetRequestPdu:
            case SnmpDefinitions.pduGetBulkRequestPdu:
                result = true;
                break;
            default:
                if (isDebugOn()) {
                    debug("checkPduType", "cannot respond to this kind of PDU");
                }
                result = false;
                break;
        }
        return result;
    }

    /**
     * Check if the specified pdu is conform to the ACL.
     * This method returns null if the pdu is ok. If not, it returns
     * the response pdu to be replied.
     */
    private SnmpPduPacket checkAcl(SnmpPduPacket pdu) {
        SnmpPduPacket response = null;
        String community = new String(pdu.community);
        if (ipacl != null) {
            if (pdu.type == SnmpDefinitions.pduSetRequestPdu) {
                if (!((InetAddressAcl) ipacl).checkWritePermission(pdu.address, community)) {
                    if (isTraceOn()) {
                        trace("checkAcl", "sender is " + pdu.address + " with " + community);
                        trace("checkAcl", "sender has no write permission");
                    }
                    int err = SnmpSubRequestHandler.mapErrorStatus(SnmpDefinitions.snmpRspAuthorizationError, pdu.version, pdu.type);
                    response = newErrorResponsePdu(pdu, err, 0);
                } else {
                    if (isTraceOn()) {
                        trace("checkAcl", "sender is " + pdu.address + " with " + community);
                        trace("checkAcl", "sender has write permission");
                    }
                }
            } else {
                if (!((InetAddressAcl) ipacl).checkReadPermission(pdu.address, community)) {
                    if (isTraceOn()) {
                        trace("checkAcl", "sender is " + pdu.address + " with " + community);
                        trace("checkAcl", "sender has no read permission");
                    }
                    int err = SnmpSubRequestHandler.mapErrorStatus(SnmpDefinitions.snmpRspAuthorizationError, pdu.version, pdu.type);
                    response = newErrorResponsePdu(pdu, err, 0);
                    SnmpAdaptorServer snmpServer = (SnmpAdaptorServer) adaptorServer;
                    snmpServer.updateErrorCounters(SnmpDefinitions.snmpRspNoSuchName);
                } else {
                    if (isTraceOn()) {
                        trace("checkAcl", "sender is " + pdu.address + " with " + community);
                        trace("checkAcl", "sender has read permission");
                    }
                }
            }
        }
        if (response != null) {
            SnmpAdaptorServer snmpServer = (SnmpAdaptorServer) adaptorServer;
            snmpServer.incSnmpInBadCommunityUses(1);
            if (((InetAddressAcl) ipacl).checkCommunity(community) == false) snmpServer.incSnmpInBadCommunityNames(1);
        }
        return response;
    }

    /**
     * Make a response pdu with the specified error status and index.
     * NOTE: the response pdu share its varBindList with the request pdu. 
     */
    private SnmpPduRequest newValidResponsePdu(SnmpPduPacket reqPdu, SnmpVarBind[] varBindList) {
        SnmpPduRequest result = new SnmpPduRequest();
        result.address = reqPdu.address;
        result.port = reqPdu.port;
        result.version = reqPdu.version;
        result.community = reqPdu.community;
        result.type = result.pduGetResponsePdu;
        result.requestId = reqPdu.requestId;
        result.errorStatus = SnmpDefinitions.snmpRspNoError;
        result.errorIndex = 0;
        result.varBindList = varBindList;
        ((SnmpAdaptorServer) adaptorServer).updateErrorCounters(result.errorStatus);
        return result;
    }

    /**
     * Make a response pdu with the specified error status and index.
     * NOTE: the response pdu share its varBindList with the request pdu. 
     */
    private SnmpPduRequest newErrorResponsePdu(SnmpPduPacket req, int s, int i) {
        SnmpPduRequest result = newValidResponsePdu(req, null);
        result.errorStatus = s;
        result.errorIndex = i;
        result.varBindList = req.varBindList;
        ((SnmpAdaptorServer) adaptorServer).updateErrorCounters(result.errorStatus);
        return result;
    }

    private SnmpMessage newTooBigMessage(SnmpMessage reqMsg) throws SnmpTooBigException {
        SnmpMessage result = null;
        SnmpPduPacket reqPdu = null;
        try {
            reqPdu = (SnmpPduPacket) pduFactory.decodeSnmpPdu(reqMsg);
            if (reqPdu != null) {
                SnmpPduPacket respPdu = newTooBigPdu(reqPdu);
                result = (SnmpMessage) pduFactory.encodeSnmpPdu(respPdu, packet.getData().length);
            }
        } catch (SnmpStatusException x) {
            debug("InternalError: ", x);
            throw new InternalError();
        }
        return result;
    }

    private SnmpPduPacket newTooBigPdu(SnmpPduPacket req) {
        SnmpPduRequest result = newErrorResponsePdu(req, SnmpDefinitions.snmpRspTooBig, 0);
        result.varBindList = null;
        return result;
    }

    private SnmpPduPacket reduceResponsePdu(SnmpPduPacket req, SnmpPduPacket resp, int acceptedVbCount) throws SnmpTooBigException {
        if (req.type != req.pduGetBulkRequestPdu) {
            if (isDebugOn()) {
                debug("reduceResponsePdu", "cannot remove anything");
            }
            throw new SnmpTooBigException(acceptedVbCount);
        }
        int vbCount = resp.varBindList.length;
        if (acceptedVbCount >= 3) vbCount = Math.min(acceptedVbCount - 1, resp.varBindList.length); else if (acceptedVbCount == 1) vbCount = 1; else vbCount = resp.varBindList.length / 2;
        if (vbCount < 1) {
            if (isDebugOn()) {
                debug("reduceResponsePdu", "cannot remove anything");
            }
            throw new SnmpTooBigException(acceptedVbCount);
        } else {
            SnmpVarBind[] newVbList = new SnmpVarBind[vbCount];
            for (int i = 0; i < vbCount; i++) {
                newVbList[i] = resp.varBindList[i];
            }
            if (isDebugOn()) {
                debug("reduceResponsePdu", (resp.varBindList.length - newVbList.length) + " items have been removed");
            }
            resp.varBindList = newVbList;
        }
        return resp;
    }

    /**
     * The method takes the incoming requests and split it into subrequests.
     */
    private void splitRequest(SnmpPduRequest req) {
        int nbAgents = mibs.size();
        SnmpMibAgent agent = (SnmpMibAgent) mibs.firstElement();
        if (nbAgents == 1) {
            subs.put(agent, new SnmpSubRequestHandler(agent, req, true));
            return;
        }
        if (req.type == pduGetNextRequestPdu) {
            for (Enumeration e = mibs.elements(); e.hasMoreElements(); ) {
                SnmpMibAgent ag = (SnmpMibAgent) e.nextElement();
                subs.put(ag, new SnmpSubNextRequestHandler(adaptor, ag, req));
            }
            return;
        }
        int nbReqs = req.varBindList.length;
        SnmpVarBind[] vars = req.varBindList;
        SnmpSubRequestHandler sub;
        for (int i = 0; i < nbReqs; i++) {
            agent = root.getAgentMib(vars[i].oid);
            sub = (SnmpSubRequestHandler) subs.get(agent);
            if (sub == null) {
                sub = new SnmpSubRequestHandler(agent, req);
                subs.put(agent, sub);
            }
            sub.updateRequest(vars[i], i);
        }
    }

    /**
     * The method takes the incoming get bulk requests and split it into 
     * subrequests.
     */
    private void splitBulkRequest(SnmpPduBulk req, int nonRepeaters, int maxRepetitions, int R) {
        for (Enumeration e = mibs.elements(); e.hasMoreElements(); ) {
            SnmpMibAgent agent = (SnmpMibAgent) e.nextElement();
            if (isDebugOn()) trace("splitBulkRequest", "Create a sub with : " + agent + " " + nonRepeaters + " " + maxRepetitions + " " + R);
            subs.put(agent, new SnmpSubBulkRequestHandler(adaptor, agent, req, nonRepeaters, maxRepetitions, R));
        }
        return;
    }

    private SnmpPduPacket mergeResponses(SnmpPduRequest req) {
        if (req.type == pduGetNextRequestPdu) {
            return mergeNextResponses(req);
        }
        SnmpVarBind[] result = req.varBindList;
        for (Enumeration e = subs.elements(); e.hasMoreElements(); ) {
            SnmpSubRequestHandler sub = (SnmpSubRequestHandler) e.nextElement();
            sub.updateResult(result);
        }
        return newValidResponsePdu(req, result);
    }

    private SnmpPduPacket mergeNextResponses(SnmpPduRequest req) {
        int max = req.varBindList.length;
        SnmpVarBind[] result = new SnmpVarBind[max];
        for (Enumeration e = subs.elements(); e.hasMoreElements(); ) {
            SnmpSubRequestHandler sub = (SnmpSubRequestHandler) e.nextElement();
            sub.updateResult(result);
        }
        if (req.version == snmpVersionTwo) {
            return newValidResponsePdu(req, result);
        }
        for (int i = 0; i < max; i++) {
            SnmpValue val = result[i].value;
            if (val == SnmpVarBind.endOfMibView) return newErrorResponsePdu(req, SnmpDefinitions.snmpRspNoSuchName, i + 1);
        }
        return newValidResponsePdu(req, result);
    }

    private SnmpVarBind[] mergeBulkResponses(int size) {
        SnmpVarBind[] result = new SnmpVarBind[size];
        for (int i = size - 1; i >= 0; --i) {
            result[i] = new SnmpVarBind();
            result[i].value = SnmpVarBind.endOfMibView;
        }
        for (Enumeration e = subs.elements(); e.hasMoreElements(); ) {
            SnmpSubRequestHandler sub = (SnmpSubRequestHandler) e.nextElement();
            sub.updateResult(result);
        }
        return result;
    }

    protected boolean isTraceOn() {
        return Trace.isSelected(Trace.LEVEL_TRACE, Trace.INFO_ADAPTOR_SNMP);
    }

    protected void trace(String clz, String func, String info) {
        Trace.send(Trace.LEVEL_TRACE, Trace.INFO_ADAPTOR_SNMP, clz, func, info);
    }

    protected void trace(String func, String info) {
        trace(dbgTag, func, info);
    }

    protected boolean isDebugOn() {
        return Trace.isSelected(Trace.LEVEL_DEBUG, Trace.INFO_ADAPTOR_SNMP);
    }

    protected void debug(String clz, String func, String info) {
        Trace.send(Trace.LEVEL_DEBUG, Trace.INFO_ADAPTOR_SNMP, clz, func, info);
    }

    protected void debug(String clz, String func, Throwable exception) {
        Trace.send(Trace.LEVEL_DEBUG, Trace.INFO_ADAPTOR_SNMP, clz, func, exception);
    }

    protected void debug(String func, String info) {
        debug(dbgTag, func, info);
    }

    protected void debug(String func, Throwable exception) {
        debug(dbgTag, func, exception);
    }

    protected String makeDebugTag() {
        return "SnmpRequestHandler[" + adaptorServer.getProtocol() + ":" + adaptorServer.getPort() + "]";
    }

    Thread createThread(Runnable r) {
        return null;
    }

    private static final String InterruptSysCallMsg = "Interrupted system call";

    private static final SnmpStatusException noSuchNameException = new SnmpStatusException(SnmpDefinitions.snmpRspNoSuchName);
}
