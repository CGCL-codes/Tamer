package org.objectwiz.plugin.uibuilder.model;

import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import org.objectwiz.core.facet.customization.EntityBase;
import org.objectwiz.plugin.uibuilder.model.value.Value;
import org.objectwiz.plugin.uibuilder.model.value.ValueFromDataset;

/**
 * Additional configuration for edition forms.
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
@Entity
public class FormConfiguration extends EntityBase {

    private Map<String, Value> staticValues;

    private Map<String, ValueFromDataset> entitySelectionDatasets;

    private String hideProperties;

    private String showProperties;

    /**
     * Static values are values for attributes of the form that are resolved
     * dynamically and cannot be modified by the user.
     */
    @ManyToMany
    public Map<String, Value> getStaticValues() {
        return staticValues;
    }

    public void setStaticValues(Map<String, Value> staticValues) {
        this.staticValues = staticValues;
    }

    /**
     * An entry in this map must be defined for each association property that is displayed
     * in the form. Such entry indicates how to resolve the values when the user wants to
     * select an entity for this property.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    public Map<String, ValueFromDataset> getEntitySelectionDatasets() {
        return entitySelectionDatasets;
    }

    public void setEntitySelectionDatasets(Map<String, ValueFromDataset> entitySelectionDatasets) {
        this.entitySelectionDatasets = entitySelectionDatasets;
    }

    /**
     * Comma-separated list of names of properties that define the properties
     * to hide in associated forms.
     *
     * Properties mentioned in this list must be optional or associated to
     * static values.
     */
    public String getHideProperties() {
        return hideProperties;
    }

    public void setHideProperties(String hideProperties) {
        this.hideProperties = hideProperties;
    }

    /**
     * Comma-separated list of names of properties. If this attributes is defined,
     * then only the properties in this list will be shown.
     * This attribute takes precedence over {@link getHideProperties()}.
     */
    public String getShowProperties() {
        return showProperties;
    }

    public void setShowProperties(String showProperties) {
        this.showProperties = showProperties;
    }
}
