package it.csi.otre.business;

import java.rmi.Remote;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Webservice interface of the core class of the business layer.
 * 
 * Added some webservices annotation to fix BUG-#2021470.
 * 
 * @author oscar ghersi (email: oscar.ghersi@csi.it)
 */
@WebService(targetNamespace = "http://business.otre.csi.it/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface CoreWSInterface extends Remote {

    public static final String NAMESPACE = "http://business.otre.csi.it/";

    public static final String SERVICENAME = "CoreWSInterfaceService";

    public static final String PORTNAME = "CoreBeanPort";

    /**
	 * Executes the login, returning the userId to be used in subsequent call to other methods.
	 * 
	 * @param username Username of the login.
	 * @param password Password related to this username.
	 * @return The userId of the user logged in.
	 * @throws Exception In case of error.
	 */
    @WebMethod
    @WebResult(name = "result")
    public Integer login(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws Exception;

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
    public byte[] getTemplate(@WebParam(name = "userId") Integer userId, @WebParam(name = "templateName") String templateName, @WebParam(name = "templateArea") String templateArea) throws Exception;

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
    public void addTemplate(@WebParam(name = "userId") Integer userId, @WebParam(name = "templateName") String templateName, @WebParam(name = "templateArea") String templateArea, @WebParam(name = "content") byte[] content) throws Exception;

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
	 * 
	 * @param userId User identifier.
	 * @return A list of String that represent the templates.
	 * @throws Exception In case of error.
	 */
    @WebMethod
    @WebResult(name = "result")
    public String[] listTemplatesAsArray(@WebParam(name = "userId") Integer userId) throws Exception;

    /**
	 * Returns a list of formats permitted.
	 *  
	 * @param userId User identifier.
	 * @return A list of Format.
	 * @throws Exception In case of error.
	 */
    @WebMethod
    @WebResult(name = "result")
    public String[] listFormatsAsArray(@WebParam(name = "userId") Integer userId) throws Exception;

    /**
	 * Creates a document.
	 *   
	 * @param userId User that requires the document creation.
	 * @param name Name of the document.
	 * @param template Template to use.
	 * @param XMLData Data to use with the template.
	 * @param format Format of the document in output.
	 * @return The entity created, with the content.
	 * @throws Exception In case of error.
	 */
    @WebMethod
    @WebResult(name = "result")
    public byte[] createDocument(@WebParam(name = "userId") Integer userId, @WebParam(name = "documentName") String documentName, @WebParam(name = "templateName") String templateName, @WebParam(name = "XMLData") String XMLData, @WebParam(name = "format") Integer format) throws Exception;

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
    public void removeTemplate(@WebParam(name = "userId") Integer userId, @WebParam(name = "templateName") String templateName) throws Exception;

    /**
	 * Method to test this layer (via web service).
	 * 
	 * @throws Exception In case of error.
	 */
    @WebMethod
    public void test() throws Exception;
}
