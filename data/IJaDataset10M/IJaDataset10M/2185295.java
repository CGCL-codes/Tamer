package it.dangelo.javabinding;

import it.dangelo.javabinding.beans.IntrospectorWrapper;
import it.dangelo.javabinding.mappings.BindMapping;
import it.dangelo.javabinding.mappings.BindResolver;
import it.dangelo.javabinding.mappings.Entity;
import it.dangelo.javabinding.mappings.EntityResolver;
import it.dangelo.javabinding.mappings.EntityWrapper;
import java.util.HashMap;

/**
 * Default implementation of {@link Binding}.
 * This implementation couldn't be used 
 *
 */
public class BindingImpl implements Binding, EntityResolver {

    private HashMap<String, EntityWrapper> entities = new HashMap<String, EntityWrapper>();

    private BindResolver resolver;

    private BindMapping bindMapping;

    public BindingImpl(Entity[] entities, IntrospectorWrapper introspectorWrapper, BindResolver resolver, BindMapping bindMapping) throws Exception {
        for (Entity entity : entities) {
            this.entities.put(entity.getId(), new EntityWrapper(entity, introspectorWrapper, this));
        }
        this.resolver = resolver;
        this.bindMapping = bindMapping;
    }

    public Object marshall(String id, Object element) throws BindingException {
        EntityWrapper wrapper = this.entities.get(id);
        if (wrapper == null) throw new BindingException("Mapping id " + id + " don't exists");
        try {
            return wrapper.marshall(element, this.bindMapping);
        } catch (Exception e) {
            throw new BindingException("Error to execute unmarshalling to id " + id, e);
        }
    }

    public Object unmarshall(String id, Object element) throws BindingException {
        EntityWrapper wrapper = this.entities.get(id);
        if (wrapper == null) throw new BindingException("Mapping id " + id + " don't exists");
        try {
            return wrapper.unmarshall(element, resolver);
        } catch (Exception e) {
            throw new BindingException("Error to execute unmarshalling to id " + id, e);
        }
    }

    public EntityWrapper get(String name) {
        return this.entities.get(name);
    }
}
