package org.stars.util.reflect;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import org.stars.datastructure.map.CaseInsensitiveLinkedHashMap;
import org.stars.datastructure.stack.Stack;

/**
 * <p>
 * Questa è una classe di utilità per lavorare con i bean e le relative
 * proprietà.
 * <p>
 * Per bean si intende una classe che espone i propri attributi mediante
 * proprietà
 * <p>
 * Per proprietà si intende una caratterista dell'oggetto che possiede un getter
 * ed un setter.
 * <p>
 * Le proprietà prese in considerazione sono quelle di visibilità pubblica. I
 * getter ed i setter privati vengono ignorati.
 * <p>
 * Questa classe nasce per bypassare i problemi di recupero dei tipi di
 * attributi di una classe contenente dei generic.
 * </p>
 * <p>
 * Le mappe che i metodi di questa classe restituiscono sono tipicamente delle
 * mappe con chiave stringhe case insensitive.
 * </p>
 * 
 * @author Francesco Benincasa (908099)
 * @since 10/10/2009
 * 
 */
public abstract class BeanUtil {

    /**
	 * Date due mappe di getter, unisce i nomi delle proprietà dei due insiemi.
	 * L'insieme è in lowercase
	 * 
	 * @param map1
	 * @param map2
	 * @return insieme delle proprietà
	 */
    protected static Set<String> joinProperties(Map<String, Getter> map1, Map<String, Getter> map2) {
        Set<String> set = new TreeSet<String>();
        for (Getter item : map1.values()) {
            set.add(item.propertyName.toLowerCase());
        }
        for (Getter item : map2.values()) {
            set.add(item.propertyName.toLowerCase());
        }
        return set;
    }

    /**
	 * Dati due oggetti di qualunque tipo, restituisce una mappa contenente
	 * tutte le proprietà con valore diverso.
	 * <p>
	 * Vengono registrate anche le proprietà che in uno dei due bean non
	 * esistono.
	 * <p>
	 * 
	 * 
	 * @param a
	 * @param b
	 * @return mappa di valori. La chiave è il nome della proprietà. Il valore
	 *         <code>a</code> è il valore della proprietà nell'oggetto a, il
	 *         valore <code>b</code> è il valore della proprietà nel secondo
	 *         oggetto.
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
    public static Map<String, ValuePair> findDiff(Object a, Object b) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Map<String, ValuePair> map = new CaseInsensitiveLinkedHashMap<ValuePair>();
        ValuePair temp;
        Object value;
        if (a != null) {
            Map<String, Getter> map1 = getGetters(a.getClass());
            for (Getter item : map1.values()) {
                if (item.ignoredOnEquals) continue;
                temp = new ValuePair(item.invoke(a), null);
                map.put(item.propertyName, temp);
            }
        }
        if (b != null) {
            Map<String, Getter> map2 = getGetters(b.getClass());
            for (Getter item : map2.values()) {
                temp = map.get(item.propertyName);
                if (temp == null) {
                    temp = new ValuePair(null, item.invoke(b));
                    map.put(item.propertyName, temp);
                } else {
                    value = item.invoke(b);
                    if (item.ignoredOnEquals) value = null;
                    if (value == null && temp.a == null) {
                        map.remove(item.propertyName);
                    } else {
                        temp.b = value;
                        if ((value == null && temp.a != null) || (value != null && temp.a == null)) {
                        } else if (temp.a.equals(temp.b)) {
                            map.remove(item.propertyName);
                        }
                    }
                }
            }
        }
        return map;
    }

    /**
	 * Date due classi, ottiene un array di accessor per leggere da un bean e
	 * scrivere sull'altro bean solo le proprietà con gli stessi nomi.
	 * <p>
	 * Non viene fatto alcun controllo sul tipo di proprietà.
	 * 
	 * @param clazzSrc
	 * @param clazzDest
	 * @return
	 */
    public static AccessorPair[] join(Class<?> clazzSrc, Class<?> clazzDest) {
        List<AccessorPair> list = new LinkedList<AccessorPair>();
        AccessorPair temp;
        Getter getter;
        Setter setter;
        Map<String, Getter> map1 = getGetters(clazzSrc);
        Map<String, Setter> map2 = getSetters(clazzDest);
        Set<String> set = new TreeSet<String>();
        for (Getter item : map1.values()) {
            set.add(item.propertyName.toLowerCase());
        }
        for (Setter item : map2.values()) {
            set.add(item.propertyName.toLowerCase());
        }
        for (String item : set) {
            getter = map1.get(item);
            setter = map2.get(item);
            if (getter != null && setter != null) {
                temp = new AccessorPair(getter, setter);
                list.add(temp);
            }
        }
        AccessorPair[] a = new AccessorPair[0];
        return list.toArray(a);
    }

