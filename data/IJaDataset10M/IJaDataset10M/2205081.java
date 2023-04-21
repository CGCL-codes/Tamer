package org.zeroexchange.web.components.resource.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.repeater.data.GridView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.zeroexchange.collaboration.contract.event.ResourceChanged;
import org.zeroexchange.event.EventsDispatcher;
import org.zeroexchange.i18n.read.LocalizationReader;
import org.zeroexchange.model.resource.Resource;
import org.zeroexchange.model.resource.TendersMultiplicity;
import org.zeroexchange.model.resource.category.Category;
import org.zeroexchange.model.resource.property.Property;
import org.zeroexchange.model.resource.property.PropertySpecification;
import org.zeroexchange.model.resource.value.SingleValueHolder;
import org.zeroexchange.model.resource.value.Value;
import org.zeroexchange.resource.category.read.CategoryReader;
import org.zeroexchange.resource.read.ResourceReader;
import org.zeroexchange.resource.value.create.ValueFactory;
import org.zeroexchange.resource.write.ResourceWriter;
import org.zeroexchange.web.components.ControlDescriptor;
import org.zeroexchange.web.components.ValidatableField;
import org.zeroexchange.web.components.form.InputPanel;
import org.zeroexchange.web.components.resource.CategorySelector;
import org.zeroexchange.web.components.resource.ResourcePropertiesControlsFactory;
import org.zeroexchange.web.page.resource.ResourceManagement;

/**
 * @author black
 *
 */
public class ResourceEditPanel extends ResourceDetailsPanel {

    private static final long serialVersionUID = 1L;

    private static final String MARKER_POSTFIX = "Marker";

    private static final String MKEY_DESCRIPTION = "resource.description";

    private static final String MKEY_MULTIPLICITY = "resource.multiplicity";

    private static final String MKEY_CATEGORY = "resource.class";

    private static final String MKEY_SUBMIT = "resource.save";

    private static final String MKEY_MULTIPLICITY_VALUE_PREFIX = "multiplicity.";

    private static final String CKEY_COMPONENTS_VIEW = "components";

    private static final String CKEY_RESOURCE_FORM = "resourceForm";

    private static final String CKEY_SUBMIT_BUTTON = "submitButton";

    @SpringBean
    private ResourcePropertiesControlsFactory resourcePropertiesControlsFactory;

    @SpringBean
    private LocalizationReader localizationReader;

    @SpringBean
    private CategoryReader categoryReader;

    @SpringBean
    private ValueFactory valuesFactory;

    @SpringBean
    private ResourceWriter resourceWriter;

    @SpringBean
    private ResourceReader resourceReader;

    @SpringBean
    private EventsDispatcher eventsDispatcher;

    private CategorySelector categorySelector;

    private GridView<ControlDescriptor> controlsTable;

    private List<ControlDescriptor> controlDescriptors;

    private ResourceFormData resourceFormData = new ResourceFormData();

    /**
     * Model holds the the resource properties values.
     * 
     * @author black
     */
    private class ResourcePropertyModel implements IModel<Object> {

        private static final long serialVersionUID = 1L;

        private Property property;

        private Map<Property, Value> valuesMap;

        /**
         * Constructor.
         */
        public ResourcePropertyModel(Property property, Map<Property, Value> valuesMap) {
            this.property = property;
            this.valuesMap = valuesMap;
        }

        @Override
        public void detach() {
        }

        @Override
        public Object getObject() {
            Value valueHolder = valuesMap.get(property);
            if (valueHolder instanceof SingleValueHolder) {
                return ((SingleValueHolder) valueHolder).getValue();
            }
            return null;
        }

