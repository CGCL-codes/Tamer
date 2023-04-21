package com.teracode.prototipogwt.backend.providers.base;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.teracode.prototipogwt.domain.domainmodel.EntityBase;
import com.teracode.prototipogwt.domain.dto.EntityBaseSet;
import com.teracode.prototipogwt.domain.util.DomainConstants;

/**
 * @author Maxi
 */
public abstract class BaseEntitiesProviderTest<E extends EntityBase, F extends EntityBaseSet<E>> extends BaseProviderTest {

    private static Logger logger = Logger.getLogger(BaseEntitiesProviderTest.class);

    /**
	 * Tests getting an object.
	 */
    @Test
    public void testGetObjectJsonOK() throws Exception {
        testGetObjectOK(MediaType.APPLICATION_JSON_TYPE);
    }

    /**
	 * Tests getting an object.
	 */
    @Test
    public void testGetObjectXmlOK() throws Exception {
        testGetObjectOK(MediaType.APPLICATION_XML_TYPE);
    }

    /**
	 * @param accepts
	 * @throws Exception
	 */
    private void testGetObjectOK(MediaType accepts) throws Exception {
        logger.info("----------------------------------------------");
        logger.info("testGetObjectOK:" + accepts);
        logger.info("----------------------------------------------");
        E entity = getObject(getExistingURL(), false, accepts);
        logger.info(entity);
        verifyEntity(entity, false);
    }

    /**
	 * @param request
	 * @param obj
	 * @param mediaType
	 * @return
	 * @throws Exception
	 */
    public E postAndReleaseConnection(ClientRequest request, Object obj, MediaType mediaType) throws Exception {
        request.body(mediaType, obj);
        ClientResponse<E> response = request.post(getEntityClass());
        E e = response.getEntity();
        response.releaseConnection();
        Assert.assertEquals(Response.Status.fromStatusCode(response.getStatus()), Response.Status.OK);
        return e;
    }

    /**
	 * @param url
	 * @throws Exception
	 * @return The retrieved entity.
	 */
    private E getObject(String url, boolean isNew, MediaType accepts) throws Exception {
        ClientRequest request = getUnsecuredRequestWithToken(url, accepts);
        ClientResponse<E> response = request.get(getEntityClass());
        Assert.assertEquals(Response.Status.fromStatusCode(response.getStatus()), Response.Status.OK);
        E entity = response.getEntity();
        response.releaseConnection();
        return entity;
    }

    /**
	 * @throws Exception
	 */
    @Test
    public void testGetObjectNotFoundJson() throws Exception {
        testGetObjectNotFound(MediaType.APPLICATION_JSON_TYPE);
    }

    /**
	 * @throws Exception
	 */
    @Test
    public void testGetObjectNotFoundXml() throws Exception {
        testGetObjectNotFound(MediaType.APPLICATION_XML_TYPE);
    }

    /**
	 * @param accepts
	 * @throws Exception
	 */
    private void testGetObjectNotFound(MediaType accepts) throws Exception {
        logger.info("----------------------------------------------");
        logger.info("testGetObjectNotFound:" + accepts);
        logger.info("----------------------------------------------");
        ClientRequest request = getUnsecuredRequestWithToken(getNotExistingURL(), accepts);
        ClientResponse<E> response = request.get(getEntityClass());
        Assert.assertEquals(Response.Status.fromStatusCode(response.getStatus()), Response.Status.NOT_FOUND);
        response.releaseConnection();
    }

    /**
	 * @throws Exception
	 */
    @Test
    public void testGetListJson() throws Exception {
        testGetList(MediaType.APPLICATION_JSON_TYPE);
    }

    /**
	 * @throws Exception
	 */
    @Test
    public void testGetListXml() throws Exception {
        testGetList(MediaType.APPLICATION_XML_TYPE);
    }

