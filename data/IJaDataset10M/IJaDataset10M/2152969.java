package com.angel.data.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Collection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.angel.dao.generic.jdbc.info.JDBCConnectionInfo;
import com.angel.data.generator.base.impl.DataGeneratorRunnerImpl;
import com.angel.data.generator.exceptions.DataGeneratorException;
import com.angel.data.generator.preProcess.impl.JDBCDeletePreProcessStrategy;
import com.angel.mocks.data.generator.dataGenerator.AddressFileDataGenerator;
import com.angel.mocks.data.generator.dataGenerator.AddressImportFileDataGenerator;
import com.angel.mocks.data.generator.dataGenerator.ConfigurationParameterGenerator;
import com.angel.mocks.data.generator.dataGenerator.CountryDataGenerator;
import com.angel.mocks.data.generator.dataGenerator.HotelDataGenerator;
import com.angel.mocks.data.generator.dataGenerator.HotelDescriptionInternationalizableDataGenerator;
import com.angel.mocks.data.generator.dataGenerator.HotelRoomsDataGenerator;
import com.angel.mocks.data.generator.dataGenerator.InvalidUserDataGenerator;
import com.angel.mocks.data.generator.dataGenerator.InverseUserUserTypeDataGenerator;
import com.angel.mocks.data.generator.dataGenerator.LanguajeGenerator;
import com.angel.mocks.data.generator.dataGenerator.LanguajeInternationalizerGenerator;
import com.angel.mocks.data.generator.dataGenerator.RoleDataGenerator;
import com.angel.mocks.data.generator.dataGenerator.RoomDataGenerator;
import com.angel.mocks.data.generator.dataGenerator.UserDataGenerator;
import com.angel.mocks.data.generator.dataGenerator.UserGenerator;
import com.angel.mocks.data.generator.dataGenerator.UserTypeDataGenerator;
import com.angel.mocks.data.generator.factory.TestHibernateDAOFactory;

/**
 * @author William
 *
 */
public class DataGeneratorTest {

    private static final String CONNECTION_URI = "jdbc:mysql://localhost/initializeData";

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    protected DataGeneratorRunnerImpl runner;

    @SuppressWarnings("unchecked")
    @Test
    public void testDataGeneratorValidNew() {
        runner.addDataGeneratorClass(UserTypeDataGenerator.class);
        runner.addDataGeneratorClass(UserDataGenerator.class);
        runner.addDataGeneratorClass(RoleDataGenerator.class);
        runner.preProcess();
        Collection<Object> allDataGenerated = runner.generateData();
        boolean userTypeBeforeUser = runner.areDependenciesBeforeThat(UserDataGenerator.class, new Class[] { UserTypeDataGenerator.class });
        boolean userTypeBeforeRole = runner.areDependenciesBeforeThat(UserDataGenerator.class, new Class[] { RoleDataGenerator.class });
        assertTrue("UserType generator should be before UserData generator", userTypeBeforeUser);
        assertTrue("UserType generator should be before Role generator", userTypeBeforeRole);
        assertNotNull("All collection data generated ust NOT be null.", allDataGenerated);
        assertTrue("User generated must be more than zero.", allDataGenerated.size() > 0);
    }

    @Test
    public void testGenerateDataWithASpecificDataGenerator() {
        runner.addDataGeneratorClass(UserTypeDataGenerator.class);
        runner.addDataGeneratorClass(UserDataGenerator.class);
        runner.addDataGeneratorClass(RoleDataGenerator.class);
        Collection<Object> userDataGenerated = runner.generateDataFor(UserDataGenerator.class);
        assertNotNull("User collection generated must not be null.", userDataGenerated);
        assertTrue("User generated must be more than zero.", userDataGenerated.size() > 0);
    }

    @Test
    public void testInvalidGenerateDataWithAinverseDependence() {
        runner.addDataGeneratorClass(InverseUserUserTypeDataGenerator.class);
        runner.addDataGeneratorClass(InvalidUserDataGenerator.class);
        Collection<Object> userDataGenerated = null;
        try {
            userDataGenerated = runner.generateData();
            fail("Generate all data should be fail because a inverse dependence. Data generated size is: " + userDataGenerated.size());
        } catch (DataGeneratorException e) {
            assertNull("User collection generated must be null.", userDataGenerated);
        }
    }

