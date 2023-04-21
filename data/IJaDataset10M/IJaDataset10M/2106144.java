package sfeir.gwt.ergosoom.client.profile;

import java.util.ArrayList;
import java.util.List;
import sfeir.gwt.ergosoom.client.ClientApi;
import sfeir.gwt.ergosoom.client.ClientApiAsync;
import sfeir.gwt.ergosoom.client.ImportDialog;
import sfeir.gwt.ergosoom.client.Messages;
import sfeir.gwt.ergosoom.client.model.Address;
import sfeir.gwt.ergosoom.client.model.Email;
import sfeir.gwt.ergosoom.client.model.NetworkItem;
import sfeir.gwt.ergosoom.client.model.Person;
import sfeir.gwt.ergosoom.client.model.Profile;
import sfeir.gwt.ergosoom.client.model.Tel;
import sfeir.gwt.ergosoom.client.profile.text.RichTextToolbar;
import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

public class EditProfile extends Composite implements ClickHandler, ChangeHandler, BlurHandler, HasChangeHandlers {

    private Messages message = GWT.create(Messages.class);

    private ClientApiAsync profileManager;

    private TextBox password, firstname, lastname, nickname, company, role, aliasLink;

    private PushButton photo;

    private VerticalPanel photoPanel;

    private RichTextArea about;

    private FlexComboField phone, email, address;

    private Boolean isSave = true;

    public EditProfile(Profile profile) {
        super();
        profileManager = GWT.create(ClientApi.class);
        this.profile = profile;
        initGUI();
        loadDatas();
    }

    FlexNetworkField web;

    private Profile profile;

    private Button save, saveUp;

    Hyperlink importButton, importButtonUp;

    private HTML rbupload;

    private HTML rburl;

    private String photoURL;

    private Label infoText;

    private Boolean aliasCheck = false;

