package com.csaba.connector.service;

public abstract class AbstractChangePasswordService extends AbstractBankService implements ChangePasswordService {

    protected String newPassword;

    protected String oldPassword;

    protected String passwordType;

    @Override
    public void setNewPassword(final String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public void setOldPassword(final String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @Override
    public void setPasswordType(final String passwordType) {
        this.passwordType = passwordType;
    }
}
