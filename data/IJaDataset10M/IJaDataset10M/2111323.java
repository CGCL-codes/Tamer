package alt.djudge.frontend.client.ui;

import alt.djudge.frontend.shared.dto.contest.UserLoginInfo;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;

public abstract class AbstractLayout extends Composite {

    private final DialogBox dbAjaxLoader;

    protected abstract ContentPage[] getPages();

    LoginDialogBox loginDialogBox;

    RegisterDialogBox registerDialogBox;

    static AbstractLayout layout;

    public static AbstractLayout getLayout() {
        return layout;
    }

    public AbstractLayout() {
        layout = this;
        dbAjaxLoader = createAjaxLoaderDialog();
        History.addValueChangeHandler(new ValueChangeHandler<String>() {

            public void onValueChange(ValueChangeEvent<String> event) {
                String historyToken = event.getValue();
                onHistoryEvent(historyToken);
            }
        });
    }

    public void showAjaxLoader() {
        dbAjaxLoader.center();
        dbAjaxLoader.show();
    }

    public void hideAjaxLoader() {
        dbAjaxLoader.hide();
    }

    private DialogBox createAjaxLoaderDialog() {
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setText("Подождите, идет загрузка данных");
        dialogBox.setWidget(new Image("/images/ajax-loader.gif"));
        dialogBox.setGlassEnabled(true);
        dialogBox.setAnimationEnabled(true);
        return dialogBox;
    }

    public void onHistoryEvent(String historyToken) {
        ContentPage[] pages = getPages();
        for (ContentPage page : pages) {
            page.setVisible(false);
        }
        int cnt = 0;
        for (ContentPage page : pages) {
            cnt++;
            String pageToken = page.getHistoryTokenPrefix();
            if (cnt == pages.length || historyToken.startsWith(pageToken)) {
                page.setVisible(true);
                String restToken = pageToken.length() + 1 <= historyToken.length() ? historyToken.substring(pageToken.length() + 1) : "";
                page.dispatch(restToken);
                return;
            }
        }
    }

    protected abstract void setSessionId(String sessionId);

    protected abstract String getSessionId();

    protected void showLoginForm() {
        if (loginDialogBox == null) loginDialogBox = new LoginDialogBox();
        loginDialogBox.center();
        loginDialogBox.show();
    }

    protected void showRegisterForm() {
        if (registerDialogBox == null) registerDialogBox = new RegisterDialogBox();
        registerDialogBox.center();
        registerDialogBox.show();
    }

    public <T> AsyncCallback<T> getAsyncCallback(final AsyncCallbackSucc<T> onSuccessCallback) {
        return new AsyncCallback<T>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.toString());
            }

            @Override
            public void onSuccess(T result) {
                onSuccessCallback.onSuccess(result);
            }
        };
    }

    public abstract void setLoginInfo(UserLoginInfo info);
}
