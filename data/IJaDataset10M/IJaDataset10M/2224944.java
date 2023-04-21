package org.arastreju.core.ontology.reasoning;

import info.aduna.iteration.CloseableIteration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.arastreju.core.ontology.query.QueryResult;
import org.arastreju.core.ontology.query.QueryResultBindingElem;
import org.openrdf.model.Graph;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.sail.SailException;
import org.openrdf.sail.inferencer.InferencerConnection;

public class OWLFeaturedInferencerConnection extends ForwardChainingRDFSInferencerConnection {

    /**
	 * Constructor
	 * @param con
	 */
    public OWLFeaturedInferencerConnection(InferencerConnection con) {
        super(con);
    }

    /**
	 * 
	 */
    protected int applyRuleInternal(int rule) throws SailException {
        int result = super.applyRuleInternal(rule);
        if (result != 0) return result;
        result += transitiveProperyRule1_1();
        result += symmetric1_1();
        result += inverse1_1();
        result += sameAs1_1();
        Iterator<Statement> ntIter = newThisIteration.match(null, null, null);
        while (ntIter.hasNext()) {
            Statement stmt = ntIter.next();
            int inferRuleCnt = 0;
            if ((inferRuleCnt = inverse2_1(stmt)) > 0) {
                result += inferRuleCnt;
                break;
            }
            if ((inferRuleCnt = sameAs2_1(stmt)) > 0) {
                result += inferRuleCnt;
                break;
            }
            if ((inferRuleCnt = transitivePropertyRule2_1(stmt)) > 0) {
                result += inferRuleCnt;
                break;
            }
        }
        return result;
    }

    /**
	 * 
	 */
    protected void addAxiomStatements() throws SailException {
        super.addAxiomStatements();
        Resource owlNamespace = new URIImpl(OWL.NAMESPACE);
        addInferredStatement(OWL.INVERSEOF, RDFS.RANGE, OWL.OBJECTPROPERTY, owlNamespace);
        addInferredStatement(OWL.INVERSEOF, RDFS.DOMAIN, OWL.OBJECTPROPERTY, owlNamespace);
        addInferredStatement(OWL.INVERSEOF, RDF.TYPE, OWL.SYMMETRICPROPERTY, owlNamespace);
        addInferredStatement(OWL.INVERSEOF, RDFS.LABEL, new LiteralImpl("inverseOf"), owlNamespace);
        addInferredStatement(OWL.TRANSITIVEPROPERTY, RDFS.SUBCLASSOF, OWL.OBJECTPROPERTY, owlNamespace);
    }

    /**
	 * 
	 */
    private int sameAs2_1(Statement stmt) throws SailException {
        int nofInferred = 0;
        CloseableIteration<? extends Statement, SailException> t1Iter;
        Resource subject = stmt.getSubject();
        t1Iter = this.getWrappedConnection().getStatements(subject, OWL.SAMEAS, null, true);
        while (t1Iter.hasNext()) {
            boolean add = this.addInferredStatement(new URIImpl(t1Iter.next().getObject().stringValue()), stmt.getPredicate(), stmt.getObject());
            if (add) {
                nofInferred++;
            }
        }
        t1Iter.close();
        return nofInferred;
    }

    /**
	 * 
	 * @return
	 * @throws SailException
	 */
    private int sameAs1_1() throws SailException {
        int nofInferred = 0;
        Iterator<Statement> ntIter = newThisIteration.match(null, OWL.SAMEAS, null);
        CloseableIteration<? extends Statement, SailException> t1Iter;
        while (ntIter.hasNext()) {
            Statement stmt = ntIter.next();
            Resource subject = stmt.getSubject();
            Value object = stmt.getObject();
            if (object instanceof Resource) {
                boolean add = this.addInferredStatement((Resource) object, OWL.SAMEAS, subject);
                if (add) nofInferred++;
                t1Iter = this.getWrappedConnection().getStatements((Resource) object, null, null, true);
                while (t1Iter.hasNext()) {
                    Statement stmtToDuplicate = t1Iter.next();
                    if (!subject.stringValue().equals(stmtToDuplicate.getObject().stringValue())) {
                        add = this.addInferredStatement(subject, stmtToDuplicate.getPredicate(), stmtToDuplicate.getObject());
                        if (add) nofInferred++;
                    }
                }
                t1Iter.close();
            }
        }
        return nofInferred;
    }

    /**
	 * 
	 * @return
	 * @throws SailException
	 */
    private int symmetric1_1() throws SailException {
        int nofInferred = 0;
        Iterator<Statement> ntIter = newThisIteration.match(null, RDF.TYPE, OWL.SYMMETRICPROPERTY);
        while (ntIter.hasNext()) {
            Resource property = ntIter.next().getSubject();
            boolean add = this.addInferredStatement(property, OWL.INVERSEOF, property);
            if (add) nofInferred++;
        }
        return nofInferred;
    }

    /**
	 * 
	 * @param inverseProperties
	 * @return
	 * @throws SailException
	 */
    private int inverse2_1(Statement inverseProperties) throws SailException {
        int nofInferred = 0;
        CloseableIteration<? extends Statement, SailException> t1Iter;
        Resource subject = inverseProperties.getSubject();
        Value object = inverseProperties.getObject();
        Resource prop = inverseProperties.getPredicate();
        t1Iter = this.getWrappedConnection().getStatements(prop, OWL.INVERSEOF, null, true);
        while (t1Iter.hasNext()) {
            Value prop2 = t1Iter.next().getObject();
            if (object instanceof Resource) {
                boolean add = this.addInferredStatement(new URIImpl(object.stringValue()), new URIImpl(prop2.stringValue()), subject);
                if (add) nofInferred++;
            }
        }
        t1Iter.close();
        return nofInferred;
    }

