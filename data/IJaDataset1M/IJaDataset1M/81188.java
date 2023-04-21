package it.aco.mandragora.bo;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConstructorUtils;
import java.util.*;
import java.lang.reflect.InvocationTargetException;
import it.aco.mandragora.exception.BusinessObjectException;
import it.aco.mandragora.common.Utils;

/**
 * This class extends the HashMap and have constructors that create the instance of the Map  filled with  entries built on the base of a collection of value objects, using the values of some of its attributes
 * as entry key and entry value. Optionally as entries values could be used new instances of a class that can be provided by input to the constructor, and some attribute of the instances of such class
 * can be set with the value of some attribute of the value objects of the collection.</br>
 * Moreover this class provides method to update the map on the base of a collection of value objects in the same way of the constructors.</br>
 *
 */
public class ValueObjectsAttributeMapBO extends HashMap {

    private static org.apache.log4j.Category log = org.apache.log4j.Logger.getLogger(ValueObjectsAttributeMapBO.class.getName());

    /**
     * This constructor calls super() and then updates the recently created empty map using the method {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite)}
     * with <code>overwrite = True</code>
     *
     * @param valueObjectsCollection collection containing the elements (value objects) to create the map entries
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entry's key in the map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param valueObjectValueAttributeName  name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries values in this Map.</br>
     *                                      If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectKeyAttributeName</code>
     * @param mapValueClassAttributeToSetName  name of the attribute of <code>mapValueClass</code> that will be set with the value of the valueObject attribute specified by the string input parameter <code>valueObjectValueAttributeName</code>.</br>
     *                                       If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectValueAttributeName</code>.
     * @param mapValueClass Class of the entries values of this Map. A new instance of this class will be created (with no arguments constructor) for each element of the collection <code>valueObjectsCollection</code>.</br>
     *                      The attribute of <code>mapValueClass</code> named <code>mapValueClassAttributeToSetName</code> will be populated with the value of  attribute named <code>valueObjectValueAttributeName</code> in the element of the collection <code>valueObjectsCollection</code>, if that value is not null..</br>
     *                      Of course has to exist an attribute named with the value  <code>valueObjectValueAttributeName</code>in the the class of the element of the collection <code>valueObjectsCollection</code>
     *                      and an attribute named <code>mapValueClassAttributeToSetName</code> in the class <code>mapValueClass</code> .
     * @throws BusinessObjectException for any trouble
     */
    public ValueObjectsAttributeMapBO(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass) throws BusinessObjectException {
        super();
        update(valueObjectsCollection, valueObjectKeyAttributeName, valueObjectValueAttributeName, mapValueClassAttributeToSetName, mapValueClass, Boolean.TRUE);
    }

    /**
     * This constructor calls super() and then updates the recently created empty map using the method {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite)}
     * with <code>overwrite = True</code> and where as <code>mapValueClassAttributeToSetName</code> is passed the same  <code>valueObjectValueAttributeName</code>
     * @param valueObjectsCollection collection containing the elements (value objects) to create the map entries
     * @param valueObjectKeyAttributeName  name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries keys in the map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param valueObjectValueAttributeName  name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries values in this Map.</br>
     *                                      If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectKeyAttributeName</code>
     * @param mapValueClass Class of the entries values of this Map. A new instance of this class will be created (with no arguments constructor) for each element of the collection <code>valueObjectsCollection</code>.</br>
     *                      The attribute of <code>mapValueClass</code> named <code>valueObjectValueAttributeName</code> will be populated with the value of  attribute named with the same <code>valueObjectValueAttributeName</code> in the element of the collection <code>valueObjectsCollection</code>, if that value is not null.</br>
     *                      Of course has to exist an attribute named with the value  <code>valueObjectValueAttributeName</code>in the both class of the element of the collection <code>valueObjectsCollection</code>
     *                      and in the class <code>mapValueClass</code> .
     * @throws BusinessObjectException
     */
    public ValueObjectsAttributeMapBO(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, Class mapValueClass) throws BusinessObjectException {
        super();
        update(valueObjectsCollection, valueObjectKeyAttributeName, valueObjectValueAttributeName, valueObjectValueAttributeName, mapValueClass, Boolean.TRUE);
    }

    /**
     * This constructor calls super() and then updates the recently created empty map using the method {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite)}
     * with <code>overwrite = True</code> and where for both  <code>valueObjectValueAttributeName</code> and <code>mapValueClassAttributeToSetName</code> is passed the same  <code>valueObjectKeyAttributeName</code>
     * @param valueObjectsCollection  collection containing the elements (value objects) to create the map entries
     * @param valueObjectKeyAttributeName  name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries keys in the map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param mapValueClass Class of the entries values of this Map. A new instance of this class will be created (with no arguments constructor) for each element of the collection <code>valueObjectsCollection</code>.</br>
     *                      The attribute of <code>mapValueClass</code> named <code>valueObjectKeyAttributeName</code> will be populated with the value of  attribute named with the same <code>valueObjectKeyAttributeName</code> in the element of the collection <code>valueObjectsCollection</code>, if that value is not null..</br>
     *                      Of course has to exist an attribute named with the value  <code>valueObjectKeyAttributeName</code>in  both class of the element of the collection <code>valueObjectsCollection</code>
     *                      and in the class <code>mapValueClass</code> .
     * @throws BusinessObjectException
     */
    public ValueObjectsAttributeMapBO(Collection valueObjectsCollection, String valueObjectKeyAttributeName, Class mapValueClass) throws BusinessObjectException {
        super();
        update(valueObjectsCollection, valueObjectKeyAttributeName, valueObjectKeyAttributeName, valueObjectKeyAttributeName, mapValueClass, Boolean.TRUE);
    }

    /**
     * This constructor calls super() and then updates the recently created empty map using the method {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite)}
     * with <code>overwrite = True</code> and both <code>mapValueClassAttributeToSetName</code> and <code>mapValueClass</code> are null.
     * @param valueObjectsCollection   collection containing the elements (value objects) to create the map entries
     * @param valueObjectKeyAttributeName  name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries keys in the map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param valueObjectValueAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries values in this Map.</br>
     *                                      If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectKeyAttributeName</code>
     * @throws BusinessObjectException
     */
    public ValueObjectsAttributeMapBO(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName) throws BusinessObjectException {
        super();
        update(valueObjectsCollection, valueObjectKeyAttributeName, valueObjectValueAttributeName, null, null, Boolean.TRUE);
    }

