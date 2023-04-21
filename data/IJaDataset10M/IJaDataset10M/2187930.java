package org.equanda.tapestry.util;

import org.equanda.persistence.om.UOID;
import org.equanda.persistence.om.EquandaPersistenceException;
import org.equanda.persistence.om.EquandaProxy;
import org.equanda.persistence.om.EquandaSelector;
import org.equanda.persistence.om.query.EquandaQuery;
import org.equanda.persistence.om.query.EquandaQueryImpl;
import org.equanda.tapestry.components.shared.DescriptionFactory;
import org.equanda.tapestry.model.GMField;
import org.equanda.tapestry.model.GMTable;
import org.equanda.tapestry.model.Input;
import org.equanda.tapestry.selectionModel.ChoiceFieldSelectionModel;
import org.equanda.validation.DefaultInstance;
import javolution.lang.TextBuilder;
import org.apache.tapestry.IComponent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Description!!!
 *
 * @author <a href="mailto:andrei@paragon-software.ro">Andrei Chiritescu</a>
 * @author <a href="mailto:florin@paragon-software.ro">Florin</a>
 */
public class EquandaProxyAccessor {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EquandaProxyAccessor.class);

    public static final String GLOBAL_CLASS = "EquandaGlobal";

    public static final String GLOBAL_GET = "get";

    public static final String GLOBAL_SELECTOR = "Selector";

    public static final String SELECTOR_PREFIX = "select";

    public static Object getField(EquandaProxy proxy, String fieldName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String method = "get" + fieldName;
        return proxy.getClass().getMethod(method).invoke(proxy);
    }

    public static Object setField(EquandaProxy proxy, String fieldName, Class fieldClass, Object value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String method = "set" + fieldName;
        Class[] parameters = new Class[] { fieldClass };
        return proxy.getClass().getMethod(method, parameters).invoke(proxy, value);
    }

    public static List<EquandaProxy> select(String tableName, String selectorName, Class[] parameterTypes, Object[] parameterValues) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        List<EquandaProxy> list = new ArrayList<EquandaProxy>();
        EquandaSelector selector = null;
        try {
            selector = getSelector(tableName);
            Object results = selector.getClass().getMethod(SELECTOR_PREFIX + selectorName, parameterTypes).invoke(selector, parameterValues);
            if (results instanceof List) {
                list = (List<EquandaProxy>) results;
            } else if (results != null && results instanceof EquandaProxy) {
                list.add((EquandaProxy) results);
            }
        } finally {
            if (selector != null) selector.remove();
        }
        return list;
    }

    /**
     * Run a custom query
     *
     * @param tableName table name
     * @param queryStr query string
     * @return list of proxies
     * @throws NoSuchMethodException oops
     * @throws IllegalAccessException oops
     * @throws InvocationTargetException oops
     * @throws InstantiationException oops
     * @throws ClassNotFoundException oops
     */
    public static List<EquandaProxy> select(String tableName, String queryStr) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        List<EquandaProxy> list = new ArrayList<EquandaProxy>();
        EquandaSelector selector = null;
        EquandaQuery query = new EquandaQueryImpl(queryStr);
        try {
            selector = getSelector(tableName);
            Object results = selector.getClass().getMethod(SELECTOR_PREFIX + "Equanda", new Class[] { EquandaQuery.class }).invoke(selector, new Object[] { query });
            if (results instanceof List) {
                list = (List<EquandaProxy>) results;
            } else if (results != null && results instanceof EquandaProxy) {
                list.add((EquandaProxy) results);
            }
        } finally {
            if (selector != null) selector.remove();
        }
        return list;
    }

    public static EquandaProxy clone(String tableName, String type, String id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        EquandaProxy proxy = null;
        EquandaSelector selector = null;
        try {
            selector = getSelector(tableName);
            Class types[] = { Class.forName("java.lang.String"), Class.forName("org.equanda.persistence.om.UOID") };
            Object values[] = { type, new UOID(id) };
            Method method = selector.getClass().getMethod("buildEJB", types);
            Object obj = method.invoke(selector, values);
            proxy = (EquandaProxy) obj.getClass().getMethod("getEquandaProxy").invoke(obj);
        } finally {
            if (selector != null) selector.remove();
        }
        return proxy;
    }

    public static EquandaProxy clone(EquandaProxy proxy) throws IllegalAccessException, NoSuchMethodException, EquandaPersistenceException, InvocationTargetException, ClassNotFoundException, InstantiationException {
        return clone(getTableName(proxy), proxy.getEquandaType(), proxy.getUOID().getId());
    }

    public static EquandaProxy selectUoid(String tableName, String uoid) {
        EquandaSelector selector = null;
        try {
            selector = getSelector(tableName);
            return selector.selectUOID(new UOID(uoid));
        } catch (Exception e) {
            log.error(e, e);
            throw new RuntimeException("error selecting uoid : " + uoid);
        } finally {
            if (selector != null) selector.remove();
        }
    }

    public static EquandaProxy createProxy(String tableName) {
        try {
            EquandaProxy proxy = (EquandaProxy) getClassFor(getProxyFullName(tableName)).getMethod("equandaCreate").invoke(null);
            GMTable table = DescriptionFactory.getTableDescription(tableName);
            if (table.hasDefaultClass()) {
                proxy.setEquandaType(((DefaultInstance) Thread.currentThread().getContextClassLoader().loadClass(table.getDefaultClass()).newInstance()).getDefaultType());
            } else if (table.hasDefaultType()) {
                proxy.setEquandaType(table.getDefaultType());
            }
            return proxy;
        } catch (Exception e) {
            log.error(e, e);
            throw new RuntimeException("error creating proxy for table : " + getProxyFullName(tableName));
        }
    }

    public static String getTableName(EquandaProxy proxy) {
        return getTableName(proxy.getClass().getSimpleName());
    }

    public static String getTableName(String proxyName) {
        return proxyName;
    }

    public static String getProxyFullName(String tableName) {
        return ResourceUtil.getConfigString("ejb-client-package") + tableName;
    }

    public static String getFullGlobalClassName() {
        return ResourceUtil.getConfigString("ejb-client-package") + GLOBAL_CLASS;
    }

    public static String getDisplay(EquandaProxy proxy, String... excludeFields) {
        return getDisplay(proxy, null, excludeFields);
    }

    public static String getDisplay(EquandaProxy proxy, IComponent translator, String... excludeFields) {
        if (proxy == null) return "";
        GMTable table = DescriptionFactory.getTableDescription(getTableName(proxy));
        TextBuilder text = TextBuilder.newInstance();
        for (GMField field : table.getFields()) {
            boolean exclude = false;
            for (String excludeField : excludeFields) {
                if (field.getName().equals(excludeField)) {
                    exclude = true;
                    break;
                }
            }
            if (!exclude && field.isDisplayed() && !field.isMultiple()) {
                try {
                    Object value = getField(proxy, field.getName());
                    if (value != null) {
                        if (field.getFieldType() == FieldType.TYPE_LINK && value instanceof EquandaProxy) {
                            text.append(getDisplay((EquandaProxy) value, translator));
                        } else if (field.hasChoice()) {
                            ChoiceFieldSelectionModel model = new ChoiceFieldSelectionModel(field, true, translator);
                            for (int i = 0; i < model.getOptionCount(); i++) {
                                if (value.equals(model.getOption(i))) {
                                    text.append(model.getLabel(i)).append(' ');
                                }
                            }
                        } else {
                            text.append(value.toString()).append(' ');
                        }
                    }
                } catch (Exception e) {
                    log.error(e, e);
                }
            }
        }
        return text.toString();
    }

    public static Class getClassFor(String fullName) throws ClassNotFoundException {
        return Thread.currentThread().getContextClassLoader().loadClass(fullName);
    }

    public static EquandaSelector getSelector(String tableName) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class globalClass = getClassFor(getFullGlobalClassName());
        Object equanaGlobal = globalClass.newInstance();
        TextBuilder tb = TextBuilder.newInstance();
        tb.append(GLOBAL_GET);
        tb.append(tableName);
        tb.append(GLOBAL_SELECTOR);
        return (EquandaSelector) globalClass.getMethod(tb.toString()).invoke(equanaGlobal);
    }

    public static synchronized FieldType getFieldType(Input description) {
        FieldType type = description.getFieldType();
        if (description.getUpdateField() != null) {
            type = description.getUpdateType();
        }
        return type;
    }

    public static synchronized Class getFieldClass(Input description) throws ClassNotFoundException {
        FieldType type = getFieldType(description);
        if (type.isLink()) {
            return EquandaProxyAccessor.getClassFor(EquandaProxyAccessor.getProxyFullName(description.getLinkTableName()));
        } else {
            return type.getFieldTypeClass();
        }
    }

    /**
     * Must be used for instance after you run an action, this updates the object, then you're back in the edit page
     *
     * @param proxy proxy to refresh
     * @return refreshed object
     */
    public static synchronized EquandaProxy equandaRefresh(EquandaProxy proxy) {
        return selectUoid(getTableName(proxy), proxy.getUOID().getId());
    }
}
