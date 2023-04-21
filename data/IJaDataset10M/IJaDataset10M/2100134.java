package jcifs.smb;

import java.io.*;
import java.net.*;
import java.util.*;
import jcifs.*;
import jcifs.netbios.*;
import jcifs.util.*;
import jcifs.util.transport.*;

public class SmbTransport extends Transport implements SmbConstants {

    static final byte[] BUF = new byte[0xFFFF];

    static final SmbComNegotiate NEGOTIATE_REQUEST = new SmbComNegotiate();

    static LogStream log = LogStream.getInstance();

    static synchronized SmbTransport getSmbTransport(UniAddress address, int port) {
        return getSmbTransport(address, port, LADDR, LPORT);
    }

    static synchronized SmbTransport getSmbTransport(UniAddress address, int port, InetAddress localAddr, int localPort) {
        SmbTransport conn;
        synchronized (CONNECTIONS) {
            if (SSN_LIMIT != 1) {
                ListIterator iter = CONNECTIONS.listIterator();
                while (iter.hasNext()) {
                    conn = (SmbTransport) iter.next();
                    if (conn.matches(address, port, localAddr, localPort) && (SSN_LIMIT == 0 || conn.sessions.size() < SSN_LIMIT)) {
                        return conn;
                    }
                }
            }
            conn = new SmbTransport(address, port, localAddr, localPort);
            CONNECTIONS.add(0, conn);
        }
        return conn;
    }

    class ServerData {

        byte flags;

        int flags2;

        int maxMpxCount;

        int maxBufferSize;

        int sessionKey;

        int capabilities;

        String oemDomainName;

        int securityMode;

        int security;

        boolean encryptedPasswords;

        boolean signaturesEnabled;

        boolean signaturesRequired;

        int maxNumberVcs;

        int maxRawSize;

        long serverTime;

        int serverTimeZone;

        int encryptionKeyLength;

        byte[] encryptionKey;
    }

    InetAddress localAddr;

    int localPort;

    UniAddress address;

    Socket socket;

    int port, mid;

    OutputStream out;

    InputStream in;

    byte[] sbuf = new byte[255];

    SmbComBlankResponse key = new SmbComBlankResponse();

    long sessionExpiration = System.currentTimeMillis() + SO_TIMEOUT;

    LinkedList referrals = new LinkedList();

    SigningDigest digest = null;

    LinkedList sessions = new LinkedList();

    ServerData server = new ServerData();

    int flags2 = FLAGS2;

    int maxMpxCount = MAX_MPX_COUNT;

    int snd_buf_size = SND_BUF_SIZE;

    int rcv_buf_size = RCV_BUF_SIZE;

    int capabilities = CAPABILITIES;

    int sessionKey = 0x00000000;

    boolean useUnicode = USE_UNICODE;

    String tconHostName;

    SmbTransport(UniAddress address, int port, InetAddress localAddr, int localPort) {
        this.address = address;
        this.port = port;
        this.localAddr = localAddr;
        this.localPort = localPort;
    }

    synchronized SmbSession getSmbSession() {
        return getSmbSession(new NtlmPasswordAuthentication(null, null, null));
    }

    synchronized SmbSession getSmbSession(NtlmPasswordAuthentication auth) {
        SmbSession ssn;
        long now;
        ListIterator iter = sessions.listIterator();
        while (iter.hasNext()) {
            ssn = (SmbSession) iter.next();
            if (ssn.matches(auth)) {
                ssn.auth = auth;
                return ssn;
            }
        }
        if (SO_TIMEOUT > 0 && sessionExpiration < (now = System.currentTimeMillis())) {
            sessionExpiration = now + SO_TIMEOUT;
            iter = sessions.listIterator();
            while (iter.hasNext()) {
                ssn = (SmbSession) iter.next();
                if (ssn.expiration < now) {
                    ssn.logoff(false);
                }
            }
        }
        ssn = new SmbSession(address, port, localAddr, localPort, auth);
        ssn.transport = this;
        sessions.add(ssn);
        return ssn;
    }

