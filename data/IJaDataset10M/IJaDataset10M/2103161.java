package gridrm.onto.teamMeta;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.ibm.adtech.jastor.JastorException;
import com.ibm.adtech.jastor.JastorInvalidRDFNodeException;
import com.ibm.adtech.jastor.util.Util;

/**
 * Implementation of {@link gridrm.onto.teamMeta.TeamMeta}
 * Use the gridrm.onto.teamMeta.TeamMetaFactory to create instances of this class.
 * <p>(URI: http://gridagents.sourceforge.net/TeamMeta#TeamMeta)</p>
 * <br>
 */
public class TeamMetaImpl extends com.ibm.adtech.jastor.ThingImpl implements gridrm.onto.teamMeta.TeamMeta {

    private static com.hp.hpl.jena.rdf.model.Property teamPasswordProperty = ResourceFactory.createProperty("http://gridagents.sourceforge.net/TeamMeta#teamPassword");

    TeamMetaImpl(Resource resource, Model model) throws JastorException {
        super(resource, model);
    }

    static TeamMetaImpl getTeamMeta(Resource resource, Model model) throws JastorException {
        if (!model.contains(resource, RDF.type, TeamMeta.TYPE)) return null;
        return new TeamMetaImpl(resource, model);
    }

    static TeamMetaImpl createTeamMeta(Resource resource, Model model) throws JastorException {
        TeamMetaImpl impl = new TeamMetaImpl(resource, model);
        if (!impl._model.contains(new com.hp.hpl.jena.rdf.model.impl.StatementImpl(impl._resource, RDF.type, TeamMeta.TYPE))) impl._model.add(impl._resource, RDF.type, TeamMeta.TYPE);
        impl.addSuperTypes();
        impl.addHasValueValues();
        return impl;
    }

    void addSuperTypes() {
    }

    void addHasValueValues() {
    }

    public java.util.List listStatements() {
        java.util.List list = new java.util.ArrayList();
        StmtIterator it = null;
        it = _model.listStatements(_resource, teamPasswordProperty, (RDFNode) null);
        while (it.hasNext()) {
            list.add(it.next());
        }
        it = _model.listStatements(_resource, RDF.type, gridrm.onto.teamMeta.TeamMeta.TYPE);
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }

    public java.lang.String getTeamPassword() throws JastorException {
        com.hp.hpl.jena.rdf.model.Statement stmt = _model.getProperty(_resource, teamPasswordProperty);
        if (stmt == null) return null;
        if (!stmt.getObject().canAs(com.hp.hpl.jena.rdf.model.Literal.class)) throw new JastorInvalidRDFNodeException(uri() + ": teamPassword getProperty() in gridrm.onto.teamMeta.TeamMeta model not Literal", stmt.getObject());
        com.hp.hpl.jena.rdf.model.Literal literal = (com.hp.hpl.jena.rdf.model.Literal) stmt.getObject().as(com.hp.hpl.jena.rdf.model.Literal.class);
        Object obj = Util.fixLiteral(literal.getValue(), "java.lang.String");
        return (java.lang.String) obj;
    }

    public void setTeamPassword(java.lang.String teamPassword) throws JastorException {
        if (_model.contains(_resource, teamPasswordProperty)) {
            _model.removeAll(_resource, teamPasswordProperty, null);
        }
        if (teamPassword != null) {
            _model.add(_model.createStatement(_resource, teamPasswordProperty, _model.createTypedLiteral(teamPassword)));
        }
    }
}
