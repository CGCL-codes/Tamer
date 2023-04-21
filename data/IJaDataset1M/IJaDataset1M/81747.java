package com.angel.data.generator.checkers;

import com.angel.data.generator.exceptions.DataGeneratorException;
import com.angel.data.generator.interfaces.DataGenerator;
import com.angel.data.generator.repository.DataGeneratorRepository;

/**
 *
 * @author William
 * @since 11/Ago/2009
 *
 */
public interface DataGeneratorChecker {

    /**
	 *
	 * @param dataGenerator
	 * @param dependencies
	 */
    public void checkDataGenerator(DataGenerator dataGenerator, DataGeneratorRepository dataGeneratorRepository) throws DataGeneratorException;
}
