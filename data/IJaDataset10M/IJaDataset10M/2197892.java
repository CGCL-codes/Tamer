package org.deft.repository.event;

import java.util.EventListener;

public interface RepositoryOptionsChangeListener extends EventListener {

    public void repositoryLocationAboutToChange();

    public void repositoryLocationChanged();
}
