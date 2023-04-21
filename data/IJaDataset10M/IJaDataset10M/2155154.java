package org.jcvi.glk;

import org.hibernate.HibernateException;
import org.hibernate.PropertyValueException;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.GenericJDBCException;
import org.junit.Before;
import org.junit.Test;
import org.jcvi.glk.hsqldb.HsqlDbTest;
import static junit.framework.Assert.*;

public class DbTestExtentType extends HsqlDbTest {

    String name = "SOME_EXTENT_TYPE";

    String description = "description of extent type";

    ExtentType type;

    @Before
    public void setup() {
        type = new ExtentType(name, description);
    }

    @Test
    public void insert() {
        int id = this.saveToDb(type);
        ExtentType actualType = this.getExtentTypeFromDb(id);
        assertEquals(type, actualType);
    }

    @Test
    public void insertNullNameShouldThrowPropertyValueException() {
        Transaction tx = null;
        try {
            tx = beginTransaction();
            getSession().save(new ExtentType(null, "null name"));
            fail("should throw PropertyValueException if same name is null");
        } catch (PropertyValueException expected) {
            rollbackAfterException(tx);
        }
    }

    @Test
    public void insertNullDescriptionShouldThrowPropertyValueException() {
        Transaction tx = null;
        try {
            tx = beginTransaction();
            getSession().save(new ExtentType(name, null));
            fail("should throw PropertyValueException if same description is null");
        } catch (PropertyValueException expected) {
            rollbackAfterException(tx);
        }
    }

    @Test
    public void insert2ExtentTypesWithSameNameShouldThrowConstraintViolationException() {
        this.saveToDb(type);
        ExtentType illegalExtentType = new ExtentType(name, "has the same name");
        Transaction tx = null;
        try {
            tx = beginTransaction();
            getSession().save(illegalExtentType);
            fail("should throw exception if same ExtentType name is added 2x");
        } catch (ConstraintViolationException expected) {
            rollbackAfterException(tx);
        }
    }

    @Test
    public void updateNameToAlreadyExistingNameShouldThrowGenericJDBCException() {
        this.saveToDb(type);
        int id = this.saveToDb(new ExtentType("different name", "different type"));
        Transaction tx = beginTransaction();
        try {
            ExtentType typetoModify = this.getExtentTypeFromDb(id);
            typetoModify.setName(name);
            tx.commit();
            fail("should throw GenericJDBCException if modifying name to pre-existing name");
        } catch (GenericJDBCException t) {
            rollbackAfterException(tx);
        }
    }

    @Test
    public void updateName() throws Throwable {
        int id = this.saveToDb(type);
        Transaction tx = beginTransaction();
        String updatedName = "updated" + name;
        try {
            ExtentType typetoModify = this.getExtentTypeFromDb(id);
            typetoModify.setName(updatedName);
            tx.commit();
            typetoModify = null;
            ExtentType newName = this.getExtentTypeFromDb(id);
            assertEquals(updatedName, newName.getName());
        } catch (Throwable t) {
            rollbackAfterException(tx);
            throw t;
        }
    }

    @Test
    public void updateDescription() throws Throwable {
        int id = this.saveToDb(type);
        Transaction tx = beginTransaction();
        String updatedDescription = "updated" + description;
        try {
            ExtentType typetoModify = this.getExtentTypeFromDb(id);
            typetoModify.setDescription(updatedDescription);
            tx.commit();
            typetoModify = null;
            ExtentType newDescription = this.getExtentTypeFromDb(id);
            assertEquals(updatedDescription, newDescription.getDescription());
        } catch (Throwable t) {
            rollbackAfterException(tx);
            throw t;
        }
    }

    @Test
    public void insertAlreadyExistingDescription() throws Throwable {
        this.saveToDb(type);
        Transaction tx = beginTransaction();
        try {
            int id = this.saveToDb(new ExtentType("same description name", description));
            ExtentType sameDescription = this.getExtentTypeFromDb(id);
            assertEquals(description, sameDescription.getDescription());
        } catch (Throwable t) {
            rollbackAfterException(tx);
            throw t;
        }
    }

    @Test
    public void updateDescriptionToAlreadyExistingDescription() throws Throwable {
        this.saveToDb(type);
        int id = this.saveToDb(new ExtentType("different type", "different" + description));
        Transaction tx = beginTransaction();
        try {
            ExtentType sameDescription = this.getExtentTypeFromDb(id);
            sameDescription.setDescription(description);
            tx.commit();
            sameDescription = null;
            assertEquals(description, this.getExtentTypeFromDb(id).getDescription());
        } catch (Throwable t) {
            rollbackAfterException(tx);
            throw t;
        }
    }

    @Test
    public void updateIdShouldThrowHibernateException() {
        int id = this.saveToDb(type);
        Transaction tx = beginTransaction();
        try {
            ExtentType typetoModify = this.getExtentTypeFromDb(id);
            typetoModify.setId(1234);
            tx.commit();
            fail("should throw HibernateException if modifying id");
        } catch (HibernateException t) {
            rollbackAfterException(tx);
        }
    }
}