    /**
	 * Dato un oggetto ed un nome di una sua proprietà, ne restituisce la classe
	 * 
	 * @param obj
	 *            istanza dell'oggetto
	 * @param propertyName
	 *            nome dell'attributo, case insensitive
	 * @return tipo dell'attributo
	 * @throws Exception
	 *             in caso di errore
	 */
    public static Class<?> getPropertyType(Object obj, String propertyName) throws Exception {
        Map<String, Getter> attributeMap = BeanUtil.getGetters(obj);
        Getter getter = attributeMap.get(propertyName);
        if (getter != null) {
            return getter.getPropertyType();
        }
        Exception ex = new Exception("Cannot get attribute " + propertyName + " from class " + obj.getClass().getSimpleName());
        throw ex;
    }

    /**
	 * Dato un oggetto src e un set di proprietà, questo metodo recupera tutte
	 * le proprietà indicate e le restituisce sottoforma di hashmap.
	 * 
	 * @param src
	 *            oggetto sorgente
	 * @param inputProps
	 *            set delle proprietà da recuperare
	 * @return mappa con i valori delle proprietà desiderate
	 * 
	 * @throws Exception
	 *             in caso di errore
	 */
    public static Map<String, Object> getPropertyValues(Object src, Set<String> inputProps) throws Exception {
        Map<String, Getter> mapGetter = BeanUtil.getGetters(src);
        Map<String, Object> ret = new CaseInsensitiveLinkedHashMap<Object>();
        Object value;
        Getter getter;
        for (String item : inputProps) {
            getter = mapGetter.get(item);
            if (getter != null) {
                try {
                    value = getter.invoke(src);
                    ret.put(getter.propertyName, value);
                } catch (Exception e) {
                }
            }
        }
        return ret;
    }

    /**
	 * Imposta tutte le proprietà di un oggetto, in base alla hashmap che
	 * contiene i nomi delle proprietà ed i rispettivi valori.
	 * 
	 * @param obj
	 *            oggetto le cui proprietà devono essere valorizzate
	 * @param values
	 *            map contenente i nomi delle proprietà in uppercase ed i
	 *            rispettivi valori
	 * 
	 * @throws Exception
	 *             in caso di errore
	 */
    public static void setPropertyValues(Object obj, Map<String, Object> values) throws Exception {
        Class<?> classSrc = obj.getClass();
        Map<String, Setter> mapSetter = BeanUtil.getSetters(classSrc);
        Setter setter;
        for (Entry<String, Object> item : values.entrySet()) {
            setter = mapSetter.get(item.getKey());
            if (setter != null) {
                try {
                    setter.invoke(obj, item.getValue());
                } catch (Exception e) {
                }
            }
        }
    }

