package br.ufmg.saotome.arangi.controller.bean;

import java.util.Map;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import br.ufmg.saotome.arangi.commons.ArangiConstants;

/**
 * 
 * @author Cesar Correia
 *
 */
public class FacesBeanHelper {

    protected static Logger log = Logger.getLogger(FacesBeanHelper.class);

    /**
	 * 
	 * @param expression
	 * @param expectedType
	 * @return
	 */
    public static ValueExpression createValueExpression(String expression, Class<?> expectedType) {
        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elContext = context.getELContext();
        ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
        return expressionFactory.createValueExpression(elContext, expression, expectedType);
    }

    public static MethodExpression createMethodExpression(String expression, Class<?> expectedReturnType, Class<?>[] expectedParamType) {
        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elContext = context.getELContext();
        ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
        return expressionFactory.createMethodExpression(elContext, expression, expectedReturnType, expectedParamType);
    }

    /**
	 * 
	 * @param <T>
	 * @param expression
	 * @param expectedType
	 * @return
	 */
    public static <T> T getBeanByExpression(String expression, Class<T> expectedType) {
        if (!expression.endsWith("}")) expression = "#{" + expression + "}";
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        ValueExpression valueExpression = createValueExpression(expression, expectedType);
        Object obj = valueExpression.getValue(elContext);
        return expectedType.cast(obj);
    }

    /**
	 * 
	 * @param expression
	 * @param expectedType
	 */
    public static void setBeanByExpression(String expression, Class<?> expectedType, Object bean) {
        if (!expression.endsWith("}")) expression = "#{" + expression + "}";
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        ValueExpression valueExpression = createValueExpression(expression, expectedType);
        valueExpression.setValue(elContext, bean);
    }

    /**
	 * 
	 * @param beanName
	 * @param scope
	 * @param bean
	 */
    public static void setBeanByScope(String beanName, String scope, Object bean) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (ArangiConstants.ApplicationScope.APPLICATION.equals(scope)) {
            Map<String, Object> appplicationMap = context.getExternalContext().getApplicationMap();
            appplicationMap.put(beanName, bean);
        } else if (ArangiConstants.ApplicationScope.SESSION.equals(scope)) {
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            sessionMap.put(beanName, bean);
        } else {
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            requestMap.put(beanName, bean);
        }
    }

    /**
	 * 
	 * @param <T>
	 * @param beanName
	 * @param scope
	 * @param expectedType
	 * @return
	 */
    public static <T> T getBeanByScope(String beanName, String scope, Class<T> expectedType) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (ArangiConstants.ApplicationScope.APPLICATION.equals(scope)) {
            Map<String, Object> appplicationMap = context.getExternalContext().getApplicationMap();
            return (T) appplicationMap.get(beanName);
        } else if (ArangiConstants.ApplicationScope.SESSION.equals(scope)) {
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            return (T) sessionMap.get(beanName);
        } else if (ArangiConstants.ApplicationScope.REQUEST.equals(scope)) {
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            return (T) requestMap.get(beanName);
        } else {
            Map<String, String> parameterMap = context.getExternalContext().getRequestParameterMap();
            return (T) parameterMap.get(beanName);
        }
    }

    public static boolean belongsToConversationScope(String beanName) {
        IFacesBeanManager bm = getBeanManager();
        if (bm.isBeanInConversationScope(beanName)) {
            return true;
        }
        return false;
    }

    public static Object instantiateBean(String beanName) {
        IFacesBeanManager bm = getBeanManager();
        return bm.instantiateBean(beanName);
    }

    private static IFacesBeanManager getBeanManager() {
        return FacesBeanManager.getInstance();
    }
}