    boolean matches(UniAddress address, int port, InetAddress localAddr, int localPort) {
        return address.equals(this.address) && (port == 0 || port == this.port || (port == 445 && this.port == 139)) && (localAddr == this.localAddr || (localAddr != null && localAddr.equals(this.localAddr))) && localPort == this.localPort;
    }

    boolean hasCapability(int cap) throws SmbException {
        try {
            connect(RESPONSE_TIMEOUT);
        } catch (IOException ioe) {
            throw new SmbException("", ioe);
        }
        return (capabilities & cap) == cap;
    }

    boolean isSignatureSetupRequired(NtlmPasswordAuthentication auth) {
        return (flags2 & ServerMessageBlock.FLAGS2_SECURITY_SIGNATURES) != 0 && digest == null && auth != NtlmPasswordAuthentication.NULL && NtlmPasswordAuthentication.NULL.equals(auth) == false;
    }

    void ssn139() throws IOException {
        Name calledName = new Name(address.firstCalledName(), 0x20, null);
        do {
            if (localAddr == null) {
                socket = new Socket(address.getHostAddress(), 139);
            } else {
                socket = new Socket(address.getHostAddress(), 139, localAddr, localPort);
            }
            socket.setSoTimeout(SO_TIMEOUT);
            out = socket.getOutputStream();
            in = socket.getInputStream();
            SessionServicePacket ssp = new SessionRequestPacket(calledName, NbtAddress.getLocalName());
            out.write(sbuf, 0, ssp.writeWireFormat(sbuf, 0));
            if (readn(in, sbuf, 0, 4) < 4) {
                throw new SmbException("EOF during NetBIOS session request");
            }
            switch(sbuf[0] & 0xFF) {
                case SessionServicePacket.POSITIVE_SESSION_RESPONSE:
                    if (log.level > 2) log.println("session established ok with " + address);
                    return;
                case SessionServicePacket.NEGATIVE_SESSION_RESPONSE:
                    int errorCode = (int) (in.read() & 0xFF);
                    switch(errorCode) {
                        case NbtException.CALLED_NOT_PRESENT:
                        case NbtException.NOT_LISTENING_CALLED:
                            socket.close();
                            break;
                        default:
                            disconnect(true);
                            throw new NbtException(NbtException.ERR_SSN_SRVC, errorCode);
                    }
                    break;
                case -1:
                    disconnect(true);
                    throw new NbtException(NbtException.ERR_SSN_SRVC, NbtException.CONNECTION_REFUSED);
                default:
                    disconnect(true);
                    throw new NbtException(NbtException.ERR_SSN_SRVC, 0);
            }
        } while ((calledName.name = address.nextCalledName()) != null);
        throw new IOException("Failed to establish session with " + address);
    }

    private void negotiate(int port, ServerMessageBlock resp) throws IOException {
        synchronized (sbuf) {
            if (NETBIOS_HOSTNAME != null && NETBIOS_HOSTNAME.equals("") == false) {
                port = 139;
            }
            if (port == 139) {
                ssn139();
            } else {
                if (port == 0) port = DEFAULT_PORT;
                if (localAddr == null) {
                    socket = new Socket(address.getHostAddress(), port);
                } else {
                    socket = new Socket(address.getHostAddress(), port, localAddr, localPort);
                }
                socket.setSoTimeout(SO_TIMEOUT);
                out = socket.getOutputStream();
                in = socket.getInputStream();
            }
            if (++mid == 32000) mid = 1;
            NEGOTIATE_REQUEST.mid = mid;
            int n = NEGOTIATE_REQUEST.encode(sbuf, 4);
            Encdec.enc_uint32be(n & 0xFFFF, sbuf, 0);
            out.write(sbuf, 0, 4 + n);
            out.flush();
            if (peekKey() == null) throw new IOException("transport closed in negotiate");
            int size = Encdec.dec_uint16be(sbuf, 2);
            if (size < 33 || (4 + size) > sbuf.length) {
                throw new IOException("Invalid payload size: " + size);
            }
            readn(in, sbuf, 4 + 32, size - 32);
            resp.decode(sbuf, 4);
        }
    }

