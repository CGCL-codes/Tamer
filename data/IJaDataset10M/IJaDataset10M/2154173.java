package hrashk.chemistry.layout;

import java.io.*;
import java.nio.charset.Charset;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import java.util.List;
import java.util.Set;
import org._3pq.jgrapht.Edge;
import hrashk.chemistry.structure.Bond;
import hrashk.chemistry.structure.Atom;
import hrashk.chemistry.structure.Molecule;
import hrashk.chemistry.transform.XML2MDL;
import hrashk.chemistry.transform.MDL2XML;
import org._3pq.jgrapht.Graph;

/** This class is for testing purposes. */
public class FractionTest {

    /**XML to MDL converter */
    private static XML2MDL _converter = new XML2MDL();

    /** The general Document object. */
    private static Document _document = null;

    /** The number of written molecules */
    private static int _counter = 0;

    /** Program entry point. */
    public static void main(String[] args) throws Exception {
        DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dFactory.newDocumentBuilder();
        _document = dBuilder.newDocument();
        MDL2XML m2x = new MDL2XML();
        FileInputStream fis = new FileInputStream(new File(args[0]));
        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("US-ASCII"));
        BufferedReader br = new BufferedReader(isr);
        FileOutputStream fos = new FileOutputStream(new File(args[1]));
        OutputStreamWriter osw = new OutputStreamWriter(fos, Charset.forName("US-ASCII"));
        BufferedWriter bw = new BufferedWriter(osw);
        try {
            for (Node record = m2x.sdf2xml(br, _document); record != null; record = m2x.sdf2xml(br, _document)) {
                Node node;
                for (node = record.getFirstChild(); node.getNodeType() != Node.ELEMENT_NODE; node = node.getNextSibling()) ;
                Molecule molecule = Molecule.load(node);
                BlockGraphInspector bgi = new BlockGraphInspector(molecule);
                FractionGenerator fg = new FractionGenerator(bgi, null);
                for (Fraction f : fg.getFractions()) {
                    Molecule mol = new Molecule();
                    Atom center = (Atom) f.getCentralVertex();
                    String old_center = null;
                    if (center != null) {
                        old_center = center._element;
                        center._element = "T";
                    }
                    Atom start = (Atom) f.getStartVertex();
                    String old_start = null;
                    if (start != null) {
                        old_start = start._element;
                        start._element = "D";
                    }
                    mol.addAllVertices(f.vertexSet());
                    mol.addAllEdges(f.edgeSet());
                    write(mol, bw);
                    if (center != null) center._element = old_center;
                    if (start != null) start._element = old_start;
                }
            }
        } finally {
            br.close();
            bw.close();
        }
    }

    /** Write the buffer into a file. */
    private static void write(Molecule molecule, BufferedWriter bw) throws IOException {
        Node record_node = _document.createElement("record");
        record_node.appendChild(molecule.store(_document));
        Element field_node = _document.createElement("field");
        field_node.setAttribute("name", "IDNUMBER");
        field_node.appendChild(_document.createTextNode(String.valueOf(++_counter)));
        record_node.appendChild(field_node);
        StringBuilder sb = _converter.recordToSDF(record_node);
        bw.write(sb.toString());
    }
}
