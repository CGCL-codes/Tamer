package ias.springnote.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.codec.binary.Base64;
import org.restlet.Client;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.CharacterSet;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.StringRepresentation;
import com.openmaru.api.ErrorsType;
import com.springnote.api.AttachmentType;
import com.springnote.api.AttachmentsType;
import com.springnote.api.CollaborationsType;
import com.springnote.api.LockType;
import com.springnote.api.PageType;
import com.springnote.api.PagesType;
import com.springnote.api.RevisionType;
import com.springnote.api.RevisionsType;
import com.springnote.api.TagsType;
import java.io.File;

/**
 *
 * @author ias
 */
public class RestClient {

    private static final String SERVER_URL = "http://api.springnote.com";

    private static final String PAGES_PLURAL_URI = "%s/pages.xml%s";

    private static final String PAGES_PLURAL_QUERY_URI = "%s/pages.xml%s";

    private static final String PAGES_SINGULAR_URI = "%s/pages/%d.xml%s";

    private static final String REVISIONS_PLURAL_URI = "%s/pages/%d/revisions.xml%s";

    private static final String REVISIONS_SINGULAR_URI = "%s/pages/%d/revisions/%d.xml%s";

    private static final String ATTACHMENTS_PLURAL_URI = "%s/pages/%d/attachments.xml%s";

    private static final String ATTACHMENTS_UPLOAD_URI = "%s/pages/%d/attachments.xml%s";

    private static final String ATTACHMENTS_SINGULAR_URI = "%s/pages/%d/attachments/%d.xml%s";

    private static final String ATTACHMENTS_SINGULAR_RAW_URI = "%s/pages/%d/attachments/%d%s";

    private static final String COLLABORATION_SINGULAR_URI = "%s/pages/%d/collaboration.xml%s";

    private static final String LOCK_SINGULAR_URI = "%s/pages/%d/lock.xml%s";

    private static final String TAGS_PLURAL_URI = "%s/pages/%d/tags.xml%s";

    private Client client = new Client(Protocol.HTTP);

    private HttpClient httpClient = new HttpClient();

    private String basicAuthValue;

    private ChallengeResponse authentication;

    private String domain;

    /** Creates a new instance of RestClient */
    public RestClient() {
    }

