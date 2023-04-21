package cn.myapps.webservice.client;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import junit.framework.TestCase;
import cn.myapps.webservice.fault.DocumentServiceFault;
import cn.myapps.webservice.model.SimpleDocument;

public class DocumentServiceTest extends TestCase {

    DocumentService service;

    protected void setUp() throws Exception {
        super.setUp();
        DocumentServiceServiceLocator locator = new DocumentServiceServiceLocator();
        service = locator.getDocumentService();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCreateDocumentByGuest() {
        String formName = "fm_dayoff_copy27";
        HashMap parameters = new HashMap();
        parameters.put("姓名", "nicholas0001");
        String applicationId = "01b98ff4-8d8c-b3c0-8d30-ece2aa60d534";
    }

    public void testUpdateDocumentByGuest() {
        fail("Not yet implemented");
    }

    public void testCreateDocumentByDomainUser() {
        fail("Not yet implemented");
    }

    public void testUpdateDocumentByDomainUser() {
        fail("Not yet implemented");
    }

    public void testSearchDocumentsByFilter() {
        String formName = "fm_dayoff_copy27";
        HashMap parameters = new HashMap();
        String applicationId = "01b98ff4-8d8c-b3c0-8d30-ece2aa60d534";
        String domainId = "guest";
        try {
            Object[] documents = service.searchDocumentsByFilter(formName, parameters, applicationId);
            for (int i = 0; i < documents.length; i++) {
                SimpleDocument doc = (SimpleDocument) documents[i];
                System.out.println(doc.getItems().get("姓名"));
            }
        } catch (DocumentServiceFault e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (RemoteException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void testSearchDocumentByFilter() {
        fail("Not yet implemented");
    }
}
