package com.sodad.weka.core.pmml;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.sodad.weka.core.Attribute;

/**
 * Class encapsulating an Apply Expression.
 * 
 * @author Mark Hall (mhall{[at]}pentaho{[dot]}com)
 * @version $Revision 1.0 $
 */
class Apply extends Expression {

    /**
   * For serialization
   */
    private static final long serialVersionUID = -2790648331300695083L;

    /** The list of arguments the function encapsulated in this Apply Expression */
    protected ArrayList<Expression> m_arguments = new ArrayList<Expression>();

    /** The function to apply (either built-in or a DefineFunction) */
    protected Function m_function = null;

    /** The structure of the result of Apply Expression */
    protected Attribute m_outputStructure = null;

    /**
   * Constructor. Reads the function name and argument Expressions for
   * this Apply Expression.
   * 
   * @param apply the Element encapsulating this Apply
   * @param opType the optype for this expression (taken from either the 
   * enclosing DefineFunction or DerivedField)
   * @param fieldDefs an ArrayList of Attributes for the fields that this
   * Expression might need to access
   * @param transDict the TransformationDictionary (may be null if there is
   * no dictionary)
   * @throws Exception if there is a problem parsing this Apply Expression
   */
    protected Apply(Element apply, FieldMetaInfo.Optype opType, ArrayList<Attribute> fieldDefs, TransformationDictionary transDict) throws Exception {
        super(opType, fieldDefs);
        String functionName = apply.getAttribute("function");
        if (functionName == null || functionName.length() == 0) {
            functionName = apply.getAttribute("name");
        }
        if (functionName == null || functionName.length() == 0) {
            throw new Exception("[Apply] No function name specified!!");
        }
        m_function = Function.getFunction(functionName, transDict);
        NodeList children = apply.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String tagName = ((Element) child).getTagName();
                if (!tagName.equals("Extension")) {
                    Expression tempExpression = Expression.getExpression(tagName, child, m_opType, m_fieldDefs, transDict);
                    if (tempExpression != null) {
                        m_arguments.add(tempExpression);
                    }
                }
            }
        }
        if (fieldDefs != null) {
            updateDefsForArgumentsAndFunction();
        }
    }

    public void setFieldDefs(ArrayList<Attribute> fieldDefs) throws Exception {
        super.setFieldDefs(fieldDefs);
        updateDefsForArgumentsAndFunction();
    }

    private void updateDefsForArgumentsAndFunction() throws Exception {
        for (int i = 0; i < m_arguments.size(); i++) {
            m_arguments.get(i).setFieldDefs(m_fieldDefs);
        }
        ArrayList<Attribute> functionFieldDefs = new ArrayList<Attribute>(m_arguments.size());
        for (int i = 0; i < m_arguments.size(); i++) {
            functionFieldDefs.add(m_arguments.get(i).getOutputDef());
        }
        m_function.setParameterDefs(functionFieldDefs);
        m_outputStructure = m_function.getOutputDef();
    }

    /**
   * Get the result of evaluating the expression. In the case
   * of a continuous optype, a real number is returned; in
   * the case of a categorical/ordinal optype, the index of the nominal
   * value is returned as a double.
   * 
   * @param incoming the incoming parameter values
   * @return the result of evaluating the expression
   * @throws Exception if there is a problem computing the result
   */
    public double getResult(double[] incoming) throws Exception {
        double[] functionIncoming = new double[m_arguments.size()];
        for (int i = 0; i < m_arguments.size(); i++) {
            functionIncoming[i] = m_arguments.get(i).getResult(incoming);
        }
        double result = m_function.getResult(functionIncoming);
        return result;
    }

    /**
   * Get the result of evaluating the expression for continuous
   * optype. Is the same as calling getResult() when the optype
   * is continuous.
   * 
   * @param incoming the incoming parameter values
   * mining schema
   * @return the result of evaluating the expression.
   * @throws Exception if the optype is not continuous.
   */
    public String getResultCategorical(double[] incoming) throws Exception {
        if (m_opType == FieldMetaInfo.Optype.CONTINUOUS) {
            throw new IllegalArgumentException("[Apply] Can't return result as " + "categorical/ordinal because optype is continuous!");
        }
        double result = getResult(incoming);
        return m_outputStructure.value((int) result);
    }

    /**
   * Return the structure of the result of applying this Expression
   * as an Attribute.
   * 
   * @return the structure of the result of applying this Expression as an
   * Attribute.
   */
    public Attribute getOutputDef() {
        if (m_outputStructure == null) {
            return (m_opType == FieldMetaInfo.Optype.CATEGORICAL || m_opType == FieldMetaInfo.Optype.ORDINAL) ? new Attribute("Placeholder", new ArrayList<String>()) : new Attribute("Placeholder");
        }
        return m_outputStructure;
    }

    public String toString(String pad) {
        StringBuffer buff = new StringBuffer();
        String[] parameterNames = null;
        buff.append(pad + "Apply [" + m_function.toString() + "]:\n");
        buff.append(pad + "args:");
        if (m_function instanceof DefineFunction) {
            parameterNames = m_function.getParameterNames();
        }
        for (int i = 0; i < m_arguments.size(); i++) {
            Expression e = m_arguments.get(i);
            buff.append("\n" + ((parameterNames != null) ? pad + parameterNames[i] + " = " : "") + e.toString(pad + "  "));
        }
        return buff.toString();
    }
}
