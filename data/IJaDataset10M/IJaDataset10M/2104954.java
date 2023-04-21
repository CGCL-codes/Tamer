package org.proteusframework.platformservice.persistence.messagebean;

import org.proteusframework.core.api.model.INamespace;
import org.proteusframework.core.base.Namespace;
import org.proteusframework.core.util.Assert;
import org.proteusframework.platformservice.persistence.api.messagebean.IMessageBeanDescriptor;
import org.proteusframework.platformservice.persistence.api.messagebean.IMessageBeanIndex;
import org.proteusframework.platformservice.persistence.api.messagebean.IMessageBeanProperty;
import java.util.*;

/**
 * Package private {@link IMessageBeanDescriptor} concrete implementation. Developer must use the
 * {@link MessageBeanBuilder} to create instances of {@link IMessageBeanDescriptor}.
 *
 * @author Tacoma Four
 */
final class MessageBeanDescriptor extends Namespace implements IMessageBeanDescriptor {

    private String description;

    private Properties metadata = new Properties();

    private Map<String, MessageBeanProperty> propertyMap = new HashMap<String, MessageBeanProperty>();

    private List<IMessageBeanProperty> properties = new ArrayList<IMessageBeanProperty>();

    private List<Class<?>> interfaces = new ArrayList<Class<?>>();

    private List<IMessageBeanIndex> indices = new ArrayList<IMessageBeanIndex>();

    private boolean hasIdentityColumn = false;

    private List<String> identityColumnNames = new ArrayList<String>();

    MessageBeanDescriptor(INamespace namespace) {
        super(namespace);
    }

    @Override
    public String getName() {
        return getRefId();
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Properties getMetadata() {
        return metadata;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCanonicalName() {
        return this.getFamily() + "." + this.getId();
    }

    @Override
    public IMessageBeanProperty[] lookupSpecificProperties(String... propertyNames) {
        List<IMessageBeanProperty> matchingProperties = new ArrayList<IMessageBeanProperty>();
        for (String propertyName : propertyNames) {
            if (propertyMap.containsKey(propertyName)) {
                matchingProperties.add(propertyMap.get(propertyName));
            }
        }
        IMessageBeanProperty[] properties = new IMessageBeanProperty[matchingProperties.size()];
        return matchingProperties.toArray(properties);
    }

    @Override
    public List<IMessageBeanProperty> properties() {
        return Collections.unmodifiableList(properties);
    }

    public void addProperty(MessageBeanProperty property) {
        Assert.parameterNotNull(property, "Parameter 'property' must not be null");
        this.properties.add(property);
        propertyMap.put(property.getName(), property);
        if (property.getDataType() == DataType.IdentityType) {
            hasIdentityColumn = true;
            identityColumnNames.add(property.getName());
        }
    }

    @Override
    public List<IMessageBeanIndex> indices() {
        return indices;
    }

    public void addIndex(IMessageBeanIndex index) {
        Assert.parameterNotNull(index, "Parameter 'index' must not be null");
        this.indices.add(index);
    }

    @Override
    public List<Class<?>> interfaces() {
        return interfaces;
    }

    public void addInterface(Class<?> interfaceName) {
        Assert.parameterNotNull(interfaceName, "Parameter 'interfaceName' must not be null");
        this.interfaces.add(interfaceName);
    }

    public MessageBeanProperty getProperty(String property) {
        return propertyMap.get(property);
    }

    @Override
    public boolean hasIdentityColumn() {
        return hasIdentityColumn;
    }

    @Override
    public String[] getIdentityColumn() {
        String[] names = new String[identityColumnNames.size()];
        names = identityColumnNames.toArray(names);
        return names;
    }

    @Override
    public String toString() {
        return "MessageBeanDescriptor{" + super.toString() + ", description='" + description + '\'' + ", metadata=" + metadata + ", properties=" + properties + ", interfaces=" + interfaces + ", indices=" + indices + "}";
    }
}
