package com.tinywebgears.tuatara.core.service;

import java.util.Collection;
import com.google.appengine.api.datastore.Key;
import com.tinywebgears.tuatara.core.dao.DataPersistenceException;
import com.tinywebgears.tuatara.core.model.MyFile;
import com.tinywebgears.tuatara.core.model.Sharing;
import com.tinywebgears.tuatara.core.model.User;
import com.tinywebgears.tuatara.framework.common.ApplicationException;
import com.tinywebgears.tuatara.framework.gui.message.UserMessageException;
import com.vaadin.terminal.Resource;

public interface UserServicesIF extends ServiceIF {

    User setUsername(String username, String email, String nickname) throws DataPersistenceException;

    Resource serveTextFile(String filename, byte[] text) throws UserMessageException;

    MyFile getFileByKey(Key fileKey) throws DataPersistenceException;

    User getUserByKey(Key userKey) throws DataPersistenceException;

    void removeAllFiles();

    void createFile(MyFile file) throws DataPersistenceException;

    public void createContributor(String name, String email, Boolean readOnly) throws ApplicationException;

    public Collection<Sharing> getContributors() throws DataPersistenceException;

    public Collection<Sharing> getExporters() throws DataPersistenceException, ServiceException;

    void removeContributors(Collection<Sharing> sharings) throws ApplicationException;

    void removeExporters(Collection<Sharing> sharings) throws ApplicationException;

    void acceptInvitations(Collection<Sharing> sharings) throws ApplicationException;
}
