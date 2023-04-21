package matrixviewer.model;

import com.clarkparsia.pellet.sparqldl.jena.SparqlDLExecutionFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.iri.IRI;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.InfGraph;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TableModelListener;
import org.mindswap.pellet.owlapi.PelletReasonerFactory;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.io.*;
import org.semanticweb.owl.model.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Map;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.mindswap.pellet.jena.PelletReasoner;
import org.mindswap.pellet.owlapi.Reasoner;

/**
 *
 * @author bchisham
 */
public class CDAOMatrix implements Matrix {

    public static final String CDAO = "http://www.evolutionaryontology.org/cdao.owl";

    private List<List<MatrixDatum>> rows;

    private List<List<MatrixDatum>> cols;

    private List<OWLIndividual> row_names;

    private List<OWLIndividual> col_names;

    private Set<String> unique_values;

    private OWLOntology matrixOntologyData;

    private OWLOntologyManager ontologyManager;

    public CDAOMatrix() {
        this.rows = new ArrayList();
        this.cols = new ArrayList();
        this.row_names = new ArrayList();
        this.col_names = new ArrayList();
        this.unique_values = new TreeSet();
    }

    public List<MatrixDatum> getRow(int i) {
        return rows.get(i);
    }

    public List<MatrixDatum> getRow(String name) {
        return rows.get(row_names.indexOf(name));
    }

    public List<MatrixDatum> getColumn(int i) {
        return cols.get(i);
    }

    public List<MatrixDatum> getColumn(String name) {
        return cols.get(col_names.indexOf(name));
    }

    public Set<String> getUniqueValues() {
        return this.unique_values;
    }

    public MatrixDatum getDatum(int row, int column) {
        return this.rows.get(row).get(column);
    }

    public ListIterator getRowIterator() {
        return this.rows.listIterator();
    }

    public ListIterator getColumnIterator() {
        return this.cols.listIterator();
    }

    public ListIterator getRowIterator(int row) {
        return this.rows.listIterator(row);
    }

    public ListIterator getColumnIterator(int col) {
        return this.cols.listIterator(col);
    }

    public int getrowcount() {
        return this.row_names.size();
    }

    public int getcolumncount() {
        return this.col_names.size();
    }

    public String getRowLabel(int row) {
        return this.row_names.get(row).toString();
    }

    public String getColumnLabel(int col) {
        return this.col_names.get(col).toString();
    }

    private void extractData() {
        if (this.matrixOntologyData != null) {
            OWLDataFactory dataFactory = this.ontologyManager.getOWLDataFactory();
            PelletReasonerFactory reasonerFactory = new PelletReasonerFactory();
            URI row_type_uri = URI.create(CDAO + "#TU");
            URI col_type_uri = URI.create(CDAO + "#Character");
            URI matrix_type_uri = URI.create(CDAO + "#CharacterStateDataMatrix");
            URI datum_type_uri = URI.create(CDAO + "#CharacterStateDatum");
            URI has_character_uri = URI.create(CDAO + "#has_Character");
            URI has_tu_uri = URI.create(CDAO + "#has_TU");
            OWLClass tu = dataFactory.getOWLClass(row_type_uri);
            OWLClass character = dataFactory.getOWLClass(col_type_uri);
            OWLClass cell = dataFactory.getOWLClass(datum_type_uri);
            OWLObjectProperty has_TU = dataFactory.getOWLObjectProperty(has_tu_uri);
            OWLObjectProperty has_Character = dataFactory.getOWLObjectProperty(has_character_uri);
            Reasoner reasoner = reasonerFactory.createReasoner(this.ontologyManager);
            reasoner.setOntology(matrixOntologyData);
            KnowledgeBase kb = reasoner.getKB();
            PelletInfGraph graph = new PelletReasoner().bind(kb);
            Model model = ModelFactory.createInfModel(graph);
            Set<OWLIndividual> tus = reasoner.getIndividuals(tu, true);
            Set<OWLIndividual> chars = reasoner.getIndividuals(character, true);
            Set<OWLIndividual> cells = reasoner.getIndividuals(cell, true);
            Iterator<OWLIndividual> tuit = tus.iterator();
            while (tuit.hasNext()) {
                this.row_names.add(tuit.next());
            }
            Iterator<OWLIndividual> chit = chars.iterator();
            while (chit.hasNext()) {
                this.col_names.add(chit.next());
            }
            for (int row = 0; row < this.row_names.size(); ++row) {
                ArrayList<MatrixDatum> trow = new ArrayList();
                this.rows.add(trow);
                for (int col = 0; col < this.col_names.size(); ++col) {
                    trow.add(new MolecularDatum(row, col, ""));
                }
            }
            for (int col = 0; col < this.col_names.size(); ++col) {
                ArrayList<MatrixDatum> tcol = new ArrayList();
                this.cols.add(tcol);
                for (int row = 0; col < this.row_names.size(); ++row) {
                    tcol.add(this.rows.get(row).get(col));
                }
            }
            Iterator<OWLIndividual> cellit = cells.iterator();
            while (cellit.hasNext()) {
                OWLIndividual c = cellit.next();
                Map<OWLObjectPropertyExpression, Set<OWLIndividual>> myprops = c.getObjectPropertyValues(matrixOntologyData);
                Set<OWLIndividual> mychar = myprops.get(has_Character);
                Set<OWLIndividual> mytu = myprops.get(has_TU);
                assert (mychar.size() == 1 && mytu.size() == 1);
                Iterator<OWLIndividual> tuval = mytu.iterator();
                Iterator<OWLIndividual> charval = mychar.iterator();
                OWLIndividual thistu = tuval.next();
                OWLIndividual thischar = tuval.next();
                MatrixDatum md = this.rows.get(this.row_names.indexOf(thistu)).get(this.col_names.indexOf(thischar));
                md.setvalue(c.getURI().toString());
                if (!this.unique_values.contains(c.getURI().toString())) {
                    this.unique_values.add(c.getURI().toString());
                }
            }
        }
    }

