package com.angel.data.generator.base.impl;

import com.angel.dao.generic.factory.DAOFactory;
import com.angel.data.generator.repository.AnnotationDataGeneratorRepository;

/**
 * @author William
 *
 */
public class DataGeneratorAnnotationRunner extends DataGeneratorRunnerImpl {

    public DataGeneratorAnnotationRunner() {
        super();
        super.setDataGeneratorRepository(new AnnotationDataGeneratorRepository());
    }

    public DataGeneratorAnnotationRunner(DAOFactory daoFactory) {
        super(daoFactory);
        super.setDataGeneratorRepository(new AnnotationDataGeneratorRepository());
    }
}
