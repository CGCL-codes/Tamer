package ast.common;

import ast.ASTTestSuite;
import ast.common.error.DispatchError;
import ast.common.error.PropertyError;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link ast.common.PropertyHandler}.
 *
 * @author maermler
 */
public class PropertyHandlerTest {

    /**
     * Instance of {@link ast.common.PropertyHandler} for testing.
     */
    PropertyHandler propertyHandler;

    /**
     * Controller for testing
     */
    DummyXControllerData dummyXControllerData;

    /**
     * Data for the controller
     */
    DummyXControllerDataClass dummyXControllerDataClass1;

    /**
     * More data for the controller
     */
    DummyXControllerDataClass dummyXControllerDataClass2;

    /**
     * Data list which contains the data of the controller
     */
    ArrayList<DummyXControllerDataClass> dataList;

    /**
     * To save the data which we get by using the method
     * getControllerData.
     *
     */
    ArrayList<DummyXControllerDataClass> saveInDataList;

    /**
     * Empty constructor.
     */
    public PropertyHandlerTest() {
    }

    /**
     * {@inheritDoc}
     *
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUp() {
        this.propertyHandler = new PropertyHandler(ASTTestSuite.docController);
        this.dummyXControllerData = new DummyXControllerData(ASTTestSuite.docController);
        this.dummyXControllerDataClass1 = new DummyXControllerDataClass(0, "blub", true);
        this.dataList = new ArrayList<DummyXControllerDataClass>();
        this.dataList.add(dummyXControllerDataClass1);
        this.dummyXControllerDataClass2 = new DummyXControllerDataClass(1, "fasel", false);
        this.dataList.add(dummyXControllerDataClass2);
    }

    /**
     * Test whether the Flag isASTDocument is set or not.
     *
     * @throws ast.common.error.PropertyError
     */
    @Test
    public void testSetIsASTDocument() throws PropertyError {
        this.propertyHandler.setASTDocumentFlag();
        Assert.assertEquals(this.propertyHandler.isASTDocument(), true);
    }

    /**
     * Test wheter get- and setControllerData of the PropertyHandler-Class
     * works or not. The dummyXControllerData is a Controller and the
     * dataList is the data, which is saved in the Controller.
     *
     * @throws ast.common.error.PropertyError
     * @throws ast.common.error.DispatchError
     */
    @Test
    public void testSetGetControllerData() throws PropertyError, DispatchError {
        this.propertyHandler.setControllerData(this.dummyXControllerData, "dummyXControllerDataClassList", this.dataList);
        this.saveInDataList = (ArrayList<DummyXControllerDataClass>) this.propertyHandler.getControllerData(this.dummyXControllerData, "dummyXControllerDataClassList", new ArrayList<DummyXControllerDataClass>(), new DummyXControllerDataClass(0, null, false));
        Assert.assertNotNull(this.saveInDataList);
        Assert.assertFalse(this.saveInDataList.isEmpty());
        Assert.assertEquals(this.dataList.get(0).getNumber(), this.saveInDataList.get(0).getNumber());
        Assert.assertEquals(this.dataList.get(0).getString(), this.saveInDataList.get(0).getString());
        Assert.assertEquals(this.dataList.get(0).isBool(), this.saveInDataList.get(0).isBool());
        Assert.assertEquals(this.dataList.get(1).getNumber(), this.saveInDataList.get(1).getNumber());
        Assert.assertEquals(this.dataList.get(1).getString(), this.saveInDataList.get(1).getString());
        Assert.assertEquals(this.dataList.get(1).isBool(), this.saveInDataList.get(1).isBool());
    }
}
