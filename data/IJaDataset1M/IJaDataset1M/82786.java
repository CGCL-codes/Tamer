package com.mockturtlesolutions.snifflib.reposconfig.database;

import java.util.Vector;
import java.lang.reflect.Constructor;

/**
This is a tagging interface for all classes that store themselves in a
repository.  
*/
public abstract class AbstractRepositoryStorage implements RepositoryStorage {

    protected Vector reposListeners;

    protected String nickname;

    protected RepositoryStorageTransferAgent transferAgent;

    public AbstractRepositoryStorage() {
        this.reposListeners = new Vector();
        this.nickname = "untitled";
        try {
            Class transferAgentClass = this.getStorageTransferAgentClass();
            if (transferAgentClass == null) {
                throw new RuntimeException("Transfer agent class can not be null.");
            }
            Class[] parameterTypes = new Class[] { RepositoryStorage.class };
            Constructor constr = transferAgentClass.getConstructor(parameterTypes);
            Object[] actualValues = new Object[] { this };
            this.transferAgent = (RepositoryStorageTransferAgent) constr.newInstance(actualValues);
        } catch (Exception err) {
            throw new RuntimeException("Unable to instantiate transfer agent.", err);
        }
    }

    public void beforeTransferStorage() {
    }

    public void afterTransferStorage() {
    }

    public void beforeCopyStorage() {
    }

    public void afterCopyStorage() {
    }

    public AbstractRepositoryStorage(String nn) {
        this();
        this.nickname = nn;
    }

    public void show() {
        Class DOMCLASS = this.getDOMStorageClass();
        RepositoryStorageDOM DOM = null;
        try {
            DOM = (RepositoryStorageDOM) DOMCLASS.newInstance();
        } catch (Exception err) {
            throw new RuntimeException("Unable to instantiate RepositoryStorageDOM.", err);
        }
        DOM.transferStorage(this);
        DOM.show();
        System.out.println("\n");
    }

    /**
	This method should be overridden by subclasses wanting to extend the transfer method.
	*/
    public Class getStorageTransferAgentClass() {
        return (RepositoryStorageTransferAgent.class);
    }

    /**
	This method should be overridden by subclasses wanting to extend calculations done
	directly in regard to this storage instance including convenience calculations.
	*/
    public Class getStorageCalculationsAgentClass() {
        return (null);
    }

    public void transferStorageCommands(RepositoryStorage that) {
    }

    public void copyStorageCommands(RepositoryStorage that) {
    }

    public void copyStorage(RepositoryStorage that) {
        this.transferAgent.copyStorage(that);
    }

    public void transferStorage(RepositoryStorage that) {
        this.reposListeners = new Vector();
        this.transferAgent.transferStorage(that);
    }

    public void addRepositoryListener(RepositoryListener lr) {
        this.reposListeners.add(lr);
    }

    public void removeRepositoryListener(RepositoryListener lr) {
        this.reposListeners.remove(lr);
    }

    /**
	Returns a DOM storage variant of this storage instance.  DOM storage classes,
	because they are independent of any repository, are by definition transient
	and often used when transfering amond RepositoryStorage classes which may
	be more persistent (e.g. backed by an XML or an SQL repository).
	*/
    public abstract Class getDOMStorageClass();

    /**
	Gets the state of the (possibly remote) storage.  For example, dis-enabled storages may be slated for
	deletion from some repostories.  Typically a storage is backed by static
	information stored in a database.
	*/
    public abstract String getEnabled();

    /**
	Sets the enabled state of the (possibly remote) storage instance. Typically a storage is backed by static
	information stored in a database.
	
	*/
    public abstract void setEnabled(String n);

    /**
	Gets the project name for this Wedge inverse problem.
	*/
    public abstract String getNickname();

    /**
	Sets the project name for this Wedge inverse problem.
	*/
    public abstract void setNickname(String n);

    public abstract String getCreatedBy();

    public abstract void setCreatedBy(String a);

    public abstract String getCreatedOn();

    public abstract void setCreatedOn(String a);

    /**
	Gets the descriptive comment for this Wedge inverse problem as a whole.
	*/
    public abstract String getComment();

    public abstract void setComment(String s);
}
