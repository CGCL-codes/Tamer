package org.gruposp2p.aularest.server.resource;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gruposp2p.aularest.model.Course;
import org.gruposp2p.aularest.service.AulaDataService;
import org.gruposp2p.aularest.service.AulaDataServiceImpl;
import org.gruposp2p.aularest.utils.AulaLogger;
import org.gruposp2p.aularest.utils.FileUtils;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.jaxb.JaxbRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.Delete;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.xml.sax.SAXException;

/**
 *
 * @author jj
 */
public class CourseResource extends ServerResource {

    private static final Logger logger = AulaLogger.getLogger(CourseResource.class.getName());

    public static final String COURSE_PUT_SCHEMA_PATH = "schema" + File.separator + "course_put.xsd";

    private Course course;

    private AulaDataService dataService;

    public void doInit() throws ResourceException {
        String courseID = (String) getRequestAttributes().get("courseID");
        dataService = new AulaDataServiceImpl();
        try {
            this.course = dataService.findCourse(new Integer(courseID));
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, e);
        }
        if (this.course == null) {
            throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
        }
        setConditional(false);
    }

    /**
    * HTTP GET. Get resource
    *
    * @param variant
    * @throws ResourceException
    */
    @Get
    public Representation represent(Variant variant) throws ResourceException {
        JaxbRepresentation jaxbRep = new JaxbRepresentation(MediaType.TEXT_XML, this.course);
        return jaxbRep;
    }

    /**
     * HTTP DELETE. Delete resource
     * 
     * @throws ResourceException
     */
    @Delete
    public void removeRepresentations() throws ResourceException {
        try {
            dataService.destroyCourse(course);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, ex);
        }
    }

    /**
    * HTTP PUT. Update resource
    *
    * @param entity
    * @throws ResourceException
    */
    @Put
    public void storeRepresentation(Representation entity) throws ResourceException {
        try {
            JaxbRepresentation<Course> jaxbRep = new JaxbRepresentation<Course>(entity, Course.class);
            jaxbRep.validate(FileUtils.getSchema(COURSE_PUT_SCHEMA_PATH));
            Course updatedCourse = (Course) jaxbRep.getObject();
            dataService.putCourse(course, updatedCourse);
            getResponse().setEntity(new JaxbRepresentation(MediaType.TEXT_XML, updatedCourse));
            getResponse().setStatus(Status.SUCCESS_OK);
        } catch (SAXException ex) {
            logger.log(Level.SEVERE, ex.toString(), ex);
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, ex);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.toString(), ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, ex);
        }
    }
}