    /**
     * This constructor calls super() and then updates the recently created empty map using the method {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite)}
     * with <code>overwrite = True</code> and where as <code>valueObjectValueAttributeName</code> is passed the same  <code>valueObjectKeyAttributeName</code>
     * and both <code>mapValueClassAttributeToSetName</code> and <code>mapValueClass</code> are null.
     *
     * @param valueObjectsCollection  collection containing the elements (value objects) to create the map entries
     * @param valueObjectKeyAttributeName  name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries keys in the map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @throws BusinessObjectException
     */
    public ValueObjectsAttributeMapBO(Collection valueObjectsCollection, String valueObjectKeyAttributeName) throws BusinessObjectException {
        super();
        update(valueObjectsCollection, valueObjectKeyAttributeName, valueObjectKeyAttributeName, null, null, Boolean.TRUE);
    }

    /**
     * This method adds new entries to this Map, only if the key of the entry to be added doesn't exist yet in this Map or if exists, is mapped to a null value.</br>
     * To do his job this method calls {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite)}
     * with <code>overwrite = False</code>
     *
     * @param valueObjectsCollection collection containing the elements (value objects) to use to add entries to this Map
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as the keys of the entries to add to this Map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param valueObjectValueAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries values in this Map.</br>
     *                                      If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectKeyAttributeName</code>
     * @param mapValueClassAttributeToSetName name of the attribute of <code>mapValueClass</code> that will be set with the value of the valueObject attribute specified by the string input parameter <code>valueObjectValueAttributeName</code>.</br>
     *                                       If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectValueAttributeName</code>.
     * @param mapValueClass Class of the entries values of this Map. A new instance of this class will be created (with no arguments constructor) for each element of the collection <code>valueObjectsCollection</code>.</br>
     *                      The attribute of <code>mapValueClass</code> named <code>mapValueClassAttributeToSetName</code> will be populated with the value of  attribute named <code>valueObjectValueAttributeName</code> in the element of the collection <code>valueObjectsCollection</code>, if that value is not null..</br>
     *                      Of course has to exist an attribute named with the value  <code>valueObjectValueAttributeName</code>in the the class of the element of the collection <code>valueObjectsCollection</code>
     *                      and an attribute named <code>mapValueClassAttributeToSetName</code> in the class <code>mapValueClass</code>
     * @throws BusinessObjectException
     */
    public void addNoOverwrite(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass) throws BusinessObjectException {
        update(valueObjectsCollection, valueObjectKeyAttributeName, valueObjectValueAttributeName, mapValueClassAttributeToSetName, mapValueClass, Boolean.FALSE);
    }

    /**
     * This method works in the same way of {@link #addNoOverwrite(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass)},
     * with the difference that in place of acting on this Map, it works statically on a map provided by input</br>
     *
     * @param valueObjectsCollection collection containing the elements (value objects) to use to add entries to the <code>map</code>
     * @param map map that will be modified by this method
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as the keys of the entries to add to the <code>map</code>.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param valueObjectValueAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries values the <code>map</code>.</br>
     *                                      If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectKeyAttributeName</code>
     * @param mapValueClassAttributeToSetName name of the attribute of <code>mapValueClass</code> that will be set with the value of the valueObject attribute specified by the string input parameter <code>valueObjectValueAttributeName</code>.</br>
     *                                       If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectValueAttributeName</code>.
     * @param mapValueClass Class of the entries values of the <code>map</code>. A new instance of this class will be created (with no arguments constructor) for each element of the collection <code>valueObjectsCollection</code>.</br>
     *                      The attribute of <code>mapValueClass</code> named <code>mapValueClassAttributeToSetName</code> will be populated with the value of  attribute named <code>valueObjectValueAttributeName</code> in the element of the collection <code>valueObjectsCollection</code>, if that value is not null..</br>
     *                      Of course has to exist an attribute named with the value  <code>valueObjectValueAttributeName</code>in the the class of the element of the collection <code>valueObjectsCollection</code>
     *                      and an attribute named <code>mapValueClassAttributeToSetName</code> in the class <code>mapValueClass</code>
     * @throws BusinessObjectException
     */
    public static void addNoOverwrite(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass) throws BusinessObjectException {
        update(valueObjectsCollection, map, valueObjectKeyAttributeName, valueObjectValueAttributeName, mapValueClassAttributeToSetName, mapValueClass, Boolean.FALSE);
    }

    /**
     * This method adds new entries to this Map, only if the key of the entry to be added doesn't exist yet in this Map or if exists, is mapped to a null value.</br>
     * To do his job this method calls {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass, Boolean overwrite)}
     * with <code>overwrite = False</code> and where as <code>mapValueClassAttributeToSetName</code> is passed the same  <code>valueObjectValueAttributeName</code>
     *
     * @param valueObjectsCollection  collection containing the elements (value objects) to use to add entries to this Map
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as the keys of the entries to add to this Map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param valueObjectValueAttributeName  name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries values in this Map.</br>
     *                                      If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectKeyAttributeName</code>
     * @param mapValueClass Class of the entries values of this Map. A new instance of this class will be created (with no arguments constructor) for each element of the collection <code>valueObjectsCollection</code>.</br>
     *                      The attribute of <code>mapValueClass</code> named <code>valueObjectValueAttributeName</code> will be populated with the value of  attribute named with the same <code>valueObjectValueAttributeName</code> in the element of the collection <code>valueObjectsCollection</code>, if that value is not null..</br>
     *                      Of course has to exist an attribute named with the value  <code>valueObjectValueAttributeName</code>in the both class of the element of the collection <code>valueObjectsCollection</code>
     *                      and in the class <code>mapValueClass</code> .
     * @throws BusinessObjectException
     */
    public void addNoOverwrite(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, Class mapValueClass) throws BusinessObjectException {
        update(valueObjectsCollection, valueObjectKeyAttributeName, valueObjectValueAttributeName, valueObjectValueAttributeName, mapValueClass, Boolean.FALSE);
    }

