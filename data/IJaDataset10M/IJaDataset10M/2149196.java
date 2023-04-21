package au.edu.qut.yawl.engine;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import org.hibernate.HibernateException;
import org.jdom.JDOMException;
import au.edu.qut.yawl.elements.YSpecification;
import au.edu.qut.yawl.elements.state.YIdentifier;
import au.edu.qut.yawl.engine.domain.YWorkItem;
import au.edu.qut.yawl.exceptions.YDataStateException;
import au.edu.qut.yawl.exceptions.YDataValidationException;
import au.edu.qut.yawl.exceptions.YPersistenceException;
import au.edu.qut.yawl.exceptions.YQueryException;
import au.edu.qut.yawl.exceptions.YSchemaBuildingException;
import au.edu.qut.yawl.exceptions.YStateException;
import au.edu.qut.yawl.exceptions.YSyntaxException;
import au.edu.qut.yawl.persistence.AbstractTransactionalTestCase;
import au.edu.qut.yawl.unmarshal.YMarshal;

public class TestEnginePersistence extends AbstractTransactionalTestCase {

    private YSpecification _specification;

    private AbstractEngine _engine;

    private MessageFormat format = new MessageFormat("<Funky>" + "<FunkyName>Funky</FunkyName>" + "<FunkyNumber>{0}</FunkyNumber>" + "</Funky>");

    public void setUp() throws Exception {
        super.setUp();
        _engine = EngineFactory.createEngine();
        URL fileURL = getClass().getResource("TestFunkyStartParams.xml");
        File yawlXMLFile = new File(fileURL.getFile());
        _specification = (YSpecification) YMarshal.unmarshalSpecifications(yawlXMLFile.getAbsolutePath()).get(0);
        _engine.addSpecifications(yawlXMLFile, false, new ArrayList());
    }

    /**
     * Tests the launching of a case containing an XML failure.
     * The xml failure throws an exception, but the
     * hib transaction isn't rolled-back/aborted - causing the next persistent
     * call on the engine to deadlock.
     */
    public void testLaunchCasePersistence() throws YPersistenceException, YSchemaBuildingException, YDataStateException, YStateException, InterruptedException {
        try {
            _engine.launchCase(null, _specification.getID(), format.format(new String[] { "beef" }), null);
        } catch (YDataValidationException e) {
            Thread nextEngineCall = new Thread(new Runnable() {

                public void run() {
                    try {
                        _engine.launchCase(null, _specification.getID(), format.format(new String[] { "beef" }), null);
                    } catch (Exception e) {
                    }
                }
            });
            nextEngineCall.start();
            Thread.sleep(5000);
            if (nextEngineCall.isAlive()) {
                fail("Engine deadlocked due to failure to abort " + "a hibernate transaction.");
            }
        }
    }

    /**
     * Tests this scenario
     * 1. Case is launched
     * 2. workitem checked out
     * 3. case cancelled
     * 4. engine DB is checked for absence of left over work items
     */
    public void testPersistenceWithCaseCancellation() throws YSchemaBuildingException, YDataStateException, YPersistenceException, YStateException, YQueryException, YSyntaxException, IOException, JDOMException, HibernateException {
        String caseID = _engine.launchCase(null, _specification.getID(), format.format(new String[] { "1" }), null);
        YIdentifier id = _engine.getCaseID(caseID);
        YWorkItem enabledItem = (YWorkItem) _engine.getAvailableWorkItems().iterator().next();
        _engine.startWorkItem(enabledItem.getIDString(), null);
        _engine.cancelCase(id);
    }
}