    public void connect() throws SmbException {
        try {
            super.connect(RESPONSE_TIMEOUT);
        } catch (TransportException te) {
            throw new SmbException("", te);
        }
    }

    protected void doConnect() throws IOException {
        SmbComNegotiateResponse resp = new SmbComNegotiateResponse(server);
        try {
            negotiate(port, resp);
        } catch (ConnectException ce) {
            port = (port == 0 || port == DEFAULT_PORT) ? 139 : DEFAULT_PORT;
            negotiate(port, resp);
        }
        if (resp.dialectIndex > 10) {
            throw new SmbException("This client does not support the negotiated dialect.");
        }
        tconHostName = address.getHostName();
        if (server.signaturesRequired || (server.signaturesEnabled && SIGNPREF)) {
            flags2 |= ServerMessageBlock.FLAGS2_SECURITY_SIGNATURES;
        } else {
            flags2 &= 0xFFFF ^ ServerMessageBlock.FLAGS2_SECURITY_SIGNATURES;
        }
        maxMpxCount = Math.min(maxMpxCount, server.maxMpxCount);
        if (maxMpxCount < 1) maxMpxCount = 1;
        snd_buf_size = Math.min(snd_buf_size, server.maxBufferSize);
        capabilities &= server.capabilities;
        if ((capabilities & ServerMessageBlock.CAP_UNICODE) == 0) {
            if (FORCE_UNICODE) {
                capabilities |= ServerMessageBlock.CAP_UNICODE;
            } else {
                useUnicode = false;
                flags2 &= 0xFFFF ^ ServerMessageBlock.FLAGS2_UNICODE;
            }
        }
    }

    protected void doDisconnect(boolean hard) throws IOException {
        ListIterator iter = sessions.listIterator();
        while (iter.hasNext()) {
            SmbSession ssn = (SmbSession) iter.next();
            ssn.logoff(hard);
        }
        socket.shutdownOutput();
        out.close();
        in.close();
        socket.close();
        digest = null;
    }

    protected void makeKey(Request request) throws IOException {
        if (++mid == 32000) mid = 1;
        ((ServerMessageBlock) request).mid = mid;
    }

    protected Request peekKey() throws IOException {
        int n;
        do {
            if ((n = readn(in, sbuf, 0, 4)) < 4) return null;
        } while (sbuf[0] == (byte) 0x85);
        if ((n = readn(in, sbuf, 4, 32)) < 32) return null;
        if (log.level > 2) {
            log.println("New data read: " + this);
            jcifs.util.Hexdump.hexdump(log, sbuf, 4, 32);
        }
        for (; ; ) {
            if (sbuf[0] == (byte) 0x00 && sbuf[1] == (byte) 0x00 && sbuf[4] == (byte) 0xFF && sbuf[5] == (byte) 'S' && sbuf[6] == (byte) 'M' && sbuf[7] == (byte) 'B') {
                break;
            }
            for (int i = 0; i < 35; i++) {
                sbuf[i] = sbuf[i + 1];
            }
            int b;
            if ((b = in.read()) == -1) return null;
            sbuf[35] = (byte) b;
        }
        key.mid = Encdec.dec_uint16le(sbuf, 34);
        return key;
    }

    protected void doSend(Request request) throws IOException {
        synchronized (BUF) {
            ServerMessageBlock smb = (ServerMessageBlock) request;
            int n = smb.encode(BUF, 4);
            Encdec.enc_uint32be(n & 0xFFFF, BUF, 0);
            if (log.level > 3) {
                do {
                    log.println(smb);
                } while (smb instanceof AndXServerMessageBlock && (smb = ((AndXServerMessageBlock) smb).andx) != null);
                if (log.level > 5) {
                    Hexdump.hexdump(log, BUF, 4, n);
                }
            }
            out.write(BUF, 0, 4 + n);
        }
    }