    /**
     * This method works in the same way of {@link #addNoOverwrite(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName,Class mapValueClass)},
     * with the difference that in place of acting on this Map, it works statically on a map provided by input</br>
     *
     * @param valueObjectsCollection  collection containing the elements (value objects) to use to add entries to the <code>map</code>
     * @param map map that will be modified by this method
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as the keys of the entries to add to the <code>map</code>.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param valueObjectValueAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries values the <code>map</code>.</br>
     *                                      If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectKeyAttributeName</code>
     * @param mapValueClass Class of the entries values of the <code>map</code>. A new instance of this class will be created (with no arguments constructor) for each element of the collection <code>valueObjectsCollection</code>.</br>
     *                      The attribute of <code>mapValueClass</code> named <code>valueObjectValueAttributeName</code> will be populated with the value of  attribute named with the same <code>valueObjectValueAttributeName</code> in the element of the collection <code>valueObjectsCollection</code>, if that value is not null..</br>
     *                      Of course has to exist an attribute named with the value  <code>valueObjectValueAttributeName</code>in the both class of the element of the collection <code>valueObjectsCollection</code>
     *                      and in the class <code>mapValueClass</code> .
     * @throws BusinessObjectException
     */
    public static void addNoOverwrite(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, Class mapValueClass) throws BusinessObjectException {
        update(valueObjectsCollection, map, valueObjectKeyAttributeName, valueObjectValueAttributeName, valueObjectValueAttributeName, mapValueClass, Boolean.FALSE);
    }

    /**
     * This method adds new entries to this Map, only if the key of the entry to be added doesn't exist yet in this Map or if exists, is mapped to a null value.</br>
     * To do his job this method calls {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass, Boolean overwrite)}
     * with <code>overwrite = False</code> and where for both  <code>valueObjectValueAttributeName</code> and <code>mapValueClassAttributeToSetName</code> is passed the same  <code>valueObjectKeyAttributeName</code>
     *
     * @param valueObjectsCollection   collection containing the elements (value objects) to use to add entries to this Map
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as the keys of the entries to add to this Map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param mapValueClass Class of the entries values of this Map. A new instance of this class will be created (with no arguments constructor) for each element of the collection <code>valueObjectsCollection</code>.</br>
     *                      The attribute of <code>mapValueClass</code> named <code>valueObjectKeyAttributeName</code> will be populated with the value of  attribute named with the same <code>valueObjectKeyAttributeName</code> in the element of the collection <code>valueObjectsCollection</code>, if that value is not null..</br>
     *                      Of course has to exist an attribute named with the value  <code>valueObjectKeyAttributeName</code>in  both class of the element of the collection <code>valueObjectsCollection</code>
     *                      and in the class <code>mapValueClass</code> .
     * @throws BusinessObjectException
     */
    public void addNoOverwrite(Collection valueObjectsCollection, String valueObjectKeyAttributeName, Class mapValueClass) throws BusinessObjectException {
        update(valueObjectsCollection, valueObjectKeyAttributeName, valueObjectKeyAttributeName, valueObjectKeyAttributeName, mapValueClass, Boolean.FALSE);
    }

    /**
     * This method works in the same way of {@link #addNoOverwrite(Collection valueObjectsCollection, String valueObjectKeyAttributeName, Class mapValueClass)},
     * with the difference that in place of acting on this Map, it works statically on a map provided by input</br>
     *
     * @param valueObjectsCollection collection containing the elements (value objects) to use to add entries to the <code>map</code>
     * @param map map that will be modified by this method
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as the keys of the entries to add to the <code>map</code>.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param mapValueClass Class of the entries values of the <code>map</code>. A new instance of this class will be created (with no arguments constructor) for each element of the collection <code>valueObjectsCollection</code>.</br>
     *                      The attribute of <code>mapValueClass</code> named <code>valueObjectKeyAttributeName</code> will be populated with the value of  attribute named with the same <code>valueObjectKeyAttributeName</code> in the element of the collection <code>valueObjectsCollection</code>, if that value is not null..</br>
     *                      Of course has to exist an attribute named with the value  <code>valueObjectKeyAttributeName</code>in  both class of the element of the collection <code>valueObjectsCollection</code>
     *                      and in the class <code>mapValueClass</code> .
     * @throws BusinessObjectException
     */
    public static void addNoOverwrite(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, Class mapValueClass) throws BusinessObjectException {
        update(valueObjectsCollection, map, valueObjectKeyAttributeName, valueObjectKeyAttributeName, valueObjectKeyAttributeName, mapValueClass, Boolean.FALSE);
    }

    /**
     * This method adds new entries to this Map, only if the key of the entry to be added doesn't exist yet in this Map or if exists, is mapped to a null value.</br>
     * To do his job this method calls {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass, Boolean overwrite)}
     * with <code>overwrite = False</code> and both <code>mapValueClassAttributeToSetName</code> and <code>mapValueClass</code> are null.
     *
     * @param valueObjectsCollection collection containing the elements (value objects) to use to add entries to this Map
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as the keys of the entries to add to this Map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param valueObjectValueAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries values in this Map.</br>
     *                                      If this parameter is null, or empty or blank characters string, it will be forced to assume the value of <code>valueObjectKeyAttributeName</code>
     * @throws BusinessObjectException
     */
    public void addNoOverwrite(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName) throws BusinessObjectException {
        update(valueObjectsCollection, valueObjectKeyAttributeName, valueObjectValueAttributeName, null, null, Boolean.FALSE);
    }

    /**
     * This method works in the same way of {@link #addNoOverwrite(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName)},
     * with the difference that in place of acting on this Map, it works statically on a map provided by input</br>
     *
     * @param valueObjectsCollection collection containing the elements (value objects) to use to add entries to the <code>map</code>
     * @param map map that will be modified by this method
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as the keys of the entries to add to the <code>map</code>.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param valueObjectValueAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries values the <code>map</code>.</br>
     *                                      If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectKeyAttributeName</code>
     * @throws BusinessObjectException
     */
    public static void addNoOverwrite(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName) throws BusinessObjectException {
        update(valueObjectsCollection, map, valueObjectKeyAttributeName, valueObjectValueAttributeName, null, null, Boolean.FALSE);
    }

    /**
     *
     * This method adds new entries to this Map, only if the key of the entry to be added doesn't exist yet in this Map or if exists, is mapped to a null value.</br>
     * To do his job this method calls {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass, Boolean overwrite)}
     * with <code>overwrite = False</code> and where as <code>valueObjectValueAttributeName</code> is passed the same  <code>valueObjectKeyAttributeName</code>
     * and both <code>mapValueClassAttributeToSetName</code> and <code>mapValueClass</code> are null.
     *
     * @param valueObjectsCollection  collection containing the elements (value objects) to use to add entries to this Map
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as the keys of the entries to add to this Map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @throws BusinessObjectException
     */
    public void addNoOverwrite(Collection valueObjectsCollection, String valueObjectKeyAttributeName) throws BusinessObjectException {
        update(valueObjectsCollection, valueObjectKeyAttributeName, valueObjectKeyAttributeName, null, null, Boolean.FALSE);
    }

