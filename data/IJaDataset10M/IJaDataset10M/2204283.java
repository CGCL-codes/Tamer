package com.mvu.banana.guest.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.*;
import com.mvu.banana.common.client.AsyncCallbackAdapter;
import com.mvu.banana.common.client.CommonComposite;
import com.mvu.banana.common.client.InputColumn;
import com.mvu.banana.common.widget.DayBox;
import com.mvu.banana.common.widget.EnumBox;
import com.mvu.banana.domain.client.Gender;
import com.mvu.banana.guest.client.gen.SignUpFormDTO;

/**
 * @generate-server-code
 */
public class SignUpForm extends CommonComposite<SignUpFormDTO> implements ClickHandler {

    interface SignUpFormUiBinder extends UiBinder<DockLayoutPanel, SignUpForm> {
    }

    private static SignUpFormUiBinder binder = GWT.create(SignUpFormUiBinder.class);

    /** @see com.mvu.banana.domain.stub.Profile#name
   * @hint */
    public TextBox name;

    /** @see com.mvu.banana.domain.stub.Profile#email
   * @hint */
    public TextBox email;

    /** @see com.mvu.banana.domain.stub.Credential#password
   * @hint */
    public PasswordTextBox password;

    /** @see com.mvu.banana.domain.stub.Profile#gender */
    public EnumBox<Gender> gender;

    /** @see com.mvu.banana.domain.stub.Profile#birthDate */
    public DayBox birthDay;

    Button signUpButton = new Button("Sign Up");

    InputColumn center;

    private DockLayoutPanel main;

    MyMessages messages = GWT.create(MyMessages.class);

    public SignUpForm() {
        super(new SignUpFormDTO());
        main = binder.createAndBindUi(this);
        initWidget(main);
        center = new InputColumn(name, email, password, gender, birthDay, signUpButton);
        main.add(center);
        signUpButton.addClickHandler(this);
    }

    public void onClick(ClickEvent event) {
        post(signUpButton, new AsyncCallbackAdapter<SignUpFormDTO>(signUpButton) {

            public void afterUpdate(SignUpFormDTO dto) {
                center.removeFromParent();
                main.add(new Label(messages.thankyou()));
            }
        });
    }
}