    /**
	 * Dato un oggetto ed un nome di una sua proprietà, ne restituisce il
	 * valore.
	 * 
	 * @param src
	 *            istanza dell'oggetto
	 * @param propertyName
	 *            nome dell'attributo, case insensitive
	 * @return valore dell'attributo
	 * @throws Exception
	 *             in caso di errore
	 */
    public static Object getPropertyValue(Object src, String propertyName) throws Exception {
        Map<String, Getter> attributeMap = BeanUtil.getGetters(src);
        Getter getter = attributeMap.get(propertyName);
        if (getter != null) {
            return getter.invoke(src);
        }
        Exception ex = new Exception("Can not get property " + propertyName + " from class " + src.getClass().toString());
        throw ex;
    }

    /**
	 * Imposta tutte le proprietà di un oggetto, in base alla hashmap che
	 * contiene i nomi delle proprietà ed i rispettivi valori.
	 * 
	 * @param obj
	 *            oggetto le cui proprietà devono essere valorizzate
	 * @param values
	 *            hashmap contenente i nomi degli attributi in uppercase ed i
	 *            rispettivi valori
	 * 
	 * @throws Exception
	 *             in caso di errore
	 */
    public static <E> void setPropertyValue(Object obj, String propertyName, E value) throws Exception {
        Map<String, Setter> mapSetter = BeanUtil.getSetters(obj);
        Setter setter = mapSetter.get(propertyName);
        if (setter != null) {
            try {
                setter.invoke(obj, value);
            } catch (Exception e) {
            }
        }
    }

    /**
	 * Dati due oggetti, provvedere a copiare tutte le proprietà di beanSrc che
	 * hanno un'equivalente proprietà (con lo stesso nome) in beanDest.
	 * 
	 * @param beanSrc
	 *            oggetto sorgente
	 * @param beanDest
	 *            oggetto destinazione
	 * @throws Exception
	 *             in caso di errore
	 * @param beanSrc
	 * @param beanDest
	 * @throws Exception
	 */
    public static void copy(Object beanSrc, Object beanDest) throws Exception {
        Map<String, Getter> mapSrc = BeanUtil.getGetters(beanSrc);
        Map<String, Setter> mapDest = BeanUtil.getSetters(beanDest);
        Setter setter;
        Object value = null;
        for (Getter item : mapSrc.values()) {
            try {
                setter = mapDest.get(item.getPropertyName());
                if (setter == null || item.isIgnoredOnCopy() || setter.isIgnoredOnCopy()) {
                    continue;
                }
                value = item.invoke(beanSrc);
                setter.invoke(beanDest, value);
            } catch (Exception e) {
            }
        }
    }

    /**
	 * <p>
	 * Recupera una mappa contenente il nome della proprietà ed il relativo
	 * metodo getter a partire da un bean. Nel caso venga passata un bean
	 * <code>null</code> viene restituito <code>null</code>.
	 * </p>
	 * <p>
	 * La mappa restituita ha le chiavi di tipo case insensitive.
	 * </p>
	 * 
	 * @param bean
	 *            oggetto da analizzare
	 * @return mappa degli attributi e dei relativi getter o <code>null</code>
	 */
    public static Map<String, Setter> getSetters(Object bean) {
        if (bean == null) {
            Map<String, Setter> mapField = new CaseInsensitiveLinkedHashMap<Setter>();
            return mapField;
        }
        return getSetters(bean.getClass());
    }

    /**
	 * <p>
	 * Recupera una mappa contenente il nome della proprietà ed il relativo
	 * metodo setter a partire da una clase. Nel caso venga passata una classe
	 * <code>null</code> viene restituita una mappa con 0 elementi.
	 * </p>
	 * <p>
	 * La mappa restituita ha le chiavi di tipo case insensitive.
	 * </p>
	 * 
	 * @param clazz
	 *            classe da analizzare
	 * @return mappa degli attributi e dei relativi setter o <code>null</code>
	 */
    public static Map<String, Setter> getSetters(Class<?> clazz) {
        Map<String, Setter> mapField = new CaseInsensitiveLinkedHashMap<Setter>();
        Setter setter;
        if (clazz == null) return mapField;
        try {
            Method[] metodi = clazz.getMethods();
            for (Method metodo : metodi) {
                setter = Setter.getSetter(metodo);
                if (setter == null) continue;
                mapField.put(setter.propertyName, setter);
            }
        } catch (Exception e) {
        }
        return mapField;
    }

