package com.eris4.benchdb.test.person.operation;

import java.util.Random;
import org.apache.log4j.Logger;
import com.eris4.benchdb.core.Database;
import com.eris4.benchdb.core.NoSuitableDriverException;
import com.eris4.benchdb.core.Operation;
import com.eris4.benchdb.core.OperationException;
import com.eris4.benchdb.core.TestDriverException;
import com.eris4.benchdb.test.person.domain.Person;
import com.eris4.benchdb.test.person.domain.PersonDriver;

public class ReadPersonOperation extends Operation {

    private PersonDriver personDriver;

    private int numberOfObject;

    private Random random = new Random();

    private Logger logger = Logger.getLogger(ReadPersonOperation.class);

    public void setDatabase(Database database) throws NoSuitableDriverException {
        personDriver = (PersonDriver) database.getSpecificDriver(PersonDriver.class);
    }

    public void setUp() throws TestDriverException, OperationException {
        logger.trace("Connection to the driver");
        personDriver.connect();
        numberOfObject = personDriver.getNumberOfPerson();
        if (numberOfObject == 0) throw new OperationException("The number of objects initializated in the database must not be zero");
    }

    public void warmUp() throws OperationException, TestDriverException {
        for (int i = 0; i < 10; i++) {
            doOperation();
        }
    }

    public void doOperation() throws OperationException, TestDriverException {
        int randomId = random.nextInt(numberOfObject);
        Person person = personDriver.read(randomId);
        if (person == null) {
            logger.debug("Person == null for the id = " + randomId);
            throw new OperationException("Null person in ReadPersonOperation: is the database initialized?");
        } else if (person.getId() != randomId) {
            logger.debug("Person with wrong id: " + person.getId() + ". It should be: " + randomId);
            throw new OperationException("Wrong id in person in ReadPersonOperation");
        }
    }

    public void tearDown() throws TestDriverException {
        logger.trace("Disconnection from the driver");
        personDriver.close();
    }
}
