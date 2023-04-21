package org.mobicents.slee.util;

import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipStack;
import javax.sip.TransactionUnavailableException;
import javax.sip.TransportNotSupportedException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentLengthHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ProxyAuthenticateHeader;
import javax.sip.header.ProxyAuthorizationHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import net.java.slee.resource.sip.SleeSipProvider;
import org.apache.log4j.Logger;

public class SipUtilsImpl implements SipUtils {

    private static Logger log = Logger.getLogger(SipUtilsImpl.class);

    private SleeSipProvider sipProvider;

    private HeaderFactory headerFactory;

    private MessageFactory messageFactory;

    private AddressFactory addressFactory;

    public SipUtilsImpl(SleeSipProvider sipProvider, HeaderFactory headerFactory, MessageFactory messageFactory, AddressFactory addressFactory) {
        this.sipProvider = sipProvider;
        this.headerFactory = headerFactory;
        this.messageFactory = messageFactory;
        this.addressFactory = addressFactory;
    }

    public Dialog getDialog(ResponseEvent event) throws SipException {
        Dialog retVal = null;
        if (event.getDialog() == null) {
            log.error("responseEvent.getDialog returned null, if AUTOMATIC_DIALOG_SUPPORT is disabled " + "you must obtain a dialog before the first sip response arrives: " + event.getClientTransaction().getDialog());
            throw new SipException("responseEvent.getDialog returned null, if AUTOMATIC_DIALOG_SUPPORT is disabled " + "you must obtain a dialog before the first sip response arrives");
        } else {
            retVal = event.getDialog();
            log.debug("Returning dialog in getDialog(ResponseEvent) obtained directly from ResponseEvent");
        }
        return retVal;
    }

    public Dialog getDialog(RequestEvent event) throws SipException {
        Dialog retVal = null;
        try {
            if (event.getDialog() == null) {
                retVal = sipProvider.getNewDialog(event.getServerTransaction());
            } else {
                retVal = event.getDialog();
            }
        } catch (SipException ex) {
            log.error("Exception in creating a new dialog in getDialog(RequestEvent)", ex);
            throw ex;
        }
        return retVal;
    }

    /**
	 * Hex characters
	 */
    private final char[] toHex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public String generateTag() {
        String tag = new Integer((int) (Math.random() * 10000)).toString();
        return tag;
    }

    /**
	 * Convert an array of bytes to an hexadecimal string
	 * 
	 * @param b
	 *            The byte array to convert to a hexadecimal string
	 * @return The hexadecimal string representation of the byte array
	 * 
	 */
    private String toHexString(byte b[]) {
        int pos = 0;
        char[] c = new char[b.length * 2];
        for (int i = 0; i < b.length; i++) {
            c[pos++] = toHex[(b[i] >> 4) & 0x0F];
            c[pos++] = toHex[b[i] & 0x0f];
        }
        return new String(c);
    }