        @Override
        public void setObject(Object value) {
            Value valueHolder = valuesMap.get(property);
            if (valueHolder == null) {
                valueHolder = valuesFactory.createValue(property.getPropertyType());
                valuesMap.put(property, valueHolder);
                Resource resource = getResourceModel().getObject();
                PropertySpecification propertySpecification = new PropertySpecification();
                propertySpecification.setProperty(property);
                propertySpecification.setValue(valueHolder);
                propertySpecification.setResource(resource);
                resource.getSpecification().add(propertySpecification);
            }
            if (valueHolder instanceof SingleValueHolder) {
                ((SingleValueHolder) valueHolder).setValue(value);
            }
        }
    }

    private class ResourceFormData implements Serializable {

        private static final long serialVersionUID = 1L;

        private String description;

        private Category category;

        private TendersMultiplicity multiplicity;

        /** Property id -> value*/
        private Map<Property, Value> resourceValues;

        /**
         * @return the titles
         */
        public String getDescription() {
            return description;
        }

        /**
         * @return the category
         */
        public Category getCategory() {
            return category;
        }

        /**
         * Returns map for the resource properties. 
         */
        public Map<Property, Value> getResourceValues() {
            if (resourceValues == null) {
                resourceValues = new HashMap<Property, Value>();
            }
            return resourceValues;
        }

        public TendersMultiplicity getMultiplicity() {
            return multiplicity;
        }
    }

