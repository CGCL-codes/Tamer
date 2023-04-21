package gemini.castor.ui.client.page.content.information.stepbystep;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class StepByStepView extends Composite implements StepByStepPresenter.Display {

    @UiField
    VerticalPanel contentPanel;

    @UiTemplate("StepByStepView.ui.xml")
    interface MyUiBinder extends UiBinder<Panel, StepByStepView> {
    }

    private static final MyUiBinder binder = GWT.create(MyUiBinder.class);

    public StepByStepView() {
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
