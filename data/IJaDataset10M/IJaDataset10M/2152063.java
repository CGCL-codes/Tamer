package net.sf.hibernate4gwt.core.beanlib.mapper;

import net.sf.hibernate4gwt.core.beanlib.mapper.DirectoryClassMapper;
import junit.framework.TestCase;

/**
 * Test case for the Directory Class Mapper class
 * @author bruno.marchesson
 *
 */
public class DirectoryClassMapperTest extends TestCase {

    /**
	 * Test method for {@link net.sf.hibernate4gwt.core.beanlib.mapper.DirectoryClassMapper#getTargetClass(java.lang.Class)}.
	 */
    public final void testNominalCase() {
        DirectoryClassMapper classMapper = new DirectoryClassMapper();
        classMapper.setRootDomainPackage("net.sf.hibernate4gwt.core.beanlib.mapper.domain1");
        classMapper.setRootClonePackage("net.sf.hibernate4gwt.core.beanlib.mapper.dto1");
        classMapper.setCloneSuffix("DTO");
        Class sourceClass = net.sf.hibernate4gwt.core.beanlib.mapper.domain1.DomainClass1.class;
        Class targetClass = net.sf.hibernate4gwt.core.beanlib.mapper.dto1.DomainClass1DTO.class;
        assertEquals(targetClass, classMapper.getTargetClass(sourceClass));
        assertNull(classMapper.getTargetClass(targetClass));
        assertEquals(sourceClass, classMapper.getSourceClass(targetClass));
        assertNull(classMapper.getSourceClass(sourceClass));
    }

    /**
	 * Test nested DTO and Domain package issue
	 */
    public final void testNestedPackages() {
        DirectoryClassMapper classMapper = new DirectoryClassMapper();
        classMapper.setRootDomainPackage("net.sf.hibernate4gwt.core.beanlib.mapper.domain1");
        classMapper.setRootClonePackage("net.sf.hibernate4gwt.core.beanlib.mapper.domain1.dto");
        classMapper.setCloneSuffix("DTO");
        Class sourceClass = net.sf.hibernate4gwt.core.beanlib.mapper.domain1.DomainClass1.class;
        Class targetClass = net.sf.hibernate4gwt.core.beanlib.mapper.domain1.dto.DomainClass1DTO.class;
        assertEquals(targetClass, classMapper.getTargetClass(sourceClass));
        assertNull(classMapper.getTargetClass(targetClass));
        assertEquals(sourceClass, classMapper.getSourceClass(targetClass));
        assertNull(classMapper.getSourceClass(sourceClass));
    }
}
