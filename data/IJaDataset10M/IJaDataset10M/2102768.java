package org.arastreju.core.ontology.odql;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * [TODO insert description here.]
 * 
 * Created: 08.08.2009
 *
 * @author Oliver Tigges
 */
public class ASTSchemaAssignment extends SimpleNode implements OdqlStatement {

    public boolean abstractSchema;

    public ASTSchemaAssignment(int id) {
        super(id);
    }

    public StatementType getType() {
        return StatementType.DEFINE_STATEMENT_SCHEMA;
    }

    public String getSchemaName() {
        ASTName name = (ASTName) jjtGetChild(0);
        return name.getQualifiedName();
    }

    public List<BasicActellonExpression> getActellonExpressions() {
        List<BasicActellonExpression> result = new ArrayList<BasicActellonExpression>();
        result.addAll(getExpressionPattern().getActellonExpressions());
        result.addAll(getExpressionPattern().getAdverbialExpressions());
        return result;
    }

    public List<ASTAdverbExpression> getAdverbExpressions() {
        return getExpressionPattern().getAdverbExpressions();
    }

    public List<ASTActellonSchemaChoice> getActellonChoices() {
        return getExpressionPattern().getActellonChoices();
    }

    public List<ImplicationDefinition> getImplications() {
        List<ImplicationDefinition> defs = new ArrayList<ImplicationDefinition>();
        ASTImplicationDefinition def = (ASTImplicationDefinition) getChildByType(JJTIMPLICATIONDEFINITION);
        if (def != null) {
            defs.addAll(def.getAssociateImplications());
            defs.addAll(def.getEqualizeImplications());
            defs.addAll(def.getFunctionImplications());
        }
        return defs;
    }

    public boolean isAbstract() {
        return abstractSchema;
    }

    @SuppressWarnings("unchecked")
    public Set<String> getExtends() {
        Set<String> result = new HashSet<String>();
        List<ASTExtension> extensionNodes = getChildrenByType(JJTEXTENSION);
        for (ASTExtension extension : extensionNodes) {
            result.add(extension.name);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public Set<String> getImplements() {
        Set<String> result = new HashSet<String>();
        List<ASTImplementation> implementNodes = getChildrenByType(JJTIMPLEMENTATION);
        for (ASTImplementation current : implementNodes) {
            result.add(current.name);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (abstractSchema) {
            sb.append("abstract ");
        }
        sb.append(getSchemaName() + " := " + getExpressionPattern());
        if (!getImplications().isEmpty()) {
            sb.append(" ==> " + getImplications());
        }
        return sb.toString();
    }

    private ASTExpressionPattern getExpressionPattern() {
        return (ASTExpressionPattern) getChildByType(JJTEXPRESSIONPATTERN);
    }
}
