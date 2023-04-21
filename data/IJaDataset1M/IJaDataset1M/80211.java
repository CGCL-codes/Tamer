package org.josef.test.demo.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import java.util.logging.Logger;
import org.josef.demo.jpa.Car;
import org.josef.demo.jpa.CarSearchCriteria;
import org.josef.jpa.EntityDataAccess;
import org.josef.jpa.EntityManagerHelper;
import org.josef.test.jpa.TestJpaUtil;
import org.josef.util.CColor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit test class for class {@link Car} and
 * {@link org.josef.demo.jpa.CarDetail}.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 3080 $
 */
public final class CarTest {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(CarTest.class.getName());

    /**
     * Name of the Car.
     */
    private static final String NAME = "UnitTestCarName";

    /**
     * License plate number of the Car.
     */
    private static final String LICENSE_PLATE_NUMBER = "JA-VA";

    /**
     * License plate number of the Car after update.
     */
    private static final String LICENSE_PLATE_NUMBER_AFTER_UPDATE = "CA-FE";

    /**
     * Cleans the Car from the Database that was created as a result of
     * executing the Unit Tests in this class.
     * <br>Since a test could leave a transaction uncommitted, an active
     * transaction is rolled back first.
     */
    @BeforeClass
    @AfterClass
    public static void cleanUpDatabase() {
        TestJpaUtil.rollbackCurrentTransaction();
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.createQuery("delete from Car c where c.licensePlateNumber ='" + LICENSE_PLATE_NUMBER + "'").executeUpdate();
        EntityManagerHelper.createQuery("delete from Car c where c.licensePlateNumber ='" + LICENSE_PLATE_NUMBER_AFTER_UPDATE + "'").executeUpdate();
        EntityManagerHelper.commit();
        EntityManagerHelper.closeEntityManager();
    }

    /**
     * Tests CRUD actions on an Car.
     * <br>Additionally it tests getting an Car using search criteria.
     */
    @Test
    public void crudCar() {
        final Long primaryKey = createCar();
        final Car readCar = readCarByPrimaryKey(primaryKey);
        assertNotNull("Could not read Car", readCar);
        assertEquals("Incorrect name", NAME, readCar.getName());
        assertEquals("Incorrect license plate number", LICENSE_PLATE_NUMBER, readCar.getLicensePlateNumber());
        readCarByCriteria();
        updateCar(readCar);
        final Car updatedCar = readCarByPrimaryKey(primaryKey);
        assertEquals("Incorrect license plate number", LICENSE_PLATE_NUMBER_AFTER_UPDATE, updatedCar.getLicensePlateNumber());
        deleteCar(readCar);
        final Car deletedCar = readCarByPrimaryKey(primaryKey);
        assertNull("Did not delete the Car", deletedCar);
    }

    /**
     * Creates a Car.
     * @return The primary key of the Car.
     */
    private static Long createCar() {
        EntityManagerHelper.beginTransaction();
        final Car createCar = new Car();
        createCar.setName(NAME);
        createCar.setLicensePlateNumber(LICENSE_PLATE_NUMBER);
        createCar.getCarDetail().setAirco(true);
        createCar.getCarDetail().setColor(CColor.BLACK);
        createCar.getCarDetail().setSeats(2);
        EntityDataAccess.persist(createCar);
        EntityManagerHelper.commit();
        LOGGER.info("Created:" + createCar.toString());
        return createCar.getPrimaryKey();
    }

    /**
     * Read the previously created Car by primary key.
     * @param primaryKey The primary key of the Car to read.
     * @return The Car.
     */
    private Car readCarByPrimaryKey(final Long primaryKey) {
        return EntityDataAccess.findById(Car.class, primaryKey);
    }

    /**
     * Read the previously created Car by criteria.
     * <br>Note: Some criteria exist on {@link Car}, others on
     * {@link org.josef.demo.jpa.CarDetail}.
     */
    private void readCarByCriteria() {
        final CarSearchCriteria searchCriteria = new CarSearchCriteria();
        searchCriteria.setName(NAME);
        searchCriteria.setLicensePlateNumber(LICENSE_PLATE_NUMBER);
        searchCriteria.setAirco(1);
        searchCriteria.setColor(CColor.BLACK);
        searchCriteria.setSeats(2);
        final List<Car> cars = EntityDataAccess.findByCriteria(Car.class, searchCriteria);
        assertEquals("A single Car must match the criteria", 1, cars.size());
    }

    /**
     * Updates the previously created Car.
     * @param car The Car to update.
     */
    private void updateCar(final Car car) {
        EntityManagerHelper.beginTransaction();
        car.setLicensePlateNumber(LICENSE_PLATE_NUMBER_AFTER_UPDATE);
        EntityDataAccess.merge(car);
        EntityManagerHelper.commit();
    }

    /**
     * Deletes the previously created Car.
     * @param car The Car to remove.
     */
    private void deleteCar(final Car car) {
        EntityManagerHelper.beginTransaction();
        EntityDataAccess.remove(Car.class, car.getPrimaryKey());
        EntityManagerHelper.commit();
    }

    /**
     * Tests {@link Car#toString()}.
     */
    @Test
    public void testToString() {
        assertTrue("Should start with class name:", new Car().toString().startsWith(Car.class.getSimpleName() + ":"));
    }
}
