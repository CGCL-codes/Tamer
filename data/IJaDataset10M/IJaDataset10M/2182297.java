package com.mockturtlesolutions.snifflib.reposconfig.database;

import java.util.Vector;

/**
Interface methods which each RepositoryStorage will typically implement in a
different way.  This contrasts for example with the interface 
RepositoryStorageCalculations which each storage will use the same algorithms
and typically rely heavily on the RepositoryStorage interface. 
*/
public interface RepositoryStorage extends RepositoryStorageTransfer, DOMStorable {

    public void addRepositoryListener(RepositoryListener lr);

    public void removeRepositoryListener(RepositoryListener lr);

    public Class getDefaultGraphicalEditorClass();

    /**
	Gets the state of the (possibly remote) storage.  For example, dis-enabled storages may be slated for
	deletion from some repostories.  Typically a storage is backed by static
	information stored in a database.
	*/
    public String getEnabled();

    /**
	Sets the enabled state of the (possibly remote) storage instance. Typically a storage is backed by static
	information stored in a database.
	*/
    public void setEnabled(String n);

    /**
	Gets the project name for this Wedge inverse problem.
	*/
    public String getNickname();

    /**
	Sets the project name for this Wedge inverse problem.
	*/
    public void setNickname(String n);

    public String getCreatedBy();

    public void setCreatedBy(String a);

    public String getCreatedOn();

    public void setCreatedOn(String a);

    /**
	Gets the descriptive comment for this Wedge inverse problem as a whole.
	*/
    public String getComment();

    public void setComment(String s);
}