    /**
     * Constructor.
     */
    public ResourceEditPanel(String id, IModel<Resource> model) {
        super(id, model);
        updateFormData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initUI() {
        super.initUI();
        add(getResourceForm());
    }

    /**
     * {@inheritDoc}
     */
    private void updateFormData() {
        Resource editingResource = (Resource) getDefaultModelObject();
        if (editingResource != null) {
            if (resourceFormData == null) {
                resourceFormData = new ResourceFormData();
            }
            resourceFormData.category = editingResource.getCategory();
            resourceFormData.description = editingResource.getDescription();
            resourceFormData.multiplicity = editingResource.getTendersMultiplicity();
            Set<PropertySpecification> resourceSpecification = editingResource.getSpecification();
            for (PropertySpecification specItem : resourceSpecification) {
                Property property = specItem.getProperty();
                Value value = specItem.getValue();
                resourceFormData.getResourceValues().put(property, value);
            }
        }
    }

    /**
     * Returns contract's form. 
     */
    protected Form<Void> getResourceForm() {
        Form<Void> form = new Form<Void>(CKEY_RESOURCE_FORM) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                saveSubmittedResourceData(resourceFormData);
            }
        };
        controlsTable = getControlsView(CKEY_COMPONENTS_VIEW);
        form.add(controlsTable);
        form.setOutputMarkupId(true);
        form.add(new Button(CKEY_SUBMIT_BUTTON, new ResourceModel(MKEY_SUBMIT)));
        return form;
    }

    /**
     * Creates list of the control descriptors.
     */
    @Override
    protected List<ControlDescriptor> getControlDescriptors() {
        if (controlDescriptors != null) {
            return controlDescriptors;
        }
        controlDescriptors = new ArrayList<ControlDescriptor>();
        controlDescriptors.add(new ControlDescriptor(new ResourceModel(MKEY_CATEGORY)) {

            private static final long serialVersionUID = 1L;

            @Override
            public Component createComponent(String componentId) {
                categorySelector = new CategorySelector(componentId, new PropertyModel<Category>(resourceFormData, "category")) {

                    private static final long serialVersionUID = 1L;

                    /**
                     * {@inheritDoc}
                     */
                    @Override
                    protected void onCategorySelected(Category category, AjaxRequestTarget target) {
                        controlDescriptors = null;
                        target.add(controlsTable.getParent());
                    }
                };
                categorySelector.setRequired(true);
                categorySelector.setEnabled(getResourceModel().getObject().getId() == null);
                categorySelector.setOutputMarkupId(true);
                return createValidatableField(categorySelector);
            }
        });
        final Category category = resourceFormData.getCategory();
        if (category != null) {
            controlDescriptors.add(new ControlDescriptor(new ResourceModel(MKEY_MULTIPLICITY)) {

                private static final long serialVersionUID = 1L;

                @Override
                public Component createComponent(String componentId) {
                    return createValidatableField(new InputPanel(componentId) {

                        private static final long serialVersionUID = 1L;

                        @Override
                        protected Component createControl(String controlId) {
                            List<TendersMultiplicity> multiplicityValues = new ArrayList<TendersMultiplicity>();
                            CollectionUtils.addAll(multiplicityValues, TendersMultiplicity.values());
                            DropDownChoice<TendersMultiplicity> multiplicityDropdown = new DropDownChoice<TendersMultiplicity>(controlId, new PropertyModel<TendersMultiplicity>(resourceFormData, "multiplicity"), multiplicityValues, new IChoiceRenderer<TendersMultiplicity>() {

                                private static final long serialVersionUID = 1L;

                                @Override
                                public Object getDisplayValue(TendersMultiplicity value) {
                                    return getString(MKEY_MULTIPLICITY_VALUE_PREFIX + value.name());
                                }

                                @Override
                                public String getIdValue(TendersMultiplicity value, int index) {
                                    return value.name();
                                }
                            });
                            multiplicityDropdown.setRequired(true);
                            return multiplicityDropdown;
                        }
                    }.setOutputMarkupId(true));
                }
            });
        }
        if (category != null) {
            controlDescriptors.add(new ControlDescriptor(new ResourceModel(MKEY_DESCRIPTION)) {

                private static final long serialVersionUID = 1L;

                @Override
                public Component createComponent(String componentId) {
                    return createValidatableField(new InputPanel(componentId) {

                        private static final long serialVersionUID = 1L;

                        @Override
                        protected Component createControl(String controlId) {
                            TextArea<String> descriptionInput = new TextArea<String>(controlId, new PropertyModel<String>(resourceFormData, "description"));
                            descriptionInput.setRequired(true);
                            descriptionInput.setOutputMarkupId(true);
                            return descriptionInput;
                        }
                    }.setOutputMarkupId(true));
                }
            });
        }
        Collection<Property> properties = categoryReader.getProperties(category);
        for (final Property property : properties) {
            String propertyTitle = localizationReader.getString(property);
            controlDescriptors.add(new ControlDescriptor(new Model<String>(propertyTitle)) {

                private static final long serialVersionUID = 1L;

                @Override
                public Component createComponent(String componentId) {
                    return createValidatableField(new InputPanel(componentId) {

                        private static final long serialVersionUID = 1L;

                        @Override
                        protected Component createControl(String controlId) {
                            IModel<Object> controlModel = new ResourcePropertyModel(property, resourceFormData.getResourceValues());
                            return resourcePropertiesControlsFactory.createControl(controlId, property, controlModel);
                        }
                    }.setOutputMarkupId(true));
                }
            });
        }
        return controlDescriptors;
    }

    /**
     * Updates the transient contract.
     */
    private void saveSubmittedResourceData(ResourceFormData resourceFormData) {
        IModel<Resource> resourceModel = getResourceModel();
        Resource resource = resourceModel.getObject();
        resource.setCategory(resourceFormData.getCategory());
        resource.setDescription(resourceFormData.getDescription());
        resource.setTendersMultiplicity(resourceFormData.getMultiplicity());
        Resource savedResource = resourceWriter.save(resource);
        savedResource = resourceReader.getResource(savedResource.getId());
        resourceModel.setObject(savedResource);
        eventsDispatcher.publishEvent(new ResourceChanged(savedResource.getContract(), savedResource));
        setResponsePage(ResourceManagement.class, new PageParameters().add(ResourceManagement.PARAM_RESOURCE_ID, savedResource.getId()));
    }

    /**
     * Wraps the specified into the validation marker. 
     */
    private Component createValidatableField(Component component) {
        return new ValidatableField(component.getId() + MARKER_POSTFIX).add(component);
    }
}
