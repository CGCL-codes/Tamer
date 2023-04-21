package org.springframework.jmx.export.assembler;

/**
 * @author Rob Harrop
 */
public class ReflectiveAssemblerTests extends AbstractJmxAssemblerTests {

    protected static final String OBJECT_NAME = "bean:name=testBean1";

    protected String getObjectName() {
        return OBJECT_NAME;
    }

    protected int getExpectedOperationCount() {
        return 11;
    }

    protected int getExpectedAttributeCount() {
        return 4;
    }

    protected MBeanInfoAssembler getAssembler() {
        return new SimpleReflectiveMBeanInfoAssembler();
    }

    protected String getApplicationContextPath() {
        return "org/springframework/jmx/export/assembler/reflectiveAssembler.xml";
    }
}
