package de.fuberlin.wiwiss.ng4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import junit.framework.TestCase;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;
import com.hp.hpl.jena.rdf.model.impl.StatementImpl;
import com.hp.hpl.jena.rdf.model.impl.StmtIteratorImpl;
import de.fuberlin.wiwiss.ng4j.NamedGraphModel;
import de.fuberlin.wiwiss.ng4j.NamedGraphSet;
import de.fuberlin.wiwiss.ng4j.NamedGraphStatement;
import de.fuberlin.wiwiss.ng4j.Quad;
import de.fuberlin.wiwiss.ng4j.impl.NamedGraphSetImpl;

/**
 * Unit tests for {@link NamedGraphModel}. We do not test the whole
 * Model interface because it is so large. We left out methods which
 * likely are unaffected by our changes, or which just call other methods
 * that we already have tested.
 *
 * @author Richard Cyganiak (richard@cyganiak.de)
 */
public class NamedGraphModelTest extends TestCase {

    private static final String uri1 = "http://example.org/#graph1";

    private static final Node node1 = Node.createURI("http://example.org/#graph1");

    private static final Resource foo = new ResourceImpl("http://example.org/#foo");

    private static final Property bar = new PropertyImpl("http://example.org/#bar");

    private static final Resource baz = new ResourceImpl("http://example.org/#baz");

    private static final Node fooNode = Node.createURI("http://example.org/#foo");

    private static final Node barNode = Node.createURI("http://example.org/#bar");

    private static final Node bazNode = Node.createURI("http://example.org/#baz");

    private static final String fooString = "http://example.org/#foo";

    private NamedGraphSet set;

    private NamedGraphModel model;

    protected void setUp() throws Exception {
        this.set = new NamedGraphSetImpl();
        this.model = this.set.asJenaModel(uri1);
        super.setUp();
    }

    public void testGetDefaultGraphName() {
        assertEquals(uri1, this.model.getDefaultGraphName().getURI());
    }

    public void testAddStatementList() {
        this.model.add(twoStatementsList());
        assertTwoStatementsAdded();
    }

    public void testAddModel() {
        Model other = ModelFactory.createDefaultModel();
        other.add(twoStatementsList());
        this.model.add(other);
        assertTwoStatementsAdded();
    }

    public void testAddStatement() {
        this.model.add(new StatementImpl(foo, bar, baz));
        assertTrue(this.set.containsQuad(new Quad(node1, fooNode, barNode, bazNode)));
        assertTrue(this.model.listStatements().next() instanceof NamedGraphStatement);
    }

    public void testAddStatementArray() {
        this.model.add((Statement[]) twoStatementsList().toArray(new Statement[] {}));
        assertTwoStatementsAdded();
    }

    public void testAddStatementIterator() {
        this.model.add(new StmtIteratorImpl(twoStatementsList().iterator()));
        assertTwoStatementsAdded();
    }

    public void testContains() {
        this.model.add(twoStatementsList());
        assertTrue(this.model.contains(foo, bar, baz));
        assertTrue(this.model.contains(baz, bar, foo));
        assertFalse(this.model.contains(foo, bar, foo));
        assertTrue(this.model.contains(new StatementImpl(foo, bar, baz)));
        assertTrue(this.model.contains(new StatementImpl(baz, bar, foo)));
        assertFalse(this.model.contains(new StatementImpl(foo, bar, foo)));
        assertTrue(this.model.contains(new NamedGraphStatement(foo, bar, baz, this.model)));
        assertTrue(this.model.contains(new NamedGraphStatement(baz, bar, foo, this.model)));
        assertFalse(this.model.contains(new NamedGraphStatement(foo, bar, foo, this.model)));
    }

    public void testCreateStatement() {
        Statement stmt = this.model.createStatement(foo, bar, baz);
        assertTrue(stmt instanceof NamedGraphStatement);
        assertTrue(this.model.isEmpty());
    }

    public void testListStatementsAll() {
        this.model.add(twoStatementsList());
        assertAllNamedGraphStatements(this.model.listStatements());
        Set stmts = this.model.listStatements().toSet();
        assertTrue(stmts.contains(new NamedGraphStatement(foo, bar, baz, this.model)));
        assertTrue(stmts.contains(new NamedGraphStatement(baz, bar, foo, this.model)));
        assertEquals(2, stmts.size());
    }

    public void testListStatementsWithSelector() {
        this.model.add(twoStatementsList());
        assertAllNamedGraphStatements(this.model.listStatements(new SimpleSelector()));
        Collection<Statement> stmts = toCollection(this.model.listStatements(new SimpleSelector()));
        assertTrue(stmts.contains(new NamedGraphStatement(foo, bar, baz, this.model)));
        assertTrue(stmts.contains(new NamedGraphStatement(baz, bar, foo, this.model)));
        assertEquals(2, stmts.size());
    }

    private Collection<Statement> toCollection(StmtIterator it) {
        Collection<Statement> result = new ArrayList<Statement>();
        while (it.hasNext()) {
            result.add((Statement) it.next());
        }
        return result;
    }

    private List<Statement> twoStatementsList() {
        List<Statement> statements = new ArrayList<Statement>();
        statements.add(new StatementImpl(foo, bar, baz));
        statements.add(new NamedGraphStatement(baz, bar, foo, this.model));
        return statements;
    }

    private void assertTwoStatementsAdded() {
        assertTrue(this.set.containsQuad(new Quad(node1, fooNode, barNode, bazNode)));
        assertTrue(this.set.containsQuad(new Quad(node1, bazNode, barNode, fooNode)));
        assertEquals(2, this.set.countQuads());
        assertTrue(this.model.contains(foo, bar, baz));
        assertTrue(this.model.contains(baz, bar, foo));
        assertEquals(2, this.model.size());
        assertAllNamedGraphStatements(this.model.getResource(fooString).listProperties());
    }

    private void assertAllNamedGraphStatements(StmtIterator it) {
        while (it.hasNext()) {
            Object o = it.next();
            if (!(o instanceof NamedGraphStatement)) {
                fail("Expected NamedGraphStatement, was " + o.getClass().getName() + ": " + o);
            }
        }
    }
}
