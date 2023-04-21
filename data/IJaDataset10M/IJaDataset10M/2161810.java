package com.simconomy.server.content;

import javax.jcr.Repository;

public class ContentServiceImpl implements ContentService {

    private Repository repository = null;

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }
}