    /**
	 * @param accepts
	 * @throws Exception
	 */
    private void testGetList(MediaType accepts) throws Exception {
        logger.info("----------------------------------------------");
        logger.info("testGetList:" + accepts);
        logger.info("----------------------------------------------");
        ClientRequest request = getUnsecuredRequestWithToken(getListURL(), accepts);
        ClientResponse<F> response = request.get(getListClass());
        F f = response.getEntity();
        verifyList(f);
        Assert.assertEquals(Response.Status.fromStatusCode(response.getStatus()), Response.Status.OK);
        response.releaseConnection();
    }

    /**
	 * @throws Exception
	 */
    @Test
    public void testSaveURLJson() throws Exception {
        testSaveURL(MediaType.APPLICATION_JSON_TYPE);
    }

    /**
	 * @throws Exception
	 */
    @Test
    public void testSaveURLXml() throws Exception {
        testSaveURL(MediaType.APPLICATION_XML_TYPE);
    }

    /**
	 * @throws Exception
	 */
    private void testSaveURL(MediaType accepts) throws Exception {
        logger.info("----------------------------------------------");
        logger.info("testSaveURL:" + accepts);
        logger.info("----------------------------------------------");
        ClientRequest request = getUnsecuredRequestWithToken(getListURL(), accepts);
        E e = postAndReleaseConnection(request, getEntityToSave(), accepts);
        Assert.assertEquals(e.getId().toString(), DomainConstants.FIRST_AVAILABLE_ID);
    }

    /**
	 * Deletes an object.
	 */
    @Test
    public void testDeleteObjectOKJson() throws Exception {
        testDeleteObjectOK(MediaType.APPLICATION_JSON_TYPE);
    }

    /**
	 * Deletes an object.
	 */
    @Test
    public void testDeleteObjectOKXml() throws Exception {
        testDeleteObjectOK(MediaType.APPLICATION_XML_TYPE);
    }

    /**
	 * @param accepts
	 * @throws Exception
	 */
    private void testDeleteObjectOK(MediaType accepts) throws Exception {
        logger.info("----------------------------------------------");
        logger.info("testDeleteObjectOK:" + accepts);
        logger.info("----------------------------------------------");
        String url = getDeleteableURL();
        ClientRequest request = getUnsecuredRequestWithToken(url, accepts);
        ClientResponse<E> response = request.delete(getEntityClass());
        Assert.assertEquals(Response.Status.fromStatusCode(response.getStatus()), Response.Status.OK);
        response.releaseConnection();
    }

    /**
	 * Tries to delete a non-existing object.
	 */
    @Test
    public void testDeleteObjectNotFoundJson() throws Exception {
        testDeleteObjectNotFound(MediaType.APPLICATION_JSON_TYPE);
    }

    /**
	 * Tries to delete a non-existing object.
	 */
    @Test
    public void testDeleteObjectNotFoundXml() throws Exception {
        testDeleteObjectNotFound(MediaType.APPLICATION_XML_TYPE);
    }

    /**
	 * @param accepts
	 * @throws Exception
	 */
    private void testDeleteObjectNotFound(MediaType accepts) throws Exception {
        logger.info("----------------------------------------------");
        logger.info("testDeleteObjectNotFound:" + accepts);
        logger.info("----------------------------------------------");
        String url = getNotExistingURL();
        ClientRequest request = getUnsecuredRequestWithToken(url, accepts);
        ClientResponse<E> response = request.delete(getEntityClass());
        Assert.assertEquals(Response.Status.fromStatusCode(response.getStatus()), Response.Status.NOT_FOUND);
        response.releaseConnection();
    }

    /**
	 * Updates an object.
	 */
    @Test
    public void testUpdateObjectOKJson() throws Exception {
        testUpdateObjectOK(MediaType.APPLICATION_JSON_TYPE);
    }

    /**
	 * Updates an object.
	 */
    @Test
    public void testUpdateObjectOKXml() throws Exception {
        testUpdateObjectOK(MediaType.APPLICATION_XML_TYPE);
    }

