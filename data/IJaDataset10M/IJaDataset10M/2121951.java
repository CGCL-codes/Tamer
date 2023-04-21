package org.itsnat.feashow.features.comp.custom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.text.ItsNatHTMLInputPassword;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.feashow.features.comp.shared.MyCustomComponentBase;
import org.w3c.dom.Element;

public class LoginComponent extends MyCustomComponentBase implements ActionListener {

    protected ItsNatHTMLInputText userComp;

    protected ItsNatHTMLInputPassword passwordComp;

    protected ItsNatHTMLInputButton validateComp;

    protected Element logElem;

    protected ValidateLoginListener validateListener;

    public LoginComponent(Element parentElem, ItsNatComponentManager compMgr) {
        super(parentElem, compMgr);
        this.userComp = (ItsNatHTMLInputText) compMgr.createItsNatComponentById("userId");
        this.passwordComp = (ItsNatHTMLInputPassword) compMgr.createItsNatComponentById("passwordId");
        this.validateComp = (ItsNatHTMLInputButton) compMgr.createItsNatComponentById("validateId");
        validateComp.getButtonModel().addActionListener(this);
    }

    public void dispose() {
        userComp.dispose();
        passwordComp.dispose();
        validateComp.dispose();
    }

    public ValidateLoginListener getValidateLoginListener() {
        return validateListener;
    }

    public void setValidateLoginListener(ValidateLoginListener listener) {
        this.validateListener = listener;
    }

    public void actionPerformed(ActionEvent e) {
        if (validateListener != null) {
            validateListener.validate(getUser(), getPassword());
        }
    }

    public String getUser() {
        return userComp.getText();
    }

    public String getPassword() {
        return passwordComp.getText();
    }
}
