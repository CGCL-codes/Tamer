package org.jgenesis.bean;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgenesis.beanset.BeanSetException;
import org.jgenesis.helper.BeanUtils;

/**
 * @author Fabr?cio Barroso de Carvalho
 */
public abstract class BeanInfo {

    protected static Log log = LogFactory.getLog(BeanInfo.class);

    /**
	 * Caracter usado para separar propriedades aninhadas. 
     */
    private static char nestedPropertySeparator = '.';

    private Map properties = new HashMap();

    private List<String> propertyNames;

    private Map nodes = new HashMap();

    private BeanInfo parentNode;

    private Class beanClass;

    protected BeanInfo() {
    }

    public BeanInfo(Class beanClass) {
        this.beanClass = beanClass;
        try {
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(beanClass).getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) if (!ignorePropertyOnMapping(propertyDescriptor.getName())) {
                Class propertyClass = propertyDescriptor.getPropertyType();
                if (!beanClass.isAssignableFrom(propertyClass)) this.properties.put(propertyDescriptor.getName(), propertyDescriptor); else this.properties.put(propertyDescriptor.getName(), new BeanProperty(propertyClass == beanClass ? this : this.getChild(propertyClass), propertyDescriptor));
            }
        } catch (IntrospectionException e) {
        }
    }

    public BeanInfo getChild(String className) {
        try {
            return this.getChild(Class.forName(className));
        } catch (ClassNotFoundException ex) {
            log.error("Classe n�o encontrada: " + className, ex);
            return null;
        }
    }

    /**
	 * Fornece uma inst?ncia de BeanInfo para uma subclasse da classe encapsulada
	 * pelo BeanWrapper corrente.Se a subclasse ainda n?o foi mapeada, o mapeamente 
	 * ser? feito e um novo BeanInfo ser? criado. 
	 *  
	 * @param childClass Subclasse requisitada. 
	 * @return BeanWrapper respons?gel pelo gerenciamento dos objetos do tipo 
	 * 	especificado. 
	 */
    public BeanInfo getChild(Class childClass) {
        BeanInfo childNode, parentNode;
        if (!this.beanClass.isAssignableFrom(childClass)) return null;
        if (childClass == this.beanClass) return this;
        if (childClass.getSuperclass() == this.beanClass) parentNode = this; else parentNode = this.getChild(childClass.getSuperclass());
        if ((childNode = parentNode.getChildNode(childClass)) != null) return childNode;
        if (log.isDebugEnabled()) log.debug("mapping " + childClass);
        childNode = this.getNewInstance();
        childNode.setBeanClass(childClass);
        childNode.setParentNode(parentNode);
        parentNode.nodes.put(childClass, childNode);
        try {
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(childClass).getPropertyDescriptors();
            BeanInfo rootNode = getRoot();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) if (propertyDescriptor.getPropertyType() != null && !ignorePropertyOnMapping(propertyDescriptor.getName()) && !parentNode.containsProperty(propertyDescriptor.getName())) {
                if (log.isDebugEnabled()) log.debug("mapping property " + propertyDescriptor.getName());
                if (rootNode.getBeanClass().isAssignableFrom(propertyDescriptor.getPropertyType())) childNode.properties.put(propertyDescriptor.getName(), new BeanProperty(rootNode.getChild(propertyDescriptor.getPropertyType()), propertyDescriptor)); else childNode.properties.put(propertyDescriptor.getName(), propertyDescriptor);
            }
        } catch (IntrospectionException e) {
        }
        return childNode;
    }

    /**
     * Fornece o caracter usado para a separa??o de campos aninhados.
     * Ex.: cliente.nome -> '.', cliente_nome -> '_' 
     * @return
     */
    public static char getNestedPropertySeparator() {
        return nestedPropertySeparator;
    }

    /**
     * Modifica o caracter usado para a separa??o de campos aninhados.
     *  
     * @param nestedPropertySeparator Novo caracter a ser usado 
     */
    public static void setNestedPropertySeparator(char nestedPropertySeparator) {
        BeanInfo.nestedPropertySeparator = nestedPropertySeparator;
    }

    protected abstract boolean ignorePropertyOnMapping(String propertyName);

    protected abstract BeanInfo getNewInstance();

    protected abstract Object getNewPropertyInstance(Class propertyType);

    protected abstract boolean isPropertyAccessible(Object bean, String propertyName);

    public BeanInfo getRoot() {
        return this.parentNode == null ? this : this.parentNode.getRoot();
    }

    private boolean isChild(Class nodeClass) {
        return this.nodes.containsKey(nodeClass);
    }

    private BeanInfo getChildNode(Class nodeClass) {
        return (BeanInfo) this.nodes.get(nodeClass);
    }

    public Object getNewBeanInstance() {
        try {
            return this.beanClass.newInstance();
        } catch (InstantiationException e) {
            log.error("Erro ao instanciar bean " + this.beanClass.getName(), e);
        } catch (IllegalAccessException e) {
            log.error("Erro ao instanciar bean " + this.beanClass.getName(), e);
        }
        return null;
    }

    private void getPropertyDescriptor(String fieldName, NestedProperty propertyAux) {
        Object property;
        int pos = fieldName.indexOf(nestedPropertySeparator);
        if (pos > 0) property = properties.get(fieldName.substring(0, pos)); else property = properties.get(fieldName);
        if (property != null) if (property instanceof PropertyDescriptor) propertyAux.property = (PropertyDescriptor) property; else {
            if (pos > 0) {
                Object bean = this.getPropertyValue(((BeanProperty) property).getProperty(), propertyAux.bean);
                if (bean == null || bean instanceof InaccessibleProperty) {
                    if (log.isDebugEnabled()) log.debug("new instance to property: ");
                    bean = getNewPropertyInstance(((BeanProperty) property).getProperty().getPropertyType());
                    this.setPropertyValue(fieldName.substring(0, pos), bean, propertyAux.bean);
                }
                propertyAux.bean = bean;
                ((BeanProperty) property).getBeanInfo(propertyAux.bean.getClass()).getPropertyDescriptor(fieldName.substring(pos + 1), propertyAux);
            } else propertyAux.property = ((BeanProperty) property).getProperty();
        } else {
            if (this.parentNode == null) propertyAux = null; else this.parentNode.getPropertyDescriptor(fieldName, propertyAux);
        }
    }

    private PropertyDescriptor getPropertyDescriptor(String fieldName) {
        Object property;
        int pos = fieldName.indexOf(nestedPropertySeparator);
        if (pos > 0) property = properties.get(fieldName.substring(0, pos)); else property = properties.get(fieldName);
        if (property != null) {
            if (property instanceof PropertyDescriptor) return (PropertyDescriptor) property; else {
                return pos > 0 ? ((BeanProperty) property).getBeanInfo().getPropertyDescriptor(fieldName.substring(pos + 1)) : ((BeanProperty) property).property;
            }
        } else return this.parentNode == null ? null : this.parentNode.getPropertyDescriptor(fieldName);
    }

    /**
	 * Fornece a classe de um dos atributos do bean gerenciado pelo BeanWrapper
	 * 
	 * @param fieldName Nome do atributo
	 * @return Tipo do atributo especificado. Se o atributo n?o for encontrado, 
	 * devolve Object.class. Isso pode ocorrer em dois casos: 
	 * 		<br>Quando o nome do attributo n?o estiver correto
	 * 		<br>Quando o tipo s? pode ser determinado a quando houver uma inst?ncia do bean (polimorfismo)
	 */
    public Class getPropertyClass(String fieldName) {
        if (log.isDebugEnabled()) log.debug("getting class for field " + fieldName + " of " + beanClass);
        PropertyDescriptor property = this.getPropertyDescriptor(fieldName);
        return property != null ? property.getPropertyType() : Object.class;
    }

    /**
	 * Fornece o valor do atributo fieldName do bean passado.   
	 * @param fieldName Nome do atributo
	 * @param bean Inst?ncia de onde o valor do atributo ser? extra?do
	 * @return Valor do atributo especificado
	 */
    public Object getPropertyValue(String fieldName, Object bean) {
        if (fieldName == null || bean == null) return null;
        Object property;
        int separator = fieldName.indexOf(nestedPropertySeparator);
        if (separator > 0) property = properties.get(fieldName.substring(0, separator)); else property = properties.get(fieldName);
        if (property != null) {
            if (property instanceof PropertyDescriptor) return this.getPropertyValue((PropertyDescriptor) property, bean); else {
                if (separator > 0) {
                    bean = this.getPropertyValue(((BeanProperty) property).getProperty(), bean);
                    if (bean instanceof InaccessibleProperty) return bean; else {
                        bean = bean == null ? null : ((BeanProperty) property).getBeanInfo(bean.getClass()).getPropertyValue(fieldName.substring(separator + 1), bean);
                        if (bean instanceof InaccessibleProperty) ((InaccessibleProperty) bean).addPathToPropertyName(fieldName.substring(0, separator));
                        return bean;
                    }
                } else return this.getPropertyValue(((BeanProperty) property).getProperty(), bean);
            }
        } else if (this.parentNode == null) {
            if (log.isWarnEnabled()) log.warn("field not found: " + fieldName + ", beanInstance: " + bean);
            return null;
        } else return this.parentNode.getPropertyValue(fieldName, bean);
    }

    BeanInfo getPropertyBeanInfo(String fieldName, Object bean) {
        if (fieldName == null || bean == null) return null;
        Object property;
        int pos = fieldName.indexOf(nestedPropertySeparator);
        if (pos > 0) property = properties.get(fieldName.substring(0, pos)); else property = properties.get(fieldName);
        if (property != null) {
            if (property instanceof PropertyDescriptor) return null; else {
                if (pos > 0) {
                    bean = this.getPropertyValue(((BeanProperty) property).getProperty(), bean);
                    return bean == null ? null : ((BeanProperty) property).getBeanInfo(bean.getClass());
                } else return ((BeanProperty) property).getBeanInfo(bean.getClass());
            }
        } else if (this.parentNode == null) {
            if (log.isWarnEnabled()) log.warn("field not found: " + fieldName + ", beanInstance: " + bean);
            return null;
        } else return this.parentNode.getPropertyBeanInfo(fieldName, bean);
    }

    private Object getPropertyValue(PropertyDescriptor property, Object bean) {
        Object value = null;
        if (property != null && bean != null) try {
            value = property.getReadMethod().invoke(bean, (Object[]) null);
            if (!this.isPropertyAccessible(bean, property.getName())) return new InaccessibleProperty(value, property.getName(), (AbstractBean) bean);
        } catch (IllegalArgumentException e) {
            if (log.isWarnEnabled()) log.warn("Error getting property value: " + this.beanClass.getName() + "." + property.getName());
        } catch (IllegalAccessException e) {
            if (log.isWarnEnabled()) log.warn("Error getting property value: " + this.beanClass.getName() + "." + property.getName());
        } catch (InvocationTargetException e) {
            if (log.isWarnEnabled()) log.warn("Error getting property value: " + this.beanClass.getName() + "." + property.getName());
        } catch (NullPointerException ex) {
            log.error("Error getting property value: " + this.beanClass.getName() + "." + property.getName());
        }
        return value;
    }

    public Object setPropertyValue(String fieldName, Object value, Object bean) {
        NestedProperty beanProperty = new NestedProperty(bean);
        getPropertyDescriptor(fieldName, beanProperty);
        if (beanProperty == null || beanProperty.property == null || beanProperty.property.getWriteMethod() == null) return null; else {
            Object result = this.setPropertyValue(beanProperty.property, BeanUtils.convertValue(value, beanProperty.property.getPropertyType()), beanProperty.bean);
            if (!(value instanceof InaccessibleProperty)) ((AbstractBean) beanProperty.bean).addInitializedProperty(beanProperty.property.getName());
            return result;
        }
    }

    private Object setPropertyValue(PropertyDescriptor property, Object value, Object bean) {
        if (property != null && bean != null) try {
            return property.getWriteMethod().invoke(bean, new Object[] { value });
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public BeanInfo getParentNode() {
        return parentNode;
    }

    public void setParentNode(BeanInfo rootNode) {
        this.parentNode = rootNode;
    }

    public boolean containsProperty(String propertyName) {
        return this.properties.containsKey(propertyName) ? true : this.parentNode != null ? this.parentNode.containsProperty(propertyName) : false;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public List<String> getPropertyNames() {
        if (this.propertyNames != null) return this.propertyNames; else {
            this.propertyNames = new Vector<String>(20);
            if (this.parentNode != null) this.propertyNames.addAll(this.parentNode.getPropertyNames());
            this.propertyNames.addAll(this.properties.keySet());
            Collections.sort(this.propertyNames);
            return this.propertyNames;
        }
    }

    Map getNodes() {
        return nodes;
    }

    private class NestedProperty {

        Object bean;

        PropertyDescriptor property;

        NestedProperty(Object bean) {
            this.bean = bean;
        }
    }

    private class BeanProperty {

        private PropertyDescriptor property;

        private BeanInfo beanInfo;

        BeanProperty(BeanInfo beanInfo, PropertyDescriptor property) {
            this.beanInfo = beanInfo;
            this.property = property;
        }

        BeanInfo getBeanInfo() {
            return beanInfo;
        }

        BeanInfo getBeanInfo(Class clazz) {
            return clazz == beanInfo.getBeanClass() ? beanInfo : beanInfo.getChild(clazz);
        }

        PropertyDescriptor getProperty() {
            return property;
        }
    }
}
