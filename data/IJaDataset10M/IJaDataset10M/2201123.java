package com.liferay.portal.service.persistence;

import com.liferay.portal.NoSuchClassNameException;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.model.ClassName;
import com.liferay.portal.service.persistence.BasePersistenceTestCase;

/**
 * <a href="ClassNamePersistenceTest.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ClassNamePersistenceTest extends BasePersistenceTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        _persistence = (ClassNamePersistence) PortalBeanLocatorUtil.locate(ClassNamePersistence.class.getName() + ".impl");
    }

    public void testCreate() throws Exception {
        long pk = nextLong();
        ClassName className = _persistence.create(pk);
        assertNotNull(className);
        assertEquals(className.getPrimaryKey(), pk);
    }

    public void testRemove() throws Exception {
        ClassName newClassName = addClassName();
        _persistence.remove(newClassName);
        ClassName existingClassName = _persistence.fetchByPrimaryKey(newClassName.getPrimaryKey());
        assertNull(existingClassName);
    }

    public void testUpdateNew() throws Exception {
        addClassName();
    }

    public void testUpdateExisting() throws Exception {
        long pk = nextLong();
        ClassName newClassName = _persistence.create(pk);
        newClassName.setValue(randomString());
        _persistence.update(newClassName, false);
        ClassName existingClassName = _persistence.findByPrimaryKey(newClassName.getPrimaryKey());
        assertEquals(existingClassName.getClassNameId(), newClassName.getClassNameId());
        assertEquals(existingClassName.getValue(), newClassName.getValue());
    }

    public void testFindByPrimaryKeyExisting() throws Exception {
        ClassName newClassName = addClassName();
        ClassName existingClassName = _persistence.findByPrimaryKey(newClassName.getPrimaryKey());
        assertEquals(existingClassName, newClassName);
    }

    public void testFindByPrimaryKeyMissing() throws Exception {
        long pk = nextLong();
        try {
            _persistence.findByPrimaryKey(pk);
            fail("Missing entity did not throw NoSuchClassNameException");
        } catch (NoSuchClassNameException nsee) {
        }
    }

    public void testFetchByPrimaryKeyExisting() throws Exception {
        ClassName newClassName = addClassName();
        ClassName existingClassName = _persistence.fetchByPrimaryKey(newClassName.getPrimaryKey());
        assertEquals(existingClassName, newClassName);
    }

    public void testFetchByPrimaryKeyMissing() throws Exception {
        long pk = nextLong();
        ClassName missingClassName = _persistence.fetchByPrimaryKey(pk);
        assertNull(missingClassName);
    }

    protected ClassName addClassName() throws Exception {
        long pk = nextLong();
        ClassName className = _persistence.create(pk);
        className.setValue(randomString());
        _persistence.update(className, false);
        return className;
    }

    private ClassNamePersistence _persistence;
}