    /**
	 * 
	 * @return
	 * @throws SailException
	 */
    private int inverse1_1() throws SailException {
        int nofInferred = 0;
        CloseableIteration<? extends Statement, SailException> t1Iter;
        Iterator<Statement> ntIter = newThisIteration.match(null, OWL.INVERSEOF, null);
        while (ntIter.hasNext()) {
            Statement propertyStatement = ntIter.next();
            Resource propertySubject = propertyStatement.getSubject();
            Value propertyObject = propertyStatement.getObject();
            if (propertyObject instanceof Resource) {
                boolean add = this.addInferredStatement(new URIImpl(propertyObject.stringValue()), OWL.INVERSEOF, propertySubject);
                if (add) nofInferred++;
            }
            t1Iter = getWrappedConnection().getStatements(null, new URIImpl(propertyObject.stringValue()), null, true);
            while (t1Iter.hasNext()) {
                Statement statement = t1Iter.next();
                Resource subject = statement.getSubject();
                Value object = statement.getObject();
                if (object instanceof Resource) {
                    boolean add = this.addInferredStatement(new URIImpl(object.stringValue()), new URIImpl(propertySubject.stringValue()), subject);
                    if (add) nofInferred++;
                }
            }
            t1Iter.close();
        }
        return nofInferred;
    }

    /**
	 * 
	 */
    private int transitiveProperyRule1_1() throws SailException {
        int nofInferred = 0;
        CloseableIteration<? extends Statement, SailException> t1Iter;
        t1Iter = getWrappedConnection().getStatements(null, RDF.TYPE, OWL.TRANSITIVEPROPERTY, true);
        while (t1Iter.hasNext()) {
            Resource property = t1Iter.next().getSubject();
            Iterator<Statement> ntIter = newThisIteration.match(null, new URIImpl(property.stringValue()), null);
            while (ntIter.hasNext()) {
                Statement stmt = ntIter.next();
                Resource subject = stmt.getSubject();
                Value object = stmt.getObject();
                if (object instanceof Resource) {
                    CloseableIteration<? extends Statement, SailException> t2Iter;
                    t2Iter = getWrappedConnection().getStatements(null, new URIImpl(property.stringValue()), subject, true);
                    while (t2Iter.hasNext()) {
                        Statement stmt2 = t2Iter.next();
                        if (!(stmt2.getSubject().stringValue().equals(object.stringValue()))) {
                            boolean added = addInferredStatement(stmt2.getSubject(), new URIImpl(property.stringValue()), object);
                            if (added) {
                                nofInferred++;
                            }
                        }
                    }
                    t2Iter.close();
                    t2Iter = getWrappedConnection().getStatements((Resource) object, new URIImpl(property.stringValue()), null, true);
                    while (t2Iter.hasNext()) {
                        Statement stmt2 = t2Iter.next();
                        if (!(subject.stringValue().equals(stmt2.getObject().stringValue()))) {
                            boolean added = addInferredStatement(subject, new URIImpl(property.stringValue()), stmt2.getObject());
                            if (added) {
                                nofInferred++;
                            }
                        }
                    }
                }
            }
        }
        t1Iter.close();
        return nofInferred;
    }

    /**
	 * calculate transitive order by new 'flushed' transitive property assignments
	 * @return int, number of inferred statements by this rule
	 */
    private int transitivePropertyRule2_1(Statement stmt) throws SailException {
        int nofInferred = 0;
        if (!(stmt.getPredicate().stringValue().equals(RDF.TYPE) && stmt.getObject().stringValue().equals(OWL.TRANSITIVEPROPERTY.stringValue()))) return nofInferred;
        Resource property = stmt.getSubject();
        CloseableIteration<? extends Statement, SailException> t1Iter;
        t1Iter = getWrappedConnection().getStatements(null, new URIImpl(property.stringValue()), null, true);
        while (t1Iter.hasNext()) {
            Statement t1 = t1Iter.next();
            Resource yyy = t1.getSubject();
            Value zzz = t1.getObject();
            if (zzz instanceof Resource) {
                CloseableIteration<? extends Statement, SailException> t2Iter;
                t2Iter = getWrappedConnection().getStatements(null, new URIImpl(property.stringValue()), yyy, true);
                while (t2Iter.hasNext()) {
                    Statement t2 = t2Iter.next();
                    Resource xxx = t2.getSubject();
                    if (!(xxx.stringValue().equals(zzz.stringValue()))) {
                        boolean added = addInferredStatement(xxx, new URIImpl(property.stringValue()), zzz);
                        if (added) {
                            nofInferred++;
                        }
                    }
                }
                t2Iter.close();
                t2Iter = getWrappedConnection().getStatements(new URIImpl(zzz.stringValue()), new URIImpl(property.stringValue()), null, true);
                while (t2Iter.hasNext()) {
                    Statement t2 = t2Iter.next();
                    Value xxx = t2.getObject();
                    if (!(xxx.stringValue().equals(yyy.stringValue()))) {
                        if (xxx instanceof Resource) {
                            boolean added = addInferredStatement(yyy, new URIImpl(property.stringValue()), new URIImpl(xxx.stringValue()));
                            if (added) nofInferred++;
                        }
                    }
                }
                t2Iter.close();
                t1Iter.close();
            }
        }
        return nofInferred;
    }
}
