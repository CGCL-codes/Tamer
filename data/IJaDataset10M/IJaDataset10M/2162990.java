package honeycrm.client.mvp.views;

import honeycrm.client.mvp.presenters.CsvImportPresenter.Display;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CsvImportView extends Composite implements Display {

    private static CsvImportViewUiBinder uiBinder = GWT.create(CsvImportViewUiBinder.class);

    interface CsvImportViewUiBinder extends UiBinder<Widget, CsvImportView> {
    }

    public CsvImportView() {
        initWidget(uiBinder.createAndBindUi(this));
        popup.setGlassEnabled(true);
        header.setText("CSV Import: Insert some SugarCRM CSV export data here.");
        status.setText("Status:");
        cancel.setText("Cancel");
        importBtn.setText("Import");
    }

    @UiField
    VerticalPanel p;

    @UiField
    Label header;

    @UiField
    Label status;

    @UiField
    Button cancel;

    @UiField
    Button importBtn;

    @UiField
    TextArea textArea;

    @UiField
    DecoratedPopupPanel popup;

    @Override
    public HasClickHandlers getCancelBtn() {
        return cancel;
    }

    @Override
    public HasClickHandlers getImportBtn() {
        return importBtn;
    }

    @Override
    public HasText getStatus() {
        return status;
    }

    @Override
    public HasText getHeader() {
        return header;
    }

    @Override
    public HasValue<String> getTextArea() {
        return textArea;
    }

    @Override
    public void center() {
    }

    @Override
    public void hide() {
        popup.getParent().removeFromParent();
    }
}
