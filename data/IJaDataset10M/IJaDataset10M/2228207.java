package org.epistem.diagram.model.emitter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import org.epistem.diagram.model.Graphic;
import org.epistem.diagram.model.Line;
import org.epistem.diagram.model.Shape;
import org.epistem.diagram.model.Table;
import org.epistem.graffle.rdf.URIPrefix;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * Base for RDF model emitters
 *
 * @author nickmain
 */
public class RDFModelEmitter extends ModelEmitter {

    protected final Model model = ModelFactory.createDefaultModel();

    @Override
    public void write() {
        try {
            FileOutputStream out = new FileOutputStream(outputFile);
            model.write(out);
            out.close();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    @Override
    public void generate(String defaultNamespace, Map<String, String> namespaces) {
        model.setNsPrefixes(namespaces);
        super.generate(defaultNamespace, namespaces);
    }

    /**
     * Get the RDF resource for a graphic
     */
    protected Resource resourceFor(Graphic g) {
        if (g instanceof Shape) {
            String name = ((Shape) g).text;
            return model.createResource(toURL(name));
        } else if (g instanceof Table) {
            Table table = (Table) g;
            Shape[][] grid = table.table;
            int rowCount = grid.length;
            int colCount = grid[0].length;
            if (rowCount != 1 && colCount != 1) {
                throw new RuntimeException("Cannot create RDF list from table that isn't one dimensional");
            }
            RDFNode[] elements;
            if (rowCount == 1) {
                elements = new RDFNode[colCount];
                for (int col = 0; col < colCount; col++) {
                    elements[col] = nodeFor(grid[0][col]);
                }
            } else {
                elements = new RDFNode[rowCount];
                for (int row = 0; row < rowCount; row++) {
                    elements[row] = nodeFor(grid[row][0]);
                }
            }
            return model.createList(elements);
        } else throw new RuntimeException("Cannot create RDF resource for graphic type " + g.getClass().getSimpleName());
    }

    /**
     * Create a Resource or Literal
     */
    protected RDFNode nodeFor(Graphic g) {
        if (g instanceof Shape) {
            String name = ((Shape) g).text;
            String note = g.metadata.notes;
            if (note != null && note.startsWith("xsd:")) {
                String xsdType = note.substring(4);
                if (!namespaces.containsKey("xsd")) {
                    String xsdNS = URIPrefix.xsd.prefix;
                    namespaces.put("xsd", xsdNS);
                    model.setNsPrefix("xsd", xsdNS);
                }
                XSDDatatype dataType = new XSDDatatype(xsdType);
                return model.createTypedLiteral(name, dataType);
            }
            return resourceFor(g);
        } else if (g instanceof Table) return resourceFor(g); else throw new RuntimeException("Cannot create RDF node for graphic type " + g.getClass().getSimpleName());
    }

    /**
     * Generate a resource
     */
    @Handler("rdf-resource")
    public void rdfResource() {
        resourceFor(graphic);
    }

    /**
     * Generate a statement
     */
    @Handler("rdf-statement")
    public void rdfStatement() {
        if (graphic instanceof Line) {
            Line line = (Line) graphic;
            if (line.head == null) throw new RuntimeException("RDF statement has no head");
            if (line.tail == null) throw new RuntimeException("RDF statement has no tail");
            if (line.labels.isEmpty()) throw new RuntimeException("RDF statement line must have label(s) to denote the property");
            Graphic head = line.head;
            Graphic tail = line.tail;
            Resource tailResource = resourceFor(tail);
            RDFNode headNode = nodeFor(head);
            for (Shape label : line.labels) {
                String uri = toURL(label.text);
                Property prop = model.createProperty(uri);
                model.add(tailResource, prop, headNode);
            }
        } else throw new RuntimeException("Cannot create RDF statement from graphic " + graphic);
    }

    /**
     * Generate a type assertion
     */
    @Handler("rdf-type")
    public void rdfType() {
        if (graphic instanceof Line) {
            Line line = (Line) graphic;
            if (line.head == null || !(line.head instanceof Shape)) throw new RuntimeException("rdf:type statement has no head");
            if (line.tail == null || !(line.tail instanceof Shape)) throw new RuntimeException("rdf:type statement has no tail");
            Shape head = (Shape) line.head;
            Shape tail = (Shape) line.tail;
            Resource tailResource = resourceFor(tail);
            Resource headResource = resourceFor(head);
            model.add(tailResource, RDF.type, headResource);
        } else throw new RuntimeException("Cannot create rdf:type statement from graphic " + graphic);
    }
}
