package com.skruk.elvis.admin.db.rdf;

import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.db.ModelRDB;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFException;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.rdql.Query;
import com.hp.hpl.jena.rdql.QueryEngine;
import com.hp.hpl.jena.rdql.QueryExecution;
import com.hp.hpl.jena.rdql.QueryResults;
import com.hp.hpl.jena.vocabulary.RDF;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Tworzy interface do komunikacji z bazą RDF
 *
 * @author     skruk
 * @created    22 listopad 2003
 */
public class JenaFace extends TimerTask {

    /**
	 * czas po jakim ma zostać wysłane testowe zapytanie zachowujące połączenie z bazą SQL
	 */
    private static final int CONNECTION_TIMEOUT = 60000;

    /** URI pod którym dostępny jest backend modelu RDF */
    public static String DATABASE_URI = "jdbc:hsqldb:JENA";

    /** JDBC driver * */
    public static String DATABASE_DRIVER = "org.hsqldb.jdbcDriver";

    /** Rodzaj używanego backendu    - patrz dokumentacja <tt>Jena</tt> */
    public static String LAYOUT_STYLE = "MMGeneric";

    /** Nazwa bazy wykorzstywanej jako backend */
    public static String DATABASE_TYPE = "HSQL";

    /** Nazwa wykorzystywanego modelu */
    public static final String MODEL = "ELVIS";

    /** Description of the Field */
    protected static final boolean DO_FAST_PATH = false;

    /** Hasło użytkownika dostępu do bazy danych backendu */
    private static String user = "sa";

    /** Hasło użytkownika dostępu do bazy danych backendu */
    private static String password = "";

    /** Description of the Field */
    private static final String S_TYPE = "type";

    /** Has JenFace class been inited * */
    private static boolean inited = false;

    /** Obiekt odpowiedający za połącznie z bazą RDF */
    private IDBConnection dbcon = null;

    /** Model danych systemu Elvis */
    private Model model = null;

    /** Model ontologii systemu Elvis */
    private OntModel ontModel = null;

    /** Obiekt odpowiedzialny za odmierzanie czasu - utrzymywanie połącznia z bazą */
    private Timer timer = null;

    /**
	 *  Description of the Method
	 *
	 * @param  uri        Description of the Parameter
	 * @param  driver     Description of the Parameter
	 * @param  type       Description of the Parameter
	 * @param  _user      Description of the Parameter
	 * @param  _password  Description of the Parameter
	 */
    public static void init(String uri, String driver, String type, String _user, String _password) {
        synchronized (JenaFace.class) {
            DATABASE_URI = uri;
            DATABASE_DRIVER = driver;
            DATABASE_TYPE = type;
            user = _user;
            password = _password;
            inited = true;
            JenaFace.class.notifyAll();
        }
    }

    /**  Description of the Method */
    public static void init() {
        synchronized (JenaFace.class) {
            inited = true;
            JenaFace.class.notifyAll();
        }
    }

