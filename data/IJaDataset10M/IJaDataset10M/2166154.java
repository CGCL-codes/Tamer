package ru.itbrains.jicard.identity;

import java.util.List;
import java.util.LinkedList;

/**
 *
 */
public class TokenServiceList {

    private List<TokenService> tokenServices = new LinkedList<TokenService>();

    public void addTokenService(TokenService tokenService) {
        tokenServices.add(tokenService);
    }

    public List<TokenService> getTokenServices() {
        return tokenServices;
    }

    public void setTokenServices(List<TokenService> tokenServices) {
        this.tokenServices = tokenServices;
    }

    public TokenService getTokenService(int i) {
        return tokenServices.get(i);
    }

    public int getNoOfServices() {
        return tokenServices.size();
    }

    public String toString() {
        return "TokenServiceList{" + "tokenServices=" + tokenServices + '}';
    }
}
