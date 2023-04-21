package com.mindquarry.persistence.jcr.trafo;

import java.lang.reflect.Type;
import java.util.Collection;
import com.mindquarry.persistence.jcr.JcrNode;
import com.mindquarry.persistence.jcr.JcrNodeIterator;
import com.mindquarry.persistence.jcr.JcrPersistenceInternalException;
import com.mindquarry.persistence.jcr.model.ModelException;

/**
 * @author 
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
class ParametrizedCollectionTransformer implements Transformer {

    private Type componentType_;

    private Class<?> collectionImplementation_;

    private Transformer componentTransformer_;

    ParametrizedCollectionTransformer(Type componentType, Class<?> collectionImplementation) {
        componentType_ = componentType;
        collectionImplementation_ = collectionImplementation;
    }

    public void initialize(TransformerRegistry registry) {
        componentTransformer_ = registry.findContentTransformer(componentType_);
    }

    public Object readFromJcr(JcrNode jcrNode) {
        Collection children = newComponentCollection();
        for (JcrNode itemNode : jcrNode.getNodes()) {
            Object child = componentTransformer_.readFromJcr(itemNode);
            children.add(child);
        }
        return children;
    }

    private Collection newComponentCollection() {
        try {
            return (Collection) collectionImplementation_.newInstance();
        } catch (InstantiationException e) {
            throw new ModelException("error during transforming jcr entries " + "into a collection. could not create an instance of " + "type: " + collectionImplementation_);
        } catch (IllegalAccessException e) {
            throw new JcrPersistenceInternalException(e);
        }
    }

    public void writeToJcr(Object object, JcrNode jcrNode) {
        Collection collection = (Collection) object;
        JcrNodeIterator collectionNodeIt = jcrNode.getNodes();
        for (Object item : collection) {
            JcrNode itemNode;
            if (collectionNodeIt.hasNext()) itemNode = collectionNodeIt.next(); else itemNode = jcrNode.addNode("item", "xt:element");
            componentTransformer_.writeToJcr(item, itemNode);
        }
        while (collectionNodeIt.hasNext()) {
            collectionNodeIt.next().remove();
        }
    }
}
