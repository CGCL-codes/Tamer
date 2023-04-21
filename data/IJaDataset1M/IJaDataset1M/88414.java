package net.sf.kfgodel.bean2bean.interpreters.ognl;

import java.util.HashMap;
import java.util.Map;
import net.sf.kfgodel.bean2bean.exceptions.BadMappingException;
import net.sf.kfgodel.bean2bean.exceptions.MissingPropertyException;
import net.sf.kfgodel.bean2bean.instantiation.ObjectFactory;
import net.sf.kfgodel.bean2bean.interpreters.ExpressionInterpreter;
import net.sf.kfgodel.dgarcia.lang.reflection.ReflectionUtils;
import ognl.NoSuchPropertyException;
import ognl.Ognl;
import ognl.OgnlException;

/**
 * Esta clase es un interprete de expresiones utilizando el lenguaje de OGNL
 * 
 * @author D.Garcia
 */
public class OgnlExpressionInterpreter implements ExpressionInterpreter {

    /**
	 * @see net.sf.kfgodel.bean2bean.interpreters.ExpressionInterpreter#precompile(String,
	 *      ObjectFactory)
	 */
    public Object precompile(String expression, ObjectFactory objectFactory) {
        try {
            return Ognl.parseExpression(expression);
        } catch (OgnlException e) {
            throw new BadMappingException("Expression[\"" + expression + "\"] is not a valid OGNL expression", e);
        }
    }

    /**
	 * @see net.sf.kfgodel.bean2bean.interpreters.ExpressionInterpreter#generateGetterContextFrom(java.lang.Object)
	 */
    public Object generateGetterContextFrom(Object sourceObject) {
        Map<String, Object> contexto = new HashMap<String, Object>();
        contexto.put(OgnlConstants.OBJETO_ORIGEN, sourceObject);
        return contexto;
    }

    /**
	 * @see net.sf.kfgodel.bean2bean.interpreters.ExpressionInterpreter#evaluateGetterOn(java.lang.Object,
	 *      java.lang.Object, java.lang.Object)
	 */
    public Object evaluateGetterOn(Object source, Object expression, Object context) throws MissingPropertyException {
        try {
            @SuppressWarnings("unchecked") Map<String, Object> contexto = (Map) context;
            Object value = Ognl.getValue(expression, contexto, source);
            return value;
        } catch (NoSuchPropertyException e) {
            throw new MissingPropertyException("OGNL couldn't access the property", e);
        } catch (OgnlException e) {
            throw new BadMappingException("OGNL error with expression[" + expression + "] on object[" + source + "], context:[" + context + "]", e);
        }
    }

    /**
	 * @see net.sf.kfgodel.bean2bean.interpreters.ExpressionInterpreter#evaluate(java.lang.String)
	 */
    public Object evaluate(String expression) {
        try {
            Object value = Ognl.getValue(expression, null);
            return value;
        } catch (OgnlException e) {
            throw new BadMappingException("OGNL error evaluating expression[" + expression + "]", e);
        }
    }

    /**
	 * @see net.sf.kfgodel.bean2bean.interpreters.ExpressionInterpreter#generateSetterContextFor(java.lang.Object,
	 *      java.lang.Object)
	 */
    public Object generateSetterContextFor(Object destination, Object value) {
        Map<String, Object> contexto = new HashMap<String, Object>();
        contexto.put(OgnlConstants.OBJETO_DESTINO, destination);
        contexto.put(OgnlConstants.VALOR, value);
        return contexto;
    }

    /**
	 * @see net.sf.kfgodel.bean2bean.interpreters.ExpressionInterpreter#makeAssignmentOn(java.lang.Object,
	 *      java.lang.Object, java.lang.Object, java.lang.Object)
	 */
    public void makeAssignmentOn(Object destination, Object expression, Object context, Object value) {
        try {
            @SuppressWarnings("unchecked") Map<String, Object> contexto = (Map) context;
            if (ReflectionUtils.isPropertyChain(expression.toString())) {
                Ognl.setValue(expression, contexto, destination, value);
            } else {
                Ognl.getValue(expression, contexto, destination);
            }
        } catch (NoSuchPropertyException e) {
            throw new MissingPropertyException("OGNL could not find the property", e);
        } catch (OgnlException e) {
            throw new BadMappingException("OGNL error with expression[" + expression + "] on object[" + destination + "] and value[" + value + "], context:[" + context + "]", e);
        }
    }
}
