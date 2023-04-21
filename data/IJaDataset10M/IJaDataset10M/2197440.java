package br.ucam.kuabaSubsystem.kuabaModel;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import br.ucam.kuabaSubsystem.abstractTestCase.AbstractKuabaTestCase;
import br.ucam.kuabaSubsystem.kuabaModel.Activity;
import br.ucam.kuabaSubsystem.kuabaModel.Artifact;
import br.ucam.kuabaSubsystem.kuabaModel.CompositeArtifact;
import br.ucam.kuabaSubsystem.kuabaModel.ReasoningElement;
import br.ucam.kuabaSubsystem.kuabaModel.Role;
import com.hp.hpl.jena.util.FileUtils;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFIndividual;

public class CompositeArtifactTest extends AbstractKuabaTestCase {

    public void test_add_composition_function() {
        CompositeArtifact classDiagram = this.factory.createCompositeArtifact("classDiagram");
        assertTrue(classDiagram.getCompositionFunction().isEmpty());
        classDiagram.addCompositionFunction("agregation");
        assertFalse(classDiagram.getCompositionFunction().isEmpty());
        assertEquals("agregation", (String) classDiagram.listCompositionFunction().next());
        classDiagram.addCompositionFunction("association");
        assertEquals(2, classDiagram.getCompositionFunction().size());
        assertEquals(true, classDiagram.getCompositionFunction().contains("association"));
        ArrayList<String> errors = new ArrayList<String>();
        this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
        assertTrue(errors.isEmpty());
    }

    public void test_add_repeated_composition_function() {
        CompositeArtifact useCaseDiagram = this.factory.getCompositeArtifact("use_case_diagram");
        useCaseDiagram.addCompositionFunction("include");
        useCaseDiagram.addCompositionFunction("include");
        useCaseDiagram.addCompositionFunction("include");
        Collection<String> compositionFunctions = useCaseDiagram.getCompositionFunction();
        assertTrue(useCaseDiagram.hasCompositionFunction());
        assertEquals(1, compositionFunctions.size());
        assertTrue(compositionFunctions.contains("include"));
        this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, new ArrayList());
    }

    public void test_remove_composition_function() {
        CompositeArtifact sequenceDiagram = this.factory.createCompositeArtifact("sequence_diagram");
        sequenceDiagram.addCompositionFunction("assychronous message");
        assertFalse(sequenceDiagram.getCompositionFunction().isEmpty());
        assertEquals("assychronous message", (String) sequenceDiagram.listCompositionFunction().next());
        ArrayList<String> errors = new ArrayList<String>();
        this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
        assertTrue(errors.isEmpty());
        sequenceDiagram.removeCompositionFunction("assychronous message");
        assertTrue(sequenceDiagram.getCompositionFunction().isEmpty());
        this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
        assertTrue(errors.isEmpty());
    }

    /**
	 * Tests {@link CompositeArtifact#getCompositionOf()} method.
	 */
    public void test_getCompositionOf() {
        CompositeArtifact classDiagram = this.factory.getCompositeArtifact("class_diagram");
        Collection<Artifact> compositionOfCollection = classDiagram.getCompositionOf();
        assertFalse(compositionOfCollection.isEmpty());
        assertEquals(1, compositionOfCollection.size());
        Iterator<Artifact> compositionOfIterator = compositionOfCollection.iterator();
        assertEquals("cd_class", compositionOfIterator.next().getId());
    }

    /**
	 * Tests {@link CompositeArtifact#hasCompositionOf()} method.
	 */
    public void test_hasCompositionOf() {
        CompositeArtifact classDiagram = this.factory.getCompositeArtifact("class_diagram");
        assertTrue(classDiagram.hasCompositionOf());
    }

    /**
	 * Tests {@link CompositeArtifact#listCompositionOf()} method.
	 */
    public void test_listCompositionOf() {
        CompositeArtifact classDiagram = this.factory.getCompositeArtifact("class_diagram");
        Iterator<Artifact> CompositionOfIterator = classDiagram.listCompositionOf();
        assertEquals("cd_class", ((DefaultRDFIndividual) CompositionOfIterator.next()).getName());
        try {
            CompositionOfIterator.next();
            fail();
        } catch (NoSuchElementException ex) {
            assertTrue(true);
        }
    }

    /**
	 * Tests {@link CompositeArtifact#addCompositionOf(Artifact)} method.
	 * 
	 * @throws FileNotFoundException: thrown if the br.ucam.kuabaSubsystem.testBase.xml was
	 * not found.
	 * 
	 * @throws Exception: thrown if save method generates an error. 
	 */
    public void test_addCompositionOf() throws FileNotFoundException, Exception {
        CompositeArtifact classDiagram = this.factory.getCompositeArtifact("class_diagram");
        Artifact newElement = this.factory.createArtifact("another_Artifact");
        assertEquals(1, classDiagram.getCompositionOf().size());
        classDiagram.addCompositionOf(newElement);
        ArrayList<String> errors = new ArrayList<String>();
        this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
        assertTrue(errors.isEmpty());
        Iterator<Artifact> CompositionOfIterator = classDiagram.listCompositionOf();
        assertEquals("cd_class", ((DefaultRDFIndividual) CompositionOfIterator.next()).getName());
        assertEquals("another_Artifact", ((DefaultRDFIndividual) CompositionOfIterator.next()).getName());
    }

    /**
	 * Tests {@link CompositeArtifact#addCompositionOf(Artifact)} method.
	 * 
	 * The goal of this test is to guarantee that the addCompositionOf method do not
	 * add the same reasoning element more than one time.
	 */
    public void test_add_repeated_CompositionOf() {
        CompositeArtifact classDiagram = this.factory.getCompositeArtifact("class_diagram");
        Artifact cd_class = this.factory.getArtifact("cd_class");
        classDiagram.addCompositionOf(cd_class);
        classDiagram.addCompositionOf(cd_class);
        classDiagram.addCompositionOf(cd_class);
        classDiagram.addCompositionOf(cd_class);
        assertEquals(1, classDiagram.getCompositionOf().size());
    }

    /**
	 * Tests {@link CompositeArtifact#removeCompositionOf(Artifact)} method.
	 */
    public void test_removeCompositionOf() {
        CompositeArtifact classDiagram = this.factory.getCompositeArtifact("class_diagram");
        Artifact cd_class = this.factory.getArtifact("cd_class");
        assertEquals(1, classDiagram.getCompositionOf().size());
        classDiagram.removeCompositionOf(cd_class);
        assertTrue(classDiagram.getCompositionOf().isEmpty());
        assertEquals(0, classDiagram.getCompositionOf().size());
        Iterator<Artifact> CompositionOfIterator = classDiagram.listCompositionOf();
        try {
            CompositionOfIterator.next();
            fail();
        } catch (NoSuchElementException ex) {
            assertTrue(true);
        }
        ArrayList<String> errors = new ArrayList<String>();
        this.model.save(this.testKuabaKnowlegeBase.toURI(), FileUtils.langXMLAbbrev, errors);
        assertTrue(errors.isEmpty());
    }
}
