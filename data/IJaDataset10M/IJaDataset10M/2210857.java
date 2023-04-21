package gov.ornl.nice.niceclient.test.niceclienttests;

import static org.junit.Assert.*;
import org.junit.Test;
import gov.ornl.nice.niceclient.eclipseuiwidgets.EclipseExtraInfoWidget;
import gov.ornl.nice.niceclient.iniceclient.uiwidgets.IExtraInfoWidget;
import gov.ornl.nice.niceclient.iniceclient.uiwidgets.IFormWidget;
import gov.ornl.nice.niceclient.internal.niceclient.ItemProcessor;
import gov.ornl.nice.nicecore.iNiCECore.INiCECore;
import gov.ornl.nice.nicedatastructures.form.Form;
import gov.ornl.nice.nicedatastructures.form.FormStatus;

/** 
 * <!-- begin-UML-doc -->
 * <p>This class is responsible for testing the ItemProcessor class.</p>
 * <!-- end-UML-doc -->
 * @author bkj
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ItemProcessorTester {

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>The ItemProcessor to test.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private ItemProcessor itemProcessor;

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation checks the accessor operations of the ItemProcessor class.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    @Test
    public void checkAccessors() {
        itemProcessor = new ItemProcessor();
        IExtraInfoWidget infoWidget = new FakeExtraInfoWidget();
        assertNull(itemProcessor.getInfoWidget());
        itemProcessor.setInfoWidget(infoWidget);
        assertNotNull(itemProcessor.getInfoWidget());
        assertEquals(infoWidget, itemProcessor.getInfoWidget());
        IFormWidget formWidget = new FakeFormWidget();
        assertNull(itemProcessor.getFormWidget());
        itemProcessor.setFormWidget(formWidget);
        assertNotNull(itemProcessor.getFormWidget());
        assertEquals(formWidget, itemProcessor.getFormWidget());
        String actionName = "Fire Phasers!";
        assertNull(itemProcessor.getActionName());
        itemProcessor.setActionName(actionName);
        assertNotNull(itemProcessor.getActionName());
        assertEquals(actionName, itemProcessor.getActionName());
        int id = 9;
        assertEquals(-1, itemProcessor.getItemId());
        itemProcessor.setItemId(id);
        assertTrue(itemProcessor.getItemId() > 0);
        assertEquals(id, itemProcessor.getItemId());
        INiCECore core = new FakeCore();
        assertNull(itemProcessor.getCore());
        itemProcessor.setCore(core);
        assertNotNull(itemProcessor.getCore());
        assertEquals(core, itemProcessor.getCore());
        int pollTime = 5;
        assertNotNull(itemProcessor.getPollTime());
        assertEquals(100, itemProcessor.getPollTime());
        itemProcessor.setPollTime(pollTime);
        assertEquals(pollTime, itemProcessor.getPollTime());
        pollTime = -5;
        itemProcessor.setPollTime(pollTime);
        assertEquals(100, itemProcessor.getPollTime());
        return;
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation checks the ItemProcessor to make sure that it can properly process an Item. It resets the polling time to 50ms and waits for 75ms on the thread to make sure that setting the polling time actually affects the thread.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    @Test
    public void checkProcessing() {
        int itemId = -1;
        FormStatus status = null;
        FakeExtraInfoWidget infoWidget = new FakeExtraInfoWidget();
        IFormWidget formWidget = new FakeFormWidget();
        String actionName = "blend";
        FakeCore core = new FakeCore();
        Thread processThread = null;
        itemProcessor = new ItemProcessor();
        processThread = new Thread(itemProcessor);
        itemId = core.createItem("Red");
        itemProcessor.setFormWidget(formWidget);
        itemProcessor.setInfoWidget(infoWidget);
        itemProcessor.setActionName(actionName);
        itemProcessor.setItemId(itemId);
        itemProcessor.setCore(core);
        itemProcessor.setPollTime(50);
        assertTrue(itemId > 0);
        processThread.start();
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(FormStatus.Processed, core.getLastProcessStatus());
        core.reset();
        actionName = "NeedsInfo";
        itemProcessor = new ItemProcessor();
        processThread = new Thread(itemProcessor);
        itemId = core.createItem("Red");
        itemProcessor.setActionName(actionName);
        itemProcessor.setFormWidget(formWidget);
        itemProcessor.setInfoWidget(infoWidget);
        itemProcessor.setActionName(actionName);
        itemProcessor.setItemId(itemId);
        itemProcessor.setCore(core);
        itemProcessor.setPollTime(50);
        processThread.start();
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        }
        assertTrue(!processThread.isAlive());
        assertEquals(FormStatus.NeedsInfo, core.getLastProcessStatus());
        assertTrue(infoWidget.widgetDisplayed());
        assertTrue(core.itemUpdated());
        return;
    }
}
