package gwt.mosaic.client.beans;

import gwt.mosaic.client.events.KeyValueUpdateEvent;
import gwt.mosaic.client.events.KeyValueUpdateHandler;
import gwt.mosaic.client.events.PropertyChangeEvent;
import gwt.mosaic.client.events.PropertyChangeHandler;
import gwt.mosaic.client.util.JSON;
import gwt.mosaic.client.util.observablecollections.ObservableMap;
import java.util.List;
import java.util.Map;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Represents a binding relationship between a source and a target property
 * within a namespace.
 */
public class NamespaceBinding {

    /**
	 * Namespace bind mapping interface.
	 */
    public interface BindMapping {

        /**
		 * Transforms a source value during a bind operation.
		 * 
		 * @param value
		 * @return
		 */
        public Object evaluate(Object value);
    }

    private Map<String, Object> namespace;

    private String sourcePath;

    private Object source;

    private Map<String, Object> sourceMap;

    private BeanMonitor sourceMonitor;

    private String sourceKey;

    private String targetPath;

    private Object target;

    private Map<String, Object> targetMap;

    private String targetKey;

    private BindMapping bindMapping;

    private boolean updating = false;

    private final KeyValueUpdateHandler<String, Object> sourceMapHandler = new KeyValueUpdateHandler<String, Object>() {

        @Override
        public void onKeyValueUpdate(KeyValueUpdateEvent<String, Object> event) {
            if (event.getKey().equals(sourceKey) && !updating) {
                updating = true;
                targetMap.put(targetKey, getTransformedSourceValue());
                updating = false;
            }
        }
    };

    private final PropertyChangeHandler sourcePropertyChangeHandler = new PropertyChangeHandler() {

        @Override
        public void onPropertyChanged(PropertyChangeEvent event) {
            if (event.getPropertyName().equals(sourceKey) && !updating) {
                updating = true;
                targetMap.put(targetKey, getTransformedSourceValue());
                updating = false;
            }
        }
    };

    public NamespaceBinding(Map<String, Object> namespace, String sourcePath, String targetPath) {
        this(namespace, sourcePath, targetPath, null);
    }

    @SuppressWarnings("unchecked")
    public NamespaceBinding(Map<String, Object> namespace, String sourcePath, String targetPath, BindMapping bindMapping) {
        if (namespace == null) {
            throw new IllegalArgumentException();
        }
        if (sourcePath == null) {
            throw new IllegalArgumentException();
        }
        if (targetPath == null) {
            throw new IllegalArgumentException();
        }
        this.namespace = namespace;
        this.sourcePath = sourcePath;
        List<String> sourceKeys = JSON.parse(sourcePath);
        sourceKey = JSON.remove(sourceKeys, sourceKeys.size() - 1, 1).get(0);
        source = JSON.get(namespace, sourceKeys);
        if (source instanceof Map<?, ?>) {
            sourceMap = (Map<String, Object>) source;
            sourceMonitor = null;
        } else {
            sourceMap = BeanAdapterFactory.createFor(source);
            sourceMonitor = new BeanMonitor(source);
        }
        if (!sourceMap.containsKey(sourceKey)) {
            throw new IllegalArgumentException("Source property \"" + sourcePath + "\" does not exist.");
        }
        if (sourceMonitor != null && !sourceMonitor.isNotifying(sourceKey)) {
            throw new IllegalArgumentException("\"" + sourceKey + "\" is not a notifying property.");
        }
        this.targetPath = targetPath;
        List<String> targetKeys = JSON.parse(targetPath);
        targetKey = JSON.remove(targetKeys, targetKeys.size() - 1, 1).get(0);
        target = JSON.get(namespace, targetKeys);
        if (target instanceof Map<?, ?>) {
            targetMap = (Map<String, Object>) target;
        } else {
            targetMap = BeanAdapterFactory.createFor(target);
        }
        if (!targetMap.containsKey(targetKey)) {
            throw new IllegalArgumentException("Target property \"" + targetPath + "\" does not exist.");
        }
        this.bindMapping = bindMapping;
        targetMap.put(targetKey, getTransformedSourceValue());
    }

    /**
	 * Returns the namespace.
	 */
    public Map<String, Object> getNamespace() {
        return namespace;
    }

    /**
	 * Returns the path to the source property.
	 */
    public String getSourcePath() {
        return sourcePath;
    }

    /**
	 * Returns the source object.
	 */
    public Object getSource() {
        return source;
    }

    /**
	 * Returns the name of the source property.
	 */
    public String getSourceKey() {
        return sourceKey;
    }

    /**
	 * Returns the path to the target property.
	 */
    public String getTargetPath() {
        return targetPath;
    }

    /**
	 * Returns the target object.
	 */
    public Object getTarget() {
        return target;
    }

    /**
	 * Returns the name of the target property.
	 */
    public String getTargetKey() {
        return targetKey;
    }

    /**
	 * Returns the bind mapping.
	 * 
	 * @return The bind mapping to use during binding, or <tt>null</tt> if no
	 *         bind mapping is specified.
	 */
    public BindMapping getBindMapping() {
        return bindMapping;
    }

    /**
	 * Returns the current source value with any bind mapping applied.
	 */
    public Object getTransformedSourceValue() {
        Object sourceValue = sourceMap.get(sourceKey);
        return (bindMapping == null) ? sourceValue : bindMapping.evaluate(sourceValue);
    }

    private HandlerRegistration bindHR = null;

    /**
	 * Binds the source property to the target property.
	 */
    public void bind() {
        if (source instanceof Map<?, ?>) {
            if (source instanceof ObservableMap<?, ?>) {
                bindHR = ((ObservableMap<String, Object>) sourceMap).addKeyValueUpdateHandler(sourceMapHandler);
            }
        } else {
            bindHR = sourceMonitor.addPropertyChangeHandler(sourcePropertyChangeHandler);
        }
    }

    /**
	 * Unbinds the source property from the target property.
	 */
    public void unbind() {
        if (bindHR != null) {
            bindHR.removeHandler();
            bindHR = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        boolean equals = false;
        if (o instanceof NamespaceBinding) {
            NamespaceBinding namespaceBinding = (NamespaceBinding) o;
            equals = (source == namespaceBinding.source && sourceKey.equals(namespaceBinding.sourceKey) && target == namespaceBinding.target && targetKey.equals(namespaceBinding.targetKey));
        }
        return equals;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + source.hashCode();
        result = prime * result + sourceKey.hashCode();
        result = prime * result + target.hashCode();
        result = prime * result + targetKey.hashCode();
        return result;
    }
}