    /** Creates a new instance of JenaFace */
    public JenaFace() {
        synchronized (JenaFace.class) {
            while (!inited) {
                try {
                    JenaFace.class.wait();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        openDB();
        try {
            this.openModel();
        } catch (Exception ex) {
            this.createModel();
        }
        timer = new Timer(true);
        timer.schedule(this, 100, CONNECTION_TIMEOUT);
    }

    /**
	 * Ponieważ klasa ta implementuje <code>TimerTask</code> kolejne wywołania czasowe odwoływać się będą do tej metody.
	 */
    public void run() {
        synchronized (JenaFace.class) {
            if (!inited) {
                this.cancel();
            }
        }
        try {
            this.queryModel("pages", "10");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** otwieranie bazy RDF */
    private void openDB() {
        try {
            synchronized (JenaFace.class) {
                Class.forName(DATABASE_DRIVER);
            }
            dbcon = ModelFactory.createSimpleRDBConnection(DATABASE_URI, user, password, DATABASE_TYPE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** Zamykanie bazy RDF */
    public void closeDB() {
        try {
            dbcon.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
	 * Tworzenie nowego modelu w bazie
	 *
	 * @return    Model danych bazy RDF
	 */
    protected Model createModel() {
        try {
            ModelMaker maker = ModelFactory.createModelRDBMaker(dbcon);
            OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM_RDFS_INF);
            model = maker.createModel(MODEL);
            ontModel = ModelFactory.createOntologyModel(spec, model);
        } catch (Exception ex) {
            System.err.println("JenaFace[131](" + model + "):" + ex);
            ex.printStackTrace();
        }
        return model;
    }

    /**
	 * Otwieranie połączenia z modelem
	 *
	 * @return                Model danych bazy RDF
	 * @exception  Exception  Description of the Exception
	 */
    protected Model openModel() throws Exception {
        ModelMaker maker = ModelFactory.createModelRDBMaker(dbcon);
        OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM_RDFS_INF);
        model = maker.openModel(MODEL);
        ontModel = ModelFactory.createOntologyModel(spec, model);
        return model;
    }

    /**
	 * Czyszczenie zawartości modelu RDF
	 *
	 * @exception  com.hp.hpl.jena.rdf.model.RDFException  Description of the Exception
	 */
    public void clear() throws com.hp.hpl.jena.rdf.model.RDFException {
        this.model = (ModelRDB) this.getModel().remove(this.getModel());
    }

    /**
	 * Dodaje zdanie RDF do bazy
	 *
	 * @param  subject    Podmiot zdania
	 * @param  predicate  orzeczenie zdania
	 * @param  object     Description of the Parameter
	 * @return            Zasób reprezentujący dodane zdanie
	 */
    public Resource add(String subject, String predicate, String object) {
        Resource js = null;
        try {
            Property property;
            if (predicate.equals(S_TYPE)) {
                property = RDF.type;
            } else {
                property = this.getModel().getProperty(predicate);
            }
            js = getModel().getResource(subject).addProperty(property, object);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return js;
    }

    /**
	 * Dodaje zdanie do bazy - podmiot jest podawany jako zasób
	 *
	 * @param  js         Description of the Parameter
	 * @param  predicate  orzeczenie zdania
	 * @param  object     Description of the Parameter
	 * @return            Zasób reprezentujący dodane zdanie
	 */
    public Resource add(Resource js, String predicate, String object) {
        Property property = RDF.type;
        try {
            if (!predicate.equals(S_TYPE)) {
                property = this.getModel().getProperty(predicate);
            }
            js.addProperty(property, getModel().createLiteral(object));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return js;
    }

    /**
	 * Dodaje zdanie do bazy - podmiot i określenie są podawane jako zasób
	 *
	 * @param  js         Description of the Parameter
	 * @param  predicate  orzeczenie zdania
	 * @param  object     Description of the Parameter
	 * @return            Zasób reprezentujący dodane zdanie
	 */
    public Resource add(Resource js, String predicate, Resource object) {
        Property property = RDF.type;
        try {
            if (!predicate.equals(S_TYPE)) {
                property = this.getModel().getProperty(predicate);
            }
            js.addProperty(property, object);
        } catch (Exception ex) {
            System.err.println("JenaFace[234](No such predicate: " + predicate + ") " + ex);
        }
        return js;
    }

    /**
	 * Dodaje zdanie do bazy - określenie jest podawane jako zasób
	 *
	 * @param  subject    Podmiot zdania
	 * @param  predicate  orzeczenie zdania
	 * @param  object     Description of the Parameter
	 * @return            Zasób reprezentujący dodane zdanie
	 */
    public Resource add(String subject, String predicate, Resource object) {
        Resource js = null;
        try {
            Property property;
            if (predicate.equals(S_TYPE)) {
                property = RDF.type;
            } else {
                property = this.getModel().getProperty(predicate);
            }
            js = getModel().getResource(subject).addProperty(property, object);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return js;
    }

    /**
	 * Wysyła zapytanie RDQL
	 *
	 * @param  query  Zapytanie RDQL
	 * @return        Iterator z wynikami
	 */
    public QueryResults ask(String query) {
        QueryResults res = null;
        Query q = null;
        try {
            q = new Query(query);
            q.setSource(getModel());
            QueryExecution qe = new QueryEngine(q);
            res = qe.exec();
        } catch (com.hp.hpl.jena.rdql.QueryException qpar) {
            System.err.println("JenaFace[291](" + query + "):" + qpar);
            this.openDB();
            try {
                this.openModel();
                q.setSource(getModel());
                QueryExecution qe = new QueryEngine(q);
                res = qe.exec();
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
        } catch (Exception ex) {
            System.err.println("JenaFace[311](" + query + "):" + ex);
            ex.printStackTrace();
        }
        return res;
    }

    /**
	 * Setter for property user.
	 *
	 * @param  user  New value of property user.
	 */
    public void setUser(java.lang.String user) {
        synchronized (JenaFace.class) {
            JenaFace.user = user;
        }
    }

    /**
	 * Setter for property password.
	 *
	 * @param  password  New value of property password.
	 */
    public void setPassword(java.lang.String password) {
        synchronized (JenaFace.class) {
            JenaFace.password = password;
        }
    }

    /**
	 * Getter for property model.
	 *
	 * @return    Value of property model.
	 */
    public Model getModel() {
        synchronized (JenaFace.class) {
            if (this.model == null) {
                this.createModel();
            }
        }
        return model;
    }

    /**
	 * Getter for property model.
	 *
	 * @return    Value of property model.
	 */
    public OntModel getOntModel() {
        synchronized (JenaFace.class) {
            if (this.ontModel == null) {
                this.createModel();
            }
        }
        return this.ontModel;
    }

    /**
	 * Wysyła zapytanie do modelu danych w postaci pary [orzeczenie]:[określenie]
	 *
	 * @param  predicate  Orzeczenie
	 * @param  value      Określenie
	 * @return            Lista wyników zapytania
	 */
    public List queryModel(String predicate, String value) {
        ArrayList result = new ArrayList();
        String query = JenaQuery.getQuery((predicate.equals(S_TYPE)) ? 7 : 8, new Object[] { value, predicate });
        QueryResults it = this.ask(query);
        while (it.hasNext()) {
            result.add(it.next());
        }
        return result;
    }

    /**
	 * Sprawdza czy istnieje dany zasób o podanej klasie RDF i URI
	 *
	 * @param  type  Klasa zasobu (<tt><i>rdf</i>:Class</tt>)
	 * @param  uri   URI zasobu
	 * @return       <tt>true</tt> jeżeli zasób istnieje
	 */
    public boolean resourceExists(String type, String uri) {
        String query = JenaQuery.getQuery(6, new Object[] { type, uri });
        QueryResults results = this.ask(query);
        boolean result = results.hasNext();
        results.close();
        return result;
    }

    /**
	 * Tworzy mapę reprezentującą dany przez URI zasób.
	 *
	 * @param  uri                    URI zasobu do mapowania
	 * @param  withUri                Czy dopisywać do mapy uri wszystkich zasobów związanych z danym zasobem?
	 * @return                        Mapa reprezentująca dany zasób
	 * @exception  RDFException       Description of the Exception
	 * @throws  NullPointerException
	 */
    public Map loadResource(String uri, boolean withUri) throws RDFException, NullPointerException {
        HashMap result = new HashMap();
        Resource res = this.getModel().getResource(uri);
        StmtIterator props = res.listProperties();
        while (props.hasNext()) {
            Statement stm = props.nextStatement();
            Property prop = stm.getPredicate();
            RDFNode obj = stm.getObject();
            String sProp = prop.getLocalName();
            if (sProp.equals(S_TYPE)) {
                continue;
            }
            if (obj instanceof Literal) {
                String value = ((Literal) obj).getString();
                result.put(sProp, value);
            } else if (((Resource) obj).isAnon()) {
                StringBuffer sbNode = new StringBuffer();
                Resource resNode = (Resource) obj;
                StmtIterator stmtIt = resNode.listProperties();
                while (stmtIt.hasNext()) {
                    Statement stmNode = stmtIt.nextStatement();
                    if (stmNode.getPredicate().equals(com.hp.hpl.jena.vocabulary.RDF.type)) {
                        continue;
                    }
                    RDFNode node = stmNode.getObject();
                    if (node instanceof Literal) {
                        sbNode.append(((Literal) node).getString());
                    } else {
                        Statement stName = ((Resource) node).getProperty(this.getModel().getProperty("name"));
                        if (stName != null) {
                            RDFNode stObj = stName.getObject();
                            if (stObj instanceof Literal) {
                                if (withUri) {
                                    sbNode.append(((Resource) node).getURI()).append("_");
                                }
                                sbNode.append(((Literal) stObj).getString());
                            }
                        }
                    }
                    if (stmtIt.hasNext()) {
                        sbNode.append(",");
                    }
                }
                result.put(sProp, sbNode.toString());
            } else {
                Statement stName = ((Resource) obj).getProperty(this.getModel().getProperty("name"));
                if (stName != null) {
                    RDFNode stObj = stName.getObject();
                    if (stObj instanceof Literal) {
                        StringBuffer sbObj = new StringBuffer();
                        if (withUri) {
                            sbObj.append(((Resource) obj).getURI()).append("_");
                        }
                        sbObj.append(((Literal) stObj).getString());
                        result.put(sProp, sbObj.toString());
                    }
                }
            }
        }
        return result;
    }
}
