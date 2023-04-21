package net.sf.revolver.s2blazeds.sample.service.login;

import net.sf.bulletlib.validate.annotation.NotEmpty;

public class LoginDto {

    @NotEmpty(replace = { "loginId" })
    public String loginId;

    @NotEmpty(replace = { "password" })
    public String password;
}
