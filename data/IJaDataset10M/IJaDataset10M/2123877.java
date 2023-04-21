package com.mockturtlesolutions.snifflib.reposconfig.database;

import java.util.EventListener;

public interface RepositoryListener extends EventListener {

    /**
	Invoked when an a repository is changed.
	*/
    public void repositoryChanged(RepositoryEvent e);

    public void setRepository(String repos);

    public String getRepository();
}
