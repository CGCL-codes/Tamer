package com.tinywebgears.gaedownload.core.service;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import com.tinywebgears.gaedownload.core.dao.DataPersistenceException;
import com.tinywebgears.gaedownload.core.dao.FileRepository;
import com.tinywebgears.gaedownload.core.dao.UserRepository;
import com.tinywebgears.gaedownload.core.model.MyFile;
import com.tinywebgears.gaedownload.core.model.User;
import com.tinywebgears.gaedownload.servlet.TextFileServlet;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Resource;

public class DefaultUserServices extends AbstractService implements UserServicesIF {

    private final Logger logger = LoggerFactory.getLogger(DefaultUserServices.class);

    private final UserRepository userRepo;

    private final FileRepository fileRepo;

    public DefaultUserServices(UserRepository userRepo, FileRepository fileRepo) {
        super(userRepo);
        this.userRepo = userRepo;
        this.fileRepo = fileRepo;
    }

    @Override
    public User getUserByKey(Key userKey) throws DataPersistenceException {
        return userRepo.getByKey(userKey);
    }

    @Override
    public User setUser(String username, String email, String nickname) throws DataPersistenceException {
        logger.info("setUser - username: " + username + " email: " + email + " nickname: " + nickname);
        if (username == null) user = null; else {
            user = userRepo.getByName(username);
            if (user == null) {
                user = new User(username, email, nickname, null);
                userRepo.persist(user);
            }
        }
        return user;
    }

    @Override
    public User updateUserLoginDate(Key userKey, Date lastLoginDate) throws DataPersistenceException {
        logger.info("updateUserLoginDate - key: " + userKey + " last login date: " + lastLoginDate);
        if (userKey == null) return null; else {
            user = userRepo.getByKey(userKey);
            if (user == null) throw new DataPersistenceException("No user found.");
            user.setLastLoginDate(lastLoginDate);
            userRepo.persist(user);
        }
        return user;
    }

    private void createFile(MyFile file) throws DataPersistenceException {
        file.setOwnerKey(getUserKey());
        fileRepo.persist(file);
    }

    @Override
    public void removeAllFiles() {
    }

    @Override
    public Resource serveTextFile(String filename, byte[] text) throws ServiceException {
        try {
            Blob imageBlob = new Blob(text);
            MyFile myFile = new MyFile(filename, getUsername(), imageBlob);
            createFile(myFile);
            Long id = myFile.getKey();
            return new ExternalResource(TextFileServlet.SERVLET_PATH + "?" + TextFileServlet.PARAM_BLOB_ID + "=" + id);
        } catch (DataPersistenceException e) {
            throw new ServiceException("An error occurred when downloading the file " + filename + ".", e);
        }
    }
}
