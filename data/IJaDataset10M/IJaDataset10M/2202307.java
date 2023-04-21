package com.angel.data.generator.checkers.impl;

import com.angel.data.generator.checkers.DataGeneratorChecker;
import com.angel.data.generator.exceptions.DataGeneratorCheckerException;
import com.angel.data.generator.exceptions.DataGeneratorException;
import com.angel.data.generator.interfaces.DataGenerator;
import com.angel.data.generator.repository.DataGeneratorRepository;

/**
 * @author William
 *
 */
public class RepeatDataGeneratorDependenceChecker implements DataGeneratorChecker {

    public void checkDataGenerator(DataGenerator dataGenerator, DataGeneratorRepository dataGeneratorRepository) throws DataGeneratorException {
        for (DataGenerator currentDataGenerator : dataGeneratorRepository.getDependencies()) {
            if (currentDataGenerator.getClass().equals(dataGenerator.getClass())) {
                throw new DataGeneratorCheckerException("Data generator class [" + dataGenerator.getClass() + "] contains a data generator dependencies.");
            }
        }
    }
}
