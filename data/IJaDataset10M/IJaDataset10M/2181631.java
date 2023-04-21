package org.jomc.model.test;

import java.io.IOException;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import junit.framework.Assert;
import org.jomc.model.DefaultModelManager;
import org.jomc.model.DefaultModelObjectValidator;
import org.jomc.model.Dependencies;
import org.jomc.model.Dependency;
import org.jomc.model.Implementation;
import org.jomc.model.Implementations;
import org.jomc.model.Message;
import org.jomc.model.Messages;
import org.jomc.model.ModelObject;
import org.jomc.model.ModelObjectValidationReport;
import org.jomc.model.ModelObjectValidator;
import org.jomc.model.Modules;
import org.jomc.model.Properties;
import org.jomc.model.Property;
import org.jomc.model.SpecificationReference;
import org.jomc.model.Specifications;
import org.xml.sax.SAXException;

/**
 * Test cases for class {@code org.jomc.model.ModelObjectValidator} implementations.
 *
 * @author <a href="mailto:cs@jomc.org">Christian Schulte</a> 1.0
 * @version $Id: ModelObjectValidatorTest.java 891 2009-11-02 03:40:00Z schulte2005 $
 */
public class ModelObjectValidatorTest {

    /** The model object validator of the instance. */
    private ModelObjectValidator modelObjectValidator;

    /** The test suite to run. */
    private TestSuite testSuite;

    /**
     * Gets the model object validator of the instance.
     *
     * @return The model object validator of the instance.
     */
    public ModelObjectValidator getModelObjectValidator() {
        if (this.modelObjectValidator == null) {
            this.modelObjectValidator = new DefaultModelObjectValidator();
        }
        return this.modelObjectValidator;
    }

    /**
     * Sets the model object validator to test.
     *
     * @param value The model object validator to test.
     */
    public void setModelObjectValidator(final ModelObjectValidator value) {
        this.modelObjectValidator = value;
    }

    /**
     * Gets the test suite of the instance.
     *
     * @return The test suite of the instance.
     */
    public TestSuite getTestSuite() throws IOException, SAXException, JAXBException {
        if (this.testSuite == null) {
            final Unmarshaller u = new DefaultModelManager().getUnmarshaller(this.getClass().getClassLoader());
            final JAXBElement<TestSuite> e = (JAXBElement<TestSuite>) u.unmarshal(this.getClass().getResource("testsuite.xml"));
            this.testSuite = e.getValue();
        }
        return this.testSuite;
    }

    public void testSchemaConstraints() throws Exception {
        final JAXBContext context = new DefaultModelManager().getContext(this.getClass().getClassLoader());
        final Schema schema = new DefaultModelManager().getSchema(this.getClass().getClassLoader());
        for (SchemaConstraintsTest test : this.getTestSuite().getSchemaConstraintsTest()) {
            System.out.println("SchemaConstraintsTest: " + test.getIdentifier());
            final JAXBElement<? extends ModelObject> modelObject = (JAXBElement<? extends ModelObject>) test.getModelObject().getAny();
            final ModelObjectValidationReport report = this.getModelObjectValidator().validateModelObject(modelObject, context, schema);
            log(report);
            if (test.getModelObject().isValid()) {
                if (!report.isModelObjectValid()) {
                    Assert.fail("[" + test.getIdentifier() + "] Unexpected invalid model object.");
                }
            } else {
                if (report.isModelObjectValid()) {
                    Assert.fail("[" + test.getIdentifier() + "] Unexpected valid model object.");
                }
            }
        }
    }

    public void testModulesConstraints() throws Exception {
        final JAXBContext context = new DefaultModelManager().getContext(this.getClass().getClassLoader());
        final Schema schema = new DefaultModelManager().getSchema(this.getClass().getClassLoader());
        for (ModulesConstraintsTest test : this.getTestSuite().getModulesConstraintsTest()) {
            System.out.println("ModulesConstraintsTest: " + test.getIdentifier());
            final JAXBElement<Modules> modules = (JAXBElement<Modules>) test.getModules().getAny();
            final ModelObjectValidationReport report = this.getModelObjectValidator().validateModules(modules, context, schema);
            log(report);
            if (test.getModules().isValid()) {
                if (!report.isModelObjectValid()) {
                    Assert.fail("[" + test.getIdentifier() + "] Unexpected invalid model object.");
                }
            } else {
                if (report.isModelObjectValid()) {
                    Assert.fail("[" + test.getIdentifier() + "] Unexpected valid model object.");
                }
                for (ModelObjectValidationReportDetail expectedDetail : test.getDetail()) {
                    final List<ModelObjectValidationReport.Detail> reportedDetails = report.getDetails(expectedDetail.getIdentifier());
                    Assert.assertTrue("[" + test.getIdentifier() + "] Expected " + expectedDetail.getCount() + " " + expectedDetail.getIdentifier() + " details but got " + reportedDetails.size() + ".", expectedDetail.getCount() == reportedDetails.size());
                    report.getDetails().removeAll(reportedDetails);
                }
                if (!report.getDetails().isEmpty()) {
                    for (ModelObjectValidationReport.Detail d : report.getDetails()) {
                        Assert.fail("[" + test.getIdentifier() + "] Unexpected " + d.getIdentifier() + " detail.");
                    }
                }
            }
        }
    }

