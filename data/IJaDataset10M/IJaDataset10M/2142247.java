package net.sf.warpcore.domperignon.common;

/**
 * interface used by RepositoryObjects to register with their session
 */
public interface Registrable extends java.io.Serializable {

    /**
   * sets the session on the repositoryObject
   */
    public void setSession(RepositorySession rs);
}