    @Test
    public void testInvalidGenerateDataWithASpecificDataGenerator() {
        runner.addDataGeneratorClass(UserDataGenerator.class);
        Collection<Object> userDataGenerated = null;
        try {
            userDataGenerated = runner.generateDataFor(UserDataGenerator.class);
            fail("Generate specific data should be fail because it doesn't contains dependence. Data generated size is: " + userDataGenerated.size());
        } catch (DataGeneratorException e) {
            assertNull("User collection generated must be null.", userDataGenerated);
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testInvalidGenerateDataWithASpecificNullDataGenerator() {
        runner.addDataGeneratorClass(UserDataGenerator.class);
        runner.addDataGeneratorClass(UserTypeDataGenerator.class);
        Collection<Object> userDataGenerated = null;
        try {
            boolean userTypeBeforeUser = runner.areDependenciesBeforeThat(UserDataGenerator.class, new Class[] { UserTypeDataGenerator.class });
            assertTrue("UserType generator should be before UserData generator", userTypeBeforeUser);
            userDataGenerated = runner.generateDataFor(null);
            fail("Generate specific data should be fail because it is a null data generator. Data generated size is: " + userDataGenerated.size());
        } catch (DataGeneratorException e) {
            assertNull("User collection generated must be null.", userDataGenerated);
        }
    }

    @Test
    public void testValidGenerateDataWithoutDataGenerators() {
        Collection<Object> dataGenerated = runner.generateData();
        assertNotNull("Data collection generated must NOT be null.", dataGenerated);
        assertTrue("Data collection length generated must be equals zero.", dataGenerated.size() == 0);
    }

    @Test
    public void testValidFileDataGenerator() {
        runner.addDataGeneratorClass(AddressFileDataGenerator.class);
        Collection<Object> dataGenerated = runner.generateData();
        assertNotNull("Data collection generated must NOT be null.", dataGenerated);
        assertEquals("Data collection length generated must be equals 2.", 2, dataGenerated.size());
    }

    @Test
    public void testValidFileDataGeneratorWithIOLibrary() {
        runner.addDataGeneratorClass(AddressFileDataGenerator.class);
        Collection<Object> dataGenerated = runner.generateData();
        assertNotNull("Data collection generated must NOT be null.", dataGenerated);
        assertEquals("Data collection length generated must be equals 2.", 2, dataGenerated.size());
    }

    @Test
    public void testValidImportFileDataGeneratorWithIOLibrary() {
        runner.addDataGeneratorClass(AddressImportFileDataGenerator.class);
        Collection<Object> dataGenerated = runner.generateData();
        assertNotNull("Data collection generated must NOT be null.", dataGenerated);
        assertEquals("Data collection length generated must be equals 4.", 4, dataGenerated.size());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = DataGeneratorException.class)
    public void testValidDataGeneratorValidatingOrder() {
        runner.addDataGeneratorClass(UserGenerator.class);
        runner.addDataGeneratorClass(ConfigurationParameterGenerator.class);
        runner.addDataGeneratorClass(LanguajeGenerator.class);
        runner.addDataGeneratorClass(LanguajeInternationalizerGenerator.class);
        runner.addDataGeneratorClass(RoomDataGenerator.class);
        runner.addDataGeneratorClass(HotelDataGenerator.class);
        runner.addDataGeneratorClass(HotelRoomsDataGenerator.class);
        runner.addDataGeneratorClass(CountryDataGenerator.class);
        runner.addDataGeneratorClass(HotelDescriptionInternationalizableDataGenerator.class);
        boolean hotelDescriptionAfterRoom = runner.areDependenciesBeforeThat(HotelDescriptionInternationalizableDataGenerator.class, new Class[] { RoomDataGenerator.class });
        boolean hotelDescriptionAfterHotel = runner.areDependenciesBeforeThat(HotelDescriptionInternationalizableDataGenerator.class, new Class[] { HotelDataGenerator.class });
        boolean hotelDescriptionAfterRoomAndHotel = runner.areDependenciesBeforeThat(HotelDescriptionInternationalizableDataGenerator.class, new Class[] { RoomDataGenerator.class, HotelDataGenerator.class });
        assertTrue("hotelDescription should be After Hotel", hotelDescriptionAfterHotel);
        assertTrue("hotelDescription should be After Room", hotelDescriptionAfterRoom);
        assertTrue("hotelDescription should be After Room And Hotel", hotelDescriptionAfterRoomAndHotel);
        runner.generateData();
    }

    @Before
    public void before() {
        JDBCConnectionInfo connectionInfo = new JDBCConnectionInfo(JDBC_DRIVER, CONNECTION_URI);
        connectionInfo.setUserName("root");
        connectionInfo.setPassword("root");
        runner = new DataGeneratorRunnerImpl(new TestHibernateDAOFactory(), new JDBCDeletePreProcessStrategy(connectionInfo));
    }

    @After
    public void after() {
        runner.clearDataGenerators();
        runner = null;
    }
}
