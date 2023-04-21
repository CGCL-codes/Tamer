package com.genia.toolbox.persistence.jcr.repository;

import java.io.File;
import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.jackrabbit.core.TransientRepository;
import com.genia.toolbox.basics.exception.technical.WrapperException;
import com.genia.toolbox.basics.manager.FileManager;

/**
 * delegate class to {@link TransientRepository} that generates a new temporary
 * repository.
 */
public class CustomTransientRepository implements Repository, Runnable {

    private TransientRepository repository = null;

    private File homeDir;

    private boolean shutdown = false;

    private final String config;

    /**
   * the {@link FileManager} to use.
   */
    private FileManager fileManager;

    public CustomTransientRepository(final String config) {
        this.config = config;
    }

    /**
   * @param key
   * @return
   * @see javax.jcr.Repository#getDescriptor(java.lang.String)
   */
    public String getDescriptor(String key) {
        return getRepository().getDescriptor(key);
    }

    /**
   * @return
   * @see javax.jcr.Repository#getDescriptorKeys()
   */
    public String[] getDescriptorKeys() {
        return getRepository().getDescriptorKeys();
    }

    /**
   * getter for the fileManager property.
   * 
   * @return the fileManager
   */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
   * @return
   * @throws LoginException
   * @throws RepositoryException
   * @see javax.jcr.Repository#login()
   */
    public Session login() throws LoginException, RepositoryException {
        return getRepository().login();
    }

    /**
   * @param credentials
   * @return
   * @throws LoginException
   * @throws RepositoryException
   * @see javax.jcr.Repository#login(javax.jcr.Credentials)
   */
    public Session login(Credentials credentials) throws LoginException, RepositoryException {
        return getRepository().login(credentials);
    }

    /**
   * @param credentials
   * @param workspaceName
   * @return
   * @throws LoginException
   * @throws NoSuchWorkspaceException
   * @throws RepositoryException
   * @see javax.jcr.Repository#login(javax.jcr.Credentials, java.lang.String)
   */
    public Session login(Credentials credentials, String workspaceName) throws LoginException, NoSuchWorkspaceException, RepositoryException {
        return getRepository().login(credentials, workspaceName);
    }

    /**
   * @param workspaceName
   * @return
   * @throws LoginException
   * @throws NoSuchWorkspaceException
   * @throws RepositoryException
   * @see javax.jcr.Repository#login(java.lang.String)
   */
    public Session login(String workspaceName) throws LoginException, NoSuchWorkspaceException, RepositoryException {
        return getRepository().login(workspaceName);
    }

    /**
   * When an object implementing interface <code>Runnable</code> is used to
   * create a thread, starting the thread causes the object's <code>run</code>
   * method to be called in that separately executing thread.
   * <p>
   * The general contract of the method <code>run</code> is that it may take
   * any action whatsoever.
   * 
   * @see java.lang.Thread#run()
   * @see java.lang.Runnable#run()
   */
    public void run() {
        shutdown();
    }

    /**
   * setter for the fileManager property.
   * 
   * @param fileManager
   *          the fileManager to set
   */
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void shutdown() {
        if (!shutdown) {
            try {
                repository.shutdown();
            } finally {
                getFileManager().deleteRecursively(homeDir);
            }
            shutdown = true;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            shutdown();
        } finally {
            super.finalize();
        }
    }

    /**
   * getter for the repository property.
   * 
   * @return the repository
   */
    public TransientRepository getRepository() {
        try {
            if (repository == null) {
                homeDir = getFileManager().createAutoDeletableTempDirectory("jackRabbitRepository", ".dir");
                System.setProperty("derby.stream.error.file", new File(homeDir, "derby.log").getAbsolutePath());
                repository = new TransientRepository(config, homeDir.getAbsolutePath());
                Runtime.getRuntime().addShutdownHook(new Thread(this));
            }
            return repository;
        } catch (Exception e) {
            throw new WrapperException(e);
        }
    }
}
