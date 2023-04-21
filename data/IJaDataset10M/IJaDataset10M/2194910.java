package net.tralfamadore.security;

/**
 * User: billreh
 * Date: 2/12/11
 * Time: 12:01 AM
 */
public class NoneCurrentUserInfo implements CurrentUserInfo {

    @Override
    public String getCurrentUser() {
        return "cmfAdmin";
    }
}
