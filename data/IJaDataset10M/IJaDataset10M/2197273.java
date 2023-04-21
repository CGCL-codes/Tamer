package net.videgro.oma.services;

import net.videgro.oma.domain.MemberPermissions;

public interface IAuthenticationService {

    public int authenticateUser(String username, String password);

    public void myLogout();

    public boolean requestNewPassword(String username, String email);

    public MemberPermissions getPermissions(int who, int target);
}