    /**
	 * <p>
	 * Recupera una mappa contenente il nome della proprietà ed il relativo
	 * metodo getter a partire da un bean. Nel caso venga passata un bean
	 * <code>null</code> viene restituita mappa con 0 elementi. La proprietà
	 * <code>class</code> viene ignorata.
	 * <p>
	 * La mappa restituita ha le chiavi di tipo case insensitive.
	 * </p>
	 * 
	 * @param bean
	 *            oggetto da analizzare
	 * @return mappa degli attributi e dei relativi getter.
	 */
    public static Map<String, Getter> getGetters(Object bean) {
        if (bean == null) {
            Map<String, Getter> mapField = new CaseInsensitiveLinkedHashMap<Getter>();
            return mapField;
        }
        return getGetters(bean.getClass());
    }

    /**
	 * <p>
	 * Recupera una mappa contenente il nome della proprietà ed il relativo
	 * metodo getter a partire da una classe. Nel caso venga passata una classe
	 * <code>null</code> viene restituito una mappa con 0 elementi.
	 * <p>
	 * La proprietà <code>class</code> viene ignorata.
	 * <p>
	 * La mappa restituita ha le chiavi di tipo case insensitive.
	 * </p>
	 * 
	 * @param clazz
	 *            classe da analizzare
	 * @return mappa di getter
	 */
    public static Map<String, Getter> getGetters(Class<?> clazz) {
        Map<String, Getter> mapField = new CaseInsensitiveLinkedHashMap<Getter>();
        if (clazz == null) return mapField;
        Method[] metodi = clazz.getMethods();
        Getter getter;
        for (Method metodo : metodi) {
            getter = Getter.getGetter(metodo);
            if (getter == null || getter.propertyName.equals("class")) continue;
            mapField.put(getter.propertyName, getter);
        }
        return mapField;
    }

    /**
	 * <p>
	 * Recupera una mappa contenente il nome della proprietà ed il relativo
	 * metodo getter a partire da una classe. Nel caso venga passata una classe
	 * <code>null</code> viene restituito una mappa con 0 elementi.
	 * <p>
	 * Il secondo parametro <code>properptiesToInclude</code> contiene il nome
	 * delle proprietà per le quali si desiderano i relativi getter. Nel caso in
	 * cui non ci siano proprietà con tali nomi nella classe desiderata, viene
	 * restituita una mappa con 0 elementi. L'insieme di proprietà è case
	 * insensitive.
	 * <p>
	 * La mappa restituita ha le chiavi di tipo case insensitive.
	 * </p>
	 * 
	 * @param clazz
	 *            classe da analizzare
	 * @param propertiesToInclude
	 *            insieme di proprietà da considerare
	 * @return mappa di setter
	 */
    public static Map<String, Getter> getGetters(Class<?> clazz, Set<String> propertiesToInclude) {
        Map<String, Getter> input = getGetters(clazz);
        Map<String, Getter> mapField = new CaseInsensitiveLinkedHashMap<Getter>();
        Getter getter;
        for (String item : propertiesToInclude) {
            getter = input.get(item);
            if (getter != null) {
                mapField.put(getter.propertyName, getter);
            }
        }
        return mapField;
    }

