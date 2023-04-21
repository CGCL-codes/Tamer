package MiningFlow.src.br.ufrj.pesc.owl;

import MiningFlow.src.br.ufrj.pesc.database.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import edu.stanford.smi.protege.util.Disposable;
import edu.stanford.smi.protegex.owl.model.OWLDatatypeProperty;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFResource;
import edu.stanford.smi.protegex.owl.model.RDFSNames;
import edu.stanford.smi.protegex.owl.model.event.ModelAdapter;
import edu.stanford.smi.protegex.owl.model.event.ModelListener;
import edu.stanford.smi.protegex.owl.ui.OWLLabeledComponent;
import edu.stanford.smi.protegex.owl.ui.ResourceRenderer;
import edu.stanford.smi.protegex.owl.ui.icons.OWLIcons;
import edu.stanford.smi.protegex.owl.ui.widget.OWLUI;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Daniel
 */
public class owlMiningFlowOntology {

    /** Creates a new instance of owlMiningFlowOntology */
    public owlMiningFlowOntology() {
    }

    public static void main(String args[]) throws SQLException {
    }

    public static void Teste() {
        LinkedList ClassesOnto = new LinkedList();
        owlMiningFlowOntology x = new owlMiningFlowOntology();
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter("C:/Teste.txt"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String Teste = new String();
        for (Iterator i = ClassesOnto.iterator(); i.hasNext(); ) {
            try {
                out.write((String) i.next());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        try {
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
