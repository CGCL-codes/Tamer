package library.proxies;

import library.enums.Library;
import library.enums.Role;

public interface LibraryClientLogon {

    public void performLogon(String user, char[] pass, Role role, Library lib, String host) throws Exception;
}
