package net.sourceforge.fluxion.portal.impl;

import net.sourceforge.fluxion.portal.PussycatService;
import net.sourceforge.fluxion.portal.beans.RepositoryBean;
import java.net.URL;

/**
 * Javadocs go here!
 *
 * @author Tony Burdett
 * @date 20-Nov-2008
 */
public class PlaceholderPussycatService implements PussycatService {

    private RepositoryBean repository;

    public void setRepository(RepositoryBean repository) {
        this.repository = repository;
    }

    public RepositoryBean getRepository() {
        return repository;
    }

    public void display(URL datasourceURL) {
        System.out.println("Pussycat will start a new session to display " + datasourceURL);
    }
}
