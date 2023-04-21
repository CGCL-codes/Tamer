package net.sourceforge.seqware.webservice.resources.tables;

import java.io.IOException;
import java.util.Random;
import net.sf.beanlib.hibernate3.Hibernate3DtoCopier;
import net.sourceforge.seqware.common.business.WorkflowParamService;
import net.sourceforge.seqware.common.business.WorkflowService;
import net.sourceforge.seqware.common.factory.BeanFactory;
import net.sourceforge.seqware.common.model.Workflow;
import net.sourceforge.seqware.common.model.WorkflowParam;
import net.sourceforge.seqware.common.model.lists.WorkflowParamList;
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
public class WorkflowParamResource extends DatabaseResource {

    private Random random = new Random(System.currentTimeMillis());

    public WorkflowParamResource() {
        super("WorkflowParams");
    }

    @Post
    public void postJaxb(Representation entity) {
        authenticate();
        try {
            JaxbObject<WorkflowParam> jo = new JaxbObject<WorkflowParam>();
            String text = entity.getText();
            WorkflowParam p;
            try {
                p = (WorkflowParam) XmlTools.unMarshal(jo, new WorkflowParam(), text);
            } catch (SAXException ex) {
                throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
            }
            WorkflowService wos = BeanFactory.getWorkflowServiceBean();
            Workflow w = wos.findByID(p.getWorkflow().getWorkflowId());
            w.givesPermission(registration);
            p.setWorkflow(w);
            WorkflowParamService ws = BeanFactory.getWorkflowParamServiceBean();
            Integer id = ws.insert(registration, p);
            WorkflowParam wp = ws.findByID(id);
            Hibernate3DtoCopier copier = new Hibernate3DtoCopier();
            WorkflowParam detachedWP = copier.hibernate2dto(WorkflowParam.class, wp);
            Document line = XmlTools.marshalToDocument(jo, detachedWP);
            getResponse().setEntity(XmlTools.getRepresentation(line));
            getResponse().setLocationRef(getRequest().getRootRef() + "/workflowparams?id=" + detachedWP.getWorkflowParamId());
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

    @Get
    public void getXml() {
        authenticate();
        WorkflowParamService ss = BeanFactory.getWorkflowParamServiceBean();
        Hibernate3DtoCopier copier = new Hibernate3DtoCopier();
        Document line = null;
        if (queryValues.get("id") != null) {
            JaxbObject<WorkflowParam> jaxbTool = new JaxbObject<WorkflowParam>();
            WorkflowParam wp = ((WorkflowParam) testIfNull(ss.findByID(Integer.parseInt(queryValues.get("id")))));
            WorkflowParam dto = copier.hibernate2dto(WorkflowParam.class, wp);
            Workflow w = wp.getWorkflow();
            Workflow detachedW = copier.hibernate2dto(Workflow.class, w);
            dto.setWorkflow(detachedW);
            line = XmlTools.marshalToDocument(jaxbTool, dto);
        } else {
            JaxbObject<WorkflowParam> jaxbTool = new JaxbObject<WorkflowParam>();
            WorkflowParamList list = new WorkflowParamList();
            for (WorkflowParam wp : ss.list()) {
                list.add(copier.hibernate2dto(WorkflowParam.class, wp));
            }
            line = XmlTools.marshalToDocument(jaxbTool, list);
        }
        getResponse().setEntity(XmlTools.getRepresentation(line));
    }
}