    public void testImplementations() throws Exception {
        final JAXBContext context = new DefaultModelManager().getContext(this.getClass().getClassLoader());
        final Schema schema = new DefaultModelManager().getSchema(this.getClass().getClassLoader());
        for (ImplementationTest test : this.getTestSuite().getImplementationTest()) {
            System.out.println("ImplementationTest: " + test.getIdentifier());
            final JAXBElement<Modules> modules = (JAXBElement<Modules>) test.getModules().getAny();
            final ModelObjectValidationReport modulesReport = this.getModelObjectValidator().validateModules(modules, context, schema);
            Assert.assertTrue("[" + test.getIdentifier() + "] Unexpected invalid modules.", modulesReport.isModelObjectValid());
            final JAXBElement<Implementation> expected = (JAXBElement<Implementation>) test.getImplementation().getAny();
            final ModelObjectValidationReport implementationReport = this.getModelObjectValidator().validateModelObject(expected, context, schema);
            Assert.assertTrue("[" + test.getIdentifier() + "] Unexpected invalid implementation.", implementationReport.isModelObjectValid());
            final Implementation i = modules.getValue().getImplementation(expected.getValue().getIdentifier());
            Assert.assertNotNull(i);
            assertEquals(expected.getValue(), i);
            assertEquals(expected.getValue().getDependencies(), modules.getValue().getDependencies(expected.getValue().getIdentifier()));
            assertEquals(expected.getValue().getMessages(), modules.getValue().getMessages(expected.getValue().getIdentifier()));
            assertEquals(expected.getValue().getProperties(), modules.getValue().getProperties(expected.getValue().getIdentifier()));
            assertEquals(expected.getValue().getSpecifications(), modules.getValue().getSpecifications(expected.getValue().getIdentifier()));
        }
    }

    public static void assertEquals(final ModelObject expected, final ModelObject computed) throws Exception {
        if (expected != null) {
            Assert.assertNotNull(computed);
            Assert.assertEquals(expected.getCreateDate(), computed.getCreateDate());
            Assert.assertEquals(expected.getModelVersion(), computed.getModelVersion());
            Assert.assertEquals(expected.isDeprecated(), computed.isDeprecated());
        } else {
            Assert.assertNull(computed);
        }
    }

    public static void assertEquals(final Implementations expected, final Implementations computed) throws Exception {
        if (expected != null) {
            Assert.assertNotNull(computed);
            assertEquals((ModelObject) expected, (ModelObject) computed);
            for (Implementation i : expected.getImplementation()) {
                assertEquals(i, computed.getImplementation(i.getIdentifier()));
            }
        } else {
            Assert.assertNull(computed);
        }
    }

    public static void assertEquals(final Implementation expected, final Implementation computed) throws Exception {
        if (expected != null) {
            Assert.assertNotNull(computed);
            assertEquals((ModelObject) expected, (ModelObject) computed);
            Assert.assertEquals(expected.getClazz(), computed.getClazz());
            Assert.assertEquals(expected.getIdentifier(), computed.getIdentifier());
            Assert.assertEquals(expected.getLocation(), computed.getLocation());
            Assert.assertEquals(expected.getName(), computed.getName());
            Assert.assertEquals(expected.getVendor(), computed.getVendor());
            Assert.assertEquals(expected.getVersion(), computed.getVersion());
            Assert.assertEquals(expected.isAbstract(), computed.isAbstract());
            Assert.assertEquals(expected.isFinal(), computed.isFinal());
            Assert.assertEquals(expected.isStateless(), computed.isStateless());
        } else {
            Assert.assertNull(computed);
        }
    }