    protected void doSend0(Request request) throws IOException {
        try {
            doSend(request);
        } catch (IOException ioe) {
            if (log.level > 2) ioe.printStackTrace(log);
            try {
                disconnect(true);
            } catch (IOException ioe2) {
                ioe2.printStackTrace(log);
            }
            throw ioe;
        }
    }

    protected void doRecv(Response response) throws IOException {
        ServerMessageBlock resp = (ServerMessageBlock) response;
        resp.useUnicode = useUnicode;
        synchronized (BUF) {
            System.arraycopy(sbuf, 0, BUF, 0, 4 + HEADER_LENGTH);
            int size = Encdec.dec_uint16be(BUF, 2);
            if (size < (HEADER_LENGTH + 1) || (4 + size) > rcv_buf_size) {
                throw new IOException("Invalid payload size: " + size);
            }
            if (resp.command == ServerMessageBlock.SMB_COM_READ_ANDX) {
                SmbComReadAndXResponse r = (SmbComReadAndXResponse) resp;
                int off = HEADER_LENGTH;
                readn(in, BUF, 4 + off, 27);
                off += 27;
                resp.decode(BUF, 4);
                if (r.dataLength > 0) {
                    readn(in, BUF, 4 + off, r.dataOffset - off);
                    readn(in, r.b, r.off, r.dataLength);
                }
            } else {
                readn(in, BUF, 4 + 32, size - 32);
                resp.decode(BUF, 4);
                if (resp instanceof SmbComTransactionResponse) {
                    ((SmbComTransactionResponse) resp).nextElement();
                }
            }
            if (digest != null && resp.errorCode == 0) {
                digest.verify(BUF, 4, resp);
            }
        }
    }

    protected void doSkip() throws IOException {
        int size = Encdec.dec_uint16be(sbuf, 2);
        if (size < 33 || (4 + size) > rcv_buf_size) {
            in.skip(in.available());
        } else {
            in.skip(size - 32);
        }
    }

    void checkStatus(ServerMessageBlock req, ServerMessageBlock resp) throws SmbException {
        resp.errorCode = SmbException.getStatusByCode(resp.errorCode);
        switch(resp.errorCode) {
            case NtStatus.NT_STATUS_OK:
                break;
            case NtStatus.NT_STATUS_ACCESS_DENIED:
            case NtStatus.NT_STATUS_WRONG_PASSWORD:
            case NtStatus.NT_STATUS_LOGON_FAILURE:
            case NtStatus.NT_STATUS_ACCOUNT_RESTRICTION:
            case NtStatus.NT_STATUS_INVALID_LOGON_HOURS:
            case NtStatus.NT_STATUS_INVALID_WORKSTATION:
            case NtStatus.NT_STATUS_PASSWORD_EXPIRED:
            case NtStatus.NT_STATUS_ACCOUNT_DISABLED:
            case NtStatus.NT_STATUS_ACCOUNT_LOCKED_OUT:
            case NtStatus.NT_STATUS_TRUSTED_DOMAIN_FAILURE:
                throw new SmbAuthException(resp.errorCode);
            case NtStatus.NT_STATUS_PATH_NOT_COVERED:
                if (req.auth == null) {
                    throw new SmbException(resp.errorCode, null);
                }
                DfsReferral dr = getDfsReferral(req.auth, req.path);
                referrals.add(dr);
                throw dr;
            case 0x80000005:
                break;
            default:
                throw new SmbException(resp.errorCode, null);
        }
        if (resp.verifyFailed) {
            throw new SmbException("Signature verification failed.");
        }
    }

