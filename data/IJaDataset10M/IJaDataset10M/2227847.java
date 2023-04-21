package com.sun.ebxml.registry.interfaces.soap;

import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.util.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.ServletException;
import javax.xml.messaging.URLEndpoint;
import javax.xml.parsers.*;
import javax.xml.soap.*;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.xml.sax.SAXException;
import com.sun.ebxml.registry.RegistryException;
import com.sun.ebxml.registry.security.SecurityUtil;
import com.sun.ebxml.registry.security.authentication.AuthenticationServiceImpl;
import com.sun.ebxml.registry.util.Utility;

/**
 * Sends a SOAP 1.1 message with attachments to an ebXML Registry <br>
 *
 * It takes these command line parameters:<br>
 *
 * req - The file containing the ebXML registry request <br>
 *
 * alias - Optional. If it is provided the request will be signed with the
 * private key of the alias in the keystore<br>
 *
 * keyStore - Optional. The keystore is used for signing. If it is not provided
 * but the alias is provided, the keystore specified by
 * ebxmlrr.security.keystoreFile properties in ebxmlrr.properties is used for
 * signing. The supported keystore types are pkcs12 and jks <br>
 *
 * keyPassword - The key password for accessing the private key<br>
 *
 * keyStoreType - The type of the keystore. It may be pkcs12 or jks <br>
 *
 * keyStorePassword - The password for accessing the keystore <br>
 *
 * url - The URL of the ebXML registry server<br>
 *
 * attach - comma-delimited list of attached file, MIME type of the file and the UUID (either
 * temporary or real) of corresponding ExtrinsicObject in the SubmitObjectsRequest. More than
 * one attach parameter can be added<br>
 *
 * res - If this parameter exists, the response from registry will be saved to the file
 * of path specified by this parameter<br>
 *
 * @see
 * @author Farrukh S. Najmi
 */
public class SOAPSender {

    private static AuthenticationServiceImpl authc = AuthenticationServiceImpl.getInstance();

    private static final String defaultRequestFileName = "c:/osws/ebxmlrr-spec/misc/samples/SubmitObjectsRequest_Sun.xml";

    String reqFileName = null;

    private String registryURL = null;

    private static final String defaultAlias = null;

    private String alias = defaultAlias;

    private static final boolean defaultLocalCall = true;

    private static boolean localCall = defaultLocalCall;

    private static boolean requestIsAScheme = false;

    private static boolean debug = false;

    private SOAPMessage msg = null;

    private HashMap attachmentMap = new HashMap();

    private static ArrayList attachments = new ArrayList();

    private KeyStore keyStore;

    private String keyPassword;

    private String signingAlgo;

    private java.security.cert.Certificate[] certs;

    private java.security.PrivateKey privateKey;