    /**
	 * <p>
	 * Recupera una mappa contenente il nome della proprietà ed il relativo
	 * metodo setter a partire da una classe. Nel caso venga passata una classe
	 * <code>null</code> viene restituito una mappa con 0 elementi.
	 * <p>
	 * Il secondo parametro <code>properptiesToInclude</code> contiene il nome
	 * delle proprietà per le quali si desiderano i relativi setter. Nel caso in
	 * cui non ci siano proprietà con tali nomi nella classe desiderata, viene
	 * restituita una mappa con 0 elementi. L'insieme di proprietà è case
	 * insensitive.
	 * <p>
	 * La mappa restituita ha le chiavi di tipo case insensitive.
	 * </p>
	 * 
	 * @param clazz
	 *            classe da analizzare
	 * @param propertiesToInclude
	 *            insieme di proprietà da considerare
	 * @return mappa di setter
	 */
    public static Map<String, Setter> getSetters(Class<?> clazz, Set<String> propertiesToInclude) {
        Map<String, Setter> input = getSetters(clazz);
        Map<String, Setter> mapField = new CaseInsensitiveLinkedHashMap<Setter>();
        Setter setter;
        for (String item : propertiesToInclude) {
            setter = input.get(item);
            if (setter != null) {
                mapField.put(setter.propertyName, setter);
            }
        }
        return mapField;
    }

    /**
	 * Questo metodo confronta due oggetti in base al contenuto dei loro
	 * attributi al fine di stabilire se contengono la stessa informazione. Due
	 * attributi con lo stesso nome sono uguali se sono entrambi
	 * <code>null</code> o se il loro valore è uguale (mediante il metodo
	 * <code>equals</code>.
	 * <p>
	 * Nel caso in cui uno dei due oggetti è nullo e l'altro no, questa funzione
	 * restituisce <code>false</code>.
	 * <p>
	 * Nel caso in cui tutti e due sono posti <code>null</null>, 
	 * la funzione restituisce <code>true</code>.
	 * <p>
	 * Se i oggetti sono di classe diversa, viene restituito <code>false</code>.
	 * 
	 * @param eb1
	 *            primo oggetto da confrontare.
	 * @param eb2
	 *            secondo oggetto da confrontare.
	 * @param properties
	 *            elenco degli attributi da confrontare. Devono essere messi
	 *            tutti in lowercase.
	 * @return <code>true</code> se i due entity bean sono uguali,
	 *         <code>false</code> altrimenti.
	 */
    public static boolean isEquals(Object eb1, Object eb2, Set<String> properties) {
        return isEquals(eb1, eb2, properties, null);
    }

    /**
	 * Questo metodo confronta due oggetti in base al contenuto delle loro
	 * proprietà al fine di stabilire se contengono la stessa informazione. Due
	 * attributi con lo stesso nome sono uguali se sono entrambi
	 * <code>null</code> o se il loro valore è uguale (mediante il metodo
	 * <code>equals</code>.
	 * <p>
	 * Nel caso in cui uno dei due oggetti è nullo e l'altro no, questa funzione
	 * restituisce <code>false</code>.
	 * <p>
	 * Nel caso in cui tutti e due sono posti <code>null</null>, 
	 * la funzione restituisce <code>true</code>.
	 * <p>
	 * Se i oggetti sono di classe diversa, viene restituito <code>false</code>.
	 * 
	 * @param eb1
	 *            primo oggetto da confrontare.
	 * @param eb2
	 *            secondo oggetto da confrontare.
	 * @return <code>true</code> se i due entity bean sono uguali,
	 *         <code>false</code> altrimenti.
	 */
    public static boolean isEquals(Object eb1, Object eb2) {
        return isEquals(eb1, eb2, null, null);
    }