    void send(ServerMessageBlock request, ServerMessageBlock response) throws SmbException {
        connect();
        request.flags2 |= flags2;
        request.useUnicode = useUnicode;
        request.response = response;
        if (request.digest == null) request.digest = digest;
        try {
            if (response == null) {
                doSend0(request);
                return;
            } else if (request instanceof SmbComTransaction) {
                response.command = request.command;
                SmbComTransaction req = (SmbComTransaction) request;
                SmbComTransactionResponse resp = (SmbComTransactionResponse) response;
                req.maxBufferSize = snd_buf_size;
                resp.reset();
                try {
                    BufferCache.getBuffers(req, resp);
                    req.nextElement();
                    if (req.hasMoreElements()) {
                        SmbComBlankResponse interim = new SmbComBlankResponse();
                        super.sendrecv(req, interim, RESPONSE_TIMEOUT);
                        if (interim.errorCode != 0) {
                            checkStatus(req, interim);
                        }
                        req.nextElement();
                    } else {
                        makeKey(req);
                    }
                    synchronized (response_map) {
                        response.received = false;
                        resp.isReceived = false;
                        try {
                            response_map.put(req, resp);
                            do {
                                doSend0(req);
                            } while (req.hasMoreElements() && req.nextElement() != null);
                            long timeout = RESPONSE_TIMEOUT;
                            resp.expiration = System.currentTimeMillis() + timeout;
                            while (resp.hasMoreElements()) {
                                response_map.wait(timeout);
                                timeout = resp.expiration - System.currentTimeMillis();
                                if (timeout <= 0) {
                                    throw new TransportException(this + " timedout waiting for response to " + req);
                                }
                            }
                            if (response.errorCode != 0) {
                                checkStatus(req, resp);
                            }
                        } catch (InterruptedException ie) {
                            throw new TransportException(ie);
                        } finally {
                            response_map.remove(req);
                        }
                    }
                } finally {
                    BufferCache.releaseBuffer(req.txn_buf);
                    BufferCache.releaseBuffer(resp.txn_buf);
                }
            } else {
                response.command = request.command;
                super.sendrecv(request, response, RESPONSE_TIMEOUT);
            }
        } catch (SmbException se) {
            throw se;
        } catch (IOException ioe) {
            throw new SmbException("", ioe);
        }
        checkStatus(request, response);
    }

    public String toString() {
        return super.toString() + "[" + address + ":" + port + "]";
    }

    DfsReferral getDfsReferral(NtlmPasswordAuthentication auth, String path) throws SmbException {
        String subpath, node, host;
        DfsReferral dr = new DfsReferral();
        int p, n, i, s;
        UniAddress addr;
        SmbTree ipc = getSmbSession(auth).getSmbTree("IPC$", null);
        Trans2GetDfsReferralResponse resp = new Trans2GetDfsReferralResponse();
        ipc.send(new Trans2GetDfsReferral(path), resp);
        subpath = path.substring(0, resp.pathConsumed);
        node = resp.referral.node;
        if (subpath.charAt(0) != '\\' || (i = subpath.indexOf('\\', 1)) < 2 || (p = subpath.indexOf('\\', i + 1)) < (i + 2) || node.charAt(0) != '\\' || (s = node.indexOf('\\', 1)) < 2) {
            throw new SmbException("Invalid DFS path: " + path);
        }
        if ((n = node.indexOf('\\', s + 1)) == -1) {
            n = node.length();
        }
        dr.path = subpath.substring(p);
        dr.node = node.substring(0, n);
        dr.nodepath = node.substring(n);
        dr.server = node.substring(1, s);
        dr.share = node.substring(s + 1, n);
        dr.resolveHashes = auth.hashesExternal;
        return dr;
    }

    DfsReferral lookupReferral(String unc) {
        synchronized (referrals) {
            DfsReferral dr;
            ListIterator iter = referrals.listIterator();
            int i, len;
            while (iter.hasNext()) {
                dr = (DfsReferral) iter.next();
                len = dr.path.length();
                for (i = 0; i < len && i < unc.length(); i++) {
                    if (dr.path.charAt(i) != unc.charAt(i)) {
                        break;
                    }
                }
                if (i == len && (len == unc.length() || unc.charAt(len) == '\\')) {
                    return dr;
                }
            }
        }
        return null;
    }
}