    public static void assertEquals(final Specifications expected, final Specifications computed) throws Exception {
        if (expected != null) {
            Assert.assertNotNull(computed);
            assertEquals((ModelObject) expected, (ModelObject) computed);
            for (SpecificationReference r : expected.getReference()) {
                assertEquals(r, computed.getReference(r.getIdentifier()));
            }
        } else {
            Assert.assertNull(computed);
        }
    }

    public static void assertEquals(final SpecificationReference expected, final SpecificationReference computed) throws Exception {
        if (expected != null) {
            Assert.assertNotNull(computed);
            assertEquals((ModelObject) expected, (ModelObject) computed);
            Assert.assertEquals(expected.getIdentifier(), computed.getIdentifier());
            Assert.assertEquals(expected.getVersion(), computed.getVersion());
            Assert.assertEquals(expected.isDeprecated(), computed.isDeprecated());
            Assert.assertEquals(expected.isFinal(), computed.isFinal());
            Assert.assertEquals(expected.isOverride(), computed.isOverride());
        } else {
            Assert.assertNull(computed);
        }
    }

    public static void assertEquals(final Dependencies expected, final Dependencies computed) throws Exception {
        if (expected != null) {
            Assert.assertNotNull(computed);
            for (Dependency d : expected.getDependency()) {
                assertEquals(d, computed.getDependency(d.getName()));
            }
        } else {
            Assert.assertNull(computed);
        }
    }

    public static void assertEquals(final Dependency expected, final Dependency computed) throws Exception {
        if (expected != null) {
            Assert.assertNotNull(computed);
            Assert.assertEquals(expected.getIdentifier(), computed.getIdentifier());
            Assert.assertEquals(expected.getImplementationName(), computed.getImplementationName());
            Assert.assertEquals(expected.getName(), computed.getName());
            Assert.assertEquals(expected.isDeprecated(), computed.isDeprecated());
            Assert.assertEquals(expected.isFinal(), computed.isFinal());
            Assert.assertEquals(expected.isOverride(), computed.isOverride());
            Assert.assertEquals(expected.isBound(), computed.isBound());
            Assert.assertEquals(expected.isOptional(), computed.isOptional());
            Assert.assertEquals(expected.getVersion(), computed.getVersion());
            assertEquals(expected.getProperties(), computed.getProperties());
        } else {
            Assert.assertNull(computed);
        }
    }

    public static void assertEquals(final Messages expected, final Messages computed) throws Exception {
        if (expected != null) {
            Assert.assertNotNull(computed);
            for (Message m : expected.getMessage()) {
                assertEquals(m, computed.getMessage(m.getName()));
            }
        } else {
            Assert.assertNull(computed);
        }
    }

    public static void assertEquals(final Message expected, final Message computed) throws Exception {
        if (expected != null) {
            Assert.assertNotNull(computed);
            Assert.assertEquals(expected.getName(), computed.getName());
            Assert.assertEquals(expected.isDeprecated(), computed.isDeprecated());
            Assert.assertEquals(expected.isFinal(), computed.isFinal());
            Assert.assertEquals(expected.isOverride(), computed.isOverride());
        } else {
            Assert.assertNull(computed);
        }
    }

    public static void assertEquals(final Properties expected, final Properties computed) throws Exception {
        if (expected != null) {
            Assert.assertNotNull(computed);
            for (Property p : expected.getProperty()) {
                assertEquals(p, computed.getProperty(p.getName()));
            }
        } else {
            Assert.assertNull(computed);
        }
    }

    public static void assertEquals(final Property expected, final Property computed) throws Exception {
        if (expected != null) {
            Assert.assertNotNull(computed);
            Assert.assertEquals(expected.getJavaValue(ModelManagerTest.class.getClassLoader()), computed.getJavaValue(ModelManagerTest.class.getClassLoader()));
            Assert.assertEquals(expected.getName(), computed.getName());
            Assert.assertEquals(expected.getType(), computed.getType());
            Assert.assertEquals(expected.getValue(), computed.getValue());
            Assert.assertEquals(expected.isDeprecated(), computed.isDeprecated());
            Assert.assertEquals(expected.isFinal(), computed.isFinal());
            Assert.assertEquals(expected.isOverride(), computed.isOverride());
        } else {
            Assert.assertNull(computed);
        }
    }

    public static void log(final ModelObjectValidationReport report) {
        for (ModelObjectValidationReport.Detail d : report.getDetails()) {
            System.out.println("\t" + d.toString());
        }
    }
}
