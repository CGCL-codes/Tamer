package plexil;

import java.util.Vector;
import net.n3.nanoxml.*;

public class GlobalDeclaration extends PlexilName {

    protected NameType m_declarationType;

    protected Vector<VariableName> m_paramSpecs;

    protected Vector<VariableName> m_returnSpecs;

    public GlobalDeclaration(PlexilTreeNode declaration, String myName, NameType declType, Vector<VariableName> paramSpecs, Vector<VariableName> returnSpecs) {
        super(myName, declType, declaration);
        m_declarationType = declType;
        m_paramSpecs = paramSpecs;
        m_returnSpecs = returnSpecs;
    }

    public PlexilDataType getReturnType() {
        if (m_returnSpecs == null) return null;
        return m_returnSpecs.firstElement().getVariableType();
    }

    public Vector<PlexilDataType> getReturnTypes() {
        if (m_returnSpecs == null) return null;
        Vector<PlexilDataType> result = new Vector<PlexilDataType>();
        for (VariableName v : m_returnSpecs) result.add(v.getVariableType());
        return result;
    }

    public Vector<VariableName> getReturnVariables() {
        return m_returnSpecs;
    }

    public VariableName getReturnVariable() {
        if (m_returnSpecs != null && m_returnSpecs.size() != 0) return m_returnSpecs.firstElement(); else return null;
    }

    public Vector<PlexilDataType> getParameterTypes() {
        if (m_paramSpecs == null) return null;
        Vector<PlexilDataType> result = new Vector<PlexilDataType>();
        for (VariableName v : m_paramSpecs) result.add(v.getVariableType());
        return result;
    }

    public Vector<VariableName> getParameterVariables() {
        return m_paramSpecs;
    }

    public VariableName getParameterByName(String name) {
        if (m_paramSpecs == null) return null;
        for (VariableName candidate : m_paramSpecs) if (name.equals(candidate.getName())) return candidate;
        return null;
    }

    public boolean hasParameterNamed(String name) {
        return (getParameterByName(name) != null);
    }
}
