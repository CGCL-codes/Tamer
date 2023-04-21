package fr.cnes.sitools.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Test;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Preference;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import fr.cnes.sitools.common.SitoolsSettings;
import fr.cnes.sitools.common.model.Response;
import fr.cnes.sitools.server.Consts;
import fr.cnes.sitools.tasks.model.TaskModel;
import fr.cnes.sitools.tasks.model.TaskResourceModel;
import fr.cnes.sitools.tasks.model.TaskStatus;
import fr.cnes.sitools.util.RIAPUtils;

/**
 * Test the Task Resources Synchronously
 * 
 * 
 * @author m.gond
 */
public abstract class AbstractAsynchronousTaskResourceTestCase extends AbstractTaskResourceTestCase {

    /**
   * The if of the dataset
   */
    private static final String PROJECT_ID = "premier";

    /**
   * The url of the dataset
   */
    private static final String PROJECT_URL = "/proj/premier";

    /**
   * The class name of the resourceModel
   */
    private String resourceModelClassName = "fr.cnes.sitools.resources.tasks.test.MyTaskResourceFacadeModel";

    /** user login */
    private String userLogin = "admin";

    /** user password */
    private String password = "admin";

    /**
   * The url attachment for the resource model
   */
    private String urlAttach = "/test";

    /**
   * absolute url for dataset management REST API
   * 
   * @return url
   */
    public final String getBaseProjectUrl() {
        return super.getBaseUrl() + SitoolsSettings.getInstance().getString(Consts.APP_PROJECTS_URL) + "/" + PROJECT_ID;
    }

    /**
   * Test the Resource with a POST call
   * 
   * @throws ClassNotFoundException
   *           if the class cannot be found
   * @throws InstantiationException
   *           if there is an error while instantiating the resource
   * @throws IllegalAccessException
   *           if there is an error while instantiating the resource
   * @throws IOException
   *           if the response cannot be read
   * @throws InterruptedException
   *           if there is an error while waiting for the resource to finish
   */
    @Test
    public void testPost() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, InterruptedException {
        test(Method.POST, "TestPost");
    }

    /**
   * Test the Resource with a PUT call
   * 
   * @throws ClassNotFoundException
   *           if the class cannot be found
   * @throws InstantiationException
   *           if there is an error while instantiating the resource
   * @throws IllegalAccessException
   *           if there is an error while instantiating the resource
   * @throws IOException
   *           if the response cannot be read
   * @throws InterruptedException
   *           if there is an error while waiting for the resource to finish
   */
    @Test
    public void testPut() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, InterruptedException {
        test(Method.PUT, "TestPut");
    }

    /**
   * Test the Resource with a DELETE call
   * 
   * @throws ClassNotFoundException
   *           if the class cannot be found
   * @throws InstantiationException
   *           if there is an error while instantiating the resource
   * @throws IllegalAccessException
   *           if there is an error while instantiating the resource
   * @throws IOException
   *           if the response cannot be read
   * @throws InterruptedException
   *           if there is an error while waiting for the resource to finish
   */
    @Test
    public void testDelete() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, InterruptedException {
        test(Method.DELETE, "TestDelete");
    }

    /**
   * Test the Resource with a PUT call
   * 
   * @throws ClassNotFoundException
   *           if the class cannot be found
   * @throws InstantiationException
   *           if there is an error while instantiating the resource
   * @throws IllegalAccessException
   *           if there is an error while instantiating the resource
   * @throws IOException
   *           if the response cannot be read
   * @throws InterruptedException
   *           if there is an error while waiting for the resource to finish
   */
    @Test
    public void testPutError() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, InterruptedException {
        testWithError(Method.PUT, "TestPut_withError");
    }

    /**
   * Test the Resource with a specified Method call and assert that the result is expected
   * 
   * 
   * @param method
   *          the Method
   * @param expected
   *          the expected String
   * 
   * @throws ClassNotFoundException
   *           if the class cannot be found
   * @throws InstantiationException
   *           if there is an error while instantiating the resource
   * @throws IllegalAccessException
   *           if there is an error while instantiating the resource
   * @throws IOException
   *           if the response cannot be read
   * @throws InterruptedException
   *           if there is an error while waiting for the resource to finish
   */
    public void test(Method method, String expected) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, InterruptedException {
        assertNoneTasks(userLogin, password);
        TaskResourceModel taskResource = createResourceModel(resourceModelClassName, "1000", urlAttach);
        taskResource.getParameterByName("async").setValue("true");
        create(taskResource, getBaseProjectUrl());
        String url = getHostUrl() + PROJECT_URL + urlAttach;
        TaskModel taskModel = callResource(url, method);
        taskModel = testGetAsyncTaskModel(taskModel, userLogin, password);
        assertStatusSuccess(taskModel);
        getResult(taskModel, expected);
        deleteTask(userLogin, taskModel.getId(), password);
        assertNoneTasks(userLogin, password);
        delete(taskResource, getBaseProjectUrl());
    }

