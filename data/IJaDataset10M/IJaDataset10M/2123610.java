package com.columboid.protocol.syncml.tests;

import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import javax.xml.bind.JAXBException;
import junit.framework.TestCase;
import org.junit.Test;
import org.xml.sax.SAXException;
import com.columboid.protocol.syncml.ClientCredential;
import com.columboid.protocol.syncml.CredentialType;
import com.columboid.protocol.syncml.ServerAuthentication;
import com.columboid.protocol.syncml.helper.EncryptionHelper;
import com.columboid.protocol.syncml.helper.JaxbHelper;
import com.columboid.protocol.syncml.metainfo.MetInf;
import com.columboid.protocol.syncml.metainfo.NextNonce;
import com.columboid.protocol.syncml.representation.Cred;
import com.columboid.protocol.syncml.representation.Key;
import com.columboid.protocol.syncml.representation.ObjectFactory;
import com.columboid.protocol.syncml.representation.Routing;
import com.columboid.protocol.syncml.representation.Source;
import com.columboid.protocol.syncml.representation.SyncML;
import com.columboid.protocol.syncml.representation.Target;
import com.columboid.protocol.syncml.tests.resources.LoginString;

public class LoginTest extends TestCase {

    @Test
    public void testFirstLoginRequest() throws JAXBException {
        String xml = LoginString.getFirstLoginRequest();
        String xsd = JaxbHelper.getSyncMLSchema();
        StringReader xmlReader = new StringReader(xml);
        StringReader xsdReader = new StringReader(xsd);
        try {
            SyncML sml = JaxbHelper.CreateSyncMLObject(xmlReader, xsdReader, SyncML.class, ObjectFactory.class);
            assertTrue(sml.getSyncHdr().getSource().getLocName().equals("Bruce2"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            assertTrue(false);
        } finally {
            if (xmlReader != null) {
                xmlReader.close();
                xmlReader = null;
            }
            if (xsdReader != null) {
                xsdReader.close();
                xsdReader = null;
            }
        }
    }

    @Test
    public void testLoginRequiresTokenVerificationRequest() throws JAXBException {
        String xml = LoginString.getClientRequiresTokenRequest("abcdefg");
        String xsd = JaxbHelper.getSyncMLSchema();
        StringReader xmlReader = new StringReader(xml);
        StringReader xsdReader = new StringReader(xsd);
        try {
            SyncML sml = JaxbHelper.CreateSyncMLObject(xmlReader, xsdReader, SyncML.class, ObjectFactory.class);
            String metaString = sml.getSyncHdr().getCred().getMeta();
            String metaXml = metaString;
            String metaXsd = JaxbHelper.getMetaSchema();
            StringReader metaXmlReader = new StringReader(metaXml);
            StringReader metaXsdReader = new StringReader(metaXsd);
            MetInf metaSml = JaxbHelper.CreateSyncMLObject(metaXmlReader, metaXsdReader, MetInf.class, com.columboid.protocol.syncml.metainfo.ObjectFactory.class);
            NextNonce requestNonce = metaSml.getNextNonce();
            assertTrue(requestNonce.getContent().equals("12345"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            assertTrue(false);
        } finally {
            if (xmlReader != null) {
                xmlReader.close();
                xmlReader = null;
            }
            if (xsdReader != null) {
                xsdReader.close();
                xsdReader = null;
            }
        }
    }

    @Test
    public void testLoginRequiresTokenVerificationResponse() throws JAXBException {
        String expected = LoginString.getClientRequiresTokenResponse("abcdefg");
        SyncML sml = JaxbHelper.getServerResponse(1, 2, "abcdefg", "12345", "IMEI:493005100592800", "http://www.columboid.com/Service", "200");
        String actual = JaxbHelper.getJaxbObjectString(sml, true);
        assertTrue(actual.equals(expected));
    }

    @Test
    public void testFirstLoginResponse() throws JAXBException {
        String nonce = UUID.randomUUID().toString();
        String expected = LoginString.getFirstLoginResponse(nonce);
        SyncML sml = JaxbHelper.getServerResponse(1, 1, "", nonce, "IMEI:493005100592800", "http://www.columboid.com/Service", "407");
        String actual = JaxbHelper.getJaxbObjectString(sml, false);
        assertTrue(actual.equals(expected));
    }

    @Test
    public void testLoginWithChallengeRequest() throws NoSuchAlgorithmException {
        String nonce = UUID.randomUUID().toString();
        String hashedPassword = "/tO2GyYIGEk3gICzTmk9Lg==";
        String user = "Bruce2";
        String chanllenge = EncryptionHelper.GetHashString(user, hashedPassword, nonce, CredentialType.MD5);
        String expected = LoginString.getSecondLoginRequest(chanllenge);
        String response = LoginString.getFirstLoginResponse(nonce);
        String xsd = JaxbHelper.getSyncMLSchema();
        StringReader xmlReader = new StringReader(response);
        StringReader xsdReader = new StringReader(xsd);
        try {
            String firstRequest = LoginString.getFirstLoginRequest();
            StringReader xmlFirstReader = new StringReader(firstRequest);
            SyncML firstSml = JaxbHelper.CreateSyncMLObject(xmlFirstReader, xsdReader, SyncML.class, ObjectFactory.class);
            Routing routing = (Routing) firstSml.getSyncBody().getKeyOrRoutingOrAlert().get(0);
            xsdReader.close();
            xsdReader = new StringReader(xsd);
            SyncML sml = JaxbHelper.CreateSyncMLObject(xmlReader, xsdReader, SyncML.class, ObjectFactory.class);
            Cred cred = new Cred();
            String xml = "";
            xml += "<Type xmlns=\"syncml:metinf\">syncml:auth-md5</Type>";
            cred.setMeta(xml);
            cred.setData(chanllenge);
            sml.getSyncHdr().setCred(cred);
            Target target = sml.getSyncHdr().getTarget();
            Source source = sml.getSyncHdr().getSource();
            String sourceUri = target.getLocURI();
            sml.getSyncHdr().getTarget().setLocURI(source.getLocURI());
            sml.getSyncHdr().getSource().setLocURI(sourceUri);
            sml.getSyncHdr().getSource().setLocName(user);
            sml.getSyncBody().getKeyOrRoutingOrAlert().remove(0);
            sml.getSyncBody().getKeyOrRoutingOrAlert().add(routing);
            String actual = JaxbHelper.getJaxbObjectString(sml, false);
            Boolean result = actual.equals(expected);
            assertTrue(result);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            assertTrue(false);
        } finally {
            if (xmlReader != null) {
                xmlReader.close();
                xmlReader = null;
            }
            if (xsdReader != null) {
                xsdReader.close();
                xsdReader = null;
            }
        }
    }

    @Test
    public void testSucceedLoginResponse() throws NoSuchAlgorithmException {
        String nonce = UUID.randomUUID().toString();
        String hashedPassword = "/tO2GyYIGEk3gICzTmk9Lg==";
        String user = "Bruce2";
        String expected = LoginString.getSucceedLoginRequest(nonce, "200");
        String chanllenge = EncryptionHelper.GetHashString(user, hashedPassword, nonce, CredentialType.MD5);
        String request = LoginString.getSecondLoginRequest(chanllenge);
        String xsd = JaxbHelper.getSyncMLSchema();
        StringReader xmlReader = new StringReader(request);
        StringReader xsdReader = new StringReader(xsd);
        try {
            SyncML requestSml = JaxbHelper.CreateSyncMLObject(xmlReader, xsdReader, SyncML.class, ObjectFactory.class);
            String requestUser = requestSml.getSyncHdr().getSource().getLocName();
            String requestToken = requestSml.getSyncHdr().getCred().getData();
            ServerAuthentication authentication = new ServerAuthentication();
            ClientCredential credential = new ClientCredential();
            credential.setHashPassword(hashedPassword);
            credential.setNonceToken(nonce);
            credential.setUserName(requestUser);
            Boolean result = authentication.checkCredential(credential, requestToken);
            if (result) {
                SyncML sml = JaxbHelper.getServerResponse(1, 2, "", nonce, "IMEI:493005100592800", "http://www.columboid.com/Service", "200");
                String actual = JaxbHelper.getJaxbObjectString(sml, true);
                assertTrue(actual.equals(expected));
            } else {
                assertTrue(false);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            assertTrue(false);
        } finally {
            if (xmlReader != null) {
                xmlReader.close();
                xmlReader = null;
            }
            if (xsdReader != null) {
                xsdReader.close();
                xsdReader = null;
            }
        }
    }

    @Test
    public void testFailedLoginResponse() throws NoSuchAlgorithmException {
        String nonce = UUID.randomUUID().toString();
        String hashedPassword = "/tO2GyYIGEk3gICzTmk9Lg==";
        String user = "Bruce3";
        String expected = LoginString.getSucceedLoginRequest(nonce, "401");
        String chanllenge = EncryptionHelper.GetHashString(user, hashedPassword, nonce, CredentialType.MD5);
        String request = LoginString.getSecondLoginRequest(chanllenge);
        String xsd = JaxbHelper.getSyncMLSchema();
        StringReader xmlReader = new StringReader(request);
        StringReader xsdReader = new StringReader(xsd);
        try {
            SyncML requestSml = JaxbHelper.CreateSyncMLObject(xmlReader, xsdReader, SyncML.class, ObjectFactory.class);
            String requestUser = requestSml.getSyncHdr().getSource().getLocName();
            String requestToken = requestSml.getSyncHdr().getCred().getData();
            ServerAuthentication authentication = new ServerAuthentication();
            ClientCredential credential = new ClientCredential();
            credential.setHashPassword(hashedPassword);
            credential.setNonceToken(nonce);
            credential.setUserName(requestUser);
            Boolean result = authentication.checkCredential(credential, requestToken);
            if (!result) {
                SyncML sml = JaxbHelper.getServerResponse(1, 2, "", nonce, "IMEI:493005100592800", "http://www.columboid.com/Service", "401");
                String actual = JaxbHelper.getJaxbObjectString(sml, false);
                assertTrue(actual.equals(expected));
            } else {
                assertTrue(false);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            assertTrue(false);
        } finally {
            if (xmlReader != null) {
                xmlReader.close();
                xmlReader = null;
            }
            if (xsdReader != null) {
                xsdReader.close();
                xsdReader = null;
            }
        }
    }

    @Test
    public void testLoginRequiredClientPublicKeyResponse() throws NoSuchAlgorithmException {
        String nonce = UUID.randomUUID().toString();
        String hashedPassword = "/tO2GyYIGEk3gICzTmk9Lg==";
        String user = "Bruce2";
        String expected = LoginString.getSucceedLoginRequest(nonce, "417");
        String chanllenge = EncryptionHelper.GetHashString(user, hashedPassword, nonce, CredentialType.MD5);
        String request = LoginString.getSecondLoginRequest(chanllenge);
        String xsd = JaxbHelper.getSyncMLSchema();
        StringReader xmlReader = new StringReader(request);
        StringReader xsdReader = new StringReader(xsd);
        try {
            SyncML requestSml = JaxbHelper.CreateSyncMLObject(xmlReader, xsdReader, SyncML.class, ObjectFactory.class);
            String requestUser = requestSml.getSyncHdr().getSource().getLocName();
            String requestToken = requestSml.getSyncHdr().getCred().getData();
            ServerAuthentication authentication = new ServerAuthentication();
            ClientCredential credential = new ClientCredential();
            credential.setHashPassword(hashedPassword);
            credential.setNonceToken(nonce);
            credential.setUserName(requestUser);
            Boolean result = authentication.checkCredential(credential, requestToken);
            if (result) {
                SyncML sml = JaxbHelper.getServerResponse(1, 2, "", nonce, "IMEI:493005100592800", "http://www.columboid.com/Service", "417");
                String actual = JaxbHelper.getJaxbObjectString(sml, false);
                assertTrue(actual.equals(expected));
            } else {
                assertTrue(false);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            assertTrue(false);
        } finally {
            if (xmlReader != null) {
                xmlReader.close();
                xmlReader = null;
            }
            if (xsdReader != null) {
                xsdReader.close();
                xsdReader = null;
            }
        }
    }

    @Test
    public void testLoginWithClientPublicKeyRequest() throws NoSuchAlgorithmException {
        String nonce = UUID.randomUUID().toString();
        String hashedPassword = "/tO2GyYIGEk3gICzTmk9Lg==";
        String user = "Bruce2";
        String chanllenge = EncryptionHelper.GetHashString(user, hashedPassword, nonce, CredentialType.MD5);
        String publicKey = "ClientPublicKey";
        String expected = LoginString.getThridLoginRequest(chanllenge, publicKey);
        String response = LoginString.getFirstLoginResponse(nonce);
        String xsd = JaxbHelper.getSyncMLSchema();
        StringReader xmlReader = new StringReader(response);
        StringReader xsdReader = new StringReader(xsd);
        Key key = new Key();
        key.setCmdID("1");
        key.setData(publicKey);
        SyncML sml = null;
        try {
            sml = JaxbHelper.CreateSyncMLObject(xmlReader, xsdReader, SyncML.class, ObjectFactory.class);
        } catch (JAXBException e1) {
            e1.printStackTrace();
        } catch (SAXException e1) {
            e1.printStackTrace();
        }
        Cred cred = new Cred();
        String xml = "";
        xml += "<Type xmlns=\"syncml:metinf\">syncml:auth-md5</Type>";
        cred.setMeta(xml);
        cred.setData(chanllenge);
        sml.getSyncHdr().setCred(cred);
        Target target = sml.getSyncHdr().getTarget();
        Source source = sml.getSyncHdr().getSource();
        String sourceUri = target.getLocURI();
        sml.getSyncHdr().getTarget().setLocURI(source.getLocURI());
        sml.getSyncHdr().getSource().setLocURI(sourceUri);
        sml.getSyncHdr().getSource().setLocName(user);
        sml.getSyncBody().getKeyOrRoutingOrAlert().remove(0);
        sml.getSyncBody().getKeyOrRoutingOrAlert().add(key);
        String actual = null;
        try {
            actual = JaxbHelper.getJaxbObjectString(sml, false);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        Boolean result = actual.equals(expected);
        assertTrue(result);
        if (xmlReader != null) {
            xmlReader.close();
            xmlReader = null;
        }
        if (xsdReader != null) {
            xsdReader.close();
            xsdReader = null;
        }
    }
}
