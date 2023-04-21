package booksandfilms.client.view;

import booksandfilms.client.entities.Topic;
import booksandfilms.client.presenter.TopicEditPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class TopicEditView extends Composite implements TopicEditPresenter.Display {

    private static TopicEditUiBinder uiBinder = GWT.create(TopicEditUiBinder.class);

    interface TopicEditUiBinder extends UiBinder<Widget, TopicEditView> {
    }

    @UiField
    TextBox descriptionField;

    @UiField
    Button cancelButton, saveButton;

    Topic currentTopic = new Topic();

    public TopicEditView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        descriptionField.setFocus(true);
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    public HasClickHandlers getCancelButton() {
        return cancelButton;
    }

    public HasValue<String> getDescription() {
        return descriptionField;
    }

    public HasClickHandlers getSaveButton() {
        return saveButton;
    }
}