    /**
     * This method works in the same way of {@link #addNoOverwrite(Collection valueObjectsCollection, String valueObjectKeyAttributeName)},
     * with the difference that in place of acting on this Map, it works statically on a map provided by input</br>
     *
     * @param valueObjectsCollection collection containing the elements (value objects) to use to add entries to the <code>map</code>
     * @param map map that will be modified by this method
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as the keys of the entries to add to the <code>map</code>.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @throws BusinessObjectException
     */
    public static void addNoOverwrite(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName) throws BusinessObjectException {
        update(valueObjectsCollection, map, valueObjectKeyAttributeName, valueObjectKeyAttributeName, null, null, Boolean.FALSE);
    }

    /**
     * This method adds new entries to this Map, or update the existing ones.</br>
     * To do his job this method calls {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite)}
     * with <code>overwrite = True</code>
     * @param valueObjectsCollection collection containing the elements to create the map entries to add to this Map, or to use to update the existing ones in this Map
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entry's key in the map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param valueObjectValueAttributeName  name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries values  in this Map.</br>
     *                                      If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectKeyAttributeName</code>
     * @param mapValueClassAttributeToSetName name of the attribute of <code>mapValueClass</code> that will be set with the value of the valueObject attribute specified by the string input parameter <code>valueObjectValueAttributeName</code>.</br>
     *                                          If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectValueAttributeName</code>.
     * @param mapValueClass Class of the entries values of this Map. A new instance of this class will be created (with no arguments constructor) for each element of the collection <code>valueObjectsCollection</code>.</br>
     *                      The attribute of <code>mapValueClass</code> named <code>mapValueClassAttributeToSetName</code> will be populated with the value of  attribute named <code>valueObjectValueAttributeName</code> in the element of the collection <code>valueObjectsCollection</code>, if that value is not null.</br>
     *                      Of course has to exist an attribute named with the value  <code>valueObjectValueAttributeName</code>in the the class of the element of the collection <code>valueObjectsCollection</code>
     *                      and an attribute named <code>mapValueClassAttributeToSetName</code> in the class <code>mapValueClass</code> .
     * @throws BusinessObjectException
     */
    public void update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass) throws BusinessObjectException {
        update(valueObjectsCollection, valueObjectKeyAttributeName, valueObjectValueAttributeName, mapValueClassAttributeToSetName, mapValueClass, Boolean.TRUE);
    }

    /**
     * This method works in the same way of {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass)},
     * with the difference that in place of acting on this Map, it works statically on a map provided by input</br>
     *
     * @param valueObjectsCollection collection containing the elements to create the map entries to add to the <code>map</code>, or to use to update the existing ones in the <code>map</code>
     * @param map map that will be modified by this method
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entry's key in the map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param valueObjectValueAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries values  in the <code>map</code>.</br>
     *                                      If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectKeyAttributeName</code>
     * @param mapValueClassAttributeToSetName  name of the attribute of <code>mapValueClass</code> that will be set with the value of the valueObject attribute specified by the string input parameter <code>valueObjectValueAttributeName</code>.</br>
     *                                         If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectValueAttributeName</code>.
     * @param mapValueClass Class of the entries values of the <code>map</code>. A new instance of this class will be created (with no arguments constructor) for each element of the collection <code>valueObjectsCollection</code>.</br>
     *                      The attribute of <code>mapValueClass</code> named <code>mapValueClassAttributeToSetName</code> will be populated with the value of  attribute named <code>valueObjectValueAttributeName</code> in the element of the collection <code>valueObjectsCollection</code>, if that value is not null.</br>
     *                      Of course has to exist an attribute named with the value  <code>valueObjectValueAttributeName</code>in the the class of the element of the collection <code>valueObjectsCollection</code>
     *                      and an attribute named <code>mapValueClassAttributeToSetName</code> in the class <code>mapValueClass</code> .
     * @throws BusinessObjectException
     */
    public static void update(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass) throws BusinessObjectException {
        update(valueObjectsCollection, map, valueObjectKeyAttributeName, valueObjectValueAttributeName, mapValueClassAttributeToSetName, mapValueClass, Boolean.TRUE);
    }

    /**
     * This method adds new entries to this Map, or update the existing ones.</br>
     * To do his job this method calls {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass, Boolean overwrite)}
     * with <code>overwrite = True</code> and where as <code>mapValueClassAttributeToSetName</code> is passed the same  <code>valueObjectValueAttributeName</code>
     *
     * @param valueObjectsCollection collection containing the elements to create the map entries to add to this Map, or to use to update the existing ones in this Map
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entry's key in the map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param valueObjectValueAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries values  in this Map.</br>
     *                                      If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectKeyAttributeName</code>
     * @param mapValueClass Class of the entries values of this Map. A new instance of this class will be created (with no arguments constructor) for each element of the collection <code>valueObjectsCollection</code>.</br>
     *                      The attribute of <code>mapValueClass</code> named <code>valueObjectValueAttributeName</code> will be populated with the value of attribute of the same name <code>valueObjectValueAttributeName</code> in the element of the collection <code>valueObjectsCollection</code>, if that value is not null.</br>
     *                      Of course has to exist an attribute named with the value  <code>valueObjectValueAttributeName</code>in the the class of the element of the collection <code>valueObjectsCollection</code>
     *                      and in the class <code>mapValueClass</code> .
     * @throws BusinessObjectException
     */
    public void update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, Class mapValueClass) throws BusinessObjectException {
        update(valueObjectsCollection, valueObjectKeyAttributeName, valueObjectValueAttributeName, valueObjectValueAttributeName, mapValueClass, Boolean.TRUE);
    }

    /**
     * This method works in the same way of {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName,Class mapValueClass)},
     * with the difference that in place of acting on this Map, it works statically on a map provided by input</br>
     *
     * @param valueObjectsCollection collection containing the elements to create the map entries to add to the <code>map</code>, or to use to update the existing ones in the <code>map</code>
     * @param map map that will be modified by this method
     * @param valueObjectKeyAttributeName  name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entry's key in the map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param valueObjectValueAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries values  in the <code>map</code>.</br>
     *                                      If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectKeyAttributeName</code>
     * @param mapValueClass Class of the entries values of the <code>map</code>. A new instance of this class will be created (with no arguments constructor) for each element of the collection <code>valueObjectsCollection</code>.</br>
     *                      The attribute of <code>mapValueClass</code> named <code>valueObjectValueAttributeName</code> will be populated with the value of attribute of the same name <code>valueObjectValueAttributeName</code> in the element of the collection <code>valueObjectsCollection</code>, if that value is not null.</br>
     *                      Of course has to exist an attribute named with the value  <code>valueObjectValueAttributeName</code>in the the class of the element of the collection <code>valueObjectsCollection</code>
     *                      and in the class <code>mapValueClass</code> .
     * @throws BusinessObjectException
     */
    public static void update(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, Class mapValueClass) throws BusinessObjectException {
        update(valueObjectsCollection, map, valueObjectKeyAttributeName, valueObjectValueAttributeName, valueObjectValueAttributeName, mapValueClass, Boolean.TRUE);
    }

    /**
     * This method adds new entries to this Map, or update the existing ones.</br>
     * To do his job this method calls {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass, Boolean overwrite)}
     * with <code>overwrite = True</code> and where for both  <code>valueObjectValueAttributeName</code> and <code>mapValueClassAttributeToSetName</code> is passed the same  <code>valueObjectKeyAttributeName</code>
     *
     * @param valueObjectsCollection  collection containing the elements to create the map entries to add to this Map, or to use to update the existing ones in this Map
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entry's key in the map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param mapValueClass Class of the entries values of this Map. A new instance of this class will be created (with no arguments constructor) for each element of the collection <code>valueObjectsCollection</code>.</br>
     *                      The attribute of <code>mapValueClass</code> named <code>valueObjectKeyAttributeName</code> will be populated with the value of attribute of the same name <code>valueObjectKeyAttributeName</code> in the element of the collection <code>valueObjectsCollection</code>, if that value is not null.</br>
     *                      Of course has to exist an attribute named with the value  <code>valueObjectKeyAttributeName</code>in the the class of the element of the collection <code>valueObjectsCollection</code>
     *                      and in the class <code>mapValueClass</code> .
     * @throws BusinessObjectException
     */
    public void update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, Class mapValueClass) throws BusinessObjectException {
        update(valueObjectsCollection, valueObjectKeyAttributeName, valueObjectKeyAttributeName, valueObjectKeyAttributeName, mapValueClass, Boolean.TRUE);
    }

    /**
     * This method works in the same way of {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, Class mapValueClass)},
     * with the difference that in place of acting on this Map, it works statically on a map provided by input</br>
     *
     * @param valueObjectsCollection  collection containing the elements to create the map entries to add to the <code>map</code>, or to use to update the existing ones in the <code>map</code>
     * @param map map that will be modified by this method
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entry's key in the map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param mapValueClass Class of the entries values of the <code>map</code>. A new instance of this class will be created (with no arguments constructor) for each element of the collection <code>valueObjectsCollection</code>.</br>
     *                      The attribute of <code>mapValueClass</code> named <code>valueObjectKeyAttributeName</code> will be populated with the value of attribute of the same name <code>valueObjectKeyAttributeName</code> in the element of the collection <code>valueObjectsCollection</code>, if that value is not null.</br>
     *                      Of course has to exist an attribute named with the value  <code>valueObjectKeyAttributeName</code>in the the class of the element of the collection <code>valueObjectsCollection</code>
     *                      and in the class <code>mapValueClass</code> .
     * @throws BusinessObjectException
     */
    public static void update(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, Class mapValueClass) throws BusinessObjectException {
        update(valueObjectsCollection, map, valueObjectKeyAttributeName, valueObjectKeyAttributeName, valueObjectKeyAttributeName, mapValueClass, Boolean.TRUE);
    }

    /**
     * This method adds new entries to this Map, or update the existing ones.</br>
     * To do his job this method calls {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass, Boolean overwrite)}
     * with <code>overwrite = True</code> and both <code>mapValueClassAttributeToSetName</code> and <code>mapValueClass</code> are null.
     *
     * @param valueObjectsCollection  collection containing the elements to create the map entries to add to this Map, or to use to update the existing ones in this Map
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entry's key in the map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param valueObjectValueAttributeName  name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries values  in this Map.</br>
     *                                      If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectKeyAttributeName</code>
     * @throws BusinessObjectException
     */
    public void update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName) throws BusinessObjectException {
        update(valueObjectsCollection, valueObjectKeyAttributeName, valueObjectValueAttributeName, null, null, Boolean.TRUE);
    }

    /**
     * This method works in the same way of {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName)},
     * with the difference that in place of acting on this Map, it works statically on a map provided by input</br>
     *
     * @param valueObjectsCollection collection containing the elements to create the map entries to add to the <code>map</code>, or to use to update the existing ones in the <code>map</code>
     * @param map map that will be modified by this method
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entry's key in the map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param valueObjectValueAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries values  in the <code>map</code>.</br>
     *                                      If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectKeyAttributeName</code>
     * @throws BusinessObjectException
     */
    public static void update(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName) throws BusinessObjectException {
        update(valueObjectsCollection, map, valueObjectKeyAttributeName, valueObjectValueAttributeName, null, null, Boolean.TRUE);
    }

    /**
     * This method adds new entries to this Map, or update the existing ones.</br>
     * To do his job this method calls {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass, Boolean overwrite)}
     * with <code>overwrite = True</code> and where as <code>valueObjectValueAttributeName</code> is passed the same  <code>valueObjectKeyAttributeName</code>
     * and both <code>mapValueClassAttributeToSetName</code> and <code>mapValueClass</code> are null.
     *
     * @param valueObjectsCollection collection containing the elements to create the map entries to add to this Map, or to use to update the existing ones in this Map
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entry's key in the map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @throws BusinessObjectException
     */
    public void update(Collection valueObjectsCollection, String valueObjectKeyAttributeName) throws BusinessObjectException {
        update(valueObjectsCollection, valueObjectKeyAttributeName, valueObjectKeyAttributeName, null, null, Boolean.TRUE);
    }

    /**
     * This method works in the same way of {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName)},
     * with the difference that in place of acting on this Map, it works statically on a map provided by input</br>
     *
     * @param valueObjectsCollection  collection containing the elements to create the map entries to add to the <code>map</code>, or to use to update the existing ones in the <code>map</code>
     * @param map map that will be modified by this method
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entry's key in the map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @throws BusinessObjectException
     */
    public static void update(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName) throws BusinessObjectException {
        update(valueObjectsCollection, map, valueObjectKeyAttributeName, valueObjectKeyAttributeName, null, null, Boolean.TRUE);
    }

    /**
     * This method adds new map-entries to, and/or updates the values of existing entries in this Map on the base of the values of specific attributes of the elements of <code>valueObjectsCollection</code>.</br>
     * The elements of <code>valueObjectsCollection</code>, as his name states, are valueObjects.</br>
     * </br>
     * This method generates map-entries for this Map using as keys the values of the valueObjects attribute named as specified by the string parameter <code>valueObjectKeyAttributeName</code>.</br>
     * More precisely, for each valueObject, if its attribute specified by the string <code>valueObjectKeyAttributeName</code> has not null value, will be generated a map entry with the same value as key;
     * the value of the map entry will be a new instance the class <code>mapValueClass</code>, or , if <code>mapValueClass</code> is null, will be the valueObject's attribute value of the attribute specified by the string <code>valueObjectValueAttributeName</code>.</br>
     * So resuming is created a Map entry for each not null value of the <code>valueObjectsCollection</code>'s valueObjects attribute specified by the input parameter <code>valueObjectKeyAttributeName</code>, with entry key the value of the <code>valueObjectKeyAttributeName</code>
     * and entry value the  value of the <code>valueObjectValueAttributeName</code> or a new instance of <code>mapValueClass</code>. </br>
     * Of course each valueObjects of <code>valueObjectsCollection</code> must have an attribute named as the value of <code>valueObjectKeyAttributeName</code>, otherwise a BusinessObjectException will be thrown.</br>
     * If the input string parameter <code>valueObjectKeyAttributeName</code> is null or empty string or blank characters string, a BusinessObjectException will be thrown as well.</br>
     * </br>
     * If <code>mapValueClass</code> is not null the value of the entry value of each generated entry is a new instance of <code>mapValueClass</code> created with the no arguments constructor. This newly created instance must have
     * an attribute named as the value of <code>mapValueClassAttributeToSetName</code>, and this attribute will be set with the value of the attribute specified by the string <code>valueObjectValueAttributeName</code> of the correspondent valueObject,
     * if this value is not null. If this value is null the attribute named as the value of <code>mapValueClassAttributeToSetName</code> won't be set at all.</br>
     * As in both cases of <code>mapValueClass</code> null or not null, the attribute specified by <code>valueObjectValueAttributeName</code> is needed for the entry value, each valueObjects of <code>valueObjectsCollection</code>
     * must have an attribute named as the value of the input string <code>valueObjectValueAttributeName</code>, otherwise a BusinessObjectException will be thrown.</br>
     * If the input string parameter <code>valueObjectValueAttributeName</code> is null or empty string or blank characters string, <code>valueObjectValueAttributeName</code>
     * is forced to assume the same value of the input string parameter <code>valueObjectKeyAttributeName</code>.</br>
     * Analogously the <code>mapValueClass</code>, if not null, must have an attribute named as specified by the input string parameter <code>mapValueClassAttributeToSetName</code> </br>
     * If the input string parameter <code>mapValueClassAttributeToSetName</code> is null or empty string or blank characters string, <code>mapValueClassAttributeToSetName</code>
     * is forced to assume the same value of the input string parameter <code>valueObjectValueAttributeName</code> (that eventually has been forced to assume the value of <code>valueObjectKeyAttributeName<code> ).</br>
     * All the entries generated by this method will be added to this Map, if not existing yet an entry in this Map with the same key, and if such key is yet existing, this Map
     * will be updated with the new entry value if and only if is verified the following condition: </br>
     * the input parameter <code>overwrite</code> is true or null, or in this Map, the value of entry to update is null.</br>
     * So if an existing entry has a null value in this Map, it can be updated independently of the value of <code>overwrite</code>, and the default value of <code>overwrite</code> is true.</br>
     * </br>
     *
     * @param valueObjectsCollection collection containing the elements to create the map entries to add to this Map, or to use to update the existing ones in this Map
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entry's key in the map.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param valueObjectValueAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries values  in this Map.</br>
     *                                      If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectKeyAttributeName</code>
     * @param mapValueClassAttributeToSetName name of the attribute of <code>mapValueClass</code> that will be set with the value of the valueObject attribute specified by the string input parameter <code>valueObjectValueAttributeName</code>.</br>
     *                                          If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectValueAttributeName</code>.
     * @param mapValueClass Class of the entries values of this Map. A new instance of this class will be created (with no arguments constructor) for each element of the collection <code>valueObjectsCollection</code>.</br>
     *                      The attribute of <code>mapValueClass</code> named <code>mapValueClassAttributeToSetName</code> will be populated with the value of  attribute named <code>valueObjectValueAttributeName</code> in the element of the collection <code>valueObjectsCollection</code>, if that value is not null.</br>
     *                      Of course has to exist an attribute named with the value  <code>valueObjectValueAttributeName</code>in the the class of the element of the collection <code>valueObjectsCollection</code>
     *                      and an attribute named <code>mapValueClassAttributeToSetName</code> in the class <code>mapValueClass</code> .
     * @param overwrite parameter that specifies if existing entries with not null values must be updated or not. Note that if the entry to update has a null value will be updated independently of this parameter.</br>
     *                  If this parameter is null will assume True as default.
     * @throws BusinessObjectException
     */
    private void update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass, Boolean overwrite) throws BusinessObjectException {
        try {
            if (valueObjectsCollection == null) return;
            if (valueObjectKeyAttributeName == null || valueObjectKeyAttributeName.trim().equals("")) throw new BusinessObjectException("Error in  ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite):valueObjectKeyAttributeName can't be null or empty or blank string ");
            if (valueObjectValueAttributeName == null || valueObjectValueAttributeName.trim().equals((""))) valueObjectValueAttributeName = valueObjectKeyAttributeName;
            if (mapValueClassAttributeToSetName == null || mapValueClassAttributeToSetName.trim().equals((""))) mapValueClassAttributeToSetName = valueObjectValueAttributeName;
            if (overwrite == null) overwrite = Boolean.TRUE;
            PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
            Iterator iterator = valueObjectsCollection.iterator();
            if (mapValueClass == null) {
                while (iterator.hasNext()) {
                    Object object = iterator.next();
                    Object valueObjectKeyAttributeValue = propertyUtilsBean.getProperty(object, valueObjectKeyAttributeName);
                    if (valueObjectKeyAttributeValue != null) {
                        if (overwrite.booleanValue() || get(valueObjectKeyAttributeValue) == null) {
                            put(valueObjectKeyAttributeValue, propertyUtilsBean.getProperty(object, valueObjectValueAttributeName));
                        }
                    }
                }
            } else {
                while (iterator.hasNext()) {
                    Object object = iterator.next();
                    Object valueObjectKeyAttributeValue = propertyUtilsBean.getProperty(object, valueObjectKeyAttributeName);
                    if (valueObjectKeyAttributeValue != null) {
                        Object valueObjectValueAttributeNameValue = propertyUtilsBean.getProperty(object, valueObjectValueAttributeName);
                        Object mapEntryValue = ConstructorUtils.invokeConstructor(mapValueClass, null, null);
                        if (valueObjectValueAttributeNameValue != null) {
                            propertyUtilsBean.setProperty(mapEntryValue, mapValueClassAttributeToSetName, valueObjectValueAttributeNameValue);
                        }
                        if (overwrite.booleanValue() || get(valueObjectKeyAttributeValue) == null) {
                            put(propertyUtilsBean.getProperty(object, valueObjectKeyAttributeName), mapEntryValue);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException caught in ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString());
            throw new BusinessObjectException("BusinessObjectException thrown in  ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString(), e);
        } catch (InvocationTargetException e) {
            log.error("IllegalAccessException caught in ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString());
            throw new BusinessObjectException("BusinessObjectException thrown in  ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString(), e);
        } catch (NoSuchMethodException e) {
            log.error("NoSuchMethodException caught in ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString());
            throw new BusinessObjectException("BusinessObjectException thrown in  ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString(), e);
        } catch (InstantiationException e) {
            log.error("InstantiationException caught in ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString());
            throw new BusinessObjectException("BusinessObjectException thrown in  ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception thrown in ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString());
            throw new BusinessObjectException("BusinessObjectException thrown in  ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString(), e);
        }
    }

    /**
     * This method works in the same way of {@link #update(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite)},
     * with the difference that in place of acting on this Map, it works statically on a map provided by input</br>
     * @param valueObjectsCollection collection containing the elements to create the map entries to add to <code>map</code>, or to use to update the existing ones in <code>map</code>
     * @param map map that will be modified by this method
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entry's key in <code>map</code>.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param valueObjectValueAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be used as entries values  in <code>map</code>.</br>
     *                                      If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectKeyAttributeName</code>
     * @param mapValueClassAttributeToSetName name of the attribute of <code>mapValueClass</code> that will be set with the value of the valueObject attribute specified by the string input parameter <code>valueObjectValueAttributeName</code>.</br>
     *                                       If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectValueAttributeName</code>.
     * @param mapValueClass Class of the entries values of the <code>map</code>. A new instance of this class will be created (with no arguments constructor) for each element of the collection <code>valueObjectsCollection</code>.</br>
     *                      The attribute of <code>mapValueClass</code> named <code>mapValueClassAttributeToSetName</code> will be populated with the value of  attribute named <code>valueObjectValueAttributeName</code> in the element of the collection <code>valueObjectsCollection</code>, if that value is not null.</br>
     *                      Of course has to exist an attribute named with the value  <code>valueObjectValueAttributeName</code>in the the class of the element of the collection <code>valueObjectsCollection</code>
     *                      and an attribute named <code>mapValueClassAttributeToSetName</code> in the class <code>mapValueClass</code> .
     * @param overwrite parameter that specifies if existing entries with not null values must be updated or not. Note that if the entry to update has a null value will be updated independently  of this parameter.</br>
     *                  If this parameter is null will assume True as default
     * @throws BusinessObjectException
     */
    private static void update(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName, Class mapValueClass, Boolean overwrite) throws BusinessObjectException {
        try {
            if (valueObjectsCollection == null) return;
            if (valueObjectKeyAttributeName == null || valueObjectKeyAttributeName.trim().equals("")) throw new BusinessObjectException("Error in  ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite):valueObjectKeyAttributeName can't be null or empty or blank string ");
            if (valueObjectValueAttributeName == null || valueObjectValueAttributeName.trim().equals((""))) valueObjectValueAttributeName = valueObjectKeyAttributeName;
            if (mapValueClassAttributeToSetName == null || mapValueClassAttributeToSetName.trim().equals((""))) mapValueClassAttributeToSetName = valueObjectValueAttributeName;
            if (overwrite == null) overwrite = Boolean.TRUE;
            PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
            Iterator iterator = valueObjectsCollection.iterator();
            if (mapValueClass == null) {
                while (iterator.hasNext()) {
                    Object object = iterator.next();
                    Object valueObjectKeyAttributeValue = propertyUtilsBean.getProperty(object, valueObjectKeyAttributeName);
                    if (valueObjectKeyAttributeValue != null) {
                        if (overwrite.booleanValue() || map.get(valueObjectKeyAttributeValue) == null) {
                            map.put(valueObjectKeyAttributeValue, propertyUtilsBean.getProperty(object, valueObjectValueAttributeName));
                        }
                    }
                }
            } else {
                while (iterator.hasNext()) {
                    Object object = iterator.next();
                    Object valueObjectKeyAttributeValue = propertyUtilsBean.getProperty(object, valueObjectKeyAttributeName);
                    if (valueObjectKeyAttributeValue != null) {
                        Object valueObjectValueAttributeNameValue = propertyUtilsBean.getProperty(object, valueObjectValueAttributeName);
                        Object mapEntryValue = ConstructorUtils.invokeConstructor(mapValueClass, null, null);
                        if (valueObjectValueAttributeNameValue != null) {
                            propertyUtilsBean.setProperty(mapEntryValue, mapValueClassAttributeToSetName, valueObjectValueAttributeNameValue);
                        }
                        if (overwrite.booleanValue() || map.get(valueObjectKeyAttributeValue) == null) {
                            map.put(propertyUtilsBean.getProperty(object, valueObjectKeyAttributeName), mapEntryValue);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException caught ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString());
            throw new BusinessObjectException("BusinessObjectException thrown in  ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString(), e);
        } catch (InvocationTargetException e) {
            log.error("IllegalAccessException caught in ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString());
            throw new BusinessObjectException("BusinessObjectException thrown in  ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString(), e);
        } catch (NoSuchMethodException e) {
            log.error("NoSuchMethodException caught in ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString());
            throw new BusinessObjectException("BusinessObjectException thrown in  ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection,Map map,  String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString(), e);
        } catch (InstantiationException e) {
            log.error("InstantiationException caught in ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString());
            throw new BusinessObjectException("BusinessObjectException thrown in  ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString(), e);
        } catch (Exception e) {
            log.error("Exception caught in ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString());
            throw new BusinessObjectException("BusinessObjectException thrown in  ValueObjectsAttributeMapBO.update(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName, String mapValueClassAttributeToSetName,Class mapValueClass, Boolean overwrite): " + e.toString(), e);
        }
    }

    /**
     * This method updates an attribute of specific elements of the  collection <code>valueObjectsCollection</code> with elements of  this Map.</br>
     * This method has the same behavior of {@link #updateCollection(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName)}
     * where as <code>valueObjectValueAttributeName<code> is  passed  <code>valueObjectKeyAttributeName<code>
     *
     * @param valueObjectsCollection Collection whose elements attribute have to be update
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, whose value has to match with an entry's key this Map,
     *                                      to update the attribute <code>valueObjectValueAttributeName</code> of the same element with the entry value.</br>
     *                                     If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @throws BusinessObjectException  for any trouble
     */
    public void updateCollection(Collection valueObjectsCollection, String valueObjectKeyAttributeName) throws BusinessObjectException {
        updateCollection(valueObjectsCollection, valueObjectKeyAttributeName, valueObjectKeyAttributeName);
    }

    /**
     * This method updates an attribute of specific elements of the  collection <code>valueObjectsCollection</code> with elements of  this Map.</br>
     * For each <code>key</code> of this Map, this method looks for all the elements in <code>valueObjectsCollection</code> having the attribute <code>valueObjectKeyAttributeName==key</code>.</br>
     * For all the elements found, this method updates the attribute <code>valueObjectValueAttributeName</code>  with <code>this.get(key)</code>.</br>
     * If  valueObjectsCollection is null nothing is done.</br>
     *
     * @param valueObjectsCollection  Collection whose elements attribute have to be update
     * @param valueObjectKeyAttributeName  name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, whose value has to match with an entry's this Map,
     *                                    to update the attribute <code>valueObjectValueAttributeName</code> of the same element with the entry value.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param valueObjectValueAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, whose value has to match with an entry's key this Map,
     *                                      to update the attribute <code>valueObjectValueAttributeName</code> of the same element with the entry value.</br>
     *                                     If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @throws BusinessObjectException
     */
    public void updateCollection(Collection valueObjectsCollection, String valueObjectKeyAttributeName, String valueObjectValueAttributeName) throws BusinessObjectException {
        updateCollection(valueObjectsCollection, this, valueObjectKeyAttributeName, valueObjectValueAttributeName);
    }

    /**
     * This method updates an attribute of specific elements of the  collection <code>valueObjectsCollection</code> with elements of  the input parameter <code>map</code>.</br>
     * This method has the same behavior of {@link #updateCollection(Collection valueObjectsCollection,  Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName)}
     * where as <code>valueObjectValueAttributeName<code> is  passed  <code>valueObjectKeyAttributeName<code>
     *
     * @param valueObjectsCollection  Collection whose elements attribute have to be update
     * @param map map used to update the <code>valueObjectsCollection</code>
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, whose value has to match with an entry's key of  <code>map</code>,
     *                                    to update the attribute <code>valueObjectValueAttributeName</code> of the same element with the entry value.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @throws BusinessObjectException -
     *       <code>map</code> is null.</br>
     *      if  <code>valueObjectKeyAttributeName</code>  is null, or empty or blank characters string.</br>
     */
    public static void updateCollection(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName) throws BusinessObjectException {
        updateCollection(valueObjectsCollection, map, valueObjectKeyAttributeName, valueObjectKeyAttributeName);
    }

    /**
     * This method updates an attribute of specific elements of the  collection <code>valueObjectsCollection</code> with elements of  the input parameter <code>map</code>.</br>
     * For each <code>key</code> of the <code>map</code>, this method looks for all the elements in <code>valueObjectsCollection</code> having the attribute <code>valueObjectKeyAttributeName==key</code>.</br>
     * For all the elements found, this method updates the attribute <code>valueObjectValueAttributeName</code>  with <code>map.get(key)</code>.</br>
     * If  valueObjectsCollection is null nothing is done.</br>
     * @param valueObjectsCollection Collection whose elements attribute have to be update
     * @param map map used to update the <code>valueObjectsCollection</code>
     * @param valueObjectKeyAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, whose value has to match with an entry's key of  <code>map</code>,
     *                                    to update the attribute <code>valueObjectValueAttributeName</code> of the same element with the entry value.</br>
     *                                    If this parameter is null, or empty or blank characters string a BusinessObjectException will be thrown.
     * @param valueObjectValueAttributeName name of the attribute of the generic element of the collection <code>valueObjectsCollection</code>, that will be updated with the entry value of the <code>map</code>
     *                                      If this parameter is null, or empty or blank characters string it will be forced to assume the value of <code>valueObjectKeyAttributeName</code>.
     * @throws BusinessObjectException -
     *       <code>map</code> is null.</br>
     *      if  <code>valueObjectKeyAttributeName</code>  is null, or empty or blank characters string.</br>
     */
    public static void updateCollection(Collection valueObjectsCollection, Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName) throws BusinessObjectException {
        try {
            if (valueObjectsCollection == null) return;
            if (valueObjectKeyAttributeName == null || valueObjectKeyAttributeName.trim().equals("")) throw new BusinessObjectException("Error in  ValueObjectsAttributeMapBO.updateCollection(Collection valueObjectsCollection,  Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName):valueObjectKeyAttributeName can't be null or empty or blank string ");
            if (valueObjectValueAttributeName == null || valueObjectValueAttributeName.trim().equals((""))) valueObjectValueAttributeName = valueObjectKeyAttributeName;
            PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
            Object key;
            Iterator iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                key = iterator.next();
                Collection collection = Utils.selectWhereFieldEqualsTo(valueObjectsCollection, valueObjectKeyAttributeName, key);
                if (collection != null) {
                    Object value = map.get(key);
                    Iterator collectionIterator = collection.iterator();
                    while (collectionIterator.hasNext()) {
                        propertyUtilsBean.setProperty(collectionIterator.next(), valueObjectValueAttributeName, value);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Exception caught in ValueObjectsAttributeMapBO.updateCollection(Collection valueObjectsCollection,  Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName): " + e.toString());
            throw new BusinessObjectException("BusinessObjectException thrown in ValueObjectsAttributeMapBO.updateCollection(Collection valueObjectsCollection,  Map map, String valueObjectKeyAttributeName, String valueObjectValueAttributeName)" + e.toString(), e);
        }
    }
}