    /**
	 * @param accepts
	 * @throws Exception
	 */
    private void testUpdateObjectOK(MediaType accepts) throws Exception {
        logger.info("----------------------------------------------");
        logger.info("testUpdateObjectOK:" + accepts);
        logger.info("----------------------------------------------");
        String url = getUpdateableURL();
        E entity = getObject(url, false, accepts);
        changeEntityToUpdate(entity);
        ClientRequest request = getUnsecuredRequestWithToken(url, accepts);
        request.body(accepts, entity);
        ClientResponse<E> response = request.put(getEntityClass());
        Assert.assertEquals(Response.Status.fromStatusCode(response.getStatus()), Response.Status.OK);
        response.releaseConnection();
        E modified = getObject(url, false, accepts);
        logger.info(modified);
        verifyModifiedEntity(modified);
    }

    /**
	 * Tries to update a non-existent object.
	 */
    @Test
    public void testUpdateObjectNotFoundJson() throws Exception {
        testUpdateObjectNotFound(MediaType.APPLICATION_JSON_TYPE);
    }

    /**
	 * Tries to update a non-existent object.
	 */
    @Test
    public void testUpdateObjectNotFoundXml() throws Exception {
        testUpdateObjectNotFound(MediaType.APPLICATION_XML_TYPE);
    }

    /**
	 * @param accepts
	 * @throws Exception
	 */
    private void testUpdateObjectNotFound(MediaType accepts) throws Exception {
        logger.info("----------------------------------------------");
        logger.info("testUpdateObjectNotFound:" + accepts);
        logger.info("----------------------------------------------");
        String url = getNotExistingURL();
        E entity = getEntityToSave();
        entity.setId(getNonexistentEntityId());
        ClientRequest request = getUnsecuredRequestWithToken(url, accepts);
        request.body(accepts, entity);
        ClientResponse<E> response = request.put(getEntityClass());
        Assert.assertEquals(Response.Status.fromStatusCode(response.getStatus()), Response.Status.NOT_FOUND);
        response.releaseConnection();
    }

    /**
	 * Tries to update an object by specifying an incorrect id.
	 */
    @Test
    public void testUpdateObjectWrongIdJson() throws Exception {
        testUpdateObjectWrongId(MediaType.APPLICATION_JSON_TYPE);
    }

    /**
	 * Tries to update an object by specifying an incorrect id.
	 */
    @Test
    public void testUpdateObjectWrongIdXml() throws Exception {
        testUpdateObjectWrongId(MediaType.APPLICATION_XML_TYPE);
    }

    /**
	 * @param accepts
	 * @throws Exception
	 */
    private void testUpdateObjectWrongId(MediaType accepts) throws Exception {
        logger.info("----------------------------------------------");
        logger.info("testUpdateObjectWrongId:" + accepts);
        logger.info("----------------------------------------------");
        String url = getNotExistingURL();
        E entity = getEntityToSave();
        entity.setId(null);
        ClientRequest request = getUnsecuredRequestWithToken(url, accepts);
        request.body(accepts, entity);
        ClientResponse<E> response = request.put(getEntityClass());
        Assert.assertEquals(Response.Status.fromStatusCode(response.getStatus()), Response.Status.BAD_REQUEST);
        response.releaseConnection();
    }

    /**
	 * The URL of an existing resource we can retrieve (and will NOT change or delete).
	 */
    protected abstract String getExistingURL();

    /**
	 * The URL of a non-existing resource.
	 */
    protected abstract String getNotExistingURL();

    /**
	 * The Id of an entity which doesn't exist.
	 */
    protected abstract long getNonexistentEntityId();

    /**
	 * The URL of an existing resource we can delete.
	 */
    protected abstract String getDeleteableURL();

    /**
	 * The URL of an existing resource we can update.
	 */
    protected abstract String getUpdateableURL();

    protected abstract String getListURL();

    protected abstract void verifyEntity(E e, boolean isNew);

    /**
	 * Verify an updated entity. 
	 */
    protected abstract void verifyModifiedEntity(E entity);

    protected abstract void verifyList(F f);

    protected abstract Class<E> getEntityClass();

    protected abstract Class<F> getListClass();

    protected abstract E getEntityToSave() throws Exception;

    /**
	 * Make a change to an entity in order to try updating it.
	 */
    protected abstract void changeEntityToUpdate(E entity);
}
