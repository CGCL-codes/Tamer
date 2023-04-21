package org.modelibra.wicket.concept;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.modelibra.IEntity;
import org.modelibra.config.ConceptConfig;
import org.modelibra.config.PropertiesConfig;
import org.modelibra.config.PropertyConfig;
import org.modelibra.type.PropertyClass;
import org.modelibra.type.ValidationType;
import org.modelibra.wicket.app.DomainApp;
import org.modelibra.wicket.container.DmPanel;
import org.modelibra.wicket.container.PropertyNameLabelValuePanelListView;
import org.modelibra.wicket.util.LocalizedText;
import org.modelibra.wicket.util.PropertyNameLabelValuePanelPair;
import org.modelibra.wicket.view.View;
import org.modelibra.wicket.view.ViewModel;
import org.modelibra.wicket.widget.CheckBoxPanel;
import org.modelibra.wicket.widget.ExternalLinkPanel;
import org.modelibra.wicket.widget.LabelPanel;
import org.modelibra.wicket.widget.MultiLineLabelPanel;

/**
 * Entity display minimal panel (only essential properties without title).
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-12-12
 */
public class EntityDisplayMinPanel extends DmPanel {

    private static final long serialVersionUID = 102130L;

    /**
	 * Constructs an entity display min panel.
	 * 
	 * @param viewModel
	 *            view model
	 * @param view
	 *            view
	 */
    public EntityDisplayMinPanel(final ViewModel viewModel, final View view) {
        super(view.getWicketId());
        DomainApp app = (DomainApp) getApplication();
        IEntity<?> entity = viewModel.getEntity();
        ConceptConfig conceptConfig = entity.getConceptConfig();
        List<PropertyNameLabelValuePanelPair> propertyNameLabelValuePanelPairs = new ArrayList<PropertyNameLabelValuePanelPair>();
        ViewModel propertyModel = new ViewModel();
        propertyModel.copyPropertiesFrom(viewModel);
        propertyModel.setEntity(entity);
        PropertiesConfig propertiesConfig = conceptConfig.getPropertiesConfig();
        for (PropertyConfig propertyConfig : propertiesConfig) {
            if (propertyConfig.isEssential()) {
                String propertyName = LocalizedText.getPropertyName(this, entity, propertyConfig);
                PropertyNameLabelValuePanelPair propertyNameLabelValuePanelPair = new PropertyNameLabelValuePanelPair();
                Label propertyNameLabel = new Label("propertyName", propertyName);
                propertyNameLabelValuePanelPair.setPropertyNameLabel(propertyNameLabel);
                propertyModel.setPropertyConfig(propertyConfig);
                View propertyValueView = new View();
                propertyValueView.copyPropertiesFrom(view);
                propertyValueView.setWicketId("valuePanel");
                Panel essentialPropertyPanel;
                if (propertyConfig.getPropertyClass().equals(PropertyClass.getUrl()) || propertyConfig.getPropertyClass().equals(PropertyClass.getEmail())) {
                    essentialPropertyPanel = new ExternalLinkPanel(propertyModel, propertyValueView);
                } else if (propertyConfig.getPropertyClass().equals(PropertyClass.getString()) && propertyConfig.isValidateType() && (propertyConfig.getValidationType().equals(ValidationType.getUrl()) || propertyConfig.getValidationType().equals(ValidationType.getEmail()))) {
                    essentialPropertyPanel = new ExternalLinkPanel(propertyModel, propertyValueView);
                } else if (propertyConfig.getPropertyClass().equals(PropertyClass.getBoolean())) {
                    essentialPropertyPanel = new CheckBoxPanel(propertyModel, propertyValueView);
                } else if (propertyConfig.getPropertyClass().equals(PropertyClass.getString()) && propertyConfig.getDisplayLengthInt() > DomainApp.MIN_LONG_TEXT_LENGTH) {
                    essentialPropertyPanel = new MultiLineLabelPanel(propertyModel, propertyValueView);
                } else {
                    essentialPropertyPanel = new LabelPanel(propertyModel, propertyValueView);
                }
                if (!app.getAccessPoint().isPropertyDisplayAllowed(getAppSession(), propertyConfig)) {
                    essentialPropertyPanel.setVisible(false);
                }
                propertyNameLabelValuePanelPair.setPropertyValuePanel(essentialPropertyPanel);
                propertyNameLabelValuePanelPairs.add(propertyNameLabelValuePanelPair);
            }
        }
        ListView propertyNameLabelValuePanelListView = new PropertyNameLabelValuePanelListView("propertyNameLabelValuePanelListView", propertyNameLabelValuePanelPairs);
        add(propertyNameLabelValuePanelListView);
        if (!app.getAccessPoint().isConceptDisplayAllowed(getAppSession(), conceptConfig)) {
            propertyNameLabelValuePanelListView.setVisible(false);
        }
    }
}