    /** Creates a new instance of RestClient */
    public RestClient(String userId, String userKey, String appKey) {
        setAuthInfo(userId, userKey, appKey);
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    private String getDomainParam() {
        return domain != null ? "?domain=" + domain : "";
    }

    private String getTagParam(String param, String tag) {
        return param.equals("") ? "?tags=" + tag : param + "&tags=" + tag;
    }

    private ErrorsType checkErrors(Response response) {
        Status status = response.getStatus();
        ErrorsType errors = null;
        if (status.isError()) {
            try {
                JAXBContext jc = JAXBContext.newInstance("com.openmaru.api");
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                errors = ((JAXBElement<ErrorsType>) unmarshaller.unmarshal(response.getEntity().getStream())).getValue();
            } catch (JAXBException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return errors;
    }

    public PagesType getPages() throws ResourceException {
        Request request = new Request(Method.GET, String.format(PAGES_PLURAL_URI, SERVER_URL, getDomainParam()));
        request.setChallengeResponse(authentication);
        Response response = client.handle(request);
        ErrorsType errors = checkErrors(response);
        if (errors != null) {
            throw new ResourceException(errors);
        }
        PagesType pages = null;
        try {
            JAXBContext jc = JAXBContext.newInstance("com.springnote.api");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            pages = ((JAXBElement<PagesType>) unmarshaller.unmarshal(response.getEntity().getStream())).getValue();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return pages;
    }

    public PagesType getPages(String sortField, String sortOrder, int limit) throws ResourceException {
        Request request = new Request(Method.GET, String.format(PAGES_PLURAL_URI, SERVER_URL, getDomainParam()));
        request.setChallengeResponse(authentication);
        Response response = client.handle(request);
        ErrorsType errors = checkErrors(response);
        if (errors != null) {
            throw new ResourceException(errors);
        }
        PagesType pages = null;
        try {
            JAXBContext jc = JAXBContext.newInstance("com.springnote.api");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            pages = ((JAXBElement<PagesType>) unmarshaller.unmarshal(response.getEntity().getStream())).getValue();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return pages;
    }

    public PagesType getPagesByTag(String tag) throws ResourceException {
        Request request = new Request(Method.GET, String.format(PAGES_PLURAL_URI, SERVER_URL, getTagParam(getDomainParam(), tag)));
        request.setChallengeResponse(authentication);
        Response response = client.handle(request);
        ErrorsType errors = checkErrors(response);
        if (errors != null) {
            throw new ResourceException(errors);
        }
        PagesType pages = null;
        try {
            JAXBContext jc = JAXBContext.newInstance("com.springnote.api");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            pages = ((JAXBElement<PagesType>) unmarshaller.unmarshal(response.getEntity().getStream())).getValue();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return pages;
    }

    public PageType getPage(int identifier) throws ResourceException {
        Request request = new Request(Method.GET, String.format(PAGES_SINGULAR_URI, SERVER_URL, identifier, getDomainParam()));
        request.setChallengeResponse(authentication);
        Response response = client.handle(request);
        ErrorsType errors = checkErrors(response);
        if (errors != null) {
            throw new ResourceException(errors);
        }
        PageType page = null;
        try {
            JAXBContext jc = JAXBContext.newInstance("com.springnote.api");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            page = ((JAXBElement<PageType>) unmarshaller.unmarshal(response.getEntity().getStream())).getValue();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return page;
    }

    public PageType createPage(PageType page) throws ResourceException {
        Request request = new Request(Method.POST, String.format(PAGES_PLURAL_URI, SERVER_URL, getDomainParam()));
        request.setChallengeResponse(authentication);
        try {
            JAXBContext jc = JAXBContext.newInstance("com.springnote.api");
            Marshaller marshaller = jc.createMarshaller();
            StringWriter writer = new StringWriter();
            marshaller.marshal(new JAXBElement(new QName("page"), PageType.class, page), writer);
            request.setEntity(new StringRepresentation(writer.getBuffer(), MediaType.TEXT_XML, Language.ALL, CharacterSet.UTF_8));
            Response response = client.handle(request);
            ErrorsType errors = checkErrors(response);
            if (errors != null) {
                throw new ResourceException(errors);
            }
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            page = ((JAXBElement<PageType>) unmarshaller.unmarshal(response.getEntity().getStream())).getValue();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return page;
    }

    public boolean updatePage(PageType page) throws ResourceException {
        Request request = new Request(Method.PUT, String.format(PAGES_SINGULAR_URI, SERVER_URL, page.getIdentifier(), getDomainParam()));
        request.setChallengeResponse(authentication);
        try {
            JAXBContext jc = JAXBContext.newInstance("com.springnote.api");
            Marshaller marshaller = jc.createMarshaller();
            StringWriter writer = new StringWriter();
            marshaller.marshal(new JAXBElement(new QName("page"), PageType.class, page), writer);
            request.setEntity(new StringRepresentation(writer.getBuffer(), MediaType.TEXT_XML, Language.ALL, CharacterSet.UTF_8));
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
        Response response = client.handle(request);
        ErrorsType errors = checkErrors(response);
        if (errors != null) {
            throw new ResourceException(errors);
        }
        return response.getStatus().isSuccess();
    }

    public boolean deletePage(int identifier) throws ResourceException {
        Request request = new Request(Method.DELETE, String.format(PAGES_SINGULAR_URI, SERVER_URL, identifier, getDomainParam()));
        request.setChallengeResponse(authentication);
        Response response = client.handle(request);
        ErrorsType errors = checkErrors(response);
        if (errors != null) {
            throw new ResourceException(errors);
        }
        return response.getStatus().isSuccess();
    }

    public RevisionsType getRevisions(int pageIdentifier) throws ResourceException {
        Request request = new Request(Method.GET, String.format(REVISIONS_PLURAL_URI, SERVER_URL, pageIdentifier, getDomainParam()));
        request.setChallengeResponse(authentication);
        Response response = client.handle(request);
        ErrorsType errors = checkErrors(response);
        if (errors != null) {
            throw new ResourceException(errors);
        }
        RevisionsType revisions = null;
        try {
            JAXBContext jc = JAXBContext.newInstance("com.springnote.api");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            revisions = ((JAXBElement<RevisionsType>) unmarshaller.unmarshal(response.getEntity().getStream())).getValue();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return revisions;
    }

    public RevisionType getRevision(int pageIdentifier, int identifier) throws ResourceException {
        Request request = new Request(Method.GET, String.format(REVISIONS_SINGULAR_URI, SERVER_URL, pageIdentifier, identifier, getDomainParam()));
        request.setChallengeResponse(authentication);
        Response response = client.handle(request);
        ErrorsType errors = checkErrors(response);
        if (errors != null) {
            throw new ResourceException(errors);
        }
        RevisionType revision = null;
        try {
            JAXBContext jc = JAXBContext.newInstance("com.springnote");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            revision = ((JAXBElement<RevisionType>) unmarshaller.unmarshal(response.getEntity().getStream())).getValue();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return revision;
    }

    public AttachmentsType getAttachments(int pageIdentifier) throws ResourceException {
        Request request = new Request(Method.GET, String.format(ATTACHMENTS_PLURAL_URI, SERVER_URL, pageIdentifier, getDomainParam()));
        request.setChallengeResponse(authentication);
        Response response = client.handle(request);
        ErrorsType errors = checkErrors(response);
        if (errors != null) {
            throw new ResourceException(errors);
        }
        AttachmentsType attachments = null;
        try {
            JAXBContext jc = JAXBContext.newInstance("com.springnote.api");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            attachments = ((JAXBElement<AttachmentsType>) unmarshaller.unmarshal(response.getEntity().getStream())).getValue();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return attachments;
    }

    public AttachmentType getAttachment(int pageIdentifier, int identifier) throws ResourceException {
        Request request = new Request(Method.GET, String.format(ATTACHMENTS_SINGULAR_URI, SERVER_URL, pageIdentifier, identifier, getDomainParam()));
        request.setChallengeResponse(authentication);
        Response response = client.handle(request);
        ErrorsType errors = checkErrors(response);
        if (errors != null) {
            throw new ResourceException(errors);
        }
        AttachmentType attachment = null;
        try {
            JAXBContext jc = JAXBContext.newInstance("com.springnote.api");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            attachment = ((JAXBElement<AttachmentType>) unmarshaller.unmarshal(response.getEntity().getStream())).getValue();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return attachment;
    }

    public InputStream getAttachmentStream(int pageIdentifier, int identifier) throws ResourceException {
        Request request = new Request(Method.GET, String.format(ATTACHMENTS_SINGULAR_RAW_URI, SERVER_URL, pageIdentifier, identifier, getDomainParam()));
        request.setChallengeResponse(authentication);
        Response response = client.handle(request);
        ErrorsType errors = checkErrors(response);
        if (errors != null) {
            throw new ResourceException(errors);
        }
        InputStream stream = null;
        try {
            stream = response.getEntity().getStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return stream;
    }

    public AttachmentType putAttachmentStream(int pageIdentifier, byte[] stream, String fileName) throws ResourceException {
        PostMethod filePost = new PostMethod(String.format(ATTACHMENTS_UPLOAD_URI, SERVER_URL, pageIdentifier, getDomainParam()));
        filePost.addRequestHeader("Authorization", "Basic " + basicAuthValue);
        Part[] parts = { new FilePart("Filedata", new ByteArrayPartSource(fileName, stream)) };
        filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
        AttachmentType attachment = null;
        try {
            httpClient.executeMethod(filePost);
            JAXBContext jc = JAXBContext.newInstance("com.springnote.api");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            attachment = ((JAXBElement<AttachmentType>) unmarshaller.unmarshal(filePost.getResponseBodyAsStream())).getValue();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            filePost.releaseConnection();
        }
        return attachment;
    }

    public AttachmentType putAttachmentStream(int pageIdentifier, File file) throws ResourceException {
        PostMethod filePost = new PostMethod(String.format(ATTACHMENTS_UPLOAD_URI, SERVER_URL, pageIdentifier, getDomainParam()));
        filePost.addRequestHeader("Authorization", "Basic " + basicAuthValue);
        AttachmentType attachment = null;
        try {
            Part[] parts = { new FilePart("Filedata", file) };
            filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
            httpClient.executeMethod(filePost);
            JAXBContext jc = JAXBContext.newInstance("com.springnote.api");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            attachment = ((JAXBElement<AttachmentType>) unmarshaller.unmarshal(filePost.getResponseBodyAsStream())).getValue();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            filePost.releaseConnection();
        }
        return attachment;
    }

    public CollaborationsType getCollaboration(int pageIdentifier) throws ResourceException {
        Request request = new Request(Method.GET, String.format(COLLABORATION_SINGULAR_URI, SERVER_URL, pageIdentifier, getDomainParam()));
        request.setChallengeResponse(authentication);
        Response response = client.handle(request);
        ErrorsType errors = checkErrors(response);
        if (errors != null) {
            throw new ResourceException(errors);
        }
        CollaborationsType collaboration = null;
        try {
            JAXBContext jc = JAXBContext.newInstance("com.springnote.api");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            collaboration = ((JAXBElement<CollaborationsType>) unmarshaller.unmarshal(response.getEntity().getStream())).getValue();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return collaboration;
    }

    public LockType getLock(int pageIdentifier) throws ResourceException {
        Request request = new Request(Method.GET, String.format(LOCK_SINGULAR_URI, SERVER_URL, pageIdentifier, getDomainParam()));
        request.setChallengeResponse(authentication);
        Response response = client.handle(request);
        ErrorsType errors = checkErrors(response);
        if (errors != null) {
            throw new ResourceException(errors);
        }
        LockType lock = null;
        try {
            JAXBContext jc = JAXBContext.newInstance("com.springnote.api");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            lock = ((JAXBElement<LockType>) unmarshaller.unmarshal(response.getEntity().getStream())).getValue();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return lock;
    }

    public TagsType getTags(int pageIdentifier) throws ResourceException {
        Request request = new Request(Method.GET, String.format(TAGS_PLURAL_URI, SERVER_URL, pageIdentifier, getDomainParam()));
        request.setChallengeResponse(authentication);
        Response response = client.handle(request);
        ErrorsType errors = checkErrors(response);
        if (errors != null) {
            throw new ResourceException(errors);
        }
        TagsType tags = null;
        try {
            JAXBContext jc = JAXBContext.newInstance("com.springnote.api");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            tags = ((JAXBElement<TagsType>) unmarshaller.unmarshal(response.getEntity().getStream())).getValue();
        } catch (JAXBException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return tags;
    }

    public void setAuthInfo(String userId, String userKey, String appKey) {
        java.lang.String password = userKey + "." + appKey;
        try {
            userId = java.net.URLEncoder.encode(userId, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        }
        setAuthorization(userId, password);
    }

    public void setAuthorization(String username, String password) {
        basicAuthValue = new String(Base64.encodeBase64((username + ":" + password).getBytes()));
        authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, username, password);
    }
}
