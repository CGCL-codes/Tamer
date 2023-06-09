package au.edu.qut.yawl.persistence.dao;

import java.io.IOException;
import java.io.StringReader;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import au.edu.qut.yawl.elements.YSpecification;
import au.edu.qut.yawl.elements.state.YIdentifier;
import au.edu.qut.yawl.engine.domain.YWorkItem;
import au.edu.qut.yawl.exceptions.YPersistenceException;

public class TestYWorkItemHibernateDAO extends AbstractHibernateDAOTestCase {

    YSpecification testSpec;

    public void testDelete() throws YPersistenceException {
        DAO hibernateDAO = getDAO();
        YIdentifier yid = new YIdentifier("abc");
        hibernateDAO.save(yid);
        YWorkItem item = new YWorkItem("testspec", "1", yid, "test_task", true, false);
        hibernateDAO.save(item);
        YWorkItem item2 = (YWorkItem) hibernateDAO.retrieve(YWorkItem.class, hibernateDAO.getKey(item));
        assertNotNull(item2);
        hibernateDAO.delete(item);
        Object key = hibernateDAO.getKey(item);
        Object o = hibernateDAO.retrieve(YWorkItem.class, key);
        assertNull("retrieval should have failed for work item with key " + key, o);
    }

    public void testSaveAndRetrieveNoData() throws YPersistenceException {
        DAO hibernateDAO = getDAO();
        YIdentifier yid = new YIdentifier("abc");
        YWorkItem item = new YWorkItem("testspec", "1", yid, "test_task", true, false);
        hibernateDAO.save(yid);
        hibernateDAO.save(item);
        YWorkItem item2 = (YWorkItem) hibernateDAO.retrieve(YWorkItem.class, hibernateDAO.getKey(item));
        assertNotNull(item2);
        assertTrue("dynamic creation error", item2.allowsDynamicCreation() == true);
        assertTrue("", item2.getCaseID().getId().equals("abc") == true);
        assertTrue("", item2.getCaseID().equals("abc") == true);
        assertTrue("", item2.getTaskID().equals("test_task") == true);
        assertTrue("", item2.getSpecificationID().equals("testspec") == true);
        hibernateDAO.delete(item2);
    }

    public void testSaveAndRetrieveWithData() throws YPersistenceException, JDOMException, IOException {
        DAO hibernateDAO = getDAO();
        YIdentifier yid = new YIdentifier("abc");
        YWorkItem item = new YWorkItem("testspec2", "1", yid, "test_task2", true, false);
        String datastring = "<data><somedata1>XYZ</somedata1>" + "<somedata2>XYZ</somedata2>" + "<somedata3>XYZ</somedata3>" + "<somedata4>XYZ</somedata4>" + "<somedata5>XYZ</somedata5>" + "<somedata6>XYZ</somedata6>" + "<somedata7>XYZ</somedata7>" + "<somedata8>XYZ</somedata8>" + "<somedata9>XYZ</somedata9>" + "<somedata10>XYZ</somedata10>" + "<somedata11>XYZ</somedata11>" + "<somedata12>XYZ</somedata12>" + "<somedata13>XYZ</somedata13>" + "</data>";
        SAXBuilder builder = new SAXBuilder();
        Document d = builder.build(new StringReader(datastring));
        Element e = (Element) d.getRootElement().clone();
        item.setInitData(e);
        hibernateDAO.save(yid);
        hibernateDAO.save(item);
        YWorkItem item2 = (YWorkItem) hibernateDAO.retrieve(YWorkItem.class, hibernateDAO.getKey(item));
        assertNotNull(item2);
        assertTrue("dynamic creation error", item2.allowsDynamicCreation() == true);
        assertTrue("case id error", item2.getCaseID().getId().equals("abc") == true);
        assertTrue("case id -identifier error", item2.getCaseID().equals("abc") == true);
        assertTrue("task id error", item2.getTaskID().equals("test_task2") == true);
        assertTrue("spec id error", item2.getSpecificationID().equals("testspec2") == true);
        assertTrue("Data error", item2.getDataString().replaceAll("[\\r\\n]", "").replaceAll(" ", "").equals(datastring));
        hibernateDAO.delete(item2);
    }
}
