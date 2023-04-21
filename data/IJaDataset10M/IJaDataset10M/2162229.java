package fastforward.meta;

import java.util.*;
import java.util.logging.*;
import fastforward.meta.util.*;
import fastforward.meta.util.PropertyExpression.*;
import fastforward.util.*;

public class DefaultPropertyMetadataRepository implements PropertyMetadataRepository {

    private Logger logger = Logger.getLogger(PropertyMetadataRepository.class.getName());

    private Map<PropertyContextPath, PropertyMetadataSet> contextSet = new HashMap<PropertyContextPath, PropertyMetadataSet>();

    public DefaultPropertyMetadataRepository() {
    }

    public DefaultPropertyMetadataRepository(PropertyMetadataRepository anotherRepository) {
        addAll(anotherRepository, true);
    }

    public Set<PropertyContextPath> getPropertyContextPaths() {
        return Collections.unmodifiableSet(contextSet.keySet());
    }

    public void addAll(PropertyMetadataRepository anotherRepository, boolean overwriteExisting) {
        Set<PropertyContextPath> anotherContextSets = anotherRepository.getPropertyContextPaths();
        for (PropertyContextPath path : anotherContextSets) {
            if (contextSet.containsKey(path) && !overwriteExisting) continue;
            contextSet.put(path, anotherRepository.getMetadata(path));
        }
    }

    public PropertyMetadataEntries getMetadata(Class<?> c) {
        PropertyMetadataEntries out = new PropertyMetadataEntries();
        for (Iterator<PropertyContextPath> iterator = contextSet.keySet().iterator(); iterator.hasNext(); ) {
            PropertyContextPath contextPath = iterator.next();
            if (contextPath.getBaseClass() == c) {
                if (contextPath.getPropertyExpression().size() == 1) {
                    out.put(contextPath.getPropertyExpression().toString(), contextSet.get(contextPath));
                }
            }
        }
        return out;
    }

    public PropertyMetadataEntries getInnerPropertiesMetadata(Class<?> c) {
        return getInnerPropertiesMetadata(new PropertyContextPath(c, ""));
    }

    /**
     * Return the properties define for a property. To get the properties for a type, use getMetadata(Class).
     */
    public PropertyMetadataEntries getInnerPropertiesMetadata(PropertyContextPath contextPath) {
        return getInnerPropertiesMetadata(contextPath, true);
    }

    private PropertyMetadataEntries getInnerPropertiesMetadata(PropertyContextPath contextPath, boolean generalizeCollectionsUsingCollectionType) {
        Monitor m = new Monitor();
        if (contextPath == null) return null;
        if (contextPath.getPropertyExpression().size() == 0) return getMetadata(contextPath.getBaseClass());
        PropertyMetadataEntries out = new PropertyMetadataEntries();
        String contextPathExpression = contextPath.toString();
        for (Iterator<PropertyContextPath> iterator = contextSet.keySet().iterator(); iterator.hasNext(); ) {
            PropertyContextPath cp = iterator.next();
            String expression = cp.toString();
            if (contextPath.getBaseClass() == cp.getBaseClass()) {
                if (expression.startsWith(contextPathExpression) && cp.getPropertyExpression().size() == contextPath.getPropertyExpression().size() + 1) {
                    PropertyExpressionItem lastExpressionPart = cp.getPropertyExpression().getLast();
                    out.put(lastExpressionPart.toString(), contextSet.get(cp));
                }
            }
        }
        if (contextPath.getPropertyExpression().size() >= 1) {
            List<PropertyContextPath> generalization = generalize(contextPath, generalizeCollectionsUsingCollectionType);
            for (Iterator<PropertyContextPath> iterator = generalization.iterator(); iterator.hasNext(); ) {
                PropertyContextPath propertyContextPath = iterator.next();
                PropertyMetadataEntries generalized = getInnerPropertiesMetadata(propertyContextPath);
                if (generalized != null && generalized.size() > 0) {
                    merge(out, generalized);
                }
            }
        }
        if (out.size() == 0) return null;
        logger.fine("Time to resolve " + contextPath.toString() + " -> " + m.elapsed());
        return out;
    }

    /**
     * Merge generic into specific, not overriding existing property in specific.
     * @param specific
     * @param generic
     */
    private void merge(PropertyMetadataEntries specific, PropertyMetadataEntries generic) {
        List<String> properties = generic.getPropertyNames();
        for (Iterator iterator = properties.iterator(); iterator.hasNext(); ) {
            String propertyName = (String) iterator.next();
            if (specific.get(propertyName) == null) specific.put(propertyName, generic.get(propertyName));
        }
    }

    protected List<PropertyContextPath> generalize(PropertyContextPath contextPath, boolean generalizeCollectionsUsingCollectionType) {
        return contextPath.generalize(this, generalizeCollectionsUsingCollectionType);
    }

    public PropertyMetadataSet getMetadata(PropertyContextPath contextPath) {
        if (contextPath.getPropertyExpression().size() == 0) return null;
        PropertyExpression searchExpression;
        if (contextPath.getPropertyExpression().getLast().isBracketIndex()) searchExpression = contextPath.getPropertyExpression().removeLast().removeLast(); else searchExpression = contextPath.getPropertyExpression().removeLast();
        PropertyMetadataEntries entries = getInnerPropertiesMetadata(new PropertyContextPath(contextPath.getBaseClass(), searchExpression), true);
        if (entries != null) {
            PropertyExpression targetPropertyExpression = contextPath.getPropertyExpression();
            if (contextPath.getPropertyExpression().getLast().isBracketIndex()) targetPropertyExpression = targetPropertyExpression.removeLast();
            return entries.get(targetPropertyExpression.getLast().toString());
        }
        return null;
    }

    public void set(PropertyContextPath contextPath, PropertyMetadataSet metadata) {
        contextSet.put(contextPath, metadata);
    }

    public void remove(PropertyContextPath contextPath) {
        contextSet.remove(contextPath);
    }
}
