package org.proteored.miapeapi.xml.mzidentml;

import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;

public class ProjectImpl implements Project {

    private final String projectName;

    private final User owner;

    private final PersistenceManager dbManager;

    private int id = -1;

    public ProjectImpl(String projectName, User user, PersistenceManager dbManager) {
        this.projectName = projectName;
        this.owner = user;
        this.dbManager = dbManager;
    }

    @Override
    public String getComments() {
        return null;
    }

    @Override
    public MiapeDate getDate() {
        return new MiapeDate(System.currentTimeMillis());
    }

    @Override
    public String getName() {
        return projectName;
    }

    @Override
    public User getOwner() {
        return owner;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int store() throws MiapeDatabaseException, MiapeSecurityException {
        if (this.dbManager != null) {
            id = dbManager.saveProject(this);
        }
        throw new MiapeDatabaseException("The persistance method is not defined.");
    }

    @Override
    public void delete(String userName, String password) throws MiapeDatabaseException, MiapeSecurityException {
        if (this.dbManager != null) {
            dbManager.deleteProjectById(this.getId(), userName, password);
        }
        throw new MiapeDatabaseException("The persistance method is not defined.");
    }
}
