package fr.cnes.sitools.ext.test.tasks;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import fr.cnes.sitools.common.SitoolsSettings;
import fr.cnes.sitools.server.Consts;
import fr.cnes.sitools.tasks.model.TaskResourceModel;
import fr.cnes.sitools.util.RIAPUtils;

/**
 * Tests the HTML resource
 * 
 * 
 * @author m.gond
 */
public class HtmlResourceTestCase extends AbstractTaskResourceTestCase {

    /**
   * The if of the dataset
   */
    private static final String DATASET_ID = "bf77955a-2cec-4fc3-b95d-7397025fb299";

    /**
   * The url of the dataset
   */
    private static final String DATASET_URL = "/mondataset";

    /**
   * The class name of the resourceModel
   */
    private String htmlResourceModelClassName = "fr.cnes.sitools.resources.tasks.html.HtmlResourceModel";

    /**
   * The url attachment for the resource model
   */
    private String urlAttach = "/html";

    /**
   * absolute url for dataset management REST API
   * 
   * @return url
   */
    public final String getBaseDatasetUrl() {
        return super.getBaseUrl() + SitoolsSettings.getInstance().getString(Consts.APP_DATASETS_URL) + "/" + DATASET_ID;
    }

    /**
   * Test the HTML Resource
   * 
   * @throws ClassNotFoundException
   *           if the class cannot be found
   * @throws InstantiationException
   *           if there is an error while instantiating the resource
   * @throws IllegalAccessException
   *           if there is an error while instantiating the resource
   * @throws IOException
   *           if the response cannot be read
   */
    @Test
    public void testHtmlResource() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        TaskResourceModel taskResource = createResourceModel(htmlResourceModelClassName, "1000", urlAttach);
        taskResource.getParameterByName("title").setValue("HTML title");
        create(taskResource, getBaseDatasetUrl());
        queryResourceHTML();
        delete(taskResource, getBaseDatasetUrl());
    }

    /**
   * Query the Resoure
   * 
   * @throws IOException
   *           if the response cannot be read
   */
    private void queryResourceHTML() throws IOException {
        String url = getHostUrl() + DATASET_URL + urlAttach + "?start=0&limit=5";
        ClientResource cr = new ClientResource(url);
        Representation result = cr.get(MediaType.TEXT_HTML);
        assertNotNull(result);
        assertTrue(cr.getStatus().isSuccess());
        String html = result.getText();
        System.out.println(html);
        RIAPUtils.exhaust(result);
    }
}
