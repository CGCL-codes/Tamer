package de.barmenia.dms.cs.interfaces;

/**
 * Local interface for MasterSession.
 */
public interface MasterSessionLocal extends javax.ejb.EJBLocalObject {

    /**
    * Fuer Testzwecke hat es sich bewaehrt in jede Session Bean eine ping-Methode einzufuegen.
    */
    public java.lang.String ping(java.lang.String message);
}
