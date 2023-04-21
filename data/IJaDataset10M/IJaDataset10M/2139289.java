package metamodel;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import at.tuvienna.metamodel.MetaAssociation;
import at.tuvienna.metamodel.MetaClass;
import at.tuvienna.metamodel.MetaModel;

/**
 * 
 * @author Andreas Gruenwald <a.gruenw@gmail.com>.
 * Test cases for reference example models. Same for all different converters.
 * Tests if associations works (are converted correctly) for default reference models.
 * Uni- and bidirectional associations between classes are tested using the reference model.
 * Should be used for every converter. See {@code VParadigmDefaultAttributeTest}.
 *
 */
public abstract class DefaultAssociationTest {

    protected static MetaModel metaModel;

    protected static String PACKAGE_3 = "Test of Associations";

    protected MetaClass readClass(String classname) {
        return metaModel.findPackageByName(PACKAGE_3).findClassByName(classname);
    }

    public MetaClass findClassName(Set<MetaClass> set, String classname) {
        Iterator<MetaClass> it = set.iterator();
        while (it.hasNext()) {
            MetaClass mc = it.next();
            if (mc.getName().equals(classname)) {
                return mc;
            }
        }
        return null;
    }

    @Test
    public void carClass() {
        assertNotNull(readClass("Car"));
    }

    @Test
    public void tireClass() {
        assertNotNull(readClass("Tire"));
    }

    @Test
    public void researcherClass() {
        assertNotNull(readClass("Researcher"));
    }

    @Test
    public void treeClass() {
        assertNotNull(readClass("Tree"));
    }

    @Test
    public void linkClass() {
        assertNotNull(readClass("Link"));
    }

    @Test
    public void fingerClass() {
        assertNotNull(readClass("Finger"));
    }

    @Test
    public void universityClass() {
        assertNotNull(readClass("University"));
    }

    @Test
    public void documentClass() {
        assertNotNull(readClass("Document"));
    }

    @Test
    public void humanClass() {
        assertNotNull(readClass("Human"));
    }

    @Test
    public void trunkClass() {
        assertNotNull(readClass("Trunk"));
    }

    @Test
    public void examClass() {
        assertNotNull(readClass("Exam"));
    }

    @Test
    public void studentClass() {
        assertNotNull(readClass("Student"));
    }

    @Test
    public void carTireAssociation() {
        assertNotNull(readClass("Car").findAssociationByName("Tire"));
    }

    @Test
    public void carTireAssociationCardinality() {
        MetaAssociation a = readClass("Car").findAssociationByName("Tire");
        assertThat(a.getTo().getRange().get(0), is(equalTo("3")));
        assertThat(a.getTo().getRange().get(1), is(equalTo("4")));
    }

    @Test
    public void tireCarAssociationCardinality() {
        MetaAssociation a = readClass("Tire").findAssociationByName("Car");
        assertThat(a.getTo().getRange().get(0), is(equalTo("1")));
    }

    @Test
    public void resarcherUniversityAggregationCardinality() {
        MetaAssociation a = readClass("Researcher").findAssociationByName("University");
        assertThat(a.getTo().getRange().get(0), is(equalTo("1")));
        assertThat(a.getTo().getRange().get(1), is(equalTo("*")));
    }

    @Test
    public void resarcherUniversityIsNotAggregation() {
        MetaAssociation a = readClass("Researcher").findAssociationByName("University");
        assertFalse(a.isAggregation());
    }

    @Test
    public void universityResearcherIsAggregation() {
        MetaAssociation a = readClass("University").findAssociationByName("Researcher");
        assertTrue(a.isAggregation());
    }

    @Test
    public void universityResearcherIsNotComposition() {
        MetaAssociation a = readClass("University").findAssociationByName("Researcher");
        assertFalse(a.isComposition());
    }

    @Test
    public void universityResarcherAggregationCardinality() {
        MetaAssociation a = readClass("University").findAssociationByName("Researcher");
        assertThat(a.getTo().getRange().get(0), is(equalTo("1")));
        assertThat(a.getTo().getRange().get(1), is(equalTo("*")));
    }

