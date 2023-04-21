package FireNow.resources;

import com.whereisnow.datatypes.document.*;
import com.whereisnow.datatypes.simple.*;
import com.whereisnow.datatypes.simple.ReservedType;
import com.whereisnow.webservice.client.AccountHandlerWSClient;
import com.whereisnow.webservice.client.DocsHandlerWSClient;
import com.whereisnow.webservice.datatypes.docs.*;
import com.whereisnow.webservice.stubs.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.axis2.AxisFault;
import org.apache.axis2.databinding.types.URI;
import org.apache.axis2.databinding.types.URI.MalformedURIException;
import org.apache.axis2.databinding.types.UnsignedInt;

public class WritingUtils {

    public static Version buildVersion(HashMap parameters) throws MalformedURIException {
        Version version = version = new Version();
        AttributeList versionAttributeList = new AttributeList();
        AddressList addressList = new AddressList();
        version.setTitle(buildShortTextField((String) parameters.get("title")));
        version.setDescription(buildLongTextField((String) parameters.get("description")));
        version.setTags(buildLongTextField((String) parameters.get("tags")));
        versionAttributeList = new AttributeList();
        versionAttributeList.addAttribute(buildAttribute("subject", (String) parameters.get("subject")));
        versionAttributeList.addAttribute(buildAttribute("customProtocol", (String) parameters.get("protocol")));
        version.setAttributes(versionAttributeList);
        String url = (String) parameters.get("url");
        if (!url.equals("")) {
            Address address = buildAddress(parameters);
            addressList.addAddress(address);
            version.setAddresses(addressList);
        }
        return version;
    }

    public static Address buildAddress(HashMap parameters) throws MalformedURIException {
        String url = (String) parameters.get("url");
        Address address = new Address();
        address.setReserved(true);
        address.setUrl(new URI(url));
        boolean reserved = Boolean.parseBoolean(parameters.get("reserved").toString());
        ReservedType reservedType = new ReservedType();
        reservedType.setReservedType("whereisnow_users");
        address.setReserved(reserved);
        address.setReservedType(reservedType);
        AttributeList addressAttributeList = null;
        if (!((String) parameters.get("language")).equals("-1")) {
            addressAttributeList = new AttributeList();
            addressAttributeList.addAttribute(buildAttribute("language", (String) parameters.get("language")));
        }
        if (!((String) parameters.get("fileName")).equals("")) {
            if (addressAttributeList == null) {
                addressAttributeList = new AttributeList();
            }
            addressAttributeList.addAttribute(buildAttribute("fileName", (String) parameters.get("fileName")));
            addressAttributeList.addAttribute(buildAttribute("fileSize", (String) parameters.get("fileSize")));
            addressAttributeList.addAttribute(buildAttribute("fileFormat", (String) parameters.get("fileFormat")));
        }
        if (addressAttributeList != null) {
            address.setAttributes(addressAttributeList);
        }
        return address;
    }

    private static ShortTextField buildShortTextField(String text) {
        ShortTextField localShortTextField = null;
        localShortTextField = new ShortTextField();
        localShortTextField.setShortTextField(text);
        return localShortTextField;
    }

    private static LongTextField buildLongTextField(String text) {
        LongTextField localLongTextField = null;
        localLongTextField = new LongTextField();
        localLongTextField.setLongTextField(text);
        return localLongTextField;
    }

    private static Attribute buildAttribute(String attributeName, String attributeValue) {
        Attribute localAttribute = null;
        localAttribute = new Attribute();
        localAttribute.setName(buildShortTextField(attributeName));
        localAttribute.setValue(buildLongTextField(attributeValue));
        return localAttribute;
    }

    public static HashMap registerDocument(DocsHandlerWSClient client, HashMap parameters) throws AxisFault, RemoteException, UserNameOnHttpException, InputDataException, InternalException, RegistrationException, MalformedURIException, NoSuchAlgorithmException, UnsupportedEncodingException {
        Document doc = new Document();
        VersionList versions = new VersionList();
        Version version = buildVersion(parameters);
        HashMap result = new HashMap();
        versions.addVersion(version);
        doc.setVersions(versions);
        DocumentReport report = client.registerDocument(doc);
        result.put("DocumentNow", report.getDocumentId().toString());
        result.put("VersionNow", report.getLastVersionId().toString());
        return result;
    }

    public static HashMap registerVersion(DocsHandlerWSClient client, ArrayList registeredDocs, HashMap parameters) throws AxisFault, RemoteException, UserNameOnHttpException, InputDataException, InternalException, RegistrationException, NotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException, MalformedURIException {
        Version version = buildVersion(parameters);
        DocumentReport documentReport = null;
        HashMap result = new HashMap();
        String strDocId = (String) parameters.get("DocumentNow");
        int DocumentNow = Integer.parseInt(strDocId);
        if (isLastVersionPublished(client, strDocId)) {
            documentReport = client.newVersion(DocumentNow, version);
        } else {
            documentReport = client.editVersion(DocumentNow, version);
        }
        result.put("DocumentNow", documentReport.getDocumentId());
        result.put("VersionNow", documentReport.getLastVersionId());
        return result;
    }

