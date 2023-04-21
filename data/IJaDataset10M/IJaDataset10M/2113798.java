package kirin.client;

import java.util.List;
import kirin.client.model.AlbumModel;
import kirin.client.model.ContactModel;
import kirin.client.model.LoginInfo;
import kirin.client.model.PhotoModel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Kirin implements EntryPoint {

    private LoginInfo loginInfo = null;

    private HorizontalPanel loginInfoPanel = new HorizontalPanel();

    private List<ContactModel> contactList = null;

    private KirinServiceAsync kirinService = GWT.create(KirinService.class);

    LoginServiceAsync loginService = GWT.create(LoginService.class);

    public void onModuleLoad() {
        loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {

            public void onFailure(Throwable error) {
            }

            public void onSuccess(LoginInfo result) {
                loginInfo = result;
                if (loginInfo.isLoggedIn()) {
                    loadLogout();
                    loadKirinData();
                } else {
                    loadLogin();
                }
            }
        });
    }

    private void loadLogin() {
        Anchor loginLink = new Anchor("Sign In");
        loginLink.setHref(loginInfo.getLoginUrl());
        loginInfoPanel.add(new Label("Please sign in to your Google Account."));
        loginInfoPanel.add(loginLink);
        RootPanel.get("authInfo").add(loginInfoPanel);
    }

    private void loadLogout() {
        Anchor logoutLink = new Anchor("Sign Out");
        logoutLink.setHref(loginInfo.getLogoutUrl());
        loginInfoPanel.add(new Label(loginInfo.getEmailAddress()));
        loginInfoPanel.add(logoutLink);
        RootPanel.get("authInfo").add(loginInfoPanel);
    }

    private void loadKirinData() {
        kirinService.loadContact(loginInfo, new AsyncCallback<List<ContactModel>>() {

            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(List<ContactModel> result) {
                contactList = result;
                initTabs();
            }
        });
    }

    private void initTabs() {
        final DecoratedTabPanel tabPanel = new DecoratedTabPanel();
        tabPanel.setAnimationEnabled(true);
        for (final ContactModel contactModel : contactList) {
            final FlexTable o_layout = new FlexTable();
            o_layout.setCellSpacing(6);
            FlexCellFormatter o_cellFormatter = o_layout.getFlexCellFormatter();
            o_cellFormatter.setColSpan(0, 0, 2);
            tabPanel.add(o_layout, contactModel.getNikeName());
            final ListBox dropBox = new ListBox(false);
            for (AlbumModel album : contactModel.getAlbumList()) {
                dropBox.addItem(album.getName());
            }
            o_layout.setWidget(0, 0, dropBox);
            dropBox.addChangeHandler(new ChangeHandler() {

                @Override
                public void onChange(ChangeEvent event) {
                    initPhotoLayout(contactModel, dropBox.getSelectedIndex(), o_layout);
                }
            });
            tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {

                @Override
                public void onSelection(SelectionEvent<Integer> event) {
                    FlexTable currentPhotoTable = (FlexTable) tabPanel.getWidget(event.getSelectedItem());
                    ListBox currentDropBox = (ListBox) currentPhotoTable.getWidget(0, 0);
                    currentDropBox.setSelectedIndex(0);
                }
            });
            initPhotoLayout(contactModel, 0, o_layout);
        }
        tabPanel.selectTab(0);
        RootPanel.get("kirin_tabs").add(tabPanel);
    }

    private void initPhotoLayout(ContactModel contactModel, int selectedIdx, final FlexTable o_layout) {
        for (int i = 1; i < o_layout.getRowCount(); i++) {
            o_layout.removeRow(i);
        }
        final FlexTable m_layout = new FlexTable();
        o_layout.setCellSpacing(6);
        FlexCellFormatter o_cellFormatter = o_layout.getFlexCellFormatter();
        o_cellFormatter.setColSpan(0, 0, 6);
        AlbumModel currentAlbum = contactModel.getAlbumList().get(selectedIdx);
        if (currentAlbum.getPhotos() == null || currentAlbum.getPhotos().size() == 0) {
            kirinService.loadPhoto(contactModel.getNikeName(), currentAlbum.getAlbumid(), new AsyncCallback<List<PhotoModel>>() {

                @Override
                public void onSuccess(List<PhotoModel> result) {
                    int i = 0, j = 0;
                    final PopupPanel imagePopup = new PopupPanel(true);
                    imagePopup.setAnimationEnabled(true);
                    for (int k = 0; k < result.size(); k++) {
                        PhotoModel photoModel = result.get(k);
                        FlexTable i_layout = new FlexTable();
                        i_layout.setCellSpacing(6);
                        FlexCellFormatter i_cellFormatter = i_layout.getFlexCellFormatter();
                        i_layout.setHTML(0, 0, photoModel.getTitle());
                        Image thumbImage = new Image(photoModel.getThumbURL());
                        i_cellFormatter.setColSpan(0, 0, 1);
                        i_cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
                        i_layout.setWidget(1, 0, thumbImage);
                        DecoratorPanel decPanel = new DecoratorPanel();
                        decPanel.setWidget(i_layout);
                        if (j >= 5) {
                            j = 0;
                            i++;
                        }
                        m_layout.setWidget(i, j++, decPanel);
                        imagePopup.setHeight(photoModel.getHeight() + "px");
                        imagePopup.setWidth(photoModel.getWidth() + "px");
                        final String fullImageURL = photoModel.getURL();
                        final Image fullImage = new Image();
                        thumbImage.addClickHandler(new ClickHandler() {

                            public void onClick(ClickEvent event) {
                                imagePopup.clear();
                                fullImage.setUrl(fullImageURL);
                                imagePopup.setWidget(fullImage);
                                imagePopup.center();
                            }
                        });
                        ImageMouseDownHandler mouseDownHandler = new ImageMouseDownHandler();
                        mouseDownHandler.setPhotoModelList(result);
                        mouseDownHandler.setImagePopup(imagePopup);
                        mouseDownHandler.setCurrIdx(k);
                        mouseDownHandler.setFullImage(fullImage);
                        fullImage.addMouseDownHandler(mouseDownHandler);
                        ImageMouseMoveHandler mouseMoveHandler = new ImageMouseMoveHandler();
                        mouseMoveHandler.setFullImage(fullImage);
                        fullImage.addMouseMoveHandler(mouseMoveHandler);
                    }
                    o_layout.setWidget(1, 0, m_layout);
                }

                @Override
                public void onFailure(Throwable caught) {
                }
            });
        }
    }

    class ImageMouseDownHandler implements MouseDownHandler {

        private Image fullImage;

        private PopupPanel imagePopup;

        private List<PhotoModel> photoModelList;

        private int currIdx;

        @Override
        public void onMouseDown(MouseDownEvent event) {
            Element fullImageElement = fullImage.getElement();
            int x_pos = event.getRelativeX(fullImageElement);
            int imageWidth = fullImageElement.getClientWidth();
            if (x_pos > (imageWidth / 2)) {
                PhotoModel nextPhotoModel = currIdx < (photoModelList.size() - 1) ? photoModelList.get(currIdx + 1) : null;
                if (nextPhotoModel != null) {
                    loadNewImage(nextPhotoModel, currIdx + 1);
                }
            } else {
                PhotoModel prePhotoModel = currIdx > 0 ? photoModelList.get(currIdx - 1) : null;
                if (prePhotoModel != null) {
                    loadNewImage(prePhotoModel, currIdx - 1);
                }
            }
        }

        private void loadNewImage(final PhotoModel photoModel, final int nextIdx) {
            imagePopup.clear();
            imagePopup.setHeight(photoModel.getHeight() + "px");
            imagePopup.setWidth(photoModel.getWidth() + "px");
            Image fullImage = new Image(photoModel.getURL());
            imagePopup.setWidget(fullImage);
            if (currIdx > 0) {
                ImageMouseDownHandler mouseDownHandler = new ImageMouseDownHandler();
                mouseDownHandler.setPhotoModelList(photoModelList);
                mouseDownHandler.setImagePopup(imagePopup);
                mouseDownHandler.setCurrIdx(nextIdx);
                mouseDownHandler.setFullImage(fullImage);
                fullImage.addMouseDownHandler(mouseDownHandler);
                ImageMouseMoveHandler mouseMoveHandler = new ImageMouseMoveHandler();
                mouseMoveHandler.setFullImage(fullImage);
                fullImage.addMouseMoveHandler(mouseMoveHandler);
            }
            getImagePopup().center();
        }

        public void setFullImage(Image fullImage) {
            this.fullImage = fullImage;
        }

        public Image getFullImage() {
            return fullImage;
        }

        public void setImagePopup(PopupPanel imagePopup) {
            this.imagePopup = imagePopup;
        }

        public PopupPanel getImagePopup() {
            return imagePopup;
        }

        public void setPhotoModelList(List<PhotoModel> photoModelList) {
            this.photoModelList = photoModelList;
        }

        public List<PhotoModel> getPhotoModelList() {
            return photoModelList;
        }

        public void setCurrIdx(int currIdx) {
            this.currIdx = currIdx;
        }

        public int getCurrIdx() {
            return currIdx;
        }
    }

    class ImageMouseMoveHandler implements MouseMoveHandler {

        private Image fullImage;

        public void onMouseMove(MouseMoveEvent event) {
            Element fullImageElement = getFullImage().getElement();
            int x_pos = event.getRelativeX(fullImageElement);
            int imageWidth = fullImageElement.getClientWidth();
            if (x_pos > (imageWidth / 2)) {
                fullImageElement.removeClassName("leftCursor");
                fullImageElement.addClassName("rightCursor");
            } else {
                fullImageElement.removeClassName("rightCursor");
                fullImageElement.addClassName("leftCursor");
            }
        }

        public void setFullImage(Image fullImage) {
            this.fullImage = fullImage;
        }

        public Image getFullImage() {
            return fullImage;
        }
    }
}
