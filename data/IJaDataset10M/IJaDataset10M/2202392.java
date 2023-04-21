package tps12.axwax.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import tps12.axwax.client.LoginClientImpl.LoginResults;
import tps12.axwax.client.view.SolvePuzzle;
import tps12.axwax.client.presenter.Presenter;
import tps12.axwax.client.presenter.SolvePuzzlePresenter;
import tps12.axwax.client.widgets.SelectPuzzle;
import tps12.axwax.client.widgets.UploadPuzzle;

public class AxWaxUI implements EntryPoint {

    @Override
    public void onModuleLoad() {
        History.addValueChangeHandler(new ValueChangeHandler<String>() {

            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                String value = event.getValue();
                if (value.equals("")) {
                    RootPanel.get().clear();
                    showSelect();
                } else {
                    String url = "", letters = "";
                    for (String arg : value.split("&")) {
                        String[] parts = arg.split("=");
                        if (parts[0].equals("solve")) url = parts[1]; else if (parts[0].equals("letters")) letters = parts[1];
                    }
                    RootPanel.get().clear();
                    Presenter p = new SolvePuzzlePresenter(url, letters, new LoginClientImpl(), new PuzzleClientImpl(new PuzzleClientImpl.GwtRequestBuilderFactory()), new SolvePuzzle());
                    p.go(RootPanel.get());
                }
            }
        });
        History.fireCurrentHistoryState();
    }

    void showSelect() {
        LoginClient login = new LoginClientImpl();
        login.login(Window.Location.getHref(), new AsyncCallback<LoginResults>() {

            @Override
            public void onFailure(Throwable caught) {
                RootPanel.get().add(new Label(caught.getMessage()));
            }

            @Override
            public void onSuccess(LoginResults result) {
                RootPanel root = RootPanel.get();
                root.add(new SelectPuzzle());
                if (result.admin) {
                    root.add(new UploadPuzzle());
                    root.add(new Anchor("Log out", result.url));
                } else if (result.loggedIn) root.add(new Anchor("Log out", result.url)); else root.add(new Anchor("Log in", result.url));
            }
        });
    }
}