    public static HashMap addAddresses(DocsHandlerWSClient client, ArrayList registeredDocs, HashMap parameters) throws AxisFault, RemoteException, UserNameOnHttpException, InputDataException, InternalException, RegistrationException, NotFoundException, MalformedURIException, NoSuchAlgorithmException, UnsupportedEncodingException {
        HashMap result = new HashMap();
        String DocumentNow = parameters.get("DocumentNow").toString();
        UnsignedInt docId = new UnsignedInt(DocumentNow);
        Version version = buildVersion(parameters);
        AddressList addresses = new AddressList();
        if (!registeredDocs.contains(parameters.get("DocumentNow").toString())) {
            result = registerDocument(client, parameters);
        } else {
            if (isLastVersionPublished(client, DocumentNow.toString())) {
                DocumentReport report = client.newVersion(docId.intValue(), version);
                result.put("DocumentNow", report.getDocumentId().toString());
                result.put("VersionNow", report.getLastVersionId().toString());
            } else {
                String url = (String) parameters.get("url");
                if (url != null || !url.equals("")) {
                    Address address = buildAddress(parameters);
                    addresses.addAddress(address);
                }
                DocumentReport report = client.addAddresses(docId.intValue(), addresses.getAddress());
                result.put("DocumentNow", report.getDocumentId().toString());
                result.put("VersionNow", report.getLastVersionId().toString());
            }
        }
        return result;
    }

    public static String publish(DocsHandlerWSClient client, String DocumentNow) throws AxisFault, RemoteException, UserNameOnHttpException, InputDataException, InternalException, RegistrationException, NotFoundException, MalformedURIException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String result = "";
        UnsignedInt docId = new UnsignedInt(DocumentNow);
        PublishRequest pubReq = new PublishRequest();
        pubReq.setDocumentId(docId);
        DocumentReport report = client.publish(docId.intValue());
        result = report.getLastVersionId().toString();
        return result;
    }

    public static HashMap getDocuments(DocsHandlerWSClient client) throws AxisFault, RemoteException, UserNameOnHttpException, InputDataException, NotFoundException, InternalException, RegistrationException, NoSuchAlgorithmException, UnsupportedEncodingException {
        HashMap documents = new HashMap();
        ArrayList publishedList = new ArrayList();
        ArrayList editablesList = new ArrayList();
        ArrayList publishablesList = new ArrayList();
        ArrayList documentsId = new ArrayList();
        Summary[] docs = client.getDocumentList();
        for (int i = 0; i < docs.length; i++) {
            Summary element = docs[i];
            String obj = element.getId().toString() + "###" + element.getTitle();
            documentsId.add(element.getId().toString());
            editablesList.add(obj);
            if (docs[i].getState().getState().equals("NOT_IN_EDIT")) {
                publishedList.add(obj);
            } else if (docs[i].getState().getState().equals("PUBLISHABLE")) {
                publishablesList.add(obj);
            }
        }
        documents.put("published", publishedList);
        documents.put("unpublished", editablesList);
        documents.put("publishables", publishablesList);
        documents.put("allIds", documentsId);
        return documents;
    }

    public static boolean isLastVersionPublished(DocsHandlerWSClient client, String DocumentNow) throws AxisFault, RemoteException, UserNameOnHttpException, InputDataException, InternalException, NotFoundException, RegistrationException, NoSuchAlgorithmException, UnsupportedEncodingException {
        boolean result = false;
        ArrayList tempPublished = (ArrayList) getDocuments(client).get("published");
        ArrayList published = new ArrayList();
        for (int i = 0; i < tempPublished.size(); i++) {
            published.add(tempPublished.get(i).toString().split("###")[0]);
        }
        if (published.contains(DocumentNow)) {
            System.out.println(DocumentNow);
            result = true;
        }
        return result;
    }

    public static String getUserId(AccountHandlerWSClient client) throws NoSuchAlgorithmException, UnsupportedEncodingException, AxisFault, RemoteException, UserNameOnHttpException, InternalException {
        String result = "" + client.getMyUserID();
        return result;
    }

    public static AccountHandlerWSClient createAccountClient(String userEmail, String userPassword, String reposPath) throws NoSuchAlgorithmException, UnsupportedEncodingException, AxisFault, RemoteException, UserNameOnHttpException, InternalException {
        String repositoryPath = reposPath + File.separator + "repository";
        String certPath = repositoryPath + File.separator + "SSL-Key" + File.separator + "whereisnow_wa_cert.jks";
        AccountHandlerWSClient client = new AccountHandlerWSClient(userEmail, SHA1DigestGenerator.getSHA1Digest(userPassword), "https://www.whereisnow.com/accounthandler");
        client.supplyClientParameters(certPath, "Coffee4Ever", repositoryPath);
        client.initializeSecureStub();
        return client;
    }

    public static DocsHandlerWSClient createDocsClient(String userEmail, String userPassword, String reposPath) throws AxisFault, NoSuchAlgorithmException, UnsupportedEncodingException {
        String repositoryPath = reposPath + File.separator + "repository";
        String certPath = repositoryPath + File.separator + "SSL-Key" + File.separator + "whereisnow_wa_cert.jks";
        DocsHandlerWSClient client = new DocsHandlerWSClient(userEmail, SHA1DigestGenerator.getSHA1Digest(userPassword), "https://www.whereisnow.com/docshandler");
        client.supplyClientParameters(certPath, "Coffee4Ever", repositoryPath);
        client.initializeSecureStub();
        return client;
    }
}