    public void read(URL url) {
        ontologyManager = OWLManager.createOWLOntologyManager();
        try {
            matrixOntologyData = ontologyManager.loadOntology(url.toURI());
        } catch (URISyntaxException ex) {
            Logger.getLogger(CDAOMatrix.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OWLOntologyCreationException ex) {
            Logger.getLogger(CDAOMatrix.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void read(File file) {
        try {
            ontologyManager = OWLManager.createOWLOntologyManager();
            matrixOntologyData = ontologyManager.loadOntologyFromPhysicalURI(new URI("file://" + file.getAbsolutePath()));
        } catch (URISyntaxException ex) {
            Logger.getLogger(CDAOMatrix.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OWLOntologyCreationException ex) {
            Logger.getLogger(CDAOMatrix.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void read(InputStream input) {
    }

    public void write(OutputStream output) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Matrix copy() {
        CDAOMatrix ret = new CDAOMatrix();
        List<List<MatrixDatum>> ret_rows = new ArrayList();
        List<List<MatrixDatum>> ret_cols = new ArrayList();
        List<OWLIndividual> ret_row_names = new ArrayList();
        List<OWLIndividual> ret_col_names = new ArrayList();
        Set<String> ret_unique_values = new TreeSet();
        Iterator<List<MatrixDatum>> rowit = this.rows.iterator();
        while (rowit.hasNext()) {
            List<MatrixDatum> row = rowit.next();
            Iterator<MatrixDatum> dit = row.iterator();
            List<MatrixDatum> current = new ArrayList();
            ret_rows.add(current);
            while (dit.hasNext()) {
                current.add(dit.next().clone());
            }
        }
        Iterator<List<MatrixDatum>> colit = this.cols.iterator();
        while (colit.hasNext()) {
            List<MatrixDatum> col = colit.next();
            Iterator<MatrixDatum> dit = col.iterator();
            List<MatrixDatum> current = new ArrayList();
            ret_cols.add(current);
            while (dit.hasNext()) {
                current.add(dit.next().clone());
            }
        }
        Iterator<OWLIndividual> rnit = this.row_names.iterator();
        while (rnit.hasNext()) {
            ret_row_names.add(rnit.next());
        }
        Iterator<OWLIndividual> cnit = this.col_names.iterator();
        while (cnit.hasNext()) {
            ret_col_names.add(cnit.next());
        }
        Iterator<String> uv = this.unique_values.iterator();
        while (uv.hasNext()) {
            ret_unique_values.add(uv.next());
        }
        ret.rows = ret_rows;
        ret.cols = ret_cols;
        ret.row_names = ret_row_names;
        ret.col_names = ret_col_names;
        ret.unique_values = ret_unique_values;
        return ret;
    }

    public Matrix extractRange(Range rows, Range cols) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addAnnation(Annotation annotation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Annotation getAnnotation(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int size() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Iterator iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object[] toArray(Object[] a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean add(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean containsAll(Collection c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addAll(Collection c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addAll(int index, Collection c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean removeAll(Collection c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object get(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object set(int index, Object element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void add(int index, Object element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object remove(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int indexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ListIterator listIterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ListIterator listIterator(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getRowCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getColumnCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getColumnName(int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Class<?> getColumnClass(int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addTableModelListener(TableModelListener l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeTableModelListener(TableModelListener l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
