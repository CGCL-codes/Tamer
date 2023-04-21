package pl.agh.picturebrowser.client.widgets;

import pl.agh.picturebrowser.client.events.GalleryUpdatedEvent;
import pl.agh.picturebrowser.client.events.GalleryUpdatedEventHandler;
import pl.agh.picturebrowser.client.services.LoginService;
import pl.agh.picturebrowser.client.services.LoginServiceAsync;
import pl.agh.picturebrowser.client.services.UserImageService;
import pl.agh.picturebrowser.client.services.UserImageServiceAsync;
import pl.agh.picturebrowser.shared.LoginInfo;
import pl.agh.picturebrowser.shared.UploadedImage;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.Widget;

public class Upload extends Composite {

    interface Binder extends UiBinder<Widget, Upload> {
    }

    UserImageServiceAsync userImageService = GWT.create(UserImageService.class);

    LoginServiceAsync loginService = GWT.create(LoginService.class);

    private HandlerManager handlerManager;

    private static final Binder binder = GWT.create(Binder.class);

    private Listener listener;

    @UiField
    Button uploadButton;

    @UiField
    FormPanel uploadForm;

    @UiField
    FileUpload uploadField;

    @UiField
    Anchor loginAnchor;

    LoginInfo loginInfo;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Upload() {
        initWidget(binder.createAndBindUi(this));
        loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {

            @Override
            public void onSuccess(LoginInfo result) {
                Window.alert("Success PicBrowser");
                loginInfo = result;
                if (loginInfo.isLoggedIn()) {
                    Window.alert("Zalogowany");
                    prepareUpload();
                } else {
                    loadLogin();
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("NIeZalogowany");
            }
        });
    }

    private void loadLogin() {
        loginAnchor.setHref(loginInfo.getLoginUrl());
    }

    public void prepareUpload() {
        loginAnchor.setVisible(false);
        uploadButton.setText("Za�aduj");
        uploadField.setName("image");
        startNewBlobstoreSession();
        uploadForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

            @Override
            public void onSubmitComplete(SubmitCompleteEvent event) {
                uploadForm.reset();
                startNewBlobstoreSession();
                String key = event.getResults();
                userImageService.get(key, new AsyncCallback<UploadedImage>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Upload.onSubmitComplete.FAIL");
                    }

                    @Override
                    public void onSuccess(UploadedImage result) {
                        if (listener != null) listener.onUploadPicture();
                    }
                });
            }
        });
    }

    private void startNewBlobstoreSession() {
        userImageService.getBlobstoreUploadUrl(new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println("Success Upload " + result);
                Window.alert(result);
                uploadForm.setAction(result);
                uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
                uploadForm.setMethod(FormPanel.METHOD_POST);
                uploadButton.setText("Upload");
                uploadButton.setEnabled(true);
            }

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Failed");
            }
        });
    }

    @UiHandler("uploadButton")
    void onSubmit(ClickEvent e) {
        uploadForm.submit();
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }
}
