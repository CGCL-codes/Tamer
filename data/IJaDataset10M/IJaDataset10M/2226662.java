package org.internna.ossmoney.services;

import java.io.IOException;
import org.internna.ossmoney.model.security.UserDetails;

public interface AdminService {

    void createPayee(String name);

    void createPayee(UserDetails owner, String name);

    byte[] backup(UserDetails user) throws IOException;

    void createInstitution(String name, String web, String icon);

    void createInstitution(UserDetails owner, String name, String web, String icon);
}
