package ca.ericslandry.client.mvp.view;

import ca.ericslandry.client.mvp.presenter.PeoplePresenter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class PeopleView extends BaseView implements PeoplePresenter.Display {

    private SimplePanel panel = new SimplePanel();

    public PeopleView() {
        panel.add(new HTML("people view"));
    }

    public Widget asWidget() {
        return panel;
    }
}
