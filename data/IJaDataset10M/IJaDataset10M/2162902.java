package uk.co.pointofcare.echobase.neoutils.importing.annotated;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.BatchInserterIndex;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.impl.batchinsert.BatchInserter;
import uk.co.pointofcare.echobase.datatypes.Tuple;

/**
 * @author rchallen
 *
 */
public class AnnotatedNodeBuilder {

    static Logger log = Logger.getLogger(AnnotatedNodeBuilder.class);

    private static Map<String, Tuple<Object, Boolean>> propertyMap(Object object) {
        HashMap<String, Tuple<Object, Boolean>> out = new HashMap<String, Tuple<Object, Boolean>>();
        for (Field f : object.getClass().getFields()) {
            try {
                if (f.isAnnotationPresent(NodeValue.class)) {
                    String attribute = f.getAnnotation(NodeValue.class).attributeName();
                    if (attribute.length() == 0) attribute = f.getName();
                    Object value;
                    value = f.get(object);
                    if (Collection.class.isAssignableFrom(value.getClass())) {
                        value = ((Collection<?>) value).toArray();
                    }
                    if (f.getAnnotation(NodeValue.class).indexed()) {
                        out.put(attribute, Tuple.create(value, true));
                    } else {
                        out.put(attribute, Tuple.create(value, false));
                    }
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            }
        }
        for (Method m : object.getClass().getMethods()) {
            try {
                if (m.isAnnotationPresent(NodeValue.class)) {
                    String attribute = m.getAnnotation(NodeValue.class).attributeName();
                    if (attribute.length() == 0) attribute = m.getName();
                    Object value;
                    value = m.invoke(object);
                    if (Collection.class.isAssignableFrom(value.getClass())) {
                        value = ((Collection<?>) value).toArray();
                    }
                    if (m.getAnnotation(NodeValue.class).indexed()) {
                        out.put(attribute, Tuple.create(value, true));
                    } else {
                        out.put(attribute, Tuple.create(value, false));
                    }
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Only methods with no parameters can be node values");
            }
        }
        return out;
    }

    public static Node create(Node n, Index<Node> index, Object object) {
        log.debug("Creating node for: " + object.toString());
        for (Entry<String, Tuple<Object, Boolean>> entry : propertyMap(object).entrySet()) {
            n.setProperty(entry.getKey(), entry.getValue().getFirst());
            if (entry.getValue().getSecond()) index.add(n, entry.getKey(), entry.getValue().getFirst());
        }
        return n;
    }

    /**
	 * @param neoDb
	 * @param index
	 * @param atom
	 * @return
	 */
    public static Long batchCreate(BatchInserter neoDb, BatchInserterIndex index, Object object) {
        log.debug("Creating node for: " + object.toString());
        Long node = neoDb.createNode(unindexedPropertyMap(object));
        index.add(node, indexedPropertyMap(object));
        return node;
    }

    public static HashMap<String, Object> unindexedPropertyMap(Object object) {
        HashMap<String, Object> nodeMap = new HashMap<String, Object>();
        for (Entry<String, Tuple<Object, Boolean>> entry : propertyMap(object).entrySet()) {
            nodeMap.put(entry.getKey(), entry.getValue().getFirst());
        }
        return nodeMap;
    }

    public static HashMap<String, Object> indexedPropertyMap(Object object) {
        HashMap<String, Object> nodeMap = new HashMap<String, Object>();
        for (Entry<String, Tuple<Object, Boolean>> entry : propertyMap(object).entrySet()) {
            if (entry.getValue().getSecond()) nodeMap.put(entry.getKey(), entry.getValue().getFirst());
        }
        return nodeMap;
    }
}
