package org.fudaa.dodico.crue.projet.conf;

/**
 *
 * @author Chris
 */
public class Configuration {

    private SiteConfiguration site;

    private UserConfiguration user;

    public SiteConfiguration getSite() {
        return site;
    }

    public void setSite(SiteConfiguration site) {
        this.site = site;
    }

    public UserConfiguration getUser() {
        return user;
    }

    public void setUser(UserConfiguration user) {
        this.user = user;
    }
}
