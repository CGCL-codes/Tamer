package it.csi.otre.business;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import it.csi.otre.integration.CoreDAOLocal;
import it.csi.otre.integration.DocumentArchiverLocal;
import it.csi.otre.integration.DocumentCreatorLocal;
import it.csi.otre.mapping.Area;
import it.csi.otre.mapping.Format;
import it.csi.otre.mapping.Template;
import it.csi.otre.mapping.User;
import it.csi.otre.util.ODocument;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

/**
 * Core class of the business layer.
 * 
 * Implements the methods deducted from use cases.
 * 
 * Added some webservices annotation to fix BUG-#2021470.
 * 
 * @author oscar ghersi (email: oscar.ghersi@csi.it)
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
@WebService(endpointInterface = "it.csi.otre.business.CoreWSInterface", serviceName = "CoreWSInterfaceService", targetNamespace = "http://business.otre.csi.it/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class CoreBean implements CoreRemote, CoreLocal, CoreWSInterface {

    private static final Logger log = Logger.getLogger(CoreBean.class);

    @EJB(mappedName = CoreDAOLocal.BEAN_NAME)
    private CoreDAOLocal coreDAO;

    @EJB(mappedName = DocumentArchiverLocal.BEAN_NAME)
    private DocumentArchiverLocal docArchiver;

    @EJB(mappedName = DocumentCreatorLocal.BEAN_NAME)
    private DocumentCreatorLocal docCreator;

    /**
	 * Executes the login, returning the userId to be used in subsequent call to methods that requires it.
	 * 
	 * @param username Username.
	 * @param password Password.
	 * @return The userId of the user logged in.
	 * @throws Exception In case of error.
	 */
    @WebMethod
    @WebResult(name = "result")
    public Integer login(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws Exception {
        Integer ret = null;
        try {
            User u = coreDAO.findUserByUsernamePassword(username, password);
            ret = u.getId();
        } catch (Exception e) {
            log.error("login(" + username + ",**********) - Failed to login user: " + e);
            throw e;
        }
        return ret;
    }

    /**
	 * Returns a template [added for FR-#2021415].
	 * 
	 * @param userId User identifier.
	 * @param templateName Name of the template to add.
	 * @param templateArea Name of the area which the template belongs to.
	 * @return the content of the template.
	 * @throws Exception In case of error.
	 */
    @WebMethod
    @WebResult(name = "result")
    public byte[] getTemplate(@WebParam(name = "userId") Integer userId, @WebParam(name = "templateName") String templateName, @WebParam(name = "templateArea") String templateArea) throws Exception {
        byte[] ret = null;
        try {
            User user = coreDAO.getUser(userId);
            Template template = coreDAO.findTemplateByName(templateName);
            if (!user.administrator() && !template.getArea().equals(user.getArea())) throw new Exception("User not allowed to manage templates of this area");
            ret = docCreator.getTemplate(template);
        } catch (Exception e) {
            log.error("listTemplates(" + userId + ") - Failed to get template list: " + e);
            throw e;
        }
        return ret;
    }

    /**
	 * Adds a template.
	 * 
	 * This method verifies if the user belongs to the area specified by the argument.
	 * 
	 * @param userId User identifier.
	 * @param templateName Name of the template to add.
	 * @param templateArea Name of the area which the template belongs to.
	 * @param content Content of the template.
	 * @throws Exception In case of error.
	 */
    @WebMethod
    public void addTemplate(@WebParam(name = "userId") Integer userId, @WebParam(name = "templateName") String templateName, @WebParam(name = "templateArea") String templateArea, @WebParam(name = "content") byte[] content) throws Exception {
        try {
            User user = coreDAO.getUser(userId);
            Area area = coreDAO.findAreaByName(templateArea);
            if (!user.administrator() && !area.equals(user.getArea())) throw new Exception("User not allowed to manage templates of this area");
            Template t = null;
            try {
                t = coreDAO.findTemplateByName(templateName);
            } catch (Exception e) {
                t = null;
            }
            if (t != null && t.getArea().equals(area)) {
                docCreator.removeTemplate(t);
                docArchiver.removeTemplate(t);
                coreDAO.deleteTemplate(t);
            }
            Template template = new Template(null, templateName, true, content, area);
            docCreator.addTemplate(template);
            docArchiver.addTemplate(template);
            coreDAO.addTemplate(template);
        } catch (Exception e) {
            log.error("addTemplate(" + userId + "," + templateName + "," + templateArea + "," + content + ") - Failed to add template: " + e);
            throw e;
        }
    }

    /**
	 * Returns a list of all the templates as one or more String of the form:
	 * 
	 *  "[templateId];[templateName];[areaName]"
	 *  
	 * The template id can be used in subsequent calls of method that needs it, like, for example, the
	 * removeTemplate() method.
	 * The templates returned are only those related to the area of the user; if it belongs to the special
	 * area of administrators, the method returns all the templates.
	 * The userId is the identifier returned from the call to the login() method.
	 * This method is for the Java interface only.
	 * 
	 * @param userId User identifier.
	 * @return A list of String that represent the templates.
	 * @throws Exception In case of error.
	 */
    public List listTemplates(Integer userId) throws Exception {
        List<String> ret = new ArrayList<String>();
        try {
            User user = coreDAO.getUser(userId);
            Set lista = null;
            if (!user.administrator()) lista = coreDAO.listTemplatesByArea(user.getArea()); else lista = coreDAO.listTemplates();
            for (Iterator iterator = lista.iterator(); iterator.hasNext(); ) {
                Template template = (Template) iterator.next();
                ret.add(template.getId() + ";" + template.getName() + ";" + template.getArea().getName());
            }
        } catch (Exception e) {
            log.error("listTemplates(" + userId + ") - Failed to get template list: " + e);
            throw e;
        }
        return ret;
    }

    /**
	 * Returns a list of all the templates as one or more String of the form:
	 * 
	 *  "[templateId];[templateName];[areaName]"
	 *  
	 * The template id can be used in subsequent calls of method that needs it, like, for example, the
	 * removeTemplate() method.
	 * The templates returned are only those related to the area of the user; if it belongs to the special
	 * area of administrators, the method returns all the templates.
	 * The userId is the identifier returned from the call to the login() method.
	 * This method is for the WebServices interface only.
	 * 
	 * @param userId User identifier.
	 * @return A list of String that represent the templates.
	 * @throws Exception In case of error.
	 */
    @WebMethod
    @WebResult(name = "result")
    public String[] listTemplatesAsArray(@WebParam(name = "userId") Integer userId) throws Exception {
        String[] ret = null;
        try {
            Object[] oArray = listTemplates(userId).toArray();
            ret = new String[oArray.length];
            for (int i = 0; i < oArray.length; i++) {
                ret[i] = (String) oArray[i];
            }
        } catch (Exception e) {
            log.error("listTemplates(" + userId + ") - Failed to get template list: " + e);
            throw e;
        }
        return ret;
    }

    /**
	 * Returns a list of formats permitted.
	 * This method is for the Java interface only.
	 *  
	 * @param userId User identifier.
	 * @return A list of Format.
	 * @throws Exception In case of error.
	 */
    public List listFormats(Integer userId) throws Exception {
        List<Format> ret = new ArrayList<Format>();
        try {
            Set list = coreDAO.listFormats();
            for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
                Format format = (Format) iterator.next();
                ret.add(format);
            }
        } catch (Exception e) {
            log.error("listFormats() - Failed to get format list: " + e);
            throw e;
        }
        return ret;
    }

    /**
	 * Returns a list of formats permitted.
	 * This method is for the WebServices interface only.
	 *  
	 * @param userId User identifier.
	 * @return A list of Format.
	 * @throws Exception In case of error.
	 */
    @WebMethod
    @WebResult(name = "result")
    public String[] listFormatsAsArray(@WebParam(name = "userId") Integer userId) throws Exception {
        String[] ret = null;
        try {
            List l = listFormats(userId);
            ret = new String[l.size()];
            int k = 0;
            for (Iterator iterator = l.iterator(); iterator.hasNext(); ) {
                Format f = (Format) iterator.next();
                ret[k++] = f.getExtension();
            }
        } catch (Exception e) {
            log.error("listFormats() - Failed to get format list: " + e);
            throw e;
        }
        return ret;
    }

    /**
	 * Creates a document.
	 *   
	 * @param userId User that requires the document creation.
	 * @param documentName Name of the document.
	 * @param templateName Name of the template to use.
	 * @param XMLData Data to use with the template.
	 * @param format Format of the document in output.
	 * @return The entity created, with the content.
	 * @throws Exception In case of error.
	 */
    @WebMethod
    @WebResult(name = "result")
    public byte[] createDocument(@WebParam(name = "userId") Integer userId, @WebParam(name = "documentName") String documentName, @WebParam(name = "templateName") String templateName, @WebParam(name = "XMLData") String XMLData, @WebParam(name = "format") Integer format) throws Exception {
        byte[] ret = null;
        try {
            ODocument doc = new ODocument();
            Template t = coreDAO.findTemplateByName(templateName);
            User u = coreDAO.getUser(userId);
            if (u.administrator() || u.getArea().equals(t.getArea())) {
                doc.setTemplate(t);
                doc.setArea(t.getArea());
            } else throw new Exception("User is not allowed to use this template");
            SAXReader reader = new SAXReader();
            Document data = reader.read(new StringReader(XMLData));
            Format f = coreDAO.findFormatByCode(format);
            doc.setFormat(f);
            doc.setName(documentName + "." + f.getExtension());
            byte[] b = docCreator.create(documentName, t.getArea().getPath() + File.separator + templateName, data, f.getExtension());
            doc.setContent(b);
            if (docArchiver.isActive()) doc = docArchiver.addDocument(doc);
            ret = doc.getContent();
        } catch (Exception e) {
            log.error("createDocument() - Failed to create document: " + e);
            throw e;
        }
        return ret;
    }

    /**
	 * Removes a template.
	 * 
	 * This method verifies if the user belongs to the area related to the template specified.
	 * 
	 * @param userId User identifier.
	 * @param templateName Name of the template to remove.
	 * @throws Exception In case of error.
	 */
    @WebMethod
    public void removeTemplate(@WebParam(name = "userId") Integer userId, @WebParam(name = "templateName") String templateName) throws Exception {
        try {
            User user = coreDAO.getUser(userId);
            Template template = coreDAO.findTemplateByName(templateName);
            if (!user.administrator() && !template.getArea().equals(user.getArea())) throw new Exception("User not allowed to manage templates of the area");
            docCreator.removeTemplate(template);
            docArchiver.removeTemplate(template);
            coreDAO.deleteTemplate(template);
        } catch (Exception e) {
            log.error("removeTemplate(" + userId + "," + templateName + ") - Failed to remove template: " + e);
            throw e;
        }
    }

    /**
	 * Method to test this layer.
	 * This method is for the Java interface only.
	 * 
	 * @throws Exception In case of error.
	 */
    @WebMethod
    public void test() throws Exception {
        try {
            coreDAO.test();
            docArchiver.test();
            docCreator.test();
            log.info("test() - Business layer test OK");
        } catch (Exception e) {
            log.error("test() - Failed testing layer: " + e);
            throw e;
        }
    }
}
