package ontopoly.components;

import java.util.List;
import ontopoly.LockManager;
import ontopoly.OntopolySession;
import ontopoly.model.FieldAssignment;
import ontopoly.model.FieldDefinition;
import ontopoly.model.FieldInstance;
import ontopoly.model.Topic;
import ontopoly.model.TopicType;
import ontopoly.models.FieldDefinitionModel;
import ontopoly.models.FieldInstanceModel;
import ontopoly.models.FieldValueModel;
import ontopoly.models.FieldValuesModel;
import ontopoly.models.FieldsViewModel;
import ontopoly.models.TopicModel;
import ontopoly.utils.OntopolyUtils;
import org.apache.wicket.PageParameters;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

public class FieldInstanceQueryPanel extends AbstractFieldInstancePanel {

    public FieldInstanceQueryPanel(String id, final FieldInstanceModel fieldInstanceModel, final FieldsViewModel fieldsViewModel, final boolean readonly, final boolean traversable) {
        this(id, fieldInstanceModel, fieldsViewModel, readonly, false, traversable);
    }

    public FieldInstanceQueryPanel(String id, final FieldInstanceModel fieldInstanceModel, final FieldsViewModel fieldsViewModel, final boolean readonly, final boolean embedded, final boolean traversable) {
        super(id, fieldInstanceModel);
        FieldInstance fieldInstance = fieldInstanceModel.getFieldInstance();
        FieldAssignment fieldAssignment = fieldInstance.getFieldAssignment();
        FieldDefinition fieldDefinition = fieldAssignment.getFieldDefinition();
        add(new FieldDefinitionLabel("fieldLabel", new FieldDefinitionModel(fieldDefinition)));
        this.fieldValuesContainer = new WebMarkupContainer("fieldValuesContainer");
        fieldValuesContainer.setOutputMarkupId(true);
        add(fieldValuesContainer);
        this.feedbackPanel = new FeedbackPanel("feedback", new AbstractFieldInstancePanelFeedbackMessageFilter());
        feedbackPanel.setOutputMarkupId(true);
        fieldValuesContainer.add(feedbackPanel);
        WebMarkupContainer fieldValuesList = new WebMarkupContainer("fieldValuesList");
        fieldValuesContainer.add(fieldValuesList);
        this.fieldValuesModel = new FieldValuesModel(fieldInstanceModel);
        fieldValuesModel.setAutoExtraField(false);
        fieldValuesModel.getObject();
        this.listView = new ListView<FieldValueModel>("fieldValues", fieldValuesModel) {

            public void populateItem(final ListItem<FieldValueModel> item) {
                final FieldValueModel fieldValueModel = item.getModelObject();
                final WebMarkupContainer fieldValueButtons = new WebMarkupContainer("fieldValueButtons");
                fieldValueButtons.setOutputMarkupId(true);
                item.add(fieldValueButtons);
                Object value = fieldValueModel.getObject();
                final boolean isTopicValue;
                final String topicMapId;
                final String topicId;
                final boolean isLockedByOther;
                if (value instanceof Topic) {
                    isTopicValue = true;
                    Topic oPlayer = (Topic) value;
                    topicMapId = (oPlayer == null ? null : oPlayer.getTopicMap().getId());
                    topicId = (oPlayer == null ? null : oPlayer.getId());
                    if (embedded && fieldValueModel.isExistingValue()) {
                        OntopolySession session = (OntopolySession) Session.get();
                        String lockerId = session.getLockerId(getRequest());
                        LockManager.Lock lock = session.lock(oPlayer, lockerId);
                        isLockedByOther = !lock.ownedBy(lockerId);
                    } else {
                        isLockedByOther = false;
                    }
                    final boolean _readonly = readonly || isLockedByOther;
                    if (embedded) {
                        TopicType defaultTopicType = OntopolyUtils.getDefaultTopicType(oPlayer);
                        List<FieldInstance> fieldInstances = oPlayer.getFieldInstances(defaultTopicType, fieldsViewModel.getFieldsView());
                        if (fieldInstances.isEmpty()) {
                            TopicLink<Topic> playerLink = new TopicLink<Topic>("fieldValue", new TopicModel<Topic>(oPlayer), fieldsViewModel);
                            playerLink.setEnabled(traversable);
                            item.add(playerLink);
                        } else {
                            List<FieldInstanceModel> fieldInstanceModels = FieldInstanceModel.wrapInFieldInstanceModels(fieldInstances);
                            FieldInstancesPanel fip = new FieldInstancesPanel("fieldValue", fieldInstanceModels, fieldsViewModel, _readonly, traversable);
                            fip.setRenderBodyOnly(true);
                            item.add(fip);
                        }
                    } else {
                        TopicLink<Topic> playerLink = new TopicLink<Topic>("fieldValue", new TopicModel<Topic>(oPlayer), fieldsViewModel);
                        playerLink.setEnabled(traversable);
                        item.add(playerLink);
                    }
                } else {
                    isTopicValue = true;
                    topicMapId = null;
                    topicId = null;
                    isLockedByOther = false;
                    item.add(new Label("fieldValue", new Model<String>(value == null ? null : value.toString())));
                }
                OntopolyImageLink gotoButton = new OntopolyImageLink("goto", "goto.gif", new ResourceModel("icon.goto.topic")) {

                    @Override
                    public boolean isVisible() {
                        if (!isTopicValue) return false;
                        FieldValueModel fieldValueModel = item.getModelObject();
                        return embedded && fieldValueModel.isExistingValue();
                    }

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        PageParameters pageParameters = new PageParameters();
                        pageParameters.put("topicMapId", topicMapId);
                        pageParameters.put("topicId", topicId);
                        setResponsePage(getPage().getClass(), pageParameters);
                        setRedirect(true);
                    }
                };
                fieldValueButtons.add(gotoButton);
                OntopolyImageLink lockButton = new OntopolyImageLink("lock", "lock.gif", new ResourceModel("icon.topic.locked")) {

                    @Override
                    public boolean isVisible() {
                        return embedded && isLockedByOther;
                    }

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                    }
                };
                fieldValueButtons.add(lockButton);
            }
        };
        listView.setReuseItems(true);
        fieldValuesList.add(listView);
        this.fieldInstanceButtons = new WebMarkupContainer("fieldInstanceButtons");
        fieldInstanceButtons.setOutputMarkupId(true);
        add(fieldInstanceButtons);
    }
}