    /**
   * Test the Resource with a specified Method call and assert that the result is expected
   * 
   * 
   * @param method
   *          the Method
   * @param expected
   *          the expected String
   * 
   * @throws ClassNotFoundException
   *           if the class cannot be found
   * @throws InstantiationException
   *           if there is an error while instantiating the resource
   * @throws IllegalAccessException
   *           if there is an error while instantiating the resource
   * @throws IOException
   *           if the response cannot be read
   * @throws InterruptedException
   *           if there is an error while waiting for the resource to finish
   */
    public void testWithError(Method method, String errorMessage) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, InterruptedException {
        assertNoneTasks(userLogin, password);
        TaskResourceModel taskResource = createResourceModel(resourceModelClassName, "1000", urlAttach);
        taskResource.getParameterByName("async").setValue("true");
        taskResource.getParameterByName("error").setValue("true");
        taskResource.getParameterByName("error_message").setValue(errorMessage);
        create(taskResource, getBaseProjectUrl());
        String url = getHostUrl() + PROJECT_URL + urlAttach;
        TaskModel taskModel = callResource(url, method);
        taskModel = testGetAsyncTaskModel(taskModel, userLogin, password);
        assertStatusError(taskModel);
        getResultError(taskModel, errorMessage, Status.SERVER_ERROR_INTERNAL);
        deleteTask(userLogin, taskModel.getId(), password);
        assertNoneTasks(userLogin, password);
        delete(taskResource, getBaseProjectUrl());
    }

    /**
   * Call the ressource Asynchronously
   * 
   * @param url
   *          the url
   * @param method
   *          the method to call
   * @return the TaskModel result
   */
    private TaskModel callResource(String url, Method method) {
        url += "?runTypeUserInput=TASK_RUN_ASYNC";
        org.restlet.Response result = null;
        try {
            final Client client = new Client(Protocol.HTTP);
            Request request = new Request(method, url);
            ArrayList<Preference<MediaType>> mediaTypes = new ArrayList<Preference<MediaType>>();
            mediaTypes.add(new Preference<MediaType>(getMediaTest()));
            request.getClientInfo().setAcceptedMediaTypes(mediaTypes);
            if (method.equals(Method.PUT) || method.equals(Method.POST)) {
                request.setEntity(new EmptyRepresentation());
            }
            ChallengeResponse chal = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, userLogin, password);
            request.setChallengeResponse(chal);
            result = client.handle(request);
            assertNotNull(result);
            assertTrue(result.getStatus().isSuccess());
            Representation resultRepr = result.getEntity();
            Response response = getResponse(getMediaTest(), resultRepr, TaskModel.class);
            assertTrue(response.getSuccess());
            return (TaskModel) response.getItem();
        } finally {
            RIAPUtils.exhaust(result);
        }
    }

    /**
   * Assert if the status given is successful
   * 
   * @param status
   *          the status to assert
   */
    private void assertStatusSuccess(TaskModel status) {
        assertNotNull(status.getStatus());
        assertEquals(TaskStatus.TASK_STATUS_FINISHED, status.getStatus());
    }

    /**
   * Assert if the status given is not successfull
   * 
   * @param status
   *          the status to assert
   */
    private void assertStatusError(TaskModel status) {
        assertNotNull(status.getStatus());
        assertEquals(TaskStatus.TASK_STATUS_FAILURE, status.getStatus());
    }

    /**
   * Get the query result and assert that the result is the expectedResult
   * 
   * @param taskModel
   *          taskModel
   * @param expectedResult
   *          the expectedResult
   * @throws IOException
   *           if there is an error while reading the response
   */
    private void getResult(TaskModel taskModel, String expectedResult) throws IOException {
        assertNotNull(taskModel.getUrlResult());
        ClientResource cr = new ClientResource(getHostUrl() + taskModel.getUrlResult());
        ChallengeResponse chal = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, userLogin, password);
        cr.setChallengeResponse(chal);
        Representation result = cr.get();
        assertNotNull(result);
        assertTrue(cr.getStatus().isSuccess());
        assertEquals(expectedResult, result.getText());
        RIAPUtils.exhaust(result);
    }

    /**
   * Get the query result and assert that the result is the expectedResult
   * 
   * @param taskModel
   *          taskModel
   * @param expectedResult
   *          the expectedResult
   * @throws IOException
   *           if there is an error while reading the response
   */
    private void getResultError(TaskModel taskModel, String expectedResult, Status expectedStatus) throws IOException {
        final Client client = new Client(Protocol.HTTP);
        Request request = new Request(Method.GET, getHostUrl() + taskModel.getUrlResult());
        ChallengeResponse chal = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, userLogin, password);
        request.setChallengeResponse(chal);
        org.restlet.Response response = client.handle(request);
        assertNotNull(response);
        assertEquals(expectedStatus, response.getStatus());
        RIAPUtils.exhaust(response);
    }
}
