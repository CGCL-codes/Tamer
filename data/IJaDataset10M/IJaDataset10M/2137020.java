package gemini.castor.ui.client.page.content.information.library;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LibraryView extends Composite implements LibraryPresenter.Display {

    @UiField
    VerticalPanel contentPanel;

    @UiTemplate("LibraryView.ui.xml")
    interface MyUiBinder extends UiBinder<Panel, LibraryView> {
    }

    private static final MyUiBinder binder = GWT.create(MyUiBinder.class);

    public LibraryView() {
        initWidget(binder.createAndBindUi(this));
    }

    @Override
    public void showWidget(Widget widget) {
        contentPanel.clear();
        contentPanel.add(widget);
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}