    /**
	 * Questo metodo confronta due oggetti in base al contenuto dei loro
	 * attributi al fine di stabilire se contengono la stessa informazione. Due
	 * attributi con lo stesso nome sono uguali se sono entrambi
	 * <code>null</code> o se il loro valore è uguale (mediante il metodo
	 * <code>equals</code>.
	 * <p>
	 * E' possibile definire degli attributi da confrontare inserendo i loro
	 * nomi in <b>lowerCase</b> nel parametro <code>attributes</code>.
	 * <p>
	 * Nel caso in cui uno dei due oggetti è nullo e l'altro no, questa funzione
	 * restituisce <code>false</code>.
	 * <p>
	 * Nel caso in cui tutti e due sono posti <code>null</null>, 
	 * la funzione restituisce <code>true</code>.
	 * <p>
	 * Se i oggetti sono di classe diversa, viene restituito <code>false</code>.
	 * <p>
	 * Se si vuole ignorare forzatamente un attributo, e' possibile utilizzare
	 * l'annotazione {@link DontCompare}.
	 * </p>
	 * 
	 * @param eb1
	 *            primo oggetto da confrontare.
	 * @param eb2
	 *            secondo oggetto da confrontare.
	 * @param properties
	 *            elenco delle proprietà da confrontare. Devono essere messi
	 *            tutti in lowercase.
	 * @param ignoredAttributes
	 *            elenco degli attributi da ignorare. Devono essere messi tutti
	 *            in lowercase.
	 * @return <code>true</code> se i due entity bean sono uguali,
	 *         <code>false</code> altrimenti.
	 */
    public static boolean isEquals(Object eb1, Object eb2, Set<String> properties, Set<String> ignoredAttributes) {
        boolean bCompareAllAttributes = false;
        if (properties == null && ignoredAttributes == null) {
            bCompareAllAttributes = true;
        }
        if (properties == null) properties = new TreeSet<String>();
        if (ignoredAttributes == null) ignoredAttributes = new TreeSet<String>();
        if (!bCompareAllAttributes) {
            {
                Set<String> temp = new HashSet<String>();
                for (String item : properties) {
                    item = item.toLowerCase();
                    temp.add(item);
                }
                properties = temp;
            }
            {
                Set<String> temp = new HashSet<String>();
                for (String item : ignoredAttributes) {
                    item = item.toLowerCase();
                    temp.add(item);
                }
                ignoredAttributes = temp;
            }
        }
        if (eb1 == null && eb2 == null) return true;
        if ((eb1 == null) || (eb2 == null)) return false;
        Class<?> class1 = eb1.getClass();
        Class<?> class2 = eb2.getClass();
        if (!class1.equals(class2)) return false;
        try {
            Map<String, Getter> attributeMap = BeanUtil.getGetters(class1);
            Getter getter;
            Object val1;
            Object val2;
            String itemLowerCase;
            for (String item : attributeMap.keySet()) {
                getter = attributeMap.get(item);
                if (getter == null) continue;
                if (!bCompareAllAttributes) {
                    itemLowerCase = item.toLowerCase();
                    if (ignoredAttributes.contains(itemLowerCase)) continue;
                    if (!properties.contains(itemLowerCase)) {
                        continue;
                    }
                }
                if (getter.isIgnoredOnEquals()) {
                    continue;
                }
                val1 = getter.invoke(eb1);
                val2 = getter.invoke(eb2);
                if ((val1 != null && val2 == null) || (val1 == null && val2 != null)) {
                    return false;
                }
                if (val1 == null) {
                    continue;
                }
                if (!val1.equals(val2)) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
	 * Dati due bean, prova a vedere se hanno le proprietà con gli stessi
	 * valori. I due bean possono essere anche di tipi diversi.
	 * <ul>
	 * <li>Se uno dei due bean è <code>null</code> e l'altro no, viene
	 * restituito <code>false</code>.</li>
	 * <li>Se entrambi i bean sono <code>null</code> viene restituito
	 * <code>true</code>.</li>
	 * <li>Se entrambi i bean sono <code>null</code> viene restituito
	 * <code>true</code>.</li>
	 * </ul>
	 * <p>
	 * 
	 * @param bean1
	 * @param bean2
	 * @return
	 * 
	 */
    public static boolean isPropertiesEquals(Object bean1, Object bean2) {
        return isPropertiesEquals(bean1, bean2, null, null);
    }

    /**
	 * Dati due bean, prova a vedere se hanno le proprietà con gli stessi
	 * valori. I due bean possono essere anche di tipi diversi.
	 * <ul>
	 * <li>Se uno dei due bean è <code>null</code> e l'altro no, viene
	 * restituito <code>false</code>.</li>
	 * <li>Se entrambi i bean sono <code>null</code> viene restituito
	 * <code>true</code>.</li>
	 * <li>Se entrambi i bean sono <code>null</code> viene restituito
	 * <code>true</code>.</li>
	 * </ul>
	 * <p>
	 * 
	 * @param bean1
	 * @param bean2
	 * @param propertyToInclude
	 *            proprietà da includere
	 * @return
	 * 
	 */
    public static boolean isPropertiesEquals(Object bean1, Object bean2, Set<String> propertyToInclude) {
        return isPropertiesEquals(bean1, bean2, propertyToInclude, null);
    }

    /**
	 * @param bean1
	 * @param bean2
	 * @param properties
	 * @param ignoredAttributes
	 * @return
	 */
    public static boolean isPropertiesEquals(Object bean1, Object bean2, Set<String> propertyToCompare, Set<String> propertyToIgnore) {
        Set<String> properties = new TreeSet<String>();
        Set<String> ignoredProperties = new TreeSet<String>();
        boolean bCompareAllAttributes = false;
        if (propertyToCompare == null && propertyToIgnore == null) {
            bCompareAllAttributes = true;
        }
        if (!bCompareAllAttributes) {
            properties = new TreeSet<String>();
            if (propertyToCompare != null) {
                for (String item : propertyToCompare) {
                    properties.add(item.toLowerCase());
                }
            }
            ignoredProperties = new TreeSet<String>();
            if (propertyToIgnore != null) {
                for (String item : propertyToIgnore) {
                    ignoredProperties.add(item.toLowerCase());
                }
            }
        }
        if (bean1 == null && bean2 == null) return true;
        if ((bean1 == null) || (bean2 == null)) return false;
        Class<?> class1 = bean1.getClass();
        Class<?> class2 = bean2.getClass();
        try {
            Map<String, Getter> getterMap1 = BeanUtil.getGetters(class1);
            Map<String, Getter> getterMap2 = BeanUtil.getGetters(class2);
            Set<String> setProperties = joinProperties(getterMap1, getterMap2);
            Getter getter1, getter2;
            Object val1, val2;
            for (String item : setProperties) {
                if (!bCompareAllAttributes) {
                    if (ignoredProperties.contains(item)) continue;
                    if (!properties.contains(item)) {
                        continue;
                    }
                }
                getter1 = getterMap1.get(item);
                getter2 = getterMap2.get(item);
                if (getter1 != null && getter1.isIgnoredOnEquals()) getter1 = null;
                if (getter2 != null && getter2.isIgnoredOnEquals()) getter2 = null;
                if (getter1 == null && getter2 == null) continue;
                if ((getter1 == null && getter2 != null) || (getter1 != null && getter2 == null)) {
                    return false;
                }
                val1 = getter1.invoke(bean1);
                val2 = getter2.invoke(bean2);
                if ((val1 != null && val2 == null) || (val1 == null && val2 != null)) {
                    return false;
                }
                if (val1 == null) {
                    continue;
                }
                if (!val1.equals(val2)) {
                    return false;
                }
            }
            for (String item : properties) {
                if (!getterMap1.containsKey(item)) return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
	 * Dato un oggetto, immette nello <code>StringBuffer</code> i relativi
	 * attributi e i valori che essi hanno, incluse le collection
	 * 
	 * @param obj
	 *            istanza di oggetto da ispezionare
	 * @return string con la definizione degli attributi
	 */
    public static String inspectBean(Object obj) {
        try {
            return inspectBean(obj, obj.getClass(), new StringBuilder(), new Stack<Integer>()).toString();
        } catch (Exception e) {
            return "[Error during parsing of object]";
        }
    }

    /**
	 * Dato un oggetto, immette nello <code>StringBuffer</code> i relativi
	 * attributi e i valori che essi hanno
	 * 
	 * @param obj
	 *            istanza di oggetto da ispezionare
	 * @param buffer
	 *            string buffer da ispezionare
	 * @return string buffer con la definizione degli attributi
	 */
    public static StringBuilder inspectBean(Object obj, StringBuilder buffer) {
        try {
            return inspectBean(obj, obj.getClass(), buffer, new Stack<Integer>());
        } catch (Exception e) {
            return buffer.append("[Error during parsing of object]");
        }
    }

    /**
	 * Restituisce l'elenco degli attributi di un oggetto ed i relativi valori
	 * 
	 * @param obj
	 *            istanza di oggetto da ispezionare
	 * @param clazz
	 *            verranno visualizzati tutti gli attributi dell'oggetto che
	 *            fanno parte della definizione di questa classe
	 * @param buffer
	 *            string buffer nel quale inserire gli attributi
	 * @return string buffer con la definizione degli attributi
	 * @throws Exception
	 */
    private static final StringBuilder inspectBean(Object obj, Class<?> clazz, StringBuilder buffer, Stack<Integer> stack) throws Exception {
        Getter getter;
        Object objApp = "{Undefined}";
        boolean bAlreadyInStack;
        stack.push(obj.hashCode());
        if (clazz != null && buffer != null) {
            Map<String, Getter> mapAttribute = BeanUtil.getGetters(clazz);
            for (String item : mapAttribute.keySet()) {
                buffer.append(item);
                buffer.append(" = ");
                try {
                    getter = mapAttribute.get(item);
                    objApp = getter.invoke(obj);
                } catch (Exception e) {
                    objApp = "{Property not readable}";
                    buffer.append(objApp);
                    buffer.append(", ");
                    continue;
                }
                if (objApp == null) {
                    objApp = "{Null value}";
                    buffer.append(objApp);
                    buffer.append(", ");
                    continue;
                }
                try {
                    bAlreadyInStack = (stack.search(objApp.hashCode())) > -1;
                } catch (Exception e) {
                    objApp = "{Undefined value (hashCode is invalid)}";
                    buffer.append(", ");
                    continue;
                }
                if (IgnoredClasses.list != null) {
                    boolean bFound = false;
                    for (String className : IgnoredClasses.list) {
                        if (objApp.getClass().getName().equalsIgnoreCase(className)) {
                            buffer.append("{The type " + objApp.getClass() + " is ignored}");
                            buffer.append(", ");
                            bFound = true;
                            break;
                        }
                    }
                    if (bFound) {
                        continue;
                    }
                }
                if (objApp instanceof OutputStream || objApp instanceof Logger || objApp instanceof InputStream || objApp instanceof Reader || objApp instanceof Writer || objApp instanceof HttpServletResponse || objApp instanceof Filter || objApp instanceof HttpServlet) {
                    buffer.append("{The type " + objApp.getClass() + " is ignored}");
                    buffer.append(", ");
                    continue;
                }
                if (objApp instanceof Collection) {
                    Collection<?> app = (Collection<?>) objApp;
                    if (app.size() > 10) {
                        buffer.append("{" + "Size of collection is " + app.size() + "}");
                        buffer.append(", ");
                        continue;
                    }
                }
                if (objApp instanceof String || objApp instanceof Integer || objApp instanceof Long || objApp instanceof Double || objApp instanceof Byte || objApp instanceof Character || objApp instanceof BigDecimal || objApp instanceof Boolean || objApp instanceof Float || objApp instanceof Class || objApp instanceof Date) {
                    buffer.append(objApp.toString());
                } else if (bAlreadyInStack) {
                    buffer.append("{It is already parsed}");
                } else {
                    buffer.append("{" + inspectBean(objApp, objApp.getClass(), new StringBuilder(), stack) + "}");
                }
                buffer.append(", ");
            }
        }
        if (buffer.length() > 2) {
            buffer.setLength(buffer.length() - 2);
        }
        stack.pop();
        return buffer;
    }
}
