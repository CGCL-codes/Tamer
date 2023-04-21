package net.sourceforge.seqware.webservice.resources.tables;

import java.io.IOException;
import java.util.*;
import net.sf.beanlib.hibernate3.Hibernate3DtoCopier;
import net.sourceforge.seqware.common.business.WorkflowService;
import net.sourceforge.seqware.common.factory.BeanFactory;
import net.sourceforge.seqware.common.model.Workflow;
import net.sourceforge.seqware.common.model.lists.WorkflowList;
import net.sourceforge.seqware.common.util.xmltools.JaxbObject;
import net.sourceforge.seqware.common.util.xmltools.XmlTools;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author mtaschuk
 */
public class WorkflowResource extends DatabaseResource {

    private Random random = new Random(System.currentTimeMillis());

    public WorkflowResource() {
        super("Workflows");
    }

    @Get
    public void getXml() {
        authenticate();
        WorkflowService ss = BeanFactory.getWorkflowServiceBean();
        Document line = null;
        if (queryValues.get("id") != null) {
            Workflow p = (Workflow) testIfNull(ss.findByID(Integer.parseInt(queryValues.get("id"))));
            line = detachWorkflow(p);
        } else if (queryValues.get("name") != null) {
            List<Workflow> workflows = ss.findByName(queryValues.get("name"));
            if (queryValues.get("version") != null) {
                String version = queryValues.get("version").trim();
                for (Workflow workflow : workflows) {
                    if (version.equals(workflow.getVersion())) {
                        line = detachWorkflow(workflow);
                    }
                }
            } else {
                line = detachWorkflowList(workflows);
            }
        } else {
            List<Workflow> workflows = ss.list();
            line = detachWorkflowList(workflows);
        }
        getResponse().setEntity(XmlTools.getRepresentation(line));
    }

    private Document detachWorkflow(Workflow workflow) {
        Hibernate3DtoCopier copier = new Hibernate3DtoCopier();
        JaxbObject<Workflow> jaxbTool = new JaxbObject<Workflow>();
        Workflow dto = copier.hibernate2dto(Workflow.class, workflow);
        Document line = XmlTools.marshalToDocument(jaxbTool, dto);
        return line;
    }

    private Document detachWorkflowList(List<Workflow> workflows) {
        Hibernate3DtoCopier copier = new Hibernate3DtoCopier();
        JaxbObject<WorkflowList> jaxbTool = new JaxbObject<WorkflowList>();
        WorkflowList eList = new WorkflowList();
        eList.setList(new ArrayList());
        for (Workflow workflow : workflows) {
            Workflow dto = copier.hibernate2dto(Workflow.class, workflow);
            eList.add(dto);
        }
        Document line = XmlTools.marshalToDocument(jaxbTool, eList);
        return line;
    }

    @Post("xml")
    public void postJaxb(Representation entity) {
        authenticate();
        try {
            JaxbObject<Workflow> jo = new JaxbObject<Workflow>();
            String text = entity.getText();
            Workflow p;
            try {
                p = (Workflow) XmlTools.unMarshal(jo, new Workflow(), text);
            } catch (SAXException ex) {
                throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
            }
            if (p.getOwner() != null) {
                p.setOwner(BeanFactory.getRegistrationServiceBean().findByEmailAddress(p.getOwner().getEmailAddress()));
            } else {
                p.setOwner(registration);
            }
            WorkflowService ws = BeanFactory.getWorkflowServiceBean();
            Integer id = ws.insert(registration, p);
            Workflow w = ws.findBySWAccession(id);
            Hibernate3DtoCopier copier = new Hibernate3DtoCopier();
            Workflow detachedW = copier.hibernate2dto(Workflow.class, w);
            Document line = XmlTools.marshalToDocument(jo, detachedW);
            getResponse().setEntity(XmlTools.getRepresentation(line));
            getResponse().setLocationRef(getRequest().getRootRef() + "/workflows/" + detachedW.getSwAccession());
            getResponse().setStatus(Status.SUCCESS_CREATED);
        } catch (SecurityException e) {
            getResponse().setStatus(Status.CLIENT_ERROR_FORBIDDEN, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, e.getMessage());
        }
    }
}
