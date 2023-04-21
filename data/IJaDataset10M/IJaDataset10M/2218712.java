package org.azrul.epice.rest.service;

import com.thoughtworks.xstream.XStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;
import org.azrul.epice.dao.AttachmentDAO;
import org.azrul.epice.dao.FileRepositoryDAO;
import org.azrul.epice.dao.ItemDAO;
import org.azrul.epice.dao.factory.AttachmentDAOFactory;
import org.azrul.epice.dao.factory.FileRepositoryDAOFactory;
import org.azrul.epice.dao.factory.ItemDAOFactory;
import org.azrul.epice.domain.Attachment;
import org.azrul.epice.domain.FileRepository;
import org.azrul.epice.domain.Item;
import org.azrul.epice.rest.dto.DeleteAttachmentRequest;
import org.azrul.epice.rest.dto.DeleteAttachmentResponse;

/**
 * REST Web Service
 *
 * @author azrulhasni
 */
@Path("deleteAttachment")
public class DeleteAttachmentResource {

    @Context
    private UriInfo context;

    /** Creates a new instance of Resource */
    public DeleteAttachmentResource() {
    }

    /**
     * Retrieves representation of an instance of org.azrul.epice.rest.service.Resource
     * @return an instance of java.lang.String
     */
    @POST
    @Produces("application/xml")
    public String getXml() {
        XStream writer = new XStream();
        writer.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        writer.alias("DeleteAttachmentResponse", DeleteAttachmentResponse.class);
        XStream reader = new XStream();
        reader.setMode(XStream.XPATH_ABSOLUTE_REFERENCES);
        reader.alias("DeleteAttachmentRequest", DeleteAttachmentRequest.class);
        DeleteAttachmentResponse errorResponse = new DeleteAttachmentResponse();
        List<String> errors = new ArrayList<String>();
        errors.add("DELEGATE ITEM ERROR");
        errorResponse.setErrors(errors);
        MultivaluedMap<String, String> params = context.getQueryParameters();
        try {
            String request = URLDecoder.decode(params.getFirst("REQUEST"), "UTF-8");
            if (request == null) {
                return writer.toXML(errorResponse);
            }
            DeleteAttachmentRequest req = (DeleteAttachmentRequest) reader.fromXML(request);
            DeleteAttachmentResponse res = doService(req);
            return URLEncoder.encode(writer.toXML(res), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return writer.toXML(errorResponse);
    }

    public DeleteAttachmentResponse doService(DeleteAttachmentRequest req) {
        ItemDAO itemDAO = ItemDAOFactory.getInstance();
        Item item = itemDAO.findItemById(req.getItemId());
        FileRepositoryDAO fileRepoDAO = FileRepositoryDAOFactory.getInstance();
        FileRepository fileRepo = fileRepoDAO.refresh(item.getFileRepository());
        AttachmentDAO attachmentDAO = AttachmentDAOFactory.getInstance();
        Set<Attachment> attachments = attachmentDAO.findAttachmentsByIds(req.getAttachmentIds());
        fileRepoDAO.removeAttachmentsFromFileRepository(fileRepo, attachments, req.getSessionId());
        DeleteAttachmentResponse res = new DeleteAttachmentResponse();
        res.setCarriedQuery(req.getCarriedQuery());
        res.setSessionId(req.getSessionId());
        return res;
    }

    /**
     * PUT method for updating or creating an instance of Resource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }
}