    private void initGUI() {
        VerticalPanel root = new VerticalPanel();
        HorizontalPanel toolbar = new HorizontalPanel();
        toolbar.setWidth("100%");
        HorizontalPanel HPaliasLink = buildAliasBar();
        toolbar.add(HPaliasLink);
        toolbar.setCellHorizontalAlignment(HPaliasLink, HasHorizontalAlignment.ALIGN_LEFT);
        toolbar.setCellVerticalAlignment(HPaliasLink, HasVerticalAlignment.ALIGN_MIDDLE);
        infoText = new Label("");
        infoText.setWidth("100%");
        toolbar.add(infoText);
        toolbar.setCellHorizontalAlignment(infoText, HasHorizontalAlignment.ALIGN_CENTER);
        toolbar.setCellVerticalAlignment(infoText, HasVerticalAlignment.ALIGN_MIDDLE);
        toolbar.setCellWidth(infoText, "100%");
        importButtonUp = new Hyperlink(message.import_label(), "");
        importButtonUp.setTitle(message.import_profile());
        importButtonUp.addClickHandler(this);
        toolbar.add(importButtonUp);
        toolbar.setCellVerticalAlignment(importButtonUp, HasVerticalAlignment.ALIGN_MIDDLE);
        toolbar.setSpacing(8);
        saveUp = new Button(message.save());
        saveUp.addClickHandler(this);
        saveUp.setEnabled(false);
        toolbar.add(saveUp);
        toolbar.setCellVerticalAlignment(saveUp, HasVerticalAlignment.ALIGN_TOP);
        root.add(toolbar);
        Grid layout_grid = new Grid(2, 2);
        layout_grid.setSize("100%", "100%");
        layout_grid.setCellSpacing(5);
        layout_grid.setCellPadding(8);
        VerticalPanel mainInfos = new VerticalPanel();
        mainInfos.add(buildPersonalInfos());
        mainInfos.add(buildProInfos());
        DecoratorPanel mainDecoratorPanel = new DecoratorPanel();
        mainDecoratorPanel.setWidth("100%");
        mainDecoratorPanel.add(mainInfos);
        layout_grid.setWidget(0, 0, mainDecoratorPanel);
        layout_grid.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        VerticalPanel aboutField = buildAbout();
        DecoratorPanel aboutDecoratorPanel = new DecoratorPanel();
        aboutDecoratorPanel.setWidth("100%");
        aboutDecoratorPanel.add(aboutField);
        layout_grid.setWidget(0, 1, aboutDecoratorPanel);
        layout_grid.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
        DecoratorPanel contactDecoratorPanel = new DecoratorPanel();
        contactDecoratorPanel.setWidth("100%");
        contactDecoratorPanel.add(buildContactMethods());
        layout_grid.setWidget(1, 0, contactDecoratorPanel);
        layout_grid.getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
        DecoratorPanel webDecoratorPanel = new DecoratorPanel();
        webDecoratorPanel.setWidth("100%");
        web = new FlexNetworkField();
        web.addChangeHandler(this);
        webDecoratorPanel.add(web);
        layout_grid.setWidget(1, 1, webDecoratorPanel);
        layout_grid.getCellFormatter().setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_TOP);
        root.add(layout_grid);
        HorizontalPanel buttonBarBottom = new HorizontalPanel();
        buttonBarBottom.setWidth("100%");
        importButton = new Hyperlink(message.import_label(), "");
        importButton.setTitle(message.import_profile());
        importButton.addClickHandler(this);
        buttonBarBottom.add(importButton);
        buttonBarBottom.setCellHorizontalAlignment(importButton, HasAlignment.ALIGN_LEFT);
        save = new Button(message.save());
        save.addClickHandler(this);
        save.setEnabled(false);
        buttonBarBottom.add(save);
        buttonBarBottom.setCellHorizontalAlignment(save, HasAlignment.ALIGN_RIGHT);
        root.add(buttonBarBottom);
        initWidget(root);
    }

    private void loadDatas() {
        if (profile != null) {
            String alias = profile.getAlias();
            if (alias != null && !alias.isEmpty()) {
                aliasLink.setText(alias);
                aliasCheck = true;
            }
            Person person = profile.getPerson();
            if (person != null) {
                firstname.setText(person.getFirstname());
                lastname.setText(person.getName());
                nickname.setText(person.getNickname());
                if (person.getNickname() != null) nickname.setText(person.getNickname());
                if (person.getPhoto() != null) {
                    photoURL = person.getPhoto();
                    Image image = new Image(photoURL);
                    if (image != null) {
                        image.setPixelSize(80, 100);
                        photo = new PushButton(image);
                        photo.setPixelSize(80, 100);
                        photo.addClickHandler(new ClickHandler() {

                            @Override
                            public void onClick(ClickEvent event) {
                                showPhotoDialog();
                            }
                        });
                        photoPanel.clear();
                        photoPanel.add(photo);
                    }
                }
                company.setText(person.getCompany());
                role.setText(person.getTitle());
                if (person.getTels() != null) phone.setRows((ArrayList<?>) person.getTels());
                if (person.getEmails() != null) email.setRows((ArrayList<?>) person.getEmails());
                if (person.getAddresses() != null) address.setRows((ArrayList<?>) person.getAddresses());
                if (person.getNetworkItems() != null) web.setRows(person.getNetworkItems());
                String aboutString = person.getAboutMe();
                if (aboutString != null && !aboutString.equalsIgnoreCase("")) about.setHTML(aboutString);
                setIsSave(true);
            }
        }
    }

    /**
     * Panel of the Alias panel
     * @return
     */
    private HorizontalPanel buildAliasBar() {
        HorizontalPanel HPaliasLink = new HorizontalPanel();
        HPaliasLink.setStyleName("alias-link-panel");
        HPaliasLink.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        HPaliasLink.add(new Label("http://" + Window.Location.getHost() + "/profile/"));
        aliasLink = new TextBox();
        aliasLink.addChangeHandler(this);
        aliasLink.setWidth("100px");
        HPaliasLink.add(aliasLink);
        return HPaliasLink;
    }

    /**
     * Panel of FlexComboFields phone, email, and postal address
     * 
     * @return
     */
    private VerticalPanel buildContactMethods() {
        VerticalPanel contactMethods = new VerticalPanel();
        phone = new FlexComboField(FlexComboField.PHONE);
        phone.addChangeHandler(this);
        email = new FlexComboField(FlexComboField.MAIL);
        email.addChangeHandler(this);
        address = new FlexComboField(FlexComboField.ADDRESS);
        address.addChangeHandler(this);
        contactMethods.add(phone);
        contactMethods.add(email);
        contactMethods.add(address);
        return contactMethods;
    }

    /**
     * rich text area for "about me"
     * 
     * @return
     */
    private VerticalPanel buildAbout() {
        VerticalPanel aboutField = new VerticalPanel();
        aboutField.setWidth("100%");
        HTML aboutLabel = new HTML(message.aboutme());
        about = new RichTextArea();
        about.addBlurHandler(this);
        about.ensureDebugId("cwRichText-area");
        about.setSize("100%", "14em");
        RichTextToolbar toolbar = new RichTextToolbar(about);
        toolbar.ensureDebugId("cwRichText-toolbar");
        toolbar.setWidth("100%");
        Panel panel = new FlowPanel();
        panel.add(about);
        panel.setStyleName("panel-RichText");
        Grid grid = new Grid(2, 1);
        grid.setStyleName("cw-RichText");
        grid.setWidget(0, 0, toolbar);
        grid.setWidget(1, 0, panel);
        aboutField.add(aboutLabel);
        aboutField.add(grid);
        return aboutField;
    }

    /**
     * set the professionnal fields
     * 
     * @return Grid
     */
    private Grid buildProInfos() {
        Grid pro = new Grid(2, 2);
        HTML companyLabel = new HTML(message.company());
        HTML roleLabel = new HTML(message.role());
        company = new TextBox();
        company.addChangeHandler(this);
        company.setWidth("20em");
        role = new TextBox();
        role.addChangeHandler(this);
        role.setWidth("20em");
        pro.setWidget(0, 0, companyLabel);
        pro.setWidget(0, 1, company);
        pro.setWidget(1, 0, roleLabel);
        pro.setWidget(1, 1, role);
        return pro;
    }

    /**
     * Set the names fields and the photo button
     * 
     * @param perso
     */
    private HorizontalPanel buildPersonalInfos() {
        HorizontalPanel perso = new HorizontalPanel();
        photoPanel = new VerticalPanel();
        photo = new PushButton();
        photo.setPixelSize(80, 100);
        photo.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                showPhotoDialog();
            }
        });
        photoPanel.add(photo);
        Grid names = new Grid(4, 2);
        HTML passwordLabel = new HTML(message.password());
        HTML firstnameLabel = new HTML(message.firstname());
        HTML lastnameLabel = new HTML(message.lastname());
        HTML nicknameLabel = new HTML(message.nickname());
        password = new PasswordTextBox();
        password.addChangeHandler(this);
        firstname = new TextBox();
        firstname.addChangeHandler(this);
        lastname = new TextBox();
        lastname.addChangeHandler(this);
        nickname = new TextBox();
        nickname.addChangeHandler(this);
        names.setWidget(0, 0, passwordLabel);
        names.setWidget(0, 1, password);
        names.setWidget(1, 0, firstnameLabel);
        names.setWidget(1, 1, firstname);
        names.setWidget(2, 0, lastnameLabel);
        names.setWidget(2, 1, lastname);
        names.setWidget(3, 0, nicknameLabel);
        names.setWidget(3, 1, nickname);
        perso.add(photoPanel);
        perso.add(names);
        return perso;
    }

    /**
     * Set the dialog form to change the profile's photo
     */
    private void showPhotoDialog() {
        final DialogBox photoDialog = new DialogBox();
        photoDialog.setText(message.change_photo());
        final VerticalPanel dialogPanel = new VerticalPanel();
        dialogPanel.setSpacing(5);
        photoDialog.setWidget(dialogPanel);
        dialogPanel.add(new HTML(message.change_photo_msg(), true));
        HorizontalPanel urlField = new HorizontalPanel();
        HorizontalPanel uploadField = new HorizontalPanel();
        final TextBox url = new TextBox();
        final FormPanel form = new FormPanel();
        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setMethod(FormPanel.METHOD_POST);
        form.setAction("/gwtergosoom/import");
        final FileUpload upload = new FileUpload();
        upload.setName("photo");
        form.setWidget(upload);
        form.addSubmitHandler(new SubmitHandler() {

            @Override
            public void onSubmit(SubmitEvent event) {
                String[] ext = upload.getFilename().split("\\.");
                int size = ext.length;
                if (size > 0 && ext[size - 1].length() > 1) {
                    if (!ext[size - 1].equalsIgnoreCase("jpg") && !ext[size - 1].equalsIgnoreCase("png") && !ext[size - 1].equalsIgnoreCase("jpeg")) {
                        event.cancel();
                        Window.alert(message.only_img_file());
                    }
                }
            }
        });
        form.addSubmitCompleteHandler(new SubmitCompleteHandler() {

            @Override
            public void onSubmitComplete(SubmitCompleteEvent event) {
                photoURL = event.getResults();
                if (!photoURL.contains("http://")) {
                    Window.alert(message.error_upload_img());
                    GWT.log(photoURL, null);
                }
                if (profile != null && profile.getPerson() != null) {
                    profile.getPerson().setPhoto(photoURL);
                    setIsSave(false);
                }
                Image image = new Image(photoURL);
                if (image != null) {
                    image.setPixelSize(80, 100);
                    photo = new PushButton(image);
                    photo.setPixelSize(80, 100);
                    photo.addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            showPhotoDialog();
                        }
                    });
                    photoPanel.clear();
                    photoPanel.add(photo);
                }
                photoDialog.hide();
            }
        });
        rburl = new HTML(message.url());
        rburl.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                url.setEnabled(true);
            }
        });
        rbupload = new HTML(message.upload());
        urlField.add(rburl);
        urlField.add(url);
        uploadField.add(rbupload);
        uploadField.add(form);
        dialogPanel.add(urlField);
        dialogPanel.add(uploadField);
        HorizontalPanel buttons = new HorizontalPanel();
        buttons.setSpacing(10);
        Button cancel = new Button(message.cancel());
        cancel.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                photoDialog.hide();
            }
        });
        Button ok = new Button(message.ok());
        ok.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                String urlString = url.getText();
                String filename = upload.getFilename();
                if (filename != null && !filename.equalsIgnoreCase("")) {
                    if (filename.length() == 0) {
                        Window.alert(message.choose_file_upload());
                    } else {
                        form.submit();
                    }
                } else if (urlString != null && !urlString.equalsIgnoreCase("")) {
                    if (profile != null && profile.getPerson() != null) profile.getPerson().setPhoto(photoURL);
                    photoURL = urlString;
                    photo.setHTML("<img src='" + photoURL + "' />");
                    photo.getUpFace().setHTML("<img src='" + photoURL + "' />");
                    photoDialog.hide();
                    setIsSave(false);
                }
            }
        });
        buttons.add(cancel);
        buttons.add(ok);
        dialogPanel.add(buttons);
        dialogPanel.setCellHorizontalAlignment(buttons, HasAlignment.ALIGN_RIGHT);
        photoDialog.center();
        photoDialog.show();
    }

    @Override
    public void onChange(ChangeEvent event) {
        setIsSave(false);
        if (event != null && event.getSource() == aliasLink) {
            onChangeAlias();
        }
    }

    private void onChangeAlias() {
        aliasLink.setTitle("");
        String value = aliasLink.getText();
        aliasCheck = (profile != null && value.equals(profile.getAlias()));
        if (!value.matches("^([a-zA-Z0-9_.@+-])+$")) {
            aliasLink.addStyleName("form-input-error");
            aliasLink.setTitle(message.error_use_alphanum());
            aliasCheck = false;
            return;
        }
        if (!aliasCheck) {
            profileManager.checkAlias(value, new AsyncCallback<Boolean>() {

                @Override
                public void onFailure(Throwable caught) {
                    GWT.log(message.error_checkalias(), caught);
                }

                @Override
                public void onSuccess(Boolean result) {
                    aliasCheck = result;
                    if (result) {
                        aliasLink.removeStyleName("form-input-error");
                        aliasLink.addStyleName("form-input-ok");
                        aliasLink.setTitle(message.available());
                    } else {
                        aliasLink.setTitle(message.already_used());
                    }
                }
            });
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == save || event.getSource() == saveUp) {
            save.setEnabled(false);
            saveUp.setEnabled(false);
            if (!aliasCheck) {
                aliasLink.addStyleName("form-input-error");
                Window.alert(message.must_set_url());
                save.setEnabled(true);
                saveUp.setEnabled(true);
                return;
            }
            final Profile profile = getEditProfile();
            if (profile == null) {
                Window.alert(message.must_set_lastname());
            } else {
                if (!password.getText().isEmpty()) profile.setPassword(password.getText()); else profile.setPassword(null);
                profileManager.createProfile(profile, new AsyncCallback<Long>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        setIsSave(false);
                        flashMessage(message.failure(caught.getMessage()), "message-red");
                    }

                    @Override
                    public void onSuccess(Long result) {
                        EditProfile.this.profile = profile;
                        setIsSave(true);
                        flashMessage(message.profile_save(), "message-green");
                    }
                });
            }
        } else if (event.getSource() == importButton || event.getSource() == importButtonUp) {
            final ImportDialog importDialog = new ImportDialog();
            importDialog.addCloseHandler(new CloseHandler<PopupPanel>() {

                @Override
                public void onClose(CloseEvent<PopupPanel> event) {
                    Person p = importDialog.getPerson();
                    if (p != null) {
                        setPerson(p);
                        setIsSave(false);
                    }
                }
            });
            importDialog.center();
        }
    }

    public Person getPerson() {
        return profile.getPerson();
    }

    public void setPerson(Person person) {
        this.profile.setPerson(person);
        loadDatas();
    }

    public Profile getProfile() {
        return profile;
    }

    @SuppressWarnings({ "unchecked", "null" })
    public Profile getEditProfile() {
        if (isSave) return profile;
        String name = lastname.getText();
        List<Email> listEmail = (List<Email>) email.getDatas();
        if (name == null || name.equalsIgnoreCase("")) {
            if (listEmail != null && listEmail.size() > 0) name = ((Email) (listEmail.get(0))).getEmail();
        }
        if (name != null || !name.equalsIgnoreCase("")) {
            Person person = new Person();
            person.setName(name);
            profile.setAlias(aliasLink.getText());
            String fn = firstname.getText();
            String nn = nickname.getText();
            String comp = company.getText();
            String rol = role.getText();
            String abo = about.getHTML();
            if (!fn.equalsIgnoreCase("")) person.setFirstname(fn);
            if (!nn.equalsIgnoreCase("")) person.setNickname(nn);
            if (!comp.equalsIgnoreCase("")) person.setCompany(comp);
            if (!rol.equalsIgnoreCase("")) person.setTitle(rol);
            if (!abo.equalsIgnoreCase("")) person.setAboutMe(abo);
            person.setTels((List<Tel>) (phone.getDatas()));
            person.setEmails(listEmail);
            person.setAddresses((List<Address>) (address.getDatas()));
            person.setNetworkItems((List<NetworkItem>) (web.getDatas()));
            if (photoURL != null && !photoURL.isEmpty()) person.setPhoto(photoURL);
            profile.setPerson(person);
            return profile;
        }
        return null;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
        loadDatas();
    }

    public void setIsSave(Boolean isSave) {
        this.isSave = isSave;
        save.setEnabled(!isSave);
        saveUp.setEnabled(!isSave);
        if (!isSave) {
            flashMessage(message.not_saved(), "message-red");
        } else {
            flashMessage("", "message-hidden");
            if (changeReg != null) {
                changeReg.onChange(null);
            }
        }
    }

    private void flashMessage(String message, String style) {
        infoText.setText(message);
        infoText.setStylePrimaryName(style);
    }

    public Boolean getIsSave() {
        return isSave;
    }

    @Override
    public void onBlur(BlurEvent event) {
        setIsSave(false);
    }

    private ChangeHandler changeReg = null;

    @Override
    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        changeReg = handler;
        return null;
    }
}
