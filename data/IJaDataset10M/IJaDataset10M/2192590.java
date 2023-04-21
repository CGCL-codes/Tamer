package basictests;

import com.apelon.modularclassifier.jaxb.*;
import junit.framework.TestCase;
import javax.xml.bind.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dionne
 * Date: Jul 21, 2004
 * Time: 7:58:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestJAXBModel extends TestCase {

    private JAXBContext jaxc = null;

    private ObjectFactory objFactory = null;

    private Validator validator = null;

    protected void setUp() {
        try {
            jaxc = JAXBContext.newInstance("com.apelon.modularclassifier.jaxb");
            objFactory = new ObjectFactory();
            validator = jaxc.createValidator();
        } catch (JAXBException jbe) {
            jbe.printStackTrace();
            assertTrue(false);
        }
    }

    private Concept createTypicalConcept() {
        try {
            Concept con = objFactory.createConcept();
            con.setGid("New Guy");
            con.setKind("Procedure_kind");
            con.setPrimitive(false);
            ConceptRef ref = objFactory.createConceptRef();
            ref.setIdentifier("SINGLE_RESULT_LABORATORY_TEST");
            ref.setNamespaceLocale(NamespaceLocaleEnumeration.LOCAL);
            con.getDefiningParents().add(ref);
            List roles = con.getDefiningRoles();
            Role r1 = objFactory.createRole();
            r1.setRolemod("all");
            r1.setRolename("HAS_SUBSTANCE_MEASURED");
            ConceptRef ref1 = objFactory.createConceptRef();
            ref1.setIdentifier("CHEMICAL");
            ref1.setNamespaceLocale(NamespaceLocaleEnumeration.LOCAL);
            r1.setRolevalue(ref1);
            r1.setRolegroup(0);
            roles.add(r1);
            return con;
        } catch (JAXBException jbe) {
            jbe.printStackTrace();
            assertTrue(false);
            return null;
        }
    }

    /**
     * create a simple concept and validate
     */
    public void testConcept() {
        try {
            Concept con = createTypicalConcept();
            if (con == null) {
                assertTrue(false);
            }
            if (validator.validate(con)) {
                assertTrue(true);
            } else {
                assertTrue(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void testClassifierSpace() {
        try {
            Concept con = this.createTypicalConcept();
            if (con == null) {
                assertTrue(false);
            }
            ClassifierSpaceElement cct = objFactory.createClassifierSpaceElement();
            cct.setRefBy(RefByEnumeration.NAME);
            cct.setVersion("2004.04.03");
            cct.setNamespace("triad");
            cct.getConcepts().add(con);
            FileOutputStream os = new FileOutputStream(new File("foo"));
            Marshaller m = objFactory.createMarshaller();
            m.marshal(cct, os);
            os.close();
            Unmarshaller um = objFactory.createUnmarshaller();
            cct = (ClassifierSpaceElement) um.unmarshal(new FileInputStream("foo"));
            if (validator.validate(cct)) {
            } else {
                assertTrue(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
