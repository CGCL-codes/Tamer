package br.com.lopes.gci.util;

import java.util.Locale;
import java.util.Map;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Commom needs in java server faces
 * 
 * @version 1.4
 * @author Hugo Tota
 */
public class FacesUtil {

    private FacesUtil() {
    }

    public static ELContext getELContext() {
        return FacesContext.getCurrentInstance().getELContext();
    }

    public static Application getApplication() {
        return FacesContext.getCurrentInstance().getApplication();
    }

    public static FacesContext getContext() {
        return FacesContext.getCurrentInstance();
    }

    public static ELResolver getELResolver() {
        return getApplication().getELResolver();
    }

    public static ExpressionFactory getExpressionFactory() {
        return getApplication().getExpressionFactory();
    }

    public static MethodExpression getMethodExpression(final String expression) {
        return getExpressionFactory().createMethodExpression(getELContext(), expression, null, new Class<?>[0]);
    }

    public static ValueExpression getValueExpression(final String expression) {
        return getExpressionFactory().createValueExpression(getELContext(), expression, Object.class);
    }

    public static ValueExpression getValueExpression(final String expression, Class<?> expectedType) {
        return getExpressionFactory().createValueExpression(getELContext(), expression, expectedType);
    }

    public static ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }

    public static Locale getLocale() {
        return getExternalContext().getRequestLocale();
    }

    public static HttpServletResponse getResponse() {
        return (HttpServletResponse) getExternalContext().getResponse();
    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) getExternalContext().getRequest();
    }

    public static HttpSession getSession() {
        return (HttpSession) getExternalContext().getSession(false);
    }

    public static Map<String, Object> getRequestMap() {
        return getExternalContext().getRequestMap();
    }

    public static Map<String, Object> getSessionMap() {
        return getExternalContext().getSessionMap();
    }

    public static Object getFromRequestMap(String key) {
        return getRequestMap().get(key);
    }

    public static Object getFromSessionMap(String key) {
        return getRequestMap().get(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getSessionMapAttribute(String key) {
        return (T) getSessionMap().get(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getRequestMapAttribute(String key) {
        return (T) getRequestMap().get(key);
    }

    public static void removeAttributeFromRequestMap(String paramName) {
        getRequestMap().remove(paramName);
    }

    public static void removeAttributeFromSessionMap(String paramName) {
        getSessionMap().remove(paramName);
    }

    public static void addAttribute(String param, Object obj) {
        getSession().setAttribute(param, obj);
    }

    public static Object getAttribute(String param) {
        return getSession().getAttribute(param);
    }

    public static void removeAttribute(String paramName) {
        getSession().removeAttribute(paramName);
    }

    public static void setRequestMapAttribute(String key, Object value) {
        getRequestMap().put(key, value);
    }

    public static void setSessionMapAttribute(String key, Object value) {
        getSessionMap().put(key, value);
    }

    public static Object getAttributeValue(String key, Class<?> returnClass) {
        ValueExpression expression = getValueExpression(key, returnClass);
        return expression.getValue(getELContext());
    }

    public static NavigationHandler getNavigationHandler() {
        return getApplication().getNavigationHandler();
    }

    public static void redirect(String outcome) {
        getNavigationHandler().handleNavigation(getContext(), null, outcome);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAttributeValueParametrized(String key, Class<?> returnClass) {
        return (T) getAttributeValue(key, returnClass);
    }

    public static boolean isEL(Object expression) {
        boolean boll = false;
        if (expression instanceof ValueExpression) {
            boll = true;
        }
        boll = expression.toString().startsWith("#{") && expression.toString().endsWith("}") ? true : false;
        return boll;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getManagedBean(Class<?> clazz) {
        T bean = (T) getELResolver().getValue(getELContext(), null, String.format("%s%s", new Object[] { Character.toLowerCase(clazz.getSimpleName().charAt(0)), clazz.getSimpleName().substring(1) }));
        return bean;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getManagedBean(String name) {
        T bean = (T) getELResolver().getValue(getELContext(), null, name);
        return bean;
    }

    public static void populateLocale() {
        Locale locale = getLocale();
        if ("pt_BR".equals(String.format("%s_%s", new Object[] { locale.getLanguage(), locale.getCountry() }))) {
            setRequestMapAttribute("locale_pt_BR", Boolean.TRUE);
        } else {
            setRequestMapAttribute("locale_pt_BR", Boolean.FALSE);
        }
    }
}