    @Test
    public void trunkTreeAssociationName() {
        MetaAssociation a = readClass("Trunk").findAssociationByName("Tree");
        assertThat(a.getName(), is(equalTo("based on")));
    }

    @Test
    public void trunkTreeAssociationCardinality() {
        MetaAssociation a = readClass("Trunk").findAssociationByName("Tree");
        assertThat(a.getTo().getRange().size(), is(equalTo(1)));
        assertThat(a.getTo().getRange().get(0), is(equalTo("1")));
    }

    @Test
    public void trunkTreeAssociationComment() {
        MetaAssociation a = readClass("Trunk").findAssociationByName("Tree");
        assertThat(a.getComment().size(), is(equalTo(1)));
        assertTrue(a.getComment().get(0).contains("best practice"));
    }

    @Test
    public void treeTrunkAssociationComment() {
        MetaAssociation a = readClass("Tree").findAssociationByName("Trunk");
        assertThat(a.getComment().size(), is(equalTo(1)));
        assertTrue(a.getComment().get(0).contains("best practice"));
    }

    @Test
    public void humanFingerAssociationCardinality() {
        MetaAssociation a = readClass("Human").findAssociationByName("Finger");
        assertThat(a.getTo().getRange().get(0), is(equalTo("8")));
        assertThat(a.getTo().getRange().get(a.getTo().getRange().size() - 1), is(equalTo("10")));
    }

    @Test
    public void fingerHumanAssociationCardinality() {
        MetaAssociation a = readClass("Finger").findAssociationByName("Human");
        assertThat(a.getTo().getRange().get(0), is(equalTo("1")));
        assertThat(a.getTo().getRange().size(), is(equalTo(1)));
    }

    @Test
    public void carTireIsNotAggregation() {
        MetaAssociation a = readClass("Car").findAssociationByName("Tire");
        assertFalse(a.isAggregation());
    }

    @Test
    public void carTireIsComposition() {
        MetaAssociation a = readClass("Car").findAssociationByName("Tire");
        assertTrue(a.isComposition());
    }

    @Test
    public void tireCareAssociationCardinality() {
        MetaAssociation a = readClass("Tire").findAssociationByName("Car");
        assertThat(a.getTo().getRange().get(0), is(equalTo("1")));
    }

    @Test
    public void examStudentAssociationCardinality() {
        MetaAssociation a = readClass("Exam").findAssociationByName("Student");
        assertThat(a.getTo().getRange().get(0), is(equalTo("1")));
        assertThat(a.getTo().getRange().get(1), is(equalTo("*")));
    }

    @Test
    public void studentExamAssociationCardinality() {
        MetaAssociation a = readClass("Student").findAssociationByName("Exam");
        assertThat(a.getTo().getRange().get(0), is(equalTo("1")));
        assertThat(a.getTo().getRange().get(1), is(equalTo("*")));
    }

    @Test
    public void studentExamAssociationInverse() {
        MetaAssociation a = readClass("Student").findAssociationByName("Exam");
        assertThat(a.getInverseAssociation().getFrom().getMetaClass().getName(), is(equalTo("Exam")));
        assertThat(a.getInverseAssociation().getTo().getMetaClass().getName(), is(equalTo("Student")));
    }

    @Test
    public void studentExamComment() {
        MetaAssociation a = readClass("Student").findAssociationByName("Exam");
        assertThat(a.getComment().size(), is(1));
    }

    @Test
    public void documentLinkAssociation() {
        MetaAssociation a = readClass("Document").findAssociationByName("Link");
        assertNotNull(a);
    }

    @Test
    public void notLinkDocumentAssociation() {
        MetaAssociation a = readClass("Link").findAssociationByName("Document");
        assertNull(a);
    }

    @Test
    public void documentLinkAssociationCardinality() {
        MetaAssociation a = readClass("Document").findAssociationByName("Link");
        List<String> range = a.getTo().getRange();
        assertTrue(range.contains("*"));
        assertTrue(range.size() == 1 || range.size() == 2);
    }
}
