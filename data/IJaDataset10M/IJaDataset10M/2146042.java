package au.edu.diasb.annotation.danno.impl.jena;

import au.edu.diasb.annotation.danno.model.RDFContainer;
import au.edu.diasb.annotation.danno.model.RDFLiteral;
import au.edu.diasb.annotation.danno.model.RDFStatement;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

public class JenaRDFStatement implements RDFStatement {

    private final Statement statement;

    private final JenaRDFContainer container;

    private final JenaAnnoteaTypeFactory factory;

    public JenaRDFStatement(Statement statement, JenaAnnoteaTypeFactory factory, JenaRDFContainer container) {
        this.statement = statement;
        this.factory = factory;
        this.container = container;
    }

    public String getSubjectURI() {
        return statement.getSubject().getURI();
    }

    public String getSubjectId() {
        Resource resource = statement.getSubject();
        if (resource.getURI() != null) {
            return null;
        } else {
            return resource.getId().getLabelString();
        }
    }

    public String getSubject() {
        Resource subject = statement.getSubject();
        String res = subject.getURI();
        if (res == null) {
            AnonId id = statement.getSubject().getId();
            res = id.getLabelString();
        }
        return res;
    }

    public String getPredicateURI() {
        return statement.getPredicate().toString();
    }

    public JenaRDFResource getObjectAsResource() {
        RDFNode object = statement.getObject();
        if (object instanceof Resource) {
            return new JenaRDFResource((Resource) object, factory);
        } else {
            return null;
        }
    }

    public RDFLiteral getObjectLiteral() {
        RDFNode object = statement.getObject();
        if (object.isLiteral()) {
            return new JenaRDFLiteral((Literal) object);
        } else {
            return null;
        }
    }

    public boolean isObjectLiteral() {
        return statement.getObject().isLiteral();
    }

    public String getObjectId() {
        RDFNode object = statement.getObject();
        if (object.isAnon()) {
            return ((Resource) object).getId().getLabelString();
        } else {
            return null;
        }
    }

    public boolean isObjectBlankNode() {
        return statement.getObject().isAnon();
    }

    public boolean isSubjectBlankNode() {
        return statement.getSubject().isAnon();
    }

    public String getObjectURI() {
        RDFNode object = statement.getObject();
        if (object.isURIResource()) {
            return ((Resource) object).getURI();
        } else {
            return null;
        }
    }

    public boolean isObjectURI() {
        return statement.getObject().isURIResource();
    }

    public String getObject() {
        RDFNode object = statement.getObject();
        if (object.isURIResource()) {
            return ((Resource) object).getURI();
        } else if (object.isAnon()) {
            return ((Resource) object).getId().getLabelString();
        } else {
            return ((Literal) object).getString();
        }
    }

    public String getAnnoteaTypeName() {
        return "RDF Statement";
    }

    Statement getStatement() {
        return statement;
    }

    @Override
    public RDFContainer getContainer() {
        return container;
    }

    public String toString() {
        return "JenaRDFStatement{subject=" + statement.getSubject().toString() + ",predicate=" + statement.getPredicate().toString() + ",object=" + statement.getObject().toString() + "}";
    }
}