    public void setKeyStore(String keyStoreFile, String keyStoreType, String keyStorePassword, String keyPassword) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(new FileInputStream(keyStoreFile), stringToCharArray(keyStorePassword));
    }

    private char[] stringToCharArray(String str) {
        char[] arr = null;
        if (str != null) {
            arr = str.toCharArray();
        }
        return arr;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    public void setRequestFileName(String reqFileName) {
        this.reqFileName = reqFileName;
    }

    public void setRegistryURL(String registryURL) {
        this.registryURL = registryURL;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setLocalCall(boolean localCall) {
        this.localCall = localCall;
    }

    private static void printUsage() {
        System.err.println("...SOAPSender [-help] req=<requestFile.xml>|scheme=<schemeFile.xml> keyStore=<KeyStore> " + "keyStoreType=<jks|pkcs12> keyStorePassword=<password> keyPassword=<password> alias=<aliasInKeyStore> url=<registryURL> " + "attach=<file>,mimeType,id localCall=<true|false> " + "res=<responseFile.xml>");
        System.exit(-1);
    }

    private static class AttachmentInfo {

        public AttachmentInfo(String fileName, String mimeType, String id) {
            this.id = id;
            this.fileName = fileName;
            this.mimeType = mimeType;
        }

        String id = null;

        String fileName = null;

        String mimeType = null;
    }

    public static void main(String[] args) {
        try {
            String reqFileName = defaultRequestFileName;
            String resFileName = null;
            String url = null;
            String alias = defaultAlias;
            boolean localCall = defaultLocalCall;
            String keyStoreFile = null;
            String keyStoreType = null;
            String keyStorePassword = null;
            String keyPassword = null;
            for (int i = 0; i < args.length; i++) {
                if (args[i].equalsIgnoreCase("-help")) {
                    printUsage();
                } else if (args[i].equalsIgnoreCase("-debug")) {
                    debug = true;
                } else if (args[i].startsWith("req=")) {
                    reqFileName = args[i].substring(4, args[i].length());
                } else if (args[i].startsWith("res=")) {
                    resFileName = args[i].substring(4, args[i].length());
                } else if (args[i].startsWith("scheme=")) {
                    reqFileName = args[i].substring(7, args[i].length());
                    requestIsAScheme = true;
                } else if (args[i].startsWith("alias=")) {
                    alias = args[i].substring(6, args[i].length());
                    if (alias.equalsIgnoreCase("RegistryOperator")) {
                        alias = authc.ALIAS_REGISTRY_OPERATOR;
                    } else if (alias.equalsIgnoreCase("RegistryGuest")) {
                        alias = authc.ALIAS_REGISTRY_GUEST;
                    } else if (alias.equalsIgnoreCase("Farrukh")) {
                        alias = authc.ALIAS_FARRUKH;
                    } else if (alias.equalsIgnoreCase("Nikola")) {
                        alias = authc.ALIAS_NIKOLA;
                    } else if (alias.equalsIgnoreCase("Adrian")) {
                        alias = authc.ALIAS_ADRIAN;
                    } else if (alias.equalsIgnoreCase("CY")) {
                        alias = authc.ALIAS_CY;
                    }
                } else if (args[i].startsWith("url=")) {
                    url = args[i].substring(4, args[i].length());
                } else if (args[i].startsWith("attach=")) {
                    StringTokenizer tokenizer = new StringTokenizer(args[i], "=,");
                    String attachFileName = null;
                    String mimeType = "text/plain";
                    String attachId = "id";
                    int j = 0;
                    while (tokenizer.hasMoreTokens()) {
                        String token = tokenizer.nextToken();
                        if (j == 1) {
                            attachFileName = token;
                        } else if (j == 2) {
                            mimeType = token;
                        }
                        if (j == 3) {
                            attachId = token;
                        }
                        j++;
                    }
                    AttachmentInfo ai = new AttachmentInfo(attachFileName, mimeType, attachId);
                    attachments.add(ai);
                } else if (args[i].startsWith("localCall=")) {
                    String localCallStr = args[i].substring(10, args[i].length());
                    if (localCallStr.equalsIgnoreCase("false")) {
                        localCall = false;
                    }
                } else if (args[i].startsWith("keyStore=")) {
                    keyStoreFile = args[i].substring(9, args[i].length());
                } else if (args[i].startsWith("keyStoreType=")) {
                    keyStoreType = args[i].substring(13, args[i].length());
                } else if (args[i].startsWith("keyStorePassword=")) {
                    keyStorePassword = args[i].substring(17, args[i].length());
                } else if (args[i].startsWith("keyPassword=")) {
                    keyPassword = args[i].substring(12, args[i].length());
                } else {
                    System.err.println("Unknown parameter: '" + args[i] + "' at position " + i);
                    if (i > 0) {
                        System.err.println("Last valid parameter was '" + args[i - 1] + "'");
                    }
                    printUsage();
                }
            }
            if (reqFileName == null) {
                System.err.println("'req' is mandatory!");
                printUsage();
            }
            if (url == null) {
                System.err.println("'url' is mandatory!");
                printUsage();
            }
            if (keyStoreFile != null) {
                if (keyStoreType == null) {
                    System.err.println("'keyStoreType' is mandatory for signing if keyStore parameter is provided!");
                    printUsage();
                }
                if (keyPassword == null) {
                    System.err.println("'keyPassword' is mandatory for signing if keyStore parameter is provided!");
                    printUsage();
                }
                if (keyStorePassword == null) {
                    System.err.println("'keyStorePassword' is mandatory for signing if keyStore parameter is provided!");
                    printUsage();
                }
            }
            SOAPSender sender = new SOAPSender();
            sender.setRequestFileName(reqFileName);
            sender.setRegistryURL(url);
            sender.setAlias(alias);
            sender.setLocalCall(localCall);
            if (keyStoreFile != null) {
                sender.setKeyStore(keyStoreFile, keyStoreType, keyStorePassword, keyPassword);
            }
            sender.setKeyPassword(keyPassword);
            SOAPMessage reply = sender.send();
            if (resFileName != null && reply != null) {
                FileOutputStream responseFileStream = new FileOutputStream(resFileName);
                reply.writeTo(responseFileStream);
            }
            SOAPPart sp = reply.getSOAPPart();
            SOAPEnvelope se = sp.getEnvelope();
            SOAPBody body = se.getBody();
            Iterator iter = body.getChildElements();
            Object obj = null;
            SOAPElement rootElem = null;
            while (iter.hasNext()) {
                obj = iter.next();
                if (obj instanceof SOAPElement) {
                    rootElem = (SOAPElement) obj;
                    break;
                }
            }
            if (rootElem != null) {
                String rootElemName = rootElem.getElementName().getLocalName();
                if (rootElemName.equalsIgnoreCase("RegistryResponse")) {
                    iter = rootElem.getChildElements();
                    obj = null;
                    SOAPElement childElem = null;
                    while (iter.hasNext()) {
                        obj = iter.next();
                        if (obj instanceof SOAPElement) {
                            childElem = (SOAPElement) obj;
                            break;
                        }
                    }
                    if (childElem != null) {
                        String childElemName = childElem.getElementName().getLocalName();
                        if (childElemName.equalsIgnoreCase("RegistryErrorList")) {
                            System.exit(-1);
                        }
                    }
                }
            }
            System.exit(0);
        } catch (RegistryException e) {
            e.printStackTrace();
            Exception e1 = e.getException();
            if (e1 != null) {
                System.err.println("Nested exception was: ");
                e1.printStackTrace();
            }
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private SOAPMessage createSOAPMessage(String alias, String reqFileName) throws RegistryException, IOException, FileNotFoundException, SOAPException, ParseException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        SOAPMessage msg = null;
        File file = new File(reqFileName);
        FileInputStream fis = new FileInputStream(file);
        if (requestIsAScheme) {
        }
        InputStream soapStream = Utility.getInstance().createSOAPStreamFromRequestStream(fis);
        fis.close();
        if (alias != null) {
            if (keyStore == null) {
                certs = authc.getCertificateChain(alias);
                privateKey = authc.getPrivateKey(alias, alias);
            } else {
                certs = keyStore.getCertificateChain(alias);
                privateKey = (PrivateKey) keyStore.getKey(alias, stringToCharArray(keyPassword));
            }
            if (privateKey == null) {
                System.err.println("No private key with alias " + "'" + alias + "'");
                System.exit(-1);
            }
            File soapFile = new File("signedSOAPRequest.xml");
            signingAlgo = privateKey.getAlgorithm();
            if (signingAlgo.equalsIgnoreCase("DSA")) {
                signingAlgo = XMLSignature.ALGO_ID_SIGNATURE_DSA;
            } else if (signingAlgo.equalsIgnoreCase("RSA")) {
                signingAlgo = XMLSignature.ALGO_ID_SIGNATURE_RSA;
            } else {
                throw new NoSuchAlgorithmException("Algorithm not supported");
            }
            FileOutputStream fos = new FileOutputStream(soapFile);
            SecurityUtil.getInstance().signSOAPMessage(soapStream, fos, privateKey, certs, signingAlgo);
            fos.close();
            soapStream = new FileInputStream(soapFile);
        }
        msg = Utility.getInstance().createSOAPMessageFromSOAPStream(soapStream);
        return msg;
    }

    public void addAttachment(String id, String fileName, String mimeType) throws FileNotFoundException, MessagingException, RegistryException {
        MimeMultipart mp = new MimeMultipart();
        MimeBodyPart bp2 = new MimeBodyPart();
        File attachFile = new File(fileName);
        FileDataSource ds = new FileDataSource(attachFile);
        DataHandler dh = new DataHandler(ds);
        bp2.setDataHandler(dh);
        bp2.addHeader("Content-Type", mimeType);
        bp2.addHeader("Content-ID", "payload2");
        mp.addBodyPart(bp2);
        SecurityUtil secUtil = SecurityUtil.getInstance();
        ByteArrayOutputStream payloadSigStream = new ByteArrayOutputStream();
        secUtil.signPayload(mp, id, payloadSigStream, privateKey, (X509Certificate) certs[0], signingAlgo);
        mp.removeBodyPart(0);
        MimeBodyPart bp1 = new MimeBodyPart();
        bp1.setContent(new String(payloadSigStream.toByteArray()), "text/plain");
        bp1.addHeader("Content-ID", "payload1");
        mp.addBodyPart(bp1);
        mp.addBodyPart(bp2);
        attachmentMap.put(id, mp);
    }

    public SOAPMessage send() throws RegistryException, ServletException, SOAPException, IOException, ParseException, MessagingException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, ParserConfigurationException, SAXException, XMLSecurityException {
        msg = createSOAPMessage(alias, reqFileName);
        Iterator attachIter = attachments.iterator();
        while (attachIter.hasNext()) {
            AttachmentInfo ai = (AttachmentInfo) attachIter.next();
            addAttachment(ai.id, ai.fileName, ai.mimeType);
        }
        Iterator iter = attachmentMap.keySet().iterator();
        while (iter.hasNext()) {
            String id = (String) iter.next();
            MimeMultipart mp = (MimeMultipart) attachmentMap.get(id);
            AttachmentPart ap = msg.createAttachmentPart(mp, "multipart/related");
            ap.setContentId(id);
            ContentType contentTypeMP = new ContentType(mp.getContentType());
            String boundary = contentTypeMP.getParameter("boundary");
            ContentType contentType = new ContentType("multipart/related");
            contentType.setParameter("boundary", boundary);
            String contentTypeStr = contentType.toString();
            if (debug) {
                System.err.println("payloads contentTypeStr = '" + contentTypeStr + "'");
            }
            ap.setContentType(contentTypeStr);
            msg.addAttachmentPart(ap);
        }
        return sendSOAPMessage(msg, this.registryURL);
    }

    private static SOAPMessage sendSOAPMessage(SOAPMessage msg, String url) throws ServletException, SOAPException, MessagingException, RegistryException, IOException {
        if (url == null) {
            throw new RegistryException("Destination URL for SOAP message not set.");
        }
        SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
        SOAPConnection connection = scf.createConnection();
        URLEndpoint endPoint = new URLEndpoint(url);
        FileOutputStream os = new FileOutputStream("senderReq.mime");
        msg.writeTo(os);
        os.close();
        long t1 = System.currentTimeMillis();
        SOAPMessage reply = null;
        if (localCall) {
            RegistryJAXMServlet servlet = new RegistryJAXMServlet();
            servlet.init();
            reply = servlet.onMessage(msg);
        } else {
            reply = connection.call(msg, endPoint);
        }
        long t2 = System.currentTimeMillis();
        if (reply != null) {
            processReplyAttachments(reply);
            reply.writeTo(System.err);
        }
        System.err.println("Elapsed time in seconds: " + (t2 - t1) / 1000);
        return reply;
    }

    private static void processReplyAttachments(SOAPMessage reply) throws SOAPException, MessagingException, RegistryException {
        Iterator apIter = reply.getAttachments();
        while (apIter.hasNext()) {
            AttachmentPart ap = (AttachmentPart) apIter.next();
            String id = ap.getContentId();
            System.err.println("Processing repository item with contentId: '" + id + "'");
            Object obj = ap.getContent();
            if (!(obj instanceof javax.mail.internet.MimeMultipart)) {
                throw new RegistryException("Expected javax.mail.internet.MimeMultipart got " + obj.getClass().getName());
            }
            MimeMultipart mp = (MimeMultipart) obj;
            if (mp.getCount() != 2) {
                throw new RegistryException("Found " + mp.getCount() + " BodyParts. A Multipart for a RepositoryItem must have exactly 2 BodyParts. First is the signature, second is the repository item.");
            }
            System.out.println("Verifying payload signature for repository item: '" + id + "'");
            SecurityUtil.getInstance().verifyPayloadSignature(id, mp);
            BodyPart bp1 = mp.getBodyPart(0);
            BodyPart bp2 = mp.getBodyPart(1);
            DataHandler dh = bp2.getDataHandler();
            String contentType = dh.getContentType();
            System.out.println("contentType = " + contentType);
            Object o;
            try {
                o = dh.getContent();
            } catch (IOException ioe) {
                o = ioe;
            }
            System.out.println("DataHandler, name=" + dh.getName() + ", type=" + contentType + ", content: (" + o.getClass().getName() + ")\n" + o);
            DataSource ds = dh.getDataSource();
            try {
                String extension = ".out";
                int slashIndex = contentType.indexOf("/");
                int contentTypeLen = contentType.length();
                if (slashIndex >= 0 && contentTypeLen > slashIndex + 1) {
                    extension = contentType.substring(slashIndex + 1, contentTypeLen);
                }
                InputStream inputStream = ds.getInputStream();
                FileOutputStream attachFileOs = new FileOutputStream("attachFile-" + Utility.getInstance().stripId(id) + "." + extension);
                OutputStream attachOs = new BufferedOutputStream(attachFileOs);
                ByteArrayOutputStream byteArrayOs = new ByteArrayOutputStream();
                byte buf[] = new byte[4096];
                int len;
                while (true) {
                    len = inputStream.read(buf);
                    if (len < 0) break;
                    byteArrayOs.write(buf, 0, len);
                }
                byte[] data = byteArrayOs.toByteArray();
                attachOs.write(data);
                attachOs.flush();
                attachOs.close();
            } catch (Exception e) {
                String errmsg = "[SOAPSender::processReplyAttachments()] -->" + " Exception writing attachment to file ..." + e;
                System.err.println(errmsg);
                throw new RegistryException(errmsg);
            }
        }
        if (reply.countAttachments() > 0) {
            reply.removeAllAttachments();
            if (reply.saveRequired()) {
                reply.saveChanges();
            }
        }
    }
}
