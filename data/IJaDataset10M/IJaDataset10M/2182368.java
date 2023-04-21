package org.cantaloop.cgimlet.lang.java;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.cantaloop.cgimlet.lang.*;
import org.cantaloop.cgimlet.lang.CGUtils;
import org.cantaloop.cgimlet.lang.Expr;
import org.cantaloop.cgimlet.lang.MethodTemplate;
import org.cantaloop.cgimlet.lang.Modifier;
import org.cantaloop.cgimlet.lang.Type;

/**
 *
 * @created: Fri Dec  7 19:15:18 2001
 * @author <a href="mailto:david@cantaloop.org">David Leuschner</a>
 * @author <a href="mailto:stefan@cantaloop.org">Stefan Heimann</a>
 * @version @version@ ($Revision: 1.11 $)
 */
public class JMethodTemplate extends JConstructorTemplate implements MethodTemplate {

    protected Type m_returnType;

    private CGUtils m_utils;

    /**
   * Creates a new <code>JMethodTemplate</code> instance.
   *
   * @param name the name of the method.
   */
    public JMethodTemplate(String name) {
        super(name);
        m_returnType = DESCR.getVoidType();
        m_modifier.setAccess(Modifier.Access.PUBLIC);
        m_utils = JUtils.getInstance();
    }

    public JMethodTemplate(String name, Type type1, String name1) {
        this(name);
        addParameter(type1, name1);
    }

    public JMethodTemplate(String name, Type type1, String name1, Type type2, String name2) {
        this(name);
        addParameter(type1, name1);
        addParameter(type2, name2);
    }

    protected String getHelperMethodName() {
        return m_name + "Helper" + (m_helperMethods.size() + 1);
    }

    public Type getReturnType() {
        return m_returnType;
    }

    public void setReturnType(Type type) {
        this.m_returnType = type;
    }

    public String invokeExpr(String param1) {
        return m_utils.invokeExpr(getName(), param1);
    }

    public String invokeExpr(String param1, String param2) {
        return m_utils.invokeExpr(getName(), param1, param2);
    }

    public String invokeExpr() {
        return m_utils.invokeExpr(getName());
    }

    public String invokeExpr(String[] params) {
        return m_utils.invokeExpr(getName(), params);
    }

    public String invoke(String param1) {
        return m_utils.invoke(getName(), param1);
    }

    public String invoke(String param1, String param2) {
        return m_utils.invoke(getName(), param1, param2);
    }

    public String invoke() {
        return m_utils.invoke(getName());
    }

    public String invoke(String[] params) {
        return m_utils.invoke(getName(), params);
    }

    public String invokeOnExpr(String varName, String param1) {
        return m_utils.invokeOnExpr(varName, getName(), param1);
    }

    public String invokeOnExpr(String varName, String param1, String param2) {
        return m_utils.invokeOnExpr(varName, getName(), param1, param2);
    }

    public String invokeOnExpr(String varName) {
        return m_utils.invokeOnExpr(varName, getName());
    }

    public String invokeOnExpr(String varName, String[] params) {
        return m_utils.invokeOnExpr(varName, getName(), params);
    }

    public String invokeOn(String varName, String param1) {
        return m_utils.invokeOn(varName, getName(), param1);
    }

    public String invokeOn(String varName, String param1, String param2) {
        return m_utils.invokeOn(varName, getName(), param1, param2);
    }

    public String invokeOn(String varName) {
        return m_utils.invokeOn(varName, getName());
    }

    public String invokeOn(String varName, String[] params) {
        return m_utils.invokeOn(varName, getName(), params);
    }

    public String getCode() {
        StringBuffer sb = new StringBuffer();
        sb.append(m_modifier.getCode() + " ");
        sb.append(m_returnType + " ");
        sb.append(m_name);
        sb.append(parameterToString());
        sb.append(" {\n");
        sb.append(getBody());
        if (m_body.length() > 0 && m_body.charAt(m_body.length() - 1) != '\n') {
            sb.append("\n");
        }
        sb.append("}\n\n");
        for (Iterator it = m_helperMethods.iterator(); it.hasNext(); ) {
            MethodTemplate tmpl = (MethodTemplate) it.next();
            sb.append(tmpl.getCode());
        }
        return sb.toString();
    }

    public int getParameterCount() {
        return m_params.size();
    }

    public List getParameters() {
        return Collections.unmodifiableList(m_params);
    }

    public void addReturnStatement(Expr rootVar) {
        append("return " + ((JExpression) rootVar).getExpressionCode() + ";");
    }
}
