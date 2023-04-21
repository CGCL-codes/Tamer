package org.skycastle.util.propertyaccess.metadata;

import org.skycastle.util.propertyaccess.PropertyAccessor;
import org.skycastle.util.propertyaccess.PropertyAccessorImpl;
import java.util.*;

/**
 * @author Hans H�ggstr�m
 */
public final class PropertyMetadatasImpl implements PropertyMetadatas {

    private final Map<String, PropertyMetadata> myPropertyMetadatas;

    private final List<PropertyMetadata> myIndexedPropertyMetadatas;

    /**
     * @param propertyMetadatas a list of the property metadata entries to include in this set of metadatas.
     */
    public PropertyMetadatasImpl(final List<PropertyMetadata> propertyMetadatas) {
        myIndexedPropertyMetadatas = new ArrayList<PropertyMetadata>(propertyMetadatas);
        myPropertyMetadatas = new LinkedHashMap<String, PropertyMetadata>(propertyMetadatas.size());
        for (PropertyMetadata propertyMetadata : propertyMetadatas) {
            myPropertyMetadatas.put(propertyMetadata.getPropertyIdentifier(), propertyMetadata);
        }
    }

    /**
     * @param propertyMetadatas a sequence of the property metadata entries to include in this set of metadatas.
     */
    public PropertyMetadatasImpl(PropertyMetadata... propertyMetadatas) {
        this(Arrays.asList(propertyMetadatas));
    }

    public Collection<String> getPropertyNames() {
        return Collections.unmodifiableCollection(myPropertyMetadatas.keySet());
    }

    public PropertyMetadata getPropertyMetadata(String propertyName) {
        return myPropertyMetadatas.get(propertyName);
    }

    public PropertyMetadata getPropertyMetadata(int index) {
        if (index >= 0 && index < myIndexedPropertyMetadatas.size()) {
            return myIndexedPropertyMetadatas.get(index);
        } else {
            return null;
        }
    }

    public Collection<PropertyMetadata> getPropertyMetadatas() {
        return Collections.unmodifiableCollection(myPropertyMetadatas.values());
    }

    public List<PropertyValidationReport> validate(Map<String, ? extends Object> properties) {
        return validate(new PropertyAccessorImpl(properties));
    }

    @SuppressWarnings({ "RawUseOfParameterizedType", "unchecked" })
    public List<PropertyValidationReport> validate(PropertyAccessor properties) {
        List<PropertyValidationReport> report = null;
        final Collection<String> availableProperties = properties.getAvailableProperties();
        for (String propertyName : availableProperties) {
            final Object value = properties.get(propertyName);
            report = validateProperty(report, propertyName, value);
        }
        for (Map.Entry<String, PropertyMetadata> entry : myPropertyMetadatas.entrySet()) {
            final PropertyMetadata metadata = entry.getValue();
            final String propertyName = entry.getKey();
            if (!availableProperties.contains(propertyName) && metadata.isRequired()) {
                report = addValidationReport(report, new PropertyValidationReportImpl(propertyName, "Required parameter '" + metadata.getSignature() + "' is missing.", metadata.getDefaultValue()));
            }
        }
        if (report == null) {
            report = Collections.emptyList();
        }
        return report;
    }

    public List<PropertyValidationReport> validate(final String propertyName, final Object propertyValue) {
        List<PropertyValidationReport> report = validateProperty(null, propertyName, propertyValue);
        if (report == null) {
            report = Collections.emptyList();
        }
        return report;
    }

    private List<PropertyValidationReport> validateProperty(List<PropertyValidationReport> report, final String propertyName, final Object value) {
        final PropertyMetadata metadata = myPropertyMetadatas.get(propertyName);
        if (metadata == null) {
            report = addValidationReport(report, new PropertyValidationReportImpl(propertyName, "Unexpected parameter named '" + propertyName + "' with value '" + value + "'.", null));
        } else {
            final PropertyValidationReport validationReport = metadata.validate(value);
            if (validationReport != null) {
                report = addValidationReport(report, validationReport);
            }
        }
        return report;
    }

    private List<PropertyValidationReport> addValidationReport(List<PropertyValidationReport> report, final PropertyValidationReport validationReport) {
        if (report == null) {
            report = new ArrayList<PropertyValidationReport>(4);
        }
        report.add(validationReport);
        return report;
    }
}
