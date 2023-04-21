package fr.loria.ecoo.sesameEngine.op;

import info.aduna.iteration.Iterations;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.openrdf.OpenRDFException;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.BNodeImpl;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.nativerdf.NativeStore;

public class SesameOp {

    Repository sesameRep;

    private static String viewpageservletname;

    public SesameOp(String path, String viewpageservletname) throws Exception {
        File dataDir = new File(path);
        Repository rep = new SailRepository(new NativeStore(dataDir));
        rep.initialize();
        this.sesameRep = rep;
        SesameOp.viewpageservletname = viewpageservletname;
    }

    public String getSemanticPattern() {
        return "(\\[[^\\[\\]]*::[^\\[\\]]*])";
    }

    public void addStmt(String subj, String predicat, String value) throws OpenRDFException {
        ValueFactory f = this.sesameRep.getValueFactory();
        URI s = f.createURI(subj);
        URI p = f.createURI(predicat);
        Value v;
        Resource[] temp = new Resource[0];
        RepositoryConnection con = this.sesameRep.getConnection();
        try {
            v = f.createURI(value);
        } catch (IllegalArgumentException e) {
            v = f.createLiteral(value);
        }
        int n = 0;
        if (con.hasStatement(s, p, v, false, (Resource[]) temp)) {
            RepositoryResult st = con.getStatements(s, p, v, false, temp);
            try {
                Statement stmt = (Statement) st.next();
                BNodeImpl nb = (BNodeImpl) stmt.getContext();
                n = new Integer(nb.getID()).intValue();
                n++;
                nb = new BNodeImpl(new Integer(n).toString());
                con.remove(con.getStatements(s, p, v, false, temp), temp);
                con.add(s, p, v, (Resource[]) new BNodeImpl[] { nb });
            } catch (Exception ex) {
                System.err.print(ex.getMessage());
            }
        } else {
            n = 1;
            con.add(s, p, v, (Resource[]) new BNodeImpl[] { new BNodeImpl("1") });
        }
        con.close();
    }

    public void delRessource(String subj) throws RepositoryException {
        RepositoryConnection con = this.sesameRep.getConnection();
        ValueFactory f = this.sesameRep.getValueFactory();
        URI s = f.createURI(subj);
        Resource[] temp = {};
        con.remove(con.getStatements(s, null, null, true, temp), temp);
        con.remove(con.getStatements(null, s, null, true, temp), temp);
        con.remove(con.getStatements(null, null, s, true, temp), temp);
        con.close();
    }

    public void delRessource(String sub, String predicat, String value) throws RuntimeException {
        try {
            RepositoryConnection con = this.sesameRep.getConnection();
            ValueFactory f = this.sesameRep.getValueFactory();
            URI s = f.createURI(sub);
            URI p = f.createURI(predicat);
            int n = 0;
            Resource[] temp = {};
            Value v;
            try {
                v = f.createURI(value);
            } catch (IllegalArgumentException e) {
                v = f.createLiteral(value);
            }
            if (con.hasStatement(s, p, v, false, (Resource[]) temp)) {
                RepositoryResult st = con.getStatements(s, p, v, false, temp);
                try {
                    Statement stmt = (Statement) st.next();
                    BNodeImpl nb = (BNodeImpl) stmt.getContext();
                    n = new Integer(nb.getID()).intValue();
                    n--;
                    con.remove(con.getStatements(s, p, v, true, temp), temp);
                    nb = new BNodeImpl(new Integer(n).toString());
                    if (n > 0) {
                        con.add(s, p, v, (Resource[]) new BNodeImpl[] { nb });
                    }
                } catch (Exception e1) {
                    System.out.println(e1.getMessage());
                }
            }
            con.close();
        } catch (RepositoryException e) {
            throw new RuntimeException("Error ");
        }
    }

    public StringBuffer getURIFactAbout(String pageName) throws Exception {
        RepositoryConnection con = this.sesameRep.getConnection();
        ValueFactory f = this.sesameRep.getValueFactory();
        URI subj = f.createURI(pageName);
        Resource[] temp = new Resource[0];
        RepositoryResult<Statement> statements = con.getStatements(subj, null, null, true);
        List<Statement> about = Iterations.addAll(statements, new ArrayList<Statement>());
        StringBuffer bw = new StringBuffer();
        for (Statement s : about) {
            Statement st = con.getStatements(s.getSubject(), s.getPredicate(), s.getObject(), false, temp).next();
            BNodeImpl bn = (BNodeImpl) st.getContext();
            bw.append("[" + s.getPredicate() + "] : [" + s.getObject().toString() + "] (" + bn.getID() + ")");
            bw.append(System.getProperty("line.separator"));
            bw.append(System.getProperty("line.separator"));
        }
        return bw;
    }

    public Vector getFactAboutList(String pageName) throws Exception {
        RepositoryConnection con = this.sesameRep.getConnection();
        ValueFactory f = this.sesameRep.getValueFactory();
        URI subj = f.createURI(pageName);
        RepositoryResult<Statement> statements = con.getStatements(subj, null, null, true);
        List<Statement> about = Iterations.addAll(statements, new ArrayList<Statement>());
        Vector<Object> result = new Vector<Object>();
        for (Statement s : about) {
            URI temp = s.getPredicate();
            int beginIndex = temp.getNamespace().length() + viewpageservletname.length() - 1;
            String temp1 = "[" + temp.toString().substring(beginIndex);
            temp1 += "::";
            temp1 += s.getObject().toString().substring(beginIndex);
            temp1 += "]";
            result.add(temp1);
        }
        return result;
    }

    public String execQuery(String query) throws Exception {
        RepositoryConnection con = this.sesameRep.getConnection();
        String queryString = query;
        TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
        TupleQueryResult result = tupleQuery.evaluate();
        Object[] colName = result.getBindingNames().toArray();
        String[] nameCol = new String[colName.length];
        for (int i = 0; i < colName.length; i++) {
            nameCol[i] = (String) colName[i];
        }
        StringBuffer sb = new StringBuffer();
        while (result.hasNext()) {
            BindingSet bindingSet = result.next();
            for (int i = 0; i < colName.length; i++) {
                sb.append("[" + bindingSet.getValue(nameCol[i]).toString() + "]" + "\t");
            }
            sb.append(System.getProperty("line.separator"));
            sb.append(System.getProperty("line.separator"));
        }
        result.close();
        con.close();
        return sb.toString();
    }
}
