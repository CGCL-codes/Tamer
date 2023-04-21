package au.edu.archer.metadata.mde.validator.test;

/**
 * Junit tests for validation of records against MSS schemas with OWL profiles.
 *
 * @author scrawley@itee.uq.edu.au
 */
public class RDFValidatorTest extends ValidatorTestBase {

    public void testAnnotation1() throws Exception {
        testCaseRDF("Annotation_v1.0-rdf.mss", "decoded-Annotation_v1.0_1.xml", "Annotation_v1.0_1.diag");
    }

    public void testAnnotation2() throws Exception {
        testCaseRDF("Annotation_v1.0-rdf.mss", "decoded-Annotation_v1.0_2.xml", "Annotation_v1.0_2.diag");
    }

    public void testAnnotation3() throws Exception {
        testCaseRDF("Annotation_v1.0-rdf.mss", "decoded-Annotation_v1.0_3.xml", "Annotation_v1.0_3.diag");
    }
}
