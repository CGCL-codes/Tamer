package org.gruposp2p.aularest.server.resource;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gruposp2p.aularest.model.Tutor;
import org.gruposp2p.aularest.model.Tutors;
import org.gruposp2p.aularest.utils.AulaLogger;
import org.gruposp2p.aularest.service.AulaDataService;
import org.gruposp2p.aularest.service.AulaDataServiceImpl;
import org.gruposp2p.aularest.utils.FileUtils;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.xml.sax.SAXException;

/**
 *
 * @author jj
 */
public class TutorsResource extends ServerResource {

    private static final Logger logger = AulaLogger.getLogger(TutorsResource.class.getName());

    public static final String TUTORS_POST_SCHEMA_PATH = "schema" + File.separator + "tutors_post.xsd";

    private AulaDataService dataService;

    public void doInit() {
        dataService = new AulaDataServiceImpl();
        setConditional(false);
    }

    /**
    * @GET
    */
    @Get
    public Representation represent(Variant variant) throws ResourceException {
        Representation resultRepresentation = null;
        try {
            List<Tutor> tutorList = dataService.findAllTutors();
            Tutors tutors = new Tutors();
            tutors.setTutorCollection(tutorList);
            resultRepresentation = new JaxbRepresentation(MediaType.TEXT_XML, tutors);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.toString(), ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, ex);
        }
        return resultRepresentation;
    }

    /**
    * @param entity
    * @throws ResourceException
    */
    @Post
    public void acceptRepresentation(Representation entity) throws ResourceException {
        Representation resultRepresentation = null;
        try {
            JaxbRepresentation<Tutors> jaxbRep = new JaxbRepresentation<Tutors>(entity, Tutors.class);
            jaxbRep.validate(FileUtils.getSchema(TUTORS_POST_SCHEMA_PATH));
            Tutors tutors = (Tutors) jaxbRep.getObject();
            Collection<Tutor> tutorCollection = tutors.getTutorCollection();
            tutors.setTutorCollection(dataService.createTutors(tutorCollection));
            JaxbRepresentation<Tutors> resultRepresentationCreated = new JaxbRepresentation<Tutors>(MediaType.TEXT_XML, tutors);
            getResponse().setEntity(resultRepresentationCreated);
            getResponse().setStatus(Status.SUCCESS_OK);
        } catch (SAXException ex) {
            logger.log(Level.SEVERE, ex.toString(), ex);
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.toString(), ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
        }
        setConditional(false);
    }
}
