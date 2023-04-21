package com.angel.data.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.angel.data.generator.interfaces.DataGenerator;
import com.angel.data.generator.repository.DataGeneratorRepository;
import com.angel.mocks.data.generator.dataGenerator.AddressDataGenerator;
import com.angel.mocks.data.generator.dataGenerator.ConfigurationParameterGenerator;
import com.angel.mocks.data.generator.dataGenerator.CountryDataGenerator;
import com.angel.mocks.data.generator.dataGenerator.HotelDataGenerator;
import com.angel.mocks.data.generator.dataGenerator.HotelDescriptionInternationalizableDataGenerator;
import com.angel.mocks.data.generator.dataGenerator.HotelRoomsDataGenerator;
import com.angel.mocks.data.generator.dataGenerator.LanguajeGenerator;
import com.angel.mocks.data.generator.dataGenerator.LanguajeInternationalizerGenerator;
import com.angel.mocks.data.generator.dataGenerator.RoomDataGenerator;
import com.angel.mocks.data.generator.dataGenerator.UserDataGenerator;
import com.angel.mocks.data.generator.dataGenerator.UserGenerator;
import com.angel.mocks.data.generator.dataGenerator.UserTypeDataGenerator;

/**
 * @author William
 *
 */
public class DataClassGeneratorComparatorTest {

    private DataGeneratorRepository dataGeneratorRepository = new DataGeneratorRepository();

    @Test
    public void testDataClassGeneratorComparatorOrderAddedInInverseWay() {
        dataGeneratorRepository.putDependenciesFor(UserDataGenerator.class);
        dataGeneratorRepository.putDependenciesFor(UserTypeDataGenerator.class);
        DataGenerator[] sortedDataGenerators = dataGeneratorRepository.getSortedDependencies();
        assertNotNull("Sorted data generators must be not null. ", sortedDataGenerators);
        assertEquals("Sorted data generators size must be equals 2. ", 2, sortedDataGenerators.length);
        assertEquals("Data Generator Class at position 0 be equals UserTypeDataGenerator.class. ", UserTypeDataGenerator.class, sortedDataGenerators[0].getClass());
        assertEquals("Data Generator Class at position 1 be equals UserTypeDataGenerator.class. ", UserDataGenerator.class, sortedDataGenerators[1].getClass());
    }

    @Test
    public void testDataClassGeneratorComparatorOrderWithThreeDependences() {
        dataGeneratorRepository.putDependenciesFor(UserTypeDataGenerator.class);
        dataGeneratorRepository.putDependenciesFor(UserDataGenerator.class);
        dataGeneratorRepository.putDependenciesFor(AddressDataGenerator.class);
        DataGenerator[] sortedDataGenerators = dataGeneratorRepository.getSortedDependencies();
        assertNotNull("Sorted data generators must be not null. ", sortedDataGenerators);
        assertEquals("Sorted data generators size must be equals 3. ", 3, sortedDataGenerators.length);
        assertEquals("Data Generator Class at position 0 be equals UserTypeDataGenerator.class. ", UserTypeDataGenerator.class, sortedDataGenerators[0].getClass());
        assertEquals("Data Generator Class at position 0 be equals UserDataGenerator.class. ", UserDataGenerator.class, sortedDataGenerators[1].getClass());
        assertEquals("Data Generator Class at position 0 be equals AddressDataGenerator.class. ", AddressDataGenerator.class, sortedDataGenerators[2].getClass());
    }

    @Test
    public void testDataClassGeneratorComparatorOrder() {
        dataGeneratorRepository.putDependenciesFor(UserTypeDataGenerator.class);
        dataGeneratorRepository.putDependenciesFor(UserDataGenerator.class);
        DataGenerator[] sortedDataGenerators = dataGeneratorRepository.getSortedDependencies();
        assertNotNull("Sorted data generators must be not null. ", sortedDataGenerators);
        assertEquals("Sorted data generators size must be equals 2. ", 2, sortedDataGenerators.length);
        assertEquals("Data Generator Class at position 0 be equals UserTypeDataGenerator.class. ", UserTypeDataGenerator.class, sortedDataGenerators[0].getClass());
        assertEquals("Data Generator Class at position 0 be equals UserTypeDataGenerator.class. ", UserDataGenerator.class, sortedDataGenerators[1].getClass());
    }

    @Test
    public void testDataClassGeneratorComparatorOrderWithDependencies() {
        dataGeneratorRepository.putDependenciesFor(UserGenerator.class);
        dataGeneratorRepository.putDependenciesFor(ConfigurationParameterGenerator.class);
        dataGeneratorRepository.putDependenciesFor(LanguajeGenerator.class);
        dataGeneratorRepository.putDependenciesFor(LanguajeInternationalizerGenerator.class);
        dataGeneratorRepository.putDependenciesFor(RoomDataGenerator.class);
        dataGeneratorRepository.putDependenciesFor(HotelDataGenerator.class);
        dataGeneratorRepository.putDependenciesFor(HotelRoomsDataGenerator.class);
        dataGeneratorRepository.putDependenciesFor(CountryDataGenerator.class);
        dataGeneratorRepository.putDependenciesFor(HotelDescriptionInternationalizableDataGenerator.class);
        DataGenerator[] sortedDataGenerators = dataGeneratorRepository.getSortedDependencies();
        assertNotNull("Sorted data generators must be not null. ", sortedDataGenerators);
        assertEquals("Sorted data generators size must be equals 9. ", 9, sortedDataGenerators.length);
    }

    @Before
    public void beforeTest() {
    }

    @After
    public void afterTest() {
        this.dataGeneratorRepository.clear();
        assertEquals("Data generator repository size should be equals 0. ", new Long(0), this.dataGeneratorRepository.size());
    }
}
