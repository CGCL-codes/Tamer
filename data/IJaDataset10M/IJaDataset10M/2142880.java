package com.m4f.appengine.utils.web.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class RecoveryForm {

    @NotNull(message = "Mail blank")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Bad mail")
    private String email;

    public RecoveryForm() {
        super();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