    private AddressFactory getAddressFactory() {
        AddressFactory addressFactory = null;
        try {
            Context myEnv = (Context) new InitialContext().lookup("java:comp/env");
            SleeSipProvider factoryProvider = (SleeSipProvider) myEnv.lookup("slee/resources/jainsip/1.2/provider");
            addressFactory = factoryProvider.getAddressFactory();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return addressFactory;
    }

    public Address convertURIToAddress(String uri) {
        SipURI sipURI = convertURIToSipURI(uri);
        return getAddressFactory().createAddress(sipURI);
    }

    public SipURI convertURIToSipURI(String uri) {
        String[] sipUserAndHost = null;
        SipURI sipURI = null;
        try {
            sipUserAndHost = parseSipUri(uri);
            final String sipUser = sipUserAndHost[0];
            final String sipHost = sipUserAndHost[1];
            sipURI = getAddressFactory().createSipURI(sipUser, sipHost);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sipURI;
    }

    public String[] parseSipUri(String sipURI) throws URISyntaxException {
        String[] userAndHost = new String[2];
        final String sipPrexfix = "sip:";
        final String uriSeparator = "@";
        final int sipPrefixIndex = sipURI.indexOf(sipPrexfix) + 3;
        final int uriIndex = sipURI.indexOf(uriSeparator);
        final int nameIndex = sipPrefixIndex + 1;
        final int hostIndex = uriIndex + 1;
        if (sipURI.indexOf(sipPrexfix) == -1 || sipURI.indexOf(uriSeparator) == -1 || sipPrefixIndex > uriIndex) {
            throw new URISyntaxException(sipURI, "Malformed URI, the URI must use the format \"sip:user@host\". The incorrect URI was \"" + sipURI + "\".");
        }
        userAndHost[0] = sipURI.substring(nameIndex, uriIndex);
        userAndHost[1] = sipURI.substring(hostIndex);
        return userAndHost;
    }

    public SipURI convertAddressToSipURI(Address address) throws ParseException {
        URI sipURI = address.getURI();
        SipURI retVal = null;
        if (sipURI.isSipURI()) retVal = (SipURI) sipURI; else {
            throw new ParseException("URI was not of type SipURI!", -1);
        }
        return retVal;
    }

    public void sendCancel(ClientTransaction ct) throws SipException {
        Request request = ct.createCancel();
        ct = sipProvider.getNewClientTransaction(request);
        ct.sendRequest();
    }

    public Request buildInvite(Address fromAddress, Address toAddress, byte[] content, int cSeq) throws ParseException, InvalidArgumentException {
        return buildInvite(fromAddress, toAddress, content, cSeq, null);
    }

    public Request buildInvite(Address fromAddress, Address toAddress, byte[] content, int cSeq, String callId) throws ParseException, InvalidArgumentException {
        FromHeader fromHeader = headerFactory.createFromHeader(fromAddress, generateTag());
        ToHeader toHeader = headerFactory.createToHeader(toAddress, null);
        Request request = null;
        CSeqHeader cseqHeader = headerFactory.createCSeqHeader(cSeq, Request.INVITE);
        ArrayList viaHeadersList = new ArrayList();
        viaHeadersList.add(createLocalViaHeader());
        MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);
        final SipURI requestURI = convertAddressToSipURI(toAddress);
        SipURI fromUri = convertAddressToSipURI(fromAddress);
        CallIdHeader callIdHeader = sipProvider.getNewCallId();
        if ((callId != null) && (callId.trim().length() > 0)) {
            callIdHeader.setCallId(callId);
        }
        request = messageFactory.createRequest(requestURI, Request.INVITE, callIdHeader, cseqHeader, fromHeader, toHeader, viaHeadersList, maxForwardsHeader);
        if (content != null) {
            setContent(request, "application", "sdp", content);
        }
        ContactHeader contactHeader = createLocalContactHeader(fromUri.getUser());
        request.setHeader(contactHeader);
        if (log.isDebugEnabled()) {
            log.debug("Contact Header = " + contactHeader);
        }
        return request;
    }

    public ContactHeader createLocalContactHeader(String user) throws ParseException {
        final String transport = sipProvider.getListeningPoints()[0].getTransport();
        SipURI sipURI = sipProvider.getLocalSipURI(transport);
        sipURI.setUser(user);
        sipURI.setTransportParam(transport);
        Address contactAddress = addressFactory.createAddress(sipURI);
        ContactHeader contactHeader = headerFactory.createContactHeader(contactAddress);
        return contactHeader;
    }

    public ViaHeader createLocalViaHeader() throws ParseException, InvalidArgumentException {
        final String transport = sipProvider.getListeningPoints()[0].getTransport();
        ViaHeader viaHeader = null;
        try {
            viaHeader = this.sipProvider.getLocalVia(transport, null);
        } catch (TransportNotSupportedException e) {
            e.printStackTrace();
        }
        return viaHeader;
    }

    /**
	 * Sets a content for a request
	 * 
	 * @param request
	 *            The request to set the content for
	 * @param contentType
	 *            the new string content type value.
	 * @param contentSubType
	 *            the new string content sub-type value.
	 * @param content
	 *            The content to set
	 * @throws ParseException
	 * @throws InvalidArgumentException
	 */
    private void setContent(Request request, String contentType, String contentSubType, byte[] content) throws ParseException, InvalidArgumentException {
        ContentTypeHeader contentTypeHeader = headerFactory.createContentTypeHeader(contentType, contentSubType);
        request.setContent(content, contentTypeHeader);
        ContentLengthHeader contentLengthHeader = headerFactory.createContentLengthHeader(content.length);
        request.setContentLength(contentLengthHeader);
    }

    public Request buildRequestWithAuthorizationHeader(ResponseEvent event, String password) throws TransactionUnavailableException {
        Request request = event.getClientTransaction().getRequest();
        Response response = event.getResponse();
        if (request == null) {
            if (log.isDebugEnabled()) {
                log.debug("The request that caused the 407 could not be retrieved.");
            }
            return null;
        } else {
            CSeqHeader cseqHeader = (CSeqHeader) request.getHeader(CSeqHeader.NAME);
            FromHeader fromHeaderReq = (FromHeader) request.getHeader(FromHeader.NAME);
            Address fromAddressReq = fromHeaderReq.getAddress();
            ToHeader toHeader = (ToHeader) request.getHeader(ToHeader.NAME);
            Address toAddress = toHeader.getAddress();
            Request newRequest = null;
            String callId = ((CallIdHeader) response.getHeader(CallIdHeader.NAME)).getCallId();
            try {
                newRequest = buildInvite(fromAddressReq, toAddress, null, cseqHeader.getSequenceNumber() + 1, callId);
            } catch (ParseException parseExc) {
                parseExc.printStackTrace();
            } catch (InvalidArgumentException invaliArgExc) {
                invaliArgExc.printStackTrace();
            }
            WWWAuthenticateHeader wwwAuthenticateHeader = (WWWAuthenticateHeader) response.getHeader(WWWAuthenticateHeader.NAME);
            ProxyAuthenticateHeader proxyAuthenticateHeader = (ProxyAuthenticateHeader) response.getHeader(ProxyAuthenticateHeader.NAME);
            String realm = null;
            String nonce = null;
            if (wwwAuthenticateHeader != null) {
                if (log.isDebugEnabled()) {
                    log.debug("wwwAuthenticateHeader found!");
                }
                realm = wwwAuthenticateHeader.getRealm();
                nonce = wwwAuthenticateHeader.getNonce();
            } else if (proxyAuthenticateHeader != null) {
                if (log.isDebugEnabled()) {
                    log.debug("ProxyAuthenticateHeader found!");
                }
                realm = proxyAuthenticateHeader.getRealm();
                nonce = proxyAuthenticateHeader.getNonce();
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Neither a ProxyAuthenticateHeader or AuthorizationHeader found!");
                }
                return null;
            }
            final String method = cseqHeader.getMethod();
            final FromHeader fromHeader = ((FromHeader) response.getHeader(FromHeader.NAME));
            Address address = fromHeader.getAddress();
            String fromHost = null;
            String fromUser = null;
            int fromPort = 0;
            String toHost = null;
            String toUser = null;
            int toPort = 0;
            SipURI fromSipURI = null;
            SipURI toSipURI = null;
            try {
                fromSipURI = convertAddressToSipURI(address);
                toSipURI = convertAddressToSipURI(toAddress);
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
            fromHost = fromSipURI.getHost();
            fromUser = fromSipURI.getUser();
            fromPort = fromSipURI.getPort();
            toHost = toSipURI.getHost();
            toUser = toSipURI.getUser();
            toPort = toSipURI.getPort();
            if (fromPort != -1) {
                fromHost += ":" + fromPort;
            }
            SipURI uri = null;
            try {
                uri = addressFactory.createSipURI(toUser, toHost);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            String A1 = fromUser + ":" + realm + ":" + password;
            String A2 = method.toUpperCase() + ":" + uri.toString();
            byte mdbytes[] = md5.digest(A1.getBytes());
            String HA1 = toHexString(mdbytes);
            mdbytes = md5.digest(A2.getBytes());
            String HA2 = toHexString(mdbytes);
            String KD = HA1 + ":" + nonce + ":" + HA2;
            mdbytes = md5.digest(KD.getBytes());
            if (wwwAuthenticateHeader != null) {
                AuthorizationHeader ah = null;
                try {
                    ah = headerFactory.createAuthorizationHeader("Digest");
                    ah.setUsername(fromUser);
                    ah.setRealm(realm);
                    ah.setAlgorithm("MD5");
                    ah.setURI(uri);
                    ah.setNonce(nonce);
                    ah.setResponse(toHexString(mdbytes));
                    newRequest.setHeader(ah);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                ProxyAuthorizationHeader pah = null;
                try {
                    pah = headerFactory.createProxyAuthorizationHeader("Digest");
                    pah.setUsername(fromUser);
                    pah.setRealm(realm);
                    pah.setAlgorithm("MD5");
                    pah.setURI(uri);
                    pah.setNonce(nonce);
                    pah.setResponse(toHexString(mdbytes));
                    newRequest.setHeader(pah);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            log.debug("********* New Request *******************");
            log.debug(newRequest);
            return newRequest;
        }
    }

    public Request buildAck(Dialog dialog, Object content) throws SipException {
        Request ackRequest = null;
        try {
            ackRequest = dialog.createAck(dialog.getLocalSeqNumber());
        } catch (InvalidArgumentException invalidArgExc) {
            invalidArgExc.printStackTrace();
        }
        if (content != null) {
            String contentString = null;
            if (content instanceof byte[]) {
                contentString = new String((byte[]) content);
            } else if (content instanceof String) {
                contentString = (String) content;
            }
            if (log.isDebugEnabled()) {
                log.debug("sendCalleeAck Content = " + contentString);
            }
            final byte[] sdpContent = contentString.getBytes();
            try {
                setContent(ackRequest, "application", "sdp", sdpContent);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (InvalidArgumentException e) {
                e.printStackTrace();
            }
        }
        return ackRequest;
    }

    public void sendOk(Request request) throws ParseException, SipException {
        Response okResponse = messageFactory.createResponse(Response.OK, request);
        sipProvider.sendResponse(okResponse);
    }

    public void sendStatefulOk(RequestEvent event) throws ParseException, SipException, InvalidArgumentException {
        ServerTransaction tx = event.getServerTransaction();
        Request request = event.getRequest();
        Response response = messageFactory.createResponse(Response.OK, request);
        tx.sendResponse(response);
    }
}
